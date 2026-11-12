package project.wms.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import project.common.bean.CommonConfig;
import project.common.bean.DataMap;
import project.common.controller.BaseController;
import project.common.service.CommonService;
import project.common.util.SqlUtil;
import project.wms.service.InboundService;
import project.wms.service.OutboundServiceDL042;

/**
* @packageName  project.wms.controller
* @fileName     OutboundControllerDL042.java
* @description  할당관리 선적기준 HL
* @history
* DATE_______AUTHOR__NOTE__________________________________<br>
* 2023.12.03 JHJ 소스분리(ORG:DL04) 기준변경:출하오더번호->선적기준
*/
@Controller
public class OutboundControllerDL042 extends BaseController {
	
	private static Logger log = Logger.getLogger(OutboundControllerDL042.class);
	
	@Autowired
	private OutboundServiceDL042 DL042outboundService;
		
	@Autowired
	private InboundService inboundService;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("/wms/outbound/popDL042.*") 
	public String popDL042(HttpServletRequest request, Map model) throws SQLException{ DataMap
	map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
	
	model.put("param", map);
	
	return "/wms/outbound/DL042POP"; }
	
	@RequestMapping("/wms/outbound/json/DL042POPSave.*")
	public String popSaveDL042(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data;
//		try {
			data = DL042outboundService.popSaveDL042(map);
//		} catch (Exception e) {
//			throw e;
//		}
		
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/StockValidationDL042.*")
	public String stockValidationDL042(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		String result = DL042outboundService.stockValidationDL042(map);
		model.put("data", result);
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/popSearchDL042.*")
	public String popSearchDL042(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		model.put("param", map);
		
		map.setModuleCommand("DL042", "DL042_SEARCHITEM");
		
		List itemList = commonService.getList(map);
		
		model.put("itemList", itemList);
		
		return "/wms/outbound/DL042POP";
	}
	
	@RequestMapping("/wms/outbound/json/assignmentDL042.*")
	public String assignmentDL042(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL042outboundService.assignmentDL042(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/deleteDL042.*")
	public String deleteDL042(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL042outboundService.deleteDL042(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/orderDeleteDL042.*")
	public String allDeleteDL042(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL042outboundService.orderDeleteDL042(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}

	@RequestMapping("/wms/outbound/json/saveDL042.*")
	public String saveDL042(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL042outboundService.saveDL042(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/saveSubDL042.*")
	public String saveSubDL042(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL042outboundService.saveSubDL042(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	/**
	* @methodName   assignDL042
	* @description  할당 처리 후 I/F
	*/
	@RequestMapping("/wms/outbound/json/assignDL042.*")
	public String assignDL042(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
//		java.util.Properties props = new Properties(System.getProperties());
		//Object data = outboundService.AssignDL04(map);
		/*Original */
		//DataMap rsMap = outboundService.AssignDL04(map);
		DataMap rsMap = DL042outboundService.assignDL042_2(map);	//실제 할당 처리 로직
				
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
	
	@RequestMapping("/wms/outbound/json/completeDL042.*")
	public String CompleteDL04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL042outboundService.completeDL042(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/unallocateDL042.*")
	public String unallocateDL042(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL042outboundService.unallocateDL042(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}

	@RequestMapping("/wms/outbound/json/closingDL042.*")
	public String closingDL042(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL042outboundService.closingDL042(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	/**
	 * RANGE SQL 여러개 생성
	 */
	@RequestMapping("/wms/outbound/json/headRangeSqlDL042.*")
	public String HeadRangeSqlDL042(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("DL042", "DL042_HEAD");	// module, command
		
		DataMap rangeMap = map.getMap(CommonConfig.RNAGE_DATA_MAP);
		
		SqlUtil sqlUtil = new SqlUtil();
		
		String rSql1 = sqlUtil.getRangeSql(rangeMap);
		String progid = map.getString("PROGID");

		map.put("RANGE_SQL_1", rSql1);
//		map.put("PROGID", progid);
		
//		sqlUtil.changeRangeKey(rangeMap, "SH.RQSHPD", "IFW.WADAT"); // 출하요청일자
//		sqlUtil.changeRangeKey(rangeMap, "SH.SHPMTY", "IFW.BWART"); // 출고유형
//		sqlUtil.changeRangeKey(rangeMap, "SH.SHPOKY", "IFW.VBELN"); // 출고문서번호
//		sqlUtil.changeRangeKey(rangeMap, "SI.SVBELN", "IFW.VBELN"); // 출고오더번호
//		sqlUtil.changeRangeKey(rangeMap, "SH.DPTNKY", "IFW.KUNAG"); // 매출처
		
//		String rSql2 = sqlUtil.getRangeSql(rangeMap);
//		map.put("RANGE_SQL_2", rSql2); 
		
		
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}
	
	/**
	 * RANGE SQL 여러개 생성
	 */
	@RequestMapping("/wms/outbound/json/detailDL042.*")
	public String detailDL042(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("DL042", "DL042_SUB");	// module, command
		
		DataMap rangeMap = map.getMap(CommonConfig.RNAGE_DATA_MAP);
		
		SqlUtil sqlUtil = new SqlUtil();
		
		String rSql1 = sqlUtil.getRangeSql(rangeMap);
		map.put("RANGE_SQL_1", rSql1);
		
//		sqlUtil.changeRangeKey(rangeMap, "SH.RQSHPD", "IFW.WADAT"); // 출하요청일자
//		sqlUtil.changeRangeKey(rangeMap, "SH.SHPMTY", "IFW.BWART"); // 출고유형
//		sqlUtil.changeRangeKey(rangeMap, "SH.SHPOKY", "IFW.VBELN"); // 출고문서번호
//		sqlUtil.changeRangeKey(rangeMap, "SI.SVBELN", "IFW.VBELN"); // 출고오더번호
//		sqlUtil.changeRangeKey(rangeMap, "SH.DPTNKY", "IFW.KUNAG"); // 매출처
		
//		String rSql2 = sqlUtil.getRangeSql(rangeMap);
//		map.put("RANGE_SQL_2", rSql2); 
		
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}

	@RequestMapping("/wms/outbound/json/totalPickingCreateDL042.*")
	public String totalPickingCreateDL042(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

			Object data = DL042outboundService.totalPickingCreateDL042(map);
			
			model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/totalPickingDeleteDL042.*")
	public String totalPickingDeleteDL042(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
				

			Object data = DL042outboundService.totalPickingDeleteDL042(map);
			
			model.put("data", data);
		
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL042POP.*")
	public String popListDL042(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("DL042", "DL042POP");
				
		String listData = commonService.getJdbcData(map);
		
		model.put("data", listData);
		
		return TEXT_VIEW;
	}
	
//	@RequestMapping("/wms/outbound/json/TaskLockReset.*")
//	public String TaskLockReset(HttpServletRequest request, Map model) throws SQLException{
//		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
//		DataMap rsMap = outboundService.TaskLockReset(map);
//		
//		model.put("data", rsMap);
//
//		return JSON_VIEW;
//	}
	
	@RequestMapping("/wms/outbound/json/prtUpdateDL042.*")
	public String prtUpdateDL04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		DataMap rsMap = DL042outboundService.prtUpdateDL042(map);
		
		model.put("data", rsMap);

		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/test042.*")
	public String test(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = DL042outboundService.test(map);				
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	}

	/**
	* @methodName   sendStknumInfoDL042
	* @description  선적제한해제 서비스 호출
	*/
	@RequestMapping("/wms/outbound/json/sendStknumInfoDL042.*")
	public String sendStknumInfoDL042(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = DL042outboundService.sendStknumInfoDL042(map);
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	}
	
	/**
	* @methodName   callSapFuncDL042
	* @description  SAP변경수신 호출
	*/
	@RequestMapping("/wms/outbound/json/callSapFuncDL042.*")
	public String callSapFuncDL042(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = DL042outboundService.callSapFuncDL042(map);
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	}
	

	@RequestMapping("/wms/outbound/json/insertExportKiosk.*")
	public String insertExportKiosk(HttpServletRequest request, Map model) throws SQLException{

	    DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

	    DataMap rsMap = DL042outboundService.insertExportKiosk(map);

	    model.put("data", rsMap);

	    return JSON_VIEW;

	}
}