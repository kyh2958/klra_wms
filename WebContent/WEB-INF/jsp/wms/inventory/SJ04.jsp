<%@page import="project.common.airfw.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<%
	User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<script type="text/javascript">
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			name : "gridList",
			module : "WmsInventory",
			command : "SJ03",
			editable : true
			
		});
		
		gridList.setGrid({
			id : "gridListSub",
			name : "gridListSub",
			module : "WmsInventory",
			command : "SJ03Sub",
			editable : true
		});
		
		initSearchArea();
		initBundleModifyArea();
	});
	
	function initSearchArea() {
		<%if(plant.equals("")){%>
			$('select[name="S.LOTA02"]').multipleSelect('checkAll');
		<%}else{%>
			$('select[name="S.LOTA02"]').multipleSelect('setSelects', ['<%=plant%>']);
		<%}%>
		$('select[name="SKUL01"]').val("");
	}
	
	function initBundleModifyArea() {
		$("#bundleModify1").val("");
		$("#bundleModify2").val("");
		$("#bundleModify3").val("");
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Reflect2"){
			setGridDataByReflectBtn('2');
		}else if(btnName == "Reflect3"){
			setGridDataByReflectBtn('3');
		}
	}
	
	var searchType = true;
	
	function searchList(){
		gridList.setReadOnly("gridList", false);
		gridList.setReadOnly("gridListSub", false);
		
		searchType = true;
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			param.put('WAREKY', '<%=wareky%>');
			param.put('OWNRKY', '<%=ownrky%>');
			param.put('LANGKY', '<%=langky%>');
			
			gridList.gridList({
				id : "gridListSub",
				param : param
			});
		}
		
		initBundleModifyArea();
	}
	
	function gridListEventDataBindEnd(gridId, dataCount){
		if(dataCount == 0) {
			return;
		}
		
		if(gridId == "gridList"){
			uiList.setActive("Search", true);
			uiList.setActive("Save", true);
			uiList.setActive("Reflect", true);
		} else if(gridId == "gridListSub" && searchType){
			uiList.setActive("Search", false);
			
			var param = inputList.setRangeParam("searchArea");
			param.put('WAREKY', '<%=wareky%>');
			param.put("PROGID", "<%=menuId%>");
			param.put("LANGKY", "<%=langky%>");
			
			gridList.gridList({
				id : "gridList",
				param : param
			});
		}
	}
	

	// 반영(Reflect)버튼 이벤트
	function setGridDataByReflectBtn(flag){
		if(flag) {
			var bundleModify = $("#bundleModify" + flag).val();
			if(bundleModify){
				var targetCol = '';
				switch (flag) {
					case '2' : targetCol = 'ARSNADJ'; break;
					case '3' : targetCol = 'AADJRSN'; break;
					default  : targetCol = ''; break;
				}
				var selectNumList = gridList.getSelectRowNumList("gridListSub");
				for(var i=0;i<selectNumList.length;i++){
					var rowNum = selectNumList[i];
					gridList.setColValue("gridListSub", rowNum, targetCol, bundleModify);
				}
			}
		}
	}
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId == "gridList"){
			uiList.setActive("Save", true);
			
			// 전기일자 체크
			if(colName == "DOCDAT") {
				var curMonth = new Date().getMonth() + 1;
				if(curMonth != Number(colValue.substr(4, 2))) {
					commonUtil.msgBox('VALID_M0131'); // 전기일자는 당월이어야 합니다.
					gridList.setColValue(gridId, rowNum, "DOCDAT", "");
					gridList.setColFocus(gridId, rowNum, colName);
					return
				}
			}
		}
		if(gridId == "gridListSub") {
			// 가용수량 체크
			if(colName == "AQTADJU") {
				var USEQTY  = Number(gridList.getColData(gridId, rowNum, "USEQTY"));
				var AQTADJU = Number(gridList.getColData(gridId, rowNum, "AQTADJU"));
				
				if (USEQTY == 0) {
					gridList.setColValue("gridListSub", rowNum, "AQTADJU", 0);
					gridList.setColFocus(gridId, rowNum, colName);
					return;
				}
				
				if (AQTADJU <= 0) {
					commonUtil.msgBox("TASK_M0021", '0'); // 작업수량은 {0}보다 커야 됩니다.
					gridList.setColValue("gridListSub", rowNum, "AQTADJU", USEQTY);
					gridList.setColFocus(gridId, rowNum, colName);
					return;
				}
				
				if (AQTADJU > USEQTY) {
					commonUtil.msgBox("VALID_M0923"); // 조정수량은 가용수량보다 클 수 없습니다.
					gridList.setColValue("gridListSub", rowNum, "AQTADJU", USEQTY);
					gridList.setColFocus(gridId, rowNum, colName);
					return;
				}
			}
		}
	}
	
	function saveData(){
		if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
			return;
		}
		
		if(gridList.validationCheck("gridListSub", "select")){
			
			var head = gridList.getRowData("gridList", 0);
			var list = gridList.getSelectModifyList("gridListSub");
			
			if(list.length == 0){
				commonUtil.msgBox("INV_M0055"); // 기존값과 동일합니다. 확인해주세요.
				return;
			}
			
			var param = new DataMap();
			param.put("LANGKY", "<%=langky%>");
			param.put("head", head);
			param.put("list", list);
			
			var json = netUtil.sendData({
				url : "/wms/inventory/json/saveStock.data",
				param : param
			});
			
			if(json && json.data){
				var sapResList = json.data.sapResList;
				var successCnt = json.data.successCnt;
				
				if (sapResList && sapResList.length > 0) {
					for (var i in sapResList) {
						var SAPRES = sapResList[i].SAPRES;
						var ERRTXT = sapResList[i].ERRTXT;
						if (SAPRES != 'SUCCESS' || ERRTXT) {
							var msg = ERRTXT ? ERRTXT : SAPRES;
							commonUtil.msgBox(successCnt + "건 성공 후 에러 발생\n : " + msg);
							searchList();
							return;
						}
					}
				}
				
				var SADJKY = json.data.SADJKY;
				
				searchType = false;
				
				var paramH = new DataMap();
				paramH.put("SADJKY", SADJKY);
				paramH.put("DOCUTY", head.get('ADJUCA'));
				
				gridList.gridList({
					id : "gridList",
					command : "ADJDH",
					param : paramH
				});
				
				gridList.gridList({
					id : "gridListSub",
					command : "ADJDI",
					param : paramH
				});
				
				gridList.setReadOnly("gridList", true);
				gridList.setReadOnly("gridListSub", true);
				uiList.setActive("Save", false);
				uiList.setActive("Reflect", false);
			} else {
				uiList.setActive("Save", true);
				uiList.setActive("Reflect", true);
			}
		}
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType){
		if(searchCode == "SHSKUMA"){ // 품목코드
			var param = new DataMap();
			param.put("WAREKY", "<%=wareky%>");
			param.put("OWNRKY", "<%=ownrky%>");
			return param;
		}else if(searchCode == "SHCMCDV"){ // 재고상태
			var param = new DataMap();
			param.put("CMCDKY", "LOTA06");
			return param;
		}else if(searchCode == "SHLOCMA_PT") { // 로케이션
			var param = dataBind.paramData("searchArea");
			var plntkyArr = $('select[name="S.LOTA02"]').val();
			plntkyArr = plntkyArr ? plntkyArr : [];
			plntkyArr.push(' ');
			
			param.put("PLNTKY", plntkyArr);
			return param;
		} else if(searchCode == "SHSTLMAP") { // 저장위치
			var param = new DataMap();
			var PLNTKY = $('select[name="LOTA02"]').val();
			if(!PLNTKY) {
				var label = commonLabel.getLabel("STD_LOTA02", 1);
				commonUtil.msgBox("VALID_required",[label]); // {0} 은(는) 필수 입력항목입니다.
				return false;
			}
			param.put("PLNTKY", PLNTKY);
			return param;
		}
	}
	
	function comboEventDataBindeBefore(comboAtt){
		var param = new DataMap();
		if(comboAtt == "airfw,RSNADJ"){
			param.put("DOCUTY", "422");
		} else if(comboAtt == "Wms,DOCTCOMBO"){
			param.put("DOCUTY", "422");
		} else if(comboAtt == "Wms,PLNTKYCOMBO"){
			param.put("WAREKYS", "'<%=wareky%>'");
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
		}
		return param;
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
					<input type="button" CB="Save SAVE STD_SAVE" />
					<!-- <input type="button" CB="GetVariant GETVARIANT BTN_GETVARIANT" />
					<input type="button" CB="SaveVariant SAVEVARIANT BTN_SAVEVARIANT" /> -->
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_WAREKY">거점</dt>
						<dd>
							<input type="text" class="input" name="WAREKY" value="<%=wareky%>" readonly="readonly" validate="required" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA02">플랜트</dt>
						<dd>
							<select name="S.LOTA02" class="input" Combo="Wms,PLNTKYCOMBO" ComboType="MS" validate="required">
							</select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOTA01">저장위치</dt>
						<dd>
							<input type="text" class="input" name="S.LOTA01" UIInput="R,SHSTLMAP" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_DOCUTY">조정문서유형</dt>
						<dd>
							<select class="input" Combo="Wms,DOCTCOMBO" name="DOCUTY" validate="required"></select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_SKUKEY">품목코드</dt>
						<dd>
							<input type="text" class="input" name="S.SKUKEY" UIInput="R,SHSKUMA" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_AREAKY">영역</dt>
						<dd>
							<input type="text" class="input" name="S.AREAKY" UIInput="R,SHAREMA" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_ZONEKY">구역</dt>
						<dd>
							<input type="text" class="input" name="S.ZONEKY" UIInput="R,SHZONMA" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOCAKY">로케이션</dt>
						<dd>
							<input type="text" class="input" name="S.LOCAKY" UIInput="R,SHLOCMA_PT" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_DESC01">품목명</dt>
						<dd>
							<input type="text" class="input" name="DESC01"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_SKUG01">품목유형</dt>
						<dd>
							<select class="input" name="SKUG01" Combo="WmsInventory,SKUG01COMBO"><option value=""/></select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_SKUL01">품목구분</dt>
						<dd>
							<select class="input" name="SKUL01" Combo="WmsInventory,SKUL01COMBO">
								<option></option>
							</select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA06">재고상태</dt>
						<dd>
							<select name="S.LOTA06" class="input" CommonCombo="LOTA06" ComboType="MS"></select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA11">제조일자</dt>
						<dd>
							<input type="text" class="input" name="S.LOTA11" UIInput="R" UIFormat="C" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA13">유통기한</dt>
						<dd>
							<input type="text" class="input" name="S.LOTA13" UIInput="R" UIFormat="C" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA12">입고일자</dt>
						<dd>
							<input type="text" class="input" name="S.LOTA12" UIInput="R" UIFormat="C" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOTA14">제조LOT</dt>
						<dd>
							<input type="text" class="input" name="S.LOTA14" UIInput="R" />
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
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40"					GCol="rownum">1</td>
										<td GH="100 STD_SADJKY"		GCol="text,SADJKY" ></td>
										<td GH="100 STD_DOCDAT"		GCol="input,DOCDAT"	GF="C 8" validate="required"></td>
										<td GH="100 STD_DOCCAT"		GCol="text,DOCCAT" ></td>
										<td GH="100 STD_DOCCATNM"	GCol="text,DOCCATNM" ></td>
										<td GH='100 STD_DOCUTY'		GCol="text,ADJUCA"></td>
										<td GH='100 STD_DOCUTYNM'	GCol="text,ADJUCANM"></td>
										<td GH="100 STD_DOCTXT"		GCol="input,DOCTXT"	GF="S 1000" ></td>
										<td GH="100 STD_WAREKY"		GCol="text,WAREKY" ></td>
										<td GH="100 STD_WAREKYNM"	GCol="text,WAREKYNM" ></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="excel"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
			</div>
			
			<div class="content_layout tabs bottom_layout">
				<ul class="tab tab_style02">
					<li><a href="#tab1-1"><span CL="STD_DETAILLIST">상세리스트</span></a></li>
					<li class="btn_zoom_wrap">
						<ul>
							<!-- <li><button class="btn btn_smaller"><span>축소</span></button></li> -->
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
						
					</li>
				</ul>
				<div class="search_sub">
					<p>
						<span CL="STD_ADJDES">조정사유</span>
						<select class="input" id="bundleModify2" Combo="airfw,RSNADJ"></select>
						<input type="button" CB="Reflect2 REFLECT BTN_REFLECT" />&nbsp;&nbsp;
							
						<span CL="STD_ADJRSN">조정상세사유</span>
						<input  class="input" id="bundleModify3" type="text" style="border:1px solid #ddd;"/>
						<input type="button" CB="Reflect3 REFLECT BTN_REFLECT" />&nbsp;&nbsp;
					</p>
				</div>
				<div class="table_box section" id="tab1-1" style="height: calc(100% - 90px);">
					
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridListSub">
									<tr CGRow="true">
										<td GH="40"					GCol="rownum"							>1</td>
										<td GH="40"					GCol="rowCheck"							>V</td>
										<td GH="100 STD_STOKKY"		GCol="text,STOKKY"						>재고키</td>
										<td GH="100 STD_LOCAKY"		GCol="text,LOCAKY"						>지번</td>
										<td GH="100 STD_SKUL01"		GCol="text,SKUL01NM"					>품목구분</td>
										<td GH="100 STD_SKUKEY"		GCol="text,SKUKEY" 						>품목코드</td>
										<td GH="100 STD_DESC01"		GCol="text,DESC01" 						>품목명</td>
										<td GH="100 STD_QTSIWH"		GCol="text,QTSIWH"		GF="N 20,3"		>재고수량</td>
										<td GH="100 STD_USEQTY"		GCol="text,USEQTY"		GF="N 20,3"		>가용수량</td>
										<td GH="100 STD_QTADJU"		GCol="input,AQTADJU" 	GF="N 20,3"	validate="max(GRID_COL_AQTADJU_*),INV_M0024">조정수량</td>
										<td GH="100 STD_UOMKEY"		GCol="text,UOMKEY"						>단위</td>
										<td GH="100 STD_LOTA06"		GCol="text,LOTA06NM"					>재고상태</td>
										<td GH="100 STD_LOTA02"		GCol="text,LOTA02"						>플랜트</td>
										<td GH="100 STD_LOTA03"		GCol="input,ALOTA03"					>포장타입(PS)</td>
										<td GH="100 STD_LOTA04"		GCol="input,ALOTA04"					>호기</td>
										<td GH="100 STD_LOTA08"		GCol="input,ALOTA08,SHVNATNKY"			>국가코드</td>
										<td GH="100 STD_LOTA10"		GCol="input,ALOTA10"					>보류사유명</td>
										<td GH="100 STD_LOTA11"		GCol="input,ALOTA11"	GF="C"			>제조일자</td>
										<td GH="100 STD_LOTA12"		GCol="input,ALOTA12"	GF="C"			>입고일자</td>
										<td GH="100 STD_LOTA13"		GCol="input,ALOTA13"	GF="C"			>유통기한</td>
										<td GH="100 STD_LOTA14"		GCol="input,ALOTA14"					>개별바코드</td>
										<td GH="100 STD_LOTA15"		GCol="text,LOTA15"						>LOT번호</td>
										<td GH="100 STD_RSNADJ"		GCol="select,ARSNADJ"	validate="required">
											<!-- 조정사유코드 -->
											<select Combo="airfw,RSNADJ"><option></option></select>
										</td>
										<td GH="100 STD_ADJRSN"		GCol="input,AADJRSN"	GF="S 255"		>조정상세사유</td>
										<td GH="100 STD_TOTWG"		Gcol="text,TOTWG"		GF="N 20,3"		>총중량</td>
										<td GH="100 STD_BAGQT"		Gcol="text,BAGQT"		GF="N 20,3"		>EA/BAG</td>
										<td GH="100 STD_BOXQT"		Gcol="text,BOXQT"		GF="N 20,3"		>BAG/BOX</td>
										<td GH="100 STD_PLTQT"		Gcol="text,PLTQT"		GF="N 20,3"		>BOX/Pallet</td>
										<td GH="100 STD_SOKQT"		Gcol="text,SOKQT"		GF="N 20,3"		>속(개/BALE)</td>
										<td GH="100 STD_EAQTY"		Gcol="text,EAQTY"		GF="N 20,3"		>매(sheet/속)</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="excel"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- //content -->
<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>