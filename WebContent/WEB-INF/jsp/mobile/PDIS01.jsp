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
.tem4_content table th           { padding: 3px !important; }
.tem4_content table td           { padding: 3px !important; }
.number { text-align: right }

.additional { color: red ; display: none; font-size: 0.9rem; }
</style>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	$(document).ready(function(){
		initTable();
		$('#LOCAKY').focus();
		
		$('.number').change(function (e) {
			var value = e.currentTarget.value;
			e.currentTarget.value = setDecimalValue(value, 3);
		})
		
		$('#QTYSOK, #QTYEA').change(function(){
			var regex = /[^0-9.]/g;
			var eaVal  = $('#QTYEA' ).val().replace(regex, '');
			var sokVal = $('#QTYSOK').val().replace(regex, '');
			
			var value = (eaVal * sokVal) / 500;
			
			var $tag = $('#QTYRCV');
			$tag.val(setValueByClass($tag, value));
		});
	});
	
	var searched = false;
	function initTable() {
		$('#paramArea').find('input[type="hidden"]').remove();
		$('#paramArea').find('input[name!="LOCAKY"]').val("");
		$('#LOTA14').focus();
		searched = false;
		
		$('.additional').parent().find('td[class!="additional"]').prop('colspan', '2');
		$('.additional').find('span').text('');
		$('.additional').hide();
	}
	
	function playSuccessSound(){
		PlaySound('s');
	}
	
	// 로케이션 리스트 조회
	function searchLocationList(locaky){
		initTable();
		var param = inputList.setRangeParam("searchArea");
		param.remove('LOCAKY');
		
		param.put('WAREKY', '<%=wareky%>');
		param.put('LOCAKY', locaky);
		
		if (searchedLocData) {
			param.put("AREAKY", searchedLocData.get("AREAKY"));
			param.put("ZONEKY", searchedLocData.get("ZONEKY"));
		}
		
		var json = netUtil.sendData({
			module : "WmsSystem",
			command: "PDIS01LOCMA",
			param  : param
		});
		
		var data = json.data;
		
		// 1건 조회 시
		if (data && data.length == 1) {
			var dataMap = new DataMap();
			dataMap.putObject(data[0]);
			LocPopCallback(dataMap);
		// 다건 조회 시
		} else if (data && data.length > 1) {
			param.put("json", json);
			mobile.linkPopOpen('/mobile/PDLOC_POP.page?showPlntky=true', param);
		// 미조회 시
		} else {
			PlaySound('e');
			commonUtil.msgBox("VALID_M0005", null, null, function () {
				initTable();
				$('#LOCAKY').focus();
			});
		}
	}
	
	var searchedLocData = null;
	
	var plntky = '';
	function LocPopCallback(data) {
		searchedLocData = data;
		
		plntky = data.get('PLNTKY');
		$('#LOCAKY').val(data.get("LOCAKY"));
		$('#LOTA14').focus();
	}
	
	function getData(reSearch) {
		if (validateLocaky()) {
			
			var param = inputList.setRangeParam("searchArea");
			param.put('WAREKY', '<%=wareky%>');
			param.put('PLNTKY', plntky);
			
			var json = netUtil.sendData({
				module : "WmsSystem",
				command: "PDIS01",
				param  : param
			});
			
			// 1건 조회 시
			if (json.data && json.data.length == 1) {
				setTable(json.data[0], reSearch);
			// 다건 조회 시
			} else if (json.data && json.data.length > 1) {
				var finded = json.data.find(data => data.PHYCHK == "V");
				setTable(finded, reSearch);
			// 미조회 시
			} else {
				PlaySound('e');
				commonUtil.msgBox("VALID_M0005", null, null, function () {
					$("#LOTA14").focus();
					initTable();
				}); // 조회된 데이터가 없습니다.
			}
		}
	}
	
	function validateLocaky() {
		var LOCAKY = $('#LOCAKY').val();
		if (!LOCAKY) {
			PlaySound('e');
			var label = commonLabel.getLabel("STD_LOCAKY", 1);
			commonUtil.msgBox("VALID_required",[label], null, function () {
				$('#LOCAKY').focus();
			}); // {0} 은(는) 필수 입력항목입니다.
			return false;
		}
		
		var param = inputList.setRangeParam("searchArea");
		param.put('WAREKY', '<%=wareky%>');
		param.put('PLNTKY', plntky);
		param.put("ALOCAKY", param.get('LOCAKY'));
		
		var json = netUtil.sendData({
			module : "WmsTask",
			command : "LOCAKYval2",
			sendType : "map",
			param : param
		});
		
		if (!json || !json.data) {
			PlaySound('e');
			commonUtil.msgBox('작업대상 로케이션이 유효하지 않습니다.', null, null, function () {
				$("#LOCAKY").focus();
			});
			return false;
		}
		
		var PLNTKY = json.data["PLNTKY"];
		var STATUS = json.data["STATUS"];
		
		if (!PLNTKY ||
			!PLNTKY.trim() ||
			STATUS != '00'
		) {
			PlaySound('e');
			commonUtil.msgBox('작업대상 로케이션이 유효하지 않습니다.', null, null, function () {
				$("#LOCAKY").focus();
			});
			return false;
		}
		
		return true;
	}
	
	function setTable(data, reSearch) {
		if(!data) {
			commonUtil.msgBox("VALID_M0005"); // 조회된 데이터가 없습니다.
			return;
		}
		
		initTable();
		searched = true;
		
		/* 2022.09.21 거점이 1100이면서
			플랜트가 P200이거나 P300인 경우 서로 혼적 할 수 있게 변경
		*/ 
		var similarPlnt = data.WAREKY == '1100' &&
						(
							(data.LOTA02 == 'P200' && data.ALOTA02 == 'P300') ||
							(data.LOTA02 == 'P300' && data.ALOTA02 == 'P200')
						);
		if (similarPlnt) {
			data.ALOTA02 = data.LOTA02;
		}
		
		// 화면 세팅
		for (var key in data) {
			if (key == 'LOCAKY') { // 지번인 경우 CONTINUE
				continue;
			}
			
			var value = data[key];
			
			var  $inputTag = $('#bindArea').find( 'input[name=' + key + ']');
			var $selectTag = $('#bindArea').find('select[name=' + key + ']');
			var   $spanTag = $('#bindArea').find(  'span[name=' + key + ']');
			
			// input 태그가 존재하는 경우 값 세팅
			if ($inputTag.length > 0) {
				value = setValueByClass($inputTag, value);
				$inputTag.val(value);
			// select 태그가 존재하는 경우 값 세팅
			} else if ($selectTag.length > 0) {
				value = setValueByClass($selectTag, value);
				$selectTag.val(value);
			// span 태그가 존재하는 경우 값 세팅
			} else if ($spanTag.length > 0) {
				value = setValueByClass($spanTag, value);
				$spanTag.text(value);
			// 없는 경우 신규 hidden 태그 생성
			} else {
				var hiddenInput = '<input type="hidden" name="' + key + '" value="' + value + '"/>';
				$('#paramArea').append(hiddenInput);
			}
		}
		
		if (data.PHYCHK == "V") {
			var $tag = $('#QTYRCV');
			$tag.val(setValueByClass($tag, data.QTYPHY));
		}
		
		getQTYUOM('QTYRCV');
		
		// 재조회인 경우 메시지 출력 X
		if (reSearch) {
			return;
		}
		
		// 실사완료 데이터인 경우
		if (data.PHYCHK == "V") {
			PlaySound('e');
			
			commonUtil.msgBox('이미 실사가 완료된 재고입니다.', null, null, function () {
				$("#LOTA14").focus();
			});
			
// 			alert("이미 실사가 완료된 재고입니다.");
// 			$('#LOTA14').focus();
			return;
		}
		
		if (!similarPlnt){
			// 변경될 플랜트에서 관리되는 품목인지 확인
			var param = new DataMap();
			param.put('OWNRKY', '<%=ownrky%>');
			param.put('WAREKY' , data.WAREKY );
			param.put('SKUKEY' , data.SKUKEY );
			param.put('ALOTA02', data.ALOTA02);
			
			var json = netUtil.sendData({
				module : "WmsInventory",
				command : "SKUPLT_VALIDATE",
				sendType : "map",
				param : param
			});
			
			if(json && !json.data) {
				PlaySound('e');
				commonUtil.msgBox("VALID_M2099", [param.get("SKUKEY"), param.get("ALOTA02")], null, function () {
					$("#LOTA14").focus();
				}); // 해당 품목({0})은 플랜트 {1}의 관리대상품목이 아닙니다.
				return;
			}
		}
		
		var msg = '';
		
		// 플랜트, 저장위치가 다른 경우
		if (data.LOTA01 != data.ALOTA01 ||
			data.LOTA02 != data.ALOTA02
		) {
			$('.additional').parent().find('td[class!="additional"]').prop('colspan', '1');
			$('.additional').show();
			
			msg += "라벨 생성 시점의 플랜트나 저장위치와\n" +
			       "실물의 위치가 변경된 바코드입니다.\n" +
			       "수량 및 위치, 라벨정보를 확인 후\n" +
			       "완료버튼을 눌러주십시오.";
		} else {
			$('.additional').parent().find('td[class!="additional"]').prop('colspan', '2');
			$('.additional').hide();
		}
		
		if (data.QTPUOM == '0') {               // 기본단위수량(PAL)이 존재하지 않는 경우
			if(msg) { msg += "\n\n"; }
			msg += "팔레트당 수량 정보가 없어\n" +
			       "완팔레트 여부를 확인 할 수 없으므로\n" +
			       "수량 확인 후 완료버튼을 눌러주십시오.";
		} else if (data.QTPUOM != data.QTYRCV) { // 기본단위수량(PAL), 입고수량이 다른 경우
			if(msg) { msg += "\n\n"; }
			msg += "완팔레트 라벨이 아닙니다.\n"+
			       "수량 확인 후 완료버튼을 눌러주십시오.";
		}
		
		if (msg) {
			PlaySound('e');
			commonUtil.msgBox(msg);
			return;
		}
		
		// 완팔레트인 경우 autoSave
		if($('#INTACT').prop('checked')) {
			saveData();
		} else {
			PlaySound('s');
		}
	}
	
	function getQTYUOM(id) {
		if(!searched) {
			return;
		}
		
		var param = inputList.setRangeParam("searchArea");
		param.put('WAREKY', '<%=wareky%>');
		
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
			
			var $tag = $('#QTYRCV');
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
				
				if(data.UOMKEY == "R"){
					$('#REAM_ROW').show();
					$('#BOX_ROW').hide();
					$('#QTYSOK').focus();
				}else{
					$('#REAM_ROW').hide();
					$('#BOX_ROW').show();
				}
			}
		}
	}
	
	function setValueByClass($tag, value) {
		var className = $tag[0].className;
		
		// class에 number가 포함되어 있는 데이터 가공
		var isNumberType = className && className.indexOf(' ') > 0 && className.split(' ').includes('number');
		var isDateType   = className && className.indexOf(' ') > 0 && className.split(' ').includes('date'  );
		
		if (isNumberType) {
			value = setDecimalValue(value, 3);
		} else if (isDateType) {
			value = setDateValue(value);
		}
		
		return value;
	}
	
	function setDecimalValue(str, decimal) {
		str = new String(str);
		var regex = /[^0-9.]/g;
		var number = Number(str.replace(regex, ""));
		number = number ? number : 0;
		return number.toLocaleString(undefined, { minimumFractionDigits: decimal });
	}
	
	function setDateValue(str) {
		try {
			var value = new String(str);
			return value.length != 8 ? value : value.substr(0, 4) + '.' + value.substr(4, 2) + '.' + value.substr(6, 2);
		} catch (e) {
			console.warn(e);
			return str;
		}
	}
	
	function saveData(){
		if (!searched) { // 미조회시
			PlaySound('e');
			commonUtil.msgBox("조회 후 실사를 진행해주세요.");
			return;
		}
		
		if(!$('#LOCAKY').val()) {
			PlaySound('e');
			var label = commonLabel.getLabel("STD_LOCAKY", 1);
			commonUtil.msgBox("VALID_required",[label], null, function () {
				$('#LOCAKY').focus();
			}); // {0} 은(는) 필수 입력항목입니다.
			return false;
		}
		
		// 1. validation
		if (validateLocaky()){
// 			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){ // 저장하시겠습니까?
// 				return;
// 			}

			var param = inputList.setRangeParam("paramArea");
			param.put("WAREKY", "<%=wareky%>");
			param.put('OWNRKY', '<%=ownrky%>');
			param.put('PLNTKY', plntky);
			
			// 변경될 플랜트에서 관리되는 품목인지 확인 
			var json = netUtil.send({
				module : "WmsInventory",
				command : "SKUPLT_VALIDATE",
				sendType : "map",
				param : param,
				successFunction : "skupltValidateCallBack"
			});
		}
	}
	
	function skupltValidateCallBack(json, status) {
		if(json && !json.data) {
			PlaySound('e');

			commonUtil.msgBox("VALID_M2099", [json.map.SKUKEY, json.map.ALOTA02], null, function () {
				$("#LOTA14").focus();
			}); // 해당 품목({0})은 플랜트 {1}의 관리대상품목이 아닙니다.
			
			return;
		}
		
		$('.date'  ).each(function (i, data) {
			data.value = data.value.replaceAll('.', ''); // set unformatted
		})
		$('.number').each(function (i, data) {
			data.value = data.value.replaceAll(',', ''); // set unformatted
		})
		
		var param = inputList.setRangeParam("paramArea");
		param.put("WAREKY", "<%=wareky%>");
		param.put('OWNRKY', '<%=ownrky%>');
		param.put('PLNTKY', plntky);
			
		$('.additional').find('span').each(function(i, tag) {
			param.put($(tag).attr('name'), $(tag).text());
		})
		
		if (!param.get("LOTA14")) {
			PlaySound('e');
			var label = commonLabel.getLabel("STD_BARCOD", 1);
			commonUtil.msgBox("VALID_required", [label], null, function () {
				$('#LOTA14').focus();
			}); // {0} 은(는) 필수 입력항목입니다.
			return false;
		}
		
		if (param.get('PHYCHK') == "V") {
			PlaySound('e');
			commonUtil.msgBox("이미 실사가 완료된 재고입니다.", null, null, function () {
				$('#LOTA14').focus();
			});
			return;
		}
		
		// save data
		var json = netUtil.send({
			url : "/wms/wmsSystem/json/savePDIS01.data",
			param : param,
			successFunction : "savePDIS01CallBack"
		});
		
	}
	
	function savePDIS01CallBack(json, status) {
		if(json && json.data){
			PlaySound('s');
// 			commonUtil.msgBox(configData.MSG_SYSTEM_SAVEOK); // 저장 완료되었습니다.
			getData(true); // 메시지 alert하지 않고 재조회
			$('#LOTA14').focus();
		} else {
			PlaySound('e');
			commonUtil.msgBox("COMMON_M0465"); // 저장 실패하였습니다.
		}
	}
	
	function fn_onfocus(objId){
		$("#"+objId).select();
	}
	
	// 재고실사 목록 팝업 오픈
	function openListPop(){
		var locaky = $.trim($('#LOCAKY').val());
		if(locaky == ""){
			commonUtil.msgBox("VALID_M0404", commonUtil.getMsg("STD_LOCAKY"), null, null, function(){ // 지번을 입력해 주세요.
				$('#LOCAKY').focus();
			});
		}else{
			var param = inputList.setRangeParam("searchArea");
			param.remove('LOCAKY');
			
			param.put('WAREKY', '<%=wareky%>');
			param.put('LOCAKY', locaky);
			
			mobile.linkPopOpen('/mobile/PDIS01_POP.page', param);
		}
		
	}
</script>
</head>
<body>
	<div class="main_wrap" id="paramArea">
		<div class="tem4_content">
			<div class="select_box" id="bindArea" style="margin-bottom: 48px;">
				<table>
					<colgroup>
						<col width="30%" />
						<col width="50%" />
						<col width="20%" />
					</colgroup>
					<tbody id="searchArea">
						<tr>
							<th CL="STD_LOCAKY">지번</th>
							<td><input type="text" class="text" name="LOCAKY" id="LOCAKY" onfocus="fn_onfocus(this.id)" onchange="searchLocationList(this.value)"/></td>
							<td onclick="searchLocationList('')"><button class="bt" style="height: 32px; min-width: 50px;"><p CL='BTN_FINDDATA'/></button></td>
						</tr>
						<tr>
							<th CL='STD_BARCOD'>바코드</th>
							<td colspan="2"><input type="text" class="text" name="LOTA14" id="LOTA14" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'getData()')"/></td>
						</tr>
						<tr>
							<th CL='STD_INTACT'>완팔레트</th>
							<td colspan="2"><input type="checkbox" class="text" name="INTACT" id="INTACT"/></td><!-- 완팔레트여부 -->
						</tr>
						<tr id="BOX_ROW">
							<th CL="STD_PQTYBOX">박스단위수량</th>
							<td><input type="text" class="text number" name="QTYBOX" id="QTYBOX" onfocus="fn_onfocus(this.id)" onchange="getQTYUOM(this.id);"/></td>
							<td><input type="text" class="text"        name="BOXUOM" id="BOXUOM" readonly/></td>
						</tr>
						<tr id="REAM_ROW" style="display:none;">
							<th >수량계산</th>
							<td colspan="2">
							     (
							     <select style="width:50px;" name="QTYEA" id="QTYEA">
							         <option value="50">50</option>
							         <option value="100">100</option>
							         <option value="125">125</option>
							     </select>
							     X
							     <input type="text" class="text number" name="QTYSOK" id="QTYSOK" onfocus="fn_onfocus(this.id)" style="width:100px;" />
							     ) / 500
							</td>
						</tr>
						<tr>
							<th CL="STD_SUOMQTY">기본단위수량</th>
							<td><input type="text" class="text number" name="QTYRCV" id="QTYRCV" onfocus="fn_onfocus(this.id)" onchange="getQTYUOM(this.id);"/></td>
							<td><input type="text" class="text"        name="UOMKEY" id="UOMKEY" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_SKUKEY">품목코드</th>
							<td colspan="2"><input type="text" class="text" name="SKUKEY" id="SKUKEY" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_DESC01">품목명</th>
							<td colspan="2"><input type="text" class="text" name="DESC01" id="DESC01" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA02">플랜트</th>
							<td colspan="2">
								<input type="text" class="text" name="ALOTA02" id="ALOTA02" readonly/>
							</td>
							<td class="additional">
								<span name="LOTA02"></span>
							</td>
						</tr>
						<tr>
							<th CL="STD_LOTA01">저장위치</th>
							<td colspan="2">
								<input type="text" class="text" name="ALOTA01" id="ALOTA01" readonly/>
							</td>
							<td class="additional">
								<span name="LOTA01"></span>
							</td>
						</tr>
						<tr>
							<th CL="STD_LOTA04">호기</th>
							<td colspan="2"><input type="text" class="text" name="LOTA04" id="LOTA04" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA06">재고상태</th>
							<td colspan="2"><input type="text" class="text" name="LOTA06NM" id="LOTA06NM" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA08">국가</th>
							<td colspan="2"><input type="text" class="text" name="LOTA08" id="LOTA08" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA07">MTO판매번호</th>
							<td colspan="2"><input type="text" class="text" name="LOTA07" id="LOTA07" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA09NM">MTO납품처명</th>
							<td colspan="2"><input type="text" class="text" name="LOTA09NM" id="LOTA09NM" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA11">제조일자</th>
							<td colspan="2"><input type="text" class="text date" name="LOTA11" id="LOTA11" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA12">입고일자</th>
							<td colspan="2"><input type="text" class="text date" name="LOTA12" id="LOTA12" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA13">유통기한</th>
							<td colspan="2"><input type="text" class="text date" name="LOTA13" id="LOTA13" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA15">LOT번호</th>
							<td colspan="2"><input type="text" class="text" name="LOTA15" id="LOTA15" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA16">평량</th>
							<td colspan="2"><input type="text" class="text number" name="LOTA16" id="LOTA16" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA17">컨테이너유형</th>
							<td colspan="2"><input type="text" class="text" name="LOTA17" id="LOTA17" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA03">포장타입</th>
							<td colspan="2"><input type="text" class="text" name="LOTA03" id="LOTA03" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA21">지관</th>
							<td colspan="2"><input type="text" class="text" name="LOTA21" id="LOTA21" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA23">생산자ID</th>
							<td colspan="2"><input type="text" class="text" name="LOTA23" id="LOTA23" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA24">생산자명</th>
							<td colspan="2"><input type="text" class="text" name="LOTA24" id="LOTA24" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA41">컨테이너NO</th>
							<td colspan="2"><input type="text" class="text" name="LOTA41" id="LOTA41" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_LOTA42">공급업체코드(수입밀롤)</th>
							<td colspan="2"><input type="text" class="text" name="LOTA42" id="LOTA42" readonly/></td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<!-- end table_body -->
			<div class="footer_5" style="position: fixed;">
				<table>
					<tr>
						<td class=f_1 width="33%" onclick="window.top.closePage()">
							<label CL='BTN_CLOSE'></label>
						</td>
						<td class=f_1 width="34%" onclick="openListPop()">
							<label CL='BTN_PHYLIST'></label>
						</td>
						<td class=f_1 width="33%" onclick ="saveData()" >
							<label CL='BTN_COMPLE'></label>
						</td>
					</tr>
				</table>
			</div>
			<!-- end footer_5 -->

		</div>
	</div>
</body>