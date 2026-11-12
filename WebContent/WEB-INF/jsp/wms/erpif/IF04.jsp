<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList1",
			editable : true,
			module : "Erpif",
			command : "IFSAPLOG"
		});

		// 조회
		searchList();
	});
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			// 조회
			searchList();
		}
	}
		
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeDataParam("searchArea");

			gridList.gridList({
				id : "gridList1",
				param : param
			});
		}
		
		$("#tab1").trigger('click'); 
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
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_CDATE">처리일자</dt>
						<dd>
							<input type="text" name="CREDAT" UIInput="R" UIFormat="C -1" validate="required(STD_CDATE)"/>
						</dd>
					</dl>
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
			</div>
		</div>
		<div class="search_next_wrap">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
					<li><a href="#tabs1-1" CL='STD_LIST'><span>리스트</span></a></li>
				</ul>
				<div class="table_box section" id="tabs1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList1">
									<tr CGRow="true">
										<td GH="40" GCol="rownum"></td>
										<td GH="60 STD_SEQNO" GCol="text,SEQNO">순번</td>
										<td GH="60 STD_MENUID" GCol="text,MENUID">MENUID</td>
										<td GH="100 STD_MENUNAME" GCol="text,MENUNAME">MENUNAME</td>
										<td GH="150 STD_WMSDNO" GCol="text,ZDOCNO">ZDOCNO</td>
										<td GH="800 STD_ERRTXT" GCol="text,ERRTXT">ERRTXT</td>
										<td GH="100 STD_WAREKY" GCol="text,WAREKY">WAREKY</td>
										<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="C">생성일자</td>
										<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
										<td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자</td>
										<td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자명</td>
										<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="C">수정일자</td>
										<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
										<td GH="100 STD_LMOUSR" GCol="text,LMOUSR">수정자</td>
										<td GH="100 STD_LUSRNM" GCol="text,LUSRNM">수정자명</td>
										
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
<!-- 						<button type='button' GBtn="add"></button> -->
<!-- 						<button type='button' GBtn="copy"></button> -->
<!-- 						<button type='button' GBtn="delete"></button> -->
<!-- 						<button type='button' GBtn="total"></button> -->
<!-- 						<button type='button' GBtn="layout"></button> -->
						<button type='button' GBtn="excel"></button>
<!-- 						<button type='button' GBtn="excelUpload"></button> -->
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