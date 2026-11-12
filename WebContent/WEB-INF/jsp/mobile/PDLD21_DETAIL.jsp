<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%	
	String STKNUM = request.getParameter("STKNUM");
	String DPTNKY = request.getParameter("DPTNKY");
	String BARCOD = request.getParameter("BARCOD");	
	String TASKKY = request.getParameter("TASKKY");
	String SEARCHCOUNT = request.getParameter("SEARCHCOUNT");
	
	if(STKNUM == null){
		STKNUM = "";
	}
	
	if(DPTNKY == null){
		DPTNKY = "";
	}
	
	if(BARCOD == null){
		BARCOD = "";
	}
	
	if(TASKKY == null){
		TASKKY = "";
	}
	
	if(SEARCHCOUNT == null){
		SEARCHCOUNT = "";
	}
%>
<!doctype html>
<head>
<style type="text/css">
button{ vertical-align: bottom; height: 37px; }
input:read-only{ background-color: #e2e2e2; }
select:disabled{ background-color: #e2e2e2; }
.tem5_content .tableWrap_search  { overflow: hidden; }
.tem4_content .select_box table  { width  : 98% !important; margin: auto; }
.tem4_content .select_box .text  { padding: unset !important; margin: unset !important; }
.tem4_content table th           { padding: 3px !important; white-space: normal; }
.tem4_content table td           { padding: 3px !important; }
.number { text-align: right }

</style>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>Mobile WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			editable : false,
			module : "WmsMobileOutboundPicking",
			command : "PDLD21_DETAIL",
			gridMobileType : true,
			emptyMsgType:false
		});
				
		 
		if("<%=BARCOD%>" != "") {
			$("#BARCOD").val("<%=BARCOD%>");
			var param = inputList.setRangeParam("searchArea");
			param.put("LANGKY", "<%=langky%>");
			param.put("WAREKY", "<%=wareky%>");
			searchList(param);
		}
		
		$('#BARCOD').focus();
		searchList();
		
	});
	
	// 작업지시번호로 문서정보검색
	function searchList(){		

		var BARCOD = $("#BARCOD").val();
		
		var param = new DataMap();
		
		param.put("WAREKY", "<%=wareky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("LANGKY", "<%=langky%>");
<%-- 		param.put("PLNTKY", '<%=plntky%>'); --%>
		
		param.put("BARCOD", BARCOD);
		param.put("DPTNKY", '<%=DPTNKY%>');
		param.put("STKNUM", '<%=STKNUM%>');
		param.put("TASKKY", '<%=TASKKY%>');
		param.put("PROGID", "PK05_DETAIL");

		gridList.resetGrid("gridList");
		
		gridList.gridList({
			id : "gridList",
			param : param
		});		
	}
	
	function start(){
		var param = new DataMap();
		var head = gridList.getSelectData("gridList");					
		var totallen = gridList.getGridData("gridList").length;
		var checkedCount = gridList.getSelectRowNumList("gridList").length;
		

		var BARCOD = $("#BARCOD").val();
		var checkCnt=0;
		var messageAskCode = "SYSTEM_M0129";	//상차완료 하시겠습니까?
		var messageDoneCode = "SYSTEM_M0133";	//상차완료되었습니다.
		
		if(head.length < 1) {
			
			PlaySound('e');
			commonUtil.msgBox("MOBILE_M0039", null, null, function () { //체크박스 선택 후 상차완료를 진행해주세요.				
				$('#BARCOD').focus();   
			}); 
			
			return;
		}							
		
		// 부분상차
		if (gridList.getSelectRowNumList("gridList").length < totallen) {
			messageAskCode = "SYSTEM_M0137";
			messageDoneCode = "SYSTEM_M0138";
		}

		commonUtil.msgConfirm(messageAskCode,null, //상차완료 하시겠습니까?		
				function () {
									
					param.put("head", head);
					param.put("WAREKY", "<%=wareky%>");
					param.put("OWNRKY", "<%=ownrky%>");
					
					param.put("LANGKY", "<%=langky%>");		
					param.put("BARCOD", BARCOD);
					param.put("DPTNKY", '<%=DPTNKY%>');
					param.put("STKNUM", '<%=STKNUM%>');
					param.put("TASKKY", '<%=TASKKY%>');
					
					param.put("TOTALCNT", totallen);	
			<%-- 		param.put("PLNTKY", '<%=plntky%>'); --%>
					
					var json = netUtil.sendData({
						url: "/mobile/Mobile/json/PDLD21SaveEnd.data",
						param: param
					});
					
					if(json && json.data) {
						if(!json.data.CNT && json.data.SHPOKY){							
				    		PlaySound("e");					
							commonUtil.msgBox("OUT_M0195", json.data.SHPOKY, null, function () {
								$('#BARCOD').focus();   
							}); 
							
				    	}else{
				    		
				    		PlaySound("s");					
							commonUtil.msgBox(messageDoneCode, null, null, function () { //상차완료되었습니다.
							
								if(totallen == json.data.CNT){
									previtem("PASS");
								}else{
									searchList();
								}
							}); 							
				    	}
					}
					
				}, function () {
					return;
				}
			);
	}	
	
	function gridListEventDataBindEnd(gridId, dataLength){
		
		if(gridId == "gridList" && dataLength == 0){
			PlaySound('e');
			gridList.resetGrid("gridList");
			
			location.href='/mobile/PDLD21.page?STKNUM='+<%=STKNUM%>+'&DPTNKY='+<%=DPTNKY%>+'&PREVITEM=NO&TASKKY='+<%=TASKKY%>+"&COMPLETE_YN=Y";
		}else if(gridId == "gridList" && dataLength > 0){
									
			var param = new DataMap();
			
			param.put("WAREKY", '<%=wareky%>');						
			param.put("DPTNKY", '<%=DPTNKY%>');
			param.put("STKNUM", '<%=STKNUM%>');
			param.put("LANGKY", "<%=langky%>");
			param.put("TASKKY", '<%=TASKKY%>');
<%-- 			param.put('PLNTKY', '<%=plntky%>'); --%>
			 
			var json = netUtil.sendData({
				url : "/mobile/Mobile/json/PDLD21DETAILCNT.data",			
				param : param
			});
			
			if(json && json.data["CNT"] > 0){
				
				var CNT = Number(json.data["CNT"]);
				
				$("#COMPCNT").val(CNT);        //전체 건수
				$("#COMPCNT2").val(Number(json.data["COMPCNT2"]));   //현재까지 완료된 건수
				
				
				if(CNT == 0){
					resetGrid();
					
					PlaySound("e");					
					commonUtil.msgBox("MOBILE_M0001", null, null, function () { //작업지시번호가 존재하지 않습니다.
						$('#BARCOD').focus();            			
					}); 
				
				}else{
					PlaySound('s');
				}
				
				$('#BARCOD').focus();
			}				
			 
			for(var i=0; i < dataLength; i++){
	 			if(gridList.getColData(gridId, i, "FCKCOM") == "V"){
	 				gridList.setRowCheck(gridId, i, true);
	 				gridList.setRowReadOnly(gridId, i, true, ["rowCheck"]);
	 				gridList.setRowCss(gridId, i, {'color':'blue'});
	 			} 
	 		}
		}
	}
	
	
// 	 function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		
// 			if(gridId == "gridList" && colName =="FCKCOM"){
								
// 				if(colValue == "" || colValue == " "){
					
// 					gridList.setRowCheck(gridId, i, false);
// 				}else{
					
// 					gridList.setRowCheck(gridId, i, true);
// 				}
				
// 			}
			
// 	}
	 
	function gridListEventRowCheck(gridId, rowNum, checkType) {
		

		/* if( gridList.getRowState("gridList", rowNum) != "R"){
		
			gridList.setColValue(gridId, rowNum, "FCKCOM", checkType);
		
		}  */
	}
	 
	function gridListEventRowCheckAll(gridId, checkType) {
		
		for(var i = 0; i < gridList.getGridData(gridId).length; i++){
			
			if(gridList.getColData(gridId, i, "FCKCOM") == "V"){
				gridList.setRowCheck(gridId, i, true);	
			}
		}
		
	}
	
	 
	function showMain() {
		window.top.closePage();
	}
	
	function gridListEventRowClick(gridId, rowNum, colName){
		
		if(gridId == "gridList" && colName == "rowCheck"){
			
			var selectHeadRownum = gridList.getSelectRowNumList("gridList");
			
			
			$("#COMPCNT2").val(selectHeadRownum.length);
		}
		
		
		
		
	}
	
	function eventKeydown(event,objId){
		if(event.keyCode == "13"){
				var head = gridList.getGridData("gridList");
				var key = $.trim($("#BARCOD").val());
				var param = new DataMap();
				var taskkyList = new Array();
				
				if(key == ""){
					PlaySound("e");					
					commonUtil.msgBox("VALID_M0936", null, null, function () { //오더번호를 입력해주세요.
						$('#BARCOD').focus();    
						return;	
					}); 
										
				}
				
				
				var barcod = $.trim($("#BARCOD").val());
				var comCnt = $.trim($("#COMPCNT2").val());
				var diffCnt = 0;

				for(var i=0; i<head.length; i++){
					
					if(gridList.getColData("gridList", i, "LOTA14") == barcod){
						
							if(gridList.getSelectType("gridList",i) == true){
								diffCnt = 0
								break;
							}
							
							gridList.setRowCheck("gridList", i, true);
							PlaySound('s');
							diffCnt = 0;
							break;
					}else{
						diffCnt++;
					}
				}								
				
				if(diffCnt >= 1){
			
					PlaySound("e");					
					commonUtil.msgBox("MOBILE_M0047", null, null, function () { //조회된 데이터가 없습니다
						$('#BARCOD').focus();            			
					}); 
				}
				
				
				var selectHeadRownum = gridList.getSelectRowNumList("gridList");
				
		
				$("#COMPCNT2").val(selectHeadRownum.length);
				
				$('#BARCOD').focus();
				
		} 		
	}
	
	function previtem(goType) {
		
		var chk = 0;
		
		var headChk = gridList.getSelectRowNumList("gridList");
		var headChkLen = gridList.getSelectRowNumList("gridList").length;		
		
		for(i = 0; i < headChkLen; i++){
			
			var fckcom = gridList.getColData("gridList", headChk[i], "FCKCOM");   //Head상태
			
			if(fckcom != "V"){
				
				chk++;
			}
			
		}
		
		
		
		if(chk > 0 && !goType){

			commonUtil.msgConfirm("VALID_M2094",null,
					function () {
						location.href='/mobile/PDLD21.page?STKNUM='+'<%=STKNUM%>'+'&DPTNKY='+'<%=DPTNKY%>'+'&PREVITEM=NO&TASKKY='+<%=TASKKY%>+'&SEARCHCOUNT='+<%=SEARCHCOUNT%>;
					}, function () {
						$('#BARCOD').focus();   
						return;
						
					}
				);
			
			
		}else{
			
			location.href='/mobile/PDLD21.page?STKNUM='+'<%=STKNUM%>'+'&DPTNKY='+'<%=DPTNKY%>'+'&PREVITEM=NO&TASKKY='+<%=TASKKY%>+'&SEARCHCOUNT='+<%=SEARCHCOUNT%>;
		}
	}
	
</script>
</head>
<body>
	<div class="main_wrap">
		<div class="tem4_content">
			<div class="select_box" id="bindArea">
				<div id="searchArea">
					<table>
						<colgroup>
							<col width="80px" />
							<col width="*" />
						</colgroup>
						<tbody>
							<tr style="display:none">
								<th CL="STD_OWNRKY" width="100">화주</th>
								<td>
									<input type="hidden" id="selectSKU">
									<!-- <select Combo="WmsMaster_ComboBoxDatas,OWNER" name="OWNRKY" id="OWNRKY"">
									</select> -->
								</td>
							</tr>
							<tr>
								<th  CL='STD_BARCOD'>바코드</th>	
								<td colspan="3">
									<input type="text" class="text" name="BARCOD" id="BARCOD" onfocus="this.select();" onkeypress="eventKeydown(event,this.id)"/>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="tableWrap_search section" >
					<div class="tableHeader">						 
						<table style="width: 100%;">
							<colgroup>
								<col width="40px" />
								<col width="45px" />
								<col width="150px" />
								<col width="150px" />
								<col width="250px" />
								<col width="75px" />
								<col width="50px" />
								<col width="75px" />
								<col width="75px" />
								<col width="100px" />
								<col width="120px" />
								<col width="75px" />
								<col width="75px" />
								<col width="75px" />							
							</colgroup> 
							<thead>
								<tr>
									<th CL="STD_NO">NO</th>			
									<th GBtnCheck="true"></th>					
									<th CL="STD_LOTA14">제조LOT</th>
									<th CL="STD_SKUKEY">품목코드</th>
									<th CL="STD_DESC01">품목명</th>
									<th CL="STD_QTSHPC">상차수량</th>									
									<th CL="STD_UOM">단위</th>
									<th CL="STD_LOTA02">플랜트</th>
									<th CL="STD_LOTA01">저장위치</th>
									<th CL="STD_SUPLNM">공급업체코드</th>
									<th CL="STD_LOTA09">거래처코드MTO</th>
									<th CL="STD_LOTA11">제조일자</th>
									<th CL="STD_LOTA12">입고일자</th>
									<th CL="STD_LOTA13">유통기한</th>								
								</tr>
							</thead>
						</table>
					</div>
					<div class="tableBody">
						<table style="width: 100%;">
							<colgroup>
								<col width="40px" />
								<col width="45px" />
								<col width="150px" />
								<col width="150px" />
								<col width="250px" />
								<col width="75px" />
								<col width="50px" />
								<col width="75px" />
								<col width="75px" />
								<col width="100px" />
								<col width="120px" />
								<col width="75px" />
								<col width="75px" />
								<col width="75px" />										
								<col width="0px" />
								<col width="0px" />
								<col width="0px" />
								<col width="0px" />
								<col width="0px" />																		
							</colgroup>
							<tbody id="gridList">
								<tr CGRow="true">
										<td GCol="rownum">순번</td>
<!-- 										<td GCol="check,FCKCOM">상차완료</td> -->
										<td GCol="rowCheck" >체크박스</td>

										<td GCol="text,LOTA14">제조LOT</td>
										<td GCol="text,SKUKEY">품목코드</td>
										<td GCol="text,DESC01">품목명</td>
										<td GCol="text,QTCOMP" GF="N 20,2">상차수량</td>
										<td GCol="text,UOMKEY">단위</td>
										<td GCol="text,LOTA02">플랜트</td>
										<td GCol="text,LOTA01">저장위치</td>
										<td GCol="text,LOTA03">공급처코드</td>
										<td GCol="text,LOTA09">거래처코드MTO</td>																										
										<td GCol="text,LOTA11">제조일자</td>
										<td GCol="text,LOTA12">입고일자</td>
										<td GCol="text,LOTA13">유통기한</td>										
										<td GCol="text,SHPOKY" hidden="hidden">출하문서번호</td>
										<td GCol="text,SHPOIT" hidden="hidden">출하문서아이템번호</td>
										
										<td GCol="text,TASKKY" hidden="hidden">작업문서번호</td>
										<td GCol="text,TASKIT" hidden="hidden">작업문서아이템번호</td>
										<td GCol="rowCheck" hidden="hidden">체크박스</td>
										<td GCol="text,FCKCOM" hidden="hidden">상차완료</td>
										
								</tr>
							</tbody>
						</table>
					</div>
				</div>			
			<div class="select_box">
				<table>
					<colgroup>
						<col width="100px"/>
						<col width="50px"/>
						<col width="50px"/>
						<col width="50px"/>
						<col width="50px"/>
						<col width="50px"/>
						<col width="50px"/>
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
			<div class="footer_5" style="margin-top: 5px; position:fixed;" >
				<table>
					<tr>
						<td class=f_1 width="50%" onclick="previtem()">
							<label CL='BTN_PREVITEM'></label>
						</td>					
						<td class=f_1 width="50%" onclick="start()">
							<label CL='BTN_COMPLE'></label>
						</td>						
					</tr>
				</table>
			</div><!-- end footer_5 -->	
		</div>
	</div>
</body>