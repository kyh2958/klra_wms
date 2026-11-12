<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript">

	$(document).ready(function() { 
		gridList.setGrid({
			id : "gridList",
			editable : true,
			pkcol : "USERID",
			module : "System",
			command : "USER",
			//bindArea : "tabs1-2",
			validation : "UROLKY,MENUKY"
		});
		
		/*gridList.setReadOnly("gridList", true, ["USERG5"]);*/
	}); 

	function searchList() {
		//var param = dataBind.paramData("searchArea");
	
		if(validate.check("searchArea")){
			var param = inputList.setRangeParam("searchArea");
			//alert(param);
			gridList.gridList({
		    	id : "gridList",
		    	param : param
		    });
		}
		$('.tabs').tabs("option","active",0);
	}

	function saveData() {
		if(gridList.validationCheck("gridList", "modify")){
			//var list = gridList.getModifyList("gridList");
			var list = gridList.getModifyData("gridList", "A");
			var param = new DataMap();
			param.put("list", list);
			
			var json = netUtil.send({
				url : "/wms/system/json/UI01.data",
				param : param,
				returnParam : param,
				successFunction : "saveEnd",
				failFunction : "saveFail"
			});
			
			
		}
	}
	
	function saveEnd(json, param){
		
		if(json && json.data){
			if(json.data != "0"){
				//alert("비밀번호가 초기화 되었습니다.");
				alert(commonUtil.getMsg(configData.MSG_SYSTEM_SAVEOK));
			}else{
				alert(commonUtil.getMsg("COMMON_M0465")); // 저장 실패하였습니다.
			}
			searchList();
		}else{
			alert(commonUtil.getMsg("COMMON_M0465")); // 저장 실패하였습니다.
			searchList();
		}
	}

	function saveFail(a, b, c, param){ // a:request, b:status, c:error
		var resultText = a.responseText.trim();
		alert(commonUtil.getMsg("COMMON_M0465")); // 저장 실패하였습니다.
		searchList();
	}
	
	
	function gridListEventRowAddAfter(gridId, rowNum){
		if(gridId == "gridList") {
			gridList.setColValue(gridId, rowNum, "NATNKY", "KR");
		}
	}
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		if(gridId=="gridList" && colName=="UROLKY"){
			if(colValue != ""){
				var param = new DataMap();
				param.put("UROLKY",colValue);
				var json = netUtil.sendData({
					module : "System",
					command : "UROval",
					sendType : "map",
					param : param
				});
				
			 if(json.data["CNT"] < 1){
					commonUtil.msgBox("SYSTEM_M0086", colValue);
					gridList.setColValue("gridList", rowNum, "UROLKY", "");
				}
			}
			
		}else if(gridId=="gridList" && colName=="USERID"){
			if(colValue != ""){
				var param = new DataMap();
				param.put("USERID",colValue);
				var json = netUtil.sendData({
					module : "System",
					command : "USERval",
					sendType : "map",
					param : param
				});
				if(json.data["CNT"]>0){
					commonUtil.msgBox("SYSTEM_M0055", colValue);
					gridList.setColValue("gridList", rowNum, "USERID", "");
				}
			}
		}
	}
	function commonBtnClick(btnName){
		//commonUtil.debugMsg("commonBtnClick : ", arguments);
		if(btnName == "Search"){
			var param = new DataMap();
			param.put("PROGID", "<%= menuId%>");
			param.put("LGTYPE", "SEARCH");
			/* var json = netUtil.sendData({
				url: "/wms/system/json/saveLog.data",
				param : param
			}); */
			
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Pwreset"){
			pwReset();
		}
	}
	
	function checkPv(colValue, rowNum, colName){
		var rowData = gridList.getRowData("gridList", rowNum);
		if(rowData.get("USERG1") == "PV" && $.trim(colValue) == ""){
			gridList.setColFocus("gridList", rowNum, colName);
			return false;
		}
		return true;
	}
	
	function pwReset(){
		//if(gridList.validationCheck("gridList", "select")){
		var chkList = gridList.getSelectRowNumList("gridList");

		if(chkList.length < 1){
			alert("선택된 데이터가 없습니다.");
			return;
		}else{
			var chkGrid = gridList.getSelectData("gridList");
			var param = new DataMap();
			param.put("list", chkGrid);
			
			var json = netUtil.send({
				url : "/wms/system/json/initPwUI01.data",
				param : param,
				returnParam : param,
				successFunction : "pwResetEnd",
				failFunction : "pwResetFail"
			});
			
			//alert("비밀번호가 초기화 되었습니다.");
			//searchList();
		}
		//}
	}
	
	function searchHelpEventOpenBefore(searchCode, multyType, $inputObj, rowNum){
		var param = new DataMap();
		if(searchCode == "SHMSTMENUG"){
			param.put("DELETEYN", "N");
		}
		return param;
	}
	
	function pwResetEnd(json, param){
		
		if(json && json.data){
			if(json.data != "0"){
				alert("비밀번호가 초기화 되었습니다.");
			}else{
				alert(commonUtil.getMsg("COMMON_M0465")); // 저장 실패하였습니다.
			}
			searchList();
		}else{
			alert(commonUtil.getMsg("COMMON_M0465")); // 저장 실패하였습니다.
			searchList();
		}
	}

	function pwResetFail(a, b, c, param){ // a:request, b:status, c:error
		var resultText = a.responseText.trim();
		alert(commonUtil.getMsg("COMMON_M0465")); // 저장 실패하였습니다.
		searchList();
	}
	
function comboEventDataBindeBefore(comboAtt, test){
	console.log(test);
        if(comboAtt == "Wms,PLNTKYCOMBO"){
            var param = new DataMap();
            
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
					<input type="button" CB="Search SEARCH BTN_SEARCH" />
					<input type="button" CB="Save SAVE BTN_SAVE" />
					<input type="button" CB="Pwreset SAVE BTN_PWRESET">
				</div>
			</div>
			<div class="search_inner">
				<div class="search_wrap ">
					<dl>
						<dt CL="STD_USERID"></dt>
						<dd>
							<input type="text" class="text" name="U.USERID" UIInput="R,SHUSRMA" />
						</dd>
					</dl>
					<dl>
						<dt CL="STD_NMLAST">이름</dt>
						<dd><input type="text" name="NMLAST" UIInput="R" /></dd>
					</dl>
					<dl>
						<dt CL="STD_PTNRKY1_DSC">관련업무</dt>
						<dd><input type="text" name="ADDR05" UIInput="R" ></dd>
					</dl>
					<dl>
						<dt CL="STD_MENUKY">메뉴키</dt>
						<dd><input type="text" name="MENUKY" UIInput="R,SHMSTMENUG" /></dd>
					</dl>
					<!-- <dl>
						<dt CL="STD_DEPART">부서</dt>
						<dd><input type="text" name="DEPART" UIInput="R" /></dd>
					</dl> -->
					<!-- <dl>
						<dt CL="DELYN"></dt>
						<dd>
							<select name="DELYN" class="input" CommonCombo="DELYN" ComboCodeView=false>
								<option value="" CL="STD_ALL"></option>
							</select>
						</dd>
					</dl> -->
				</div>
				<div class="btn_tab">
					<input type="button" class="btn_more" value="more" onclick="searchMore()"/>
				</div>
			</div>
		</div>
        <div class="search_next_wrap">
			<div class="content_layout tabs">
				<ul class="tab tab_style02">
					<li><a href="#tab1-1"><span>일반</span></a></li>
				</ul>
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="40" GCol="rownum">1</td>
										<td GH="40" GCol="rowCheck"></td>
										<td GH="100" GCol="input,USERID,SHUSRMA" validate="required,MASTER_M0001"  GF="S 200" ></td>
										<td GH="100 STD_DELMAK" GCol="check,DELMAK"></td>
										<td GH="100" GCol="input,UROLKY,SHROLDF" GF="S 200" ></td>
										<td GH="100" GCol="input,MENUKY,SHMSTMENUG" GF="S 10" validate="required" ></td>
										<td GH="100" GCol="input,PDAMKY,SHMSTMENUG" GF="S 60" ></td>
										<td GH="100" GCol="input,NMLAST" GF="S 60" validate="required,SYSTEM_M0100"></td>
										<td GH="100 STD_PTNRKY1_DSC" GCol="input,ADDR05" GF="S 60" ></td>
										<td GH="100" GCol="input,ADDR01" GF="S 180" ></td>
										<td GH="100" GCol="input,ADDR02" GF="S 180" ></td> 
										<td GH="100" GCol="input,ADDR03" GF="S 180" ></td>
										<td GH="100" GCol="input,ADDR04" GF="S 180" ></td>
										<!-- <td GH="100" GCol="input,ADDR05" GF="S 180" ></td> -->
										<td GH="100" GCol="input,CITY01" GF="S 180" ></td>
										<td GH="100" GCol="input,REGN01" GF="S 12" ></td>
										<td GH="100" GCol="input,POSTCD" GF="S 30" ></td>
										<td GH="100" GCol="select,NATNKY">
											<select Combo="System,NATNKYCOMBO">
											</select>
										</td>
										<td GH="100" GCol="input,TELN01" GF="S 20"></td>
										<td GH="100" GCol="input,TELN02" GF="S 20"></td>
										<td GH="100" GCol="input,TELN03" GF="S 20"></td>
										<td GH="100" GCol="input,TLEXT1" GF="S 10"></td>
										<td GH="100" GCol="input,FAXTL1" GF="S 20"></td>
										<td GH="100" GCol="input,FAXTL2" GF="S 20"></td>
										<td GH="100" GCol="input,POBOX1" GF="S 10" ></td>
										<td GH="100" GCol="input,POBPC1" GF="S 10" ></td>
										<td GH="100" GCol="input,EMAIL1" GF="S 120"></td>
										<td GH="100" GCol="input,EMAIL2" GF="S 120"></td>
										<td GH="100" GCol="input,COMPKY" GF="S 4" ></td>
										<!-- <td GH="100" GCol="input,DEPART" GF="S 20" ></td> -->
										<td GH="100" GCol="input,EMPLID" GF="S 20" ></td>
										<td GH="100" GCol="input,USERG1" GF="S 40" ></td>
										<td GH="100" GCol="input,USERG2" GF="S 40" validate="remote(checkPv)" ></td>
										<td GH="100 STD_WAREKY" GCol="input,USERG3,SHWAHMA" >거점</td>
										<td GH="130 STD_USERG4" GCol="select,USERG4" >
                                          <select Combo="Wms,MTYPECOMBO" >
                                            <option value="%">전체</option>
                                          </select>
                                        </td>
										
										<!-- <td GH="100" GCol="select,USERG5">
											<select Combo="System,AREAKYCOMBO">
												<option value="">Option</option>
											</select>
										</td> -->
										<td GH="100" GCol="select,LANGKY">
											<select Combo="System,LANGKYCOMBO" ></select>
										</td>
										<td GH="100" GCol="text,LLOGID" GF="D" ></td>
										<td GH="100" GCol="text,LLOGIT" GF="T" ></td>
										<td GH="100" GCol="text,LLOGOD" GF="D" ></td>
										<td GH="100" GCol="text,LLOGOT" GF="T" ></td>
										<td GH="100" GCol="text,CREDAT" GF="D" ></td>
										<td GH="100" GCol="text,CRETIM" GF="T" ></td>
										<td GH="100" GCol="text,CREUSR" ></td>
										<td GH="100" GCol="text,CUSRNM"></td>
										<td GH="100" GCol="text,LMODAT" GF="D" ></td>
										<td GH="100" GCol="text,LMOTIM" GF="T" ></td>
										<td GH="100" GCol="text,LMOUSR"></td>
										<td GH="100" GCol="text,LUSRNM"></td>
							 
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
						<!-- <button type='button' GBtn="delete"></button> -->
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