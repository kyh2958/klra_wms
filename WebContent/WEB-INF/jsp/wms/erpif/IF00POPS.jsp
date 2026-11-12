<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>인터페이스 실적전송</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript">

	window.resizeTo('1250','600');
	
	var iftbl;
	var ifid;
	var iflastdat;
	var bwart;
	var rfcfn;
	
	$(document).ready(function(){
		var data = page.getLinkPopData();
		dataBind.dataNameBind(data, "searchArea");
		
		ifid = data.get("IFID");
		iftbl = data.get("IFTBL");
// 		iflastdat = data.get("LASTDAT");
		bwart = data.get("BWART");
		bwartnm = data.get("BWARTNM");
		rfcfn = data.get("RFCFN");
		
		$("#IFID").val(ifid);
		$("#IFKNM").val(data.get("IFKNM"));
		$("#IFTBL").val(iftbl);
// 		$("#LASTDAT").val(iflastdat);
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
					xcolI.push(jcol.data[i].TARGET);
				}
			}
		}

//-----------------------------------------------------------
//        SAP측 전용 컬럼으로 셋팅
//-----------------------------------------------------------

		if(iftbl == "IFWMS203") {
			// 입고실적
			if (ifid == "WMS_IF_217") { lifnrnm="납품처"; } else { lifnrnm="공급처"; }
			colH = ['문서번호','문서아이템번호'];
			colI = ['RECVKY'  ,'RECVIT'];
		} else if(iftbl == "IFWMS209") {
			// 품질검사대상
			colH = ['문서번호','문서아이템번호'];
			colI = ['RECVKYIN'  ,'RECVIT'];
		} else if(iftbl == "IFWMS213") {
			// 출고실적
			if (ifid == "WMS_IF_311") { lifnrnm="공급처"; } else { lifnrnm="납품처"; }
			colH = ['문서번호','문서아이템번호'];
			colI = ['SHPOKY'  ,'SHPOIT'];
		} else if(iftbl == "IFWMS223") {
			// 재고조정
			colH = ['재고조정문서번호','재고조정문서아이템번호','품목코드','조정수량','TO품목코드','단위' ,'저장위치','플랜트','포장타입(PS)','호기'  ,'재고상태','주문_아이템번호','국가코드','거래처코드MTO','보류사유명','제조일자','입고일자','유통기한','개별바코드','LOT번호','평량'  ,'컨테이너유형','TO저장위치','TO플랜트','TO포장타입(PS)','TO호기' ,'TO재고상태','TO주문_아이템번호','TO국가코드','TO거래처코드MTO','TO보류사유명','TO제조일자','TO입고일자','TO유통기한','TO개별바코드','TOLOT번호','TO평량' ,'TO컨테이너유형','WMS문서번호','WMS문서아이템번호'];
			colI = ['SADJKY'          ,'SADJIT'                ,'MATNR'   ,'QTADJU'  ,'TMATNR'    ,'MEINS','LOTA01'  ,'LOTA02','LOTA03'      ,'LOTA04','LOTA06'  ,'LOTA07'         ,'LOTA08'  ,'LOTA09'       ,'LOTA10'    ,'LOTA11'  ,'LOTA12'  ,'LOTA13'  ,'LOTA14'    ,'LOTA15' ,'LOTA16','LOTA17'      ,'TLOTA01'   ,'TLOTA02' ,'TLOTA03'       ,'TLOTA04','TLOTA06'   ,'TLOTA07'          ,'TLOTA08'   ,'TLOTA09'        ,'TLOTA10'     ,'TLOTA11'   ,'TLOTA12'   ,'TLOTA13'   ,'TLOTA14'     ,'TLOTA15'  ,'TLOTA16','TLOTA17'       ,'VGBEL'      ,'VGPOS'];
		}

		/** rownum 추가 **/
		$("#appendBodyGrid").append('<td GH="40" GCol="rownum">1</td>');
		
		var typstr = "";

		for(var i=0; i < xcolH.length; i++){
			var width = xcolH[i].length > 10 ? 160 : (xcolH[i].length > 6 ? 130 : 100);

			$("#appendBodyGrid").append('<td GH="'+width+' '+xcolH[i]+'" GCol="text,'+xcolI[i]+'" '+typstr+'></td>');
		}

		$('.closer').click();
		
		gridList.setGrid({
	    	id : "gridListSub",
			module : "Erpif",
			command : "IFDETAIL",
			colorType : true,
			emptyMsgType : false
		});
	}

	function fn_closing(){
		this.close();
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Send"){
			send();
		} else if(btnName == "Download"){
			excelDownload();
		} else if(btnName == "Close"){
			fn_closing();
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

	function send(){
		var list = gridList.getGridData("gridListSub");
		if( list.length == 0 ){
			commonUtil.msgBox("ITF_M0029"); // 전송할 데이터가 없습니다.
			return;
		}
		
		// check validation
		if(gridList.validationCheck("gridListSub", "data")) {
			//전송하시겠습니까?
			if(!commonUtil.msgConfirm(commonUtil.getMsg("ITF_M0030"))){
				return;
			}
			
			// 로그순번 채번
			var jsonLOGSEQNO = netUtil.sendData({
				url : "/IF/map/json/IFWMSLOGSEQNO.data",
				param : param
			});
			
			if(jsonLOGSEQNO && jsonLOGSEQNO.data){			
				var param = new DataMap();
				param.put("list", list);
				param.put("rfcfn", rfcfn); // SAP Func OR RESTFUL URL
				param.put("IFID", ifid);   // UI에서 호출시에는 문서유형별 인터페이스정의서에서 해당 IFID로 셋팅
				param.put("iftbl", iftbl); // UI에서 호출시에는 문서유형별 인터페이스정의서에서 해당 IFTBL로 셋팅
				param.put("ownrky", "<%=ownrky%>");
				param.put("LOGSEQNO", jsonLOGSEQNO.data.LOGSEQNO);
				// 품질검사 --------------------------------
				// WMS_IF_208 입고실적정보(자재)-품질검사대상		111	IFWMS209
				// WMS_IF_210 입고실적정보(수입자재)-품질검사대상	113	IFWMS209
				// WMS_IF_212 반품입고실적정보-품질검사대상		131	IFWMS209
				//
				// 입고실적 --------------------------------
				// WMS_IF_214 입고실적정보(자재,상품)			111 IFWMS203
				// WMS_IF_215 입고실적정보(수입자재,수입상품)		215 IFWMS203
				// WMS_IF_216 입고실적정보(이송)				121 IFWMS203
				// WMS_IF_222 입고실적정보(제품)-MES			101 IFWMS203
				// WMS_IF_221 입고실적정보(제품)-SAP			101 IFWMS203
				// WMS_IF_217 반품입고실적정보					131 IFWMS203
				// WMS_IF_218 반납입고실적정보-MES				135 IFWMS203
				// WMS_IF_224 반납입고실적정보(자재)-SAP			135 IFWMS203
				// WMS_IF_219 재고조정입고						199 IFWMS203
				//
				// 출고실적 --------------------------------
				// WMS_IF_317 생산투입불출실적정보				353 IFWMS213
				// WMS_IF_307 출고실적정보(주문)				201 IFWMS213
				// WMS_IF_308 출고실적정보(이송)				221 IFWMS213
				// WMS_IF_309 자재불출실적정보 mes				205 IFWMS213
				// WMS_IF_320 자재불출실적정보 sap				205 IFWMS213
				// WMS_IF_310 즉시불출실적정보 mes				354 IFWMS213
				// WMS_IF_321 즉시불출실적정보 sap				354 IFWMS213
				// WMS_IF_311 반품출고실적정보(자재,상품)			231 IFWMS213
				// WMS_IF_313 폐기출고실적정보					361 IFWMS213
				// WMS_IF_314 재고조정출고						399 IFWMS213
				// WMS_IF_315 무상출고실적정보					206,207 IFWMS213
				// 
				// 재고조정 --------------------------------
				// WMS_IF_401 재고상태변경정보					402 IFWMS223
				// WMS_IF_402 MTO/MTS변경정보					423 IFWMS223
				// WMS_IF_403 제조LOT변경정보					422 IFWMS223
				// WMS_IF_404 저장위치변경정보					401 IFWMS223
				// WMS_IF_405 품목코드 변경 (MTM)				421 IFWMS223
	
				// 인터페이스 테이블 등록
				// 중요 : UI에서 TRANSACTION 포함할것 (예, 입고완료 처리시 IFWMSXXX 품검 혹은 실적테이블까지 등록되어야함)
				var json = netUtil.sendData({
					url : "/wms/erpif/json/dataInsertIF.data",
					param : param
				});
				
				if(json && json.data){
					if(json.data == "SUCCESS") {
						// 전송호출
						// 중요 : UI에서 TRANSACTION에 포함하지 않는다... 단독으로 처리하며 통신상의 오류로 인한 재전송 처리를 위함
						var jsend = netUtil.sendData({
							url : "/wms/erpif/json/sendIF.data",
							param : param
						});
						if(jsend && jsend.data){
							if(jsend.data == "SUCCESS") {
								commonUtil.msgBox("ITF_M0031", jsend.data); // 전송 완료했습니다.
							} else {
								alert(jsend.data);
							}
						} else {
							// 전송호출 실패
							commonUtil.msgBox("ITF_M0032", jsend.data); // 전송 살패했습니다.
						}
					} else {
						alert(json.data);
					}
				} else {
					// 인터페이스 테이블 등록실패
					commonUtil.msgBox("ITF_M0033", json.data); // 데이터 등록 실패했습니다.
				}
			} else {
				
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
					<input type="button" CB="Send SEARCH BTN_SEND">
					<input type="button" CB="Download REFLECT BTN_TEMPLD">
					<input type="button" CB="Close CHECK BTN_CLOSE">
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap">
<!-- 					<dl> -->
<!-- 						<dd>IFID : <input type="text" id="IFID" readonly></dd> -->
<!-- 						<dd>IFKNM : <input type="text" id="IFKNM" readonly></dd> -->
<!-- 						<dd>IFTBL : <input type="text" id="IFTBL" readonly></dd> -->
<!-- 						<dd>BWART : <input type="text" id="BWART" readonly></dd> -->
<!-- 					</dl> -->
					<dl>
						<dd>
							<table style="width:1000px;"> <!--  style="border:1px solid red;"> -->
								<tr>
									<td style="width:180px;vertical-align:middle;">생성일자 : <input type="text" id="LASTDAT" UIFormat="C N" style="width:80px;"/></td>
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