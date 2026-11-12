<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String ptnrty = request.getParameter("PTNRTY");
	if(ptnrty==null)ptnrty="";	
%>

<!doctype html>
<head>
<style type="text/css">
.tem5_content .tableWrap_search select	{width:172px;  height:25px; }
.tem5_content .tableWrap_search .text_2{width:90%; margin-right:3px;height:22px;border:#b8b8b8;}

.select-editable { position:relative; background-color:white; border:solid 1px #b8b8b8; width:172px; height:25px; }
.select-editable select { top:0px; left:0px; width:172px; height:25px; font-size:14px;  border:none;  }
.select-editable input { position:absolute; top:0px; left:0px; width:90%; height:23px; font-size:12px;  border:none;   }
.select-editable select:focus, .select-editable input:focus { outline:none; }

.select-editable { position:relative; background-color:white; border:solid 1px #d5d5d5; width:100%; height:35px; }
.select-editable select { position:absolute; top:0px; left:0px; width:100%; height:35px; font-size:14px;  border:none;  }
.select-editable input { position:absolute; top:0px; left:0px; width:90%; height:33px; font-size:12px;  border:none;   }
.select-editable select:focus, .select-editable input:focus { outline:none; }
/* .tableBody{height: 395px !important;} */

</style>

<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>Mobile WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			editable : false,
			module : "WmsMobileInboundGoodsReceipt",
			command : "PDGR05_POP_PTNRTY",
			gridMobileType : true
		});
		
		var data = mobile.getLinkPopData();
		var PTNRTY = data.get("PTNRTY");
		$("#PTNRTY").val(PTNRTY);

		searchList();
		
		$("#PTNRKY").focus();
		
	});
		
	function searchList(){	
		
		var PTNRTY = $("#PTNRTY").val();
		var PTNRKY = $("#PTNRKY").val();
		
		var param = new DataMap();
		param.put("LANGKY", "<%=langky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("WAREKY", "<%=wareky%>");
		param.put("PROGID", "<%=menuId%>");
		param.put("PTNRTY", PTNRTY);
		param.put("PTNRKY", PTNRKY);
		
		gridList.resetGrid("gridList");
		
		gridList.gridList({
			id : "gridList",
			param : param
		});

		$("#PTNRKY").focus();

	}
	
	function showMain(){
		window.top.menuOpen();
	}
	
	function fn_onfocus(objId){
		$("#"+objId).select();
	}
	
	function eventKeydown(event,objId){
		if(event.keyCode == "13"){	
			
			searchList();
		}
	}
	
	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList"){
			//var list = gridList.getGridData("gridList");
			
			var rowCnt = gridList.getGridDataCount(gridId);
			if(rowCnt>0){
				PlaySound('s');
			}else{
				PlaySound('e');
			}
		}
	}
	
	function gridListEventRowClick(gridId, rowNum, colName){
		
	}
	
	function gridListEventRowDblclick(gridId, rowNum) {
		var data = gridList.getRowData("gridList", rowNum);
		callback(data);
	}
	
	function closePop(){
		self.close();
 	}
	
	function selectData(){
		var rowNum = gridList.getFocusRowNum("gridList")
		var data = gridList.getRowData("gridList", rowNum);
		callback(data);
 	}
	
	function callback(data){
		data.put('param', mobile.getLinkPopData());
		window.opener.ptnrkyPopCallback(data);
		self.close();
	}
	
	
	
</script>
</head>
<body>
	<div class="main_wrap">
		<div class="tem4_content">
			<div class="select_box" id="bindArea">
				<div class="searchArea" id="searchArea">
					<table>
						<colgroup>
							<col width="80px"  />
							<col />
						</colgroup>
						<tbody id="paramArea">
							
							<tr>
								<th CL="STD_PTNRTY" width="100">거래처유형</th>
								<td >
									<select id="PTNRTY" name="PTNRTY" CommonCombo="PTNRTY"style="width:100%;" onchange="searchList()" disabled ></select>
								</td>
							</tr>
							
							<tr>
								<th CL="STD_PTNRKY" width="100">거래처</th>
								<td >
									<input type="text" class="text" inputmode="none" name="PTNRKY" id="PTNRKY" onchange="searchList()" onkeydown="eventKeydown(event,this.id)" style="text-align:center;float:left;"/>
								</td>
							</tr>
							
						</tbody>
					</table>
				</div>
			</div>
			
			<div class="tableWrap_search section">
					<div class="tableHeader">
					<table style="width: 100%">
						<colgroup>
								<col width="30px" />
								<col width="80px" />
								<col width="100px" />
								<col width="120px" />
						</colgroup>
						<thead>
							<tr>
								<th CL="STD_NO">No.</th>
								<th CL='STD_PTNRTY'>거래처유형</th>
								<th CL='STD_PTNRKY'>거래처</th>
								<th CL='STD_NAME01'>거래처명</th>
							</tr>
						</thead>
					</table>
				</div>
				
				<div class="tableBody">
<!-- 					<table style="width: 100%; overflow:hidden;word-wrap:break-word;"> -->
					<table style="width: 100%;">
						<colgroup>
								<col width="30px" />
								<col width="80px" />
								<col width="100px" />
								<col width="120px" />
						</colgroup>
						<tbody id="gridList">
							<tr CGRow="true">
								<td GCol="rownum">1</td>
								<td GCol="text,PTNRTY">거래처유형</td>
								<td GCol="text,PTNRKY">거래처</td>
								<td GCol="text,NAME01">거래처명</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			
			<!-- end table_body -->
			<div class="footer_5" style="position:fixed;">
<!-- 			<div class="footer_5" "> -->
				<table>
					<tr>
						<td class=f_1 onclick="self.close();" style="width: 50%;"><label CL='BTN_CLOSE'></label></td>
						<td class=f_1 onclick="selectData();" width="50%"><label CL='STD_SPOSIT'></label></td>
					</tr>
				</table>
			</div><!-- end footer_5 -->	
		</div>
	</div>
</body>