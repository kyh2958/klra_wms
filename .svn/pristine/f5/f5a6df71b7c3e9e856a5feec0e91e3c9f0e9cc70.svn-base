package project.mobile.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import project.common.bean.CommonConfig;
import project.common.bean.DataMap;
import project.common.controller.BaseController;
import project.common.service.CommonService;
import project.mobile.service.MobilePhysicalCountService;
import project.mobile.service.MobileService;

@Controller
public class MobilePhysicalCountController extends BaseController {
	
	static final Logger log = LogManager.getLogger(MobilePhysicalCountController.class.getName());

	@Autowired
	private CommonService commonService;

	@Autowired
	private MobileService mobileService;
	
	@Autowired
	private MobilePhysicalCountService mobilePhysicalCountService;
	
	@RequestMapping("/mobile/mobilePhysicalCount/json/SavePDMV47.*")
	public String savePDMV47(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobilePhysicalCountService.savePDMV47(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
}