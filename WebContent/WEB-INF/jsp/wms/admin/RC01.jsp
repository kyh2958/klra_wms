<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<style type="text/css">
 .content_wrap{background: none;}
</style>
<script type="text/javascript">
	$(document).ready(function(){
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
						<dt CL="STD_DOCCAT">문서유형</dt>
						<dd>
							<input type="text" class="input" name="DOCCAT" id="DOCCAT" UIInput="S,SHDOCCM" IAname="Search"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_DOCUTY">문서타입</dt>
						<dd>
							<input type="text" class="input" name="DOCUTY" UIInput="S,SHDOCTM" IAname="Search"/>
						</dd>
					</dl>
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
			</div>
		</div>
		<div class="search_next_wrap">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
					<li><a href="#tab1-1"><span>리스트</span></a></li>  
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="150 STD_DOCCAT" GCol="input,DOCCAT,SHDOCCM" validate="required,VALID_M0414" GF="S 4"></td>
										<td GH="150 STD_DOCUTY" GCol="input,DOCUTY,SHDOCTM" validate="required,VALID_M0415" GF="S 8"></td>
										<td GH="150 STD_RSNCOD" GCol="input,RSNCOD" validate="required,VALID_M0413" GF="S 4" onKeyPress="return numkeyCheck(event)" /></td>
										<td GH="150 STD_SHORTX" GCol="text,SHORTX" GF="S 180"></td>
										<td GH="150 STD_DOCTYPNM" GCol="text,DOCTYPNM"></td>
										<td GH="150 STD_RSNCDNM" GCol="input,DOCCATNM" GF="S 150"></td>
										<td GH="150 STD_CREDAT" GCol="text,CREDAT" GF='D'></td>
										<td GH="150 STD_CRETIM" GCol="text,CRETIM" GF='T'></td>
										<td GH="150 STD_CREUSR" GCol="text,CREUSR"></td>
										<td GH="150 STD_CUSRNM" GCol="text,CUSRNM"></td>
										<td GH="150 STD_LMODAT" GCol="text,LMODAT" GF='D'></td>
										<td GH="150 STD_LMOTIM" GCol="text,LMOTIM" GF='T'></td>
										<td GH="150 STD_LMOUSR" GCol="text,LMOUSR"></td>
										<td GH="150 STD_LUSRNM" GCol="text,LUSRNM"></td>
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
		</div>
	</div>
</div>
<!-- // content -->
<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>