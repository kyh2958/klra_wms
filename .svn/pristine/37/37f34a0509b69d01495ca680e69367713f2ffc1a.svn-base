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
			command : "IFCOLMAP"
		});

		// 검색조건 - 인터페이스 ID
		var $selIFID = $('select[name="IFID"]');
		inputList.setMultiComboSelectAll($("IFID"),true);
		$selIFID.multipleSelect('checkAll');
		
		// 콤보박스 사이즈 강제 조정
// 		$('[ms-choice="IFID"]').parent().css("width","400px");
// 		$('[ms-choice="IFID"]').parent().css("width","400px");
		$('[ms-drop="IFID"]').css("width","400px");
		
		// 조회
		// searchList();
	});
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			// 조회
			searchList();
// 		} else if(btnName == "Detail") {
// 			// 상세조회
// 			if(gridList.getGridDataCount("gridList1") > 0) {
// 				var rowData = gridList.getRowData("gridList1", gridList.getFocusRowNum("gridList1"));
// 				page.linkPopOpen("/wms/erpif/IF01POPD.page", rowData);
// 			} else {
// 				commonUtil.msgBox("VALID_M0005"); // 조회된 데이터가 없습니다.
// 			}
		}
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
	
// 	function gridListEventRowDblclick(gridId, rowNum){
// 		if(gridId == "gridList1"){
// 			commonBtnClick("Detail");
// 		}
// 	}
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
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_IFID">인터페이스ID</dt>
						<dd>
							<select Combo="Erpif,IFCOMBO" class="input" name="IFID" id="IFID" ComboType="MS" validate="required"></select>
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
										<td GH="100 STD_IFID" GCol="text,IFID">인터페이스ID</td>
										<td GH="250 ITF_IFKNM" GCol="text,IFKNM">인터페이스명</td>
										<td GH="60 ITF_SYSFR" GCol="text,SYSFR">송신</td>
										<td GH="150 ITF_SOURCE" GCol="text,SOURCE">송신측컬럼</td>
										<td GH="60 ITF_SYSTO" GCol="text,SYSTO">수신</td>
										<td GH="150 ITF_TARGET" GCol="text,TARGET">수신측컬럼</td>
										<td GH="100 ITF_IFAT" GCol="text,IFAT">통신대상</td>
										<td GH="100 ITF_EXCELAT" GCol="text,EXCELAT">엑셀업로드</td>
										<td GH="300 ITF_COLNM" GCol="text,COLNM">컬럼명</td>
										<td GH="100 ITF_VIEWAT" GCol="text,VIEWAT">UI VIEW</td>
										<td GH="100 ITF_SORTNO" GCol="text,SORTNO" GF="N 0">정렬순서</td>
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