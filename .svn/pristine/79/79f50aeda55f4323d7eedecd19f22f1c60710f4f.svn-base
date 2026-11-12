<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp"%>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<script type="text/javascript">
	//var dblIdx = -1;
	var headFocusNum = -1;
	var dblIdx = 0;
	$(document).ready(function() {
		gridList.setGrid({
			id : "gridList",
			name : "gridList",
			editable : true,
			pkcol : "OWNRKY,SKUKEY",
			module : "WmsAdmin",
			command : "SKUMA",
			validation : "OWNRKY,WAREKY,SKUKEY,MEASKY,LOCARV,DPUTLO",
			bindArea : "tabs1-2"
		});
		gridList.setReadOnly('gridList',true, ['DELMAK','SKUG01','ASKU05','SKUL01','SKUL02']);
		
		$("#WAREKY").val("<%=wareky%>");
		
	});

	function searchList() {
		if (validate.check("searchArea")) {
			var param = inputList.setRangeParam("searchArea");
			gridList.gridList({
				id : "gridList",
				param : param
			});
		}
	}
	
	 function saveData(){
		if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
			return;
		} 
		if(gridList.validationCheck("gridList", "modify")){
			var head = gridList.getModifyList("gridList", "A");
			
			var vparam = new DataMap();
			vparam.put("list", head);

			if(head.length > 0) {
				vparam.put("key", "OWNRKY,WAREKY,SKUKEY,MEASKY,LOCARV,PASTKY,DPUTLO,ALSTKY,DPUTZO");
				var json = netUtil.sendData({
					url : "/wms/admin/json/validationSK01.data",
					param : vparam
				});
				
				if(json.data != "OK"){
					var msgList = json.data.split(" ");
					//var msgTxt = commonUtil.getMsg(msgList[0], msgList.pop());
					var msgTxt = commonUtil.getMsg(msgList[0], msgList[1]);
					commonUtil.msgBox(msgTxt);
					return false;
				}
			    
				var param = new DataMap();
				param.put("head", head);
				
				var json = netUtil.sendData({
					url : "/wms/admin/json/saveSK01.data",
					param : param
				});
				
				if(json && json.data){
					commonUtil.msgBox("HHT_T0008");
					searchList();
				}
			}else{
				commonUtil.msgBox("MASTER_M0545");	// 변경된 데이터가 없습니다.
				searchList();
			}
		}
	}
	 
	/* function sendSynch(){
		var list = gridList.getFocusRowNum("gridList");
		var param = new DataMap();
		param.put("list", list);
		
		var json = netUtil.sendData({
			url : "/wms/admin/json/sendSynch.data",
			param : param
		});
	 } */

<%-- 	function gridListEventRowAddBefore(gridId, rowNum) {
		dblIdx = rowNum;
		
		var param=new DataMap();
		param.put("WAREKY","<%=wareky%>");
		var json = netUtil.sendData({
			module : "WmsAdmin",
			command : "WAHMA_ADDR05",
			sendType : "map",
			param : param
		});
		
		var newData = new DataMap();
		newData.put("OWNRKY", "<%=ownrky%>");
		newData.put("WAREKY", "<%=wareky%>");
		newData.put("SKUG01", "001"); 
		newData.put("DUOMKY", "EA");
		newData.put("WEIGHT", "0");
		newData.put("LENGTH", "0");
		newData.put("WIDTHW", "0");
		newData.put("HEIGHT", "0");
		newData.put("CUBICM", "0");
		newData.put("CAPACT", "0"); 
		newData.put("QTYBOX", "0");
		newData.put("QTYCNT", "0");
		newData.put("QTDUOM", "0");
		
		return newData;
	} --%>
	
	function searchHelpEventOpenBefore(searchCode, gridType){
		if(searchCode == "SHWAHMA"){
			return dataBind.paramData("searchArea");
		} else if(searchCode == "SHZONMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHMEASH"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHVRCVLO1"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHPASTH"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHALSTH"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHSKUMA"){
			var param = dataBind.paramData("searchArea");
			param.put("OWNRKY", "<%=ownrky%>");
			return param;
		}else if(searchCode=="SHCMCDV"){
			var param = new DataMap();
			param.put("CMCDKY", "SKUG01");
			return param;
		}
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Print"){
			reportPrint();
		}/* else if(btnName == "Reflect"){
			sendSynch();
		} */
	}
	
	function reportPrint(){
		
		var head = gridList.getSelectData("gridList");
		
		if (head.length == 0){
			commonUtil.msgBox("VALID_M0006");
			return;
		}
		
		var url = "";
		var where = "AND (";
		for(var i=0; i<head.length; i++){
			var row = head[i];
			var key = row.get("SKUKEY");
			var ownrky = row.get("OWNRKY");
			where += "SKUKEY='"+key+"' AND OWNRKY='"+ownrky+"'";
			if(i==head.length-1) break;
			where += " OR ";	
		}
		where += ")"; 
		url = "/ezgen/skuba_s.ezg";
		
		var map = new DataMap();
		WriteEZgenElement(url, where, "", "<%=langky%>", map, 900, 350);
	}
</script>
</head>
<body>
	<div class="contentHeader">
		<div class="util">
			<button CB="Search SEARCH BTN_DISPLAY"></button>
			<button CB="Save SAVE STD_SAVE"></button>
			<button CB="Print PRINT BTN_SKUPRINT"></button>
<!-- 			<button CB="Reflect REFLECT BTN_SYNCH"></button> -->
		</div>
		<div class="util3">
			<button class="button type2" id="showPop" type="button">
				<img src="/common/images/ico_btn4.png" alt="List" />
			</button>
		</div>
	</div>

	<!-- searchPop -->
	<div class="searchPop" id="searchArea">
		<button type="button" class="closer">X</button>
		<div class="searchInnerContainer">
			<p class="util">
				<button CB="Search SEARCH BTN_DISPLAY"></button>
				<!-- <button CB="GetVariant GETVARIANT BTN_GETVARIANT"></button>
				<button CB="SaveVariant SAVEVARIANT BTN_SAVEVARIANT"></button> -->
			</p>
			
			<div class="searchInBox">
				<h2 class="tit" CL="STD_SELECTOPTIONS">검색조건</h2>
				<table class="table type1">
					<colgroup>
						<col width="100" />
						<col />
					</colgroup>
					<tbody>
						<tr>
							<th CL="STD_WAREKY">거점</th>
							<td><input type="text" name="WAREKY" id="WAREKY" readonly="readonly" validation="required,VALID_M0401" /></td>
						</tr>
						<!-- <tr>
							<th CL="STD_OWNRKY">화주</th>
							<td>
								<input type="text" name="A.OWNRKY" UIInput="R" />
							</td>
						</tr> -->
						<tr>
						<th CL="STD_SAEDAT,3">생성일자</th>
							<td>
								<input type="text" name="A.CREDAT" UIInput="R" UIFormat="C" />
							</td>
						</tr>
						<tr>
							<th CL="STD_SKUKEY">품번코드</th>
							<td><input type="text" name="A.SKUKEY" UIInput="R,SHSKUMA" /></td>
						</tr>
						<tr>
							<th CL="STD_DESC01">품명</th>
							<td><input type="text" name="A.DESC01" UIInput="R" /></td>
						</tr>
						<!-- <tr>
							<th CL="STD_DESC02">모델</th>
							<td><input type="text" name="A.DESC02" UIInput="R" /></td>
						</tr>
						 <tr>
							<th CL="STD_SKUG01">품목대분류</th>
							<td><input type="text" name="A.SKUG01" UIInput="R,SHCMCDV" /></td>
						</tr>
						<tr>
							<th CL="STD_SKUG05">규격</th>
							<td><input type="text" name="A.SKUG05" UIInput="R" /></td>
						</tr> -->
						<tr>
						<th CL="STD_GTINCD">바코드</th>
						<td>
							<input type="text" name="GTINCD"  UIInput="R" />
						</td>
					</tr>
						<tr>
							<th CL="STD_DELMAK">삭제여부</th>
							<td>
								<input type="checkbox" id="DELMAK" name="DELMAK" value="V"/>
							</td>
						</tr>
					</tbody>
				</table>

			</div>
		</div>

		<!-- <div class="searchInnerContainer">
			<div class="searchInBox">
				<h2 class="tit" CL="STD_STRATEGYINFO"></h2>
				<table class="table type1">
					<colgroup>
						<col width="100" />
						<col />
					</colgroup>
					<tbody>
						<tr>
							<th CL="STD_PASTKY">적치전략키</th>
							<td><input type="text" name="B.PASTKY" UIInput="R,SHPASTH" /></td>
						</tr>
						<tr>
							<th CL="STD_ALSTKY">할당전략키</th>
							<td><input type="text" name="B.ALSTKY" UIInput="R,SHALSTH" /></td>
						</tr>
						<tr>
							<th CL="STD_IBPROT">입고불허</th><td>
							<select Combo="WmsAdmin,IBPROTCOMBO" name="IBPROT" id="IBPROT" validate="required">
								<option value="">선택</option>
							</select>
						</td>
						</tr>
						<tr>
							<th CL="STD_OBPROT">출고제한상품</th><td>
							<select Combo="WmsAdmin,OBPROTCOMBO" name="OBPROT" id="OBPROT" validate="required">
								<option value="">선택</option>
							</select>
						</td>
						</tr>
					</tbody>
				</table>

			</div>
		</div> -->

	</div>
	<!-- //searchPop -->

<!-- content -->
	<div class="content">
		<div class="innerContainer">

			<!-- contentContainer -->
			<div class="contentContainer">

				<div class="bottomSect type1">
					<div class="tabs">
						<ul class="tab type2">
							<li><a href="#tabs1-1"><span CL='STD_LIST'>리스트</span></a></li>
							<li style="display:none"><a href="#tabs1-2"><span CL='STD_DETAIL'>탭메뉴2</span></a></li>
						</ul>
						<div id="tabs1-1">
							<div class="section type1">
								<div class="table type2">
									<div class="tableHeader">
										<table>
										<colgroup>
											<col width="40" />
											<col width="40" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" /> 
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											
											<col width="100" />
											
											<col width="100" />
											<col width="100" />
											<col width="100" />
											
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
										</colgroup>
										<thead>
											<tr>
												<th CL='STD_NUMBER'>번호</th>
												<th GBtnCheck="true"></th>
												<th CL='STD_WAREKY'>거점</th>
												<th CL='STD_DELMAK'>삭제</th>
												<th CL='STD_SKUKEY'>품번코드</th>
												<th CL='STD_DESC01'>품명</th>
												<th CL='STD_DUOMKY'>기본단위</th>
												<th CL='STD_DPUTZO'>기본적치구역</th>
												<th CL='STD_DPUTAR'>기본적치영역</th>
												<th CL='STD_SKUG02'>즉시불출여부</th>
												<th CL='STD_GTINCD'>바코드</th>
												<th CL='STD_SKUG01'>품목유형</th>
												<th CL='STD_SKUG01NM'>품목유형명</th>
												<th CL='STD_SKUL01'>품목구분</th>
												<th CL='STD_SKUL01NM'>품목구분명</th>
												<th CL='STD_SKUG05'>제품군</th>
												<th CL='STD_SKUG05NM'>제품군명</th>
												<th CL='STD_LENGTH'>가로</th>
												<th CL='STD_WIDTHW'>세로</th>
												<th CL='STD_HEIGHT'>높이</th>
												<th CL='STD_CUBICM'>CBM</th>
												
												<th CL='STD_ASKU04'>유통기간</th>
												<!-- <th CL='STD_PLOCOV'>Default picking location for over allocation</th> -->
												<th CL='STD_PASTKY'>적치전략키</th>
												<th CL='STD_ALSTKY'>할당전략키</th>
												<th CL='STD_MEASKY'>단위구성코드</th>
												
												<th CL='STD_CREDAT'>생성일자</th>
												<th CL='STD_CRETIM'>생성시간</th>
												<th CL='STD_CREUSR'>생성자</th>
												<th CL='STD_CUSRNM'>생성자명</th>
												<th CL='STD_LMODAT'>수정일자</th>
												<th CL='STD_LMOTIM'>수정시간</th>
												<th CL='STD_LMOUSR'>수정자</th>
												<th CL='STD_LUSRNM'>수성자명</th>
											</tr>
										</thead>
									</table>
								</div>
								<div class="tableBody">
									<table>
										<colgroup>
											<col width="40" />
											<col width="40" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" /> 
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											
											<col width="100" />
											
											<col width="100" />
											<col width="100" />
											<col width="100" />
											
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
											<col width="100" />
										</colgroup>
										<tbody id="gridList">
											<tr CGRow="true">
												<td GCol="rownum">1</td>
												<td GCol="rowCheck"></td>
												<td GCol="text,WAREKY">물류센터</td>
												<td GCol="check,DELMAK">삭제</td>
												<td GCol="text,SKUKEY">품목코드</td>
												<td GCol="text,DESC01">품목명</td>
												<td GCol="text,DUOMKY">기본단위</td>
												<td GCol="input,DPUTZO,SHZONMA">기본적치구역</td>
												<td GCol="input,DPUTAR,SHAREMA">기본적치영역</td>
												<td GCol="check,SKUG02">즉시불출여부</td>
												<td GCol="text,GTINCD">바코드</td>
												<td GCol="text,SKUG01">품목유형</td>
												<td GCol="text,SKUG01NM">품목유형명</td>
												<td GCol="text,SKUL01">품목구분</td>
												<td GCol="text,SKUL01NM">품목구분명</td>
												<td GCol="text,SKUG05">제품군</td>
												<td GCol="text,SKUG05NM">제품군명</td>
												<td GCol="text,LENGTH" GF="N">가로</td>
												<td GCol="text,WIDTHW" GF="N">세로</td>
												<td GCol="text,HEIGHT" GF="N">높이</td>
												<td GCol="text,CUBICM" GF="N">CBM</td>
												
												<td GCOL="text,ASKU04">유통기간</td>
												<!-- <td GCOL="text,PLOCOV">기본적치</td> -->
												<td GCOL="input,PASTKY,SHPLSTH">적치전략키</td>
												<td GCOL="input,ALSTKY,SHALSTH">할당전략키</td>
												<td GCOL="text,MEASKY">단위구성코드</td>
												
												<td GCOL="text,CREDAT" GF="C"></td>
												<td GCOL="text,CRETIM" GF="T"></td>
												<td GCol="text,CREUSR"></td>
												<td GCOL="text,CUSRNM"></td>
												<td GCOL="text,LMODAT" GF="C"></td>
												<td GCOL="text,LMOTIM" GF="T"></td>
												<td GCOL="text,LMOUSR"></td>
												<td GCOL="text,LUSRNM"></td>
											</tr>									
										</tbody>
									</table>
									</div>
								</div>
								<div class="tableUtil">
									<div class="leftArea">	
										<button type="button" GBtn="find"></button>
										<button type="button" GBtn="sortReset"></button>
										<!-- <button type="button" GBtn="copy"></button> -->
										<button type="button" GBtn="layout"></button>
										<button type="button" GBtn="total"></button>
										<button type="button" GBtn="excel"></button>
									</div>
									<div class="rightArea">
										<p class="record" GInfoArea="true"></p>
									</div>
								</div>
							</div>
						</div>

						<!-- <div id="tabs1-2">
							<div class="section type1" style="overflow-y:scroll;">
								<div class="controlBtns type2"  GNBtn="gridList">
									<a href="#"><img src="/common/images/btn_first.png" alt="" /></a>
									<a href="#"><img src="/common/images/btn_prev.png" alt="" /></a>
									<a href="#"><img src="/common/images/btn_next.png" alt="" /></a>
									<a href="#"><img src="/common/images/btn_last.png" alt="" /></a>
								</div>
								<br/>
								<div class="searchInBox">
								<h2 class="tit" CL="STD_GENERAL">일반</h2>
									<table class="table type1">
										<colgroup>
											<col width="5%"/>
											<col width="5%"/>
											<col width="5%"/>
										</colgroup>
										<tbody>
											<tr>
												<th CL='STD_SKUKEY'>품번코드</th>
												<td>
													<input type="text" name="SKUKEY" readonly="readonly"/>
													<select CommonCombo="SKUG01" name="SKUG01" disabled="disabled"></select>
												</td>
												<td GCol="select,SKUG01">
												</td>
												<th CL='STD_DELMAK'>삭제</th>
												<td>
													<input type="checkbox" name="DELMAK" value="V" disabled="disabled"/>
												</td>
											</tr>
											<tr>
												<th CL='STD_DESC01'>품명</th>
												<td>
													<input type="text" name="DESC01" size="60" readonly="readonly"/>
												</td>
											</tr>
											<tr>
												<th CL='STD_WAREKY'>거점</th>
												<td>
													<input type="text" name="WAREKY" readonly="readonly"/>
												</td>
												<th CL='STD_OWNRKY'>화주</th>
												<td>
													<input type="text" name="OWNRKY" readonly="readonly"/>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="searchInBox">
								<h2 class="tit" CL="STD_SKUINFO"></h2>
									<table class="table type1">
										<colgroup>
											<col width="5%"/>
											<col width="13%"/>
											<col width="5%"/>
										</colgroup>
										<tbody>
											<tr>
												<th CL='STD_DUOMKY'>단위</th>
												<td>
													<input type="text" name="DUOMKY" readonly="readonly"/>
												</td>
												<th CL='STD_MEASKY'>단위구성</th>
												<td>
													<input type="text" name="MEASKY" />
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="searchInBox">
								<h2 class="tit" CL="STD_TB_SKU_ITM1_5"></h2>
									<table class="table type1">
										<colgroup>
											<col width="5%"/>
											<col width="13%"/>
											<col width="5%"/>
											<col width="13%"/>
											<col width="5%"/>
										</colgroup>
										<tbody>
											<tr>
												<th CL='STD_PASTKY'>적치전략키</th>
												<td>
													<input type="text" name="PASTKY"/>
												</td>
												<th  CL='STD_DPUTZO'>기본 적치구역</th>
												<td>
													<input type="text" name="DPUTZO"/>
												</td>
												<th  CL='STD_DPUTLO'>기본 적치지번</th>
												<td>
													<input type="text" name="DPUTLO"/>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="searchInBox">
								<h2 class="tit" CL="STD_ETC"></h2>
									<table class="table type1">
										<colgroup>
											<col width="5%"/>
											<col width="13%"/>
											<col width="5%"/>
											<col width="13%"/>
											<col width="5%"/>
										</colgroup>
										<tbody>
											<tr>
												<th CL='STD_LOCARV'>기본입하지번</th>
												<td>
													<input type="text" name="LOCARV"/>
												</td>
												<th CL='STD_ALSTKY'>할당전략키</th>
												<td>
													<input type="text" name="ALSTKY"/>
												</td>
												<th CL='STD_OBPROT'>출하불허</th>
												<td>
													<input type="checkbox" name="OBPROT" value="V" disabled="disabled"/>
												</td>
											</tr>
											<tr>
												<th CL='STD_SAFQTY'>안전재고수량</th>
												<td>
													<input type="text" name="SAFQTY"/>
												</td>
												<th CL='STD_QTYBOX'>박스수량</th>
												<td>
													<input type="text" name="QTYBOX" readonly="readonly"/>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div> -->
					</div>
				</div>
			</div>	
			<!-- //contentContainer -->
		</div>
	</div>
		<!-- //content -->
	<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>