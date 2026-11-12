package project.wms.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.common.bean.CommonConfig;
import project.common.bean.DataMap;
import project.common.dao.CommonDAO;

@Service
public class WmsBatchService {

	private static Logger log = Logger.getLogger(WmsBatchService.class);

	@Autowired
	public CommonDAO commonDao;
	@Autowired
	private WmsService wmsService;
	@Autowired
	public InventoryService inventoryService;

	@Transactional
	public void initMTO() throws SQLException {
		// MTO대상 목록 조회
		DataMap param = new DataMap();
		param.setModuleCommand("WmsBatch", "MTO_WAREKY");
		List<DataMap> wareList = commonDao.getList(param);
		//log.debug("list size : " + wareList.size());

		String userId = "BATCH";
		
		List<String> docnumList = new ArrayList<>();
		for (DataMap ware : wareList) {
			String wareky = ware.getString("WAREKY");
			String docSeq = wmsService.getDocSeq("423"); // 재고MTO변경
			
			// 조정문서 header 양식 조회
			param = new DataMap();
			param.put("WAREKY", wareky);
			param.put("DOCUTY", "423");
			param.setModuleCommand("WmsInventory", "SJ03");
			List<DataMap> headList = commonDao.getList(param);
			
			// 조정문서 header 생성
			DataMap head = headList.get(0);
			head.put("SADJKY", docSeq);
			head.put(CommonConfig.SES_USER_ID_KEY, userId);
			head.setModuleCommand("WmsInventory", "ADJDH");
			commonDao.update(head);

			// insert itemList
			param = new DataMap();
			param.put("WAREKY", wareky);
			param.put("SADJKY", docSeq);
			param.put(CommonConfig.SES_USER_ID_KEY, userId);
			param.setModuleCommand("WmsBatch", "INITMTO");
			commonDao.insert(param);
			
			docnumList.add(docSeq);
		}
		//log.debug("docnumList : " + docnumList);
	}
}
