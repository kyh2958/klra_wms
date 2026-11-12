package project.wms.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import project.common.bean.CommonConfig;
import project.common.bean.DataMap;
import project.common.controller.BaseController;
import project.common.service.CommonService;
import project.common.util.SqlUtil;
import project.wms.service.InboundService;
import project.wms.service.OutboundService;

@Controller
public class OutboundController extends BaseController {
	
	private static Logger log = Logger.getLogger(OutboundController.class);
	
	@Autowired
	private OutboundService outboundService;	
		
	@Autowired
	private InboundService inboundService;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("/wms/outbound/json/DL01Validation.*")
	public String DL01Validation(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		try{
			String result = outboundService.saveDL01ValueCheck(map);
			
			model.put("data", result);
		}catch(Exception e){
			model.put("data", "error");
			log.debug("Controller Error = "+e);
		}
		return JSON_VIEW;
	}
	
	/**
	 * 고객오더/창간 오더 정보 저장
	 * @param request
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/wms/outbound/json/DL01Save.*")
	public String DL01Save(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.saveDl01(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	/**
	 * 체크된 헤더 정보 일괄 저장
	 * @param request
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/wms/outbound/json/DL01AllChkeckSave.*")
	public String DL01AllChkeckSave(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data;
		try {
			data = outboundService.saveDL01AllChkeck(map);
		} catch (Exception e) {
			data = 0;
		}
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL01IndexSelectCheckSave.*")
	public String DL01IndexSelectCheckSave(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data;
		try {
			data = outboundService.DL01IndexSelectCheckSave(map);
		} catch (Exception e) {
			data = 0;
		}
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	/**
	 * 고객오더/창간오더 정보 할당처리
	 * @param request
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/wms/outbound/json/DL01Assignment.*")
	public String DL01Assignment(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.assignmentDl01(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL01AllCheckAssignment.*")
	public String DL01AllCheckAssignment(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data;
		try {
			data = outboundService.assignmentAllDl01(map);
		} catch (Exception e) {
			data = 0;
		}
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL01IndexSelectCheckAssignment.*")
	public String DL01IndexSelectCheckAssignment(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data;
		try {
			data = outboundService.DL01IndexSelectCheckAssignment(map);
		} catch (Exception e) {
			data = 0;
		}
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL01Barcode.*")
	public String DL01Barcode(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		List list = outboundService.DL01Barcode(map);
		
		model.put("data", list);
		
		return JSON_VIEW;
	}
	
	
	  @RequestMapping("/wms/outbound/DL04pop.*") 
	  public String DL04pop(HttpServletRequest request, Map model) throws SQLException{ DataMap
	  map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
	  
	  model.put("param", map);
	  
	  return "/wms/outbound/DL04POP"; }
	 
	
	@RequestMapping("/wms/outbound/json/DL04POPSave.*")
	public String DL04POPSave(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data;
//		try {
			data = outboundService.DL04POPSave(map);
//		} catch (Exception e) {
//			throw e;
//		}
		
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL04StockValidation.*")
	public String PT01StockValidation(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		String result = outboundService.DL04StockValidation(map);
		model.put("data", result);
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/DL04PopSearch.*")
	public String DL04PopSearch(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		model.put("param", map);
		
		map.setModuleCommand("WmsOutbound", "SEARCHITEM");
		
		List itemList = commonService.getList(map);
		
		model.put("itemList", itemList);
		
		return "/wms/outbound/DL04POP";
	}
	
	@RequestMapping("/wms/outbound/json/DL04Assignment.*")
	public String DL04Assignment(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.DL04Assignment(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	/**
	 * 오더 정보 삭제
	 * @param request
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/wms/outbound/json/DL01OrderDelete.*")
	public String DL01OrderDelete(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.DL01OrderDelete(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL04Delete.*")
	public String DL04Delete(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.DL04Delete(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL04orderDelete.*")
	public String DL04AllDelete(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.DL04orderDelete(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}


	@RequestMapping("/wms/outbound/json/DeleteDL04.*")
	public String DeleteDL04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.DeleteDL04(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL06StockValidation.*")
	public String DL06StockValidation(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		String result = outboundService.DL06StockValidation(map);
		model.put("data", result);
		return JSON_VIEW;
	}

	@RequestMapping("/wms/outbound/json/DL06POPQTUALOCNT.*")
	public String DL06POPQTUALOCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		DataMap data = outboundService.DL06POPQTUALOCNT(map);
		model.put("data", data);
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL06POPQTSWRKCNT.*")
	public String DL06POPQTSWRKCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		DataMap data = outboundService.DL06POPQTSWRKCNT(map);
		model.put("data", data);
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL06POPSave.*")
	public String DL06POPSave(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = outboundService.DL06POPSave(map);
		
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/validatePikingSave.*")
	public String validatePikingSave(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		String result = outboundService.validatePikingSave(map);
		
		model.put("data", result);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL06PikingSave.*")
	public String DL06PikingSave(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.DL06PikingSave(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	/**
	 * 오더 조회 삭제
	 * @param request
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/wms/outbound/json/TM04Delete.*")
	public String TM04Delete(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.TM04Delete(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/SaveTM05.*")
	public String saveTM05(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = outboundService.SaveTM05(map);
		model.put("data", data);
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/SaveTM03.*")
	public String saveTM03(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = outboundService.SaveTM03(map);
		model.put("data", data);
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/TM07_Delete.*")
	public String DeleteTM07(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.DeleteTM07(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	
	@RequestMapping("/wms/outbound/json/SaveTM06.*")
	public String SaveTM06(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.SaveTM06(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	
	}
	
	@RequestMapping("/wms/outbound/json/saveShpd.*")
	public String saveShpd(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.saveShpd(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/deleteOrder.*")
	public String deleteOrder(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.deleteOrder(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/dl04Procall.*")
	public String dl04Procall(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.dl04Procall(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}

	
	@RequestMapping("/wms/outbound/json/validateSendEcms.*")
	public String validateSendEcms(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object result = outboundService.validateSendEcms(map);
		
		model.put("data", result);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/sendEcms.*")
	public String sendEcms(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

		DataMap rsMap = outboundService.sendEcmsEX(map); //ERP전송
		//DataMap rsMap = outboundService.sendEcmsEX_X(map); //ERP전송X
		
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/savePrtseq.*")
	public String savePrtseq(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.savePrtseq(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/savePrtseq2.*")
	public String savePrtseq2(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.savePrtseq2(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/saveDL04.*")
	public String saveDL04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.saveDL04(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/saveDL05.*")
	public String saveDL05(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.saveDL05(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/saveDL04Sub.*")
	public String saveDL04Sub(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.saveDL04Sub(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/AssignDL04.*")
	public String AssignDL04(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		//Object data = outboundService.AssignDL04(map);
		/*Original */
		//DataMap rsMap = outboundService.AssignDL04(map);
		DataMap rsMap = outboundService.AssignDL04_2(map);
				
		List<DataMap> ifidList = rsMap.getList("ifidList");
		if(null!=ifidList && ifidList.size()>0) {
			// 인터페이스 전송
			for(DataMap item : ifidList) {
				item.put("ownrky", map.getString("OWNRKY"));
				item.put("IFID", item.getString("IFID"));
				item.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				inboundService.sendIf(item);
			}

		}
		
		model.put("data",rsMap);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/CompleteDL04.*")
	public String CompleteDL04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.CompleteDL04(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/UnallocateDL04.*")
	public String UnallocateDL04(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.UnallocateDL04(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL09CancelShipment.*")
	public String DL09CancelShipment(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.DL09CancelShipment(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/CompleteDL06.*")
	public String CompleteDL06(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.CompleteDL06(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/CompleteDL16.*")
	public String CompleteDL16(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.CompleteDL16(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	
	@RequestMapping("/wms/outbound/json/ValidPickingCancelDL06.*")
	public String ValidPickingCancelDL06(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.ValidPickingCancelDL06(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	
	@RequestMapping("/wms/outbound/json/PickingCancelDL06.*")
	public String PickingCancelDL06(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.PickingCancelDL06(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/PickingCancelDL06Sub.*")
	public String PickingCancelDL06Sub(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.PickingCancelDL06Sub(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/savePrtseqDL09.*")
	public String savePrtseqDL09(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.savePrtseqDL09(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/savePrtseqPT01.*")
	public String savePrtseqPT01(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.savePrtseqPT01(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/savePrtseqDL04.*")
	public String savePrtseqDL04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.savePrtseqDL04(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/closingDL04.*")
	public String closingDL04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.closingDL04(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	/**
	 * RANGE SQL 여러개 생성
	 */
	@RequestMapping("/wms/outbound/json/DL04HEAD.*")
	public String DL04HEAD(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("WmsOutbound", "DL04");
		
		DataMap rangeMap = map.getMap(CommonConfig.RNAGE_DATA_MAP);
		
		SqlUtil sqlUtil = new SqlUtil();
		
		String rSql1 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_1", rSql1);
		
		sqlUtil.changeRangeKey(rangeMap, "SH.RQSHPD", "IFW.WADAT"); //출하요청일자
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPMTY", "IFW.BWART"); //출고유형
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPOKY", "IFW.VBELN"); // 출고문서번호
		sqlUtil.changeRangeKey(rangeMap, "SI.SVBELN", "IFW.VBELN"); // 출고오더번호
		sqlUtil.changeRangeKey(rangeMap, "SH.DPTNKY", "IFW.KUNAG"); // 매출처
		
		String rSql2 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_2", rSql2); 
		
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL05HEAD.*")
	public String DL05HEAD(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("WmsOutbound", "DL05");
		
		DataMap rangeMap = map.getMap(CommonConfig.RNAGE_DATA_MAP);
		
		SqlUtil sqlUtil = new SqlUtil();
		
		String rSql1 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_1", rSql1);
		
		sqlUtil.changeRangeKey(rangeMap, "SH.RQSHPD", "IFW.WADAT"); //출하요청일자
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPMTY", "IFW.BWART"); //출고유형
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPOKY", "IFW.VBELN"); // 출고문서번호
		sqlUtil.changeRangeKey(rangeMap, "SI.SVBELN", "IFW.VBELN"); // 출고오더번호
		sqlUtil.changeRangeKey(rangeMap, "SH.DPTNKY", "IFW.KUNAG"); // 매출처
		
		String rSql2 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_2", rSql2); 
		
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL06Head.*")
	public String dl06Head(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

		Object data = outboundService.dl06Head(map);

		model.put("data", data);

		return JSON_VIEW;
	}
	
	/**
	 * 피킹완료 출고 완료시 체크
	 * @param request
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/wms/outbound/json/fullPkChk.*")
	public String fullPkChk(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

		Object data = outboundService.fullPkChk(map);

		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/dl06HeadNew.*")
	public String dl06HeadNew(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

		Object data = outboundService.dl06HeadNew(map);

		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL06ITEMGROUPDELETE.*")
	public int DL06ITEMGROUPDELETE(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

		int count = 0;
		map.setModuleCommand("WmsTask", "TASDIGROUP");
		commonService.delete(map);
		count++;
		
		return count;
	}
	
	@RequestMapping("/wms/outbound/json/MDL16PikingSave.*")
	public String MDL16PikingSave(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.MDL16PikingSave(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	/**
	 * WDL00 : 그룹오더생성(상품) 생성
	 */
	@RequestMapping("/wms/outbound/json/waveCreate.*")
	public String waveCreate(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = outboundService.waveCreate(map);
		model.put("data",data);
		
		return JSON_VIEW;
	}		
	
	/**
	 * WDL00 : 그룹오더삭제
	 */	
	@RequestMapping("/wms/outbound/json/waveDelete.*")
	public String waveDelete(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = outboundService.waveDelete(map);		
		model.put("data", data);
		
		return JSON_VIEW;
	}	
	
	/**
	 * RANGE SQL 여러개 생성
	 */
	@RequestMapping("/wms/outbound/json/WDL04HEAD.*")
	public String WDL04HEAD(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("WmsOutbound", "WDL04");
		
		DataMap rangeMap = map.getMap(CommonConfig.RNAGE_DATA_MAP);
		
		SqlUtil sqlUtil = new SqlUtil();
		
		String rSql1 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_1", rSql1);
		
		sqlUtil.changeRangeKey(rangeMap, "SH.RQSHPD", "IFW.WADAT"); //출하요청일자
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPMTY", "IFW.BWART"); //출고유형
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPOKY", "IFW.VBELN"); // 출고문서번호
		sqlUtil.changeRangeKey(rangeMap, "SI.SVBELN", "IFW.VBELN"); // 출고오더번호
		sqlUtil.changeRangeKey(rangeMap, "SH.DPTNKY", "IFW.KUNAG"); // 매출처
		
		String rSql2 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_2", rSql2); 
		
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}	
	
	/**
	 * WDL00 : 그룹오더할당(상품)
	 */
	@RequestMapping("/wms/outbound/json/totalAssignWDL04.*")
	public String TOTSKUAssignWDL04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.totalAssignWDL04(map);
		
		model.put("data",data);
		
		return JSON_VIEW;
	}					
	
	@RequestMapping("/wms/outbound/json/SaveWDL05.*")
	public String SaveWDL05(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = outboundService.SaveWDL05(map);
		model.put("data", data);
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/CheckCancel.*")
	public String CheckCancel(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.CheckCancel(map);
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/validateInsepectSave.*")
	public String validateInsepectSave(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		String result = outboundService.validateInsepectSave(map);
		model.put("data", result);
		return JSON_VIEW;
	}	
	
	@RequestMapping("/wms/outbound/json/saveInspectWDL05.*")
	public String saveInspectWDL05(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.saveInspectWDL05(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}	
	
	@RequestMapping("/wms/outbound/json/saveInspectItemWDL05.*")
	public String saveInspectItemWDL05(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = outboundService.saveInspectItemWDL05(map);
		model.put("data", data);
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/saveDL55.*")
	public String saveDL55(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.saveDL55(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL05CheckCancel.*")
	public String DL05CheckCancel(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.DL05CheckCancel(map);
		model.put("data", data);
		
		return JSON_VIEW;
	}
										
	@RequestMapping("/wms/outbound/json/PDL05CheckCancel.*")
	public String PDL05CheckCancel(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.PDL05CheckCancel(map);
		model.put("data", data);
		
		return JSON_VIEW;
	}

	@RequestMapping("/wms/outbound/json/DL12.*")
	public String DL12(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("WmsOutbound", "DL12");
		
		DataMap rangeMap = map.getMap(CommonConfig.RNAGE_DATA_MAP);
		
		SqlUtil sqlUtil = new SqlUtil();
		
		String rSql1 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_1", rSql1);
		
		sqlUtil.changeRangeKey(rangeMap, "SH.RQSHPD", "IFW.WADAT"); //출하요청일자
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPMTY", "IFW.BWART"); //출고유형
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPOKY", "IFW.VBELN"); // 출고문서번호
		sqlUtil.changeRangeKey(rangeMap, "SI.SVBELN", "IFW.VBELN"); // 출고오더번호
		sqlUtil.changeRangeKey(rangeMap, "SH.DPTNKY", "IFW.KUNAG"); // 매출처
		
		String rSql2 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_2", rSql2); 
		
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}

	@RequestMapping("/wms/outbound/json/DL12DPT.*")
	public String DL12DPT(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("WmsOutbound", "DL12DPT");
		
		DataMap rangeMap = map.getMap(CommonConfig.RNAGE_DATA_MAP);
		
		SqlUtil sqlUtil = new SqlUtil();
		
		String rSql1 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_1", rSql1);
		
		sqlUtil.changeRangeKey(rangeMap, "SH.RQSHPD", "IFW.WADAT"); //출하요청일자
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPMTY", "IFW.BWART"); //출고유형
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPOKY", "IFW.VBELN"); // 출고문서번호
		sqlUtil.changeRangeKey(rangeMap, "SI.SVBELN", "IFW.VBELN"); // 출고오더번호
		sqlUtil.changeRangeKey(rangeMap, "SH.DPTNKY", "IFW.KUNAG"); // 매출처
		
		String rSql2 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_2", rSql2); 
		
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/totalPickingCreate.*")
	public String totalPickingCreate(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
				

			Object data = outboundService.totalPickingCreate(map);
			
			model.put("data", data);
		
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/totalPickingDelete.*")
	public String totalPickingDelete(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
				

			Object data = outboundService.totalPickingDelete(map);
			
			model.put("data", data);
		
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/saveDL12Sub.*")
	public String saveDL12DPT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.saveDL12Sub(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL04POP.*")
	public String DL04POPLIST(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("WmsOutbound", "DL04POP");
				
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL06POPH.*")
	public String DL06POPHEADLIST(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("WmsTask", "DL06POPH");
				
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}
			
	@RequestMapping("/wms/outbound/json/DL06POPD.*")
	public String DL06POPITEMLIST(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("WmsTask", "DL06POPD");
				
		String listData = commonService.getJdbcData(map);

		model.put("data", listData);			
		
		return TEXT_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL06MAXLOTA11.*")
	public String DL06MAXLOTA11(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
				
		Object data = outboundService.DL06MAXLOTA11(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL06POPSAVE2.*")
	public String DL06POPSAVE2(HttpServletRequest request, Map model) throws SQLException, InterruptedException{	
		
		 DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		 Object data = outboundService.DL06POPSave2(map);
		  
		 model.put("data", data);
		 

		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/saveDL08.*")
	public String saveDL08(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = outboundService.saveDL08(map);
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/cancelDL08.*")
	public String cancelDL08(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		//DataMap rsMap = outboundService.cancelDL08EX(map);	// ERP전송
		
		
		DataMap rsMap = outboundService.cancelDL08EX_T(map); 	// ERP전송 실서버 반영
		//DataMap rsMap = outboundService.cancelDL08EX_X(map); 	// ERP전송X
		
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	}
	
	/** 거래명세서(Trading statement) 출력 */
	@RequestMapping("/wms/outbound/json/DL08PrintTS.*")
	public String DL08PrintTS(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap) request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		DataMap rsMap = outboundService.DL08PrintTS(map);
		
//		List<DataMap> ifidList = rsMap.getList("ifidList");
//		if(null!=ifidList && ifidList.size()>0) {
//			// 인터페이스 전송
//			for(DataMap item : ifidList) {
//				item.put("ownrky", map.getString("OWNRKY"));
//				item.put("IFID", item.getString("IFID"));
//				item.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
//				inboundService.sendIf(item);
//			}
//			
//		}
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL08POPSave.*")
	public String DL08POPSave(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		DataMap rsMap = outboundService.DL08POPSave(map);
		
		model.put("data", rsMap);

		return JSON_VIEW;
	}
	
	/**
	 * RANGE SQL 여러개 생성
	 */
	@RequestMapping("/wms/outbound/json/DL21HEAD.*")
	public String DL21HEAD(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("WmsOutbound", "DL21");
		
		DataMap rangeMap = map.getMap(CommonConfig.RNAGE_DATA_MAP);
		
		SqlUtil sqlUtil = new SqlUtil();
		
		String rSql1 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_1", rSql1);
		
		sqlUtil.changeRangeKey(rangeMap, "SH.RQSHPD", "IFW.WADAT"); //출하요청일자
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPMTY", "IFW.BWART"); //출고유형
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPOKY", "IFW.VBELN"); // 출고문서번호
		sqlUtil.changeRangeKey(rangeMap, "SI.SVBELN", "IFW.VBELN"); // 출고오더번호
		sqlUtil.changeRangeKey(rangeMap, "SH.DPTNKY", "IFW.KUNAG"); // 매출처
		
		String rSql2 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_2", rSql2); 
		
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL21POP.*")
	public String DL21POPLIST(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);		
		
		List<DataMap> dataList = map.getList("data");

		DataMap rowMap = new DataMap();
		StringBuilder sb = new StringBuilder();
		
		int stlcnt=0;
		
		for (int i=0; i < dataList.size(); i++) {
			rowMap = dataList.get(i).getMap("map");
			
			if(rowMap.getString("STLCHK").equals("V")) {
				
				
				if (stlcnt != 0) {
					sb.append(",");
				}
				
				sb.append("'").append(rowMap.getString("SHPOKY")).append("'");
				
				stlcnt++;
			}
			
		}
		
		map.put("SHPOKY_LIST", sb.toString());		
		map.put("WAREKY", map.getString(CommonConfig.SES_USER_WHAREHOUSE_KEY));
		
		map.setModuleCommand("WmsOutbound", "DL21POP");
		
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/TaskLockReset.*")
	public String TaskLockReset(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		DataMap rsMap = outboundService.TaskLockReset(map);
		
		model.put("data", rsMap);

		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/PrtUpdateDL04.*")
	public String PrtUpdateDL04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		DataMap rsMap = outboundService.PrtUpdateDL04(map);
		
		model.put("data", rsMap);

		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/test.*")
	public String test(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = outboundService.test(map);				
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/sendDL04StknumInfo.*")
	public String sendDL04StknumInfo(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = outboundService.sendDL04StknumInfo(map);				
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	}
	
	/**
	 * RANGE SQL 여러개 생성
	 */
	@RequestMapping("/wms/outbound/json/DL211HEAD.*")
	public String DL211HEAD(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("WmsOutbound", "DL211");
		
		DataMap rangeMap = map.getMap(CommonConfig.RNAGE_DATA_MAP);
		
		SqlUtil sqlUtil = new SqlUtil();
		
		String rSql1 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_1", rSql1);
		
		sqlUtil.changeRangeKey(rangeMap, "SH.RQSHPD", "IFW.WADAT"); //출하요청일자
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPMTY", "IFW.BWART"); //출고유형
		sqlUtil.changeRangeKey(rangeMap, "SH.SHPOKY", "IFW.VBELN"); // 출고문서번호
		sqlUtil.changeRangeKey(rangeMap, "SI.SVBELN", "IFW.VBELN"); // 출고오더번호
		sqlUtil.changeRangeKey(rangeMap, "SH.DPTNKY", "IFW.KUNAG"); // 매출처
		
		String rSql2 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_2", rSql2); 
		
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/validateDL211.*")
	public String validateDL211(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.validateDL211(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/saveDL211.*")
	public String saveDL211(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.saveDL211(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/ValidPickingCancelDL061.*")
	public String ValidPickingCancelDL061(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.ValidPickingCancelDL061(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/PickingCancelDL061.*")
	public String PickingCancelDL061(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.PickingCancelDL061(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/PickingCancelDL061Sub.*")
	public String PickingCancelDL061Sub(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.PickingCancelDL061Sub(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL211CheckCancel.*")
	public String DL211CheckCancel(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = outboundService.DL211CheckCancel(map);
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	
	@RequestMapping("/wms/outbound/json/saveSubDL23.data")
	public String saveSubDL042(HttpServletRequest request, Map model) throws Exception {
	    DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
	    Object data = outboundService.saveSubDL23(map);
	    model.put("data", data);
	    return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/saveDL24.data")
	public String saveDL24(HttpServletRequest request, Map model) throws Exception {
	    DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
	    Object data = outboundService.saveDL24(map);
	    model.put("data", data);
	    return JSON_VIEW;
	}
	 
}