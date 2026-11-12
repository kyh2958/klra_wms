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
			pkcol : "DOCUTY",
			module : "WmsAdmin",
			command : "DT01",
			validation : "NUMOBJ,SYSLOC"
		});
	});
	
	function searchList(){
		//var param = dataBind.paramData("searchArea");
		/* var doc = $("#DOCCAT").val();
		if(doc == ""){
			commonUtil.msgBox("VALID_M0400");	//문서타입를 선택해 주세요.
			return;
		} */
		
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			gridList.gridList({
				id : "gridList",
				param : param
			});

			netUtil.send({
				module : "WmsAdmin",
				command : "DT01TOP",
				bindId : "DT01top",
				sendType : "map",
				bindType : "field",
				param : param
			}); 
		}
	}
	
	function saveData(){
		var modCnt = gridList.getModifyRowCount("gridList");
		if(modCnt == 0){
			commonUtil.msgBox("MASTER_M0545");
			return;
		}
		
		var modifylist = gridList.getModifyData("gridList", "A");
		for(var i=0; i<modCnt; i++){
			var row = modifylist[i];
			var grpchk = row.get("DOCGRP");
			
			if(grpchk == 'V'){
				//문서타입 그룹 사용여부 Validation
				var paramV = new DataMap();
				var json = netUtil.sendData({
					module : "WmsOutbound",
					command : "DOCGRP_VAL",
					sendType : "map",
					param : paramV
				});
				
				if(json.data.CNT > 0){
					commonUtil.msgBox("VALID_M0023"); //그룹오더가 존재하므로 변경할 수 없습니다.
					return;
				}
			}
		}
	
		if(gridList.validationCheck("gridList", "modify")){
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){ //저장하시겠습니까?
				return;
			}
			
			var list = gridList.getModifyData("gridList", "A");
			var param = dataBind.paramData("searchArea");
			param.put("list", list);

			var json = netUtil.sendData({
				url : "/wms/admin/json/saveDT01.data",
				param : param
			});
	
			if(json.data !="OK"){
				var msgList = json.data.split(" ");
				var msgTxt = commonUtil.getMsg(msgList[0], msgList[1]);
				commonUtil.msgBox(msgTxt);
				return false;
			}
			commonUtil.msgBox("MASTER_M0564"); //저장이 완료되었습니다.
			searchList();
		}
	}
	
	function gridListEventRowAddBefore(gridId, rowNum){
		var doccat = gridList.getColData("gridList", 0, "DOCCAT");
		var insinc = gridList.getColData("gridList", 0, "INSINC");
		var trnhty = gridList.getColData("gridList", 0, "TRNHTY");
		var itninc = gridList.getColData("gridList", 0, "ITNINC");
		
		var param = inputList.setRangeParam("searchArea");
		
		/* var doccat = inputList.getComboData("DOCCAT"); */
		
		var newData = new DataMap();
		
		newData.put("DOCCAT", doccat);
		newData.put("INSINC", insinc);
		newData.put("TRNHTY", trnhty);
		newData.put("ITNINC", itninc);
		
		return newData;
	}
	
	//문서유형 : 200(출고)일경우에만  검수여부, 문서타입, 배분우선순위 입력가능
	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList" && dataLength > 0){
			var dataList = gridList.getGridData(gridId);
			var rowData;
			for(var i = 0; i< dataList.length;i++){
				rowData = dataList[i];
				if($("#DOCCAT").val() != "200"){
					gridList.setRowReadOnly(gridId, i, true, ["DOCGRP","PRIORT"]);
				}
			}
		}
	}
	
	function docutyCheck(valueTxt, $colObj){
		var rowNum = gridList.getColObjRowNum("gridList", $colObj);
		var rowCount = gridList.getGridDataCount("gridList");
		for(var i = 0 ; i < rowCount ; i ++){
			if(i != rowNum){
				var docuty = gridList.getColData("gridList", i, "DOCUTY");
				
				if(docuty == valueTxt){
					return false;
				}
			}
		}
		return true;
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Execute"){
			test3();
		}
	}
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId == "gridList"){
			if (colName == "DOCUTY") {
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
					
					if(json.data["CNT"] > 0 ) {
						commonUtil.msgBox("VALID_M0115", colValue);    //[{0}]는 이미 존재하는 문서타입 입니다.
						gridList.setColValue("gridList", rowNum, "DOCUTY", "");
						gridList.setColValue("gridList", rowNum, "DOCTYPNM", "");
						gridList.setFocus("gridList", rowNum, colValue);
					}else{
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
					}
				}else{
					gridList.setColValue("gridList", rowNum, "DOCTYPNM", "");
					gridList.setFocus("gridList", rowNum, colValue);
				}
			}else if (colName == "NUMOBJ") {
				if(colValue != ""){
					var param = new DataMap();
					param.put("NUMOBJ",colValue);
					
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "NUMOBJval",
						sendType : "map",
						param : param
					});
					
					if(json.data["CNT"] < 1 ) {
						commonUtil.msgBox("MASTER_M0074", colValue);    //채번 오브젝트 {0}은 존재하지 않습니다.
						gridList.setColValue("gridList", rowNum, "NUMOBJ", "");
						gridList.setFocus("gridList", rowNum, colValue);
					}
				}
			}
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
							<select Combo="WmsAdmin,DOCCATCOMBO" name="DOCCAT" id="DOCCAT" class="input">
                                <option value="">All</option>
                            </select>
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
					<li><a href="#tabs1-1" CL='STD_LIST'><span>리스트</span></a></li>  
				</ul>
				<div class="table_box section" id="tabs1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40 STD_NUMBER"   GCol="rownum"/></td>
										<td GH="100 STD_DOCCAT"  GCol="text,DOCCAT" readonly="readonly">
										<td GH="100 STD_DOCUTY"  GCol="input,DOCUTY" validate="required,VALID_M0400" GF="S 4">문서타입</td>
										<td GH="150 STD_SHORTX"  GCol="input,SHORTX"  GF="S 180">설명 </td>
										<td GH="100 STD_NUMOBJ"  GCol="input,NUMOBJ,SHNMOBJ" validate="required,MASTER_M0072" GF="S 10">채번 오브젝트</td>
										<td GH="100 STD_TRNHTY"  GCol="input,TRNHTY" GF="S 4">물동타입</td>
										<td GH="100 STD_INDCHK"  GCol="check,INDCHK">INDCHK</td>
										<td GH="100 STD_SYSLOC"  GCol="input,SYSLOC,SHLOCMA" GF="S 20">SYSLOC</td>
										<td GH="100 STD_CREDAT"  GCol="text,CREDAT" GF="D">생성일자</td>
										<td GH="100 STD_CRETIM"  GCol="text,CRETIM" GF="T">생성시간</td>
										<td GH="100 STD_CREUSR"  GCol="text,CREUSR">생성자</td>
										<td GH="100 STD_CUSRNM"  GCol="text,CUSRNM">생성자명</td>
										<td GH="100 STD_LMODAT"  GCol="text,LMODAT" GF="D">수정일자</td>
										<td GH="100 STD_LMOTIM"  GCol="text,LMOTIM" GF="T">수정시간</td>
										<td GH="100 STD_LMOUSR"  GCol="text,LMOUSR">수정자 </td>
										<td GH="100 STD_LUSRNM"  GCol="text,LUSRNM">수정자명</td>										
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