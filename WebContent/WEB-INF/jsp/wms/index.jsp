<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="project.common.bean.DataMap,java.util.ArrayList,project.common.bean.CommonConfig,project.common.util.RSA"%>
<%@ page import="project.common.util.*"%>
<%
ArrayList list = (ArrayList)request.getAttribute("WAHMACOMBO");
ArrayList list1 = (ArrayList)request.getAttribute("LANGKY");
ArrayList list2 = (ArrayList)request.getAttribute("MSG");

String serverIp = request.getLocalAddr();
String serverNo = serverIp.substring(serverIp.lastIndexOf(".") + 1);

if (serverIp != null && serverIp.contains(".")) {
    serverNo = serverIp.substring(serverIp.lastIndexOf(".") + 1);
} else if ("0:0:0:0:0:0:0:1".equals(serverIp) || "::1".equals(serverIp)) {
    serverNo = "LOCAL";
}

DataMap row;

Object SES_USER_ID = request.getSession().getAttribute(CommonConfig.SES_USER_ID_KEY);
String langky = (String)request.getSession().getAttribute(CommonConfig.SES_USER_LANGUAGE_KEY);
if(langky == null || langky.equals("")){
    langky = "KR";
}

RSA rsa = RSA.getEncKey();
request.setAttribute("publicKeyModulus", rsa.getPublicKeyModulus());
request.setAttribute("publicKeyExponent", rsa.getPublicKeyExponent());
request.getSession().setAttribute("__rsaPrivateKey__", rsa.getPrivateKey());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>WMS</title>
	<link rel="shortcut icon" href="/common/theme/webdek/images/logo01.png" type="image/x-ico" />
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript" src="/common/theme/webdek/js/login/jquery-1.11.3.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/login/layout.css" />
    <link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/login/sub.css" />
    
	<script type="text/javascript" src="/common/js/jquery.js"></script>
	<script type="text/javascript" src="/common/js/jquery-ui.js"></script>
	<script type="text/javascript" src="/common/js/json2.js"></script>
	<script type="text/javascript" src="/common/js/jquery.cookie.js"></script>
	<script type="text/javascript" src="/common/js/dataMap.js"></script>
	<script type="text/javascript" src="/common/js/configData.js"></script>
	<script type="text/javascript" src="/common/js/site.js"></script>
	<script type="text/javascript" src="/common/js/commonUtil.js"></script>
	<script type="text/javascript" src="/common/js/dataBind.js"></script>
	<script type="text/javascript" src="/common/js/input.js"></script>
	<script type="text/javascript" src="/common/js/netUtil.js"></script>
	<script type="text/javascript" src="/common/js/ui.js"></script>
	<script type="text/javascript" src="/common/js/worker-ajax.js"></script>
	<script type="text/javascript" src="/common/js/grid.js"></script>
	<script type="text/javascript" src="/common/js/validateUtil.js"></script>
	<script type="text/javascript" src="/common/theme/webdek/js/theme.js"></script>
	<script type="text/javascript" src="/common/js/page.js"></script>
	<script type="text/javascript" src="/common/js/label.js"></script>
	
	<script type="text/javascript" src="/wms/js/jsbn.js"></script>
	<script type="text/javascript" src="/wms/js/rsa.js"></script>
	<script type="text/javascript" src="/wms/js/prng4.js"></script>
	<script type="text/javascript" src="/wms/js/rng.js"></script>

<script type="text/javascript">
    <%
    if(SES_USER_ID != null){
    %>
        this.location.href = "/main.page";
    <%
    }
    %>
	<%
	for(int i=0;i<list2.size();i++){
	    row = (DataMap)list2.get(i);
	%>
	commonMessage.message.put("<%=row.get("MESGKY")%>","<%=row.get("MESGTX")%>");
	<%
	}
	%>
	function getMsg(msgCode){
	    msgCode = $("#LANGKY").val()+"_"+msgCode;
	    return commonMessage.message.get(msgCode);
	}
	//로딩 열기
	function loadingOpen() {
	
	    var loader = $('<div class="contentLoading"></div>').appendTo('body');
	
	    loader.stop().animate({
	        top : '0px'
	    }, 30, function() {
	    });
	}
	
	// 로딩 닫기
	function loadingClose() {
	
	    var loader = $('.contentLoading');
	
	    loader.stop().animate({
	        top : '100%'
	    }, 30, function() {
	        loader.remove();
	    });
	}
	
	$(document).ready(function(){
	    
	    var userInputId = $.cookie("userInputId");
	    var setCookieYN = $.cookie("setCookieYN");
	    
	    if(setCookieYN == 'Y') {
	       $("#save_id").prop("checked", true);
	    } else {
	       $("#save_id").prop("checked", false);
	    }
	    
	    $("#COMPID").val("AIRFW");
	    
	    var COLOR = $.cookie("COLOR");
	    if(COLOR){
	        //jQuery("#LANGKY").val(LANGKY);
	        $("input[value="+COLOR+"]").attr("checked","checked");
	    }
	    
	    var LANGKY = $.cookie("LANGKY");
	    if(LANGKY){
	        $("input[value="+LANGKY+"]").attr("checked","checked");
	    }
	    
	    if(userInputId){
	        jQuery("#USERID").val(userInputId);
	    }
	
	    var WAREKY = $.cookie("WAREKY");
	    if(WAREKY){
	        jQuery("#WAREKY").val(WAREKY);
	    }
	});
	
	function login(){
	    //this.location.href = "/common/main.page";
	    if(validate.check("searchArea")){
	    	
	        var langky = $("#LANGKY").val();
	        if($("#save_id").is(":checked")){ 
	            var userInputId = $("#USERID").val();
	            setCookie("userInputId", userInputId, 60); 
	            setCookie("setCookieYN", "Y", 60);
	        } else {
	            deleteCookie("userInputId");
	            deleteCookie("setCookieYN");
	        }
	        
	        var param = dataBind.paramData("searchArea");
	        param.put("WAREKY", $("#WAREKY").val());
	        param.put("USERID", $("#USERID").val());
	        param.put("LANGKY", langky);
	        
	        var rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
	        var rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;
	        var encPassword = fnRsaEnc($('input[name=PASSWD]').val(), rsaPublicKeyModulus, rsaPublicKeyExponent);
	        param.put("PASSWD", encPassword);
	        param.put("PASSWD2", $('input[name=PASSWD]').val());
	        
	        var json = netUtil.sendData({
	            url : "/wms/json/login.data",
	            param : param
	        });
	        
	        var UserId = $('input[name=USERID]').val();
	        if(!UserId){
	        	 commonUtil.msg("아이디를 입력해주세요.");
	        	 $('input[name=USERID]').focus();
	             return;
	        }
	        
	        var Password = $('input[name=PASSWD]').val();
	        if(!Password){
	        	 commonUtil.msg("비밀번호를 입력해주세요.");
	        	 $('input[name=PASSWD]').focus();
	             return;
	        }
	        
	        if(json && json.data){

	            if(json.data == "N"){
	                commonUtil.msg("접속 정보가 다릅니다.\n다시 로그인 해주세요.");
	                return;
	            } else if(json.data == "N_NOT_EXIST_PLANT"){
	                commonUtil.msg("품목군별 플랜트 정보가 존재하지 않습니다.");
	                return;
	            } else if(json.data == "P"){
	                commonUtil.msg("암호화 적용에 실패하였습니다.");
	                location.reload();
	                return;
	            } else if(json.data == "F"){
	                if(json.cnt < 5){
	                    commonUtil.msg("로그인 접속 오류 ( "+json.cnt+"/5 )");
	                }else{
	                    commonUtil.msg("로그인이 제한됩니다.");
	                }
	                return;
	            }
	            else if(json.data == "F_LOGIN_DEL"){
	                commonUtil.msgBox(getMsg("M0012"));
	                return;
	            }
	            else if(json.data == "F_LOGIN_PW_90_OVER"){
	                commonUtil.msgBox(getMsg("M0014"));
	                page.linkPopOpen("/wms/password01.page", param);
	                return;
	            }
	            else if(json.data == "F_LOGIN_90_OVER"){
	                commonUtil.msgBox(getMsg("M0015"));
	                return;
	            }
	            else if(json.data == "F_LOGIN_ROLE"){
	                commonUtil.msgBox(getMsg("M0010"));
	                return;
	            }
	            else{
	                for(var prop in param.map){
	                    $.cookie(prop, param.get(prop),{expires : 7});
	                }
	                if(json && json.data){
	                    this.location.href = "/main.page";
	                }
	            }
	        }
	    }   
	}
	
	function fnRsaEnc(value, rsaPublicKeyModulus, rsaPpublicKeyExponent) {
	    var rsa = new RSAKey();
	    rsa.setPublic(rsaPublicKeyModulus, rsaPpublicKeyExponent);
	    var encValue = rsa.encrypt(value); 
	    return encValue;
	}
	
	function validationEventMsg(valObjType, objIndex, objId, objName, objValue, valType){
	    //commonUtil.debugMsg("validationEventMsg : ", arguments);
	    if(objName == "USERID"){
	        return "ID required.";
	    }else if(objName == "PASSWD"){
	        return "Password required.";
	    }else if(objName == "LANGKY"){
	        return "Language required.";
	    }
	}
	
	function checkEZPlatformApp(){
	   if(typeof window.localStorage == 'undefined') {
	      //alert("로컬 스토리지를 지원하지 않습니다."); //LOGIN_M0006
	      //alert(getMsg("SYSTEM_NOLOCALSTORAGE"));
	      commonUtil.msgBox("SYSTEM_NOLOCALSTORAGE");
	      return;
	   }
	   var version = window.localStorage.getItem('EZPlatformApp');
	   if( version != "1.0.0.11" ){
	       window.open('/downloadPlatformApp.html','egzen','width=500,height=300');
	   }
	}
	
	function changePassword(){
	    //if(validate.check("searchArea")){
	        var param = dataBind.paramData("searchArea");
	        page.linkPopOpen("/wms/password01.page", param);
	    //}   
	}
	
	function setCookie(cookieName, value, exdays){
	    var exdate = new Date();
	    exdate.setDate(exdate.getDate() + exdays);
	    var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + 
	    exdate.toGMTString());
	    document.cookie = cookieName + "=" + cookieValue;
	}
	
	function deleteCookie(cookieName){
	    var expireDate = new Date();
	    expireDate.setDate(expireDate.getDate() - 1);
	    document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
	}
	
	function getCookie(cookie_name) {
	    var x, y;
	    var val = document.cookie.split(';');
	    
	    for (var i = 0; i < val.length; i++) {
	        x = val[i].substr(0, val[i].indexOf('='));
	        y = val[i].substr(val[i].indexOf('=') + 1);
	        x = x.replace(/^\s+|\s+$/g, ''); // 앞과 뒤의 공백 제거하기
	        
	        if (x == cookie_name) {
	          return unescape(y); // unescape로 디코딩 후 값 리턴
	        }
	    }
	}
	
	function idSave(){
	    if($("#save_id").is(":checked")){ 
	        $("#save_id").prop("checked", true);
	    }else{
	        $("#save_id").prop("checked", false);
	    }
	}
</script>
<body>
    <div id="A_Wrap">
        <div id="A_Container">
            <div class="login_form">
	            <input type="hidden" id="rsaPublicKeyModulus" value="${publicKeyModulus}">
	            <input type="hidden" id="rsaPublicKeyExponent" value="${publicKeyExponent}">
                <div class="inner">
                    <h1 class="logo" onclick="location.href='javascript:;'">logo <img src="/common/theme/webdek/images/login/logo.png" alt="logo"> </h1>
                    <div class="logint_layout">
                        <div class="left">
                            <img src="/common/theme/webdek/images/login/login_img01.png" />
                        </div>
                        <div class="center">
                            <form class="" action="index.html" method="post">
                                <h3 class="nanumgothic title">LOGIN (<%=serverNo%>)</h3>
                                <ul class="login_list">
                                    <li>
                                        <select class="hyundai" id="LANGKY" name="LANGKY">
                                        <%
					                        for(int i=0;i<list1.size();i++){
					                            row = (DataMap)list1.get(i);
					                    %>
					                        <option value='<%=row.get("VALUE_COL")%>' <%="KR".equals(row.get("VALUE_COL")) ? "selected" : ""%>>[<%=row.get("VALUE_COL")%>]<%=row.get("TEXT_COL")%></option>
					                    <%
					                        }
					                    %>
                                        </select>
                                    </li>
                                    <li>
                                        <select class="hyundai" id="WAREKY" name="WAREKY">
                                            <%
						                        for(int i=0;i<list.size();i++){
						                            row = (DataMap)list.get(i);
						                    %>
						                        <option value='<%=row.get("VALUE_COL")%>'>[<%=row.get("VALUE_COL")%>]<%=row.get("TEXT_COL")%></option>
						                    <%
						                        }
						                    %>
                                        </select>
                                    </li>
                                    <li class="user">
                                        <input type="text" id="USERID" name="USERID" value="" onkeypress="commonUtil.enterKeyCheck(event, 'login()')">
                                    </li>
                                    <li class="pass">
                                        <input type="password" id="PASSWD" name="PASSWD" value="" onkeypress="commonUtil.enterKeyCheck(event, 'login()')">
                                    </li>
                                </ul>
                                <div class="chk_box">
                                    <input type="checkbox" id="save_id" onclick="idSave()">
                                    <label for="save_id"><span></span>
                                        <p>아이디 저장</p>
                                    </label>
                                </div>
                                <button type="button" name="button" class="login_btn hyundai" onclick="login();">로그인</button>
                                <ul class="searchList">
                                    <li>
                                        <a href="/downloadPlatformApp.html">Install Report Tool</a>
                                    </li>
                                    <li>
                                        <a href="javascript:changePassword();">비밀번호 변경</a>
                                    </li>
                                </ul>
                                <p class="customer"><strong class="nanumgothic">고객 센터 0000-0000</strong> (평일 09:00-18:00)</p>
                            </form>
                        </div>
                        <div class="right">
                            <ul>
                                <li>
                                    <div class="img_box">
                                        <img src="/common/theme/webdek/images/login/login_img02.png" />
                                    </div>
                                </li>
                                <li>
                                    <div class="img_box">
                                        <img src="/common/theme/webdek/images/login/login_img03.png" />
                                    </div>
                                </li>
                                <li>
                                    <div class="img_box">
                                        <img src="/common/theme/webdek/images/login/login_img04.png" />
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <p class="hyundai copy">Copyrightⓒ 2022 KleanNara, All Rights Reserved. </p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
