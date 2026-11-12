<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<head>
<style type="text/css">
button{ vertical-align: bottom; height: 37px; }
input:read-only{ background-color: #e2e2e2; }
select:disabled{ background-color: #e2e2e2; }
.tem5_content .tableWrap_search  { overflow: hidden; }
.tem4_content .select_box table  { width  : 98% !important; margin: auto; }
.tem4_content .select_box .text  { padding: unset !important; margin: unset !important; }
.tem4_content table th           { padding: 3px !important; }
.tem4_content table td           { padding: 3px !important; }
.number { text-align: right }

.additional { color: red ; display: none; font-size: 0.9rem; }
</style>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	$(document).ready(function(){

        gridList.setGrid({
            id : "gridList",
            editable : false,
            module : "WmsSystem",
            command : "PDIS01_POP",
            gridMobileType : true,
            emptyMsgType:false
        });
		
		var param = mobile.getLinkPopData();
        searchList(param);
	});
	
	
	// 목록 조회
	function searchList(param){
		$('#LOCAKY').text(param.map['LOCAKY']);
        gridList.gridList({
            id : "gridList",
            param : param
        });
	}
	
</script>
</head>
<body>
	<div id="menu-container">
        <div class="menu-item"></div>
    </div>
    <div class="main_wrap" id="paramArea">
        <div class="tem4_content">
            <div class="select_box" id="searchArea">
                <table>
                    <tbody>
                        <tr>
                            <th CL="STD_LOCAKY">지번</th>
                            <td id="LOCAKY">
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="tableWrap_search section">
                <div class="tableHeader">
                    <table >
                        <colgroup>
                            <col width="50px" />
                            <col width="150px" />
                            <col width="250px" />
                            <col width="100px" />
                            <col width="80px" />
                            <col width="100px" />
                            <col width="120px" />
                            <col width="150px" />
                            <col width="100px" />
                            <col width="100px" />
                        </colgroup>
                        <thead>
                            <tr class="thead">
                                <th CL='STD_NO'>순번</th>
                                <th GCol="STD_SKUKEY">품목코드</th>
                                <th GCol="STD_DESC01">품목명</th>
                                <th GCol="STD_QTYPHY">실사수량</th>
                                <th GCol="STD_UOMKEY">단위</th>
                                <th GCol="STD_LOTA02">플랜트</th>
                                <th GCol="STD_LOTA01">저장위치</th>
                                <th GCol="STD_PHYUSR">실사사용자</th>
                                <th GCol="STD_PHYDAT">실사일</th>
                                <th GCol="STD_PHYTIM">실사시간</th>
                            </tr>
                        </thead>
                    </table>
                </div>
                <div class="tableBody">
                    <table >
                        <colgroup>
                            <col width="50px" />
                            <col width="150px" />
                            <col width="250px" />
                            <col width="100px" />
                            <col width="80px" />
                            <col width="100px" />
                            <col width="120px" />
                            <col width="150px" />
                            <col width="100px" />
                            <col width="100px" />
                        </colgroup>
                        <tbody id="gridList">
                            <tr CGRow="true">
                                <td GCol="rownum">1</td>
                                <td GCol="text,SKUKEY"></td>
                                <td GCol="text,DESC01"></td>
                                <td GCol="text,QTYPHY" GF="N 20,3"></td>
                                <td GCol="text,UOMKEY,CENTER"></td>
                                <td GCol="text,LOTA02,CENTER"></td>
                                <td GCol="text,LOTA01,CENTER"></td>
                                <td GCol="text,PHYUSR" GF="C"></td>
                                <td GCol="text,PHYDAT,CENTER" GF="D"></td>
                                <td GCol="text,PHYTIM,CENTER" GF="T"></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- end table_body -->
            <div class="footer_5" style="position:fixed;">
                <table>
                    <tr>
                        <td class=f_1 width="100%" onclick="self.close()">
                            <label CL='BTN_CLOSE'></label>
                        </td>
                    </tr>
                </table>
            </div>
            <!-- end footer_5 -->

        </div>
    </div>
</body>