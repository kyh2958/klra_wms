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
import project.mobile.service.MobileOutboundPickingService;
import project.mobile.service.MobileOutboundPickingService_COPY;
import project.wms.service.InboundService;

@Controller
public class MobileOutboundPickingController_COPY extends BaseController {
	
	static final Logger log = LogManager.getLogger(MobileOutboundPickingController_COPY.class.getName());

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CommonLabel commonLabel;
	
	@Autowired
	private InboundService inboundService;
	
	@Autowired
	private MobileOutboundPickingService_COPY mobileOutboundPickingService;
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK01SKU.*")
	public String makeTaskNomal_PDMV04(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPK01SKU");
		
		Object data = commonService.getList(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK01SKUCNT.*")
	public String PDPK01SKUCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPK01SKUCNT");
		
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}

	@RequestMapping("/mobile/Mobile_COPY/json/ConfirmPickingPDPK01.*")
	public String ConfirmPickingPDPK01(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
			
		Object data = mobileOutboundPickingService.ConfirmPickingPDPK01(map);
				
		model.put("data", data);

		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK01CNT.*")
	public String PDPK01CNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPK01CNT");
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK01SUBCNT.*")
	public String PDPK01SUBCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPK01SUBCNT");
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK01ITEMLIST.*")
	public String GR01ITEMLIST(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPK01ALSTKY");
		
		Object data = commonService.getList(map);
				
		model.put("data", data);

		return JSON_VIEW;	
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK01UOM.*")
	public String PDPK01UOM(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPK01UOM");
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK02SKU.*")
	public String PDPK02SKU(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPK02SKU");
		
		Object data = commonService.getList(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK02SKUCNT.*")
	public String PDPK02SKUCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPK02SKUCNT");
		
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK02CNT.*")
	public String PDPK02CNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPK02CNT");
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK02SUBCNT.*")
	public String PDPK02SUBCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPK02SUBCNT");
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}  
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK02UOM.*")
	public String PDPK02UOM(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPK02UOM");
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/ConfirmPickingPDPK02.*")
	public String ConfirmPickingPDPK02(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
			
		Object data = mobileOutboundPickingService.ConfirmPickingPDPK02(map);
				
		model.put("data", data);

		return JSON_VIEW;
	} 
	
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPKRM01SKUCNT.*")
	public String PDPKRM01SKUCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPKRM01SKUCNT");
		
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPKRM01CNT.*")
	public String PDPKRM01CNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPKRM01CNT");
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK01Save.*")
	public String PDPK01Save(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
			
		Object data = mobileOutboundPickingService.PDPK01Save(map);
				
		model.put("data", data);

		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPKRM01SUBCNT.*")
	public String PDPKRM01SUBCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPKRM01SUBCNT");
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPKRM01UOM.*")
	public String PDPKRM01UOM(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPKRM01UOM");
		Object data = commonService.getMap(map);
				
		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/ConfirmPickingPDPKRM01.*")
	public String ConfirmPickingPDPKRM01(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
			
		Object data = mobileOutboundPickingService.ConfirmPickingPDPKRM01(map);
				
		model.put("data", data);

		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPKRM01SKU.*")
	public String PDPKRM01SKU(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		map.setModuleCommand("WmsMobileOutboundPicking", "PDPKRM01SKU");
		Object data = commonService.getList(map);
				
		model.put("data", data);

		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPKRM01QTSWRK.*")
	public String PDPKRM01QTSWRKCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.PDPKRM01QTSWRK(map);
		model.put("data", data);
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPKRM01QTSAVBCNT.*")
	public String PDPKRM01QTSAVBCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.PDPKRM01QTSAVBCNT(map);
		model.put("data", data);
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPKRM01SHPINFO.*")
	public String PDPKRM01SHPINFO(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.PDPKRM01SHPINFO(map);
		model.put("data", data);
		return JSON_VIEW; 
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPKRM01QTUALOCNT.*")
	public String PDPKRM01QTUALOCNT(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		int qtualo = mobileOutboundPickingService.PDPKRM01QTUALOCNT(map);
		model.put("CNT", qtualo);
		return JSON_VIEW; 
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PickingInfoPDPKRM01.*")
	public String PickingInfoPDPKRM01(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.PickingInfoPDPKRM01(map);
		model.put("data", data);
		return JSON_VIEW; 
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/SavePDDL05.*")
	public String SavePDDL05(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.SavePDDL05(map);
		model.put("data", data);
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK05DETAILCNT.*")
	public String PDPK05DETAILCNT(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.PDPK05DETAILCNT(map);
		model.put("data", data);
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK05DETAIL02HEAD.*")
	public String PDPK05DETAIL02HEAD(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.PDPK05DETAIL02HEAD(map);
		model.put("data", data);
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK05SaveEnd.*")
	public String PDPK05SaveEnd(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.PDPK05SaveEnd(map);
		model.put("data", data);
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK05StockValidation.*")
	public String PDPK05StockValidation(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.PDPK05StockValidation(map);
		model.put("data", data);
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/DL061REAPPLY.*")
	public String DL061REAPPLY(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.DL061REAPPLY(map);
		model.put("data", data);
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDLD21DETAILCNT.*")
	public String PDLD21DETAILCNT(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.PDLD21DETAILCNT(map);
		model.put("data", data);
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDLD21LISTValidation.*")
	public String PDLD21LISTValidation(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.PDLD21LISTValidation(map);
		model.put("data", data);
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDLD21SaveEnd.*")
	public String PDLD21SaveEnd(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		Object data = mobileOutboundPickingService.PDLD21SaveEnd(map);
		model.put("data", data);
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/savePDTK10.*")
	public String savePDTK10(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		//Object data = mobileOutboundPickingService.savePDTK10(map);
		
		DataMap rsMap = mobileOutboundPickingService.savePDTK10(map);
		
		List<DataMap> rcpttyList = rsMap.getList("rcpttyList");
		

		model.put("data", rsMap);
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/barcodCheck.*")
	public String barcodCheck(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = mobileOutboundPickingService.barcodCheck(map);			

		model.put("data", rsMap);
		
		return JSON_VIEW;
	} 
	@RequestMapping("/mobile/Mobile_COPY/json/barcodCheckHL.*")
	public String barcodCheckHL(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = mobileOutboundPickingService.barcodCheckHL(map);			
		
		model.put("data", rsMap);
		
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/taskLockCheck.*")
	public String taskLockCheck(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = mobileOutboundPickingService.taskLockCheck(map);			

		model.put("data", rsMap);
		
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/taskLockUpdate.*")
	public String taskLockUpdate(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = mobileOutboundPickingService.taskLockUpdate(map);			

		model.put("data", rsMap);
		
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/OrderCheck.*")
	public String OrderCheck(HttpServletRequest request, Map model) throws SQLException{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = mobileOutboundPickingService.OrderCheck(map);			

		model.put("data", rsMap);
		
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/sendStknumInfo.*")
	public String sendStknumInfo(HttpServletRequest request, Map model) throws Exception{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = mobileOutboundPickingService.sendStknumInfo(map);			

		model.put("data", rsMap);
		
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK05DETAIL02_SAVECNT.*")
	public String PDPK05DETAIL02_SAVECNT(HttpServletRequest request, Map model) throws Exception{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = mobileOutboundPickingService.PDPK05DETAIL02_SAVECNT(map);			

		model.put("data", rsMap);
		
		return JSON_VIEW;
	} 
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDPK05DETAIL02_DELETE.*")
	public String PDPK05DETAIL02_DELETE(HttpServletRequest request, Map model) throws Exception{
		
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		DataMap rsMap = mobileOutboundPickingService.PDPK05DETAIL02_DELETE(map);			

		model.put("data", rsMap);
		
		return JSON_VIEW;
	} 
	
	
	@RequestMapping("/mobile/Mobile_COPY/json/PDMTO_CHANGE_DATA.*")
	public String PDMTO_CHANGE_DATA(HttpServletRequest request, Map model) throws Exception{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		DataMap rsMap = mobileOutboundPickingService.mtoChangeData(map);			
		model.put("data", rsMap);
		return JSON_VIEW;
	}
}
