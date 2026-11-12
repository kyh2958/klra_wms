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
import project.common.bean.DataMap;
import project.common.controller.BaseController;
import project.common.service.CommonService;
import project.mobile.service.MobileInboundGoodsReceiptService;
import project.mobile.service.MobileService;
import project.wms.service.InboundService;

@Controller
public class MobileInboundGoodsReceiptController extends BaseController {

	static final Logger log = LogManager.getLogger(MobileInboundGoodsReceiptController.class.getName());

	@Autowired
	private CommonService commonService;

	@Autowired
	private MobileService mobileService;

	@Autowired
	private MobileInboundGoodsReceiptService mobileInboundGoodsReceiptService;
	
	@Autowired
	private InboundService inboundService;

	@RequestMapping("/mobile/mobileInboundGoodsReceipt/json/SavePDGR01.*")
	public String savePDGR01(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

		Object data = mobileInboundGoodsReceiptService.savePDGR01(map);

		model.put("data", data);

		return JSON_VIEW;
	}

	@RequestMapping("/mobile/mobileInboundGoodsReceipt/json/SavePDGR01_POP.*")
	public String savePDGR01_POP(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

		//Object data = mobileInboundGoodsReceiptService.savePDGR01_POP(map);
		DataMap rsMap = mobileInboundGoodsReceiptService.savePDGR01_POP(map);
		
		List<DataMap> ifidList = rsMap.getList("ifidList");
		
		String ifRsCode = "S";
		String ifRsMsg = "";
		
		if(null!=ifidList && ifidList.size()>0) {
			
			// 인터페이스 전송
			for(DataMap item : ifidList) {
				item.put("ownrky", map.getString("OWNRKY"));
				item.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				inboundService.sendIf(item);
				
				if(!item.getString("ifRsMsg").equals("SUCCESS")) {
					ifRsCode = "E";
					ifRsMsg = item.getString("ifRsMsg");
				}
				
			}
			
		}

		model.put("data", rsMap);
		
		model.put("ifRsCode", ifRsCode);
		model.put("ifRsMsg", ifRsMsg);

		return JSON_VIEW;
	}

	@RequestMapping("/mobile/mobileInboundGoodsReceipt/json/SavePDGR02.*")
	public String savePDGR02(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		Object data = mobileInboundGoodsReceiptService.savePDGR02(map);
		
		model.put("data", data);
		
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/mobileInboundGoodsReceipt/json/SavePDGR05.*")
	public String savePDGR05(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

		DataMap rsMap = mobileInboundGoodsReceiptService.savePDGR05(map);
		
		List<DataMap> ifidList = rsMap.getList("ifidList");
		
		String ifRsCode = "S";
		String ifRsMsg = "";
		
		if(null!=ifidList && ifidList.size()>0) {
			
			// 인터페이스 전송
			for(DataMap item : ifidList) {
				item.put("ownrky", map.getString("OWNRKY"));
				item.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				inboundService.sendIf(item);
				
				if(!item.getString("ifRsMsg").equals("SUCCESS")) {
					ifRsCode = "E";
					ifRsMsg = item.getString("ifRsMsg");
				}
				
			}
			
		}

		model.put("data", rsMap);
		
		model.put("ifRsCode", ifRsCode);
		model.put("ifRsMsg", ifRsMsg);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/mobileInboundGoodsReceipt/json/SavePDGR05_POP.*")
	public String savePDGR05_POP(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

		DataMap rsMap = mobileInboundGoodsReceiptService.savePDGR05_POP(map);
		
		model.put("data", rsMap);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/mobileInboundGoodsReceipt/json/GR01ITEMLIST.*")
	public String GR01ITEMLIST(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileInboundGoodsReceipt", "GR01ITEMLIST");
		
		Object data = commonService.getList(map);
				
		model.put("data", data);

		return JSON_VIEW;	
	}
	
	@RequestMapping("/mobile/mobileInboundGoodsReceipt/json/GR02ITEMLIST.*")
	public String GR02ITEMLIST(HttpServletRequest request, Map model) throws SQLException{
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		
		map.setModuleCommand("WmsMobileInboundGoodsReceipt", "GR02ITEMLIST");
		
		Object data = commonService.getList(map);
				
		model.put("data", data);

		return JSON_VIEW;

	}
	
	@RequestMapping("/mobile/mobileInboundGoodsReceipt/json/SavePDGR08.*")
	public String SavePDGR08(HttpServletRequest request, Map model) throws Exception{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);

		Object data = mobileInboundGoodsReceiptService.savePDGR08(map);

		model.put("data", data);

		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/mobileInboundGoodsReceipt/json/validationCompleteBarcod.*") 
	public String validationCompleteBarcod(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		String result = mobileInboundGoodsReceiptService.validationCompleteBarcod(map);
		model.put("data", result);
		return JSON_VIEW;
	}
	
	@RequestMapping("/mobile/mobileInboundGoodsReceipt/json/validationCompleteInBarcod.*") 
	public String validationCompleteInBarcod(HttpServletRequest request, Map model) throws SQLException{				
		DataMap map = (DataMap)request.getAttribute(CommonConfig.PARAM_ATT_KEY);
		String result = mobileInboundGoodsReceiptService.validationCompleteInBarcod(map);
		model.put("data", result);
		return JSON_VIEW;
	}
	
}