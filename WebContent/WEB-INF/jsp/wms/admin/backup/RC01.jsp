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
		setTopSize(250);
		gridList.setGrid({
	    	id : "gridList",
	    	name : "gridList",
			editable : true,
			pkcol : "OWNRKY,DOCCAT,DOCUTY,RSNCOD",
			module : "WmsAdmin",
			command : "RC01" ,
			validation : "DOCCAT,DOCUTY,RSNCOD"
	    });
	});
	
	function searchList(){
		//var param = dataBind.paramData("searchArea");
		var param = inputList.setRangeParam("searchArea");
		gridList.gridList({
	    	id : "gridList",
	    	param : param
	    });
	}

 	function saveData(){
		var param = dataBind.paramData("searchArea");
		var list = gridList.getModifyData("gridList", "A");
		
		if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
			return;
		}
		
		var param = new DataMap();
		param.put("list", list);
		param.put("OWNRKY", "<%=ownrky%>");
		
		var json = netUtil.sendData({
			url : "/wms/admin/json/saveRC01.data",
			param : param,
			returnParam : param
		});
		
		/* if(json.data !=  'OK'){
			var msgList = json.data.split(" ");
			var msgTxt = commonUtil.getMsg(msgList[0], msgList[1]);
			commonUtil.msgBox(msgTxt);
			return false;
		}
		
		commonUtil.msgBox("MASTER_M0564"); //저장이 완료되었습니다.
		searchList(); */

		if(json && json.data){
			if(json.data == "OK") {
				searchList();
				alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEOK));
			} else {
				alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEFAIL) + "\n [" + json.data + "]");
			}
		}
		
		

	}
 	
 	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId == "gridList" && colName == "DOCCAT"){
			if(colValue != ""){
				var param = new DataMap();
				param.put("DOCCAT",colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "RC01DOCCATNM",
					sendType : "map",
					param : param
				});
				
				if(json && json.data){
					gridList.setColValue("gridList", rowNum, "SHORTX", json.data["SHORTX"]); 
				}else{
					commonUtil.msgBox("MASTER_M0366");
					gridList.setColValue("gridList", rowNum, "DOCCAT", "");
					gridList.setColValue("gridList", rowNum, "SHORTX", "");
				}
				
				gridList.setColValue("gridList", rowNum, "DOCUTY", "");
				gridList.setColValue("gridList", rowNum, "DOCTYPNM", "");
			}else{
				gridList.setColValue("gridList", rowNum, "SHORTX", "");
				gridList.setColValue("gridList", rowNum, "DOCUTY", "");
				gridList.setColValue("gridList", rowNum, "DOCTYPNM", "");
			}
		} else if (gridId == "gridList" && colName == "DOCUTY") {
			if(colValue != ""){
				
				var doccat = gridList.getColData("gridList", rowNum, "DOCCAT");
				var ownrky = gridList.getColData("gridList", rowNum, "OWNRKY");
				var param = new DataMap();
				param.put("DOCUTY",colValue);
				param.put("DOCCAT",doccat);
				param.put("OWNRKY",ownrky);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "RC01DOCUTYval",
					sendType : "map",
					param : param
				});
				
				if(json.data["CNT"] >= 1) {
					var parama = new DataMap();
					parama.put("DOCUTY",colValue);
					parama.put("DOCCAT",doccat);
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "RC01DOCTYPNM",
						sendType : "map",
						param : parama
					});
					
					if(json && json.data){
						gridList.setColValue("gridList", rowNum, "DOCTYPNM", json.data["DOCTYPNM"]);
					} 
				} else if (json.data["CNT"] < 1) {
					//alert(commonUtil.getMsg("MASTER_M0031",colValue));
					commonUtil.msgBox("MASTER_M0031",colValue);
					gridList.setColValue("gridList", rowNum, "DOCUTY", "");
					gridList.setColValue("gridList", rowNum, "DOCTYPNM", "");
				}
			}else{
				gridList.setColValue("gridList", rowNum, "DOCTYPNM", "");
			}
		}else if(gridId == "gridList" && colName == "RSNCOD"){
			if(colValue != ""){
				var doccat = gridList.getColData("gridList", rowNum, "DOCCAT"); //2020.04.10LHJ 사유코드 validation오류 수정
				var docuty = gridList.getColData("gridList", rowNum, "DOCUTY");
				var ownrky = gridList.getColData("gridList", rowNum, "OWNRKY");
				
				var param = new DataMap();
				param.put("OWNRKY",ownrky);
				param.put("RSNCOD",colValue);
				param.put("DOCCAT",doccat); //2020.04.10LHJ 사유코드 validation오류 수정
				param.put("DOCUTY",docuty);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "RC01RSNCODval",
					sendType : "map",
					param : param
				});
				
				if(json.data["CNT"] >= 1) {
					gridList.setColValue("gridList", rowNum, "RSNCOD", "");
					commonUtil.msgBox("VALID_M0012"); 
					return false;
				}
			}
		}
	}
	
 	function gridListEventRowAddBefore(gridId, rowNum){
		var ownrky = gridList.getColData("gridList", 0, "OWNRKY");
		var newData = new DataMap();
		newData.put("OWNRKY", ownrky);
		
		return newData;
	}
	
 	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}
	}
 	
 	function searchHelpEventOpenBefore(searchCode, gridType){
 		if(searchCode == "SHDOCTM") {
 			//2020.04.10 LHJ 문서타입 서치헬프 조회 시 앞서 선택한 문서유형값 표시
 			var rowNum = gridList.getFocusRowNum("gridList");
			var	doccat = gridList.getColData("gridList",rowNum,"DOCCAT");
			var param = new DataMap();
			param.put("DOCCAT", doccat);
			return param;
 		} else if (searchCode == "SHDOCCM") {
 			return dataBind.paramData("searchArea");
 		} 
/* 		if(searchCode == "SHDOCTM"){
			var rowNum = gridList.getFocusRowNum("gridList");
			var	doccat = gridList.getColData("gridList",rowNum,"DOCCAT");
			
			var param = new DataMap();
			param.put("DOCCAT", doccat);
			return param;
		} */
	}
 	//2020.04.10 LHJ 숫자입력 validation
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
		<button CB="Search SEARCH BTN_DISPLAY"></button>
		<button CB="Save SAVE STD_SAVE"></button>
	</div>
	<div class="util3">
		<button class="button type2" id="showPop" type="button"><img src="/common/images/ico_btn4.png" alt="List" /></button>
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
						<th CL="STD_DOCCAT">문서유형</th>
						<td>
							<input type="text" name="DOCCAT" id="DOCCAT" UIInput="S,SHDOCCM" />
						</td>
					</tr>
					<tr>
						<th CL="STD_DOCUTY">문서타입</th>
						<td>
							<input type="text" name="DOCUTY" UIInput="S,SHDOCTM" />
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
						<li><a href="#tabs1-1" CL='STD_LIST'><span>리스트</span></a></li>
					</ul>
					<div id="tabs1-1">
						<div class="section type1">
							<div class="table type2">
								<div class="tableHeader">
									<table>
										<colgroup>
											<col width="40" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
										</colgroup>
										<thead>
											<tr>
												<th CL='STD_NUMBER'>번호</th>
												<th CL='STD_DOCCAT'></th>  <!-- 문서유형 -->
												<th CL='STD_DOCUTY'></th>  <!-- 문서타입 -->
												<th CL='STD_RSNCOD'></th>  <!-- 사유코드 -->
												<th CL='STD_SHORTX'></th>
												<th CL='STD_DOCTYPNM'></th> <!-- 문서타입+설명 -->
												<th CL='STD_RSNCDNM'></th> <!-- 2020.04.10 LHJ문서타입설명 -> 사유코드 설명으로 변경 -->
												<th CL='STD_CREDAT'>생성일자</th>
												<th CL='STD_CRETIM'>생성시간</th>
												<th CL='STD_CREUSR'>생성자</th>
												<th CL='STD_CUSRNM'>생성자명</th>
												<th CL='STD_LMODAT'>수정일자</th>
												<th CL='STD_LMOTIM'>수정시간</th>
												<th CL='STD_LMOUSR'>수정자</th>
												<th CL='STD_LUSRNM'>수정자명</th>
											</tr>
										</thead>
									</table>
								</div>
								<div class="tableBody">
									<table>
										<colgroup>
											<col width="40" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
											<col width="150" />
										</colgroup>
										<tbody id="gridList">
											<tr CGRow="true">
												<td GCol="rownum">1</td>
												<td GCol="input,DOCCAT,SHDOCCM" validate="required,VALID_M0414" GF="S 4"></td>
												<td GCol="input,DOCUTY,SHDOCTM" validate="required,VALID_M0415" GF="S 8"></td>
												<td GCol="input,RSNCOD" validate="required,VALID_M0413" GF="S 4" onKeyPress="return numkeyCheck(event)" /></td>
												<td GCol="text,SHORTX" GF="S 180"></td>
												<td GCol="text,DOCTYPNM"></td>
												<td GCol="input,DOCCATNM" GF="S 150"></td>
												<td GCol="text,CREDAT" GF='D'></td>
												<td GCol="text,CRETIM" GF='T'></td>
												<td GCol="text,CREUSR"></td>
												<td GCol="text,CUSRNM"></td>
												<td GCol="text,LMODAT" GF='D'></td>
												<td GCol="text,LMOTIM" GF='T'></td>
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