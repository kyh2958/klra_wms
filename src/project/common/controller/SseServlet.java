package project.common.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.common.task.SseEventManager;
import project.common.task.SseEventManager.SseClient;

@WebServlet(value = "/api/sse/subscribe", asyncSupported = true)
public class SseServlet extends HttpServlet {

    // 워커 스레드를 점유하지 않는 소규모 전용 heartbeat 스케줄러
    // (daemon 스레드라 WAS 종료 시 함께 종료됨)
    private static final ScheduledExecutorService HEARTBEAT =
            Executors.newScheduledThreadPool(2, r -> {
                Thread t = new Thread(r, "sse-heartbeat");
                t.setDaemon(true);
                return t;
            });

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // topic은 람다/익명 클래스에서 캡처하므로 final 단일 대입
        // ※ 서블릿 인스턴스 필드로 빼면 요청 스레드 간 공유되어 동시성 버그가 됨
        final String topic = req.getParameter("topic") != null
                ? req.getParameter("topic") : "default";

        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Connection", "keep-alive");
        // nginx/WAS 버퍼링 방지 (SSE가 지연되거나 끊기지 않게)
        resp.setHeader("X-Accel-Buffering", "no");

        // 비동기 처리: 요청 스레드를 즉시 풀에 반환 (톰캣 maxThreads 고갈 방지)
        final AsyncContext async = req.startAsync();
        async.setTimeout(0); // 서버 타임아웃 비활성화 (연결 수명은 heartbeat가 관리)

        final PrintWriter writer = resp.getWriter();
        final SseClient client = new SseClient(writer);
        final AtomicReference<ScheduledFuture<?>> hbRef = new AtomicReference<>();

        // ★ 정리 로직 1곳 통합 + 1회만 실행
        //   client.close()가 쓰기와 같은 lock을 사용하므로
        //   이후 heartbeat/broadcast가 종료된 writer에 쓰지 않음
        final Runnable cleanup = () -> {
            if (!client.close()) {
                return; // 이미 정리됨
            }
            ScheduledFuture<?> f = hbRef.get();
            if (f != null) {
                f.cancel(false); // interrupt 하지 않음 (flush 중인 NIO 채널 보호)
            }
            SseEventManager.removeClient(topic, client);
            try {
                async.complete();
            } catch (IllegalStateException ignore) {
                // 이미 완료된 AsyncContext
            }
        };

        // 쓰기 실패(heartbeat/broadcast 어느 쪽이든) 시 즉시 정리
        client.setOnFailure(cleanup);

        // ★ 리스너를 먼저 등록 (이후 어느 시점에 끊겨도 정리되도록)
        async.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent e) {
                cleanup.run();
            }

            @Override
            public void onTimeout(AsyncEvent e) {
                cleanup.run();
            }

            @Override
            public void onError(AsyncEvent e) {
                // complete() 호출로 에러 페이지 dispatch 방지
                cleanup.run();
            }

            @Override
            public void onStartAsync(AsyncEvent e) {
                // 사용하지 않음
            }
        });

        // 최초 연결 이벤트 (+ 재연결 주기 안내) → 성공한 경우에만 구독 등록
        if (!client.send("retry: 5000\n"
                + "event: connected\n"
                + "data: SSE Connected (" + topic + ")\n\n")) {
            return; // 이미 끊김 → send()가 cleanup 실행함
        }
        SseEventManager.addClient(topic, client);
        if (client.isClosed()) {
            // 등록 직전에 종료된 경우(경쟁 조건) 목록에서 제거
            SseEventManager.removeClient(topic, client);
            return;
        }

        // heartbeat(주석 프레임)로 연결 유지 + 단절 감지
        // - ": ping" 주석 줄은 EventSource가 무시하지만 프록시 유휴 타임아웃을 막아줌
        // - 실패 시 client.send()가 cleanup 실행
        hbRef.set(HEARTBEAT.scheduleAtFixedRate(() -> {
            try {
                client.send(": ping\n\n");
            } catch (Throwable t) {
                // 예외가 밖으로 새면 ScheduledExecutor가 이후 실행을 조용히 중단하므로 Throwable까지 처리
                cleanup.run();
            }
        }, 15, 15, TimeUnit.SECONDS));

        // heartbeat 등록 직전에 이미 종료된 경우(경쟁 조건) 스케줄 취소
        if (client.isClosed()) {
            hbRef.get().cancel(false);
        }
    }
}