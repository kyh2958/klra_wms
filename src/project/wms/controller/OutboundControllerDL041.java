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
import project.wms.service.OutboundServiceDL041;

/**
* @packageName  project.wms.controller
* @fileName     OutboundControllerDL041.java
* @description  할당관리 선적기준 PS
* @history
* DATE_______AUTHOR__NOTE__________________________________<br>
* 2023.08.23 JHJ 소스분리(ORG:DL04) 기준변경:출하오더번호->선적기준
*/
@Controller
public class OutboundControllerDL041 extends BaseController {
	
	private static Logger log = Logger.getLogger(OutboundControllerDL041.class);
	
	@Autowired
	private OutboundServiceDL041 DL041outboundService;
		
	@Autowired
	private InboundService inboundService;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("/wms/outbound/popDL041.*") 
	public String popDL041(HttpServletRequest request, Map model) throws SQLException{ DataMap
	map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
	
	model.put("param", map);
	
	return "/wms/outbound/DL041POP"; }
	
	@RequestMapping("/wms/outbound/json/DL041POPSave.*")
	public String popSaveDL041(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data;
//		try {
			data = DL041outboundService.popSaveDL041(map);
//		} catch (Exception e) {
//			throw e;
//		}
		
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/StockValidationDL041.*")
	public String stockValidationDL041(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		String result = DL041outboundService.stockValidationDL041(map);
		model.put("data", result);
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/popSearchDL041.*")
	public String popSearchDL041(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		model.put("param", map);
		
		map.setModuleCommand("DL041", "DL041_SEARCHITEM");
		
		List itemList = commonService.getList(map);
		
		model.put("itemList", itemList);
		
		return "/wms/outbound/DL041POP";
	}
	
	@RequestMapping("/wms/outbound/json/assignmentDL041.*")
	public String assignmentDL041(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL041outboundService.assignmentDL041(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/deleteDL041.*")
	public String deleteDL041(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL041outboundService.deleteDL041(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/orderDeleteDL041.*")
	public String allDeleteDL041(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL041outboundService.orderDeleteDL041(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}

	@RequestMapping("/wms/outbound/json/saveDL041.*")
	public String saveDL041(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL041outboundService.saveDL041(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/saveSubDL041.*")
	public String saveSubDL041(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL041outboundService.saveSubDL041(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	/**
	* @methodName   assignDL041
	* @description  할당 처리 후 I/F
	*/
	@RequestMapping("/wms/outbound/json/assignDL041.*")
	public String assignDL041(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
//		java.util.Properties props = new Properties(System.getProperties());
		//Object data = outboundService.AssignDL04(map);
		/*Original */
		//DataMap rsMap = outboundService.AssignDL04(map);
		DataMap rsMap = DL041outboundService.assignDL041_2(map);	//실제 할당 처리 로직
				
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
	
	@RequestMapping("/wms/outbound/json/completeDL041.*")
	public String CompleteDL04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL041outboundService.completeDL041(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/unallocateDL041.*")
	public String unallocateDL041(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL041outboundService.unallocateDL041(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}

	@RequestMapping("/wms/outbound/json/closingDL041.*")
	public String closingDL041(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = DL041outboundService.closingDL041(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	/**
	 * RANGE SQL 여러개 생성
	 */
	@RequestMapping("/wms/outbound/json/headRangeSqlDL041.*")
	public String HeadRangeSqlDL041(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("DL041", "DL041_HEAD");	// module, command
		
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
	@RequestMapping("/wms/outbound/json/detailDL041.*")
	public String detailDL041(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("DL041", "DL041_SUB");	// module, command
		
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

	@RequestMapping("/wms/outbound/json/totalPickingCreateDL041.*")
	public String totalPickingCreateDL041(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

			Object data = DL041outboundService.totalPickingCreateDL041(map);
			
			model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/totalPickingDeleteDL041.*")
	public String totalPickingDeleteDL041(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
				

			Object data = DL041outboundService.totalPickingDeleteDL041(map);
			
			model.put("data", data);
		
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/outbound/json/DL041POP.*")
	public String popListDL041(HttpServletRequest request, Map model) throws SQLException, InterruptedException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("DL041", "DL041POP");
				
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
	
	@RequestMapping("/wms/outbound/json/prtUpdateDL041.*")
	public String prtUpdateDL04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		DataMap rsMap = DL041outboundService.prtUpdateDL041(map);
		
		model.put("data", rsMap);

		return JSON_VIEW;
	}
	
	/**
	* @methodName   sendStknumInfoDL041
	* @description  선적제한해제 서비스 호출
	*/
	@RequestMapping("/wms/outbound/json/sendStknumInfoDL041.*")
	public String sendStknumInfoDL041(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = DL041outboundService.sendStknumInfoDL041(map);
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	}
	
	/**
	* @methodName   callSapFuncDL041
	* @description  SAP변경수신 호출
	*/
	@RequestMapping("/wms/outbound/json/callSapFuncDL041.*")
	public String callSapFuncDL041(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = DL041outboundService.callSapFuncDL041(map);
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	}
}