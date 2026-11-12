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
			pkcol : "WAREKY,AREAKY",
			module : "WmsAdmin",
			command : "AREMA",
			bindArea : "tabs1-2" 
	    });
		//gridList.setReadOnly('gridList',true, ['NEGSTK','INDCDO','INDAES']);
	});
	
	function searchHelpEventOpenBefore(searchCode, gridType){
		//commonUtil.debugMsg("searchHelpEventOpenBefore : ", arguments);
		if(searchCode == "SHAREMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHWAHMA"){
			return dataBind.paramData("searchArea");
		}
	}
	function searchList(){
		//var param = dataBind.paramData("searchArea");
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			//alert(param);
			gridList.gridList({
				id : "gridList",
				param : param
			});
		}
	}
	
	function saveData(){
		if(gridList.validationCheck("gridList", "modify")){
			var param = dataBind.paramData("searchArea");
			var list = gridList.getModifyData("gridList", "A");
			
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
				return;
			}
			
			var param = new DataMap();
			param.put("list", list);
			
			var json = netUtil.send({
				url : "/wms/admin/json/saveMA01.data",
				param : param,
				returnParam : param,
				successFunction : "saveEnd",
				failFunction : "saveFail"
			});
			
			/* var json = gridList.gridSave({
				id : "gridList",
				param : param
			});
			
			if(json){
				if(json.data == 0){
					commonUtil.msgBox("MASTER_M0545");
					searchList();
				}
				if(json.data){
					searchList();
				}
			} */
		}
	}
	
	function saveEnd(json, param){
		
		if(json && json.data){
			if(json.data != "0"){
				alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEOK));
			}else{
				alert(commonUtil.getMsg("COMMON_M0465")); // 저장 실패하였습니다.
			}
			searchList();
		}else{
			alert(commonUtil.getMsg("COMMON_M0465")); // 저장 실패하였습니다.
			searchList();
		}
	}
	
	function saveFail(a, b, c, param){ // a:request, b:status, c:error
		var resultText = a.responseText.trim();
		alert(commonUtil.getMsg("COMMON_M0465")); // 저장 실패하였습니다.
		searchList();
	}
	
 	function gridListEventRowAddBefore(gridId, rowNum){
		var areatyt = inputList.getComboData("AREATY", "STOR");
		var newData = new DataMap();
		newData.put("WAREKY",  "<%=wareky%>");
		//newData.put("AREAKY", "RECV");
		newData.put("AREATY", "STOR");
		newData.put("AREATYT", areatyt);
		
		return newData;
	}  
	
	function gridListEventRowRemove(gridId, rowNum){
		var rowAea = gridList.getColData("gridList", rowNum, "AREAKY");
		  if(gridId == "gridList"){
			var param = new DataMap();
			param.put("AREAKY", rowAea);
			//2020.04.14 
			param.put("WAREKY", "<%=wareky%>");
			var json = netUtil.sendData({
				module : "WmsAdmin",
				command : "AREMAval",
				sendType : "map",
				param : param
			});
			
			if(json && json.data){
				if(json.data["CNT"] >= 1){
					//alert("삭제 할 수 없습니다.");
					commonUtil.msgBox("VALID_M0017");//구역이 존재하는 영역을 삭제할 수 없습니다.
					return false;
				} else if (json.data["CNT"] == 0){
					return true;
				}
			}
		}  
	}
	
	function areakyCheck(valueTxt, $colObj){
		var rowNum = gridList.getColObjRowNum("gridList", $colObj);
		var rowCount = gridList.getGridDataCount("gridList");
		for(var i=0;i<rowCount;i++){
			if(i != rowNum){
				var areaky = gridList.getColData("gridList", i, "AREAKY");
				if(areaky == valueTxt){
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
		}
	}
	
	//<동일한 영역있으면 필터링해주는 이벤트>
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){		
		if(gridId == "gridList" && colName == "AREAKY"){
			var param = new DataMap();
			param.put("WAREKY","<%=wareky%>");
			param.put("AREAKY",colValue);
			var json = netUtil.sendData({
				module : "WmsAdmin",
				command : "AREMAval",
				sendType : "map",
				param : param
			});
			
			if(json.data["CNT"] > 0) {
				commonUtil.msgBox("VALID_M0302", colValue);
				gridList.setColValue("gridList", rowNum, "AREAKY", " ");	
				return;
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
						<dt CL="STD_WAREKY">거점</dt>
						<dd>
							<input type="text" id="WAREKY" name="WAREKY" class="input" value="<%=wareky%>" readonly="readonly"/> 
						</dd>
					</dl>
					<dl>
						<dt CL="STD_AREAKY">창고</dt>
						<dd>
							<input type="text" id="AREAKY" name="AREAKY" class="input"  UIInput="R,SHAREMA"/>
						</dd>
					</dl>					
					<dl>
						<dt CL="STD_AREATY">Area Type</dt>
						<dd>
							<select CommonCombo="AREATY" name="AREATY" class="input">
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
										<td GH='40 STD_NUMBER'   GCol="rownum">1</td>
										<td GH='60 STD_WAREKY'   GCol="text,WAREKY">거점</td>
									    <td GH='50 STD_AREAKY'   GCol="input,AREAKY,SHAREMA" id="AREAKY" validate="required"></td>
										<td GH='150 STD_AREATY'  GCol="select,AREATY" >
											<select CommonCombo="AREATY" validate="required,VALID_M0402">
                                            </select>
										</td>
										<td GH='120 STD_SHORTX'  GCol="input,SHORTX" GF="S 60">설명</td>
										<!-- <td GH='100 STD_NEGSTK'  GCol="check,NEGSTK">(-)허용</td> -->
										<td GH='100 STD_INDCDO'  GCol="check,INDCDO">문서번호복사</td>
										<td GH="90 STD_MAXCPC" GCol="input,MAXCPC" GF="N 15,3">팔렛Capa.</td>
										<!-- <td GH='100 STD_INDAES'  GCol="check,INDAES">재고가산</td> -->
										<td GH='100 STD_CREDAT'  GCol="text,CREDAT" GF="D">생성일자</td>
										<td GH='100 STD_CRETIM'  GCol="text,CRETIM" GF="T">생성시간</td>
										<td GH='100 STD_CREUSR'  GCol="text,CREUSR">생성자</td>
										<td GH='100 STD_CUSRNM'  GCol="text,CUSRNM">생성자명</td>
										<td GH='100 STD_LMODAT'  GCol="text,LMODAT" GF="D">수정일자</td>
										<td GH='100 STD_LMOTIM'  GCol="text,LMOTIM" GF="T">수정시간</td>
										<td GH='100 STD_LMOUSR'  GCol="text,LMOUSR">수정자</td>
										<td GH='100 STD_LUSRNM'  GCol="text,LUSRNM">수정자명</td>										
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