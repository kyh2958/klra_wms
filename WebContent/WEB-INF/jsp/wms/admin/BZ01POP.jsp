<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>인터페이스 상세내역</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript">

	window.resizeTo('1220','680');
	
	var iftbl;
	var ifid;
	var bwart;
	var rfcfn;
	var rfctbl;
	var rfcp1;
	var rfcp2;
	var ifstat;
	
	$(document).ready(function(){
		
		var data = page.getLinkPopData();
		var PTNRKY = data.get("PTNRKY");
		
		gridList.setGrid({
	    	id : "gridList",
			editable : true,
			pkcol : "PTNRKY_FROM,PTNRKY_TO",
			module : "WmsAdmin",
			command : "BZPTN_DISTANCE",
			bindArea : "tabs1-2",
			validation : "PTNRKY_FROM,PTNRKY_TO",
			colorType : true
	    });

		if ($.trim(PTNRKY) != '') {
			$("#PTNRKY").val($.trim(PTNRKY))
			searchList(data)
		}
		
	});

	function searchList(param){
		gridList.gridList({
	    	id : "gridList",
	    	param : param
	    });
	}

	function fn_closing(){
		this.close();
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			
			if(validate.check("searchArea")){
				// 납품처코드 필수입력
				var param = inputList.setRangeParam("searchArea");
				searchList(param);
			}
			
		} else if(btnName == "Save"){
			toggleYN();
		} else if(btnName == "ToggleYN"){
			toggleYN();
		} else if(btnName == "Close"){
			fn_closing();
		} 
	}

	function gridListEventRowDblclick(gridId, rowNum){
	}
	
	function gridListEventDataBindEnd(gridId, dataLength){
	}
	
	function gridListRowTextColorChange(gridId, rowNum, rowData){
		if(gridId === "gridList"){
			
			if(rowData.get("DISTANCE") <= 15000){
			}else {
				return configData.GRID_COLOR_TEXT_RED_CLASS;
			}
		}
	}
	
	function toggleYN() {
		
		var param = dataBind.paramData("searchArea");
		var json = gridList.gridSave({
	    	id : "gridList",
	    	param : param
	    });
		
		if(json){
			if(json.data){
				searchList(param);
			}
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
					<input type="button" CB="ToggleYN TOGGLEYN BTN_TOGGLE_YN" />
					<input type="button" CB="Save SAVE BTN_SAVE" />
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<!-- 
					<dl>
						<dt CL="STD_PTNRTY">업체타입</dt>
						<dd>
							<select class="input" Combo="WmsAdmin,BZ01COMBO" name="PTNRTY" id="PTNRTY" style="width: 160px;"></select> 
						</dd>
					</dl>
					 -->
					<dl>
						<dt CL="STD_PTNRKY">업체코드</dt>
						<dd>
							<input id="PTNRKY" type="text" class="input" name="PTNRKY" UIInput="S,SHBZPTN" validate="required"/> 
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
					<li><a href="#tab1-1"><span>납품처간 이동거리</span></a></li>  
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">번호</td>		
										<td GH="40"	GCol="rowCheck">V</td>
										<td GH="80 STD_PTNRKY_FROM"   GCol="text,PTNRKY_FROM">거래처(출발)</td>
										<td GH="100 STD_NAME01"   GCol="text,NAME01_FROM" >설명</td>
										<td GH="100 STD_ADDR01"   GCol="text,ADDR01_FROM" >대표주소</td>
										<td GH="100 STD_ADDR02"   GCol="text,ADDR02_FROM" >대표주소2</td>
										<td GH="80 STD_POSTCD"   GCol="text,POSTCD_FROM" >우편번호</td>
										
										<td GH="80 STD_PTNRKY_TO"   GCol="text,PTNRKY_TO">거래처(도착)</td>
										<td GH="100 STD_NAME01"   GCol="text,NAME01_TO" >설명</td>
										<td GH="100 STD_ADDR01"   GCol="text,ADDR01_TO" >대표주소</td>
										<td GH="100 STD_ADDR02"   GCol="text,ADDR02_TO" >대표주소2</td>
										<td GH="80 STD_POSTCD"   GCol="text,POSTCD_TO" >우편번호</td>
										
										<td GH="60 STD_DISTANCE"   GCol="text,DISTANCE" GF="N 0">거리(M)</td>
										<td GH="60 STD_DURATION"   GCol="text,DURATION" >시간(분)</td>
										<td GH="70 STD_USE_01"   GCol="select,USE_YN" >
											<select>
												<option value="Y">사용</option>
												<option value="N">미사용</option>
											</select>
										</td>
										<td GH="100 STD_CREDAT"   GCol="text,CREDAT" GF="D">생성일자</td>
										<td GH="100 STD_CRETIM"   GCol="text,CRETIM" GF="T">생성시간</td>
										<td GH="100 STD_CREUSR"   GCol="text,CREUSR" >생성자</td>								
										<td GH="100 STD_LMOUSR"   GCol="text,LMOUSR" >생성자</td>								
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="delete"></button>
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