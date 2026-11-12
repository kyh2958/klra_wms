<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp" %>

<script type="text/javascript">

$(document).ready(function(){

    gridList.setGrid({
        id : "gridList",
        module : "WmsMobileOutboundPicking_COPY",
        command : "PALLET_IF_RESEND"
    });

});

function commonBtnClick(btnName){
    if(btnName == "Search"){
        searchList();
    }else if(btnName == "Save"){
        resendIf();
    }
}

function searchList(){

    if(validate.check("searchArea")){
        var param = inputList.setRangeParam("searchArea");

        gridList.resetGrid("gridList");

        gridList.gridList({
            id : "gridList",
            param : param
        });
    }
}

function resendIf(){

    var list = gridList.getSelectData("gridList");

    if(list.length == 0){
        commonUtil.msgBox("VALID_M0006");
        return;
    }

    if(!commonUtil.msgConfirm("선택한 미처리 건을 I/F 재전송 하시겠습니까?")){
        return;
    }

    var param = new DataMap();
    param.put("list", list);
    param.put("WAREKY", "<%=wareky%>");
    param.put("OWNRKY", "<%=ownrky%>");
    param.put("LANGKY", "<%=langky%>");

    netUtil.sendData({
        url : "/mobile/Mobile/json/resendPalletIf.data",
        param : param,
        async : true,
        loading : true,
        callBackFunction : function(json){

            if(json && json.data){
                commonUtil.msgBox(
                    "재전송 완료\n성공 : "
                    + json.data.SUCCESS_CNT
                    + "건\n실패 : "
                    + json.data.FAIL_CNT
                    + "건"
                );

                searchList();
            }

            commonUtil.displayLoading(false);
        }
    });
}

</script>
</head>

<body>
<%@ include file="/common/include/webdek/layout.jsp" %>

<div class="content_wrap">
    <div class="content_inner">
        <%@ include file="/common/include/webdek/title.jsp" %>

        <div class="content_serch" id="searchArea">
            <div class="btn_wrap">
                <div class="fl_r">
                    <input type="button" CB="Search SEARCH STD_SEARCH" />
                    <input type="button" CB="Save SAVE I/F재전송" />
                </div>
            </div>

            <div class="search_inner">
                <div class="search_wrap">

                    <dl>
                        <dt>전송일자</dt>
                        <dd>
                            <input type="text" class="input"
                                   name="W.SEND_DAT"
                                   UIFormat="C -7 7"
                                   UIInput="R"
                                   validate="required"/>
                        </dd>
                    </dl>

                    <dl>
                        <dt>선적번호</dt>
                        <dd>
                            <input type="text" class="input"
                                   name="WMS_STKNUM"
                                   UIInput="SR"/>
                        </dd>
                    </dl>

                    <dl>
                        <dt>납품처명</dt>
                        <dd>
                            <input type="text" class="input"
                                   name="CST_ARV_CST_NAM"
                                   UIInput="SR"/>
                        </dd>
                    </dl>

                </div>
            </div>
        </div>

        <div class="search_next_wrap">
            <div class="content_layout tabs top_layout">
                <ul class="tab tab_style02">
                    <li><a href="#tab1-1"><span>미처리 I/F 리스트</span></a></li>
                </ul>

                <div class="table_box section" id="tab1-1">
                    <div class="table_list01">
                        <div class="scroll">
                            <table class="table_c">
                                <tbody id="gridList">
                                    <tr CGRow="true">
                                        <td GH="40" GCol="rownum">1</td>
                                        <td GH="40" GCol="rowCheck"></td>
                                        <td GH="80 업체" GCol="text,IF_VNDR_CD">업체</td>
                                        
              
                                        <td GH="100 전송일자" GCol="text,SEND_DAT" GF="C">전송일자</td>
                                        <td GH="100 순번" GCol="text,SEND_SEQ">순번</td>
                                        <td GH="130 IF_KEY" GCol="text,IF_KEY">IF_KEY</td>
                                        <td GH="130 EXT_IF_KEY" GCol="text,EXT_IF_KEY">EXT_IF_KEY</td>
                                        <td GH="120 선적번호" GCol="text,WMS_STKNUM">선적번호</td>
                                        <td GH="120 납품처" GCol="text,WMS_DPTNKY">납품처</td>
                                        <td GH="180 납품처명" GCol="text,CST_ARV_CST_NAM">납품처명</td>
                                        <td GH="80 구분" GCol="text,DATA_TYP">구분</td>
                                        <td GH="80 상태" GCol="text,WMS_STATUS">WMS상태</td>
                                        <td GH="100 품목" GCol="text,PRD_COD">품목</td>
                                        <td GH="100 품목명" GCol="text,ITEM">품목명</td>
                                        <td GH="80 수량" GCol="text,DLV_QTY" GF="N 20,3">수량</td>
                                        <td GH="130 이동전표" GCol="text,CHIT_NUM">CHIT_NUM</td>
                                        <td GH="100 차량번호" GCol="text,CAR_NUM">차량번호</td>
                                        <td GH="80 IF상태" GCol="text,IF_STATUS">IF상태</td>
                                        <td GH="300 오류메시지" GCol="text,IF_ERR_MSG">오류메시지</td>
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
                        <span class='txt_total'>총 건수 : <span GInfoArea='true'>0</span></span>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>