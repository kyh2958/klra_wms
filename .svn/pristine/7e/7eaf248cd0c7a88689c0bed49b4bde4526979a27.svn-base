<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
    String process = String.valueOf(request.getAttribute("sso_process"));
    String ssoDomain = String.valueOf(request.getAttribute("sso_domain"));
    String recallURL = String.valueOf(request.getAttribute("recall_url"));
    String entityId = String.valueOf(request.getAttribute("entityId"));
    
%>
<body onload="document.forms[0].submit()">
	<form action="<%=ssoDomain%><%=process%>" method="post">
	    <div>
	        <!-- 시스템별 식별 entityId -->
	        <input type="hidden" name=entityId value="<%=entityId%>"/>
			<!-- 시스템별 통합로그인 후 이동되어야할 페이지 주소 -->
	        <input type="hidden" name="relayState" value="<%=recallURL%>"/>
	    </div>
	    <noscript>
	        <div>
	            <input type="submit" value="Continue"/>
	        </div>
	    </noscript>
	</form>
</body>
</html>