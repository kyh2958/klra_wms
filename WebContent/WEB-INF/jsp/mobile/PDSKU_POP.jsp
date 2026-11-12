<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<head>
<style type="text/css">
button{ vertical-align: bottom; height: 37px; }
input:read-only{ background-color: #e2e2e2; }
select:DISABLED{ background-color: #e2e2e2; }
.tem5_content .tableWrap_search{ overflow: hidden; }
</style>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	$(document).ready(function () {
		init();
		
		gridList.setGrid({
			id             : "gridList",
			module         : "WmsInventory",
			command        : "SKUMA",
			editable       : false,
			gridMobileType : true,
		});

		var param = mobile.getLinkPopData();
		dataBind.dataNameBind(param, "bindArea");
		if(	param.get("SKUKEY") ||
			param.get("DESC01") ||
			param.get("SKUL01")) { // 한가지라도 존재하는 경우
			searchList();
		}
	})
	
	var selectedRowNum = 0;
	
	function init() {
		selectedRowNum = 0;
		
		$('#SKUL01').val("");
		$('#DESC01').val("");
	}
	
	function searchList() {
		gridList.resetGrid('gridList');
		
		var param = inputList.setRangeParam("searchArea");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("LANGKY", "<%=langky%>");
		param.put("WAREKY", "<%=wareky%>");
		
		if(	param.get("SKUKEY") ||
			param.get("DESC01") ||
			param.get("SKUL01")) { // 한가지라도 존재하는 경우
			gridList.gridList({
				id : "gridList",
				param : param
			});
		} else {
			commonUtil.msgBox("VALID_M1551", null, null, function () {
				$('#SKUKEY').focus();
			}); // 품목을 입력하세요.
		}
	}
	
	function comboEventDataBindeBefore(comboAtt){
		if (comboAtt == "airfw,COMCOMBO") {
			var param = new DataMap();
			param.put("CODE", 'SKUL01');
			return param;
		} else if (comboAtt == "WmsInventory,ZONECOMBO") {
			var param = inputList.setRangeParam("searchArea");
			return param;
		}
	}
	
	// 더블클릭 이벤트
	function gridListEventRowDblclick(gridId, rowNum) {
		var data = gridList.getRowData("gridList", rowNum);
		callback(data);
	}
	
	// 선택버튼 클릭 이벤트
	function onClickChooseBtn() {
		var data = gridList.getRowData("gridList", selectedRowNum);
		callback(data);
	}
	
	// 그리드 클릭 시 rowNum 변수 저장
	function gridListEventRowClick(gridId, rowNum) {
		selectedRowNum = rowNum;
	}
	
	function callback (data) {
		if(!data.isEmpty()) {
			data.put('param', mobile.getLinkPopData());
			// GRowState, GRowNum 미삭제 시 appendJson 호출 시 데이터 바인딩 이상하게 됨.
			data.remove("GRowState");
			data.remove("GRowNum");
			window.opener.SkuPopCallback(data);
			self.close();
		}
	}
</script>
</head>
<body>
	<div id="menu-container">
		<div class="menu-item"></div>
	</div>
	<div class="main_wrap" id="paramArea">
		<div class="tem4_content">
			<div class="select_box" id="bindArea">
				<table>
					<tbody id="searchArea">
						<tr>
							<th CL="STD_SKUKEY">품목코드</th>
							<td>
								<input type="text" class="text" id="SKUKEY" name="SKUKEY" style="width:100%;"/>
							</td>
						</tr>
						<tr>
							<th CL="STD_SKUL01">품목구분</th>
							<td>
								<select id="SKUL01" name="SKUL01" Combo="airfw,COMCOMBO" style="width: 100%;" onchange="searchList()"><option value=""/></select>
							</td>
						</tr>
						<tr>
							<th CL="STD_DESC01" >구역</th>
							<td>
								<input type="text" class="text" id="DESC01" name="DESC01" style="width:100%;"/>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="tableWrap_search section">
				<div class="tableHeader">
					<table style="width: 100%">
						<colgroup>
							<col width="10%" />
							<col width="*" />
						</colgroup>
						<thead>
							<tr class="thead">
								<th CL='STD_NO'>순번</th>
								<th CL='STD_SKUKEY'>품목코드</th>
								<th CL='STD_SKUL01'>품목구분</th>
								<th CL='STD_DESC01'>품목명</th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="tableBody">
					<table style="width: 100%">
						<colgroup>
							<col width="10%" />
							<col width="*" />
						</colgroup>
						<tbody id="gridList">
							<tr CGRow="true">
								<td GCol="rownum">1</td>
								<td GCol="text,SKUKEY">품목코드</td>
								<td GCol="text,SKUL01">품목구분</td>
								<td GCol="text,DESC01">품목명</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<!-- end table_body -->
			<div class="footer_5" style="position:fixed;">
				<table>
					<tr>
						<td class=f_1 width="33%" onclick="self.close()">
							<label CL='BTN_CLOSE'></label>
						</td>
						<td class=f_1 width="33%" onclick="searchList()">
							<label CL='BTN_SEARCH'>조회</label>
						</td>
						<td class=f_1 width="34%" onclick ="onClickChooseBtn()" >
							<label CL='BTN_CHOOSE'></label>
						</td>
					</tr>
				</table>
			</div>
			<!-- end footer_5 -->
		</div>
	</div>
</body>