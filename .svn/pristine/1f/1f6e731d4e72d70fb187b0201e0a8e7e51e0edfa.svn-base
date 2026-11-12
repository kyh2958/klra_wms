<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
    String menuky = user.getMenuky();
%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<%@ page import="project.common.airfw.*"%>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<style>
    input[type="checkbox"]{height:unset;}
</style>
<script type="text/javascript">
var nowHeadIndex = -1;

$(document).ready(function(){
    gridList.setGrid({
        id : "gridList",
        editable : true,
        pkcol : "PROGID",
        module : "WmsAdmin",
        itemGrid : "gridListSub",
        command : "DOCPG_H",
        itemSearch : true,
        emptyMsgType : true
    });
    
    gridList.setGrid({
        id : "gridListSub",
        editable : true,
        pkcol : "PROGID,DOCCAT,DOCUTY",
        module : "WmsAdmin",
        command : "DOCPG",
        emptyMsgType : false
    });
    
});

function commonBtnClick(btnName){
    if(btnName == "Search"){
        searchList();
    }else if(btnName == "Save"){
        saveData();
    }
}

function searchList(){
    
    //$.jQueryAlert("메세지를 입력해주세요");
    
    if(validate.check("searchArea")){
        
        //nowHeadIndex = 0;
        gridList.resetGrid("gridList");
        gridList.resetGrid("gridListSub");
        
        var param = inputList.setRangeParam("searchArea");
        param.put("WAREKY","<%=wareky%>");
        param.put("LANGKY", "<%=langky%>");
        
        gridList.gridList({
            id : "gridList",
            param : param,
            command : "DOCPG_H"
        });
    }
}

function saveData(){
            
    if(gridList.validationCheck("gridListSub", "modify")){
        var list = gridList.getModifyData("gridListSub", "A");
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
            url : "/wms/admin/json/saveDT02.data",
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

function gridListEventDataBindEnd(gridId, dataLength){
    if(gridId == "gridList" && dataLength > 0){
        if(nowHeadIndex==-1){
            nowHeadIndex=0;
        }
        //alert("nowHeadIndex : " + nowHeadIndex);
        gridList.setRowFocus("gridList", nowHeadIndex);
        gridListEventRowDblclick(gridId, nowHeadIndex, "", "");
    }else if(gridId == "gridListSub") {
        
    }
}

function gridListEventRowDblclick(gridId, rowNum, colName, colValue){
    if(gridId == "gridList"){

        nowHeadIndex = rowNum;
        
        var progid = gridList.getColData(gridId, rowNum, "PROGID");
        
        gridList.resetGrid("gridListSub");
        
        var param = inputList.setRangeParam("searchArea");
        param.put("PROGID", progid);
        param.put("LANGKY", "<%=langky%>");
        
        if(progid == null || progid.trim() == ""){
            return;
        }
        
        gridList.gridList({
            id : "gridListSub",
            param : param,
            command : "DOCPG"
        });

    }
}

function gridListEventColValueChange(gridId, rowNum, colName, colValue){
    
    var param = new DataMap();
    
    if(gridId == "gridList") {
        if(colName == "PROGID") {
            if(colValue.length > 0 && colValue != " "){
                param.clear();
                
                param = inputList.setRangeParam("searchArea");
                param.put("LANGKY", "<%=langky%>");
                param.put("OWNRKY", "<%=ownrky%>");
                param.put("PROGID", colValue);
                
                var json = netUtil.sendData({
                    module: "WmsAdmin",
                    command: "DT02_PROGNM",
                    sendType: "map",
                    param: param
                });
                
                if(json && json.data){
                    gridList.setColValue("gridList", rowNum, "PROGNM", json.data["MENUNAME"]); 
                }else{
                    var menutxt = $("#MENUTEXT").val();
                    //commonUtil.msgBox("VALID_M1701",[menutxt,colValue]); //메뉴키 [{0}] : 프로그램ID [{1}] 존재하지 않는 프로그램 입니다.
                    gridList.setColValue("gridList", rowNum, "PROGID", "");
                    gridList.setColValue("gridList", rowNum, "PROGNM", "");
                }
                
            }else{
                gridList.setColValue("gridList", rowNum, "DOCCATNM", "");
            }
        }
    }else if(gridId == "gridListSub") {
        
        if(colName == "DOCCAT") {
            
            if(colValue.length > 0 && colValue != " "){
                param.clear();
                param.put("LANGKY", "<%=langky%>");
                param.put("DOCCAT", colValue);
                
                var json = netUtil.sendData({
                    module: "WmsAdmin",
                    command: "DT02_DOCCATNM",
                    sendType: "map",
                    param: param
                });
                
                if(json && json.data){
                    var doccatnm = json.data["DOCCATNM"];
                    if(doccatnm.length > 0 && doccatnm != " "){
                        gridList.setColValue("gridListSub", rowNum, "DOCCATNM", doccatnm);
                    }else{
                        gridList.setColValue("gridListSub", rowNum, "DOCCAT", "");
                        gridList.setColValue("gridListSub", rowNum, "DOCCATNM", "");
                    }
                }else{
                    gridList.setColValue("gridListSub", rowNum, "DOCCAT", "");
                    gridList.setColValue("gridListSub", rowNum, "DOCCATNM", "");
                }
                
            }else{
                gridList.setColValue("gridListSub", rowNum, "DOCCATNM", "");
            }
            
            gridList.setColValue("gridListSub", rowNum, "DOCUTY", "");
            gridList.setColValue("gridListSub", rowNum, "DOCUTYNM", "");
            gridList.setColValue("gridListSub", rowNum, "NUMOBJ", "");
            gridList.setColValue("gridListSub", rowNum, "TRNHTY", "");
            gridList.setColValue("gridListSub", rowNum, "SYSLOC", "");
            
        }else if(colName == "DOCUTY") {
            
            if(colValue.length > 0 && colValue != " "){
                
                var doccat = gridList.getColData(gridId, rowNum, "DOCCAT");
                
                param.clear();
                param.put("LANGKY", "<%=langky%>");
                param.put("DOCCAT", doccat);
                param.put("DOCUTY", colValue);
                
                var json = netUtil.sendData({
                    module: "WmsAdmin",
                    command: "DT02_DOCUTYNM",
                    sendType: "map",
                    param: param
                });
                
                if(json && json.data){
                    gridList.setColValue("gridListSub", rowNum, "DOCUTYNM", json.data["DOCUTYNM"]);
                    gridList.setColValue("gridListSub", rowNum, "NUMOBJ", json.data["NUMOBJ"]);
                    gridList.setColValue("gridListSub", rowNum, "TRNHTY", json.data["TRNHTY"]);
                    gridList.setColValue("gridListSub", rowNum, "SYSLOC", json.data["SYSLOC"]);
                }else{
                    
                    commonUtil.msgBox("VALID_M1700",[doccat,colValue]); //문서카테고리 [{0}] : 문서유형 [{1}] 존재하지 않는 문서유형 입니다.
                    
                    gridList.setColValue("gridListSub", rowNum, "DOCUTY", "");
                    gridList.setColValue("gridListSub", rowNum, "DOCUTYNM", "");
                    gridList.setColValue("gridListSub", rowNum, "NUMOBJ", "");
                    gridList.setColValue("gridListSub", rowNum, "TRNHTY", "");
                    gridList.setColValue("gridListSub", rowNum, "SYSLOC", "");
                }
                
            }else{
                gridList.setColValue("gridListSub", rowNum, "DOCUTYNM", "");
                gridList.setColValue("gridListSub", rowNum, "NUMOBJ", "");
                gridList.setColValue("gridListSub", rowNum, "TRNHTY", "");
                gridList.setColValue("gridListSub", rowNum, "SYSLOC", "");
            }
        }
    }
}

function gridListEventRowCopyAfter(gridId, rowNum){
    if(gridId == "gridList") {
    }
}

function gridListEventRowAddBefore(gridId, rowNum){
    
    if(validate.check("searchArea")){

        var newData = new DataMap();
        
        if(gridId == "gridList") {
            //newData.put("WAREKY", $("#WAREKY").val());
            //alert("aaa");
            nowHeadIndex = -1;

        }else if(gridId == "gridListSub") {
            
            //alert("sub nowHeadIndex : " + nowHeadIndex);
            
            var progid = gridList.getColData("gridList", nowHeadIndex, "PROGID");
            if(nowHeadIndex==-1 || progid=="" || progid==undefined){
                commonUtil.msgBox("MASTER_M0062"); //프로그램 ID를 먼저 입력해 주세요.
                return false;
            }
            
            newData.put("PROGID", progid);
            
        }
        
        return newData;
    }else{
        return false;
    }
}

function gridListEventRowAddAfter(gridId, rowNum){
    if(gridId == "gridList") {
        
        gridList.resetGrid("gridListSub");
    }
}

function gridListEventRowRemove(gridId, rowNum){
    if(gridId == "gridList"){
        var param = new DataMap();
        param.put("WAREKY", "<%=wareky%>");
    }else if(gridId == "gridListSub"){
        var param = new DataMap();
        param.put("WAREKY", "<%=wareky%>");
    }
    
    return true;
}

function searchHelpEventOpenBefore(searchCode, gridType, aa){
    var param = new DataMap();
    if(searchCode == "SHDOCTM"){
        // DOCCAT
        var gridListSubIdx = gridList.getSelectIndex("gridListSub");
        var progid = gridList.getColData("gridListSub", gridListSubIdx, "PROGID");
        var doccat = gridList.getColData("gridListSub", gridListSubIdx, "DOCCAT");
        
        param.put("PROGID", progid);
        param.put("DOCCAT", doccat);
        $("#DOCCAT").val(doccat);
        
        return param;
    }else if(searchCode == "MDMTMG"){
    	
    	var menutxt = $("#MENUTEXT").val();
    	
    	param.put("LANGKY", "<%=langky%>");
        param.put("OWNRKY", "<%=ownrky%>");
        param.put("WAREKY", "<%=wareky%>");
        param.put("COMPID", "<%=ownrky%>");
        
        param.put("MENUGID", menutxt);
//         console.log(JSON.stringify(param));
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
                    
                    <input type="hidden" class="input" id="COMPID" name="COMPID" value="<%=ownrky %>" />
                    <input type="hidden" class="input" id="DOCCAT" name="DOCCAT" value="" />  
                </div>
            </div>
        
			<!-- 
            <div class="" style="display:none;">
                <div class="search_wrap ">
                    <dl>
                        <dt CL="STD_MENUKY">메뉴키</dt>
                        <dd>
                            <input type="text" class="input" UIInput="S,SHMNUDF" id="MENUTEXT" name="MENUKY" value="<%=menuky%>" validate="required,MASTER_M0434" />
                        </dd>
                    </dl>
                    <dl style="display:none;">
						<dt CL="STD_OWNRKY">화주</dt>
						<dd>
							<input type="text" class="input" name="COMPID" value="<%=ownrky %>" />
						</dd>
					</dl>
                </div>
                <div class="btn_tab">
                    <input type="button" class="btn_more" value="more" onclick="searchMore()"/>
                </div>
            </div>
         -->    
        </div>
         
        <div class="search_next_wrap noSearch">
            <div class="content_layout tabs top_layout">
                <ul class="tab tab_style02">
                    <li><a href="#tab1-1"><span>리스트</span></a></li>
                    <li class="btn_zoom_wrap">
                        <ul>
                            <li><button class="btn btn_bigger"><span>확대</span></button></li>
                        </ul>
                    </li>
                </ul>
                <div class="table_box section" id="tab1-1">
                    <div class="table_list01">
                        <div class="scroll">
                            <table class="table_c">
                                <tbody id="gridList">
                                    <tr CGRow="true">
                                        <td GH="40 STD_NUMBER"   GCol="rownum"/></td>
                                        <td GH="100 STD_PROGID" GCol="input,PROGID,MDMTMG" >프로그램ID</td>
                                        <td GH="200 STD_PROGNM" GCol="text,PROGNM">프로그램명</td>
                                        <td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D">생성일자</td>
                                        <td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
                                        <td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D">수정일자</td>
                                        <td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>                    
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="btn_lit tableUtil">
                        <button type='button' GBtn="find"></button>
                        <button type='button' GBtn="sortReset"></button>
                        <button type="button" GBtn="add"></button>
                        <button type="button" GBtn="delete"></button>
                        <button type='button' GBtn="layout"></button>
                        <button type='button' GBtn="excel"></button>
                        <span class='txt_total' >총 건수 : <span GInfoArea='true'>0</span></span>
                    </div>
                </div>
            </div>
            
            <div class="content_layout tabs bottom_layout">
                <ul class="tab tab_style02">
                    <li><a href="#tab1-1"><span>아이템 리스트</span></a></li>
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
                                <tbody id="gridListSub">
                                    <tr CGRow="true">
                                        <td GH="40" GCol="rownum">1</td>
                                         <td GH="100 STD_PROGID" GCol="text,PROGID" >프로그램ID</td>
                                         <td GH="100 STD_DOCCAT" GCol="input,DOCCAT,SHDOCCM" validate="required">문서카테고리</td>
                                         <td GH="100 STD_DOCCATNM" GCol="text,DOCCATNM">문서카테고리명</td>
                                         <td GH="100 STD_DOCUTY" GCol="input,DOCUTY,SHDOCTM" validate="required">문서유형</td>
                                         <td GH="200 STD_DOCUTYNM" GCol="text,DOCUTYNM">문서유형명</td>
                                         <td GH="100 STD_NUMOBJ" GCol="text,NUMOBJ">채번 오브젝트</td>
                                         <td GH="100 STD_TRNHTY" GCol="text,TRNHTY">물동타입</td>
                                         <td GH="100 SYSLOC" GCol="text,SYSLOC">SYSLOC</td>

                                         <td GH="100 STD_CREDAT" GCol="text,CREDAT" GF="D">생성일자</td>
                                         <td GH="100 STD_CRETIM" GCol="text,CRETIM" GF="T">생성시간</td>
                                         <td GH="100 STD_CREUSR" GCol="text,CREUSR">생성자ID</td>
                                         <td GH="100 STD_CUSRNM" GCol="text,CUSRNM">생성자명</td>
                                         <td GH="100 STD_LMODAT" GCol="text,LMODAT" GF="D">수정일자</td>
                                         <td GH="100 STD_LMOTIM" GCol="text,LMOTIM" GF="T">수정시간</td>
                                         <td GH="100 STD_LMOUSR" GCol="text,LMOUSR">수정자ID</td>
                                         <td GH="100 STD_LUSRNM" GCol="text,LUSRNM">수정자명</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="btn_lit tableUtil">
                        <button type='button' GBtn="find"></button>
                        <button type='button' GBtn="sortReset"></button>
                        <button type="button" GBtn="add"></button>
                        <button type="button" GBtn="delete"></button>
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