<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp"%>
<script type="text/javascript">
	var dblIdx = 0;
	$(document).ready(function(){
		setTopSize(300);
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
	<div class="contentHeader">
		<div class="util">
			<button CB="Search SEARCH BTN_DISPLAY">
			</button>
			<button CB="Save SAVE STD_SAVE">
			</button>
		</div>
		<div class="util2">
			<button class="button type2" id="showPop" type="button">
				<img src="/common/images/ico_btn4.png" alt="List" />
			</button>
		</div>
	</div>

	<!-- searchPop -->
	<div class="searchPop" id="searchArea">
		<button type="button" class="closer">X</button>
		<div class="searchInnerContainer">
			<p class="util">
				<button CB="Search SEARCH BTN_DISPLAY"></button>
				<!-- <button CB="GetVariant GETVARIANT BTN_GETVARIANT"></button>
				<button CB="SaveVariant SAVEVARIANT BTN_SAVEVARIANT"></button> -->
			</p>
			
			<div class="searchInBox">
				<h2 class="tit" CL="STD_SELECTOPTIONS">검색조건</h2>
				<table class="table type1">
					<colgroup>
						<col width="100" />
						<col />
					</colgroup>
					<tbody>
						<tr>
							<th CL="STD_WAREKY">거점</th>
							<td><input type="text" name="WAREKY" value="<%=wareky%>"
								readonly="readonly"  /></td>
						</tr>
						<tr>
							<th CL="STD_ALSTKY">할당전략키</th>
							<td><input type="text" name="ALSTKY" UIInput="R,SHALSTH" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- //searchPop -->

	<!-- content -->
	<div class="content">
		<div class="innerContainer">

			<!-- contentContainer -->
			<div class="contentContainer">

				<div class="bottomSect top">
					<button type="button" class="button type2 fullSizer">
						<img src="/common/images/ico_full.png" alt="Full Size">
					</button>
					<div class="tabs">
						<ul class="tab type2">
							<li><a href="#tabs1-1"><span CL='STD_LIST'>리스트</span></a></li>
							<li style="display:none"><a href="#tabs1-2"><span CL='STD_DETAIL'>상세</span></a></li>
							<!--li><a href="#tabs1-3"><span value="Item상세">관리</span></a></li-->
						</ul>
						<div id="tabs1-1">
							<div class="section type1">
								<div class="table type2">
									<div class="tableHeader">
										<table>
											<colgroup>
												<col width="40" />
												<col width="40" />
												<col width="100" />
												<col width="180" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
											</colgroup>
											<thead>
												<tr>
													<th CL='STD_NUMBER'></th>
													<th CL='STD_WAREKY'></th>
													<th CL='STD_ALSTKY'></th>
													<th CL='STD_SHORTX'></th>
													<th CL='STD_CREDAT'></th>
													<th CL='STD_CRETIM'></th>
													<th CL='STD_CREUSR'></th>
													<th CL='STD_CUSRNM'></th>
													<th CL='STD_LMODAT'></th>
													<th CL='STD_LMOTIM'></th>
													<th CL='STD_LMOUSR'></th>
													<th CL='STD_LUSRNM'></th>
												</tr>
											</thead>
										</table>
									</div>
									<div class="tableBody">
										<table>
											<colgroup>
												<col width="40" />
												<col width="40" />
												<col width="100" />
												<col width="180" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
											</colgroup>
											<tbody id="gridList">
												<tr CGRow="true">
													<td GCol="rownum">1</td>
													<td GCol="text,WAREKY"></td>
													<td GCol="input,ALSTKY" validate="required,MASTER_M0097"
														GF="S 10"></td>
													<td GCol="input,SHORTX" validate="required" GF="S 180"></td>
													<td GCol="text,CREDAT" GF="D"></td>
													<td GCol="text,CRETIM" GF="T"></td>
													<td GCol="text,CREUSR"></td>
													<td GCol="text,CUSRNM"></td>
													<td GCol="text,LMODAT" GF="D"></td>
													<td GCol="text,LMOTIM" GF="T"></td>
													<td GCol="text,LMOUSR"></td>
													<td GCol="text,LUSRNM"></td>
												</tr>
											</tbody>
										</table>
									</div>

								</div>
								<div class="tableUtil">
									<div class="leftArea">
										<button type="button" GBtn="find"></button>
										<button type="button" GBtn="sortReset"></button>
										<button type="button" GBtn="copy"></button>
										<button type="button" GBtn="add"></button>
										<button type="button" GBtn="delete"></button>
										<button type="button" GBtn="layout"></button>
										<!-- <button type="button" GBtn="total"></button> -->
										<button type="button" GBtn="excel"></button>
									</div>
									<div class="rightArea">
										<p class="record" GInfoArea="true"></p>
									</div>
								</div>
							</div>
						</div>
						<!--  -->
						<!-- <div id="tabs1-2">
							<div class="section type1 hei200">
								<div class="searchInBox">
									<div class="table type1">
										<table>
											<colgroup>
												<col width="100" />
												<col />
												<col width="100" />
												<col />
											</colgroup>
											<tbody id="gridList">
												<tr>
													<th CL='STD_WAREKY'></th>
													<td><input type="text" name="WAREKY" readonly /></td>
												</tr>
												<tr>
													<th CL='STD_ALSTKY'></th>
													<td><input type="text" name="ALSTKY" readonly /></td>
												</tr>
												<tr>
													<th CL='STD_SHORTX'></th>
													<td><input type="text" name="SHORTX" /></td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div> -->
						<!-- div id="tabs1-3">
						<div class="section type1 hei200">
							<div class="searchInBox">
								<div class="table type1">
									관리
									<table>
										<tr>
											<th>생성일시</th>
											<td>
												<input type="text" /><input type="text" />
											</td>
											<th>수정일시</th>
											<td>
												<input type="text" /><input type="text" />
											</td>
										</tr>
										<tr>
											<th>생성자</th>
											<td>
												<input type="text" />
											</td>
											<th>수정자</th>
											<td>
												<input type="text" />
											</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</div>
					<!--  -->
					</div>
				</div>

				<div class="bottomSect bottom">
					<button type="button" class="button type2 fullSizer">
						<img src="/common/images/ico_full.png" alt="Full Size">
					</button>
					<div class="tabs">
						<ul class="tab type2" id="commonMiddleArea">
							<li><a href="#tabs1-1" CL='STD_DETAILLIST'><span>상세리스트</span></a></li>
						</ul>
						<div id="tabs1-1">
							<div class="section type1" style="overflow-y: scroll;">
								<div class="table type2">
									<div class="tableHeader">
										<table>
											<colgroup>
												<col width="40" />
												<col width="80" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
											</colgroup>
											<thead>
												<tr>
													<th CL='STD_NUMBER'></th>
													<th CL='STD_STEPNO'></th>
													<th CL='STD_ALFTKY'></th>
													<th CL='STD_SSORKY'></th>
													<th CL='STD_ALORTO'></th>
													<th CL='STD_ALUNIT'></th>
													<th CL='STD_SUALKY'></th>
													<th CL='STD_WAREKY'></th>
													<th CL='STD_ALSTKY'></th>
													<th CL='STD_CREDAT'></th>
													<th CL='STD_CRETIM'></th>
													<th CL='STD_CREUSR'></th>
													<th CL='STD_CUSRNM'></th>
													<th CL='STD_LMODAT'></th>
													<th CL='STD_LMOTIM'></th>
													<th CL='STD_LMOUSR'></th>
													<th CL='STD_LUSRNM'></th>
												</tr>
											</thead>
										</table>
									</div>
									<div class="tableBody">
										<table>
											<colgroup>
												<col width="40" />
												<col width="80" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
												<col width="100" />
											</colgroup>
											<tbody id="gridListSub">
												<tr CGRow="true">
													<td GCol="rownum">1</td>
													<td GCol="input,STEPNO" validate="duplication" GF="S 3" onKeyPress="return numkeyCheck(event)"></td>
													<td GCol="input,ALFTKY,SHRLRRH" validate="required" 
														GF="S 10"></td> <!-- 할당필터링키 -->
													<td GCol="input,SSORKY,SHSTSRH" validate="required"
														GF="S 10"></td> <!-- 재고정렬키 -->
													<td GCol="input,ALORTO" validate="max(100)" GF="N"></td>
													<td GCol="input,ALUNIT"></td>
													<td GCol="input,SUALKY" GF="S 10"></td>
													<td GCol="text,WAREKY"></td>
													<td GCol="text,ALSTKY"></td>
													<td GCol="text,CREDAT" GF="D"></td>
													<td GCol="text,CRETIM" GF="T"></td>
													<td GCol="text,CREUSR"></td>
													<td GCol="text,CREUNM"></td>
													<td GCol="text,LMODAT" GF="D"></td>
													<td GCol="text,LMOTIM" GF="T"></td>
													<td GCol="text,LMOUSR"></td>
													<td GCol="text,LUSRNM"></td>
												</tr>
											</tbody>
										</table>
									</div>

								</div>
								<div class="tableUtil">
									<div class="leftArea">  
										<button type="button" GBtn="find"></button>
										<button type="button" GBtn="sortReset"></button>
										<button type="button" GBtn="copy"></button>
										<button type="button" GBtn="add"></button>
										<button type="button" GBtn="delete"></button>
										<button type="button" GBtn="layout"></button>
										<button type="button" GBtn="total"></button>
										<button type="button" GBtn="excel"></button>
									</div>
									<div class="rightArea">
										<p class="record" GInfoArea="true"></p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- //contentContainer -->
		</div>
	</div>
	<!-- //content -->
	<%@ include file="/common/include/webdek/bottom.jsp"%>
</body>
</html>