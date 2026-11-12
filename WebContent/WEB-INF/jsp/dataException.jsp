<%@ page language="java"
    contentType="text/plain; charset=UTF-8"
    pageEncoding="UTF-8"
    import="
        org.apache.logging.log4j.LogManager,
        org.apache.logging.log4j.Logger,
        java.util.UUID,
        java.util.Enumeration,
        java.util.Set,
        java.util.HashSet,
        java.util.Arrays,
        javax.servlet.RequestDispatcher"
%>

<%!
private static final Logger log = LogManager.getLogger("DataExceptionJSP");

/**
 * Root Cause 조회
 */
private Throwable getRootCause(Throwable t) {
    if (t == null) {
        return null;
    }

    Throwable cur = t;
    int guard = 0;

    while (cur.getCause() != null && cur.getCause() != cur && guard++ < 50) {
        cur = cur.getCause();
    }

    return cur;
}

private boolean isBlank(String s) {
    return s == null || s.trim().length() == 0;
}

private String safeString(Object obj) {
    if (obj == null) {
        return "";
    }

    try {
        return String.valueOf(obj);
    } catch (Throwable t) {
        return "[[STRING_CONVERSION_FAILED:" + t.getClass().getName() + "]]";
    }
}

private boolean isSensitiveKey(String key) {
    if (key == null) {
        return false;
    }

    String k = key.toLowerCase();

    return k.contains("password")
        || k.contains("passwd")
        || k.contains("pwd")
        || k.contains("token")
        || k.contains("authorization")
        || k.contains("secret")
        || k.contains("credential")
        || k.contains("j_password")
        || k.contains("loginpw")
        || k.contains("userpw");
}

private String joinParameterValues(String[] values) {
    if (values == null) {
        return "";
    }

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < values.length; i++) {
        if (i > 0) {
            sb.append(", ");
        }
        sb.append(values[i]);
    }

    return sb.toString();
}

private String normalizeClientMessage(String msg) {
    if (msg == null) {
        return "SYSTEM_ERROR";
    }

    String s = msg.replace('\r', ' ').replace('\n', ' ').trim();

    if (s.length() == 0) {
        return "SYSTEM_ERROR";
    }

    if (s.length() > 500) {
        s = s.substring(0, 500);
    }

    return s;
}

private String buildClientErrorMessage(Throwable ex, Throwable root, String errorId) {
    String msg = null;

    if (root instanceof StackOverflowError || ex instanceof StackOverflowError) {
        msg = "StackOverflowError";
    } else if (root != null && !isBlank(root.getMessage())) {
        msg = root.getMessage();
    } else if (ex != null && !isBlank(ex.getMessage())) {
        msg = ex.getMessage();
    } else {
        msg = "SYSTEM_ERROR";
    }

    msg = normalizeClientMessage(msg);

    // 필요하면 아래처럼 ERROR_ID를 같이 붙여도 됨
    // return msg + " [ERROR_ID:" + errorId + "]";
    return msg;
}
%>

<%
/*************************************************
 * Request 정보 사전 조회
 *************************************************/
Object statusCodeObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
Object originalUriObj = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
Object servletNameObj = request.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME);

String currentUri = safeString(request.getRequestURI());
String originalUri = safeString(originalUriObj);
String method = safeString(request.getMethod());
String remoteAddr = safeString(request.getRemoteAddr());
String queryString = safeString(request.getQueryString());
String dispatchType = safeString(request.getDispatcherType());
String servletName = safeString(servletNameObj);

/*************************************************
 * Error Page 재진입 방지
 *************************************************/
if (Boolean.TRUE.equals(request.getAttribute("__DATA_EXCEPTION_PAGE_VISITED__"))) {
    log.error("==============================================================");
    log.error("DATA EXCEPTION PAGE RE-ENTRY DETECTED");
    log.error("THREAD          : {}", Thread.currentThread().getName());
    log.error("DISPATCH TYPE   : {}", dispatchType);
    log.error("METHOD          : {}", method);
    log.error("CURRENT URI     : {}", currentUri);
    log.error("ORIGINAL URI    : {}", originalUri);
    log.error("REMOTE ADDR     : {}", remoteAddr);
    log.error("==============================================================");

    response.setStatus(500);
    out.print("SYSTEM_ERROR");
    return;
}

request.setAttribute("__DATA_EXCEPTION_PAGE_VISITED__", Boolean.TRUE);

/*************************************************
 * Skip URI
 *************************************************/
Set<String> skipUriSet = new HashSet<String>(Arrays.asList(
    "/wms/inventory/json/MV01CreatTaskOrder.data"
));

if (skipUriSet.contains(originalUri)) {
    response.setStatus(500);
    out.print("SYSTEM_ERROR");
    return;
}

/*************************************************
 * Error ID
 *************************************************/
String errorId = UUID.randomUUID().toString();

/*************************************************
 * Exception
 *************************************************/
Throwable ex = (Throwable) request.getAttribute("exceptionMsg");

if (ex == null) {
    ex = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
}

Throwable root = getRootCause(ex);

/*************************************************
 * 로그 출력
 *************************************************/
log.error("");
log.error("==============================================================");
log.error("                  DATA EXCEPTION OCCURRED");
log.error("==============================================================");

log.error("ERROR ID        : {}", errorId);
log.error("THREAD          : {}", Thread.currentThread().getName());

log.error("DISPATCH TYPE   : {}", dispatchType);
log.error("METHOD          : {}", method);

log.error("CURRENT URI     : {}", currentUri);
log.error("ORIGINAL URI    : {}", originalUri);

log.error("QUERY STRING    : {}", queryString);
log.error("REMOTE ADDR     : {}", remoteAddr);

log.error("STATUS CODE     : {}", safeString(statusCodeObj));
log.error("SERVLET NAME    : {}", servletName);

/*************************************************
 * Parameter 출력
 *************************************************/
Enumeration<String> names = request.getParameterNames();

while (names != null && names.hasMoreElements()) {
    String name = null;

    try {
        name = names.nextElement();

        if (isSensitiveKey(name)) {
            log.error("PARAM [{}] = ******", name);
        } else {
            String[] values = request.getParameterValues(name);
            log.error("PARAM [{}] = {}", name, joinParameterValues(values));
        }
    } catch (Throwable t) {
        log.error("PARAM LOGGING FAILED [{}] : {} / {}",
            safeString(name),
            t.getClass().getName(),
            safeString(t.getMessage()));
    }
}

/*************************************************
 * Exception 정보
 *************************************************/
if (ex != null) {
    log.error("----------------------------------------------");
    log.error("TOP ERROR TYPE : {}", ex.getClass().getName());
    log.error("TOP ERROR MSG  : {}", safeString(ex.getMessage()));
} else {
    log.error("----------------------------------------------");
    log.error("TOP ERROR : NULL");
}

if (root != null) {
    log.error("----------------------------------------------");
    log.error("ROOT ERROR TYPE : {}", root.getClass().getName());
    log.error("ROOT ERROR MSG  : {}", safeString(root.getMessage()));
}

/*************************************************
 * StackOverflowError 전용 처리
 *************************************************/
boolean stackOverflowCase = (root instanceof StackOverflowError) || (ex instanceof StackOverflowError);

if (stackOverflowCase) {
    Throwable target = (root instanceof StackOverflowError) ? root : ex;
    StackTraceElement[] arr = target.getStackTrace();

    log.error("----------------------------------------------");
    log.error("STACKOVERFLOW DETECTED - FIRST 30 FRAMES ONLY");

    for (int i = 0; i < Math.min(arr.length, 30); i++) {
        log.error("[{}] {}", i, arr[i]);
    }
} else if (ex != null) {
    /*************************************************
     * 일반 예외만 전체 StackTrace 출력
     *************************************************/
    log.error("----------------------------------------------");
    log.error("FULL STACK TRACE", ex);
}

log.error("==============================================================");

/*************************************************
 * Status 보정
 *************************************************/
int sc = 500;

try {
    if (statusCodeObj != null) {
        sc = Integer.parseInt(statusCodeObj.toString());
    }
} catch (Throwable t) {
    sc = 500;
}

if (sc < 400) {
    sc = 500;
}

response.setStatus(sc);

/*************************************************
 * Client Response
 * 상세 내부 오류는 로그에만 남기고
 * 클라이언트에는 오류 메시지 반환
 *************************************************/
String clientMsg = buildClientErrorMessage(ex, root, errorId);
out.print(clientMsg);
%>
