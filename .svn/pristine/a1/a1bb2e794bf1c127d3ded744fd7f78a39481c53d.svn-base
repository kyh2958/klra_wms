<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Grid default</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<script type="text/javascript" src="/common/js/ezgencontrol.js"></script>
<%
    User user = (User)request.getSession().getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
%>
<%@ page import="project.common.airfw.*"%>
<script type="text/javascript">
	
	var dblIdx = 0;
	var subIdx = 0;
	var searchType = 0;
	var paramH = new DataMap();
	var menuky = configData.MENU_ID;

	$(document).ready(function(){
		
		//오라클 버전으로 있음
		var data = page.getLinkPopData();	
		
		paramH.put("data",data);
		
		inputList.setInput("detail");
		
		<%if(plant.equals("")){%>
        $('select[name="LOTA02"]').multipleSelect('checkAll');
        <%}else{%>
        $('select[name="LOTA02"]').multipleSelect('setSelects', ['<%=plant%>']);
        <%}%>
	});
	

	
	function fn_closing(){
		this.close();
	}

	
	
	function gridListEventColValueChange(gridId, rowNum, colName, colValue){
		
		
	}
	
	function commonBtnClick(btnName){
		if(btnName == "SAVE"){
			saveData();
		}
	}
	
	function saveData() {
								
		var SIGNI = $.trim($("#SIGNI").val());
		var EXTI2 = $.trim($("#EXTI2").val());
		var ZLGORT = $.trim($("#ZLGORT").val());
		
	    var param = new DataMap();
	    param.put("list", paramH.get("data"));	    
	   /*  param.put("SIGNI", SIGNI);
        param.put("EXTI2", EXTI2); */
        param.put("ZLGORT", ZLGORT);
              
        json = netUtil.sendData({
	        url : "/wms/outbound/json/DL08POPSave.data",
	        param : param
	    });
	    
	    if(json && json.data){
	    	
	    	if(json.data["CNT"] > 0){
	    		
	        	commonUtil.msgBox("COOMON_M0463"); //저장이 완료되었습니다.
	    		callback();
	        	
	    	}else{
	    		commonUtil.msgBox("처리실패"); //저장이 완료되었습니다.
	    	}
	        
	    }	
	}
	
	function comboEventDataBindeBefore(comboAtt){
        if(comboAtt == "Wms,PLNTKYCOMBO"){
            var param = new DataMap();
	        param.put("WAREKYS", "'<%= wareky%>'");
            param.put("OWNRKY", "<%=ownrky%>");
            param.put("LANGKY", "<%=langky%>");
            
            return param;
        }
    }
	
	function searchHelpEventOpenBefore(searchCode, gridType, $inputObj){
        var param = new DataMap();
        if(searchCode == "SHSTLMAP2"){
	    	
	    	var PLNTKY = $('select[name="LOTA02"]').val();
			
			if(!PLNTKY) {
				var label = commonLabel.getLabel("STD_LOTA02", 1);
				commonUtil.msgBox("VALID_required",[label]); // {0} 은(는) 필수 입력항목입니다.
				return false;
			}
			param.put("PLNTKY", PLNTKY);
	    	
	    	return param;
	    }
    } 
	
	function callback () {
		
		window.opener.DocPopCallback();
		self.close();
		
	}
</script>
</head>
<body>
<%@ include file="/common/include/webdek/layout.jsp" %>
<!-- content -->
<div class="content_wrap">
	<div class="content_inner">
		<%@ include file="/common/include/webdek/title.jsp" %>
		<div class="content_serch" id="searchArea" style="height: 20px;">
			<div class="btn_wrap">
				<div class="fl_l">					
				</div>
				<div class="fl_r">
					<input type="button" CB="SAVE SAVE BTN_SAVE" />				      
				</div>
			</div>			
		</div>

				<div class="inner_search_wrap">
						 <table class="detail_table" id="detail">
									<colgroup>
			                            <col width="20%">
			                            <col width="80%">
			                        </colgroup>
			                        <tbody>         
			                            <!-- <tr>
			                                <th cl="STD_SIGNI">컨테이너번호</th>
			                                <td>
			                                    <input type="text" class="input" name="SIGNI" id="SIGNI" style="width:calc(100% - 26px);height: 100%;border: 0;margin: 0;">
			                                </td>
			                            </tr>
			                            <tr>
			                                <th cl="STD_EXTI2">SEAL No</th>
			                                <td>
			                                    <input type="text" class="input" name="EXTI2" id="EXTI2" style="width:calc(100% - 26px);height: 100%;border: 0;margin: 0;">
			                                </td>
			                            </tr>  -->
			                             <tr>
			                                <th cl="STD_ZLGORT">CY저장위치</th>
			                                <td> 
			                                    <input type="text" class="input" name="ZLGORT" id="ZLGORT" UIInput="S,SHSTLMAP2" IAname="Search" UIFormat="U" style="width:calc(100% - 36px);height: 100%;border: 0;margin: 0;">
			                                    <!-- <button type="button" class="btn btn_search"><span>search</span></button> -->
			                                </td>			                                 
				                    	 </tr>  
				                    	  <tr hidden="hidden">
			                                <th cl="STD_LOTA02">CY저장위치</th>
			                                <td> 
			                                    <select type="text" class="input" Combo="Wms,PLNTKYCOMBO" name="LOTA02" id="PLNTKY" ComboType="MS" style="width: 160px;" validate="required">
                            					</select>
			                                </td>			                                 
				                    	 </tr>  
			                        </tbody>
								</table> 
				</div>
		</div>						
		<!-- //contentContainer -->
	
	
			
</div>
<!-- //content -->
<%@ include file="/common/include/webdek/bottom.jsp" %>
</body>
</html>