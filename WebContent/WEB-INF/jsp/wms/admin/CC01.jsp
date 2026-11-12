<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<script type="text/javascript">
	var dblIdx = 0;
	
	$(document).ready(function(){
		gridList.setGrid({
	    	id : "gridList",
			editable : true,
			pkcol : "CMCDKY",
			module : "WmsAdmin",
			command : "CC01",
			bindArea : "tabs1-2"
	    });
		
		gridList.setGrid({
	    	id : "gridListSub",
			editable : true,
			pkcol : "CMCDKY,CMCDVL",
			module : "WmsAdmin",
			command : "CC01Sub"
	    });
	});

	function searchList(){
		var param = inputList.setRangeParam("searchArea");
		gridList.gridList({
	    	id : "gridList",
	    	param : param
	    });
	}
	
	function searchSubList(headRowNum){
		var rowVal = gridList.getColData("gridList", headRowNum, "CMCDKY");
		var param = inputList.setRangeParam("searchArea");
		param.put("CMCDKY", rowVal);
		
		gridList.gridList({
			id : "gridListSub",
			param : param
		});

		lastFocusNum = headRowNum; //2020.04.13 LHJ 오류수정
		dblIdx = headRowNum;
	}
	
	var lastFocusNum = -1;
	
	function gridListEventDataBindEnd(gridId, dataLength){
		if(gridId == "gridList" && dataLength > 0){
			searchSubList(0);
		}
	}
	
	function gridListEventRowDblclick(gridId, rowNum){
		if(gridId == "gridList"){
			if(gridList.getColData("gridList", rowNum, "STATUS") == "C"){
				return false;
			}
			dblIdx = rowNum;
			
			searchSubList(rowNum);
		}
	}
	
	//그리드 위에 클릭시 아이템 리셋
	function gridListEventRowFocus(gridId, rowNum){
		if(gridId == "gridList"){
			if(dblIdx != rowNum){
				var modRowCnt = gridList.getModifyRowCount("gridListSub");
				if(modRowCnt == 0){
					if(dblIdx != rowNum){
						gridList.resetGrid("gridListSub");
						dblIdx = -1;
					}
				//저장
				}else{
					if(commonUtil.msgConfirm("COMMON_M0024")){//값이 변화된 데이터가 있습니다. 이동하시겠습니까?
						gridList.resetGrid("gridListSub");
					}else{
						//gridListEventRowFocus("gridList", dblIdx);
						gridList.setRowFocus("gridList", dblIdx, false);
						return;
					}
				}
			}
		}
	}
	
	function gridListEventRowAddBefore(gridId, rowNum) {
		if(gridId == "gridList"){
			var headCnt = gridList.getModifyRowCount("gridList");
			var listCnt = gridList.getModifyRowCount("gridListSub");
			if(listCnt > 0 || headCnt > 0){
				commonUtil.msgBox("MASTER_M0702");//변경된 row를 저장 후 행추가 하십시오.
				return false;
			}else{
				var newData = new DataMap();
				newData.put("STATUS", "C");

				return newData;
			}
		}else if(gridId == "gridListSub"){
			if(dblIdx == -1){
				var headNum = gridList.getGridDataCount("gridList") -1;
				
				if(gridList.getColData("gridList", headNum, "STATUS") == ""){
					//alert(commonUtil.getMsg("MASTER_M0577"));
					commonUtil.msgBox("MASTER_M0577");
					return false;
				}
	
				if(gridList.getColData("gridList", headNum, "STATUS") == "C"){
					var cmcdky = gridList.getColData("gridList", headNum, "CMCDKY");

					if(cmcdky != ""){
						var newData = new DataMap();
						newData.put("CMCDKY",cmcdky);
					
						return newData;
					}else{
						//alert(commonUtil.getMsg("MASTER_M0128"));
						commonUtil.msgBox("MASTER_M0128");
						return false;
					}
				}else{
					//alert(commonUtil.getMsg("TASK_M0003"));
					commonUtil.msgBox("TASK_M0003");
					return false;
				}
			}else{
				
				var cmcdky = gridList.getColData("gridList", dblIdx, "CMCDKY");
	
				var newData = new DataMap();
				newData.put("CMCDKY",cmcdky);
				
				return newData;
			}
		}
	}
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId == "gridList" && colName == "CMCDKY"){
			if(colValue != ""){
				var cmcdky = gridList.getColData("gridList", rowNum, "CMCDKY");
				
				var param = new DataMap();
				param.put("CMCDKY",colValue);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "CC01CMCDKYval",
					sendType : "map",
					param : param
				});
				
				if(json.data["CNT"] >= 1) {
					gridList.setColValue("gridList", rowNum, "CMCDKY", "");
					//alert(commonUtil.getMsg("MASTER_M0129",colValue));
					commonUtil.msgBox("MASTER_M0129",colValue);
					return false;
				}
			}
			if(gridList.getGridDataCount("gridListSub") > 0){
				var listCnt = gridList.getGridDataCount("gridList");
				var sublistCnt = gridList.getGridDataCount("gridListSub");
				if(listCnt != 0){
					for(var i = 0; i < sublistCnt; i++){
						gridList.setColValue("gridListSub", i, "CMCDKY", colValue);
					}
				}
			}
		}else if(gridId == "gridListSub" && colName == "CMCDVL"){
			if(colValue != ""){
				var cmcdky = gridList.getColData("gridListSub", rowNum, "CMCDKY");
				
				var param = new DataMap();
				param.put("CMCDKY",cmcdky);
				param.put("CMCDVL",colValue);
				
				var json = netUtil.sendData({
					module : "WmsAdmin",
					command : "CC01CMCDVLval",
					sendType : "map",
					param : param
				});
				
				//2020.04.16 삭제후 같은 데이터 추가가능
				var validCheck = false;
				if(json.data["CNT"] >= 1) {
					var subListCnt = gridList.getGridDataCount("gridListSub");
					for(var i = 0; i < subListCnt; i++){
						if(gridList.getRowState("gridListSub", i) == "D") {
							var subDataMap = gridList.getRowData("gridListSub", i);
							if(subDataMap.get("CMCDVL") == colValue) {
								validCheck = true;
								break;	
							}
						}
					}
					
					if(validCheck) {
						return;
					}
					
					gridList.setColValue("gridListSub", rowNum, "CMCDVL", "");
					//alert(commonUtil.getMsg("MASTER_M0318"));
					commonUtil.msgBox("MASTER_M0318");
					return false;
				}
			}
		}
	}
	
	function saveData(){
		//2020.04.14 ItemList없이 HeadList저장되는 오류 수정 
		//var rowNum = 0;
		// 2020.04.16 아이템리스트 없을때 저장되는 오류 재수정
		var dCnt = 0;
		if(gridId = "gridListSub"){
			for(var i = 0; i < gridList.getGridDataCount("gridListSub"); i++) {				
				if(gridList.getRowState(gridId, i) == configData.GRID_ROW_STATE_DELETE){
					dCnt++;
				}
			}
			if(gridList.getModifyRowCount("gridList") > 0) {				
				if(gridList.getModifyData("gridList")[0].get("GRowState") != configData.GRID_ROW_STATE_DELETE 
						&& gridList.getGridDataCount("gridListSub") == dCnt) {
					commonUtil.msgBox("OUT_M0045");
					return;
				}
			}
		}
		
		if(gridList.getModifyRowCount("gridList") == 0 && gridList.getModifyRowCount("gridListSub") == 0 ){
			//alert(commonUtil.getMsg("MASTER_M0545"));
			commonUtil.msgBox("MASTER_M0545");
			return;
		}
		if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
			return;
		}
		if(gridList.validationCheck("gridList", "modify") && gridList.validationCheck("gridListSub", "modify")){
			var headNum = gridList.getGridDataCount("gridList") -1;
			
			if(gridList.getRowStatus("gridList",headNum) == "C" && gridList.getGridDataCount("gridListSub") == 0){
				//alert(commonUtil.getMsg("OUT_M0045"));
				commonUtil.msgBox("OUT_M0045");   //처리를 위한 아이템이 존재하지 않습니다.
				return;
			}
			
			var headCnt = gridList.getGridDataCount("gridList");
			for(var i = 0; i < headCnt; i++){
				gridList.setColValue("gridList", i, "STATUS", gridList.getRowStatus("gridList", i));
			}
			var head = gridList.getGridData("gridList");
			
			var listCnt = gridList.getGridDataCount("gridListSub");
			for(var i = 0; i < listCnt; i++){
				gridList.setColValue("gridListSub", i, "STATUS", gridList.getRowStatus("gridListSub", i));
			}
			
			var list = gridList.getGridData("gridListSub");
			
			var param = new DataMap();
			
			param.put("head", head);
			param.put("list", list);
	
			var json = netUtil.sendData({
				url : "/wms/admin/json/saveCC01.data",
				param : param
			});
			
			alert(commonUtil.getMsg(json.data));
			if(json){
				if(json.data){
					gridList.resetGrid("gridListSub");
					searchList();
				}
			}
		}
	}
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
	
	function gridExcelDownloadEventBefore(gridId){
		var param = inputList.setRangeParam("searchArea");
		if(gridId == "gridListSub"){
			var rowNum = gridList.getFocusRowNum("gridList");
			var cmcdky = gridList.getColData("gridList", rowNum, "CMCDKY");
		
			param.put("CMCDKY",cmcdky);
		}
		return param;
	}

	//2020.04.14 LHJ HeadList삭제 시 아이템리스트 삭제여부 메시지 추가
	function gridListEventRowRemove(gridId, rowNum){
		if(gridId == "gridList"){
			commonUtil.msgBox("VALID_M0016");//아이템리스트도 같이 삭제됩니다.삭제하시겠습니까?
			return;
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
						<dt CL="STD_CMCDKY">공통 코드 키</dt>
						<dd>
							<input type="text" class="input" name="CV.CMCDKY" UIInput="R"/>
						</dd>
					</dl>
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
			</div>
		</div>
        <div class="search_next_wrap">
			<div class="content_layout tabs top_layout">
				<ul class="tab tab_style02">
					<li><a href="#tabs1-1" CL='STD_LIST'><span>리스트</span></a></li>
					<!-- <li><a href="#tabs1-2" CL='STD_DETAIL'><span>상세</span></a></li> -->
					<li class="btn_zoom_wrap">
						<ul>
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section" id="tabs1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH='40 STD_NUMBER'  GCol="rownum">번호</td>                        
										<td GH='150 STD_CMCDKY' GCol="input,CMCDKY" validate="required,MASTER_M0023" GF="S 20">공통 코드 키</td>        
										<td GH='150 STD_SHORTX' GCol="input,SHORTX" GF="S 180">설명</td>             
										<td GH='150 STD_DBFILD' GCol="input,DBFILD" GF="S 10">DB 필드 이름</td>      
										<td GH='150 STD_USARL1' GCol="input,USARL1" GF="S 30">UA Lbl.1</td>    
										<td GH='150 STD_USARL2' GCol="input,USARL2" GF="S 30">UA Lbl.2</td>    
										<td GH='150 STD_USARL3' GCol="input,USARL3" GF="S 30">UA Lbl.3</td>    
										<td GH='150 STD_USARL4' GCol="input,USARL4" GF="S 30">UA Lbl.4</td>    
										<td GH='150 STD_USARL5' GCol="input,USARL5" GF="S 30">UA Lbl.5</td>    
										<td GH='150 STD_USARL6' GCol="input,USARL6" GF="S 30">UA Lbl.6</td>    
										<td GH='150 STD_USARL7' GCol="input,USARL7" GF="S 30">UA Lbl.7</td>    
										<td GH='150 STD_USARL8' GCol="input,USARL8" GF="S 30">UA Lbl.8</td>    
										<td GH='150 STD_USARL9' GCol="input,USARL9" GF="S 30">UA Lbl.9</td>    
										<td GH='150 STD_SYONLY' GCol="check,SYONLY">시스템에서만사용</td>       
										<td GH='150 STD_CREDAT' GCol="text,CREDAT"  GF='D'>생성일자</td>            
										<td GH='150 STD_CRETIM' GCol="text,CRETIM"  GF='T'>생성시간</td>            
										<td GH='150 STD_CREUSR' GCol="text,CREUSR">생성자</td>          
										<td GH='150 STD_CUSRNM' GCol="text,CUSRNM">생성자명</td>          
										<td GH='150 STD_LMODAT' GCol="text,LMODAT" GF='D'>수정일자</td>           
										<td GH='150 STD_LMOTIM' GCol="text,LMOTIM" GF='T'>수정시간</td>           
										<td GH='150 STD_LMOUSR' GCol="text,LMOUSR">수정자</td>          
										<td GH='150 STD_LUSRNM' GCol="text,LUSRNM">수정자명</td>											
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
			
			<div class="content_layout tabs bottom_layout">
				<ul class="tab tab_style02">
					<li><a href="#tabs1-1"><span>상세리스트</span></a></li>
					<li class="btn_zoom_wrap">
						<ul>
							<!-- <li><button class="btn btn_smaller"><span>축소</span></button></li> -->
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section" id="tabs1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridListSub">
									<tr CGRow="true">
										<td GH="40 STD_NUMBER"  GCol="rownum">1</td>                                                      
										<td GH="150 STD_CMCDKY"  GCol="text,CMCDKY">공통 코드 키</td>                              
										<td GH="150 STD_CMCDVL"  GCol="input,CMCDVL" validate="required" GF="S 40">유형코드</td> 
										<td GH="150 STD_CDESC1"  GCol="input,CDESC1" GF="S 180">코드 설명</td>                      
										<td GH="150 STD_CDESC2"  GCol="input,CDESC2" GF="S 180">코드 설명2</td>                      
										<td GH="150 STD_USARG1"  GCol="input,USARG1" GF="S 40">UA. 1</td>                  
										<td GH="150 STD_USARG2"  GCol="input,USARG2" GF="S 10">UA. 2</td>                  
										<td GH="150 STD_USARG3"  GCol="input,USARG3" GF="S 10">UA. 3</td>                  
										<td GH="150 STD_USARG4"  GCol="input,USARG4" GF="S 10">UA. 4</td>                  
										<td GH="150 STD_USARG5"  GCol="input,USARG5" GF="S 10">UA. 5</td>                  
										<td GH="150 STD_USARG6"  GCol="input,USARG6" GF="S 10">UA. 6</td>                  
										<td GH="150 STD_USARG7"  GCol="input,USARG7" GF="S 10">UA. 7</td>                  
										<td GH="150 STD_USARG8"  GCol="input,USARG8" GF="S 10">UA. 8</td>                  
										<td GH="150 STD_USARG9"  GCol="input,USARG9" GF="S 10">UA. 9</td>                  
										<td GH="150 STD_CREDAT"  GCol="text,CREDAT" GF='D'>생성일자</td>                          
										<td GH="150 STD_CRETIM"  GCol="text,CRETIM" GF='T'>생성시간</td>                          
										<td GH="150 STD_CREUSR"  GCol="text,CREUSR">생성자</td>                           
										<td GH="150 STD_CUSRNM"  GCol="text,CUSRNM">생성자명</td>                           
										<td GH="150 STD_LMODAT"  GCol="text,LMODAT" GF='D'>수정일자</td>                          
										<td GH="150 STD_LMOTIM"  GCol="text,LMOTIM" GF='T'>수정시간</td>                          
										<td GH="150 STD_LMOUSR"  GCol="text,LMOUSR">수정자</td>                           
										<td GH="150 STD_LUSRNM"  GCol="text,LUSRNM">수정자명</td>      
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