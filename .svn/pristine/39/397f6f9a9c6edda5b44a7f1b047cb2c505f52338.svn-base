package project.wms.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import project.common.airfw.User;
import project.common.airfw.Utils;
import project.common.bean.CommonConfig;
import project.common.bean.CommonLabel;
import project.common.bean.DataMap;
import project.common.bean.UriInfo;
import project.common.controller.BaseController;
import project.common.service.CommonService;
import project.common.util.ShaUtil;
import project.wms.service.WmsService;

@Controller
public class WmsController extends BaseController {
	
	private static Logger log = Logger.getLogger(WmsController.class);

	@Autowired
	private CommonLabel commonLabel;	
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private WmsService wmsService;
	
	@RequestMapping("/wms/index/{page}.*")
	public String index(HttpServletRequest request, Map model, @PathVariable String page) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("Wms", "WAHMACOMBO");
		
		List list = commonService.getList(map);
		
		model.put("WAHMACOMBO", list);
		
		map.setModuleCommand("Common", "COMCOMBO");
		map.put("CODE", "LANGKY");
		
		List list1 = commonService.getList(map);
		
		model.put("LANGKY", list1);
		
		map.setModuleCommand("Common", "LOGINMSG");
		List list2 = commonService.getList(map);
		
		model.put("MSG", list2);
		
		return "/wms/"+page;
	}

	@RequestMapping("/wms/common/json/leftMenu.*")
	public String leftMenu(HttpServletRequest request, Map model) throws SQLException{
		String langky = request.getSession().getAttribute(CommonConfig.SES_USER_LANGUAGE_KEY).toString();
		String userid = request.getSession().getAttribute(CommonConfig.SES_USER_ID_KEY).toString();
		
		DataMap map = new DataMap("Common", "MENUTREE");
		map.put("LANGKY", langky);
		map.put("USERID", userid);
		
		List<DataMap> list = commonService.getList(map);
		
		model.put("list", list);
		
		return "/wms/left";
	}
	
	@RequestMapping("/wms/{page}.*")
	public String page(HttpServletRequest request, Map model, @PathVariable String page) throws SQLException{
		if(page.equals("index")) {
			DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
			map.setModuleCommand("Wms", "WAHMACOMBO");
			
			List list = commonService.getList(map);
			
			model.put("WAHMACOMBO", list);
			
			map.put("CODE", "LANGKY");
			
			List list1 = commonService.getList("airfw.COMCOMBO", map);
			
			model.put("LANGKY", list1);
			
			List list2 = commonService.getList("airfw.LOGINMSG", map);
			
			model.put("MSG", list2);
		}
		return "/wms/"+page;
	}
	
	@RequestMapping("/index.*")
	public String indexPage(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("Wms", "WAHMACOMBO");
		
		List list = commonService.getList(map);
		
		model.put("WAHMACOMBO", list);
		
		map.put("CODE", "LANGKY");
		
		List list1 = commonService.getList("airfw.COMCOMBO", map);
		
		model.put("LANGKY", list1);
		
		List list2 = commonService.getList("airfw.LOGINMSG", map);
		
		model.put("MSG", list2);
		return "/wms/index";
	}
	
	@RequestMapping("/wms/{module}/{page}.*")
	public String mpage(@PathVariable String module, @PathVariable String page) throws SQLException{
		
		return "/wms/"+module+"/"+page;
	}
	
	@RequestMapping("/wms/common/json/getDocSeq.*")
	public String getDocSeq(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map = commonService.getMap(map);
		String docseq = map.getString("DOCSEQ");
		model.put("DOCNO", docseq);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/common/json/changeWareky.*")
	public String login(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("Wms", "OWNER");
		
		DataMap data = (DataMap)commonService.getMap(map);
		
		if(data.containsKey("OWNER")){
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute(CommonConfig.SES_USER_OBJECT_KEY);
			user.setOwner(data.getString("OWNER"));
			
			String wareky = map.getString("WAREKY");
			String mtype = user.getUserg4();
			
			DataMap param = new DataMap();
			param.setModuleCommand("Common", "MTYPE_PLANT");
			param.put("WAREKY", wareky);
			param.put("MTYPE", mtype);
			param.put(CommonConfig.SES_USER_ID_KEY, user.getUserid());
			DataMap plantMap = commonService.getMap(param);
			if (plantMap == null || plantMap.containsKey("PLNTKY") == false) {
				model.put("data", "N_NOT_EXIST_PLANT");
				return JSON_VIEW;
			}
			
			session.setAttribute(CommonConfig.SES_USER_WHAREHOUSE_KEY, wareky);
			session.setAttribute(CommonConfig.SES_USER_OWNER_KEY, user.getOwner());
			session.setAttribute(CommonConfig.SES_MTYPE, mtype);
			session.setAttribute(CommonConfig.SES_USER_PLANT_KEY, plantMap.getString("PLNTKY"));
			
			model.put("data", "S");
		}else{
			model.put("data", "F");
		}	
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/common/common_ezprint.*")
	public String ezpage(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		UriInfo uriInfo = (UriInfo)request.getAttribute(CommonConfig.REQUEST_URI_INFO_KEY);
		//model.put("filename", map.get("filename"));
		model.put("andvalue", map.get("andvalue"));
		model.put("width", map.get("width"));
		model.put("height", map.get("height"));
		model.put("url", uriInfo.getUrl());
		boolean secure= request.isSecure();
		
		if( request.isSecure() ) {
			  model.put("filename", "/ezgen/https/"+map.get("filename")); 
		}else{ 
			  model.put("filename", "/ezgen/"+map.get("filename"));
		}
		return "/wms/common/common_ezprint";
	}
	
	@RequestMapping("/wms/common/userinfo.*")
	public String userInfo(HttpSession session, HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		
		Object obj = session.getAttribute(CommonConfig.SES_USER_INFO_KEY);
		DataMap data = null;
		
		if(obj != null){
			data = (DataMap)obj;
		}
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/wms/json/loginChk.*")
	public String loginChk(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Utils utils = new Utils();
		
		String chkyn = "Y";		
		String userid     = map.get("USERID").toString();
		String newpasswd  = map.get("NEWPASSWD").toString();
		String newpasswd2 = map.get("NEWPASSWD2").toString();
		log.debug("사용자ID값(USERID):"+userid+"/(새 비밀번호 값):"+newpasswd+"/(새 비밀번호 확인값):"+newpasswd2);

				
		map.setModuleCommand("Common", "LOGINCHK");		
		User user = (User)commonService.getObj(map);		
		String oldpasswd = user.getPasswd();
		
		// 0. 기존 비밀번호와 같을 수 없습니다.
		if(oldpasswd.equals(newpasswd)){ 
			chkyn = "N";
			model.put("Msg", "기존 비밀번호와 같을 수 없습니다.");
		}
		
		// 1. 변경 비밀번호 두개가 일치하지 않습니다.1
		if(!newpasswd.equals(newpasswd2)){
			chkyn = "N";
			model.put("Msg", "변경 비밀번호 두개가 일치하지 않습니다.");
		}
		
		// 2. ID와 비밀번호는 같을 수 없습니다2
		if(userid.equals(newpasswd)){
			chkyn = "N";
			model.put("Msg", "ID와 비밀번호는 같을 수 없습니다");
		}
		
		// 3. 자리수 체크3
		if(newpasswd.length() <8 || newpasswd.length() >= 20){
			chkyn = "N";
			model.put("Msg", "자리수 체크 하십시오. 8자리 이상 20자리이하 입니다.");
		}
		
		// 4. 영문자 숫자 패턴 체크 
        // 영문, 숫자, 특수문자 2종 이상 혼용
 		int chk = 0;
		Pattern patt=Pattern.compile("[\\w&&[^_]]+");  //혼용
	    Pattern pnum=Pattern.compile("[\\d]+");  //0-9
	    Pattern pstr=Pattern.compile("[a-zA-Z]+");  //a-zA-Z
	    
	    if(!pnum.matcher(newpasswd).matches())chk ++; 
	    if(!pstr.matcher(newpasswd).matches())chk ++; 
	    if(!patt.matcher(newpasswd).matches())chk ++; 
        if(chk < 2){ 
        	chkyn = "N";
			model.put("Msg", "영문, 숫자, 특수문자 2종 이상 혼용");
        }
        
        // 5. 1234567890 / ABCDEFG 등 연속된 숫자가 4자리 이상이면 안될 것-> 역순 포함 
        if (!utils.passwd_count(newpasswd)){
        	chkyn = "N";
			model.put("Msg", " 연속된 숫자가 4자리 이상이면 안됩니다.");
        }
		
		if(chkyn == "Y"){
			// SHA 256 - Passwd 암호화 .
			map.put("PASSWD",map.get("PASSWD").toString());
			
			if(user != null && !"V".equals(user.getDelmak()) ){		
				model.put("data", "S");
			}else if( user != null && "V".equals(user.getDelmak())){
				model.put("data","D");
			}else{
				model.put("data", "F");
			}	
		}else{
			model.put("data", "N");
		}
		
		return JSON_VIEW;
	}	
	
	@RequestMapping("/wms/json/updatepassword.*")
	public String updatepassword(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		String newPwd = map.get("NEWPASSWD").toString();
		String shaNewPasswd ="";
		
		try {
			shaNewPasswd = ShaUtil.toSha256String(newPwd);
			map.put("PASSWD",      shaNewPasswd);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		map.setModuleCommand("Common", "PASSWORDNEW");
		int count = commonService.update(map);  
		model.put("data", count);
		
		return JSON_VIEW;
	}	
	
	@RequestMapping("/wms/system/json/saveLog.*")
	public String saveRT09CS(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = wmsService.saveLog(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
}