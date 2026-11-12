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
<script>
	$(document).ready(function(){
// 		initSearchArea(); // onReadBarcode에서 수행중
		
		var data = mobile.getLinkPopData();
		dataBind.dataNameBind(data, "bindArea");
		
		$('#QTYPDA').val(setDecimalValue(0, 3)); // QTYPDA init
		
		gridList.setGrid({
			id : "gridList",
			gridMobileType : true
		});
		
		$('#BARCOD').focus();
		onReadBarcode();
	});

	function fn_onfocus(objId){
		$("#"+objId).select();
	}
	
	function initSearchArea() {
		// 버튼 비활성화
		var targetBtn   = $('#searchArea').find('button');
		targetBtn.attr('disabled', true);
		
		// input 초기화
		var targetInput = $('#searchArea').find('input')
										  .not('#BARCOD')  // 바코드 제외
										  .not('#QTYPDA'); // 실사수량 제외
		targetInput.val("");
		targetInput.attr("readonly", true);
		$('#QTYPDA').val(setDecimalValue(0, 3)); // QTYPDA init
		
	}
	
	function onReadBarcode() {
		initSearchArea();
		
		var BARCOD = $('#BARCOD').val();
		if (!BARCOD) {
			return;
		}
		
		// 바코드, 품목코드가 동일하면 return?
		var param = inputList.setRangeParam("searchArea");
		
		var json = netUtil.sendData({
			module  : "WmsInventory",
			command : "PDPC01_NEW",
			param   : param,
		});
		
		if (!json || !json.data || json.data.length < 1) {
			// 버튼, input 활성화
			var targetBtn   = $('#searchArea').find('button');
			var targetInput = $('#searchArea').find('input')
											  .not('#UOMKEY');
			targetBtn.attr('disabled', false);
			targetInput.attr("readonly", false);
			
			commonUtil.msgBox("VALID_M0005", null, null, function () {
				$('#LOCAKY').focus();
			}); // 조회된 데이터가 없습니다.
			
			return;
		}
		
		if (json.data.length == 1) {
			// append grid
			appendRowJsonObj(json.data[0]);
		} else {
			param.put("json", json);
			param.put('flag', '1');
			mobile.linkPopOpen('/mobile/PDLOC_POP.page', param);
		}
	}
	
	function searchLocationList () {
		var param = new DataMap();
		param.put('WAREKY', '<%=wareky%>');
		param.put("OWNRKY", "<%=ownrky%>");
		
		var json = netUtil.sendData({
			module : "WmsAdmin",
			command : "LOCMA",
			param : param
		});
		
		if (!json || !json.data || json.data.length == 0) {
			commonUtil.msgBox("VALID_M0005"); // 조회된 데이터가 없습니다.
			return;
		}
		
		if (json.data.length == 1){
			var data = new DataMap();
			data.putObject(json.data[0]);
			data.put('param', param);
			
			LocPopCallback(data);
		} else {
			param.put("json", json);
			param.put('flag', '2');
			mobile.linkPopOpen('/mobile/PDLOC_POP.page', param);
		}
	}
	
	function LocPopCallback(data) {
		var flag = data.get("param").get('flag'); // 1: 재고조회, 2: 마스터조회
		
		if (flag == '1') {
			appendRowJsonObj(JSON.parse(data));
		} else if (flag =='2') {
			$('#LOCAKY').val(data.get("LOCAKY"));
		}
	}
	
	function searchSkumaList () {
		var param = inputList.setRangeParam("searchArea");
		var SKUKEY = param.get("SKUKEY");
		var DESC01 = param.get("DESC01");
		
		var popParam = new DataMap();
		popParam.put("SKUKEY", SKUKEY);
		popParam.put("DESC01", DESC01);
		mobile.linkPopOpen('/mobile/PDSKU_POP.page', popParam);
	}
	
	function SkuPopCallback(data) {
		var $LOCAKY = $('#LOCAKY');
		if(!$LOCAKY.val().trim()) {
			$LOCAKY.focus()
			return;
		}
		
		// 버튼, input 비활성화
		var targetBtn = $('#searchArea').find('button');
		targetBtn.attr('disabled', true);
		
		var targetInput = $('#searchArea').find('input')
										  .not('#BARCOD')
										  .not('#QTYPDA');
		targetInput.attr("readonly", true);
		
		/*
			STKKY 조회 결과와 SKUMA 조회 결과에서 컬럼 차이가 존재하는 경우
			gridList.appendJson 사용 시 데이터 이상하게 매핑 됨.
			해당 현상 해결하기 위해 빈 값으로 세팅해주는 로직.
		*/
		var jsonParam = inputList.setRangeParam("searchArea");
		jsonParam.putAll(data);
		jsonParam.put("LOTA14", jsonParam.get('BARCOD'));
		
		var emptyStrList = ["AREAKY", "ZONEKY", "LOTA01", "LOTA02"];
		var emptyNumList = ["QTSIWH", "QTSALO", "QTSPMO", "QTSPMI", "QTBLKD", "QTYUOM"];
		emptyStrList.forEach(function (key) { jsonParam.put(key, " "); });
		emptyNumList.forEach(function (key) { jsonParam.put(key, 0  ); });
		
		jsonParam.remove('param');
		jsonParam.remove('BARCOD');
		jsonParam.remove('RANGE_DATA_PARAM');
		
		// append grid
		var jsonData = JSON.parse(jsonParam);
		appendRowJsonObj(jsonData);
		
		$('#BARCOD').focus();
	}
	
	/* gridList에 jsonObject(단건) append */
	function appendRowJsonObj (json) {
		// bind data
		var bindData = new DataMap();
		bindData.putObject(json);
		dataBind.dataNameBind(bindData, "bindArea");
		checkNumber('QTYPDA');
		/* 
			gridList에 append 시
			기존 데이터 key 순서와 json 내 key 순서가 다른 경우
			데이터가 이상하게 바인딩되어 key sort 적용.
		 */
		var sorted = sortJsonByKey(json);
		
		var jsonArray = [];
		jsonArray.push(sorted);
		
		var jsonObj = {data: jsonArray}; // parse jsonObject
		
		gridList.appendJson('gridList', jsonObj); // append grid
		focusOnGridLast(); // 마지막 row focus
		$('#BARCOD').focus();
	}
	
	// json key sort asc
	function sortJsonByKey (jsonObj) {
		var sorted = {};
		Object.keys(jsonObj).sort().forEach(function(key) {
			sorted[key] = jsonObj[key];
		});
		return sorted;
	}
	
	// 마지막 row focus
	function focusOnGridLast () {
		var rowNumList = gridList.getRowNumList('gridList');
		var maxRowNum = Math.max(...rowNumList);
		gridList.setColFocus('gridList', maxRowNum, 'LOTA14');
	}
	
	function onChangeQtyPda () {
		var rowNum = gridList.getFocusRowNum('gridList');
		var qty = setDecimalValue($('#QTYPDA').val(), 3);
		
		$('#QTYPDA').val(qty);
		
		gridList.setColValue("gridList", rowNum, "QTYPDA", qty);
		gridList.setColFocus("gridList", rowNum, 'LOTA14');

		$('#BARCOD').focus();
	}
	
	function saveData() {
		if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
			return;
		}
		// 그리드 데이터 가져오기
		var dataList = gridList.getGridData('gridList', true);
		if (dataList.length > 0) {
			var reqParam = mobile.getLinkPopData();
			
			var param = new DataMap();
			param.put("list", dataList);
			param.put("PROGID", "<%=menuId%>");
			param.put("LANGKY", "<%=langky%>");
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("PHYIKY", reqParam.get("PHYIKY"));
			
			
			var json = netUtil.sendData({
				url : "/wms/inventory/json/savePDPC01new.data",
				param : param
			});
			
			if(json && json.data){
				commonUtil.msgBox("MASTER_M0564", null, null, function () {
					// 팝업 닫고 재조회
					window.opener.searchList();
					closePop();
				}); //저장이 완료되었습니다.
			}
		} else {
			commonUtil.msgBox("ORDER_M0005"); // 저장할 데이터가 없습니다.
			return false;
		}
	}
	
	function closePop () {
		mobile.linkPopClose();
	}
	
	function setDecimalValue(str, decimal) {
		str = new String(str);
		var regex = /[^0-9.]/g;
		var number = Number(str.replace(regex, ""));
		number = number ? number : 0;
		return number.toLocaleString(undefined, { minimumFractionDigits: decimal });
	}
	
	function checkNumber(id) {
		$tag = $('#' + id);
		$tag.val(setDecimalValue($tag.val(), 3));
	}
</script>
</head>
<body>
	<div class="main_wrap">
		<div class="tem4_content">
			<div class="select_box" id="bindArea">
				<table>
					<colgroup>
						<col width="25%" />
						<col width="25%" />
						<col width="25%" />
						<col width="25%" />
					</colgroup>
					<tbody id="searchArea">
						<tr>
							<th CL="STD_BARCOD">바코드</th>
							<td colspan="3"><input type="text" class="text" name="BARCOD" id="BARCOD" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'onReadBarcode()')"/></td>
						</tr>
						<tr>
							<th CL="STD_LOCAKY">지번</th>
							<td colspan="2"><input type="text" class="text" name="LOCAKY" id="LOCAKY" onfocus="fn_onfocus(this.id)"/></td>
							<td><button class="bt" style="height: 36px;" onclick="searchLocationList()"><p CL='BTN_FINDDATA'></p></button></td>
						</tr>
						<tr>
							<th CL="STD_SKUKEY">품목코드</th>
							<td colspan="2"><input type="text" class="text" name="SKUKEY" id="SKUKEY" onfocus="fn_onfocus(this.id)"/></td>
							<td><button class="bt" style="height: 36px;" onclick="searchSkumaList()"><p CL='BTN_FINDDATA'></p></button></td>
						</tr>
						<tr>
							<th CL="STD_DESC01">품목명</th>
							<td colspan="3"><input type="text" class="text" name="DESC01" id="DESC01" onfocus="fn_onfocus(this.id)"/></td>
						</tr>
						<tr>
							<th CL="STD_QTYPDA">PDA실사수량</th>
							<td colspan="2"><input type="text" class="text" name="QTYPDA" id="QTYPDA" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'onChangeQtyPda()')" onchange="onChangeQtyPda()" style="text-align: right"/></td>
							<td colspan="1"><input type="text" class="text" name="UOMKEY" id="UOMKEY"/></td>
						</tr>
					</tbody>
				</table>
				<div class="tableWrap_search section">
					<div class="tableHeader">
						<table style="width: 100%">
							<colgroup>
								<col width="60px"  />
								<col width="150px" />
								<col width="100px" />
								<col width="120px" />
								<col width="60px" />
								<col width="150px" />
								<col width="300px" />
								<col width="100px" />
								<col width="100px" />
								<col width="100px" />
								<col width="100px" />
							</colgroup>
							<thead>
								<tr>
									<th CL="STD_NUMBER">1</th>
									<th CL="STD_LOTA14">개별바코드</th>
									<th CL="STD_LOCAKY">로케이션</th>
									<th CL="STD_QTYPDA">PDA실사수량</th>
									<th CL="STD_UOMKEY">단위</th>
									<th CL="STD_SKUKEY">품목코드</th>
									<th CL="STD_DESC01">품목명</th>
									<th CL="STD_LOTA01">저장위치</th>
									<th CL="STD_LOTA02">플랜트</th>
									<th CL="STD_WAREKY">거점</th>
									<th CL="STD_WARENM">거점명</th>
								</tr>
							</thead>
						</table>
					</div>
					<div class="tableBody">
						<table style="width: 100%">
							<colgroup>
								<col width="60px"  />
								<col width="150px" />
								<col width="100px" />
								<col width="120px" />
								<col width="60px" />
								<col width="150px" />
								<col width="300px" />
								<col width="100px" />
								<col width="100px" />
								<col width="100px" />
								<col width="100px" />
							</colgroup>
							<tbody id="gridList">
								<tr CGRow="true">
									<td GCol="rownum">1</td>
									<td GCol="text,LOTA14"				>개별바코드</td>
									<td GCol="text,LOCAKY"				>로케이션</td>
									<td GCol="text,QTYPDA"	GF="N 20,3"	>PDA실사수량</td>
									<td GCol="text,UOMKEY"				>단위</td>
									<td GCol="text,SKUKEY"				>품목코드</td>
									<td GCol="text,DESC01"				>품목명</td>
									<td Gcol="text,LOTA01"				>저장위치</td>
									<td Gcol="text,LOTA02"				>플랜트</td>
									<td Gcol="text,WAREKY"				>거점</td>
									<td Gcol="text,WARENM"				>거점명</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- end table_body -->
			<div class="footer_5" style="position:fixed;">
				<table>
					<tr>
						<td class=f_1 onclick ="closePop()" width="50%"><label CL='BTN_PREVITEM'></label></td>
						<td class=f_1 onclick ="saveData()" width="50%"><label CL='BTN_SAVE'></label></td>
					</tr>
				</table>
			</div>
			<!-- end footer_5 -->	
		</div>
	</div>
</body>