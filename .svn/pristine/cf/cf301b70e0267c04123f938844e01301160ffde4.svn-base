<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<script type="text/javascript">
	var nowHeadIndex = 0;
	var searchYN = "Y";
	var dblIdx = 0;
	
	$(document).ready(function() {
		gridList.setGrid({
			id : "gridList",
			editable : true,
			pkcol : "WAREKY,SSORKY",
			module : "WmsAdmin",
			command : "STSRH",
			emptyMsgType : false
		});
		
		gridList.setGrid({
			id : "gridListSub",
			editable : true,
			pkcol : "WAREKY,SSORKY,ITEMNO",
			module : "WmsAdmin",
			command : "STSRI",
			emptyMsgType : false
		});
		
		searchList();
	});

	function searchList() {
		searchYN = "Y";
 		if(validate.check("searchArea")){
			nowHeadIndex = 0;
			gridList.resetGrid("gridList");
			gridList.resetGrid("gridListSub");
			
			var param = inputList.setRangeParam("searchArea");
			param.put("WAREKY","<%=wareky%>");
			
			gridList.gridList({
				id : "gridList",
				param : param,
			});
 		}
	}

	function searchSubList(headRowNum) {
		var rowVal = gridList.getColData("gridList", headRowNum, "WAREKY");
		var rowMea = gridList.getColData("gridList", headRowNum, "SSORKY");
		var param = inputList.setRangeParam("searchArea");
		param.put("WAREKY", rowVal);
		param.put("SSORKY", rowMea);

		gridList.gridList({
			id : "gridListSub",
			param : param
		});
		lastFocusNum = headRowNum;
		dblIdx = headRowNum;
	}
	
	var lastFocusNum = -1;
	function gridListEventDataBindEnd(gridId, dataLength){
		if (gridId == "gridList" && dataLength > 0) {
			searchSubList(0);
		}
	}
	
	function gridListEventRowDblclick(gridId, rowNum, colName, colValue){
		if (gridId == "gridList") {
			if (gridList.getColData("gridList", rowNum, "STATUS") == "C") {
				return false;
			}
			dblIdx = rowNum;
			searchSubList(rowNum);
		}
	}
	
	//그리드 위에 클릭시 아이템 리셋
	function gridListEventRowFocus(gridId, rowNum) {
		if (gridId == "gridList") {
			var modRowCnt = gridList.getModifyRowCount("gridListSub");
			if (modRowCnt == 0) {
				if (dblIdx != rowNum) {
					gridList.resetGrid("gridListSub");
					dblIdx = -1;
				}
				//저장
			} else {
				if (confirm(commonUtil.getMsg("COMMON_M0024"))) {
					saveData();
				} else {
					gridList.resetGrid("gridListSub");
				}
			}
		}
	}
	
	function gridListEventRowRemove(gridId, rowNum){
		var rowAea = gridList.getColData("gridListSub", rowNum, "SSORKY");
		if(gridId == "gridList"){
			gridList.resetGrid("gridListSub");
		}else if(gridId == "gridListSub"){
			var param = new DataMap();
			param.put("WAREKY", "<%=wareky%>");
			param.put("SSORKY", rowAea);
			
			var json = netUtil.sendData({
				module : "WmsAdmin",
				command : "SSORKval",
				sendType : "map",
				param : param
				
			});
			
			if(json && json.data){
				if(json.data["CNT"] >= 1){
					commonUtil.msgBox("MASTER_M0689");
					//alert("할당정렬키가 존재하여 하위를 삭제 할 수 없습니다.");// messegeUtik
					return false;
				} else if (json.data["CNT"] <= 1){
					return true;
				}
			}
		}
		
		return true;
	}
	
	function gridListEventRowAddBefore(gridId, rowNum){
		var newData = new DataMap();
	
		if(gridId == "gridList") {
			var headCnt = gridList.getModifyRowCount("gridList");
			var listCnt = gridList.getModifyRowCount("gridListSub");
			if (listCnt > 0 || headCnt > 0) {
				//alert("변경된 row를 저장 후 행추가 하십시요.");
				commonUtil.msgBox("MASTER_M0686");
				return false;
			} else {
				var newData = new DataMap();
				newData.put("WAREKY", "<%=wareky%>");
				newData.put("STATUS", "C");
				
				return newData;
			}
		}else if(gridId == "gridListSub") {
			if (dblIdx == -1) {
				var headNum = gridList.getGridDataCount("gridList") - 1;
				if (gridList.getColData("gridList", headNum, "STATUS") == "") {
					//* 헤더의 할당 전략키를 입력하세요.
					commonUtil.msgBox("MASTER_M0577");
					return false;
				}
				
				if (gridList.getColData("gridList", headNum, "STATUS") == "C") {
					var wareky = gridList.getColData("gridList", headNum, "WAREKY");
					var ssorky = gridList.getColData("gridList", headNum, "SSORKY");
					
					var itemno = 1;
					var lastRow = gridList.getGridDataCount("gridListSub");
					
					if(lastRow > 0){
						itemno = parseInt(gridList.getColData("gridListSub", lastRow-1, "ITEMNO"))+1;
					}
					
					if(itemno < 10){
						itemno = "00" + itemno;
					}else if(itemno < 100){
						itemno = "0" + itemno;
					}

					if (ssorky != "") {
						var newData = new DataMap();
						newData.put("WAREKY", wareky);
						newData.put("SSORKY", ssorky);
						newData.put("ITEMNO", itemno);
						
						return newData;
					} else {
						//적치전략키를 먼저 입력해주세요.
						commonUtil.msgBox("MASTER_M0242");
						return false;
					}

				} else {
					//alert("적치전략키를 선택하세요.");
					commonUtil.msgBox("MASTER_M0687");
					return false;

				}
			} else {

				var wareky = gridList.getColData("gridList", dblIdx, "WAREKY");
				var ssorky = gridList.getColData("gridList", dblIdx, "SSORKY");
				
				var itemno = 1;
				var lastRow = gridList.getGridDataCount("gridListSub");
				
				if(lastRow > 0){
					itemno = parseInt(gridList.getColData("gridListSub", lastRow-1, "ITEMNO"))+1;
				}
				
				if(itemno < 10){
					itemno = "00" + itemno;
				}else if(itemno < 100){
					itemno = "0" + itemno;
				}
				
				var newData = new DataMap();
				newData.put("WAREKY", wareky);
				newData.put("SSORKY", ssorky);
				newData.put("ITEMNO", itemno);
				
				return newData;
			}
		}
		
		return newData;
	}
	
	function saveData() {
		if (gridList.getModifyRowCount("gridList") == 0
				&& gridList.getModifyRowCount("gridListSub") == 0) {
			commonUtil.msgBox("MASTER_M0545");
			return;
		}//변경된 데이터가 없습니다.

		if (!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)) {
			return;
			
		}//저장하시겠습니까?
		if (gridList.validationCheck("gridList", "modify")
				&& gridList.validationCheck("gridListSub", "modify")) {

			var headNum = gridList.getGridDataCount("gridList") - 1;

			/* if (gridList.getRowStatus("gridList", headNum) == "C"
					&& gridList.getGridDataCount("gridListSub") == 0) {
				//alert(commonUtil.getMsg("OUT_M0045"));
				commonUtil.msgBox("OUT_M0045");
				
				return;//처리를 위한 아이템이 존재하지 않습니다.
			} */

			var headCnt = gridList.getGridDataCount("gridList");
			var itemCnt = gridList.getGridDataCount("gridListSub");
			var colValue;
			var rowValue;
			for (var i = 0; i < headCnt; i++) {
				wareky = gridList.getColData("gridList", i, "WAREKY");
				
				colValue = gridList.getRowStatus("gridList", i);
				gridList.setColValue("gridList", i, "STATUS", colValue);
			}

			for (var j = 0; j < itemCnt; j++) {
				rowValue = gridList.getRowStatus("gridListSub", j);
				gridList.setColValue("gridListSub", j, "STATUS", rowValue);
			}

			var head = gridList.getGridData("gridList");
			var list = gridList.getGridData("gridListSub");
	
			var param = new DataMap();
			param.put("head", head);
			param.put("list", list);

			var json = netUtil.sendData({
				
				url : "/wms/admin/json/saveTS01.data",
				param : param
			});

			
			if (json && json.data) {
				if(json.data == "OK") {
					alert(commonUtil.getMsg("SYSTEM_SAVEOK"));
					gridList.resetGrid("gridListSub");
					searchList();
				} else {
					alert(commonUtil.getMsg("SYSTEM_SAVEFAIL") + " [" + json.data + "]");
				}
				/* commonUtil.msgBox("HHT_T0008");
				gridList.resetGrid("gridListSub");
				searchList(); */
			}
		}
		
	}
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue) {
		if(gridId == "gridListSub") {
			if(colName == "ACTVAT") {
				if(colValue == "V"){
					var max = 0;
					$.each(gridList.getGridData("gridListSub"), function(i, obj){
						var num = (Number)(obj.get("SORTSQ"));
						if(max < num) {
							max = num;
						}
					});
					gridList.setColValue(gridId, rowNum, "SORTSQ", max+1);
					gridList.setColValue(gridId, rowNum, "INDASD", "A");
				} else {
					gridList.setColValue(gridId, rowNum, "SORTSQ", 0);
					gridList.setColValue(gridId, rowNum, "INDASD", " ");
				}
			}
		}
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType){
 		var param = new DataMap();
 		
		if(searchCode=="SHWAHMA"){
			param =  dataBind.paramData("searchArea");
		}else if(searchCode == "SHAREMA"){
			param = dataBind.paramData("searchArea");
		}
		
		return param;
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}
	}
	
	function gridExcelDownloadEventBefore(gridId){
		var param = inputList.setRangeParam("searchArea");
		if(gridId == "gridListSub"){
			var rowNum = gridList.getFocusRowNum("gridList");
			var ssorky = gridList.getColData("gridList", rowNum, "SSORKY");
		
			param.put("SSORKY",ssorky);
		}
		return param;
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
					<input type="button" CB="Save SAVE STD_SAVE" />	
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_WAREKY">거점</dt>
						<dd>
							<input type="text" class="input" name="WAREKY" value="<%=wareky%>" readonly="readonly" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_SSORKY">재고 정렬 키</dt>
						<dd>
							<input type="text" class="input" name="SSORKY" id="SSORKY" validation="required" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_SHORTX">설명</dt>
						<dd>
							<input type="text" class="input" name="SHORTX" id="SHORTX" style="width:175px" validation="required" />
						</dd>
					</dl>
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
			</div>
		</div>
        <div class="search_next_wrap">
			<div class="content_layout tabs top_layout">
				<ul class="tab tab_style02">
					<li><a href="#tab1-1"><span>리스트</span></a></li>
					<li class="btn_zoom_wrap">
						<ul>
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="100 STD_WAREKY" GCol="text,WAREKY"></td>
										<td GH="100 STD_SSORKY" GCol="input,SSORKY" GF="S 10"></td>
										<td GH="100 STD_SHORTX" GCol="input,SHORTX" GF="S 180"></td>
										<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D">생성일자</td>
										<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
										<td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자ID</td>
										<td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자명</td>
										<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D">수정일자</td>
										<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
										<td GH="100 STD_LMOUSR" GCol="text,LMOUSR">수정자ID</td>
										<td GH="100 STD_LUSRNM" GCol="text,LUSRNM">수정자명</td>								
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="add"></button>
						<button type='button' GBtn="delete"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
			</div>
			
			<div class="content_layout tabs bottom_layout">
				<ul class="tab tab_style02">
					<li><a href="#tab1-1"><span>Item리스트</span></a></li>
					<li class="btn_zoom_wrap">
						<ul>
							<!-- <li><button class="btn btn_smaller"><span>축소</span></button></li> -->
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridListSub">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="80 STD_FIELDN" GCol="text,FIELDN">필드 명</td>
										<td GH="80 STD_FLDCMT" GCol="text,FLDCMT">설명</td>
										<td GH="80 STD_ACTVAT" GCol="check,ACTVAT">활성화</td>
										<td GH="80 STD_SORTSQ" GCol="input,SORTSQ" GF="N 20">정렬 순서</td>
										<td GH="120 STD_INDASD" GCol="select,INDASD">
											<select CommonCombo="INDASD">
												<option value=" "></option>
											</select>
										</td>
										<td GH="80 STD_WAREKY" GCol="text,WAREKY">거점</td>
										<td GH="100 STD_SSORKY" GCol="text,SSORKY">재고정렬키</td>
										<td GH="100 STD_ITEMNO" GCol="text,ITEMNO">LineNo.</td>
										<td GH="100 STD_TABLEN" GCol="text,TABLEN">테이블명</td>
										<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D">생성일자</td>
										<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
										<td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자ID</td>
										<td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자명</td>
										<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D">수정일자</td>
										<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
										<td GH="100 STD_LMOUSR" GCol="text,LMOUSR">수정자ID</td>
										<td GH="100 STD_LUSRNM" GCol="text,LUSRNM">수정자명</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
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