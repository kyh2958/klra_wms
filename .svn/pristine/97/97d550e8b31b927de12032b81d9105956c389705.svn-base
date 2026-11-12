<%@page import="project.common.airfw.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<%
	User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">


<script type="text/javascript">
	var searchFlag = 1;
	var tabPage = 1;
	var paramH = new DataMap();
	
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			module : "WmsOutbound",
			command : "DL22",
		});	
	});

   function commonBtnClick(btnName){
	      if(btnName == "Search"){
	         if(checkListCount()){
	            searchList();
	         }
	      }
	   }
   
   function checkListCount(){
	      //var param = new DataMap();
	//      param.put("DOCDAT_FR", inputList.setRangeParam().map.RANGE_DATA_PARAM.map.DOCDAT_Range.substr(2,8));
	//      param.put("DOCDAT_TO", inputList.setRangeParam().map.RANGE_DATA_PARAM.map.DOCDAT_Range.substr(11,8));
	  	  var param = inputList.setRangeParam("searchArea");
	         param.put("LANGKY", "<%=langky%>");
	         param.put("WAREKY", "<%=wareky%>"); 
	         param.put("OWNRKY", "<%=ownrky%>");
	         
	         param.put("PROGID", configData.MENU_ID);    
	      var json = netUtil.sendData({
	         module : "WmsOutbound",
	         command : "DL22_LIST_COUNT",
	         sendType : "map",
	         param : param
	      });
	      
	      if (json && json.data){
	         if(json.data["CNT"] > 30000){
	            commonUtil.msgBox("30000건 이상일 경우에는 조회되지 않습니다.");//30000 이상 어쩌구 저쩌구
	            return false;
	         }
	         return true;
	      }
	}
   
   function searchList(){
	      if(validate.check("searchArea")){
	         
	         var param = inputList.setRangeParam("searchArea");
	         param.put("LANGKY", "<%=langky%>");
	         param.put("WAREKY", "<%=wareky%>"); 
	         param.put("OWNRKY", "<%=ownrky%>");
	         
	         param.put("PROGID", configData.MENU_ID);
	         
	         gridList.resetGrid("gridList");
	         
	            gridList.gridList({
	               id : "gridList",
	               param : param
	            });  
	      }
	      
	   }

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
					<input type="button" CB="Search SEARCH BTN_SEARCH" />
					<!-- <input type="button" CB="Print PRINT BTN_IBLBPRINT" />
					<input type="button" CB="IPrint PRINT BTN_INLBPRINT" /> -->
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<input type="hidden" class="input" name="OWNRKY" value="<%=ownrky %>" />
					<dl>
                        <dt CL="STD_LSHPCD">출고일자</dt>
                        <dd>
                            <input type="text" class="input" name="DOCDAT" UIFormat="C -7 7" UIInput="R"/>
                        </dd>
                    </dl>
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
			</div>
		</div>
		<div class="search_next_wrap">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
<!-- 					<li><a href="#tab1-1" onclick="tabChg(1);"><span CL="STD_BYSKUKEY">탭메뉴1</span></a></li> -->
<!-- 					<li><a href="#tab1-2" onclick="tabChg(2);"><span CL="STD_BYAREAKY">탭메뉴2</span></a></li> -->
<!-- 					<li><a href="#tab1-3" onclick="tabChg(3);"><span CL="STD_BYLOCAKY">탭메뉴3</span></a></li> -->
					<!-- <li><a href="#tab1-4" onclick="tabChg(4);"><span CL="STD_BYLOTNUM">탭메뉴4</span></a></li> -->
					<li class="btn_zoom_wrap">
						<ul>
							<!-- <li><button class="btn btn_smaller"><span>축소</span></button></li> -->
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section" id="tab1-4">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40 STD_NUMBER" 			GCol="rownum"						>1</td>
										<td GH="100 STD_KEY"			GCol="text,KEY"						>KEY			</td>
										<td GH="100 STD_WAREKY"			GCol="text,WAREKY"					>출하지점			</td>
										<td GH="100 STD_PLNTKY"			GCol="text,LOTA02"					>출고플랜트		</td>
										<td GH="100 STD_STKNUM"			GCol="text,STKNUM"					>선적문서			</td>
										<td GH="100 STD_LFART"			GCol="text,LFART"					>납품유형      	</td>
										<!-- <td GH="100 STD_SVBELN"			GCol="text,SVBELN"					>납품문서			</td> -->
										<td GH="100 STD_DOCUIT_L8210"   GCol="text,SVBELN"					>아이템번호</td>
										<td GH="100 STD_SPOSNR"			GCol="text,SPOSNR"					>납품품목			</td>
										<td GH="100 STD_STATUS"			GCol="text,STATUS"					>상태  			</td>
										<td GH="100 STD_SKUKEY"			GCol="text,SKUKEY"					>자재코드			</td>
										<td GH="100 STD_LOTA01"			GCol="text,LOTA01"					>출고저장위치		</td>
										<td GH="100 STD_QTYORG"			GCol="text,QTYORG"	GF="N 20,3"		>주문수량			</td>
										<td GH="100 STD_FSHQTY"			GCol="text,FSHQTY"	GF="N 20,3"		>츨고미완료수량		</td>
										<td GH="100 STD_QTQTY"			GCol="text,QTQTY"	GF="N 20,3"		>출고완료수량		</td>
										<td GH="100 STD_UOMKEY"			GCol="text,UOMKEY"					>납품단위			</td>
										<td GH="100 STD_DOCDAT"			GCol="text,DOCDAT"  GF="D"		    >전기일			</td>
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
						<button type='button' GBtn="subTotal"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- // content -->
<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>