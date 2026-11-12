<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ page import="project.common.airfw.*"%>
<%@ include file="/common/include/webdek/head.jsp"%>
<script type="text/javascript">
	var itemListMap = new DataMap();
	var nowHeadIndex = 0;
	var searchYN = "Y";
	
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
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Create"){
			create();
		}else if(btnName == "POP_Confirm"){
			confirmCreatePopup();
		}else if(btnName == "POP_Close"){
			closeCreatePopup();
		}
	}

	function searchList() {
		searchYN = "Y";
// 		if(validate.check("searchArea")){
			itemListMap = new DataMap();
			nowHeadIndex = 0;
			gridList.resetGrid("gridList");
			gridList.resetGrid("gridListSub");
			
			var param = inputList.setRangeParam("searchArea");
			param.put("WAREKY","<%=wareky%>");
			
			gridList.gridList({
				id : "gridList",
				param : param,
				command : "STSRH"
			});
// 		}
	}
	
	function create(){
		if(!commonUtil.msgConfirm(configData.MSG_MASTER_LOST_MODIFY_CREATE_NEW_CONFIRM)){
			return;
		}
		
		tempPutItemList();
		$(".createContainer").css("display", "block");
		$("#WAREKY").val("<%=wareky%>");
		$("#SSORKY").val("");
		$("#SHORTX").val("");
	}
	
	function confirmCreatePopup(){
		var wareky = $("#WAREKY").val();
		var ssorky = $("#SSORKY").val();
		var shortx = $("#SHORTX").val();
		if(wareky.trim() == ""){
			alert(commonUtil.getMsg("COMMON_M0009", [commonLabel.getLabel("STD_WAREKY", 2)]));
			return;
		}else if(ssorky.trim() == ""){
			alert(commonUtil.getMsg("COMMON_M0009", [commonLabel.getLabel("STD_SSORKY", 2)]));
			return;
		}else if(shortx.trim() == ""){
			alert(commonUtil.getMsg("COMMON_M0009", [commonLabel.getLabel("STD_SHORTX", 2)]));
			return;
		}
		
		var param = new DataMap();
		param.put("WAREKY", wareky);
		param.put("SSORKY", ssorky);
		param.put("SHORTX", shortx);
		
		var json = netUtil.sendData({
			url : "/wms/admin/json/createTS01.data",
			param : param
		});
		
		if(json && json.data){
			if(json.data != "OK") {
				alert("신규 생성 실패");
				return;
			}
		}
		
		searchList();
		
		closeCreatePopup();
	}
	
	function closeCreatePopup(){
		$(".createContainer").css("display", "none");
	}

	function saveData() {
		tempPutItemList();
		
		var list = gridList.getGridData("gridList");
		if(list.length == 0){
			alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEEMPTY));
			return;
		}
				
		if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
			return;
		}
		
		var param = new DataMap();
		param.put("list", list);
		param.put("itemlist", itemListMap);
		
		var json = netUtil.sendData({
			url : "/wms/admin/json/saveTS01.data",
			param : param
		});
		
		if(json && json.data){
			if(json.data == "OK") {
				searchList();
				alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEOK));
			} else {
				alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEFAIL) + " [" + json.data + "]");
			}
		}
	}
	
	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList" && dataLength > 0){
			itemListMap = new DataMap();
			gridListEventRowDblclick(gridId, 0, "", "");
		}else if(gridId == "gridListSub") {
			tempPutItemList();
		}
	}

	function gridListEventRowDblclick(gridId, rowNum, colName, colValue){
		if(gridId == "gridList"){
// 			alert(searchYN);
			if(searchYN == "Y"){
				searchYN = "N";
			}else{
				tempPutItemList();
			}
			
			nowHeadIndex = rowNum;
			
			var wareky = gridList.getColData(gridId, rowNum, "WAREKY");
			var ssorky = gridList.getColData(gridId, rowNum, "SSORKY");
			var key = wareky + "/" + ssorky;
			
			gridList.resetGrid("gridListSub");
			if(!itemListMap.containsKey(key)) {
				var param = inputList.setRangeParam("searchArea");
				param.put("WAREKY", wareky);
				param.put("SSORKY", ssorky);
				
				if(ssorky == null || ssorky.trim() == ""){
					return;
				}
				gridList.gridList({
					id : "gridListSub",
					param : param,
					command : "STSRI"
				});
			} else {
				var itemlist = itemListMap.get(key);
				gridList.appendList("gridListSub", itemlist, true);
				
				var list = gridList.getGridData("gridListSub");
				for(var i = itemlist.length-1; i >= 0; i--){
					var rowState = itemlist[i].get(configData.GRID_ROW_STATE_ATT);
					if(rowState == configData.GRID_ROW_STATE_DELETE){
						gridList.deleteRow("gridListSub", i, false);
					}else if(rowState == configData.GRID_ROW_STATE_INSERT || rowState == configData.GRID_ROW_STATE_UPDATE){
						gridList.setColValue("gridListSub", i, configData.GRID_ROW_STATE_ATT, gridList.getColData("gridListSub", i, configData.GRID_ROW_STATE_ATT));
					}
				}
				
				gridList.setRowFocus("gridListSub", 0, false);
			}
		}
	}
	
	function tempPutItemList(){
		var wareky = gridList.getColData("gridList", nowHeadIndex, "WAREKY");
		var ssorky = gridList.getColData("gridList", nowHeadIndex, "SSORKY");
		var key = wareky + "/" + ssorky;
		
		var itemList = gridList.getGridData("gridListSub");
		
		if(itemList != null && typeof key != "undefined" && typeof itemList != "undefined"){
			itemListMap.put(key, itemList);
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
	
	function gridListEventRowCopyAfter(gridId, rowNum){
		if(gridId == "gridList") {
		}
	}
	
	function gridListEventRowAddBefore(gridId, rowNum){
		var newData = new DataMap();
	
		if(gridId == "gridList") {
			
		}else if(gridId == "gridListSub") {
			
		}
		
		return newData;
	}
	
	function gridListEventRowAddAfter(gridId, rowNum){
		if(gridId == "gridList") {
			tempPutItemList();
			
			gridList.resetGrid("gridListSub");
		}
	}
	
	function gridListEventRowRemove(gridId, rowNum){
		var rowAea = gridList.getColData("gridListSub", rowNum, "SSORKY");
		if(gridId == "gridList"){
			var param = new DataMap();
			param.put("WAREKY", "<%=wareky%>");
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
	
 	function searchHelpEventOpenBefore(searchCode, gridType){
 		var param = new DataMap();
 		
		if(searchCode=="SHWAHMA"){
			param =  dataBind.paramData("searchArea");
		}else if(searchCode == "SHAREMA"){
			param = dataBind.paramData("searchArea");
		}
		
		return param;
	}
 	
 	function  gridListEventColBtnclick(gridId, rowNum, btnName){
		if(gridId == "gridList"){
			if(btnName == "Delete"){
				if(!commonUtil.msgConfirm(configData.MSG_MASTER_DELETE_CONFIRM)){
					return;
				}
				
				var wareky = gridList.getColData(gridId, rowNum, "WAREKY");
				var ssorky = gridList.getColData(gridId, rowNum, "SSORKY");
				
				var param = new DataMap();
				param.put("WAREKY", wareky);
				param.put("SSORKY", ssorky);
				
				var json = netUtil.sendData({
					url : "/wms/admin/json/deleteTS01.data",
					param : param
				});
				
				if(json && json.data && json.data == "OK"){
					searchList();
				} else {
					alert(json.data);
				}
			}
		}
	}
 	
</script>
</head>
<body>
<div class="createContainer"> 
	<div class="createContent">
		<div class="createHeader">
			<button CB="POP_Confirm EXPAND STD_CONFIRM"></button>
			<button CB="POP_Close CLOSE BTN_CLOSE"></button>
		</div>
		<div class="createBody section type1">
			<div class="searchInBox">
				<table class="table type1">
					<colgroup>
						<col width="120px" />
						<col />
					</colgroup>
					<tr>
						<th><h2 class="tit" CL="STD_ADD"></h2></th>
					</tr>
					<tr>
						<th CL="STD_WAREKY">거점</th>
						<td><input type="text" name="WAREKY" id="WAREKY" value="<%=wareky%>" readonly="readonly"/></td>
					</tr> 
					<tr>
						<th CL="STD_SSORKY">재고 정렬 키</th>
						<td><input type="text" name="SSORKY" id="SSORKY" validation="required" /></td> 
					</tr> 
					<tr>
						<th CL="STD_SHORTX">설명</th>
						<td><input type="text" name="SHORTX" id="SHORTX" style="width:175px" validation="required" /></td>
					</tr> 
				</table>
			</div>
		</div>
	</div>
</div>

<div class="contentHeader">
	<div class="util">
		<button id="SEARCH" CB="Search SEARCH BTN_DISPLAY"></button>
		<button CB="Create CREATE BTN_CREATE"></button>
		<button CB="Save SAVE STD_SAVE"></button>
	</div>
</div>

<div class="content" >
	<div class="innerContainer">

		<!-- contentContainer -->
		<div class="contentContainer">
			<div class="bottomSect top">
				<div class="tabs">
					<ul class="tab type2">
						<li><a href="#tabs1-1" CL="STD_LIST"><span>리스트</span></a></li>
					</ul>
					<div id="tabs1-1">
						<div class="section type1">
							<div class="table type2">
								<div class="tableBody">
									<table>
										<tbody id="gridList">
											<tr CGRow="true">
												<td GH="40" GCol="rownum">1</td>
<!-- 												<td GH="40" GBtnCheck="true" GCol="rowCheck"></td> -->
												<td GH="40 STD_DELETE" GCol="btn2,DELETE" GB="Delete delete_icon DELETE"></td>
												<td GH="100 STD_WAREKY" GCol="text,WAREKY"></td>
												<td GH="100 STD_SSORKY" GCol="text,SSORKY" GF="S 10"></td>
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
							<div class="tableUtil">
								<div class="leftArea">	
									<button type="button" GBtn="find"></button>
									<button type="button" GBtn="sortReset"></button>
<!-- 									<button type="button" GBtn="copy"></button> -->
<!-- 									<button type="button" GBtn="add"></button> -->
<!-- 									<button type="button" GBtn="delete"></button> -->
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
				</div>
			</div>

			<div class="bottomSect bottom">
				<button type="button" class="button type2 fullSizer">
					<img src="/common/images/ico_full.png" alt="Full Size">
				</button>
				<div class="tabs">
					<ul class="tab type2" id="commonMiddleArea">
						<li><a href="#tabs2-1" CL="STD_ITEMLIST" ><span>Item리스트</span></a></li>
					</ul>
					<div id="tabs1-1">
						<div class="section type1">
							<div class="table type2">
								<div class="tableBody">
									<table>
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
							<div class="tableUtil">
								<div class="leftArea">	    
									<button type="button" GBtn="find"></button>
									<button type="button" GBtn="sortReset"></button>
									<!-- <button type="button" GBtn="copy"></button>
									<button type="button" GBtn="delete"></button> -->
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