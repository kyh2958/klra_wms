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
		initData();
		
		gridList.setGrid({
			id : "gridList",
			editable : false,
			module : "WmsInventory",
			command : "PDMV02DOC",
			gridMobileType : true,
		});
		
		/* 파라미터에 TASKKY가 존재하는 경우 재조회 */
		var TASKKY = '<%=paramDataMap.get("TASKKY")%>';
		if (TASKKY) {
			$('#TASKKY').val(TASKKY);
			searchList();
		}
		
		$('#TASKKY').focus();
	});
	
	var searchedParam = null;
	
	function initData() {
		searchedParam = null;
	}
	
	function initGrid() {
		gridList.resetGrid('gridList');
	}
	
	// 찾기버튼 클릭 이벤트
	function searchDocList() {
		mobile.linkPopOpen('/mobile/PDMV02_POP.page', searchedParam);
	}
	
	// 팝업 callback
	function DocPopCallback (data) {
		$('#TASKKY').val(data.get('TASKKY'));
		searchList();
	}
	
	// 작업지시번호로 문서정보검색
	function searchList(){
		initGrid();
		
		if(!validate.check("searchArea")){
			$('#TASKKY').focus();
			return;
		}
		
		var param = inputList.setRangeParam("searchArea");
		param.put("WAREKY", "<%=wareky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("LANGKY", "<%=langky%>");
		param.put("PROGID", "MV02");
		
		gridList.gridList({
			id : "gridList",
			param : param
		});

		searchedParam = new DataMap();
		searchedParam.putObject(structuredClone(param.map));
	}
	
	function searchEndEventFunction(){
		$("#TASKKY").focus();
	}
	// search bind end event 처리 = 조회 후 그리드에 data set 이후 발생
// 	function gridListEventDataBindEnd(gridId, dataLength){
// 		if(gridId == "gridList" && dataLength > 0) {
// 			var rowList = gridList.getGridData(gridId, true);
// 			if(dataLength == 1) {
// 				movePage(rowList[0]);
// 			}
// 		}
// 	}
	
	// 더블클릭 이벤트
	function gridListEventRowDblclick(gridId, rowNum) {
		var rowData = gridList.getRowData(gridId, rowNum);
		movePage(rowData);
	}
	
	function movePage(rowData) {
		var param = dataBind.paramData('searchArea', rowData);
		var map = param.map;
		
		var paramStr = '';
		for (var key in map) {
			if(paramStr) {
				paramStr += '&';
			}
			paramStr += key + '=' + map[key];
		}
		
		location.href = "/mobile/PDMV02_LIST.page?" + paramStr;
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
						<col width="25%" />
						<col width="25%" />
						<col width="25%" />
						<col width="25%" />
					</colgroup>
					<tbody id="searchArea">
						<tr>
							<th CL="STD_TASKKY">작업지시번호</th>
							<td colspan="2">
								<input type="text" class="text" name="TASKKY" id="TASKKY" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'searchList()')" validate="required"/>
							</td>
							<td onclick="searchDocList()">
								<button class="bt" style="height: 32px;">
									<p CL='BTN_FINDDATA'></p>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="tableWrap_search section" style="margin-bottom: 0px;">
				<div class="tableHeader">
					<table style="width: inherit"> 
						<colgroup>
							<col width="40px" />
							<col width="100px" />
							<col width="100px" />
							<col width="100px" />
							<col width="100px" />
						</colgroup>
						<thead>
							<tr class="thead">
								<th CL="STD_NO"			>No.</th>
								<th CL="STD_LOCASR"		>지번</th>
								<th CL="STD_LOCATG"		>To 지번</th>
								<th CL="STD_QTTAOR"		>주문건수</th>
								<th CL="STD_QTCOMP"		>완료건수</th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="tableBody">
					<table style="width: inherit">
						<colgroup>
							<col width="40px" />
							<col width="100px" />
							<col width="100px" />
							<col width="100px" />
							<col width="100px" />
						</colgroup>
						<tbody id="gridList">
							<tr CGRow="true">
								<td GCol="rownum"					>No.</td>
								<td GCol="text,LOCASR"				>지번</td>
								<td GCol="text,LOCATG"				>To 지번</td>
								<td GCol="text,QTTAOR"	GF="N 20,3"	>주문건수</td>
								<td GCol="text,QTCOMP"	GF="N 20,3"	>완료건수</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="footer_5" style="position:fixed;">
				<table>
					<tr>
						<td class=f_1 width="50%" onclick="window.top.closePage()">
							<label CL='BTN_CLOSE'></label>
						</td>
						<td class=f_1 width="50%" onclick ="searchList()" >
							<label CL='BTN_REFRESH'></label>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>