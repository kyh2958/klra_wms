package project.wms.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import project.common.airfw.PropertyUtil;
import project.common.bean.CommonConfig;
import project.common.bean.DataMap;
import project.common.controller.BaseController;
import project.wms.service.KppIFService;

@Controller
public class KppIFController extends BaseController {
	private static Logger log = Logger.getLogger(KppIFController.class);
	
	@Autowired
	private KppIFService service;	
	
	private String SERVER_TYPE =  PropertyUtil.getProperty("commonconfig.properties", "server.type");

	
	@RequestMapping("/KppIF/list/json/{command}.*")
	public String list(HttpServletRequest request, @PathVariable String command, Map<String, Object> model) throws SQLException{		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("KppIF", command);

		// 운영 환경에서만 I/F 수행되도록 처리
		if ("prod".equals(SERVER_TYPE)) {
		}
		
		List<DataMap> list = service.getList(map);
		
		model.put("data", list);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/KppIF/map/json/{command}.*")
	public String map(HttpServletRequest request, @PathVariable String command, Map<String, Object> model) throws SQLException{		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("KppIF", command);

		DataMap data = service.getMap(map);

		model.put("data", data);
		
		return JSON_VIEW;
	}
}