package project.wms.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import project.common.airfw.RfcCallFunction;
import project.common.bean.CommonConfig;
import project.common.bean.DataMap;
import project.common.dao.CommonDAO;
import project.common.util.Util;

@Service
public class OutboundServiceDL042 {

	private static Logger log = Logger.getLogger(OutboundServiceDL042.class);

	@Autowired
	private Util util;

	@Autowired
	public CommonDAO commonDao;

	@Autowired
	public WmsService wmsService;

	@Autowired
	public TaskService taskService;

	@Autowired
	public InboundService inboundService;

	@Autowired
	public IFService iFService;

	@Autowired
	private PlatformTransactionManager transactionManager;

	public static boolean isNull(String value) {
		return (value == null);
	}

	public static boolean isEmpty(String value) {
		return (isNull(value) || value.trim().length() == 0);
	}

	public static boolean isNotEmpty(String value) {
		return isEmpty(value) ? false : true;
	}

	public static String leftPad(String stringToPad, String padder, int size) {
		StringBuffer strb;
		StringCharacterIterator sci;

		if (padder.length() == 0)
			return stringToPad;

		if (isNull(stringToPad)) {
			char[] t = new char[size];
			for (int i = 0; i < t.length; i++)
				t[i] = padder.charAt(0);

			stringToPad = String.valueOf(t);
		}

		strb = new StringBuffer(size);
		sci = new StringCharacterIterator(padder);

		while (strb.length() < (size - stringToPad.length()))
			for (char ch = sci.first(); ch != CharacterIterator.DONE; ch = sci.next())
				if (strb.length() < size - stringToPad.length())
					strb.insert(strb.length(), String.valueOf(ch));
		return strb.append(stringToPad).toString();
	}

	public String itemValueCheck(DataMap map) throws SQLException {
		String result = "OK";
		List<DataMap> list = map.getList("list");
		DataMap head = map.getMap("head");

		DataMap row;
		String VALIDATION_SQL = "";

		String validationSql = "";
		String key = map.get("key").toString();
		if (map.containsKey("key")) {
			validationSql = util.itemCreateValidationSql(map, key);
			map.put(CommonConfig.VALIDATION_SQL_KEY, validationSql);
			map.put("WAREKY", head.getString("WAREKY"));
			map.setModuleCommand("DL042", "DL042_SHPDH");

			List<DataMap> vList = commonDao.getValidation(map);
			//log.debug("size:::::::::::::" + vList.size());
			if (vList.size() > 0) {
				result = "OUT_M0140 VBELN";
			}
		}
		//log.debug("result:::::::::::::" + result);
		return result;
	}

	@Transactional
	public int assignmentDL042(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");
		List<DataMap> list = map.getList("list");

		int count = 0;
		DataMap row;
		DataMap procMap;
		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");
			row.setModuleCommand("DL042", "BATCHALLOCATION");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

			procMap = commonDao.getMap(row);

			count++;
		}

		return count;
	}

	@Transactional
	public int deleteDL042(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");

		int count = 0;
		DataMap row = new DataMap();
		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.setModuleCommand("DL042", "DL042_ALLOCATIONCANCLE");
			commonDao.update(row);

			/*
			 * row.setModuleCommand("WmsOutbound", "IF313PROC"); DataMap procSubMap =
			 * commonDao.getMap(row);
			 * 
			 * if(row.getString("SHPMTY").equals("299")){
			 * row.setModuleCommand("WmsOutbound", "PDELSHPREF"); DataMap delShpMap =
			 * commonDao.getMap(row); }
			 */
			row.setModuleCommand("DL042", "REMOVETASKKY");
			String taskky = (String) commonDao.getObject(row);
			if (taskky != null && !taskky.equals("")) {
				row.setModuleCommand("DL042", "DL042_REMOVETASDI");
				row.put("TASKKY", taskky);
				commonDao.delete(row);

				row.setModuleCommand("DL042", "DL042_REMOVETASDH");
				commonDao.delete(row);
			}

			if (row.getString("SHPMTY").equals("299")) {

			} else {
				row.setModuleCommand("DL042", "DL042_TKNUMQNTSUB");
				commonDao.update(row);
			}

			row.setModuleCommand("DL042", "DL042_SHPDI");
			commonDao.delete(row);

			row.setModuleCommand("DL042", "DL042_SHPDH");
			commonDao.delete(row);

			count++;
		}

		return count;
	}

	@Transactional
	public int orderDeleteDL042(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");

		int count = 0;
		DataMap row = new DataMap();
		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

			row.setModuleCommand("DL042", "DL042_REMOVETASKKY");
			String taskky = (String) commonDao.getObject(row);
			if (taskky != null && !taskky.equals("")) {
				row.setModuleCommand("DL042", "REMOVETASDI");
				row.put("TASKKY", taskky);
				commonDao.delete(row);

				row.setModuleCommand("DL042", "REMOVETASDH");
				commonDao.delete(row);
			}

			count++;
		}

		return count;
	}

	@Transactional
	public int popSaveDL042(DataMap map) throws SQLException {

		List<DataMap> list = map.getList("list");

//	   자바 map 뽑아내기 연습중... by Joseph.Shim  16/11/18		
//		DataMap test = map.getMap("SES_USER_INFO_ID");
//		String tset1 = test.getString("DEPART");
//		String tset2 = map.getMap("SES_USER_INFO_ID").getString("DEPART");
//		String tQty =  list.get(0).getMap("map").getString("QTSALO");
//		
//	    List<ArrayList> arrList = map.getList("SDH_ARRAY");
//	    Object aa = arrList.get(0); 
//		//log.debug("*******************"+ tset1);

		String TASOTY = "210";
		String DOCCAT = "300";
		String STATDO = "NEW";

		SimpleDateFormat fmDate = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		String date = fmDate.format(new Date());
		SimpleDateFormat fmTime = new SimpleDateFormat("HHmmss", Locale.KOREA);
		String time = fmTime.format(new Date());

		String wareky = map.getString("SES_WAREKY");
		String shpoky = map.getString("SHPOKY");
		String shpoit = map.getString("SHPOIT");

		int count = 1;
		map.put("WAREKY", wareky);
		// 1. SHPDH, SHPDI, TASDI 정보 조회
		map.setModuleCommand("DL042", "DL042_SEARCHSHPDH");
		DataMap shpdhMap = commonDao.getMap(map);
		map.setModuleCommand("DL042", "DL042_SEARCHSHPDI");
		DataMap shpdiMap = commonDao.getMap(map);
		String svbeln = shpdiMap.getString("SVBELN");
		String sposnr = shpdiMap.getString("SPOSNR");
		String stknum = shpdiMap.getString("STKNUM");
		String stpnum = shpdiMap.getString("STPNUM");
		String ssornu = shpdiMap.getString("SSORNU");
		String ssorit = shpdiMap.getString("SSORIT");

		map.setModuleCommand("DL042", "DL042_SEARCHTASDIALL");
		List<DataMap> tasdiList = commonDao.getList(map);
		// 2. 기존의 TASDI ITEM NUMBER MAX 값 조회
		map.setModuleCommand("DL042", "DL042_SEARCHMAXTASDH");
		List<DataMap> tasdhList = commonDao.getList(map);

		DataMap dbData;
		DataMap gridData = new DataMap();
		String webKey = null;
		String dbKey = null;
		int taskit = Integer.parseInt(tasdhList.get(0).getString("TASKIT"));

		// 3. tasdi 에서 수정된 item number만 삭제
		for (int i = 0; i < tasdiList.size(); i++) {
			dbData = tasdiList.get(i).getMap("map");
			dbKey = dbData.getString("TASKKY") + dbData.getString("TASKIT");
			int dbQTY = Integer.parseInt(dbData.getString("QTTAOR"));

			for (int j = 0; j < list.size(); j++) {
				gridData = list.get(j).getMap("map");
				webKey = gridData.getString("TASKKY") + gridData.getString("TASKIT");
				int webQTY = Integer.parseInt(gridData.getString("QTSALO")); // 이 수량이 수정해서 입력한수량 QTSALO

				if (dbKey.equals(webKey) && (dbQTY != webQTY)) {
					map.put("TASKKY", dbData.getString("TASKKY"));
					map.put("TASKIT", dbData.getString("TASKIT"));
					map.setModuleCommand("DL042", "DL042POPREMOVE");
					commonDao.delete(map);
					break;
				}
			}
		}

		// 4. 재고 SELECT하여 Task Order Item/Reference 생성
		DataMap rowData = new DataMap();
		BigDecimal qty, qttaor, qtsiwh = null;

		for (DataMap row : list) {
			rowData = (DataMap) row.get("map");
			qty = new BigDecimal((String) rowData.get("QTSALO")); // 이 수량이 수정해서 입력한수량 QTSALO

			// 4-1 수량이<0 통과
			if (qty.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}

			boolean insFlag = false;
			String selectTaskit = "";
			String selectLocation = "";
			// 4-2 TASKKY 있을 때
			for (int i = 0; i < tasdiList.size(); i++) {
				String dbKey2 = tasdiList.get(i).getMap("map").getString("TASKKY")
						+ tasdiList.get(i).getMap("map").getString("TASKIT");
				String webKey2 = rowData.getString("TASKKY") + rowData.getString("TASKIT");
				// (1) taskky 있고, 수량 변화 있음
				if (dbKey2.equals(webKey2)) {
					selectTaskit = tasdiList.get(i).getMap("map").getString("TASKIT");
					selectLocation = tasdiList.get(i).getMap("map").getString("LOCASR");
					if (rowData.getInt("QTSALO") != tasdiList.get(i).getMap("map").getInt("QTTAOR")) {
						insFlag = true;
						break;
					}
				} else if ("".equals(webKey2.trim()) && rowData.getInt("QTSALO") > 0) {// taskky 없고, 수량 변화 있음
					insFlag = true;
					break;
				}
			}

			if (insFlag) {
				// 재고 셀렉트
				map.setModuleCommand("DL042", "DL042_MANUALALLOCATE_AVAILABLE_STOCK");
				map.put("OWNRKY", rowData.get("OWNRKY"));
				map.put("WAREKY", rowData.get("WAREKY"));
				map.put("AREAKY", rowData.get("AREAKY"));
				map.put("LOCAKY", rowData.get("LOCAKY"));
				map.put("SKUKEY", rowData.get("SKUKEY"));
				map.put("TRNUID", rowData.get("TRNUID"));
				map.put("PACKID", rowData.get("PACKID"));
				map.put("LOTNUM", rowData.get("LOTNUM"));
				map.put("SHPMTY", shpdhMap.get("SHPMTY"));
				List<DataMap> stockMapList = commonDao.getList(map);

//				for(DataMap stock : stockMapList){
				if (qty.compareTo(BigDecimal.ZERO) <= 0)
					break;

				map.put("TASKKY", tasdhList.get(0).getString("TASKKY"));
				if (rowData.get("LOCAKY").equals(selectLocation)) {
					map.put("TASKIT", leftPad(selectTaskit + "", "0", 6));
				} else {
					taskit += 10;
					map.put("TASKIT", leftPad(taskit + "", "0", 6));
				}
				map.put("TASKTY", "PK");
				map.put("RSNCOD", "MANU");
				map.put("STATIT", "NEW");

//					map.put("QTTAOR", qttaor);
				map.put("QTTAOR", qty);

				map.put("QTCOMP", BigDecimal.ZERO);
				map.put("OWNRKY", stockMapList.get(0).get("OWNRKY"));
				map.put("SKUKEY", stockMapList.get(0).get("SKUKEY"));
				map.put("LOTNUM", stockMapList.get(0).get("LOTNUM"));
				map.put("ACTCDT", "000000");
				map.put("ACTCTI", "000000");
//					map.put("QTYUOM", qttaor);
				map.put("QTYUOM", qty);
				map.put("TKFLKY", " ");
				map.put("STEPNO", " ");
				map.put("LSTTFL", " ");

				map.put("LOCASR", stockMapList.get(0).get("LOCAKY"));
				map.put("SECTSR", stockMapList.get(0).get("SECTID"));
				map.put("PAIDSR", stockMapList.get(0).get("PACKID"));
				map.put("TRNUSR", stockMapList.get(0).get("TRNUID"));
				map.put("STRUTY", stockMapList.get(0).get("TRUNTY"));
				map.put("SMEAKY", stockMapList.get(0).get("MEASKY"));
				map.put("SUOMKY", stockMapList.get(0).get("UOMKEY"));
				map.put("QTSPUM", stockMapList.get(0).get("QTPUOM"));
				map.put("SDUOKY", stockMapList.get(0).get("DUOMKY"));
				map.put("QTSDUM", stockMapList.get(0).get("QTDUOM"));

				map.put("LOCATG", stockMapList.get(0).get("LOCATG"));
				map.put("SECTTG", stockMapList.get(0).get("SECTID"));
				map.put("PAIDTG", stockMapList.get(0).get("PACKID"));
				map.put("TRNUTG", stockMapList.get(0).get("TRNUID"));
				map.put("TTRUTY", stockMapList.get(0).get("TRUNTY"));
				map.put("TMEAKY", stockMapList.get(0).get("MEASKY"));
				map.put("TUOMKY", stockMapList.get(0).get("UOMKEY"));
				map.put("QTTPUM", stockMapList.get(0).get("QTPUOM"));
				map.put("TDUOKY", stockMapList.get(0).get("DUOMKY"));
				map.put("QTTDUM", stockMapList.get(0).get("QTDUOM"));

				map.put("LOCAAC", " ");
				map.put("SECTAC", stockMapList.get(0).get(" "));
				map.put("PAIDAC", stockMapList.get(0).get(" "));
				map.put("TRNUAC", stockMapList.get(0).get(" "));
				map.put("ATRUTY", stockMapList.get(0).get(" "));
				map.put("AMEAKY", stockMapList.get(0).get(" "));
				map.put("AUOMKY", stockMapList.get(0).get(" "));
				map.put("QTAPUM", stockMapList.get(0).get("1"));
				map.put("ADUOKY", stockMapList.get(0).get(" "));
				map.put("QTADUM", stockMapList.get(0).get("1"));
				map.put("REFDKY", stockMapList.get(0).get("REFDKY"));
				map.put("REFDIT", stockMapList.get(0).get("REFDIT"));
				map.put("REFCAT", "200");
				map.put("REFDAT", date);
				map.put("PURCKY", stockMapList.get(0).get("PURCKY"));
				map.put("PURCIT", stockMapList.get(0).get("PURCIT"));
				map.put("ASNDKY", stockMapList.get(0).get("ASNDKY"));
				map.put("ASNDIT", stockMapList.get(0).get("ASNDIT"));
				map.put("RECVKY", stockMapList.get(0).get("RECVKY"));
				map.put("RECVIT", stockMapList.get(0).get("RECVIT"));
				map.put("SHPOKY", shpoky);
				map.put("SHPOIT", shpoit);
				map.put("GRPOKY", " ");
				map.put("GRPOIT", " ");
				map.put("SADJKY", stockMapList.get(0).get("SADJKY"));
				map.put("SADJIT", stockMapList.get(0).get("SADJIT"));
				map.put("SDIFKY", " ");
				map.put("SDIFIT", " ");
				map.put("PHYIKY", stockMapList.get(0).get("PHYIKY"));
				map.put("PHYIIT", stockMapList.get(0).get("PHYIIT"));
				map.put("DROPID", " ");
				map.put("DESC01", stockMapList.get(0).get("DESC01"));
				map.put("DESC02", stockMapList.get(0).get("DESC02"));
				map.put("ASKU01", stockMapList.get(0).get("ASKU01"));
				map.put("ASKU02", stockMapList.get(0).get("ASKU02"));
				map.put("ASKU03", stockMapList.get(0).get("ASKU03"));
				map.put("ASKU04", stockMapList.get(0).get("ASKU04"));
				map.put("ASKU05", stockMapList.get(0).get("ASKU05"));
				map.put("EANCOD", stockMapList.get(0).get("EANCOD"));
				map.put("GTINCD", stockMapList.get(0).get("GTINCD"));
				map.put("SKUG01", stockMapList.get(0).get("SKUG01"));
				map.put("SKUG02", stockMapList.get(0).get("SKUG02"));
				map.put("SKUG03", stockMapList.get(0).get("SKUG03"));
				map.put("SKUG04", stockMapList.get(0).get("SKUG04"));
				map.put("SKUG05", stockMapList.get(0).get("SKUG05"));
				map.put("GRSWGT", stockMapList.get(0).get("GRSWGT"));
				map.put("NETWGT", stockMapList.get(0).get("NETWGT"));
				map.put("WGTUNT", stockMapList.get(0).get("WGTUNT"));
				map.put("LENGTH", stockMapList.get(0).get("LENGTH"));
				map.put("WIDTHW", stockMapList.get(0).get("WIDTHW"));
				map.put("HEIGHT", stockMapList.get(0).get("HEIGHT"));
				map.put("CUBICM", stockMapList.get(0).get("CUBICM"));
				map.put("CAPACT", stockMapList.get(0).get("CAPACT"));
				map.put("WORKID", " ");
				map.put("WORKNM", " ");
				map.put("HHTTID", " ");
				map.put("AREAKY", stockMapList.get(0).get("AREAKY"));
				map.put("LOTA01", stockMapList.get(0).get("LOTA01"));
				map.put("LOTA02", stockMapList.get(0).get("LOTA02"));
				map.put("LOTA03", stockMapList.get(0).get("LOTA03"));
				map.put("LOTA04", stockMapList.get(0).get("LOTA04"));
				map.put("LOTA05", stockMapList.get(0).get("LOTA05"));
				map.put("LOTA06", stockMapList.get(0).get("LOTA06"));
				map.put("LOTA07", stockMapList.get(0).get("LOTA07"));
				map.put("LOTA08", stockMapList.get(0).get("LOTA08"));
				map.put("LOTA09", stockMapList.get(0).get("LOTA09"));
				map.put("LOTA10", stockMapList.get(0).get("LOTA10"));
				map.put("LOTA11", stockMapList.get(0).get("LOTA11"));
				map.put("LOTA12", stockMapList.get(0).get("LOTA12"));
				map.put("LOTA13", stockMapList.get(0).get("LOTA13"));
				map.put("LOTA14", stockMapList.get(0).get("LOTA14"));
				map.put("LOTA15", stockMapList.get(0).get("LOTA15"));
				map.put("LOTA16", stockMapList.get(0).get("LOTA16"));
				map.put("LOTA17", stockMapList.get(0).get("LOTA17"));
				map.put("LOTA18", stockMapList.get(0).get("LOTA18"));
				map.put("LOTA19", stockMapList.get(0).get("LOTA19"));
				map.put("LOTA20", stockMapList.get(0).get("LOTA20"));
				map.put("AWMSNO", " ");
				map.put("AWMSTS", " ");
				map.put("SMANDT", stockMapList.get(0).get("SMANDT"));
				map.put("SEBELN", stockMapList.get(0).get("SEBELN"));
				map.put("SEBELP", stockMapList.get(0).get("SEBELP"));
				map.put("SZMBLNO", stockMapList.get(0).get("SZMBLNO"));
				map.put("SZMIPNO", stockMapList.get(0).get("SZMIPNO"));
				map.put("STRAID", stockMapList.get(0).get("STRAID"));
				map.put("SVBELN", svbeln);
				map.put("SPOSNR", sposnr);
				map.put("STKNUM", stknum);
				map.put("STPNUM", stpnum);
				map.put("SWERKS", stockMapList.get(0).get("SWERKS"));
				map.put("SLGORT", stockMapList.get(0).get("SLGORT"));
				map.put("SDATBG", stockMapList.get(0).get("SDATBG"));
				map.put("STDLNR", stockMapList.get(0).get("STDLNR"));
				map.put("SSORNU", ssornu);
				map.put("SSORIT", ssorit);
				map.put("SMBLNR", stockMapList.get(0).get("SMBLNR"));
				map.put("SZEILE", stockMapList.get(0).get("SZEILE"));
				map.put("SMJAHR", stockMapList.get(0).get("SMJAHR"));
				map.put("SXBLNR", stockMapList.get(0).get("SXBLNR"));
				map.put("SAPSTS", stockMapList.get(0).get("SAPSTS"));
				map.put("DOORKY", " ");
				map.put("PTLT01", stockMapList.get(0).get("LOTA01"));
				map.put("PTLT02", stockMapList.get(0).get("LOTA02"));
				map.put("PTLT03", stockMapList.get(0).get("LOTA03"));
				map.put("PTLT04", stockMapList.get(0).get("LOTA04"));
				map.put("PTLT05", stockMapList.get(0).get("LOTA05"));
				map.put("PTLT06", stockMapList.get(0).get("LOTA06"));
				map.put("PTLT07", stockMapList.get(0).get("LOTA07"));
				map.put("PTLT08", stockMapList.get(0).get("LOTA08"));
				map.put("PTLT09", stockMapList.get(0).get("LOTA09"));
				map.put("PTLT10", stockMapList.get(0).get("LOTA10"));
				map.put("PTLT11", stockMapList.get(0).get("LOTA11"));
				map.put("PTLT12", stockMapList.get(0).get("LOTA12"));
				map.put("PTLT13", stockMapList.get(0).get("LOTA13"));
				map.put("PTLT14", stockMapList.get(0).get("LOTA14"));
				map.put("PTLT15", stockMapList.get(0).get("LOTA15"));
				map.put("PTLT16", stockMapList.get(0).get("LOTA16"));
				map.put("PTLT17", stockMapList.get(0).get("LOTA17"));
				map.put("PTLT18", stockMapList.get(0).get("LOTA18"));
				map.put("PTLT19", stockMapList.get(0).get("LOTA19"));
				map.put("PTLT20", stockMapList.get(0).get("LOTA20"));
				map.put("PASTKY", " ");
				map.put("ALSTKY", " ");
				map.put("SBKTXT", " ");
				map.put("TASRSN", " ");
				map.put("CREDAT", date);
				map.put("CRETIM", time);
				map.put("CREUSR", CommonConfig.SES_USER_ID_KEY);
				map.put("LMODAT", date);
				map.put("LMOTIM", time);
				map.put("LMOUSR", CommonConfig.SES_USER_ID_KEY);
				map.put("INDBZL", " ");
				map.put("INDARC", " ");
				map.put("UPDCHK", "0");

//					map.put("TASKKY", tasdhList.get(0).getString("TASKKY"));
//					map.put("TASKIT", leftPad(taskit+"", "0", 6));
//					map.put("TASKIR", "0001");
//					map.put("STOKKY", stock.get("STOKKY"));
//					map.put("QTSTKM", qttaor);
//					map.put("QTSTKC", BigDecimal.ZERO);
//					map.put("CREDAT", date);
//					map.put("CRETIM", time);
//					map.put("CREUSR", CommonConfig.SES_USER_ID_KEY);
//					map.put("LMODAT", date);
//					map.put("LMOTIM", time);
//					map.put("LMOUSR", CommonConfig.SES_USER_ID_KEY);
//					map.put("INDBZL", " ");
//					map.put("INDARC", " ");
//					map.put("UPDCHK", "0");
//					
				map.setModuleCommand("DL042", "TASDI");
				commonDao.insert(map);

				map.setModuleCommand("DL042", "TASDR_PROCALL");
				commonDao.getMap(map);

//				} //재고넣기 for문				
//				if(qty.compareTo(BigDecimal.ZERO) > 0){
//					throw new SQLException("재고가 부족하여 할당할 수 없습니다.");
//				}
			} // if(insflag)문
		} // isnert for문

		return count;
	}

	@Transactional
	public DataMap saveDL042(DataMap map) throws SQLException {
		DataMap rsMap = new DataMap();
		List<DataMap> headList = map.getList("head");
		List<DataMap> itemList = null;
		List<DataMap> selectList = new ArrayList<DataMap>();

		int headModifyIndex = -1;
		if (map.containsKey("headModifyIndex")) {
			headModifyIndex = map.getInt("headModifyIndex");
			itemList = map.getList("itemList");
		}

		DataMap row;
		List docNumList = new ArrayList();

		for (int i = 0; i < headList.size(); i++) {
			if (map.containsKey("param")) {
				row = new DataMap(map.getMap("param").getMap("map"));
			} else {
				row = new DataMap();
			}

			map.clonSessionData(row);
			row.append(headList.get(i).getMap("map"));

			String docNum;
			int itemCount = 0;

			// 1. 출고문서번호가 생성되지 않음, [INSERT]
			if ("".equals(row.getString("SHPOKY").trim())) {
				docNum = wmsService.getDocSeq(row.getString("DOCUTY"));
				row.put("SHPOKY", docNum);
				// 1.1 head insert
				row.setModuleCommand("WmsOutbound", "SHPDH");
				commonDao.insert(row);
				// 1.2 item insert
				if (headModifyIndex == i) { // 아이템그리드에 선택되었을 때
					row.setModuleCommand("DL042", "DL042_SHPDI");
					for (int j = 0; j < itemList.size(); j++) {
						row.append(itemList.get(j).getMap("map"));

						itemCount += 10;
						String snum = String.valueOf(itemCount);
						String inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
						row.put("SHPOKY", docNum);
						row.put("SHPOIT", inum);
						commonDao.insert(row);
					}
				} else { // 선택되지 않았을때
					row.setModuleCommand("WmsOutbound", "DL04Sub");
					selectList = commonDao.getList(row);
					for (int j = 0; j < selectList.size(); j++) {
						row.append(selectList.get(j).getMap("map"));
						row.setModuleCommand("WmsOutbound", "SHPDI");

						itemCount += 10;
						String snum = String.valueOf(itemCount);
						String inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
						row.put("SHPOKY", docNum);
						row.put("SHPOIT", inum);
						commonDao.insert(row);
					}
				}
			} else { // 2. 출고문서번호가 생성됨, [UPDATE]
				docNum = row.getString("SHPOKY");
				row.setModuleCommand("WmsOutbound", "SHPDHORG");
				commonDao.update(row);

				row.setModuleCommand("WmsOutbound", "SHPDIORG");
				for (int j = 0; j < itemList.size(); j++) {
					row.append(itemList.get(j).getMap("map"));
					commonDao.update(row);
				}
			}
			docNumList.add(docNum);
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < docNumList.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append("'").append(docNumList.get(i)).append("'");
		}

		rsMap.put("SHPOKY", sb.toString());

		return rsMap;
	}

	@Transactional
	public int saveSubDL042(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("list");
		int count = 0;

		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

			commonDao.update("WmsOutbound.DL04Sub", row);
			count++;
		}
		return count;
	}

	/**
	* @methodName   ConvertStknumToStkunm
	* @param map 출하오더번호
	* @return List<DataMap> 선적번호
	* @throws Exception
	* @description  선적번호(STKNUM)로 출하오더번호(SHPOKY) 조회
	* @history
	* __DATE_______AUTHOR__NOTE__________________________________<br>
	* * 2023.08.23 JHJ 최초생성<br>
	* * 2023.09.05 JHJ 선적번호 없는 건 조회 가능하도록 WHERE절 구문 변경<br>
	*/
	@Transactional
	public List<DataMap> ConvertStknumToShpoky(DataMap map) throws Exception {

		try {
			map.setModuleCommand("DL042", "CONVERT_STKNUM_TO_SHPOKY"); // STKNUM 로 SHPOKY 조회
			List<DataMap> shpokyList = commonDao.getList(map);

			return shpokyList;
		} catch (Exception e) {
			log.error("ERROR ConvertStknumToStkunm : {}", e);
//			throw new RuntimeException();	//임시 예외 발생
			throw new SQLException(e.getMessage());
		}
	}

	/**
	* @methodName   assignDL042
	* @description  (ORG) 할당 : 현재 사용 안함 (기준:20230905)
	*/
	@Transactional
	public DataMap assignDL042(DataMap map) throws Exception {
		List<DataMap> head = map.getList("head");
		List<DataMap> itemList = null;
		// List<DataMap> itemList = map.getList("list"); //210915 item별 할당 ->
		// head(shpoky)별 할당으로 변경하므로 주석처리
		List<DataMap> selectList = new ArrayList<DataMap>();

		int count = 0;
		String HeadGridSvbeln = map.get("HeadGridSvbeln").toString();
		String HeadGridShpoky = map.get("HeadGridShpoky").toString();

		DataMap rsRowMap = new DataMap();
		DataMap rsMap = new DataMap();
		DataMap rqMap = new DataMap();

		// SHPDH I/F CHECK

//		for (int j = 0; j < head.size(); j++) { 
//			rsRowMap = head.get(j).getMap("map");
//			rsMap.setModuleCommand("WmsTask", "VALIDATION_SHPDH"); 
//			rsMap.put("SHPOKY",
//			rsRowMap.get("SHPOKY")); 
//			rqMap = commonDao.getMap(rsMap);
//			
//			if (rqMap != null) {
//				
//				rsMap.put("SHPOKY", rqMap.getString("SHPOKY"));
//			
//			return rsMap; 
//			}
//		
//		}

		int headModifyIndex = -1;
		if (map.containsKey("headModifyIndex")) {
			headModifyIndex = map.getInt("headModifyIndex");
			itemList = map.getList("list");
		}

		DataMap row = new DataMap();
		String docNum;
		int itemCount1 = 0;
		int itemCount2 = 0;
		StringBuilder sb = new StringBuilder();

		DataMap rowMap = new DataMap();
		List<String> SHPOKY_LIST = new ArrayList<>();
		List<DataMap> headLoop = null;

		for (int j = 0; j < head.size(); j++) {
			rowMap = head.get(j).getMap("map");

			String SHPOKY = rowMap.getString("SHPOKY");

			SHPOKY_LIST.add(SHPOKY);
		}

		Collections.sort(SHPOKY_LIST);

		int sumResult = 0;

		for (int i = 0; i < SHPOKY_LIST.size(); i++) {
			row.put("SHPOKY", SHPOKY_LIST.get(i));

			// row = head.get(i).getMap("map");
			// 1. 출고문서번호가 없을 때 저장 후, 할당
//			if ("".equals(row.getString("SHPOKY").trim())) {
//				docNum = wmsService.getDocSeq(row.getString("DOCUTY"));
//				row.put("SHPOKY", docNum);
//				row.setModuleCommand("WmsOutbound", "SHPDH");
//				commonDao.insert(row);
//
//				if (headModifyIndex == i) {
//					row.setModuleCommand("WmsOutbound", "SHPDI");
//					for (int j = 0; j < itemList.size(); j++) {
//						row.append(itemList.get(j).getMap("map"));
//
//						itemCount1 += 10;
//						String snum = String.valueOf(itemCount1);
//						String inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
//						row.put("LOTA10", row.getString("LOTA10"));
//						row.put("SHPOKY", docNum);
//						row.put("SHPOIT", inum);
//						commonDao.insert(row);
//
//						// 210915 item별 할당 -> head(shpoky)별 할당으로 변경하므로 주석처리
//						// 할당(item별)
//						// row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
//						// commonDao.getMap("WmsOutbound.DL042_ALLOC_ITEM_PROCALL", row);
//					}
//				} else {
//					row.setModuleCommand("WmsOutbound", "DL04Sub");
//					selectList = commonDao.getList(row);
//					for (int j = 0; j < selectList.size(); j++) {
//						row.append(selectList.get(j).getMap("map"));
//
//						row.setModuleCommand("WmsOutbound", "SHPDI");
//
//						itemCount2 += 10;
//						String snum = String.valueOf(itemCount2);
//						String inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
//
//						row.put("SHPOKY", docNum);
//						row.put("SHPOIT", inum);
//						commonDao.insert(row);
//					}
//
//				}
//
//			}

			// 2. 출고문서번호가 있을 때 바로 할당
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			commonDao.getMap("WmsOutbound.DL042_ALLOC_PROCALL", row);

			count++;
			int Result = 0;

			if (!row.getString("result").equals("EMPTY")) {
				Result = Integer.parseInt(row.getString("result"));
			}
			;

			sumResult += Result;

			if (i != 0) {
				sb.append(",");
			}
			sb.append("'").append(row.getString("SHPOKY")).append("'");
		}

		rsMap.put("RESULT", sumResult);
		rsMap.put("SHPOKY", sb.toString());
		rsMap.put("CNT", count);

		return rsMap;
	}

	/**
	* @methodName   assignDL042_2
	* @description  실제 할당 처리 로직
	*/
	@Transactional
	public DataMap assignDL042_2(DataMap map) throws Exception {
		try {
			List<DataMap> head = map.getList("head");
			DataMap dmStknum = new DataMap(); // jhj : 마스터 출고오더번호,출하문서번호 기준을 -> 선적번호 기준으로 변경
			dmStknum.putAll(map);

			DataMap rowMap = new DataMap(); // 선택 된 마스터 선적 리스트
			StringJoiner sjStknum = new StringJoiner("','", "'", "'");
			for (int i = 0; i < head.size(); i++) {
				rowMap = head.get(i).getMap("map");

				sjStknum = sjStknum.add(rowMap.getString("STKNUM")); // 1.선적번호 여러개로 연결
			}
			String stknumList = sjStknum.toString(); // 2.문자열 변경
			dmStknum.put("STKNUM", stknumList);
			dmStknum.put("STATDOLIST", "'NEW', 'PAL'"); // 할당완료 (미작업, 부분할당 대상)
			dmStknum.put("STATITLIST", "'NEW', 'PAL'"); // 할당완료 (미작업, 부분할당 대상)

			List<DataMap> listMapShpoky = new ArrayList<>();
			listMapShpoky.addAll(ConvertStknumToShpoky(dmStknum)); // 3. 선적번호 여러개로 출하문서번호 전체 조회
			
			StringJoiner sjShpoky = new StringJoiner(",");

			if (listMapShpoky.size() < 1) {
				log.error("assignDL042_2() > listMapShpoky.size() :" + listMapShpoky.size());
				throw new Exception("할당 대상이 존재하지 않습니다.");
			}else {
				for (DataMap shpoky : listMapShpoky) {
					sjShpoky = sjShpoky.add(shpoky.getString("SHPOKY"));
				}
				String shpokyListParam = sjShpoky.toString();
				
				// Hub 프로시저 호출
				DataMap row = new DataMap(); // 출하문서번호만 추출
				row.put("SHPOKY", shpokyListParam);
				row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				
				if (!"".equals(map.getString("LOTA14_LIST"))) {
					row.put("INCLUDEBARCOD", map.getString("LOTA14_LIST"));
				}
				
				commonDao.getMap("DL042.DL042_CALL_ALLO", row);
				
				String result = row.getString("result");
				String[] resultArr = result.split("/");
				DataMap rsMap = new DataMap(); // 할당처리 후 결과
				rsMap.put("RESULT", resultArr[0]);
				rsMap.put("SHPOKY", resultArr[1]);
				rsMap.put("CNT", resultArr[2]);
				return rsMap;
			}

		} catch (Exception e) {
			log.error("ERROR AssignDL04_2 : {}", e);
			throw new SQLException(e.getMessage());
		}
	}

	/**
	* @methodName   unallocateDL042
	* @description  할당취소, 부분할당 취소
	*/
	@Transactional
	public DataMap unallocateDL042(DataMap map) throws Exception {
		List<DataMap> head = map.getList("head");
		List<DataMap> itemList = map.getList("sublist");
		List<DataMap> selectList = new ArrayList<DataMap>();
		String unalType = map.getString("unalType"); // 할당취소, 부분할당취소 구분값

		DataMap dmStknum = new DataMap(); // jhj : 마스터 출고오더번호,출하문서번호 기준을 -> 선적번호 기준으로 변경
		dmStknum.putAll(map);

		DataMap rsMap = new DataMap();
//		int count = 0;
		String HeadGridSTKNUM = map.get("HeadGridSTKNUM").toString();

		DataMap rowMap = new DataMap(); // 선택 된 마스터 선적 리스트
		StringJoiner sjStknum = new StringJoiner("','", "'", "'");
		for (int i = 0; i < head.size(); i++) {
			rowMap = head.get(i).getMap("map");

			sjStknum = sjStknum.add(rowMap.getString("STKNUM")); // 1.선적번호 여러개로 연결
		}
		String stknumList = sjStknum.toString(); // 2.문자열 변경
		dmStknum.put("STKNUM", stknumList);
		dmStknum.put("STATITLIST", "'FAL', 'PAL'"); // 할당취소 (할당완료, 부분할당 대상)

		List<DataMap> listMapShpoky = new ArrayList<>();
		listMapShpoky.addAll(ConvertStknumToShpoky(dmStknum)); // 3. 선적번호 여러개로 출하문서번호 전체 조회

		DataMap row = new DataMap(); // 출하문서번호, 아이템상태 추출
		StringJoiner sjShpoky = new StringJoiner("','", "('", "')");
		for (DataMap shpoky : listMapShpoky) {
			row.put("STATIT", shpoky.getString("STATIT"));
			row.put("SHPOKY", shpoky.getString("SHPOKY"));
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

			String taskky;
			List<DataMap> taskkyList;
			DataMap taskkyRow;

			taskkyList = commonDao.getList("DL042.DL042_SELECT_TASKKY", row); // param : SHPOKY
			if ("PCN".equals(unalType)) { // 부분할당취소
				DataMap rowSub;
				for (int j = 0; j < itemList.size(); j++) {
					rowSub = itemList.get(j).getMap("map");
					rowSub.put("KEEPIT", " ");
					rowSub.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
					// param : SHPOKY, SHPOIT
					commonDao.delete("DL042.DL042_REMOVE1", rowSub); // TASDI 부분 삭제 DELTEE
					commonDao.update("DL042.DL042_SHPDI", rowSub); // SHPDI 수정 정보 UPDATE
					commonDao.update("DL042.DL042_SHPDH", rowSub); // SHPDH 수정 정보 UPDATE
				}
			} else { // 할당취소
				row.put("KEEPIT", " ");
				commonDao.delete("DL042.DL042_REMOVE2", row); // TASDI 헤더 삭제 DELTEE
				commonDao.update("DL042.DL042_SHPDI", row); // SHPDI 수정 정보 UPDATE
				commonDao.update("DL042.DL042_SHPDH", row); // SHPDH 수정 정보 UPDATE
			}

			// TASDH 헤더 삭제
			for (int k = 0; k < taskkyList.size(); k++) {

				taskkyRow = taskkyList.get(k).getMap("map");

				// TASDH에 엮여있는 TASDI가 전부 삭제되면 TASDH 삭제
				if (commonDao.getList("DL042.DL042_REMOVE_CHECK", row).size() == 0) {
					row.put("TASKKY", taskkyRow.get("TASKKY"));
					// commonDao.delete("DL042.DL042_REMOVE3", row); // TASDH 헤더 삭제 DELTEE
				}
			}

			rsMap.put("ERRMSG", "OK");

			sjShpoky = sjShpoky.add(shpoky.getString("SHPOKY"));
		}
		String shpokyListParam = sjShpoky.toString();
//		--------------------------------------------------------------
//		List shpokyList = new ArrayList();
//		for (int i = 0; i < head.size(); i++) {
		/*
		 * String shpokyNum = head.get(i).getMap("map").getString("SHPOKY"); //
		 * shpokyList.add(shpokyNum);
		 * 
		 * row = head.get(i).getMap("map"); // String ItemStatit2 =
		 * row.getString("STATDO");
		 * 
		 * row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
		 * 
		 * String taskky; List<DataMap> taskkyList; DataMap taskkyRow;
		 * 
		 * taskkyList = commonDao.getList("DL042.DL042_SELECT_TASKKY", row); //param :
		 * SHPOKY
		 * 
		 * if ("PCN".equals(unalType)) { DataMap rowSub;
		 * 
		 * for (int j = 0; j < itemList.size(); j++) {
		 * 
		 * rowSub = itemList.get(j).getMap("map"); rowSub.put("KEEPIT", " ");
		 * rowSub.put(CommonConfig.SES_USER_ID_KEY,
		 * map.get(CommonConfig.SES_USER_ID_KEY)); //param : SHPOKY, SHPOIT
		 * commonDao.delete("DL042.DL042_REMOVE1", rowSub); // TASDI 부분 삭제 DELTEE
		 * commonDao.update("DL042.DL042_SHPDI", rowSub); // SHPDI 수정 정보 UPDATE
		 * commonDao.update("DL042.DL042_SHPDH", rowSub); // SHPDH 수정 정보 UPDATE }
		 * 
		 * } else { row.put("KEEPIT", " "); commonDao.delete("DL042.DL042_REMOVE2",
		 * row); // TASDI 헤더 삭제 DELTEE commonDao.update("DL042.DL042_SHPDI", row); //
		 * SHPDI 수정 정보 UPDATE commonDao.update("DL042.DL042_SHPDH", row); // SHPDH 수정 정보
		 * UPDATE }
		 * 
		 * // TASDH 헤더 삭제 for (int k = 0; k < taskkyList.size(); k++) {
		 * 
		 * taskkyRow = taskkyList.get(k).getMap("map");
		 * 
		 * // TASDH에 엮여있는 TASDI가 전부 삭제되면 TASDH 삭제 if
		 * (commonDao.getList("DL042.DL042_REMOVE_CHECK", row).size() == 0) {
		 * row.put("TASKKY", taskkyRow.get("TASKKY"));
		 * //commonDao.delete("DL042.DL042_REMOVE3", row); // TASDH 헤더 삭제 DELTEE } }
		 * 
		 * rsMap.put("ERRMSG", "OK"); // count++;
		 * 
		 * // StringBuilder sb = new StringBuilder(); // for (int k = 0; k <
		 * shpokyList.size(); k++) { // if (k != 0) { // sb.append(","); // } //
		 * sb.append("'").append(shpokyList.get(k)).append("'"); // } //
		 * --------------------------------------------------------------
		 */
//			rsMap.put("SHPOKY", shpokyListParam);

//			if(head.size()-1 == i) {	//SHPOKY
		// 처리 완료 후 프로세스

		DataMap ifMap = new DataMap();
		List<DataMap> stknumInfoList = null;

		map.put("STATUS", "D");
		map.put("SHPOKY", shpokyListParam);
		map.put("WAREKY", map.getString(CommonConfig.SES_USER_WHAREHOUSE_KEY));
		map.setModuleCommand("DL042", "DL042_SHPDI_STKNUM_INFO");

		stknumInfoList = commonDao.getList(map);

		if (stknumInfoList.size() > 0) {

			ifMap.put("list", stknumInfoList);
			ifMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

			
			try {
				String resultMsg = iFService.send_TK_CONTROL(ifMap);

				if (!"SUCCESS".equals(resultMsg)) {
					rsMap.put("MSG", "SAP : " + resultMsg);
				} else {
					rsMap.put("MSG", resultMsg);
				}
				rsMap.put("SHPOKY", HeadGridSTKNUM); // 선택 된 선적번호 Row 유지
			} catch (Exception e) {
				throw new SQLException(e.getMessage());
			}
		}
//			}
//		}
		return rsMap;
	}

	@Transactional
	public int completeDL042(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("head");
		int count = 0;

		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

			commonDao.getMap("WmsOutbound.DL042_SHPDPROCALL", row);
			count++;
		}
		return count;
	}

	@Transactional
	public int deleteDL042_2(DataMap map) throws SQLException {
		List<DataMap> headList = map.getList("head");

		DataMap row;
		int count = 0;

		for (int i = 0; i < headList.size(); i++) {
			row = headList.get(i).getMap("map");

			// SHPDH SHPDI TASDR TASDI TASDH 도 삭제
			row.setModuleCommand("DL042", "DL042_SHPDI");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			commonDao.delete(row);

			row.setModuleCommand("DL042", "DL042_SHPDH");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			commonDao.delete(row);
			count++;
		}

		return count;
	}

	@Transactional
	public String closingDL042(DataMap map) throws SQLException {
		List<DataMap> headList = map.getList("head");

		// Validation
		StringBuffer sb = new StringBuffer();
		for (DataMap head : headList) {
			if (sb.length() > 0)
				sb.append(" UNION ALL\n ");

			sb.append(" SELECT '").append(head.getMap("map").get("SHPOKY")).append("' AS SHPOKY FROM DUAL ");
		}

		map.setModuleCommand("DL042", "DL042_CLOSING_SHIPMENT"); // 쿼리키값 넣을것
		map.put("APPEND_QUERY", sb.toString());
		List<DataMap> validList = commonDao.getValidation(map);
		if (validList.size() > 0) {
			DataMap valid = validList.get(0);
			return valid.getString("RESULTMSG");
		}

		int itemCount = 0;
		for (DataMap head : headList) {
			head.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			head.put("SHPOKY", head.getMap("map").get("SHPOKY"));

			commonDao.update("DL042.DL042_CLOSING_SHIPMENT", head);
		}
		return "OK";
	}

	@Transactional
	public String stockValidationDL042(DataMap map) throws SQLException {
		String result = "OK";

		StringBuffer sb = new StringBuffer();
		List<DataMap> itemList = map.getList("list");
		DataMap rowInfo = map.getMap("headData");

		for (DataMap item : itemList) {
			if (sb.length() > 0)
				sb.append(" UNION ALL\n ");

			sb.append(" SELECT '").append(item.getMap("map").get("WAREKY")).append("' AS WAREKY, ");
			sb.append("        '").append(item.getMap("map").get("LOTNUM")).append("' AS LOTNUM, ");
			sb.append("        '").append(item.getMap("map").get("LOCAKY")).append("' AS LOCAKY, ");
			sb.append("        '").append(item.getMap("map").get("TRNUID")).append("' AS TRNUID, ");
			sb.append("        '").append(item.getMap("map").get("PACKID")).append("' AS PACKID, ");
			sb.append("        '").append(item.getMap("map").get("TASKKY")).append("' AS TASKKY, ");
			sb.append("        '").append(item.getMap("map").get("TASKIT")).append("' AS TASKIT, ");
			sb.append("        '").append(item.getMap("map").get("QTSALO")).append("' AS QTTAOR FROM DUAL ");
		}

		map.setModuleCommand("DL042", "DL042_STOCK"); // 쿼리키값 넣을것
		map.put("APPEND_QUERY", sb.toString());

		List<DataMap> validList = commonDao.getValidation(map);
		if (validList.size() > 0) {
			DataMap valid = validList.get(0);
			return valid.getString("RESULTMSG");
		}

		//log.debug("DL04StockValidation ALSTKY ::" + rowInfo.get("ALSTKY"));

		// [2021.07.20] Modify junghyojin - 수정된 행 할당전략키에 해당하는지 체크하여 메세지 처리
		DataMap row = new DataMap();
		String rowState = "";
		StringBuffer sbupd = new StringBuffer();
		for (int j = 0; j < itemList.size(); j++) {
			row = itemList.get(j).getMap("map");
			rowState = row.getString(CommonConfig.GRID_ROW_STATE_ATT);
			//log.debug("DL04StockValidation QTSALO ::" + row.getMap("map").getInt("QTSALO"));
			if (rowState.charAt(0) == 'U' && row.getMap("map").getInt("QTSALO") > 0) { // 수량 변화 있음
				if (sbupd.length() > 0)
					sbupd.append(" UNION ALL\n ");

				sbupd.append(" SELECT '").append(row.getMap("map").get("WAREKY")).append("' AS WAREKY, ");
				sbupd.append("        '").append(row.getMap("map").get("LOTNUM")).append("' AS LOTNUM, ");
				sbupd.append("        '").append(row.getMap("map").get("LOCAKY")).append("' AS LOCAKY, ");
				sbupd.append("        '").append(row.getMap("map").get("TRNUID")).append("' AS TRNUID, ");
				sbupd.append("        '").append(row.getMap("map").get("PACKID")).append("' AS PACKID, ");
				sbupd.append("        '").append(rowInfo.get("ALSTKY")).append("' AS ALSTKY FROM DUAL ");
			}
		}

		map.setModuleCommand("DL042", "DL06_ALSTKY");
		map.put("APPEND_QUERY", sbupd.toString());
		List<DataMap> validAlstkyList = commonDao.getValidation(map);

		if (validAlstkyList.size() > 0) {
			DataMap valid = validAlstkyList.get(0);
			return valid.getString("RESULTMSG");
		}
		return result;
	}

	@Transactional
	public Object totalPickingCreateDL042(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("list");

		int count = 0;

		DataMap reqMap = new DataMap();

		// stknum 조회
		reqMap.setModuleCommand("DL042", "TOTAL_PICKING_STKNUM");

		Map<String, Object> paramMap = (Map<String, Object>) commonDao.getObject(reqMap);

		DataMap row;

		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));
			row.putAll(paramMap);

			reqMap.setModuleCommand("DL042", "TOTAL_PICKING_DUPLICATE_CHECK");
			reqMap.put("SHPOKY", row.get("SHPOKY")); // 선적번호로 출하오더번호
			String shpoky = (String) commonDao.getObject(reqMap);

			if (!isEmpty(shpoky)) {

				DataMap resMap = new DataMap();
				resMap.put("RESULT_VALUE", shpoky);

				return resMap;
			}

			commonDao.insert("DL042.TOTAL_PICKING_CREATE", row);
			count++;
		}

		return count;
	}

	@Transactional
	public Object totalPickingDeleteDL042(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("list");

		int count = 0;

		DataMap row;

		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

			commonDao.delete("WmsOutbound.TOTAL_PICKING_DELETE", row);
			count++;
		}

		return count;
	}

	/**
	 * 거래명세서(Trading statement) 출력
	 * 
	 * @throws SQLException
	 */
	@Transactional
	public DataMap printTSDL08(DataMap map) throws SQLException {
		String ifid = "WMS_IF_319";

		List<DataMap> list = map.getList("list");

		DataMap logMap = new DataMap();
		logMap.setModuleCommand("Erpif", "IFWMSLOG");
		map.clonSessionData(logMap);
		logMap.put("IFID", ifid);
		logMap.put("COMSTAT", "C"); // 호출
		commonDao.insert(logMap);

		String logSeq = logMap.getString("SEQNO");

		/* ITFMA에서 RFCFN, RFCTBL, RFCP1, RFCP2 조회 */
		DataMap rfcinfo = new DataMap();
		rfcinfo.setModuleCommand("Erpif", "RFCINFO");
		rfcinfo.put("IFID", ifid);
		DataMap rfcrst = commonDao.getMap(rfcinfo);

		String rfcfn = rfcrst.get("RFCFN").toString(); // FUNCTION
		String rfctbl = rfcrst.get("RFCTBL").toString(); // IF_RETBL
		String rfcp1 = rfcrst.get("RFCP1").toString(); // IF_RESTR
		String rfcp2 = rfcrst.get("RFCP2").toString(); // IF_RESTR

		DataMap pMap = new DataMap();
		pMap.put("I_IF_NAM", ifid);
		pMap.put("I_IF_KEY", logSeq);

		List<DataMap> sapList = new ArrayList<>();
		for (DataMap data : list) {
			data = data.getMap("map");
			DataMap sapData = new DataMap();

			String STLNUM = data.getString("STLNUM").trim();
			String STKNUM = data.getString("STKNUM").trim();
			String TKNUM = StringUtils.isNotEmpty(STLNUM) ? STLNUM : StringUtils.isNotEmpty(STKNUM) ? STKNUM : " ";
			String KUNNR = data.getString("DPTNKY");

			sapData.put("KUNNR", KUNNR); // 납품처코드
			sapData.put("TKNUM", TKNUM); // 선적번호

			boolean existCheck = false;
			for (DataMap exist : sapList) {
				if (StringUtils.equals(KUNNR, exist.getString("KUNNR"))
						&& StringUtils.equals(TKNUM, exist.getString("TKNUM"))) {
					existCheck = true;
					break;
				}
			}

			if (existCheck == false) {
				sapList.add(sapData);
			}
		}

		// SAP 호출
		RfcCallFunction sapConn = new RfcCallFunction();
		DataMap sapRes = sapConn.RfcCallTableDL08(rfcfn, rfctbl, rfcp1, pMap, sapList);

		List<DataMap> headList = sapRes.getList("head");
		List<DataMap> itemList = sapRes.getList("item");

		// 시퀀스넘버 가져옴
		Object DEALKY = commonDao.getObj("DL042_DEALKY", new DataMap());

		// SAP에서 받은 데이터 DB에 저장
		List<DataMap> mList = new ArrayList<>();
		for (int i = 0; i < headList.size(); i++) {
			DataMap head = headList.get(i);
			map.clonSessionData(head);
			head.put("DEALKY", DEALKY);
			head.put("LOGSEQNO", logSeq);
			mList.add(head);

			if (i != 0 && i % 100 == 0 || i == headList.size() - 1) {
				commonDao.insert("DL042_DEALH_MULTI", mList);
				mList.clear();
			}
		}

		mList = new ArrayList<>();
		for (int i = 0; i < itemList.size(); i++) {
			DataMap item = itemList.get(i);
			item.put("DEALKY", DEALKY);
			mList.add(item);

			if (i != 0 && i % 100 == 0 || i == itemList.size() - 1) {
				commonDao.insert("DL042_DEALI_MULTI", mList);
				mList.clear();
			}
		}

		DataMap res = new DataMap();
		res.put("DEALKY", DEALKY);
		return res;
	}

//	@Transactional
	public DataMap sendIf(DataMap map) throws SQLException {

		DataMap sendResult = new DataMap();

		String ifid = map.getString("IFID");

		try {
			String sendErrMsg = iFService.sendIF(map);

			if (sendErrMsg.equals("") || sendErrMsg.equals(" ") || sendErrMsg.equals(null)) {
				sendErrMsg = "I/F부터 받은 메세지가 없습니다.";
			}

			sendResult.put("sendMsg", sendErrMsg);

			//log.debug("InboundService.sendIF - ifid : " + ifid + " - Msg : " + sendErrMsg);
		} catch (Exception e) {
			log.error("InboundService.sendIF - ifid : " + ifid + " - Error : " + e.getMessage());
			e.printStackTrace();
		}

		return sendResult;
	}

	@Transactional
	public String getLOGSEQNO() throws SQLException {

		String LOGSEQNO = "";

		DataMap logoIdxMap = new DataMap();
		logoIdxMap.setModuleCommand("Erpif", "IFWMSLOGSEQNO");
		logoIdxMap = commonDao.getMap(logoIdxMap);

		LOGSEQNO = logoIdxMap.getString("LOGSEQNO");

		return LOGSEQNO;
	}

	@Transactional
	public DataMap prtUpdateDL042(DataMap map) throws SQLException {

		DataMap rsMap = new DataMap();

		List<DataMap> list = map.getList("list");
		String statdoList= map.getString("STATDO_LIST");

		String MSG = "OK";

		for (DataMap itemRow : list) {			
			DataMap row = itemRow.getMap("map");
			String prtchk = row.getString("PRTCHK");

			if (!prtchk.equals("V")) {
				
				if (statdoList != null && !"".equals(statdoList)) {
					row.put("statdoList", statdoList);
				}
				
				row.setModuleCommand("DL042", "DL042_PRTCHK_SHPDH");
				commonDao.update(row);
			}
		}

		rsMap.put("MSG", MSG);

		return rsMap;
	}

	/**
	* @methodName   DL042sendStknumInfo
	* @description  선적제한해제 로직 수행
	*/
	@Transactional(rollbackFor = Exception.class)
	public DataMap sendStknumInfoDL042(DataMap map) throws Exception {

		List<DataMap> head = map.getList("head");
		String MSG = "SUCCESS";

		DataMap rsMap = new DataMap();
		DataMap dmStknum = new DataMap(); // jhj : 마스터 출고오더번호,출하문서번호 기준을 -> 선적번호 기준으로 변경
		dmStknum.putAll(map);

		DataMap rowMap = new DataMap(); // 선택 된 마스터 선적 리스트
		StringJoiner sjStknum = new StringJoiner("','", "'", "'");
		for (int i = 0; i < head.size(); i++) {
			rowMap = head.get(i).getMap("map");

			sjStknum = sjStknum.add(rowMap.getString("STKNUM"));	// 1.선적번호 여러개로 연결
		}
		String stknumList = sjStknum.toString(); // 2.문자열 변경
		dmStknum.put("STKNUM", stknumList);
		
		List<DataMap> listMapShpoky = ConvertStknumToShpoky(dmStknum); // 3. 선적번호 여러개로 출하문서번호 전체 조회

		List<DataMap> stknumInfoList;
		String USER = map.getString(CommonConfig.SES_USER_ID_KEY);

		for (DataMap shpoky : listMapShpoky) {	//4. 출고
			DataMap ifMap = new DataMap();

			shpoky.setModuleCommand("DL042", "DL042_STKNUM_INFO");
			shpoky.put("STATUS", "D");

			stknumInfoList = commonDao.getList(shpoky);

			if (stknumInfoList.size() > 0) {

				ifMap.put("list", stknumInfoList);
				ifMap.put(CommonConfig.SES_USER_ID_KEY, USER);

				try {
					String resultMsg = iFService.send_TK_CONTROL(ifMap);
					if (!"SUCCESS".equals(resultMsg)) {
						MSG += "\nSAP : " + resultMsg;
					}
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}

		rsMap.put("MSG", MSG);

		return rsMap;
	}
	
	/**
	* @methodName   DL042callSapFunc
	* @description  SAP변경수신 호출
	*/
	@Transactional(rollbackFor = Exception.class)
	public DataMap callSapFuncDL042(DataMap map) throws Exception {
		List<DataMap> head = map.getList("head");
		String MSG = "SUCCESS";
		DataMap rsMap = new DataMap();
		DataMap dmStknum = new DataMap(); // jhj : 마스터 출고오더번호,출하문서번호 기준을 -> 선적번호 기준으로 변경
		dmStknum.putAll(map);

		DataMap rowMap = new DataMap(); // 선택 된 마스터 선적 리스트
		StringJoiner sjStknum = new StringJoiner("','", "'", "'");
		for (int i = 0; i < head.size(); i++) {
			rowMap = head.get(i).getMap("map");

			sjStknum = sjStknum.add(rowMap.getString("STKNUM")); // 1.선적번호 여러개로 연결
		}
		String stknumList = sjStknum.toString(); // 2.문자열 변경
		dmStknum.put("STKNUM", stknumList);

		List<DataMap> listMapShpoky = ConvertStknumToShpoky(dmStknum); // 3. 선적번호 여러개로 출하문서번호 전체 조회

		// Hub 프로시저 호출
		DataMap stknumInfoList;

		String USER = map.getString(CommonConfig.SES_USER_ID_KEY);

		for (DataMap reqRow : listMapShpoky) {
			DataMap ifMap = new DataMap();

			reqRow.setModuleCommand("Erpif", "WMS_IFS301_STATDO");

			commonDao.getMap(reqRow);

//			if (stknumInfoList.size() > 0) {
//
//				ifMap.put("list", stknumInfoList);
//				ifMap.put(CommonConfig.SES_USER_ID_KEY, USER);
//
//				try {
//
//					String resultMsg = iFService.send_TK_CONTROL(ifMap);
//
//					if (!"SUCCESS".equals(resultMsg)) {
//						MSG += "\nSAP : " + resultMsg;
//					}
//
//				} catch (Exception e) {
//					throw new SQLException(e.getMessage());
//				}
//			}
		}

		rsMap.put("MSG", MSG);

		return rsMap;
	}
	
	/*
	 * 수출출고(208) 출력 시 키오스크 접수이력 생성
	 * - 할당피킹지시서 / 출고지시서 출력 시 호출
	 * - PRTLG_KIOSK에 선적번호 단위로 등록
	 * - 동일 선적번호 + DOC_CD는 Mapper의 NOT EXISTS로 중복 방지
	 */
	@Transactional
	public DataMap insertExportKiosk(DataMap map) throws SQLException {

		DataMap rsMap = new DataMap();

		// PRTLG_KIOSK INSERT용 DataMap
		DataMap logMap = new DataMap();

		// 로그인 사용자 등 세션정보 복사
		map.clonSessionData(logMap);

		logMap.put("PRTSEQ", map.getString("PRTSEQ"));
		logMap.put("DOC_CD", map.getString("DOC_CD"));
		logMap.put("STKNUM", map.getString("STKNUM"));
		logMap.put("WAREKY", map.getString("WAREKY"));
		logMap.put("OWNRKY", map.getString("OWNRKY"));
		logMap.put("CREUSR", map.getString("SES_USER_ID"));

	
		logMap.setModuleCommand("WmsMobileOutboundPicking_COPY", "PRTLG_KIOSK");

		Object insertResultObj = commonDao.insert(logMap);

		int insertCnt = 0;

		if (insertResultObj instanceof Number) {
			insertCnt = ((Number) insertResultObj).intValue();
		} else if (insertResultObj != null && !"".equals(insertResultObj.toString())) {
			insertCnt = Integer.parseInt(insertResultObj.toString());
		}

		rsMap.put("MSG", "OK");
		rsMap.put("CNT", insertCnt);

		return rsMap;
	}
}
