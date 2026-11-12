<%@page import="project.common.airfw.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>입차시스템 - PS</title>

<%@ include file="/common/include/webdek/head.jsp"%>

<%
User user = (User) request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%>

<link rel="stylesheet" type="text/css"
	href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<style type="text/css">


input[type="checkbox"] {
    height: unset;
}
#searchArea input[name="CREDAT_FROM"],
#searchArea input[name="CREDAT_TO"] {
    width: 90px;
}

/*
 * 상단 차량현황 그리드
 */
.top_layout {
    height: 430px;
}


/*
 * 하단 차량상태 상세 그리드
 */
.bottom_layout {
    height: 260px;
    margin-top: 10px;
}

/*
 * 선택 선적번호 표시
 */
.selected_stknum_wrap {
    float: left;
    height: 28px;
    line-height: 28px;
    padding-left: 10px;
    font-weight: bold;
}

.selected_stknum_label {
    margin-right: 5px;
}

#selectedStknum {
    color: #2456a6;
}

</style>

<script type="text/javascript">

    var searchFlag = 1;

    /*
     * 현재 상단 그리드에서 선택된 행
     */
    var selectedHeadRowNum = -1;

    /*
     * 긴급 여부 동기화 시 이벤트 재호출 방지
     */
    var urgentSyncFlag = false;

    $(document).ready(function(){

        /*
         * 상단 선적번호별 차량현황
         */
        gridList.setGrid({
            id       : "gridList",
            module   : "WmsOutbound",
            command  : "DL24",
            itemGrid : "gridListSub"
        });

        /*
         * 하단 출고문서별 상세현황
         */
        gridList.setGrid({
            id      : "gridListSub",
            module  : "WmsOutbound",
            command : "DL24_SUB",
            colorType : true
        });


        /*
         * 하단 그리드는 최초 빈 상태
         */
        gridList.resetGrid("gridListSub");
    });

    /*
     * 공통 버튼
     */
     function commonBtnClick(btnName){

    	    if(btnName == "Search"){

    	        searchList();

    	    }else if(btnName == "Save"){

    	        saveData();
    	    }
    	}

     function searchList(){

    	    if(!validate.check("searchArea")){
    	        return;
    	    }

    	    var param = inputList.setRangeParam("searchArea");

    	    param.put("WAREKY", "<%=wareky%>");
    	    param.put("OWNRKY", "<%=ownrky%>");
    	    param.put("LANGKY", "<%=langky%>");
    	    param.put("PROGID", configData.MENU_ID);

    	    /*
    	     * hl 사업부
    	     */
    	    param.put("SKUG05", "20");

    	    param.put("DATE_TYPE", "CREDAT");

    	    gridList.resetGrid("gridList");
    	    gridList.resetGrid("gridListSub");

    	    selectedHeadRowNum = -1;

    	    $("#selectedStknum").text("");

    	    gridList.gridList({
    	        id    : "gridList",
    	        param : param
    	    });
    	}
     function searchSubList(rowNum){

    	    var rowData = gridList.getRowData(
    	        "gridList",
    	        rowNum
    	    );

    	    if(!rowData){
    	        return;
    	    }

    	    var stknum = $.trim(
    	        rowData.get("STKNUM")
    	    );

    	    if(stknum == ""){
    	        return;
    	    }

    	    selectedHeadRowNum = rowNum;

    	    $("#selectedStknum").text(stknum);

    	    var param = new DataMap();

    	    param.put("WAREKY", "<%=wareky%>");
    	    param.put("OWNRKY", "<%=ownrky%>");
    	    param.put("LANGKY", "<%=langky%>");
    	    param.put("PROGID", configData.MENU_ID);
    	    param.put("STKNUM", stknum);

    	    /*
    	     * hl 사업부
    	     */
    	    param.put("SKUG05", "20");

    	    gridList.resetGrid("gridListSub");

    	    gridList.gridList({
    	        id    : "gridListSub",
    	        param : param
    	    });
    	}
   
     
    function gridListEventRowDblclick(
        gridId,
        rowNum,
        colName,
        colValue
    ){

        if(gridId != "gridList"){
            return;
        }

        searchSubList(rowNum);
    }

   
    function gridListEventRowClick(
    	    gridId,
    	    rowNum,
    	    colName
    	){

    	    if(gridId != "gridList"){
    	        return;
    	    }

    	    /*
    	     * 조회 전용 컬럼 클릭 차단
    	     */
    	    if(colName == "ENTRY_YN"
    	            || colName == "RECEIPT_YN"
    	            || colName == "FAL_YN"
    	            || colName == "FPC_YN"
    	            || colName == "FCK_YN"
    	            || colName == "FSH_YN"
    	            || colName == "EXIT_YN"
    	            || colName == "TDLNR"
    	            || colName == "CARART"
    	            || colName == "CARTON"){

    	        return false;
    	    }

    	    var rowData = gridList.getRowData(
    	        gridId,
    	        rowNum
    	    );

    	    if(!rowData){
    	        return;
    	    }

    	    $("#selectedStknum").text(
    	        $.trim(rowData.get("STKNUM"))
    	    );
    	}

    function saveData(){

        var allList = gridList.getGridData("gridList");
        var saveList = [];

        if(!allList || allList.length == 0){

            commonUtil.msgBox(
                "조회된 데이터가 없습니다."
            );

            return;
        }

        for(var i = 0; i < allList.length; i++){

            var row = allList[i];

            var rowState = $.trim(
                row.get("GRowState")
            );

            if(rowState == "C"
                    || rowState == "U"
                    || rowState == "D"){

                /*
                 * KIOSK_INOUT PK
                 */
                row.put("WAREKY", "<%=wareky%>");
                row.put("SKUG05", "20");

                saveList.push(row);
            }
        }

        if(saveList.length == 0){

            commonUtil.msgBox(
                "변경된 데이터가 없습니다."
            );

            return;
        }

        if(!commonUtil.msgConfirm(
            configData.MSG_MASTER_SAVE_CONFIRM
        )){
            return;
        }

        var param = new DataMap();

        param.put("list", saveList);

        param.put("WAREKY", "<%=wareky%>");
        param.put("OWNRKY", "<%=ownrky%>");
        param.put("LANGKY", "<%=langky%>");
        param.put("PROGID", configData.MENU_ID);

        /*
         * HL
         */
        param.put("SKUG05", "20");

        var json = netUtil.sendData({
            url   : "/wms/outbound/json/saveDL24.data",
            param : param
        });

        if(json
                && json.data
                && json.data.RESULT == "S"
                && Number(json.data.CNT) > 0){

            commonUtil.msgBox(
                "저장되었습니다."
            );

            searchList();

        }else{

            commonUtil.msgBox(
                "저장할 데이터가 없거나 저장에 실패했습니다."
            );
        }
    }

    function gridListEventColValueChange(
    	    gridId,
    	    rowNum,
    	    colName,
    	    colValue
    	){

    	    if(gridId != "gridList"){
    	        return;
    	    }

    	    var gridBox = gridList.gridMap.get(gridId);
    	    var rowData = gridList.getRowData(gridId, rowNum);

    	    /*
    	     * 조회 전용 컬럼
    	     * - 진행상황
    	     * - 운송/차량정보
    	     */
    	    if(colName == "ENTRY_YN"
    	            || colName == "RECEIPT_YN"
    	            || colName == "FAL_YN"
    	            || colName == "FPC_YN"
    	            || colName == "FCK_YN"
    	            || colName == "FSH_YN"
    	            || colName == "EXIT_YN"
    	            || colName == "TDLNR"
    	            || colName == "CARART"
    	            || colName == "CARTON"){

    	        /*
    	         * 변경값 다시 원래 조회값으로 복원
    	         */
    	        searchList();

    	        return;
    	    }

    	    /*
    	     * PS 사용자 입력 가능 항목
    	     */
    	    if(colName == "WH10_YN"
    	            || colName == "TURN_NO"
    	            || colName == "REMARK"){

    	        gridBox.setColValue(
    	            rowNum,
    	            "GRowState",
    	            "U"
    	        );
    	    }
    	}
     
     function gridListEventDataBindEnd(
    		    gridId,
    		    dataLength
    		){

    		    console.log(
    		        "[DataBindEnd]",
    		        "gridId =", gridId,
    		        "dataLength =", dataLength
    		    );

    		    if(gridId == "gridList"){

    		        /*
    		         * 조회된 실제 그리드 데이터 확인
    		         */
    		        var dataList = gridList.getGridData(gridId);

    		        console.log(
    		            "[DL24] 전체 데이터",
    		            dataList
    		        );

    		        for(var i = 0; i < dataList.length; i++){

    		            var rowData = dataList[i];


    		            /*
    		             * 값의 자료형과 공백 여부까지 확인
    		             */
    		            console.log(
    		                "[DL24] row " + i + " type/length",
    		                {
    		                    ENTRY: {
    		                        value: rowData.get("ENTRY_YN"),
    		                        type: typeof rowData.get("ENTRY_YN"),
    		                        length: String(
    		                            rowData.get("ENTRY_YN")
    		                        ).length
    		                    },
    		                    RECEIPT: {
    		                        value: rowData.get("RECEIPT_YN"),
    		                        type: typeof rowData.get("RECEIPT_YN"),
    		                        length: String(
    		                            rowData.get("RECEIPT_YN")
    		                        ).length
    		                    },
    		                    FAL: {
    		                        value: rowData.get("FAL_YN"),
    		                        type: typeof rowData.get("FAL_YN"),
    		                        length: String(
    		                            rowData.get("FAL_YN")
    		                        ).length
    		                    }
    		                }
    		            );
    		        }

    		        gridList.resetGrid("gridListSub");

    		        selectedHeadRowNum = -1;

    		        $("#selectedStknum").text("");
    		    }
    		}
    
     function searchHelpEventOpenBefore(
         searchCode,
         gridType,
         $inputObj
     ){

         var param = new DataMap();

      
         if(searchCode == "SHBZPTN"){

             param.put("PTNRTY", "CT");
             param.put("OWNRKY", "<%=ownrky%>");

             return param;
         }
     }
    function gridListRowBgColorChange(
        gridId,
        rowNum,
        rowData
    ){

        if(gridId == "gridListSub"){

            var statdo = rowData.get("STATDO");

            /*
             * 출고완료
             */
            if(statdo == "FSH"){
                return configData.GRID_COLOR_BG_GREEN_CLASS;
            }

            /*
             * 상차완료
             */
            if(statdo == "FCK"){
                return configData.GRID_COLOR_BG_YELLOW_CLASS;
            }
        }
    }

    /*
     * 운송사 콤보 조회 파라미터
     */
    function comboEventDataBindeBefore(comboAtt){

        if(comboAtt == "airfw,CARRIERCOMBO"){

            var param = new DataMap();

            param.put("WAREKY", "<%=wareky%>");
            param.put("OWNRKY", "<%=ownrky%>");

			return param;
		}
	}
</script>
</head>

<body>

	<%@ include file="/common/include/webdek/layout.jsp"%>

	<div class="content_wrap">
		<div class="content_inner">

			<%@ include file="/common/include/webdek/title.jsp"%>

			
<!-- <!-- 조회조건 -->
<div class="content_serch" id="searchArea">
    <div class="btn_wrap">
        <div class="fl_l">
        </div>

        <div class="fl_r">
            <input type="button" CB="Search SEARCH BTN_SEARCH" />
            <input type="button" CB="Save SAVE STD_SAVE" />
        </div>
    </div>
    <div class="search_inner">
        <div class="search_wrap">
            <input type="hidden"
                   class="input"
                   name="OWNRKY"
                   value="<%=ownrky%>" />
            <dl>
                <dt>기간</dt>
                <dd style="width: 280px;">
                    <input type="text"
                           class="input"
                           name="CREDAT_FROM"
                           UIFormat="C N"
                           validate="required"
                           style="width: 90px;" />
                    <span>~</span>
                    <input type="text"
                           class="input"
                           name="CREDAT_TO"
                           UIFormat="C N"
                           validate="required"
                           style="width: 90px;" />

                </dd>
            </dl>
            <dl>
                <dt>납품구분</dt>
                <dd style="width: 290px;">
                    <input type="checkbox"
                           class="input"
                           id="DELIVERY_DOMESTIC"
                           name="DELIVERY_DOMESTIC"
                           value="V"/>
                    <label for="DELIVERY_DOMESTIC">내수</label>
                    <input type="checkbox"
                           class="input"
                           id="DELIVERY_EXPORT"
                           name="DELIVERY_EXPORT"
                           value="V"/>
                    <label for="DELIVERY_EXPORT">수출</label>
                    <input type="checkbox"
                           class="input"
                           id="DELIVERY_TRANSFER"
                           name="DELIVERY_TRANSFER"
                           value="V"/>
                    <label for="DELIVERY_TRANSFER">재고이동</label>
                    <input type="checkbox"
                           class="input"
                           id="DELIVERY_OEM"
                           name="DELIVERY_OEM"
                           value="V"/>
                    <label for="DELIVERY_OEM">OEM입고</label>
                </dd>
            </dl>
            <dl>
                <dt>운송업체</dt>
                <dd>
                    <select class="input"
                            name="TDLNR"
                            Combo="airfw,CARRIERCOMBO">
                        <option value=""></option>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt>차량번호</dt>
                <dd>
                    <input type="text"
                           class="input"
                           name="CARNO" />
                </dd>
            </dl>
            <dl>
                <dt>납품처</dt>
                <dd>
                    <input type="text"
                           class="input"
                           name="DPTNKY"
                           UIInput="S,SHBZPTN" />
                </dd>
            </dl>
            <dl>
                <dt>납품처명</dt>
                <dd>
                    <input type="text"
                           class="input"
                           name="DPTNKYNM" />
                </dd>
            </dl>
            <dl>
                <dt>선적번호</dt>
                <dd>
                    <input type="text"
                           class="input"
                           name="STKNUM" />
                </dd>
            </dl>
            <dl>
                <dt>진행상황</dt>
                <dd style="width: 430px;">
                    <input type="checkbox"
                           class="input"
                           id="STATUS_ENTRY"
                           name="STATUS_ENTRY"
                           value="V"/>
                    <label for="STATUS_ENTRY">입문</label>
                    <input type="checkbox"
                           class="input"
                           id="STATUS_RECEIPT"
                           name="STATUS_RECEIPT"
                           value="V"/>
                    <label for="STATUS_RECEIPT">접수</label>
                    <input type="checkbox"
                           class="input"
                           id="STATUS_FAL"
                           name="STATUS_FAL"
                           value="V"/>
                    <label for="STATUS_FAL">할당</label>
                    <input type="checkbox"
                           class="input"
                           id="STATUS_FPC"
                           name="STATUS_FPC"
                           value="V"/>
                    <label for="STATUS_FPC">피킹</label>
                    <input type="checkbox"
                           class="input"
                           id="STATUS_FCK"
                           name="STATUS_FCK"
                           value="V"/>
                    <label for="STATUS_FCK">상차</label>
                    <input type="checkbox"
                           class="input"
                           id="STATUS_FSH"
                           name="STATUS_FSH"
                           value="V"/>
                    <label for="STATUS_FSH">출고</label>
                    <input type="checkbox"
                           class="input"
                           id="STATUS_EXIT"
                           name="STATUS_EXIT"
                           value="V"/>
                    <label for="STATUS_EXIT">출문</label>
                </dd>
            </dl>
        </div>
        <div class="btn_tab">
            <input type="button"
                   class="btn_more"
                   value="more"
                   onclick="searchMore()"/>
        </div>
    </div>
</div>

			<div class="search_next_wrap">
				<div class="content_layout tabs top_layout">
					<ul class="tab tab_style02">
						<li><a href="#tab1-1"> <span>차량 진행현황</span></a></li>
						<li class="btn_zoom_wrap">
							<ul>
								<li>
									<button class="btn btn_bigger">
										<span>확대</span>
									</button>
								</li>
							</ul>
						</li>
					</ul>
					<div class="table_box section" id="tab1-1">
						<div class="table_list01">
							<div class="scroll">
								<table class="table_c">
									<tbody id="gridList">
										<tr CGRow="true">
											<td GH="40 No." GCol="rownum">1</td>
											<td GH="95 날짜" GCol="text,CREDAT" GF="C">날짜</td>
											<td GH="120 선적번호" GCol="text,STKNUM">선적번호</td>
											<td GH="55 입문" GCol="check,ENTRY_YN">입문</td>
											<td GH="55 접수" GCol="check,RECEIPT_YN">접수</td>
											<td GH="55 할당" GCol="check,FAL_YN">할당</td>
											<td GH="55 피킹" GCol="check,FPC_YN">피킹</td>
											<td GH="55 상차" GCol="check,FCK_YN">상차</td>
											<td GH="55 출고" GCol="check,FSH_YN">출고</td>
											<td GH="55 출문" GCol="check,EXIT_YN">출문</td>
											<td GH="75 납품구분" GCol="text,DELIVERY_TYPE_NM">납품구분</td>
											<td GH="100 납품처" GCol="text,DPTNKY">납품처</td>
											<td GH="180 납품처명" GCol="text,DPTNKYNM">납품처명</td>
											<td GH="140 운송업체" GCol="text,TDLNR">운송업체</td>
											
											<td GH="85 차량구분" GCol="text,CARART">차량구분</td>
											
											<td GH="85 차량규격" GCol="text,CARTON">차량규격</td>
											<td GH="100 차량번호" GCol="text,CARNO">차량번호</td>
											<td GH="80 기사명" GCol="text,DRIVER">기사명</td>
											<td GH="110 기사연락처" GCol="text,DRIVERCEL">기사연락처</td>
											<td GH="80 수량" GCol="text,TOTAL_QTY" GF="N 20,3">수량</td>
											<td GH="60 단위" GCol="text,UOMKEY">단위</td>

											<!-- 창고 -->
											<td GH="70 천막창고" GCol="check,WH1_YN">천막창고</td>
											<td GH="70 화_3호기" GCol="check,WH2_YN">화_3호기</td>
											<td GH="65 패드창고" GCol="check,WH3_YN">패드창고</td>
											<td GH="65 동진창고" GCol="check,WH4_YN">동진창고</td>
											<td GH="65 원단창고" GCol="check,WH5_YN">원단창고</td>
											<td GH="65 강내창고" GCol="check,WH6_YN">강내창고</td>
											<td GH="65 음성창고" GCol="check,WH7_YN">음성창고</td>
											<td GH="180 비고" GCol="input,REMARK">비고</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="btn_lit tableUtil">
							<button type="button" GBtn="find"></button>
							<button type="button" GBtn="sortReset"></button>
							<button type="button" GBtn="layout"></button>
							<button type="button" GBtn="total"></button>
							<button type="button" GBtn="excel"></button>
							<span class="txt_total"> 총 건수 : <span GInfoArea="true">0</span>
							</span>
						</div>
					</div>
				</div>
				<!-- ===================================================== -->
				<!-- 하단: 선택 선적번호의 출고문서별 상세현황 -->
				<!-- ===================================================== -->

				<div class="content_layout tabs bottom_layout">
					<ul class="tab tab_style02">
						<li><a href="#tab1-2"> <span>차량상태 및 시간</span>
						</a></li>
						<li class="selected_stknum_wrap"><span
							class="selected_stknum_label"> 선택 선적번호 : </span> <span
							id="selectedStknum"></span></li>
						<li class="btn_zoom_wrap">
							<ul>
								<li>
									<button class="btn btn_bigger">
										<span>확대</span>
									</button>
								</li>
							</ul>
						</li>
					</ul>
					<div class="table_box section" id="tab1-2">
						<div class="table_list01">
							<div class="scroll">
								<table class="table_c">
									<tbody id="gridListSub">
										<tr CGRow="true">
											<td GH="40 No." GCol="rownum">1</td>
											<!-- 선택 선적번호 -->
											<td GH="120 선적번호" GCol="text,STKNUM">선적번호</td>
											<!-- TB_CARINOUT 입문시간 -->
											<td GH="150 입문시간" GCol="text,ENTRY_DTM">입문시간</td>
											<!-- PRTLG_KIOSK 접수시간 -->
											<td GH="150 접수시간" GCol="text,RECEIPT_DTM">접수시간</td>
											<!-- SHPDH FAL 완료시간 -->
											<td GH="150 할당완료시간" GCol="text,FAL_DTM">할당완료시간</td>
											<!-- SHPDH FPC 완료시간 -->
											<td GH="150 피킹완료시간" GCol="text,FPC_DTM">피킹완료시간</td>
											<!-- SHPDH FCK 완료시간 -->
											<td GH="150 상차완료시간" GCol="text,FCK_DTM">상차완료시간</td>
											<!-- SHPDH FSH 완료시간 -->
											<td GH="150 출고완료시간" GCol="text,FSH_DTM">출고완료시간</td>
											<!-- TB_CARINOUT 출문시간 -->
											<td GH="150 출문시간" GCol="text,EXIT_DTM">출문시간</td>
											<!-- SHPDH 운전기사 연락처 -->
											<td GH="140 운전기사 연락처" GCol="text,DRIVERCEL">운전기사 연락처</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div class="btn_lit tableUtil">
							<button type="button" GBtn="find"></button>
							<button type="button" GBtn="sortReset"></button>
							<button type="button" GBtn="layout"></button>
							<button type="button" GBtn="excel"></button>
							<span class="txt_total"> 총 건수 : <span GInfoArea="true">0</span>
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/common/include/webdek/bottom.jsp"%>
</body>
</html>