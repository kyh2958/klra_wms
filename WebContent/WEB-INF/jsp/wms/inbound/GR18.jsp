<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ page import="project.common.airfw.*"%>
<%@ include file="/common/include/webdek/head.jsp" %>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<style type="text/css">
 .content_wrap{background: none;}
</style>
<%
	User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%>
<script type="text/javascript">
	
	$(document).ready(function(){
		gridList.setGrid({
	    	id : "gridList",
			editable : true,
			module : "WmsInbound",
			command : "GR18"
	    });
	
		initSearchArea();
		
	});
	
	function initSearchArea() {
	  // 플랜트
      <%if(null!=plant && plant.trim().equals("")){%>
        $('select[id="PLNTKY"]').multipleSelect('checkAll');
      <%}else{%>
        $('select[id="PLNTKY"]').multipleSelect('setSelects', ['<%=plant%>']);
      <%}%>
   	  // 입고유형
	  var $selRCPTTY = $('select[id="RCPTTY"]');
	  $selRCPTTY.multipleSelect('checkAll');
	  // 아이템상태
	  var $selSTATIT = $('select[id="STATIT"]');
	  $selSTATIT.multipleSelect('checkAll');
      //$('select[id="SKUL01"]').val("");
    }
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}
	}
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			var docdatMin = $('#searchArea input[iacookie="IAname_GR18_RH.DOCDAT_RF"]').val().replaceAll(".", "");
			
			param.put("WAREKY", "<%= wareky%>");
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			param.put("DOCDATMIN", docdatMin);
			param.put("PROGID", configData.MENU_ID);

			var json = netUtil.sendData({
	            module : "WmsInbound",
				command : "GR18_RECVKYMIN",
				sendType : "list",
	            param : param,
	            async : false,	// 동기처리 적용
	        });
			
			if (json.data[0].RECVKY) {
				param.put("RECVKY_MIN", json.data[0].RECVKY);
			}
			
			gridList.gridList({
		    	id : "gridList",
		    	param : param
		    });
		}
	}
	
	function gridExcelDownloadEventBefore(gridId){
		var param = new DataMap();
		if(gridId == "gridList"){
			param.put("dataGridExcel", "입고내역상세_리스트");
		}
		
		return param;
	}
	
	function comboEventDataBindeBefore(comboAtt){
		
		if(comboAtt == "Wms,PLNTKYCOMBO"){
			var param = new DataMap();
			
			//var WAREKY_LIST = ['<%= wareky%>'];
			//param.put("WAREKY_LIST", WAREKY_LIST);
			
			var WAREKYS = "'<%= wareky%>'";
			param.put("WAREKYS", WAREKYS);
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			
			return param;
		}else if(comboAtt == "WmsOrder,RCPTTYCOMBO"){
			var param = new DataMap();
			param.put("DOCCAT", "100");
			param.put("PROGID", configData.MENU_ID);
			return param;
		}
		
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType, $inputObj){
		
		var param = new DataMap();
		
		if(searchCode == "SHSKUMA"){
			param.put("OWNRKY", "<%=ownrky%>");
			return param;
		}else if(searchCode == "SHBZPTN"){
			param.put("OWNRKY", "<%=ownrky%>");
			return param;
		}else if(searchCode == "SHSTLMAP"){
			var PLNTKY = $('select[id="PLNTKY"]').val();
        	param.put("LANGKY", "<%=langky%>");
            param.put("OWNRKY", "<%=ownrky%>");
            param.put("WAREKY", "<%=wareky%>");
            param.put("PLNTKY", PLNTKY);
            if(PLNTKY==null || $.trim(PLNTKY)==""){
            	return false;
            }
            return param;
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
					<input type="button" CB="Search SEARCH BTN_DISPLAY" />
					<!-- <input type="button" CB="GetVariant GETVARIANT BTN_GETVARIANT" />
					<input type="button" CB="SaveVariant SAVEVARIANT BTN_SAVEVARIANT" /> -->
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_LOTA02">플랜트</dt>
						<dd>
							<select type="text" class="input" Combo="Wms,PLNTKYCOMBO" name="RI.LOTA02" id="PLNTKY" ComboType="MS" style="width: 160px;" validate="required">
							</select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA01">저장위치</dt>
						<dd>
							<input type="text" class="input" name="RI.LOTA01" UIInput="R,SHSTLMAP" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_DOCDAT">전기일자</dt>
						<dd>
							<input type="text" class="input" name="RH.DOCDAT" UIInput="R" UIFormat="C -1"  validate="required" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_MATNR">품목코드</dt>
						<dd>
							<input type="text" class="input" name="RI.SKUKEY" UIInput="SR,SHSKUMA" UIFormat="U"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_RCPTTY">입고유형</dt>
						<dd>
<!-- 							<select Combo="WmsOrder,RCPTTYCOMBO" name="RCPTTY" id="RCPTTY" STYLE="width: 160px;"> -->
<!-- 								<option value=""></option> -->
<!-- 							</select> -->
							<select type="text" class="input" Combo="WmsOrder,RCPTTYCOMBO" name="RH.RCPTTY" id="RCPTTY" ComboType="MS" >
							</select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_PRODLOT">개별바코드</dt>
						<dd>
							<input type="text" class="input" name="RI.LOTA14" UIInput="SR" UIFormat="U"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA11">제조일자</dt>
						<dd>
							<input type="text" class="input" name="RI.LOTA11" UIInput="R" UIFormat="C"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA12">입고일자</dt>
						<dd>
							<input type="text" class="input" name="RI.LOTA12" UIInput="R" UIFormat="C"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_RECVKY">입고문서번호</dt>
						<dd>
							<input type="text" name="RH.RECVKY" UIInput="R"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_EBELN">오더번호</dt>
						<dd>
							<input type="text" name="RI.SEBELN" UIInput="R" UIFormat="U"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_DPTNKY3">공급사/반품매출처</dt>
						<dd>
							<input type="text" class="input" name="RH.DPTNKY" UIInput="SR,SHBZPTN" UIFormat="U"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_DPTNKY3NM">공급사/반품매출처명</dt>
						<dd>
							<input type="text" class="input" name="BT.NAME01" UIInput="SR" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_DESC01">품목명</dt>
						<dd>
							<input type="text" class="input" name="SM.DESC01" UIInput="SR" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_SKUG01">품목유형</dt>
						<dd>
							<select class="input" name="SKUG01" Combo="WmsInbound,SKUG01COMBO" style="width: 177px;">
								<option value=""/>
							</select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_SKUL01">품목구분</dt>
						<dd>
							<select CommonCombo="SKUL01" class="input" name="SKUL01" id="SKUL01" style="width: 177px;">
							  <option value=""></option>
							</select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA06">재고상태</dt>
						<dd>
							<select class="input" CommonCombo="LOTA06" name="LOTA06" id="LOTA06" STYLE="width: 177px;">
							  <option value=""></option>
							</select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA13">유통기한</dt>
						<dd>
							<input type="text" class="input" name="RI.LOTA13" UIInput="R" UIFormat="C"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_AREAKY">영역</dt>
						<dd>
							<input type="text" name="RI.AREAKY" UIInput="R,SHAREMA" />
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_ZONEKY">구역</dt>
						<dd>
							<input type="text" name="LM.ZONEKY" UIInput="R,SHZONMA" UIFormat="U"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOCAKY">지번</dt>
						<dd>
							<input type="text" name="RI.LOCAKY" UIInput="R,SHLOCMA" UIFormat="U"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_STATDO">문서상태</dt>
						<dd>
							<select class="input" id="STATDO" name="STATDO" Combo="WmsInbound,GR09_STATDO_COMBO" style="width: 177px;">
								<option value=""/>
							</select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA15">LOT번호</dt>
						<dd>
							<input type="text" name="RI.LOTA15" UIInput="SR" UIFormat="U"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_STKNUM">선적번호</dt>
						<dd>
							<input type="text" name="RI.STKNUM" UIInput="SR" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_WAREKY">거점</dt>
						<dd>
							<input type="text" class="input" name="WAREKY" value="<%=wareky%>" readonly="readonly" validate="required"/>
						</dd>
					</dl>
					
					<dl style="display:none;">
						<dt CL="STD_OWNRKY">화주</dt>
						<dd>
							<!-- <select type="text" class="input" Combo="WmsOrder,OWNRKYCOMBO" name="OWNRKY" id="OWNRKY" STYLE="width: 150px;">
							</select> -->
							<input type="text" class="input" name="OWNRKY" value="<%=ownrky %>" />
						</dd>
					</dl>
					<!-- 
					<dl>
						<dt CL="STD_BIZPTN">거래처</dt>
						<dd>
							<input type="text" class="input" name="RH.DPTNKY1" UIInput="R,SHBZPTN" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_DPTNNM">거래처명</dt>
						<dd>
							<input type="text" class="input" name="VD.NAME011" UIInput="R" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_STATIT">아이템상태</dt>
						<dd>
							<select type="text" class="input" CommonCombo="STATIT" name="RI.STATIT" id="STATIT" ComboType="MS" >
							</select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_TKZONE">작업구역</dt>
						<dd>
							<input type="text" name="LM.TKZONE" UIInput="R" />
						</dd>
					</dl>
					 -->
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
				
			</div>
		</div>
		<div class="search_next_wrap">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
					<li><a href="#tab1-1"><span CL="STD_LIST">탭메뉴1</span></a></li>  
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">번호</td>
										
										<td GH="100 STD_STKNUM" GCol="text,STKNUM">선적번호</td>
										<td GH="150 STD_RECVKY" GCol="text,RECVKY">입고문서번호</td>
										<td GH="100 STD_RECVIT" GCol="text,RECVIT">입고문서아이템</td>
										<td GH="100 STD_ORDDAT" GCol="text,ORDDAT" GF="D">오더일자</td>
<!-- 										<td GH="100 STD_LOTA15" GCol="text,LOTA15">SAP번호</td> -->
										<td GH="100 STD_DOCDAT" GCol="text,DOCDAT" GF="C N">전기일자</td>
										<td GH="70 STD_RCPTTY" GCol="text,RCPTTY">입고유형</td>
										<td GH="120 STD_RCPTTYNM" GCol="text,RCPTNM">입고유형명</td>
										<td GH="100 STD_STATIT" GCol="text,STATITNM">아이템상태</td>
										<td GH="120 STD_DPTNKY3" GCol="text,DPTNKY">공급사/반품매출처</td>
										<td GH="180 STD_DPTNKY3NM" GCol="text,DPTNNM">공급사/반품매출처명</td>
										<td GH="100 STD_LOCAKY" GCol="text,LOCAKY">로케이션</td>
										<td GH="150 STD_SKUKEY" GCol="text,SKUKEY">품번</td>
										<td GH="250 STD_DESC01" GCol="text,DESC01">품명</td>
										<td GH="100 STD_QTYRCV" GCol="text,QTYRCV" GF="N 20,3">입고수량</td>
										<td GH="100 STD_UOMKEY" GCol="text,UOMKEY">단위</td>
										<td GH="100 STD_SUOMQTY" GCol="text,QTYUOM" GF="N 20,3">기본단위수량</td>
										<td GH="100 STD_MEINS" GCol="text,DUOMKY">기본단위</td>
										
										<td GH="100 STD_LOTA02" GCol="text,LOTA02NM">플랜트</td>
										<td GH="80 STD_LOTA01" GCol="text,LOTA01">저장위치</td>
										<td GH="100 STD_STLNM" GCol="text,LOTA01NM">저장위치</td>
										
										<td GH="80 STD_LOTA04" GCol="text,LOTA04">호기</td>
										<td GH="100 STD_LOTA04NM" GCol="text,LOTA04NM">호기명</td>

										<td GH="100 STD_LOTA06" GCol="text,LOTA06">재고상태</td>
										<td GH="100 STD_LOTA06NM" GCol="text,LOTA06NM">재고상태명</td>
										<td GH="150 STD_NATNKY" GCol="text,LOTA08NM">국가</td>
										<td GH="120 STD_LOTA07" GCol="text,LOTA07">MTO판매번호</td>
										<td GH="120 STD_LOTA09" GCol="text,LOTA09">거래처코드MTO</td>
										<td GH="100 STD_LOTA11" GCol="text,LOTA11" GF="C N">제조일자</td>
										<td GH="100 STD_LOTA12" GCol="text,LOTA12" GF="C N">입고일자</td>
										<td GH="100 STD_LOTA13" GCol="text,LOTA13" GF="C N">유통기한</td>
										<td GH="150 STD_PRODLOT" GCol="text,LOTA14">제조LOT</td>
										<td GH="150 STD_LOTA15" GCol="text,LOTA15">SAP배치번호</td>
										<td GH="100 STD_RSNCOD" GCol="text,RSNCOD">사유코드</td>
										<td GH="100 STD_RSNCDNM" GCol="text,RSNCODNM">사유코드명</td>
										<td GH="150 STD_RSNTXT" GCol="text,RCPRSN">상세사유</td>
										<td GH="180 STD_DOCTXT" GCol="text,DOCTXT">비고</td>
										<td GH="100 STD_WAREKY" GCol="text,WAREKY">거점</td>
										<td GH="100 STD_WARENM" GCol="text,WARENM">거점명</td>
										<td GH="150 STD_SEBELN" GCol="text,SEBELN">구매오더번호</td>
										<td GH="100 STD_SEBELP" GCol="text,SEBELP">구매오더순번</td>
										<td GH="100 STD_OWNRKY" GCol="text,OWNRKY">화주</td>
										<td GH="100 STD_OWNRNM" GCol="text,OWNRNM">화주명</td>
										<td GH="100 STD_DOCCAT" GCol="text,DOCCAT">문서카테고리</td>
										<td GH="100 STD_DOCCATNM" GCol="text,DOCCATNM">문서카테고리명</td>
										<td GH="100 STD_AREAKY" GCol="text,AREAKY">영역</td>
										<td GH="100 STD_AREAKYNM" GCol="text,AREANM">영역명</td>
										<td GH="100 STD_ZONEKY" GCol="text,ZONEKY">구역</td>
										
										<td GH="100 STD_LOTA16" GCol="text,LOTA16">평량</td>
										<td GH="100 STD_LOTA03" GCol="text,LOTA03" >포장타입</td>
										<td GH="100 STD_LOTA17" GCol="text,LOTA17">컨테이너유형</td>
										
<!-- 										<td GH="100 STD_ASNDAT" GCol="text,ASNDAT" GF="C N">입고예정일자</td> -->
<!-- 										<td GH="100 STD_INDRCN" GCol="check,INDRCN">취소여부</td> -->
<!-- 										<td GH="100 STD_CRECVD" GCol="text,CRECVD" GF="D">입고취소 일자</td> -->
<!-- 										<td GH="100 STD_DESC02" GCol="text,DESC02">규격</td> -->
																				
										<td GH="100 STD_TOTWG" GCol="text,TOT" GF="N 20,3">총중량</td>
										<td GH="100 STD_BOXBAG" GCol="text,BOXBAG" GF="N 20,3">BOXBAG</td>
										<td GH="100 STD_BAGQT" GCol="text,BAG" GF="N 20,3">BAG</td>
										<td GH="100 STD_BOXQT" GCol="text,BOX" GF="N 20,3">BOX</td>
										<td GH="100 STD_PLTQT" GCol="text,PLT" GF="N 20,3">Pallet</td>
										<td GH="100 STD_SOKQT" GCol="text,SOK" GF="N 20,3">속(개/BALE)</td>
										<td GH="100 STD_EAQTY" GCol="text,EA" GF="N 20,3">매(sheet/속)</td>
										
										<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D">생성일자</td>
										<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
										<td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자명</td>
										<td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자ID</td>
										<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D">수정일자</td>
										<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
										<td GH="100 STD_LMOUSR" GCol="text,LMOUSR">수정자명</td>
										<td GH="100 STD_LUSRNM" GCol="text,LUSRNM">수정자ID</td>
										
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