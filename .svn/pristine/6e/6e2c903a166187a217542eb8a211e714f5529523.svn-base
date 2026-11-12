package project.common.task;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.common.bean.DataMap;

public class SseEventManager {

    private static final Logger LOG = Logger.getLogger(SseEventManager.class.getName());

    // 토픽별 클라이언트 목록
    // - ConcurrentHashMap : 토픽 단위 동시 접근 안전
    // - CopyOnWriteArraySet : 반복(iteration) 중 add/remove 안전
    private static final Map<String, Set<SseClient>> clients = new ConcurrentHashMap<>();

    // ObjectMapper는 설정 변경이 없으면 thread-safe
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    /**
     * 클라이언트 등록
     * ★ compute로 "Set 생성 + 추가"를 원자적으로 처리
     *   (removeClient가 빈 Set을 지우는 동안 추가되어 유실되는 race 방지)
     */
    public static void addClient(String topic, SseClient client) {
        clients.compute(topic, (k, set) -> {
            Set<SseClient> s = (set != null) ? set : new CopyOnWriteArraySet<>();
            s.add(client);
            return s;
        });
    }

    /**
     * 클라이언트 제거
     * ★ computeIfPresent로 "제거 + 빈 Set 정리"를 원자적으로 처리
     *   (null 반환 시 해당 토픽 키가 Map에서 제거됨)
     */
    public static void removeClient(String topic, SseClient client) {
        clients.computeIfPresent(topic, (k, set) -> {
            set.remove(client);
            return set.isEmpty() ? null : set;
        });
    }

    /**
     * List<DataMap>을 JSON으로 직렬화 후 해당 토픽 구독자에게 SSE 전송
     */
    public static void sendEvent(String topic, String event, List<DataMap> data) {
        Set<SseClient> set = clients.get(topic);
        if (set == null || set.isEmpty()) {
            return;
        }

        // 1) 직렬화는 1회만 수행 (실패 시 전송하지 않음)
        final String frame;
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("time", LocalDateTime.now().format(TIME_FORMAT));
            payload.put("data", data);
            String jsonData = objectMapper.writeValueAsString(payload);
            frame = "event: " + event + "\n" + "data: " + jsonData + "\n\n";
        } catch (Exception e) {
            LOG.log(Level.WARNING, "SSE 이벤트 직렬화 실패 topic=" + topic, e);
            return;
        }

        // 2) 클라이언트별 전송
        //    ★ 실패한 클라이언트는 SseClient.send()가 정리 콜백(Servlet의 cleanup)을 실행
        //      → 목록 제거 + heartbeat 취소 + AsyncContext 종료까지 즉시 처리
        //    ★ 한 클라이언트의 실패가 다른 클라이언트 전송을 막지 않음
        for (SseClient c : set) {
            c.send(frame);
        }
    }

    /**
     * SSE 연결 1개를 표현
     * - 쓰기와 종료를 같은 lock(writer)으로 보호 → 종료 후 (톰캣이 재활용한) writer에 절대 쓰지 않음
     * - 쓰기 실패 시 등록된 onFailure 콜백을 1회 실행
     */
    public static final class SseClient {

        private final PrintWriter writer;
        private final AtomicBoolean closed = new AtomicBoolean(false);
        private volatile Runnable onFailure;

        public SseClient(PrintWriter writer) {
            this.writer = writer;
        }

        /** 쓰기 실패 시 실행할 정리 로직 등록 (Servlet에서 설정) */
        public void setOnFailure(Runnable onFailure) {
            this.onFailure = onFailure;
        }

        /**
         * 프레임 전송
         * @return 성공 시 true, 이미 종료됐거나 실패 시 false
         */
        public boolean send(String frame) {
            boolean failed;
            synchronized (writer) {
                if (closed.get()) {
                    return false;
                }
                try {
                    writer.write(frame);
                    writer.flush();
                    // PrintWriter(CoyoteWriter)는 IOException을 던지지 않고 에러 플래그만 세움
                    failed = writer.checkError();
                } catch (RuntimeException e) {
                    failed = true;
                }
            }
            // lock 밖에서 정리 실행 (close()가 같은 lock을 잡으므로)
            if (failed) {
                Runnable r = onFailure;
                if (r != null) {
                    try {
                        r.run();
                    } catch (RuntimeException e) {
                        LOG.log(Level.FINE, "SSE 클라이언트 정리 중 예외", e);
                    }
                }
            }
            return !failed;
        }

        /**
         * 종료 표시 (진행 중인 쓰기가 끝난 뒤에 종료되도록 같은 lock 사용)
         * @return 최초 종료 호출이면 true
         */
        public boolean close() {
            synchronized (writer) {
                return closed.compareAndSet(false, true);
            }
        }

        public boolean isClosed() {
            return closed.get();
        }
    }
}