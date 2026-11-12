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
 #detail{margin-bottom:10px;}
 h2{font-weight: bold; padding:10px 5px}
</style>
<script type="text/javascript">
	function searchList() {
		if (validate.check("searchArea")) {
			var param = inputList.setRangeParam("searchArea");
			var numobj = param.get("NUMOBJ");
			var numChk = new DataMap();
			numChk.put("NUMOBJ", numobj);

			 var json = netUtil.sendData({
				module : "WmsAdmin",
				command : "NUMOBJval",
				sendType : "map",
				param : param
			});
			
			if (json.data["CNT"] > 0) {
				flag = "U";
			
				netUtil.send({
					module : "WmsAdmin",
					command : "NR01",
					bindId : "tabs1-1",
					sendType : "map",
					bindType : "field",
					param : param
				});
				//searchOpen(false);
			} else {
				flag = "I";
				//searchOpen(false);
			} 
		}

	}

	function saveData() {
		if (!commonUtil.msgConfirm(configData.MSG_MASTER_SAVE_CONFIRM)) {
			return;
		}
		var list = dataBind.paramData("content");
		var param = new DataMap();

		param.put("list", list);
		param.put("flag", flag);

		var json = netUtil.sendData({
			url : "/wms/admin/json/NR01.data",
			param : param
		});

		if (json && json.data) {
			searchList();
		}
	}
	
	function commonBtnClick(btnName){
		if(btnName == "Search"){
			searchList();
		}else if(btnName == "Save"){
			saveData();
		}else if(btnName == "Execute"){
			test3();
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
						<dt CL="STD_NUMOBJ">채번 오브젝트</dt>
						<dd>
							<input type="text" name="NUMOBJ" class="input" UIInput="S,SHNMOBJ" validate="required" /> 
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
					<li><a href="#tabs1-1" CL='STD_GENERAL'><span>일반</span></a></li>  
				</ul>
				
				<div class="table_box section" id="tabs1-1">
					<div class="inner_search_wrap">
						<h2>일반</h2>
						<table class="detail_table" id="detail">
							<colgroup>
	                            <col width="20%" />
	                            <col width="80%"/>
	                        </colgroup>
	                        <tbody>         
	                            <tr>
	                                <th CL="STD_SHORTX">설명</th>
	                                <td>
	                                    <input type="text" class="input" name="SHORTX" style="width: 100%;height: 100%;border: 0;margin: 0;"/>
	                                </td>
	                            </tr>
	                            <tr>
	                                <th CL="STD_NUMBFR">From 번호</th>
	                                <td>
	                                    <input type="text" class="input" name="NUMBFR" style="width: 100%;height: 100%;border: 0;margin: 0;" readonly="readonly"/>
	                                </td>
	                            </tr> 
	                            <tr>
	                                <th CL="STD_NUMBTO">To 번호</th>
	                                <td>
	                                    <input type="text" class="input" name="NUMBTO" style="width: 100%;height: 100%;border: 0;margin: 0;" readonly="readonly"/>
	                                </td>
	                            </tr> 
	                            <tr>
	                                <th CL="STD_NUMBCR">현재 번호</th>
	                                <td>
										<input type="text" class="input" name="NUMBST" GF="N 10" style="width: 100%;height: 100%;border: 0;margin: 0;"/>
	                                </td>
	                            </tr> 
	                        </tbody>
						</table>
						<h2>조회</h2>
						<table class="detail_table" id="detail2">
							<colgroup>
	                            <col width="5%" />
	                            <col width="20%"/>
	                            <col width="5%" />
	                            <col width="20%"/>
	                            <col width="5%" />
	                            <col width="20%"/>
	                            <col width="5%" />
	                            <col width="20%"/>
	                        </colgroup>
	                        <tbody>         
	                            <tr>
	                                <th CL="STD_CREDAT">생성일자</th>
	                                <td>
	                                    <input type="text" class="input" name="CREDAT" GF="D" style="width: 100%;height: 100%;border: 0;margin: 0;" readonly="readonly" />
	                                </td>
	                                <th CL="STD_CRETIM">생성시간</th>
	                                <td>
	                                    <input type="text" class="input" name="CREDAT" GCol="text,CRETIM" GF="T" style="width: 100%;height: 100%;border: 0;margin: 0;" readonly="readonly" />
	                                </td>
	                                <th CL="STD_CREUSR">생성자</th>
	                                <td>
	                                    <input type="text" class="input" name="CREUSR" style="width: 100%;height: 100%;border: 0;margin: 0;" readonly="readonly" />
	                                </td>
	                                <th CL="STD_CUSRNM">생성자명</th>
	                                <td>
	                                    <input type="text" class="input" name="CUSRNM" style="width: 100%;height: 100%;border: 0;margin: 0;" readonly="readonly" />
	                                </td>
	                            </tr>
	                            <tr>
	                                <th CL="STD_LMODAT">수정일자</th>
	                                <td>
	                                    <input type="text" class="input" name="LMODAT" GF="D" style="width: 100%;height: 100%;border: 0;margin: 0;" readonly="readonly" />
	                                </td>
	                                <th CL="STD_LMOTIM">수정시간</th>
	                                <td>
	                                    <input type="text" class="input" name="LMOTIM" GF="T" style="width: 100%;height: 100%;border: 0;margin: 0;" readonly="readonly" />
	                                </td>
	                                <th CL="STD_LMOUSR">수정자</th>
	                                <td>
	                                    <input type="text" class="input" name="LMOUSR" style="width: 100%;height: 100%;border: 0;margin: 0;" readonly="readonly" />
	                                </td>
	                                <th CL="STD_LUSRNM">수정자명</th>
	                                <td>
	                                    <input type="text" class="input" name="LUSRNM" style="width: 100%;height: 100%;border: 0;margin: 0;" readonly="readonly" />
	                                </td>
	                            </tr> 
	                        </tbody>
						</table>
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