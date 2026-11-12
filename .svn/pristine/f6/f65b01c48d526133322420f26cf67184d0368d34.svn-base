<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript">
	$(document).ready(function(){
		gridList.setGrid({
	    	id : "gridList",
			editable : true,
			pkcol : "PTNRKY,PTNRTY",
			module : "WmsAdmin",
			command : "BZPTN",
			bindArea : "tabs1-2",
 			validation : "PTNRKY,PTNRTY",
		    validationType : "C"
	    });
		gridList.setReadOnly('gridList',true, ['DELMAK']);
		$("#PTNRTY").val("CT");
	});
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			gridList.gridList({
		    	id : "gridList",
		    	param : param
		    });
		}
	}
	
	function saveData(){
		var param = dataBind.paramData("searchArea");
		var json = gridList.gridSave({
	    	id : "gridList",
	    	param : param
	    });
		
		if(json){
			if(json.data){
				searchList();
			}
		}		
	}
 	
	<%-- function gridListEventRowAddBefore(gridId, rowNum){
		
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
		newData.put("PTNL05", "<%=ownrky%>");
		
		return newData;
	} --%>
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId == "gridList" && colName == "PTNRTY"){
			if(colValue != ""){
				var param = new DataMap();
				
				param.put("PTNRTY",colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "PTNRTYval",
					sendType : "map",
					param : param
				});
				 if (json.data["CNT"] < 1) {
					/* alert("업체타입이 정확하지 않습니다.\n다시 입력 해주세요."); */
					commonUtil.msgBox("MASTER_M1005",colValue);
					gridList.setColValue("gridList", rowNum, "PTNRTY", ""); 
				}
			}
		} 
	} 
	
	function gridListEventRowRemove(gridId, rowNum){
		if(gridId == "gridList"){
			var EWGUBN = gridList.getColData(gridId, rowNum, "EWGUBN");
			if(EWGUBN != "W"){
				
				return false;
			}
		}
		return true;
	}
	
	/* function sendSynch2(){
		var list = gridList.getFocusRowNum("gridList");
		var param = new DataMap();
		param.put("list", list);
		
		var json = netUtil.sendData({
			url : "/wms/admin/json/sendSynch2.data",
			param : param
		});
	 } */
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}/* else if(btnName == "Reflect"){
			sendSynch2();
		} */
	}
	 function searchHelpEventOpenBefore(searchCode, gridType){
			if(searchCode == "SHBZPTN"){
				var param = dataBind.paramData("searchArea");
				param.put("OWNRKY", "<%=ownrky%>");
				return param;
			} 
	}
 	
</script>
</head>
<body>
<div class="contentHeader">
	<div class="util">
		<button CB="Search SEARCH BTN_DISPLAY">
		</button>
		<button CB="Save SAVE STD_SAVE ">
		</button>
<!-- 		<button CB="Reflect REFLECT BTN_SYNCH"></button> -->
	</div>
	<div class="util3">
		<button class="button type2" id="showPop" type="button"><img src="/common/images/ico_btn4.png" alt="List" /></button>
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
					<%-- <tr>
						<th CL="STD_WAREKY">거점</th>
						<td>
							<input type="text" name="WAREKY" readonly="readonly"
							value="<%=wareky%>" validation="required,VALID_M0401" />
						</td>
					</tr> --%>
					<tr>
						<th CL="STD_PTNRTY">업체타입</th>
						<td GCol="select,PTNRTY">
							<select Combo="WmsAdmin,BZ01COMBO" name="PTNRTY" id="PTNRTY" style="width: 160px;"></select>							
						</td>
					</tr>
					<tr>
						<th CL="STD_PTNRKY">업체코드</th>
						<td>
							<input type="text" name="PTNRKY" UIInput="R,SHBZPTN" id="PTNRKY"/> 
						</td>
					</tr>
					<!-- <tr>
						<th CL="STD_PTNRTY">업체타입</th>
						<td>
							<input type="text" name="PTNRTY" value="<%=ownrky%>"  UIInput="R" id="PTNRKY"/> 
						</td>
					</tr> -->
					<tr>
						<th CL="STD_NAME01">설명</th>
						<td>
							<input type="text" name="NAME01"  UIInput="R" />
						</td>
					</tr>
					<%-- <tr>
						<th CL="STD_OWNRKY">화주</th>
						<td>
							<input type="text" name="OWNRKY"  value="<%=ownrky%>" UIInput="R" />
						</td>
					</tr> --%>
					
					<!-- <tr>
						<th CL="STD_EXPTNK">판매처</th>
						<td>
							<input type="text" id="EXPTNK" name="EXPTNK" UIInput="R" />
						</td>
					</tr> -->
					<!-- <tr>
						<th CL="STD_DELMAK">삭제</th>
						<td>
							<input type="checkbox" id="DELMAK" name="DELMAK" />
						</td>
					</tr> -->
					
<!-- 					<tr> -->
<!-- 							<th CL="STD_DELMAK,3">삭제</th><td> -->
<!-- 							<select Combo="WmsAdmin,DELMAKCOMBO" name="DELMAK" id="DELMAK" validate="required" -->
<!-- 							style="width: 160px;"> -->
<!-- 							</select> -->
<!-- 						</td> -->
<!-- 					</tr> -->
						
				    <tr>
				    	<th CL="STD_DELMAK">삭제</th>
						<td>
							<input type="checkbox" id="DELMAK" name="DELMAK" value="V"/>
						</td>
					</tr>						
						
				</tbody>
			</table>
		</div>
	</div>
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
							<li style="display:none"><a href="#tabs1-2"><span CL='STD_PERSONALINFO'>탭메뉴2</span></a></li>
						</ul>
						<div id="tabs1-1">
							<div class="section type1">
								<div class="table type2">
									<div class="tableHeader">
										<table>
											<colgroup>
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
											</colgroup>
											<thead>
												<tr>
													<th CL='STD_NUMBER'>번호</th>
													<th CL='STD_OWNRKY'>화주</th>
													<th CL='STD_OWNRNM'>화주명</th>
													<th CL='STD_PTNRTY'>거래처유형</th>
													<th CL='STD_PTNRTYT'>거래처유형명</th>
													<th CL='STD_PTNRKY'>거래처</th>
													<th CL='STD_DELMAK'>삭제</th>
													<th CL='STD_NAME01'>설명</th>
													<th CL='STD_NAME02'>대표자이름</th>
													<th CL='STD_ADDR01'>대표주소</th>
													<th CL='STD_CITY01'>도시</th>
													<th CL='STD_POSTCD'>우편번호</th>
													<th CL='STD_NATNKY'>국가</th>
													<th CL='STD_TELN01'>전신번호</th>
													<th CL='STD_TELN02'>SMS수신번호</th>
													<th CL='STD_VATREG'>사업자등록번호</th>
													<th CL='STD_EMAIL1'>E-MAIL</th>
													<th CL='STD_PROCHA'>거래처입출고</th>
													<th CL='STD_PTNG01'>거래처 메모</th>
													<th CL='STD_CREDAT'>생성일자</th>
													<th CL='STD_CRETIM'>생성시간</th>
													<th CL='STD_CREUSR'>생성자</th>
													<th CL='STD_CUSRNM'>생성자명</th>
													<th CL='STD_LMODAT'>수정일자</th>
													<th CL='STD_LMOTIM'>수정시간</th>
													<th CL='STD_LMOUSR'>수정자</th>
													<th CL='STD_LUSRNM'>수정자명</th>
													<th CL='STD_PTNL01'>공급처구분</th>
													<th CL='STD_PTNL01NM'>공급처명</th>
												</tr>
											</thead>
										</table>
									</div>
									<div class="tableBody">
										<table>
											<colgroup>
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
											</colgroup>
											<tbody id="gridList">
												<tr CGRow="true">
													<td GCol="rownum">번호</td>
													<td GCol="text,OWNRKY" >화주</td>
													<td GCol="text,OWNRNM" >화주명</td>
													<td GCol="text,PTNRTY" >거래처유형</td>
													<td GCol="text,PTNRTYT">거래처유형명</td>
													<td GCol="text,PTNRKY">거래처</td>
													<td GCol="check,DELMAK" >삭제</td>
													<td GCol="text,NAME01" >설명</td>
													<td GCol="text,NAME02" >대표자이름</td>
													<td GCol="text,ADDR01" >대표주소</td>
													<td GCol="text,CITY01" >도시</td>
													<td GCol="text,POSTCD" >우편번호</td>
													<td GCol="text,NATNKY" >국가</td>
													<td GCol="text,TELN01" >전화번호</td>
													<td GCol="text,TELN02" >SMS수신번호</td>
													<td GCol="text,VATREG" >사업자등록번호</td>
													<td GCol="text,EMAIL1" >E-MAIL</td>
													<td GCol="check,PROCHA" >거래처입출고</td>
													<td GCol="input,PTNG01" >거래처메모</td>
													<td GCol="text,CREDAT" GF="D">생성일자</td>
													<td GCol="text,CRETIM" GF="T">생성시간</td>
													<td GCol="text,CREUSR" >생성자</td>
													<td GCol="text,CUSRNM" >생성자명</td>
													<td GCol="text,LMODAT" GF="D">수정일자</td>
													<td GCol="text,LMOTIM" GF="T">수정시간</td>
													<td GCol="text,LMOUSR" >수정자</td>
													<td GCol="text,LUSRNM" >수정자명</td>
													<td GCol="text,PTNL01" >공급처구분</td>
													<td GCol="text,PTNL01NM" >공급처명</td>
												</tr>                 
											</tbody>                 
										</table>
									</div>
								</div>
								<div class="tableUtil">
									<div class="leftArea">		
										<button type="button" GBtn="find"></button>
										<button type="button" GBtn="sortReset"></button>
										<!-- <button type="button" GBtn="copy"></button>
										<button type="button" GBtn="add"></button>
										<button type="button" GBtn="delete"></button>	 -->
										<button type="button" GBtn="layout"></button>
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
								<h2 class="tit" CL="STD_SELECTOPTIONS">검색조건</h2>
									<table class="table type1">
										<colgroup>
											<col width="6%"/>
											<col width="15%"/>
											<col width="6%"/>
										</colgroup>
										<tbody>
											<tr>
											<th CL='STD_PTNRKY'>업체코드</th>
											<td>
												<input type="text" name="PTNRKY" readonly="readonly"/>
											</td>
											<th CL='STD_PTNRTY'>업체타입</th>
											<td GCol="select,PTNRTY">
												<select CommonCombo="PTNRTY" name="PTNRTY" disabled="disabled"></select>
											</td>
										</tr>
										<tr>
											<th CL='STD_PTNL3'>업태</th>
											<td>
												<input type="text" name="PTNL03" readonly="readonly"/>
											</td>
											<th CL='STD_PTNL4'>업종</th>
											<td>
												<input type="text" name="PTNL04" readonly="readonly"/>
											</td>
										</tr>
										<tr>
											<th CL='STD_VATREG'>사업자등록번호</th>
											<td>
												<input type="text" name="VATREGS" readonly="readonly"/>
											</td>
											<th  CL='STD_DELMAK'>삭제</th>
											<td>
												<input type="checkbox" name="DELMAK" value="V">
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							
							<div class="searchInBox">
								<table class="table type1">
									<colgroup>
										<col width="100" />
										<col />
									</colgroup>
									<tbody>
										<tr>
											<th CL='STD_NAME01'>이름</th>
											<td>
												<input type="text" size="70" name="NAME01" readonly="readonly"/>
											</td>
										</tr>
										<tr>
											<th CL='STD_NAME02'>대표자이름</th>
											<td>
												<input type="text" size="70" name="NAME02" readonly="readonly"/>
											</td>
										</tr>
										<tr>
											<th CL='STD_ADDR01'>주소</th>
											<td>
												<input type="text" size="70" name="ADDR01" readonly="readonly"/>
											</td>
										</tr>
										<tr>
											<th CL='STD_POSTCD'>우편번호</th>
											<td>
												<input type="text" name="POSTCD" readonly="readonly"/>
											</td>
										</tr>
										<tr>
											<th CL='STD_TELN01'>전화번호</th>
											<td>
												<input type="text" name="TELN01" readonly="readonly"/>
											</td>
										</tr>
										<tr>
											<th CL='STD_PROCHA'>발주불가</th>
											<td>
												<input type="checkbox" name="PROCHA" disabled="disabled"/>
											</td>
										</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div> -->
					</div>
				</div>
				<!-- //contentContainer -->
			</div>
		</div>
		<!-- //content -->
		<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>