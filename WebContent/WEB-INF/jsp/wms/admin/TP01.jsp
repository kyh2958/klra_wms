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
	var dblIdx = 0; 
	$(document).ready(function() {
		gridList.setGrid({
			id : "gridList",
			name : "gridList",
			editable : true,
			pkcol : "WAREKY,PASTKY",
			module : "WmsAdmin",
			command : "TP01",
	    	bindArea : "tabs1-2"

		});

		gridList.setGrid({
			id : "gridListSub",
			name : "gridListSub",
			editable : true,
			pkcol : "WAREKY,PASTKY,STEPNO",
			module : "WmsAdmin",
			command : "TP01Sub"
		});
		
		<%-- $("#USERAREA").val("<%=user.getUserg5()%>"); --%>
	});

	function searchList() {
		if($("#searchArea").find("[name=ZONE]").prop("checked") == false 
			&& $("#searchArea").find("[name=LOC]").prop("checked") == false){
			inputList.addValidation("H.PASTKY", "required");
		}
		
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			
			gridList.gridList({
				id : "gridList",
				param : param
			});
		}
	}
	
	function searchSubList(headRowNum) {
		var rowVal = gridList.getColData("gridList", headRowNum, "WAREKY");
		var rowMea = gridList.getColData("gridList", headRowNum, "PASTKY");
		var param = inputList.setRangeParam("searchArea");
		param.put("WAREKY", rowVal);
		param.put("PASTKY", rowMea);

		gridList.gridList({
			id : "gridListSub",
			param : param
		});
		lastFocusNum = headRowNum;
		dblIdx = headRowNum;
	}
	
	var lastFocusNum = -1;
	function gridListEventDataBindEnd(gridId, dataLength) {
		//alert( " gridId : " + gridId + "\n dataLength : " + dataLength );
		if (gridId == "gridList" && dataLength > 0) {
			searchSubList(0);
		}
	}

	// 상단 그리드 더블 클릭시 하단 그리드 조회
	function gridListEventRowDblclick(gridId, rowNum) {
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
	
	//헤드 삭제시 서브도 날리기.
	function gridListEventRowRemove(gridId, rowNum){
		if(gridId == "gridList"){
			gridList.resetGrid("gridListSub");
		}
	}

	function gridListEventRowAddBefore(gridId, rowNum) {
		if (gridId == "gridList") {
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
		} else if (gridId == "gridListSub") { 
			if (dblIdx == -1) {
				var headNum = gridList.getGridDataCount("gridList") - 1;
				if (gridList.getColData("gridList", headNum, "STATUS") == "") {
					commonUtil.msgBox("MASTER_M0577");
					return false;
				}

				if (gridList.getColData("gridList", headNum, "STATUS") == "C") {
					var wareky = gridList.getColData("gridList", headNum, "WAREKY");
					var pastky = gridList.getColData("gridList", headNum, "PASTKY");
					
					var stepno = 1;
					var lastRow = gridList.getGridDataCount("gridListSub");
					
					if(lastRow > 0){
						stepno = parseInt(gridList.getColData("gridListSub", lastRow-1, "STEPNO"))+1;
					}
					
					if(stepno < 10){
						stepno = "00" + stepno;
					}else if(stepno < 100){
						stepno = "0" + stepno;
					}

					if (pastky != "") {
						var newData = new DataMap();
						newData.put("WAREKY", wareky);
						newData.put("PASTKY", pastky);
						newData.put("STEPNO", stepno);
						
						return newData;
					} else {
						//alert(commonUtil.getMsg("MASTER_M0242"));
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
				var pastky = gridList.getColData("gridList", dblIdx, "PASTKY");
				
				var stepno = 1;
				var lastRow = gridList.getGridDataCount("gridListSub");
				
				if(lastRow > 0){
					stepno = parseInt(gridList.getColData("gridListSub", lastRow-1, "STEPNO"))+1;
				}
				
				if(stepno < 10){
					stepno = "00" + stepno;
				}else if(stepno < 100){
					stepno = "0" + stepno;
				}
				
				var newData = new DataMap();
				newData.put("WAREKY", wareky);
				newData.put("PASTKY", pastky);
				newData.put("STEPNO", stepno);
				
				return newData;
			}
		}

	}

	function saveData() {
		debugger;
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

			if (gridList.getRowStatus("gridList", headNum) == "C"
					&& gridList.getGridDataCount("gridListSub") == 0) {
				//alert(commonUtil.getMsg("OUT_M0045"));
				commonUtil.msgBox("OUT_M0045");
				
				return;//처리를 위한 아이템이 존재하지 않습니다.
			}

			var headCnt = gridList.getGridDataCount("gridList");
			var itemCnt = gridList.getGridDataCount("gridListSub");
			var colValue;
			var rowValue;
			for (var i = 0; i < headCnt; i++) {
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
				url : "/wms/admin/json/saveTP01.data",
				param : param
			});

			
			if (json && json.data) {
				commonUtil.msgBox("HHT_T0008");
				gridList.resetGrid("gridListSub");
				searchList();
			}
		}
	}

	function gridListEventColValueChange(gridId, rowNum, colName, colValue) {
		if (gridId == "gridList" && colName == "PASTKY") {
			if (colValue != "") {
				var wareky = gridList.getColData("gridList", rowNum, "WAREKY");
				
				var param = new DataMap();
				param.put("PASTKY", colValue);
				param.put("WAREKY", wareky);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "TP01PASTHval",
					sendType : "map",
					param : param
				});

				if (json.data["CNT"] >= 1) {
					commonUtil.msgBox("VALID_M0308", colValue);
					gridList.setColValue("gridList", rowNum, "PASTKY", "");
					//return false;
				}
			}
			if(gridList.getGridDataCount("gridListSub") > 0){
				var listCnt = gridList.getGridDataCount("gridList");
				if(listCnt != 0){
					for(var i = 0; i < listCnt; i++){
						gridList.setColValue("gridListSub", i, "PASTKY", colValue);
					}
				}
			}
		}else if(gridId == "gridListSub" && colName == "STEPNO"){
			if(colValue != ""){
				var wareky = gridList.getColData("gridListSub", rowNum, "WAREKY");
				var pastky = gridList.getColData("gridListSub", rowNum, "PASTKY");
				
				var param = new DataMap();
				param.put("STEPNO",colValue);
				param.put("WAREKY",wareky);
				param.put("PASTKY",pastky);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "TP01STEPNOval",
					sendType : "map",
					param : param
				});
				
				if(json.data["CNT"] >= 1) {
					gridList.setColValue("gridListSub", rowNum, "STEPNO", "");
					//alert(commonUtil.getMsg("MASTER_M0100"));
					commonUtil.msgBox("MASTER_M0100");
					return false;
				}
			}
		}else if(gridId=="gridListSub" && colName=="LOCASR"){
				if (colValue != "") {
					var param=new DataMap();
					param.put("LOCASR",colValue);
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "LOCAval",
						sendType : "map",
						param : param
					});
					if(json.data["CNT"] < 1){
						commonUtil.msgBox("MASTER_M0477", colValue);
						gridList.setColValue("gridListSub", rowNum, "LOCASR", ""); 
					}
				}
			}else if(gridId=="gridListSub" && colName=="ZONETG"){
				if (colValue != "") {
					var param=new DataMap();
					param.put("ZONETG",colValue);
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "ZONETGval",
						sendType : "map",
						param : param
					});
					if(json.data["CNT"] < 1){
						commonUtil.msgBox("MASTER_M0044", colValue);
						gridList.setColValue("gridListSub", rowNum, "ZONETG", ""); 
					}
				}
			}else if(gridId=="gridListSub" && colName=="LOCATG"){
				if (colValue != "") {
					var param=new DataMap();
					param.put("LOCATG",colValue);
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "LOCATGval",
						sendType : "map",
						param : param 
					});
					if(json.data["CNT"] < 1){
						commonUtil.msgBox("MASTER_M0477", colValue);
						gridList.setColValue("gridListSub", rowNum, "LOCATG", ""); 
					}
				}
			}
	}
	function searchHelpEventOpenBefore(searchCode, gridType){
		//commonUtil.debugMsg("searchHelpEventOpenBefore : ", arguments);
		if(searchCode == "SHRLRRH"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHWAHMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHPASTH"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHLOCMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHZONMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHAREMA"){
			return dataBind.paramData("searchArea");
		}
		
	}
	
	function commonBtnClick(btnName){
		//commonUtil.debugMsg("commonBtnClick : ", arguments);
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Execute"){
			test3();
		}
	}
	
	function gridExcelDownloadEventBefore(gridId){
		var param = inputList.setRangeParam("searchArea");
		if(gridId == "gridListSub"){
			var rowNum = gridList.getFocusRowNum("gridList");
			var pastky = gridList.getColData("gridList", rowNum, "PASTKY");
		
			param.put("PASTKY",pastky);
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
						<dt CL="STD_PASTKY">적치전략키</dt>
						<dd>
							<input type="text" class="input" name="H.PASTKY" UIInput="R,SHPLSTH" />
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
										<td GH="100 STD_PASTKY" GCol="input,PASTKY" validate="required"></td>
										<td GH="100 STD_SHORTX" GCol="input,SHORTX" validate="required" GF="S 10"></td>
										<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D"></td>
										<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T"></td>
										<td GH="100 STD_CREUSR" GCol="text,CREUSR"></td>
										<td GH="100 STD_CUSRNM" GCol="text,CUSRNM"></td>
										<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D"></td>
										<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T"></td>
										<td GH="100 STD_LMOUSR" GCol="text,LMOUSR"></td>
										<td GH="100 STD_LUSRNM" GCol="text,LUSRNM"></td>									
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="add"></button>
						<button type='button' GBtn="copy"></button>
						<button type='button' GBtn="delete"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
			</div>
			
			<div class="content_layout tabs bottom_layout">
				<ul class="tab tab_style02">
					<li><a href="#tab1-1"><span>아이템</span></a></li>
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
										<td GH="100 STD_STEPNO" GCol="input,STEPNO" validate="required" GF="S 3"></td>
										<td GH="100 STD_FROMLOC" GCol="input,LOCASR,SHLOCMA" ></td>
										<td GH="100 STD_RMTGLS" GCol="select,RMTGLS">
											<select Combo="WmsAdmin,PATYPECOMBO"></select>
										</td>
										<td GH="100 STD_LOCATG" GCol="input,LOCATG,SHLOCMA"></td>
										<td GH="100 STD_ZONETG" GCol="input,ZONETG,SHZONMA"></td>
										<td GH="100 STD_AREATG" GCol="input,AREATG,SHAREMA"></td>
										<td GH="100 STD_CAPACR" GCol="input,CAPACR"></td>
										<td GH="100 STD_WAREKY" GCol="input,WAREKY"></td>
										<td GH="100 STD_PASTKY" GCol="input,PASTKY"></td>
										<td GH="100 STD_SRMEKY" GCol="input,SRMEKY"></td>
										<td GH="100 STD_USDOCT" GCol="input,USDOCT"></td>
										<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D"></td>
										<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T"></td>
										<td GH="100 STD_CREUSR" GCol="text,CREUSR"></td>
										<td GH="100 STD_CUSRNM" GCol="text,CUSRNM"></td>
										<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D"></td>
										<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T"></td>
										<td GH="100 STD_LMOUSR" GCol="text,LMOUSR"></td>
										<td GH="100 STD_LUSRNM" GCol="text,LUSRNM"></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="add"></button>
						<button type='button' GBtn="copy"></button>
						<button type='button' GBtn="delete"></button>
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