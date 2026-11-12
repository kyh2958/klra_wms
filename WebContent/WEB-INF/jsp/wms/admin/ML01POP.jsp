<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<script type="text/javascript">
	
	var data;	
	$(document).ready(function(){
		data = page.getLinkPopData();
		
		gridList.setGrid({
			id : "gridListPOP",
			editable : true,
			pkcol : "WAREKY,LOCAKY",
			module : "WmsAdmin",
			command : "ML01POP"
		});
	
		gridList.setReadOnly("gridListPOP", true);
		
		gridList.gridMap.get("gridListPOP").startHeadColDrag();
			
	});
	
	
	function fn_closing(){
		this.close();
	}
	
	
	function commonBtnClick(btnName){
		if(btnName == "Save"){
			fn_save();
		} else if(btnName == "Check"){
			fn_closing();
		}
	}
	
	function gridListEventRowAddBefore(gridId, rowNum){
		var newData = new DataMap();
	
		newData.put("WAREKY", "<%=wareky%>");
		newData.put("LOCATY", "10"); 
		newData.put("STATUS", "00");
		newData.put("LENGTH", 0);
		newData.put("WIDTHW", 0);
		newData.put("HEIGHT", 0);
		newData.put("CUBICM", 0);
		newData.put("MAXCPC", 0);
		newData.put("MAXQTY", 0);
		newData.put("MAXWGT", 0);
		newData.put("MAXLDR", 0);
		newData.put("MAXSEC", 0);
		newData.put("QTYCHK", 0);
		
		return newData;
	}
	
	function gridListEventRowAddAfter(gridId, rowNum){
		//alert("gridListEventRowAddAfter called");
	} 
	
	function gridListEventDataBindEnd(gridId, dataLength){
		
		//alert("dataLength : " + dataLength);
		for(var i=0; i<dataLength; i++){
			gridList.setColValue(gridId, i, "WAREKY", "<%=wareky%>");
		}
	}
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		
		if(gridId == "gridListPOP") {
			if(colName == "ZONEKY") {
				if(colValue == ""){
					gridList.setColValue("gridList", rowNum, "AREAKY", "");
				}else{
					
					var wareky = gridList.getColValue("gridList", rowNum, "WAREKY");
					
					var param = new DataMap();
					param.put("WAREKY",wareky);
					param.put("ZONEKY",colValue);
					alert(colValue);
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "AREAKY",
						sendType : "map",
						param : param
					});
					
					
					
					if(json && json.data){						
						gridList.setColValue(gridId, rowNum, "AREAKY", json.data["AREAKY"]); 
					}else{
						gridList.setColValue(gridId, rowNum, "AREAKY", "");
					}
				}
			}
		}
		
	}
	
	function fn_save() {
		
		var chkList = gridList.getGridData("gridListPOP");
	
		if( chkList.length == 0 ){
			commonUtil.msgBox("ORDER_M0005"); // 저장할 데이터가 없습니다.
			return;
		}
		
		/*
		for(var i=0; i<chkList.length; i++){
			var check = chkList[i].get("PHYIKY");
			for(var a=i+1; a<chkList.length; a++){
				var check1 = chkList[a].get("PHYIKY");
				if(check != check1){
					commonUtil.msgBox("VALID_M1613"); // 상이한 재고실사번호가 존재합니다.
					gridList.setRowFocus("gridListPOP",a);
					return;
				}
				a++;
			}
			i++;
		}
		*/
		
		// check validation
		if(gridList.validationCheck("gridListPOP", "data")) {
			//저장하시겠습니까?
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
				return;
			}
			
			var param = new DataMap();
			param.put("list", chkList);
			//param.put("DOCUTY", data.get("DOCUTY"));
			//param.put("BARCHECK", "1");
			//param.put("PHYIKY", data.get("PHYIKY"));
			
			var json = netUtil.sendData({
				url:"/wms/admin/json/saveML01.data",
				sendType : "map",
				param : param
			});
			
			
			if(json && json.data){
				if(json.data == "OK") {
					commonUtil.msgBox("MASTER_M0564", json.data); // 저장에 성공하였습니다.
					fn_closing();
				} else {
					//commonUtil.msgBox("VALID_M0002", json.data); // 저장이 실패하였습니다.
					alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEFAIL) + " [" + json.data + "]");
				}
			} else {
				commonUtil.msgBox("VALID_M0002", json.data); // 저장이 실패하였습니다.
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
		<!-- <div class="content_serch" id="searchArea"> -->
			<div class="btn_wrap">
				<div class="fl_l">
				</div>
				<div class="fl_r">
					<input type="button" CB="Save SAVE BTN_SAVE" />
					<input type="button" CB="Check CHECK BTN_CLOSE" />
				</div>
			</div>
		<!-- </div> -->
        <!-- <div class="search_next_wrap"> -->
			<div class="content_layout tabs top_layout" style="width: 975px; height: 555px;">
				<ul class="tab tab_style02">
					<li><a href="#tabs1-1" CL='STD_LIST'><span>리스트</span></a></li>
					<!-- <li><a href="#tabs1-2" CL='STD_ITEMDET'><span>Item상세</span></a></li> -->
					<li class="btn_zoom_wrap">
						<ul>
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section" id="tabs1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridListPOP">
									<tr CGRow="true">
										<td GH="40 STD_NUMBER" GCol="rownum">1</td>
										<td GH="80 STD_WAREKY" GCol="text,WAREKY"></td>
										<td GH="80 STD_AREAKY" GCol="text,AREAKY" id="AREAKY"></td>
										<td GH="80 STD_ZONEKY" GCol="text,ZONEKY" GF="S 10"></td> 
										<td GH="80 STD_LOCAKY" GCol="text,LOCAKY" GF="S 20"></td>
										<td GH="80 STD_TKZONE" GCol="text,TKZONE" GF="S 10"></td>
										<td GH="80 STD_LOCATY" GCol="text,LOCATY"></td>
										<td GH="80 STD_STATUS" GCol="text,STATUS"></td>
										<td GH="80 STD_INDUPA" GCol="text,INDUPA"></td>
										<td GH="80 STD_INDUPK" GCol="text,INDUPK"></td>
										<td GH="80 STD_INDCPC" GCol="text,INDCPC"></td>
										<td GH="80 STD_MAXCPC" GCol="text,MAXCPC" GF="N 15,3"></td>
										<td GH="80 STD_LENGTH" GCol="text,LENGTH" GF="N 15,3"></td>
										<td GH="80 STD_WIDTHW" GCol="text,WIDTHW" GF="N 15,3"></td>
										<td GH="80 STD_HEIGHT" GCol="text,HEIGHT" GF="N 15,3"></td>
										<td GH="80 STD_MIXSKU" GCol="text,MIXSKU"></td>
										<td GH="80 STD_MIXLOT" GCol="text,MIXLOT"></td>
										<td GH="80 STD_IBROUT" GCol="text,IBROUT" GF="S 10"></td>
										<td GH="80 STD_OBROUT" GCol="text,OBROUT" GF="S 10"></td>
										<td GH="80 STD_RPROUT" GCol="text,RPROUT" GF="S 10"></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type="button" GBtn="excelUpload"></button>
<!-- 						<button type='button' GBtn="add"></button> -->
<!-- 						<button type='button' GBtn="copy"></button> -->
<!-- 						<button type='button' GBtn="delete"></button> -->
<!-- 						<button type='button' GBtn="total"></button> -->
<!-- 						<button type='button' GBtn="layout"></button> -->
<!-- 						<button type='button' GBtn="excel"></button> -->
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
			</div>
		<!-- </div> -->
	</div>
</div>

<!-- // content -->
<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>