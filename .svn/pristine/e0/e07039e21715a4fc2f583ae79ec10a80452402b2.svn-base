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
			pkcol : "LOTNUM",
			module : "WmsAdmin",
			command : "LOTAT",
			bindArea : "tabs1-2"
	    });
	});
	
	function searchList() {
		//var param = dataBind.paramData("searchArea");
		if (validate.check("searchArea")) {
			var param = inputList.setRangeParam("searchArea");
			gridList.gridList({
				id : "gridList",
				param : param
			});
		}
	}
	
	function saveData() {
		var param = dataBind.paramData("searchArea");
		var json = gridList.gridSave({
			id : "gridList",
			param : param
			//modifyType : 'A'
		});
		if (json) {
			if (json.data) {
				searchList();
			}
		}
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}/* else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Execute"){
			
		} */
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
						<dt CL="STD_WAREKY">Center Code</dt>
						<dd>
							<input type="text" class="input" name="WAREKY" UIInput="S,SHWAHMA" value="<%=wareky%>" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_SKUKEY">품번코드</dt>
						<dd>
							<input type="text" class="input" name="SKUKEY" UIInput="R,SHSKUMA" IAname="Search" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTNUM">Lot Number</dt>
						<dd>
							<input type="text" class="input" name="LOTNUM" UIInput="R,SHLOTAT" IAname="Search" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA01">배치번호(원산지)</dt>
						<dd>
							<input type="text" class="input" name="LOTA01" UIInput="R" IAname="Search" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA02"></dt>
						<dd>
							<input type="text" class="input" name="LOTA02" UIInput="R" IAname="Search" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA11">생산일자</dt>
						<dd>
							<input type="text" class="input" name="LOTA11" UIInput="R" UIFormat="C" IAname="Search" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA14">개별바코드</dt>
						<dd>
							<input type="text" class="input" name="LOTA14" UIInput="R" IAname="Search" UIFormat="U"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA15">LOT 번호</dt>
						<dd>
							<input type="text" class="input" name="LOTA15" UIInput="R" IAname="Search" UIFormat="U"/>
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
					<li><a href="#tab1-1"><span>리스트</span></a></li> 
					<li style="display:none"><a href="#tab1-2"><span>상세</span></a></li> 
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="120 STD_LOTNUM" GCol="text,LOTNUM">Lot Number</td>
										<td GH="70 STD_WAREKY" GCol="text,WAREKY">Center Code</td>
										<td GH="70 STD_OWNRKY" GCol="text,OWNRKY">Owner Code</td>
										<td GH="120 STD_SKUKEY" GCol="text,SKUKEY">품번코드</td>
										<td GH="100 STD_LOTA01" GCol="text,LOTA01">원산지</td> <!-- S/N번호 SHCMCDV 재고유형 SHCMCDV 문서번호SHCMCDV-->
										<td GH="100 STD_LOTA02" GCol="text,LOTA02">ERP창고</td>
										<td GH="100 STD_LOTA03" GCol="text,LOTA03">임가공업체</td>
										<td GH="100 STD_LOTA04" GCol="text,LOTA04">참조문서번호</td>
										<td GH="100 STD_LOTA05" GCol="text,LOTA05">박스번호</td>
										
										<td GH="100 STD_LOTA06" GCol="text,LOTA06">재고상태</td>
										<td GH="70 STD_LOTA07" GCol="text,LOTA07">발주부서</td>
										<td GH="70 STD_LOTA08" GCol="text,LOTA08">생산업체</td>
										<td GH="70 STD_LOTA09" GCol="text,LOTA09">입고오더번호</td>
										<td GH="70 STD_LOTA10" GCol="text,LOTA10">출하의뢰번호</td>
										<td GH="90 STD_LOTA11" GCol="text,LOTA11" GF="D">생산일자</td>
										<td GH="90 STD_LOTA12" GCol="text,LOTA12" GF="D">입고일자</td>
										<td GH="90 STD_LOTA13" GCol="text,LOTA13" GF="D">포장일자</td>
										<td GH="70 STD_LOTA14" GCol="text,LOTA14">LOT속성14</td>
										<td GH="70 STD_LOTA15" GCol="text,LOTA15">수주번호</td>
										
										<td GH="70 STD_LOTA16" GCol="text,LOTA16">LOT속성16</td>
										<td GH="70 STD_LOTA17" GCol="text,LOTA17">LOT속성17</td>
										<td GH="70 STD_LOTA18" GCol="text,LOTA18">LOT속성18</td>
										<td GH="70 STD_LOTA19" GCol="text,LOTA19">LOT속성19</td>
										<td GH="70 STD_LOTA20" GCol="text,LOTA20">LOT속성20</td>
										
										<td GH="90 STD_CREDAT" GCol="text,CREDAT" GF="D">생성일자</td>
										<td GH="70 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
										<td GH="70 STD_CREUSR" GCol="text,CREUSR">생성자</td>
										<td GH="70 STD_CUSRNM" GCol="text,CUSRNM">생성자명</td>
										<td GH="90 STD_LMODAT" GCol="text,LMODAT" GF="D">수정일자</td>
										<td GH="70 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
										<td GH="70 STD_LMOUSR" GCol="text,LMOUSR">수정자</td>
										<td GH="70 STD_LUSRNM" GCol="text,LUSRNM">수정자명</td>
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
				
				<div class="table_box" id="tab1-2">
					<div class="inner_search_wrap">
						<table class="detail_table">
							<colgroup>
								<col width="7%"/>
								<col width="26%"/>
								<col width="7%"/>
								<col width="25%"/>
								<col width="7%"/>
								<col width=""/>
							</colgroup>
							<tbody>	
								<tr>
									<th CL='STD_LOTA01'>원산지</th>
									<td>
										<input type="text" class="input" name="LOTA01" readonly="readonly"/>
									</td>
									<th CL='STD_LOTA04'>참조문서번호</th>
									<td>
										<input type="text" class="input" name="LOTA04" readonly="readonly"/>
									</td>
								</tr>
								<tr>
									<th CL='STD_LOTA02'>ERP창고</th>
									<td>
										<input type="text" class="input" name="LOTA02"readonly="readonly"/>
									</td>
									<th CL='STD_LOTA11'>생산일자</th>
									<td>
										<input type="text" class="input" name="LOTA11" readonly="readonly"/>
									</td>
								</tr>
								<tr>
									<th CL='STD_LOTA03'>임가공업체</th>
									<td>
										<input type="text" class="input" name="LOTA03" readonly="readonly"/>
									</td>
								</tr>
							
							</tbody>
						</table>
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