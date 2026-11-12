<%@page import="project.common.airfw.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript">
	window.resizeTo('1250','800');
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridListPOP",
			editable : true,
			module : "WmsInventory",
			command : "IP02POP"
		});

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
	
	function fn_save() {
		var chkList = gridList.getGridData("gridListPOP");

		if(chkList.length == 0){
			commonUtil.msgBox("ORDER_M0005"); // 저장할 데이터가 없습니다.
			return;
		}
		
		var PHYIKY = chkList[0].get("PHYIKY");
		var notEqualKey = chkList.find(data => data.get('PHYIKY') != PHYIKY)
		
		if(notEqualKey) {
			commonUtil.msgBox("VALID_M1613"); // 상이한 재고실사번호가 존재합니다.
			gridList.setRowFocus("gridListPOP", notEqualKey.get("GRowNum"));
			return;
		}
		
		// check validation
		if(gridList.validationCheck("gridListPOP", "data")) {
			//저장하시겠습니까?
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
				return;
			}
			
			var param = new DataMap();
			param.put("OWNRKY","<%=ownrky%>");
			param.put("list", chkList);
			
			var json = netUtil.sendData({
				url:"/wms/inventory/json/saveIP02POP.data",
				sendType : "map",
				param : param
			});
			
			if(json && json.data && json.data > 0){
				var failedList = json.data;
				if (failedList.length > 0) {
					var msg = "[";
					for (var i in failedList) {
						msg += i;
					}
					msg += "]";
					alert("실패건 발생: " + msg);
					return;
				} else {
					commonUtil.msgBox("MASTER_M0564", json.data); // 저장에 성공하였습니다.
					fn_closing();
				}
			} else {
				commonUtil.msgBox("MASTER_M0564", json.data); // 저장에 성공하였습니다.
				fn_closing();
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
					<input type="button" CB="Save SAVE BTN_SAVE" />
					<input type="button" CB="Check CHECK BTN_CLOSE" />
				</div>
			</div>
			
			<div class="search_inner">
				<div class="search_wrap">
					<dl>
						<dd>
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
					<li><a href="#tab1-1"><span CL="BOTTOM_EXCLUPLOAD">리스트</span></a></li>
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridListPOP">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="100 STD_PHYIKY"		GCol="text,PHYIKY"				>재고실사번호	</td>
										<td GH="80 STD_PHYIIT"		GCol="text,PHYIIT"				>재고실사순번	</td>
										<td GH="60 STD_AREAKY"		GCol="text,AREAKY"				>영역			</td>
										<td GH="60 STD_ZONEKY"		GCol="text,ZONEKY"				>구역			</td>
										<td GH="80 STD_LOCAKY"		GCol="text,LOCAKY"				>로케이션		</td>
										<td GH="100 STD_SKUL01"		GCol="text,SKUL01NM"			>품목구분		</td>
										<td GH="100 STD_SKUKEY"		GCol="text,SKUKEY"				>품목코드		</td>
										<td GH="100 STD_DESC01"		GCol="text,DESC01"				>품목명		</td>
										<td GH="100 STD_LOTA06NM"	GCol="text,LOTA06NM"			>재고상태		</td>
										<td GH="100 STD_QTYPDA"		GCol="text,QTYPDA"	GF="N 20,3"	>PDA실사수량	</td>
										<td GH="100 STD_QTSPHY"		GCol="text,QTSPHY"	GF="N 20,3"	>재고조사수량	</td>
										<td GH="100 STD_QTADJU"		GCol="text,QTADJU"	GF="N 20,3"	>조정수량		</td>
										<td GH="100 STD_QTSIWH"		GCol="text,QTSIWH"	GF="N 20,3"	>재고수량		</td>
										<td GH="100 STD_USEQTY"		GCol="text,QTABLE"	GF="N 20,3"	>가용수량		</td>
										<td GH="100 STD_QTSALO"		GCol="text,QTSALO"	GF="N 20,3"	>할당수량		</td>
										<td GH="100 STD_QTSPMO"		GCol="text,QTSPMO"	GF="N 20,3"	>이동OUT		</td>
										<td GH="100 STD_QTSPMI"		GCol="text,QTSPMI"	GF="N 20,3"	>이동IN		</td>
										<td GH="60 STD_UOMKEY"		GCol="text,UOMKEY"				>단위			</td>
										<td GH="100 STD_TOTWG"		Gcol="text,TOTWG"	GF="N 20,3"	>총중량		</td>
										<td GH="100 STD_BAGQT"		Gcol="text,BAGQT"	GF="N 20,3"	>EA/BAG		</td>
										<td GH="100 STD_BOXQT"		Gcol="text,BOXQT"	GF="N 20,3"	>BAG/BOX	</td>
										<td GH="100 STD_PLTQT"		Gcol="text,PLTQT"	GF="N 20,3"	>BOX/Pallet	</td>
										<td GH="100 STD_SOKQT"		Gcol="text,SOKQT"	GF="N 20,3"	>속(개/BALE)	</td>
										<td GH="100 STD_EAQTY"		Gcol="text,EAQTY"	GF="N 20,3"	>매(sheet/속)	</td>
										<td GH="60 STD_LOTA02"		GCol="text,LOTA02"				>플랜트		</td>
										<td GH="60 STD_LOTA01"		GCol="text,LOTA01"				>저장위치		</td>
										<td GH="100 STD_LOTA04"		Gcol="text,LOTA04"				>호기			</td>
										<td GH="100 STD_LOTA08"		GCol="text,LOTA08"				>국가			</td>
										<td GH="100 STD_LOTA07"		GCol="text,LOTA07"				>판매번호		</td>
										<td GH="100 STD_LOTA09"		GCol="text,LOTA09"				>거래처코드MTO	</td>
										<td GH="100 STD_LOTA11"		GCol="text,LOTA11"	GF="C"		>제조일자		</td>
										<td GH="100 STD_LOTA12"		GCol="text,LOTA12"	GF="C"		>입고일자		</td>
										<td GH="100 STD_LOTA13"		GCol="text,LOTA13"	GF="C"		>유통기한		</td>
										<td GH="100 STD_LOTA14"		GCol="text,LOTA14"				>개별바코드	</td>
										<td GH="100 STD_LOTA15"		GCol="text,LOTA15"				>LOT번호		</td>
										<td GH="100 STD_LOTA03"		Gcol="text,LOTA03"				>포장타입		</td>
										<td GH="100 STD_LOTA16"		Gcol="text,LOTA16"	GF="N 20,3"	>평량			</td>
										<td GH="100 STD_LOTA17"		Gcol="text,LOTA17"				>컨테이너유형	</td>
										<td GH="100 STD_CREDAT" 	GCol="text,CREDAT"	GF="D"		>생성일자		</td>
										<td GH="100 STD_CRETIM" 	GCol="text,CRETIM"	GF="T"		>생성시간		</td>
										<td GH="100 STD_CREUSR" 	GCol="text,CREUSR"				>생성자ID		</td>
										<td GH="100 STD_CUSRNM" 	GCol="text,CUSRNM"				>생성자명		</td>
										<td GH="100 STD_LMODAT" 	GCol="text,LMODAT"	GF="D"		>수정일자		</td>
										<td GH="100 STD_LMOTIM" 	GCol="text,LMOTIM"	GF="T"		>수정시간		</td>
										<td GH="100 STD_LMOUSR" 	GCol="text,LMOUSR"				>수정자ID		</td>
										<td GH="100 STD_LUSRNM" 	GCol="text,LUSRNM"				>수정자명		</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type="button" GBtn="find"></button>
						<button type="button" GBtn="sortReset"></button>
						<button type="button" GBtn="excelUpload"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- //content -->
<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>