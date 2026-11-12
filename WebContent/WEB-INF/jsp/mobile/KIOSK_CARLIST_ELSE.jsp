<table style="width: 100%">
	<colgroup>
		<col width="20px" />
		<col width="20px" />
		<col width="60px" />   <!-- 납품처 : 제일 길게 -->
		<col width="40px" />   <!-- 지역 -->
		<col width="30px" />   <!-- 선적번호 유지 -->
		<col width="20px" />   <!-- BAG/BOX 유지 -->
		<col width="20px" />   <!-- BOX/Pallet 유지 -->
	</colgroup>
	<thead>
		<tr>
			<th CL="STD_NO">1</th>
			<th GBtnCheck="true"></th>
			<th CL="STD_KUNNR">납품처</th>
			<th CL="STD_REGION">지역</th>
			<th CL="STD_STKNUM">선적번호</th>
			<th CL='STD_BOXQT'>BAG/BOX</th>
			<th CL='STD_PLTQT'>BOX/Pallet</th>
		</tr>
	</thead>
</table>
</div>
</div>

<div class="tableWrap_search section">
	<div class="tableBody">
		<table style="width: 100%">
			<colgroup>
				<col width="20px" />
				<col width="20px" />
				<col width="60px" />
				<col width="40px" />
				<col width="30px" />
				<col width="20px" />
				<col width="20px" />
			</colgroup>
			<tbody id="gridList">
				<tr CGRow="true">
					<td GCol="rownum">1</td>
					<td GCol="rowCheck"></td>
					<td GCol="text,KUNNR">납품처</td>
					<td GCol="text,REGION">지역</td>
					<td GCol="text,STKNUM">선적번호</td>
					<td Gcol="text,BOXQT" GF="N 20, 2">BOXQT</td>
					<td Gcol="text,PLTQT" GF="N 20, 2">BOX/Pallet</td>
				</tr>
			</tbody>
		</table>

		<table style="width: 100%; display: none">
			<colgroup>
				<col width="20px" />
				<col width="20px" />
				<col width="60px" />
				<col width="40px" />
				<col width="30px" />
				<col width="20px" />
				<col width="20px" />
			</colgroup>
			<tbody id="gridListSkuSum">
				<tr CGRow="true">
					<td GCol="rownum">1</td>
					<td GCol="rowCheck"></td>
					<td GCol="text,KUNNR">납품처</td>
					<td GCol="text,REGION">지역</td>
					<td GCol="text,STKNUM">선적번호</td>
					<td Gcol="text,BOXQT" GF="N 20, 2">BOXQT</td>
					<td Gcol="text,PLTQT" GF="N 20, 2">BOX/Pallet</td>
				</tr>
			</tbody>
		</table>