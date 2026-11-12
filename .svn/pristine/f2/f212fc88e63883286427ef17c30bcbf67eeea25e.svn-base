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
import project.wms.service.WmsSystemService;

@Controller
public class WmsSystemController extends BaseController {

	private static Logger log = Logger.getLogger(WmsSystemController.class);

	@Autowired
	private WmsSystemService wmsSystemService;

	/** PDA기초실사 > 저장 */
	@RequestMapping("/wms/wmsSystem/json/savePDIS01.*")
	public String savePDIS01(HttpServletRequest request, Map model) throws Exception {
		DataMap map = (DataMap) request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		DataMap data = wmsSystemService.savePDIS01(map);
		model.put("data", data);
		return JSON_VIEW;
	}
	
	/** PDA기초실사내역 > 취소 */
	@RequestMapping("/wms/wmsSystem/json/canclePDIS03.*")
	public String canclePDIS03(HttpServletRequest request, Map model) throws SQLException {
		DataMap map = (DataMap) request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		DataMap data = wmsSystemService.canclePDIS03(map);
		model.put("data", data);
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/system/json/saveIS01EXCEL.*")
	public String saveIS01EXCEL(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = wmsSystemService.saveIS01EXCEL(map);
		model.put("data", data);
		return JSON_VIEW;
	}
    
    @RequestMapping("/wms/system/json/inspectionIntstki.*")
	public String inspectionIntstki(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = wmsSystemService.inspectionIntstki(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
    
    @RequestMapping("/wms/system/json/palletizingIntstki.*")
	public String palletizingIntstki(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = wmsSystemService.palletizingIntstki(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
    
    @RequestMapping("/wms/system/json/deleteIS01.*")
	public String deleteIS01(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = wmsSystemService.deleteIS01(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
    @RequestMapping("/wms/system/json/saveSI02Proc.*")
	public String saveSI02Proc(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = wmsSystemService.saveSI02Proc(map);
		model.put("data", data);
		return JSON_VIEW;
	}
    
    @RequestMapping("/wms/system/json/getPsKlogiToWms.*")
	public String getPsKlogiToWms(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = wmsSystemService.getPsKlogiToWms(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
    
    @RequestMapping("/wms/system/json/saveIS03EXCEL.*")
	public String saveIS03EXCEL(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = wmsSystemService.saveIS03EXCEL(map);
		model.put("data", data);
		return JSON_VIEW;
	}
    
    @RequestMapping("/wms/system/json/palletizingIntstkiIS03.*")
	public String palletizingIntstkiIS03(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = wmsSystemService.palletizingIntstkiIS03(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
    
}
