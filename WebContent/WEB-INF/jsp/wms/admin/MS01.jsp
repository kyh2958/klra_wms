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
 #tabs1-1{height: calc(100% - 90px);}
 .search_sub{height: 30px;}
 .content_layout .search_sub dl{padding:0 0 10px;}
 .content_layout .search_sub dd{margin-left:unset;text-align:unset;}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			editable : true,
			pkcol : "OWNRKY,PLNTKY,STLKY",
			module : "WmsAdmin",
			command : "STLMA",
			emptyMsgType : true
		});
		
		// 플랜트
		var $selPLNTKY = $('select[name="PLNTKY"]');
		$selPLNTKY.multipleSelect('checkAll');
		
//		gridList.setReadOnly("gridList",true, ["NEGSTK","INDCDO","INDAES"]);
		
//		searchList();
	});
 	
 	function commonBtnClick(btnName){
 		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Print"){
			printLocationBarcode();
		}else if(btnName == "Reflect"){
			setLOCAKY();
		}
	}
	
	function searchList(){
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			//param.put("WAREKY","<%=wareky%>")
			
			gridList.gridList({
				id : "gridList",
				param : param
			});
		}
	}
	
	function saveData(){
		if(gridList.validationCheck("gridList", "modify")){
			var list = gridList.getModifyData("gridList", "A");
			if(list.length == 0){
				alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEEMPTY));
				return;
			}
			
			if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
				return;
			}
			
			var param = new DataMap();
			param.put("list", list);
			
			var json = netUtil.sendData({
				url : "/wms/admin/json/saveMS01.data",
				param : param
			});
			
			if(json && json.data){
				if(json.data == "OK") {
					searchList();
					alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEOK));
				} else {
					alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEFAIL) + " [" + json.data + "]");
				}
			}
		}
	}
		
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId == "gridList") {
			
			if(colName == "WAREKY") {
				if(colValue == ""){
					gridList.setColValue("gridList", rowNum, "WARENM", "");
				}else{
					var param = new DataMap();
					param.put("WAREKY",colValue);
					
					var json = netUtil.sendData({
						module : "WmsAdmin",
						command : "WAREKY",
						sendType : "map",
						param : param
					});
					
					if(json && json.data){
						
						var name01 = json.data["NAME01"];
						if(name01.length > 0 && name01 != " "){
							gridList.setColValue("gridList", rowNum, "WARENM", name01);
						}else{
							gridList.setColValue("gridList", rowNum, "WAREKY", "");
							gridList.setColValue("gridList", rowNum, "WARENM", "");
						}
						
					}else{
						gridList.setColValue("gridList", rowNum, "WAREKY", "");
						gridList.setColValue("gridList", rowNum, "WARENM", "");
					}
				}
			}
			
			
		}
		
	}
	
	function gridListEventRowCopyAfter(gridId, rowNum){
		if(gridId == "gridList") {
			//gridList.setColValue("gridList", rowNum, "LOCAKY", "");
		}
	}
	
	function gridListEventRowAddBefore(gridId, rowNum){
		var newData = new DataMap();
		
		newData.put("WAREKY", "");
		
		return newData;
	}
	
	function gridListEventRowRemove(gridId, rowNum){
		if(gridId == "gridList"){
			var param = new DataMap();
			
			return true;
		}
	}
	
	function searchHelpEventOpenBefore(searchCode, gridType){
		var param = new DataMap();
		
		if(searchCode == "SHWAHMA"){
			//param = dataBind.paramData("searchArea");
			param.put("WAREKY", "");
			param.put("COMPKY", "<%=compky%>");
		}else if(searchCode=="SHSTLMA"){
			param.put("OWNRKY", "<%=ownrky%>");
		}
		
		return param;
	}
	
	function setLOCAKY(){
		
		var apyWareky = $("#APY_WAREKY").val();
		
		if(gridList.getSelectData("gridList").length < 1) {
			alert("선택된 데이터가 없습니다.");
			return;
		}
		
		
		var param = new DataMap();
		param.put("WAREKY",apyWareky);
		
		var json = netUtil.sendData({
			module : "WmsAdmin",
			command : "WAREKY",
			sendType : "map",
			param : param
		});
		
		var selectNumList = gridList.getSelectRowNumList("gridList");
		
		for(var i=0;i<selectNumList.length;i++){
			var rowNum = selectNumList[i];
			gridList.setColValue("gridList", rowNum, "WAREKY", apyWareky);
			if(json && json.data){
				gridList.setColValue("gridList", rowNum, "WARENM", json.data["NAME01"]);
			}else{
				gridList.setColValue("gridList", rowNum, "WARENM", "");
			}
		}
	}

    function comboEventDataBindeBefore(comboAtt){
        if(comboAtt == "WmsInbound,PLNTKYCOMBO"){
            var param = new DataMap();
            param.put("WAREKY_LIST", "<%= wareky%>");
            param.put("OWNRKY", "<%=ownrky%>");
            param.put("LANGKY", "<%=langky%>");
            
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
							<select class="input" combo="Wms,USERWAHCOMBO" name="WAREKY" style="width: 160px">
                              <option value="">전체</option>
                            </select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA02" style="vertical-align:middle">플랜트</dt>
						<dd>
                            <select CommonCombo="LOTA02" class="input" name="PLNTKY" ComboCodeView=false ComboType="MS" style="width: 160px" >
                            </select>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA01">저장위치</dt>
						<dd>
							<input type="text" name="MS.STLKY" class="input" UIInput="R,SHSTLMA"/>
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
					<li><a href="#tabs1-1" CL='STD_LIST'><span>리스트</span></a></li> 
					<!-- <li><a href="#tabs1-1" CL="STD_DOCUMENTINFO"><span>문서정보</span></a></li> -->
				</ul>
				<div class="search_sub">
					<dl>
						<dt CL="STD_GENERALWAREHOUSE">거점</dt>
						<dd>
							<select id="APY_WAREKY" name="APY_WAREKY" class="input" Combo="Wms,USERWAHCOMBO"></select>
							<input type="button" CB="Reflect REFLECT BTN_REFLECT" />
						</dd>
					</dl>
				</div>
				<div class="table_box section" id="tabs1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="40" GBtnCheck="true" GCol="rowCheck"></td>
										<td GH="100 STD_PLNTKY" GCol="text,PLNTKY">플랜트</td>
										<td GH="110 STD_PLNTNM" GCol="text,PLNTNM">플랜트명</td>
										<td GH="100 STD_STLKY" GCol="text,STLKY">저장위치</td>
										<td GH="200 STD_STLNM" GCol="text,STLNM">저장위치명</td>
										<td GH="100 STD_WMSMAK" GCol="check,WMSMAK">WMS재고대상</td>
										<td GH="100 STD_WAREHOUSE" GCol="input,WAREKY,SHWAHMA" >거점</td>
										<td GH="110 STD_WANAME" GCol="text,WARENM">거점명</td>
										<td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D">생성일자</td>
										<td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
										<td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자ID</td>
										<td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자명</td>
										<td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D">수정일자</td>
										<td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
										<td GH="100 STD_LMOUSR" GCol="text,LMOUSR">수정자ID</td>
										<td GH="100 STD_LUSRNM" GCol="text,LUSRNM">수정자명</td>
										<td GH="0 STD_ERPWERKS" GCol="text,OWNRKY">화주</td>						
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
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