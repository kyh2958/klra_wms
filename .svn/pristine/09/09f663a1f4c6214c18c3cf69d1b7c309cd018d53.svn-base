<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<%
    User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%>
<%@ page import="project.common.airfw.*"%>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<style>
    input[type="checkbox"]{height:unset;} 
</style>
<script type="text/javascript">
	var dblIdx = 0;
	var subIdx = 0;
	var searchType = 0;
	var paramH = new DataMap();
	var menuky = configData.MENU_ID;
	
	$(document).ready(function(){
	    //setTopSize(300);
	    
	    gridList.setGrid({
	        id : "gridList",
	        module : "WmsOutbound",
	        editable : true,
	        command : "DL021POP",
	    });
	    
		searchList();
	    
	});
	
	function searchList(){

		//오라클 버전으로 있음
		var data = page.getLinkPopData();	
		
		var param = new DataMap();
		param.put("data", data);	
			
		
		 gridList.resetGrid("gridList");
			
		 gridList.gridList({
	            id : "gridList",
	            url : "/wms/outbound/json/DL21POP.data",
	            param : param 
	     });
		
	}	
	
	function fn_closing(){
		this.close();
	}
	
	/* 
	function commonBtnClick(btnName){
		if(btnName == "Delete"){
			TotalPickingDelete();
		} else if(btnName == "printShpping"){
			printShpping();
		} else if(btnName == "printPicking"){
			TotalPickingPrint();
		} else if(btnName == "Search"){
			searchList()
		}
		
	} */
</script>
</head>
<body>
<%@ include file="/common/include/webdek/layout.jsp" %>
<!-- content -->
<div class="content_wrap">
	<div class="content_inner">
		<%@ include file="/common/include/webdek/title.jsp" %>	
		<div class="content_serch" id="searchArea">
			<div class="btn_wrap">
				<div class="fl_l">								
				</div>				
				<div class="fl_r">												
				</div>			        
			</div>				
		</div>	
		<div class="search_next_wrap" style="height: 95%">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
					<li><a href="#tab1-1"><span CL="STD_STKNUMLIST">선적번호 관리</span></a></li>
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
					 	<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
										<tr CGRow="true">
												<td GH="40" GCol="rownum">1</td>											
												<td GH="100 STD_SHPOKY" GCol="text,SHPOKY"></td>																								
												<td GH="80 STD_STKNUM" GCol="text,STKNUM"></td>
												<td GH="100 STD_STLNUM" GCol="text,STLNUM"></td>
												<td GH="100 STD_VEHINO" GCol="text,CARNO">차량번호</td>
                                        		<td GH="100 STD_WADT01" GCol="text,DRIVERCEL">연락처</td>
											</tr>
										</tbody>
									</table>
									</div>
							</div>
							<div class="btn_lit tableUtil">
									<button type="button" GBtn="find"></button>
									<button type="button" GBtn="sortReset"></button>
									<button type="button" GBtn="layout"></button>
									<button type="button" GBtn="total"></button>
									<button type="button" GBtn="excel"></button>
									<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
							</div>
						</div>
					  </div>	
					</div>
					
				<!-- //contentContainer -->
	</div>		
</div>
<!-- // content -->
<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>