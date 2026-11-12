<table>
	<colgroup>
		<col width="95px" />
		<col width="50%" />
		<col width="*" />
	</colgroup>
	<tbody>
		<tr>
			<th CL="STD_LOTA06_TO">재고상태</th>
			<td colspan="2"><select id="LOTA06_TO" name="ALOTA06" Combo="WmsInventory,LOTA06COMBO" validate="required"><option value=""/></select></td>
		</tr>
		<tr>
			<th CL="STD_QTSIWH">재고수량</th>
			<td><input type="text" class="text number" name="QTSIWH"  id="QTSIWH" readonly/></td>
			<td><input type="text" class="text"        name="UOMKEY"  id="UOMKEY" readonly/></td>
		</tr>
		<tr>
			<th CL="STD_USEQTY">가용수량</th>
			<td><input type="text" class="text number" name="USEQTY"  id="USEQTY" readonly/></td>
			<td><input type="text" class="text"        name="UOMKEY"              readonly/></td>
		</tr>
		<tr>
			<th CL="STD_QTADJU">조정수량</th>
			<td><input type="text" class="text number" name="AQTADJU" id="AQTADJU"           validate="required" onfocus="fn_onfocus(this.id)" onchange="getQTYUOM(this.id);"/></td>
			<td><input type="text" class="text"        name="AUOMKEY"             readonly/></td>
		</tr>
		<tr>
			<th CL="STD_PQTYBOX">박스단위수량</th>
			<td><input type="text" class="text number" name="QTYBOX" id="QTYBOX"                                 onfocus="fn_onfocus(this.id)" onchange="getQTYUOM(this.id);"/></td>
			<td><input type="text" class="text"        name="BOXUOM" id="BOXUOM" readonly/></td>
		</tr>
		<tr>
			<th CL="STD_RSNADJ">조정사유코드</th>
			<td colspan="2"><select id="RSNADJ" name="ARSNADJ" Combo="airfw,RSNADJ" validate="required(STD_RSNADJ)" ></select></td>
		</tr>
		<tr>
			<th CL="STD_ADJRSN">조정상세사유</th>
			<td colspan="2"><input type="text" class="text" name="AADJRSN" id="AADJRSN" onfocus="fn_onfocus(this.id)"/></td>
		</tr>
	</tbody>
</table>
<table>
	<colgroup>
		<col width="95px" />
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
		<col width="95px" />
		<col width="*" />
	</colgroup>
	<tbody>
		<tr>
			<th CL="STD_DESC01">품목명</th>
			<td colspan="3"><input type="text" class="text" name="DESC01" id="DESC01" readonly/></td>
		</tr>
		<tr>
			<th CL='STD_TOTWG'>총중량</th>
			<td><input type="text" class="text number" name="TOTWG" id="TOTWG" readonly/></td>
			<th CL='STD_BAGQT'>EA/BAG</th>
			<td><input type="text" class="text number" name="BAGQT" id="BAGQT" readonly/></td>
		</tr>
		<tr>
			<th CL='STD_BOXQT'>BAG/BOX</th>
			<td><input type="text" class="text number" name="BOXQT" id="BOXQT" readonly/></td>
			<th CL='STD_PLTQT'>BOX/Pallet</th>
			<td><input type="text" class="text number" name="PLTQT" id="PLTQT" readonly/></td>
		</tr>
		<tr>
			<th CL='STD_SOKQT'>속(개/BALE)</th>
			<td><input type="text" class="text number" name="SOKQT" id="SOKQT" readonly/></td>
			<th CL='STD_EAQTY'>매(sheet/속)</th>
			<td><input type="text" class="text number" name="EAQTY" id="EAQTY" readonly/></td>
		</tr>
	</tbody>
</table>