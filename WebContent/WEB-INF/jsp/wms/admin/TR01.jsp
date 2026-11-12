<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<style type="text/css">
 .content_wrap{background: none;}
</style>
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
				})
				;
				if (json.data["CNT"] < 1) {
					gridList.setColValue("gridList", rowNum, "ALSTKY", ""); 
					
					commonUtil.msgBox("VALID_M2044"); //존재하지 않는 할당전략키입니다.
					return;
				} 
			}
		}else if(gridId == "gridList" && colName == "DOCUTY"){
			if(colValue != ""){
				var param = new DataMap();
				param.put("WAREKY", "<%=wareky%>");
				param.put("DOCUTY",colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "DOCUTYval",
					sendType : "map",
					param : param
				});
				
				var json2 = netUtil.sendData({
					module : "WmsAdmin",
					command : "DOCUTYchk",
					sendType : "map",
					param : param
				});
				
				
				if (json.data["CNT"] < 1) {
					gridList.setColValue("gridList", rowNum, "DOCUTY", ""); 
					
					commonUtil.msgBox("VALID_M2074"); // 출하 유형의 문서 유형을 입력해야 합니다.
					return;
				} 
				
				if(json2.data["CNT"] >= 1) {
					gridList.setColValue("gridList", rowNum, "DOCUTY", "");
				
					commonUtil.msgBox("VALID_M2075", colValue);  //거점에 {0}문서유형이 이미 등록되어 다른 문서유형을 등록해주세요.
					return;
				}
				
			}
		} else if (gridId == "gridList" && colName == "CUSTMR") {
			if(colValue != ""){
				var param = new DataMap();
				param.put("WAREKY", "<%=wareky%>");
				param.put("CUSTMR",colValue);
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "PTNRKYval",
					sendType : "map",
					param : param
				});
				
				var json2 = netUtil.sendData({
					module : "WmsAdmin",
					command : "PTNRKYchk",
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
				
				if(json2.data["CNT"] >= 1) {
					gridList.setColValue("gridList", rowNum, "CUSTMR", "");
				
					commonUtil.msgBox("VALID_M2076", colValue);  //거점에 {0}거래처가 이미 등록되어 다른 거래처을 등록해주세요.
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
			if(object[0].name == "CHANEL"){
				param.put("CMCDKY", "KVGR1");
			}else{
				param.put("CMCDKY", "NATNKY");
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
					<input type="button" CB="Save SAVE STD_SAVE" />
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_WAREKY">거점</dt>
						<dd>
							<input type="text" class="input" name="WAREKY" readonly="readonly"  value="<%=wareky%>"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_ALSTKY">할당전략키</dt>
						<dd>
							<input type="text" class="input" id="ALSTKY" name="ALSTKY" UIInput="R,SHALSTH" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_CUSTMR">거래처</dt>
						<dd>
							<input type="text" class="input" id="CUSTMR" name="CUSTMR" UIInput="R,SHBZPTN" />
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
					<li><a href="#tab1-1"><span>리스트</span></a></li>  
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
												<!-- <td GH="40" GBtnCheck="true" GCol="rowCheck"></td> -->
												
										<td GH="100 STD_WAREKY" GCol="text,WAREKY">센터</td>
										<td GH="100 STD_SEQCUS" GCol="text,SEQCUS">우선순위</td>
										<td GH="100 STD_ALSTKY" GCol="input,ALSTKY,SHALSTH" GF="S 10">할당전략키</td>
										<td GH="100 STD_OWNRKY" GCol="input,OWNRKY,SHOWNER" GF="S 10">화주</td>
										<td GH="100 STD_OWNRNM" GCol="text,OWNRNM" GF="S 10">화주명</td>
										<td GH="100 STD_KVGR1" GCol="input,CHANEL,SHCMCDV" GF="S 10">고객그룹1(채널)</td>
										<td GH="100 STD_KVGRNM" GCol="text,CHANNM" GF="S 10">고객명</td>
										<td GH="100 STD_CUSTMR" GCol="input,CUSTMR,SHBZPTN" GF="S 10">거래처</td>
										<td GH="100 STD_PTNRNM" GCol="text,CUSTMRNM" GF="S 10">거래처명</td>
										<!-- <td GH="100 STD_REGN01" GCol="input,REGN01" GF="S 12">관할거점</td> -->
										<!-- <td GH="100 STD_NATNKY" GCol="input,NATNKY,SHCMCDV">국가</td> --> 
										<td GH="100 STD_DOCUTY" GCol="input,DOCUTY,SHDOCTM">문서타입</td>
										<td GH="100 STD_DOCUTYNM" GCol="text,DOCUNM">문서타입명</td>  
										<td GH="100 STD_LASTOB" GCol="check,LASTOB">역출고불가여부</td> 
										<!-- <td GH="100 STD_SLGORT" GCol="text,SLGORT" GF="S 15">관할거점</td>  -->
										
										<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D">생성일자</td>
										<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
										<td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자ID</td>
										<td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자명</td>
										<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D">수정일자</td>
										<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
										<td GH="100 STD_LMOUSR" GCol="text,LMOUSR">수정자ID</td>
										<td GH="100 STD_LUSRNM" GCol="text,LUSRNM">수정자명</td>
										
										<!-- <td GH="100 STD_INDBZL" GCol="text,INDBZL">비즈니스로직구분자</td>
										<td GH="100 STD_INDARC" GCol="text,INDARC">아카이브구분자</td>
										<td GH="100 STD_UPDCHK" GCol="text,UPDCHK">수정체크</td> -->
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="add"></button>
						<button type='button' GBtn="copy"></button>
						<button type='button' GBtn="delete"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
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