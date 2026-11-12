<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList1",
			editable : true,
			module : "Erpif",
			command : "IFWMSLOG",
			colorType : true
		});

		// 검색조건 - 인터페이스 ID
		var $selIFID = $('select[name="IFID"]');
		inputList.setMultiComboSelectAll($("IFID"),true);
		$selIFID.multipleSelect('checkAll');
		// 검색조건 - 데이터수신
		var $selCOMSTAT = $('select[name="COMSTAT"]');
		inputList.setMultiComboSelectAll($("COMSTAT"),true);
		$selCOMSTAT.multipleSelect('setSelects', ['S','R','E']);

		// 콤보박스 사이즈 강제 조정
// 		$('[ms-choice="IFID"]').parent().css("width","400px");
// 		$('[ms-choice="IFID"]').parent().css("width","400px");
		$('[ms-drop="IFID"]').css("width","400px");
		
		// 조회
		//searchList();
	});
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			// 조회
			searchList();
		} else if(btnName == "Detail") {
			// 상세조회
			if(gridList.getGridDataCount("gridList1") > 0) {
				var rowData = gridList.getRowData("gridList1", gridList.getFocusRowNum("gridList1"));
				page.linkPopOpen("/wms/erpif/IF01POPD.page", rowData);
			} else {
				commonUtil.msgBox("VALID_M0005"); // 조회된 데이터가 없습니다.
			}
		} else if (btnName == "ReSend") {
			
			if(!commonUtil.msgConfirm("재전송하시겠습니까?")){
				return;
			}
			
			var rowNum = gridList.getFocusRowNum('gridList1');
			var row = gridList.getRowData('gridList1', rowNum);
			
			var param = new DataMap();
			param.put("IFID"    , row.get("IFID" ));
			param.put("LOGSEQNO", row.get("SEQNO"));
			param.put("RETRYYN" , "Y");
			
			var json = netUtil.send({
				url : "/wms/erpif/json/sendIF.data",
				param : param,
				successFunction : "reSendCallBack"
			});
		}
	}
		
	function reSendCallBack (json) {
		alert(json.data);
	}
	
	function searchList(){
		if(validate.check("searchArea")){
			var ifid = $('#IFID').val();
			var param = inputList.setRangeDataParam("searchArea");
			// 멀티콤보박스 전체 선택시 SQL상 조건절 OR 형태로 전체 추가되지 않도록 RANGE 제거
			if(inputList.getMultiComboValue($("#IFID")).length == $('#IFID > option').length) {param.get("RANGE_DATA_PARAM").remove("IFID_Single")};

			gridList.gridList({
				id : "gridList1",
				param : param
			});
			
		}
		
		$("#tab1").trigger('click'); 
	}
	
	function gridListEventRowDblclick(gridId, rowNum){
		if(gridId == "gridList1"){
			commonBtnClick("Detail");
		}
	}

	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList1"){
			var list = gridList.getGridData(gridId);
			if (0 < list.length) {
				gridListEventRowClick(gridId, 0);
			}
		}
	}
	
	function gridListEventRowClick(gridId, rowNum){
		// 재전송 버튼제어
		if(gridList.getGridData("gridList1")[rowNum].get("SYSFR") == "WMS"
		&& gridList.getGridData("gridList1")[rowNum].get("SYSTO") != "WMS"
		&& gridList.getGridData("gridList1")[rowNum].get("IFID") != "WMS_IF_209213"
		&& gridList.getGridData("gridList1")[rowNum].get("IFID") != "WMS_IF_401"
		&& gridList.getGridData("gridList1")[rowNum].get("COMSTAT") == "E"
		) {
			$('#btn_resend').css('visibility','visible');
		} else {
			$('#btn_resend').css('visibility','hidden');
		}
	}
	
	function gridListRowColorChange(gridId, rowNum, rowData){
		if(gridId == "gridList1"){
			if(rowData.get("COMSTAT") == "E" || rowData.get("INERRAT") == "E"){
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
					<input type="button" CB="Detail SEARCH BTN_DISDTL" />
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" CB="ReSend RESEND BTN_RESEND" id="btn_resend"/>
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_CDATE">처리일자</dt>
						<dd>
							<input type="text" name="CREDAT" UIInput="R" UIFormat="C -1" validate="required(STD_CDATE)"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_IFID">인터페이스ID</dt>
						<dd>
							<select Combo="Erpif,IFCOMBO" style="width:200px;" class="input" name="IFID" id="IFID" ComboType="MS" validate="required"></select>
						</dd>
					</dl>
					<dl>
						<dt CL="ITF_COMSTAT">통신상태</dt>
						<dd>
							<select CommonCombo="COMSTAT" style="width:200px;" class="input" name="COMSTAT" id="COMSTAT" ComboType="MS"></select>
						</dd>
					</dl>
					<dl>
						<dt CL="ITF_INERRAT">내부처리상태</dt>
						<dd>
							<select CommonCombo="INERRAT" style="width:200px;" class="input" name="INERRAT" id="INERRAT" ComboType="MS"></select>
						</dd>
					</dl>
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
				</ul>
				<div class="table_box section" id="tabs1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList1">
									<tr CGRow="true">
										<td GH="40" GCol="rownum"></td>
<!-- 										<td GH="40" GCol="rowCheck"></td> -->
										<td GH="230 STD_LOGSEQ" GCol="text,SEQNO">로그순서</td>
										<td GH="100 STD_IFID" GCol="text,IFID">인터페이스ID</td>
										<td GH="150 STD_IFKNM" GCol="text,IFKNM">인터페이스명</td>
										<td GH="60 STD_SYSFR" GCol="text,SYSFR">송신시스템</td>
										<td GH="60 STD_SYSTO" GCol="text,SYSTO">수신시스템</td>
										<td GH="160 STD_DOCUTY" GCol="text,BWARTNM">문서유형</td>
										<td GH="100 STD_IFTBL" GCol="text,IFTBL">인터페이스테이블</td>
										<td GH="80 ITF_COMSTAT" GCol="text,COMSTATNM">통신상태</td>
										<td GH="300 STD_ERRTXT" GCol="text,ERRTXT">에러내역</td>
										<td GH="100 ITF_INERRAT" GCol="text,INERRAT">내부처리에러여부</td>
										<td GH="300 ITF_INERRTXT" GCol="text,INERRTXT">내부처리에러내용</td>
										<td GH="100 STD_REMAKS" GCol="text,REMARK">비고</td>
										<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="C">생성일자</td>
										<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
										<td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자</td>
										<td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자명</td>
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