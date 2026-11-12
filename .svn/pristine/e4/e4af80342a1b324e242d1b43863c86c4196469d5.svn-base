<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<script type="text/javascript">

var searchFlag = 1;

	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			module : "WmsInbound",
			command : "GR30POP",
		});
		
		searchList();
	});

	function searchList(){
		var data = page.getLinkPopData();
		var stkNum = data.get("STKNUM"); 
		var wareKy = data.get("WAREKY"); 
		var param = inputList.setRangeParam("searchArea");
		param.put("STKNUM", stkNum);
		param.put("WAREKY", wareKy);
            
		gridList.gridList({
			id : "gridList",
			param : param
		});
	}
	
	
	 function commonBtnClick(btnName){
	        if(btnName == "Search"){
	            searchList();
	
			}
		}
	 
	 
	
	
	
//   	//search 전체 체크박스 클릭
// 	function searchCheckBoxAll(obj) {
// 		var objId  = $(obj).attr("id");
// 		var param = new DataMap();
// /*		
// 		console.log(param);
		
// 		param {key : value}
// 		{"IFFLG", "N"}
// */		
// 		//전체 checkbox 해제
//   		if (objId == "STAT00") {
// 			if($(obj).prop("checked") == true){
// 				$(obj).parent('dd').children('input').prop("checked", true);
// 				param.put("IFFLG", "O");
// 			}else{
// 				$(obj).parent('dd').children('input').prop("checked", false);
// 				param.put("IFFLG", "X");
// 			}
//   		} else if ( objId == "STTNEW") { //미작업 선택시
//   			if ($('#STTNEW').is(":checked")) { //미작업이 선택됐을 때
// 	  			if ($('#STTTC2').is(":checked")) { // 입고완료가 check이면
// 	  				$('#STAT00').prop("checked", true);
// 	  				param.put("IFFLG", "O"); //Y,N 전체
// 	  			} else {
// 	  				$('#STAT00').prop("checked", false);
// 	  				param.put("IFFLG", "N");
// 	  			}
//   			} else {
//   				$('#STAT00').prop("checked", false);
//   				param.put("IFFLG", "Y");
//   			}
//   		} else if ( objId == "STTTC2") { //입고완료 선택시
//   			if ($('#STTTC2').is(":checked")) { //입고완료가 선택됐을 때
// 	  			if ($('#STTNEW').is(":checked")) { // 미작업이 check이면
// 	  				$('#STAT00').prop("checked", true);
// 	  				param.put("IFFLG", "N");
// 	  			} else {
// 	  				$('#STAT00').prop("checked", false);
// 	  				param.put("IFFLG", "Y");
// 	  			}
//   			} else {
//   				$('#STAT00').prop("checked", false);
//   				param.put("IFFLG", "X");
//   			}
//   		}
  		
//   		searchList2(param);

// 	}
  	
	
// 	function searchList2(param){
// 		alert(param);
	
		
// 		gridList.gridList({
// 			id : "gridList",
// 			param : param
// 		});
// 	}
  	
  	
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
                    <input type="button" CB="Search SEARCH BTN_DISPLAY" /><!-- 조회 -->

                </div>
            </div>
			<div class="search_inner">
				<div class="search_wrap ">
					 <dl>
					 		
	                        <dt CL="STD_STATDO">문서상태</dt>
	                        <dd>
	                        	<input type="checkbox" class="input" id="STAT00" name="STAT00" value="V" checked="checked" onclick="searchCheckBoxAll(this)"/>
								<label CL="STD_ALL" for="STAT00">전체</label>
	                            <input type="checkbox" class="input" id="STTNEW" name="STTNEW" value="V" checked="checked" onclick="searchCheckBoxAll(this)"/>
	                            <label CL="STD_NEW" for="STTNEW">미작업</label>
	                            <input type="checkbox" class="input" id="STTTC2" name="STTTC2" value="V" checked="checked" onclick="searchCheckBoxAll(this)"/>
	                            <label CL="STD_EXRECP" for="STTTC2">입고완료</label>
	                        </dd>
	                    </dl>
	            </div>
	        </div>
        </div>
		<div class="search_next_wrap">
			<div class="content_layout tabs">
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
									    <td GH="40" GCol="rownum">1</td>
									    <td GH="80 STD_STATDONM" GCol="text,STATDONM">문서상태명</td>
									    <td GH="150 STD_EBELN" GCol="text,SEBELN">오더번호 (생산지시번호)</td>
                                        <td GH="100 STD_STKNUM"     GCol="text,STKNUM">선적번호</td>
                                        <td GH="120 STD_DPTNKY2" GCol="text,DPTNKY"       >납품처</td>
                                        <td GH="200 STD_DPTNNM3" GCol="text,DPTNNM">납품처명</td>
                                        <td GH="150 STD_SKUKEY" GCol="text,SKUKEY">상품코드</td>
                                        <td GH="250 STD_DESC01" GCol="text,DESC01">상품명</td>
                                        <td GH="100 STD_QTYORG" GCol="text,MENGE" GF="N 20,3">원오더수량</td>
                                        <td GH="100 STD_UOMKEY" GCol="text,UOMKEY">단위</td>
                                        <td GH="100 STD_MEINS" GCol="text,MUOMKEY">기본단위</td>
                                        <td GH="100 STD_BOXQT" GCol="text,BOX" GF="N 20,3">BAG/BOX</td>
									</tr> 
								</tbody>
							</table>
						</div>sm
					</div>
					<div class="btn_lit tableUtil">
					<button type='button' GBtn="find"></button>
                        <button type='button' GBtn="sortReset"></button>
                        <button type='button' GBtn="total"></button>
                        <button type='button' GBtn="layout"></button>
                        <button type='button' GBtn="excel"></button>
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