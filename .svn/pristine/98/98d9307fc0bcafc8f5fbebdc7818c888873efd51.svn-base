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
	    	name : "gridList",
			editable : true,
			pkcol : "WAREKY,SEQCUS",
			module : "WmsAdmin",
			command : "ALCUS",
			validation : "WAREKY,ALSTKY,CUSTMR,NATNKY"
	    });
	});
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea")
			gridList.gridList({
		    	id : "gridList",
		    	param : param
		    });			
		}
	}
	
	function saveData(){
		/* 중복 할당전략키 방지 밸리데이션  */
		/* if(gridList.getModifyRowCount('gridList') >0){
			var list = gridList.getModifyList('gridList');
			for(var i=0; i <list.length;i++){
				var tmpRow = list[i];
				var wareky = $.trim(tmpRow.get("WAREKY"));
				var alstky = $.trim(tmpRow.get("ALSTKY"));
				var param1 = new DataMap();
				
				param1.put("WAREKY", wareky);
				param1.put("ALSTKY", alstky);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "TR01ALSTKYval",
					sendType : "map",
					param : param1
				});
				
				if(json.data["CNT"] > 0) {
					commonUtil.msgBox("중복된 할당전략키가 있습니다."); //중복된 할당전략키가 있습니다.
					return;
				}
				
			}
		} */
		
		var param = dataBind.paramData("searchArea");
		
		var json = gridList.gridSave({
	    	id : "gridList",
	    	param : param
	    });
		
		if(json && json.data){
			searchCnt = 1;
					
			var param = dataBind.paramData("searchArea");
			
			gridList.gridList({
				id : "gridList",
		    	param : param
		    });
			
			listFlag = true;

			//gridList.setReadOnly("gridList");
		}
	}

	function gridListEventRowAddBefore(gridId, rowNum) {
			var newData = new DataMap();
			newData.put("WAREKY", "<%=wareky%>");
			return newData;
	}
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId == "gridList" && colName == "ALSTKY"){
			if(colValue != ""){
				var param = new DataMap();
				param.put("WAREKY", "<%=wareky%>");
				param.put("ALSTKY", colValue);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "TR01ALSTKYval",
					sendType : "map",
					param : param
				});
				
				if(json.data["CNT"] < 1) {
					var param = new DataMap();
					param.put("WAREKY", "<%=wareky%>");
					param.put("ALSTKY", colValue);
					
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "ALSTKYY",
						sendType : "map",
						param : param
					});
					
					if(json && json.data){
						gridList.setColValue("gridList", rowNum, "SHORTX", json.data["SHORTX"]); 
					}else{
						gridList.setColValue("gridList", rowNum, "SHORTX", ""); 
					}
				} else {
					gridList.setColValue("gridList", rowNum, "ALSTKY", ""); 
					gridList.setColValue("gridList", rowNum, "SHORTX", ""); 
					
					commonUtil.msgBox("중복된 할당전략키가 있습니다."); //중복된 할당전략키가 있습니다.
					return;
				}
			}else if(colValue==""){
				gridList.setColValue("gridList", rowNum, "SHORTX", "");
			}
		} else if (gridId == "gridList" && colName == "CUSTMR") {
			if(colValue != ""){
				var param = new DataMap();
				param.put("CUSTMR",colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "PTNRKYval",
					sendType : "map",
					param : param
				});
				if(json.data["CNT"] >= 1) {
					var param = new DataMap();
					param.put("CUSTMR",colValue);
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "NAME012",
						sendType : "map",
						param : param
					});
					
					if(json && json.data){
						gridList.setColValue("gridList", rowNum, "CUSTMRNM", json.data["NAME01"]); 
					} 
				} else if (json.data["CNT"] < 1) {
					gridList.setColValue("gridList", rowNum, "CUSTMR", ""); 
					gridList.setColValue("gridList", rowNum, "CUSTMRNM", "");
					
					commonUtil.msgBox("VALID_M0205", colValue);  ///[{0}]는 존재하지 않는 거래처 입니다.
					return;
				}
			}
		}else if (gridId == "gridList" && colName == "NATNKY") {
			if(colValue != ""){
				var param = new DataMap();
				param.put("NATNKY",colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "NATNKYval",
					sendType : "map",
					param : param
				});
				if(json.data["CNT"] >= 1) {
					var param = new DataMap();
					param.put("NATNKY",colValue);
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "NATNKY",
						sendType : "map",
						param : param
					});
					
					if(json && json.data){
						gridList.setColValue("gridList", rowNum, "NATNKYNM", json.data["DESC01"]); 
					} 
				} else if (json.data["CNT"] < 1) {
					gridList.setColValue("gridList", rowNum, "NATNKY", ""); 
					gridList.setColValue("gridList", rowNum, "NATNKYNM", "");
					
					//commonUtil.msgBox("VALID_M0583", colValue);  ///국가 {0}이 유효하지 않습니다.
					commonUtil.msgBox("MASTER_M0583", colValue);  ///국가 {0}이 유효하지 않습니다.
					return;
				}
			}
		}
	}
	
	/* function gridListEventRowRemove(gridId, rowNum){
		var rowWar = gridList.getColData("gridList", rowNum, "WAREKY");
		var rowSeq = gridList.getColData("gridList", rowNum, "SEQCUS");
		if(gridId == "gridList"){
			var param = new DataMap();
			param.put("WAREKY", rowWar);
			param.put("SEQCUS", rowSeq);
			var json = netUtil.sendData({
				url : "/wms/admin/json/delTR01.data",
				param : param
			});
			
			if(json && json.data){
				commonUtil.msgBox("VALID_M0003");
			} else {
				commonUtil.msgBox("VALID_M0004");
			}
			searchList();
		}
	} */
	
	
	function commonBtnClick(btnName){
		//commonUtil.debugMsg("commonBtnClick : ", arguments);
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Execute"){
			test3();
		}
	}
	function searchHelpEventOpenBefore(searchCode, gridType, object, rownum){
		var param = new DataMap();
		if(searchCode == "SHALSTH"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHWAHMA"){
			return dataBind.paramData("searchArea");
		}else if(searchCode == "SHBZPTN"){
			param = dataBind.paramData("searchArea");
			param.put("PTNRTY","CT");
			param.put("OWNRKY", gridList.getColData("gridList", rownum, "OWNRKY"));			
			return param; 
		}else if(searchCode == "SHDOCTM"){
			param = dataBind.paramData("searchArea");
			param.put("DOCCAT", "200");
			return param; 
		}else if(searchCode == "SHCMCDV"){
			param = dataBind.paramData("searchArea");
			param.put("CMCDKY", "NATNKY");
			return param; 
		}
	}
</script>
</head>
<body style="position: relative;">
<div class="contentHeader">
	<div class="util">
		<button CB="Search SEARCH BTN_DISPLAY">
		</button>
		<button CB="Save SAVE STD_SAVE ">
		</button>
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
					<tr>
						<th CL="STD_WAREKY">거점</th>
						<td>
							<input type="text" name="WAREKY" readonly="readonly"  value="<%=wareky%>"/>
						</td>
					</tr>
					<tr>
						<th CL="STD_ALSTKY">할당전략키</th>
						<td>
							<input type="text" id="ALSTKY" name="ALSTKY" UIInput="R,SHALSTH" />
						</td>
					</tr>
					<!-- <tr>
						<th CL="STD_SLGORT">영업부문</th>
						<td>
							<input type="text" id="SLGORT" name="SLGORT" UIInput="R" />
						</td>
					</tr>
					<tr>
						<th CL="STD_NATNKY">국가키</th>
						<td>
							<input type="text" id="NATNKY" name="NATNKY" UIInput="R,SHVNATNKY" />
						</td>
					</tr> -->
					<tr>
						<th CL="STD_CUSTMR">거래처</th>
						<td>
							<input type="text" id="CUSTMR" name="CUSTMR" UIInput="R,SHBZPTN" />
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
					</ul>
					<div id="tabs1-1">
						<div class="section type1">
							<div class="table type2">
								<div class="tableBody">
									<table>
										<tbody id="gridList">
											<tr CGRow="true">
												<td GH="40" GCol="rownum">1</td>
												<!-- <td GH="40" GBtnCheck="true" GCol="rowCheck"></td> -->
												
												<td GH="100 STD_WAREKY" GCol="text,WAREKY">센터</td>
												<td GH="100 STD_SEQCUS" GCol="text,SEQCUS">우선순위</td>
												<td GH="100 STD_ALSTKY" GCol="input,ALSTKY,SHALSTH" GF="S 10">할당전략키</td>
												<td GH="100 STD_OWNRKY" GCol="input,OWNRKY,SHOWNER" GF="S 10">화주</td>
												<td GH="100 STD_CHANEL" GCol="input,CHANEL" GF="S 10">채널</td>
												<td GH="100 STD_CUSTMR" GCol="input,CUSTMR,SHBZPTN" GF="S 10">거래처</td>
												<td GH="100 STD_REGN01" GCol="input,REGN01" GF="S 12">관할거점</td>
												<td GH="100 STD_NATNKY" GCol="input,NATNKY,SHCMCDV">국가</td> 
												<td GH="100 STD_DOCUTY" GCol="input,DOCUTY,SHDOCTM">오더타입</td> 
												<td GH="100 STD_SLGORT" GCol="text,SLGORT" GF="S 15">관할거점</td> 
												
												<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D">생성일자</td>
												<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
												<td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자ID</td>
												<td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자명</td>
												<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D">수정일자</td>
												<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
												<td GH="100 STD_LMOUSR" GCol="text,LMOUSR">수정자ID</td>
												<td GH="100 STD_LUSRNM" GCol="text,LUSRNM">수정자명</td>
												
												<td GH="100 STD_INDBZL" GCol="text,INDBZL">비즈니스로직구분자</td>
												<td GH="100 STD_INDARC" GCol="text,INDARC">아카이브구분자</td>
												<td GH="100 STD_UPDCHK" GCol="text,UPDCHK">수정체크</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div class="tableUtil">
								<div class="leftArea">	
									<button type="button" GBtn="find"></button>
									<button type="button" GBtn="sortReset"></button>
									<button type="button" GBtn="copy"></button>
									<button type="button" GBtn="add"></button>
									<button type="button" GBtn="delete"></button>
									<button type="button" GBtn="layout"></button>
									<!-- <button type="button" GBtn="total"></button> -->
									<button type="button" GBtn="excel"></button>
								</div>
								<div class="rightArea">
									<p class="record" GInfoArea="true"></p>
								</div>
							</div>
						</div>
					</div>
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