<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp"%>
<%
User user = (User) request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%>
<%@ page import="project.common.airfw.*"%>
<link rel="stylesheet" type="text/css"
	href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<style>
input[type="checkbox"] {
	height: unset;
}
</style>
<script type="text/javascript">
    var menuky = configData.MENU_ID;
    
    $(document).ready(function(){
        gridList.setGrid({
            id : "gridList",
            module : "WmsOutbound",
            command : "DL09",
            itemGrid : "gridListSub",
            itemSearch : true
        });
        
        gridList.setGrid({
            id : "gridListSub",
            module : "WmsOutbound",
            command : "DL09Sub"
        });
        
        //메뉴별(DL09/WDL09) 그룹오더 채크 여부
        var value = (menuky == "DL09") ? "" : "V";
        $("#GRORDYN").val(value);
        if(value == "V"){
            $("#GRORDYN").prop('checked', true);
            $('#SVBELN').hide();
        }
        
        $("#USERAREA").val("<%=user.getUserg5()%>");
        gridList.setReadOnly("gridList", true, ['OBPROTCT','OBPROTPT']);
        gridList.setReadOnly("gridListSub", true, ['OBPROT']);
        
        <%if (plant.equals("")) {%>
        $('select[name="SI.LOTA02"]').multipleSelect('checkAll');
       <%} else {%>
         $('select[name="SI.LOTA02"]').multipleSelect('setSelects', ['<%=plant%>']);
       <%}%>
       
        /* 출고유형 전체선택 */
        var $selRCPTTY = $('select[id="SH.SHPMTY"]');
        $selRCPTTY.multipleSelect('checkAll');
              
    });
    
    
    
    
    /* function gridListEventItemGridSearch(gridId, rowNum, itemGridList){
        if(gridId == "gridList"){
            var rowData = gridList.getRowData(gridId, rowNum);
            var param = inputList.setRangeParam("searchArea");
            param.putAll(rowData);
            
            gridList.gridList({
               id : "gridListSub",
               param : param
            });
        }
    } */
    
    function gridListEventItemGridSearch(gridId, rowNum, itemGridList){
        if(gridId == "gridList"){
            var param = getItemSearchParam(rowNum);
    
            gridList.gridList({
                id : "gridListSub",
                param : param
            });
        }
    }
    
    function getItemSearchParam(rowNum){
        var rowData = gridList.getRowData("gridList", rowNum);
        var param = inputList.setRangeParam("searchArea");
        param.putAll(rowData);
    
        return param;
    }
    
    function searchList(headList){
        if(validate.check("searchArea")){
            var param = inputList.setRangeParam("searchArea");
            param.put("PROGID", menuky);
            
            
            var rqshpd1 = param.get("RQSHPD1");
            var rqshpd2 = param.get("RQSHPD2");
            var date1 = uiList.getDateObj(rqshpd1);
            var date2 = uiList.getDateObj(rqshpd2);
            var day = (date2 - date1)/1000/60/60/24; 
            
            if(day > 30){
                commonUtil.msgBox("OUT_M0169");  //입출고 요청일자의 범위를 30일 이내로 지정해주세요.
                return;
            }else if(date1 > date2){
                commonUtil.msgBox("OUT_M0170");
                return;
            }
            
            gridList.resetGrid("gridList");
	        gridList.resetGrid("gridListSub");
	        
            gridList.gridList({
                id : "gridList",
                param : param
            });
        }
    }
    
    function inputListEventRangeDataChange(rangeName, singleData, rangeData){
        if(rangeName == "SH.SHPOKY" || rangeName == "SI.SVBELN" || rangeName == "SI.SEBELN" || rangeName == "SI.SKUKEY"){
            var dataCount = inputList.getDataCount("SH.SHPOKY")
                          + inputList.getDataCount("SI.SVBELN")
                          + inputList.getDataCount("SI.SEBELN")
                          + inputList.getDataCount("SI.SKUKEY");
            if(dataCount == 0){
                var now = new Date();
                now.setDate(now.getDate());
                var tmpValue = uiList.dateFormat(now, configData.COMMON_DATE_TYPE);
                $("#searchArea").find("[name=RQSHPD]").val(tmpValue);
                inputList.addValidation("RQSHPD", "required");
            }else{
                $("#searchArea").find("[name=RQSHPD]").val("");
                inputList.removeValidation("RQSHPD", "required");
            }
        }
    }
    
    function gridExcelDownloadEventBefore(gridId){
        if(gridId == "gridList"){
            var param = inputList.setRangeParam("searchArea");
            param.put("PROGID", menuky);
            return param;
        }else if(gridId == "gridListSub"){
            var rowNum = gridList.getSearchRowNum("gridList");
            if(rowNum == -1){
                return false;
            }else{
                var param = getItemSearchParam(rowNum);
                return param;
            }
        }
    }
    
    function cancelShipment(){
        if(gridList.validationCheck("gridList", "select")){
            if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
                return;
            }
            
            var head = gridList.getSelectData("gridList");
            
            if(head.length == 0){
                commonUtil.msgBox("VALID_M0006");
                return;
            }
            
            var param = new DataMap();
            param.put("head", head);
            
            var json = netUtil.sendData({
                url : "/wms/outbound/json/DL09CancelShipment.data",
                param : param
            });
            
            if(json.data != "OK"){
                var msgList = json.data.split("†");
                var msgTxt = commonUtil.getMsg(msgList[0], (msgList.length > 1 ? msgList[1].split("/") : null));
                commonUtil.msg(msgTxt);
                return;
            }
            
            if(json && json.data){
                searchList();
                commonUtil.msgBox("MASTER_M0564");
            }
        }
    }
    
    function PrintList(){
        var url = "";
        var prtseq = "";
        
        var head = gridList.getSelectData("gridList");
        
        if(head.length < 1){
            commonUtil.msgBox("VALID_M0006");
            return;
        }
        
        var param = new DataMap();
        param.put("head", head);
        
        // SELECT SEQ_PRTSEQ.NEXTVAL FROM DUAL
        var json = netUtil.sendData({
            module : "WmsOutbound",
            command : "SEQPRTSEQ",
            sendType : "map",
            param : param
        });
        
        var prtseq = json.data["PRTSEQ"];
        param.put("PRTSEQ", prtseq);
        
        var json = netUtil.sendData({
            url : "/wms/outbound/json/savePrtseqDL09.data",
            param : param
        });
    
        var where = "AND PRTSEQ IN ('" + prtseq + "')";
    
        url = "/ezgen/shipping_list.ezg";
        
        var map = new DataMap();
        WriteEZgenElement(url, where, "", '<%=langky%>', map, 850, 650);
    }
    
    function searchHelpEventOpenBefore(searchCode, gridType, $inputObj){
        var param = new DataMap();
        if(searchCode == "SHBZPTN"){
			param.put("PTNRTY", "CT");
			param.put("OWNRKY", "<%=ownrky%>");			
			return param;
		}else if(searchCode == "SHSKUMA"){
            param.put("OWNRKY", "<%=ownrky%>");
            return param;
        }else if(searchCode == "SHSTLMAP"){
	    	
	    	var PLNTKY = $('select[name="SI.LOTA02"]').val();
			
			if(!PLNTKY) {
				var label = commonLabel.getLabel("STD_LOTA02", 1);
				commonUtil.msgBox("VALID_required",[label]); // {0} 은(는) 필수 입력항목입니다.
				return false;
			}
			param.put("PLNTKY", PLNTKY);
	    	
	    	return param;
	    }
    }
    
    function commonBtnClick(btnName){
        if(btnName == "Search"){
            searchList();
        }else if(btnName == "Cancel"){
            cancelShipment();
        }else if(btnName == "Print"){
            PrintList();
        }
    }
    
    function comboEventDataBindeBefore(comboAtt){
        if(comboAtt == "Wms,PLNTKYCOMBO"){
            var param = new DataMap();
	        param.put("WAREKYS", "'<%=wareky%>'");
            param.put("OWNRKY", "<%=ownrky%>");
            param.put("LANGKY", "<%=langky%>");
            
            return param;
        }else if(comboAtt == "WmsOrder,RCPTTYCOMBO"){
        	
        	
            var param = new DataMap();

	 			param.put("PROGID", configData.MENU_ID);
            
            return param;
           
       }if(comboAtt == "Wms,DEPTID"){
			var param = new DataMap();
			param.put("WAREKY", "<%=wareky%>");
			return param;
	  }else if(comboAtt == "Wms,VTXTK"){
			var param = new DataMap();
			param.put("WAREKY", "<%=wareky%>");
			return param;
	   }else if(comboAtt == "Wms,KVGR"){
			var param = new DataMap();
			param.put("WAREKY", "<%=wareky%>");
			return param;
	   }
    }
</script>
</head>
<body>
	<%@ include file="/common/include/webdek/layout.jsp"%>
	<!-- content -->
	<div class="content_wrap">
		<div class="content_inner">
			<%@ include file="/common/include/webdek/title.jsp"%>
			<div class="content_serch" id="searchArea">
				<div class="btn_wrap">
					<div class="fl_l"></div>
					<div class="fl_r">
						<input type="button" CB="Search SEARCH BTN_DISPLAY" />
					</div>
				</div>
				<div class="search_inner">
					<div class="search_wrap ">
						<input type="hidden" class="input" name="OWNRKY" value="<%=ownrky%>" />
						<dl>
							<dt CL="STD_LOTA02">플랜트</dt>
							<dd>
								<select type="text" class="input" Combo="Wms,PLNTKYCOMBO"
									name="SI.LOTA02" id="SI.LOTA02" ComboType="MS"
									style="width: 160px;" validate="required">
								</select>
							</dd>
						</dl>
						<dl>
							<dt CL="STD_LOTA01">저장위치</dt>
							<dd>
								<input type="text" class="input" name="SI.LOTA01"
									UIInput="R,SHSTLMA" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_SKUKEY">품목코드</dt>
							<dd>
								<input type="text" class="input" name="SI.SKUKEY"
									UIInput="SR" UIFormat="U" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_DOCDAT">전기일자</dt>
							<dd>
								<input type="text" class="input" name="DOCDAT" UIFormat="C -7 7"
									UIInput="R" validate="required" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_RQSHPD">출하요청일자</dt>
							<dd>
								<input type="text" class="input" name="RQSHPD" UIFormat="C"
									UIInput="R" />
							</dd>
						</dl>

						<dl>
							<dt CL="STD_LSHPCD">출하일자</dt>
							<dd>
								<input type="text" class="input" name="LSHPCD" UIFormat="C"
									UIInput="R" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_LOTA14">개별바코드</dt>
							<dd>
								<input type="text" class="input" name="TI.LOTA14" UIInput="SR"
									UIFormat="U" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_STKNUM">선적번호</dt>
							<dd>
								<input type="text" class="input" name="SI.STKNUM" UIInput="SR" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_SSHTYP">출고유형</dt>
							<dd>
								<select type="text" class="input" Combo="WmsOrder,RCPTTYCOMBO"
									name="SH.SHPMTY" id="SH.SHPMTY" ComboType="MS"
									validate="required">
								</select>
							</dd>
						</dl>
						<dl>
							<dt CL="STD_SHPOKY">출하문서번호</dt>
							<dd>
								<input type="text" class="input" name="SH.SHPOKY" UIInput="SR" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_SVBELN">출고오더번호</dt>
							<dd>
								<input type="text" class="input" name="SI.SVBELN" UIInput="SR"
									UIFormat="U" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_SAP2">항차번호</dt>
							<dd>
								<input type="text" class="input" name="SH.SAP2" UIInput="SR" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_DPTNKY2">납품처</dt>
							<dd>
								<input type="text" class="input" name="SH.DPTNKY"
									UIInput="SR,SHBZPTN" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_DPTNNM3">납품처명</dt>
							<dd>
								<input type="text" class="input" name="CT.NAME01" UIInput="SR" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_KUNAG">판매처</dt>
							<dd>
								<input type="text" class="input" name="SH.PTRCVR"
									UIInput="SR,SHBZPTN" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_KUNAGNM">판매처명</dt>
							<dd>
								<input type="text" class="input" name="CT2.NAME01" UIInput="SR" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_DESC01">품목명</dt>
							<dd>
								<input type="text" class="input" name="SI.DESC01" UIInput="SR" />
							</dd>
						</dl>

						<dl>
							<dt CL="STD_SKUG01">품목유형</dt>
							<dd>
								<select class="input" name="SKUG01"
									Combo="WmsInventory,SKUG01COMBO"><option value="" /></select>
							</dd>
						</dl>

						<dl>
							<dt CL="STD_SKUL01">품목구분</dt>
							<dd>
								<select type="text" class="input" CommonCombo="SKUL01"
									name="SKUL01" id="SKUL01" style="width: 160px;">
									<option value=""></option>
								</select>
							</dd>
						</dl>
						<dl>
							<dt CL="STD_LOTA15">LOT 번호</dt>
							<dd>
								<input type="text" class="input" name="LOTA15" IAname="Search"
									UIFormat="U" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_BNAME">영업사원명</dt>
							<dd>
								<input type="text" class="input" name="SH.UNAME1" UIInput="SR" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_USRID1">영업사원ID</dt>
							<dd>
								<input type="text" class="input" name="SH.USRID1" UIInput="SR" />
							</dd>
						</dl>
						<dl>
							<dt CL="STD_DEPTID5">사업장ID</dt>
							<dd>
								<select type="text" class="input" Combo="Wms,DEPTID"
									name="SH.DEPTID1" id="DEPTID" ComboType="MS">
								</select>
							</dd>
						</dl>
						<dl>
							<dt CL="STD_KDGRP">고객그룹</dt>
							<dd>
								<input type="text" class="input" Combo="Wms,KDGRP"
									name="SH.VTXTK" id="VTXTK" ComboType="MS">
							</dd>
						</dl>
						<dl>
							<dt CL="STD_KVGR1">고객그룹1</dt>
							<dd>
								<input type="text" class="input" Combo="Wms,KVGR"
									name="SH.KVGR1" id="KVGR1" ComboType="MS">
							</dd>
						</dl>
						<dl>
							<dt CL="STD_WAREKY">거점</dt>
							<dd>
								<input type="text" class="input" name="WAREKY" name="WAREKY"
									value="<%=wareky%>" readonly="readonly" validate="required" />
							</dd>
						</dl>
						
						<!--  <dl>
	                        <dt CL="STD_TOTPICKNUM">TOTAL 피킹번호</dt>
	                        <dd>
	                            <input type="text" class="input" name="TP.STKNUM" UIInput="SR"/>
	                        </dd>	
	                    </dl> -->
					</div>
					<div class="btn_tab">
						<input type="button" class="btn_more" value="more"
							onclick="searchMore()" />
					</div>
				</div>
			</div>
			<div class="search_next_wrap">
				<div class="content_layout tabs top_layout">
					<ul class="tab tab_style02">
						<li><a href="#tab1-1"><span CL="STD_LIST">리스트</span></a></li>
						<li class="btn_zoom_wrap">
							<ul>
								<li><button class="btn btn_bigger">
										<span>확대</span>
									</button></li>
							</ul>
						</li>
					</ul>
					<div class="table_box section" id="tab1-1">
						<div class="table_list01">
							<div class="scroll">
								<table class="table_c">
									<tbody id="gridList">
										<tr CGRow="true">
											<td GH="40" GCol="rownum">1</td>
											<td GH="40" GCol="rowCheck"></td>
											<td GH="100 STD_SHPOKY" GCol="text,SHPOKY">출고문서번호</td>
											<td GH="100 STD_SVBELN" GCol="text,SVBELN">출고오더번호</td>
											<td GH="100 STD_STKNUM" GCol="text,STKNUM">선적번호</td>
											<td GH='100 STD_SAP2' GCol="text,SAP2">항차번호</td>
											<!-- <td GH="100 STD_TOTPICKNUM" GCol="text,TOTPICKNUM">	TOTAL 피킹번호</td>    -->
											<td GH="100 STD_DOCDAT" GCol="text,DOCDAT" GF="D">전기일자</td>
											<td GH="100 STD_STATDONM" GCol="text,STATNM">문서상태명</td>

											<td GH="100 STD_DPTNKY2" GCol="text,DPTNKY">납품처</td>
											<td GH="100 STD_DPTNNM3" GCol="text,DPTNNM">납품처명</td>
											<td GH="100 STD_KUNAG" GCol="text,PTRCVR">판매처</td>
											<td GH="100 STD_KUNAGNM" GCol="text,KUNAGNM">판매처명</td>
											<!-- <td GH="100 STD_INDSHP"     GCol="check,INDDCL">출고완료여부</td> -->

											<td GH="100 STD_VEHINO" GCol="text,CARNO">차량번호</td>
											<td GH="100 STD_WADT01" GCol="text,DRIVERCEL">연락처</td>

											<td GH="100 STD_RQSHPD" GCol="text,RQSHPD" GF="D">출하예정일자</td>
											<td GH="100 STD_LSHPCD" GCol="text,LSHPCD" GF="D">출하일자</td>

											<td GH="100 STD_DOCUTY" GCol="text,DOCUTY">문서유형</td>
											<td GH="100 STD_DOCUTYNM" GCol="text,DOCUTYNM">문서유형명</td>
											<td GH="100 STD_WAREKY" GCol="text,WAREKY">거점</td>
											<td GH="100 STD_WARENM" GCol="text,WARENM">거점명</td>
											<td GH="100 STD_SAPDOCNO" GCol="text,SXBLNR">SAP I/F
												문서번호</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="btn_lit tableUtil">
							<button type='button' GBtn="find"></button>
							<button type='button' GBtn="sortReset"></button>
							<button type='button' GBtn="total"></button>
							<button type='button' GBtn="layout"></button>
							<button type='button' GBtn="excel"></button>
							<button type='button' GBtn="subTotal"></button>
							<span class='txt_total'>총 건수 : <span GInfoArea='true'>0</span></span>
						</div>
					</div>
				</div>

				<div class="content_layout tabs bottom_layout">
					<ul class="tab tab_style02">
						<li><a href="#tab1-1"><span CL="STD_DETAILLIST">아이템
									리스트</span></a></li>
						<li class="btn_zoom_wrap">
							<ul>
								<li><button class="btn btn_bigger">
										<span>확대</span>
									</button></li>
							</ul>
						</li>
					</ul>
					<div class="table_box section" id="tab1-1">
						<div class="table_list01">
							<div class="scroll">
								<table class="table_c">
									<tbody id="gridListSub">
										<tr CGRow="true">

											<td GH="40" GCol="rownum">1</td>
											<td GH="100 STD_SHPOIT" GCol="text,SHPOIT">아이템번호</td>
											<td GH="100 STD_STATIT" GCol="text,STATNM">아이템상태</td>
											<td GH="100 STD_SKUKEY" GCol="text,SKUKEY">품목코드</td>
											<td GH="100 STD_DESC01" GCol="text,DESC01">품목명</td>
											<td GH="100 STD_QTYORG" GCol="text,QTYORG" GF="N 20,3">오더수량</td>
											<td GH="100 STD_QTALOC" GCol="text,QTALOC" GF="N 20,3">할당수량</td>
											<td GH="100 STD_QTJCMP" GCol="text,QTJCMP" GF="N 20,3">피킹수량</td>
											<td GH="100 STD_QTSHPD" GCol="text,QTSHPD" GF="N 20,3">출고수량</td>


											<td GH="100 STD_UOMKEY" GCol="text,UOMKEY">단위</td>
											<td GH="100 STD_SDUOKY" GCol="text,DUOMKY">기본단위</td>
											<!-- 	                                    <td GH="100 STD_SKUL01"     GCol="text,SKUL01">품목구분</td>	                                     -->

											<td GH="100 STD_TOTWG" GCol="text,TOT" GF="N 20,3">총중량</td>
											<td GH="100 STD_BOXBAG" GCol="text,BOXBAG" GF="N 20,3">BOXBAG</td>
											<td GH="100 STD_BAGQT" GCol="text,BAG" GF="N 20,3">BAG</td>
											<td GH="100 STD_BOXQT" GCol="text,BOX" GF="N 20,3">BOX</td>
											<td GH="100 STD_PLTQT" GCol="text,PLT" GF="N 20,3">Pallet</td>
											<td GH="100 STD_SOKQT" GCol="text,SOK" GF="N 20,3">속(개/BALE)</td>
											<td GH="100 STD_EAQTY" GCol="text,EA" GF="N 20,3">매(sheet/속)</td>

											<td GH="100 STD_LOTA02" GCol="text,LOTA02">플랜트</td>
											<td GH="110 STD_PLNTNM" GCol="text,LOTA02NM">플랜트명</td>
											<td GH="100 STD_LOTA01" GCol="text,LOTA01">저장위치</td>
											<td GH="130 STD_LOTA01NM" GCol="text,LOTA01NM">저장위치명</td>
											<td GH="100 STD_LOTA03" GCol="text,LOTA03">공급업체코드</td>
											<td GH="100 STD_LOTA08" Gcol="text,LOTA08">국가코드</td>
											<td GH="150 STD_NATNKYNM" Gcol="text,LOTA08NM">국가명</td>
											<td GH="100 STD_LOTA07" Gcol="text,LOTA07">MTO판매번호</td>
											<td GH="100 STD_LOTA09NM" GCol="text,LOTA09NM">거래처코드MTO</td>
											<td GH="100 STD_LOTA11" GCol="text,LOTA11" GF="C">제조일자</td>
											<td GH="100 STD_LOTA12" GCol="text,LOTA12" GF="C">입고일자</td>
											<td GH="100 STD_LOTA13" GCol="text,LOTA13" GF="C">유통기한</td>
											<!--<td GH="100 STD_LOTA14"     GCol="text,LOTA14" >제조LOT</td>-->
											<!--<td GH="100 STD_LOTA15"     GCol="text,LOTA15" >SAP 배치번호</td> -->

											<td GH="100 STD_REMAKS" GCol="text,DOCTXT">비고</td>

											<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="C">생성일자</td>
											<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
											<td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자ID</td>
											<td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자명</td>
											<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="C">수정일자</td>
											<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
											<td GH="100 STD_LMOUSR" GCol="text,LMOUSR">수정자ID</td>
											<td GH="100 STD_LUSRNM" GCol="text,LUSRNM">수정자명</td>

										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="btn_lit tableUtil">
							<button type='button' GBtn="find"></button>
							<button type='button' GBtn="sortReset"></button>
							<button type='button' GBtn="total"></button>
							<button type='button' GBtn="layout"></button>
							<button type='button' GBtn="excel"></button>
							<button type='button' GBtn="subTotal"></button>
							<span class='txt_total'>총 건수 : <span GInfoArea='true'>0</span></span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- // content -->
	<%@ include file="/common/include/webdek/bottom.jsp"%>
</body>
</html>