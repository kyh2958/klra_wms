<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<script type="text/javascript">
	var flag = "U";
	var wareky = "";
	var locaky="";
	$(document).ready(function(){
		gridList.setGrid({
	    	id : "gridList",
	    	name : "gridList",
			editable : true,
			pkcol : "WAREKY,LOCAKY",
			module : "WmsAdmin",
			command : "LOCMA",
			validation : "WAREKY,ZONEKY,LOCAKY",
			bindArea : "tabs1-2" 
	    });
		gridList.setGrid({
			id : "gridListHidden",
			name : "gridListHidden",
			editable : true,
			module : "WmsAdmin",
			command : "LOCMA",
			colorType : true
		});
		
		// 플랜트
		//var $selPLNTKY = $('select[name="ZA.PLNTKY"]');
		//$selPLNTKY.multipleSelect('checkAll');
		gridList.setReadOnly("gridList", true, ['PLNTKY','STLKY']);
	});
	
	function searchList(){
		var flag = "U";
//		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			gridList.gridList({
		    	id : "gridList",
		    	param : param
		    });
//		}
	}
	
	function creatList(){
		flag = "I";
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			wareky = param.get("WAREKY");
			//locaky=param.get("RANGE_SQL");	
			locaky=$("[name=LOCAKY]").parent().find(".searchInput").val();
			
			if(!locaky){
				commonUtil.msgBox("HHT_T0032"); //지번을 입력하세요
				return;
			}
			
			param.put("LOCAKY",locaky);
			
			var json = netUtil.sendData({
				module : "WmsAdmin",
				command : "LOCMAval",
				sendType : "map",
				param : param
			});
			
			if(json.data["CNT"] > 0) {
				commonUtil.msgBox("VALID_M0104",locaky); // 이미 존재하는 지번입니다.
				return;
			}
			
			/* gridList.gridList({
		    	id : "gridList",
		    	module : "WmsAdmin",
				command : "LOCMA2",
		    	param : param
		    }); */
		    
			var newData = new DataMap();
			newData.put("LOCAKY", locaky);
			newData.put("WAREKY", "<%=wareky%>");
			newData.put("LOCATY", "10"); 
			newData.put("STATUS", "00");
			newData.put("MAXCPC", 0);
			newData.put("ERPLOC", " ");
			newData.put("SFSTOK", 0);
			newData.put("WIDTHW", 0);
			newData.put("HEIGHT", 0);
			gridList.setAddRow("gridList", newData);
			newData.clear();
			searchOpen(false);
		}
	}
	
	function saveData(){
//		if(checkValidationType){
		if(gridList.validationCheck("gridList","modify")){
			var param = dataBind.paramData("searchArea");

			var map = new DataMap();
			var modifyList = gridList.getModifyList("gridList","A");
			
			var json = gridList.gridSave({
				id : "gridList",
				param : param,
				modifyType : 'A'
			});

			param.put("list", modifyList);
			var json = netUtil.sendData({
				url : "/wms/admin/json/saveML01.data",
				param : param
			});
			
			if(json && json.data){
				//commonUtil.msgBox("HHT_T0008");   //저장되었습니다.
				searchList();
			} else {
				commonUtil.msgBox("MASTER_M0545");
				searchList();
			}
		}
	}
	
//	var checkValidationType = true;
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId == "gridList" && colName == "ZONEKY"){
			if(colValue != ""){
				var param = new DataMap();
				var rowState = gridList.getRowState(gridId, rowNum);
				param.put("WAREKY","<%=wareky%>");
				param.put("ZONEKY",colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "ZONEKYval",
					sendType : "map",
					param : param
				});
				if(json.data["CNT"] > 0) {
					var param = new DataMap();
					param.put("WAREKY","<%=wareky%>");
					param.put("ZONEKY",colValue);
					
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "AREAKY",
						sendType : "map",
						param : param
					});
					
					if(json && json.data){
						gridList.setColValue("gridList", rowNum, "AREAKY", json.data["AREAKY"]);
						//구역을 수정하였을 때 구역이 신규인 상태일 떄만 작업구역에도 똑같이 값 세팅
						if(rowState == "C"){
							gridList.setColValue("gridList", rowNum, "TKZONE", json.data["AREAKY"]);	
						}
						
					} 
				} else if (json.data["CNT"] < 1) {
					commonUtil.msgBox("MASTER_M0044", colValue);
					gridList.setColValue("gridList", rowNum, "ZONEKY", ""); 
					gridList.setColValue("gridList", rowNum, "AREAKY", "");
					return;
				}
			}else if(colValue==""){
				gridList.setColValue("gridList", rowNum, "AREAKY", "");
			}
		}
		
		if(gridId == "gridList" && colName == "LOCAKY"){
			if(colValue != ""){
				var param = new DataMap();
				param.put("WAREKY", "<%=wareky%>");
				param.put("LOCAKY", colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "LOCMAval",
					sendType : "map",
					param : param
				});
				
				if(json.data["CNT"] > 0) {
					commonUtil.msgBox("VALID_M0304", colValue);
					gridList.setColValue("gridList", rowNum, "LOCAKY", "");	
					return;
				}
			}
		}
		
		if (gridId == "gridList" && colName == "TKZONE") {
			if(colValue != ""){
				var param = new DataMap();
				param.put("WAREKY","<%=wareky%>");
				param.put("TKZONE",colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "TKZONEval",
					sendType : "map",
					param : param
				});
				if(json.data["CNT"] < 1) {
					commonUtil.msgBox("MASTER_M0461", colValue);  //작업구역이 올바르지 않습니다.
					gridList.setColValue("gridList", rowNum, "TKZONE", ""); 
					return;
				}
			}
		}
		
		if (gridId == "gridList" && colName == "MAXCPC") {
			if(colValue != ""){
				//자릿수 제한 체크
				var maxcpc = gridList.getColData(gridId, rowNum, "MAXCPC");
				var lengthchk = maxcpc.length;
				
				if(lengthchk > 19){
					commonUtil.msgBox("COMMON_M0054");
					gridList.setColFocus(gridId, rowNum, colName);
					return;
				}
			}
		}
	}
	
<%-- 	function gridListEventColValueChange(gridId, rowNum, colName, colValue){		
		if(gridId == "gridList" && colName == "ZONEKY"){
			if(colValue != ""){
				var param = new DataMap();
				param.put("WAREKY","<%=wareky%>");
				param.put("ZONEKY",colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "ZONEKYval",
					sendType : "map",
					param : param
				});
				if(json.data["CNT"] > 0) {
					var param = new DataMap();
					param.put("WAREKY","<%=wareky%>");
					param.put("ZONEKY",colValue);
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "AREAKY",
						sendType : "map",
						param : param
					});
					
					if(json && json.data){
						gridList.setColValue("gridList", rowNum, "AREAKY", json.data["AREAKY"]); 
					} 
					checkValidationType = true;
				} else if (json.data["CNT"] < 1) {
					commonUtil.msgBox("MASTER_M0044", colValue);
					gridList.setColValue("gridList", rowNum, "ZONEKY", ""); 
					checkValidationType = false;
				}
			}else if(colValue==""){
				gridList.setColValue("gridList", rowNum, "AREAKY", "");
				checkValidationType = true;
			}
		} else if (gridId == "gridList" && colName == "TKZONE") {
			if(colValue != ""){
				var param = new DataMap();
				param.put("WAREKY","<%=wareky%>");
				param.put("TKZONE",colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "TKZONEval",
					sendType : "map",
					param : param
				});
				if(json.data["CNT"] < 1) {
					commonUtil.msgBox("MASTER_M0461", colValue);  //작업구역이 올바르지 않습니다.
					gridList.setColValue("gridList", rowNum, "TKZONE", ""); 
					checkValidationType = false;
				}else{
					checkValidationType = true;
				}
			}
		} else if (gridId == "gridList" && colName == "MAXCPC") {
			if(colValue != ""){
				//자릿수 제한 체크
				var maxcpc = gridList.getColData(gridId, rowNum, "MAXCPC");
				var lengthchk = maxcpc.length;
				
				if(lengthchk > 19){
					alert("정수는 15자리까지 가능 / 소수점은 3자리까지 가능");
					gridList.setColFocus(gridId, rowNum, colName);
					return;
				}
			}
		}
	}
	 --%>
	 
	function gridListEventRowCopyAfter(gridId, rowNum){
		if(gridId == "gridList") {
			gridList.setColValue("gridList", rowNum, "LOCAKY", "");	
		}
	}

 	function gridListEventRowAddBefore(gridId, rowNum){
 		
		var newData = new DataMap();
		newData.put("WAREKY", "<%=wareky%>");
		newData.put("LOCATY", "10"); 
		newData.put("STATUS", "00");
		newData.put("LENGTH", 0);
		newData.put("WIDTHW", 0);
		newData.put("HEIGHT", 0);
		newData.put("CUBICM", 0);
		newData.put("MAXCPC", 0);
		newData.put("MAXQTY", 0);
		newData.put("MAXWGT", 0);
		newData.put("MAXLDR", 0);
		newData.put("MAXSEC", 0);
		newData.put("QTYCHK", 0);
		
		return newData;
	}  
 	
 	
 	function locakyCheck(valueTxt, $colObj){
		var rowNum = gridList.getColObjRowNum("gridList", $colObj);
		var rowCount = gridList.getGridDataCount("gridList");
		for(var i=0;i<rowCount;i++){
			if(i != rowNum){
				var locaky = gridList.getColData("gridList", i, "LOCAKY");
				if(locaky == valueTxt){
					return false;
				}
			}			
		}
		return true;
	}
 	
 	function gridListEventRowRemove(gridId, rowNum){
		var rowWar = gridList.getColData("gridList", rowNum, "WAREKY");
		var rowLoc = gridList.getColData("gridList", rowNum, "LOCAKY");
		  if(gridId == "gridList"){
			var param = new DataMap();
			param.put("WAREKY", rowWar);
			param.put("LOCAKY", rowLoc);
			var json = netUtil.sendData({
				module : "WmsAdmin",
				command : "LOCMADELval",
				sendType : "map",
				param : param
			});
			
			if(json && json.data){
				if(json.data["CNT"] > 0){
					commonUtil.msgBox("MASTER_M2008", rowNum);
					/* alert("선택한 지번에 재고수량이 존재하여 삭제할 수 없습니다."); */
					return false;
				} else if (json.data["CNT"] == 0){
					return true;
				}
			}
		}  
	}
 	
 	function searchHelpEventOpenBefore(searchCode, gridType){
		 if(searchCode == "SHWAHMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode=="SHAREMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode=="SHZONMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode=="SHLOCMA"){
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
 	
 	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Create"){
			creatList();
		}else if(btnName == "Print"){
			reportPrint();
		}else if(btnName == "A4Print"){
			A4reportPrint();	
		}else if(btnName == "UploadRcvList") {
			// 엑셀업로드
			var rowNum = gridList.getFocusRowNum("gridList")
			var data = gridList.getRowData("gridList", rowNum);
			page.linkPopOpen("/wms/admin/ML01POP.page", data);
		}else if(btnName == "Download"){
			excelDownload();
		}
	}
 	
	function reportPrint(){
		var list = gridList.getSelectData("gridList");
		
		if (list.length == 0){
			commonUtil.msgBox("VALID_M0006");
			return;
		}
		
		if(!commonUtil.msgConfirm("SYSTEM_PRINTYN")){
			return;
		} 
		
		var param = new DataMap();
		param.put("list",list);
		param.put("columnList", "WAREKY,LOCAKY");
		param.put("PRTTYP","LOC_BCD");
		param.put("OWNRKY", "<%=ownrky%>");
		
		var json = netUtil.sendData({
			url : "/wms/admin/json/commonPrint.data",
			param : param
		});
		
		var prtseq = null;
		if(json && json.data){
			prtseq = json.data;
		}else{
			alert("출력불가");
			return;
		}
		
		var url = "";
		var wareky  = "<%=wareky%>";
		var where = "AND WAREKY='"+wareky+"' AND (";
		for(var i=0; i<list.length; i++){
			var row = list[i];
			var key = row.get("LOCAKY");
			
			where += "LOCAKY='"+key+"'";
			if(i==list.length-1) break;
			where += " OR ";	
		}
		where += ")"; 
		
		url = "Location_Barcode_5_by_8.ezg";
		
		var map = new DataMap();
		WriteEZgenElement(url, where, "", "<%=langky%>", map, 900, 400);
	}
	
	function A4reportPrint(){
		var list = gridList.getSelectData("gridList");
		
		if (list.length == 0){
			commonUtil.msgBox("VALID_M0006");
			return;
		}
		
		if(!commonUtil.msgConfirm("SYSTEM_PRINTYN")){
			return;
		} 
		
		var param = new DataMap();
		param.put("list",list);
		param.put("columnList", "WAREKY,LOCAKY");
		param.put("PRTTYP","LOC_BCD");
		param.put("OWNRKY", "<%=ownrky%>");
		
		var json = netUtil.sendData({
			url : "/wms/admin/json/commonPrint.data",
			param : param
		});
		
		var prtseq = null;
		if(json && json.data){
			prtseq = json.data;
		}else{
			alert("출력불가");
			return;
		}
		
		var url = "";
		var wareky  = "<%=wareky%>";
		var where = "AND WAREKY='"+wareky+"' AND (";
		for(var i=0; i<list.length; i++){
			var row = list[i];
			var key = row.get("LOCAKY");
			
			where += "LOCAKY='"+key+"'";
			if(i==list.length-1) break;
			where += " OR ";	
		}
		where += ")"; 
		
		url = "Location_Barcode_A4.ezg";
		
		var map = new DataMap();
		WriteEZgenElement(url, where, "", "<%=langky%>", map, 850, 640);
	}
	
	function excelDownload(){
		gridList.excelUpGridId = "gridListHidden";
	    gridList.excelUploadTemplateDown();
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
					<input type="button" CB="Print PRINT BTN_LOCAPRINT" />
					<input type="button" CB="A4Print PRINT BTN_LOCAA4PRINT" />
					<input type="button" CB="UploadRcvList UP BTN_EXCELUP" />
					<input type="button" CB="Download REFLECT BTN_TEMPLD" id="Download" />
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
						<dt CL="STD_AREAKY">영역</dt>
						<dd>
							<input type="text" name="A.AREAKY" class="input" UIInput="R,SHAREMA"/>
						</dd>
					</dl>					
					<dl>
						<dt CL="STD_ZONEKY">구역</dt>
						<dd>
							<input type="text" name="A.ZONEKY" class="input" UIInput="R,SHZONMA"/>
						</dd>
					</dl>		
					<dl>
						<dt CL="STD_TKZONE">작업구역</dt>
						<dd>
							<input type="text" name="A.TKZONE" class="input" UIInput="R"/>
						</dd>
					</dl>	
					<dl>
						<dt CL="STD_LOCAKY">지번</dt>
						<dd>
							<input type="text" name="A.LOCAKY" class="input" UIInput="R,SHLOCMA"/>
						</dd>
					</dl>	
					<dl>
						<dt CL="STD_LOCATY">로케이션유형</dt>
						<dd>
							<select CommonCombo="LOCATY" name="LOCATY" class="input" style="width: 150px">
                                <option value="">All</option>
                            </select>
						</dd>
					</dl>	
					<dl>
						<dt CL="STD_STATUS">상태</dt>
						<dd>
							<select CommonCombo="STATUS" name="STATUS" class="input" style="width: 150px">
                                <option value="">All</option>
                            </select>
						</dd>
					</dl>	
					<dl>
						<dt CL="STD_LOTA02" style="vertical-align:middle">플랜트</dt>
						<dd>
                            <select CommonCombo="LOTA02" class="input" name="ZA.PLNTKY" ComboCodeView=false ComboType="MS" style="width: 160px" >
                            </select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA01">저장위치</dt>
						<dd>
							<input type="text" name="ZA.STLKY" class="input" UIInput="R,SHSTLMA"/>
						</dd>
					</dl>	
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
			</div>
		</div>
        <div class="search_next_wrap">
			<div class="content_layout tabs top_layout">
				<ul class="tab tab_style02">
					<li><a href="#tabs1-1" CL='STD_LIST'><span>리스트</span></a></li>
					<!-- <li><a href="#tabs1-2" CL='STD_ITEMDET'><span>Item상세</span></a></li> -->
					<li class="btn_zoom_wrap">
						<ul>
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section" id="tabs1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40 STD_NUMBER" GCol="rownum">1</td>
										<td GH="40" GCol="rowCheck"></td>
										<td GH="80 STD_WAREKY" GCol="text,WAREKY"></td>
										<td GH="80 STD_AREAKY" GCol="text,AREAKY" id="AREAKY"></td>
										<td GH="80 STD_ZONEKY" GCol="input,ZONEKY,SHZONMA" validate="required" GF="S 10"></td> 
										<td GH="80 STD_LOCAKY" GCol="input,LOCAKY" validate="required" GF="S 20"></td>
										<td GH="80 STD_SHORTX" GCol="input,SHORTX"></td>
										<td GH="100 STD_PLNTKY" GCol="select,PLNTKY">
                                            <select Combo="Wms,PLNTKYCOMBO" >
                                                <option value=" "></option>
                                            </select>
                                        </td>
                                        <td GH="100 STD_LOTA01" GCol="input,STLKY,SHSTLMAP"></td>
                                        <td GH="200 STD_STLNM" GCol="text,STLKYNM">저장위치명</td>
										<td GH="80 STD_TKZONE" GCol="input,TKZONE,SHZONMA" validate="required" GF="S 10"></td>
										<td GH="100 STD_LOCATY " GCol="select,LOCATY">
											<select CommonCombo="LOCATY">
											</select>
										</td>
										<td GH="80 STD_STATUS " GCol="select,STATUS">
											<select CommonCombo="STATUS">
											</select>
										</td>
										<td GH="80 STD_INDUPA" GCol="check,INDUPA"></td>
										<td GH="80 STD_INDUPK" GCol="check,INDUPK"></td>
										<td GH="80 STD_INDCPC" GCol="check,INDCPC"></td>
										<td GH="80 STD_MAXCPC" GCol="input,MAXCPC" GF="N 15,3"></td>
										<td GH="80 STD_MAXWGT" GCol="input,MAXWGT" GF="N 15,3"></td>
										<!-- <td GH="80 STD_ERPLOC'" GCol="input,ERPLOC" GF="S 20"></td>
										<td GH="80 STD_SFSTOK" GCol="input,SFSTOK" GF="N 20,3"></td> -->
										<td GH="80 STD_LENGTH" GCol="input,LENGTH" GF="N 15,3"></td>
										<td GH="80 STD_WIDTHW" GCol="input,WIDTHW" GF="N 15,3"></td>
										<td GH="80 STD_HEIGHT" GCol="input,HEIGHT" GF="N 15,3"></td>
										<td GH="80 STD_MIXSKU" GCol="check,MIXSKU"></td>
										<td GH="80 STD_MIXLOT" GCol="check,MIXLOT"></td>
										<td GH="80 STD_IBROUT" GCol="input,IBROUT" GF="S 10"></td>
										<td GH="80 STD_OBROUT" GCol="input,OBROUT" GF="S 10"></td>
										<td GH="80 STD_RPROUT" GCol="input,RPROUT" GF="S 10"></td>		
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
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
				<div class="table_box section" id="tabs1-2" style="display:none";>
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridListHidden">
									<tr CGRow="true">
										<td GH="40 STD_NUMBER" GCol="rownum">1</td>
										<td GH="40" GCol="rowCheck"></td>
										<td GH="80 STD_WAREKY" GCol="text,WAREKY"></td>
										<td GH="80 STD_AREAKY" GCol="text,AREAKY" id="AREAKY"></td>
										<td GH="80 STD_ZONEKY" GCol="input,ZONEKY,SHZONMA" validate="required" GF="S 10"></td> 
										<td GH="80 STD_LOCAKY" GCol="input,LOCAKY" validate="required" GF="S 20"></td>
										<td GH="80 STD_TKZONE" GCol="input,TKZONE,SHZONMA" validate="required" GF="S 10"></td>
										<td GH="100 STD_PLNTKY" GCol="select,PLNTKY">
                                            <select Combo="Wms,PLNTKYCOMBO" >
                                                <option value=" "></option>
                                            </select>
                                        </td>
                                        <td GH="100 STD_LOTA01" GCol="input,STLKY,SHSTLMAP"></td>
										<td GH="100 STD_LOCATY" GCol="select,LOCATY">
											<select CommonCombo="LOCATY">
											</select>
										</td>
										<td GH="80 STD_STATUS" GCol="select,STATUS">
											<select CommonCombo="STATUS">
											</select>
										</td>
										<td GH="80 STD_INDUPA" GCol="check,INDUPA"></td>
										<td GH="80 STD_INDUPK" GCol="check,INDUPK"></td>
										<td GH="80 STD_INDCPC" GCol="check,INDCPC"></td>
										<td GH="80 STD_MAXWGT" GCol="input,MAXWGT" GF="N 15,3"></td>
										<td GH="80 STD_MAXCPC" GCol="input,MAXCPC" GF="N 15,3"></td>
										<!-- <td GH="80 STD_ERPLOC" GCol="input,ERPLOC" GF="S 20"></td>
										<td GH="80 STD_SFSTOK" GCol="input,SFSTOK" GF="N 20,3"></td> -->
										<td GH="80 STD_LENGTH" GCol="input,LENGTH" GF="N 15,3"></td>
										<td GH="80 STD_WIDTHW" GCol="input,WIDTHW" GF="N 15,3"></td>
										<td GH="80 STD_HEIGHT" GCol="input,HEIGHT" GF="N 15,3"></td>
										<td GH="80 STD_MIXSKU" GCol="check,MIXSKU"></td>
										<td GH="80 STD_MIXLOT" GCol="check,MIXLOT"></td>
										<td GH="80 STD_IBROUT" GCol="input,IBROUT" GF="S 10"></td>
										<td GH="80 STD_OBROUT" GCol="input,OBROUT" GF="S 10"></td>
										<td GH="80 STD_RPROUT" GCol="input,RPROUT" GF="S 10"></td>		
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
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