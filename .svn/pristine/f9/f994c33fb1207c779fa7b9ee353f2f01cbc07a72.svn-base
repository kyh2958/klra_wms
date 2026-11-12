<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp"%>
<%@ page import="project.common.airfw.*"%>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<%
	User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%>
<script type="text/javascript">
	
	var dblIdx = -1;
	var subIdx = 0;
	var headSebeln = "";
	var headRownum = "";
	var searchFlag = 1;
	var paramH = new DataMap();
	
	$(document).ready(function(){
		setTopSize(250);
		
		gridList.setGrid({
			id : "gridList",
			module : "WmsInbound",
			command : "GR02",
			itemGrid : "gridListSub",
			itemSearch : true
		});
		
		gridList.setGrid({
			id : "gridListSub",
			module : "WmsInbound",
			command : "GR02Sub",
         	validation : "LOCAKY",
			defaultRowStatus : configData.GRID_ROW_STATE_START
		});
		
	   
		// 플랜트
		var $selPLNTKY = $('select[name="PO.LOTA02"]');
		$selPLNTKY.multipleSelect('checkAll');
		
		//$("#USERAREA").val("<%=user.getUserg5()%>");
		gridList.setReadOnly("gridListSub", true, ['LOTA06','LOTA08']);
	});
	
	function gridListEventItemGridSearch(gridId, rowNum, itemGridList){
		
		if(gridId == "gridList"){
	        var param = getItemSearchParam(rowNum);
	        
	        gridList.gridList({
	           id : "gridListSub",
	           param : param,
	        });
	        
	        dblIdx = rowNum;
			subIdx = rowNum;
		}
	}
	
	function gridListEventDataBindEnd(gridId, dataLength){
		
		if(gridId == "gridList"){
			var list = gridList.getGridData("gridList");
			for(var i=0; i < list.length; i++) {
				var STATDO = list[i].get("STATDO");
				if(STATDO!="NEW"){
					gridList.setRowReadOnly(gridId, i, true, ['rowCheck','DOCDAT','DOCTXT']);
				}
			}
		}else if(gridId == "gridListSub"){
			var list = gridList.getGridData("gridListSub");
			for(var i=0; i < list.length; i++) {
				var QTYRCV1 = list[i].get("QTYRCV1");
				if(QTYRCV1=="0"){
					gridList.setRowReadOnly(gridId, i, true, ['rowCheck','QTYRCV','LOTA11','LOTA14']);
				}
			}
		}
	}
	
 	
 	function getItemSearchParam(rowNum){
 		var rowData = gridList.getRowData("gridList", rowNum);
        var param = inputList.setRangeParam("searchArea");
        param.putAll(rowData);
        
        return param;
 	}
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			gridList.resetGrid("gridList");
			gridList.resetGrid("gridListSub");
			
			gridList.gridList({
		    	id : "gridList",
		    	param : param/*,
		    	url : "/wms/inbound/json/ebelnGR02.data"*/
		    });
		}
	}
	
	function gridExcelDownloadEventBefore(gridId){
		if(gridId == "gridList"){
			var param = inputList.setRangeParam("searchArea");
			return param;
		}else if(gridId == "gridListSub"){
			 var rowNum = gridList.getSearchRowNum("gridList");
			 if(rowNum == -1){
				 return false;
			 }else{
				 var param = getItemSearchParam(rowNum);
				 return param;
			 }
		}
	}
	
	function saveData(){
		
		if(gridList.validationCheck("gridListSub", "select")){
			var chkItemIdx = gridList.getSelectRowNumList("gridListSub");
			var chkItemLen = chkItemIdx.length;	
			var headChkLen = gridList.getSelectRowNumList("gridList").length;
			
			if( chkItemLen == 0 ){
				commonUtil.msgBox("VALID_M0006");// 선택된 아이템이 없습니다.
				return;
			}
			
			//저장하시겠습니까?
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
				return;
			}
			
			var chkItemList = gridList.getSelectData("gridListSub"); 
			
			var rowData;
			for(var i=0;i<chkItemList.length;i++){
				rowData = chkItemList[i];
				
				var QTYORD = new Number(rowData.get("QTYRCV1")); //오더수량
				var QTYRCV = new Number(rowData.get("QTYRCV")); //입고수량
				if((QTYORD - QTYRCV) < 0){
					commonUtil.msgBox("IN_M0078");		//입고수량은 입고가능수량보다 클 수 없습니다.
					var rowNum = rowData.get("GRowNum");
					gridList.setColFocus("gridListSub", rowNum, "QTYRCV");
					return;
				}
			}
			//alert("2 - headChkLen : " + headChkLen + "\ndblIdx : " + dblIdx + "\nsubIdx : " + subIdx);
			var list = "";
			if(headChkLen > 0 && dblIdx == subIdx){
				
				var head = gridList.getRowData("gridList", dblIdx);
				//list = gridList.getGridData("gridListSub");
				list = gridList.getSelectData("gridListSub");
				
				headSebeln = gridList.getColData("gridList", dblIdx, "SEBELN");
			}
			
			var head = gridList.getSelectData("gridList");
			//console.log(JSON.stringify(list));
			
			var param = new DataMap();
			param.put("head", head); 
			param.put("list", list);
			param.put("headSebeln", headSebeln);
			param.put("headModifyIndex", dblIdx);
			
			var json = netUtil.sendData({
				url : "/wms/inbound/json/SaveGR02.data",
				param : param
			});
			
			if(/*json.data.length > 5 && json.data.length != 10*/ json.data.RESULTMSG ){
				var msgList = json.data.split("†");
				var msgTxt = commonUtil.getMsg(msgList[0], (msgList.length > 1 ? msgList[1].split("/") : null));
				commonUtil.msg(msgTxt);
				return;
			}
			
			if(json && json.data){
				
				dblIdx = -1;
				
				commonUtil.msgBox("MASTER_M0564");
				gridList.resetGrid("gridList");
				gridList.resetGrid("gridListSub");
				searchList();
			}
		}
	}
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		
		//alert("gridId : " + gridId + "\nrowNum : " + rowNum + "\ncolName : " + colName + "\ncolValue : " + colValue);
		
		if (gridId == "gridList") {
			if (colName == "DOCDAT") {
				var rowData = gridList.getRowData(gridId, rowNum);
				var asndat = rowData.get("ASNDAT");
				var docdat = rowData.get("DOCDAT");
				var asndate = asndat-1;
				var now = new Date();
				now.setDate(now.getDate());
				var tmpValue = uiList.dateFormat(now, configData.COMMON_DATE_TYPE);
				
				if(docdat < asndate){
					//alert("과거일자로 입고하실 수 없습니다.");
					commonUtil.msgBox("IN_M0139")
					gridList.setColValue(gridId, rowNum, "DOCDAT", tmpValue);
				}
			}
		}else if (gridId == "gridListSub"){
			
			if (colName == "QTYRCV") {
				
				var param = new DataMap();
				
				var SKUKEY = gridList.getColData("gridListSub", rowNum, "SKUKEY");
				var UOMKEY = gridList.getColData("gridListSub", rowNum, "UOMKEY");
				param.put("SKUKEY", SKUKEY);
				param.put("QTY", colValue);
				param.put("UOMKEY", UOMKEY);
				
				var json = netUtil.sendData({
					module : "WmsInbound",
					command : "UOMQTYval",
					sendType : "map",
					param : param
				});
				
				var UOMQTY = json.data["UOMQTY"];
				gridList.setColValue(gridId, rowNum, "UOMQTY", UOMQTY);
				
				//alert("UOMQTY : " + UOMQTY);
				
				
			}
			
		}
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType){
		var param = new DataMap();
		if(searchCode == "SHSKUMA"){
			param.put("OWNRKY", "<%=ownrky%>");
			return param;
		}else if(searchCode == "SHBZPTN"){
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("PTNRTY", "VD");
			return param;
		}
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Print"){
			reportPrint();
		}
	}
	
	function reportPrint(){
		var url = "";
		var ebeln = "";
		var listcnt = gridList.getGridDataCount("gridList");
		
		if(listcnt < 1){
			commonUtil.msgBox("VALID_M0006");
			return;
		}

		var where =   "AND EBELN IN (" ;
		for(var i=0 ; i<listcnt ; i++){
			ebeln = gridList.getColData("gridList", i, "EBELN");		
			if(i>0){
				where += ", "
			}
			where += "'" + ebeln +"'";
		}
		where += ")";
		url = "/ezgen/ebeln_s.ezg";
				
		var map = new DataMap();
		WriteEZgenElement(url, where, "", '<%= langky%>', map, 650, 350);	
		
	}
	
	function comboEventDataBindeBefore(comboAtt){
		
		if(comboAtt == "WmsOrder,RCPTTYCOMBO"){
			var param = new DataMap();
			param.put("DOCCAT", "100");
			param.put("PROGID", configData.MENU_ID);
			return param;
		}else if(comboAtt == "WmsInbound,PLNTKYCOMBO"){
			var param = new DataMap();
			
			var WAREKY_LIST = ['<%= wareky%>'];
			param.put("WAREKY_LIST", WAREKY_LIST);
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			
			return param;
		}else if(comboAtt == "WmsInbound,SKUL01COMBO"){
			var param = new DataMap();
			
			// 화장지,패드,밀롤
			var CMCDVL_LIST = ['01', '02', '03'];
			param.put("CMCDVL_LIST", CMCDVL_LIST);
			
			return param;
		}else if(comboAtt == "WmsInbound,COMMON_VALUE_LANG"){
			var param = new DataMap();
			param.put("LANGKY", "<%=langky%>");
			param.put("CMCDKY", "LOTA06");
			return param;
		}
		
		
	}
	
</script>
</head>
<body>

<!-- contentHeader -->
<div class="contentHeader">
	<div class="util">
		<button CB="Search SEARCH BTN_DISPLAY">조회</button>
		<button CB="Save SAVE STD_INBOUND">입고</button>
		<button CB="Print PRINT BTN_IBLBPRINT">입고라벨출력</button>
	</div>
	<div class="util2">
		<button class="button type2" id="showPop" type="button"><img src="/common/images/ico_btn4.png" alt="List" /></button>
		
	</div>
</div>
<!-- //contentHeader -->

<!-- searchPop -->
<div class="searchPop">
	<button type="button" class="closer">X</button>
	<div class="searchInnerContainer" id="searchArea">
		<p class="util">
			<button CB="Search SEARCH BTN_DISPLAY"></button>
			<!--<button CB="GetVariant GETVARIANT BTN_GETVARIANT"></button>-->
			<!--<button CB="SaveVariant SAVEVARIANT BTN_SAVEVARIANT"></button>-->
		</p>
		<!-- searchInBox -->		
		<div class="searchInBox">
			<h2 class="tit" CL="STD_SELECTOPTIONS">검색조건</h2>
			<table class="table type1">
				<colgroup>
					<col width="100" />
					<col />
				</colgroup>
				<tbody>
					<tr>
						<th CL="STD_WAREKY"></th>
						<td>
							<input type="text" name="WAREKY" name="WAREKY"  value="<%=wareky%>" readonly="readonly" validate="required"/>
						</td>
					</tr>
					<tr style="display:none">
						<th CL="STD_OWNRKY">화주</th>
						<td>
							<select Combo="WmsOrder,OWNRKYCOMBO" name="OWNRKY" id="OWNRKY" STYLE="width: 150px;">
							</select>
						</td>
					</tr>
					<tr>
						<th CL="STD_LOTA02" style="vertical-align:middle">플랜트</th>
						<td >
							<select Combo="WmsInbound,PLNTKYCOMBO" name="PO.LOTA02" id="PLNTKY" ComboType="MS" style="width: 160px;" validate="required">
							</select>
						</td>
					</tr>
					<tr>
						<th CL="STD_LOTA01">저장위치</th>
						<td>
							<input type="text" name="PO.LOTA01" UIInput="R,SHSTLMA" />
						</td>
					</tr>
					
				</tbody>
			</table>
		</div>
		
		<div class="searchInBox">
			<h2 class="tit type1" CL="STD_DOCUMENTINFO">문서정보</h2>
			<table class="table type1">
				<colgroup>
					<col width="100" />
					<col />
				</colgroup>
				<tbody>
					<tr>
						<th CL="STD_RCPTTY">입고유형</th>
						<td>
							<select Combo="WmsOrder,RCPTTYCOMBO" name="RCPTTY" id="RCPTTY" STYLE="width: 160px;" validate="required(STD_RCPTTY)">
							</select>
						</td>
					</tr>
					<tr>
						<th CL="STD_STATDO">문서상태</th>
						<td>
							<input type="checkbox" name="STTNEW" value="V" checked="checked"/>
							<label CL="STD_NEW">미작업</label>
							<input type="checkbox" name="STTTC2" value="V"/>
							<label CL="STD_EXRECP">입고완료</label>
						</td>
					</tr>
					<tr>
						<th CL="STD_EBELN">오더번호</th>
						<td>
							<input type="text" name="PO.EBELN" UIInput="R" />
						</td>
					</tr>
					<tr>
						<th CL="STD_ORDDAT">오더일자</th>
						<td>
							<input type="text" name="PO.BEDAT" UIInput="R" UIFormat="C N" validate="required"/>
						</td>
					</tr>
					
					
					
				</tbody>
			</table>
		</div>
		
		
		<div class="searchInBox">
			<h2 class="tit type1" CL="STD_PTNLINFO">공급처정보</h2>
			<table class="table type1">
				<colgroup>
					<col width="100" />
					<col />
				</colgroup>
				<tbody>
					<tr>
						<th CL="STD_DPTNKY,3">공급사</th>
						<td>
							<input type="text" name="PO.LIFNR" UIInput="R,SHBZPTN" />
						</td>
					</tr>
					<tr>
						<th CL="STD_DPTNNM">공급사명</th>
						<td>
							<input type="text" name="BZ.NAME01" UIInput="R" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>	
		<div class="searchInBox">
			<h2 class="tit type1" CL="STD_SKUINFO">상품정보</h2>
			<table class="table type1">
				<colgroup>
					<col width="100" />
					<col />
				</colgroup>
				<tbody>
					<tr>
						<th CL="STD_MATNR">품번</th>
						<td>
							<input type="text" name="PO.SKUKEY" UIInput="R,SHSKUMA" />
						</td>
					</tr>
					<tr>
						<th CL="STD_DESC01">품명</th>
						<td>
							<input type="text" name="SM.DESC01" UIInput="R" />
						</td>
					</tr>
					<tr>
						<th CL="STD_SKUL01">품목구분</th>
						<td>
							<select Combo="WmsInbound,SKUL01COMBO" name="SKUL01" id="SKUL01" STYLE="width: 160px;" >
							  <option value=""></option>
							</select>
						</td>
					</tr>
					<tr>
						<th CL="STD_LOTA14">제조LOT</th>
						<td>
							<input type="text" name="RI.LOTA14" UIInput="R" />
						</td>
					</tr>
					
					
				</tbody>
			</table>
		</div>
		
		
		<!-- //searchInBox -->
	</div>
</div>
<!-- //searchPop -->

<!-- content -->
<div class="content">
	<div class="innerContainer">
		<!-- contentContainer -->
		<div class="contentContainer">
			<div class="bottomSect top">
				<div class="tabs">
					<ul class="tab type2">
						<li><a href="#tabs1-1" CL="STD_DOCUMENTINFO"><span>문서정보</span></a></li>
					</ul>
					<div id="tabs1-1">
						<div class="section type1">
							<div class="table type2">
								<div class="tableBody">
									<table>
										<tbody id="gridList">
											<tr CGRow="true">
												<td GH="40" GCol="rownum">1</td>
												<td GH="40" GCol="rowCheck"></td>
												
												<td GH="70 STD_STATDO" GCol="text,STATDO">문서상태</td>
												<td GH="80 STD_STATDONM,2" GCol="text,STATDONM">문서상태명</td>
												<td GH="70 STD_DOCUTY" GCol="text,DOCUTY">문서유형</td>
												<td GH="100 STD_DOCUTYNM" GCol="text,RCPTNM">문서유형명</td>
												<td GH="100 STD_DOCDAT" GCol="input,DOCDAT" GF="C N" validate="required">전기일자</td>
<!-- 												<td GH="90 STD_RECVKY" GCol="text,RECVKY">입고문서번호</td> -->
												<td GH="90 STD_EBELN,3" GCol="text,SEBELN">오더번호 (생산지시번호)</td>
<!-- 												<td GH="90 STD_EBELP,3" GCol="text,EBELP">오더아이템번호</td> -->
												<td GH="100 STD_DPTNKY,3" GCol="text,DPTNKY">공급사</td>
												<td GH="180 STD_DPTNNM" GCol="text,DPTNNM">공급사명</td>
												<td GH="100 STD_ORDDAT" GCol="text,BEDAT" GF="C N">오더일자</td>
<!-- 												<td GH="100 STD_ORDNUM,3" GCol="text,ORDNUM">선적번호</td> -->
												<td GH="180 STD_DOCTXT" GCol="input,DOCTXT">비고</td>
												<td GH="100 STD_DOCCAT" GCol="text,DOCCAT">문서카테고리</td>
												<td GH="100 STD_DOCCATNM" GCol="text,DOCCATNM">문서카테고리명</td>
												<td GH="100 STD_DOCGRP" GCol="text,DOCGRP">문서타입그룹</td>
												<td GH="60 STD_WAREKY" GCol="text,WAREKY">물류센터</td>
												<td GH="120 STD_WARENM" GCol="text,WARENM">물류센터명</td>
<!-- 												<td GH="60 STD_ODSYST" GCol="text,MANDT">오더시스템</td> -->
												
<!-- 												<td GH="100 STD_BWART" GCol="text,BWART">오더유형</td> -->
												
												<td GCol="text,RCOLOR" style="display:none;">색</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div class="tableUtil">
								<div class="leftArea">
									<button type="button" GBtn="find"></button>
									<button type="button" GBtn="sortReset"></button>
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
			
			<div class="bottomSect bottom">
				<div class="tabs">
					<ul class="tab type2" id="commonMiddleArea">
						<li><a href="#tabs1-1" id="tab01" onclick="tabChg(0);" CL="STD_ITEMLIST"><span>아이템 리스트</span></a></li>
					</ul>
					<div id="tabs1-1">
						<div class="section type1">
							<div class="table type2" style="top:10px;">
								<div class="tableBody">
									<table>
										<tbody id="gridListSub">
											<tr CGRow="true">
												<td GH="40" GCol="rownum">1</td>
												<td GH="40" GCol="rowCheck"></td>

<!-- 												<td GH="80 STD_STATIT" GCol="text,STATIT">아이템상태</td> -->
<!-- 												<td GH="100 STD_STATNM" GCol="text,STATITNM">아이템상태명</td> -->
												<td GH="120 STD_SKUKEY" GCol="text,SKUKEY">상품코드</td>
												<td GH="250 STD_DESC01" GCol="text,DESC01">상품명</td>
												
												
												<td GH="80 STD_QTYORG,3" GCol="text,MENGE" GF="N 20,3">원오더수량</td>
												
												<td GH="100 STD_QTSTOK1" GCol="text,QTYRCV1" GF="N 20,3">입고가능수량</td>
												<td GH="100 STD_QTYRCV" GCol="input,QTYRCV" GF="N 20,3" validate="required max(GRID_COL_QTYRCV1_*),IN_M0078">입고수량</td>
<!-- 												<td GH="100 STD_QTYRCV" GCol="input,QTYRCV" GF="N 20,3" validate="required">입고수량</td> -->
												
												<td GH="100 STD_UOMKEY" GCol="text,UOMKEY">단위</td>
												
												<td GH="100 STD_UOMQTY,3" GCol="text,UOMQTY" GF="N 20,3">기본단위수량</td>
												<td GH="100 STD_UOMKEY,3" GCol="text,MUOMKEY">기본단위</td>
												<td GH="100 STD_MENGE_B" GCol="text,MENGE_R" GF="N 20,3">기입고수량</td>
												
												
												<td GH="100 STD_LOTA02" GCol="text,PLNTNM">플랜트</td>
												<td GH="100 STD_LOTA01" GCol="text,STLNM">저장위치</td>
												<td GH="100 STD_LOTA03" GCol="text,LOTA03">공급업체</td>
												<td GH="100 STD_LOTA06" GCol="select,LOTA06">
													<select CommonCombo="LOTA06">재고상태</select>
												</td>
												<td GH="120 STD_LOTA08" GCol="select,LOTA08">
													<select CommonCombo="NATNKY">국가코드</select>
												</td>
												<td GH="120 STD_LOTA09" GCol="text,LOTA09">거래처코드MTO</td>
												<td GH="100 STD_LOTA11" GCol="input,LOTA11" GF="C">제조일자</td>
												<td GH="100 STD_LOTA12" GCol="text,LOTA12" GF="D">입고일자</td>
												<td GH="100 STD_LOTA13" GCol="text,LOTA13" GF="D">유통기한</td>
												<td GH="100 STD_LOTA14" GCol="input,LOTA14">제조LOT</td>
												<td GH="100 STD_LOTA15" GCol="text,LOTA15">SAP배치번호</td>
												<td GH="100 STD_EBELN" GCol="text,EBELN">오더번호</td>
												<td GH="100 STD_EBELP" GCol="text,EBELP">오더순번</td>
												<td GH="100 STD_BEDAT" GCol="text,BEDAT" GF="D">오더일자</td>
												<td GH="100 STD_EANCOD,3" GCol="text,EANCOD">바코드</td>
												
												
												<td GH="60 STD_WAREKY" GCol="text,WAREKY">물류센터</td>
												<td GH="100 STD_WARENM" GCol="text,WARENM">물류센터명</td>
												
												<td GH="100 STD_TOTWG" GCol="text,TOT" GF="N 20,3">총중량</td>
												<td GH="100 STD_BAGQT" GCol="text,BAG" GF="N 20,3">EA/BAG</td>
												<td GH="100 STD_BOXQT" GCol="text,BOX" GF="N 20,3">BAG/BOX</td>
												<td GH="100 STD_PLTQT" GCol="text,PLT" GF="N 20,3">BOX/Pallet</td>
												<td GH="100 STD_SOKQT" GCol="text,SOK" GF="N 20,3">속(개/BALE)</td>
												<td GH="100 STD_EAQTY" GCol="text,EA" GF="N 20,3">매(sheet/속)</td>
												
												
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
		<!-- //contentContainer -->
	</div>
</div>
<!-- //content -->
<%@ include file="/common/include/webdek/bottom.jsp"%>
</body>
</html>