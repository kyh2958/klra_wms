<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String ebeln = request.getParameter("EBELN");
	if(ebeln==null)ebeln="";
	
	String parProgid = request.getParameter("PAR_PROGID");
	if(parProgid==null)parProgid="";
	
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
	var data;
	var year;
	var month;
	var date;
	
	$(document).ready(function(){
		
		data = mobile.getLinkPopData().map;
		
		gridList.setGrid({
			id : "gridList",
			editable : false,
			module : "WmsMobileInboundGoodsReceipt",
			command : "PDGR01_EBELN",
			gridMobileType : true
		});
		
		var json = netUtil.sendData({
			module : "WmsMobileInboundGoodsReceipt",
			command : "GR_CURDATE",
			bindId : "gridHeadList",
			sendType : "map"
		});
		if(json && json.data){
			year = json.data.DOCDAT.substr(0,4);
			month = json.data.DOCDAT.substr(4,2);
			date = json.data.DOCDAT.substr(6,2);
			
			$("#BEDAT").val(year + "." + month + "." + date);
		}
		
		searchList();
		
	});
	
	function searchList(){	
		
		var BEDAT = $("#BEDAT").val().replace(/\./gi, "");
		
		var param = new DataMap();
		param.put("LANGKY", "<%=langky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("WAREKY", "<%=wareky%>");
		param.put("MTYPE", "<%=mtype%>");
		param.put("REGN01", "<%=regn01%>");
		param.put("PROGID", data.PAR_PROGID);
		param.put("BEDAT", BEDAT);
		
		gridList.resetGrid("gridList");
		
		gridList.gridList({
			id : "gridList",
			param : param
		});
		
		//$("#BEDAT").focus();
		
	}
	
	function showMain(){
		window.top.menuOpen();
		/*
		if(opt == "pop"){
			mobile.linkPopClose();
		}else if(flag == 'Y'){
			mobile.linkPopClose();
		}else{
			window.top.closePage();
		}
		*/
	}
	
	function fn_onfocus(objId){
		$("#"+objId).select();
	}
	
	function eventKeydown(event,objId){
		if(event.keyCode == "13"){	
			/*
			if($("#BEDAT").val() == ""){
				PlaySound('e');
				commonUtil.msgBox("VALID_M1590"); //전기일자가 입력되지 않았습니다.
				return;
			}
			*/
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
	
	function dateChange(){
		/*
		var input = $("#BEDAT").val().substr(5,2);
		if(month != input){
			PlaySound('e');
			commonUtil.msgBox("VALID_M0131"); // 전기일자는 당월이어야 합니다. VALID_M0131
			$("#BEDAT").val(year + "." + month + "." + date);
		}
		*/
		searchList();
	}
	
	function closePop(){
 		//var paramStr = "EBELN=<%=ebeln%>";
		//location.href="/mobile/PDGR01.page?"+paramStr;
		window.opener.barcodFocus();
		self.close();
 	}
	
	function selectData(){
		var rowNum = gridList.getFocusRowNum("gridList")
		var data = gridList.getRowData("gridList", rowNum);
		callback(data);
 	}
	
	function callback(data){
		data.put('param', mobile.getLinkPopData());
		window.opener.ebelnPopCallback(data);
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
								<th CL="STD_BEDAT" width="100">전기일자</th>
								<td >
									<input type="text" class="text" inputmode="none" name="BEDAT" id="BEDAT" UIFormat="C" onchange="dateChange()" onkeydown="eventKeydown(event,this.id)" style="width:80%;text-align:center;float:left;"/>
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
								<col width="100px" />
								<col width="150px" />
						</colgroup>
						<thead>
							<tr>
								<th CL="STD_NO">No.</th>
								<th CL='STD_SEBELN'>오더번호</th>
								<th CL='STD_DPTNNM'>공급업체</th>
							</tr>
						</thead>
					</table>
				</div>
				
				<div class="tableBody">
<!-- 					<table style="width: 100%; overflow:hidden;word-wrap:break-word;"> -->
					<table style="width: 100%;">
						<colgroup>
								<col width="30px" />
								<col width="100px" />
								<col width="150px" />
						</colgroup>
						<tbody id="gridList">
							<tr CGRow="true">
								<td GCol="rownum">1</td>
								<td GCol="text,EBELN">오더번호</td>
								<td GCol="text,DPTNNM">공급업체</td>
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
						<td class=f_1 onclick="closePop()" style="width: 50%;"><label CL='BTN_CLOSE'></label></td>
						<td class=f_1 onclick="selectData()" width="50%"><label CL='STD_SPOSIT'></label></td>
					</tr>
				</table>
			</div><!-- end footer_5 -->	
		</div>
	</div>
</body>