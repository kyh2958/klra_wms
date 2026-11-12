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
			pkcol : "WAREKY,MTYPE,PLNTKY",
			module : "WmsAdmin",
			command : "MTPLNT",
		});
	});
	
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
			var param = dataBind.paramData("searchArea");
			var list = gridList.getModifyData("gridList", "A");
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
				return;
			}
			var param = new DataMap();
			param.put("list", list);
			var json = netUtil.send({
				url : "/wms/admin/json/saveMT01.data",
				param : param,
				returnParam : param,
				successFunction : "saveEnd",
				failFunction : "saveFail"
			});
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
// 		searchList();
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}
	}
	
	function comboEventDataBindeBefore(comboAtt){
		if(comboAtt == "Wms,PLNTKYCOMBO"){
			var param = inputList.setRangeParam("searchArea");
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			return param;
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
							<select class="input" combo="Wms,USERWAHCOMBO" name="WAREKY" style="width: 160px">
								<option value="">전체</option>
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
										<td GH='40 STD_NUMBER'	GCol="rownum"				>1</td>
										<td GH="40"				GCol="rowCheck"				>V</td>
										<td GH='100 STD_WAREKY'	GCol="select,WAREKY"	validate="required"	>
											<select Combo="Wms,USERWAHCOMBO" >
												<option value=" ">
											</select>
										</td>
										<td GH='100 STD_MTYPE'	GCol="select,MTYPE"		validate="required"	>
											<select Combo="Wms,MTYPECOMBO">
												<option value=" ">
											</select>
										</td>
										<td GH="100 STD_PLNTKY" GCol="select,PLNTKY"	validate="required"	>
											<select Combo="Wms,PLNTKYCOMBO">
												<option value=" ">
											</select>
										</td>
										<td GH='100 STD_CREDAT'	GCol="text,CREDAT" GF="D"	>생성일자</td>
										<td GH='100 STD_CRETIM'	GCol="text,CRETIM" GF="T"	>생성시간</td>
										<td GH='100 STD_CREUSR'	GCol="text,CREUSR"			>생성자</td>
										<td GH='100 STD_CUSRNM'	GCol="text,CUSRNM"			>생성자명</td>
										<td GH='100 STD_LMODAT'	GCol="text,LMODAT" GF="D"	>수정일자</td>
										<td GH='100 STD_LMOTIM'	GCol="text,LMOTIM" GF="T"	>수정시간</td>
										<td GH='100 STD_LMOUSR'	GCol="text,LMOUSR"			>수정자</td>
										<td GH='100 STD_LUSRNM'	GCol="text,LUSRNM"			>수정자명</td>
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