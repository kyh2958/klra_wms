<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<head>
<style type="text/css">
button {float: right; }
input:read-only { 
	background-color: #e2e2e2;
}
select:DISABLED {
	background-color: #e2e2e2;
}
</style>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>Mobile WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script type="text/javascript" src="/wms/js/wms.js"></script>
<script>
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			editable : false,
			module : "WmsInventory",
			command : "MV02HEAD",
			gridMobileType : true,
		});
		searchList();
// 		$('#DOCDAT').focus();
	});
	
	var selectedRowNum = 0;
	
	function initGrid() {
		gridList.resetGrid('gridList');
	}
	
	// 이동지시 문서 head 검색
	function searchList(param){
		initGrid();
		if (validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			param.put("WAREKY", "<%=wareky%>");
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			param.put("PROGID", "MV02");
			
			gridList.gridList({
				id : "gridList",
				param : param
			});
		}
	}
	
	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList" && dataLength == 0){
			$('#DOCDAT').focus();
		}
	}
	
	// 더블클릭 이벤트
	function gridListEventRowDblclick(gridId, rowNum) {
		var data = gridList.getRowData("gridList", rowNum);
		callback(data);
	}
	
	// 그리드 클릭 시 rowNum 변수 저장
	function gridListEventRowClick(gridId, rowNum) {
		selectedRowNum = rowNum;
	}
	
	// 선택버튼 클릭 이벤트
	function onClickChooseBtn() {
		var data = gridList.getRowData("gridList", selectedRowNum);
		callback(data);
	}
	
	function callback (data) {
		if(!data.isEmpty()) {
			data.put('param', mobile.getLinkPopData());
			window.opener.DocPopCallback(data);
			self.close();
		}
	}
	
	function fn_onfocus(objId){
		$("#"+objId).select();
	}
</script>
</head>
<body>
	<div class="main_wrap">
		<div class="tem4_content">
			<div class="select_box" id="searchArea">
				<table>
					<colgroup>
						<col width="20%" />
						<col width="30%" />
						<col width="30%" />
						<col width="20%" />
					</colgroup>
					<tbody id="searchArea">
						<tr>
							<th CL="STD_DOCDAT">전기일자</th>
							<td colspan="3">
								<input type="text" class="text" name="DOCDAT" id="DOCDAT" UIFormat="C N" onchange="searchList();" validate="required" style="text-align: center; width: 80%" onfocus="fn_onfocus(this.id)" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="tableWrap_search section" style="margin-bottom: 0px;">
				<div class="tableHeader">
					<table style="width: inherit"> 
						<colgroup>
							<col width="10%" />
							<col width="45%" />
							<col width="45%" />
						</colgroup>
						<thead>
							<tr class="thead">
								<th CL="STD_NO"			>No.</th>
								<th CL="STD_TASKKY"		>지번</th>
								<th CL="STD_STATDO"		>To 지번</th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="tableBody">
					<table style="width: inherit">
						<colgroup>
							<col width="10%" />
							<col width="45%" />
							<col width="45%" />
						</colgroup>
						<tbody id="gridList">
							<tr CGRow="true">
								<td GCol="rownum"		>No.</td>
								<td GCol="text,TASKKY"	>지번</td>
								<td GCol="text,STATDO"	>To 지번</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="footer_5" style="position:fixed;">
				<table>
					<tr>
						<td class=f_1 width="50%" onclick="self.close()">
							<label CL='BTN_CLOSE'></label>
						</td>
						<td class=f_1 width="50%" onclick ="onClickChooseBtn()" >
							<label CL='BTN_COMPLE'></label>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>