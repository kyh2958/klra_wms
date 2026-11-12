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
	var flag = "U";
	var wareky = "";
	var zoneky="";
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			name : "gridList",
			editable : true,
			checkHead : "gridListCheckHead", 
			pkcol : "WAREKY,ZONEKY",
			module : "WmsAdmin",
			command : "ZONMA",
			bindArea : "tabs1-2" 
		});
		
		// 플랜트
		var $selPLNTKY = $('select[name="S.PLNTKY"]');
		$selPLNTKY.multipleSelect('checkAll');
	});
	
	function searchList(){
		var param = inputList.setRangeParam("searchArea");
// 		console.log(param)
		gridList.gridList({
			id : "gridList",
			param : param
		});
	}
	
	function creatList(){
		 flag = "I";
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			wareky = param.get("WAREKY");
			range = param.get("RANGE_SQL");		
			
			zoneky=$("[name=ZONEKY]").parent().find(".searchInput").val();
			areaky=$("[name=AREAKY]").parent().find(".searchInput").val();
			
			param.put("ZONEKY",zoneky);
			
			if(!zoneky){
				commonUtil.msgBox("VALID_M0403");
				return;
			}
			
			var json = netUtil.sendData({
				module : "WmsAdmin",
				command : "ZONMAval",
				sendType : "map",
				param : param
			});
			if(json.data["CNT"] > 0) {
				commonUtil.msgBox("VALID_M0103",zoneky);//[{0}]는 이미 존재하는 구역 입니다.
				gridList.setColValue("gridList", rowNum, "ZONEKY", "");
				return;
			} 
			
			/* gridList.gridList({
				id : "gridList",
				module : "WmsAdmin",
				command : "ZONMA2",
				param : param 
				
			}); */
			
			var newData = new DataMap();
			newData.put("WAREKY", wareky);
			newData.put("AREAKY", areaky);
			newData.put("ZONEKY", zoneky);
			gridList.setAddRow("gridList", newData);
			newData.clear();
			searchOpen(false);
			
		 }
	}
	
	function saveData(){
		if(gridList.validationCheck("gridList", "modify")){
			var param = dataBind.paramData("searchArea");
			var list = gridList.getModifyList("gridList", "A");
			var chklist = gridList.getGridData("gridList");
			
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
				return;
			}
			
			for(var i=0; i<chklist.length; i++){
				var zonety = gridList.getColData("gridList", i, "ZONETY");
				var plntky = gridList.getColData("gridList", i, "PLNTKY");
				var stlky = gridList.getColData("gridList", i, "STLKY");
				
				if(gridList.getRowState("gridList", i) == "U" || gridList.getRowState("gridList", i) == "C"){
					if(zonety == "STOR"){
						if(plntky == " " || stlky == " "){
							commonUtil.msgBox("VALID_M2065");
							return false;
						}
					}
				}
			}
			
			var param = new DataMap();
			param.put("list", list);
			
			var json = netUtil.send({
				url : "/wms/admin/json/saveMZ01.data",
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
				//alert("비밀번호가 초기화 되었습니다.");
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
		var newData = new DataMap();
		newData.put("WAREKY", "<%=wareky%>");
		//newData.put("ZONETY", "STOR");
		
		return newData;
	} 
 	
 	function gridListEventRowRemove(gridId, rowNum){
 		//2020.04.16 구역Validation오류 수정 
		var rowAea = gridList.getColData("gridList", rowNum, "ZONEKY");
		if(gridId == "gridList"){
			var param = new DataMap();
			param.put("ZONEKY", rowAea);
			param.put("WAREKY", "<%=wareky%>");
			
			var json = netUtil.sendData({
				module : "WmsAdmin",
				command : "ZONMAdel",
				sendType : "map",
				param : param
			});
			
			if(json && json.data){
				if(json.data["CNT"] > 0){
					//alert("로케이션이 존재하여 하위를 삭제 할 수 없습니다.");
					commonUtil.msgBox("VALID_M0018");
					return false;
				} else if (json.data["CNT"] == 0){
					return true;
				}
			}
		}  
	}
 	
/*  	function zonekyCheck(valueTxt, $colObj){
		var rowNum = gridList.getColObjRowNum("gridList", $colObj);
		var rowCount = gridList.getGridDataCount("gridList");
		for(var i=0;i<rowCount;i++){
			if(i != rowNum){
				var zoneky = gridList.getColData("gridList", i, "ZONEKY");
				if(zoneky == valueTxt){
					return false;
				}
			}			
		}
		
		return true;
	} */
 	
 	function searchHelpEventOpenBefore(searchCode, gridType){
		if(searchCode == "SHZONMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHWAHMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHAREMA"){
			return dataBind.paramData("searchArea");
		}else if (searchCode == "SHSTLMAP"){
			var param = new DataMap();
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("WAREKY", "<%=wareky%>");
			
			var rowNum = gridList.getSelectIndex("gridList");
			var lota02 = gridList.getColData("gridList", rowNum, "PLNTKY");
			param.put("PLNTKY", lota02);
			
			if(lota02=="" || lota02==" "){
				commonUtil.msgBox("VALID_M2002", ["플랜트"]); // {0}를 먼저 입력하여 주세요.
				gridList.setColFocus("gridList", rowNum, "PLNTKY");
				return false;
			}
			
			return param;
		}
	}
	
	function comboEventDataBindeBefore(comboAtt){
		
		if(comboAtt == "Wms,PLNTKYCOMBO"){
			var param = new DataMap();
		
			var WAREKYS = "'<%= wareky%>'";
			param.put("WAREKYS", WAREKYS);
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			
			return param;
		}
	}

	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		var param = new DataMap();
		if(gridId == "gridList" && colName == "ZONEKY"){
			if(colValue != ""){
				param.put("WAREKY", "<%=wareky%>");
				param.put("ZONEKY", colValue);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "ZONMAval",
					sendType : "map",
					param : param
				});
				if(json.data["CNT"] > 0) {
					commonUtil.msgBox("VALID_M0103", colValue);
					gridList.setColValue("gridList", rowNum, "ZONEKY", "");
					return;
				} 
			}
		}<%-- else if(gridId == "gridList" && colName == "STLKY"){
			if(colValue != ""){
				var param = new DataMap();	
				var STLNM = "";
				param.put("OWNRKY","<%=ownrky%>");
				param.put("STLKY",colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "STLMAYval",
					sendType : "map",
					param : param
				});
				
				 if(json && json.data){
				 	STLNM = json.data["STLNM"];
				 	gridList.setColValue("gridList", rowNum, "STLNM", STLNM); 
				}else {
					commonUtil.msgBox("저장위치 코드를 잘못 입력하였습니다.");
					gridList.setColValue("gridList", rowNum, "STLKY", "");
					gridList.setColValue("gridList", rowNum, "STLNM", "");
				}	
			}
		} --%>
	}
	
	function commonBtnClick(btnName){
		//commonUtil.debugMsg("commonBtnClick : ", arguments);
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Create"){
			creatList();
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
							<input type="text" name="WAREKY" class="input" value="<%=wareky%>" readonly="readonly"/> 
						</dd>
					</dl>
					<dl>
						<dt CL="STD_AREAKY">창고</dt>
						<dd>
							<input type="text" name="AREAKY" class="input" UIInput="R,SHAREMA"/>
						</dd>
					</dl>					
					<dl>
						<dt CL="STD_ZONEKY">구역</dt>
						<dd>
							<input type="text" name="ZONEKY" class="input" UIInput="R,SHZONMA"/>
						</dd>
					</dl>	
					<dl>
						<dt CL="STD_ZONETY">구역타입</dt>
						<dd>
							<select CommonCombo="ZONETY" name="ZONETY" class="input">
								<option value="">All</option>
							</select>
						</dd>
					</dl>	
					<dl>
						<dt CL="STD_LOTA02" style="vertical-align:middle">플랜트</dt>
						<dd>
                            <select CommonCombo="LOTA02" class="input" name="S.PLNTKY" ComboCodeView=false ComboType="MS" style="width: 160px" >
                            </select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA01">저장위치</dt>
						<dd>
							<input type="text" name="S.STLKY" class="input" UIInput="R,SHSTLMA"/>
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
                                        <td GH="100 STD_WAREKY" GCol="text,WAREKY"></td>
                                        <td GH="100 STD_AREAKY" GCol="input,AREAKY,SHAREMA" GF="S 10" validate="required,MASTER_M0455"></td>
                                        <td GH="100 STD_ZONEKY" GCol="input,ZONEKY,SHZONMA" validate="required" ></td>
                                        <td GH="100 STD_ZONETY" GCol="select,ZONETY" validate="required">
                                            <select CommonCombo="ZONETY">
                                                <option value=" "></option>
                                            </select>
                                        </td>
                                        <td GH="100 STD_PLNTKY" GCol="select,PLNTKY">
                                            <select Combo="Wms,PLNTKYCOMBO" >
                                                <option value=" "></option>
                                            </select>
                                        </td>
                                        <td GH="100 STD_LOTA01" GCol="input,STLKY,SHSTLMAP"></td> 
                                        <td GH="200 STD_STLNM" GCol="text,STLNM">저장위치명</td>
                                        <td GH="100 STD_SHORTX" GCol="input,SHORTX" GF="S 180"></td> 
                                        <td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D"></td>
                                        <td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T"></td>
                                        <td GH="100 STD_CREUSR" GCol="text,CREUSR"></td>
                                        <td GH="100 STD_CUSRNM" GCol="text,CUSRNM"></td>
                                        <td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D"></td>
                                        <td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T"></td>
                                        <td GH="100 STD_LMOUSR" GCol="text,LMOUSR"></td>
                                        <td GH="100 STD_LUSRNM" GCol="text,LUSRNM"></td>
																		
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