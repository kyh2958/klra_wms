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
		</div>
		<div class="search_next_wrap" style="height: calc(100% - 45px);">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
					<li><a href="#tabs1-1" CL='STD_LIST'><span>리스트</span></a></li>  
				</ul>
				<div class="table_box section" id="tabㄴ1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40 STD_NUMBER"  GCol="rownum">1</td>
										<td GH="150 STD_DOCCAT"  GCol="input,DOCCAT" GF="S 4" validate="required,VALID_M0414" onKeyPress="return numkeyCheck(event)">문서유형</td>
										<td GH="150 STD_SHORTX"  GCol="input,SHORTX" GF="S 150" validate="required">설명</td>
										<td GH="150 STD_CREDAT"  GCol="text,CREDAT" GF='D'></td>
										<td GH="150 STD_CRETIM"  GCol="text,CRETIM" GF='T'></td>
										<td GH="150 STD_CREUSR"  GCol="text,CREUSR"></td>
										<td GH="150 STD_CUSRNM"  GCol="text,CUSRNM"></td>
										<td GH="150 STD_LMODAT"  GCol="text,LMODAT" GF='D'></td>
										<td GH="150 STD_LMOTIM"  GCol="text,LMOTIM" GF='T'></td>
										<td GH="150 STD_LMOUSR"  GCol="text,LMOUSR"></td>
										<td GH="150 STD_LUSRNM"  GCol="text,LUSRNM"></td>											
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