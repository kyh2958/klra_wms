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
			pkcol : "DOCCAT",
			module : "WmsAdmin",
			command : "DC01",
		});
		searchList();
	});
	
	//var rowCnt = 0;
	
	function searchList(){
		//var param = dataBind.paramData("searchArea");
		var param = inputList.setRangeParam("searchArea");
		gridList.gridList({
			id : "gridList",
			param : param
		});
	}
		
 	function saveData(){
 		if(gridList.validationCheck("gridList", "modify")){
			var param = dataBind.paramData("searchArea");
			var json = gridList.gridSave({
				id : "gridList",
				param : param
			});
		}
		if(json && json.data){
			searchList();
		}
	}
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}
	}
	
	//2020.04.16 문서유형 Validation추가
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId == "gridList" && colName == "DOCCAT"){
			if(colValue != ""){
				var doccat = gridList.getColData(gridId, rowNum, "DOCCAT");
				var param = new DataMap();
				param.put("DOCCAT",doccat);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "DOCCval",
					sendType : "map",
					param : param
				});
				if(json.data["CNT"] > 0) {
					commonUtil.msgBox("VALID_M0314",colValue);
					gridList.setColValue("gridList", rowNum, "DOCCAT", "");	
					return false;
				}
			}
		}
	}
	
 	//2020.04.16 숫자입력 validation
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
</div>
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
										</colgroup>
										<thead>
											<tr>
												<th CL='STD_NUMBER'>번호</th>
												<th CL='STD_DOCCAT'>문서유형</th>
												<th CL='STD_SHORTX'>설명</th>
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
												<td GCol="input,DOCCAT" GF="S 4" validate="required,VALID_M0414" onKeyPress="return numkeyCheck(event)">문서유형</td>
												<td GCol="input,SHORTX" GF="S 150" validate="required">설명</td>
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