<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			editable : true,
			pkcol : "OWNRKY,PLNTKY,STLKY",
			module : "WmsAdmin",
			command : "STLMA",
			emptyMsgType : true
		});
		
		// 플랜트
		var $selPLNTKY = $('select[name="PLNTKY"]');
		$selPLNTKY.multipleSelect('checkAll');
		
//		gridList.setReadOnly("gridList",true, ["NEGSTK","INDCDO","INDAES"]);
		
//		searchList();
	});
 	
 	function commonBtnClick(btnName){
 		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Print"){
			printLocationBarcode();
		}else if(btnName == "Reflect"){
			setLOCAKY();
		}
	}
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			//param.put("WAREKY","<%=wareky%>")
			
			gridList.gridList({
				id : "gridList",
				param : param
			});
		}
	}
	
	function saveData(){
		if(gridList.validationCheck("gridList", "modify")){
			var list = gridList.getModifyData("gridList", "A");
			if(list.length == 0){
				alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEEMPTY));
				return;
			}
			
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
				return;
			}
			
			var param = new DataMap();
			param.put("list", list);
			
			var json = netUtil.sendData({
				url : "/wms/admin/json/saveMS01.data",
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
	}
		
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId == "gridList") {
			
			if(colName == "WAREKY") {
				if(colValue == ""){
					gridList.setColValue("gridList", rowNum, "WARENM", "");
				}else{
					var param = new DataMap();
					param.put("WAREKY",colValue);
					
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "WAREKY",
						sendType : "map",
						param : param
					});
					
					if(json && json.data){
						
						var name01 = json.data["NAME01"];
						if(name01.length > 0 && name01 != " "){
							gridList.setColValue("gridList", rowNum, "WARENM", name01);
						}else{
							gridList.setColValue("gridList", rowNum, "WAREKY", "");
							gridList.setColValue("gridList", rowNum, "WARENM", "");
						}
						
					}else{
						gridList.setColValue("gridList", rowNum, "WAREKY", "");
						gridList.setColValue("gridList", rowNum, "WARENM", "");
					}
				}
			}
			
			
		}
		
	}
	
	function gridListEventRowCopyAfter(gridId, rowNum){
		if(gridId == "gridList") {
			//gridList.setColValue("gridList", rowNum, "LOCAKY", "");
		}
	}
	
	function gridListEventRowAddBefore(gridId, rowNum){
		var newData = new DataMap();
		
		newData.put("WAREKY", "");
		
		return newData;
	}
	
	function gridListEventRowRemove(gridId, rowNum){
		if(gridId == "gridList"){
			var param = new DataMap();
			
			return true;
		}
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType){
		var param = new DataMap();
		
		if(searchCode == "SHWAHMA"){
			//param = dataBind.paramData("searchArea");
			param.put("WAREKY", "");
		}else if(searchCode=="SHSTLMA"){
			param.put("OWNRKY", "<%=ownrky%>");
		}
		
		return param;
	}
	
	function setLOCAKY(){
		
		var apyWareky = $("#APY_WAREKY").val();
		
		if(gridList.getSelectData("gridList").length < 1) {
			alert("선택된 데이터가 없습니다.");
			return;
		}
		
		
		var param = new DataMap();
		param.put("WAREKY",apyWareky);
		
		var json = netUtil.sendData({
			module : "WmsAdmin",
			command : "WAREKY",
			sendType : "map",
			param : param
		});
		
		var selectNumList = gridList.getSelectRowNumList("gridList");
		
		for(var i=0;i<selectNumList.length;i++){
			var rowNum = selectNumList[i];
			gridList.setColValue("gridList", rowNum, "WAREKY", apyWareky);
			if(json && json.data){
				gridList.setColValue("gridList", rowNum, "WARENM", json.data["NAME01"]);
			}else{
				gridList.setColValue("gridList", rowNum, "WARENM", "");
			}
		}
	}
	
</script>
</head>
<body>
<div class="contentHeader">
	<div class="util">
		<button CB="Search SEARCH BTN_DISPLAY"></button>
		<button CB="Save SAVE STD_SAVE"></button>
	</div>
	<div class="util2">
		<button class="button type2" id="showPop" type="button"><img src="/common/images/ico_btn4.png" alt="List" /></button>
	</div>
</div>

<!-- searchPop -->
<div class="searchPop" id="searchArea">
	<button type="button" class="closer">X</button>
	<div class="searchInnerContainer">
		<p class="util">
			<!-- <button CB="Create CREATE BTN_CREATE"></button> -->
			<button CB="Search SEARCH BTN_DISPLAY"></button>
		</p>
		
		<div class="searchInBox">
			<h2 class="tit" CL="STD_SELECTOPTIONS">검색조건</h2>
			<table class="table type1">
				<colgroup>
					<col width="120px" />
					<col />
				</colgroup>
				<tbody>
					<tr>
						<th CL="STD_WAREKY">거점</th>
						<td>
							<select combo="Wms,USERWAHCOMBO" name="WAREKY" style="width: 160px">
							  <option value="">전체</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<th CL="STD_LOTA02" style="vertical-align:middle">플랜트</th>
						<td >
							<select CommonCombo="LOTA02" name="PLNTKY" ComboCodeView=false ComboType="MS" style="width: 160px" >
							</select>
						</td>
					</tr>
					<tr>
						<th CL="STD_LOTA01">저장위치</th>
						<td>
							<input type="text" name="MS.STLKY" UIInput="R,SHSTLMA" />
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
			<div class="bottomSect type1">
				<div class="tabs">
					<ul class="tab type2">
- 						<li><a href="#tabs1-1"><span CL="STD_LIST">리스트</span></a></li>
						<!-- <li><a href="#tabs1-1" CL="STD_DOCUMENTINFO"><span>문서정보</span></a></li> -->
					</ul>
					<div id="tabs1-1">
						<div class="section type1">
							
							<div>

								
								<table>
									<tr>
										<td>
											<span CL='STD_GENERALWAREHOUSE'>거점</span>
											<select combo="Wms,USERWAHCOMBO" id="APY_WAREKY" name="APY_WAREKY">
												<option value=""> </option>
											</select>
											&nbsp;
										</td>
										<td>
											<button CB="Reflect REFLECT BTN_REFLECT">반영</button>
											&nbsp;&nbsp;
										</td>
									</tr>
								</table>
							</div>
							
							
							<div class="table type2" style="top:45px;">
								<div class="tableBody">
									<table>
										<tbody id="gridList">
											<tr CGRow="true">
												<td GH="40" GCol="rownum">1</td>
												<td GH="40" GBtnCheck="true" GCol="rowCheck"></td>

												<td GH="100 STD_PLNTKY" GCol="text,PLNTKY">플랜트</td>
												<td GH="110 STD_PLNTNM" GCol="text,PLNTNM">플랜트명</td>
												
												<td GH="100 STD_STLKY" GCol="text,STLKY">저장위치</td>
												<td GH="200 STD_STLNM" GCol="text,STLNM">저장위치명</td>
												
												<td GH="100 STD_WAREHOUSE" GCol="input,WAREKY,SHWAHMA" >거점</td>
												<td GH="110 STD_WANAME" GCol="text,WARENM">거점명</td>
												
												<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D">생성일자</td>
												<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
												<td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자ID</td>
												<td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자명</td>
												<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D">수정일자</td>
												<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
												<td GH="100 STD_LMOUSR" GCol="text,LMOUSR">수정자ID</td>
												<td GH="100 STD_LUSRNM" GCol="text,LUSRNM">수정자명</td>
												<td GH="0 STD_ERPWERKS" GCol="text,OWNRKY">화주</td>
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
<!-- 									<button type="button" GBtn="total"></button> -->
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
<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>