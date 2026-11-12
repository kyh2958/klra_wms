<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<style>
	div.fl_2 {
		margin-top: -6px;
	}
</style>
<script type="text/javascript">
	var flag = "U";
	var wareky = "";
	var locaky="";
	var DAYS_FROM = 10;
	var checkHeadRow;
	var currentTabsub = 1;
	var GRowNum = 0;
	
	$(document).ready(function(){
		// 로케이션 목록
		gridList.setGrid({
	    	id : "gridList",
	    	name : "gridList",
			editable : true,
			pkcol : "WAREKY,LOCAKY",
			module : "WmsInventory",
			command : "SKUMA_PPR",
			validation : "WAREKY,ZONEKY,LOCAKY",
			bindArea : "tabs1-0",
			colorType : true
	    });
		
		// 재고목록
		gridList.setGrid({
			id : "gridListSub2",
			name : "gridListSub2",
			editable : true,
			module : "WmsInventory",
			command : "STKPPR",
			colorType : true
		});
		
		// 입출고내역
		gridList.setGrid({
			id : "gridListSub3",
			name : "gridListSub3",
			editable : true,
			module : "WmsInventory",
			command : "STKPPRGRGI",
			colorType : true
		});
		
		// 품목별 재고현황
		gridList.setGrid({
			id : "gridListSub4",
			name : "gridListSub4",
			editable : true,
			module : "WmsInventory",
			command : "STKPPR_GROUP",
			colorType : true
		});
		
		//var param = inputList.setRangeParam("searchArea");
		
		var param = new DataMap();
		
		param.put("AREAKY" , "PPR");
		gridList.gridList({
	    	id : "gridList",
	    	param : param
	    });
		
		tabChg(2, true);
	});
	
	function searchList(idx){

		var param = inputList.setRangeParam("searchArea");
		param.put("AREAKY" , "PPR");
		
		gridList.gridList({
	    	id : "gridList",
	    	param : param
	    });
		
		var headSelectIdx = parseInt(gridList.getSelectRowNumList("gridList"));
		//var LOCAKY = gridList.getColData('gridList', headSelectIdx, "LOCAKY");
		var SKUKEY = gridList.getColData('gridList', headSelectIdx, "SKUKEY");
		var CREDAT_TO = $('#tab1-2-sub input[name="CREDAT"]').val().replaceAll(".", ""); // 시점재고
		var LOTA12 = $('#tab1-0-sub input[name="LOTA12"]').val().replaceAll(".", "");	// 입고/출고일
		
		param.put("SKUKEY" , SKUKEY);

		param.put("DAYS_FROM" , DAYS_FROM);
		
		if (idx == 1) {
			param.put("LOTA12" , LOTA12);
		}
		
		if (idx == 2) {
			if (CREDAT_TO) {
				param.put("CREDAT_TO" , CREDAT_TO);
			} else {
			}
		}
		
		if (idx == 3) {
			param.put("CREDAT" , LOTA12);
		}
		
		if (idx == 4) {
			if (CREDAT_TO) {
				param.put("CREDAT_TO" , CREDAT_TO);
			} 
		}
		
		gridList.gridList({
	    	id : "gridListSub"+idx,
	    	param : param
	    });
	}
	
	
	function sendData(){

        // item)
		var chkItemList = gridList.getSelectData("gridListSub2");
		var modRowCnt = gridList.getModifyRowCount("gridListSub2");
		
		if(modRowCnt < 1) {
			commonUtil.msgBox("VALID_M1573"); //변경된 데이터가 없습니다.
			return;
		}

		if(!commonUtil.msgConfirm("OUT_M0191")){ //출고완료 하시겠습니까?
            return;
        } else {

        	// 출고 처리
    	    //var list = gridList.getModifyData("gridListSub2");
    	    var list = gridList.getSelectData("gridListSub2");
    	    
    	    for(var i=0;i<list.length;i++){
				var rowData = list[i];
				var outqty = rowData.get("OUTQTY");
				var sbwart = rowData.get("SBWART");
				var lota02 = rowData.get("LOTA02");
				var lota01 = rowData.get("LOTA01");

				if (outqty < 1) {
					alert('출고수량을 입력해주세요.');
					gridList.setColFocus("gridListSub2", i, "QTSIWH");
					return;
				}
				
				if (sbwart == " " || lota02 == " " || lota01 == " ") {
					alert('이동유형, 플랜트, 저장위치 선택 후 출고 진행해주세요');
					gridList.setColFocus("gridListSub2", i, "SBWART");
					return;
				}
			}

			var param = new DataMap();
			param.put("list", list);

    	    var json = netUtil.sendData({
    	    	url : "/wms/inventory/json/SendSD82.data",
    	    	param : param
        	});
    	    
    	    //console.dir(json)
    	    if(json && json.data){
    	    	
    	    	if(json.data.EV_RCODE == "E"){
    	    		alert(json.data.EV_RMSG)
    	    	} else {
					commonUtil.msgBox("OUT_M0192");   //저장되었습니다.
					searchList(2);
    	    	}
			} else {
				commonUtil.msgBox("MASTER_M0545");	// * 변경된 데이터가 없습니다.
				searchList(2);
			}
        }
	}
	
	// 입고취소, 출고취소
	function cancelData(type) {
		var chkItemList = gridList.getSelectData("gridListSub3");
		var msgCode = "OUT_M0193";	// 출고취소 하시겠습니까?
		var msgRsCode = "OUT_M0194";	// 출고취소 되었습니다.
		
		if (type != "Cancel") {
			msgCode = "IN_M0102";	// 취소하시겠습니까?
			msgRsCode = "VALID_M1560";
		} 

		if(chkItemList.length && commonUtil.msgConfirm(msgCode)){ //출고취소 하시겠습니까?
				
			var list = gridList.getSelectData("gridListSub3");
			var initCnt = 0;
			var msg = "";
		
			// SAP 문서번호가 없는 기초재고의 경우 WMS, SAP 각각 출고취소 필요
			for(var i = 0; i< list.length;i++){
                var rowData = list[i];
                var EV_MBLNR = rowData.get("EV_MBLNR");
                var skukey = rowData.get("SKUKEY");
                var qtsiwh = rowData.get("QTSIWH");
                
                if (type == "Cancel") {
                	if (EV_MBLNR == "" || EV_MBLNR == " ") {
						initCnt++;
						
						msg += skukey + " / " + qtsiwh + "\n";
					}
                	
                	//console.dir(rowData)
					//console.dir(msg)
                }
            }

			if (type == "Cancel" && initCnt) {
				msg = "SAP 미연동 출고취소가 필요한 항목이 존재합니다. (" + initCnt +")\n" + msg;
				alert(msg);
			}
    	    
    	    var param = new DataMap();
			param.put("list", list);

			if (1) {
				var json = netUtil.sendData({
	    	    	url : "/wms/inventory/json/CancelSD82.data",
	    	    	param : param
	        	});
	    	    
	    	    if(json && json.data){
	    	    	
	    	    	if (json.data.sucCnt > 0) {
	    	    		commonUtil.msgBox(msgRsCode);
	    				searchList(3);
	    	    	}
	    	    	else {
	    	    		alert(json.data.EV_RMSG)
	    	    	}
					
				}
			}
    	    
        } else {
            return;
        }
	}

	function gridListEventRowCheck(gridId, rowNum, checkType){
		
		var headSelectIdx = parseInt(gridList.getSelectRowNumList("gridList"));
		var LOCAKY = gridList.getColData('gridList', headSelectIdx, "LOCAKY");
		
		if (LOCAKY == undefined) {
			LOCAKY = ' ';
		}

		if(gridId == "gridList"){
		}
		else if (gridId == "gridListSub3") {
			
		}
	}
	
	function gridListEventRowClick(gridId, rowNum, colName){
		if(gridId == "gridList"){

			gridList.checkAll(gridId, false);
			
			if (checkHeadRow == rowNum) {
				checkHeadRow = -1;
			}
			else {
				checkHeadRow = rowNum;
				gridList.setRowCheck(gridId, rowNum, true);
			}

			if ($("#tab1-2").css('display') == 'block') {
				tabChg(2);
			}
			if ($("#tab1-3").css('display') == 'block') {
				tabChg(3);
			}
		}else if (gridId == "gridListSub3"){
			
			
		}
	}
	
	function gridListEventRowDblclick(gridId, rowNum, colName, colValue){
		
		if (gridId == "gridListSub3"){
		}
	}
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
	}
	 
	function gridListEventRowCopyAfter(gridId, rowNum){
	}

 	function gridListEventRowAddBefore(gridId, rowNum){
 		
		if(gridId == "gridListSub2"){
    		
			var headSelectIdx = parseInt(gridList.getSelectRowNumList("gridList"));
			var LOCAKY = gridList.getColData('gridList', headSelectIdx, "LOCAKY");
			var SHORTX = gridList.getColData('gridList', headSelectIdx, "SHORTX");
			
			if (LOCAKY == undefined) {
				
				alert('지번 선택 후 진행해주세요');
				return;
			}
			
			// 재고키 설정필요
			var newData = new DataMap();	
			newData.put("LOCAKY" , LOCAKY);
			newData.put("SHORTX" , SHORTX);
			newData.put("WAREKY" , "<%=wareky%>");
			
			return newData;
		}
	}  
 	
	function gridListEventRowAddAfter(gridId, rowNum){
 		
		if(gridId == "gridListSub2"){
			gridList.setRowReadOnly(gridId, rowNum, true, ["TOTQTY", "OUTQTY"]);
			
			var LOCAKY = gridList.getColData(gridId, rowNum, "LOCAKY");

			if (LOCAKY == "") {
				gridList.removeRow(gridId, rowNum, false);
			} else {
				var rowData = gridList.getRowData("gridListSub2", gridList.getFocusRowNum("gridListSub2"));
				
				rowData.put("RFCFN", "Z_IM_ROH2_GR_LIST")
				rowData.put("IFKNM", "수입고지 입고리스트")
				rowData.put("RFCTBL", "I_MATNR")
				rowData.put("SYSFR", "SAP")
				rowData.put("SYSTO", "WMS")
				rowData.put("IFTBL", "IFWMS103")
				rowData.put("IFID", "ROH_IF_220")
				//rowData.put("RFCP1", "O_TABLE")

				GRowNum = rowNum;
				page.linkPopOpen("/wms/erpif/IF00POPR.page", rowData);
			}
		}
	}  

 	function locakyCheck(valueTxt, $colObj){
	}
 	
 	function gridListEventRowRemove(gridId, rowNum){
	}
 	
 	function searchHelpEventOpenBefore(searchCode, gridType){
 		if(searchCode == "SHSKUMA"){
			var param = new DataMap();
			param.put("OWNRKY", 'KN');
			return param;
		}
	}
 	
	function gridListEventRowCheckAll(gridId, checkType){
    	if (gridId == "gridList" && checkType == true) {
    		gridList.checkAll('gridList',false);
		}
    }
 	
 	function commonBtnClick(btnName){
		if(btnName == "Search"){
			// 시점재고일값 초기화
			$('#tab1-0-sub input[name="LOTA12"]').val("");
			$('#tab1-2-sub input[name="CREDAT"]').val("");
			searchList(currentTabSub);
		}else if(btnName == "Send"){
			$('#tab1-2-sub input[name="CREDAT"]').val("");
			sendData();
		}else if(btnName == "Cancel" || btnName == "CancelRcv" ){
			cancelData(btnName);
		}else if(btnName == "SearchGR"){
			searchList(currentTabSub);
		}else if(btnName == "Reflect"){
			//searchList(2);
			searchList(currentTabSub);
		}
	}

	function comboEventDataBindeBefore(comboAtt){
		if(comboAtt == "airfw,COMCOMBO"){			
			 var param = new DataMap();
	         param.put("CODE", 'LOTA02');
	         return param;
		}else if(comboAtt == "WmsTask,TK01_RSNCD_SET"){
			var param = new DataMap();
			param.put("LANGKY", "<%=langky%>");
			param.put("DOCUTY", "399");
			return param;
		}
    }
	
	// 90% 초과 색상변경
	function gridListRowTextColorChange(gridId, rowNum, rowData){
		if(gridId === "gridList"){
			if(parseFloat(rowData.get("PERLOD")) > 90){
				return configData.GRID_COLOR_TEXT_RED_CLASS;
			}
		}
	}
	
	function gridListEventDataBindEnd(gridId, dataLength){
        if(gridId == "gridList" && dataLength > 0){
        	
        	if (checkHeadRow > 0) {
	        	gridList.setRowCheck(gridId, checkHeadRow, true);        	
	        	gridList.setRowFocus(gridId, checkHeadRow);
        	} else {
	        	if (checkHeadRow == undefined) {
	        		//gridList.setRowCheck(gridId, 0, true);       
	        	} else {
	        		
	        	}
        	}
        }else if(gridId == "gridListSub2" && dataLength > 0){
        	
        	for(var i = 0; i< dataLength;i++){
                //gridList.setRowReadOnly(gridId, i, true, ["LOTA12", "SKUKEY", "GRSWGT"]);
                gridList.setRowReadOnly(gridId, i, true, ["LOTA12", "SKUKEY", "GRSWGT", "TOTQTY", "OUTQTY", "LOCAKY", "STOKKY", "SKUKEY_TO"]);
            }
	
        }else if(gridId == "gridListSub3" && dataLength > 0){
        	
        	for(var i = 0; i< dataLength;i++){
                var TASKTY 	=  gridList.getColData(gridId,i,"TASKTY");
                var STOKKY_CNT 	=  gridList.getColData(gridId,i,"STOKKY_CNT");
                var STOKKY 	=  gridList.getColData(gridId,i,"STOKKY");	// 기초재고 생성시 10000 미만 키값으로 생성되어 RFC 입고취소 처리 불가
                var IF_SEQ 	=  gridList.getColData(gridId,i,"IF_SEQ");	// PULPIN 미존재 데이터는 RFC연동 불가

                if (TASKTY == '입고' && (STOKKY_CNT > 1 || STOKKY < 10000 || IF_SEQ == ' ')) {
                	// 수입입고 && 출고이력 없음 > 입고취소 가능
                	if (STOKKY == '00001' && STOKKY_CNT == 1) {
                	} else {
                		gridList.setRowReadOnly(gridId, i, true, ["rowCheck"]);
                	}
                }
            }
        }
        
    }
	
	function tabChg(num, isInit){
		currentTabSub = num;
		
		var gridId = "gridListSub"+num;
		var param = inputList.setRangeParam("searchArea");
		
		$("#tab1-0-sub").hide();
		$("#tab1-2-sub").hide();

		if(num == 1) {
			$("#tab1-1").show();
			$("#tab1-2").hide();
			$("#tab1-3").hide();
			$("#tab1-0-sub").show();
			
			$('#tab1-0-sub input[name="LOTA12"]').val("");
			
			param.put("DAYS_FROM" , DAYS_FROM);

		} else if(num == 2){
			$("#tab1-1").hide();
			$("#tab1-2").show();
			$("#tab1-3").hide();
			$("#tab1-2-sub").show();
			
			$('#tab1-2-sub input[name="CREDAT"]').val("");
			
			var headSelectIdx = parseInt(gridList.getSelectRowNumList("gridList"));
			//var LOCAKY = gridList.getColData('gridList', headSelectIdx, "LOCAKY");
			var SKUKEY = gridList.getColData('gridList', headSelectIdx, "SKUKEY");
			
			var param = new DataMap();
			param.put("SKUKEY" , SKUKEY);

		} else if(num == 3){
			$("#tab1-1").hide();
			$("#tab1-2").hide();
			$("#tab1-3").show();
			$("#tab1-0-sub").show();
			
			var headSelectIdx = parseInt(gridList.getSelectRowNumList("gridList"));
			var SKUKEY = gridList.getColData('gridList', headSelectIdx, "SKUKEY");
			param.put("SKUKEY" , SKUKEY);

		} else if(num == 4){
			$("#tab1-1").hide();
			$("#tab1-2").hide();
			$("#tab1-3").hide();
			$("#tab1-4").show();
			$("#tab1-2-sub").show();
			
			$('#tab1-2-sub input[name="CREDAT"]').val("");
			
			var headSelectIdx = parseInt(gridList.getSelectRowNumList("gridList"));
			var LOCAKY = gridList.getColData('gridList', headSelectIdx, "LOCAKY");
			param.put("LOCAKY" , LOCAKY);

		} 
		
		
		gridList.resetGrid(gridId);
		
		gridList.gridList({
			id : gridId,
			param : param
		});
	}
	
	function rowDblclickCallback(data) {
		//console.dir(data)
		//console.dir(GRowNum + " / " + data.get("EBELN"))
		
		// 동일한 수입종이 PK 여부 확인 (이미 등록된 수입종이 입고대상 입니다.)
		
		var list = gridList.getGridData("gridListSub2");

		if (data.get("MATNR") == "") {
			gridList.removeRow("gridListSub2", list.length-1, false);
		}
		
		for(var i=0; i < list.length; i++) {
			var DESC01 = list[i].get("DESC01");

			if(DESC01 == data.get("EBELN")) {
				alert('중복된 수입통관번호');
				gridList.removeRow("gridListSub2", i, false);
				break;
			}
		}

		var today = new Date();
		var year = today.getFullYear();
		var month = String(today.getMonth() + 1).padStart(2, '0');
		var day = String(today.getDate()).padStart(2, '0');
		var yyyymmdd = year + "" + month + "" + day;

		gridList.setColValue('gridListSub2', GRowNum, "SKUKEY", data.get("MATNR"));
		gridList.setColValue('gridListSub2', GRowNum, "GRSWGT", data.get("GRMENGE"));
		gridList.setColValue('gridListSub2', GRowNum, "BLNO", data.get("ZFBLNO"));
		gridList.setColValue('gridListSub2', GRowNum, "IVNO", data.get("ZFIVNO"));
		gridList.setColValue('gridListSub2', GRowNum, "EBELN", data.get("EBELN"));
		gridList.setColValue('gridListSub2', GRowNum, "EBELP", data.get("EBELP"));
		gridList.setColValue('gridListSub2', GRowNum, "ZFBLIT", data.get("ZFBLIT"));
		gridList.setColValue('gridListSub2', GRowNum, "LIFNR", data.get("LIFNR"));
		gridList.setColValue('gridListSub2', GRowNum, "LOTA12", yyyymmdd);
		gridList.setColValue('gridListSub2', GRowNum, "LOTA01", "1100");	// 저장위치 고정
		gridList.setColFocus("gridListSub2", rowNum, "QTSIWH");
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
					<!-- <input type="button" CB="Save SAVE BTN_SAVE" /> -->
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
                        <dt CL="STD_SKUKEY">품목코드</dt>
                        <dd>
                            <input type="text" class="input" name="STP.SKUKEY" UIInput="SR" UIFormat="U"/>
                        </dd>
                    </dl> 
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
				
			</div>
		</div>
        <div class="search_next_wrap">
			<div class="content_layout tabs content_left" style='width: calc(35% - 10px);'>
				<ul class="tab tab_style02">
					<li><a href="#tabs1-0" CL='STD_LIST'><span>리스트</span></a></li>
					<li class="btn_zoom_wrap">
						<ul>
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section" id="tabs1-0">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40 STD_NUMBER" GCol="rownum">1</td>
										<td GH="40" GCol="rowCheck"></td>
										<td GH="60 STD_SKUKEY" GCol="text,SKUKEY"></td>
										<td GH="80 STD_DESC01" GCol="text,DESC01"></td>
	
										<td GH="60 PPR_QTSIWH" GCol="text,QTSIWH" GF="N 10"></td><!-- 배일수 -->
										<td GH="80 PPR_TOTWG" GCol="text,TOTWG" GF="N 10,4"></td><!-- 적재량 -->			
										<td GH="110 PPR_FIFO_SINCE_DAYS"GCol="text,SINCE_DAYS"	GF="D"	>적재일수</td>
										<td GH="120 PPR_FIFO_GR_CREDAT"	GCol="text,FIFO_GR_CREDAT"	GF="D"	>적재일</td>						
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="filter"></button>
						<!-- <button type='button' GBtn="find"></button> -->
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
				
			</div>
			
			<div class="content_layout tabs content_right" style='width: calc(65% - 10px);'>
				<ul class="tab tab_style02">
					<li><a href="#tab1-2" onclick="tabChg(2);"><span>재고목록</span></a></li>
					<li><a href="#tab1-3" onclick="tabChg(3);"><span>입출고내역</span></a></li>
					<li><a href="#tab1-4" onclick="tabChg(4);"><span>품목별재고현황</span></a></li>
					<li class="btn_zoom_wrap">
						<ul>
							<!-- <li><button class="btn btn_smaller"><span>축소</span></button></li> -->
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>

				
				<!-- 재고목록 -->
				<div class="table_box section" id="tab1-2">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridListSub2">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="40" GCol="rowCheck"></td>
										<td GH="80 STD_LOTA12"		Gcol="input,LOTA12"	GF="C"		>입고일자</td>
										<td GH="80 STD_SKUKEY"		GCol="input,SKUKEY,SHSKUMA" 	>품목코드</td>
							         	<td GH="110 STD_DESC01"		Gcol="text,DESC01"				>품목명</td>
										<td GH="100 PPR_SHORTX"		GCol="text,SHORTX"				>지번명</td>
										<td GH="100 STD_LOCAKY"		GCol="input,LOCAKY"				>지번</td>
										<td GH="80 STD_TOTQTY"		GCol="input,TOTQTY"	GF="N 6"	>총재고</td>
										<td GH="80 STD_GRSWGT"		Gcol="text,GRSWGT"	GF="N 9,1"	>중량	</td>
							         	<td GH="80 PPR_AVG_GRSWGT"	Gcol="text,AVG_GRSWGT"	GF="N 6, 2"	>평균중량</td>
										<td GH="110 PPR_FIFO_SINCE_DAYS"	GCol="text,FIFO_SINCE_DAYS"	GF="D"	>적재일수</td>
										<td GH="120 PPR_FIFO_GR_CREDAT"	GCol="text,FIFO_GR_CREDAT"	GF="D"	>적재일</td>
										<td GH="80 PPR_CRETIM"		GCol="text,CRETIM"				>적재시간</td>
										<td GH="110 PPR_LAST_GI_DATE"	GCol="text,LAST_GI_DATE"	GF="D"	>마지막출고일</td>
										<td GH="60 STD_STOKKY"		GCol="input,STOKKY"				>재고키</td>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="add"></button>
						<button type='button' GBtn="filter"></button>
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
				<!-- 재고 히스토리 -->
				<div class="table_box section" id="tab1-3">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridListSub3">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="40" GCol="rowCheck"></td>
										<td GH="60 STD_TASKTY"		GCol="text,TASKTY"				>작업구분</td>
										<td GH="80 STD_SKUKEY"		Gcol="text,SKUKEY"				>품목코드</td>
							         	<td GH="110 STD_DESC01"		Gcol="text,DESC01"				>품목명</td>
										<td GH="60 PPR_QTSIWH"		GCol="text,QTSIWH"	GF="N 6"	>재고수량</td>
							         	<td GH="80 STD_GRSWGT"		Gcol="text,GRSWGT"	GF="N 9,1"	>중량	</td>
										<td GH="80 STD_LOCAKY"		GCol="text,LOCAKY"				>지번</td>
										<!-- <td GH="80 STD_LOTA12"		Gcol="text,LOTA12"	GF="D"		>입고일</td> -->
										<td GH="80 PPR_GRGI_DAT"	GCol="text,GRGI_DAT"	GF="D"	>생성일</td>
										<td GH="60 PPR_CRETIM"		GCol="text,CRETIM"				>생성시간</td>
										<td GH="80 STD_WAREKY"		Gcol="text,WAREKY"				>거점</td>
										<td GH="100 STD_NAME01"		Gcol="text,NAME01"				>거래처</td>
							         	<td GH="100 STD_TELN01"		Gcol="text,TELN01"				>연락처</td>
										<td GH="80 STD_STOKKY"		GCol="text,STOKKY"				>재고키</td>
										<td GH="60 STD_TASKKY_1"	GCol="text,TASKKY_1"			>입고순서</td>
										<td GH="100 STD_MBLNR"		GCol="text,EV_MBLNR"			>출고문서번호</td>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="filter"></button>
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="subTotal"></button>
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
				<!-- 품목별 재고현황 -->
				<div class="table_box section" id="tab1-4">
					<div class="table_list01">
						<div class="table_thead tableHeader" style="margin-left: 0px;">
							<table>
								<colgroup>
									<col width="40px" />
									<col width="40px" />
									<col width="80px" />
									<col width="120px" />
									<col width="60px" />
									<col width="80px" />
									<col width="80px" />
									<col width="60px" />
									<col width="80px" />
									<col width="80px" />
									<col width="60px" />
									<col width="80px" />
									<col width="80px" />
									<col width="60px" />
									<col width="80px" />
									<col width="80px" />
								</colgroup>
								<thead>
									<tr>
										<th colspan='4'>품목정보</th>
										<th colspan='3'>기초재고(전일)</th>
										<th colspan='3'>입고 (전일)</th>
										<th colspan='3'>투입 (전일)</th>
										<th colspan='3'>기초재고 (당일)</th>
									</tr>
									<tr>
										<th CL="번호"></th>
										<th CL=" "></th>
										<th CL="STD_SKUKEY"			>품목코드</th>
										<th CL="STD_DESC01"			>품목코드</th>
										<th CL="PPR_QTSIWH"			>재고수량</th>
							         	<th CL="PPR_AVG_GRSWGT"		>평균중량(KG)</th>
							         	<th CL="STD_TOTWG"			>총중량(KG)</th>
										<th CL="PPR_QTSIWH"			>재고수량</th>
							         	<th CL="PPR_AVG_GRSWGT"		>평균중량(KG)</th>
							         	<th CL="PPR_BALE_KG"			>중량(KG)</th>
										<th CL="PPR_QTSIWH"			>재고수량</th>
							         	<th CL="PPR_AVG_GRSWGT"		>평균중량(KG)</th>
							         	<th CL="PPR_BALE_KG"			>중량(KG)</th>
										<th CL="PPR_QTSIWH"			>재고수량</th>
							         	<th CL="PPR_AVG_GRSWGT"		>평균중량(KG)</th>
							         	<th CL="STD_TOTWG"			>총중량(KG)</th>
									</tr>
								</thead>
							</table>
						</div>
					
						<div class="scroll" style='padding-bottom: 20px;'>
							<table class="table_c">
								<colgroup>
									<col width="40px" />
									<col width="40px" />
									<col width="80px" />
									<col width="120px" />
									<col width="60px" />
									<col width="80px" />
									<col width="80px" />
									<col width="60px" />
									<col width="80px" />
									<col width="80px" />
									<col width="60px" />
									<col width="80px" />
									<col width="80px" />
									<col width="60px" />
									<col width="80px" />
									<col width="80px" />
								</colgroup>
								<tbody id="gridListSub4">
									<tr CGRow="true">
										<td GCol="rownum">1</td>
										<td GCol="rowCheck"></td>
										<td Gcol="text,SKUKEY"				>품목코드</td>
										<td Gcol="text,DESC01"				>품목코드</td>
										<td GCol="text,QTSIWH_BASE_Y"	GF="N 6"	>재고수량</td>
							         	<td Gcol="text,AVGKG_BASE_Y"	GF="N 5,1"	>평균중량(KG)</td>
							         	<td Gcol="text,GRSWGT_BASE_Y"	GF="N 9,1"	>총중량(KG)</td>
										<td GCol="text,QTSIWH_GR"	GF="N 6"	>재고수량</td>
							         	<td Gcol="text,AVGKG_GR"	GF="N 5,1"	>평균중량(KG)</td>
							         	<td Gcol="text,GRSWGT_GR"	GF="N 9,1"	>중량(KG)</td>
										<td GCol="text,QTSIWH_GI"	GF="N 6"	>재고수량</td>
							         	<td Gcol="text,AVGKG_GI"	GF="N 5,1"	>평균중량(KG)</td>
							         	<td Gcol="text,GRSWGT_GI"	GF="N 9,1"	>중량(KG)</td>
										<td GCol="text,QTSIWH_BASE_T"	GF="N 6"	>재고수량</td>
							         	<td Gcol="text,AVGKG_BASE_T"	GF="N 5,1"	>평균중량(KG)</td>
							         	<td Gcol="text,GRSWGT_BASE_T"	GF="N 9,1"	>총중량(KG)</td>
						         	<tr>

								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="filter"></button>
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="total"></button>
						<!-- <button type='button' GBtn="layout"></button> -->
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