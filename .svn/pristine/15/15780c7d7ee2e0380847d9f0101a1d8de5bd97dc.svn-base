<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<style type="text/css">
 .content_wrap{background: none;}
</style>
<script type="text/javascript">
	//var dblIdx = -1;
	var headFocusNum = -1;
	var dblIdx = 0;
	$(document).ready(function() {
		gridList.setGrid({
			id : "gridList",
			name : "gridList",
			editable : true,
			//pkcol : "OWNRKY,SKUKEY",
			module : "WmsAdmin",
			command : "SKUPLT"
			//validation : "OWNRKY,WAREKY,SKUKEY,MEASKY,LOCARV,DPUTLO",
		});
		gridList.setReadOnly('gridList',true, ['PLNTKY','QRTAYN','SHTAYN','SKUG01','SKUL01', 'SKUG05']);
		
		// 플랜트
		//var $selPLNTKY = $('select[name="PLNTKY"]');
		//$selPLNTKY.multipleSelect('checkAll');
		initSearchArea();
		
		$("#WAREKY").val("<%=wareky%>");
	});

	function initSearchArea() {
		  
		// 플랜트
      <%if(null!=plant && plant.trim().equals("")){%>
        $('select[id="PLNTKY"]').multipleSelect('checkAll');
      <%}else{%>
        $('select[id="PLNTKY"]').multipleSelect('setSelects', ['<%=plant%>']);
      <%}%>
   	 
    }
	
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
	function comboEventDataBindeBefore(comboAtt){
		
		if(comboAtt == "Wms,PLNTKYCOMBO"){
			var param = new DataMap();
			var WAREKYS = "'<%= wareky%>'";
			param.put("WAREKYS", WAREKYS);
			param.put("OWNRKY", "<%=ownrky%>");
			param.put("LANGKY", "<%=langky%>");
			return param;
		}
		
	}
	
	
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
					<!-- <input type="button" CB="Save SAVE STD_SAVE" /> -->
					<!-- <input type="button" CB="Print PRINT BTN_SKUPRINT" /> -->
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_WAREKY">거점</dt>
						<dd>
							<input type="text" class="input" name="WAREKY" id="WAREKY" readonly="readonly" validation="required,VALID_M0401" IAname="Search" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_LOTA02" style="vertical-align:middle">플랜트</dt>
						<dd>
                            <select Combo="Wms,PLNTKYCOMBO" class="input" id="PLNTKY" name="SP.PLNTKY" ComboCodeView=false ComboType="MS" style="width: 160px" >
                            
                            </select>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_LOTA01">저장위치</dt>
						<dd>
							<input type="text" name="SP.STLKY" class="input" UIInput="R,SHSTLMA"/>
						</dd>
					</dl>
					
					<dl>
						<dt CL="STD_SKUKEY">품목코드</dt>
						<dd>
							<input type="text" class="input" name="SP.SKUKEY" UIInput="R,SHSKUMA" IAname="Search" UIFormat="U"/>
						</dd>
					</dl>
					<dl>
						<dt CL="STD_DESC01">품명</dt>
						<dd>
							<input type="text" class="input" name="DESC01" UIInput="R" IAname="Search" />
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
						<dt CL="STD_SKUG05">제품군</dt>
						<dd>
							<select class="input" name="SKUG05" CommonCombo="SKUG05"><option value=""/></select>
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
										<!-- <td GH="40" GCol="rowCheck"></td> -->
										<td GH="100 STD_WAREKY" GCol="text,WAREKY">물류센터</td>
										
										<td GH="100 STD_PLNTKY" GCol="select,PLNTKY">
                                            <select CommonCombo="LOTA02">
                                                <option value=" "></option>
                                            </select>
                                        </td>
										<td GH="100 STD_SKUKEY" GCol="text,SKUKEY">품목코드</td>
										<td GH="100 STD_DESC01" GCol="text,DESC01">품목명</td>
										
										<td GH="100 STD_QRTAYN" GCol="check,QRTAYN">품질검사여부</td>
										<td GH="100 STD_SKUG02" GCol="check,SHTAYN">즉시불출여부</td>
										<td GH="100 STD_RESTLKY" GCol="text,STLKY">입고저장위치</td>
										<td GH="100 STD_RESTLKYNM" GCol="text,STLKYNM">입고저장위치명</td>
										<!-- 
										<td GH="100 STD_SHTALKY" GCol="text,STLKY">즉시불출저장위치</td>
										<td GH="100 STD_STLNM" GCol="text,STLNM">저장위치명</td>
										 -->
										<td GH="100 STD_SKUG01" GCol="select,SKUG01">
                                            <select CommonCombo="SKUG01">
                                                <option value=" "></option>
                                            </select>
                                        </td>
                                        <td GH="100 STD_SKUL01" GCol="select,SKUL01">
                                            <select CommonCombo="SKUL01">
                                                <option value=" "></option>
                                            </select>
                                        </td>
                                        <td GH="100 STD_SKUG05" GCol="select,SKUG05">
                                            <select CommonCombo="SKUG05">
                                                <option value=" "></option>
                                            </select>
                                        </td>
										<td GH="100 STD_CREDAT" GCOL="text,CREDAT" GF="C"></td>
										<td GH="100 STD_CRETIM" GCOL="text,CRETIM" GF="T"></td>
										<td GH="100 STD_CREUSR" GCol="text,CREUSR"></td>
										<td GH="100 STD_CUSRNM" GCOL="text,CUSRNM"></td>
										<td GH="100 STD_LMODAT" GCOL="text,LMODAT" GF="C"></td>
										<td GH="100 STD_LMOTIM" GCOL="text,LMOTIM" GF="T"></td>
										<td GH="100 STD_LMOUSR" GCOL="text,LMOUSR"></td>
										<td GH="100 STD_LUSRNM" GCOL="text,LUSRNM"></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
						<button type='button' GBtn="find"></button>
						<button type='button' GBtn="sortReset"></button>
						<button type='button' GBtn="layout"></button>
						<button type='button' GBtn="total"></button>
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