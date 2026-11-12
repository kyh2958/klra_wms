<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<style type="text/css">
 .content_wrap{background: none;}
 #tabs1-1{height: calc(100% - 90px);}
 .search_sub{height: 30px;}
 .content_layout .search_sub dl{padding:0 0 10px;}
 .content_layout .search_sub dd{margin-left:unset;text-align:unset;}
</style>
<script type="text/javascript">
var curRow = "";

	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList1",
			editable : true,
			module : "Erpif",
			colorType : true,
			command : "ITFMA",
			colHoldType: true,
			colHold: "IFKNM"
		});

		// 검색조건 - 인터페이스 ID
		var $selIFID = $('select[name="IFID"]');
		inputList.setMultiComboSelectAll($("IFID"),true);
		$selIFID.multipleSelect('checkAll');
		// 검색조건 - IF테이블
		var $selIFTBL = $('select[name="IFTBL"]');
		inputList.setMultiComboSelectAll($("IFTBL"),true);
		$selIFTBL.multipleSelect('checkAll');
		// 검색조건 - 송신시스템
		var $selSYSFR = $('select[name="SYSFR"]');
		inputList.setMultiComboSelectAll($("SYSFR"),true);
		$selSYSFR.multipleSelect('checkAll');
		// 검색조건 - 수신시스템
		var $selSYSTO = $('select[name="SYSTO"]');
		inputList.setMultiComboSelectAll($("SYSTO"),true);
		$selSYSTO.multipleSelect('checkAll');

		// 콤보박스 사이즈 강제 조정
// 		$('[ms-choice="IFID"]').parent().css("width","400px");
// 		$('[ms-choice="IFID"]').parent().css("width","400px");
		$('[ms-drop="IFID"]').css("width","400px");
		
// 		$('[ms-choice="IFTBL"]').parent().css("width","300px");
// 		$('[ms-choice="IFTBL"]').parent().css("width","300px");
		$('[ms-drop="IFTBL"]').css("width","300px");
		
		// 조회
		//searchList();
	});
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			// 조회
			searchList();
		} else if(btnName == "Execute") {
			// 프로시저 실행
			fn_call_proc();
		} else if(btnName == "Detail") {
			// 상세조회
			if(gridList.getGridDataCount("gridList1") > 0) {
				var rowData = gridList.getRowData("gridList1", gridList.getFocusRowNum("gridList1"));
				rowData.put("CREDAT", $('#CREDAT').val());
				page.linkPopOpen("/wms/erpif/IF00POPD.page", rowData);
			} else {
				commonUtil.msgBox("VALID_M0005"); // 조회된 데이터가 없습니다.
			}
		} else if(btnName == "Excel") {
			// 엑셀업로드
			if(gridList.getGridDataCount("gridList1") > 0) {
				var rowData = gridList.getRowData("gridList1", gridList.getFocusRowNum("gridList1"));
				if(rowData.get("SYSTO") == "WMS") {
					page.linkPopOpen("/wms/erpif/IF00POP.page", rowData);
				} else {
					commonUtil.msgBox("ITF_M0028"); // 수신시스템이 WMS인 경우만 엑셀업로드 할 수 있습니다.
				}
				
			} else {
				commonUtil.msgBox("VALID_M0005"); // 조회된 데이터가 없습니다.
			}
		} else if(btnName == "Receive") {
			// 데이터수신
			if(gridList.getGridDataCount("gridList1") > 0) {
				var rowData = gridList.getRowData("gridList1", gridList.getFocusRowNum("gridList1"));
				
				if(    rowData.get("IFID") == "WMS_IF_301"
				    || rowData.get("IFID") == "WMS_IFC301"
					|| rowData.get("IFID") == "WMS_IFB301"
// 					|| rowData.get("IFID") == "WMS_IFS301" 실시간 수신은 허용
				    || rowData.get("IFID") == "WMS_IF_302") {
					commonUtil.msgBox("출고예정정보는 수동으로 데이터수신 불가합니다.");
					return;
				}
				
				if(rowData.get("IFSTAT") == "ON") {
					alert("시작[ON] 상태일 경우에는 수동으로 데이터수신 불가합니다.");
				} else {
					if(rowData.get("SYSFR") == "SAP" || rowData.get("SYSFR") == "AMANO") {
		 				page.linkPopOpen("/wms/erpif/IF00POPR.page", rowData);
					} else {
						commonUtil.msgBox("ITF_M0026"); // 송신시스템이 SAP인 경우만 수신요청 할 수 있습니다.
					}
				}
			} else {
				commonUtil.msgBox("VALID_M0005"); // 조회된 데이터가 없습니다.
			}
		} else if(btnName == "Send") {
			// 실적전송
			if(gridList.getGridDataCount("gridList1") > 0) {
				var rowData = gridList.getRowData("gridList1", gridList.getFocusRowNum("gridList1"));
				if(rowData.get("SYSFR") == "WMS") {
					page.linkPopOpen("/wms/erpif/IF00POPS.page", rowData);
				} else {
					commonUtil.msgBox("ITF_M0034"); // 송신시스템이 WMS인 경우만 데이터송신 할 수 있습니다.
				}
				
			} else {
				commonUtil.msgBox("VALID_M0005"); // 조회된 데이터가 없습니다.
			}
		} else if(btnName == "Ifon") {
			// 시작
			controlIf("ON");
		} else if(btnName == "Ifoff") {
			// 중지
			controlIf("OFF");
		} else if(btnName == "Save") {
			// 저장
			save();
		}
	}
	
	function searchList(){
		if(validate.check("searchArea")){
			var ifid = $('#IFID').val();
			var iftbl = $('#IFTBL').val();
			var param = inputList.setRangeDataParam("searchArea");

			// 멀티콤보박스 전체 선택시 SQL상 조건절 OR 형태로 전체 추가되지 않도록 RANGE 제거
			if(inputList.getMultiComboValue($("#IFID")).length == $('#IFID > option').length) {param.get("RANGE_DATA_PARAM").remove("IFID_Single")};
			if(inputList.getMultiComboValue($("#IFTBL")).length == $('#IFTBL > option').length) {param.get("RANGE_DATA_PARAM").remove("IFTBL_Single")};
			if(inputList.getMultiComboValue($("#SYSFR")).length == $('#SYSFR > option').length) {param.get("RANGE_DATA_PARAM").remove("SYSFR_Single")};
			if(inputList.getMultiComboValue($("#SYSTO")).length == $('#SYSTO > option').length) {param.get("RANGE_DATA_PARAM").remove("SYSTO_Single")};

			gridList.gridList({
				id : "gridList1",
				param : param
			});
		}
		
		$("#tab1").trigger('click'); 
	}
	
	function controlIf(stat){
		// 시작 및 중지
		var list = gridList.getSelectData("gridList1");
		if(list.length == 0) {
			commonUtil.msgBox("VALID_M0006"); // 선택된 데이터가 없습니다.
			return;
		}
		
		for(var i=0; i < list.length; i++) {
			var ifstat = list[i].get("IFSTAT");
			var ifid = list[i].get("IFID");
			
			if(ifid == "WMS_IF_900") {
				commonUtil.msgBox("SAP 재고 수신은 수동으로만 수신가능합니다.");
				return;
			}
			
			if(ifstat == "REAL" || ifstat == "SUB" || ifstat == "BATCH") {
				commonUtil.msgBox("ITF_M0027"); // 실시간 및 SUB통신은 시작,중지 할 수 없습니다.
				return;
			}
		}
		
		var param = new DataMap();
		param.put("list", list);
		param.put("IFSTAT", stat);
		
		var json = netUtil.sendData({
			url : "/wms/erpif/json/controlIF.data",
			param : param
		});
		
		// 조회
		searchList();
	}

	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList1"){
			var list = gridList.getGridData(gridId);
			for(var i=0; i < list.length; i++) {
				var ifid = list[i].get("IFID");
				var ifstat = list[i].get("IFSTAT");
				var sysfr = list[i].get("SYSFR");
				var systo = list[i].get("SYSTO");
				
				if(ifstat == "REAL" || ifstat == "SUB") {
					gridList.setRowReadOnly(gridId, i, true, ["POLLTY","POLLTM"]);
				}
				
				if(sysfr == "SAP" || systo == "SAP") {
					gridList.setRowReadOnly(gridId, i, false, ["RFCFN","RFCTBL","RFCP1","RFCP2"]);
				} else {
					gridList.setRowReadOnly(gridId, i, true, ["RFCFN","RFCTBL","RFCP1","RFCP2"]);
				}

				if(sysfr == "WMS" && systo == "SAP") {
					gridList.setRowReadOnly(gridId, i, false, ["RFCP1"]);
				} else {
					gridList.setRowReadOnly(gridId, i, true, ["RFCP1"]);
				}

				if(sysfr == "KLOGI" || sysfr == "MES" || sysfr == "PTL" || systo == "MES" || systo == "PTL") {
					gridList.setRowReadOnly(gridId, i, false, ["RFCFN"]);
				}
				
				// 데이터수신범위
				if(!(ifid == "WMS_IF_101" || ifid == "WMS_IF_103"
				  || ifid == "WMS_IF_104" || ifid == "WMS_IF_108" || ifid == "WMS_IF_109" || ifid == "WMS_IF_110")) {
					gridList.setRowReadOnly(gridId, i, true, ["RFCP2"]);
				}
				// 품목의 경우 전체는 대량데이터로 초기 셋팅이후에는 못받도록 설정
				if(ifid == "WMS_IF_101" ) {
					gridList.setRowReadOnly(gridId, i, true, ["RFCP2"]);
				}
				
				// 우선순위
				if(ifid == "WMS_IF_102") {
					gridList.setRowReadOnly(gridId, i, true, ["PRORTY"]);
					gridList.setRowReadOnly(gridId, i, true, ["RFCFN"]);
				}
			}
			
			// 기존 위치 포커스
			if(curRow == "") {
				gridListEventRowClick(gridId, 0);
			} else {
				var ifid = gridList.getColData(gridId, curRow, "IFID");
				var len = gridList.getGridData(gridId).length;
	
				gridList.setColFocus(gridId, len-1, "IFID");
				
				for(var i=0; i < len; i++) {
					if(ifid == gridList.getColData(gridId, i, "IFID")) {
						setTimeout(function(){
							gridList.setColFocus(gridId, i, "IFID");
						}, 1);
						return;
					}
				}
			}
		}
	}
	
	function fn_call_proc(){
		// 프로시저, 서비스 실행

		if(!commonUtil.msgConfirm("ITF_M0040")){ // 실행 하시겠습니까?
			return;
		}
		
		var param = new DataMap();

		var chkifid = gridList.getGridData("gridList1")[curRow].get("IFID");
		param.put("IFID", chkifid);

		// 로딩바 제어 start--------------------------------------------- 
		loadingOpen();
		setTimeout(function(){
		// 로딩바 제어 start--------------------------------------------- 
			
			var json = netUtil.sendData({
				url : "/rfc/json/rfcCall.data",
				param : param
			});
	
			if(json && json.data) {
// 				commonUtil.msgBox("ITF_M0039"); // 실행 완료했습니다.
//				리턴메시지가 없음
			}
			
			commonUtil.msgBox("ITF_M0039"); // 실행 완료했습니다.
			
		// 로딩바 제어 end--------------------------------------------- 
		loadingClose();
		}, 1);
		// 로딩바 제어 end--------------------------------------------- 
		
		// 재조회
		searchList();
	}
	
	function save(){
		var list = gridList.getSelectData("gridList1");
		if(list.length == 0) {
			commonUtil.msgBox("VALID_M0006"); // 선택된 데이터가 없습니다.
			return;
		}
		var chkPOLLTY;
		var chkPOLLTM;
		var chkPRORTY;
		for(var i=0; i < list.length; i++) {
			chkPOLLTY = list[i].get("POLLTY");
			if(list[i].get("IFSTAT") != "REAL" && list[i].get("IFSTAT") != "SUB" && chkPOLLTY.trim() == "") {
				commonUtil.msgBox("OUT_M0020", ["실행주기"]); // {0} 은(는) 필수 입력사항입니다.
				return;
			}

			chkPOLLTM = list[i].get("POLLTM");
			if(list[i].get("IFSTAT") != "REAL" && list[i].get("IFSTAT") != "SUB" && chkPOLLTM.trim() == "") {
				commonUtil.msgBox("OUT_M0020", ["실행시간"]); // {0} 은(는) 필수 입력사항입니다.
				return;
			}
			if(list[i].get("IFSTAT") != "REAL" && list[i].get("IFSTAT") != "SUB" && chkPOLLTM.trim() < 0) {
				setTimeout(function(){
				gridList.setColFocus("gridList1", i, "POLLTM");
				}, 1);
				commonUtil.msgBox("VALID_gt", ["0"]); // {0}보다 큰 값이어야 합니다.
				return;
			}

			chkPRORTY = list[i].get("PRORTY");
			if(list[i].get("IFSTAT") != "SUB" && chkPRORTY.trim() == "") {
				commonUtil.msgBox("OUT_M0020", ["우선순위"]); // {0} 은(는) 필수 입력사항입니다.
				return;
			}
		}
		
		var param = new DataMap();
		param.put("list", list);
		
		var json = netUtil.sendData({
			url : "/wms/erpif/json/saveIF.data",
			param : param
		});
		
		if(json && json.data) {
			commonUtil.msgBox("VALID_M0001"); // 저장이 성공하였습니다.

			searchList();
		}
	}
	
	function comboEventDataBindeBefore(comboAtt){
	}
	
	function gridListEventRowDblclick(gridId, rowNum){
		if(gridId == "gridList1"){
			var chkTBL = gridList.getGridData("gridList1")[rowNum].get("IFTBL");
			
			if(chkTBL != " ") {
				commonBtnClick("Detail");
			}
		}
	}
	
	function gridListEventRowClick(gridId, rowNum){
		if(gridId == "gridList1"){
			// 재조회시 포커스처리
			curRow = rowNum;
			
			// 체크사항 START (완료후 지울것)
// 			if(gridList.getGridData("gridList1")[rowNum].get("IFID") == "WMS_IF_223") {
// 				alert("[협의후 진행] 인터페이스 상세항목중 MES에서 LOT정보 보내줄수 있는지 확인 필요");
// 			}
// 			if(gridList.getGridData("gridList1")[rowNum].get("IFID") == "WMS_IF_323") {
// 				alert("[삭제예정] WMS_IF_317 생산투입불출실적정보 통합");
// 			}
			// 체크사항 END 
			
			// 시작,중지 버튼제어
			if(gridList.getGridData("gridList1")[rowNum].get("IFSTAT") == "REAL"
			|| gridList.getGridData("gridList1")[rowNum].get("IFSTAT") == "SUB") {
				$('#btn_ifstat').css('visibility','hidden');
				$('#btn_ifstop').css('visibility','hidden');
			} else {
				$('#btn_ifstat').css('visibility','visible');
				$('#btn_ifstop').css('visibility','visible');
			}
			
			var chkifid = gridList.getGridData("gridList1")[rowNum].get("IFID");
			var chkTBL = gridList.getGridData("gridList1")[rowNum].get("IFTBL");
			var chkCOMTYP = gridList.getGridData("gridList1")[rowNum].get("COMTYP");
			
			// 상세조회 버튼제어
			if(chkTBL != " ") {
				$('#btn_detail').css('visibility','visible');
			} else {
				$('#btn_detail').css('visibility','hidden');
			}
			
			// 실행버튼 제어
			if(chkCOMTYP == "PROC" || chkCOMTYP == "SERVICE") {
				$('#btn_exe').css('visibility','visible');
			} else {
				$('#btn_exe').css('visibility','hidden');
			}

			// 엑셀업로드 버튼제어
			if(gridList.getGridData("gridList1")[rowNum].get("SYSTO") == "WMS"
			&& chkTBL != " "
			&& gridList.getGridData("gridList1")[rowNum].get("COMTYP") != "PROC"
			&& chkifid != "WMS_IF_209213") {
				$('#btn_excelup').css('visibility','visible');
				$('#btn_excelup').css('visibility','visible');
			} else {
				$('#btn_excelup').css('visibility','hidden');
				$('#btn_excelup').css('visibility','hidden');
			}
			
			// 데이터수신 버튼제어
			if((gridList.getGridData("gridList1")[rowNum].get("SYSFR") == "SAP" && chkifid != "WMS_IF_102") || gridList.getGridData("gridList1")[rowNum].get("SYSFR") == "AMANO") {
				$('#btn_receive').css('visibility','visible');
				$('#btn_receive').css('visibility','visible');
			} else {
				$('#btn_receive').css('visibility','hidden');
				$('#btn_receive').css('visibility','hidden');
			}

			// 데이터송신 버튼제어
			if(gridList.getGridData("gridList1")[rowNum].get("SYSFR") == "WMS" && chkTBL != " ") {
				$('#btn_send').css('visibility','visible');
				$('#btn_send').css('visibility','visible');
			} else {
				$('#btn_send').css('visibility','hidden');
				$('#btn_send').css('visibility','hidden');
			}
		}
	}

	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridList.getColData(gridId, rowNum, "IFID") == "WMS_IF_101" && colName == "RFCFN") {
			// 품목정보의 RFCFN 변경시 WMS_IF_102의 RFCFN도 동일하게 변경
			var rows = gridList.getGridDataCount(gridId);
			for(var i=0; i < rows; i++) {
				if(gridList.getColData(gridId, i, "IFID") == "WMS_IF_102") {
					gridList.setColValue(gridId, i, colName, colValue);
// 					alert(gridList.getColData(gridId, i, "rowCheck"));
					gridList.setRowCheck(gridId, i, "rowCheck", true);
				}
			}
		}
	}
	
	function gridListRowColorChange(gridId, rowNum, rowData){
		if(gridId == "gridList1"){			
			if(rowData.get("LASTING") != "0" || rowData.get("LASTERR") != "0"){
				return configData.GRID_COLOR_BG_RED_CLASS;
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
					<input type="button" CB="Search SEARCH BTN_DISPLAY" />
					<input type="button" CB="Execute APPLY BTN_EXECUTE" id="btn_exe"/>
					<input type="button" CB="Detail SEARCH BTN_DISDTL" id="btn_detail"/>
					<input type="button" CB="Excel EXCEL_UP BTN_EXCELUP" id="btn_excelup"/>
					<input type="button" CB="Receive APPLY BTN_RECEIVE" id="btn_receive"/>
<!-- 					<input type="button" CB="Send APPLY BTN_SEND" id="btn_send"/> -->
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap">
					<dl>
						<dt CL="STD_IFID">인터페이스ID</dt>
						<dd>
							<select Combo="Erpif,IFCOMBO" style="width:400px;" class="input" name="IFID" id="IFID" ComboType="MS" validate="required"></select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_IFTBL">IF테이블</dt>
						<dd>
							<select Combo="Erpif,IFTABLE" style="width:400px;" class="input" name="IFTBL" id="IFTBL" ComboType="MS"></select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_SYSFR">송신시스템</dt>
						<dd>
							<select Combo="Erpif,IFSYSFR" style="width:200px;" class="input" name="SYSFR" id="SYSFR" ComboType="MS"></select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_SYSTO">수신시스템</dt>
						<dd>
							<select Combo="Erpif,IFSYSTO" style="width:200px;" class="input" name="SYSTO" id="SYSTO" ComboType="MS"></select>
						</dd>
					</dl>
					<dl>
						<dt CL="ITF_COMMDT">통신기준일자</dt>
						<dd>
							<input type="text" name="CREDAT" id="CREDAT" UIFormat="C N"/>
						</dd>
					</dl>
<!-- 					<dl> -->
<!-- 						<dt CL="STD_LOTA01">저장위치</dt> -->
<!-- 						<dd> -->
<!-- 							<input type="text" name="MS.STLKY" class="input" UIInput="R,SHSTLMA"/> -->
<!-- 						</dd> -->
<!-- 					</dl> -->
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
			</div>
		</div>
		<div class="search_next_wrap">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
					<li><a href="#tabs1-1" CL='STD_LIST'><span>리스트</span></a></li> 
					<!-- <li><a href="#tabs1-1" CL="STD_DOCUMENTINFO"><span>문서정보</span></a></li> -->
				</ul>
				<div class="search_sub">
					<dl>
<!--  						<dt CL="STD_GENERALWAREHOUSE">거점</dt>  -->
						<dd>
<!-- 							<button class="btn_warp">시작</button> -->
							<input type="button" CB="Save SAVE STD_SAVE" />
							&nbsp;&nbsp;&nbsp;
							<input type="button" CB="Ifon TEMP_SAVE BTN_IFSTAT" id="btn_ifstat"/>
							<input type="button" CB="Ifoff CANCEL BTN_IFSTOP"   id="btn_ifstop"/>
						</dd>
					</dl>
				</div>
				<div class="table_box section" id="tabs1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList1">
									<tr CGRow="true">
										<td GH="40" GCol="rownum"></td>
										<td GH="40" GCol="rowCheck"></td>
										<td GH="60 ITF_IFSTAT" GCol="text,IFSTATNM">상태</td>
										<td GH="100 STD_IFID" GCol="text,IFID">인터페이스ID</td>
										<td GH="200 STD_IFKNM" GCol="text,IFKNM">인터페이스한글명</td>
										<td GH="60 STD_SYSFR" GCol="text,SYSFR">송신시스템</td>
										<td GH="60 STD_SYSTO" GCol="text,SYSTO">수신시스템</td>
										<td GH="60 ITF_LASTCNT" GCol="text,LASTCNT" GF="N 20">완료</td>
										<td GH="60 ITF_LASTING" GCol="text,LASTING" GF="N 20">진행</td>
										<td GH="60 ITF_LASTERR" GCol="text,LASTERR" GF="N 20">에러</td>
<!-- 										<td GH="100 STD_LASTDAT" GCol="text,LASTDAT" GF="C">최근처리일자</td> -->
										<td GH="80 STD_DOCUTY" GCol="text,BWART">문서유형</td>
										<td GH="100 STD_IFTBL" GCol="text,IFTBL">인터페이스 테이블</td>
										<td GH="80 ITF_POLLTM" GCol="input,POLLTM" validate="required" GF="NS 6">실행시간</td>
										<!-- 실행주기 -->
										<td GH="110 ITF_POLLTY" GCol="select,POLLTY">
											<select CommonCombo="POLLTY">
												<option value=" "></option>
											</select>
										</td>
										<td GH="80 STD_PRORTY" GCol="input,PRORTY" validate="required" GF="NS 4">우선순위</td>
										<td GH="80 ITF_COMTYP" GCol="text,COMTYP">통신유형</td>
										<td GH="400 ITF_RFCFN" GCol="input,RFCFN">SAP RFC FUNCTION</td>
										<td GH="100 ITF_RFCTBL" GCol="input,RFCTBL">SAP RFC TABLE</td>
										<td GH="100 ITF_RFCP1" GCol="input,RFCP1">SAP RFC P1</td>
<!-- 										<td GH="60 ITF_RFCP2" GCol="input,RFCP2">SAP RFC P2</td> -->
										<!-- 데이터수신범위 -->
										<td GH="110 ITF_IFRCVRANGE" GCol="select,RFCP2">
											<select CommonCombo="IFRCVRANGE">
												<option value=" "></option>
											</select>
										</td>
<!-- 										<td GH="200 STD_IFACKY" GCol="text,IFENM">인터페이스영문명</td> -->
<!-- 										<td GH="70 STD_CDOCTY" GCol="text,IFTY">송수신구분 (R수신, S송신)</td> -->
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
<!-- 						<button type='button' GBtn="add"></button> -->
<!-- 						<button type='button' GBtn="copy"></button> -->
<!-- 						<button type='button' GBtn="delete"></button> -->
<!-- 						<button type='button' GBtn="total"></button> -->
<!-- 						<button type='button' GBtn="layout"></button> -->
						<button type='button' GBtn="excel"></button>
<!-- 						<button type='button' GBtn="excelUpload"></button> -->
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