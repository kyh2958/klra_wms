<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
      content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width" />
<title>상차 확인</title>

<!-- 최소 필수 JS만 로딩 -->
<%@ include file="/mobile/include/head.jsp" %>
<script src='/mobile/js/signpad.js'></script>

<style>
body { margin:0; padding:10px; font-size:14px; }
table { width:100%; border-collapse:collapse; }
th { text-align:left; padding:8px 5px; width:90px; }
td { padding:8px 5px; }
select, input { width:100%; height:32px; font-size:14px; }
canvas { border:1px solid #ccc; width:100%; height:150px; }
.btn-area { margin-top:15px; display:flex; gap:10px; }
.btn {
    flex:1;
    height:40px;
    font-size:15px;
    border:none;
    border-radius:4px;
}
.btn-confirm { background:#007bff; color:#fff; }
.btn-cancel  { background:#6c757d; color:#fff; }
.btn-clear   { margin-top:5px; width:100%; height:30px; }
.guide-box {
    background: #f2f6fb;
    border: 1px solid #c9d8ef;
    border-radius: 8px;
    padding: 18px 12px;
    margin-bottom: 16px;
    text-align: center;
    font-size: 17px;
    font-weight: 600;
    color: #1f3c88;
    line-height: 1.5;
}
body {
  margin:0;
  padding:2vw;
  font-size:clamp(11px, 2.8vw, 14px);
  background:#fff;
  overflow-x:hidden;
  -webkit-text-size-adjust:none;
}

* {
  box-sizing:border-box;
}

.guide-box {
  background:#f2f6fb;
  border:1px solid #c9d8ef;
  border-radius:8px;
  padding:clamp(8px, 2.5vw, 14px);
  margin-bottom:clamp(8px, 2.5vw, 12px);
  text-align:center;
  font-size:clamp(12px, 3.5vw, 17px);
  font-weight:600;
  color:#1f3c88;
  line-height:1.4;
}

/* 기존 가로 구조 유지 */
.flex-zone{
  display:block !important;
  width:100% !important;
  margin-bottom:clamp(8px, 2vw, 12px);
  overflow:hidden;
}

.flex-zone > .row-wrap{
  display:flex !important;
  flex-direction:row !important;
  width:100% !important;
  align-items:stretch !important;
}

/* 공통 셀 */
.flex-zone > .row-wrap > .cell{
  /*display:flex !important;*/
  flex-direction:column !important;
  justify-content:flex-start !important;
  align-items:center !important;
  min-width:0 !important;
  box-sizing:border-box !important;
  border-top:1px solid #bfc7d5 !important;
  border-left:1px solid #bfc7d5 !important;
  padding:clamp(4px, 1.5vw, 8px) clamp(2px, 1vw, 6px) !important;
  text-align:center !important;
  overflow:hidden !important;
  background:#fff;
}

/* 공통 셀 */
/* 마지막 열 (오른쪽 border 추가) */
.flex-zone > .row-wrap > .cell:last-child {
  border-right:1px solid #bfc7d5 !important;
}

/* 마지막 행 (아래 border 추가) */
.flex-zone > .row-wrap:last-child > .cell {
  border-bottom:1px solid #bfc7d5 !important;
}

/* AJ / KPP 헤더칸 */
.flex-zone > .row-wrap > .cell.fixed{
  flex:0 0 clamp(25px, 7vw, 35px) !important;
  max-width:clamp(25px, 7vw, 35px) !important;
  justify-content:center !important; 
  min-width:25px !important;
  font-size:clamp(10px, 2.8vw, 13px);
  text-align:center;
  display: none;
}



/* 나머지 품목칸 균등 */
.flex-zone > .row-wrap > .cell:not(.fixed){
  flex:1 1 0 !important;
}

/* 이미지 자동 비율 */
.flex-zone > .row-wrap img {
  display:block;
  width:100%;
  height:auto;
  margin:0 auto clamp(2px, 1vw, 6px);
}

.flex-zone > .row-wrap img[alt="P-A11"] {
    width: 60%;
}

/* 품목코드 */
.label{
  font-size:clamp(10px, 2.7vw, 14px);
  font-weight:600;
  line-height:1.2;
  margin-bottom:clamp(2px, 1vw, 6px);
  word-break:break-all;
  display: none;
}

/* 납품처 입력 영역 */
.dlv-wrap{
  display:flex;
  flex-direction:column;
  gap:clamp(3px, 1vw, 6px);
  margin-top:clamp(3px, 1vw, 6px);
  width:100%;
}

.dlv-item{
  width:100%;
  min-width:0;
  text-align:left;
}

.dlv-item .dlv-name{
  font-size:clamp(9px, 3.5vw, 14px);
  font-weight:600;
  line-height:1.2;
  margin-top:-20px;
  margin-bottom:2px;
  white-space:normal;
  word-break:break-all;
  overflow:hidden;
  display:-webkit-box;
  -webkit-line-clamp:2;
  -webkit-box-orient:vertical;
  min-height:clamp(20px, 6vw, 28px);
  text-align: center;
  letter-spacing: -1.5px;
  display: none;
}

.dlv-item input{
  width:90%;
  height:clamp(24px, 8vw, 36px);
  font-size:clamp(11px, 3.5vw, 15px);
  margin-top:-10px;
  padding:0 4px;
  border:1px solid #cfd6e4;
  border-radius:3px;
  box-sizing:border-box;
  display: block;
  margin: 0 auto;
  text-align: center;
  letter-spacing: -2px;
}

/* 서명영역 */
#signpad{
  margin-top:clamp(8px, 2vw, 12px);
}

#signpad canvas,
#signature-pad-canvas{
  width:90% !important;
  height:clamp(100px, 33vw, 160px) !important;
  display: block;
  margin-left: auto;
  margin-right: auto;
}

#signpad .buttons{
  display:flex;
  gap:clamp(4px, 1.5vw, 8px);
  margin-top:clamp(6px, 2vw, 10px);
}

#signpad .buttons .btn{
  flex:1 1 0 !important;
  width:auto !important;
  height:clamp(32px, 9vw, 44px) !important;
  font-size:clamp(12px, 3vw, 16px) !important;
}

.btn {
  height:clamp(32px, 9vw, 44px);
  font-size:clamp(12px, 3vw, 16px);
  border:none;
  border-radius:4px;
}

.btn-confirm { background:#007bff; color:#fff; }
.btn-cancel  { background:#6c757d; color:#fff; }
.btn-clear   { margin-top:5px; width:100%; height:clamp(28px, 7vw, 34px); }

.empty-box{
  border:1px solid #ddd;
  background:#fff;
  padding:14px;
  text-align:center;
  color:#666;
}
</style>

<script>
// ============================
// 전역: 납품처 Top5 리스트
// ============================
var g_palletConfigList = [];
var g_dlvTopList = []; // [{PTNRKY, PTNRNM, ...}]

//  (sendData 리턴 구조가 환경마다 달라서) 안전 파서
function pickRowsFromSendDataResult(res){
  if(!res) return [];
  if(Array.isArray(res)) return res;
  if(Array.isArray(res.data)) return res.data;
  if(Array.isArray(res.list)) return res.list;
  if(res.result && Array.isArray(res.result)) return res.result;
  if(res.data && Array.isArray(res.data.list)) return res.data.list;
  return [];
}

function escapeHtml(s){
  return (s||"").toString()
    .replace(/&/g,"&amp;")
    .replace(/</g,"&lt;")
    .replace(/>/g,"&gt;")
    .replace(/'/g,"&#39;")
    .replace(/"/g,"&quot;");
}

// 숫자 입력 안전 처리(빈값/음수 방지)
function toInt(v){
  var n = parseInt((v||"").toString(), 10);
  if(isNaN(n) || n < 0) return 0;
  return n;
}

// ============================
//모든 품목에 납품처 입력칸 생성
// ============================
function renderDlvInputsAll(rows){
  g_dlvTopList = rows || [];

  $(".dlv-wrap").each(function(){
    var $wrap   = $(this);
    var skuKey  = ($wrap.data("skukey") || "").toString();
    var ifCode  = ($wrap.data("ifcode") || "").toString();
    var vendor  = ($wrap.data("vendor") || "").toString();

    $wrap.empty();

    if(!g_dlvTopList.length){
      $wrap.append(
        "<div class='dlv-item' style='flex:1 1 100%'>" +
          "<div class='dlv-name'>납품처</div>" +
          "<input type='text' class='dlv-qty' placeholder='수량' " +
                "data-skukey='"+ escapeHtml(skuKey) +"' " +
                "data-ifcode='"+ escapeHtml(ifCode) +"' " +
                "data-vendor='"+ escapeHtml(vendor) +"' " +
                "data-ptnrky='' data-ptnrnm='' value='' oninput='this.value = this.value.replace(/[^0-9]/g, \"\")' maxlength='2' step='1' inputmode='numeric' />" +
        "</div>"
      );
      return;
    }

    for(var i=0; i<g_dlvTopList.length; i++){
      var r = g_dlvTopList[i] || {};
      var ptnrky = (r.PTNRKY || "").toString();
      var ptnrnm = (r.PTNRNM || "").toString();

      $wrap.append(
        "<div class='dlv-item'>" +
          "<div class='dlv-name' title='"+ escapeHtml(ptnrnm) +"'>" +
            escapeHtml(ptnrnm || ("납품처" + (i+1))) +
          "</div>" +
          "<input type='text' class='dlv-qty' placeholder='"+escapeHtml(ptnrnm)+"' " +
                "data-skukey='"+ escapeHtml(skuKey) +"' " +
                "data-ifcode='"+ escapeHtml(ifCode) +"' " +
                "data-vendor='"+ escapeHtml(vendor) +"' " +
                "data-ptnrky='"+ escapeHtml(ptnrky) +"' " +
                "data-ptnrnm='"+ escapeHtml(ptnrnm) +"' " +
                "value='' oninput='this.value = this.value.replace(/[^0-9]/g, \"\")' maxlength='2' step='1' inputmode='numeric' />" +
        "</div>"
      );
    }
  });
}

// ============================
//  납품처 Top5 조회 호출
// ============================
function loadDlvTop5(){
  var STKNUM = $("#STKNUM").val();
  var WAREKY = $("#WAREKY").val();

  if(!STKNUM){
    console.warn("STKNUM 파라미터가 없습니다.");
    renderDlvInputsAll([]); // fallback
    return;
  }

  var param = new DataMap();
  param.put("STKNUM", STKNUM);
  param.put("WAREKY", WAREKY);

  var res = netUtil.sendData({
    module : "WmsMobileOutboundPicking",
    command : "PDLD21_DLV_CNT",
    param : param
  });

  console.dir(res);

  var rows = pickRowsFromSendDataResult(res);
  

  renderDlvInputsAll(rows);
}

//공통코드로 품목 정보 가져오기 
function loadPalletConfig(){
	  var param = new DataMap();
	  param.put("CMCDKY", "PALLET");
	  param.put("USARG2", $("#PALLET_GB").val());

	  var res = netUtil.sendData({
	    module  : "WmsMobileOutboundPicking",
	    command : "PDLD21_PALLET_CONFIG",
	    param   : param
	  });

	  var rows = pickRowsFromSendDataResult(res);
	  g_palletConfigList = rows || [];

	  renderPalletArea(g_palletConfigList);
	}
	
// 이미지	USARG3 사용
function renderPalletArea(rows){
	  var $area = $("#palletArea");
	  $area.empty();

	  if(!rows || !rows.length){
	    $area.append("<div class='empty-box'>팔렛트 설정 정보가 없습니다.</div>");
	    return;
	  }

	  var groupMap = {};
	  for(var i=0; i<rows.length; i++){
	    var r = rows[i] || {};
	    var vendor = (r.USARG1 || "").toString();

	    if(!vendor){
	      vendor = "기타";
	    }

	    if(!groupMap[vendor]){
	      groupMap[vendor] = [];
	    }
	    groupMap[vendor].push(r);
	  }

	  var vendorOrder = [];
	  for(var j=0; j<rows.length; j++){
	    var v = (rows[j].USARG1 || "").toString();
	    if(v && $.inArray(v, vendorOrder) < 0){
	      vendorOrder.push(v);
	    }
	  }

	  for(var x=0; x<vendorOrder.length; x++){
	    var vendor = vendorOrder[x];
	    var itemList = groupMap[vendor];

	    var html = "";
	    html += "<div class='flex-zone'>";
	    html += "  <div class='row-wrap'>";
	    html += "    <div class='cell fixed'>" + escapeHtml(vendor) + "</div>";

	    for(var y=0; y<itemList.length; y++){
	      var item = itemList[y] || {};

	      var displayCode = (item.CMCDVL || "").toString(); // 팝업표시용
	      var ifCode      = (item.CDESC1 || "").toString(); // I/F용
	      var vendorNm    = (item.USARG1 || "").toString();
	      var imgFile     = (item.USARG3 || "").toString();

	      if(!imgFile){
	        imgFile = "plt11a.png";
	      }

	      html += "    <div class='cell'>";
	      html += "      <img src='/common/theme/webdek/images/" + escapeHtml(imgFile) + "' alt='" + escapeHtml(displayCode) + "'>";
	      html += "      <div class='label'>" + escapeHtml(displayCode) + "</div>";
	      html += "      <div class='dlv-wrap' ";
	      html += "           data-skukey='" + escapeHtml(displayCode) + "' ";
	      html += "           data-ifcode='" + escapeHtml(ifCode) + "' ";
	      html += "           data-vendor='" + escapeHtml(vendorNm) + "'>";
	      html += "      </div>";
	      html += "    </div>";
	    }

	    html += "  </div>";
	    html += "</div>";

	    $area.append(html);
	  }
	}
// ==============================
//  저장(사인 저장 콜백에서 호출)
// ==============================
function confirmLoad(signatureData) {

  var pltList = [];
  var sumMap  = {};

  $(".dlv-qty").each(function(){
    var $inp = $(this);

    var skuKey = ($inp.data("skukey") || "").toString();   // CMCDVL
    var ifCode = ($inp.data("ifcode") || "").toString();   // CDESC1
    var vendor = ($inp.data("vendor") || "").toString();   // USARG1
    var ptnrky = ($inp.data("ptnrky") || "").toString();
    var ptnrnm = ($inp.data("ptnrnm") || "").toString();
    var qty    = toInt($inp.val());

    if(qty <= 0) return;

    if(!ptnrky) ptnrky = " ";

    pltList.push({
      SKUKEY : skuKey,
      IFCODE : ifCode,
      VENDOR : vendor,
      PTNRKY : ptnrky,
      PTNRNM : ptnrnm,
      QTY    : qty
    });

    if(!sumMap[skuKey]) sumMap[skuKey] = 0;
    sumMap[skuKey] += qty;
  });

  if(pltList.length === 0){
    PlaySound('e');
    commonUtil.msgBox("파렛트 수량을 하나 이상 입력해 주세요.");
    return;
  }

  var pltSum = [];
  for(var k in sumMap){
    if(!sumMap.hasOwnProperty(k)) continue;
    pltSum.push({ SKUKEY: k, QTY: sumMap[k] });
  }

  var param = new DataMap();
  param.put("PLT_LIST", pltList);
  param.put("PLT_SUM",  pltSum);
  param.put("SIGN", signatureData);
  param.put("STKNUM", $("#STKNUM").val());
  param.put("WAREKY", $("#WAREKY").val());
  param.put("TASKKY", $("#TASKKY").val());
  param.put("PALLET_GB", $("#PALLET_GB").val());

  mobile.linkPopClose(param);
}

$(function(){
  
  loadPalletConfig(); // 품목/회사/이미지 먼저 그림
  loadDlvTop5(); // 1) 납품처 Top5 조회 → 모든 품목에 적용

  // 2) 사인패드 초기화
  $('#signpad').SignPad({
    width   : 100,
    height  : 150,
    userId  : 123,
    canvasId: 'signature-pad-canvas',
    styles  : {
      clearBtn: "btn btn-cancel",
      undoBtn : "btn btn-cancel",
      saveBtn : "btn btn-confirm"
    },
    onSave  : async (postData) => {
      console.log("Signature saved with data:");
      console.dir(postData);
      confirmLoad(postData.signature);
    }
  });

  $(document).on("click", "#close", function(){
    mobile.linkPopClose(null);
  });

});
</script>
</head>

<body>
<%
  String STKNUM = request.getParameter("STKNUM");
  String WAREKY = request.getParameter("WAREKY");
  String TASKKY = request.getParameter("TASKKY");
  String PALLET_GB = request.getParameter("PALLET_GB");
  if(STKNUM == null) STKNUM = "";
  if(WAREKY == null) WAREKY = "";
  if(TASKKY == null) TASKKY = "";
  if(PALLET_GB == null) PALLET_GB = "20";
%>

<input type="hidden" id="STKNUM" value="<%=STKNUM%>">
<input type="hidden" id="WAREKY" value="<%=WAREKY%>">
<input type="hidden" id="TASKKY" value="<%=TASKKY%>">
<input type="hidden" id="PALLET_GB" value="<%=PALLET_GB%>">

<div class="guide-box">파렛트 종류 및 수량을 입력해 주세요.</div>
<div id="palletArea"></div>
<div id="signpad"></div>


</body>
</html>