<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<head>
<style type="text/css">
button {float: right; }
input:read-only { background-color: #e2e2e2; }
select:disabled { background-color: #e2e2e2; }
.number { text-align: right }
</style>
<meta charset="utf-8">
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width, target-densitydpi=medium-dpi" />
<meta name="format-detection" content="telephone=no" />
<title>Mobile WMS</title>
<%@ include file="/mobile/include/head.jsp" %>
<script>
	$(document).ready(function(){
		var data = mobile.getLinkPopData();
		dataBind.dataNameBind(data, "bindArea");
		
		$('.number').each(function (i, item) {
			item.value = setDecimalValue(item.value, 3);
		});
		
		$('.dateformat').each(function (i, item) {
			var value = item.value;
			if (value.trim()) {
				item.value = value.substr(0, 4) + '.' +
							 value.substr(4, 2) + '.' +
							 value.substr(6, 2);
			}
		});
	});
	
	function showMain(){
		mobile.linkPopClose();
	}
	
	function setDecimalValue(str, decimal) {
		str = new String(str);
		var regex = /[^0-9.]/g;
		var number = Number(str.replace(regex, ""));
		number = number ? number : 0;
		return number.toLocaleString(undefined, { minimumFractionDigits: decimal });
	}
</script>
</head>
<body>
	<div class="main_wrap" id="searchArea">
		<div class="tem4_content">
			<div class="select_box" id="bindArea" style="margin-bottom: 48px;">
				<table>
					<colgroup>
						<col width="20%" />
						<col width="*" />
					</colgroup>
					<tbody>
						<tr>
							<th CL="STD_LOCAKY">지번</th>
							<td colspan="3"><input type="text" class="text" name="LOCAKY" id="LOCAKY" readonly /></td>
						</tr>
					</tbody>
				</table>
				<table>
					<colgroup>
						<col width="20%" />
						<col width="*" />
					</colgroup>
					<tbody>
						<tr>
							<th CL="STD_SKUKEY">품목코드</th>
							<td><input type="text" class="text" name="SKUKEY"  id="SKUKEY"  readonly/></td>
							<td><input type="text" class="text" name="AEANCOD" id="AEANCOD" readonly/></td>
						</tr>
					</tbody>
				</table>
				<table>
					<colgroup>
						<col width="20%" />
						<col width="*" />
					</colgroup>
					<tbody>
						<tr>
							<th CL="STD_DESC01">품목명</th>
							<td colspan="3"><input class="text" name="DESC01" id="DESC01" readonly/></td>
						</tr>
						<tr>
							<th CL="STD_UOMKEY">단위</th>
							<td><input type="text" class="text" name="UOMKEY" id="UOMKEY" readonly /></td>
							<th CL="STD_QTSIWH">재고수량</th>
							<td><input type="text" class="text number" name="QTSIWH" id="QTSIWH" readonly /></td>
						</tr>
						<tr>
							<th CL="STD_USEQTY">가용수량</th>
							<td><input type="text" class="text number" name="USEQTY" id="USEQTY" readonly /></td>
							<th CL="STD_QTSALO">할당수량</th>
							<td><input type="text" class="text number" name="QTSALO" id="QTSALO" readonly /></td>
						</tr>
						<tr>
							<th CL="STD_QTSIQT">품질수량</th>
							<td><input type="text" class="text number" name="QTSIQT" id="QTSIQT" readonly /></td>
							<th CL="STD_QTSWQT">폐기수량</th>
							<td><input type="text" class="text number" name="QTSWQT" id="QTSWQT" readonly /></td>
						</tr>
						<tr>
							<th CL="STD_QTSHQT">보류수량</th>
							<td><input type="text" class="text number" name="QTSHQT" id="QTSHQT" readonly /></td>
							<th CL="STD_QTSCNU">불용수량</th>
							<td><input type="text" class="text number" name="QTSCNU" id="QTSCNU" readonly /></td>
						</tr>
						<tr>
							<th CL="STD_QTSPMO">이동 OUT수량</th>
							<td><input type="text" class="text number" name="QTSPMO" id="QTSPMO" readonly /></td>
							<th CL="STD_QTSPMI">이동 IN수량</th>
							<td><input type="text" class="text number" name="QTSPMI" id="QTSPMI" readonly /></td>
						</tr>
						<tr>
							<th CL='STD_TOTWG'>총중량</th>
							<td><input type="text" class="text number" name="TOTWG"  id="TOTWG"  readonly/></td>
							<th CL='STD_PLTQT'>BOX/Pallet</th>
							<td><input type="text" class="text number" name="PLTQT"  id="PLTQT"  readonly/></td>
						</tr>
						<tr>
							<th CL='STD_BOXQT'>BAG/BOX</th>
							<td><input type="text" class="text number" name="BOXQT"  id="BOXQT"  readonly/></td>
							<th CL='STD_BAGQT'>EA/BAG</th>
							<td><input type="text" class="text number" name="BAGQT"  id="BAGQT"  readonly/></td>
						</tr>
						<tr>
							<th CL='STD_SOKQT'>속(개/BALE)</th>
							<td><input type="text" class="text number" name="SOKQT"  id="SOKQT"  readonly/></td>
							<th CL='STD_EAQTY'>매(sheet/속)</th>
							<td><input type="text" class="text number" name="EAQTY"  id="EAQTY"  readonly/></td>
						</tr>
						<tr>
							<th CL="STD_SALENO">판매오더</th>
							<td><input type="text" class="text" name="SALENO" id="SALENO" readonly /></td>
							<th CL="STD_SKUNUM">품목번호</th>
							<td><input type="text" class="text" name="SKUNUM" id="SKUNUM" readonly /></td>
						</tr>
						<tr>
							<th CL="STD_LOTA11">제조일자</th>
							<td style="text-align: left;">
								<input type="text" class="text dateformat" name="LOTA11" id="LOTA11" readonly/>
							</td>
							<th CL="STD_LOTA12">입고일자</th>
							<td style="text-align: left;">
								<input type="text" class="text dateformat" name="LOTA12" id="LOTA12" readonly/>
							</td>
						</tr>
						<tr>
							<th CL="STD_LOTA13">유통기한</th>
							<td style="text-align: left;">
								<input type="text" class="text dateformat" name="LOTA13" id="LOTA13" readonly/>
							</td>
							<th CL="STD_LOTA08">국가코드</th>
							<td><input type="text" class="text" name="LOTA08" id="LOTA08" readonly /></td>
						</tr>
						<tr>
							<th CL="STD_LOTA14">개별바코드</th>
							<td colspan="3"><input type="text" class="text" name="LOTA14" id="LOTA14" readonly /></td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- end table_body -->
			<div class="footer_5" style="position: fixed;">
				<table>
					<tr>
						<td class=f_1 onclick ="showMain()" width="50%"><label CL='BTN_CLOSE'></label></td>
					</tr>
				</table>
			</div>
			<!-- end footer_5 -->	
		</div>
	</div>
</body>