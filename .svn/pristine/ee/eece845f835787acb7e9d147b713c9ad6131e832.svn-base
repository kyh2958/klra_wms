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
	$(document).ready(function(){
		gridList.setGrid({
	    	id : "gridList",
	    	name : "gridList",
			editable : true,
			pkcol : "WAREKY,ALSTKY",
			module : "WmsAdmin",
			command : "TA01",
			validation : "WAREKY,ALSTKY,SHORTX",
			bindArea : "tabs1-2"
	    });
		gridList.setGrid({
	    	id : "gridListSub",
	    	name : "gridListSub",
			editable : true,
			pkcol : "WAREKY,ALSTKY,STEPNO",
			module : "WmsAdmin",
			command : "TA01Sub",
			validation : "WAREKY,ALSTKY,STEPNO"
	    });
	}); 
	
	function searchList(){
		var param = inputList.setRangeParam("searchArea");
		gridList.gridList({
	    	id : "gridList",
	    	param : param
	    });
	}
	
	function searchSubList(headRowNum){
		var rowVal = gridList.getColData("gridList", headRowNum, "WAREKY");
		var rowMea = gridList.getColData("gridList", headRowNum, "ALSTKY");
		var param = inputList.setRangeParam("searchArea");
		param.put("WAREKY", rowVal);
		param.put("ALSTKY", rowMea);
		
		gridList.gridList({
			id : "gridListSub",
			param : param
		})
		lastFocusNum = headRowNum;
		dblIdx = headRowNum;
	}
	
	var lastFocusNum = -1;
	
	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList" && dataLength > 0){
			searchSubList(0);
		}else if(gridId == "gridList" && dataLength == 0){
			//2020.04.09 LHJ 데이터 조회 후 존재하지 않는 데이터를 조회하면 이전에 남아있는 gridList reset.
			gridList.resetGrid("gridListSub");
		}
	}
	
	function gridListEventRowDblclick(gridId, rowNum){
		if(gridId == "gridList"){
			if(gridList.getColData("gridList", rowNum, "STATUS") == "C"){
				return false;
			}
			
			dblIdx = rowNum;
			
			searchSubList(rowNum);
		}
	}
	
	//그리드 위에 클릭시 아이템 리셋
	function gridListEventRowFocus(gridId, rowNum){
		if(gridId == "gridList"){
			var modRowCnt = gridList.getModifyRowCount("gridListSub");
			if(modRowCnt == 0){
				if(dblIdx != rowNum){
					gridList.resetGrid("gridListSub");
					dblIdx = -1;
				}
			//저장
			}else{
				//if(confirm(commonUtil.getMsg("COMMON_M0024"))){   //값이 변화된 데이터가 있습니다. 이동하시겠습니까?
					gridList.resetGrid("gridListSub");
				//}else{
					//return false;
				//}
			}
		}
	}
	
	function gridListEventRowRemove(gridId, rowNum){
		if(gridId == "gridList" ){
			gridList.resetGrid("gridListSub");
		}
	}
	
	function gridListEventRowAddBefore(gridId, rowNum) {
		if(gridId == "gridList"){
			var headCnt = gridList.getModifyRowCount("gridList");
			var listCnt = gridList.getModifyRowCount("gridListSub");
			if(listCnt > 0 || headCnt > 0){
				commonUtil.msgBox("MASTER_M0702");//변경된 row를 저장 후 행추가 하십시오.
				return false;
			}else{
				var newData = new DataMap();
				newData.put("WAREKY", "<%=wareky%>");
				newData.put("STATUS", "C");

				return newData;
			}
			
			if(gridList.getGridDataCount("gridListSub") > 0){
				var listCnt = gridList.getGridDataCount("gridList");
				if(listCnt != 0){
					for(var i = 0; i < listCnt; i++){
						gridList.setColValue("gridListSub", i, "CMCDKY", colValue);
					}
				}
			}
		}else if (gridId == "gridListSub"){
			if (dblIdx == -1) {
				var headNum = gridList.getGridDataCount("gridList") - 1;
				
				if (gridList.getColData("gridList", headNum, "STATUS") == "") {
					//alert(commonUtil.getMsg("MASTER_M0577"));
					commonUtil.msgBox("MASTER_M0577");
					return false;
				}

				if (gridList.getColData("gridList", headNum, "STATUS") == "C") {
					var wareky = gridList.getColData("gridList", headNum, "WAREKY");
					var alstky = gridList.getColData("gridList", headNum, "ALSTKY");

					var stepno = 1;

					var lastRow = gridList.getGridDataCount("gridListSub");
					if (lastRow > 0) {
						stepno = parseInt(gridList.getColData("gridListSub", lastRow - 1, "STEPNO")) + 1;
					}

					if (stepno < 10) {
						stepno = "00" + stepno;
					} else if (stepno < 100) {
						stepno = "0" + stepno;
					}
					
					if (alstky != "") {
						var newData = new DataMap();
						newData.put("WAREKY", wareky);
						newData.put("ALSTKY", alstky);
						newData.put("ALORTO", "100");
						newData.put("ALUNIT", "1");
						newData.put("STEPNO", stepno);

						return newData;
					}else {
						//alert(commonUtil.getMsg("MASTER_M0097"));
						commonUtil.msgBox("MASTER_M0097");
						return false;
					}
				}else {
					//commonUtil.msgBox("TASK_M0003");
					commonUtil.msgBox("MASTER_M0106");
					return false;
				}
			}else if(dblIdx == 0){
				//2020.04.08 LHJ 아이템리스트 추가 시 번호오류 수정, 할당전략키 먼저 생성하도록 수정 
				var headCnt = gridList.getGridDataCount("gridList");
				if (headCnt == 0) {
					commonUtil.msgBox("MASTER_M0106"); //할당전략키를 먼저 생성하세요
					return false;
				}
				var wareky = gridList.getColData("gridList", dblIdx, "WAREKY");
				var alstky = gridList.getColData("gridList", dblIdx, "ALSTKY");

				var stepno = 1;

				var lastRow = gridList.getGridDataCount("gridListSub");
				if (lastRow > 0) {
					stepno = parseInt(gridList.getColData("gridListSub", lastRow - 1, "STEPNO")) + 1;
				}

				if (stepno < 10) {
					stepno = "00" + stepno;
				} else if (stepno < 100) {
					stepno = "0" + stepno;
				}
				
				var newData = new DataMap();
				newData.put("WAREKY", wareky);
				newData.put("ALSTKY", alstky);
				newData.put("ALORTO", "100");
				newData.put("ALUNIT", "1");
				newData.put("STEPNO", stepno);

				return newData;
			}else{
				var headCnt = gridList.getGridDataCount("gridList");
				if (headCnt == 0) {
					commonUtil.msgBox("MASTER_M0106"); //할당전략키를 먼저 생성하세요
					return false;
				}
				var wareky = gridList.getColData("gridList", dblIdx, "WAREKY");
				var alstky = gridList.getColData("gridList", dblIdx, "ALSTKY");
				
				var stepno = 1;
				
				var lastRow = gridList.getGridDataCount("gridListSub");
				if (lastRow > 0) {
					stepno = parseInt(gridList.getColData("gridListSub", lastRow - 1, "STEPNO")) + 1;
				}
				
				if (stepno < 10) {
					stepno = "00" + stepno;
				} else if (stepno < 100) {
					stepno = "0" + stepno;
				}

				var newData = new DataMap();
				newData.put("WAREKY", wareky);
				newData.put("ALSTKY", alstky);
				newData.put("ALORTO", "100");
				newData.put("ALUNIT", "1");
				newData.put("STEPNO", stepno);

				return newData;
			}
		}
	}
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue) {
		if (gridId == "gridList" && colName == "ALSTKY") {
			if (colValue != "") {
				var wareky = gridList.getColData("gridList", rowNum, "WAREKY");

				var param = new DataMap();
				param.put("ALSTKY", colValue);
				param.put("WAREKY", wareky);

				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "TA01ALSTKYval",
					sendType : "map",
					param : param
				});

				if (json.data["CNT"] >= 1) {
					gridList.setColValue("gridList", rowNum, "ALSTKY", " ");
					//alert(commonUtil.getMsg("VALID_M0111", colValue));
					commonUtil.msgBox("VALID_M0111", colValue); //이미 존재하는 할당 전략입니다.
					return false;
				}
			}
			if (gridList.getGridDataCount("gridListSub") > 0) {
				var listCnt = gridList.getGridDataCount("gridList");
				var sublistCnt = gridList.getGridDataCount("gridListSub");
				if (listCnt != 0) {
					for (var i = 0; i < sublistCnt; i++) {
						gridList.setColValue("gridListSub", i, "ALSTKY", colValue);
					}
				}
			}
		}
		if (gridId == "gridListSub" && colName == "ALFTKY"){
			//2020.04.08 LHJ 할당필터링 키validation추가
			if (colValue != "") {
				var param = new DataMap();
				param.put("LOTRKY", colValue);
				param.put("WAREKY", "<%=wareky%>");

				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "TA01ALFTKYval",
					sendType : "map",
					param : param
				});

				if (json.data["CNT"] == 0) {
					gridList.setColValue("gridListSub", rowNum, "ALFTKY", " ");
					commonUtil.msgBox("MASTER_M0701"); //존재하지 않는 할당필터링 키 입니다.
					return false;
					
				}else if(json.data["CNT"] > 0){
					var alstky = gridList.getColData("gridListSub", rowNum, "ALSTKY");
					var alftky = gridList.getColData("gridListSub", rowNum, "ALFTKY");//할당필터링 키 
					var ssorky = gridList.getColData("gridListSub", rowNum, "SSORKY");//재고정렬 키
					
					if(alstky = ssorky)
					var param = new DataMap();
					
					param.put("WAREKY", "<%=wareky%>");
					param.put("ALFTKY", colValue);
					param.put("SSORKY", ssorky);

					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "TA01SSOALFTval",
						sendType : "map",
						param : param
					});
					
					if (json.data["CNT"] > 0) {
						commonUtil.msgBox("MASTER_M0415"); //재고정렬 키에 대한 동일한 할당필터링 키가 존재합니다.
						gridList.setColValue("gridListSub", rowNum, "ALFTKY", " ");
						return false;
					}
				}
			}
		}
		if (gridId == "gridListSub" && colName == "SSORKY"){
			//2020.04.08 LHJ 재고정렬 키validation추가
			var alstky = gridList.getColData("gridListSub", rowNum, "ALSTKY");
			if (colValue != "") {
				var param = new DataMap();
				param.put("SSORKY", colValue);
				param.put("WAREKY", "<%=wareky%>");
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "TA01STSRHval",
					sendType : "map",
					param : param
				});

				if (json.data["CNT"] == 0) {
					gridList.setColValue("gridListSub", rowNum, "SSORKY", " ");
					commonUtil.msgBox("MASTER_M0316");
					return false;
					
				}else if(json.data["CNT"] > 0){ 
					var alstky = gridList.getColData("gridListSub", rowNum, "ALSTKY");
					var alftky = gridList.getColData("gridListSub", rowNum, "ALFTKY");
					var param = new DataMap();
					param.put("WAREKY", "<%=wareky%>");
					param.put("ALSTKY", alstky);
					param.put("ALFTKY", alftky);
					param.put("SSORKY", colValue);
					
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "TA01SSOALFTval",
						sendType : "map",
						param : param
					});
					
					if (json.data["CNT"] > 0) {
						commonUtil.msgBox("MASTER_M0416")
						gridList.setColValue("gridListSub", rowNum, "SSORKY", " ");
						return false;
					}
				}
			}
		}
	}

	function saveData() {
		//2020.04.09 LHJ HeadList데이터 없이 ItemList Row추가 후 저장 불가능하도록 validation추가
		var headCnt = gridList.getGridDataCount("gridList");
		if(headCnt == 0){
			commonUtil.msgBox("MASTER_M0449");//할당전략키 없이 저장할 수 없습니다.
			return false;
		}
		
		//2020.04.14 ItemList없이 HeadList저장되는 오류 수정 
		var rowNum = 0;
		var subrowNum = gridList.getModifyRowCount("gridListSub")-1;
		if(gridId = "gridListSub"){
			if(gridList.getRowState(gridId, rowNum) == configData.GRID_ROW_STATE_DELETE){
				if(gridList.getRowStatus("gridListSub",subrowNum) != "C"){
					commonUtil.msgBox("VALID_M0015");	//처리할 아이템이 없으므로 삭제할 수 없습니다.
					return false;
				}
			}
		}
		if (gridList.getModifyRowCount("gridList") == 0
				&& gridList.getModifyRowCount("gridListSub") == 0) {
			commonUtil.msgBox("MASTER_M0545");	//* 변경된 데이터가 없습니다.
			return;
		}
		
		if (!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)) {
			return;
		}
		
		//2020.04.13 LHJ 할당전략키 및 ItemList 동시 추가 시 동일한  할당필터링 키와 재고정렬 키값 존재 유무 validation
		var listCnt = (gridList.getModifyRowCount("gridListSub"))
		var alftky = gridList.getColData("gridListSub",0,"ALFTKY");
		var ssorky = gridList.getColData("gridListSub",0,"SSORKY");
		
		for(var i = 1; i < listCnt; i++) {
			var alftky2 = gridList.getColData("gridListSub",i,"ALFTKY");
			var ssorky2 = gridList.getColData("gridListSub",i,"SSORKY");
			
			if(ssorky == ssorky2){
				if(alftky == alftky2){
					commonUtil.msgBox("VALID_M0014");//동일한 할당필터링 키와 재고정렬 키를 추가할 수 없습니다.
					return false;
				}
			}
		}
		
		if (gridList.validationCheck("gridList", "modify")
				&& gridList.validationCheck("gridListSub", "modify")) {
			var headNum = gridList.getGridDataCount("gridList") - 1;
			if (gridList.getRowStatus("gridList", headNum) == "C"
					&& gridList.getGridDataCount("gridListSub") == 0) {
				//alert(commonUtil.getMsg("OUT_M0045"));
				commonUtil.msgBox("OUT_M0045");
				return;
			}

			var headCnt = gridList.getGridDataCount("gridList");
			for (var i = 0; i < headCnt; i++) {
				gridList.setColValue("gridList", i, "STATUS", gridList.getRowStatus("gridList", i));
			}
			var head = gridList.getGridData("gridList");

			var listCnt = gridList.getGridDataCount("gridListSub");
			for (var i = 0; i < listCnt; i++) {
				gridList.setColValue("gridListSub", i, "STATUS", gridList.getRowStatus("gridListSub", i));
			}

			var list = gridList.getGridData("gridListSub");

			var param = new DataMap();

			param.put("head", head);
			param.put("list", list);

			var json = netUtil.sendData({
				url : "/wms/admin/json/saveTA01.data",
				param : param
			});

			if (json) {
				if (json.data) {
					gridList.resetGrid("gridListSub");
					commonUtil.msgBox("HHT_T0008");
					searchList();
				}
			}
		}
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType){
		//commonUtil.debugMsg("searchHelpEventOpenBefore : ", arguments);
		if(searchCode == "SHRLRRH"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHSTSRH"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHWAHMA"){
			return dataBind.paramData("searchArea");
		}
	}
	
	function commonBtnClick(btnName){
		//commonUtil.debugMsg("commonBtnClick : ", arguments);
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
			var alstky = gridList.getColData("gridList", rowNum, "ALSTKY");
		
			param.put("ALSTKY",alstky);
		}
		return param;
	}

	//2020.04.13 LHJ Stepno숫자입력 validation
 	function numkeyCheck(e) { 
 		var keyValue = event.keyCode;
 		if(((keyValue >= 48) && (keyValue <= 57)) ){
 			return true;
 		}else{
 			commonUtil.msgBox("VALID_M0013");
 			return false; 
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
					<input type="button" CB="Save SAVE STD_SAVE" />	
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_WAREKY">거점</dt>
						<dd>
							<input type="text" class="input" name="WAREKY" value="<%=wareky%>" readonly="readonly"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_ALSTKY">할당전략키</dt>
						<dd>
							<input type="text" class="input" name="ALSTKY" UIInput="R,SHALSTH" />
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
										<td GH="40 STD_WAREKY" GCol="text,WAREKY"></td>
										<td GH="100 STD_ALSTKY" GCol="input,ALSTKY" validate="required,MASTER_M0097" GF="S 10"></td>
										<td GH="180 STD_SHORTX" GCol="input,SHORTX" validate="required" GF="S 180"></td>
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
					<li><a href="#tab1-1"><span>상세리스트</span></a></li>
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
										<td GH="80 STD_STEPNO" GCol="input,STEPNO" validate="duplication" GF="S 3" onKeyPress="return numkeyCheck(event)"></td>
										<td GH="100 STD_ALFTKY" GCol="input,ALFTKY,SHRLRRH" validate="required" GF="S 10"></td> <!-- 할당필터링키 -->
										<td GH="100 STD_SSORKY" GCol="input,SSORKY,SHSTSRH" validate="required" GF="S 10"></td> <!-- 재고정렬키 -->
										<td GH="100 STD_ALORTO" GCol="input,ALORTO" validate="max(100)" GF="N 3"></td>
										<td GH="100 STD_ALUNIT" GCol="input,ALUNIT"></td>
										<td GH="100 STD_SUALKY" GCol="input,SUALKY" GF="S 10"></td>
										<td GH="100 STD_WAREKY" GCol="text,WAREKY"></td>
										<td GH="100 STD_ALSTKY" GCol="text,ALSTKY"></td>
										<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D"></td>
										<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T"></td>
										<td GH="100 STD_CREUSR" GCol="text,CREUSR"></td>
										<td GH="100 STD_CUSRNM" GCol="text,CREUNM"></td>
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