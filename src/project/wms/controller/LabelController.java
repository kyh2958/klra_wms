package project.wms.controller;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import project.common.bean.CommonConfig;
import project.common.bean.DataMap;
import project.common.controller.BaseController;
import project.wms.service.LabelService;

@Controller
public class LabelController extends BaseController {
	
	private static Logger log = Logger.getLogger(LabelController.class);
	
	@Autowired
	private LabelService labelService;	
	
	@RequestMapping("/wms/labal/json/saveLB01.*")
	public String saveSK01(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = labelService.saveLB01(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
		
	}
	
	@RequestMapping("/wms/labal/json/saveLB01PalletIDSingle.*")
	public String saveLB01PalletIDSingle(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = labelService.saveLB01PalletIDSingle(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/labal/json/PT01PalletIDPrint.*")
	public String PT01PalletIDPrint(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = labelService.PT01PalletIDPrint(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/labal/LB01.*")
	public String LB01pop(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		model.put("param", map);
		
		return "/wms/label/LB01";
	}
}