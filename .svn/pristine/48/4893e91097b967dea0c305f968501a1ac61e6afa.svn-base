<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<%@ page import="project.common.airfw.*"%>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">

<%
	User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%> 
<style>
	input[type="checkbox"]{height:unset;}
</style>
<script type="text/javascript">
	
	var dblIdx = -1;
	$(document).ready(function(){
		
		gridList.setGrid({
	    	id : "gridList",
			module : "WmsTask",
			command : "PT09",
			itemGrid : "gridListSub",
			itemSearch : true
	    });
		
		gridList.setGrid({
	    	id : "gridListSub",
			module : "WmsTask",
			command : "PT09Sub"
	    });
		
		var rdata = new DataMap();
		//rdata.put(configData.INPUT_RANGE_OPERATOR, "E");
		
		rdata = new DataMap();
		rdata.put(configData.INPUT_RANGE_OPERATOR, "E");
		rdata.put(configData.INPUT_RANGE_SINGLE_DATA, 310);
		//rdata.put(configData.INPUT_RANGE_SINGLE_DATA, "FPA");   //적치완료 : FPA
		//inputList.setRangeData("TH.STATDO", configData.INPUT_RANGE_TYPE_SINGLE, [rdata]);
		inputList.setRangeData("TH.TASOTY", configData.INPUT_RANGE_TYPE_SINGLE, [rdata]);
		
		
		initSearchArea();
		
	});
	
	function initSearchArea() {
  	  // 플랜트
      <%if(null!=plant && plant.trim().equals("")){%>
        $('select[id="PLNTKY"]').multipleSelect('checkAll');
      <%}else{%>
        $('select[id="PLNTKY"]').multipleSelect('setSelects', ['<%=plant%>']);
      <%}%>
        //$('select[id="SKUL01"]').val("");
    }
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			
			gridList.resetGrid("gridList");
			gridList.resetGrid("gridListSub");
			
			gridList.gridList({
		    	id : "gridList",
		    	param : param
		    });		
			
			gridList.setReadOnly("gridListSub", true, ['LOTA02','LOTA06','LOTA08']);
		}
	}
	
	function gridListEventItemGridSearch(gridId, rowNum, itemGridList){
		if(gridId == "gridList"){
	        var param = getItemSearchParam(rowNum);
	        
	        gridList.gridList({
	           id : "gridListSub",
	           param : param
	        });
		}
	}
		
	function getItemSearchParam(rowNum){
		var rowData = gridList.getRowData("gridList", rowNum);
	    var param = inputList.setRangeParam("searchArea");
	    param.putAll(rowData);
	    
	    return param;
	}
	
	function gridExcelDownloadEventBefore(gridId){
		
		var param = new DataMap();
		
		if(gridId == "gridList"){
			param = inputList.setRangeParam("searchArea");
			param.put("dataGridExcel", "적치내역_리스트");
			return param;
		}else if(gridId == "gridListSub"){
			 var rowNum = gridList.getSearchRowNum("gridList");
			 if(rowNum == -1){
				 return false;
			 }else{
				 var param = getItemSearchParam(rowNum);
				 param.put("dataGridExcel", "적치내역_상세_리스트");
			 }
		}
		
		return param;
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Print"){
			printList();
		}
	}
	
	function printList(){
		var url = "";
		var prtseq = "";
		
		var head = gridList.getSelectData("gridList");
		
		if(head.length < 1){
			commonUtil.msgBox("VALID_M0006");
			return;
		}
		
		var param = new DataMap();
		param.put("head", head);
		
		// SELECT SEQ_PRTSEQ.NEXTVAL FROM DUAL
		var json = netUtil.sendData({
			module : "WmsTask",
			command : "SEQPRTSEQ",
			sendType : "map",
			param : param
		});
		
		var prtseq = json.data["PRTSEQ"];
		param.put("PRTSEQ", prtseq);
		
		var json = netUtil.sendData({
			url : "/wms/task/json/savePrtseq.data",
			param : param
		});
	
		var where = "AND PRTSEQ IN ('" + prtseq + "')";
	
		url = "/ezgen/putaway_list.ezg";
		
		var map = new DataMap();
		WriteEZgenElement(url, where, "", '<%= langky%>', map, 850, 630);
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType, $inputObj){
		
		var param = new DataMap();
		
		if(searchCode == "SHSTLMAP"){
			var PLNTKY = $('select[id="PLNTKY"]').val();
        	param.put("LANGKY", "<%=langky%>");
            param.put("OWNRKY", "<%=ownrky%>");
            param.put("WAREKY", "<%=wareky%>");
            param.put("PLNTKY", PLNTKY);
            if(PLNTKY==null || $.trim(PLNTKY)==""){
            	return false;
            }
            return param;
		}
	}
	
	function comboEventDataBindeBefore(comboAtt){
		
		if(comboAtt == "Wms,PLNTKYCOMBO"){
			var param = new DataMap();
			
			//var WAREKY_LIST = ['<%= wareky%>'];
			//param.put("WAREKY_LIST", WAREKY_LIST);
			
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
					<!-- <input type="button" CB="Print PRINT BTN_PRINT32" /> -->	
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_WAREKY">거점</dt>
						<dd>
							<input type="text" class="input" name="WAREKY" name="WAREKY"  value="<%=wareky%>" readonly="readonly" validate="required" />
						</dd>
					</dl>
					<dl style="display:none;">
						<dt CL="STD_OWNRKY">화주</dt>
						<dd>
							<!-- <select type="text" class="input" Combo="WmsOrder,OWNRKYCOMBO" name="OWNRKY" id="OWNRKY" STYLE="width: 150px;">
							</select> -->
							<input type="text" class="input" name="OWNRKY" value="<%=ownrky %>" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOTA02">플랜트</dt>
						<dd>
							<select type="text" class="input" Combo="Wms,PLNTKYCOMBO" name="TI.LOTA02" id="PLNTKY" ComboType="MS" style="width: 160px;" validate="required">
							</select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOTA01">저장위치</dt>
						<dd>
							<input type="text" class="input" name="TI.LOTA01" UIInput="R,SHSTLMAP" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_DOCDAT">전기일자</dt>
						<dd>
							<input type="text" class="input" name="TH.DOCDAT" UIInput="R" UIFormat="C -1"/>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_AREAKY">영역</dt>
						<dd>
							<input type="text" name="TI.AREAKY" UIInput="R,SHAREMA" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_ZONEKY">구역</dt>
						<dd>
							<input type="text" name="LM.ZONEKY" UIInput="R,SHZONMA" />
						</dd>
					</dl>
					
					<!-- 
					<dl>
						<dt CL="STD_LOCAKY">지번</dt>
						<dd>
							<input type="text" name="LM.LOCAKY" UIInput="R,SHLOCMA" />
						</dd>
					</dl>
					 -->
					 
					<dl>
						<dt CL="STD_PRODLOT">제조LOT</dt>
						<dd>
							<input type="text" class="input" name="TI.LOTA14" UIInput="R" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOCASR">from로케이션</dt>
						<dd>
							<input type="text" name="TI.LOCASR" UIInput="R,SHLOCMA_MV" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOCATG1">to로케이션</dt>
						<dd>
							<input type="text" name="TI.LOCATG" UIInput="R,SHLOCMA_MV" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOCAAC1">실로케이션</dt>
						<dd>
							<input type="text" name="TI.LOCAAC" UIInput="R,SHLOCMA_MV" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_TASOTY">작업타입</dt>
						<dd>
							<!-- <input type="text" name="TH.TASOTY" UIInput="R,SHDOCTM" value="" /> -->
							<select Combo="WmsTask,TK310DOCUTYCOMBO" name="TASOTY" id="TASOTY" class="input" STYLE="width: 177px;" validate="required(STD_RCPTTY)">
                            </select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_TASKKY">작업문서번호</dt>
						<dd>
							<input type="text" class="input" name="TI.TASKKY" UIInput="R" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_MATNR">품번</dt>
						<dd>
							<input type="text" class="input" name="TI.SKUKEY" UIInput="R,SHSKUMA"/>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_DESC01">품명</dt>
						<dd>
							<input type="text" class="input" name="TI.DESC01" UIInput="R" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_SKUG01">품목유형</dt>
						<dd>
							<select class="input" name="SKUG01" Combo="WmsInbound,SKUG01COMBO" style="width: 177px;">
								<option value=""/>
							</select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_SKUL01">품목구분</dt>
						<dd>
							<select class="input" CommonCombo="SKUL01" name="SKUL01" id="SKUL01" STYLE="width: 177px;">
							  <option value=""></option>
							</select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOTA06">재고상태</dt>
						<dd>
							<select class="input" CommonCombo="LOTA06" name="LOTA06" id="LOTA06" STYLE="width: 177px;">
							  <option value=""></option>
							</select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOTA11">제조일자</dt>
						<dd>
							<input type="text" class="input" name="TI.LOTA11" UIInput="R" UIFormat="C"/>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOTA12">입고일자</dt>
						<dd>
							<input type="text" class="input" name="TI.LOTA12" UIInput="R" UIFormat="C"/>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOTA13">유통기한</dt>
						<dd>
							<input type="text" class="input" name="TI.LOTA13" UIInput="R" UIFormat="C"/>
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
					<li><a href="#tab1-1"><span CL="STD_LIST">리스트</span></a></li>
					<li class="btn_zoom_wrap">
						<ul>
							<!-- <li><button class="btn btn_smaller"><span>축소</span></button></li> -->
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section"  id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="40" GCol="rowCheck"></td>
										
										<td GH="100 STD_TASKKY" GCol="text,TASKKY">작업지시번호</td>
										<td GH="60 STD_WAREKY" GCol="text,WAREKY">물류센터</td>
										<td GH="120 STD_WARENM" GCol="text,WARENM">물류센터명</td>
										<td GH="100 STD_DOCDAT" GCol="text,DOCDAT" GF="D">전기일자</td>
<!-- 										<td GH="70 STD_DOCUTY" GCol="text,DOCUTY">문서유형</td> -->
<!-- 										<td GH="100 STD_DOCUTYNM" GCol="text,RCPTNM">문서유형명</td> -->
										<td GH="100 STD_TASOTY" GCol="text,TASOTY">작업타입</td>
										<td GH="100 STD_TASONM" GCol="text,TASONM">작업타입명</td>
										<td GH="70 STD_STATDO" GCol="text,STATDO">문서상태</td>
										<td GH="80 STD_STATDONM" GCol="text,STATDONM">문서상태명</td>
										<td GH="100 STD_DOCCAT" GCol="text,DOCCAT">문서카테고리</td>
										<td GH="100 STD_DOCCATNM" GCol="text,DOCCATNM">문서카테고리명</td>
										<td GH="300 STD_DOCTXT" GCol="text,DOCTXT">비고</td>
										
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
			</div>
			
			<div class="content_layout tabs bottom_layout">
				<ul class="tab tab_style02">
					<li><a href="#tab1-1"><span CL="STD_DETAILLIST">아이템 리스트</span></a></li>
					<li class="btn_zoom_wrap">
						<ul>
							<!-- <li><button class="btn btn_smaller"><span>축소</span></button></li> -->
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridListSub">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="40" GCol="rowCheck"></td>
										
										<td GH="100 STD_TASKKY" GCol="text,TASKKY">작업지시번호</td>
										<td GH="100 STD_TASKIT" GCol="text,TASKIT">작업오더순번</td>
										<td GH="80 STD_STATIT" GCol="text,STATIT">아이템상태</td>
										<td GH="100 STD_STATNM" GCol="text,STATITNM">아이템상태명</td>
										<td GH="100 STD_LOCASR" GCol="text,LOCASR">from로케이션</td>
										<td GH="100 STD_LOCATG1" GCol="text,LOCATG">to로케이션</td>
										<td GH="100 STD_LOCAAC1" GCol="text,LOCAAC">실로케이션</td>
										<td GH="150 STD_SKUKEY" GCol="text,SKUKEY">상품코드</td>
										<td GH="250 STD_DESC01" GCol="text,DESC01">상품명</td>
										<td GH="100 STD_QTTAOR" GCol="text,QTTAOR" GF="N 20,3">작업수량</td>
										<td GH="100 STD_QTCOMP" GCol="text,QTCOMP" GF="N 20,3">완료수량</td>
										
										<td GH="100 STD_AUOMKY" GCol="text,AUOMKY">실단위</td>
										
										<td GH="100 STD_TOTWG" GCol="text,TOT" GF="N 20,3">총중량</td>
										<td GH="100 STD_BAGQT" GCol="text,BAG" GF="N 20,3">EA/BAG</td>
										<td GH="100 STD_BOXQT" GCol="text,BOX" GF="N 20,3">BAG/BOX</td>
										<td GH="100 STD_PLTQT" GCol="text,PLT" GF="N 20,3">BOX/Pallet</td>
										<td GH="100 STD_SOKQT" GCol="text,SOK" GF="N 20,3">속(개/BALE)</td>
										<td GH="100 STD_EAQTY" GCol="text,EA" GF="N 20,3">매(sheet/속)</td>
<!-- 										<td GH="100 STD_LOTA02" GCol="text,LOTA02">플랜트</td> -->
<!-- 										<td GH="100 STD_PLNTNM" GCol="text,LOTA02NM">플랜트명</td> -->
										<td GH="150 STD_LOTA02" GCol="select,LOTA02">
											<select CommonCombo="LOTA02"><!-- 플랜트 -->
												<option value=""></option>
											</select>
										</td>
										<td GH="80 STD_LOTA01" GCol="text,LOTA01">저장위치</td>
										<td GH="100 STD_STLNM" GCol="text,LOTA01NM">저장위치</td>
<!-- 										<td GH="100 STD_LOTA03" GCol="text,LOTA03">공급업체</td> -->
<!-- 										<td GH="200 STD_DPTNNM" GCol="text,LOTA03NM">공급업체명</td> -->
										<td GH="100 STD_LOTA06" GCol="select,LOTA06"><!-- 재고상태 -->
											<select CommonCombo="LOTA06">
												<option value=""></option>
											</select>
										</td>
										<td GH="150 STD_LOTA08" GCol="select,LOTA08">
											<select CommonCombo="NATNKY"><!-- 국가코드 -->
												<option value=""></option>
											</select>
										</td>
										<td GH="120 STD_LOTA07" GCol="text,LOTA07">MTO판매번호</td>
										<td GH="120 STD_LOTA09" GCol="text,LOTA09">거래처코드MTO</td>
										<td GH="100 STD_LOTA11" GCol="text,LOTA11" GF="C">제조일자</td>
										<td GH="100 STD_LOTA12" GCol="text,LOTA12" GF="D">입고일자</td>
										<td GH="100 STD_LOTA13" GCol="text,LOTA13" GF="D">유통기한</td>
										<td GH="150 STD_PRODLOT" GCol="text,LOTA14">제조LOT</td>
										<td GH="150 STD_LOTA15" GCol="text,LOTA15">SAP배치번호</td>
										<td GH="100 STD_OWNRKY" GCol="text,OWNRKY">화주</td>
										<td GH="100 STD_OWNERNAME" GCol="text,OWNRNM">화주명</td>
										
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
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="layout"></button>
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