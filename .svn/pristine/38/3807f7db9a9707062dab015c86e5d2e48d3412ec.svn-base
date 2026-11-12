<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String flag = request.getParameter("FLAG");
	if(flag==null)flag="";	
	String ebeln = request.getParameter("EBELN");
	if(ebeln==null)ebeln="";
	String docdat = request.getParameter("DOCDAT");
	if(docdat==null)docdat="";
	String signi = request.getParameter("SIGNI");
	if(signi==null)signi="";
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
	
	var year;
	var month;
	var date;
	
	$(document).ready(function(){
		
		gridList.setGrid({
			id : "gridList",
			editable : false,
			module : "WmsMobileInboundGoodsReceipt",
			command : "PDGR05",
			gridMobileType : true
		});
		var json = netUtil.sendData({
			module : "WmsMobileInboundGoodsReceipt",
			command : "GR_CURDATE",
			sendType : "map"
		}); 
		if(json && json.data){
			year = json.data.DOCDAT.substr(0,4);
			month = json.data.DOCDAT.substr(4,2);
			date = json.data.DOCDAT.substr(6,2);
			
			$("#DOCDAT").val(year + "." + month + "." + date);
		}
		
		//$("#BARCOD").focus();
		
		//$("#EBELN").val($("#EBELNCOMBO").val());
		$("#SIGNI").val($("#SIGNICOMBO").val());

		if("<%=docdat%>"!=""){
			year = "<%=docdat%>".substr(0,4);
			month = "<%=docdat%>".substr(4,2);
			date = "<%=docdat%>".substr(6,2);
			$("#DOCDAT").val(year + "." + month + "." + date);
			
			var duplEq = $("#SIGNICOMBO option[value='<%=signi%>']").index();
			if(duplEq!=-1){
				$("#SIGNI").val("<%=signi%>");
				$("#SIGNICOMBO").val("<%=signi%>");
			}
			
		}
		
		if($("#SIGNI").val()!="" || "<%=docdat%>"!=""){
			searchList();
		}
		
	});
	
	function searchList(){	
		
		var EBELN = "";
		
		var SIGNI = $("#SIGNI").val();
		if(SIGNI==""){
			var STD_SIGNI = commonLabel.getLabel("STD_SIGNI", 1);
			commonUtil.msgBox("VALID_M2088",[STD_SIGNI]); // [{0}]을를 입력해주세요.
			return;
		}
		
		// 컨테이너 번호로 오더번호 구하기
		var param = new DataMap();
		param.put("LANGKY", "<%=langky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("WAREKY", "<%=wareky%>");
		param.put("MTYPE", "<%=mtype%>");
		param.put("PROGID", "<%=menuId%>");
		param.put("SIGNI", SIGNI);
		var json = netUtil.sendData({
			module : "WmsMobileInboundGoodsReceipt",
			command : "GET_SIGNI_TO_EBELN",
			sendType : "map",
			param : param
		});
		if(json.data){
			EBELN = json.data.EBELN;			
		}
		
		param = new DataMap();
		param.put("LANGKY", "<%=langky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("WAREKY", "<%=wareky%>");
		param.put("MTYPE", "<%=mtype%>");
		param.put("PROGID", "<%=menuId%>");
		param.put("EBELN", EBELN);
		param.put("SEBELN", EBELN);
		//param.put("LOTA14", $("#BARCOD").val());
		
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
		
		reloadCombo();

	    $("#DOCDAT").val(year+"."+month+"."+date);	   //전기일자
	    //$("#EBELN").val(""); // 오더번호
	    //$("#BARCOD").val(""); // 바코드
	    //$("#EBELNCOMBO").val("");
	    //$("#SIGNI").val(""); // 컨테이너No
	    //$("#SIGNICOMBO").val("");
	    
	    $("#SIGNI").val($("#SIGNICOMBO").val());
	    if($("#SIGNI").val()!=""){
			searchList();
		}
	    
	}
	
	function eventKeydown(event,objId){
		if(event.keyCode == "13"){	
			
			/*
			if($("#EBELN").val() == ""){
				commonUtil.msgBox("VALID_M1569"); //오더번호를 입력해주세요
				$('#EBELN').focus();
				return;
			}
			*/
			
			if($("#SIGNI").val() == ""){
				PlaySound('e');
				var STD_SIGNI = commonLabel.getLabel("STD_SIGNI", 1);
				commonUtil.msgBox("VALID_M2088",[STD_SIGNI],null,function(){ $('#SIGNI').focus(); }); // [{0}]을를 입력해주세요.
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
			$("#SIGNI").select();
			
			searchList();
		}
	}
	
	function comboEventDataBindeBefore(comboAtt){
		if(comboAtt == "WmsMobileInboundGoodsReceipt,PDGR01_TASOTY"){
			var param = new DataMap();
			param.put("PROGID", "<%=menuId%>");
			return param;
		}else if(comboAtt == "WmsMobileInboundGoodsReceipt,GR05_EBELNCOMBO"){
			var param = new DataMap();
			param.put("WAREKY", '<%= wareky%>');
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			param.put("PROGID", "<%=menuId%>");
			param.put("MTYPE", "<%=mtype%>");
			
			return param;
		}else if(comboAtt == "WmsMobileInboundGoodsReceipt,GR05_SIGNICOMBO"){
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
			/*
			var BARCOD = $("#BARCOD").val();
			if(BARCOD!=""){
				var rowNum = gridList.getSelectIndex(gridId);
				if(rowNum!=-1){
					detailMove(gridId, rowNum);
				}
			}
			
			$("#BARCOD").focus();
			*/
			
			var rowCnt = gridList.getGridDataCount(gridId);
			if(rowCnt>0){
				PlaySound('s');
			}else{
				PlaySound('e');
			}
		}
	}
	
	function searchEndEventFunction(){
		$('#SIGNI').focus();	
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
		
		//var EBELN = $("#EBELN").val();
		var EBELN = gridList.getColData("gridList", rowNum, "EBELN");
		var DOCDAT = $("#DOCDAT").val().replace(/\./gi, "");
		//var BARCOD = $("#BARCOD").val();
		var BARCOD = "";
		var SIGNI = $("#SIGNI").val();
		
		var paramStr = "SKUKEY="+SKUKEY+"&LOTA01="+LOTA01+"&LOTA02="+LOTA02+"&LOTA06="+LOTA06+"&EBELN="+EBELN+"&DOCDAT="+DOCDAT+"&PAR_MENUID="+parMenuId+"&QTYRCV1="+QTYRCV1+"&BARCOD="+BARCOD+"&BWART="+BWART+"&SIGNI="+SIGNI;
		location.href="/mobile/PDGR05_POP.page?"+paramStr;
		
	}
	
	function dateChange(){
		var input = $("#DOCDAT").val().substr(5,2);
		if(month != input){
			PlaySound('e');
			commonUtil.msgBox("VALID_M0131"); // 전기일자는 당월이어야 합니다. VALID_M0131
			$("#DOCDAT").val(year + "." + month + "." + date);
		}
		
		searchList();
	}
	
	function reloadCombo(){
		//$comboObj = $("#EBELNCOMBO");
		//inputList.reloadCombo($comboObj, configData.INPUT_COMBO, "WmsMobileInboundGoodsReceipt,EBELNCOMBO");
		
		$comboObj = $("#SIGNICOMBO");
		inputList.reloadCombo($comboObj, configData.INPUT_COMBO, "WmsMobileInboundGoodsReceipt,GR05_SIGNICOMBO");
		
		$("#SIGNI").focus();
	}
	
	function saveData(){
		
		gridId = "gridList";
		
		var head = new Array();
		var list = new Array();
		
		var rowCnt = gridList.getGridDataCount(gridId);
		var fRow = gridList.getFocusRowNum(gridId);
		var row = gridList.getRowData(gridId, fRow);
		
		if(rowCnt < 1){
			PlaySound('e');
			commonUtil.msgBox("VALID_M2091");// 입고완료 가능한 문서가 없습니다.
			return;
		}
		
		var ebeln = row.get("EBELN");
		//console.log("ebeln : " + ebeln);
		
		var rollMaCntMap = getRollMaCountMap(ebeln);
		//console.log("rollMaCntMap : " + JSON.stringify(rollMaCntMap));
		var cnt = rollMaCntMap.CNT;
		var ftvCnt = rollMaCntMap.FTV_CNT;
		if(cnt!=ftvCnt){
			PlaySound('e');
			commonUtil.msgBox("VALID_M2114");  // 전체 데이터를 입고처리 해주세요.
			return;
		}
		
		// 컨테이너 번호로 오더번호 구하기
		var param = new DataMap();
		param.put("SEBELN", ebeln);
		var json = netUtil.sendData({
			module : "WmsMobileInboundGoodsReceipt",
			command : "PDGR05_RECDI_ITEMCNT",
			sendType : "map",
			param : param
		});
		
		//console.log("json : " + JSON.stringify(json));
		var recdiCnt = 0;
		var recvky = "";
		if(json.data){
			recdiCnt = json.data.CNT;
			recvky = json.data.RECVKY;
		}
		
		/*
		if(recdiCnt==0){
			// 입고된 문서가 없습니다. 완료처리 하시겠습니까?
			if(!commonUtil.msgConfirm("VALID_M2092")){
				return;
			}
		}else{
			// 완료처리 하시겠습니까?
			if(!commonUtil.msgConfirm("VALID_M2093")){
				return;
			}
		}
		*/
		
		// 완료처리 하시겠습니까?
		var confirmMsg = "VALID_M2093";
		if(recdiCnt==0){
			// 입고된 문서가 없습니다. 완료처리 하시겠습니까?
			confirmMsg = "VALID_M2092";
		}
		
		commonUtil.msgConfirm(confirmMsg, null
	            , function(){
			
						var param = new DataMap();
						param.put("WAREKY", "<%=wareky%>");
						param.put("OWNRKY", "<%=ownrky%>");
						param.put("PROGID", "<%=menuId%>");
						param.put("SEBELN", ebeln);
						param.put("RECVKY", recvky);
						
						var json = netUtil.sendData({
							module : "WmsMobileInboundGoodsReceipt",
							command : "GR05_HEAD",
							sendType : "list",
							param : param
						});
						
						if (json.data && json.data.length>0){
							head = json.data;
						}
						//console.log("head : " + JSON.stringify(head));
						
						var json = netUtil.sendData({
							module : "WmsInbound",
							command : "GR40Confirm",
							sendType : "list",
							param : param
						});
						
						if (json.data && json.data.length>0){
							list = json.data;
						}
						//console.log("list : " + JSON.stringify(list));
						
						param.put("head", head); 
						param.put("list", list);
						param.put("QTYCHKYN", "N");  // 이송입고일경우 wms 관리대상 체크하는데 제외 플래그
						param.put("ROLLYN", "Y");    // 수입미롤 여부
						param.put("headSebeln", ebeln);
						param.put("PROGID", "<%=menuId%>");
						
						var json = netUtil.sendData({
							//url : "/mobile/mobileInboundGoodsReceipt/json/SavePDGR05.data",
							url : "/wms/inbound/json/SaveGR02New.data",
							param : param
						});
						//console.log("savegr05 : " + JSON.stringify(json));
						if(!json.data){
							commonUtil.msgBox("SYSTEM_SAVEFAIL");
							return;
						}
						
						if(json && json.data){
							PlaySound('s');
							var inRsCode = json.data.inRsCode; // 입고저장 결과코드
				            var inRsMsg = json.data.inRsMsg;   // 입고저장 결과 메시지
							
							var ifRsCode = json.data.ifRsCode; // 인터페이스 전송 결과코드
				            var ifRsMsg = json.data.ifRsMsg;   // 인터페이스 전송 결과 메시지
				            
				            var totCnt = json.data.totCnt;     // 입고 전체 건수
				            var sucCnt = json.data.sucCnt;     // 입고 성공 건수
				            var failCnt = json.data.failCnt;   // 입고 실패 건수 
				            
				            //var sucList = json.data.sucList;   // 입고 성공 오더목록 ( 부분입고 여부 확인 불가 )
				            
				            console.log("전체 : " + totCnt + " - 성공 : " + sucCnt + " - 실패 : " + failCnt);
				        	console.log("inRsCode : " + inRsCode);
				        	console.log("ifRsCode : " + ifRsCode);
				        	//console.log("sucList : " + sucList);
				        	
				        	if(inRsCode != "S" && ifRsCode != "S"){
				        		console.log("inRsMsg : " + inRsMsg);
				        		console.log("ifRsMsg : " + ifRsMsg);
				        		//commonUtil.msgBox("VALID_M0950"); // 입고 저장에 실패하였습니다.
				        		//commonUtil.msgBox("VALID_M0953",[totCnt,sucCnt,failCnt]); // 저장 및 인터페이스 전송에 실패 하였습니다. 전체[{0}] 성공[{1}] 실패[{2}]
				        		var msgTxt = commonUtil.getMsg("VALID_M0953",[totCnt,sucCnt,failCnt]); // 저장 및 인터페이스 전송에 실패 하였습니다. 전체[{0}] 성공[{1}] 실패[{2}]
				        		alert(msgTxt + "\n" + inRsMsg + "" + ifRsMsg);
				        		//saveRsReadonly(sucList,chkHeadList,headSebeln);
				        	}else if(inRsCode != "S"){
				            	console.log("inRsMsg : " + inRsMsg);
				            	//commonUtil.msgBox("VALID_M0950"); // 입고 저장에 실패하였습니다.
				            	//commonUtil.msgBox("VALID_M0951",[totCnt,sucCnt,failCnt]); // 입고 저장에 실패 하였습니다. 전체[{0}] 성공[{1}] 실패[{2}]
				            	var msgTxt = commonUtil.getMsg("VALID_M0951",[totCnt,sucCnt,failCnt]); // 입고 저장에 실패 하였습니다. 전체[{0}] 성공[{1}] 실패[{2}]
				            	alert(msgTxt + "\n" + inRsMsg);
				            	//saveRsReadonly(sucList,chkHeadList,headSebeln);
				        	}else if(ifRsCode != "S"){
				            	console.log("ifRsMsg : " + ifRsMsg);
				            	//commonUtil.msgBox("VALID_M0942"); // 인터페이스 전송에 실패하였습니다.
				            	//commonUtil.msgBox("VALID_M0952",[totCnt,sucCnt,failCnt]); // 인터페이스 전송에 실패 하였습니다. 전체[{0}] 성공[{1}] 실패[{2}]
				            	var msgTxt = commonUtil.getMsg("VALID_M0952",[totCnt,sucCnt,failCnt]); // 인터페이스 전송에 실패 하였습니다. 전체[{0}] 성공[{1}] 실패[{2}]
				            	alert(msgTxt + "\n" + ifRsMsg);
				            	//saveRsReadonly(sucList,chkHeadList,headSebeln);
				            }else{
				            	//commonUtil.msgBox("MASTER_M0564"); // 저장 완료되었습니다.
				            	commonUtil.msgBox("VALID_M0954",[totCnt,sucCnt,failCnt]); // 저장 완료되었습니다. 전체[{0}] 성공[{1}] 실패[{2}]
				            	/*
				            	gridList.resetGrid("gridList");
								gridList.resetGrid("gridListSub");
								searchList();
								*/
				            }
							
							gridList.resetGrid("gridList");
							reloadCombo();
							
							var duplEq = $("#SIGNICOMBO option[value='" + $("#SIGNI").val() + "']").index();
							if(duplEq==-1){
								$("#SIGNICOMBO option:eq(0)").prop("selected", true);
								$("#SIGNI").val($("#SIGNICOMBO").val());
							}
							searchList();
						}else{
							PlaySound('e');
						}
			
				  }
			    , null
		);
		
		
	}
	
	function getRollMaCountMap(sebeln){
		
		var rollMaCntMap = new DataMap();
		
		// 입고 키 조회
		var param = new DataMap();
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("WAREKY", "<%=wareky%>");
		param.put("SEBELN", sebeln);
		
		var json = netUtil.sendData({
			module : "WmsInbound",
			command : "GR40_ROLL",
			sendType : "map",
			param : param
		});
		
		if (json.data){
			rollMaCntMap = json.data;
		}
		
		return rollMaCntMap;
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
								<th CL="STD_DOCDAT" width="100">전기일자</th>
								<td >
									<input type="text" class="text" inputmode="none" name="DOCDAT" id="DOCDAT" UIFormat="C" onchange="dateChange()" onkeydown="eventKeydown(event,this.id)" style="width:calc(100% - 45px);text-align:center;float:left;"/>
								</td>
							</tr>
							
							<tr>
								<th CL='STD_SIGNI' width="100">컨테이너NO</th>
								<td >
					                <div class="select-editable">
					                    <select style="border:none" Combo="WmsMobileInboundGoodsReceipt,GR05_SIGNICOMBO" onchange="this.nextElementSibling.value=this.value,searchList()" id="SIGNICOMBO">
					                    </select>
					                    <input type="text" name="SIGNI" id="SIGNI" value="" style="text-align: center;" onkeypress="commonUtil.enterKeyCheck(event, 'searchList()')" readonly />
						             </div>
								</td>
							</tr>
							
<!-- 							<tr> -->
<!-- 								<th  CL='STD_BARCOD'>바코드</th> -->
<!-- 								<td > -->
<!-- 									<div class="select-editable"> -->
<!-- 									<input type="text" name="BARCOD" id="BARCOD" value="" style="text-align: center;" onfocus="fn_onfocus(this.id)" onkeypress="commonUtil.enterKeyCheck(event, 'searchList()')" /> -->
									
<!-- 									</div> -->
<!-- 								</td> -->
<!-- 							</tr> -->
							
						</tbody>
					</table>
				</div>
			</div>
			
			<div class="tableWrap_search section">
					<div class="tableHeader">
					<table style="width: 100%">
						<colgroup>
								<col width="30px" />
								<col width="80px" />
								<col width="150px" />
								<col width="300px" />
								
								<col width="101px" />
<!-- 								<col width="105px" /> -->
<!-- 								<col width="61px" /> -->
<!-- 								<col width="61px" /> -->
								
								<col width="51px" />
								<col width="81px" />
								<col width="100px" />
								<col width="100px" />

						</colgroup>
						<thead>
							<tr>
								<th CL="STD_NO">No.</th>
								<th CL='STD_RCPTTY'>입고유형</th>
								<th CL='STD_SKUKEY'>상품코드</th>
								<th CL='STD_DESC01'>상품명</th>
								
								<th CL='STD_QTYORD'>오더수량</th>
<!-- 								<th CL='STD_QTSTOK1'>입고가능수량</th> -->
<!-- 								<th CL='STD_QTYRCV'>입고수량</th> -->
<!-- 								<th CL='STD_MENGE_B'>기입고수량</th> -->
								
								<th CL='STD_UOMKEY'>단위</th>
								<th CL='STD_LOTA06'>재고상태</th>
								<th CL='STD_LOTA02'>플랜트</th>
								<th CL='STD_LOTA01'>저장위치</th>

							</tr>
						</thead>
					</table>
				</div>
				
				<div class="tableBody">
<!-- 					<table style="width: 100%; overflow:hidden;word-wrap:break-word;"> -->
					<table style="width: 100%;">
						<colgroup>
								<col width="30px" />
								<col width="80px" />
								<col width="150px" />
								<col width="300px" />
								
								<col width="101px" />
<!-- 								<col width="105px" /> -->
<!-- 								<col width="61px" /> -->
<!-- 								<col width="61px" /> -->
								
								<col width="51px" />
								<col width="81px" />
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
<!-- 								<td GCol="text,QTYRCV1" GF="N 20,3">입고가능수량</td> -->
<!-- 								<td GCol="text,QTYRCV" GF="N 20,3">입고수량</td> -->
<!-- 								<td GCol="text,MENGE_R" GF="N 20,3">기입고수량</td> -->
								
								<td GCol="text,MEINS">단위</td>
								<td GCol="text,LOTA06NM">재고상태</td>
								<td GCol="text,LOTA02NM">플랜트</td>
								<td GCol="text,LOTA01NM">저장위치</td>
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
						<td class=f_1 onclick="saveData()" width="33.3%"><label CL='STD_EXRECP'></label></td>
						<td class=f_1 onclick="clearData()" width="33.3%"><label CL='BTN_CLEAR'></label></td>
					</tr>
				</table>
			</div><!-- end footer_5 -->	
		</div>
	</div>
</body>