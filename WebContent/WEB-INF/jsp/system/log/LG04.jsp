<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<style>
/*
	td[gcolname='PIC_URL'] {
		font-size:20px;
		line-height: 20px;
		vertical-align: top;	
	}
*/
</style>
<script type="text/javascript">
	$(document).ready(function(){
		//setTopSize(800);
	 	gridList.setGrid({
	    	id : "gridList",
			editable : true,
			module : "System",
			command : "LG04",
			colorType : true
		}); 
	 	
	 	$('select[name="CIO.IN_EQPM_NO"]').multipleSelect('checkAll');
	});
	
	function searchList(){
		var param = inputList.setRangeParam("searchArea");
		
		gridList.gridList({
	    	id : "gridList",
	    	param : param
	    });
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}
	}
	
	function gridListEventDataBindEnd(gridId, dataLength){
        if(gridId == "gridList" && dataLength > 0){
            var dataList = gridList.getGridData(gridId);
            var rowData;
            
            for(var i = 0; i< dataList.length;i++){
                rowData = dataList[i];
                
                var CARNO = rowData.get("CAR_NO");
                var PICURL = rowData.get("IN_CAR_PIC_URL");

				if($.trim(PICURL) != "" ){
					if(CARNO.indexOf("XXXX") > -1 || CARNO == "No_Detection"){
						//gridList.setColValue(gridId, i, "PIC_URL", "📸");
					}else {
						//gridList.setColValue(gridId, i, "PIC_URL", "📷");
					}
                }
				
            }
        }
    }
	
	function gridListEventRowClick(gridId, rowNum, colName){
        if(gridId == "gridList"){
        
        	var rowData = gridList.getGridData("gridList")[rowNum];
    	    var PICURL = $.trim(rowData.get("IN_CAR_PIC_URL"));
        	
            if(PICURL !== "" && colName=="PIC_URL"){
                window.open(PICURL, "_blank");
            }
            
        }
    }
	
	// 240118 에러표시 된 열 텍스트 색상 변경
	function gridListRowTextColorChange(gridId, rowNum, rowData){
		if(gridId === "gridList"){
			
			var CARNO = rowData.get("CAR_NO");
			var PICURL = rowData.get("IN_CAR_PIC_URL");
			
			if($.trim(PICURL) != "" ){
				if(CARNO.indexOf("XXXX") > -1 || CARNO == "No_Detection"){
					return configData.GRID_COLOR_TEXT_RED_CLASS;
				}else {
				}
            }
		}
	}

	function comboEventDataBindeBefore(comboAtt){
        if(comboAtt == "Wms,APCSINEQPM"){
            var param = new DataMap();
	        param.put("WAREKYS", "'<%= wareky%>'");

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
					<input type="button" CB="Search SEARCH BTN_SEARCH" />
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="APCS_IN_DATE"></dt>
						<dd>
							<input type="text" name="CIO.IN_DATE" UIInput="R" UIFormat="C -0 1" validate="required"/>
						</dd>
					</dl>
					<dl>
						<dt CL="APCS_IN_EQPM_NM">입차게이트</dt>
                        <dd>
                            <select type="text" class="input" Combo="Wms,APCSINEQPM" name="CIO.IN_EQPM_NO" id="CIO.IN_EQPM_NO" ComboType="MS" validate="required">
                            </select>
                        </dd>
					</dl>
					
					<dl>
						<dt CL="APCS_CAR_NO"></dt>
						<dd>
							<input type="text" name="CIO.CAR_NO" UIInput="SR" size="8px" />
						</dd>
					</dl>
					<dl>
						<dt CL="APCS_USER_NAME"></dt>
						<dd>
							<input type="text" name="CIO.USER_NAME" UIInput="SR" />
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
					<li><a href="#tab1-1"><span>일반</span></a></li>  
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40 STD_NUMBER" GCol="rownum">번호</td>
										<td GH="100 APCS_CAR_NO" GCol="text,CAR_NO">차량번호</td>
										<td GH="100 APCS_USER_NAME" GCol="text,USER_NAME">기사명</td>
										<td GH="100 APCS_IN_DATE" GCol="text,IN_DATE" GF="C">이력일자</td>
										<td GH="120 APCS_IN_EQPM_NM" GCol="text,IN_EQPM_NM">입차게이트</td>
										<td GH="150 APCS_IN_DTM" GCol="text,IN_DTM" GF="DT">입차시간</td>
										<td GH="120 APCS_OUT_EQPM_NM" GCol="text,OUT_EQPM_NM">출차게이트</td>
										<td GH="150 APCS_OUT_DTM" GCol="text,OUT_DTM" GF="DT">출차시간</td>
										<td GH="60 APCS_PIC_URL" GCol="text,PIC_URL" GF="S">이미지</td>
										<!-- <td GH="150 APCS_PIC_URL" GCol="text,IN_CAR_PIC_URL" GF="S">이미지</td> -->
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type="button" GBtn="find"></button>
						<button type="button" GBtn="sortReset"></button>
						<button type="button" GBtn="filter"></button>
						<button type="button" GBtn="layout"></button>
						<button type="button" GBtn="total"></button>
						<button type="button" GBtn="excel"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>4</span></span>
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