<%@page import="project.common.airfw.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<%
	User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
   /*  String menuky = (String)request.getSession().getAttribute(CommonConfig.SES_MENU_GROUP_KEY); */
%>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<script type="text/javascript">
	var searchFlag = 1;
	var tabPage = 1;
	var paramH = new DataMap();
	<%-- var menuky = '<%=menukey%>' --%>
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList4",
			module : "WmsInventory",
			command : "SD08",
		});	
		
		initSearchArea();
		
	});
	
	function initSearchArea() {
		<%if(plant.equals("")){%>
			$('select[name="RI.LOTA02"]').multipleSelect('checkAll');
		<%}else{%>
			$('select[name="RI.LOTA02"]').multipleSelect('setSelects', ['<%=plant%>']);
		<%}%>
		$('select[name="S.WAREKY"]').multipleSelect('setSelects', ['<%=wareky%>']);
		$('select[name="SKUL01"]').val("");
	}
	
   function searchList(){
	      if(validate.check("searchArea")){
	         /* console.log(menuky); */
	         var param = inputList.setRangeParam("searchArea");
	         param.put("LANGKY", "<%=langky%>");
	         param.put("WAREKY", "<%=wareky%>");
	         param.put("OWNRKY", "<%=ownrky%>");
	         
	         param.put("PROGID", configData.MENU_ID);
	         
	         gridList.resetGrid("gridList4");
	         
	            gridList.gridList({
	               id : "gridList4",
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
		var param = new DataMap();
		if(comboAtt == "Wms,PLNTKYCOMBO"){
<%-- 			param.put("WAREKYS", "'<%=wareky%>'"); --%>
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
					<input type="button" CB="Search SEARCH BTN_SEARCH" />
					<!-- <input type="button" CB="Print PRINT BTN_IBLBPRINT" />
					<input type="button" CB="IPrint PRINT BTN_INLBPRINT" /> -->
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<input type="hidden" class="input" name="OWNRKY" value="<%=ownrky %>" />

					<dl>
						<dt CL="STD_LOTA02">플랜트</dt>
						<dd>
							<select name="RI.LOTA02" class="input" Combo="Wms,PLNTKYCOMBO" ComboType="MS" validate="required">
							</select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_DATE">일자</dt>
						<dd>
							<input type="text" class="input" name="DOCDAT" UIInput="U" UIFormat="C 0 N" validate="required"/>
						</dd>
					</dl>
					
					<!-- <dl>
						<dt CL="STD_ASKL03">평량</dt>
						<dd>
							<input type="text" class="input" name="ASKL03"/>
						</dd>
					</dl> -->
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
			</div>
		</div>
		<div class="search_next_wrap">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
<!-- 					<li><a href="#tab1-1" onclick="tabChg(1);"><span CL="STD_BYSKUKEY">탭메뉴1</span></a></li> -->
<!-- 					<li><a href="#tab1-2" onclick="tabChg(2);"><span CL="STD_BYAREAKY">탭메뉴2</span></a></li> -->
<!-- 					<li><a href="#tab1-3" onclick="tabChg(3);"><span CL="STD_BYLOCAKY">탭메뉴3</span></a></li> -->
					<!-- <li><a href="#tab1-4" onclick="tabChg(4);"><span CL="STD_BYLOTNUM">탭메뉴4</span></a></li> -->
					<li class="btn_zoom_wrap">
						<ul>
							<!-- <li><button class="btn btn_smaller"><span>축소</span></button></li> -->
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section" id="tab1-4">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList4">
									<tr CGRow="true">
										<td GH="40 STD_NUMBER" 		GCol="rownum">1</td>
										<td GH="100 STD_GUBUN"		GCol="text,SHORTX"				>구분				</td>
										<td GH="100 STD_UOMKEY"		GCol="text,UOMKEY"				>단위				</td>
										<td GH="100 STD_KPCAPA"		GCol="text,MAXCPC" GF="N 20,0"	>보관CAPA			</td>
										<td GH="100 STD_KPPR"		GCol="text,RATE" GF="N 20,0"	>보관률(%)		</td>
										<td GH="100 STD_TOTAL"		GCol="text,TOT"	GF="N 20,0"		>합계      		</td>
										<td GH="100 STD_RT"			GCol="text,RT"	GF="N 20,3"		>R/T			</td>
										<td GH="100 STD_FT"			GCol="text,FT"	GF="N 20,3"		>F/T			</td>
										<td GH="100 STD_KT"			GCol="text,KT"	GF="N 20,3"		>K/T			</td>
										<td GH="100 STD_WT"			GCol="text,WT"	GF="N 20,3"		>W/T			</td>
										<td GH="100 STD_AFH"		GCol="text,AFH"	GF="N 20,3"		>AFH			</td>
										<td GH="100 STD_SN"			GCol="text,SN"	GF="N 20,3"		>S/N			</td>
										<td GH="100 STD_BD"			GCol="text,BD"	GF="N 20,3"		>B/D			</td>
										<td GH="100 STD_AD"			GCol="text,AD"	GF="N 20,3"		>A/D			</td>
										<td GH="100 STD_WD"			GCol="text,WD"	GF="N 20,3"		>원단				</td>
										<td GH="100 STD_ET"			GCol="text,ET"	GF="N 20,3"		>기타				</td>
										<td GH="100 STD_OF"			GCol="text,FO"	GF="N 20,3"		>O/F			</td>
										<td GH="100 STD_BW"			GCol="text,BW"	GF="N 20,3"		>B/W			</td>
										
										
										<!-- <td GH="40" GCol="rowCheck"></td> -->
										<!-- <td GH="100 STD_ZONEKY"		GCol="text,ZONEKY"				>구역				</td> -->
										<!-- <td GH="100 STD_AREAKY" 	GCol="text,AREAKY"				>영역				</td> -->
										<!-- <td GH="100 STD_AREANM" 	GCol="text,AREANM"				>영역명			</td> -->
<!-- 									<td GH="100 STD_QTSCNU"		GCol="text,QTSCNU"	GF="N 20,3"	>불용수량			</td> Can Not Use -->
<!-- 									<td GH="100 STD_QTSIQT"		GCol="text,QTSIQT"	GF="N 20,3"	>품질수량			</td> Inspection QuanTity -->
<!-- 									<td GH="100 STD_QTSWQT"		GCol="text,QTSWQT"	GF="N 20,3"	>폐기수량			</td> Wasted     QuanTity -->
<!-- 									<td GH='100 STD_QTSHQT'		GCol="text,QTSHQT"	GF="N 20,3"	>보류수량			</td> Hold       QuanTity -->

									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type="button" GBtn="find"></button>
						<button type="button" GBtn="sortReset"></button>
						<button type="button" GBtn="layout"></button>
						<button type="button" GBtn="total"></button>
						<button type="button" GBtn="excel"></button>
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