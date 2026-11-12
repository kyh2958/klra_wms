<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp"%>
<script type="text/javascript">
	var itemListMap = new DataMap();
	var searchYN = "Y";
	var nowHeadIndex = 0;
	var itemList = [];
	var rangelist = [ "LOTA01", "LOTA02", "LOTA03", "AREAKY", "ZONEKY", "LOCAKY" ];
	var rngvoplist = ["BW", "NB", "EQ", "NE", "LT", "GT", "LE", "GE", "LS", "TS"];
	var operlist = ["E", "N", "E", "N", "LT", "GT", "LE", "GE", "LS", "TS"];
	
	$(document).ready(function() {
		$('input[type="text"][name="LOTA11MONTH"]').prop('disabled', 'disabled');
		$('input[type="checkbox"][name="LOTA11"]').click(function(){
			if ($(this).prop('checked')) {
				$('input[type="checkbox"][name="LOTA11"]').prop('checked', false);
				if($(this).attr('id') == "LOTA11MNCK"){					
					$('input[type="text"][name="LOTA11MONTH"]').prop('disabled', '');
				} else {
					$('input[type="text"][name="LOTA11MONTH"]').val('');
					$('input[type="text"][name="LOTA11MONTH"]').prop('disabled', 'disabled');
				}
				$(this).prop('checked', true);
				$(this).prop('disabled', '');
			} else {
				$('input[type="text"][name="LOTA11MONTH"]').val('');
				$('input[type="text"][name="LOTA11MONTH"]').prop('disabled', 'disabled');
			}
		});
		
		gridList.setGrid({
			id : "gridList",
			editable : true,
			pkcol : "WAREKY,LOTRKY",
			module : "WmsAdmin",
			command : "TF01",
			emptyMsgType : false
		});
		
		searchList();
	});
	
	function commonBtnClick(btnName) {
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Create"){
			create();
		}else if(btnName == "POP_Confirm"){
			confirmCreatePopup();
		}else if(btnName == "POP_Close"){
			closeCreatePopup();
		}else if(btnName == "Save"){
			saveData();
		}
	}
	
	function searchList() {
		itemListMap = new DataMap();
		nowHeadIndex = 0;
		searchYN = "Y";
		clearData();
		
		gridList.resetGrid("gridList");
		
		var param = inputList.setRangeParam("searchArea");
		param.put("WAREKY","<%=wareky%>");
		gridList.gridList({
			id : "gridList",
			param : param,
			command : "RLRRH"
		});
	}

	function create() {
		if(!commonUtil.msgConfirm(configData.MSG_MASTER_LOST_MODIFY_CREATE_NEW_CONFIRM)){
			return;
		}
		
		$(".createContainer").css("display", "block");
		$("#WAREKY").val("<%=wareky%>");
		$("#LOTRKY").val("");
		$("#SHORTX").val("");
	}
	
	function confirmCreatePopup(){
		var wareky = $("#WAREKY").val();
		var lotrky = $("#LOTRKY").val();
		var shortx = $("#SHORTX").val();
		if(wareky.trim() == ""){
			alert(commonUtil.getMsg("COMMON_M0009", [commonLabel.getLabel("STD_WAREKY", 2)]));
			return;
		}else if(lotrky.trim() == ""){
			alert(commonUtil.getMsg("COMMON_M0009", [commonLabel.getLabel("STD_LOTRKY", 2)]));
			return;
		}else if(shortx.trim() == ""){
			alert(commonUtil.getMsg("COMMON_M0009", [commonLabel.getLabel("STD_SHORTX", 2)]));
			return;
		}
		
		var param = new DataMap();
		param.put("WAREKY", wareky);
		param.put("LOTRKY", lotrky);
		param.put("SHORTX", shortx);
		
		var json = netUtil.sendData({
			url : "/wms/admin/json/createTF01.data",
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
			url : "/wms/admin/json/TF01.data",
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
		}
	}
	
	function gridListEventRowDblclick(gridId, rowNum, colName, colValue){
		if(gridId == "gridList"){
			if(searchYN == "Y"){
				searchYN = "N";
			}else{
				tempPutItemList();
			}
			nowHeadIndex = rowNum;
			
			var wareky = gridList.getColData(gridId, rowNum, "WAREKY");
			var lotrky = gridList.getColData(gridId, rowNum, "LOTRKY");
			var key = wareky + "/" + lotrky;
			
			clearData();
			if(!itemListMap.containsKey(key)) {
				var param = new DataMap();
				param.put("WAREKY", wareky);
				param.put("LOTRKY", lotrky);
				
				if(lotrky == null || lotrky.trim() == ""){
					return;
				}
				
				// bind item data
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "TF01SUB",
					bindId : "tabs1-2",
					sendType : "list",
					bindType : "field",
					param : param
				});
				
				itemList = json.data;
			} else {
				itemList = [];
				var tempMap = itemListMap.get(key);
				for(var i = 0; i < tempMap.length; i++){
					itemList.push(tempMap[i].map);
				}
			} 
			
			$.each(itemList, function(i, obj) {
				var name = obj.DBFILD.toUpperCase();
				if (name == "LOTA11") {
					if(obj.RNGVOP == "LS"){
						$("#LOTA11LSCK").prop("checked", true);
						$("#LOTA11TSCK").prop("checked", false);
						$("#LOTA11MNCK").prop("checked", false);
					} else if(obj.RNGVOP == "TS"){
						$("#LOTA11LSCK").prop("checked", false);
						$("#LOTA11TSCK").prop("checked", true);
						$("#LOTA11MNCK").prop("checked", false);
					} else if(obj.RNGVOP == "GT"){
						$("#LOTA11LSCK").prop("checked", false);
						$("#LOTA11TSCK").prop("checked", false);
						$("#LOTA11MNCK").prop("checked", true);
						$("#LOTA11MONTH").prop("disabled", '');
						$("#LOTA11MONTH").val(obj.RNGVMI);
					}
				} else if (name == "LOTA13") {
					var rngvop = obj.RNGVOP;
					if (rngvop == "LT") {
						$("#LOTA13RD").val(obj.RNGVMI);
					}
				} else if (name == "TKZONE"){
					$("[name=TKZONE]").val(obj.RNGVMI).prop("selected", true);
				} else if( name == "STKQTY"){
					$("[name=STKQTY]").val(obj.RNGVMI).prop("selected", true);
				}
			});
			
			$.each(rangelist, function(num, data) {
				var rangeList = new Array();
				var rngvop = "";
				$.each(itemList, function(i, obj) {
					if (obj.DBFILD.toUpperCase() == data) {
						var mangeMap = new DataMap();
						rngvop = obj.RNGVOP;
						for(var i = 0; i < rngvoplist.length; i++){
							if(rngvop == rngvoplist[i]){
								if(i < 2){
									mangeMap.put("OPER", operlist[i]);
									mangeMap.put("LOGICAL", obj.LGCOPT);
									mangeMap.put("FROM", obj.RNGVMI);
									mangeMap.put("TO", obj.RNGVMX);
								} else {
									mangeMap.put("OPER", operlist[i]);
									mangeMap.put("LOGICAL", obj.LGCOPT);
									mangeMap.put("DATA", obj.RNGVMI);
								}
								break;
							}
						}
						rangeList.push(mangeMap);
					}
				});
				
				if (rngvop == "BW" ||  rngvop == "NB") {
					inputList.setRangeData(data,
							configData.INPUT_RANGE_TYPE_RANGE, rangeList);
				} else {
					inputList.setRangeData(data,
							configData.INPUT_RANGE_TYPE_SINGLE, rangeList);
				}
			});
			
		}
	}
	
	function tempPutItemList(){
		var wareky = gridList.getColData("gridList", nowHeadIndex, "WAREKY");
		var lotrky = gridList.getColData("gridList", nowHeadIndex, "LOTRKY");
		var key = wareky + "/" + lotrky;
		
		if(typeof wareky != "undefined" && typeof lotrky != "undefined" && typeof itemList != "undefined"){
			var list = dataBind.paramData("tabs1-2");
			$.each(rangelist, function(i, obj) {
				var singleListLot = inputList.getRangeData(obj,
						configData.INPUT_RANGE_TYPE_SINGLE);
				var rangeListLot = inputList.getRangeData(obj,
						configData.INPUT_RANGE_TYPE_RANGE);
				if (singleListLot.length > 0) {
					list.put(obj, singleListLot);
				} else {
					list.put(obj, rangeListLot);
				}
			});
			
			var inputDataList = new Array();
			var keyset = list.keys();
			for(var i = 0; i < keyset.length; i++){
				if(keyset[i] == "LOTA11MONTH" || keyset[i] == "LOTA13RD") {
					continue;
				}
				if(rangelist.includes(keyset[i])){
					var curData = list.get(keyset[i]);
					var arrlen = curData.length;
					for(var ii = 0; ii < arrlen; ii++){
						var inputValue = new DataMap();
						inputValue.put("WAREKY", wareky);
						inputValue.put("LOTRKY", lotrky);
						inputValue.put("DBFILD", keyset[i]);
						var index = 0;
						if(curData[ii].get("DATA")){
							if(curData[ii].get("OPER") == "E"){
								index = 2;
							} else if(curData[ii].get("OPER") == "N"){
								index = 3;
							} else {
								for(var fidx = 4; fidx < operlist.length; fidx++){
									if(curData[ii].get("OPER") == operlist[fidx]){
										index = fidx;
										break;
									}
								}
							}
							inputValue.put("RNGVOP", rngvoplist[index]);
							inputValue.put("RNGVMI", curData[ii].get("DATA"));
							inputValue.put("RNGVMX", " ");
							inputValue.put("LGCOPT", curData[ii].get("LOGICAL"));
						} else {
							if(curData[ii].get("OPER") == "E"){
								index = 0;
							} else if(curData[ii].get("OPER") == "N"){
								index = 1;
							}
							inputValue.put("RNGVOP", rngvoplist[index]);
							inputValue.put("RNGVMI", curData[ii].get("FROM"));
							inputValue.put("RNGVMX", curData[ii].get("TO"));
							inputValue.put("LGCOPT", curData[ii].get("LOGICAL"));
						}
						inputDataList.push(inputValue);
					}
				} else {
					var inputValue = new DataMap();
					inputValue.put("WAREKY", wareky);
					inputValue.put("LOTRKY", lotrky);
					inputValue.put("DBFILD", keyset[i]);
					if(keyset[i] == "TKZONE" || keyset[i] == "STKQTY"){
						inputValue.put("RNGVOP", "EQ");
						inputValue.put("RNGVMI", list.get(keyset[i]));
					} else if(keyset[i] == "LOTA11"){
						if($("#LOTA11LSCK").prop("checked")){
							inputValue.put("RNGVOP", "LS");
							inputValue.put("RNGVMI", "0");
						} else if($("#LOTA11TSCK").prop("checked")){
							inputValue.put("RNGVOP", "TS");
							inputValue.put("RNGVMI", "1");
						} else if($("#LOTA11MNCK").prop("checked")){
							inputValue.put("RNGVOP", "GT");
							inputValue.put("RNGVMI", $("#LOTA11MONTH").val());
						}
					} else if(keyset[i] == "LOTA13"){
						inputValue.put("RNGVOP", "LT");
						inputValue.put("RNGVMI", $("#LOTA13RD").val());
					}
					inputValue.put("RNGVMX", " ");
					inputValue.put("LGCOPT", "OR");
					inputDataList.push(inputValue);
				}
			}
			itemList = inputDataList;
			itemListMap.put(key, itemList);
		}
	}
	
	function clearData(){
		/* 이전에 조회되었던 input 데이터값 삭제 */
		$("[name=SHORTX]").val("");
		$("#LOTA11LSCK").prop("checked", false);
		$("#LOTA11TSCK").prop("checked", false);
		$("#LOTA11MNCK").prop("checked", false);
		$('input[type="text"][name="LOTA11MONTH"]').val('');
		$('input[type="text"][name="LOTA11MONTH"]').prop('disabled', 'disabled');
		$("#LOTA11MONTH").val("");
		$("#LOTA13RD").val("");
		$("[name=STKQTY] option:eq(0)").attr("selected", "selected");
		
		inputList.resetRange(configData.INPUT_RANGE_TYPE_RANGE);
		inputList.resetRange(configData.INPUT_RANGE_TYPE_SINGLE);
	}

	function searchHelpEventOpenBefore(searchCode, gridType) {
		var param = new DataMap();
		
		if (searchCode == "SHWAHMA") {
			param = dataBind.paramData("searchArea");
		} else if (searchCode == "SHAREMA") {
			param = dataBind.paramData("searchArea");
		} else if (searchCode == "SHZONMA") {
			param = dataBind.paramData("searchArea");
		} else if (searchCode == "SHLOCMA") {
			param = dataBind.paramData("searchArea");
		}else if(searchCode=="SHTKZONE"){
			param.put("WAREKY", "<%=wareky%>");
			param.put("ZONETY", "TASK");
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
				var lotrky = gridList.getColData(gridId, rowNum, "LOTRKY");
				
				var param = new DataMap();
				param.put("WAREKY", wareky);
				param.put("LOTRKY", lotrky);
				
				var json = netUtil.sendData({
					url : "/wms/admin/json/deleteTF01.data",
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
							<th CL="STD_LOTRKY">재고 정렬 키</th>
							<td><input type="text" name="LOTRKY" id="LOTRKY" validation="required" /></td> 
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
	<!-- content -->
	<div class="content">
		<div class="innerContainer" id="content">
			<!-- contentContainer -->
			<div class="contentContainer">
				<div class="bottomSect top">
					<div class="tabs">
						<ul class="tab type2">
							<li><a href="#tabs1-1"><span CL="STD_LIST">리스트</span></a></li>
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
													
													<td GH="40 STD_DELETE"  GCol="btn2,DELETE" GB="Delete delete_icon DELETE"></td>
													<td GH="80 STD_WAREKY"  GCol="text,WAREKY">센터</td>
													<td GH="100 STD_LOTRKY" GCol="text,LOTRKY" GF="S 10">할당필터링키</td>
													<td GH="100 STD_SHORTX" GCol="input,SHORTX" GF="S 180">설명</td>
													
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
				<!--  -->
				<div class="bottomSect bottom">
					<div class="tabs">
						<ul class="tab type2" id="commonMiddleArea"">
							<li><a href="#tabs1-1"><span CL='STD_SEARCH'>조회</span></a></li>
						</ul>
						<div id="tabs1-2">
							<div class="section type1" style="overflow: scroll">
								<div>
									<div class="table type1">
										<!-- 	Lot 속성 -->
										<div class="searchInBox">
											<table>
												<tr>
													<th><h2 class="tit" CL="STD_LOTAAT"></h2></th>
													<td></td>
												</tr>
												<tr>
													<th CL="STD_LOTA01">S/N번호</th>
													<td><input type="text" name="LOTA01" UIInput="R" UIFormat="S 20"/></td>
												</tr>
												<tr>
													<th CL="STD_LOTA02">재고유형</th>
													<td><input type="text" name="LOTA02" UIInput="R" UIFormat="S 20"/></td>
												</tr>
												<tr>
													<th CL="STD_LOTA03">벤더</th>
													<td><input type="text" name="LOTA03" UIInput="R" UIFormat="S 20"/></td>
												</tr>
											</table>
										</div>
										<div class="searchInBox">
											<table>
												<tr>
													<th><h2 class="tit" CL="STD_PRODDT"></h2></th>
													<td></td>
													<th width="30%"><h2 class="tit"></h2></th>
													<td></td>
												</tr>
												<tr>
													<th CL="STD_LSYEAR">전년도</th>
													<td><input type="checkbox" name="LOTA11" value="V" id="LOTA11LSCK"/></td>
													<th><h2 class="tit"></h2></th>
													<td></td>
												</tr>
												<tr>
													<th CL="STD_TSYEAR">당해년도</th>
													<td><input type="checkbox" name="LOTA11" value="V" id="LOTA11TSCK"/></td>
													<th><h2 class="tit"></h2></th>
													<td></td>
												</tr>
												<tr>
													<th CL="STD_WHNDAY">월기한</th>
													<td>
														<input type="checkbox" name="LOTA11" value="V" id="LOTA11MNCK"/>
														<input type="text" name="LOTA11MONTH" UIFormat="N 10" id="LOTA11MONTH"/><label CL="STD_PERMON"></label>
													</td>
													<th><h2 class="tit"></h2></th>
													<td></td>
												</tr>
											</table>
											<br/>
											<table>
												<tr>
													<th><h2 class="tit" CL="STD_LTDTMN">유통기한</h2></th>
													<td></td>
													<th width="30%"><h2 class="tit"></h2></th>
													<td></td>
												</tr>
												<tr>
													<th CL="STD_WSD_EXPRTN_DE_DAYCNT">유통기한잔여일수</th>
													<td><input type="text" name="LOTA13" UIFormat="N 10" id="LOTA13RD"/><label CL="STD_DAY"></label></td>
													<th><h2 class="tit"></h2></th>
													<td></td>
												</tr>
											</table>
										</div>
										<div class="searchInBox">
											<table>
												<tr>
													<th><h2 class="tit" CL="STD_POSINFO"></h2></th>
													<td></td>
												</tr>
												<tr>
													<th CL="STD_AREAKY">창고</th>
													<td><input type="text" name="AREAKY" id="AREAKY" UIInput="R,SHAREMA" UIFormat="S 10"/></td>
												</tr>
												<tr>
													<th CL="STD_ZONEKY">구역</th>
													<td><input type="text" name="ZONEKY" UIInput="R,SHZONMA" UIFormat="S 10"/></td>
												</tr>
												<tr>
													<th CL="STD_LOCAKY">지번</th>
													<td><input type="text" name="LOCAKY" UIInput="R,SHLOCMA" UIFormat="S 10"/></td>
												</tr>
												<tr>
													<th CL="STD_TKZONE">작업구역</th>
													<td><input type="text" name="TKZONE" id="TKZONE" UIInput="R,SHZONMA" UIFormat="S 10"/></td>
												</tr>	
											</table>
										</div>
										<div class="searchInBox">
											<table>
												<tr>
													<th><h2 class="tit" CL="TAB_QTYINFO"></h2></th>
													<td></td>
												</tr>
												<tr>
													<th CL="STD_PICKWAY">피킹방식</th>
													<td GCol="select,STKQTY"><select CommonCombo="STKQTY" name="STKQTY">
															<option value=" "></option>
													</select>
													&nbsp&nbsp 팔렛피킹 or 낱개피킹
													</td>
												</tr>
											</table>
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
     </div>
	<!-- //content -->
<%@ include file="/common/include/webdek/bottom.jsp"%>
</body>
</html>