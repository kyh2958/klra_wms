<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ page import="project.common.airfw.*"%>
<%@ include file="/common/include/webdek/head.jsp" %>
<style type="text/css">
 .content_wrap{background: none;}
</style>
<%
	User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%>
<script type="text/javascript">
		
	$(document).ready(function(){
		
		$("#WAREKY").val("<%=wareky%>");
		
		gridList.setGrid({
			id : "gridList",
			editable : true,
			module : "WmsSystem",
			command : "IS02"
		});
		
		gridList.setGrid({
			id : "gridListHidden",
			name : "gridListHidden",
			editable : true,
			module : "WmsSystem",
			command : "IS02_EXCEL",
			colorType : true
		});
		
		gridList.setReadOnly("gridList", true, ['WAREKY','LOTA02','SKUGBN','SKUG05']);
		
		//initSearchArea();
		
		createList();
	});
	
	function initSearchArea() {
		// 플랜트
      	$('select[id="PLNTKY"]').multipleSelect('checkAll');
      	// 거점
    	$('select[id="WAREKY"]').multipleSelect('checkAll');
    	// 제품군 구분(10:PS, 20:PS제외)
        $('select[id="SKUGBN"]').multipleSelect('checkAll');
    }
	
	function gridExcelDownloadEventBefore(gridId){
		var param = new DataMap();
		if(gridId == "gridList"){
			param.put("dataGridExcel", "라벨_기초재고셋업_리스트");
		}
		
		return param;
	}
	
	function commonBtnClick(btnName){
		
		if(btnName == "Search"){
			createList();
		} else if(btnName == "Save"){
			saveData();
		} else if(btnName == "Download1"){
			searchDownloadList('V');
		} else if(btnName == "Download2"){
			searchDownloadList();
		}
	}
	
	function searchDownloadList(SEPARATION) {
		var checkedList = gridList.getSelectData("gridList");
		if (!checkedList || checkedList.length == 0) {
			commonUtil.msgBox("VALID_M0006"); //선택된 데이터가 없습니다.
			return false;
		}
		
		var stop = false;
		for (var i = 0; i < checkedList.length; i++) {
			var row = checkedList[i];
			var SKUG05 = row.get("SKUG05");
			if (SEPARATION == 'V' && SKUG05 != '10') {
				if(!commonUtil.msgConfirm("VALID_M2134")){
					stop = true;
					break;
				}
			}
		}
		
		if (stop) {
			return false;
		}
		
		gridList.resetGrid("gridListHidden");
		
		var key = new DataMap();
		key.put("WERKS", "WERKS");
		key.put("LGORT", "LGORT");
		key.put("SBOKZ", "SBOKZ");
		key.put("BLDAT", "BLDAT");
		key.put("GIDAT", "GIDAT");
		key.put("ZLDAT", "ZLDAT");
		key.put("BUDAT", "BUDAT");
		key.put("SPERR", "SPERR");
		key.put("XBUFI", "XBUFI");
		key.put("MATNR", "MATNR");
		key.put("CHARG", "CHARG");
		key.put("BSTAR", "BSTAR");
		key.put("XAMEI", "XAMEI");
		key.put("KDAUF", "KDAUF");
		key.put("KDPOS", "KDPOS");
		key.put("LIFNR", "LIFNR");
		key.put("KUNNR", "KUNNR");
		key.put("XNULL", "XNULL");
		key.put("ERFME", "ERFME");
		key.put("ERFMG", "ERFMG");
		gridList.addNewRow("gridListHidden", key);

		var req = new DataMap();
		req.put("WERKS", "(필수)");
		req.put("LGORT", "(필수)");
		req.put("SBOKZ", "(옵션)");
		req.put("BLDAT", "(필수)");
		req.put("GIDAT", "(필수)");
		req.put("ZLDAT", "(필수)");
		req.put("BUDAT", "(필수)");
		req.put("SPERR", "(옵션)");
		req.put("XBUFI", "(옵션)");
		req.put("MATNR", "(필수)");
		req.put("CHARG", "(옵션)");
		req.put("BSTAR", "(필수)");
		req.put("XAMEI", "(옵션)");
		req.put("KDAUF", "(옵션)");
		req.put("KDPOS", "(옵션)");
		req.put("LIFNR", "(옵션)");
		req.put("KUNNR", "(옵션)");
		req.put("XNULL", "(옵션)");
		req.put("ERFME", "(필수)");
		req.put("ERFMG", "(필수)");
		gridList.addNewRow("gridListHidden", req);
		
		for (var i = 0; i < checkedList.length; i++) {
			var param = checkedList[i];
			param.put('SEPARATION', SEPARATION);
			
			var json = netUtil.sendData({
				module : "WmsSystem",
				command : "IS02_EXCEL",
				sendType : "list",
				param : param,
			});
			var isLast = i == checkedList.length - 1;
			searchDownloadListCallback(json, isLast);
		}
	}
	
	async function searchDownloadListCallback(json, isLast) {
		console.log('searchDownloadListCallback', json, isLast);
		if (!json || !json.data || json.data.length < 1) {
			return;
		}
		
		var dataList = [];
		// json to DataMap
		for (var i = 0; i < json.data.length; i++) {
			var data    = json.data[i];
			var dataMap = new DataMap();
			
			for (var key in data) {
				dataMap.put(key, data[key]);
			}
			dataList.push(dataMap);
		}
		await gridList.appendList("gridListHidden", dataList);
		
		if (isLast) {
			$("#excelTemp").trigger("click");
		}
	}
	
	function createList(){
		
		gridList.resetGrid("gridList");
	
		var param = inputList.setRangeParam("searchArea");
		param.put("LANGKY", "<%=langky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		
		gridList.gridList({
			id : "gridList",
			param : param
		});
	}
	
	function saveData(){
		// 처리할 데이터 체크
		var head = gridList.getSelectData("gridList");
		
		if( head.length == 0 ){
			commonUtil.msgBox("VALID_M0006");// 선택된 데이터가 없습니다.
			return;
		}
		
		if(gridList.validationCheck("gridList", "data")){
			var rowData;
			for(var i=0;i<head.length;i++){
				rowData = head[i];
				var INTCHK = rowData.get("INTCHK"); //입고완료여부
				if(INTCHK == "Y"){
					commonUtil.msgBox("MASTER_M1106"); // 이미 입고완료된 데이터가 존재합니다.
					var rowNum = rowData.get("GRowNum");
					gridList.setColFocus("gridList", rowNum, "INTCHK");
					return;
				}
			}
			
			// 저장 여부 체크
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
				return;
			}
			
			var param = new DataMap();
			param.clear();
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("head", head);
			
			var json = netUtil.sendData({
				url : "/wms/system/json/saveSI02Proc.data",
				param : param,
				async : true,       // 응답없는 메시지 제거를위해 비동기 처리
	            loading : true,     // 전체완료될때까지 로딩 유지
	            callBackFunction : function(returnData){
	            	netUtil.processMessageOff();
	            	
	            	if(returnData && returnData.data) {
	    				var rsList = returnData.data;
	    				var msg = "";
	    				for(var i=0; i<rsList.length;i++){
	    					
	    					var rowMap = rsList[i];					
	    					var WAREKY = rowMap.WAREKY;
	    					var LOTA02 = rowMap.LOTA02;
	    					//var INTSEQ = rowMap.INTSEQ;
	    					var ERRORCODE = rowMap.ERRORCODE;
	    					var ERRORMESG = rowMap.ERRORMESG;
	    					
	    					if(""!=msg)msg += "\n";
	    					
	    					msg += "WAREKY : " + WAREKY + " - LOTA02 : " + LOTA02;
	    					msg += "\nERRORCODE : " + ERRORCODE + " - ERRORMESG : " + ERRORMESG;
	    					
	    				}
	    				alert(msg);
	    				//commonUtil.msgBox("VALID_M0001"); // 저장이 성공하였습니다.
	    				// 재조회
	    				createList();
	    			}
	            	
	            }
			});	
			
			/*
			//console.log(JSON.stringify(json));
			if(json && json.data) {
				var rsList = json.data;
				var msg = "";
				for(var i=0; i<rsList.length;i++){
					
					var rowMap = rsList[i];					
					var WAREKY = rowMap.WAREKY;
					var LOTA02 = rowMap.LOTA02;
					//var INTSEQ = rowMap.INTSEQ;
					var ERRORCODE = rowMap.ERRORCODE;
					var ERRORMESG = rowMap.ERRORMESG;
					
					if(""!=msg)msg += "\n";
					
					msg += "WAREKY : " + WAREKY + " - LOTA02 : " + LOTA02;
					msg += "\nERRORCODE : " + ERRORCODE + " - ERRORMESG : " + ERRORMESG;
					
				}
				alert(msg);
				//commonUtil.msgBox("VALID_M0001"); // 저장이 성공하였습니다.
				// 재조회
				createList();
			}
			*/
		}
	}
	

	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
	
		var param = new DataMap();
		
		if(gridId == "gridList"){
			
		}
		
	}
	
	function gridListEventRowAddBefore(gridId, rowNum) {
	
	}
	
	function gridListEventRowAddAfter(gridId, rowNum){
	
	}
	
	function gridListEventDataBindEnd(gridId, dataLength) {
		// 입고완료건 readonly
		if(gridId == "gridList") {
			
			for(var rowNum=0; rowNum<gridList.getGridDataCount(gridId);rowNum++) {
				var INTCHK = gridList.getColData(gridId, rowNum, "INTCHK");
				//console.log("rowNum : " + rowNum + " - PRCFLG : " + PRCFLG);
				if("Y" == INTCHK){
					gridList.setRowReadOnly(gridId, rowNum, true, ['rowCheck','DOCDAT','DOCTXT']);
				}
			}
		}
	}
	
	function gridModifyStateCheckEvent() {
		return false;
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType){
	
	}
	
	function comboEventDataBindeBefore(comboAtt){
		
		if(comboAtt == "Wms,PLNTKYCOMBO"){
			var param = new DataMap();
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
					<input type="button" CB="Download1 DOWNLOAD BTN_EXCELDN1" />
					<input type="button" CB="Download2 DOWNLOAD BTN_EXCELDN2" />
					<input type="button" CB="Search SEARCH BTN_DISPLAY" />
					<input type="button" CB="Save SAVE STD_INBOUND" />
				</div>
			</div>
			
			<div class="search_inner">
				<div class="search_wrap ">
<!-- 					<dl> -->
<!-- 						<dt CL="STD_SKUGBN">제품군 구분</dt> -->
<!-- 						<dd> -->
<!-- 							<select type="text" class="input" Combo="WmsSystem,SKUGBNCOMBO" name="A.SKUGBN" id="SKUGBN" ComboType="MS" > -->
<!-- 							</select> -->
<!-- 						</dd> -->
<!-- 					</dl> -->
					
					<dl>
						<dt CL="STD_WAREKY">거점</dt>
						<dd>
<%-- 							<input type="text" class="input" name="WAREKY" name="WAREKY"  value="<%=wareky%>" readonly="readonly" validate="required" /> --%>
							<select combo="Common,WAHMACOMBO" name="WAREKY" id="WAREKY" class="input" ComboType="MS" >
							</select>
						</dd>
					</dl>
					<dl style="display:none;">
						<dt CL="STD_OWNRKY">화주</dt>
						<dd>
							<!-- <select type="text" class="input" Combo="WmsOrder,OWNRKYCOMBO" name="OWNRKY" id="OWNRKY" style="width: 150px;">
							</select> -->
							<input type="text" class="input" name="OWNRKY" value="<%=ownrky %>" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOTA02">플랜트</dt>
						<dd>
							<select type="text" class="input" Combo="Wms,PLNTKYCOMBO" name="LOTA02" id="PLNTKY" ComboType="MS" >
							</select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_SKUG05">플랜트</dt>
						<dd>
							<select type="text" class="input" CommonCombo="SKUG05" name="SKUG05" id="SKUG05" ComboType="MS" >
							</select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_EXRECPYN">입고완료여부</dt>
						<dd>
							<select  name="INTCHK" id="INTCHK" class="input" style="width: 150px;">
							  <option value="" CL="STD_ALL"></option>
							  <option value="Y">Y</option>
							  <option value="N">N</option>
							</select>
						</dd>
					</dl>
					
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
			</div>
			
		</div>
		<div class="search_next_wrap ">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
					<li><a href="#tab1-1"><span CL="STD_LIST">탭메뉴1</span></a></li>  
				</ul>
				
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40 STD_NUMBER" GCol="rownum">번호</td>
										<td GH="40" GCol="rowCheck"></td>
										
<!-- 										<td GH="90 STD_INTSEQ" GCol="text,INTSEQ">기초재고번호</td> -->
										
										<!-- 제품군 구분 -->
<!--                                         <td GH="100 STD_SKUGBN" GCol="select,SKUGBN"> -->
<!--                                             <select combo="WmsSystem,SKUGBNCOMBO"> -->
<!--                                                 <option value=""></option> -->
<!--                                             </select> -->
<!--                                         </td> -->
                                        
										<!-- 거점 -->
										<td GH="150 STD_WAREKY" GCol="select,WAREKY">
                                            <select combo="Common,WAHMACOMBO2">
                                                <option value=""></option>
                                            </select>
                                        </td>
										
										<!-- 플랜트 -->
										<td GH="150 STD_LOTA02" GCol="select,LOTA02">
                                            <select CommonCombo="LOTA02">
                                                <option value=""></option>
                                            </select>
                                        </td>
                                        
                                        <!-- 제품군 -->
										<td GH="100 STD_SKUG05" GCol="select,SKUG05">
                                            <select CommonCombo="SKUG05">
                                                <option value=""></option>
                                            </select>
                                        </td>
                                        
										<td GH="90 STD_TOTCOUNT" GCol="text,CNT" GF="N 20,3">총건수</td>
										<td GH="130 STD_QTYPHY" GCol="text,QTYPHY" GF="N 20,3">실사수량</td>
										<td GH="90 STD_DOCDAT" GCol="input,DOCDAT" GF="C N">전기일자</td>
										
										<td GH="90 STD_INTCHK" GCol="text,INTCHK">처리여부</td>
										<td GH="330 STD_DOCTXT" GCol="input,DOCTXT" GF="S 1000">비고</td>
										
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
				
				<div class="table_box section" id="tabs1-2" style="display:none">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridListHidden">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="100 STD_LOTA02"		GCol="text,WERKS"	>플랜트</td>
										<td GH="100 STD_LOTA01"		GCol="text,LGORT"	>저장 위치</td>
										<td GH="100 STD_SOBKZ"		GCol="text,SBOKZ"	>특별 재고 지시자</td>
										<td GH="100 STD_BLDAT"		GCol="text,BLDAT"	>전표 증빙일 </td>
										<td GH="100 STD_GIDAT2"		GCol="text,GIDAT"	>계획 재고 실사일</td>
										<td GH="100 STD_ZLDAT"		GCol="text,ZLDAT"	>최종실사일</td>
										<td GH="100 STD_BUDAT"		GCol="text,BUDAT"	>전표의 전기일</td>
										<td GH="100 STD_SPERR"		GCol="text,SPERR"	>재고 실사로 인한 전기 보류</td>
										<td GH="100 STD_XBUFI"		GCol="text,XBUFI"	>장부재고동결</td>
										<td GH="100 STD_MATNR2"		GCol="text,MATNR"	>자재 번호</td>
										<td GH="100 STD_CHARG"		GCol="text,CHARG"	>배치 번호</td>
										<td GH="100 STD_BSTAR"		GCol="text,BSTAR"	>재고 유형</td>
										<td GH="100 STD_XAMEI"		GCol="text,XAMEI"	>대체 단위로 처리</td>
										<td GH="100 STD_KDAUF"		GCol="text,KDAUF"	>판매 오더 번호</td>
										<td GH="100 STD_KDPOS"		GCol="text,KDPOS"	>판매 오더의 품목 번호</td>
										<td GH="100 STD_LIFNR2"		GCol="text,LIFNR"	>공급업체 계정 번호</td>
										<td GH="100 STD_KUNNR2"		GCol="text,KUNNR"	>고객 계정번호</td>
										<td GH="100 STD_XNULL"		GCol="text,XNULL"	>제로카운트</td>
										<td GH="100 STD_ERFME"		GCol="text,ERFME"	>입력단위</td>
										<td GH="100 STD_ERFMG"		GCol="text,ERFMG"	>입력 단위 수량</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="excel" id="excelTemp"></button>
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