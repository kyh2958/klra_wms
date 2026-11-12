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
			name : "gridList",
			editable : true,
			pkcol : "PLNTKY",
			module : "WmsAdmin",
			command : "ZPPTM",
		});
		
		// 플랜트
		var $selPLNTKY = $('select[name="LOTA02"]');
		$selPLNTKY.multipleSelect('checkAll');
	});
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			gridList.gridList({
				id : "gridList",
				param : param
			});
		}
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}
	}
	
	function comboEventDataBindeBefore(comboAtt){
		if(comboAtt == "Wms,PLNTKYCOMBO"){
			var param = inputList.setRangeParam("searchArea");
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
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_LOTA02" style="vertical-align:middle">플랜트</dt>
						<dd>
                            <select CommonCombo="LOTA02" class="input" name="LOTA02" ComboCodeView=false ComboType="MS" style="width: 160px" >
                            </select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOTA16">평량</dt>
						<dd>
							<input type="text" class="input" name="LOTA16" UIInput="R" IAname="Search" />
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
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH='40 STD_NUMBER'	GCol="rownum"				>1</td>
										<td GH="100 STD_PLNTKY" GCol="text,LOTA02">플랜트</td>
										<td GH="110 STD_PLNTNM" GCol="text,PLNTNM">플랜트명</td>
										<td GH="70 STD_LOTA16" GCol="text,LOTA16">평량</td>
										<td GH="90 STD_LOTA17" GCol="text,LOTA17">컨테이너 유형</td>
										<td GH="100 STD_LOTA03" GCol="text,LOTA03">포장타입</td>
										<td GH="90 STD_LOTA09" GCol="text,LOTA09">MTO납품처코드</td>
										<td GH="150 STD_PALCNT" GCol="text,PALCNT">평량별Ream(팔레트수)</td>
										<td GH='100 STD_CREDAT'	GCol="text,CREDAT" GF="D"	>생성일자</td>
										<td GH='100 STD_CRETIM'	GCol="text,CRETIM" GF="T"	>생성시간</td>
										<td GH='100 STD_CREUSR'	GCol="text,CREUSR"			>생성자</td>
										<td GH='100 STD_CUSRNM'	GCol="text,CUSRNM"			>생성자명</td>
										<td GH='100 STD_LMODAT'	GCol="text,LMODAT" GF="D"	>수정일자</td>
										<td GH='100 STD_LMOTIM'	GCol="text,LMOTIM" GF="T"	>수정시간</td>
										<td GH='100 STD_LMOUSR'	GCol="text,LMOUSR"			>수정자</td>
										<td GH='100 STD_LUSRNM'	GCol="text,LUSRNM"			>수정자명</td>
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