<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="project.common.bean.DataMap,java.util.*,project.common.bean.CommonConfig"%>
<%
	//DataMap label = (DataMap)request.getAttribute("label");
	//DataMap message = (DataMap)request.getAttribute("message");
	String langky = request.getSession().getAttribute(CommonConfig.SES_USER_LANGUAGE_KEY).toString();
%>
<!doctype html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>Mobile WMS</title>
<link rel="stylesheet" type="text/css" href="/mobile/css/mobile.css" />
<script type="text/javascript" src="/common/js/jquery.js"></script>
<script type="text/javascript" src="/common/js/json2.js"></script>
<script type="text/javascript" src="/common/js/dataMap.js"></script>
<script type="text/javascript" src="/common/js/configData.js"></script>
<script type="text/javascript" src="/common/js/commonUtil.js"></script>
<script type="text/javascript" src="/common/js/netUtil.js"></script>
<script type="text/javascript">
configData.isMobile=true;

function logout(){
	location.href = "/mobile/json/logout.page";
}
</script>
<script type="text/javascript">
	var menuOpenState = false;
	var $mainFrame;
	var $topFrame;
	var $bodyFrame;
	var wcsProcessing = false;
	var wcsBarcods = '';
	
	$(document).ready(function(){
		$mainFrame = $("#mainFrame");
		$topFrame = $("#topFrame");
		$bodyFrame = $("#bodyFrame");
		
		menuOpen();
	});
	function menuOpen(){
		if(menuOpenState){
			$mainFrame.attr("rows","47px,*");
			menuOpenState = false;
		}else{
			$mainFrame.attr("rows","*,0px");
			menuOpenState = true;
		}
		return menuOpenState;
	}
	function openPage(menuId){
		var url = "/mobile/"+menuId+".page?isMobile=" + configData.isMobile;

		if ($bodyFrame.attr('src') != url) {
			wcsCancel();
		}
		
		$bodyFrame.attr("src",url);
		menuOpen();
	}
	
	function closePage(){
		
		wcsCancel();
		
		$bodyFrame.attr("src", "/mobile/info.page?isMobile=" + configData.isMobile);
		window.top.header.menuOpen(true);
	}
	
	function menuHeightReturn(){
		var topH = $topFrame.height();
		return topH;
	}
	
	function toggleFullscreen(elem) {
	    elem = elem || document.documentElement;
	    if (!document.fullscreenElement && !document.mozFullScreenElement &&
	      !document.webkitFullscreenElement && !document.msFullscreenElement) {
	      if (elem.requestFullscreen) {
	        elem.requestFullscreen();
	      } else if (elem.msRequestFullscreen) {
	        elem.msRequestFullscreen();
	      } else if (elem.mozRequestFullScreen) {
	        elem.mozRequestFullScreen();
	      } else if (elem.webkitRequestFullscreen) {
	        elem.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
	      }
	    } else {
	      if (document.exitFullscreen) {
	        document.exitFullscreen();
	      } else if (document.msExitFullscreen) {
	        document.msExitFullscreen();
	      } else if (document.mozCancelFullScreen) {
	        document.mozCancelFullScreen();
	      } else if (document.webkitExitFullscreen) {
	        document.webkitExitFullscreen();
	      }
	    }
	  }
	
	// WCS 취소요청 
	function wcsCancel() {
		if (wcsProcessing && wcsBarcods != "") {
			
			var wcsParam = new DataMap();
			wcsParam.put("ORD_TYPE", 'CANCEL');	// 요청유형 ('LOCK', 'MOVEOUT', 'CANCEL')
			wcsParam.put("IND_BCD",  wcsBarcods);	// 바코드목록
			
		    var result = netUtil.sendData({
		    	url : "/WcsIF/map/json/CALL_UPDATE_TB_PLT_STOCK.data", 
		    	param : wcsParam
			});
			
		    wcsProcessing = false;
		    wcsBarcods = '';
		}
	}
</script>
</head>
<frameset id="mainFrame" name="topFrame" rows="47px,*" noresize framespacing='0' frameborder='no' border='0' >
    <frame id="topFrame" name="header" src="/mobile/top.data?isMobile=true" noresize framespacing='0' frameborder='no' border='0' scrolling="no">
    <frame id="bodyFrame" name="main" src="/mobile/info.page?isMobile=true" noresize framespacing='0' frameborder='no' border='0' >
</frameset>