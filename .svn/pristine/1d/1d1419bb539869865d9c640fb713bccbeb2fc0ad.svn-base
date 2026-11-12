<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String taskky = request.getParameter("TASKKY");
	String SEARCHCOUNT = request.getParameter("SEARCHCOUNT");
	if(taskky==null)taskky="";
	if(SEARCHCOUNT == null){
    	SEARCHCOUNT = "";
    }

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
	var opt;
	var asndky;
	var skukey;
	var barcod;
	var rcvlmtOrg;
	var flag;
	var activeType = false;
	var year;
	var month;
	var date;
	
	$(document).ready(function(){
			gridList.setGrid({
				id : "gridList",
				editable : false,
				module : "WmsMobileOutboundPicking_COPY",					  
				command : "PDPK05_DOC",
				gridMobileType : true
			});
			var json = netUtil.sendData({
				module : "WmsMobileOutboundPicking_COPY",
				command : "GR_CURDATE",
				bindId : "gridHeadList",
				gridMobileType : true,
				sendType : "map"
			});
			if(json && json.data){
				year = json.data.ORDDAT.substr(0,4);
				month = json.data.ORDDAT.substr(4,2);
				date = json.data.ORDDAT.substr(6,2);
				
				$("#ORDDAT").val(year + "." + month + "." + date);
			}
			
			searchList();
			
		});
		
	function searchList(){	
		
		var ORDDAT = $("#ORDDAT").val().replace(/\./gi, "");
		var data = mobile.getLinkPopData();
		
		var param = new DataMap();
		param.put("LANGKY", "<%=langky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("WAREKY", "<%=wareky%>");
		param.put("REGN01", "<%=regn01%>");
		param.put("PROGID", "<%=menuId%>");	
		param.put("STATIT", data.get("STATIT"));
		
		param.put("ORDDAT", ORDDAT);
		
		gridList.resetGrid("gridList");
		
		gridList.gridList({
			id : "gridList",
			param : param
		});

		//$("#ORDDAT").focus();

	}	
		 	
	
	function fn_onfocus(objId){
		$("#"+objId).select();
	}
	
	
	function eventKeydown(event,objId){
		if(event.keyCode == "13"){	
			
			if($("#ORDDAT").val() == ""){
				PlaySound('e');
				commonUtil.msgBox("VALID_M1590"); //전기일자가 입력되지 않았습니다.				
				
				return;
			}
			
			searchList();
		}
	}
	
	function dateChange(){		
		
		searchList();
	}	
	
	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList" && dataLength == 0){
			/* $('#ORDDAT').focus(); */
		}
	}
	
	// 더블클릭 이벤트
	function gridListEventRowDblclick(gridId, rowNum) {
		var data = gridList.getRowData("gridList", rowNum);
		callback(data);
	}
	
	// 그리드 클릭 시 rowNum 변수 저장
	function gridListEventRowClick(gridId, rowNum) {
		selectedRowNum = rowNum;
	}
	
	// 선택버튼 클릭 이벤트
	function onClickChooseBtn() {
		var data = gridList.getRowData("gridList", selectedRowNum);
		callback(data);
	}
	
	function callback (data) {
		if(!data.isEmpty()) {
			data.put('param', mobile.getLinkPopData());
			window.opener.DocPopCallback(data);
			self.close();
		}
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
								<th CL="STD_DOCDAT4" width="100">작업일자</th>
								<td >
									<input type="text" class="text" name="ORDDAT" id="ORDDAT" UIFormat="C N" onchange="searchList();" validate="required" style="text-align: center; width: 80%" onfocus="fn_onfocus(this.id)" />
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
								<col width="50px" />
								<col width="70px" />
								<col width="60px" />
						</colgroup>
						<thead>
							<tr>
								<th CL="STD_NO">No.</th>
								<th CL="STD_STKNUM_TYPE">지시유형</th>
								<th CL='STD_STKNUM1'>지시번호</th>
								<th CL='STD_TASSTATDONM'>작업상태</th>
							</tr>
						</thead>
					</table>
				</div>
				
				<div class="tableBody">
<!-- 					<table style="width: 100%; overflow:hidden;word-wrap:break-word;"> -->
					<table style="width: 100%;">
						<colgroup>
								<col width="30px" />
								<col width="50px" />								
								<col width="70px" />
								<col width="60px" />
						</colgroup>
						<tbody id="gridList">
							<tr CGRow="true">
								<td GCol="rownum">1</td>
								<td GCol="text,STKNUM_STATUS">지시유형</td>
								<td GCol="text,STKNUM">지시번호</td>
								<td GCol="text,STATDONM">작업상태</td>
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
						<td class=f_1 onclick="self.close()" style="width: 50%;"><label CL='BTN_CLOSE'></label></td>
						<td class=f_1 onclick="onClickChooseBtn()" width="50%"><label CL='STD_SPOSIT'></label></td>
					</tr>
				</table>
			</div><!-- end footer_5 -->	
		</div>
	</div>
</body>