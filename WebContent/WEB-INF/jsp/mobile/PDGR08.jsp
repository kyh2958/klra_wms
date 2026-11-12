<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String flag = request.getParameter("FLAG");
	if(flag==null)flag="";	
	String ebeln = request.getParameter("EBELN");
	if(ebeln==null)ebeln="";
	String docdat = request.getParameter("DOCDAT");
	if(docdat==null)docdat="";
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

.select-editable { position:relative; background-color:white; border:solid 1px #d5d5d5; width:100%; height:32px; }
.select-editable select { position:absolute; top:0px; left:0px; width:100%; height:32px; font-size:14px;  border:none;  }
.select-editable input { position:absolute; top:0px; left:0px; width:90%; height:32px; font-size:12px;  border:none;   }
.select-editable select:focus, .select-editable input:focus { outline:none; }
/* .tableBody{height: 245px !important;} */

</style>

<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>Mobile WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	var data;
	var opt;
	var asndky;
	var skukey;
	var barcod;
	var rcvlmtOrg;
	var flag;
	var activeType = false;
	var year;
	var month;
	var date;
	
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			editable : false,
			module : "WmsMobileInboundGoodsReceipt",
			command : "PDGR08",
			gridMobileType : true
		});
		
		$("#STKNUM").focus();
		
		
	});
	
	
	function searchList(){
		
		if(validate.check("searchArea")){
			
			var stknum = $.trim($("#STKNUM").val());
			
			var param = new DataMap();
			param.put("STKNUM", stknum);
			
			gridList.resetGrid("gridList");
			
			gridList.gridList({
		    	id : "gridList",
		    	param : param
		    });
		}
	}
		
	
	function showMain(){
		//window.top.menuOpen();
		window.top.closePage();
		/*
		if(opt == "pop"){
			mobile.linkPopClose();
		}else if(flag == 'Y'){
			mobile.linkPopClose();
		}else{
			window.top.closePage();
		}
		*/
	}
		 	
	function saveData(){
		
		var gridId = "gridList";
		var param = new DataMap();
		
		var gcnt = gridList.getGridDataCount(gridId);
		
		if( gcnt == 0 ){
			PlaySound('e');
			commonUtil.msgBox("VALID_M2138");// 추가된 아이템이 없습니다.
			
			var stknum = $.trim($("#STKNUM").val());
			
			if(stknum==""){
				$("#STKNUM").val("");
				$("#STKNUM").focus();
				$("#STKNUM").select();
			}else{
				$("#BARCOD").focus();
				$("#BARCOD").select();
			}
			
			return;
		}
			
			/*
			// 중복 밸리데이션
			var vParam = new DataMap();
			
			vParam.put("head", list);
			vParam.put("list", list);
			vParam.put("headTaskky","");
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
			*/
			
			
			// 저장하시겠습니까?
			var confirmMsg = configData.MSG_MASTER_SAVE_CONFIRM;
			commonUtil.msgConfirm(confirmMsg, null
					            , function(){
									
									var list = gridList.getGridData("gridList");
									param.put("OWNRKY", "<%=ownrky%>");
									param.put("list", list);
									param.put("PROGID", "<%=menuId%>");
									
									var json = netUtil.sendData({
										url : "/mobile/mobileInboundGoodsReceipt/json/SavePDGR08.data",
										param : param
									});
									
									//console.log(JSON.stringify(json));
									if(json.data && json.data.RESULTMSG ){
										PlaySound('e');
										var msgList = json.data.RESULTMSG.split("†");
										var msgTxt = commonUtil.getMsg(msgList[0], (msgList.length > 1 ? msgList[1].split("/") : null));
										
										commonUtil.msgBox(msgTxt,null,null,function(){
											$("#BARCOD").focus();
											$("#BARCOD").select();
											
										});	// 동일한 플랜트 내 지번만 변경 가능합니다.
										
										return;
									}
									
									if(json && json.data){
										PlaySound('s');
										
										commonUtil.msgBox("MASTER_M0564",null,null,function(){
											gridList.resetGrid("gridList");
											$("#BARCOD").val("");
											$("#STKNUM").focus();
											$("#STKNUM").select();
											
										}); // 저장 완료되었습니다.
										
										
							        	
									}else{
										
										PlaySound('e');
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
	
	//초기화 버튼
	function clearData(){
		gridList.resetGrid("gridList");

	    $("#BARCOD").val(""); // 바코드
	    //$("#STKNUM").val(""); // 선적번호
	    
	    $("#STKNUM").focus();
	    $("#STKNUM").select();
	}
	
	function eventKeydown(event,objId){
		if(event.keyCode == "13"){	
			
		}
	}
	
	function comboEventDataBindeBefore(comboAtt){
		if(comboAtt == "WmsMobileInboundGoodsReceipt,PDGR01_TASOTY"){
			var param = new DataMap();
			param.put("PROGID", "<%=menuId%>");
			return param;
		}
	}
	
	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList"){
			//var list = gridList.getGridData("gridList");
			//getTimer("gridListEventDataBindEnd");
			
			PlaySound('s');
		}
	}
	
	function searchEndEventFunction(){
		$("#BARCOD").focus();
	}
	
	function gridListEventRowClick(gridId, rowNum, colName){
		
	}
	
	function getImpossibleStknumArr(){
		// 입력 가능 선적번호 
		var stknumArr = new Array();
		stknumArr.push("1001383866");
		stknumArr.push("1001383865");
		stknumArr.push("1001383864");
		stknumArr.push("1001383863");
		stknumArr.push("1001383862");
		stknumArr.push("1001383863");
		
		return stknumArr;		
	}
	
	function setStknum(){
		
		var stknum = $.trim($("#STKNUM").val());
		
		var stknumArr = getImpossibleStknumArr();
		
		
		var stknumPassYn = false;
		for(var i=0;i<stknumArr.length;i++){
			var rStknum = stknumArr[i];
			if(rStknum==stknum){
				stknumPassYn = true;
				break;
			}			
		}
		
		if(stknumPassYn==false){
			alert("입력 가능한 선적번호를 입력하여 주세요.");
			$("#STKNUM").val("");
			$("#STKNUM").focus();
			$("#STKNUM").select();
			return;
		}
		
		if(stknum==""){
			//$("#STKNUM").val("");
			$("#STKNUM").focus();
			$("#STKNUM").select();
		}else{
			$("#BARCOD").focus();
			$("#BARCOD").select();
			//searchList();
		}
		
		
	}
	
	function setBarcod(){
 		
		var gridId = "gridList";
		var stknum = $.trim($("#STKNUM").val());
 		var barcod = $.trim($("#BARCOD").val());
 		
 		if(stknum==""){
 			$("#STKNUM").val("");
 			PlaySound('e');
 			commonUtil.msgBox("VALID_M2137",null,null,function(){ $("#STKNUM").focus(); }); // 선적번호를 입력하여주세요.
 			return;
 		}
 		
 		if(barcod==""){
 			$("#BARCOD").val("");
 			PlaySound('e');
 			commonUtil.msgBox("VALID_M2050",null,null,function(){ $("#BARCOD").focus(); }); // 바코드를 입력하여주세요.
 			return;
 		}
 		
 		var gridId = "gridList";
 		var list = gridList.getGridData(gridId);
 		var duplyn = false;
 		var duplRowNum = -1;
 		
		for(var i=0; i < list.length; i++) {
			var rowData = list[i];
			var rowNum = list[i].get("GRowNum");
			var lota14 = list[i].get("LOTA14");
			
			if(barcod==lota14){
				duplyn=true;
				if(duplRowNum==-1){
					duplRowNum = rowNum;
				}
			}
			
		}
		
		if(duplyn==true){
			PlaySound('e');
			// 이미 처리된 바코드입니다.
			commonUtil.msgBox("VALID_M2117",null,null
							 , function(){ 
									gridList.setColFocus(gridId, duplRowNum, "LOTA14");
									$("#BARCOD").focus();
							   }
							 );
		}else{
			
			var gcnt = gridList.getGridDataCount(gridId);
			
			if(gcnt==0){
				getAddOneList(stknum, barcod,"N");
				$("#BARCOD").focus();
				$("#BARCOD").select();
			}else{
				/*
				var newData = new DataMap();
		 		
		 		newData.put("STKNUM", stknum);
				newData.put("LOTA14", barcod);
				
		 		gridList.addNewRow("gridList",newData);
		 		*/
		 		getAddOneList(stknum, barcod,"A");
		 		$("#BARCOD").focus();
				
			}
			
		}
		
 	}
	
	
	function getAddOneList(stknum, barcod, gbn){

		var param = new DataMap();
		param.put("STKNUM", stknum);
		param.put("LOTA14", barcod);
		param.put("WAREKY", "<%= wareky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		
		var json = netUtil.sendData({
			module : "WmsMobileInboundGoodsReceipt",
			command : "GET_PDGR08_ONE_ADD_ROW",
			sendType : "list",
			param : param
		});
		
		console.log("getLotaInfoList : " + JSON.stringify(json));
		
		if (!json.data || json.data.length==0){
			alert("선적번호["+stknum+"] 개별바코드[" + barcod + "]는 존재 하지 않는 데이터 입니다.");
			return;
		}
		
		if(gbn=="N"){
			gridList.resetGrid("gridList");
			gridList.appendJson('gridList', json);
		}else{
			var newData = new DataMap();
	 		
			var stknum = json.data[0]["STKNUM"];
			var lota14 = json.data[0]["LOTA14"];
			var svbeln = json.data[0]["SVBELN"];
			var sposnr = json.data[0]["SPOSNR"];
			var skukey = json.data[0]["SKUKEY"];
			
	 		newData.put("STKNUM", stknum);
			newData.put("LOTA14", barcod);
			newData.put("SVBELN", svbeln);
			newData.put("SPOSNR", sposnr);
			newData.put("SKUKEY", skukey);
			
	 		gridList.addNewRow("gridList",newData);
		}
		
		
	}
	
	
	
	
	function barcodFocus(){
		$("#BARCOD").focus();
		$("#BARCOD").select();
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
							<col width="80px"  />
							<col />
						</colgroup>
						<tbody id="paramArea">
							
							<tr>
								<th  CL='STD_STKNUM'>선적번호</th>
								<td >
									<div class="select-editable" style="width:calc(100% - 7px); border:0px">
									<input type="text" class="text" name="STKNUM" id="STKNUM" value="" style="text-align:center;float:left" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'setStknum()')" />
									</div>
								</td>
							</tr>
							
							<tr>
								<th CL='STD_BARCOD'>바코드</th>
								<td >
									<div class="select-editable" style="width:calc(100% - 7px); border:0px">
									<input type="text" class="text" name="BARCOD" id="BARCOD" value="" style="text-align:center;float:left" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'setBarcod()')" />
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
							<col width="150px" />
							<col width="150px" />
							<col width="150px" />
							<col width="120px" />
							<col width="120px" />
						</colgroup>
						<thead>
							<tr>
								<th CL="STD_NO">No.</th>
								<th CL='STD_STKNUM'>선적번호</th>
								<th CL='STD_LOTA14'>제조LOT</th>
								<th CL='STD_SKUKEY'>제조LOT</th>
								<th CL='STD_SVBELN'>제조LOT</th>
								<th CL='STD_SPOSNR'>제조LOT</th>
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
							<col width="150px" />
							<col width="150px" />
							<col width="120px" />
							<col width="120px" />
						</colgroup>
						<tbody id="gridList">
							<tr CGRow="true">
								<td GCol="rownum">1</td>
								<td GCol="text,STKNUM">선적번호</td>
								<td GCol="text,LOTA14">제조LOT</td>
								<td GCol="text,SKUKEY">품목키</td>
								<td GCol="text,SVBELN">품목키</td>
								<td GCol="text,SPOSNR">품목키</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			
			<!-- end table_body -->
			<div class="footer_5" style="position:fixed;">
<!-- 			<div class="footer_5" "> -->
				<table>
					<tr>
						<td class=f_1 onclick ="showMain()" width="33.3%"><label CL='BTN_CLOSE'></label></td>
						<td  class=f_1 onclick="clearData()" width="33.3%"><label CL='BTN_CLEAR'></label></td>
						<td class=f_1 onclick="saveData()" width="33.3%"><label CL='STD_SAVE'></label></td>
					</tr>
				</table>
			</div><!-- end footer_5 -->	
		</div>
	</div>
</body>