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

</style>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	$(document).ready(function(){
		initTable();
		
		$('#LOTA14').focus();
	});
	
	var searched = false;
	function initTable() {
		$('#paramArea').find('input[type="hidden"]').remove();
		$('#paramArea').find('input[name!="LOTA14"]').prop('readonly', true).val("");
		$('#LOTA14').focus();
		searched = false;
	}
	
	function playSuccessSound(){
		PlaySound('s');
	}
	
	function getData(reSearch) {
		var param = inputList.setRangeParam("searchArea");
		param.put('WAREKY', '<%=wareky%>');
		
		var json = netUtil.sendData({
			module : "WmsSystem",
			command: "PDIS03",
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
	
	function setTable(data, reSearch) {
		if(!data) {
			commonUtil.msgBox("VALID_M0005"); // 조회된 데이터가 없습니다.
			return;
		}
		
		initTable();
		searched = true;
		
		// 화면 세팅
		for (var key in data) {
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
		
		// 실사완료 데이터인 경우
		if (data.PHYCHK == "V") {
			var $tag = $('#QTYRCV');
			$tag.val(setValueByClass($tag, data.QTYPHY));
		}
		
		getQTYUOM('QTYRCV');
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
			} else {
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
	
	function cancleData () {
		if (!searched) { // 미조회시
			PlaySound('e');
			commonUtil.msgBox("조회 후 실사를 진행해주세요.");
			return;
		}
		
		commonUtil.msgConfirm("IN_M0102", null, function () {// 취소하시겠습니까?
			// cancle data
			var param = inputList.setRangeParam("paramArea");
		
			console.log(param);
		
			var json = netUtil.send({
				url : "/wms/wmsSystem/json/canclePDIS03.data",
				param : param,
				successFunction : "canclePDIS03CallBack"
			});
		});
		
	}
	
	function canclePDIS03CallBack(json, status) {
		console.log(json );
		if(json && json.data){
			PlaySound('s');
			$('#LOTA14').focus();
		} else {
			PlaySound('e');
			commonUtil.msgBox("COMMON_M0465"); // 저장 실패하였습니다.
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
	
	function fn_onfocus(objId){
		$("#"+objId).select();
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
							<th CL='STD_BARCOD'>바코드</th>
							<td colspan="2"><input type="text" class="text" name="LOTA14" id="LOTA14" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'getData()')"/></td>
						</tr>
						<tr>
							<th CL="STD_LOCAKY">지번</th>
							<td colspan="2"><input type="text" class="text" name="LOCAKY" id="LOCAKY" onfocus="fn_onfocus(this.id)" readonly/></td>
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
						</tr>
						<tr>
							<th CL="STD_LOTA01">저장위치</th>
							<td colspan="2">
								<input type="text" class="text" name="ALOTA01" id="ALOTA01" readonly/>
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
						<td class=f_1 width="33%" onclick ="cancleData()" >
							<label CL='BTN_CANCEL'></label>
						</td>
					</tr>
				</table>
			</div>
			<!-- end footer_5 -->

		</div>
	</div>
</body>