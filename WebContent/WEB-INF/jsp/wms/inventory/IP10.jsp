<%@page import="project.common.airfw.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<%@ include file="/common/include/webdek/layout.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		gridList.setGrid({
			id       : "gridList",
			module   : "WmsInventory",
			command  : "IP10",
			editable : false,
		});
		
		initSearchArea();
	});
	
	function initSearchArea() {
		<%if(plant.equals("")){%>
			$('select[name="S.LOTA02"]').multipleSelect('checkAll');
		<%}else{%>
			$('select[name="S.LOTA02"]').multipleSelect('setSelects', ['<%=plant%>']);
		<%}%>
	}
	
	function commonBtnClick(btnName){
		if (btnName == "Search"){
			searchList();
		}
	}
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			param.put("WAREKY", "<%=wareky%>");
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			param.put("PROGID", "<%=menuId%>");
			
			gridList.gridList({
				id : "gridList",
				param : param
			});
			
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
		}else if(searchCode == "SHLOCMA_PT"){ // 로케이션
			var param = dataBind.paramData("searchArea");
			var plntkyArr = $('select[name="S.LOTA02"]').val();
			plntkyArr = plntkyArr ? plntkyArr : [];
			plntkyArr.push(' ');
			
			param.put("PLNTKY", plntkyArr);
			return param;
		} else if(searchCode == "SHSTLMAP") { // 저장위치
			var param = new DataMap();
			var PLNTKY = $('select[name="S.LOTA02"]').val();
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
		if(comboAtt == "Wms,PLNTKYCOMBO"){
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
				<div class="fl_l"></div>
				<div class="fl_r">
					<input type="button" CB="Search SEARCH BTN_SEARCH" />
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap">
					<dl>
						<dt CL="STD_WAREKY">거점</dt>
						<dd><input type="text" class="input" name="WAREKY" value="<%=wareky%>" readonly="readonly" validate="required" /></dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA02">플랜트</dt>
						<dd><select name="S.LOTA02" class="input" Combo="Wms,PLNTKYCOMBO" ComboType="MS" validate="required"></select></dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA01">저장위치</dt>
						<dd><input type="text" class="input" name="S.LOTA01" UIInput="R,SHSTLMAP" /></dd>
					</dl>
					<dl>
						<dt CL="STD_SKUKEY">품목코드</dt>
						<dd><input type="text" class="input" name="S.SKUKEY" UIInput="R,SHSKUMA" /></dd>
					</dl>
					<dl>
						<dt CL="STD_AREAKY">영역</dt>
						<dd><input type="text" class="input" name="S.AREAKY" UIInput="R,SHAREMA" /></dd>
					</dl>
					<dl>
						<dt CL="STD_ZONEKY">구역</dt>
						<dd><input type="text" class="input" name="S.ZONEKY" UIInput="R,SHZONMA" /></dd>
					</dl>
					<dl>
						<dt CL="STD_LOCAKY">로케이션</dt>
						<dd><input type="text" class="input" name="S.LOCAKY" UIInput="R,SHLOCMA_PT" /></dd>
					</dl>
					<dl>
						<dt CL="STD_DESC01">품목명</dt>
						<dd><input type="text" class="input" name="DESC01"/></dd>
					</dl>
					<dl>
						<dt CL="STD_SKUG01">품목유형</dt>
						<dd><select class="input" name="SKUG01" Combo="WmsInventory,SKUG01COMBO"><option value=""/></select></dd>
					</dl>
					<dl>
						<dt CL="STD_SKUL01">품목구분</dt>
						<dd><select class="input" name="SKUL01" Combo="WmsInventory,SKUL01COMBO"><option value=""/></select></dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA06">재고상태</dt>
						<dd><select name="S.LOTA06" class="input" CommonCombo="LOTA06" ComboType="MS"></select></dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA11">제조일자</dt>
						<dd><input type="text" class="input" name="S.LOTA11" UIInput="R" UIFormat="C" /></dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA13">유통기한</dt>
						<dd><input type="text" class="input" name="S.LOTA13" UIInput="R" UIFormat="C" /></dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA12">입고일자</dt>
						<dd><input type="text" class="input" name="S.LOTA12" UIInput="R" UIFormat="C" /></dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA14">제조LOT</dt>
						<dd><input type="text" class="input" name="S.LOTA14" UIInput="R" /></dd>
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
										<td GH="40"				GCol="rownum"					>1		</td>
										<td GH="100 STD_WMSAMT"	GCol="text,WMSAMT"	GF="N 20,3"	>WMSAMT	</td>
										<td GH="100 STD_SAPAMT"	GCol="text,SAPAMT"	GF="N 20,3"	>SAPAMT	</td>
										<td GH="100 STD_DIFAMT"	GCol="text,DIFAMT"	GF="N 20,3"	>DIFAMT	</td>
										<td GH="100 X1"			GCol="text,X1"					>X1 	</td>
										<td GH="100 X2"			GCol="text,X2"					>X2 	</td>
										<td GH="100 X3"			GCol="text,X3"					>X3 	</td>
										<td GH="100 X4"			GCol="text,X4"					>X4 	</td>
										<td GH="100 X5"			GCol="text,X5"					>X5 	</td>
										<td GH="100 X6"			GCol="text,X6"					>X6 	</td>
										<td GH="100 X7"			GCol="text,X7"					>X7 	</td>
										<td GH="100 X8"			GCol="text,X8"					>X8 	</td>
										<td GH="100 X9"			GCol="text,X9"					>X9 	</td>
										<td GH="100 X10"		GCol="text,X10"					>X10	</td>
										<td GH="100 X11"		GCol="text,X11"					>X11	</td>
										<td GH="100 X12"		GCol="text,X12"					>X12	</td>
										<td GH="100 X13"		GCol="text,X13"					>X13	</td>
										<td GH="100 X14"		GCol="text,X14"					>X14	</td>
										<td GH="100 X15"		GCol="text,X15"					>X15	</td>
										<td GH="100 X16"		GCol="text,X16"					>X16	</td>
										<td GH="100 X17"		GCol="text,X17"					>X17	</td>
										<td GH="100 X18"		GCol="text,X18"					>X18	</td>
										<td GH="100 X19"		GCol="text,X19"					>X19	</td>
										<td GH="100 X20"		GCol="text,X20"					>X20	</td>
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