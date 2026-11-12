<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>인터페이스</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript">

	window.resizeTo('1250','600');
	
	var iftbl;
	var ifid;
	var bwart;
	
	$(document).ready(function(){
		var data = page.getLinkPopData();
		dataBind.dataNameBind(data, "searchArea");

		ifid = data.get("IFID");
		iftbl = data.get("IFTBL");
		bwart = data.get("BWART");
		bwartnm = data.get("BWARTNM");
		
		$("#IFID").val(ifid);
		$("#IFKNM").val(data.get("IFKNM"));
		$("#IFTBL").val(iftbl);
		$("#BWARTNM").val(bwartnm);
		
		setHead(iftbl);
	});

	/** HEAD 셋팅 및 조회처리 **/
	function setHead(iftbl) {
		$("#resetTable").empty();
		$("#resetUtil").empty();
		//CLEAR
		var html ='<table class="table_c">';
			html +='<tbody id="gridListSub">';
			html +='<tr id="appendBodyGrid" CGRow="true">';
			html +='</tr>';
			html +='</tbody>';
			html +='</table>';
// 			html +='</div>';
		$("#resetTable").html(html);

		var util = '<div class="btn_lit tableUtil">';
			util += '<button type="button" GBtn="find"></button>';
			util += '<button type="button" GBtn="sortReset"></button>';
			util += '<button type="button" GBtn="add"></button>';
			util += '<button type="button" GBtn="copy"></button>';
			util += '<button type="button" GBtn="delete"></button>';
			util += '<button type="button" GBtn="excelUpload" id="excelUpload"></button>';
// 			util += '<button type="button" GBtn="layout"></button>';
// 			util += '<button type="button" GBtn="total"></button>';
// 			util += '<button type="button" GBtn="excel"></button>';
			util += '<span class="txt_total" >총 건수 : <span GInfoArea="true">0</span></span>';
			util += '</div>';
		$("#resetUtil").html(util);
			
		// 매핑컬럼 조회
		var param = new DataMap();
		param.put("IFID", ifid);
		var jcol = netUtil.sendData({
			url : "/IF/list/json/IFCOL.data",
			param : param
		});

		var xcolH = new Array();
		var xcolI = new Array();
		if(jcol && jcol.data) {
			for(var i=0; i<Object.keys(jcol.data).length; i++) {
				if(jcol.data[i].EXCELAT == "V") {
					xcolH.push(jcol.data[i].COLNM);
					if(jcol.data[i].SYSFR == "MES") {
						xcolI.push(jcol.data[i].TARGET);
					} else {
						xcolI.push(jcol.data[i].SOURCE);
					}
				}
			}
		}
			
//-----------------------------------------------------------
//        통신대상(SAP,MES,PTL) 시스템측 컬럼으로 셋팅
//
//        유의사항 : 통신대상 시스템의 업무담당자에 따라서 동일정보라해도 수신되는 영문컬럼명이 상이함 (예:거래처의 공급처 및 판매처,납품처)
//
//-----------------------------------------------------------

		/** rownum 추가 **/
		$("#appendBodyGrid").append('<td GH="40" GCol="rownum">1</td>');
		
		var typstr = "";
		for(var i=0; i < xcolH.length; i++){
			var width = xcolH[i].length > 10 ? 160 : (xcolH[i].length > 6 ? 130 : 100);
			$("#appendBodyGrid").append('<td GH="'+width+' '+xcolH[i]+'" GCol="input,'+xcolI[i]+'" '+typstr+'></td>');
		}

		$('.closer').click();
		
		gridList.setGrid({
	    	id : "gridListSub",
			module : "Erpif",
			command : iftbl,
			colorType : true,
			emptyMsgType : false
		});
	}
	
	function fn_closing(){
		this.close();
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Save"){
			fn_save();
		} else if(btnName == "Close"){
			fn_closing();
		} else if(btnName == "Download"){
			excelDownload();
		}
	}

	function excelDownload(){
		gridList.excelUpGridId = "gridListSub";
	    gridList.excelUploadTemplateDown();
	}
	
	var timerId = 0;
	var ingCnt;
	
	function showCnt() {
		cnt.value = ingCnt;
	}
	
	function fn_save() {
		var list = gridList.getGridData("gridListSub");

		if( list.length == 0 ){
			commonUtil.msgBox("ORDER_M0005"); // 저장할 데이터가 없습니다.
			return;
		}
		
		// check validation
		if(gridList.validationCheck("gridListSub", "data")) {
			//저장하시겠습니까?
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
				return;
			}
			
			var param = new DataMap();
			param.put("data", list);
			param.put("IFID", ifid);
			param.put("iftbl", iftbl);
			var json = netUtil.sendData({
				url : "/wms/erpif/json/receiveIF.data",
				param : param
			});
			
			if(json && json.data){
				if(json.data == "SUCCESS") {
					// 완료
					commonUtil.msgBox("VALID_M0001", json.data); // 저장이 성공하였습니다.
				} else {
					commonUtil.msgBox("ITF_M0038", json.data); // 저장실패 {0}
				}
			} else {
				// 실패
				commonUtil.msgBox("ITF_M0038", json.data); // 저장실패 {0}
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
					<input type="button" CB="Save SAVE BTN_SAVE">
					<input type="button" CB="Download REFLECT BTN_TEMPLD">
					<input type="button" CB="Close CHECK BTN_CLOSE">
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap">
					<dl>
						<dd>
							<table style="width:1000px;">
								<tr>
									<td style="width:170px;">인터페이스ID : <input type="text" id="IFID" style="width:80px;" readonly></td>
									<td style="width:300px;">인터페이스명 : <input type="text" id="IFKNM" style="width:210px;" readonly></td>
									<td style="width:150px;">IF테이블 : <input type="text" id="IFTBL" style="width:80px;" readonly></td>
									<td style="width:220px;">문서유형 : <input type="text" id="BWARTNM" style="width:160px;" readonly></td>
								</tr>
							</table>
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
							<table id="resetTable" class="table_c">
							</table>
						</div>
					</div>
					
					<div class="btn_lit tableUtil" id="resetUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="add"></button>
						<button type='button' GBtn="copy"></button>
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