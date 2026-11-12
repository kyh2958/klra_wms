<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>인터페이스 로그별 상세내역</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript">

	window.resizeTo('1250','600');
	
	var iftbl;
	var ifid;
	var logseqno;
	var bwart;
	var bwartnm;
	var credat;
	
	$(document).ready(function(){
		var data = page.getLinkPopData();
		dataBind.dataNameBind(data, "searchArea");
		
		logseqno 	= data.get("SEQNO");
		ifid 		= data.get("IFID");
		iftbl 		= data.get("IFTBL");
		bwart 		= data.get("BWART");
		bwartnm 	= data.get("BWARTNM");
		credat 		= data.get("CREDAT");
		
		$("#IFID").val(ifid);
		$("#IFKNM").val(data.get("IFKNM"));
		$("#IFTBL").val(iftbl);
		$("#BWARTNM").val(bwartnm);
		setHead(iftbl);
		
		searchList();
	});

	/** HEAD 셋팅 및 조회처리 **/
	function setHead(iftbl) {
		$("#resetTable").empty();
		$("#resetUtil").empty();
		//CLEAR
		var html='<table class="table_c">';
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
// 			util += '<button type="button" GBtn="excelUpload" id="excelUpload"></button>';
// 			util += '<button type="button" GBtn="layout"></button>';
			util += '<button type="button" GBtn="total"></button>';
			util += '<button type="button" GBtn="excel"></button>';
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
				if(jcol.data[i].VIEWAT == "V") {
					xcolH.push(jcol.data[i].COLNM);
					if(jcol.data[i].SYSFR == "WMS") {
						xcolI.push(jcol.data[i].SOURCE);
					} else {
						xcolI.push(jcol.data[i].TARGET);
					}
				}
			}
		}
//-----------------------------------------------------------
//        WMS측 컬럼으로 셋팅
//-----------------------------------------------------------
		/** rownum 추가 **/
		$("#appendBodyGrid").append('<td GH="40" GCol="rownum">1</td>');
		
		var typstr = "";
		
		if(ifid != 'WMS_IF_888' && ifid != 'WMS_IF_777' && ifid != 'WMS_IF_778' && ifid != 'WMS_IF_779') {
			$("#appendBodyGrid").append('<td GH="60 처리상태" GCol="text,PRCFLG" ></td>');
		}
		
		for(var i=0; i < xcolH.length; i++){
			var width = xcolH[i].length > 10 ? 160 : (xcolH[i].length > 6 ? 130 : 100);
// 컬럼별 숫자,날짜,시간등 포맷 문자로 통일 : 컬럼종류가 너무 많고 차후 오류발생 여지가 있음
// 			if(xcolI[i] == "QTYPRD" || xcolI[i] == "MENGE" || xcolI[i] == "MENGE_B" || xcolI[i] == "QTYGOD"
// 			|| xcolI[i] == "QTYBAD" || xcolI[i] == "QTSHP" || xcolI[i] == "ZQTSACT" || xcolI[i] == "QTADJU") {
// 				typstr = 'GF="N 20,3"';
// 			} else if(xcolH[i].search(/일자/) != -1){
// 				typstr = 'GF="D"';
// 			} else {
// 				typstr = "";
// 			}
			$("#appendBodyGrid").append('<td GH="'+width+' '+xcolH[i]+'" GCol="text,'+xcolI[i]+'" '+typstr+'></td>');
		}

		if(ifid != 'WMS_IF_888' && ifid != 'WMS_IF_777' && ifid != 'WMS_IF_778' && ifid != 'WMS_IF_779') {
			$("#appendBodyGrid").append('<td GH="220 로그순번" GCol="text,LOGSEQNO" ></td>');
			$("#appendBodyGrid").append('<td GH="300 에러내용" GCol="text,PRCETX" ></td>');
		}
		
		$("#appendBodyGrid").append('<td GH="100 생성일자" GCol="text,CREDAT" GF="D"></td>');
		$("#appendBodyGrid").append('<td GH="100 생성시간" GCol="text,CRETIM" GF="T"></td>');
		$("#appendBodyGrid").append('<td GH="100 생성자" GCol="text,CREUSR" ></td>');
		
		if(ifid != 'WMS_IF_777' && ifid != 'WMS_IF_778' && ifid != 'WMS_IF_779') {
			$("#appendBodyGrid").append('<td GH="100 수정일자" GCol="text,LMODAT" GF="D"></td>');
			$("#appendBodyGrid").append('<td GH="100 수정시간" GCol="text,LMOTIM" GF="T"></td>');
			$("#appendBodyGrid").append('<td GH="100 수정자" GCol="text,LMOUSR" ></td>');
		}

		$('.closer').click();
		
		gridList.setGrid({
	    	id : "gridListSub",
			module : "Erpif",
			command : "IFWMSLOGDETAIL",
			colorType : true,
			emptyMsgType : false
		});
	}

	function fn_closing(){
		this.close();
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		} else if(btnName == "Close"){
			fn_closing();
		} else if(btnName == "Download"){
			excelDownload();
		}
	}
	
	var timerId = 0;
	var ingCnt;
	function showCnt() {
		cnt.value = ingCnt;
	}

	function searchList(){
		var param = new DataMap();
		param.put("LOGSEQNO", logseqno);
		param.put("IFTBL", iftbl);
		param.put("CREDAT", credat.replace(/\./g,''));
		
		if (ifid == "WMS_IF_901") {
			gridList.gridList({
				id : "gridListSub",
				module : "Erpif",
				command : "IFUSR",
				param : param
			});
		} else if (ifid == "WMS_IF_888") {
			gridList.gridList({
				id : "gridListSub",
				module : "Erpif",
				command : "WMS_IF_888",
				param : param
			});
		} else if (ifid == "WMS_IF_777" || ifid == "WMS_IF_778" || ifid == "WMS_IF_779") {
			gridList.gridList({
				id : "gridListSub",
				module : "Erpif",
				command : "WMS_IF_777778",
				param : param
			});
		} else {
			gridList.gridList({
				id : "gridListSub",
				param : param
			});
		}
	}
	
	function gridListRowColorChange(gridId, rowNum, rowData){
		if(gridId == "gridListSub"){			
			if(rowData.get("PRCFLG") == "E"){
				return configData.GRID_COLOR_BG_RED_CLASS;
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
					<input type="button" CB="Search SEARCH BTN_DISPLAY">
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
									<td style="width:400px;">문서유형 : <input type="text" id="BWARTNM" style="width:250px;" readonly></td>
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