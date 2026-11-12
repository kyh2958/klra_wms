<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String BARCOD = request.getParameter("BARCOD");
	String TASKKY = request.getParameter("TASKKY");
	String FLAG = request.getParameter("FLAG");
	if(BARCOD == null){
		BARCOD = "";
	}
	if(TASKKY == null){
		TASKKY = "";
	}
	if(FLAG == null){
		FLAG = "";
	}
%>
<!doctype html>
<head>
<style type="text/css">
button {float: right; }
input:read-only {background-color: #e2e2e2;}
select:DISABLED {background-color: #e2e2e2;}
</style>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>Mobile WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	
	var col = "";
		
	$(document).ready(function(){
		
		gridList.setGrid({
			id : "gridList",
			editable : false,
			module : "WmsMobilePutaway",
			command : "PDPT01",
			gridMobileType : true,
			emptyMsgType:false
		});
		
		if("<%=TASKKY%>" != "") {
			$("#TASKKY").val("<%=TASKKY%>");
			if("<%=FLAG%>" != "") {
				$("#FLAG").val("<%=FLAG%>");
			}
			var param = inputList.setRangeParam("searchArea");
			param.put("TASKKY", $("#TASKKY").val());
			param.put("WAREKY", "<%=wareky%>");
			param.put("MTYPE", "<%=mtype%>");
			searchList(param);
		}
		
		$("#TR1").hide();
		if($("#TASKKY").val().length == 0){
			$("#TASKKY").focus();
		} else {
			$("#TASKKY").select();
		}
	});

	function searchList(param){
		
		var json = netUtil.sendData({
			url : "/mobile/MobilePutaway/json/PDPT01SKUCNT.data",
			param : param
		});
		
		if(json && json.data["CNT"] > 0){
			
			gridList.gridList({
				id : "gridList",
				param : param
			});
			
			var CNT = Number(json.data["CNT"]);
			var COM_CNT = Number(json.data["COM_CNT"]);
			var DIFF_CNT = CNT - COM_CNT;
			var SKUKEY = json.data["SKUKEY"];
			
			$("#COMPCNT").val(CNT);        //전체 건수
			$("#COMPCNT2").val(COM_CNT);   //현재까지 완료된 건수
			
			if((Number(CNT)-Number(COM_CNT)) == 1 && $("#FLAG").val() == ""){
				oneRowStart(SKUKEY);
			}
			if($("#FLAG").val() != ""){
				$("#FLAG").val("");
			}
			if(CNT == 0){
				resetGrid();
				PlaySound('e');
				commonUtil.msgBox("MOBILE_M0001",null,null,function(){ $("#TASKKY").focus(); }); // 작업지시번호가 존재하지 않습니다.
			}
			
		} else {
			resetGrid();
			PlaySound('e');
			commonUtil.msgBox("MOBILE_M0001",null,null,function(){ $("#TASKKY").focus(); }); // 작업지시번호가 존재하지 않습니다.
		}
		$("#TASKKY").select();
	}
	
	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList"){
			
			var rowCnt = gridList.getGridDataCount(gridId);
			if(rowCnt>0){
				PlaySound('s');
			}else{
				PlaySound('e');
			}
			
		}
	}
	
	function searchEndEventFunction(){
		$("#TASKKY").focus();
	}

	function keyDownEvent(event){
		if(event.keyCode == "13"){
			if(validate.check("searchArea")){
				var param = inputList.setRangeParam("searchArea");
				param.put("TASKKY", $("#TASKKY").val());
				param.put("WAREKY", "<%=wareky%>");
				param.put("MTYPE", "<%=mtype%>");
				searchList(param);
			}
		}
	}

	function linkPopCloseEvent(data){
		var VALUE_COL = data.get("VALUE_COL");
		var TEXT_COL = data.get("TEXT_COL");
		if(col=='TASKKY'){
			$("#TASKKY").val(VALUE_COL);
			$("#BARCOD").focus();
		}
	}

	//아이템별 적치(PDPT01_POP) 시작버튼
	function start(){
		if(validate.check("searchArea")){
			if(gridList.getGridDataCount("gridList") > 0) {
				var gridCnt = gridList.getGridDataCount("gridList");
				if(gridCnt > 0){
					var param = new DataMap();
					param.put("TASKKY", $("#TASKKY").val());
					param.put("WAREKY", "<%=wareky%>")
					param.put("MTYPE", "<%=mtype%>");
					
					var json = netUtil.sendData({
						url : "/mobile/MobilePutaway/json/PDPT01.data",
						param : param
					});
					
					if(json && json.data) {
						var list = json.data;
						
						var rowNum = gridList.getFocusRowNum("gridList")
						var SKUKEY = gridList.getColData("gridList", rowNum, "SKUKEY");
						var paramStr = "&SKUKEY="+SKUKEY+"&TASKKY"+"="+$("#TASKKY").val();
						
						location.href="/mobile/PDPT01_POP.page?"+paramStr;
					} 
				}
			} else {
				PlaySound('e');
				commonUtil.msgBox("COMMON_M0048");   //작업 대상이 선택되지 않았습니다.
			}
		}
	}

	function showMain() {
		//window.top.menuOpen();
		window.top.closePage();
	}

	//ROW가 1줄일 때 POP화면으로 넘어가기
	function oneRowStart(SKUKEY){
		
		var paramStr = "&SKUKEY="+SKUKEY+"&TASKKY"+"="+$("#TASKKY").val();
		location.href="/mobile/PDPT01_POP.page?"+paramStr;
		
		return;
		if(validate.check("searchArea")){
			
			var param = new DataMap();
			param.put("TASKKY", $("#TASKKY").val());
			param.put("WAREKY", "<%=wareky%>")
			param.put("MTYPE", "<%=mtype%>");
			var json = netUtil.sendData({
				url : "/mobile/MobilePutaway/json/PDPT01.data",
				param : param
			});
			
			if(json && json.data) {
				//var list = json.data;
				var paramStr = "&SKUKEY="+SKUKEY+"&TASKKY"+"="+$("#TASKKY").val();
				location.href="/mobile/PDPT01_POP.page?"+paramStr;
			}
		}
	}

	function resetGrid(){
		$("#COMPCNT").val("");
		$("#COMPCNT2").val("");
		$("#QTCOMP_L7141").val("");
		$("#QTCOMP_L7141_2").val("");
		$("#UOMKEY2").val("");
		$("#UOMKEY3").val("");
		gridList.resetGrid("gridList");
	}

	function fn_onfocus(objId){
		$("#"+objId).select();
	}

	function gridListEventRowClick(gridId, rowNum, colName){
		start();
	}
	
	//초기화 버튼
	function clearData(){
		gridList.resetGrid("gridList");
	    $("#TASKKY").val("");
	    
	    //searchList();
	}
	
	function taskkyChange(){
		var TASKKY = $("#TASKKY").val();
		if(TASKKY==""){
			clearData();
		}else{
			
			var param = inputList.setRangeParam("searchArea");
			param.put("TASKKY", $("#TASKKY").val());
			param.put("WAREKY", "<%=wareky%>");
			param.put("MTYPE", "<%=mtype%>");
			searchList(param);
		}
	}
	
	function searchTaskkyList(){
		
		var param = new DataMap();
		
		param.put('WAREKY', '<%=wareky%>');
		param.put("MTYPE", "<%=mtype%>");
		param.put("OWNRKY", "<%=ownrky%>");
		
		mobile.linkPopOpen('/mobile/PDPT01_DOC_POP.page', param);
	}
	
	function TaskkyPopCallback(data) {
		
		var TASKKY = data.get("TASKKY");
		
		if(TASKKY!=undefined && TASKKY!=""){
			
			$("#TASKKY").val(TASKKY);
			
			var param = inputList.setRangeParam("searchArea");
			param.put("TASKKY", $("#TASKKY").val());
			param.put("WAREKY", "<%=wareky%>");
			param.put("MTYPE", "<%=mtype%>");
			searchList(param);
			
		}else{
			$("#TASKKY").val("");
		}
		
	}
	
	function barcodFocus(){
		$("#TASKKY").focus();
		$("#TASKKY").select();
	}
	
	
</script>
</head>
<body>
	<div class="main_wrap">
		<div class="tem4_content">
			<div class="select_box" id="bindArea">
				<div id="searchArea">
					<div style="display:none;"><input type="text" class="text" id="FLAG" name="FLAG"/></div>
					<table>
						<colgroup>
							<col width="120px" />
							<col />
							<col width="80px" />
							<col />
						</colgroup>
						<tbody>
							<tr>
								<th CL='STD_TASKKY'>작업지시번호</th>
								<td >
									<input type="text" class="text" name="TASKKY" id="TASKKY" style="text-align: center;" onfocus="fn_onfocus(this.id)" onkeydown="keyDownEvent(event)" onChange="taskkyChange()" validate="required"/>
								</td>
								<td onclick ="searchTaskkyList()">
									<button class="bt" style="height: 32px;">
										<p CL='BTN_FINDDATA'></p>
									</button>
								</td>
							</tr>
							<tr id="TR1" style="display: none;">
								<td colspan="4">
									<input type="text" class="text" id="FAIL_TEXT" style="color:white;text-align:center;background-color:red;font-size: 20px;font-weight: bold;"/>
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
							<col width="150px" />
							<col width="300px" />
							<col width="80px" />
						</colgroup>
						<thead>
							<tr>
								<th CL='STD_NO'>순번</th>
								<th CL='STD_SKUKEY'>품목코드</th>
								<th CL='STD_DESC01'>품명</th>
								<th CL='STD_STATITNM'>완료여부</th>
							</tr>
						</thead>
					</table>
				</div>
				
				<div class="tableBody">
<!-- 					<table style="width: 100%; overflow:hidden;word-wrap:break-word;"> -->
					<table style="width: 100%;">
						<colgroup>
							<col width="30px" />
							<col width="150px" />
							<col width="300px" />
							<col width="80px" />
						</colgroup>
						<tbody id="gridList">
							<tr CGRow="true">
								<td GCol="rownum">순번</td>
								<td GCol="text,SKUKEY">품목코드</td>
								<td GCol="text,DESC01">품명</td>
								<td GCol="text,STATITNM">완료여부</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			
			<div class="select_box">
				<table>
					<colgroup>
						<col width="80px"/>
						<col width="50px"/>
						<col width="50px"/>
						<col width="60px"/>
						<col width="50px"/>
						<col width="50px"/>
						<col width="60px"/>
					</colgroup>
					<tbody>
						<tr>
							<th  CL='STD_COMPCNT'>완료건수</th>
							<td CL='STD_ALL'>전체</td>
							<td>
								<input type="text" class="text gridColInputNumber" name="COMPCNT" id="COMPCNT" readonly/>
							</td>
							<td CL='STD_CNT'>count</td>
							<td CL='STD_INCLU'>중</td>
							<td>
								<input type="text" class="text gridColInputNumber" name="COMPCNT2" id="COMPCNT2" readonly/>
							</td>
							<td CL='STD_CNT'>count</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<!-- end table_body -->
			<div class="footer_5" style="position:fixed;">
				
				<table>
					<tr>
						<td class=f_1 onclick ="showMain()" width="50%"><label CL='BTN_CLOSE'></label></td>
<!-- 						<td class=f_1 onclick ="start()" width="50%"><label CL='BTN_IFSTAT'></label></td> -->
						<td  class=f_1 onclick="clearData()" width="50%"><label CL='BTN_CLEAR'></label></td>
					</tr>
				</table>
			</div><!-- end footer_5 -->	
		</div>
	</div>
</body>