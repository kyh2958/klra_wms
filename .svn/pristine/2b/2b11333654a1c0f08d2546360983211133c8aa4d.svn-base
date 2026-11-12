<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ page import="project.common.airfw.*"%>
<%@ include file="/common/include/webdek/head.jsp"%>
<%
	User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%>
<script type="text/javascript">
	$(document).ready(function(){
		gridList.setGrid({
	    	id : "gridList",
			module : "WmsInbound",
			command : "GR12"
	    });

		// 플랜트
		var $selPLNTKY = $('select[name="PN.PLNTKY"]');
		$selPLNTKY.multipleSelect('checkAll');
		
		//gridList.setReadOnly("gridList", true, ['INDRCN']);
	});
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}
	}
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			
			//console.log(JSON.stringify(param));
			gridList.gridList({
		    	id : "gridList",
		    	param : param
		    });  
		}
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType, $inputObj){
		var param = new DataMap();
		
		if(searchCode == "SHSTLMA"){
			param.put("OWNRKY", "<%=ownrky%>");
			return param;
		}else if(searchCode == "SHSKUMA"){
			param.put("OWNRKY", "<%=ownrky%>");
			return param;
		}else{
			param.put("OWNRKY", "<%=ownrky%>");
			return param;
		}
	}
	
	function comboEventDataBindeBefore(comboAtt){
		if(comboAtt == "WmsInbound,PLNTKYCOMBO"){
			var param = new DataMap();
			
			var WAREKY_LIST = ['<%= wareky%>'];
			param.put("WAREKY_LIST", WAREKY_LIST);
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			
			return param;
		}
	}
</script>
</head>
<body>
<div class="contentHeader">
	<div class="util">
		<button CB="Search SEARCH BTN_DISPLAY">
		</button>
	</div>
	<div class="util2">
		<button class="button type2" id="showPop" type="button"><img src="/common/images/ico_btn4.png" alt="List" /></button>
	</div>
</div>

<!-- searchPop -->
<div class="searchPop" id="searchArea"style="overflow-y:scroll;">
	<button type="button" class="closer">X</button>
	<div class="searchInnerContainer">
	<p class="util">
		<button CB="Search SEARCH BTN_DISPLAY"></button>
		<button CB="GetVariant GETVARIANT BTN_GETVARIANT"></button>
			<button CB="SaveVariant SAVEVARIANT BTN_SAVEVARIANT"></button>
	</p>
		<div class="searchInBox">
			<h2 class="tit" CL="STD_SELECTOPTIONS">검색조건</h2>
			<table class="table type1">
				<colgroup>
					<col width="100" />
					<col />
				</colgroup>
				<tbody>
					<tr>
						<th CL="STD_WAREKY"></th>
						<td>
							<input type="text" name="WAREKY" name="WAREKY" value="<%=wareky%>" readonly="readonly" validate="required"/>
						</td>
					</tr>
					<tr style="display:none">
						<th CL="STD_OWNRKY">화주</th>
						<td>
							<select Combo="WmsOrder,OWNRKYCOMBO" name="PN.OWNRKY" id="OWNRKY" STYLE="width: 150px;">
							</select>
						</td>
					</tr>
					
					<tr>
						<th CL="STD_LOTA02" style="vertical-align:middle">플랜트</th>
						<td >
							<select Combo="WmsInbound,PLNTKYCOMBO" name="PN.PLNTKY" id="PLNTKY" ComboType="MS" style="width: 150px;" validate="required">
							</select>
						</td>
					</tr>
					<tr>
						<th CL="STD_PROPLNDT">생산계획일</th>
						<td>
							<input type="text" name="PN.PRDDTS" UIInput="R" UIFormat="C N" validate="required(STD_PROPLNDT)"/>
						</td>
					</tr>
					
					<tr>
						<th CL="STD_LOTA04" style="vertical-align:middle">호기</th>
						<td >
							<select CommonCombo="UNITNO" name="UNITNO" ComboCodeView=false  style="width: 200px" >
							<option value=""></option>
							</select>
						</td>
					</tr>
					
				</tbody>
			</table>
		</div>
		
		<div class="searchInBox">
			<h2 class="tit type1" CL="STD_SKUINFO">상품정보</h2>
			<table class="table type1">
				<colgroup>
					<col width="100" />
					<col />
				</colgroup>
				<tbody>
					
					<tr>
						<th CL="STD_SKUL01" style="vertical-align:middle">품목구분</th>
						<td >
							<select CommonCombo="SKUL01" name="SKUL01" ComboCodeView=false  style="width: 200px" >
							<option value=""></option>
							</select>
						</td>
					</tr>
					
					<tr>
						<th CL="STD_SKUKEY">품목코드</th>
						<td>
							<input type="text" name="PN.SKUKEY" UIInput="R,SHSKUMA" />
						</td>
					</tr>
					<tr>
						<th CL="STD_DESC01">품목명</th>
						<td>
							<input type="text" name="SA.DESC01" UIInput="R" />
						</td>
					</tr>
					
				</tbody>
			</table>
		</div>
			
	</div>
</div>
<!-- //searchPop -->

<div class="content">
	<div class="innerContainer">
		<!-- contentContainer -->
		<div class="contentContainer">
			<div class="bottomSect type1">
				<div class="tabs"  id="bottomTabs">
					<ul class="tab type2">
						<li><a href="#tabs1-1" id="tab01"><span CL='STD_PROPLN,3'>탭메뉴1</span></a></li>
					</ul>
					
					<div id="tabs1-1">
						<div class="section type1">
							<div class="table type2">
								<div class="tableBody">
									<table>
										<tbody id="gridList">
											<tr CGRow="true">
												<td GH="40" GCol="rownum">번호</td>
												<td GH="60 STD_PLNTKY" GCol="text,PLNTKY">플랜트</td>
												<td GH="100 STD_PLNTNM" GCol="text,PLNTNM">플랜트</td>
												<td GH="120 STD_LOTA04" GCol="text,UNITNONM">호기</td>
<!-- 												<td GH="150 STD_SKUG01" GCol="text,SKUG01NM">품목유형</td> -->
												<td GH="100 STD_SKUL01" GCol="text,SKUL01NM">품목구분</td>
												<td GH="130 STD_SKUKEY" GCol="text,SKUKEY">품목코드</td>
												<td GH="230 STD_DESC01" GCol="text,DESC01">품목명</td>
												<td GH="80 STD_QTY" GCol="text,QTYPRD" GF="N 20,3">수량</td>
												<td GH="80 STD_UOMKEY" GCol="text,UOMKEY" GF="S 3"/>단위</td>
												
												<td GH="100 STD_PRDDTS" GCol="text,PRDDTS" GF="D">생산시작일</td>
												<td GH="100 STD_PRDDTE" GCol="text,PRDDTE" GF="D">생산종료일</td>
												<td GH="200 STD_REMAKS" GCol="text,REMARK">비고</td>
												
												<td GH="100 STD_TOTWG" GCol="text,TOT" GF="N 20,3">총중량</td>
												<td GH="100 STD_BAGQT" GCol="text,BAG" GF="N 20,3">EA/BAG</td>
												<td GH="100 STD_BOXQT" GCol="text,BOX" GF="N 20,3">BAG/BOX</td>
												<td GH="100 STD_PLTQT" GCol="text,PLT" GF="N 20,3">BOX/Pallet</td>
												<td GH="100 STD_SOKQT" GCol="text,SOK" GF="N 20,3">속(개/BALE)</td>
												<td GH="100 STD_EAQTY" GCol="text,EA" GF="N 20,3">매(sheet/속)</td>
												
												<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D">생성일자</td>
												<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
												<td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자명</td>
												<td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자ID</td>
												<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D">수정일자</td>
												<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
												<td GH="100 STD_LMOUSR" GCol="text,LMOUSR">수정자명</td>
												<td GH="100 STD_LUSRNM" GCol="text,LUSRNM">수정자ID</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div class="tableUtil">
								<div class="leftArea">
									<button type="button" GBtn="find"></button>
									<button type="button" GBtn="sortReset"></button>
									<button type="button" GBtn="layout"></button>
									<button type="button" GBtn="total"></button>
									<button type="button" GBtn="excel"></button>
								</div>
								<div class="rightArea">
									<p class="record" GInfoArea="true"></p>
								</div>
							</div>
						</div>
					</div>
					
			</div>
		</div>
		<!-- //contentContainer -->
	</div>
</div>
<!-- //content -->
<%@ include file="/common/include/webdek/bottom.jsp"%>
</body>
</html>