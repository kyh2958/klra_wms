<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<head>
<style type="text/css">
button{ vertical-align: bottom; height: 37px; }
input:read-only{ background-color: #e2e2e2; }
select:disabled{ background-color: #e2e2e2; }
.tem5_content .tableWrap_search  { overflow: hidden; }
.tem4_content .select_box table  { width  : 98% !important; margin: auto; }
.tem4_content .select_box .text  { padding: unset !important; margin: unset !important; }
.tem4_content table th           { padding: 3px !important; white-space: nowrap; }
.tem4_content table td           { padding: 3px !important; }
.number { text-align: right }
</style>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	$(document).ready(function(){
		initTable();
		initData();
		
		$('.number').change(function (e) {
			var value = e.currentTarget.value;
			e.currentTarget.value = setDecimalValue(value, 3);
		})
	});
	
	var searchedParam = null;
	
	function initTable() {
		$('#paramArea').find('input[type="hidden"]').remove();
		$('#paramArea').find('input[name!="BARCOD"]').val("");
		$('#paramArea').find('select').attr('disabled', true).val("");
		
		$('#BARCOD').focus();
	}
	
	function initData() {
		searchedParam = null;
	}
	
	function playSuccessSound(){
		PlaySound('s');
	}
	
	// 바코드 스캔시 로케이션 리스트 조회 및 리스트 사이즈에 따른 분기
	function searchLocationList(){
		initTable();
		
		if (validate.check("searchArea")) {
			var param = inputList.setRangeParam("searchArea");
			param.put('WAREKY', '<%=wareky%>');
			
			var json = netUtil.sendData({
				module : "WmsInventory",
				command : "PDSJ01LOC",
				param : param,
				successFunction : "playSuccessSound"
			});
			
			var data = json.data;
			
			// 1건 조회 시
			if (data && data.length == 1) {
				searchedParam = new DataMap();
				searchedParam.putObject(structuredClone(param.map));
				
				getDetail(param);
			// 다건 조회 시
			} else if (data && data.length > 1) {
				searchedParam = new DataMap();
				searchedParam.putObject(structuredClone(param.map));
				
				param.put("json", json);
				mobile.linkPopOpen('/mobile/PDLOC_POP.page?showPlntky=true', param);
			// 미조회 시
			} else {
				commonUtil.msgBox("VALID_M0005", null, null, function () {
					initTable();
					initData();
					$('#BARCOD').focus();
				});
			}
		}
	}
	
	function getDetail(param) {
		if(searchedParam) {
			param.put('WAREKY', '<%=wareky%>');
			
			var json = netUtil.sendData({
				module : "WmsInventory",
				command : "PDSJ01",
				param : param
			});
			
			if (!json || !json.data || json.data.length == 0) { // 미조회 시
				commonUtil.msgBox("VALID_M0005", null, null, function () {
					$('#LOTA06').val(searchedParam.get('LOTA06'));
					$('#LOTA02').val(searchedParam.get('LOTA02'));
				});
				return;
			// 다건 조회 시
			} else if (json.data.length == 1) {
				setTable(json.data[0]);
			} else {
				param.put("json", json);
				mobile.linkPopOpen('/mobile/PDSTK_LOTA_POP.page', param);
			}
		}
	}
	
	function StkLotaPopCallback(jsonObj) {
		setTable(jsonObj.data[0]);
	}
	
	function LocPopCallback(data) {
		$('#LOCAKY').val(data.get("LOCAKY"));
		
		searchedParam.putAll(data);
		searchedParam.put('LOTA02', data.get("PLNTKY"));
		getDetail(searchedParam);
	}
	
	function onParamChange() {
		var param = inputList.setRangeParam("searchArea");
		getDetail(param);
	}
	
	function setTable(data) {
		if(!data) {
			commonUtil.msgBox("VALID_M0005");
			return;
		}
		
		initTable();
		searchedParam.put('LOTA06', data.LOTA06);
		searchedParam.put('LOTA02', data.LOTA02);
		
		$('#paramArea').find('select').attr('disabled', false); //
		
		for (var key in data) {
			var value = data[key];
			
			$inputTag  = $('#bindArea').find('input[name=' + key + ']');
			$selectTag = $('#bindArea').find('select[name=' + key + ']');
			
			// input 태그가 존재하는 경우 값 세팅
			if ($inputTag.length > 0) {
				value = setValueByClass($inputTag, value);
				$inputTag.val(value);
			// select 태그가 존재하는 경우 값 세팅
			} else if ($selectTag.length > 0) {
				value = setValueByClass($selectTag, value);
				$selectTag.val(value);
			// 없는 경우 신규 hidden 태그 생성
			} else {
				var hiddenInput = '<input type="hidden" id="'+key+'" name="' + key + '" value="' + value + '"/>';
				$('#paramArea').append(hiddenInput);
			}
		}
		
		getQTYUOM('AQTADJU');
	}
	
	function setValueByClass($tag, value) {
		var className = $tag[0].className;
		
		// class에 number가 포함되어 있는 데이터 가공
		var isNumberType = className && className.indexOf(' ') > 0 && className.split(' ').includes('number');
		if (isNumberType) {
			value = setDecimalValue(value, 3);
		}
		
		return value;
	}
	
	function setDecimalValue(str, decimal) {
		
		if ($("#SKUG05").length && $("#SKUG05").val() == "20") {
			decimal = 0;
		}
		
		str = new String(str);
		var regex = /[^0-9.]/g;
		var number = Number(str.replace(regex, ""));
		number = number ? number : 0;
		return number.toLocaleString(undefined, { minimumFractionDigits: decimal });
	}
	
	function getQTYUOM(id) {
		if(!searchedParam) {
			return;
		}
		
		var param = inputList.setRangeParam("searchArea");
		param.put('WAREKY', '<%=wareky%>');
		param.put('SKUKEY', $('#SKUKEY').val());
		param.put('UOMKEY', $('#UOMKEY').val());
		
		var regex = /[^0-9.]/g;
		param.put('QTYRCV', Number($('#' + id).val().replace(regex, "")));
		
		switch (id) {
			case "QTYBOX" : param.put('UOMKEY', 'BOX'); break;
			default       : break;
		}
		
		var json = netUtil.sendData({
			module : "WmsInventory",
			command : "QTYUOM",
			sendType : "map",
			param : param,
		});
		
		if (json && json.data) {
			var data = json.data;
			
			var $tag = $('#AQTADJU');
			switch ($('#UOMKEY').val()) {
				case "PAL" : $tag.val(setValueByClass($tag, data.QTYPAL)); break;
				case "BAG" : $tag.val(setValueByClass($tag, data.QTYBAG)); break;
				case "BOX" : $tag.val(setValueByClass($tag, data.QTYBOX)); break;
				case "EA"  : $tag.val(setValueByClass($tag, data.QTYEA )); break;
				default    : break;
			}
			
			if (data.QTYBOX) {
				$tag = $('#QTYBOX');
				$('#BOXUOM').val(data.BOXUOM);
				$tag.val(setValueByClass($tag, data.QTYBOX));
				$('#QTYBOX').prop('readonly', false);
			} else {
				$('#QTYBOX').prop('readonly', true);
			}
		}
	}
	
	// 2025.10.22 HL 재고상태 변경 제한 적용 (품질/불용/폐기 3가지 상태인 경우 PDA로 재고상태 변경 불가)
	function saveData(){
		
		if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
			return;
		}
		
		var param = inputList.setRangeParam("paramArea");
		
		if(!$('#LOCAKY').val()) {
			var label = commonLabel.getLabel("STD_LOCAKY", 1);
			commonUtil.msgBox("VALID_required", [label], null, function () {
				$('#BARCOD').focus();
			}); // {0} 은(는) 필수 입력항목입니다.
			return false;
		}
		
		if(!$('#LOTA06_TO').val()) {
			var label = commonLabel.getLabel("STD_LOTA06_TO", 1);
			commonUtil.msgBox("VALID_required", [label], null, function () {
				$('#LOTA06_TO').focus();
			}); // {0} 은(는) 필수 입력항목입니다.
			return false;
		}
		if(!$('#RSNADJ').val()) {
			var label = commonLabel.getLabel("STD_RSNADJ", 1);
			commonUtil.msgBox("VALID_required", [label], null, function () {
				$('#RSNADJ').focus();
			}); // {0} 은(는) 필수 입력항목입니다.
			return false;
		}
		
		param.put("WAREKY", "<%=wareky%>");
		param.put('PLNTKY', $('#LOTA02').val());
		
		if (param.get("LOTA06") == param.get("ALOTA06")) {
			commonUtil.msgBox("INV_M0055", null, null, function () {
				$('#LOTA06_TO').focus();
			}); // 기존값과 동일합니다. 확인해주세요.
			return;
		}

		if ((param.get("LOTA02") == 'P200' || param.get("LOTA02") == 'P300') && $.inArray(param.get("LOTA06"), ["05", "30", "50"]) > -1) {
			commonUtil.msgBox("VALID_M0009", "PDA에서 품질/불용/폐기는 재고상태 변경 불가 합니다.", null, function () {
				$('#LOTA06').focus();
			});
			return;
		}

		var regex = /[^0-9.]/g;
		var AQTADJU = Number(param.get("AQTADJU").replace(regex, ""));
		var  QTSIWH = Number(param.get( "QTSIWH").replace(regex, ""));
		
		param.put('AQTADJU', AQTADJU);
		if (AQTADJU <= 0) {
			commonUtil.msgBox("TASK_M0021", '0', null, function () {
				$('#AQTADJU').focus();
			}); // 작업수량은 {0}보다 커야 됩니다.
			return;
		}
		
		if (AQTADJU > QTSIWH) {
			commonUtil.msgBox("INV_M0072", null, null, function () {
				$('#AQTADJU').focus();
			}); // 재고수량보다 더 많이 조정할 수 없습니다.
			return;
		}
		
		// save data
		var json = netUtil.sendData({
			url : "/wms/inventory/json/savePDSJ01.data",
			param : param,
			successFunction : "playSuccessSound"
		});
		
		if (json && json.data && json.data.SADJKY){
			playSuccessSound();
			commonUtil.msgBox("INV_M0010", json.data.SADJKY, null, function () {
				initTable();
				$('#BARCOD').val("");
				$('#BARCOD').focus("");
			});
			
		} else {
			commonUtil.msgBox("SAP : " + json.data.SAPRES);
		}
	}
	
	function fn_onfocus(objId){
		$("#"+objId).select();
	}
	
	function comboEventDataBindeBefore(comboAtt){
		var param = new DataMap();
		if(comboAtt == "airfw,RSNADJ"){
			param.put("DOCUTY", "402");	
		} else if(comboAtt == "Wms,PLNTKYCOMBO"){
			param.put("WAREKY", '<%=wareky%>');
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
		}
		return param;
	}
</script>
</head>
<body>
	<div class="main_wrap" id="paramArea">
		<div class="tem4_content">
			<div class="select_box" id="bindArea" style="margin-bottom: 48px;">
				<table>
					<colgroup>
						<col width="95px" />
						<col width="*" />
					</colgroup>
					<tbody id="searchArea">
						<tr>
							<th CL='STD_BARCOD'>바코드</th>
							<td colspan="2"><input type="text" class="text" name="BARCOD" id="BARCOD" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'searchLocationList()')" validate="required"/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA06">재고상태</th>
							<td colspan="2"><select id="LOTA06" name="LOTA06" CommonCombo="LOTA06" onchange="onParamChange()"></select></td>
						</tr>
						<tr>
							<th CL="STD_LOTA02">플랜트</th>
							<td colspan="2"><select id="LOTA02" name="LOTA02" Combo="Wms,PLNTKYCOMBO" onchange="onParamChange()"></select></td>
						</tr>
						<tr>
							<th CL="STD_LOCAKY">지번</th>
							<td colspan="2"><input type="text" class="text" name="LOCAKY" id="LOCAKY" readonly/></td>
<!-- 						<td onclick ="searchLocationList()"><button class="bt" style="height: 36px; min-width: 50px;"><p CL='BTN_FINDDATA'/></button></td> -->
						</tr>
					</tbody>
				</table>
				
				<hr />
			<%
				if(mtype.equals("P")){
			%>
		            <jsp:include page="./PDSJ01_PS.jsp" flush="false"/>
			<%
				}else {
			%>
		        	<jsp:include page="./PDSJ01_ELSE.jsp" flush="false"/>
			<% 
				}
			%>   
			</div>
			
			<!-- end table_body -->
			<div class="footer_5" style="position: fixed;">
				<table>
					<tr>
						<td class=f_1 width="50%" onclick="window.top.closePage()">
							<label CL='BTN_CLOSE'></label>
						</td>
						<td class=f_1 width="50%" onclick ="saveData()" >
							<label CL='BTN_COMPLE'></label>
						</td>
					</tr>
				</table>
			</div>
			<!-- end footer_5 -->

		</div>
	</div>
</body>