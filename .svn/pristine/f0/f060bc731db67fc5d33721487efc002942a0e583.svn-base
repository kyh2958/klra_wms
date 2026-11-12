<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String showPlntky = request.getParameter("showPlntky");
	String showStlky  = request.getParameter("showStlky");
	showPlntky = showPlntky == null ? "" : showPlntky.trim();
	showStlky  = showStlky  == null ? "" : showStlky.trim();
%>
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
			id : "gridList",
			editable : false,
			gridMobileType : true,
		});

		var param = searchedParam == null ? mobile.getLinkPopData() : searchedParam;
		searchList(param);
	})
	
	var searchedParam = null;
	var selectedRowNum = 0;
	var areaList = [];
	var zoneList = [];
	
	function init() {
		searchedParam  = null;
		selectedRowNum = 0;
		
		$('#AREAKY').val("");
		$('#ZONEKY').val("");
	}
	
	function searchList(param) {
		var json = param.get('json');
		var viewList = json.data;
		
		/* AREMA 조회 */
		areaList = netUtil.sendData({
			module : "WmsInventory",
			command : "AREACOMBO",
			param : param
		});
		
		/* ZONMA 조회 */
		zoneList = netUtil.sendData({
			module : "WmsInventory",
			command : "ZONECOMBO",
			param : param
		});
		
		if (areaList && areaList.data) { areaList = areaList.data; }
		if (zoneList && zoneList.data) { zoneList = zoneList.data; }
		
		areaList = areaList.filter(data => viewList.find(view => data.VALUE_COL == view.AREAKY));
		zoneList = zoneList.filter(data => viewList.find(view => data.VALUE_COL == view.ZONEKY));
		
		/* AREAKY, ZONEKY 초기화 */
		$('#AREAKY').html('');
		$('#ZONEKY').html('');
// 		$('#AREAKY').html('<option value=""/>');
// 		$('#ZONEKY').html('<option value=""/>');
		
		var paramAREAKY = param.get('AREAKY');
		var paramZONEKY = param.get('ZONEKY');
		
		/* AREAKY option 추가 */
		var $tag = $('#AREAKY');
		for (var i in areaList) {
			var AREAKY = areaList[i].VALUE_COL;
			var AREANM = areaList[i].TEXT_COL;
			$tag.append('<option value="' + AREAKY + '">[' + AREAKY + ']' + AREANM + '</option>');
		}
		
		if (!paramAREAKY && !paramZONEKY) {
			paramAREAKY = $tag.find('option')[0].value;
		}
		
		$tag.val(paramAREAKY);
		
		/* ZONEKY option 추가 */
		var $tag = $('#ZONEKY');
		for (var i in zoneList) {
			var ZONEKY = zoneList[i].VALUE_COL;
			var ZONENM = zoneList[i].TEXT_COL;
			$tag.append('<option value="' + ZONEKY + '">[' + ZONEKY + ']' + ZONENM + '</option>');
		}
		$tag.val(paramZONEKY);
		
		/** grid 데이터 필터링 */
		if (paramAREAKY) { viewList = viewList.filter(data => data.AREAKY == paramAREAKY); }
		if (paramZONEKY) { viewList = viewList.filter(data => data.ZONEKY == paramZONEKY); }
		if (param.get('PLNTKY')) { viewList = viewList.filter(data => data.PLNTKY == param.get('PLNTKY')); }
		
		var jsonObj = {data: viewList};
		
		gridList.resetGrid('gridList');
		gridList.appendJson('gridList', jsonObj);
		
		searchedParam = new DataMap();
		searchedParam.putObject(structuredClone(param.map));
	}
	
	// select 태그 변경 이벤트
	function onChangeSelectTag (tagId) {
		var $selectTag = $('#' + tagId);
		
		if (tagId == 'AREAKY') { // 변경된 태그가 AREAKY인 경우 ZONEKY 삭제
			searchedParam.remove('ZONEKY');
			$('#ZONEKY').val("");
		}
		searchedParam.map[tagId] = $selectTag.val();
		searchList(searchedParam);
	}
	
// 	function comboEventDataBindeBefore(comboAtt){
// 		if (comboAtt == "WmsInventory,AREACOMBO") {
// 			var param = new DataMap();
// 			return param;
// 		} else if (comboAtt == "WmsInventory,ZONECOMBO") {
// 			var param = new DataMap();
// 			param.put('AREAKY', $('#AREAKY').val());
// 			return param;
// 		}
// 	}
	
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
			window.opener.LocPopCallback(data);
			self.close();
		}
	}
	
	function isSearched() {
		return searchedParam != null;
	}
	
</script>
</head>
<body>
	<div id="menu-container">
		<div class="menu-item"></div>
	</div>
	<div class="main_wrap" id="paramArea">
		<div class="tem4_content">
			<div class="select_box" id="searchArea">
				<table>
					<tbody>
						<tr>
							<th CL="STD_AREAKY">영역</th>
							<td>
								<select id="AREAKY" name="AREAKY" style="width:100%;" onchange="onChangeSelectTag(this.id)">
									<option value="">
								</select>
							</td>
						</tr>
						<tr>
							<th CL="STD_ZONEKY" >구역</th>
							<td>
								<select id="ZONEKY" name="ZONEKY" style="width:100%;" onchange="onChangeSelectTag(this.id)">
									<option value="">
								</select>
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
								<th CL='STD_AREAKY'>영역</th>
								<th CL='STD_ZONEKY'>구역</th>
								<th CL='STD_LOCAKY'>지번</th>
							<% if (!"".equals(showPlntky)) { %>
								<th CL='STD_PLNTKY'>플랜트</th>
							<% } %>
							<% if (!"".equals(showStlky)) { %>
								<th CL='STD_STLKY'>저장위치</th>
							<% } %>
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
								<td GCol="text,AREAKY"></td>
								<td GCol="text,ZONEKY"></td>
								<td GCol="text,LOCAKY"></td>
							<% if (!"".equals(showPlntky)) { %>
								<td GCol="text,PLNTKY"></td>
							<% } %>
							<% if (!"".equals(showStlky)) { %>
								<td GCol="text,STLKY"></td>
							<% } %>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<!-- end table_body -->
			<div class="footer_5" style="position:fixed;">
				<table>
					<tr>
						<td class=f_1 width="50%" onclick="self.close()">
							<label CL='BTN_CLOSE'></label>
						</td>
						<td class=f_1 width="50%" onclick ="onClickChooseBtn()" >
							<label CL='BTN_CHOOSE'></label>
						</td>
					</tr>
				</table>
			</div>
			<!-- end footer_5 -->

		</div>
	</div>
</body>