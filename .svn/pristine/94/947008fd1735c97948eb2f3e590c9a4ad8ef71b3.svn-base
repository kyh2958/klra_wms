<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	
	String barcod = request.getParameter("BARCOD");
	if(barcod==null)barcod="";
	
	String taskky = request.getParameter("TASKKY");
	if(taskky==null)taskky="";
	
	String skukey = request.getParameter("SKUKEY");
	if(skukey==null)skukey="";
	
%>
<!doctype html>
<head>
<style type="text/css">
button {float: right; }
input:read-only {background-color: #e2e2e2;}
select:DISABLED {background-color: #e2e2e2;}
.select_box table td {padding-right:5px;}

/* .tableBody{height: 244px !important;} */

</style>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>Mobile WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>

	var curIdx      = -1;
	var selBarcod   = "";
	var selBarcodIdx = -1;
	var curBarcod   = "";
	
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			editable : false,
			module : "WmsMobilePutaway",
			command : "PDPT01_POP",
			gridMobileType : true,
			emptyMsgType:false
		});
		setDetailInfo();
		
		searchList();
		
		$("#BARCOD").focus();
	});

	function searchList(){
		
		var param = new DataMap();
		param.put("TASKKY", "<%=taskky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("WAREKY", "<%=wareky%>");
		param.put("SKUKEY", "<%=skukey%>");
		param.put("MTYPE", "<%=mtype%>");
		
		gridList.gridList({
			id : "gridList",
			param : param
		});
		
	}

	//그리드 data binding 이후 발생 이벤트
	function gridListEventDataBindEnd(gridId, dataCount){
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
		$("#BARCOD").focus();
	}
	
	function gridListEventRowClick(gridId, rowNum, colName){
		if(gridId == "gridList"){
			//setRowDetail(rowNum);
		}
	}

	//닫기 버튼 클릭시 기존 조회 정보를 가지고 이전 화면으로 이동 후 조회
	function screenClose(){
		
		var modifyCnt = gridList.getModifyRowCount("gridList"); 		
 		var checkCnt = gridList.getSelectData("gridList").length;
 		
 		if(modifyCnt!=0 || checkCnt!=0){
 			
 			// 작업중인 내역이 존재합니다. 이전으로 돌아가시겠습니까?
 			commonUtil.msgConfirm("VALID_M2094", null
		            			, function(){
						 				var paramStr = "TASKKY="+<%=taskky%>;
						 				location.href="/mobile/PDPT01.page?"+paramStr+"&FLAG=N";
 					  			  }
					 			, function(){
									  $("#BARCOD").focus();
								  }
 							
 			);
 			
 			/*
 			if(!commonUtil.msgConfirm("VALID_M2094")){ // 작업중인 내역이 존재합니다. 이전으로 돌아가시겠습니까?
				return;
			}
 			*/
 		}else{
 			var paramStr = "TASKKY="+<%=taskky%>;
 			location.href="/mobile/PDPT01.page?"+paramStr+"&FLAG=N";	
 		}
 		
		
	}

	//적치완료
	function saveData(){
		
		var QTCOMP_L7141_1 = $("#QTCOMP_L7141_1").val(); // 전체 오더 수량
		var QTCOMP_L7141_2 = $("#QTCOMP_L7141_2").val(); // 전체 적치수량
		/*
		if(Number(QTCOMP_L7141_1) < Number(QTCOMP_L7141_2)){
			commonUtil.msgBox("TASK_M0062",["<%=taskky%>",QTCOMP_L7141_2,QTCOMP_L7141_1]);	//작업아이템번호의 완료량이 지시량이 많습니다.
			return;
		}
		*/
		
		if(Number(QTCOMP_L7141_2) <= 0){
			PlaySound('e');
			commonUtil.msgBox("TASK_M0057");	//완료수량은 0보다 커야합니다.
			return;
		}
		
		var gridId = "gridList";
 		var list = gridList.getGridData(gridId);
 		var paramlist = new DataMap();
		
		for(var i=0; i < list.length; i++) {
			
			var rowData = list[i];
			var rowNum = rowData.get("GRowNum");
			var rowState = gridList.getRowState(gridId, rowNum);
			
			var QTCOMP = gridList.getColData(gridId, rowNum, "QTCOMP");
			var QTTAOR = gridList.getColData(gridId, rowNum, "QTTAOR");
			var LOCAAC = gridList.getColData(gridId, rowNum, "LOCAAC");
			var QTSAVB = gridList.getColData(gridId, rowNum, "QTSAVB");
			
			if(rowState==configData.GRID_ROW_STATE_INSERT && QTCOMP!=""&&QTCOMP!="0"){
				if(LOCAAC=="" || LOCAAC=="RCVLOC"){
					PlaySound('e');
					commonUtil.msgBox("HHT_T0032",null,null,function(){ gridList.setColFocus("gridList", rowNum, "LOCAAC"); });   //지번을 입력하세요.
					return;
				}
			}
			
			if( Number(QTCOMP) > Number(QTSAVB) ){
				PlaySound('e');
				commonUtil.msgBox("IN_M0078",null,null,function(){ gridList.setColFocus("gridList", rowNum, "QTCOMP"); });   //입고수량은 입고가능수량보다 클 수 없습니다.
				return;
			}

		}
		
		for(var i=0; i < list.length; i++) {
			var rowData = list[i];
			var rowNum = rowData.get("GRowNum");
			var LOCATG = gridList.getColData(gridId, rowNum, "LOCATG");
			var LOCAAC = gridList.getColData(gridId, rowNum, "LOCAAC");
			
			if($.trim(LOCAAC)==""){
				gridList.setColValue("gridList", rowNum, "LOCAAC", LOCATG);
			}
			
			if(rowNum==curIdx){
				$("#LOCAAC").val(LOCATG);
			}
			
		}
		
		list = gridList.getGridData(gridId);
		
		// 지번 밸리데이션
		var vParam = new DataMap();
		
		vParam.put("head", list);
		vParam.put("list", list);
		vParam.put("headTaskky","<%=taskky%>");
		var json = netUtil.sendData({
			url : "/wms/task/json/PTLocacValidation.data",			
			param : vParam
		});
		if(json.data != "OK"){
			PlaySound('e');
			var msgList = json.data.split("†");
			var msgTxt = commonUtil.getMsg(msgList[0], (msgList.length > 1 ? msgList[1].split("/") : null));
			commonUtil.msg(msgTxt);										//작업아이템번호에 입력된 지번은 등록되지 않았습니다.
			return;
		}
		
		/*
		//저장하시겠습니까?
		if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
			return;
		}
		*/
		
		// 저장하시겠습니까?
		commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM, null
				            , function(){
								
									//console.log("list : " + JSON.stringify(list));
									var param = new DataMap();
									param.put("list",list);
									
									param.put("TASKKY","<%=taskky%>");
									param.put("SKUKEY","<%=skukey%>");
									param.put("MTYPE", "<%=mtype%>");
									param.put("WAREKY", "<%=wareky%>");
									param.put("PROGID", "PDPT01");
									
									var json = netUtil.sendData({
										url:"/mobile/MobilePutaway/json/savePDPT01POP.data",
										param : param
									});
									
									//console.log("json : " + JSON.stringify(json));
									
									if(json && json.data){
										
										var resultMsg = json.data.resultMsg;
										if(resultMsg=="SUCCESS"){
											PlaySound('s');
											commonUtil.msgBox("TASK_M0339");   //적치완료되었습니다.
											
											setDetailInfo();
											
											var stPcnt = json.data.stPcnt;
											if(stPcnt==""||stPcnt=="0"){
												gridList.resetGrid("gridList");
												screenClose();
											}else{
												searchList();			
												$("#BARCOD").focus();
											}
											
										}else{
											PlaySound('e');
											alert(resultMsg);
										}
									}
							  }
							  , function(){
									$("#BARCOD").focus();
								}
		);		
		
		
	}
	
	function fn_onfocus(objId){
		$("#"+objId).select();
	}
	
	function setDetailInfo(){
		
		var param = new DataMap();
		param.put("WAREKY", "<%=wareky%>");
		param.put("TASKKY", "<%=taskky%>");
		param.put("SKUKEY", "<%=skukey%>");
		param.put("MTYPE", "<%=mtype%>");
		
		var json = netUtil.sendData({
			url : "/mobile/MobilePutaway/json/PDPT01CNT.data",
			param : param
		});
		
		if(json && json.data){
			
			var CNT = json.data["CNT"];
			var QTTAOR = json.data["QTTAOR"];
			var QTCOMP = json.data["QTCOMP"];
			var UOMKEY = json.data["UOMKEY"];			
			var DESC01 = json.data["DESC01"];
		
			//$("#QTTAOR").val(QTTAOR);
			//$("#QTCOMP").val(QTCOMP);
			//$("#UOMKEY").val(UOMKEY);
			
			$("#DESC01").val(DESC01);
			$("#QTCOMP_L7141_1").val(QTTAOR);
			$("#UOMKEY2").val(UOMKEY);
			$("#QTCOMP_L7141_2").val(QTCOMP);
			$("#UOMKEY3").val(UOMKEY);
		}
	}
	
	function setRowDetail(rowNum){
		
		curIdx = rowNum;
		
		var LOCATG = gridList.getColData("gridList", rowNum, "LOCATG"); // 지시로케이션
		var LOCAAC = gridList.getColData("gridList", rowNum, "LOCAAC"); // to로케이션
		var QTCOMP = gridList.getColData("gridList", rowNum, "QTCOMP"); // 적치수량
		var QTTAOR = gridList.getColData("gridList", rowNum, "QTTAOR"); // 지시수량
		var QTSAVB = gridList.getColData("gridList", rowNum, "QTSAVB"); // 입력가능수량
		var SUOMKY = gridList.getColData("gridList", rowNum, "SUOMKY"); // 단위
		var LOTA14 = gridList.getColData("gridList", rowNum, "LOTA14"); // 개별바코드
		var SKUKEY = gridList.getColData("gridList", rowNum, "SKUKEY"); // 품목코드
		
		if(QTCOMP=="" || Number(QTCOMP)==0){
			//gridList.setColValue("gridList", rowNum, "LOCAAC", LOCATG);
			gridList.setColValue("gridList", rowNum, "QTCOMP", QTTAOR);
			$("#QTCOMP").val(QTTAOR);
			
			var rsMap = getQtyuom(SKUKEY, SUOMKY, QTTAOR);
			var BOX = rsMap.get("BOX");
			gridList.setColValue("gridList", rowNum, "BOX", BOX);
			
		}else{
			$("#QTCOMP").val(QTCOMP);
		}
		//alert("QTTAOR : " + QTTAOR);
		$("#QTTAOR").val($.trim(QTSAVB));		
		$("#UOMKEY").val($.trim(SUOMKY));
		$("#LOCATG").val($.trim(LOCATG));
		$("#LOCAAC").val($.trim(LOCAAC));
		$("#BOX").val($.trim(BOX));
		
		$("#QTTAOR").trigger("blur");
		$("#QTCOMP").trigger("blur");
		$("#BARCOD").trigger("blur");
		$("#QTCOMP").trigger("blur");
		
		$("#QTCOMP").attr("readonly", false);
		$("#LOCAAC").attr("readonly", false);
		
		$("#BARCOD").val(LOTA14);
		$("#BARCOD").focus();
		
		//gridList.setColFocus("gridList", rowNum, "QTCOMP");
		
	}
	
	function ChangeValue(obj){

		var id = $(obj).attr("id");
		var value = $(obj).val();
		
		if(curIdx==-1){
			$("#QTTAOR").val("");
			$("#QTCOMP").val("");
			$("#UOMKEY").val("");
			$("#LOCATG").val("");
			$("#LOCAAC").val("");
			$("#LOCATG").val("");
			commonUtil.msgBox("VALID_M0006");//선택된 데이터가 없습니다.
		}else if(curIdx!=-1 && id=="QTCOMP" ){
			
			gridList.setColValue("gridList", curIdx, "QTCOMP", value);
			
	 		var totQTCOMP = 0;
	 		var gridId = "gridList";
	 		var list = gridList.getGridData(gridId);
			
	 		for(var i=0; i < list.length; i++) {
				var QTCOMP = gridList.getColData(gridId, i, "QTCOMP");
				//alert(i + "\ntotQTCOMP : " + totQTCOMP + "\nQTCOMP : " + QTCOMP);
				totQTCOMP += Number(QTCOMP);
			}
	 		
			$("#QTCOMP_L7141_2").val(totQTCOMP);
			$("#QTCOMP_L7141_2").select();
			$("#QTCOMP").select();
			
		}else if(curIdx!=-1 && id=="LOCAAC" ){
			
			var LOCAKY = value;
			
			var param = new DataMap();
			param.put("WAREKY", "<%=wareky%>");
			param.put("ALOCAKY",LOCAKY);
			
			var json = netUtil.sendData({
				module : "WmsTask",
				command : "LOCAKYval2",
				sendType : "map",
				param : param
			});
			
			if (json.data){
				var status = json.data["STATUS"];
				var stlky = json.data["STLKY"];
				var plntky = json.data["PLNTKY"];
			
				if( status!="00" ){
					PlaySound('e');
					commonUtil.msgBox("TASK_M0011", value);	//* 이동대상 로케이션 {0} 이 유효하지 않습니다.
					gridList.setColValue("gridList", curIdx, "LOCAAC", "");
					$(obj).val("");
					$(obj).focus();
					return false;
				}
				
				// 20220817 지번 변경시 변경 된 모든 데이터 To지번 일괄 적용 요청		
				var gridId = "gridList";
				var list = gridList.getGridData(gridId);
				for(var i=0; i < list.length; i++) {
					var rowData = list[i];
					var rowNum = rowData.get("GRowNum");
					var lota02 = rowData.get("LOTA02");
					var locatg = rowData.get("LOCATG");
					
					// 20220921 P200과 P300 묶어서 처리 되게 동시에 보이게 설정 요청.
					if(lota02=="P200"||lota02=="P300"){
						if( ( plntky!="P200" && plntky!="P300" ) && $.trim(plntky)!=""){
							PlaySound('e');
							gridList.setColValue("gridList", curIdx, "LOCAAC", "");
							$(obj).val("");
							commonUtil.msgBox("TASK_M0701",null,null,function(){ $(obj).focus(); });	// 동일한 플랜트 내 지번만 변경 가능합니다.
							return false;
						}
					}else{
						if(lota02!=plntky && $.trim(plntky)!=""){
							PlaySound('e');
							gridList.setColValue("gridList", curIdx, "LOCAAC", "");
							$(obj).val("");
							commonUtil.msgBox("TASK_M0701",null,null,function(){ $(obj).focus(); });	// 동일한 플랜트 내 지번만 변경 가능합니다.
							return false;
						}
					}
					
				}
				
				for(var i=0; i < list.length; i++) {
					var rowData = list[i];
					var rowNum = rowData.get("GRowNum");
					var lota02 = rowData.get("LOTA02");
					var locatg = rowData.get("LOCATG");
					
					gridList.setColValue("gridList", rowNum, "LOCAAC", LOCAKY);
					gridList.setColValue("gridList", rowNum, "STLKY",  stlky);
					gridList.setColValue("gridList", rowNum, "PLNTKY", plntky);
				}
				
			}else{
				PlaySound('e');
				//gridList.setColValue("gridList", curIdx, "LOCAAC", "");
				$(obj).val("");
				commonUtil.msgBox("TASK_M0011", value,null,function(){ $(obj).focus(); });	//* 이동대상 로케이션 {0} 이 유효하지 않습니다.
				
				return false;
			}

			$(obj).focus();
		}
		
	}
	
	function searchLocationList(){
		
		if(curIdx==-1){
			PlaySound('e');
			commonUtil.msgBox("VALID_M0006");//선택된 데이터가 없습니다.
			return;
		}
		
		var lota02 = gridList.getColData("gridList", curIdx, "LOTA02");
		var lota02Sql = "'"+lota02+"'";
		
		// 20220921 P200과 P300 묶어서 처리 되게 동시에 보이게 설정 요청.
		if(lota02=="P200"||lota02=="P300"){
			lota02Sql = "'P200','P300'";
		}
		
		var param = new DataMap();
		
		param.put('WAREKY', '<%=wareky%>');
		param.put("MTYPE", "<%=mtype%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("INPLNTKY", lota02Sql);
		
		var json = netUtil.sendData({
			module : "WmsMobilePutaway",
			command : "PDPT01_POP_LOC",
			param : param,
			successFunction : "playSuccessSound"
		});
		
		if(json){
			param.put("json", json);
			mobile.linkPopOpen('/mobile/PDLOC_POP.page', param);
		}
		
	}
	
	function LocPopCallback(data) {
		
		var LOCAKY = data.get('LOCAKY');
		
		var lota02 = gridList.getColData("gridList", curIdx, "LOTA02");
		
		var param = new DataMap();
		param.put("WAREKY", "<%=wareky%>");
		param.put("ALOCAKY",LOCAKY);
		
		var json = netUtil.sendData({
			module : "WmsTask",
			command : "LOCAKYval2",
			sendType : "map",
			param : param
		});
		
		var cdelay = 300;
		var ctimer = null;
		clearTimeout(ctimer);
		ctimer = setTimeout(function(){
		
			if (json.data){
				var status = $.trim(json.data["STATUS"]);
				var stlky  = $.trim(json.data["STLKY"]);
				var plntky = $.trim(json.data["PLNTKY"]);
			
				if( status!="00" ){
					PlaySound('e');
					commonUtil.msgBox("TASK_M0011", colValue);	//* 이동대상 로케이션 {0} 이 유효하지 않습니다.
					return false;
				}
				
				// 20220921 P200과 P300 묶어서 처리 되게 동시에 보이게 설정 요청.
				if(lota02=="P200"||lota02=="P300"){
					if( ( plntky!="P200" && plntky!="P300" ) && $.trim(plntky)!=""){
						PlaySound('e');
						commonUtil.msgBox("TASK_M0701");	// 동일한 플랜트 내 지번만 변경 가능합니다.
						return false;
					}
				}else{
					if(lota02!=plntky && $.trim(plntky)!=""){
						PlaySound('e');
						commonUtil.msgBox("TASK_M0701");	// 동일한 플랜트 내 지번만 변경 가능합니다.
						return false;
					}
				}
				
				/*
				if(lota02!=plntky){
					PlaySound('e');
					commonUtil.msgBox("TASK_M0701");	// 동일한 플랜트 내 지번만 변경 가능합니다.
					return false;
				}
				*/
				
			}else{
				PlaySound('e');
				commonUtil.msgBox("TASK_M0011", colValue);	//* 이동대상 로케이션 {0} 이 유효하지 않습니다.
				return false;
			}
			
			// 20220817 지번 변경시 변경 된 모든 데이터 To지번 일괄 적용 요청		
			var gridId = "gridList";
			var list = gridList.getGridData(gridId);
			for(var i=0; i < list.length; i++) {
				var rowData = list[i];
				var rowNum = rowData.get("GRowNum");
				//var rowState = gridList.getRowState(gridId, rowNum);
				//if(rowState==configData.GRID_ROW_STATE_INSERT || rowState==configData.GRID_ROW_STATE_UPDATE){
					gridList.setColValue("gridList", rowNum, "LOCAAC", LOCAKY);
					gridList.setColValue("gridList", rowNum, "STLKY", stlky);
					gridList.setColValue("gridList", rowNum, "PLNTKY", plntky);
				//}
			}
			
			$('#LOCAAC').val(LOCAKY);
			ChangeValue($('#LOCAAC'));
			PlaySound('s');
		}, cdelay);
	}
	
	function setBarcod(){
 		
 		var barcod = $("#BARCOD").val().trim();
 		
 		if(barcod==""){
 			$("#BARCOD").val("");
 			PlaySound('e');
 			commonUtil.msgBox("VALID_M2050",null,null,function(){ $("#BARCOD").focus(); }); // 바코드를 입력하여주세요.
 			return;
 		}
 		
 		$("#BARCOD").val(barcod);
 		
 		var gridId = "gridList";
 		
 		var list = gridList.getGridData(gridId);
 		
 		var totQTCOMP   = 0;     // 전체 적치 수량
 		var duplRow     = -1;    // 바코드 검색 로우
 		var lastDuplRow = -1;    // 바코드 검색 마지막 중복 로우
 		var duplYN      = false; // 한번에 한나만 선택 되도록 구분자 둠. 
 		var stkyYN      = false; // 재고 데이터 추가여부
 		
		for(var i=0; i < list.length; i++) {
			
			var rowData = list[i];
			var rowNum = rowData.get("GRowNum");
			var rowState = gridList.getRowState(gridId, rowNum);
			var lota14 = rowData.get("LOTA14");
			var qttaor = gridList.getColData(gridId, rowNum, "QTTAOR"); // 지시수량
			var qtcomp = gridList.getColData(gridId, rowNum, "QTCOMP"); // 적치수량
			var qtsavb = gridList.getColData(gridId, rowNum, "QTSAVB"); // 입력가능
			
			if(barcod==lota14){
				//console.log("existsYN : " + existsYN + " - qttaor : " + Number(qttaor) + " - qtcomp : " + Number(qtcomp));
				
				//console.log("duplRow : " + duplRow + " - curIdx : " + curIdx + " - rowNum : " + rowNum + " - duplYN : " + duplYN + " - curBarcod : " + curBarcod + " - barcod : " + barcod);
				
				//if( (duplRow==-1 && Number(qtsavb)!=Number(qtcomp)) || ( duplRow!=-1 && Number(qtsavb)!=Number(qtcomp) && duplYN==false ) ){
				if( (duplRow==-1 && ((curIdx<rowNum && curBarcod==barcod)||curBarcod!=barcod) ) || ( duplRow!=-1 && ((curIdx<rowNum && curBarcod==barcod)||curBarcod!=barcod) && duplYN==false ) ){
					duplRow = rowNum;
					selBarcodIdx = rowNum;  // 검색 실패시 이전 선택을 위해 idx 저장
					selBarcod = barcod;
					setRowDetail(duplRow);
					gridList.setColFocus(gridId, duplRow, "QTCOMP");
					curBarcod = barcod;
					duplYN = true;
				}
				
				lastDuplRow = rowNum;
				
				if(rowState==configData.GRID_ROW_STATE_INSERT){
					stkyYN = true;
				}
			}
			
			qtcomp = gridList.getColData(gridId, rowNum, "QTCOMP"); // 적치수량
			totQTCOMP += Number(qtcomp); // 적치수량 합계 계산
			
		}
		//console.log("1 duplYN : " + duplYN + " - stkyYN : " + stkyYN + " - lastDuplRow : " + lastDuplRow);
		if(duplYN==false && stkyYN==false){
	 		
			var param = new DataMap();
			
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("WAREKY", "<%=wareky%>");
			param.put("SKUKEY", "<%=skukey%>");
			param.put("MTYPE", "<%=mtype%>");
			param.put("LOTA14", barcod);
			
			var json = netUtil.sendData({
				module : "WmsMobilePutaway",
				command : "PDPT01_POP_NEW",
				sendType : "map",
				param : param
			});
			
			var QTTAOR = "";
			var QTSAVB = "";			
			var SUOMKY = "";
			var LOTA01 = ""; 
			var LOTA02 = ""; 
			var LOTA03 = ""; 
			var LOTA09 = ""; 
			var LOTA11 = ""; 
			var LOTA12 = ""; 
			var LOTA13 = ""; 
			var LOCATG = ""; 
			var LOCAAC = "";
			var LOTA01NM = "";
			var LOTA02NM = "";
			var STOKKY = "";
			
			if(json && json.data){
				
				LOTA01 = json.data["LOTA01"];
				LOTA02 = json.data["LOTA02"];
				LOTA03 = json.data["LOTA03"];
				LOTA09 = json.data["LOTA09"];
				LOTA11 = json.data["LOTA11"];
				LOTA12 = json.data["LOTA12"];
				LOTA13 = json.data["LOTA13"];
				LOCATG = json.data["LOCATG"];
				QTTAOR = json.data["QTTAOR"];
				QTSAVB = json.data["QTSAVB"];
				SUOMKY = json.data["SUOMKY"];
				
				LOTA01NM = json.data["LOTA01NM"];
				LOTA02NM = json.data["LOTA02NM"];
				STOKKY = json.data["STOKKY"];
				
				totQTCOMP += Number(QTTAOR);
		 		
				gridList.addNewRow("gridList","");
				var rowNum = gridList.getFocusRowNum("gridList");
		 		if(rowNum < 0) {
		 			PlaySound('e');
		 			return;
		 		}
		 		
		 		curIdx=rowNum;
		 		selBarcodIdx=rowNum;
		 		selBarcod = barcod;
		 		
		 		var rsMap = getQtyuom(SKUKEY, SUOMKY, QTTAOR);
				var BOX = rsMap.get("BOX");
		 		
				gridList.setColValue("gridList", rowNum, "LOTA14", barcod);
				
				gridList.setColValue("gridList", rowNum, "OWNRKY", "<%=ownrky%>");
				gridList.setColValue("gridList", rowNum, "WAREKY", "<%=wareky%>");
				gridList.setColValue("gridList", rowNum, "TASKKY", "<%=taskky%>");
				gridList.setColValue("gridList", rowNum, "QTTAOR", "0");
				gridList.setColValue("gridList", rowNum, "QTCOMP", QTTAOR);
				gridList.setColValue("gridList", rowNum, "SKUKEY", "<%=skukey%>");
				gridList.setColValue("gridList", rowNum, "SUOMKY", SUOMKY);
				
				gridList.setColValue("gridList", rowNum, "LOTA01", LOTA01);
				gridList.setColValue("gridList", rowNum, "LOTA02", LOTA02);
				gridList.setColValue("gridList", rowNum, "LOTA03", LOTA03);
				gridList.setColValue("gridList", rowNum, "LOTA09", LOTA09);
				gridList.setColValue("gridList", rowNum, "LOTA11", LOTA11);
				gridList.setColValue("gridList", rowNum, "LOTA12", LOTA12);
				gridList.setColValue("gridList", rowNum, "LOTA13", LOTA13);
				gridList.setColValue("gridList", rowNum, "LOCATG", LOCATG);
				gridList.setColValue("gridList", rowNum, "LOCAAC", LOCAAC);
				
				gridList.setColValue("gridList", rowNum, "LOTA01NM", LOTA01NM);
				gridList.setColValue("gridList", rowNum, "LOTA02NM", LOTA02NM);
				
				gridList.setColValue("gridList", rowNum, "QTSAVB", QTSAVB);
				gridList.setColValue("gridList", rowNum, "STOKKY", STOKKY);
				
				gridList.setColValue("gridList", rowNum, "BOX", BOX);
				
				gridList.setColFocus("gridList", rowNum, "QTCOMP");
				
				$("#BARCOD").val(barcod);
				$("#QTTAOR").val(QTTAOR);
				$("#QTCOMP").val(QTTAOR);
				$("#UOMKEY").val(SUOMKY);
				$("#LOCATG").val("");
				$("#LOCAAC").val("");
				$("#LOCATG").val("");
				
				$("#QTTAOR").trigger("blur");
				$("#QTCOMP").trigger("blur");
				$("#BARCOD").trigger("blur");
				$("#QTCOMP").trigger("blur");
				
				$("#QTCOMP").attr("readonly", false);
				$("#LOCAAC").attr("readonly", false);
				
				curBarcod = barcod;
				duplYN = true;  // 바코드 스캔 여부 완료
				
			}else{
				/*
				commonUtil.msgBox("HHTPROC_M0007"); // 존재하지 않는 바코드입니다.
				$("#BARCOD").val(selBarcod);        // 이전에 선택된 바코드 초기값으로 넣어줌.
				gridList.setColFocus(gridId, selBarcodIdx, "QTCOMP"); // 이전에 선택한 바코드 라인 포커싱 줌.
				$("#BARCOD").focus();
				return;
				*/
			}
			
		}
		
		$("#QTCOMP_L7141_2").val(totQTCOMP); // 적치수량 합계
		$("#QTCOMP_L7141_2").trigger("blur");
		//console.log("2 duplYN : " + duplYN + " - lastDuplRow : " + lastDuplRow);
		// 신규 수량은 없고 기존에 검색된 바코드는 있을 경우 처음 로우 선택
		if( duplYN==false && lastDuplRow!=-1 ){
			
			var duplLoopRow = -1;
			
			for(var i=0; i < list.length; i++) {
				
				var rowData = list[i];
				var rowNum = rowData.get("GRowNum");
				var rowState = gridList.getRowState(gridId, rowNum);
				var lota14 = rowData.get("LOTA14");
				
				if(barcod==lota14){
					
					if(curIdx==lastDuplRow){
						duplLoopRow = rowNum;
						break;
					}else if(curIdx<rowNum){
						duplLoopRow = rowNum;
						break;
					}
					
				}
				
			}
			
			selBarcodIdx = duplLoopRow;  // 검색 실패시 이전 선택을 위해 idx 저장
			selBarcod = barcod;
			setRowDetail(duplLoopRow);
			gridList.setColFocus(gridId, duplLoopRow, "QTCOMP");
		}
		
		//selBarcod = barcod;
		//$("#QTCOMP").attr("readonly", false);
		//$("#LOCAAC").attr("readonly", false);
		
		//console.log("2 duplYN : " + duplYN + " - lastDuplRow : " + lastDuplRow + " - duplYN : " + duplYN);
		if(lastDuplRow==-1 && duplYN==false){
			PlaySound('e');
			$("#BARCOD").val(selBarcod);        // 이전에 선택된 바코드 초기값으로 넣어줌.
			gridList.setColFocus(gridId, selBarcodIdx, "QTCOMP"); // 이전에 선택한 바코드 라인 포커싱 줌.
			commonUtil.msgBox("HHTPROC_M0007",null,null,function(){ $("#BARCOD").focus(); }); // 존재하지 않는 바코드입니다.
			return;
		}
		
		fn_onfocus("BARCOD");
		PlaySound('s');
 	}
 		
 		
	function getQtyuom(SKUKEY, UOMKEY, QTY){
 		
 		var param = new DataMap();
		
		param.put("WAREKY", "<%=wareky%>");
		param.put("SKUKEY", SKUKEY);
		param.put("UOMKEY", UOMKEY);
		param.put("QTY", QTY);
		
		var json = netUtil.sendData({
			module : "WmsInbound",
			command : "UOMQTYval",
			sendType : "map",
			param : param
		});
		
		var UOMQTY = json.data["UOMQTY"];
		var BOX = json.data["BOX"];
		
		var rsMap = new DataMap();
		rsMap.put("UOMQTY",UOMQTY);
		rsMap.put("BOX",BOX);
		
		return rsMap; 
 		
 	}
 		
	function barcodFocus(){
		$("#BARCOD").focus();
		$("#BARCOD").select();
	}
	
</script>
</head>
<body>
	<div class="main_wrap" id="bindArea">
		<div class="tem4_content">
			<div class="select_box" id="searchArea">
				<table>
					<colgroup>
						<col width="110px" />
						<col width="70px" />
						<col width="70px" />
						<col width="80px" />
						<col width="70px" />
					</colgroup>
					<tbody>
						<tr>
							<th  CL='STD_BARCOD'>바코드</th>
							<td colspan="4">
							    <input type="text" class="text" name="BARCOD" id="BARCOD" value="" style="text-align: center;" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'setBarcod()')" />
							</td>
						</tr>
						 
						<tr>
							<th CL='STD_LOCATG'>to로케이션</th>
							<td colspan="3">
								<input type="text" class="text" name="LOCAAC" id="LOCAAC" style="text-align: center;" onfocus="fn_onfocus(this.id)" onChange="ChangeValue(this)" readonly/>
							</td>
							<td onclick ="searchLocationList()">
								<button class="bt" style="height: 32px;">
									<p CL='BTN_FINDDATA'></p>
								</button>
							</td>
						</tr>
						<tr>
							<th CL='STD_LOCATG1'>지시로케이션</th>
							<td colspan="4">
								<input type="text" class="text" name="LOCATG" id="LOCATG" readonly/>
							</td>
						</tr>
						<tr>
							<th CL='STD_SKUKEY'>품번</th>
							<td colspan="4">
								<input type="text" class="text" name="SKUKEY" id="SKUKEY" value="<%=skukey%>" readonly/>
							</td>
						</tr>
						<tr>
							<th CL='STD_DESC01'>품명</th>
							<td colspan="4">
								<input type="text" class="text" name="DESC01" id="DESC01" readonly/>
							</td>
						</tr>
						<tr>
							<th  CL='STD_QTSAVB'>지시수량</th>
							<td>
								<input type="text" class="input text gridColInputNumber" name="QTTAOR" id="QTTAOR" UIFormat="N 20,3" readonly/>
							</td>
							<td>
								<input type="text" class="text" name="UOMKEY" id="UOMKEY" style="text-align:center" readonly/>
							</td>
							<th  CL=STD_QTCOMP_L7141>완료수량</th>
							<td>
								<input type="text" class="input text gridColInputNumber" name="QTCOMP" id="QTCOMP" UIFormat="N 20,3" onfocus="fn_onfocus(this.id)" onChange="ChangeValue(this)" readonly />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- hidden table -->
			<div class="tableWrap_search section" >
				<div class="tableHeader">
					<table style="width: 100%"> 
						<colgroup>
							<col width="40px" />
<!-- 								<col width="60px" /> -->
							
							<col width="130px" />
							<col width="100px" />
							<col width="90px" />
							<col width="90px" />
							<col width="90px" />
							<col width="120px" />
							<col width="80px" />
							<col width="80px" />
<!-- 								<col width="80px" /> -->
							<col width="90px" />
<!-- 								<col width="80px" /> -->
							<col width="100px" />
							<col width="150px" />
							<col width="90px" />
							<col width="90px" />
							<col width="90px" />
<!-- 								<col width="80px" /> -->
<!-- <col width="90px" /> -->
<!-- <col width="90px" /> -->
						</colgroup>
						<thead>
							<tr class="thead">
								<th CL='STD_NO'>순번</th>
<!-- 									<th GBtnCheck="true"></th> -->
								
								<th CL='STD_LOTA14'>제조LOT</th>
								<th CL='STD_LOCATG1'>지시로케이션</th>
								<th CL='STD_LOCAAC1'>to로케이션</th>
								<th CL='STD_QTCOMP'>적치수량</th>
								<th CL='STD_QTWORK'>지시수량</th>
								<th CL='STD_QTSAVB'>입력가능수량</th>
								<th CL='STD_SUOMKY'>단위</th>
								<th CL='STD_BOXQT'>BAG/BOX</th>
<!-- 									<th CL='STD_LOTA02'>플랜트</th> -->
								<th CL='STD_PLNTNM'>플랜트명</th>
<!-- 									<th CL='STD_LOTA01'>저장위치</th> -->
								<th CL='STD_STLNM'>저장위치명</th>
								<th CL='STD_LOTA09'>거래처코드MTO</th>
								<th CL='STD_LOTA11'>제조일자</th>
								<th CL='STD_LOTA12'>입고일자</th>
								<th CL='STD_LOTA13'>유통기한</th>
								
<!-- 									<th CL='STD_STOKKY'>재고키</th> -->
<!-- <th CL='STD_STLKY'>저장위치</th> -->
<!-- <th CL='STD_PLNTKY'>플랜트</th> -->
							</tr>
						</thead>
					</table>				
				</div>
				<div class="tableBody">
					<table style="width: 100%">
						<colgroup>
							<col width="40px" />
<!-- 								<col width="60px" /> -->
							
							<col width="130px" />
							<col width="100px" />
							<col width="90px" />
							<col width="90px" />
							<col width="90px" />
							<col width="120px" />
							<col width="80px" />
							<col width="80px" />
<!-- 								<col width="80px" /> -->
							<col width="90px" />
<!-- 								<col width="80px" /> -->
							<col width="100px" />
							<col width="150px" />
							<col width="90px" />
							<col width="90px" />
							<col width="90px" />
<!-- 								<col width="80px" /> -->
<!-- <col width="90px" /> -->
<!-- <col width="90px" /> -->
						</colgroup>
						<tbody id="gridList">
							<tr CGRow="true">
								<td GCol="rownum"></td>
<!-- 									<td GCol="rowCheck"></td> -->
								
								<td GCol='text,LOTA14'>제조LOT</td>
								<td GCol='text,LOCATG'>지시로케이션</td>
								<td GCol='text,LOCAAC'>to로케이션</td>
								<td GCol='text,QTCOMP' GF="N 20,3">적치수량</td>
								<td GCol='text,QTTAOR' GF="N 20,3">지시수량</td>
								<td GCol="text,QTSAVB" GF="N 20,3">입력가능수량</td>
								<td GCol='text,SUOMKY'>단위</d>
								<td GCol="text,BOX" GF="N 20,3">BAG/BOX</td>
<!-- 									<td GCol='text,LOTA02'>플랜트</td> -->
								<td GCol='text,LOTA02NM'>플랜트명</td>
<!-- 									<td GCol='text,LOTA01'>저장위치</td> -->
								<td GCol='text,LOTA01NM'>저장위치명</d>
								<td GCol='text,LOTA09'>거래처코드MTO</d>
								<td GCol='text,LOTA11'>제조일자</td>
								<td GCol='text,LOTA12'>입고일자</td>
								<td GCol='text,LOTA13'>유통기한</td>
								
<!-- 									<td GCol='text,STOKKY'>재고키</td> -->
<!-- <td GCol='text,STLKY'>저장위치</td> -->
<!-- <td GCol='text,PLNTKY'>플랜트</td> -->
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
						<col width="60px"/>
						<col width="50px"/>
						<col width="50px"/>
						<col width="60px"/>
						<col width="50px"/>
					</colgroup>
					<tbody>
						<tr>
							<th CL='STD_QTWORK'>작업수량</th>
							<td CL='STD_ALL'>전체</td>
							<td>
								<input type="text" class="text gridColInputNumber" name="QTCOMP_L7141_1" id="QTCOMP_L7141_1" readonly/>
							</td>
							<td>
								<input type="text" class="text" name="UOMKEY2" id="UOMKEY2" style="text-align:center" readonly/>
							</td>
							<td CL='STD_INCLU'>중</td>
							<td>
								<input type="text" class="text gridColInputNumber" name="QTCOMP_L7141_2" id="QTCOMP_L7141_2" readonly/>
							</td>
							<td>
								<input type="text" class="text" name="UOMKEY3" id="UOMKEY3" style="text-align:center" readonly/>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- end table_body -->
			<div class="footer_5" style="position: fixed;">
				<table>
					<tr>
						<td class=f_1 onclick ="screenClose()" width="30%"><label CL='BTN_PREVITEM'>이전</label></td>
						<td class=f_1 onclick ="saveData()" width="30%"><label CL='BTN_PCONFIRM'>적치완료</label></td>
					</tr>
				</table>
			</div><!-- end footer_5 -->	
		</div>
	</div>
</body>