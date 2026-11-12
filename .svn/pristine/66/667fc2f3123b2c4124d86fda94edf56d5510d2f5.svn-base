<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ page import="project.common.airfw.*"%>
<%@ include file="/common/include/webdek/head.jsp" %>
<style type="text/css">
 .content_wrap{background: none;}
</style>
<%
	User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%>
<script type="text/javascript">
	
	window.resizeTo('1200','600');
	
	$(document).ready(function(){
		var data = page.getLinkPopData();
		dataBind.dataNameBind(data, "searchArea");
		
		gridList.setGrid({
			id : "gridList",
			editable : true,
			module : "WmsInbound",
			command : "GR99STKKY"
		});
	
		gridList.gridMap.get("gridList").startHeadColDrag();
	});
	
	function fn_closing(){
		opener.createList();
		this.close();
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Save"){
			fn_save();
		} else if(btnName == "Close"){
			fn_closing();
		} else if(btnName == "Download"){
			excelDownload();
		}
	}
	
	function excelDownload(){
		gridList.excelUpGridId = "gridList";
	    gridList.excelUploadTemplateDown();
	}
	
	function gridExcelDownloadEventBefore(gridId){
		var param = new DataMap();
		if(gridId == "gridList"){
			param.put("dataGridExcel", "기초재고셋업_엑셀업로드_템플릿");
		}
		return param;
	}
	
	function fn_save() {
		
		var listcnt = gridList.getGridDataCount("gridList");
		
		if( listcnt == 0 ){
			commonUtil.msgBox("VALID_M0015"); // 처리할 데이터가 없습니다.
			return;
		}
		
		// check validation
		if(gridList.validationCheck("gridList", "data")) {
			//저장하시겠습니까?
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
				return;
			}
			
			var list = gridList.getGridData("gridList");
			
			var param = new DataMap();
			param.put("LANGKY", "<%=langky%>");
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("WAREKY", "<%=wareky%>");
			
			param.put("excelList", list);
			
			var json = netUtil.sendData({
				url : "/wms/inbound/json/saveGR98EXCEL.data",
				param : param
			});
			
			if(json && json.data){
				commonUtil.msgBox("VALID_M0001", json.data); // UDI 업로드 {0}건 저장에 성공하였습니다.
				//fn_closing();
			}
			
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
					
<!-- 					<input type="checkbox"  id="STTNEW" name="STTNEW" value="V" checked="checked"/> -->
<!-- 					<label CL="STD_LVORM" for="STTNEW">삭제여부</label> -->
					
					<input type="button" CB="Save SAVE BTN_SAVE">
					<input type="button" CB="Download REFLECT BTN_TEMPLD">
					<input type="button" CB="Close CHECK BTN_CLOSE">
				</div>
			</div>
		</div>
		
		<div class="search_next_wrap noSearch">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
					<li><a href="#tabs1-1" CL='STD_LIST'><span>리스트</span></a></li> 
					<!-- <li><a href="#tabs1-1" CL="STD_DOCUMENTINFO"><span>문서정보</span></a></li> -->
				</ul>
				<div class="table_box section" id="tabs1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
									    
<!-- 									    <td GH="60 STD_WAREKY" GCol="text,WAREKY">물류센터</td> -->
									    
									    <td GH="100 STD_SKUKEY" GCol="text,SKUKEY">제품코드</td>
									    <td GH="80 STD_QTYRCV" GCol="text,QTYRCV" GF="N 20,3">입고수량</td>
									    <td GH="100 STD_UOMKEY" GCol="text,UOMKEY">단위</td>
									    
									    <td GH="100 STD_PALQTY" GCol="text,PALQTY" GF="N 20,3">팔레트입수량</td>
									    
									    <td GH="80 STD_PTNRTY" GCol="text,PTNRTY">거래처유형</td>
										<td GH="120 STD_DPTNKY" GCol="text,DPTNKY">공급처코드</td>
									    
									    <td GH="80 STD_LOTA01" GCol="text,LOTA01">저장위치</td>
									    <td GH="80 STD_LOTA02" GCol="text,LOTA02">플랜트</td>
									    <td GH="100 STD_LOTA03" GCol="text,LOTA03">포장타입(PS)</td>
									    <td GH="100 STD_LOTA04" GCol="text,LOTA04">호기</td>
									    <td GH="100 STD_LOTA05" GCol="text,LOTA05">미정의</td>
									    <td GH="100 STD_LOTA06" GCol="text,LOTA06">재고상태</td>
									    <td GH="100 STD_LOTA07" GCol="text,LOTA07">주문번호(10)+아이템번호(6)MTO</td>
									    <td GH="100 STD_LOTA08" GCol="text,LOTA08">국가코드</td>
									    <td GH="120 STD_LOTA09" GCol="text,LOTA09">거래처코드MTO</td>
									    <td GH="120 STD_LOTA10" GCol="text,LOTA10">보류사유명</td>
									    
										<td GH="100 STD_LOTA11" GCol="text,LOTA11" GF="C 8">제조일자</td>
										<td GH="100 STD_LOTA11" GCol="text,LOTA12" GF="C 8">입고일자</td>
										<td GH="100 STD_LOTA13" GCol="text,LOTA13" GF="C 8">유통기한</td>
										<td GH="100 STD_LOTA14" GCol="text,LOTA14">개별바코드 (협력업체바코드)</td>
										<td GH="100 STD_LOTA15" GCol="text,LOTA15">LOT 번호</td>
										
										<td GH="100 STD_LOTA16" GCol="text,LOTA16">평량(PS)</td>
										<td GH="100 STD_LOTA17" GCol="text,LOTA17">컨테이너유형(PS)</td>
										
										<td GH="100 STD_LOTA18" GCol="text,LOTA18">미정의</td>
										<td GH="100 STD_LOTA19" GCol="text,LOTA19">미정의</td>
										<td GH="100 STD_LOTA20" GCol="text,LOTA20">미정의</td>
										
										<td GH="100 STD_LOTA21" GCol="text,LOTA21">미정의</td>
										<td GH="100 STD_LOTA22" GCol="text,LOTA22">미정의</td>
										<td GH="100 STD_LOTA23" GCol="text,LOTA23">미정의</td>
										<td GH="100 STD_LOTA24" GCol="text,LOTA24">미정의</td>
									    
									    <td GH="100 STD_RSNCOD" GCol="text,RSNCOD">사유코드</td>
										<td GH="100 STD_RSNTXT" GCol="text,RCPRSN">상세사유</td>
										
										<td GH="100 STD_AREAKY" GCol="text,AREAKY">창고</td>
										<td GH="100 STD_ZONEKY" GCol="text,ZONEKY">구역</td>
										<td GH="100 STD_LOCAKY" GCol="text,LOCAKY">로케이션</td>
										
										<td GH="120 STD_LOTA41" GCol="text,LOTA41">컨테이너NO</td>
										<td GH="120 STD_LOTA42" GCol="text,LOTA42">공급업체코드(수입밀롤)</td>
										
<!-- 										<td GH="200 STD_DOCTXT" GCol="text,DOCTXT">비고</td> -->
										
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					
					<div class="btn_lit tableUtil" id="resetUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
<!-- 						<button type='button' GBtn="add"></button> -->
<!-- 						<button type='button' GBtn="copy"></button> -->
<!-- 						<button type='button' GBtn="delete"></button> -->
<!-- 						<button type='button' GBtn="layout"></button> -->
<!-- 						<button type='button' GBtn="excel"></button> -->
						<button type='button' GBtn="excelUpload" id="excelUpload"></button>
						
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