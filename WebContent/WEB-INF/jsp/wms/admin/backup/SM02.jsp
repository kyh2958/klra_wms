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
			id : "gridList",
			editable : true,
			pkcol : "UOMKEY",
			module : "WmsAdmin",
			command : "UOMMA",
			emptyMsgType : false
		});
//		gridList.setReadOnly("gridList",true, ["NEGSTK","INDCDO","INDAES"]);
		
		searchList();
	});
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}
	}
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			
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
				url : "/wms/admin/json/saveSM02.data",
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
			if(colName == "AREAKY") {
				if(colValue == ""){
					return;
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
<%-- 			newData.put("COMPKY", "<%=compky%>"); --%>
		}
		
		return newData;
	}
	
	function gridListEventRowRemove(gridId, rowNum){
		var rowAea = gridList.getColData("gridList", rowNum, "AREAKY");
		if(gridId == "gridList"){
// 			var param = new DataMap();
<%-- 			param.put("WAREKY", "<%=wareky%>"); --%>
			
// 			var json = netUtil.sendData({
// 				module : "WmsMaster_WarehouseConfigrations",
// 				command : "AREMA_COUNT",
// 				sendType : "map",
// 				param : param
// 			});
			
// 			if(json && json.data){
// 				if(json.data["COUNT"] > 0){
// 					commonUtil.msgBox("VALID_M0017");
// 					return false;
// 				}
// 			}
			
			return true;
		}
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType){
		if(searchCode == "SHCOMMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHWAHMA"){
			return dataBind.paramData("searchArea");
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
<!-- 	<div class="util2"> -->
<!-- 		<button class="button type2" id="showPop" type="button"><img src="/common/images/ico_btn4.png" alt="List" /></button> -->
<!-- 	</div> -->
</div>

<!-- searchPop -->
<!-- 
<div class="searchPop" id="searchArea">
	<button type="button" class="closer">X</button>
	<div class="searchInnerContainer">
		<p class="util">
			<button CB="Search SEARCH BTN_DISPLAY"></button>
			<button CB="GetVariant GETVARIANT BTN_GETVARIANT"></button>
			<button CB="SaveVariant SAVEVARIANT BTN_SAVEVARIANT"></button>
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
						<th CL="STD_COMPKY">회사</th>
						<td>
							<input type="text" name="COMPKY" value="<%=compky%>"  readonly="readonly" /> // UIInput="S,SHCOMMA"
						</td>
					</tr>
					<tr>
						<th CL="STD_WAREKY">거점</th>
						<td>
							<input type="text" name="WAREKY" UIInput="R,SHWAHMA"  value="<%=wareky%>"/>
							<input type="text" name="WAREKY" UIInput="R,SHWAHMA"  />
						</td>
					</tr>
					<tr>
						<th CL="STD_DELMAK">삭제</th>
						<td>
							<input type="checkbox" name="DELMAK" value="V"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div> -->
<!-- //searchPop -->

<!-- content -->
<div class="content">
	<div class="innerContainer">
		<!-- contentContainer -->
		<div class="contentContainer">
			<div class="bottomSect type1">
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
												<td GH="100 STD_UOMKEY" GCol="input,UOMKEY" GF="S 3"/>단위</td>
												<td GH="100 STD_SHORTX" GCol="input,SHORTX" GF="S 180"/>설명</td>
												
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
<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>