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
			pkcol : "WAREKY,MEASKY",
			module : "WmsAdmin",
			command : "MEASH",
			validation : "WAREKY,MEASKY,SHORTX",
			bindArea : "tabs1-2" 
		});
		
		gridList.setGrid({
	    	id : "gridListSub",
	    	name : "gridListSub",
			editable : true,
			pkcol : "WAREKY,MEASKY,ITEMNO",
			module : "WmsAdmin",
			validation : "WAREKY,MEASKY,ITEMNO",
			command : "MEASHSub"
	    });
		//gridList.setReadOnly('gridListSub',true, ['ITEMNO','QTPUOM','UOMKEY','INDDFU','DISREC','DISSHP','DISTAS']);
		gridList.setReadOnly('gridListSub',true, ['ITEMNO','INDDFU','DISREC','DISSHP','DISTAS']);
	});

	function searchList(){
		var param = inputList.setRangeParam("searchArea");
		gridList.gridList({
	    	id : "gridList",
	    	param : param
	    });
	}
	
	function searchSubList(headRowNum){
		var param = inputList.setRangeParam("searchArea");
		var rowVal = gridList.getColData("gridList", headRowNum, "WAREKY");
		var rowMea = gridList.getColData("gridList", headRowNum, "MEASKY");
		param.put("WAREKY", rowVal);
		param.put("MEASKY", rowMea);
		
		gridList.gridList({
			id : "gridListSub",
			param : param
		});
		
		lastFocusNum = headRowNum;
		dblIdx = headRowNum;
	}
	
	var lastFocusNum = -1;
	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList" && dataLength > 0){
			searchSubList(0);
		}
	}
	
	// 상단 그리드 더블 클릭시 하단 그리드 조회
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
				if(confirm(commonUtil.getMsg("COMMON_M0024"))){   //값이 변화된 데이터가 있습니다. 이동하시겠습니까
					gridList.resetGrid("gridListSub");
				}else{
					return false;
				}
			}
		}
	}
	
	function gridListEventRowRemove(gridId, rowNum){
		if(gridId == "gridList"){
			gridList.resetGrid("gridListSub");
		}
	}
	//변경.
	function gridListEventRowAddBefore(gridId, rowNum) {
		if(gridId == "gridList"){
			var headCnt = gridList.getModifyRowCount("gridList");
			var listCnt = gridList.getModifyRowCount("gridListSub");
			if(listCnt > 0 || headCnt > 0){
				//alert("변경된 row를 저장 후 행추가 하십시요.");
				commonUtil.msgBox("MASTER_M0686");
				return false;
			}else{
				var newData = new DataMap();
				newData.put("WAREKY", "<%=wareky%>");
				newData.put("STATUS", "C");
				return newData;		
			}
		} else if(gridId == "gridListSub"){
			 if(dblIdx == -1){
				var headNum = gridList.getGridDataCount("gridList") -1;
				if(gridList.getColData("gridList", headNum, "STATUS") == ""){
					commonUtil.msgBox("VALID_M0407");
					return false;
				}
	
				if(gridList.getColData("gridList", headNum, "STATUS") == "C"){
					var wareky = gridList.getColData("gridList", headNum, "WAREKY");
					var measky = gridList.getColData("gridList", headNum, "MEASKY");
					
					//gridList.setReadOnly('gridListSub',false, ['INDDFU','DISREC','DISSHP','DISTAS']);
					
					var itemno = 10;
					var lastRow = gridList.getGridDataCount("gridListSub");
					
					if(lastRow > 0){
						itemno= Number(gridList.getColData("gridListSub", lastRow-1, "ITEMNO"))+1;
					}
					
					if(itemno < 10){
						itemno = "00000" + itemno;
					}else if(itemno < 100){
						itemno = "0000" + itemno;
					}else if(itemno < 1000){
						itemno = "000" + itemno;
					}else if(itemno < 10000){
						itemno = "00" + itemno;
					}else if(itemno < 100000){
						itemno = "0" + itemno;
					}

					if(measky != ""){
							var newData = new DataMap();
							newData.put("WAREKY",wareky);
							newData.put("MEASKY",measky);
							newData.put("ITEMNO",itemno);	
							//newData.put("UOMKEY", "BOX");
							return newData;
					} else { 
						commonUtil.msgBox("VALID_M0407");
						return false;
					}
				}else{
					commonUtil.msgBox("TASK_M0003");
					return false;
				}
			}else{
				var wareky = gridList.getColData("gridList", dblIdx, "WAREKY");
				var measky = gridList.getColData("gridList", dblIdx, "MEASKY");
				var lastRow = gridList.getGridDataCount("gridListSub");
				
				var itemno = 10;
				
				if(lastRow > 0){
	                itemno= Number(gridList.getColData("gridListSub", lastRow-1, "ITEMNO"))+1;
	            }
				
				if(itemno < 10){
					itemno = "00000" + itemno;
				}else if(itemno < 100){
					itemno = "0000" + itemno;
				}else if(itemno < 1000){
					itemno = "000" + itemno;
				}else if(itemno < 10000){
					itemno = "00" + itemno;
				}else if(itemno < 100000){
					itemno = "0" + itemno;
				}
				
				var newData = new DataMap();
				newData.put("WAREKY", wareky);
				newData.put("MEASKY", measky);
				newData.put("ITEMNO", itemno);
				newData.put("UOMKEY", "BOX");

				return newData;
			}
		}
	}
	function gridListEventColValueChange(gridId, rowNum, colName, colValue) {
		if (gridId == "gridList" && colName == "MEASKY") {
			if (colValue != "") {
				var wareky = gridList.getColData("gridList", rowNum, "WAREKY");
				
				var param = new DataMap();
				param.put("MEASKY", colValue);
				param.put("WAREKY", wareky);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "SM01MEASKYval",
					sendType : "map",
					param : param
				});

				if (json.data["CNT"] >= 1) {
					gridList.setColValue("gridList", rowNum, "MEASKY", "");
					commonUtil.msgBox("VALID_M0307", colValue);  //[{0}]는 중복된 단위 구성 입니다.
					return false;
				}
			}
			
			if(gridList.getGridDataCount("gridListSub") > 0){
				var listCnt = gridList.getGridDataCount("gridList");
				if(listCnt != 0){
					for(var i = 0; i < listCnt; i++){
						gridList.setColValue("gridListSub", i, "MEASKY", colValue);
					}
				}
			}
		}else if(gridId == "gridListSub" && colName == "ITEMNO"){
			if(colValue != ""){
				var wareky = gridList.getColData("gridListSub", rowNum, "WAREKY");
				var measky = gridList.getColData("gridListSub", rowNum, "MEASKY");
				
				var param = new DataMap();
				param.put("STEPNO",colValue);
				param.put("WAREKY",wareky);
				param.put("MEASKY",measky);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "ITEMNOval",
					sendType : "map",
					param : param
				});
				
				if(json.data["CNT"] >= 1) {
					gridList.setColValue("gridListSub", rowNum, "ITEMNO", "");
					commonUtil.msgBox("MASTER_M0100");
					return false;
				}
			}
		}else if(gridId == "gridListSub" && colName == "UOMKEY"){
			if(colValue != ""){
				var wareky = gridList.getColData("gridListSub", rowNum, "WAREKY");
				var measky = gridList.getColData("gridListSub", rowNum, "MEASKY");
			}
			
			var param = new DataMap();
			param.put("WAREKY", wareky);
			param.put("MEASKY", measky);
			param.put("UOMKEY", colValue);
			
			var json = netUtil.sendData({
				module : "WmsAdmin",
				command : "UOMKEYval",
				sendType : "map",
				param : param
			});
			
			if (json.data["CNT"] > 0) {
				gridList.setColValue("gridListSub", rowNum, "UOMKEY", "");
				commonUtil.msgBox("VALID_M0320", colValue);  //[{0}]는 중복된 단위 입니다.
				return false;
			}
		}
	}

	function saveData(){
		if (gridList.getModifyRowCount("gridList") == 0
				&& gridList.getModifyRowCount("gridListSub") == 0) {
			commonUtil.msgBox("MASTER_M0545");
			return;
		}

		if (!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)) {
			return;
		}

		if (gridList.validationCheck("gridList", "modify")
				&& gridList.validationCheck("gridListSub", "modify")) {
			var headNum = gridList.getGridDataCount("gridList") - 1;
			if (gridList.getRowStatus("gridList", headNum) == "C"
					&& gridList.getGridDataCount("gridListSub") == 0) {
				commonUtil.msgBox("OUT_M0045");
				return;
			}

			var headCnt = gridList.getGridDataCount("gridList");
			for (var i = 0; i < headCnt; i++) {

				gridList.setColValue("gridList", i, "STATUS", gridList
						.getRowStatus("gridList", i));
			}
			var head = gridList.getGridData("gridList");

			var listCnt = gridList.getGridDataCount("gridListSub");
			for (var i = 0; i < listCnt; i++) {
				gridList.setColValue("gridListSub", i, "STATUS", gridList
						.getRowStatus("gridListSub", i));
			}

			var list = gridList.getGridData("gridListSub");

			var param = new DataMap();

			param.put("head", head);
			param.put("list", list);

			var json = netUtil.sendData({
				url : "/wms/admin/json/saveSM01.data",
				param : param
			});

			if (json) {
				if (json.data) {
					commonUtil.msgBox("MASTER_M0564");
					gridList.resetGrid("gridListSub");
					searchList();
				}
			}
		}
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType){
		if(searchCode == "SHAREMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHWAHMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHMEASH"){
			return dataBind.paramData("searchArea");
		}
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
			var measky = gridList.getColData("gridList", rowNum, "MEASKY");
		
			param.put("MEASKY",measky);
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
					<!-- <input type="button" CB="Save SAVE STD_SAVE" /> -->	
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_WAREKY">Center Code</dt>
						<dd>
							<input type="text" class="input" name="WAREKY" UIInput="S,SHWAHMA" value="<%=wareky%>"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_SKUKEY">단위구성</dt>
						<dd>
							<input type="text" class="input" name="A.MEASKY" UIInput="R,SHMEASH" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_DESC01">설명</dt>
						<dd>
							<input type="text" class="input" name="A.SHORTX" UIInput="R" />
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
										<td GH="90 STD_WAREKY" GCol="text,WAREKY"></td>
										<td GH="90 STD_SKUKEY" GCol="text,MEASKY"></td>
										<td GH="270 STD_DESC01" GCol="text,SHORTX"></td>
										<td GH="90 STD_CREDAT" GCol="text,CREDAT" GF="D"></td>
										<td GH="90 STD_CRETIM" GCol="text,CRETIM" GF="T"></td>
										<td GH="90 STD_CREUSR" GCol="text,CREUSR"></td>
										<td GH="90 STD_CUSRNM" GCol="text,CUSRNM"></td>
										<td GH="90 STD_LMODAT" GCol="text,LMODAT" GF="D"></td>
										<td GH="90 STD_LMOTIM" GCol="text,LMOTIM" GF="T"></td>
										<td GH="90 STD_LMOUSR" GCol="text,LMOUSR"></td>
										<td GH="90 STD_LUSRNM" GCol="text,LUSRNM"></td> 										
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
										<td GH="70 STD_ITEMNO" GCol="text,ITEMNO" validate="required" GF="S 6" >LineNo.</td>
										<td GH="80 STD_QTAUOM" GCol="text,QTAUOM"  GF="N 20,3">분모</td>
										<td GH="100 STD_QTAUOMKY" GCol="text,UOMKEY">분모단위</td>
										<td GH="100 STD_QTPUOM" GCol="text,QTPUOM"  GF="N 20,3">분자</td>
										<td GH="100 STD_QTPUOMKY" GCol="text,DUOMKY">분자단위</td> 
										<td GH="100 STD_CUBICM" GCol="text,CUBICM" GF="N 20,3">CBM</td> 
										<td GH="100 STD_INDDFU" GCol="check,INDDFU,radio" ></td>
										<td GH="100 STD_DISREC" GCol="check,DISREC,radio" ></td>
										<td GH="100 STD_DISSHP" GCol="check,DISSHP,radio" ></td>
										<td GH="100 STD_DISTAS" GCol="check,DISTAS,radio" ></td>
										
										<td GH="90 STD_CREDAT" GCol="text,CREDAT" GF="D"></td>
										<td GH="90 STD_CRETIM" GCol="text,CRETIM" GF="T"></td>
										<td GH="90 STD_CREUSR" GCol="text,CREUSR"></td>
										<td GH="90 STD_CUSRNM" GCol="text,CUSRNM"></td>
										<td GH="90 STD_LMODAT" GCol="text,LMODAT" GF="D"></td>
										<td GH="90 STD_LMOTIM" GCol="text,LMOTIM" GF="T"></td>
										<td GH="90 STD_LMOUSR" GCol="text,LMOUSR"></td>
										<td GH="90 STD_LUSRNM" GCol="text,LUSRNM"></td>
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