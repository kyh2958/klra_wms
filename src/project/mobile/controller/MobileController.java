
package project.mobile.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import project.common.airfw.User;
import project.common.bean.CommonConfig;
import project.common.bean.CommonLabel;
import project.common.bean.DataMap;
import project.common.bean.FileRepository;
import project.common.bean.SystemConfig;
import project.common.controller.BaseController;
import project.common.service.CommonService;
import project.mobile.service.MobileService;

@Controller
public class MobileController extends BaseController {
	
	private static Logger log = Logger.getLogger(MobileController.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private MobileService mobileService;
	
	@Autowired
	private SystemConfig systemConfig;
	
	@Autowired
	private CommonLabel commonLabel;
	
	@Autowired
	private FileRepository respository;
	
	@RequestMapping("/mobile/{page}.*")
	public String page(@PathVariable String page){
		return "/mobile/"+page;
	}
	
	@RequestMapping("/mobile/{module}/{page}.*")
	public String mpage(@PathVariable String module, @PathVariable String page) throws SQLException{
		return "/mobile/"+module+"/"+page;
	}
	
	@RequestMapping("/mobile/index.*")
	public String index(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("Wms", "WAHMACOMBO");
		
		List list = commonService.getList(map);
		
		model.put("WAHMACOMBO", list);
		
		map.setModuleCommand("airfw", "COMCOMBO");
		map.put("CODE", "LANGKY");
		
		List list1 = commonService.getList(map);
		
		model.put("LANGKY", list1);
		
		map.setModuleCommand("airfw", "LOGINMSG");
		List list2 = commonService.getList(map);
		
		model.put("MSG", list2);
		
		return "/mobile/index";
	}
	

	@RequestMapping("/mobile/{module}/count/json/{command}.*")
	public String count(HttpServletRequest request, @PathVariable String module, @PathVariable String command, Map model) throws SQLException{		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand(module, command);

		int count = commonService.getCount(map);
		
		model.put("data", count);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/{module}/paging/json/{command}.*")
	public String paging(HttpServletRequest request, @PathVariable String module, @PathVariable String command, Map model) throws SQLException{		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand(module, command);
		
		List list = commonService.getPagingList(map);
		
		model.put("data", list);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/{module}/list/json/{command}.*")
	public String list(HttpServletRequest request, @PathVariable String module, @PathVariable String command, Map model) throws SQLException{		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand(module, command);
		
		List list = commonService.getList(map);
		
		model.put("data", list);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/{module}/datalist/{dataType}/{command}.*")
	public String datalist(HttpServletRequest request, @PathVariable String module, @PathVariable String command, Map model) throws SQLException, InterruptedException{		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand(module, command);
		
		String listData = commonService.getJdbcData(map);

		model.put("data", listData);
		
		return TEXT_VIEW;
	}
	
	@RequestMapping("/mobile/{module}/map/json/{command}.*")
	public String map(HttpServletRequest request, @PathVariable String module, @PathVariable String command, Map model) throws SQLException{		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand(module, command);
		
		Map data = commonService.getMap(map);
		
		model.put("data", data);
		model.put("map", map);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/{module}/insert/json/{command}.*")
	public String insert(HttpServletRequest request, @PathVariable String module, @PathVariable String command, Map model) throws SQLException{		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand(module, command);

		Object data = commonService.insert(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/{module}/update/json/{command}.*")
	public String update(HttpServletRequest request, @PathVariable String module, @PathVariable String command, Map model) throws SQLException{		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand(module, command);

		int rs = commonService.update(map);
		
		model.put("data", rs);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/{module}/delete/json/{command}.*")
	public String delete(HttpServletRequest request, @PathVariable String module, @PathVariable String command, Map model) throws SQLException{		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand(module, command);

		int rs = commonService.delete(map);
		
		model.put("data", rs);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/main.*")
	public String main(HttpServletRequest request, Map model) throws SQLException{
		
		String langky = request.getSession().getAttribute(CommonConfig.SES_USER_LANGUAGE_KEY).toString();
		
		//model.put("label", commonLabel.getLabel(langky));
		//model.put("message", commonLabel.getMessage(langky));
		
		return "/mobile/main";
	}

	@RequestMapping("/mobile/Mobile/json/updateMIP02Sub.*")
	public String updateMIP02Sub(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		
		Object data = mobileService.updateMIP02Sub(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMMV01.*")
	public String SaveMMV01(HttpServletRequest request, Map model) throws SQLException, Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.saveMMV01(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}

	
	@RequestMapping("/mobile/Mobile/json/SaveMGR01_whyfail.*")
	public String SaveMGR01_whyfail(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMGR01_whyfail(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMGR01.*")
	public String SaveMGR01(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMGR01(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMGR06.*")
	public String SaveMGR06(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMGR06(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMGR06_whyfail.*")
	public String SaveMGR06_whyfail(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMGR06_whyfail(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMGR09.*")
	public String saveMgr09(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.saveMGR09(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMDL07.*")
	public String saveMdl07(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.saveMDL07(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMGR20.*")
	public String SaveMGR20(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileService.SaveMGR20(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMDL06.*")
	public String SaveMDL06(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMDL06(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMTO07.*")
	public String SaveMTO07(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMTO07(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/LocakyCheck.*")
	public String LocakyCheck(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.LocakyCheck(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	
	@RequestMapping("/mobile/Mobile/json/validateMobileDailyCheck.*")
	public String validateMobileDailyCheck(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.validateMobileDailyCheck(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/checkLota05.*")
	public String checkLota05(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.checkLota05(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/usrname.*")
	public String usrname(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.usrname(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}

	@RequestMapping("/mobile/json/logout.*")
	public String logout(HttpSession session, HttpServletRequest request, Map model) throws SQLException {
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

		if(session.getAttribute(CommonConfig.SES_USER_ID_KEY) != null){
			String userid = session.getAttribute(CommonConfig.SES_USER_ID_KEY).toString();
			
			session.removeAttribute(CommonConfig.SES_USER_OBJECT_KEY);
			session.removeAttribute(CommonConfig.SES_USER_ID_KEY);
			session.removeAttribute(CommonConfig.SES_USER_NAME_KEY);
			session.removeAttribute(CommonConfig.SES_USER_COMPANY_KEY);
			session.removeAttribute(CommonConfig.SES_DEPT_ID_KEY);
			session.removeAttribute(CommonConfig.SES_USER_INFO_KEY);
			session.removeAttribute(CommonConfig.SES_USER_MENU_KEY);
			
			map.setModuleCommand(systemConfig.getCmmonType(), "LOGOUT");
			commonService.update(map);
			
			map.setModuleCommand("System", "LOGOUT_LOG");
			commonService.insert(map);
		}
		return "redirect:/mobile/index.page";
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMDL20.*")
	public String SaveMDL20(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMDL20(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveRegino.*")
	public String SaveRegino(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveRegino(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveRtnRegino.*")
	public String SaveRtnRegino(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveRtnRegino(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMGR02.*")
	public String SaveMGR02(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMGR02(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMPT01.*")
	public String SaveMPT01(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMPT01(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMPT02.*")
	public String SaveMPT02(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMPT02(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMIP02.*")
	public String SaveMIP02(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMIP02(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMDL08.*")
	public String SaveMDL08(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMDL08(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/SaveMIP04.*")
	public String SaveMIP04(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.SaveMIP04(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/validateMobileShpokyCheck.*")
	public String validateMobileShpokyCheck(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.validateMobileShpokyCheck(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("mobile/Mobile/json/saveMIM01.*")
	public String saveMIM01(HttpServletRequest request, Map model) throws SQLException, IOException {
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.saveMIM01(request, map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/imgFileDelete.*")
	public String imgFileDelete(HttpServletRequest request, Map model) throws FileNotFoundException, IOException, SQLException {
		DataMap map = (DataMap) request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.imgFIleDelete(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("mobile/Mobile/json/saveMIM02.*")
	public String saveMIM02(HttpServletRequest request, Map model) throws SQLException, IOException {
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.saveMIM02(request, map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile/json/dnFileDelete.*")
	public String dnFileDelete(HttpServletRequest request, Map model) throws FileNotFoundException, IOException, SQLException {
		DataMap map = (DataMap) request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileService.dnFileDelete(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
}