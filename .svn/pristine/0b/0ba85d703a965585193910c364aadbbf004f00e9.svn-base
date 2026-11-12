<%@page import="project.common.airfw.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>물류관리 상세현황 - PS</title>

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

.main_layout {
    height: 650px;
}
</style>

<script type="text/javascript">

var searchFlag = 1;

$(document).ready(function(){

    gridList.setGrid({
        id      : "gridList",
        module  : "WmsOutbound",
        command : "DL27"
    });
});

function commonBtnClick(btnName){

    if(btnName == "Search"){
        searchList();
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

    /* PS */
    param.put("SKUG05", "10");
    param.put("DATE_TYPE", "CREDAT");

    gridList.resetGrid("gridList");

    gridList.gridList({
        id    : "gridList",
        param : param
    });
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

        <div class="content_serch" id="searchArea">

            <div class="btn_wrap">
                <div class="fl_l"></div>

                <div class="fl_r">
                    <input type="button" CB="Search SEARCH BTN_SEARCH" />
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
                        <dd>
                            <input type="checkbox"
                                   class="input"
                                   id="CFS_YN"
                                   name="CFS_YN"
                                   value="V"/>
                            <label for="CFS_YN">CFS창고</label>
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

            <div class="content_layout tabs main_layout">

                <ul class="tab tab_style02">
                    <li>
                        <a href="#tab1-1">
                            <span>차량상태 및 시간</span>
                        </a>
                    </li>

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

                                        <td GH="95 날짜"
                                            GCol="text,CREDAT"
                                            GF="C">날짜</td>

                                        <td GH="120 선적번호"
                                            GCol="text,STKNUM">선적번호</td>
										<td GH="120 차량번호"
										    GCol="text,CARNO">차량번호</td>
                                        <td GH="150 입문시간"
                                            GCol="text,ENTRY_DTM">입문시간</td>

                                        <td GH="150 접수시간"
                                            GCol="text,RECEIPT_DTM">접수시간</td>

                                        <td GH="150 할당완료시간"
                                            GCol="text,FAL_DTM">할당완료시간</td>

                                        <td GH="150 피킹완료시간"
                                            GCol="text,FPC_DTM">피킹완료시간</td>

                                        <td GH="150 상차완료시간"
                                            GCol="text,FCK_DTM">상차완료시간</td>

                                        <td GH="150 출고완료시간"
                                            GCol="text,FSH_DTM">출고완료시간</td>

                                        <td GH="150 출문시간"
                                            GCol="text,EXIT_DTM">출문시간</td>

                                        <td GH="140 운전기사 연락처"
                                            GCol="text,DRIVERCEL">운전기사 연락처</td>

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

                        <span class="txt_total">
                            총 건수 :
                            <span GInfoArea="true">0</span>
                        </span>
                    </div>

                </div>
            </div>
        </div>

    </div>