package project.mobile.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import project.common.bean.CommonConfig;
import project.common.bean.CommonLabel;
import project.common.bean.DataMap;
import project.common.controller.BaseController;
import project.common.service.CommonService;
import project.mobile.service.MobilePutawayService;
import project.wms.service.InboundService;

@Controller
public class MobilePutawayController extends BaseController {
	
	static final Logger log = LogManager.getLogger(MobilePutawayController.class.getName());

	@Autowired
	private CommonService commonService;

	@Autowired
	private MobilePutawayService mobilePutawayService;
	
	@Autowired
	private CommonLabel commonLabel;	
	
	@Autowired
	private InboundService inboundService;
	
	@RequestMapping("/mobile/MobilePutaway/json/makeTaskNomal_PDMV04.*")
	public String makeTaskNomal_PDMV04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobilePutawayService.makeTaskNomal_PDMV04(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/MobilePutaway/json/PDPT01SKUCNT.*")
	public String PDPT01SKUCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobilePutaway", "PDPT01SKUCNT");
		
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/MobilePutaway/json/PDPT01CNT.*")
	public String PDPT01CNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobilePutaway", "PDPT01CNT");
		Object data = commonService.getMap(map);
				 
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/MobilePutaway/json/PDPT01SUBCNT.*")
	public String PDPT01SUBCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobilePutaway", "PDPT01SUBCNT");
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}

	@RequestMapping("/mobile/MobilePutaway/json/PDPT01.*")
	public String PDPT01(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobilePutaway", "PDPT01");
		
		Object data = commonService.getList(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}

	@RequestMapping("/mobile/MobilePutaway/json/savePDPT01POP.*")
	public String savePDPT01POP(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
			
		//Object data = mobilePutawayService.savePDPT01POP(map);
		DataMap data = mobilePutawayService.savePDPT01POP(map);
		/*
		List<DataMap> ifidList = data.getList("ifidList");
		
		if(null!=ifidList && ifidList.size()>0) {
			// 인터페이스 전송
			for(DataMap item : ifidList) {
				item.put("ownrky", map.getString("OWNRKY"));
				item.put("IFID", item.getString("IFID"));
				item.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				inboundService.sendIf(item);
			}
		}
		*/
		model.put("data", data);

		return JSON_VIEW;
	} 
	
}