<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String ebeln = request.getParameter("EBELN");
	if(ebeln==null)ebeln="";
	String docdat = request.getParameter("DOCDAT");
	if(docdat==null)docdat="";
	String barcod = request.getParameter("BARCOD");
	if(barcod==null)barcod="";
	
%>

<!doctype html>
<head>
<style type="text/css">
.tem5_content .tableWrap_search select	{width:172px;  height:25px; }
.tem5_content .tableWrap_search .text_2{width:90%; margin-right:3px;height:22px;border:#b8b8b8;}

.select-editable { position:relative; background-color:white; border:solid 1px #b8b8b8; width:172px; height:25px; }
.select-editable select { top:0px; left:0px; width:172px; height:25px; font-size:14px;  border:none;  }
.select-editable input { position:absolute; top:0px; left:0px; width:90%; height:23px; font-size:12px;  border:none;   }
.select-editable select:focus, .select-editable input:focus { outline:none; }

.select-editable { position:relative; background-color:white; border:solid 1px #d5d5d5; width:100%; height:35px; }
.select-editable select { position:absolute; top:0px; left:0px; width:100%; height:35px; font-size:14px;  border:none;  }
.select-editable input { position:absolute; top:0px; left:0px; width:90%; height:33px; font-size:12px;  border:none;   }
.select-editable select:focus, .select-editable input:focus { outline:none; }
/* .tableBody{height: 205px !important;}  */

</style>

<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>Mobile WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	var data;
	var backYN=false; // 뒤로가기여부
	var year;
	var month;
	var date;
	
	$(document).ready(function(){
		
		gridList.setGrid({
			id : "gridList",
			editable : false,
			module : "WmsMobileInboundGoodsReceipt",
			command : "PDGR01",
			gridMobileType : true
		});
		
		var json = netUtil.sendData({
			module : "WmsMobileInboundGoodsReceipt",
			command : "GR_CURDATE",
			bindId : "gridHeadList",
			sendType : "map"
		});
		if(json && json.data){
			year = json.data.DOCDAT.substr(0,4);
			month = json.data.DOCDAT.substr(4,2);
			date = json.data.DOCDAT.substr(6,2);
			
			$("#DOCDAT").val(year + "." + month + "." + date);
		}
		
		$("#BARCOD").focus();
		
		$("#EBELN").val("<%=ebeln%>");
		$("#BARCOD").val("<%=barcod%>");
		
		if("<%=ebeln%>"!="" || "<%=docdat%>"!=""){
			backYN = true;
			searchList();
		}
		
	});
	
	function searchList(){	
		
		var EBELN = $.trim($("#EBELN").val());
		var BARCOD = $.trim($("#BARCOD").val());
		
		if($.trim(EBELN)=="" && $.trim(BARCOD)==""){
			PlaySound('e');
			commonUtil.msgBox("VALID_M0965"); // 오더번호나 바코드를 입력해주세요.
			return;
		}
		
		var param = new DataMap();
		param.put("LANGKY", "<%=langky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("WAREKY", "<%=wareky%>");
		param.put("MTYPE", "<%=mtype%>");
		param.put("EBELN", EBELN);
		param.put("PROGID", "<%=menuId%>");
		param.put("LOTA14", BARCOD);
		
		gridList.gridList({
			id : "gridList",
			param : param
		});

	}
	
	function showMain(){
		//window.top.menuOpen();
		window.top.closePage();
	}
		 	
	function fn_onfocus(objId){
		$("#"+objId).select();
	}
	
	//초기화 버튼
	function clearData(){
		gridList.resetGrid("gridList");

	    $("#DOCDAT").val(year+"."+month+"."+date);	   //전기일자
	    $("#EBELN").val(""); // 오더번호
	    $("#BARCOD").val(""); // 바코드
	    $("#EBELNCOMBO").val("");
	    $("#BARCOD").focus();
	    
	}
	
	function eventKeydown(event,objId){
		if(event.keyCode == "13"){	
			
			if($("#EBELN").val() == ""){
				PlaySound('e');
				commonUtil.msgBox("VALID_M1569",null, null, function(){
									$('#EBELN').focus();
									$('#EBELN').select();
								 }); //오더번호를 입력해주세요
				return;
			}
			
			if($("#DOCDAT").val() == ""){
				PlaySound('e');
				commonUtil.msgBox("VALID_M1590"); //전기일자가 입력되지 않았습니다.
				return;
			}	
			
			var param = inputList.setRangeParam("searchArea");
			param.put("SEBELN", $("#EBELN").val());
			param.put("WAREKY", "<%=wareky%>");
			$("#EBELN").select();
			
			searchList();
		}
	}
	
	function comboEventDataBindeBefore(comboAtt){
		if(comboAtt == "WmsMobileInboundGoodsReceipt,PDGR01_TASOTY"){
			var param = new DataMap();
			param.put("PROGID", "<%=menuId%>");
			return param;
		}else if(comboAtt == "WmsMobileInboundGoodsReceipt,EBELNCOMBO"){
			var param = new DataMap();
			param.put("WAREKY", '<%= wareky%>');
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			param.put("PROGID", "<%=menuId%>");
			param.put("MTYPE", "<%=mtype%>");
			
			return param;
		}
	}
	
	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList"){
			//var list = gridList.getGridData("gridList");
			
			if(backYN==true){
				backYN = false;
				return;
			}
			
			var BARCOD = $("#BARCOD").val();
			if(BARCOD!=""){
				var rowNum = gridList.getSelectIndex(gridId);
				if(rowNum!=-1){
					detailMove(gridId, rowNum);
				}
			}
			
			var rowCnt = gridList.getGridDataCount(gridId);
			if(rowCnt>0){
				PlaySound('s');
				$("#BARCOD").focus();
			}else{
				PlaySound('e');
				$("#BARCOD").focus();
			}
		}
	}
	
	function searchEndEventFunction(){
		var ebeln = $.trim($("#EBELN").val());
		var barcod = $.trim($("#BARCOD").val());
		
		if(ebeln!=""&&barcod==""){
			$("#EBELN").focus();
			$('#EBELN').select();
		}else{
			$("#BARCOD").focus();	
			$("#BARCOD").select();
		}
		
	}
	
	function gridListEventRowClick(gridId, rowNum, colName){
		detailMove(gridId, rowNum);		
	}
	
	function detailMove(gridId, rowNum){
		
		var parMenuId = "<%=menuId%>";
		var SKUKEY = gridList.getColData("gridList", rowNum, "SKUKEY");
		var LOTA01 = gridList.getColData("gridList", rowNum, "LOTA01");
		var LOTA02 = gridList.getColData("gridList", rowNum, "LOTA02");
		var LOTA06 = gridList.getColData("gridList", rowNum, "LOTA06");
		var QTYRCV1 = gridList.getColData("gridList", rowNum, "QTYRCV1");
		var BWART = gridList.getColData("gridList", rowNum, "BWART");
		
		var PTNRKY = gridList.getColData("gridList", rowNum, "PTNRKY");
		var PTNRTY = gridList.getColData("gridList", rowNum, "PTNRTY");
		
		var EBELN = gridList.getColData("gridList", rowNum, "EBELN");
		var DOCDAT = $("#DOCDAT").val().replace(/\./gi, "");
		var BARCOD = $("#BARCOD").val();
		
		var paramStr = "SKUKEY="+SKUKEY+"&LOTA01="+LOTA01+"&LOTA02="+LOTA02+"&LOTA06="+LOTA06+"&EBELN="+EBELN+"&DOCDAT="+DOCDAT+"&PAR_MENUID="+parMenuId+"&QTYRCV1="+QTYRCV1+"&BARCOD="+BARCOD+"&BWART="+BWART+"&PTNRKY="+PTNRKY+"&PTNRTY="+PTNRTY;
		location.href="/mobile/PDGR01_POP.page?"+paramStr;
		
	}
	
	function dateChange(){
		var input = $("#DOCDAT").val().substr(5,2);
		if(month != input){
			PlaySound('e');
// 			commonUtil.msgBox("VALID_M0131"); // 전기일자는 당월이어야 합니다. VALID_M0131
// 			$("#DOCDAT").val(year + "." + month + "." + date);
		}
		
		searchList();
	}
	
	function reloadCombo(){
		$comboObj = $("#EBELNCOMBO");
		inputList.reloadCombo($comboObj, configData.INPUT_COMBO, "WmsMobileInboundGoodsReceipt,EBELNCOMBO");
		
		$("#EBELN").focus();
	}
	
	function detail(){
		var fRow = gridList.getFocusRowNum("gridList");
		var row = gridList.getRowData("gridList", fRow);

		var rowCnt = gridList.getGridDataCount("gridList");
		
		if(rowCnt < 1){
			//commonUtil.msgBox("VALID_M0006");
			PlaySound('e');
			return;
		}else{
			mobile.linkPopOpen('/mobile/PDGR01_POP.page',row);
		}
		
	}
	
	function fn_onfocus(objId){
		$("#"+objId).select();
	}
	
	function ebelnChange(){
		var EBELN = $("#EBELN").val();
		if(EBELN==""){
			clearData();
		}else{
			var param = inputList.setRangeParam("searchArea");
			param.put("EBELN", $("#EBELN").val());
			param.put("WAREKY", "<%=wareky%>");
			param.put("MTYPE", "<%=mtype%>");
			searchList(param);
		}
	}
	
	function searchEbelnList(){
		
		var param = new DataMap();
		
		param.put('WAREKY', '<%=wareky%>');
		param.put("MTYPE", "<%=mtype%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("PAR_PROGID", "<%=menuId%>");
		param.put("EBELN", $("#EBELN").val());
		
		mobile.linkPopOpen('/mobile/PDGR01_EBELN_POP.page', param);
	}
	
	function ebelnPopCallback(data) {
		
		var EBELN = data.get("EBELN");
		
		if(EBELN!=undefined && EBELN!=""){
			
			$("#EBELN").val(EBELN);
			$("#BARCOD").val("");
			
			var param = inputList.setRangeParam("searchArea");
			param.put("EBELN", $("#EBELN").val());
			param.put("WAREKY", "<%=wareky%>");
			param.put("MTYPE", "<%=mtype%>");
			searchList(param);
			
		}else{
			$("#EBELN").val("");
		}
		
	}
	
	function setDeadLinePop(){
		
		var param = new DataMap();
		
		param.put('WAREKY', '<%=wareky%>');
		param.put("MTYPE", "<%=mtype%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("PAR_PROGID", "<%=menuId%>");
		param.put("EBELN", $("#EBELN").val());
		
		mobile.linkPopOpen('/mobile/PDGR01_DEADLINE_POP.page', param);
		
	}
	
	function keyDownEvent(event){
		if(event.keyCode == "13"){
			//ebelnChange();
		}
	}
	
	function barcodFocus(){
		$("#BARCOD").focus();
		$("#BARCOD").select();
	}
	
	function setBarcod(){
 		
 		var ebeln = $.trim($("#EBELN").val());
 		var barcod = $.trim($("#BARCOD").val());
 		
 		if(barcod==""){
 			$("#BARCOD").val("");
 			PlaySound('e');
 			commonUtil.msgBox("VALID_M2050",null,null,function(){ $("#BARCOD").focus(); }); // 바코드를 입력하여주세요.
 			return;
 		}
 		
 		// 20221007 입고 완료된 바코드 여부  VALIDATION
		if(!validationCompleteBarcod(ebeln, barcod)){
			return;
		}
 		
		searchList();
 		
	}
	
	function validationCompleteBarcod(ebeln, barcod){
		// 20221007 입고 완료된 바코드 여부  VALIDATION
		/*
		var vParam = new DataMap();
		vParam.put("OWNRKY", "<//%=ownrky%>");
		vParam.put("BARCOD", barcod);
		
		var json = netUtil.sendData({
			url : "/mobile/mobileInboundGoodsReceipt/json/validationCompleteInBarcod.data",			
			param : vParam
		});
		if(json.data == "OK"){
			return true;
		}else if(json.data != "OK"){
			PlaySound('e');
			var msgList = json.data.split("†");
			var msgTxt = commonUtil.getMsg(msgList[0], (msgList.length > 1 ? msgList[1].split("/") : null));
			commonUtil.msgBox(msgTxt,null,null,function(){ 
				$("#BARCOD").focus();
				 $("#BARCOD").select(); 
			}); // 입고 완료 된 바코드[{0}] 입니다.
			 
			return false;
		}
		*/

        return true;
	}
	
	
</script>
</head>
<body>
	<div class="main_wrap">
		<div class="tem4_content">
			<div class="select_box" id="bindArea">
				<div class="searchArea" id="searchArea">
					<table>
						<colgroup>
							<col width="120px"  />
							<col />
							<col width="80px" />
						</colgroup>
						<tbody id="paramArea">
							
							<tr>
								<th CL="STD_DOCDAT" width="100">전기일자</th>
								<td colspan="2">
									<input type="text" class="text" inputmode="none" name="DOCDAT" id="DOCDAT" UIFormat="C" onchange="dateChange()" onkeydown="eventKeydown(event,this.id)" style="width:calc(100% - 45px);text-align:center;float:left;"/>
								</td>
							</tr>
							
<!-- 							<tr> -->
<!-- 								<th CL='STD_EBELN' width="100">오더번호</th> -->
<!-- 								<td colspan="2"> -->
<!-- 					                <div class="select-editable"> -->
<!-- 					                    <select style="border:none" Combo="WmsMobileInboundGoodsReceipt,EBELNCOMBO" onchange="this.nextElementSibling.value=this.value,searchList()" id="EBELNCOMBO"> -->
<!-- 					                    </select> -->
<!-- 					                    <input type="text" name="EBELN" id="EBELN" value="" style="text-align: center;"  onkeypress="commonUtil.enterKeyCheck(event, 'searchList()')" readonly /> -->
<!-- 						             </div> -->
<!-- 								</td> -->
<!-- 							</tr> -->
							
							<tr>
								<th CL='STD_EBELN' width="100">오더번호</th>
								<td>
									<input type="text" class="text" name="EBELN" id="EBELN" style="text-align: center;" onfocus="fn_onfocus(this.id)" onkeydown="keyDownEvent(event)" onChange="ebelnChange()" validate="required"/>
								</td>
								<td onclick ="searchEbelnList()">
									<button class="bt" style="height: 32px;">
										<p CL='BTN_FINDDATA'></p>
									</button>
								</td>
								
							</tr>
							
							<tr>
								<th  CL='STD_BARCOD'>바코드</th>
								<td colspan="2">
									<div class="select-editable">
<!-- 									<input type="text" class="text" name="BARCOD" id="BARCOD" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'searchList()')" style=" float:left;text-align: center;" /> -->
										<input type="text" name="BARCOD" id="BARCOD" value="" inputmode="none" style="text-align: center;" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'setBarcod()')" />
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			
			<div class="tableWrap_search section">
					<div class="tableHeader">
					<table style="width: 100%">
						<colgroup>
								<col width="30px" />
								<col width="90px" />
								<col width="150px" />
								<col width="300px" />
								
								<col width="81px" />
								<col width="105px" />
<!-- 								<col width="61px" /> -->
<!-- 								<col width="61px" /> -->
								
								<col width="61px" />
								<col width="91px" />
								<col width="100px" />
								<col width="100px" />
<!-- 								<col width="81px" /> -->
						</colgroup>
						<thead>
							<tr>
								<th CL="STD_NO">No.</th>
								<th CL='STD_RCPTTY'>입고유형</th>
								<th CL='STD_SKUKEY'>상품코드</th>
								<th CL='STD_DESC01'>상품명</th>
								
								<th CL='STD_QTYORD'>오더수량</th>
								<th CL='STD_QTSTOK1'>입고가능수량</th>
<!-- 								<th CL='STD_QTYRCV'>입고수량</th> -->
<!-- 								<th CL='STD_MENGE_B'>기입고수량</th> -->
								
								<th CL='STD_UOMKEY'>단위</th>
								<th CL='STD_LOTA06'>재고상태</th>
								<th CL='STD_LOTA02'>플랜트</th>
								<th CL='STD_LOTA01'>저장위치</th>
<!-- 								<th CL='STD_STATDONM'>문서상태명</th> -->
							</tr>
						</thead>
					</table>
				</div>
				
				<div class="tableBody">
<!-- 					<table style="width: 100%; overflow:hidden;word-wrap:break-word;"> -->
					<table style="width: 100%;">
						<colgroup>
								<col width="30px" />
								<col width="90px" />
								<col width="150px" />
								<col width="300px" />
								
								<col width="81px" />
								<col width="105px" />
<!-- 								<col width="61px" /> -->
<!-- 								<col width="61px" /> -->
								
								<col width="61px" />
								<col width="91px" />
								<col width="100px" />
								<col width="100px" />
<!-- 								<col width="81px" /> -->
						</colgroup>
						<tbody id="gridList">
							<tr CGRow="true">
								<td GCol="rownum">1</td>
								<td GCol="text,RCPTNM">입고유형</td>
								<td GCol="text,SKUKEY">상품코드</td>
								<td GCol="text,DESC01">상품명</td>
								
								<td GCol="text,MENGE" GF="N 20,3">오더수량</td>
								<td GCol="text,QTYRCV1" GF="N 20,3">입고가능수량</td>
<!-- 								<td GCol="text,QTYRCV" GF="N 20,3">입고수량</td> -->
<!-- 								<td GCol="text,MENGE_R" GF="N 20,3">기입고수량</td> -->
								
								<td GCol="text,MEINS">단위</td>
								<td GCol="text,LOTA06NM">재고상태</td>
								<td GCol="text,PLNTNM">플랜트</td>
								<td GCol="text,STLNM">저장위치</td>
<!-- 								<td GCol="text,STATDONM">문서상태명</td> -->
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			
			<!-- end table_body -->
			<div class="footer_5" style="position:fixed;">
<!-- 			<div class="footer_5" > -->
				<table>
					<tr>
						<td class=f_1 onclick ="showMain()" width="33.3%"><label CL='BTN_CLOSE'></label></td>
						<td class=f_1 onclick="clearData()" width="33.3%"><label CL='BTN_CLEAR'></label></td>
						<td class=f_1 onclick="setDeadLinePop()" width="33.3%"><label CL='STD_PTLDLE'></label></td>
					</tr>
				</table>
			</div><!-- end footer_5 -->	
		</div>
	</div>
</body>