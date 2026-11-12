<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<style type="text/css">
 .content_wrap{background: none;}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			editable : true,
			pkcol : "WAREKY",
			module : "WmsAdmin",
			command : "WAHMA"
		});
		searchList();
	});
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}
	}
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			gridList.gridList({
				id : "gridList",
				param : param
			});
		}
	}
	
	function saveData(){
		var param = dataBind.paramData("searchArea");
		
		var json = gridList.gridSave({
			id : "gridList",
			param : param
		});
		
		if(json && json.data){
			searchList();
		}
	}
	
	function gridListEventRowAddBefore(gridId, rowNum){
		var newData = new DataMap();
		newData.put("COMPKY", "<%=compky%>");
		
		return newData;
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType){
		if(searchCode == "SHCOMMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHWAHMA"){
			return dataBind.paramData("searchArea");
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
				<div class="fl_r">
					<input type="button" CB="Search SEARCH BTN_DISPLAY" />
					<input type="button" CB="Save SAVE STD_SAVE" />
				</div>
			</div>
		</div>
		<div class="search_next_wrap noSearch">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
					<li><a href="#tabs1-1" CL='STD_LIST'><span>리스트</span></a></li>  
				</ul>
				<div class="table_box section" id="tabs1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40"					GCol="rownum">1</td>
										<td GH="100 STD_COMPKY"		GCol="text,COMPKY">회사</td>
										<td GH="100 STD_WAREKY"		GCol="input,WAREKY">거점</td>
										<td GH="100 STD_NAME01"		GCol="input,NAME01"	GF="S 180">거점명</td>
										<td GH="100 STD_DELMAK"		GCol="check,DELMAK">삭제</td>
										<td GH="100 STD_NAME02"		GCol="input,NAME02"	GF="S 180">대표자이름</td>
										<td GH="100 STD_ADDR01"		GCol="input,ADDR01"	GF="S 180">대표주소</td>
										<td GH="100 STD_POSTCD"		GCol="input,POSTCD"	GF="S 30">우편번호</td>
										<td GH="100 STD_NATNKY"		GCol="select,NATNKY"><select CommonCombo="NATNKY"></select>국가</td>
										<td GH="100 STD_VATREG"		GCol="input,VATREG"	GF="S 20">사업자등록번호</td>
										<td GH="100 STD_FAXTL1"		GCol="input,FAXTL1"	GF="S 20">팩스번호</td>
										<td GH="100 STD_TELN01"		GCol="input,TELN01"	GF="S 20"  validate="tel(GRID_COL_TELN01_*)">전화번호</td>
<!--  									<td GH="100 STD_TELN01"		GCol="input,TELN01"	GF="S 20"/>전화번호</td> -->
										<td GH="100 STD_WADN01"		GCol="input,WADN01"	GF="S 60">관리자명</td>
										<td GH="100 STD_WADT01"		GCol="input,WADT01"	GF="S 60">관리자연락처</td>
										<td GH="100 STD_WADM01"		GCol="input,WADM01"	GF="S 60"  validate="email(GRID_COL_WADM01_*)">e-Mail</td>
										<td GH="100 STD_CREDAT"		GCol="text,CREDAT"	GF="D">생성일자</td>
										<td GH="100 STD_CRETIM"		GCol="text,CRETIM"	GF="T">생성시간</td>
										<td GH="100 STD_CREUSR"		GCol="text,CREUSR">생성자ID</td>
										<td GH="100 STD_CUSRNM"		GCol="text,CUSRNM">생성자명</td>
										<td GH="100 STD_LMODAT"		GCol="text,LMODAT"	GF="D">수정일자</td>
										<td GH="100 STD_LMOTIM"		GCol="text,LMOTIM"	GF="T">수정시간</td>
										<td GH="100 STD_LMOUSR"		GCol="text,LMOUSR">수정자ID</td>
										<td GH="100 STD_LUSRNM"		GCol="text,LUSRNM">수정자명</td>									
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
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