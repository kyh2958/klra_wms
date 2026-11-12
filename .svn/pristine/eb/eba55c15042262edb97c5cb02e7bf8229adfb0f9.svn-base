<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%	
	String LOTA02 = request.getParameter("LOTA02");
	String LOCGRPCD = request.getParameter("LOCGRPCD");
	String LOCLINE = request.getParameter("LOCLINE");

	if(LOTA02 == null){
		LOTA02 = "";
	}
	
	if(LOCGRPCD == null){
		LOCGRPCD = "";
	}
	
	if(LOCLINE == null){
		LOCLINE = "";
	}
	
%>
<!doctype html>
<head>
<style type="text/css">
input:read-only { 
	background-color: #e2e2e2;
}
select:disabled {
	background-color: #e2e2e2;
}
button {
	vertical-align: bottom;
    height: 37px;
}
.tem5_content .tableWrap_search {overflow: hidden;}
</style>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>Mobile WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	
	$(document).ready(function(){	
		gridList.setGrid({
			id : "gridList",
			editable : false,
			//module : "WcsIF",					  
			//command : "VW_LOC_GROUP",
			url : "/WcsIF/list/json/VW_LOC_INFO.data", 
			gridMobileType : true,
			emptyMsgType:false
		});
		
		searchList();
	});
	
	// 작업지시번호로 문서정보검색
	function searchList(){		
		var BARCOD = $("#BARCOD").val();
		var param = new DataMap();
		
		param.put("WAREKY", "<%=wareky%>");
		param.put("OWNRKY", "<%=ownrky%>");
		param.put("LANGKY", "<%=langky%>");
		param.put("LOCGRPCD", "<%=LOCGRPCD%>");
		
		gridList.gridList({
			id : "gridList",
			param : param
		});		
	}
	
	function gridListEventRowDblclick(gridBox, rowNum){
	}

	function gridListEventDataBindEnd(gridId, dataLength){
		
		
		if(gridId == "gridList" && dataLength > 0){
            var dataList = gridList.getGridData(gridId);
            var rowData;
            
            for(var i = 0; i< dataList.length;i++){
                rowData = dataList[i];
                
                var LOC_FLAG_NM = rowData.get("LOC_FLAG_NM");
                
				if(LOC_FLAG_NM == "비활성화"){          	
                	//gridList.setRowReadOnly(gridId, i, true, ["LOC_FLAG_NM"]);
                	gridList.setRowCss(gridId, i, {'color':'grey'});
                }
            }
        }
	}
	
	function showMain() {
		window.top.closePage();
	}
	
	function eventKeydown(event,objId){
		
		if(event.keyCode == "13" && objId == "BARCOD"){
			var barcod = $.trim($("#BARCOD").val());
			
			if(barcod == ""){
			    
			    PlaySound("e");                 
			    commonUtil.msgBox("VALID_M0936", null, null, function () {  // 바코드 입력해주세요.
			        $('#BARCOD').focus();               
			    }); 
			
			    return; 
			}
			
			var list = gridList.getGridData('gridList', true);
			var finded = list.find(data => data.get("LOTA14") == barcod); // 바코드가 동일하면서 완파레트가 아닌 바코드 스캔시 피킹수량 조절메세지 발생
			
			if(finded) {
				var rowNum = finded.get("GRowNum");
				gridList.setColFocus("gridList", rowNum, 'LOTA14');
			}else {
				PlaySound("e");
				commonUtil.msgBox("MOBILE_M0031", null, null, function () {  // 이미 스캔한 바코드입니다. 입력수량 변경을 통해 피킹처리 해주세요.
			    	$('#BARCOD').focus();          
			    }); 
				return ;
			}
		}
	}
	
	function previtem() {
		var checkCnt;
		
		location.href='/mobile/PDWCS.page';
	}
	
</script>
</head>
<body>
	<div class="main_wrap">
		<div class="tem4_content">
			<div class="select_box" id="bindArea">
				<div id="searchArea">
					<table>
						<colgroup>
							<col width="25%" />
							<col width="25%" />
							<col width="35%" />
							<col width="15%" />
						</colgroup>
						<tbody>
							<tr style="display:none">
								<th CL="STD_OWNRKY" width="100">화주</th>
								<td>
									<input type="hidden" id="selectSKU">
								</td>
							</tr>
							<tr>
								<th  CL='STD_BARCOD'>바코드</th>
								<td colspan="3">
									<input type="text" class="text" name="BARCOD" id="BARCOD" onfocus="this.select();" onkeypress="eventKeydown(event,this.id)"/>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="tableWrap_search section" >
				<div class="tableHeader">						 
					<table style="width: 100%">
						<colgroup>
							<!-- 
							<col width="75px" />
							<col width="140px" />
							<col width="300px" />
							<col width="55px" />
							<col width="55px" />
							 --> 
							<col width="35px" />
							<col width="80px" />
							<col width="100px" />
							<col width="40px" />
							<col width="25px" />	
							<col width="40px" />	
							<col width="60px" />	
							<!-- <col width="25px" /> -->	
							<col width="60px" />	
							<col width="60px" />	
							<!-- <col width="60px" /> -->	
							<!-- <col width="60px" /> -->							
						</colgroup> 
						<thead>
							<tr>
							<!-- 
								<th CL="STD_LOC_GRPCD">아이템상태</th>
								<th CL="STD_LOC_GRPNM">아이템상태</th>
								<th CL="STD_LOC_LINE">아이템상태</th>
								<th CL="STD_SDESC01">아이템상태</th>
								<th CL="STD_LOC_CD">아이템상태</th>
							
							 -->
								<th CL="STD_NO">NO</td>		
								<th CL="STD_SKUKEY">품목코드</th>
								<th CL="STD_LOTA14">개별바코드</th>
								<th CL="STD_QTSIWH">수량</th>
								<th CL="WCS_LOC_LV">단</th>
								<th CL="WCS_LOC_HIGH">높이</th>
								<th CL="WCS_LOC_NM">로케이션</th>
								<!-- <th CL="WCS_LOC_STATE">상태</th> -->
								<!-- <th CL="WCS_CREATE_BY">생성자</th> -->
								<th CL="WCS_LOC_FLAG_NM">지번상태</th>
								<th CL="WCS_CREATE_DT">적재일</th>
								<!-- <th CL="WCS_UPDATE_DT">수정일</th> -->
							</tr>	
						</thead>
					</table>
				</div>
				<div class="tableBody">
					<table style="width: 100%">
						<colgroup>
							<!-- 
							<col width="75px" />
							<col width="140px" />
							<col width="300px" />
							<col width="55px" />
							<col width="55px" />
							 --> 
							<col width="35px" />
							<col width="80px" />
							<col width="100px" />
							<col width="40px" />
							<col width="25px" />	
							<col width="40px" />	
							<!-- <col width="60px" /> -->	
							<!-- <col width="25px" /> -->	
							<col width="60px" />	
							<col width="60px" />	
							<col width="60px" />	
							<!-- <col width="60px" /> -->	
						</colgroup>
						<tbody id="gridList">
							<tr CGRow="true">
								<!-- 
								<td GCol="text,LOC_GRPCD">그룹코드</td>
								<td GCol="text,LOC_NM">그룹명</td>
								<td GCol="text,LOC_LINE">그룹LINE</td>
								<td GCol="text,DESC01">품목명</td>								
								<td GCol="text,LOC_CD">로케이션</td>
								 -->
								<td GCol="rownum">NO</td>
								<td GCol="text,SKUKEY">품목코드</td>
								<td GCol="text,LOTA14" >개별바코드</td>
								<td GCol="text,QTSIWH">수량</td>
								<td GCol="text,LOC_LV" GF="N 1">레벨</td>
								<td GCol="text,LOC_HIGH">PLT높이</td>
								<td GCol="text,LOC_NM">로케이션명</td>
								<!-- <td GCol="text,LOC_STATE">상태</td> -->
								<!-- <td GCol="text,CREATE_BY">생성자</td> -->
								<td GCol="text,LOC_FLAG_NM">지번상태</td>
								<td GCol="text,CREATE_DT" GF="C">적재일</td>
								<!-- <td GCol="text,UPDATE_DT" GF="C">수정일</td> -->								
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<!-- gridListHidden2 바코드 스캔 후 넘길 데이터만 가지고오기 -->
			<div style="margin-bottom: 0px;display:none;">
				<div class="tableBody">
					<table style="width: 900px">
						<tbody id="gridListSub">
							<tr CGRow="true">
								<td GCol="text,TASKKY">작업문서번호</td>
								<td GCol="text,TASKIT">작업문서아이템</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>		
			<div class="select_box" style='display:none;'>
				<table>
					<colgroup>
						<col width="80px"/>
						<col width="50px"/>
						<col width="60px"/>
						<col width="50px"/>
						<col width="30px"/>
						<col width="50px"/>
						<col width="60px"/>
					</colgroup>
					<tbody>
						<tr>
							<th  CL='STD_COMPCNT'>완료건수</th>
							<td CL='STD_ALL'>전체</td>
							<td>
								<input type="text" class="text gridColInputNumber" name="COMPCNT" id="COMPCNT" readonly/>
							</td>
							<td CL='STD_CNT'>count</td>
							<td CL='STD_INCLU'>중</td>
							<td>
								<input type="text" class="text gridColInputNumber" name="COMPCNT2" id="COMPCNT2" readonly/>
							</td>
							<td CL='STD_CNT'>count</td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- end table_body -->
			<div class="footer_5" style="margin-top: 5px; position:fixed;" >
				<table>
					<tr>
						<td class=f_1 width="30%" onclick="window.top.closePage()">
							<label CL='BTN_CLOSE'></label>
						</td>						
						<td class=f_1 width="30%" onclick ="searchList()" >
							<label CL='BTN_REFRESH'></label>
						</td>
						<td class=f_1 width="30%" onclick="previtem()">
							<label CL='BTN_PREVITEM'></label>
						</td>						
					</tr>
				</table>
			</div><!-- end footer_5 -->	
		</div>
	</div>
</body>