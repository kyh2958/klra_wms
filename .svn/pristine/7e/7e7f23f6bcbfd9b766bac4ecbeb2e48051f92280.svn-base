<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript">
	var tabNum = 1; 
	var rule = false;
	var searcflag = false;
	$(document).ready(function(){
		 gridList.setGrid({
	    	id : "gridList1",
			editable : true,
			pkcol : "UROLKY,WAREKY",
			module : "System",
			command : "UR01WK",
			validation : "WAREKY" 
	    }); 
		 
		gridList.setGrid({
	    	id : "gridList2",
			editable : true,
			pkcol : "UROLKY,PROGID",
			module : "System",
			command : "UR01PG",
			validation : "PROGID"  
		 }); 
		
		 gridList.setGrid({
	    	id : "gridList3",
			editable : true,
			pkcol : "UROLKY,USERID",
			module : "System",
			command : "UR01US",
			validation : "USERID" 
	    });
	});
	
	function searchList() {
		//var param = dataBind.paramData("searchArea");
		flag = "U";
		$('#Delete').show();
		
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");		
			
			gridList.gridList({
				id : "gridList1",
				param : param
			});
		    /* gridList.gridList({
				id : "gridList2",
				param : param
			}); */
			if(searcflag == true){
				gridList.gridList({
					id : "gridList3",
					param : param
				}); 
			}
			
			netUtil.send({
				module : "System",
				command : "UR01TOP",
				bindId : "ur01top",
				sendType : "map",
				bindType : "field",
				param : param
			});
		} else {
			searchOpen(true);
		}
	}
	
	function saveData(){
		if(validate.check("searchArea")){
			var gridId = "gridList" + tabNum;
			if(gridList.validationCheck(gridId, "modify")){
				if(!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)){
					return;
				}
				
				//var list = dataBind.paramData("foldSect");
				var param = inputList.setRangeParam("searchArea");
				var itemList = gridList.getGridData(gridId);
				var headCount = 0;
				
				var urolky = param.get("UROLKY"); 
				var paramP = new DataMap();
				paramP.put("UROLKY", urolky);
				
				// ROLDF 갯수 확인
				var json = netUtil.sendData({
						module : "System",
						command : "UROval",
						sendType : "map",
						param : paramP
				});
				
				var isEdited = false;
				if(json && json.data){
					isEdited = true;
				}
				
				var paramF = new DataMap();
				paramF.put("list", param);
				paramF.put("itemList", itemList);
				
				if(json.data["CNT"] > 0) {
					headCnt = 1;
				}else{ 
					headCnt = 0;
				}
				
				paramF.put("headCnt", headCnt);
				
				var json = netUtil.sendData({
					url : "/wms/system/json/saveUR01UI.data",
					param : paramF
				});  
				
				if((json && json.data) || isEdited){
					commonUtil.msgBox("HHT_T0008");
					searchList();
				}
			}
		} else {
			searchOpen(true);
		}
	}
 
    function tabChg(num){
    	if(num == 3){
    		searcflag = true;    		
    	}else{
    		searcflag = false;
    	}
    	
        var gridId = "gridList"+num;
        var param = dataBind.paramData("searchArea");
           
        gridList.gridList({
            id : gridId,
            param : param
         });  
      
        tabNum = num
     } 
    
	function gridListEventRowAddBefore(gridId, rowNum){
		var param = inputList.setRangeParam("searchArea");
		var urolky = param.get("UROLKY");
		var newData = new DataMap();
		
		newData.put("UROLKY", urolky);
		
		return newData;
	}
	
	function warekyCheck(valueTxt, $colObj){
		var rowNum = gridList.getColObjRowNum("gridList1", $colObj);
		var rowCount = gridList.getGridDataCount("gridList1");
		for(var i=0;i<rowCount;i++){
			if(i != rowNum){
				var wareky = gridList.getColData("gridList1", i, "WAREKY");
				
				if(wareky == valueTxt){
					return false;
				}
			}			
		}
		return true;
	} 
	
	function progidCheck(valueTxt, $colObj){
		var rowNum = gridList.getColObjRowNum("gridList2", $colObj);
		var rowCount = gridList.getGridDataCount("gridList2");
		
		for(var i=0;i<rowCount;i++){
			if(i != rowNum){
				var progid = gridList.getColData("gridList2", i, "PROGID");
				
				if(progid == valueTxt){
					return false;
				}
			}			
		}
		return true;
	}
	
	function useridCheck(valueTxt, $colObj){
		var rowNum = gridList.getColObjRowNum("gridList3", $colObj);
		var rowCount = gridList.getGridDataCount("gridList3");
		for(var i=0;i<rowCount;i++){
			if(i != rowNum){
				var userid = gridList.getColData("gridList", i, "USERID");
				
				if(userid == valueTxt){
					return false;
				}
			}			
		}
		return true;
	}
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId == "gridList3" && colName == "USERID"){
			if(colValue != ""){
				var param = new DataMap();
				
				param.put("USERID",colValue);
				
				var json = netUtil.sendData({
					module : "System",
					command : "USERIDval",
					sendType : "map",
					param : param
				});
				
				if(json.data["CNT"] >= 1) {
					var param = new DataMap();
					
					param.put("USERID",colValue);
					
					var cmd = 'NMLAST';
					
					for(var i = 0 ; i < 2 ; i ++){
						if(i == 1){
							 cmd = 'NMFIRS';
						}
						var json = netUtil.sendData({
							module : "System",
							command : cmd,
							sendType : "map",
							param : param
						});
						
						if(json && json.data){
							gridList.setColValue("gridList3", rowNum, cmd, json.data[cmd]); 
						}
					}
					
				} else if (json.data["CNT"] <= 1) {
					commonUtil.msgBox("MASTER_M0541");
					gridList.setColValue("gridList", rowNum, "USERID", ""); 
				}
			}
		}
	}
	
	function commonBtnClick(btnName){
		//commonUtil.debugMsg("commonBtnClick : ", arguments);
		if(btnName == "Search"){
			var param = new DataMap();
			//param.put("PROGID", "<%= menuId%>");
			//param.put("LGTYPE", "SEARCH");
			/* var json = netUtil.sendData({
				url: "/wms/system/json/saveLog.data",
				param : param
			}); */
			
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Create"){
			createList();
		}else if(btnName == "Delete"){
			wkDelete();
		}
	}
	
	function createList(){
		rule = true;
		flag = "I";
		$('#Delete').hide();
		
		if (validate.check("searchArea")) {
			var param = inputList.setRangeParam("searchArea");
			var urolky = param.get("UROLKY");
			
			var urolkyChk = new DataMap();
			urolkyChk.put("UROLKY",urolky);
			
			var json = netUtil.sendData({
				module : "System",
				command : "UROval",
				sendType : "map",
				param : param
			});
			
			if(json.data["CNT"] > 0) {
				commonUtil.msgBox("MASTER_M0700",urolky);    //사용자권한 키 {0}은(는) 존재하는 값입니다.
				return;
			}
	
			netUtil.send({
				module : "System",
				command : "CREUR01",
				bindId : "tabs-1",
				sendType : "map",
				bindType : "field",
				param : param
			});
			
			gridList.gridList({
		    	id : "gridList1",
		    	module : "System",
				command : "CREUR01WK",
		    	param : param
		    });
				
		}
		
	}	
	
	function gridListEventDataBindEnd(gridId, dataLength){
		//var list = dataBind.paramData("foldSect");
		
		var param = inputList.setRangeParam("searchArea");
		var urolky = param.get("UROLKY");
		var newData = new DataMap();
		
		newData.put("UROLKY", urolky);
		
		var itemList = gridList.getGridData("gridList1");
		var headCount = 0;
		var RoCnt = 0;
		
		//var urolky = list.get("UROLKY"); 
		//var paramP = new DataMap();
		//paramP.put("UROLKY", urolky);
		
		// ROLDF 갯수 확인
		var json = netUtil.sendData({
				module : "System",
				command : "UROval",
				sendType : "map",
				param : newData
		});
		
		var isEdited = false;
		if(json && json.data){
			RoCnt = json.data["CNT"];
			isEdited = true;
		}

		if(rule == true){
			if(RoCnt == 0){
				var itemList = gridList.getGridData("gridList1");
				var headCount = 0;
				
				var paramF = new DataMap();
				//var list = dataBind.paramData("foldSect");
							
				paramF.put("list", newData);
				paramF.put("itemList", itemList);
				paramF.put("headCnt", headCount);
				
				var json = netUtil.sendData({
					url : "/wms/system/json/saveUR01UI.data",
					param : paramF
				});	
			}
			rule = false;
		}
		 
	}
	
	function wkDelete(){
		if(validate.check("searchArea")){
			if(!commonUtil.msgConfirm("MASTER_M0020")){
				return;
			}
	
			var gridId = "gridList" + tabNum;
			//var list = dataBind.paramData("foldSect");
			var param = inputList.setRangeParam("searchArea");
			/* var urolky = param.get("UROLKY");
			var newData = new DataMap();
			
			newData.put("UROLKY", urolky); */
			var itemList = gridList.getGridData(gridId);
	
			var param = new DataMap();
			param.put("list",param);
			
			var urolky = param.get("UROLKY"); 
			var paramP = new DataMap();
			paramP.put("UROLKY", urolky);
		
			var json = netUtil.sendData({
				module : "System",
				command : "UROval",
				sendType : "map",
				param : paramP
			});
	
			if(json.data["CNT"] > 0) {
				var json = netUtil.sendData({
					url : "/wms/system/json/delUR01WK.data",
					param : paramP
				});  
		
				if(json && json.data){
					commonUtil.msgBox("VALID_M0003");
					inputList.resetRange();
					$("[name=UROLKY]").val("");
					gridList.resetGrid("gridList1");
					gridList.resetGrid("gridList2");
					gridList.resetGrid("gridList3");
					searchOpen(true);
				}
				return;
			} else {
				// 삭제할 데이터가 없습니다.
				return;
			}
		} else {
			searchOpen(true);
		}

	}
	
	//서치헬프 Before 이벤트 (팝업에 넘겨줄 값 세팅)
	function searchHelpEventOpenBefore(searchCode, gridType){
		//commonUtil.debugMsg("searchHelpEventOpenBefore : ", arguments);
		if(searchCode == "SHPROGM"){
			var param = new DataMap();
			param.put("WAREKY", "<%= wareky%>");
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
				<div class="fl_r" id="tab_btn01">
					<input type="button" CB="Search SEARCH BTN_SEARCH" />
					<input type="button" CB="Save SAVE BTN_SAVE" />
					<input type="button" CB="Create CREATE BTN_CREATE" />
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap" id="ur01top">
					<dl>
						<dt CL="STD_UROLKY"></dt>
						<dd>
							<input type="text" class="text" name="UROLKY" UIInput="S,SHROLDF" validate="required,SYSTEM_M0084"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_SHORTX"></dt>
						<dd>
							<input type="text" class="input" name="SHORTX" style="width:250px"/>
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
					<li><a href="#tab1-1" onclick="tabChg(1)"><span>접속 거점</span></a></li>
					<li style="display:none"><a href="#tab1-2" onclick="tabChg(2)"><span>상세</span></a></li>
					<li><a href="#tab1-3" onclick="tabChg(3)"><span>연결:사용자</span></a></li>
					<li class="btn_zoom_wrap">
						<ul>
							<!-- <li><button class="btn btn_smaller"><span>축소</span></button></li> -->
							<li><button class="btn btn_bigger"><span>확대</span></button></li>
						</ul>
					</li>
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList1">
									<tr CGRow="true">
										<td GH="40"  GCol="rownum">1</td>
										<td GH="100" GCol="text,UROLKY"></td>
										<td GH="100" GCol="input,WAREKY,SHWAHMA"></td>
										<td GH="100 STD_CREDOC" GCol="check,CREDOC"></td>
										<td GH="100 STD_ACTVAT" GCol="check,ACTVAT"></td>
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
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
						<button type='button' GBtn="excelUpload"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>4</span></span>
					</div>
				</div>
				<div class="table_box section" id="tab1-2" style="display:none">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList2">
									<tr CGRow="true">
										<td GH="40"  GCol="rownum">1</td>
										<td GH="100" GCol="text,UROLKY"></td>
										<td GH="100" GCol="input,PROGID,SHPROGM" validate="required,SYSTEM_M0006" GF="S 20"></td>
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
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
						<button type='button' GBtn="excelUpload"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>4</span></span>
					</div>
				</div>
				<div class="table_box section" id="tab1-3">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList3">
									<tr CGRow="true">
										<td GH="40"  GCol="rownum">1</td>
										<td GH="100" GCol="text,UROLKY"></td>
										<td GH="100" GCol="input,USERID,SHUSRMA" validate="required,MASTER_M0001" GF="S 20"></td>
										<td GH="100" GCol="text,NMLAST"></td>
										<td GH="100" GCol="text,NMFIRS"></td>
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
						<button type='button' GBtn="total"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="excel"></button>
						<button type='button' GBtn="excelUpload"></button>
						<span class='txt_total' >총 건수 : <span GInfoArea='true'>4</span></span>
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