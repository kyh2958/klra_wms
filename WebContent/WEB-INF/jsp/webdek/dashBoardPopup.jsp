<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WMS</title>
<%@ include file="/common/include/webdek/head.jsp" %>
<link rel="stylesheet" type="text/css" href="/common/theme/webdek/css/demo_layout.css?ver=<%=dateTime%>">
<script type="text/javascript">
	$(document).ready(function(){
		gridList.setGrid({
			id : "gridList",
			module : "WmsSystem",
			command : "DASHBOARDPOP",
		});
		
		searchList();
	});

	function searchList(){
		var param = new DataMap();
		param.put('WAREKY', '<%=wareky%>');

		gridList.gridList({
			id : "gridList",
			param : param
		});
	}
</script>
</head>
<body>
<%@ include file="/common/include/webdek/layout.jsp" %>
<!-- content -->
<div class="content_wrap">
	<div class="content_inner">
		<%@ include file="/common/include/webdek/title.jsp" %>
		<div class="search_next_wrap">
			<div class="content_layout tabs">
				<div class="table_box section" id="tab1-1">
					<div class="table_list01">
						<div class="scroll">
							<table class="table_c">
								<tbody id="gridList">
									<tr CGRow="true">
										<td GH="150" GCol="text,GUBUN"				>구분			</td>
										<td GH="150" GCol="text,KPCAPA"	GF="N 20,3"	>보관Capa		</td>
										<td GH="150" GCol="text,QTSIWH"	GF="N 20,3"	>재고량		</td>
										<td GH="150" GCol="text,CLRNCE"	GF="N 20,3"	>여유(a-b)	</td>
										<td GH="150" GCol="text,PERLOD"	GF="N 20,3"	>적재율(비율)	</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="btn_lit tableUtil">
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