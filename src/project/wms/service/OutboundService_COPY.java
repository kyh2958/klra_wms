package project.wms.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.sap.conn.jco.ext.DestinationDataProvider;

import project.common.airfw.PropertyUtil;
import project.common.airfw.RfcCallFunction;
import project.common.bean.CommonConfig;
import project.common.bean.DataMap;
import project.common.dao.CommonDAO;
import project.common.util.Util;

@Service
public class OutboundService_COPY {

	private static Logger log = Logger.getLogger(OutboundService_COPY.class);

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

	@Transactional
	public DataMap saveGr01(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");
		List<DataMap> selectList = new ArrayList<DataMap>();
		if (map.get("list") != null && !map.get("list").equals("")) {
			selectList = map.getList("list");
		}
		String headAsndky = map.get("headAsndky").toString();

		int count = 0;
		int itemCount = 0;

		DataMap row;
		DataMap itemRow;

		List docNumList = new ArrayList();
		DataMap rsMap = new DataMap();

		for (int i = 0; i < head.size(); i++) {
			if (head.get(i).getMap("map").getString("ASNDKY").equals(headAsndky)) {

				row = head.get(i).getMap("map");
				String docNum = wmsService.getDocSeq("112");
				docNumList.add(docNum);

				row.put("RECVKY", docNum);
				row.setModuleCommand("WmsInbound", "RECDH");
				row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

				commonDao.update(row);

				for (int k = 0; k < selectList.size(); k++) {
					itemRow = selectList.get(k).getMap("map");
					itemRow.put("RECVKY", docNum);
					itemRow.setModuleCommand("WmsInbound", "RECDI");

					itemCount += 10;
					String snum = String.valueOf(itemCount);
					String inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;

					itemRow.put("RECVIT", inum);

					count += commonDao.update(itemRow);
				}

			} else {
				row = head.get(i).getMap("map");
				String docNum = wmsService.getDocSeq("051");
				docNumList.add(docNum);

				row.put("RECVKY", docNum);
				row.setModuleCommand("WmsInbound", "RECDH");
				row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

				commonDao.update(row);

				row.setModuleCommand("WmsInbound", "GR01Sub");

				List<DataMap> list = commonDao.getList(row);

				for (int j = 0; j < list.size(); j++) {
					itemRow = list.get(j);
					itemRow.put("RECVKY", docNum);
					itemRow.setModuleCommand("WmsInbound", "RECDI");

					itemCount += 10;
					String snum = String.valueOf(itemCount);
					String inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;

					itemRow.put("RECVIT", inum);
					commonDao.update(itemRow);

				}
			}

			StringBuilder sb = new StringBuilder();
			for (int k = 0; k < docNumList.size(); k++) {
				if (k != 0) {
					sb.append(",");
				}
				sb.append("'").append(docNumList.get(k)).append("'");
			}
			rsMap.put("RECVKY", sb.toString());
		}
		return rsMap;
	}

	@Transactional
	public DataMap saveGr15(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");
		List<DataMap> selectList = new ArrayList<DataMap>();
		if (map.get("list") != null && !map.get("list").equals("")) {
			selectList = map.getList("list");
		}
		String headShpoky = map.get("headShpoky").toString();

		int count = 0;
		int itemCount = 0;

		DataMap row;
		DataMap itemRow;

		List docNumList = new ArrayList();
		DataMap rsMap = new DataMap();

		for (int i = 0; i < head.size(); i++) {
			if (head.get(i).getMap("map").getString("SHPOKY").equals(headShpoky)) {

				row = head.get(i).getMap("map");
				String docNum = wmsService.getDocSeq("166");
				docNumList.add(docNum);

				row.put("RECVKY", docNum);
				row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				row.setModuleCommand("WmsInbound", "GR15");

				commonDao.update(row);

				for (int k = 0; k < selectList.size(); k++) {
					itemRow = selectList.get(k).getMap("map");
					itemRow.put("RECVKY", docNum);
					itemRow.setModuleCommand("WmsInbound", "GR15Sub");

					itemCount += 10;
					String snum = String.valueOf(itemCount);
					String inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;

					itemRow.put("RECVIT", inum);

					count += commonDao.update(itemRow);
				}
			} else {
				row = head.get(i).getMap("map");

				String docNum = wmsService.getDocSeq("051");
				docNumList.add(docNum);

				row.setModuleCommand("WmsInbound", "GR15");
				row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				row.put("RECVKY", docNum);

				commonDao.update(row);

				row.setModuleCommand("WmsInbound", "GR15Sub");

				List<DataMap> list = commonDao.getList(row);

				for (int j = 0; j < list.size(); j++) {
					itemRow = list.get(j);
					itemRow.put("RECVKY", docNum);
					itemRow.setModuleCommand("WmsInbound", "GR15Sub");

					itemCount += 10;
					String snum = String.valueOf(itemCount);
					String inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;

					itemRow.put("RECVIT", inum);
					commonDao.update(itemRow);

				}

			}

			StringBuilder sb = new StringBuilder();
			for (int k = 0; k < docNumList.size(); k++) {
				if (k != 0) {
					sb.append(",");
				}
				sb.append("'").append(docNumList.get(k)).append("'");
			}
			map.setModuleCommand("WmsInbound", "GR15R");
			map.put("RECVKY", sb.toString());
			rsMap.put("RECVKY", sb.toString());
			// return count;
		}
		return rsMap;
	}

	@Transactional
	public String saveDL01ValueCheck(DataMap map) throws SQLException {
		String result = "OK";
		try {
			List<DataMap> list = map.getList("list");
			DataMap head = map.getMap("head").getMap("map");

			DataMap row;
			String VALIDATION_SQL = "";

			String validationSql = "";
			String key = map.get("key").toString();

			if (map.containsKey("key")) {
				validationSql = util.urlCreateValidationSql(map, key);
				map.put(CommonConfig.VALIDATION_SQL_KEY, validationSql);
				map.put("WAREKY", head.getString("WAREKY"));
				map.setModuleCommand("WmsOutbound_COPY", "SHPDH");

				List<DataMap> vList = commonDao.getValidation(map);
				//log.debug(":::::::::::" + vList.size());
				if (vList.size() > 0) {
					result = "OUT_M0140 VBELN";
				}
			}
		} catch (Exception e) {
			//log.debug("validataion error = " + e);
		}
		//log.debug(":::::::::::::::::::::" + result);
		return result;
	}

	@Transactional
	public int saveDl01(DataMap map) throws SQLException {
		DataMap head = map.getMap("head").getMap("map");
		List<DataMap> list = map.getList("list");

		if (head.getString("SHPMTY").toString().equals("266")) {
			head.put("PTRCVR", head.getString("DPTNKY"));
		}

		// ownrky 조회
		head.setModuleCommand("WmsOutbound_COPY", "OWNRKY");
		String ownrky = (String) commonDao.getObject(head);

		head.put("OWNRKY", ownrky);
		head.put("STATDO", "NEW");
		head.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
		head.setModuleCommand("WmsOutbound_COPY", "SHPDH");

		// merge 문
		commonDao.insert(head);

		int count = 0;
		DataMap row = new DataMap();
		int itemCount = 0;
		String snum;
		String inum;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put("OWNRKY", ownrky);
			row.setModuleCommand("WmsOutbound_COPY", "SHPDI");
			row.put("PROGID", "DL01");
			row.put("WAREKY", head.get("WAREKY"));
			row.put("SHPOKY", head.get("SHPOKY"));
			row.put("STATIT", "NEW");

			itemCount += 10;
			snum = String.valueOf(itemCount);
			inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
			row.put("SHPOIT", inum);

			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

			// merge 문
			commonDao.insert(row);

			if (!row.getString("SHPMTY").toString().equals("299")) {
				row.setModuleCommand("WmsOutbound_COPY", "IF113N00101");
				commonDao.update(row);
			}
			count++;
		}

		if (head.getString("SHPMTY").toString().equals("299")) {
			head.setModuleCommand("WmsOutbound_COPY", "IF113STKNUM");
			commonDao.update(head);
		}

		if (!head.getString("DRELIN").toString().equals("V")) {
			head.put("DRELIN", "V");
			head.setModuleCommand("WmsOutbound_COPY", "SHPDH");
			commonDao.update(head);

			if (head.getString("SHPMTY").toString().equals("299")) {
				head.setModuleCommand("WmsOutbound_COPY", "IF113PROC");
				DataMap procMap = commonDao.getMap(head);
			}

			/*
			 * head.setModuleCommand("WmsOutbound_COPY", "IF313PROC"); DataMap procSubMap =
			 * commonDao.getMap(head);
			 */
		}
		return count;
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
			map.setModuleCommand("WmsOutbound_COPY", "SHPDH");

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
	public int saveDL01AllChkeck(DataMap map) throws SQLException, Exception {
		List<DataMap> head = map.getList("head");

		int count = 0;
		DataMap row = new DataMap();
		DataMap itemRow;
		DataMap validationMap = new DataMap();
		String validationResult = "";
		int validationCount = 0;
		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");

			// ownrky 조회
			row.setModuleCommand("WmsOutbound_COPY", "OWNRKY");
			String ownrky = (String) commonDao.getObject(row);

			if (row.getString("SHPMTY").equals("266")) {
				row.put("PTRCVR", row.getString("DPTNKY"));
			}

			row.put("OWNRKY", ownrky);
			row.put("STATDO", "NEW");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.setModuleCommand("WmsOutbound_COPY", "SHPDH");

			// merge 문
			commonDao.insert(row);
			count++;

			row.setModuleCommand("WmsOutbound_COPY", "DL01");
			List<DataMap> itemList = commonDao.getList(row);

			int itemCount = 0;
			String snum;
			String inum;
			// item table validation
			for (int j = 0; j < itemList.size(); j++) {
				itemRow = new DataMap(itemList.get(j));
				validationMap.put("head", row);
				validationMap.put("list", itemList);
				validationMap.put("key", "SMANDT,SVBELN,SPOSNR,SKUKEY,QTSHPO");
				validationResult = itemValueCheck(validationMap);

				if (!validationResult.equals("OK")) {
					validationCount++;
					break;
				} else {
					itemRow.put("OWNRKY", ownrky);
					itemRow.setModuleCommand("WmsOutbound_COPY", "SHPDI");
					itemRow.put("PROGID", row.getString("PROGID"));
					itemRow.put("WAREKY", row.getString("WAREKY"));
					itemRow.put("SHPOKY", row.getString("SHPOKY"));
					itemRow.put("STATIT", "NEW");

					itemCount += 10;
					snum = String.valueOf(itemCount);
					inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
					itemRow.put("SHPOIT", inum);

					itemRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

					try {

						itemRow.put("QTSORG", itemRow.getString("QTSORG"));
						itemRow.put("QTSHPO", itemRow.getString("QTSHPO"));
						itemRow.put("QTYREF", itemRow.getString("QTYREF"));
						itemRow.put("QTUALO", itemRow.getString("QTUALO"));
						itemRow.put("QTAPPO", itemRow.getString("QTAPPO"));
						itemRow.put("QTALOC", itemRow.getString("QTALOC"));
						itemRow.put("QTJCMP", itemRow.getString("QTJCMP"));
						itemRow.put("QTSHPD", itemRow.getString("QTSHPD"));
						itemRow.put("QTSHPC", itemRow.getString("QTSHPC"));
						itemRow.put("QTYUOM", itemRow.getString("QTYUOM"));
						itemRow.put("QTPUOM", itemRow.getString("QTPUOM"));
						itemRow.put("QTDUOM", itemRow.getString("QTDUOM"));
						itemRow.put("GRSWGT", itemRow.getString("GRSWGT"));
						itemRow.put("NETWGT", itemRow.getString("NETWGT"));
						itemRow.put("LENGTH", itemRow.getString("LENGTH"));
						itemRow.put("WIDTHW", itemRow.getString("WIDTHW"));
						itemRow.put("HEIGHT", itemRow.getString("HEIGHT"));
						itemRow.put("CUBICM", itemRow.getString("CUBICM"));
						itemRow.put("CAPACT", itemRow.getString("CAPACT"));
						itemRow.put("LOTA16", itemRow.getString("LOTA16"));
						itemRow.put("LOTA17", itemRow.getString("LOTA17"));
						itemRow.put("LOTA18", itemRow.getString("LOTA18"));
						itemRow.put("LOTA19", itemRow.getString("LOTA19"));
						itemRow.put("LOTA20", itemRow.getString("LOTA20"));
						itemRow.put("AWMSNO", itemRow.getString("AWMSNO"));
						itemRow.put("WGTUNT", itemRow.getString("WGTUNT"));

						commonDao.insert(itemRow);
					} catch (Exception e) {
						//log.debug("error::::::::::::::::::" + e);
					}

					//log.debug("SHPMTY:::::::::::::::::::::" + row.getString("SHPMTY"));
					if (!row.getString("SHPMTY").toString().equals("299")) {
						itemRow.setModuleCommand("WmsOutbound_COPY", "IF113N00101");
						commonDao.update(itemRow);
					}
				}
			}

			// 헤더 정보 저장.
			if (validationResult.equals("OK")) {
				if (row.getString("SHPMTY").toString().equals("299")) {
					row.setModuleCommand("WmsOutbound_COPY", "IF113STKNUM");
					commonDao.update(row);
				}

				if (!row.getString("DRELIN").toString().equals("V")) {
					row.put("DRELIN", "V");
					row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
					commonDao.update(row);
				}
			}
		}

		if (validationCount > 0) {
			count = 0;
			throw new Exception();
		}

		return count;
	}

	@Transactional(rollbackFor = Exception.class)
	public int DL01IndexSelectCheckSave(DataMap map) throws SQLException, Exception {
		List<DataMap> head = map.getList("head");
		DataMap focusHead = map.getMap("focusHead").getMap("map");
		List<DataMap> list = map.getList("list");
		String rowHeadIdx = map.get("rowHeadIdx").toString();
		String selRowidx = map.get("arrSelRowIdx").toString();

		/**
		 * 선택된 헤드 정보와 아이탬 정보 저장
		 */
		if (focusHead.getString("SHPMTY").toString().equals("266")) {
			focusHead.put("PTRCVR", focusHead.getString("DPTNKY"));
		}

		// ownrky 조회
		focusHead.setModuleCommand("WmsOutbound_COPY", "OWNRKY");
		String ownrky = (String) commonDao.getObject(focusHead);

		focusHead.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
		focusHead.put("OWNRKY", ownrky);
		focusHead.put("STATDO", "NEW");
		focusHead.setModuleCommand("WmsOutbound_COPY", "SHPDH");

		// merge 문
		commonDao.insert(focusHead);

		int count = 0;
		DataMap row = new DataMap();
		int itemCount = 0;
		String snum;
		String inum;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put("OWNRKY", ownrky);
			row.setModuleCommand("WmsOutbound_COPY", "SHPDI");
			row.put("PROGID", "DL01");
			row.put("WAREKY", focusHead.get("WAREKY"));
			row.put("SHPOKY", focusHead.get("SHPOKY"));
			row.put("STATIT", "NEW");

			itemCount += 10;
			snum = String.valueOf(itemCount);
			inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
			row.put("SHPOIT", inum);
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

			// merge 문
			commonDao.insert(row);

			if (!row.getString("SHPMTY").toString().equals("299")) {
				row.setModuleCommand("WmsOutbound_COPY", "IF113N00101");
				commonDao.update(row);
			}
			count++;
		}

		if (focusHead.getString("SHPMTY").toString().equals("299")) {
			focusHead.setModuleCommand("WmsOutbound_COPY", "IF113STKNUM");
			commonDao.update(focusHead);
		}

		if (!focusHead.getString("DRELIN").toString().equals("V")) {
			focusHead.put("DRELIN", "V");
			focusHead.setModuleCommand("WmsOutbound_COPY", "SHPDH");
			commonDao.update(focusHead);
		}

		/**
		 * 체크된 헤드 정보 저장.(아이탬 정보 일괄 처리)
		 */
		String[] arrIdx = selRowidx.split(",");
		DataMap itemRow;
		DataMap validationMap = new DataMap();
		String validationResult = "";
		int validationCount = 0;

		for (int i = 0; i < arrIdx.length; i++) {
			// 체크된 row의 index와 선택된 row index가 동일함.
			if (!arrIdx[i].equals(rowHeadIdx)) {
				row = head.get(i).getMap("map");
				if (row.getString("SHPMTY").equals("266")) {
					row.put("PTRCVR", row.getString("DPTNKY"));
				}

				try {
					row.put("OWNRKY", ownrky);
					row.put("STATDO", "NEW");
					row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
					row.setModuleCommand("WmsOutbound_COPY", "SHPDH");

					// merge 문
					commonDao.insert(row);
				} catch (Exception e) {
					//log.debug("error:::::::::::::::::::::" + e);
				}
				count++;
				row.setModuleCommand("WmsOutbound_COPY", "DL01");
				List<DataMap> itemList = commonDao.getList(row);

				itemCount = 0;
				// item table validation
				for (int j = 0; j < itemList.size(); j++) {
					itemRow = new DataMap(itemList.get(j));
					validationMap.put("head", row);
					validationMap.put("list", itemList);
					validationMap.put("key", "SMANDT,SVBELN,SPOSNR,SKUKEY,QTSHPO");
					validationResult = itemValueCheck(validationMap);

					if (!validationResult.equals("OK")) {
						validationCount++;
						break;
					} else {
						itemRow.put("OWNRKY", ownrky);
						itemRow.setModuleCommand("WmsOutbound_COPY", "SHPDI");
						itemRow.put("PROGID", row.getString("PROGID"));
						itemRow.put("WAREKY", row.getString("WAREKY"));
						itemRow.put("SHPOKY", row.getString("SHPOKY"));
						itemRow.put("STATIT", "NEW");

						itemCount += 10;
						snum = String.valueOf(itemCount);
						inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
						itemRow.put("SHPOIT", inum);

						itemRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

						// merge 문
						try {

							itemRow.put("QTSORG", itemRow.getString("QTSORG"));
							itemRow.put("QTSHPO", itemRow.getString("QTSHPO"));
							itemRow.put("QTYREF", itemRow.getString("QTYREF"));
							itemRow.put("QTUALO", itemRow.getString("QTUALO"));
							itemRow.put("QTAPPO", itemRow.getString("QTAPPO"));
							itemRow.put("QTALOC", itemRow.getString("QTALOC"));
							itemRow.put("QTJCMP", itemRow.getString("QTJCMP"));
							itemRow.put("QTSHPD", itemRow.getString("QTSHPD"));
							itemRow.put("QTSHPC", itemRow.getString("QTSHPC"));
							itemRow.put("QTYUOM", itemRow.getString("QTYUOM"));
							itemRow.put("QTPUOM", itemRow.getString("QTPUOM"));
							itemRow.put("QTDUOM", itemRow.getString("QTDUOM"));
							itemRow.put("GRSWGT", itemRow.getString("GRSWGT"));
							itemRow.put("NETWGT", itemRow.getString("NETWGT"));
							itemRow.put("LENGTH", itemRow.getString("LENGTH"));
							itemRow.put("WIDTHW", itemRow.getString("WIDTHW"));
							itemRow.put("HEIGHT", itemRow.getString("HEIGHT"));
							itemRow.put("CUBICM", itemRow.getString("CUBICM"));
							itemRow.put("CAPACT", itemRow.getString("CAPACT"));
							itemRow.put("LOTA16", itemRow.getString("LOTA16"));
							itemRow.put("LOTA17", itemRow.getString("LOTA17"));
							itemRow.put("LOTA18", itemRow.getString("LOTA18"));
							itemRow.put("LOTA19", itemRow.getString("LOTA19"));
							itemRow.put("LOTA20", itemRow.getString("LOTA20"));
							itemRow.put("AWMSNO", itemRow.getString("AWMSNO"));
							itemRow.put("WGTUNT", itemRow.getString("WGTUNT"));

							commonDao.insert(itemRow);
						} catch (Exception e) {
							//log.debug("error::::::::::::::::::" + e);
						}

						//log.debug("SHPMTY:::::::::::::::::::::" + row.getString("SHPMTY"));
						if (!row.getString("SHPMTY").toString().equals("299")) {
							itemRow.setModuleCommand("WmsOutbound_COPY", "IF113N00101");
							commonDao.update(itemRow);
						}
					}
				}
				count++;
				// 헤더 정보 저장.
				if (validationResult.equals("OK")) {
					if (row.getString("SHPMTY").toString().equals("299")) {
						row.setModuleCommand("WmsOutbound_COPY", "IF113STKNUM");
						commonDao.update(row);
					}

					if (!row.getString("DRELIN").toString().equals("V")) {
						row.put("DRELIN", "V");
						row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
						commonDao.update(row);
					}
				}
			}
		}

		if (validationCount > 0) {
			count = 0;
			throw new Exception();
		}

		return count;
	}

	@Transactional
	public int assignmentDl01(DataMap map) throws SQLException {
		DataMap head = map.getMap("head").getMap("map");
		List<DataMap> list = map.getList("list");

		if (head.getString("SHPMTY").toString().equals("266")) {
			head.put("PTRCVR", head.getString("DPTNKY"));
		}

		// ownrky 조회
		head.setModuleCommand("WmsOutbound_COPY", "OWNRKY");
		String ownrky = (String) commonDao.getObject(head);

		head.put("OWNRKY", ownrky);
		head.put("STATDO", "NEW");
		head.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
		head.setModuleCommand("WmsOutbound_COPY", "SHPDH");

		// merge 문
		commonDao.insert(head);

		int count = 0;
		DataMap row = new DataMap();
		int itemCount = 0;
		String snum;
		String inum;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put("OWNRKY", ownrky);
			row.setModuleCommand("WmsOutbound_COPY", "SHPDI");
			row.put("PROGID", "DL01");
			row.put("WAREKY", head.get("WAREKY"));
			row.put("SHPOKY", head.get("SHPOKY"));
			row.put("STATIT", "NEW");

			itemCount += 10;
			snum = String.valueOf(itemCount);
			inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
			row.put("SHPOIT", inum);
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

			try {
				commonDao.insert(row);
			} catch (Exception e) {
				//log.debug("error :::" + e);
			}
			if (!row.getString("SHPMTY").toString().equals("299")) {
				row.setModuleCommand("WmsOutbound_COPY", "IF113N00101");
				commonDao.update(row);
			}
			count++;
		}

		if (head.getString("SHPMTY").toString().equals("299")) {
			head.setModuleCommand("WmsOutbound_COPY", "IF113STKNUM");
			commonDao.update(head);
		}

		if (!head.getString("DRELIN").toString().equals("V")) {
			head.put("DRELIN", "V");
			head.setModuleCommand("WmsOutbound_COPY", "SHPDH");
			commonDao.update(head);

			if (head.getString("SHPMTY").toString().equals("299")) {
				head.setModuleCommand("WmsOutbound_COPY", "IF113PROC");
				DataMap procMap = commonDao.getMap(head);
			}

			/*
			 * head.setModuleCommand("WmsOutbound_COPY", "IF313PROC"); DataMap procSubMap =
			 * commonDao.getMap(head);
			 */
		}

		try {
			head.setModuleCommand("WmsOutbound_COPY", "BATCHALLOCATION");
			DataMap procSubMap = commonDao.getMap(head);
		} catch (Exception e) {
			//log.debug("proc error :::" + e);
		}

		return count;
	}

	@Transactional
	public int assignmentAllDl01(DataMap map) throws SQLException, Exception {
		List<DataMap> head = map.getList("head");

		int count = 0;
		DataMap row = new DataMap();
		DataMap itemRow;
		DataMap validationMap = new DataMap();
		String validationResult = "";
		int validationCount = 0;
		DataMap procMap;
		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");

			// ownrky 조회
			row.setModuleCommand("WmsOutbound_COPY", "OWNRKY");
			String ownrky = (String) commonDao.getObject(row);

			if (row.getString("SHPMTY").equals("266")) {
				row.put("PTRCVR", row.getString("DPTNKY"));
			}

			row.put("OWNRKY", ownrky);
			row.put("STATDO", "NEW");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.setModuleCommand("WmsOutbound_COPY", "SHPDH");

			commonDao.insert(row);
			count++;

			row.setModuleCommand("WmsOutbound_COPY", "DL01");
			List<DataMap> itemList = commonDao.getList(row);

			int itemCount = 0;
			String snum;
			String inum;
			// item table validation
			for (int j = 0; j < itemList.size(); j++) {
				itemRow = new DataMap(itemList.get(j));
				validationMap.put("head", row);
				validationMap.put("list", itemList);
				validationMap.put("key", "SMANDT,SVBELN,SPOSNR,SKUKEY,QTSHPO");
				validationResult = itemValueCheck(validationMap);

				if (!validationResult.equals("OK")) {
					validationCount++;
					break;
				} else {
					itemRow.put("OWNRKY", ownrky);
					itemRow.setModuleCommand("WmsOutbound_COPY", "SHPDI");
					itemRow.put("PROGID", row.getString("PROGID"));
					itemRow.put("WAREKY", row.getString("WAREKY"));
					itemRow.put("SHPOKY", row.getString("SHPOKY"));
					itemRow.put("STATIT", "NEW");

					itemCount += 10;
					snum = String.valueOf(itemCount);
					inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
					itemRow.put("SHPOIT", inum);
					itemRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

					try {
						itemRow.put("QTSORG", itemRow.getString("QTSORG"));
						itemRow.put("QTSHPO", itemRow.getString("QTSHPO"));
						itemRow.put("QTYREF", itemRow.getString("QTYREF"));
						itemRow.put("QTUALO", itemRow.getString("QTUALO"));
						itemRow.put("QTAPPO", itemRow.getString("QTAPPO"));
						itemRow.put("QTALOC", itemRow.getString("QTALOC"));
						itemRow.put("QTJCMP", itemRow.getString("QTJCMP"));
						itemRow.put("QTSHPD", itemRow.getString("QTSHPD"));
						itemRow.put("QTSHPC", itemRow.getString("QTSHPC"));
						itemRow.put("QTYUOM", itemRow.getString("QTYUOM"));
						itemRow.put("QTPUOM", itemRow.getString("QTPUOM"));
						itemRow.put("QTDUOM", itemRow.getString("QTDUOM"));
						itemRow.put("GRSWGT", itemRow.getString("GRSWGT"));
						itemRow.put("NETWGT", itemRow.getString("NETWGT"));
						itemRow.put("LENGTH", itemRow.getString("LENGTH"));
						itemRow.put("WIDTHW", itemRow.getString("WIDTHW"));
						itemRow.put("HEIGHT", itemRow.getString("HEIGHT"));
						itemRow.put("CUBICM", itemRow.getString("CUBICM"));
						itemRow.put("CAPACT", itemRow.getString("CAPACT"));
						itemRow.put("LOTA16", itemRow.getString("LOTA16"));
						itemRow.put("LOTA17", itemRow.getString("LOTA17"));
						itemRow.put("LOTA18", itemRow.getString("LOTA18"));
						itemRow.put("LOTA19", itemRow.getString("LOTA19"));
						itemRow.put("LOTA20", itemRow.getString("LOTA20"));
						itemRow.put("AWMSNO", itemRow.getString("AWMSNO"));
						itemRow.put("WGTUNT", itemRow.getString("WGTUNT"));

						commonDao.insert(itemRow);
					} catch (Exception e) {
						//log.debug("error::::::::::::::::::" + e);
					}

					if (!row.getString("SHPMTY").toString().equals("299")) {
						itemRow.setModuleCommand("WmsOutbound_COPY", "IF113N00101");
						commonDao.update(itemRow);
					}
				}
			}

			// 헤더 정보 저장.
			if (validationResult.equals("OK")) {
				if (row.getString("SHPMTY").toString().equals("299")) {
					row.setModuleCommand("WmsOutbound_COPY", "IF113STKNUM");
					commonDao.update(row);
				}

				if (!row.getString("DRELIN").toString().equals("V")) {
					row.put("DRELIN", "V");
					row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
					commonDao.update(row);
				}

				row.setModuleCommand("WmsOutbound_COPY", "BATCHALLOCATION");
				procMap = commonDao.getMap(row);
			}
		}

		if (validationCount > 0) {
			count = 0;
			throw new Exception();
		}

		return count;
	}

	@Transactional
	public int DL01IndexSelectCheckAssignment(DataMap map) throws SQLException, Exception {
		List<DataMap> head = map.getList("head");
		DataMap focusHead = map.getMap("focusHead").getMap("map");
		List<DataMap> list = map.getList("list");
		String rowHeadIdx = map.get("rowHeadIdx").toString();
		String selRowidx = map.get("arrSelRowIdx").toString();

		/**
		 * 선택된 헤드 정보와 아이탬 정보 저장
		 */
		if (focusHead.getString("SHPMTY").toString().equals("266")) {
			focusHead.put("PTRCVR", focusHead.getString("DPTNKY"));
		}

		// ownrky 조회
		focusHead.setModuleCommand("WmsOutbound_COPY", "OWNRKY");
		String ownrky = (String) commonDao.getObject(focusHead);

		focusHead.put("OWNRKY", ownrky);
		focusHead.put("STATDO", "NEW");
		focusHead.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
		focusHead.setModuleCommand("WmsOutbound_COPY", "SHPDH");

		commonDao.insert(focusHead);

		int count = 0;
		DataMap row = new DataMap();
		int itemCount = 0;
		String snum;
		String inum;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put("OWNRKY", ownrky);
			row.setModuleCommand("WmsOutbound_COPY", "SHPDI");
			row.put("PROGID", "DL01");
			row.put("WAREKY", focusHead.get("WAREKY"));
			row.put("SHPOKY", focusHead.get("SHPOKY"));
			row.put("STATIT", "NEW");

			itemCount += 10;
			snum = String.valueOf(itemCount);
			inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
			row.put("SHPOIT", inum);
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

			commonDao.insert(row);

			if (!row.getString("SHPMTY").toString().equals("299")) {
				row.setModuleCommand("WmsOutbound_COPY", "IF113N00101");
				commonDao.update(row);
			}
			count++;
		}

		if (focusHead.getString("SHPMTY").toString().equals("299")) {
			focusHead.setModuleCommand("WmsOutbound_COPY", "IF113STKNUM");
			commonDao.update(focusHead);
		}

		if (!focusHead.getString("DRELIN").toString().equals("V")) {
			focusHead.put("DRELIN", "V");
			focusHead.setModuleCommand("WmsOutbound_COPY", "SHPDH");
			commonDao.update(focusHead);
		}

		row.setModuleCommand("WmsOutbound_COPY", "BATCHALLOCATION");
		DataMap procSubMap = commonDao.getMap(row);

		/**
		 * 체크된 헤드 정보 저장.(아이탬 정보 일괄 처리)
		 */
		String[] arrIdx = selRowidx.split(",");
		DataMap itemRow;
		DataMap validationMap = new DataMap();
		String validationResult = "";
		int validationCount = 0;

		for (int i = 0; i < arrIdx.length; i++) {
			// 체크된 row의 index와 선택된 row index가 동일함.
			if (!arrIdx[i].equals(rowHeadIdx)) {
				row = head.get(i).getMap("map");
				if (row.getString("SHPMTY").equals("266")) {
					row.put("PTRCVR", row.getString("DPTNKY"));
				}

				try {
					row.put("OWNRKY", ownrky);
					row.put("STATDO", "NEW");
					row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
					row.setModuleCommand("WmsOutbound_COPY", "SHPDH");

					commonDao.insert(row);
				} catch (Exception e) {
					//log.debug("error:::::::::::::::::::::" + e);
				}
				count++;

				row.setModuleCommand("WmsOutbound_COPY", "DL01");
				List<DataMap> itemList = commonDao.getList(row);

				itemCount = 0;
				// item table validation
				for (int j = 0; j < itemList.size(); j++) {
					itemRow = new DataMap(itemList.get(j));
					validationMap.put("head", row);
					validationMap.put("list", itemList);
					validationMap.put("key", "SMANDT,SVBELN,SPOSNR,SKUKEY,QTSHPO");
					validationResult = itemValueCheck(validationMap);

					if (!validationResult.equals("OK")) {
						validationCount++;
						break;
					} else {
						itemRow.put("OWNRKY", ownrky);
						itemRow.setModuleCommand("WmsOutbound_COPY", "SHPDI");
						itemRow.put("PROGID", row.getString("PROGID"));
						itemRow.put("WAREKY", row.getString("WAREKY"));
						itemRow.put("SHPOKY", row.getString("SHPOKY"));
						itemRow.put("STATIT", "NEW");

						itemCount += 10;
						snum = String.valueOf(itemCount);
						inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
						itemRow.put("SHPOIT", inum);
						itemRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

						try {
							itemRow.put("QTSORG", itemRow.getString("QTSORG"));
							itemRow.put("QTSHPO", itemRow.getString("QTSHPO"));
							itemRow.put("QTYREF", itemRow.getString("QTYREF"));
							itemRow.put("QTUALO", itemRow.getString("QTUALO"));
							itemRow.put("QTAPPO", itemRow.getString("QTAPPO"));
							itemRow.put("QTALOC", itemRow.getString("QTALOC"));
							itemRow.put("QTJCMP", itemRow.getString("QTJCMP"));
							itemRow.put("QTSHPD", itemRow.getString("QTSHPD"));
							itemRow.put("QTSHPC", itemRow.getString("QTSHPC"));
							itemRow.put("QTYUOM", itemRow.getString("QTYUOM"));
							itemRow.put("QTPUOM", itemRow.getString("QTPUOM"));
							itemRow.put("QTDUOM", itemRow.getString("QTDUOM"));
							itemRow.put("GRSWGT", itemRow.getString("GRSWGT"));
							itemRow.put("NETWGT", itemRow.getString("NETWGT"));
							itemRow.put("LENGTH", itemRow.getString("LENGTH"));
							itemRow.put("WIDTHW", itemRow.getString("WIDTHW"));
							itemRow.put("HEIGHT", itemRow.getString("HEIGHT"));
							itemRow.put("CUBICM", itemRow.getString("CUBICM"));
							itemRow.put("CAPACT", itemRow.getString("CAPACT"));
							itemRow.put("LOTA16", itemRow.getString("LOTA16"));
							itemRow.put("LOTA17", itemRow.getString("LOTA17"));
							itemRow.put("LOTA18", itemRow.getString("LOTA18"));
							itemRow.put("LOTA19", itemRow.getString("LOTA19"));
							itemRow.put("LOTA20", itemRow.getString("LOTA20"));
							itemRow.put("AWMSNO", itemRow.getString("AWMSNO"));
							itemRow.put("WGTUNT", itemRow.getString("WGTUNT"));

							commonDao.insert(itemRow);
						} catch (Exception e) {
							//log.debug("error::::::::::::::::::" + e);
						}

						if (!row.getString("SHPMTY").toString().equals("299")) {
							itemRow.setModuleCommand("WmsOutbound_COPY", "IF113N00101");
							commonDao.update(itemRow);
						}
					}
				}
				count++;
				// 헤더 정보 저장.
				if (validationResult.equals("OK")) {
					if (row.getString("SHPMTY").toString().equals("299")) {
						row.setModuleCommand("WmsOutbound_COPY", "IF113STKNUM");
						commonDao.update(row);
					}

					if (!row.getString("DRELIN").toString().equals("V")) {
						row.put("DRELIN", "V");
						row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
						commonDao.update(row);
					}

					row.setModuleCommand("WmsOutbound_COPY", "BATCHALLOCATION");
					procSubMap = commonDao.getMap(row);
				}
			}
		}

		if (validationCount > 0) {
			count = 0;
			throw new Exception();
		}

		return count;
	}

	@Transactional
	public int DL01OrderDelete(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");

		int count = 0;
		DataMap row;
		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.setModuleCommand("WmsOutbound_COPY", "BATCHALLOCATION");
			DataMap procSubMap = commonDao.getMap(row);
			count++;
		}

		return count;
	}

	@Transactional
	public List DL01Barcode(DataMap map) throws SQLException {
		Map head = map.getMap("head").getMap("map");
		List<DataMap> list = map.getList("list");
		List dataList = new ArrayList();

		map.setModuleCommand("WmsOutbound_COPY", "BARCODREFDKY");
		DataMap row;

		int refdky = -1;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");

			if (row.getInt("QTSHPO") > 0) {
				row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

				int count = row.getInt("QTSHPO");

				if (count > 0) {
					if (refdky == -1) {
						refdky = (Integer) commonDao.getObject(map);
					}
					row.setModuleCommand("WmsOutbound_COPY", "BARCOD");
					row.put("REFDKY", refdky);
					row.put("WAREKY", head.get("WAREKY"));

					if (row.getString("BATMNG").equals("V")) {
						for (int j = 0; j < count; j++) {
							row.put("QTSHPO", "1");
							commonDao.insert(row);
						}
					} else {
						commonDao.insert(row);
					}

					row.setModuleCommand("WmsOutbound_COPY", "BARCD");

					List tmpList = commonDao.getList(row);

					dataList.addAll(tmpList);
				}
			}
		}

		return dataList;
	}

	@Transactional
	public int DL04Assignment(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");
		List<DataMap> list = map.getList("list");

		int count = 0;
		DataMap row;
		DataMap procMap;
		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");
			row.setModuleCommand("WmsOutbound_COPY", "BATCHALLOCATION");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

			procMap = commonDao.getMap(row);

			count++;
		}

		return count;
	}

	@Transactional
	public int DL04Delete(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");

		int count = 0;
		DataMap row = new DataMap();
		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.setModuleCommand("WmsOutbound_COPY", "ALLOCATIONCANCLE");
			commonDao.update(row);

			row.setModuleCommand("WmsOutbound_COPY", "ALLOCATIONCANCLE");
			commonDao.update(row);
			/*
			 * row.setModuleCommand("WmsOutbound_COPY", "IF313PROC"); DataMap procSubMap =
			 * commonDao.getMap(row);
			 * 
			 * if(row.getString("SHPMTY").equals("299")){
			 * row.setModuleCommand("WmsOutbound_COPY", "PDELSHPREF"); DataMap delShpMap =
			 * commonDao.getMap(row); }
			 */
			row.setModuleCommand("WmsOutbound_COPY", "REMOVETASKKY");
			String taskky = (String) commonDao.getObject(row);
			if (taskky != null && !taskky.equals("")) {
				row.setModuleCommand("WmsOutbound_COPY", "REMOVETASDI");
				row.put("TASKKY", taskky);
				commonDao.delete(row);

				row.setModuleCommand("WmsOutbound_COPY", "REMOVETASDH");
				commonDao.delete(row);
			}

			if (row.getString("SHPMTY").equals("299")) {

			} else {
				row.setModuleCommand("WmsOutbound_COPY", "TKNUMQNTSUB");
				commonDao.update(row);
			}

			row.setModuleCommand("WmsOutbound_COPY", "SHPDI");
			commonDao.delete(row);

			row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
			commonDao.delete(row);

			count++;
		}

		return count;
	}

	@Transactional
	public int DL04orderDelete(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");

		int count = 0;
		DataMap row = new DataMap();
		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

			row.setModuleCommand("WmsOutbound_COPY", "REMOVETASKKY");
			String taskky = (String) commonDao.getObject(row);
			if (taskky != null && !taskky.equals("")) {
				row.setModuleCommand("WmsOutbound_COPY", "REMOVETASDI");
				row.put("TASKKY", taskky);
				commonDao.delete(row);

				row.setModuleCommand("WmsOutbound_COPY", "REMOVETASDH");
				commonDao.delete(row);
			}

			count++;
		}

		return count;
	}

	@Transactional
	public int DL04POPSave(DataMap map) throws SQLException {

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
		map.setModuleCommand("WmsOutbound_COPY", "SEARCHSHPDH");
		DataMap shpdhMap = commonDao.getMap(map);
		map.setModuleCommand("WmsOutbound_COPY", "SEARCHSHPDI");
		DataMap shpdiMap = commonDao.getMap(map);
		String svbeln = shpdiMap.getString("SVBELN");
		String sposnr = shpdiMap.getString("SPOSNR");
		String stknum = shpdiMap.getString("STKNUM");
		String stpnum = shpdiMap.getString("STPNUM");
		String ssornu = shpdiMap.getString("SSORNU");
		String ssorit = shpdiMap.getString("SSORIT");

		map.setModuleCommand("WmsOutbound_COPY", "SEARCHTASDIALL");
		List<DataMap> tasdiList = commonDao.getList(map);
		// 2. 기존의 TASDI ITEM NUMBER MAX 값 조회
		map.setModuleCommand("WmsOutbound_COPY", "SEARCHMAXTASDH");
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
					map.setModuleCommand("WmsOutbound_COPY", "DL04POPREMOVE");
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
				map.setModuleCommand("WmsOutbound_COPY", "MANUALALLOCATE_AVAILABLE_STOCK");
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
				map.setModuleCommand("WmsOutbound_COPY", "TASDI");
				commonDao.insert(map);

				map.setModuleCommand("WmsOutbound_COPY", "TASDR_PROCALL");
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
	public int TM04Delete(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("head");

		int count = 0;
		DataMap row = new DataMap();
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

			row.setModuleCommand("WmsOutbound_COPY", "TM04IFWMS");
			commonDao.delete(row);

			count++;
		}

		return count;
	}

	@Transactional
	public String validatePikingSave(DataMap map) throws SQLException {
		String result = "OK";

		int headModifyIndex = -1;
		List<DataMap> itemList = null;
		if (map.containsKey("headModifyIndex")) {
			headModifyIndex = map.getInt("headModifyIndex");
			itemList = map.getList("list");
		}

		List<DataMap> headList = map.getList("headList");

		DataMap row;
		DataMap rsMap;

		List<DataMap> paramItemList = new ArrayList<DataMap>();
		DataMap copy = new DataMap();
		DataMap paramItemMap = new DataMap();
		DataMap paramLocMap = new DataMap();

		for (int i = 0; i < headList.size(); i++) {
			if (map.containsKey("param")) {
				row = new DataMap(map.getMap("param").getMap("map"));
			} else {
				row = new DataMap();
			}

			map.clonSessionData(row);
			row.append(headList.get(i).getMap("map"));
			paramItemList = new ArrayList<DataMap>();

			if (headModifyIndex == i) {
				for (int j = 0; j < itemList.size(); j++) {
					copy = new DataMap(itemList.get(j).getMap("map"));
					paramItemList.add(copy);
				}
			} else {
				row.setModuleCommand("WmsTask_COPY", "TASDI");
				row.put("QTY_COPY", "Y");
				List<DataMap> tasdiList = commonDao.getList(row);
				for (int j = 0; j < tasdiList.size(); j++) {
					copy = new DataMap(tasdiList.get(j));
					paramItemList.add(copy);
				}
			}

			// 2012.10.27 by GOKU - 오더컨펌이 되지 않은 Shipment의 피킹 완료는 할 수 없다.
			/*
			 * if("210".equals(row.getString("TASOTY")) ||
			 * "208".equals(row.getString("TASOTY")) ){ row.setModuleCommand("WmsTask_COPY",
			 * "SHPDHDRELIN"); rsMap = commonDao.getMap(row);
			 * 
			 * if(!rsMap.get("DRELIN").toString().equals("V")){ result = "TASK_M0034 "+
			 * row.getString("TASKKY"); return result; } }
			 */

			for (int j = 0; j < paramItemList.size(); j++) {
				paramItemMap = paramItemList.get(j);
				map.clonSessionData(paramItemMap);

				// 이미 confirm 되었는지 검사
				if (!"00000000".equals(paramItemMap.getString("ACTCDT"))
						&& !"".equals(paramItemMap.getString("ACTCDT"))) {
					result = "TASK_M0009 " + row.getString("TASKKY");
					return result;
				}

				// 출발지번이 자동창고면 에러
				if ("V".equals(paramItemMap.getString("AUTLOC"))) {
					result = "TASK_M0018 " + row.getString("LOCAAC");
					return result;
				}

				// Location 검사LocationPK locPK = new LocationPK();
				paramLocMap = new DataMap();
				paramLocMap.put("WAREKY", "330".equals(row.getString("330")) ? row.getString("WARETG")
						: paramItemMap.getString("WAREKY"));
				paramLocMap.put("LOCAKY", paramItemMap.getString("LOCAAC"));
				paramLocMap.setModuleCommand("WmsAdmin", "LOCMA");

				rsMap = commonDao.getMap(paramLocMap);

				if (rsMap == null) {
					result = "MASTER_M0507 " + row.getString("LOCAAC");
					return result;
				}

				// 도착지번이 자동창고면 에러
				if ("V".equals(rsMap.getString("AUTLOC"))) {
					result = "TASK_M0024 " + row.getString("LOCAAC");
					return result;
				}

				if ("310".equals(row.getString("TASOTY")) && ("SETLOC".equals(paramItemMap.getString("LOCACC"))
						|| "RTNLOC".equals(paramItemMap.getString("LOCACC"))
						|| "SCRLOC".equals(paramItemMap.getString("LOCACC")))) {
					result = "TASK_M0035 " + row.getString("LOCAAC");
					return result;
				}

			}

		}

		return result;
	}

	@Transactional
	public DataMap DL06MAXLOTA11(DataMap map) throws SQLException {
		DataMap rsMap;

		map.setModuleCommand("WmsTask_COPY", "DL06MAXLOTA11"); // 쿼리키값 넣을것

		rsMap = commonDao.getMap(map);

		return rsMap;
	}

	@Transactional
	public String DL06StockValidation(DataMap map) throws SQLException {
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
			sb.append("        '").append(rowInfo.get("TASKKY")).append("' AS TASKKY, ");
			sb.append("        '").append(rowInfo.get("TASKIT")).append("' AS TASKIT, ");
			sb.append("        '").append(item.getMap("map").get("QTSPUT")).append("' AS QTTAOR FROM DUAL ");
		}

		map.setModuleCommand("WmsOutbound_COPY", "DL06_STOCK"); // 쿼리키값 넣을것
		map.put("APPEND_QUERY", sb.toString());

		List<DataMap> validList = commonDao.getValidation(map);
		if (validList.size() > 0) {
			DataMap valid = validList.get(0);
			return valid.getString("RESULTMSG");
		}

		// [2021.07.08] Modify junghyojin - 수정된 행 할당전략키에 해당하는지 체크하여 메세지 처리
		DataMap row = new DataMap();
		String rowState = "";
		StringBuffer sbupd = new StringBuffer();
		for (int j = 0; j < itemList.size(); j++) {
			row = itemList.get(j).getMap("map");
			rowState = row.getString(CommonConfig.GRID_ROW_STATE_ATT);
			if (rowState.charAt(0) == 'U' && row.getMap("map").getInt("QTSPUT") > 0) { // 수량 변화 있음
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

		map.setModuleCommand("WmsOutbound_COPY", "DL06_ALSTKY");
		map.put("APPEND_QUERY", sbupd.toString());
		List<DataMap> validAlstkyList = commonDao.getValidation(map);

		if (validAlstkyList.size() > 0) {
			DataMap valid = validAlstkyList.get(0);
			return valid.getString("RESULTMSG");
		}
		return result;
	}

	/*
	 * @Transactional public int DL06PikingSave(DataMap map) throws SQLException {
	 * int headModifyIndex = -1; List<DataMap> itemList = null;
	 * if(map.containsKey("headModifyIndex")){ headModifyIndex =
	 * map.getInt("headModifyIndex"); itemList = map.getList("list"); }
	 * 
	 * List<DataMap> headList = map.getList("headList");
	 * 
	 * DataMap row; int count = 0;
	 * 
	 * List<DataMap> paramItemList = new ArrayList<DataMap>(); DataMap paramItemMap
	 * = new DataMap(); DataMap paramTasdrMap = new DataMap(); DataMap copy = new
	 * DataMap();
	 * 
	 * for(int i=0;i<headList.size();i++){ if(map.containsKey("param")){ row = new
	 * DataMap(map.getMap("param").getMap("map")); }else{ row = new DataMap(); }
	 * 
	 * map.clonSessionData(row); row.append(headList.get(i).getMap("map"));
	 * paramItemList = new ArrayList<DataMap>();
	 * 
	 * if(headModifyIndex == i){ for(int j=0;j<itemList.size();j++){ copy = new
	 * DataMap(itemList.get(j).getMap("map")); paramItemList.add(copy); } }else{
	 * row.setModuleCommand("WmsTask_COPY", "TASDI"); row.put("QTY_COPY", "Y");
	 * List<DataMap> tasdiList = commonDao.getList(row); for(int j = 0; j <
	 * tasdiList.size(); j++){ copy = new DataMap(tasdiList.get(j));
	 * paramItemList.add(copy); } }
	 * 
	 * 
	 * //insert for(int j = 0; j < paramItemList.size(); j++){ paramItemMap =
	 * paramItemList.get(j);
	 * 
	 * //remove paramItemMap.setModuleCommand("WmsTask_COPY", "TASDIDEL");
	 * commonDao.delete(paramItemMap);
	 * 
	 * 
	 * paramItemMap.setModuleCommand("WmsTask_COPY", "TASDIEND");
	 * paramItemMap.put(CommonConfig.SES_USER_ID_KEY,
	 * map.get(CommonConfig.SES_USER_ID_KEY));
	 * 
	 * // "FPC" 작업수량 = 작업완료수량 // "PPC" 작업수량 > 작업완료수량 // "OPC" 작업수량 < 작업완료수량 int
	 * STAUES = Integer.parseInt(paramItemMap.getString("QTTAOR") )-
	 * Integer.parseInt(paramItemMap.getString("QTCOMP")); if (STAUES == 0) {
	 * paramItemMap.put("STATIT","FPC"); } else if (STAUES == 1) {
	 * paramItemMap.put("STATIT","PPC"); } else if (STAUES == -1) {
	 * paramItemMap.put("STATIT","OPC"); } commonDao.insert(paramItemMap);
	 * 
	 * 
	 * 
	 * paramItemMap.put("ERRNUM",0); paramItemMap.put("ERRMSG", "");
	 * paramItemMap.setModuleCommand("WmsOutbound_COPY", "SZPINSTASDR");
	 * commonDao.update(paramItemMap);
	 * 
	 * } count++; }
	 * 
	 * return count; }
	 */

	@Transactional
	public DataMap DL06POPQTUALOCNT(DataMap map) throws SQLException {
		DataMap data = map.getMap("data");
		data.setModuleCommand("WmsOutbound_COPY", "DL06POP_QTUALOCNT");
		DataMap rsMap = commonDao.getMap(data);

		return rsMap;
	}

	@Transactional
	public DataMap DL06POPQTSWRKCNT(DataMap map) throws SQLException {
		DataMap data = new DataMap();
		data.put("TASKKY", map.get("TASKKY"));
		data.setModuleCommand("WmsOutbound_COPY", "DL06POP_QTSWRKCNT");
		List<DataMap> list = commonDao.getList(data);
		DataMap rsMap = new DataMap();
		rsMap.put("list", list);

		return rsMap;
	}

	@Transactional
	public int DL06POPSave(DataMap map) throws SQLException {

		DataMap head = map.getMap("head");
		List<DataMap> list = map.getList("list");
		DataMap row1;

		String ResultMsg = null;

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
		map.put("SHPOKY", shpoky);
		map.put("SHPOIT", shpoit);

		// 피킹 취소처리(피킹 취소 후 새로운 피킹지시를 내린다.)
		head.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
		head.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
		head.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));
		head.put("OLDLOC", "");
		head.put("CANCELTYPE", "CHANGE");

		commonDao.getMap("WmsOutbound_COPY.CALL_PICKING_CANCEL_PROC_DL06", head); // 피킹취소
		// commonDao.delete("WmsOutbound_COPY.DL04REMOVE2", row); //할당취소

		// 1. SHPDH, SHPDI, TASDI 정보 조회
		map.setModuleCommand("WmsOutbound_COPY", "SEARCHSHPDH");
		DataMap shpdhMap = commonDao.getMap(map);
		map.setModuleCommand("WmsOutbound_COPY", "SEARCHSHPDI");
		DataMap shpdiMap = commonDao.getMap(map);
		String svbeln = shpdiMap.getString("SVBELN");
		String sposnr = shpdiMap.getString("SPOSNR");
		String stknum = shpdiMap.getString("STKNUM");
		String stpnum = shpdiMap.getString("STPNUM");
		String ssornu = shpdiMap.getString("SSORNU");
		String ssorit = shpdiMap.getString("SSORIT");

		// 2. 기존의 TASDI ITEM NUMBER MAX 값 조회
		map.setModuleCommand("WmsOutbound_COPY", "SEARCHMAXTASDH");
		List<DataMap> tasdhList = commonDao.getList(map);
		map.setModuleCommand("WmsOutbound_COPY", "SEARCHTASDIALL");
		List<DataMap> tasdiList = commonDao.getList(map);

		DataMap dbData;
		DataMap gridData = new DataMap();
		String webKey = null;
		String dbKey = null;
		int taskit = Integer.parseInt(tasdhList.get(0).getString("TASKIT"));

		// 3. tasdi 에서 수정된 item number만 삭제
		for (int i = 0; i < tasdiList.size(); i++) {
			dbData = tasdiList.get(i).getMap("map");
			// dbKey = dbData.getString("TASKKY") + dbData.getString("TASKIT");
			dbKey = dbData.getString("TASKKY") + dbData.getString("TASKIT") + dbData.getString("LOTNUM")
					+ dbData.getString("LOCASR");
			//int dbQTY = Integer.parseInt(dbData.getString("QTTAOR"));
			double dbQTY = Double.parseDouble(dbData.getString("QTTAOR"));
			
			for (int j = 0; j < list.size(); j++) {
				gridData = list.get(j).getMap("map");
				// webKey = gridData.getString("TASKKY") + gridData.getString("TASKIT");
				// webKey = gridData.getString("TASKKY") + gridData.getString("TASKIT") +
				// gridData.getString("LOTNUM")+gridData.getString("LOCAKY");
				webKey = gridData.getString("TASKKY") + tasdhList.get(0).getString("TASKIT")
						+ gridData.getString("LOTNUM") + gridData.getString("LOCAKY");
				double webQTY = Double.parseDouble(gridData.getString("QTSPUT"));

				if (dbData.getString("TASKIT").equals(tasdhList.get(0).getString("TASKIT"))) {
					map.put("TASKKY", dbData.getString("TASKKY"));
					map.put("TASKIT", dbData.getString("TASKIT"));
					
					map.setModuleCommand("WmsOutbound_COPY", "DL06POPREMOVE");// TASDI 부분 삭제 DELTEE										
					commonDao.delete(map);
					
					commonDao.update("WmsOutbound_COPY.DL04SHPDI", map); // SHPDI 수정 정보 UPDATE
					commonDao.update("WmsOutbound_COPY.DL04SHPDH", map); // SHPDH 수정 정보 UPDATE
					
					break;
				}
			}
		}

		// 4. 재고 SELECT하여 Task Order Item/Reference 생성
		DataMap rowData = new DataMap();
		double qty, qttaor, qtsiwh = 0;
		int loopcnt = 0;

		for (DataMap row : list) {
			rowData = (DataMap) row.get("map");
			qty = Double.parseDouble(rowData.getString("QTSPUT"));
			loopcnt++;

			// 4-1 수량이<0 통과
			if (qty <= 0)
				continue;

			boolean insFlag = false;
			String selectTaskit = "";
			String selectLocation = "";
			String selectLotnum = "";
			// 4-2 TASKKY 있을 때
			for (int i = 0; i < tasdiList.size(); i++) {
				String dbKey2 = tasdiList.get(i).getMap("map").getString("TASKKY")
						+ tasdiList.get(i).getMap("map").getString("TASKIT");
				String webKey2 = rowData.getString("TASKKY") + tasdhList.get(0).getString("TASKIT");
				// (1) taskky 있고, 수량 변화 있음
				if (dbKey2.equals(webKey2)) {
					selectTaskit = tasdiList.get(i).getMap("map").getString("TASKIT");
					selectLocation = tasdiList.get(i).getMap("map").getString("LOCASR");
					selectLotnum = tasdiList.get(i).getMap("map").getString("LOTNUM");
					// 다른로케이션에서 수량도 달라진경우 + 같은 로케이션에서 수량이 달라진경우
					if (rowData.getInt("QTSPUT") != tasdiList.get(i).getMap("map").getInt("QTTAOR")) {
						insFlag = true;
						break;
					}
					// 수량같은데 그 수량 통째로 다른로케이션에 들어간경우
					if (!rowData.getString("LOCAKY").equals(tasdiList.get(i).getMap("map").getString("LOCASR"))) {
						insFlag = true;
						break;
					}

					// 수량은 통째로 같은 로케이션 다른 롯트넘버
					if (rowData.getString("LOCAKY").equals(tasdiList.get(i).getMap("map").getString("LOCASR"))
							&& !rowData.getString("LOTNUM")
									.equals(tasdiList.get(i).getMap("map").getString("LOTNUM"))) {
						insFlag = true;
						break;
					}
				} else if ("".equals(webKey2.trim()) && rowData.getInt("QTSPUT") > 0) {// taskky 없고, 수량 변화 있음
					insFlag = true;
					break;
				}
			}

			if (insFlag) {
				// 재고 셀렉트
				map.setModuleCommand("WmsOutbound_COPY", "MANUALALLOCATE_AVAILABLE_STOCK");
				map.put("OWNRKY", rowData.get("OWNRKY"));
				map.put("WAREKY", rowData.get("WAREKY"));
				map.put("AREAKY", rowData.get("AREAKY"));
				map.put("LOCAKY", rowData.get("LOCAKY"));
				map.put("SKUKEY", rowData.get("SKUKEY"));
				map.put("TRNUID", rowData.get("TRNUID"));
				map.put("PACKID", rowData.get("PACKID"));
				map.put("LOTNUM", rowData.get("LOTNUM"));
				map.put("SHPMTY", shpdhMap.get("SHPMTY"));
				map.put("STOKKY", rowData.get("STOKKY"));
				List<DataMap> stockMapList = commonDao.getList(map);

				if (qty <= 0) {
					break;
				}

				map.put("TASKKY", tasdhList.get(0).getString("TASKKY"));
				/*
				 * if(rowData.get("LOCAKY").equals(selectLocation) &&
				 * rowData.get("LOTNUM").equals(selectLotnum)) { map.put("TASKIT",
				 * leftPad(selectTaskit+"", "0", 6)); } else { taskit += 10; map.put("TASKIT",
				 * leftPad(taskit+"", "0", 6)); }
				 */
				if (loopcnt != 1) {
					taskit += 10;
				}
				map.put("TASKIT", leftPad(taskit + "", "0", 6));

				map.put("TASKTY", "PK");
				map.put("RSNCOD", "330");
				map.put("STATIT", "NEW");
				map.put("QTTAOR", qty);
				map.put("QTCOMP", BigDecimal.ZERO);
				map.put("OWNRKY", stockMapList.get(0).get("OWNRKY"));
				map.put("SKUKEY", stockMapList.get(0).get("SKUKEY"));
				map.put("LOTNUM", stockMapList.get(0).get("LOTNUM"));
				map.put("ACTCDT", "000000");
				map.put("ACTCTI", "000000");
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

				map.put("LOCAAC", stockMapList.get(0).get("LOCATG"));
				map.put("SECTAC", stockMapList.get(0).get("SECTAC"));
				map.put("PAIDAC", stockMapList.get(0).get("PAIDAC"));
				map.put("TRNUAC", stockMapList.get(0).get("TRNUID"));  
				map.put("ATRUTY", stockMapList.get(0).get("TRUNTY")); 
				map.put("AMEAKY", stockMapList.get(0).get("MEASKY"));
				map.put("AUOMKY",  stockMapList.get(0).get("UOMKEY"));  
				map.put("QTAPUM",  stockMapList.get(0).get("QTPUOM"));  
				map.put("ADUOKY",  stockMapList.get(0).get("DUOMKY"));  
				map.put("QTADUM",  stockMapList.get(0).get("QTDUOM"));  
				map.put("REFDKY", stockMapList.get(0).get("REFDKY"));
				map.put("REFDIT", stockMapList.get(0).get("REFDIT"));
				map.put("REFDKY", shpoky);
				map.put("REFDIT", shpoit);
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
				map.setModuleCommand("WmsOutbound_COPY", "TASDI");
				commonDao.insert(map);

				map.setModuleCommand("WmsOutbound_COPY", "TASDR_PROCALL");
				commonDao.getMap(map);
			} // if(insflag)문
		} // isnert for문

		return count;
	}

	@Transactional
	public int DL06PikingSave(DataMap map) throws SQLException {
		int headModifyIndex = -1;
		List<DataMap> itemList = null;
		if (map.containsKey("headModifyIndex")) {
			headModifyIndex = map.getInt("headModifyIndex");
			itemList = map.getList("list");
		}

		List<DataMap> headList = map.getList("headList");

		if (headList == null || headList.size() == 0) {
			throw new SQLException("선택된 행이 없습니다.");
		}

		DataMap row;
		int count = 0;

		List<DataMap> paramItemList = new ArrayList<DataMap>();
		DataMap paramItemMap = new DataMap();
		DataMap paramTasdrMap = new DataMap();
		DataMap copy = new DataMap();

		for (int i = 0; i < headList.size(); i++) {
			if (map.containsKey("param")) {
				row = new DataMap(map.getMap("param").getMap("map"));
			} else {
				row = new DataMap();
			}

			map.clonSessionData(row);
			row.append(headList.get(i).getMap("map"));
			paramItemList = new ArrayList<DataMap>();

			if (headModifyIndex == i) {
				//log.debug("DL06PikingSave headModifyIndex::" + headModifyIndex);
				//log.debug("DL06PikingSave itemList size::" + itemList.size());
				for (int j = 0; j < itemList.size(); j++) {
					copy = new DataMap(itemList.get(j).getMap("map"));
					paramItemList.add(copy);
				}
			} else {
				DataMap condRow = new DataMap();
				//log.debug("DL06PikingSave condRow SHPOKY::" + row.getString("SHPOKY"));
				condRow.setModuleCommand("WmsOutbound_COPY", "MDL16");
				condRow.put("STATIT", "1");
				condRow.put("SHPOKY", row.getString("SHPOKY"));
				condRow.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));
				//log.debug("DL06PikingSave condRow SES_WAREKY::" + map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

				List<DataMap> tasdiList = commonDao.getList(condRow);
				for (int j = 0; j < tasdiList.size(); j++) {
					copy = new DataMap(tasdiList.get(j));
					paramItemList.add(copy);
				}
			}

			// insert
			for (int j = 0; j < paramItemList.size(); j++) {
				paramItemMap = paramItemList.get(j);
				paramItemMap.setModuleCommand("WmsTask_COPY", "DL06_SAVE");
				DataMap dtMap = commonDao.getMap(paramItemMap);

				// remove
				paramItemMap.setModuleCommand("WmsTask_COPY", "TASDIDEL");
				commonDao.delete(paramItemMap);

				paramItemMap.setModuleCommand("WmsTask_COPY", "TASDIEND");
				paramItemMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

				// "FPC" 작업수량 = 작업완료수량
				// "PPC" 작업수량 > 작업완료수량
				// "OPC" 작업수량 < 작업완료수량
				/*
				 * 2016.2.21 junghyojin 작업수량 정수형 처리를 위하여 주석 후 double으로 변경 처리함. int STAUES =
				 * Integer.parseInt(paramItemMap.getString("QTTAOR") )-
				 * Integer.parseInt(paramItemMap.getString("QTCOMP")); if (STAUES == 0) {
				 * paramItemMap.put("STATIT","FPC"); } else if (STAUES == 1) {
				 * paramItemMap.put("STATIT","PPC"); } else if (STAUES == -1) {
				 * paramItemMap.put("STATIT","OPC"); }
				 */
				BigDecimal p_qttaor = new BigDecimal(paramItemMap.getString("QTTAOR"));
				BigDecimal p_qtcomp = new BigDecimal(paramItemMap.getString("QTCOMP"));

				BigDecimal p_STAUES = p_qttaor.subtract(p_qtcomp);

				//log.debug(":::::::::::DL06PikingSave.p_STAUES::::::" + p_STAUES.signum());
				if (0 == p_STAUES.signum()) {
					paramItemMap.put("STATIT", "FPC");
				} else if (1 == p_STAUES.signum()) {
					paramItemMap.put("STATIT", "PPC");
				} else if (-1 == p_STAUES.signum()) {
					paramItemMap.put("STATIT", "OPC");
				}

				commonDao.insert(paramItemMap);
				paramTasdrMap = new DataMap();

				paramTasdrMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				paramTasdrMap.put("STOKKY", dtMap.getString("SRCSKY"));
				paramTasdrMap.put("LMODAT", paramItemMap.getString("LMODAT"));
				paramTasdrMap.put("LMOTIM", paramItemMap.getString("LMOTIM"));
				paramTasdrMap.put("LMOUSR", paramItemMap.getString("LMOUSR"));
				paramTasdrMap.put("TASKKY", dtMap.getString("TASKKY"));
				paramTasdrMap.put("TASKIT", dtMap.getString("TASKIT"));
				paramTasdrMap.put("QTSTKC", paramItemMap.getString("QTCOMP"));
				paramTasdrMap.put("QTSTKM", dtMap.getString("QTSTKM"));

				paramTasdrMap.setModuleCommand("WmsTask_COPY", "TASDREND");
				commonDao.insert(paramTasdrMap);
			}
			count++;
		}

		return count;
	}

	@Transactional
	public int DeleteTM07(DataMap map) throws SQLException {

		List<DataMap> head = map.getList("head");

		int count = 0;
		DataMap row = new DataMap();

		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");

			row.setModuleCommand("WmsOutbound_COPY", "TM07");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			commonDao.delete(row);

			count++;

		}
		return count;
	}

	@Transactional
	public DataMap SaveTM03(DataMap map) throws SQLException {
		List<DataMap> headTemp = map.getList("head");
		DataMap head = headTemp.get(0).getMap("map");
		List<DataMap> list = map.getList("list");

		int itemCount = 0;
		int count = 0;
		DataMap row = new DataMap();
		DataMap item = new DataMap();

		// Added by Yun
		String num_VBELN = "WMS" + wmsService.getNumSeq("NR-VBELN").substring(3);
		// Added by Yun

		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.setModuleCommand("WmsOutbound_COPY", "TM05");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));

			row.put("MANDT", "110");
			row.put("SEQNO", wmsService.getNumSeq("NR-IF113"));
//			row.put("VBELN", head.getString("VBELN") );
			row.put("VBELN", num_VBELN);
			itemCount += 1;
			String snum = String.valueOf(itemCount);
			row.put("POSNR", snum);

			row.put("BWART", head.getString("BWART"));
			row.put("STATUS", "A");
			row.put("IFFLG", "N");
			row.put("WADAT", head.getString("ZEKKO_AEDAT"));
			row.put("PTNRKY", head.getString("PTNRKY"));
			row.put("MEINS", row.get("UOMKEY"));
			row.put("WAREKY", head.getString("WAREKY"));

			commonDao.update(row);

			/*
			 * DataMap vbelnMap = new DataMap("WmsOutbound_COPY", "SETVBELN");
			 * vbelnMap.put("BWART", head.getString("BWART")); String vbelnext
			 * =(String)commonDao.getMap(vbelnMap).getString("VBELN");
			 * 
			 * item.put("VBELN", row.getString("VBELN")); item.put("POSNR",
			 * row.getString("POSNR")); item.put("VBELNEXT", vbelnext); item.put("VWAREKY",
			 * head.getString("WAREKY")); item.put("VSEQNO",
			 * wmsService.getNumSeq("NR-IF113"));
			 */
		}
//		item.setModuleCommand("WmsOutbound_COPY", "TM05G");
//		commonDao.update(item);

		DataMap rsMap = new DataMap();
		StringBuilder sb = new StringBuilder();

		map.setModuleCommand("WmsOutbound_COPY", "TM05H");
//		map.put("VBELN", head.getString("VBELN") );
		map.put("VBELN", num_VBELN);

		List dataList = commonDao.getList(map);

		rsMap.put("data", dataList);

		return rsMap;
	}

	@Transactional
	public DataMap SaveTM05(DataMap map) throws SQLException {
		List<DataMap> headTemp = map.getList("head");
		DataMap head = headTemp.get(0).getMap("map");
		List<DataMap> list = map.getList("list");

		int itemCount = 0;
		int count = 0;
		DataMap row = new DataMap();
		DataMap item = new DataMap();

		// Added by Yun
		String num_VBELN = "WMS" + wmsService.getNumSeq("NR-VBELN").substring(3);
		// Added by Yun

		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.setModuleCommand("WmsOutbound_COPY", "TM05");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));

			row.put("MANDT", "110");
			row.put("SEQNO", wmsService.getNumSeq("NR-IF113"));
//			row.put("VBELN", head.getString("VBELN") );
			row.put("VBELN", num_VBELN);
			itemCount += 1;
			String snum = String.valueOf(itemCount);
			row.put("POSNR", snum);

			row.put("BWART", head.getString("BWART"));

			row.put("STATUS", "A");
			row.put("IFFLG", "N");
			row.put("WADAT", head.getString("ZEKKO_AEDAT"));
			row.put("PTNRKY", head.getString("PTNRKY"));
			row.put("MEINS", row.get("UOMKEY"));
			row.put("WAREKY", head.getString("WAREKY"));

			commonDao.update(row);

			// blocked by Yun
			/*
			 * String vseqno = wmsService.getNumSeq("NR-IF113");
			 * 
			 * DataMap vbelnMap = new DataMap("WmsOutbound_COPY", "SETVBELN");
			 * vbelnMap.put("BWART", head.getString("BWART")); String vbelnext
			 * =(String)commonDao.getMap(vbelnMap).getString("VBELN");
			 * 
			 * item.put("VBELN", row.getString("VBELN")); item.put("POSNR",
			 * row.getString("POSNR")); item.put("VBELNEXT", vbelnext); item.put("VWAREKY",
			 * head.getString("WAREKY")); item.put("VSEQNO", vseqno);
			 */
			// blocked by Yun
		}
//		item.setModuleCommand("WmsOutbound_COPY", "TM05G");
//		commonDao.update(item);

		DataMap rsMap = new DataMap();
		StringBuilder sb = new StringBuilder();

		map.setModuleCommand("WmsOutbound_COPY", "TM05H");
//		map.put("VBELN", head.getString("VBELN") );
		map.put("VBELN", num_VBELN);

		List dataList = commonDao.getList(map);

		rsMap.put("data", dataList);

		return rsMap;
	}

	@Transactional
	public DataMap SaveTM06(DataMap map) throws SQLException {
		List<DataMap> headTemp = map.getList("head");
		DataMap head = headTemp.get(0).getMap("map");
		List<DataMap> list = map.getList("list");

		map.put("WAREKY", head.get("WAREKY"));

		int count = 0;
		int itemCount = 0;
		DataMap row = new DataMap();

		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.setModuleCommand("WmsOutbound_COPY", "TM06");
			row.put("MANDT", "110");

			row.put("SEQNO", wmsService.getNumSeq("NR-IF113"));
			row.put("EBELN", head.getString("EBELN"));

			itemCount += 1;
			String snum = String.valueOf(itemCount);
			row.put("EBELP", snum);
			row.put("LIFNR", head.getString("PTNRKY"));
			row.put("BWART", head.getString("BWART"));

			row.put("STATUS", "A");
			row.put("IFFLG", "N");
			row.put("WAREKY", head.getString("WAREKY"));
			row.put("DEPTID1", head.getString("PTNRKY"));

			commonDao.insert(row);

			count++;
		}

		DataMap rsMap = new DataMap();
		StringBuilder sb = new StringBuilder();

		map.setModuleCommand("WmsOutbound_COPY", "TM06H");
		map.put("EBELN", head.getString("EBELN"));

		List dataList = commonDao.getList(map);

		rsMap.put("data", dataList);

		return rsMap;
	}

	@Transactional
	public DataMap saveShpd(DataMap map) throws SQLException {

		int headModifyIndex = -1;
		List<DataMap> itemList = null;
		if (map.containsKey("headModifyIndex")) {
			headModifyIndex = map.getInt("headModifyIndex");
			itemList = map.getList("list");
		}

		DataMap rsMap = new DataMap();

		List<DataMap> headList = map.getList("headList");

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

			if ("".equals(row.getString("SHPOKY").trim())) {
				docNum = wmsService.getDocSeq(row.getString("DOCUTY"));
				row.put("SHPOKY", docNum);
				row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
				commonDao.insert(row);
				if (headModifyIndex == i) {
					row.setModuleCommand("WmsOutbound_COPY", "SHPDI");
					for (int j = 0; j < itemList.size(); j++) {
						row.append(itemList.get(j).getMap("map"));

						itemCount += 10;
						String snum = String.valueOf(itemCount);
						String inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
						row.put("SHPOKY", docNum);
						row.put("SHPOIT", inum);
						commonDao.insert(row);
					}
				} else {
					row.setModuleCommand("WmsOutbound_COPY", "SHPDIALL");
					commonDao.insert(row);
				}
			} else {
				docNum = row.getString("SHPOKY");
				row.setModuleCommand("WmsOutbound_COPY", "SHPDHORG");
				commonDao.update(row);

				if (headModifyIndex == i) {
					row.setModuleCommand("WmsOutbound_COPY", "SHPDIORG");
					for (int j = 0; j < itemList.size(); j++) {
						row.append(itemList.get(j).getMap("map"));
						commonDao.update(row);
					}
				}
			}
			docNumList.add(docNum);

			if (row.getString("SHPMTY").toString().equals("299")) {
				row.setModuleCommand("WmsOutbound_COPY", "IF113STKNUM");
				commonDao.update(row);
			}

			if (!row.getString("DRELIN").toString().equals("V")) {
				row.put("DRELIN", "V");
				row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
				commonDao.update(row);

				if (row.getString("SHPMTY").toString().equals("299")) {
					row.setModuleCommand("WmsOutbound_COPY", "IF113PROC");
					commonDao.getMap(row);
				}
			}

			if (map.containsKey("batch")) {
				row.setModuleCommand("WmsOutbound_COPY", "BATCHALLOCATION");

				commonDao.getMap(row);
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < docNumList.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append("'").append(docNumList.get(i)).append("'");
		}

		map.setModuleCommand("WmsOutbound_COPY", "DL04HEAD");
		map.put("SHPOKY", sb.toString());

		List dataList = commonDao.getList(map);

		rsMap.put("data", dataList);

		return rsMap;
	}

	@Transactional
	public DataMap deleteOrder(DataMap map) throws SQLException {
		int headModifyIndex = -1;
		List<DataMap> itemList = null;
		if (map.containsKey("headModifyIndex")) {
			headModifyIndex = map.getInt("headModifyIndex");
			itemList = map.getList("list");
		}

		DataMap rsMap = new DataMap();

		List<DataMap> headList = map.getList("headList");

		DataMap row;

		for (int i = 0; i < headList.size(); i++) {
			if (map.containsKey("param")) {
				row = new DataMap(map.getMap("param").getMap("map"));
			} else {
				row = new DataMap();
			}

			map.clonSessionData(row);
			row.append(headList.get(i).getMap("map"));
			row.setModuleCommand("WmsOutbound_COPY", "IFWMS113");

			if (headModifyIndex == i) {
				for (int j = 0; j < itemList.size(); j++) {
					row.append(itemList.get(j).getMap("map"));
					commonDao.update(row);
				}
			} else {
				commonDao.update(row);
			}
		}

		return rsMap;
	}

	@Transactional
	public DataMap dl04Procall(DataMap map) throws SQLException {
		int headModifyIndex = -1;
		List<DataMap> itemList = null;
		if (map.containsKey("headModifyIndex")) {
			headModifyIndex = map.getInt("headModifyIndex");
			itemList = map.getList("list");
		}

		DataMap rsMap = new DataMap();

		List<DataMap> headList = map.getList("headList");

		DataMap row = new DataMap();

		List docNumList = new ArrayList();

		for (int i = 0; i < headList.size(); i++) {
			if (map.containsKey("param")) {
				row = new DataMap(map.getMap("param").getMap("map"));
			} else {
				row = new DataMap();
			}
			map.clonSessionData(row);
			row.append(headList.get(i).getMap("map"));
			docNumList.add(headList.get(i).getMap("map").get("SHPOKY"));

			if (row.getString("SHPMTY").toString().equals("299")) {
				row.setModuleCommand("WmsOutbound_COPY", "IF113STKNUM");
				commonDao.update(row);
			}

			if (!row.getString("DRELIN").toString().equals("V")) {
				row.put("DRELIN", "V");
				row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
				commonDao.update(row);

				if (row.getString("SHPMTY").toString().equals("299")) {
					row.setModuleCommand("WmsOutbound_COPY", "IF113PROC");
					commonDao.getMap(row);
				}
			}

			if (map.containsKey("batch")) {
				row.setModuleCommand("WmsOutbound_COPY", "BATCHALLOCATION");

				commonDao.getMap(row);
			}

		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < docNumList.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append("'").append(docNumList.get(i)).append("'");
		}

		map.setModuleCommand("WmsOutbound_COPY", "DL04HEAD");
		map.put("SHPOKY", sb.toString());

		List dataList = commonDao.getList(map);

		rsMap.put("data", dataList);

		return rsMap;
	}

	
	//@Transactional
	public DataMap validateSendEcms(DataMap map) throws Exception {
		String result = "OK";
		int totcount = 0;
		long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기

		List<DataMap> headList = map.getList("headList");

		DataMap row;

		DataMap rsMap = new DataMap();
		DataMap rqMap = new DataMap();
		
		List shpokyList = new ArrayList();
		
		
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < headList.size(); i++) {
						
			row = headList.get(i).getMap("map");						
			
			String shpokyNum = row.getString("SHPOKY");
			shpokyList.add(shpokyNum);					

		}
		
		StringBuilder shpokySb = new StringBuilder();
		//StringBuilder shpokySbList = new StringBuilder();
		
		for (int k = 0; k < shpokyList.size(); k++) {
			totcount++;
			
			if (totcount != 1 && totcount%1000 != 1) {
				shpokySb.append(",");
			}
				shpokySb.append("'").append(shpokyList.get(k)).append("'");			
			
			if(totcount%1000 == 0 || totcount == shpokyList.size()) {
				
				rsMap.put("SHPOKY", shpokySb);
				rsMap.put("WAREKY", map.getString(CommonConfig.SES_USER_WHAREHOUSE_KEY));		
				
				// SHPDH I/F CHECK 주문변경 체크
				rsMap.setModuleCommand("WmsTask_COPY", "VALIDATION_SHPDHLIST");
				
				rqMap = commonDao.getMap(rsMap);

				if (rqMap != null) {
					rsMap.put("SHPOKY", rqMap.getString("SHPOKY"));

					return rsMap;
				}
				
				// Validation check
				// (PPC, FPC) && (출하완료수량 <= 피킹수량) && (출하완료수량 <= 지시수량) Validation
				if (shpokySb.length() > 0) {
					List<DataMap> valiList = new  ArrayList<DataMap>(); 
					valiList.clear();
					//rsMap.put("APPENDQUERY", sb.toString());
					rsMap.setModuleCommand("WmsOutbound_COPY", "VALIDDL08");
					 valiList = commonDao.getList(rsMap);
					
					for (int i = 0; i < valiList.size(); i++) {
						DataMap dtMap = valiList.get(i);
						
						if (!isEmpty(dtMap.getString("RESULTMSG")) && !"OK".equals(dtMap.getString("RESULTMSG"))) {
							result = dtMap.getString("RESULTMSG");
							rsMap.put("result", result);
							return rsMap;
						}

					}
				}
				
				// 1. 완료처리하지 않은 TASK 정보가 있는지 확인.
				rsMap.setModuleCommand("WmsOutbound_COPY", "CNTTASK_SHPDILIST");
				rqMap = commonDao.getMap(rsMap);
				
				if (rqMap != null) {
					result = "OUT_M0174" + " " + rqMap.getString("SHPOKY") + "/" + rqMap.getString("SHPOIT");
					rsMap.put("result", result);
					return rsMap;
				}
				
				shpokySb = new StringBuilder();
			}
			
			
		}
			
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
		System.out.println("전체 거린시간(m) : "+secDiffTime);
		
		rsMap.put("CNT", totcount);
		rsMap.put("result", result);
		return rsMap;
	}

	@Transactional
	public int savePrtseq(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("head");
		int count = 0;

		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.setModuleCommand("WmsOutbound_COPY", "PRTLG");
			row.put("PRTSEQ", map.get("PRTSEQ"));

			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			commonDao.insert(row);

			count++;
		}

		return count;
	}

	@Transactional
	public int savePrtseq2(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("head");
		int count = 0;

		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.setModuleCommand("WmsOutbound_COPY", "PRTLGPICK");
			row.put("PRTSEQ", map.get("PRTSEQ"));

			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			commonDao.insert(row);

			count++;
		}

		return count;
	}

//	@Transactional
//	public int saveDL04(DataMap map) throws SQLException {
//		List<DataMap> list = map.getList("head");
//		int count = 0;
//		
//		DataMap row;
//		for(int i=0;i<list.size();i++){
//			row = list.get(i).getMap("map");
//			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
//			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
//			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));
//			
//			commonDao.update("WmsOutbound_COPY.DL04", row);
//			count ++;
//		}
//		return count;
//	}

	@Transactional
	public DataMap saveDL04(DataMap map) throws SQLException {
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
				row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
				commonDao.insert(row);
				// 1.2 item insert
				if (headModifyIndex == i) { // 아이템그리드에 선택되었을 때
					row.setModuleCommand("WmsOutbound_COPY", "SHPDI");
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
					row.setModuleCommand("WmsOutbound_COPY", "DL04Sub");
					selectList = commonDao.getList(row);
					for (int j = 0; j < selectList.size(); j++) {
						row.append(selectList.get(j).getMap("map"));
						row.setModuleCommand("WmsOutbound_COPY", "SHPDI");

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
				row.setModuleCommand("WmsOutbound_COPY", "SHPDHORG");
				commonDao.update(row);

				row.setModuleCommand("WmsOutbound_COPY", "SHPDIORG");
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
	public int saveDL04Sub(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("list");
		int count = 0;

		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

			commonDao.update("WmsOutbound_COPY.DL04Sub", row);
			count++;
		}
		return count;
	}

	@Transactional
	public DataMap AssignDL04(DataMap map) throws Exception {
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
//			rsMap.setModuleCommand("WmsTask_COPY", "VALIDATION_SHPDH"); 
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

			//row = head.get(i).getMap("map");
			// 1. 출고문서번호가 없을 때 저장 후, 할당
//			if ("".equals(row.getString("SHPOKY").trim())) {
//				docNum = wmsService.getDocSeq(row.getString("DOCUTY"));
//				row.put("SHPOKY", docNum);
//				row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
//				commonDao.insert(row);
//
//				if (headModifyIndex == i) {
//					row.setModuleCommand("WmsOutbound_COPY", "SHPDI");
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
//						// commonDao.getMap("WmsOutbound_COPY.ALLOC_ITEM_PROCALL", row);
//					}
//				} else {
//					row.setModuleCommand("WmsOutbound_COPY", "DL04Sub");
//					selectList = commonDao.getList(row);
//					for (int j = 0; j < selectList.size(); j++) {
//						row.append(selectList.get(j).getMap("map"));
//
//						row.setModuleCommand("WmsOutbound_COPY", "SHPDI");
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
			commonDao.getMap("WmsOutbound_COPY.ALLOC_PROCALL", row);

			count++;
			int Result = 0;
			
			if(!row.getString("result").equals("EMPTY")) {
				Result = Integer.parseInt(row.getString("result"));
			};
			
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

	@Transactional
	public DataMap UnallocateDL04(DataMap map) throws Exception {
		List<DataMap> head = map.getList("head");
		List<DataMap> itemList = map.getList("list");
		List<DataMap> selectList = new ArrayList<DataMap>();
		List shpokyList = new ArrayList();
		String unalType = map.getString("unalType");
		DataMap rsMap = new DataMap();
		DataMap row;
		int count = 0;
		String HeadGridShpoky = map.get("HeadGridShpoky").toString();

		for (int i = 0; i < head.size(); i++) {
			String shpokyNum = head.get(i).getMap("map").getString("SHPOKY");
			shpokyList.add(shpokyNum);

			row = head.get(i).getMap("map");
			String ItemStatit2 = row.getString("STATDO");

			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));


				String taskky;
				List<DataMap> taskkyList;
				DataMap taskkyRow;

				taskkyList = commonDao.getList("WmsOutbound_COPY.DL04_SELECT_TASKKY", row);

				if ("PCN".equals(unalType)) {
					DataMap rowSub;

					for (int j = 0; j < itemList.size(); j++) {

						rowSub = itemList.get(j).getMap("map");
						rowSub.put("KEEPIT", " ");
						rowSub.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
						
						commonDao.delete("WmsOutbound_COPY.DL04REMOVE1", rowSub); // TASDI 부분 삭제 DELTEE
						commonDao.update("WmsOutbound_COPY.DL04SHPDI", rowSub); // SHPDI 수정 정보 UPDATE
						commonDao.update("WmsOutbound_COPY.DL04SHPDH", rowSub); // SHPDH 수정 정보 UPDATE

					}

				} else {
						row.put("KEEPIT", " ");
						commonDao.delete("WmsOutbound_COPY.DL04REMOVE2", row); // TASDI 헤더 삭제 DELTEE
						commonDao.update("WmsOutbound_COPY.DL04SHPDI", row); // SHPDI 수정 정보 UPDATE
						commonDao.update("WmsOutbound_COPY.DL04SHPDH", row); // SHPDH 수정 정보 UPDATE
				}

				// TASDH 헤더 삭제
				for (int k = 0; k < taskkyList.size(); k++) {

					taskkyRow = taskkyList.get(k).getMap("map");

					// TASDH에 엮여있는 TASDI가 전부 삭제되면 TASDH 삭제
					if (commonDao.getList("WmsOutbound_COPY.DL04REMOVE_CHECK", row).size() == 0) {
						row.put("TASKKY", taskkyRow.get("TASKKY"));
						commonDao.delete("WmsOutbound_COPY.DL04REMOVE3", row); // TASDH 헤더 삭제 DELTEE
					}
				}

				rsMap.put("ERRMSG", "OK");
				count++;
			
				
				
				
			StringBuilder sb = new StringBuilder();
			for (int k = 0; k < shpokyList.size(); k++) {
				if (k != 0) {
					sb.append(",");
				}
				sb.append("'").append(shpokyList.get(k)).append("'");
			}

			rsMap.put("SHPOKY", sb.toString());
			
			
			
			//마지막
			if(head.size()-1 == i) {
				
				DataMap ifMap = new DataMap();
				List<DataMap> stknumInfoList = null;
				
				map.put("STATUS", "D");
				map.put("SHPOKY", rsMap.get("SHPOKY"));
				map.put("WAREKY", map.getString(CommonConfig.SES_USER_WHAREHOUSE_KEY));
				map.setModuleCommand("WmsOutbound_COPY", "SHPDI_STKNUM_INFO");
				
				stknumInfoList = commonDao.getList(map);
				
				if(stknumInfoList.size() > 0) {
				
				ifMap.put("list", stknumInfoList);
				ifMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

					try {			
						
						
						String resultMsg = iFService.send_TK_CONTROL(ifMap);
						
						if(!"SUCCESS".equals(resultMsg)){
							rsMap.put("MSG", "SAP : " + resultMsg);
						
						}else {
							rsMap.put("MSG", resultMsg);
						}
									
					}catch (Exception e) {
						throw new SQLException(e.getMessage());
					}
				}
			
			}
			
		}

		return rsMap;
	}

	@Transactional
	public int CompleteDL04(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("head");
		int count = 0;

		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

			commonDao.getMap("WmsOutbound_COPY.SHPDPROCALL", row);
			count++;
		}
		return count;
	}

	@Transactional
	public String DL09CancelShipment(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("head");

		// Validation
		StringBuffer sb = new StringBuffer();
		for (DataMap head : list) {
			if (sb.length() > 0)
				sb.append(" UNION ALL\n ");

			sb.append(" SELECT '").append(head.getMap("map").get("SHPOKY")).append("' AS SHPOKY FROM DUAL ");
		}

		map.setModuleCommand("WmsOutbound_COPY", "SHIPPING_CANCEL"); // 쿼리키값 넣을것
		map.put("APPEND_QUERY", sb.toString());
		List<DataMap> validList = commonDao.getValidation(map);
		if (validList.size() > 0) {
			DataMap valid = validList.get(0);
			return valid.getString("RESULTMSG");
		}

		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));
			row.put("DOCUKY", row.get("SHPOKY"));
			row.put("DOFLAG", "C");
			// commonDao.getMap("WmsOutbound_COPY.CALL_SHIPPING_CANCEL_PROC", row);
			commonDao.getMap("WmsOutbound_COPY.CALL_AIRWMS_DOCUMENT_PROCESS", row);
		}

		return "OK";
	}

	@Transactional
	public int CompleteDL06(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("head");
		int count = 0;
		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			map.putAll(row);
			commonDao.getMap("WmsOutbound_COPY.DL06PROCALL", row);
			count++;
		}
		return count;
	}

	@Transactional
	public int CompleteDL16(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("list");

		SimpleDateFormat fmDate = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		String date = fmDate.format(new Date());
		SimpleDateFormat fmTime = new SimpleDateFormat("HHmmss", Locale.KOREA);
		String time = fmTime.format(new Date());

		int count = 0;
		DataMap row;
		DataMap searchtasdi = new DataMap();
		DataMap searchtasdr = new DataMap();
		DataMap data;
		for (int i = 0; i < list.size(); i++) {
			// 1. TASDI/R SELECT///////////////////////////
			row = list.get(i).getMap("map");
			map.put("TASKKY", row.get("TASKKY"));
			map.put("TASKIT", row.get("TASKIT"));

			map.setModuleCommand("WmsOutbound_COPY", "SEARCHTASDI");
			searchtasdi = commonDao.getMap(map);

			map.setModuleCommand("WmsOutbound_COPY", "SEARCHTASDR");
			searchtasdr = commonDao.getMap(map);

			// 2. 재고찾기//////////////////////////////////////
			searchtasdr.setModuleCommand("WmsOutbound_COPY", "STKKYFIND");
			data = commonDao.getMap(searchtasdr);

			// 3. TASDI DELETE////////////////////////////////
			searchtasdi.setModuleCommand("WmsOutbound_COPY", "TASDIDEL");
			commonDao.delete(searchtasdi);

			// 4.TASDI/R INSERT////////////////////////////////
			searchtasdi.put("LOCAAC", searchtasdi.get("LOCATG"));
			searchtasdi.put("ACTCDT", date);
			searchtasdi.put("ACTCTI", time);
			searchtasdi.put("QTCOMP", row.get("QTCOMP"));
			searchtasdi.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			searchtasdi.setModuleCommand("WmsOutbound_COPY", "TASDIEND");
			commonDao.insert(searchtasdi);

			searchtasdr.put("QTSTKC", row.get("QTCOMP"));
			searchtasdr.put("STOKKY", data.get("SRCSKY"));
			searchtasdr.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			searchtasdr.setModuleCommand("WmsOutbound_COPY", "TASDREND");
			commonDao.insert(searchtasdr);

			count++;
		}
		return count;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public String ValidPickingCancelDL06(DataMap map) throws SQLException {
		
		List<DataMap> list = map.getList("head");
		List<DataMap> itemList = map.getList("list");
		
		String cancelType = map.getString("cancel_Type");
		
		
		DataMap row;
		
		
		if(cancelType.equals("PickingCancel")) {
		
			for (int i = 0; i < list.size(); i++) {
				List<DataMap> validMapList;
				row = list.get(i).getMap("map");
				
				row.setModuleCommand("WmsTask_COPY", "TASDI_LOCK_INFO");
							
				
				validMapList = commonDao.getValidation(row);
				
			
				if (validMapList.size() > 0) {
					DataMap valid = validMapList.get(0);
					return valid.getString("RESULTMSG");
				}
			}
		}else {
			
			for (int i = 0; i < itemList.size(); i++) {
				List<DataMap> validMapList;
				row = itemList.get(i).getMap("map");
				
				row.setModuleCommand("WmsTask_COPY", "TASDI_LOCK_INFO");
							
				
				validMapList = commonDao.getValidation(row);
				
			
				if (validMapList.size() > 0) {
					DataMap valid = validMapList.get(0);
					return valid.getString("RESULTMSG");
				}
			}
		}
		
		return "OK";
	}
	
	@Transactional(rollbackFor = Exception.class)
	public String PickingCancelDL06(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("head");

		SimpleDateFormat fmDate = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		String date = fmDate.format(new Date());
		SimpleDateFormat fmTime = new SimpleDateFormat("HHmmss", Locale.KOREA);
		String time = fmTime.format(new Date());

		// Validation
		StringBuffer sb = new StringBuffer();
		for (DataMap item : list) {
			if (sb.length() > 0)
				sb.append(" UNION ALL\n ");

			sb.append(" SELECT '").append(item.getMap("map").get("TASKKY")).append("' AS TASKKY, ");
			sb.append("        '").append(item.getMap("map").get("TASKIT")).append("' AS TASKIT FROM DUAL ");
		}

		map.setModuleCommand("WmsOutbound_COPY", "PICKING_CANCEL"); // 쿼리키값 넣을것
		map.put("APPEND_QUERY", sb.toString());
		List<DataMap> validList = commonDao.getValidation(map);
		if (validList.size() > 0) {
			DataMap valid = validList.get(0);
			return valid.getString("RESULTMSG");
		}
		
		
		
		DataMap row;
		DataMap itRow;
		// 피킹취소
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");

			List<DataMap> selectList = new ArrayList<DataMap>();									
						
			row.setModuleCommand("WmsTask_COPY", "DL06DETAIL");
			selectList = commonDao.getList(row);

			for (int j = 0; j < selectList.size(); j++) {				
				itRow = selectList.get(j).getMap("map");	
				itRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				itRow.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
				itRow.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

				if ("FPC".equals(itRow.get("STATIT")) || "PPC".equals(itRow.get("STATIT"))) {
										
					itRow.put("OLDLOC", map.getString("OLDLOC"));					
					itRow.put("STOKKY", "%");
					itRow.put("CANCELTYPE", "CANCEL");
					
					commonDao.getMap("WmsOutbound_COPY.CALL_PICKING_CANCEL_PROC_DL06", itRow);
					/*
					 * row.setModuleCommand("WmsOutbound_COPY", "TASDR_PROCALL"); commonDao.getMap(row);
					 */
				}else if("FIN".equals(itRow.get("STATIT"))) {
					//기존 TASDI 작업 삭제
					itRow.setModuleCommand("WmsTask_COPY", "TASDIDEL");
					commonDao.delete(itRow);																
				}

			}

		}

		return "OK";
	}

	@Transactional(rollbackFor = Exception.class)
	public String PickingCancelDL06Sub(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("list");

		SimpleDateFormat fmDate = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		String date = fmDate.format(new Date());
		SimpleDateFormat fmTime = new SimpleDateFormat("HHmmss", Locale.KOREA);
		String time = fmTime.format(new Date());

		// Validation
		/*
		 * StringBuffer sb = new StringBuffer(); for(DataMap item : list){
		 * if(sb.length() > 0) sb.append(" UNION ALL\n ");
		 * 
		 * sb.append(" SELECT '").append(item.getMap("map").get("TASKKY")).
		 * append("' AS TASKKY, ");
		 * sb.append("        '").append(item.getMap("map").get("TASKIT")).
		 * append("' AS TASKIT FROM DUAL "); }
		 * 
		 * map.setModuleCommand("WmsOutbound_COPY", "PICKING_CANCEL_ITEM"); // 쿼리키값 넣을것
		 * map.put("APPEND_QUERY", sb.toString()); List<DataMap> validList =
		 * commonDao.getValidation(map); if(validList.size() > 0){ DataMap valid =
		 * validList.get(0); return valid.getString("RESULTMSG"); }
		 */

		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");

			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

			if ("FPC".equals(row.get("STATIT")) || "PPC".equals(row.get("STATIT"))) {
				row.put("STOKKY", "%");
				row.put("CANCELTYPE", "CANCEL");
				
				commonDao.getMap("WmsOutbound_COPY.CALL_PICKING_CANCEL_PROC_DL06", row);
				
				/*
				 * row.setModuleCommand("WmsOutbound_COPY", "TASDR_PROCALL"); commonDao.getMap(row);
				 */
			}else if("FIN".equals(row.get("STATIT"))) {
				//기존 TASDI 작업 삭제
				row.setModuleCommand("WmsTask_COPY", "TASDIDEL");
				commonDao.delete(row);	
			}
		}

		return "OK";
	}

	@Transactional
	public Object savePrtseqDL04(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("head");
		int count = 0;
		String opt;

		map.setModuleCommand("WmsOutbound_COPY", "SEQSTKNUM");
		Object stknum = commonDao.getObject(map);
		map.setModuleCommand("WmsOutbound_COPY", "SEQPRTSEQ");
		Object prtseq = commonDao.getObject(map);
		map.put("STKNUM", stknum);
		map.put("PRTSEQ", prtseq);
		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.setModuleCommand("WmsOutbound_COPY", "DL04HEAD");
			row.put("STKNUM", stknum);
			row.put("PRTSEQ", prtseq);
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			commonDao.update(row);

			count++;
		}
		opt = map.getString("opt");
		if (opt.equals("INDUPK")) {
			map.put("PRTTYP", 1);
		} else if (opt.equals("INDUPA")) {
			map.put("PRTTYP", 2);
		} else if (opt.equals("INDUPB")) {
			map.put("PRTTYP", 3);
		}

		map.setModuleCommand("WmsOutbound_COPY", "DL06PROC");
		commonDao.getMap(map);

		return prtseq;
	}

	@Transactional
	public int DeleteDL04(DataMap map) throws SQLException {
		List<DataMap> headList = map.getList("head");

		DataMap row;
		int count = 0;

		for (int i = 0; i < headList.size(); i++) {
			row = headList.get(i).getMap("map");

			// SHPDH SHPDI TASDR TASDI TASDH 도 삭제
			row.setModuleCommand("WmsOutbound_COPY", "SHPDI");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			commonDao.delete(row);

			row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			commonDao.delete(row);
			count++;
		}

		return count;
	}

	@Transactional
	public int savePrtseqPT01(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("head");
		int count = 0;

		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.setModuleCommand("WmsOutbound_COPY", "PRTLGPT01");
			row.put("PRTSEQ", map.get("PRTSEQ"));

			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			commonDao.insert(row);

			count++;
		}
		return count;
	}

	@Transactional
	public int savePrtseqDL09(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("head");
		int count = 0;

		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.setModuleCommand("WmsOutbound_COPY", "PRTLGSHIP");
			row.put("PRTSEQ", map.get("PRTSEQ"));

			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			commonDao.insert(row);

			count++;
		}

		return count;
	}

	@Transactional
	public String closingDL04(DataMap map) throws SQLException {
		List<DataMap> headList = map.getList("head");

		// Validation
		StringBuffer sb = new StringBuffer();
		for (DataMap head : headList) {
			if (sb.length() > 0)
				sb.append(" UNION ALL\n ");

			sb.append(" SELECT '").append(head.getMap("map").get("SHPOKY")).append("' AS SHPOKY FROM DUAL ");
		}

		map.setModuleCommand("WmsOutbound_COPY", "CLOSING_SHIPMENT"); // 쿼리키값 넣을것
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

			commonDao.update("WmsOutbound_COPY.ClosingShipment", head);
		}
		return "OK";
	}

	@Transactional
	public String DL04StockValidation(DataMap map) throws SQLException {
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

		map.setModuleCommand("WmsOutbound_COPY", "DL04_STOCK"); // 쿼리키값 넣을것
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

		map.setModuleCommand("WmsOutbound_COPY", "DL06_ALSTKY");
		map.put("APPEND_QUERY", sbupd.toString());
		List<DataMap> validAlstkyList = commonDao.getValidation(map);

		if (validAlstkyList.size() > 0) {
			DataMap valid = validAlstkyList.get(0);
			return valid.getString("RESULTMSG");
		}
		return result;
	}

	@Transactional
	public List<DataMap> dl06Head(DataMap map) throws SQLException {
		map.setModuleCommand("Mobile", "MDL06HEAD");
		List<DataMap> list = commonDao.getList(map);

		return list;

	}

	@Transactional
	public List<DataMap> fullPkChk(DataMap map) throws SQLException {
		List<DataMap> rtnList = new ArrayList<DataMap>();
		List<DataMap> headList = map.getList("headList");
		DataMap headRow = new DataMap();

		String sqlStr = "";
		for (int i = 0; i < headList.size(); i++) {
			headRow = headList.get(i).getMap("map");

			if (i == 0) {
				sqlStr += "'" + headRow.getString("SHPOKY") + "'";
			} else {
				sqlStr += ",'" + headRow.getString("SHPOKY") + "'";
			}
		}

		map.setModuleCommand("WmsOutbound_COPY", "FULLPK_CHK");
		map.put("SHPOPYS", sqlStr);
		rtnList = commonDao.getList(map);

		return rtnList;
	}

	@Transactional(propagation = Propagation.NESTED, rollbackFor = { SQLException.class })
	public DataMap MDL16PikingSave(DataMap map) throws SQLException {
		int headModifyIndex = -1;
		List<DataMap> itemList = null;
		List docNumList = new ArrayList();

		if (map.containsKey("headModifyIndex")) {
			headModifyIndex = map.getInt("headModifyIndex");
			// itemList = map.getList("list");
		}

		List<DataMap> headList = map.getList("headList");
		List<DataMap> dataList = null;
		List<DataMap> dataList2 = null;
		DataMap row, chkMap;
		int count = 0;
		StringBuffer chkSb = new StringBuffer();

		List<DataMap> paramItemList = new ArrayList<DataMap>();
		DataMap paramItemMap = new DataMap();
		DataMap paramTasdrMap = new DataMap();
		DataMap copy = new DataMap();
		Map sortMap = new HashMap();
		DataMap statdoMap = new DataMap();

		try {
			String shpmty = map.getString("SHPMTY");

			// if((shpmty.equals("211") || shpmty.equals("214"))){
			for (int i = 0; i < headList.size(); i++) {
				row = headList.get(i).getMap("map");
			}
			// }

			for (int i = 0; i < headList.size(); i++) {
				if (map.containsKey("param")) {
					row = new DataMap(map.getMap("param").getMap("map"));
				} else {
					row = new DataMap();
				}

				map.clonSessionData(row);
				row.append(headList.get(i).getMap("map"));
				paramItemList = new ArrayList<DataMap>();

				// 피킹완료 직전 한번더 상태체크 해당 문서가 피킹완료 상태이면 거른다
				row.setModuleCommand("WmsOutbound_COPY", "SHPDHSTATDO");
				statdoMap = commonDao.getMap(row);

				if (statdoMap != null && "FPK".equals(statdoMap.getString("STATDO"))
						|| statdoMap != null && "FPC".equals(statdoMap.getString("STATDO"))) {

					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					throw new SQLException(row.getString("SHPOKY") + "는 이미  피킹완료 되었습니다.");
				} else if (statdoMap != null && "CAN".equals(statdoMap.getString("STATDO"))) {

					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					throw new SQLException(row.getString("SHPOKY") + "는 이미  취소 되었습니다.");
				}

//					if(headModifyIndex == i){	
//						for(int j=0;j<itemList.size();j++){
//							copy = new DataMap(itemList.get(j).getMap("map"));
//							paramItemList.add(copy);
//						}
//					}else{
				row.setModuleCommand("WmsTask_COPY", "MDL16TASDI");
				row.put("QTY_COPY", "Y");
				List<DataMap> tasdiList = commonDao.getList(row);
				for (int j = 0; j < tasdiList.size(); j++) {
					copy = new DataMap(tasdiList.get(j));
					paramItemList.add(copy);
				}
//					}
				// insert
				for (int j = 0; j < paramItemList.size(); j++) {
					paramItemMap = paramItemList.get(j);

					// remove
					paramItemMap.setModuleCommand("WmsTask_COPY", "TASDIDEL");
					commonDao.delete(paramItemMap);

					paramItemMap.setModuleCommand("WmsTask_COPY", "TASDIEND");
					paramItemMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

					// "FPC" 작업수량 = 작업완료수량
					// "PPC" 작업수량 > 작업완료수량
					// "OPC" 작업수량 < 작업완료수량
					int STAUES = Integer.parseInt(paramItemMap.getString("QTTAOR"))
							- Integer.parseInt(paramItemMap.getString("QTCOMP"));
					if (STAUES == 0) {
						paramItemMap.put("STATIT", "FPC");
					} else if (STAUES == 1) {
						paramItemMap.put("STATIT", "PPC");
					} else if (STAUES == -1) {
						paramItemMap.put("STATIT", "OPC");
					}
					commonDao.insert(paramItemMap);

					paramItemMap.put("ERRNUM", 0);
					paramItemMap.put("ERRMSG", "");
					paramItemMap.setModuleCommand("WmsOutbound_COPY", "SZPINSTASDR");
					commonDao.update(paramItemMap);

					/*
					 * paramItemMap.setModuleCommand("WmsOutbound_COPY", "CHKPK"); chkMap =
					 * commonDao.getMap(paramItemMap);
					 * 
					 * if(chkMap.getInt("CNT")>0){
					 * sortMap.put(chkMap.getString("SHPOKY"),chkMap.getString("SHPOKY")
					 * +" | 품번 : "+ chkMap.getString("SKUKEY")); }
					 */
				}
				docNumList.add(row.getString("TASKKY"));
				count++;
			}
		} catch (Exception e) {
			//log.debug(e.getMessage());
			throw new SQLException(e.getMessage());
			// e.printStackTrace();
			// String msg = e.getMessage();
			// String errmsg = msg.substring(msg.indexOf("SQLException:"),msg.indexOf('*'));
			// returnMap.put("ERRMSG", errmsg);

		}

		// 오름차순 정렬
		TreeMap<String, String> treeMap = new TreeMap<String, String>(sortMap);
		Iterator<String> treeMapIter = treeMap.keySet().iterator();
		while (treeMapIter.hasNext()) {
			chkSb.append(treeMap.get(treeMapIter.next())).append(" ");
		}

		// 할당전략 안맞을때
		if (chkSb.toString().length() > 2) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			throw new SQLException(chkSb.toString() + "는 할당전략에 맞는 재고가 없습니다.");
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < docNumList.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append("'").append(docNumList.get(i)).append("'");
		}

		map.setModuleCommand("WmsOutbound_COPY", "DL06HEADSAVE");
		map.put("TASKKYS", sb.toString());

		DataMap rtnMap = new DataMap();
		rtnMap.put("data", commonDao.getList(map));

		return rtnMap;
	}

	@Transactional
	public List<DataMap> dl06HeadNew(DataMap map) throws SQLException {
		map.setModuleCommand("WmsOutbound_COPY", "MDL16");
		List<DataMap> list = commonDao.getList(map);

		return list;

	}

	// [DL05] 출고 검수 저장(SHPDI 검품수량 UPDATE)
	@Transactional
	public DataMap saveDL05(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");
		List<DataMap> itemList = map.getList("list");
		List<DataMap> selectList = new ArrayList<DataMap>();
		String HeadGridShpoky = map.get("HeadGridShpoky").toString();

		DataMap row;
		DataMap itemRow;
		List shpokyList = new ArrayList();
		DataMap validMap = new DataMap();
		DataMap totalQtyMap = new DataMap();

		int count = 0;
		int validationCount = 0;

		DataMap rsRowMap = new DataMap();
		DataMap rsMap = new DataMap();
		DataMap rqMap = new DataMap();

		// SHPDH I/F CHECK
		for (int j = 0; j < head.size(); j++) {
			rsRowMap = head.get(j).getMap("map");
			rsMap.setModuleCommand("WmsTask_COPY", "VALIDATION_SHPDH");
			rsMap.put("SHPOKY", rsRowMap.get("SHPOKY"));
			rqMap = commonDao.getMap(rsMap);

			if (rqMap != null && !rqMap.getString("SHPOKY").equals(" ")) {
				rsMap.put("SHPOKY", rqMap.getString("SHPOKY"));

				return rsMap;
			}

		}

		for (int i = 0; i < head.size(); i++) {
			String shpokyNum = head.get(i).getMap("map").getString("SHPOKY");
			shpokyList.add(shpokyNum);
			
			
			commonDao.update("WmsOutbound_COPY.DL05ZERO", head.get(i).getMap("map"));
	
			// 여러개 head를 체크했을 경우
			// 화면에 보여지는 item의 shpoky는 화면에 입력한 검수수량으로 update
			if (head.get(i).getMap("map").getString("SHPOKY").equals(HeadGridShpoky)) {
				// validation
				if (HeadGridShpoky.equals(itemList.get(0).getMap("map").getString("SHPOKY"))) {
					selectList = itemList;
				}

				for (int j = 0; j < selectList.size(); j++) {
					itemRow = selectList.get(j).getMap("map");
					
					
					
					String itemStrQtaloc = itemRow.getString("QTALOC");

					String itemStrQtjcmp = itemRow.getString("QTCOMP");
					String itemStrQtshpc = itemRow.getString("QTSHPC");
					// int itemStrDbqtshpc = Integer.parseInt(itemRow.getString("DBQTSHPC"));

					// validation
					// itemRow.put("WAREKY", head.get(i).getMap("map").getString("WAREKY"));
					// validMap = commonDao.getMap("WmsOutbound_COPY.DL05Validation1", itemRow);

					// String validMsg = validMap.getString("MSG");

					String validMsg = "OK";
					if ("OK".equals(validMsg)) {

						itemRow.put("QTSHPC", itemStrQtjcmp);
						itemRow.put("LSTTFL", "V");
						itemRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

						commonDao.update("WmsOutbound_COPY.DL21Sub", itemRow);

						// totalQtyMap = commonDao.getMap("WmsOutbound_COPY.TOTAL_QTCOMP", itemRow);
						// itemRow.put("QTSHPC", totalQtyMap.get("QTCOMP"));
						itemRow.put("QTSHPC", itemStrQtjcmp);

						commonDao.update("WmsOutbound_COPY.DL05Sub", itemRow);

						rsMap.put("MSG", validMsg);

					} else {
						rsMap.put("MSG", validMsg);
						validationCount++;
						break;
					}
					count++;
				}

			} else {// 화면에 보여지지 않는 shpoky는 db의 피킹완료수량으로 검수수량에 update
				row = head.get(i).getMap("map");

				// validation
				// validMap = commonDao.getMap("WmsOutbound_COPY.DL05Validation2", row);

				// String validMsg = validMap.getString("MSG");

				String validMsg = "OK";
				if ("OK".equals(validMsg)) {
					// SHPDI SELECT
					List<DataMap> list = commonDao.getList("WmsOutbound_COPY.DL05Sub", row);

					for (int j = 0; j < list.size(); j++) {
						itemRow = list.get(j);
						
						if(j == 0) {
							commonDao.update("WmsOutbound_COPY.DL05ZERO", itemRow);
						}
						
						String itemStrQtaloc = itemRow.getString("QTALOC");
						String itemQtaloc = itemRow.getString("QTALOC");
						String itemQtjcmp = itemRow.getString("QTJCMP");
						String itemQtshpc = itemRow.getString("QTSHPC");

						itemRow.put("QTSHPC", itemQtjcmp);
						itemRow.put("LSTTFL", "V");
						itemRow.put(CommonConfig.SES_USER_WHAREHOUSE_KEY,
								map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));
						itemRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

						commonDao.update("WmsOutbound_COPY.DL21Sub2", itemRow);

						commonDao.update("WmsOutbound_COPY.DL05Sub", itemRow);
						rsMap.put("MSG", validMsg);

					}
				} else {
					rsMap.put("MSG", validMsg);
					validationCount++;
					break;
				}
				count++;
			}

			StringBuilder sb = new StringBuilder();
			for (int k = 0; k < shpokyList.size(); k++) {
				if (k != 0) {
					sb.append(",");
				}
				sb.append("'").append(shpokyList.get(k)).append("'");
			}

			rsMap.put("SHPOKY", sb.toString());
			rsMap.put("CNT", count);
		}

		return rsMap;
	}

	/**
	 * WDL00 : 삭제
	 */
	@Transactional
	public int waveDelete(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");
		List<DataMap> selectList = new ArrayList<DataMap>();
		int count = 0;

		DataMap row;
		DataMap itemRow;
		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

			commonDao.delete("WmsOutbound_COPY.SHPDI", row);
			commonDao.delete("WmsOutbound_COPY.SHPDH", row);

			// 원 출고문서 조회
			row.setModuleCommand("WmsOutbound_COPY", "WAVECANCEL_SHPOKY");
			selectList = commonDao.getList(row);
			for (int j = 0; j < selectList.size(); j++) {
				itemRow = selectList.get(j).getMap("map");

//					commonDao.delete("WmsOutbound_COPY.SHPDI", itemRow);
//					commonDao.delete("WmsOutbound_COPY.SHPDH", itemRow);	
				itemRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				commonDao.update("WmsOutbound_COPY.WAVECANCEL_SHPDI", itemRow);

				itemRow.put("PGRC01", " ");
				itemRow.put("STATDO", "NEW");
				itemRow.put("DOCTXT", " ");
				itemRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				commonDao.update("WmsOutbound_COPY.WDL00_SHPDH", itemRow);
			}
			count++;
		}
		return count;
	}

	/**
	 * WDL04 : 그룹오더할당(상품)
	 */
	@Transactional
	public DataMap totalAssignWDL04(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");
		List<DataMap> item = map.getList("item");

		DataMap row = new DataMap();
		DataMap itemrow = new DataMap();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");
			map.clonSessionData(row);

			// 1. 그룹오더인지 체크
			row.setModuleCommand("WmsOutbound_COPY", "CNTGRPDOC");
			int count = commonDao.getMap(row).getInt("CNT");
			if (count > 0) {
				// 2021.09.09 통합작업번호 --> ITEM별 할당처리로 변경
				// 2. 통합작업번호로 할당처리
				// commonDao.getMap("WmsOutbound_COPY.ALLOC_TOTSHIP_PROCALL", row);
				// commonDao.getMap("WmsOutbound_COPY.ALLOC_TOTSUB_PROCALL", row);
				// }

				// 2.ITEM별 할당처리
				for (int j = 0; j < item.size(); j++) {
					itemrow = item.get(j).getMap("map");
					map.clonSessionData(itemrow);

					if (row.getString("SHPOKY").equals(itemrow.getString("SHPOKY"))) {
						commonDao.getMap("WmsOutbound_COPY.ALLOC_ITEM_PROCALL", itemrow);
					} else {
						row.put("SHPOIT", "%");
						commonDao.getMap("WmsOutbound_COPY.ALLOC_ITEM_PROCALL", row);
					}
				}
			}
			if (i != 0) {
				sb.append(",");
			}
			sb.append("'").append(row.getString("SHPOKY")).append("'");
		}
		DataMap rsMap = new DataMap();
		rsMap.put("SHPOKY", sb.toString());
		return rsMap;
	}

	@Transactional
	public String SaveWDL05(DataMap map) throws SQLException {
		String result = "OK";

		List<DataMap> list = map.getList("list");
		List<DataMap> Tablist = new ArrayList<DataMap>();

		DataMap row;
		DataMap Tabrow;

		// DataMap Tablist;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			map.clonSessionData(row);

			row.put("STATIT", "FCK"); // 검수완료처리
			commonDao.update("WmsOutbound_COPY.WDL05", row);

			// 해당 Gropky에 대한 list
//			Tablist = commonDao.getList("WmsOutbound_COPY.WDL05Tab", row);
//			if(Tablist.size() > 1){
//				for(int j=0; j<Tablist.size(); j++){
//					Tabrow = Tablist.get(j).getMap("map");
//					map.clonSessionData(Tabrow);
//					
//					int qtshpc = parseInt(row.get("QTSHPC"));
//					int qtjcmp = parseInt(Tabrow.get("QTJCMP"));
//					int divide = parseInt(qtshpc/qtjcmp);
//					
//					if(!("0".equals(divide))) {
//						Tabrow.put("QTSHPC", qtjcmp);
//					}else {
//						Tabrow.put("QTSHPC", qtshpc%qtjcmp);
//					}
//					qtshpc = qtshpc - qtjcmp;
//					row.put("STATIT", "FCK"); //검수완료처리
//					commonDao.update("WmsOutbound_COPY.WDL05", Tabrow);
//				}
//			}
		}
		return result;
	}

	private int parseInt(Object object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Transactional
	public String CheckCancel(DataMap map) throws SQLException {
		String result = "OK";
		// List<DataMap> head = map.getList("head");
		// List<DataMap> list = map.getList("item");
		try {

			int headModifyIndex = -1;
			List<DataMap> itemList = null;
			if (map.containsKey("headModifyIndex")) {
				headModifyIndex = map.getInt("headModifyIndex");
				itemList = map.getList("item");
			}

			List<DataMap> headList = map.getList("head");

			DataMap row;

			List docNumList = new ArrayList();
			List<DataMap> paramItemList = new ArrayList<DataMap>();
			DataMap copy = new DataMap();
			DataMap itemMap = new DataMap();

			for (int i = 0; i < headList.size(); i++) {
				if (map.containsKey("param")) {
					row = new DataMap(map.getMap("param").getMap("map"));
				} else {
					row = new DataMap();
				}

				map.clonSessionData(row);
				row.append(headList.get(i).getMap("map"));
				docNumList.add(headList.get(i).getMap("map").get("SHPOKY"));

				paramItemList = new ArrayList<DataMap>();
				if (headModifyIndex == i) {
					for (int j = 0; j < itemList.size(); j++) {
						copy = new DataMap(itemList.get(j).getMap("map"));
						paramItemList.add(copy);
					}
				} else {
					row.setModuleCommand("WmsOutbound_COPY", "DL08Sub");
					List<DataMap> dl08subList = commonDao.getList(row);
					for (int j = 0; j < dl08subList.size(); j++) {
						copy = new DataMap(dl08subList.get(j));
						paramItemList.add(copy);
					}
				}

				String statit = "";
				for (int j = 0; j < paramItemList.size(); j++) {
					itemMap = paramItemList.get(j);
					map.clonSessionData(itemMap);

					statit = itemMap.getString("STATIT");
					// 1. 출하문서번호 검수수량 취소 업데이트
					if ("PCK".equals(statit) || "FCK".equals(statit)) {
						itemMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
						commonDao.update("WmsOutbound_COPY.WDL05Cancel", itemMap);

						// 2. 그룹오더인지 체크하여 원출고문서 출하완료 처리
						itemMap.setModuleCommand("WmsOutbound_COPY", "CNTGRPDOC");
						int countCHK = commonDao.getMap(itemMap).getInt("CNT");
						if (countCHK > 0) {
							// 2-1. 원출고문서 검수취소완료 처리
							itemMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
							commonDao.update("WmsOutbound_COPY.WDL05GroupCancel", itemMap);
						} else {
							// 2-2. 그룹오더 출하문서번호 검수수량 취소 업데이트
							if (itemMap.getString("REFDKY") != null && !itemMap.getString("REFDKY").equals("")) {
								itemMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
								itemMap.put("SHPOKY", itemMap.getString("REFDKY"));
								itemMap.put("SHPOIT", itemMap.getString("REFDIT"));
								commonDao.update("WmsOutbound_COPY.WDL05Cancel", itemMap);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			//log.debug("CheckCancel validataion error = " + e);
		}
		//log.debug(":::::::::::::::::::::" + result);

		return result;
	}

	@Transactional
	public String validateInsepectSave(DataMap map) throws SQLException {
		String result = "OK";

		int headModifyIndex = -1;
		List<DataMap> itemList = null;
		if (map.containsKey("headModifyIndex")) {
			headModifyIndex = map.getInt("headModifyIndex");
			itemList = map.getList("list");
		}

		List<DataMap> headList = map.getList("head");

		DataMap row;
		DataMap rsMap;

		List<DataMap> paramItemList = new ArrayList<DataMap>();
		DataMap copy = new DataMap();
		DataMap paramItemMap = new DataMap();
		DataMap paramLocMap = new DataMap();

		for (int i = 0; i < headList.size(); i++) {
			if (map.containsKey("param")) {
				row = new DataMap(map.getMap("param").getMap("map"));
			} else {
				row = new DataMap();
			}

			map.clonSessionData(row);
			row.append(headList.get(i).getMap("map"));
			paramItemList = new ArrayList<DataMap>();

			if (headModifyIndex == i) {
				for (int j = 0; j < itemList.size(); j++) {
					copy = new DataMap(itemList.get(j).getMap("map"));
					paramItemList.add(copy);
				}
			} else {
				row.setModuleCommand("WmsOutbound_COPY", "WDL05Sub");
				List<DataMap> tasdiList = commonDao.getList(row);
				for (int j = 0; j < tasdiList.size(); j++) {
					copy = new DataMap(tasdiList.get(j));
					copy.put("QTSHPC", tasdiList.get(j).getString("QTJCMP"));
					paramItemList.add(copy);
				}
			}

			StringBuffer sb = new StringBuffer();

			for (int j = 0; j < paramItemList.size(); j++) {
				paramItemMap = paramItemList.get(j);
				map.clonSessionData(paramItemMap);

				if (sb.length() > 0)
					sb.append(" UNION ALL\n ");

				sb.append(" SELECT '").append(paramItemMap.get("WAREKY")).append("' AS WAREKY, ");
				sb.append("        '").append(paramItemMap.get("SHPOKY")).append("' AS SHPOKY, ");
				sb.append("        '").append(paramItemMap.get("SHPOIT")).append("' AS SHPOIT, ");
				sb.append("        '").append(paramItemMap.get("QTSHPC")).append("' AS QTSHPC FROM DUAL ");

			}

			map.setModuleCommand("WmsOutbound_COPY", "WDL05");
			map.put("APPEND_QUERY", sb.toString());

			List<DataMap> validList = commonDao.getValidation(map);
			if (validList.size() > 0) {
				DataMap valid = validList.get(0);
				return valid.getString("RESULTMSG");
			}
		}

		return result;
	}

	@Transactional
	public DataMap saveInspectWDL05(DataMap map) throws SQLException {
		int headModifyIndex = -1;
		List<DataMap> itemList = null;
		if (map.containsKey("headModifyIndex")) {
			headModifyIndex = map.getInt("headModifyIndex");
			itemList = map.getList("list");
		}

		List<DataMap> headList = map.getList("head");

		DataMap row;
		DataMap rsMap = new DataMap();

		List<DataMap> paramItemList = new ArrayList<DataMap>();
		DataMap copy = new DataMap();
		DataMap paramItemMap = new DataMap();
		DataMap paramLocMap = new DataMap();

		List docNumList = new ArrayList();
		String docNum;
		for (int i = 0; i < headList.size(); i++) {
			if (map.containsKey("param")) {
				row = new DataMap(map.getMap("param").getMap("map"));
			} else {
				row = new DataMap();
			}

			map.clonSessionData(row);
			row.append(headList.get(i).getMap("map"));
			paramItemList = new ArrayList<DataMap>();

			docNum = row.getString("SHPOKY");

			if (headModifyIndex == i) {
				//log.debug("saveInspectWDL05 if");
				for (int j = 0; j < itemList.size(); j++) {
					copy = new DataMap(itemList.get(j).getMap("map"));
					paramItemList.add(copy);
				}
			} else {
				//log.debug("saveInspectWDL05 else");
				row.setModuleCommand("WmsOutbound_COPY", "WDL05Sub");
				List<DataMap> tasdiList = commonDao.getList(row);
				for (int j = 0; j < tasdiList.size(); j++) {
					copy = new DataMap(tasdiList.get(j));
					copy.put("QTSHPC", tasdiList.get(j).getString("QTJCMP"));
					paramItemList.add(copy);
				}
			}
			docNumList.add(docNum);

			for (int j = 0; j < paramItemList.size(); j++) {
				paramItemMap = paramItemList.get(j);
				map.clonSessionData(paramItemMap);

				// PPC, FPC일 경우에만 검수되도록
				if ("PPC".equals(paramItemMap.getString("STATIT")) || "FPC".equals(paramItemMap.getString("STATIT"))) {
					// 검수수량 업데이트
					paramItemMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
					paramItemMap.setModuleCommand("WmsOutbound_COPY", "DL05Sub");
					commonDao.update(paramItemMap);

					// 총괄SUB검수 호출
					paramItemMap.setModuleCommand("WmsOutbound_COPY", "CALL_BATCH_INSPECT_TOT");
					commonDao.getMap(paramItemMap);
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < docNumList.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append("'").append(docNumList.get(i)).append("'");
		}

		//log.debug("saveInspectWDL05 SHPOKY:" + sb.toString());
		rsMap.put("SHPOKY", sb.toString());

		return rsMap;
	}

	@Transactional
	public String saveInspectItemWDL05(DataMap map) throws SQLException {
		String result = "OK";

		List<DataMap> list = map.getList("list");
		DataMap row;

		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			map.clonSessionData(row);

			commonDao.update("WmsOutbound_COPY.WDL05Tab", row);

			// 해당 Gropky에 대한 list
			commonDao.update("WmsOutbound_COPY.WDL05", row);

		}
		return result;
	}

	// [DL55] 출고 검수 Scan 저장(SHPDI 검품수량 UPDATE)
	@Transactional
	public DataMap saveDL55(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("list");
		DataMap itemRow;
		DataMap validMap;
		DataMap GroupMap;
		DataMap SumQtshpcMap;
		DataMap rsMap = new DataMap();

		int count = 0;
		int validationCount = 0;
		String progid = map.getString("PROGID");

		for (int j = 0; j < list.size(); j++) {
			itemRow = list.get(j).getMap("map");

			// validation
			itemRow.put("WAREKY", map.getString("WAREKY"));
			validMap = commonDao.getMap("WmsOutbound_COPY.DL55Validation", itemRow);

			String validMsg = validMap.getString("MSG");
			if ("OK".equals(validMsg)) {
				String itemStrQtaloc = itemRow.getString("QTALOC");
				String itemStrQtjcmp = itemRow.getString("QTJCMP");
				int itemQtaloc = Integer.parseInt(itemRow.getString("QTALOC"));
				int itemQtjcmp = Integer.parseInt(itemRow.getString("QTJCMP"));
				int itemQtshpc = Integer.parseInt(itemRow.getString("QTSHPC"));
				int itemDBQtshpc = Integer.parseInt(itemRow.getString("DBQTSHPC"));

				// 할당수량이 0이면 안되고, 피킹수량이 0이면 검수 진행 불가능
				if (!("0".equals(itemStrQtaloc))) {
					if (!("0".equals(itemStrQtjcmp))) {
						// 그룹오더 키값 가져오기 위한 SELECT
						itemRow.put("WAREKY", map.getString("WAREKY"));
						itemRow.put("QTSHPC", itemQtshpc + itemDBQtshpc);
						itemRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

						commonDao.update("WmsOutbound_COPY.DL05Sub", itemRow); // 기존 오더 UPDATE

						// 그룹오더 검수수량 UPDATE
						if (progid.equals("WDL55")) {
							GroupMap = commonDao.getMap("WmsOutbound_COPY.WDL55GroupKey", itemRow); // 출고번호로 그룹오더번호 가져오는
																								// SELECT

							GroupMap.put("WAREKY", map.getString("WAREKY"));
							SumQtshpcMap = commonDao.getMap("WmsOutbound_COPY.WDL55GroupQtshpc", GroupMap); // 그룹오더 SELECT

							int GroupItemDBQtshpc = Integer.parseInt(SumQtshpcMap.getString("QTSHPC"));

							GroupMap.put("GRQTSHPC", itemQtshpc + GroupItemDBQtshpc);
							GroupMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

							GroupMap.put("GSHPOKY", GroupMap.getString("REFDKY"));
							GroupMap.put("GSHPOIT", GroupMap.getString("REFDIT"));
							commonDao.update("WmsOutbound_COPY.WDL55GroupSub", GroupMap); // 그룹 오더 UPDATE
						}

						rsMap.put("MSG", validMsg);
					}
				}
			} else {
				rsMap.put("MSG", validMsg);
				validationCount++;
				break;
			}
			count++;
		}

		return rsMap;
	}

	@Transactional
	public DataMap DL05CheckCancel(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");
		List<DataMap> list = map.getList("list");
		List<DataMap> selectList = new ArrayList<DataMap>();
		DataMap rsMap = new DataMap();
		DataMap totalQtyMap = new DataMap();

		String HeadGridShpoky = map.get("HeadGridShpoky").toString();

		try {
			List docNumList = new ArrayList();
			DataMap row;
			DataMap itemRow;

			for (int i = 0; i < head.size(); i++) {
				docNumList.add(head.get(i).getMap("map").get("SHPOKY"));

				row = head.get(i).getMap("map");

				if (!head.get(i).getMap("map").getString("SHPOKY").equals(HeadGridShpoky)) {

					// SHPDI SELECT
					list = commonDao.getList("WmsOutbound_COPY.DL08Sub", row);

				}
				for (int j = 0; j < list.size(); j++) {
					itemRow = list.get(j).getMap("map");

					String statit = itemRow.getString("STATIT");
					// 1. 출하문서번호 검수수량 취소 업데이트
					if ("PCK".equals(statit) || "FCK".equals(statit)) {
						itemRow.put("WAREKY", map.getString("WAREKY"));
						itemRow.put("LSTTFL", " ");
						itemRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

						commonDao.update("WmsOutbound_COPY.DL21Sub", itemRow);

						totalQtyMap = commonDao.getMap("WmsOutbound_COPY.TOTAL_QTCOMP", itemRow);
						itemRow.put("QTSHPC", totalQtyMap.get("QTCOMP"));

						commonDao.update("WmsOutbound_COPY.DL05Cancel", itemRow);

					}
				}

				StringBuilder sb = new StringBuilder();
				for (int k = 0; k < docNumList.size(); k++) {
					if (k != 0) {
						sb.append(",");
					}
					sb.append("'").append(docNumList.get(k)).append("'");
				}
				rsMap.put("SHPOKY", sb.toString());

			}
		} catch (Exception e) {
			//log.debug("CheckCancel validataion error = " + e);
		}
		//log.debug("DL05CheckCancel::::::::::::::::::::: " + rsMap);

		return rsMap;
	}

	@Transactional
	public DataMap waveCreate(DataMap map) throws SQLException {

		List<DataMap> head = map.getList("head");
		List<DataMap> itemList = map.getList("list");
		List<DataMap> selectList = new ArrayList<DataMap>();
		int count = 0;
		String HeadGridSvbeln = map.get("HeadGridSvbeln").toString();
		String HeadGridShpoky = map.get("HeadGridShpoky").toString();
		String SelectDocgrp = map.get("GSHTYP").toString(); // 그룹출고유형

		int headModifyIndex = -1;
		if (map.containsKey("headModifyIndex")) {
			headModifyIndex = map.getInt("headModifyIndex");
		}

		DataMap row = new DataMap();
		String docNum;
		int itemCount = 0;

		String progress01 = wmsService.getNumSeq("NR07");

		//log.debug("waveCreate processID:::::::::::::::::::::" + progress01 + "/docgrp:::::" + SelectDocgrp);
		for (int i = 0; i < head.size(); i++) {
			row = head.get(i).getMap("map");
			// 1-1. 출고문서번호가 없을 때 저장 후, 할당
			if ("".equals(row.getString("SHPOKY").trim())) {
				docNum = wmsService.getDocSeq(row.getString("DOCUTY"));
				row.put("SHPOKY", docNum);
				row.put("PGRC01", progress01);
				row.put("STATDO", "REF"); // 2021.08.25 JHJ 원출하문서상태를 REF로 한다.
				row.setModuleCommand("WmsOutbound_COPY", "SHPDH");
				commonDao.insert(row);

				if (headModifyIndex == i) {
					row.setModuleCommand("WmsOutbound_COPY", "SHPDI");
					for (int j = 0; j < itemList.size(); j++) {
						row.append(itemList.get(j).getMap("map"));

						itemCount += 10;
						String snum = String.valueOf(itemCount);
						String inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;
						row.put("SHPOKY", docNum);
						row.put("SHPOIT", inum);
						row.put("STATIT", "REF"); // 2021.08.25 JHJ 원출하문서상태를 REF로 한다.
						commonDao.insert(row);
					}
				} else {
					row.setModuleCommand("WmsOutbound_COPY", "DL04Sub");
					selectList = commonDao.getList(row);
					for (int j = 0; j < selectList.size(); j++) {
						row.append(selectList.get(j).getMap("map"));

						row.setModuleCommand("WmsOutbound_COPY", "SHPDI");

						itemCount += 10;
						String snum = String.valueOf(itemCount);
						String inum = String.valueOf("000000").substring(0, (6 - snum.length())) + snum;

						row.put("SHPOKY", docNum);
						row.put("SHPOIT", inum);
						row.put("STATIT", "REF"); // 2021.08.25 JHJ 원출하문서상태를 REF로 한다.
						commonDao.insert(row);
					}
				}
				// 1-2. 출고문서번호가 있을 때 업데이트
			} else {
				row.put("PGRC01", progress01);
				row.put("STATDO", "REF"); // 2021.08.25 JHJ 원출하문서상태를 REF로 한다.
				row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

				row.setModuleCommand("WmsOutbound_COPY", "WDL00_SHPDH");
				commonDao.update(row);
			}
		}

		// 2. 그룹오더 출고문서 생성 호출
		row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
		row.put("PGRC01", progress01);
		row.put("SHPMTY", SelectDocgrp);
		commonDao.getMap("WmsOutbound_COPY.TOTALSHIPMENT_PROCALL", row);

		DataMap rsMap = new DataMap();
		rsMap.put("PGRC01", progress01);

		return rsMap;
	}

	//@Transactional(rollbackFor = Exception.class)

	public DataMap sendEcmsReal(DataMap map) throws SQLException {
		long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
        
		DataMap rsMap = new DataMap();
		String MSG = "OK";
		int count = 0;

		int headModifyIndex = -1;
		List < DataMap > itemList = null;

		if (map.containsKey("headModifyIndex")) {
		    headModifyIndex = map.getInt("headModifyIndex");
		    itemList = map.getList("list");
		}

		List < DataMap > headList = map.getList("loopHeadList");

		DataMap row;

		List docNumList = new ArrayList();
		List < DataMap > paramItemList = new ArrayList < DataMap > ();
		DataMap copy = new DataMap();
		DataMap itemMap = new DataMap();
		String headINDCHK = "";				
	    
		for (int i = 0; i < headList.size(); i++) {
			TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		    TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
		    Object transactionSavepoint = transactionStatus.createSavepoint();
		    
		    
		    List < DataMap > ifidList = new ArrayList < DataMap > (); // IFID : 인터페이스 전송 목록
		    List < String > rcpttyList = new ArrayList < String > (); // IF 실적 : 출고유형
		    DataMap rcpttyMap = new DataMap(); // IF 실적 : 출고유형별 출고헤더키
		    
		    String shpoky =headList.get(i).getMap("map").getString("SHPOKY");
		    
		    try {

		        if (map.containsKey("param")) {
		            row = new DataMap(map.getMap("param").getMap("map"));
		        } else {
		            row = new DataMap();
		        }

		        map.clonSessionData(row);
		        row.append(headList.get(i).getMap("map"));
		        docNumList.add(headList.get(i).getMap("map").get("SHPOKY"));

		        paramItemList = new ArrayList < DataMap > ();
		        if (headModifyIndex == i) {
		            for (int j = 0; j < itemList.size(); j++) {
		                copy = new
		                DataMap(itemList.get(j).getMap("map"));
		                paramItemList.add(copy);
		            }
		        } else {
		            row.setModuleCommand("WmsOutbound_COPY", "DL08Sub");
		            List < DataMap > dl08subList =
		                commonDao.getList(row);
		            for (int j = 0; j < dl08subList.size(); j++) {
		                copy =
		                    new DataMap(dl08subList.get(j));
		                paramItemList.add(copy);
		            }
		        }

		        for (int j = 0; j < paramItemList.size(); j++) {
		            itemMap =
		                paramItemList.get(j);

		            // 팔렛트는 제외 YSO 
		            if (isEmpty(itemMap.getString("SHPOIT"))) {		            			            	
		            	continue;
		            }else if(itemMap.getString("SHPOIT").substring(0, 1).equals("1")){
		            	continue;
		            }

		        map.clonSessionData(itemMap);

		        itemMap.setModuleCommand("WmsOutbound_COPY", "DL08SHPDI");
		        commonDao.update(itemMap);

		        // 해당 Gropky에 대한 list 
		        if (itemMap.getString("REFDKY") != null &&
		        !itemMap.getString("REFDKY").equals("") &&
		            !itemMap.getString("REFDKY").equals(" ")) {
		        itemMap.put("SHPOKY",
		            itemMap.get("REFDKY"));
		        itemMap.put("SHPOIT", itemMap.get("REFDIT"));
		        commonDao.update("WmsOutbound_COPY.DL08SHPDI", itemMap);
		    }
		}

		row.setModuleCommand("WmsOutbound_COPY", "DL08SHPDH");
		commonDao.update(row);

		// 5. 전송 프로시저 호출
		// row.put("DOCUKY", row.get("SHPOKY")); 
		//row.put("DOFLAG", "Z"); 
		// row.setModuleCommand("WmsOutbound_COPY","CALL_AIRWMS_DOCUMENT_PROCESS"); // commonDao.getMap(row);

		// 6. 그룹오더인지 체크하여 원출고문서 출하완료 처리 
		row.setModuleCommand("WmsOutbound_COPY",
		"CNTGRPDOC");
		int countCHK = commonDao.getMap(row).getInt("CNT");
		if (countCHK > 0) {
		    commonDao.getMap("WmsOutbound_COPY.CALL_BATCH_COMP_TOT", row);
		}

		if ("221".equals(row.getString("SHPMTY"))) {
		    commonDao.getMap("WmsOutbound_COPY.CALL_COA_STO", row);       
		}

		// 출고완료시 실적 IF 데이터 추출 
		String I_RCPTTY = row.getString("SHPMTY"); String
		I_IFID = row.getString("IFID");
		String I_OWNRKY = row.getString("OWNRKY");
		String I_RECVKY = row.getString("SHPOKY");

		// IF 실적 출고유형별 입고키 넣기 
		if (!rcpttyList.contains(I_RCPTTY)) {
		rcpttyList.add(I_RCPTTY);
		rcpttyMap.put(I_RCPTTY, "" + I_RECVKY + "");
		rcpttyMap.put(I_RCPTTY + "_IFID", I_IFID);
		rcpttyMap.put(I_RCPTTY +
		    "_OWNRKY", I_OWNRKY);
		}
		else {
		    String docNums =
		        rcpttyMap.getString(I_RCPTTY);
		    if (docNums != null &&
		        !"".contentEquals(docNums.trim())) {
		        docNums += "," + I_RECVKY + "";
		    } else {
		        docNums += "" + I_RECVKY + "";
		    }
		    rcpttyMap.put(I_RCPTTY, docNums);
		}
		count++;

		// 실적 인터페이스 저장 
		dataInsertIF(map, rcpttyList, rcpttyMap, ifidList, false);

		if (null != ifidList && ifidList.size() > 0) {

		    // 인터페이스 전송 
			DataMap resultMap;

		    for (DataMap item: ifidList) {
		        item.put("ownrky", map.getString("OWNRKY"));
		        item.put("IFID", item.getString("IFID"));
		        item.put(CommonConfig.SES_USER_ID_KEY,
		            map.get(CommonConfig.SES_USER_ID_KEY));		        		     
		        	resultMap = sendIf(item);
		        
	        	if (ifidList.size() == 1 && item.getString("RFCTYPE").equals("SAP") && ("S".equals(resultMap.getString("sendMsg")) || "SUCCESS".equals(resultMap.getString("sendMsg")))) {

		            transactionManager.commit(transactionStatus);

		        }else if (ifidList.size() > 1 && item.getString("RFCTYPE").equals("SAP") && ("S".equals(resultMap.getString("sendMsg")) || "SUCCESS".equals(resultMap.getString("sendMsg")))) {

		        	transactionSavepoint = transactionStatus.createSavepoint();

		        } else if (!"S".equals(resultMap.getString("sendMsg")) && !"SUCCESS".equals(resultMap.getString("sendMsg"))) {
		        	
		            String RFCTYPE;

		            if (item.getString("RFCTYPE").equals("SAP")) {

		            	transactionStatus.rollbackToSavepoint(transactionSavepoint);

		                RFCTYPE = "SAP : ";
		                MSG = RFCTYPE + resultMap.getString("sendMsg");
		                rsMap.put("MSG", MSG);		                
		                rsMap.put("ERRTXT", resultMap.getString("sendMsg"));
						rsMap.put("ZDOCNO", shpoky);										
						rsMap.put("WAREKY", map.getString(CommonConfig.SES_USER_WHAREHOUSE_KEY));
						rsMap.put("PROGID", map.getString("menuId"));
						rsMap.put(CommonConfig.SES_USER_ID_KEY, map.getString(CommonConfig.SES_USER_ID_KEY));

						rsMap.setModuleCommand("WmsOutbound_COPY", "IFSAPLOG");
						commonDao.insert(rsMap);
						transactionManager.commit(transactionStatus);
		                break;
		            } else {
		            	
		                RFCTYPE = "MES";
		                MSG = resultMap.getString("sendMsg");
		                
		                rsMap.put("MSG", RFCTYPE+":"+MSG);
		                rsMap.put("MSG_TYPE", RFCTYPE);
		                rsMap.put("MSG_DOC_NO", shpoky);	
		                transactionManager.commit(transactionStatus);
		            }
		        }else if (item.getString("RFCTYPE").equals("MES") && ("S".equals(resultMap.getString("sendMsg")) || "SUCCESS".equals(resultMap.getString("sendMsg")))) {

		            transactionManager.commit(transactionStatus);

		        }

		    } //send for

		}

		// 실적 인터페이스 전송 목록
		rsMap.put("ifidList", ifidList);

		} catch (Exception e) {
			transactionStatus.rollbackToSavepoint(transactionSavepoint);
			transactionManager.commit(transactionStatus);
			rsMap.put("MSG", e.getMessage());
			return rsMap;
		}
		    
		} // head for

			rsMap.put("ERRNUM", 0);
			
			//실험할 코드 추가
	        
			long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
			long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
			System.out.println("전체 거린시간(m) : "+secDiffTime);
			
			return rsMap;
		}

	@Transactional
	public Object totalPickingCreate(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("list");

		int count = 0;

		DataMap reqMap = new DataMap();

		// stknum 조회
		reqMap.setModuleCommand("WmsOutbound_COPY", "TOTAL_PICKING_STKNUM");

		Map<String, Object> paramMap = (Map<String, Object>) commonDao.getObject(reqMap);

		DataMap row;

		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));
			row.putAll(paramMap);

			reqMap.setModuleCommand("WmsOutbound_COPY", "TOTAL_PICKING_DUPLICATE_CHECK");
			reqMap.put("SHPOKY", row.get("SHPOKY"));
			String shpoky = (String) commonDao.getObject(reqMap);

			if (!isEmpty(shpoky)) {

				DataMap resMap = new DataMap();
				resMap.put("RESULT_VALUE", shpoky);

				return resMap;
			}

			commonDao.insert("WmsOutbound_COPY.TOTAL_PICKING_CREATE", row);
			count++;
		}

		return count;
	}

	@Transactional
	public Object totalPickingDelete(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("list");

		int count = 0;

		DataMap row;

		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

			commonDao.delete("WmsOutbound_COPY.TOTAL_PICKING_DELETE", row);
			count++;
		}

		return count;
	}

	@Transactional
	public int saveDL12Sub(DataMap map) throws SQLException {
		List<DataMap> list = map.getList("list");
		int count = 0;

		DataMap row;
		for (int i = 0; i < list.size(); i++) {
			row = list.get(i).getMap("map");
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

			commonDao.update("WmsOutbound_COPY.DL12_SHPDI", row);
			count++;
		}
		return count;
	}

	@Transactional
	public Object DL06POPSave2(DataMap map) throws SQLException {
		List<DataMap> head = map.getList("head");
		List<DataMap> list = map.getList("list");
		DataMap row;

		String ResultMsg = null;

		for (int i = 0; i < head.size(); i++) {

			row = head.get(i).getMap("map");

			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));
			row.put("OLDLOC", "");
			row.put("CANCELTYPE", "CHANGE");

			commonDao.getMap("WmsOutbound_COPY.CALL_PICKING_CANCEL_PROC_DL06", row); // 피킹취소
			commonDao.delete("WmsOutbound_COPY.DL04REMOVE1", row); // 할당취소
		}

		return ResultMsg;

	}

	public DataMap saveDL08(DataMap map) throws Exception {
		
		TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        
		List<DataMap> headList = map.getList("headList");
		List<DataMap> itemList = map.getList("itemList");
		DataMap row;

		DataMap rsMap = new DataMap();

		int count = 0;
		int totalcount = 0;
		int deletecount = 0;
		
		for (int i = 0; i < headList.size(); i++) {

			row = headList.get(i).getMap("map");

			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

			commonDao.update("WmsOutbound_COPY.DL08", row); // 전기일자 변경
		}

		List<DataMap> list = new ArrayList<DataMap>();
		try {
			list = map.getList("itemList");
			DataMap ifRow = new DataMap();
			
			int listSize = list.size();
			if (listSize > 0) {
				for (int i = 0; i < listSize; i++) {
					DataMap itemRow = list.get(i).getMap("map");
					map.clonSessionData(itemRow);

					String itemStrQtshpd = itemRow.getString("QTSHPD");

					itemRow.put("SHPOKY", headList.get(0).getMap("map").get("SHPOKY"));
					itemRow.put("SVBELN", headList.get(0).getMap("map").get("SVBELN"));
					itemRow.put("QTSHPD", itemStrQtshpd);
					itemRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

					itemRow.setModuleCommand("WmsOutbound_COPY", "DL08Sub"); // 팔레트정보 입력

					String gRowStatus = itemRow.getString(CommonConfig.GRID_ROW_STATE_ATT);
					switch (gRowStatus) {
					case "C":

						itemRow.setModuleCommand("WmsOutbound_COPY", "SEARCHMAXSHPOIT");
						List<DataMap> shpdiMaxList = commonDao.getList(itemRow);

						int shpoit = Integer.parseInt(shpdiMaxList.get(0).getString("SHPOIT"));

						shpoit += 10;

						itemRow.put("SHPOIT", "1" + leftPad((shpoit) + "", "0", 5));

						itemRow.setModuleCommand("WmsOutbound_COPY", "DL08Sub"); // 팔레트정보 입력
						commonDao.insert(itemRow);
						count++;						
						break;
					case "U":
						count += commonDao.update(itemRow);
						break;
					case "D":
						count += commonDao.delete(itemRow);
						deletecount++;
						break;
					default:
						break;
					}
				}
				
				String shpoky = headList.get(0).getMap("map").getString("SHPOKY");
				
				ifRow.put("SHPOKY", shpoky);
				
				String sendType;
				
				
				 ifRow.setModuleCommand("WmsOutbound_COPY", "DL08_PT"); 
				 List<DataMap> palletMapList = commonDao.getList(ifRow);
				 
				
				if(palletMapList.size() == 0 && deletecount > 0) {
					sendType = "D";
				}else {
					sendType = "C";
					list.clear();
					list = palletMapList;
				}				
				
				
				String logseq;				
				
				logseq = ifRow.getString("LOGSEQNO");

				ifRow.setModuleCommand("Erpif", "IFWMSLOGSEQNO");
				logseq = commonDao.getMap(ifRow).getString("LOGSEQNO");
				
				
				ifRow = new DataMap();
				
				
				List<DataMap> chkList = new ArrayList<DataMap>();
				
				for(DataMap chkRow : list) {
										
					chkRow.getMap("map").put("STATE", sendType);
					chkRow.getMap("map").put("QTSHPD", Double.parseDouble(chkRow.getMap("map").getString("QTSHPD")));
					
					if(chkRow.getMap("map").getString("SHPOIT").substring(0, 1).equals("1")) {
						chkList.add(chkRow);
					};
				}
				
				
				
				ifRow.put("list", chkList);
				ifRow.put("STATE", sendType);
				ifRow.put("ownrky",map.get(CommonConfig.SES_USER_OWNER_KEY));
				ifRow.put("IFID", "WMS_IF_780");
				ifRow.put("LOGSEQNO", logseq);
				ifRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
							
									
				
				// 출고실적등록 인터페이스 호출

				String insertErrMsg = iFService.dataInsertIF(ifRow);

				//log.debug("insertErrMsg::" + insertErrMsg);

				DataMap resultMap = new DataMap();
				resultMap = sendIf(ifRow);

				if (!"S".equals(resultMap.getString("sendMsg")) && !"SUCCESS".equals(resultMap.getString("sendMsg"))) {

					transactionManager.rollback(transactionStatus);

					rsMap.put("MSG", "SAP : " + resultMap.getString("sendMsg"));
					rsMap.put("ERRTXT", resultMap.getString("sendMsg"));
					rsMap.put("ZDOCNO", shpoky);
					rsMap.put("WAREKY", map.getString(CommonConfig.SES_USER_WHAREHOUSE_KEY));
					rsMap.put("PROGID", map.getString("menuId"));
					rsMap.put(CommonConfig.SES_USER_ID_KEY, map.getString(CommonConfig.SES_USER_ID_KEY));

					rsMap.setModuleCommand("WmsOutbound_COPY", "IFSAPLOG");
					commonDao.insert(rsMap);
				} else {
					transactionManager.commit(transactionStatus);

				}

			}

		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	
		return rsMap;

	}

	//@Transactional(rollbackFor = Exception.class)
	public DataMap cancelDL08(DataMap map) throws SQLException {
		TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        Object transactionSavepoint = transactionStatus.createSavepoint();

		List<DataMap> headList = map.getList("headList");
		DataMap row;

		DataMap rsMap = new DataMap();
		DataMap rsRowMap = new DataMap();
		DataMap rqMap = new DataMap();		
		
		int count = 0;
		String MSG = "OK";		
		
		// SHPDI I/F SENDYN CHECK
//		for (int j = 0; j < headList.size(); j++) {
//			rsRowMap = headList.get(j).getMap("map");
//			rsMap.setModuleCommand("WmsOutbound_COPY", "VALIDATION_SHPDI_IFSNED_CHECK");
//			rsMap.put("SHPOKY", rsRowMap.get("SHPOKY"));
//			rqMap = commonDao.getMap(rsMap);
//
//			if (rqMap != null) {
//				rsMap.put("SHPOKY", rqMap.getString("SHPOKY"));
//
//				return rsMap;
//			}
//
//		}
//		 
		List<DataMap> reqList = new ArrayList<DataMap>();
		DataMap reqMap = new DataMap();
		
		for (int i = 0; i < headList.size(); i++) {						
	        try {
		
			List<DataMap> ifidList = new ArrayList<DataMap>(); // IFID : 인터페이스 전송 목록
			List<DataMap> ifidList2 = new ArrayList<DataMap>(); // IFID : 인터페이스 전송 목록
			List<String> rcpttyList = new ArrayList<String>(); // IF 실적 : 출고유형
			DataMap rcpttyMap = new DataMap(); // IF 실적 : 출고유형별 출고헤더키
			
			
			row = headList.get(i).getMap("map");

			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

			commonDao.getMap("WmsOutbound_COPY.CALL_SHP_CONFIRM_CANCEL", row); // 출고취소
			
			String shpoky = headList.get(i).getMap("map").getString("SHPOKY"); 
			
			
			// 출고취소시 실적 IF 데이터 추출
			String I_RCPTTY = row.getString("SHPMTY");
			String I_IFID = row.getString("IFID");
			String I_OWNRKY = row.getString("OWNRKY");
			String I_RECVKY = row.getString("SHPOKY");

			// IF 실적 출고유형별 입고키 넣기
			if(!rcpttyList.contains(I_RCPTTY)) {
				rcpttyList.add(I_RCPTTY);
				rcpttyMap.put(I_RCPTTY,""+I_RECVKY+"");
				rcpttyMap.put(I_RCPTTY+"_IFID",I_IFID);
				rcpttyMap.put(I_RCPTTY+"_OWNRKY",I_OWNRKY);

			}else {
				String docNums = rcpttyMap.getString(I_RCPTTY);
				if(docNums!=null && !"".contentEquals(docNums.trim())) {
					docNums += ","+I_RECVKY+"";
				}else {
					docNums += ""+I_RECVKY+"";
				}
				rcpttyMap.put(I_RCPTTY,docNums);
			}								
			
				dataCancelIF(map, rcpttyList, rcpttyMap, ifidList);

				DataMap pMap = new DataMap();
				String rfcfn = ""; // FUNCTION
				String rfctbl = ""; // IF_RETBL
				
				
				  //if(rfctbl.equals("")) { return rsMap; }
				 
			for(DataMap item : ifidList) {
				
					//로그 쌓기
					DataMap logMap = new DataMap();
					String logseq;																													
					
					DataMap rfcinfo = new DataMap(); 
					 		 
					 rfcinfo.setModuleCommand("WmsOutbound_COPY","CANCEL_RFCINFO");
					 rfcinfo.put("IFID", item.get("IFID"));
					 		 
					 DataMap rfcrst = commonDao.getMap(rfcinfo);
					 
					 pMap.put("I_IF_KEY", item.get("LOGSEQNO"));
					 
					 logseq = item.getString("LOGSEQNO");
					 					 					
					 
				if("SAP".equals(item.get("RFCTYPE"))){
					
					try {
			
					   rfcfn = rfcrst.getString("RFCFN"); // FUNCTION
					   rfctbl = "T_INDATA";
					   
					if(rfcfn.equals("Z_WMS_DELIVERY_CANCEL")) {

						reqMap.put("VBELN", row.getString("SVBELN"));
						reqMap.put("WADAT_IST", row.getString("DOCDAT"));
						reqMap.put("BWART", row.get("SAPSTS"));
						reqMap.put("WMSDN", row.get("SHPOKY"));
						pMap.put("I_IF_NAM", "WMS_IF_777");	
					}else if(rfcfn.equals("Z_WMS_MAT_DOC_CANCEL")){
						
						reqMap.put("WMSDN", row.getString("SHPOKY"));
						reqMap.put("MBLNR", row.getString("SXBLNR"));
						reqMap.put("BUDAT", row.getString("DOCDAT"));
						reqMap.put("MJAHR", row.getString("DOCDAT").substring(0, 4));		
						pMap.put("I_IF_NAM", "WMS_IF_778");	
						

						
					}
						reqList.add(reqMap);
						
						logMap.setModuleCommand("Erpif", "IFWMSLOG_RESULT");
						logMap.put("IFID", pMap.get("I_IF_NAM"));
						logMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
						logMap.put("COMSTAT", "C"); // 호출
						logMap.put("REMARK", row.get("SHPOKY"));
						logMap.put("SEQNO", logseq);
						commonDao.insert(logMap);

						// RFC통신 
						RfcCallFunction sapConn = new RfcCallFunction(); 
						DataMap sapRes = sapConn.rfcCancel(rfcfn, rfctbl, pMap, reqList);
												
						
						String errMsg = "";

						for (DataMap reMap : sapRes.getMapList("head")) {
							
							if("S".equals(reMap.getString("EV_CODE")) || "S".equals(reMap.getString("TYPE"))) {
								
								//통신결과 update
								logMap.setModuleCommand("Erpif", "IFWMSLOG");
								logMap.put("IFID", pMap.get("I_IF_NAM"));
								logMap.put("COMSTAT", "S"); //호출
								commonDao.update(logMap);
								
								if(reMap.getString("BWART").equals("641")) {
									
									reMap.put("VBELN", reqList.get(0).getString("VBELN"));									
									reMap.setModuleCommand("WmsOutbound_COPY", "CANCEL_IFWMS103");
									
									commonDao.delete(reMap);
								}
								
								if(ifidList.size() > 1 ) {
									transactionSavepoint = transactionStatus.createSavepoint();
								}else {
									transactionManager.commit(transactionStatus);
								}
						
							}else{

								if ("E".equals(reMap.getString("EV_CODE"))) {
									errMsg = reMap.getString("EV_MSG");
								} else {
									errMsg = reMap.getString("MESSAGE");
								}
								
								 transactionStatus.rollbackToSavepoint(transactionSavepoint); // 롤백

								
								rsMap.put("MSG", "SAP : " + errMsg);
								
								rsMap.put("ERRTXT", errMsg);
								rsMap.put("ZDOCNO", shpoky);										
								rsMap.put("WAREKY", map.getString(CommonConfig.SES_USER_WHAREHOUSE_KEY));
								rsMap.put("PROGID", map.getString("menuId"));
								rsMap.put(CommonConfig.SES_USER_ID_KEY, map.getString(CommonConfig.SES_USER_ID_KEY));

								rsMap.setModuleCommand("WmsOutbound_COPY", "IFSAPLOG");
								commonDao.insert(rsMap);	
								
								//통신결과 update
								logMap.setModuleCommand("Erpif", "IFWMSLOG");
								logMap.put("IFID", pMap.get("I_IF_NAM"));
								logMap.put("COMSTAT", "S"); //호출
								commonDao.update(logMap);
								transactionManager.commit(transactionStatus);
							}
													
							
						}

						
						  if (sapRes.getMapList("head").size() < 1) {
						  
							  transactionStatus.rollbackToSavepoint(transactionSavepoint); // 롤백
							  transactionManager.commit(transactionStatus);	
						  
						  rsMap.put("MSG", "SAP : I/F에서 받은 결과가 존재하지않습니다."); }
						 
						 					
						
					
					//transactionManager.commit(transactionStatus);
					
					if(!errMsg.equals("") && !errMsg.equals(" ")) {
						
						break;
					}
				 }catch (Exception e) {
		 				transactionStatus.rollbackToSavepoint(transactionSavepoint); // 롤백
		 				transactionManager.commit(transactionStatus);
		 				rsMap.put("MSG", e.getMessage());
		 				return rsMap;
					 
				 }
				}else {
					
					// 실적 인터페이스 저장
			 		dataInsertIF(map, rcpttyList, rcpttyMap, ifidList2, true);
			 		
			 		//ifidList = rsMap.getList("ifidList");
					
			 		if(null!=ifidList2 && ifidList2.size()>0) {
						// 인터페이스 전송
			 			try {
			 				DataMap resultMap;
			 				
							for(DataMap item2 : ifidList2) {
								//transactionSavepoint = transactionStatus.createSavepoint();
								if("SAP".equals(item2.get("RFCTYPE")))continue;
								
								item2.put("ownrky", map.getString("OWNRKY"));
								item2.put("IFID", item2.getString("IFID"));
								item2.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
								resultMap = sendIf(item2);
								
								
								if(!"S".equals(resultMap.getString("sendMsg")) && !"SUCCESS".equals(resultMap.getString("sendMsg"))){
									String RFCTYPE; 
									

									  RFCTYPE = "MES";
						                MSG = resultMap.getString("sendMsg");
						                
						                rsMap.put("MSG", MSG);
						                rsMap.put("MSG_TYPE", RFCTYPE);
						                rsMap.put("MSG_DOC_NO", shpoky);
						                
									
								}
								transactionManager.commit(transactionStatus);
								
							}
							
			 			} catch (Exception e) {
			 				transactionStatus.rollbackToSavepoint(transactionSavepoint); // 롤백
			 				transactionManager.commit(transactionStatus);
			 				rsMap.put("MSG", e.getMessage());
			 				return rsMap;
						}
					}
			 		
				}
				
									
			}
																
	        
				count++;
			
			}catch (Exception e){
 				transactionStatus.rollbackToSavepoint(transactionSavepoint); // 롤백
 				transactionManager.commit(transactionStatus);
 				rsMap.put("MSG", e.getMessage());
 				return rsMap;	
			}

		}		
		
		

		rsMap.put("CNT", count);

		return rsMap;

	}

	/**
	 * 거래명세서(Trading statement) 출력
	 * 
	 * @throws SQLException
	 */
	@Transactional
	public DataMap DL08PrintTS(DataMap map) throws SQLException {
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
			String TKNUM = StringUtils.isNotEmpty(STLNUM) ? STLNUM :
						   StringUtils.isNotEmpty(STKNUM) ? STKNUM :
						                                       " " ;
			String KUNNR = data.getString("DPTNKY");
			
			sapData.put("KUNNR", KUNNR); // 납품처코드
			sapData.put("TKNUM", TKNUM); // 선적번호
			
			boolean existCheck = false;
			for (DataMap exist : sapList) {
				if (StringUtils.equals(KUNNR, exist.getString("KUNNR")) &&
					StringUtils.equals(TKNUM, exist.getString("TKNUM"))
				) {
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
		Object DEALKY = commonDao.getObj("DEALKY", new DataMap());

		// SAP에서 받은 데이터 DB에 저장
		List<DataMap> mList = new ArrayList<>();
		for (int i = 0; i < headList.size(); i++) {
			DataMap head = headList.get(i);
			map.clonSessionData(head);
			head.put("DEALKY", DEALKY);
			head.put("LOGSEQNO", logSeq);
			mList.add(head);

			if (i != 0 && i % 100 == 0 || i == headList.size() - 1) {
				commonDao.insert("DEALH_MULTI", mList);
				mList.clear();
			}
		}

		mList = new ArrayList<>();
		for (int i = 0; i < itemList.size(); i++) {
			DataMap item = itemList.get(i);
			item.put("DEALKY", DEALKY);
			mList.add(item);

			if (i != 0 && i % 100 == 0 || i == itemList.size() - 1) {
				commonDao.insert("DEALI_MULTI", mList);
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
			
			if(sendErrMsg.equals("") || sendErrMsg.equals(" ") || sendErrMsg.equals(null)) {
				sendErrMsg="I/F부터 받은 메세지가 없습니다.";
			}
			
			sendResult.put("sendMsg", sendErrMsg);
			
			//log.debug("InboundService.sendIF - ifid : " + ifid + " - Msg : " + sendErrMsg);
		} catch (Exception e) {
			log.error("InboundService.sendIF - ifid : " + ifid + " - Error : " + e.getMessage());
			e.printStackTrace();
		}

		return sendResult;
	}

	// 실적 인터페이스 저장
	@Transactional
	public void mesDataInsertIF(DataMap map, List<String> rcpttyList, DataMap rcpttyMap, List<DataMap> ifidList, boolean passType)
			throws SQLException {

		List<DataMap> ifList = new ArrayList<DataMap>(); // IF 실적 : 실적 인터페이스 목록

		DataMap ifidMap = new DataMap(); // IFID : 출고유형별 LOGSEQNO
		DataMap ifMap = new DataMap();
		DataMap ifRow = new DataMap();

		// 실적 인터페이스 저장
		for (int i = 0; i < rcpttyList.size(); i++) {

			ifMap = new DataMap();

			String RCPTTY = rcpttyList.get(i);
			String RECVKYS = rcpttyMap.getString(RCPTTY);
			String IFIDS = rcpttyMap.getString(RCPTTY + "_IFID");
			String OWNRKY = rcpttyMap.getString(RCPTTY + "_OWNRKY");			
			
			String[] IFID_ARR = IFIDS.split(",");

			String[] RECVKY_ARR = RECVKYS.split(",");

			List<String> RECVKY_LIST = Arrays.asList(RECVKY_ARR);

			for (int j = 0; j < IFID_ARR.length; j++) {

				int limitCnt = 1000;
				int totCnt = RECVKY_ARR.length;
				int loopSize = (totCnt / limitCnt);
				int rmSize = (totCnt % limitCnt);

				if (loopSize == 0) {
					loopSize = 1;
					rmSize = 0;
				}

				String RECVKYIN = "";
				String IFID = IFID_ARR[j];
				String LOGSEQNO = "";
				
				// SAP MES구분
				if (IFID.equals("WMS_IF_317") || IFID.equals("WMS_IF_307") || IFID.equals("WMS_IF_308")
						|| IFID.equals("WMS_IF_320") || IFID.equals("WMS_IF_321") || IFID.equals("WMS_IF_311")
						|| IFID.equals("WMS_IF_313") || IFID.equals("WMS_IF_314") || IFID.equals("WMS_IF_315")
						|| IFID.equals("WMS_IF_323")) {
					continue;
				}
				
				loopSize = RECVKY_LIST.size();
				
				for (int k = 0; k < loopSize; k++) {
					
					ifidMap = new DataMap();

					LOGSEQNO = getLOGSEQNO();
					
					
					
					ifidMap.put("IFID", IFID); // IFID 목록
					ifidMap.put("LOGSEQNO", LOGSEQNO); // IF 호출 유니크 키 생성
					ifidMap.put("RCPTTY", RCPTTY); // IF 호출 유니크 키 생성
					ifidList.add(ifidMap);
					
					
					if("WMS_IF_309".equals(IFID)) {
						RECVKYIN = "'"+RECVKY_LIST.get(k).toString()+"'";
						
					}else {
					
						if (totCnt < limitCnt) {
							RECVKYIN = getRecvkyArrToInString(RECVKY_LIST);
							//log.debug(k + " - RECVKYS IF send batch - start : " + (k * limitCnt) + " - end : " + RECVKY_LIST.size());
						} else {
							RECVKYIN = getRecvkyArrToInString(RECVKY_LIST.subList((k * limitCnt), (((k + 1) * limitCnt))));
							//log.debug(k + " - RECVKYS IF send batch - start : " + (k * limitCnt) + " - end : " + (((k + 1) * limitCnt)));
						}
	
						if (k == (loopSize - 1) && rmSize != 0) {
							RECVKYIN = getRecvkyArrToInString(
									RECVKY_LIST.subList(((k + 1) * limitCnt), RECVKY_LIST.size()));
							//log.debug(k + " - RECVKYS IF send batch last - start : " + ((k + 1) * limitCnt) + " - end : " + RECVKY_LIST.size());
						}
					}
					
					//log.debug("dataInsertIF - loopSize : " + k + " - IFID : " + IFID);
					ifMap.put("LOGSEQNO", LOGSEQNO);
					ifMap.put("RECVKYIN", RECVKYIN.toString());

					ifList = new ArrayList();
					ifList.add(ifMap);

					String RFCTYPE;
					
						RFCTYPE = "MES";						
				

					ifRow = new DataMap();

					ifRow.put("list", ifList);

					ifRow.put("ownrky", OWNRKY);
					ifRow.put("IFID", IFID);
					ifRow.put("LOGSEQNO", ifMap.getString("LOGSEQNO"));
					ifRow.put("RCPTTY", RCPTTY);
					ifRow.put("RFCTYPE", RFCTYPE);
					ifRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
					
					ifidMap.put("RFCTYPE", RFCTYPE);					
					
										
					
					// 출고실적등록 인터페이스 호출
					String insertErrMsg = iFService.dataInsertIF(ifRow);
					//log.debug("dataInsertIF - Msg : " + insertErrMsg);

				}

			}

		}

	}
	
	// 실적 인터페이스 저장
		@Transactional
		public void sapDataInsertIF(DataMap map, List<String> rcpttyList, DataMap rcpttyMap, List<DataMap> ifidList, boolean passType)
				throws SQLException {

			List<DataMap> ifList = new ArrayList<DataMap>(); // IF 실적 : 실적 인터페이스 목록

			DataMap ifidMap = new DataMap(); // IFID : 출고유형별 LOGSEQNO
			DataMap ifMap = new DataMap();
			DataMap ifRow = new DataMap();

			// 실적 인터페이스 저장
			for (int i = 0; i < rcpttyList.size(); i++) {

				ifMap = new DataMap();

				String RCPTTY = rcpttyList.get(i);
				String RECVKYS = rcpttyMap.getString(RCPTTY);
				String IFIDS = rcpttyMap.getString(RCPTTY + "_IFID");
				String OWNRKY = rcpttyMap.getString(RCPTTY + "_OWNRKY");
					

				String[] IFID_ARR = IFIDS.split(",");

				String[] RECVKY_ARR = RECVKYS.split(",");

				List<String> RECVKY_LIST = Arrays.asList(RECVKY_ARR);

				for (int j = 0; j < IFID_ARR.length; j++) {

					int limitCnt = 1000;
					int totCnt = RECVKY_ARR.length;
					int loopSize = (totCnt / limitCnt);
					int rmSize = (totCnt % limitCnt);

					if (loopSize == 0) {
						loopSize = 1;
						rmSize = 0;
					}

					String RECVKYIN = "";
					String IFID = IFID_ARR[j];
					String LOGSEQNO = "";
					
					// SAP MES구분
					if (!(IFID.equals("WMS_IF_317") || IFID.equals("WMS_IF_307") || IFID.equals("WMS_IF_308")
							|| IFID.equals("WMS_IF_320") || IFID.equals("WMS_IF_321") || IFID.equals("WMS_IF_311")
							|| IFID.equals("WMS_IF_313") || IFID.equals("WMS_IF_314") || IFID.equals("WMS_IF_315")
							|| IFID.equals("WMS_IF_323"))) {
						continue;
						
					}
					
					if (!ifidList.contains(IFID)) {
						ifidMap = new DataMap();

						LOGSEQNO = getLOGSEQNO();
						
						
						
						ifidMap.put("IFID", IFID); // IFID 목록
						ifidMap.put("LOGSEQNO", LOGSEQNO); // IF 호출 유니크 키 생성
						ifidMap.put("RCPTTY", RCPTTY); // IF 호출 유니크 키 생성
						ifidList.add(ifidMap);
					} else {
						for (DataMap item : ifidList) {
							if (IFID.equals(item.getMap("map").getString("IFID"))) {
								LOGSEQNO = item.getMap("map").getString("LOGSEQNO");
								break;
							}
						}
					}					
	
					
					for (int k = 0; k < loopSize; k++) {
						
						
							if (totCnt < limitCnt) {
								RECVKYIN = getRecvkyArrToInString(RECVKY_LIST);
								//log.debug(k + " - RECVKYS IF send batch - start : " + (k * limitCnt) + " - end : " + RECVKY_LIST.size());
							} else {
								RECVKYIN = getRecvkyArrToInString(RECVKY_LIST.subList((k * limitCnt), (((k + 1) * limitCnt))));
								//log.debug(k + " - RECVKYS IF send batch - start : " + (k * limitCnt) + " - end : " + (((k + 1) * limitCnt)));
							}
		
							if (k == (loopSize - 1) && rmSize != 0) {
								RECVKYIN = getRecvkyArrToInString(
										RECVKY_LIST.subList(((k + 1) * limitCnt), RECVKY_LIST.size()));
								//log.debug(k + " - RECVKYS IF send batch last - start : " + ((k + 1) * limitCnt) + " - end : " + RECVKY_LIST.size());
							}
						
						//log.debug("dataInsertIF - loopSize : " + k + " - IFID : " + IFID);
						ifMap.put("LOGSEQNO", LOGSEQNO);
						ifMap.put("RECVKYIN", RECVKYIN);

						ifList = new ArrayList();
						ifList.add(ifMap);

						String RFCTYPE;
						// SAP MES구분
						if (IFID.equals("WMS_IF_317") || IFID.equals("WMS_IF_307") || IFID.equals("WMS_IF_308")
								|| IFID.equals("WMS_IF_320") || IFID.equals("WMS_IF_321") || IFID.equals("WMS_IF_311")
								|| IFID.equals("WMS_IF_313") || IFID.equals("WMS_IF_314") || IFID.equals("WMS_IF_315")
								|| IFID.equals("WMS_IF_323")) {
							RFCTYPE = "SAP";
							
						} else {
							RFCTYPE = "MES";
						}

						ifRow = new DataMap();

						ifRow.put("list", ifList);

						ifRow.put("ownrky", OWNRKY);
						ifRow.put("IFID", IFID);
						ifRow.put("LOGSEQNO", ifMap.getString("LOGSEQNO"));
						ifRow.put("RCPTTY", RCPTTY);
						ifRow.put("RFCTYPE", RFCTYPE);
						ifRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
						
						ifidMap.put("RFCTYPE", RFCTYPE);					
						
											
						
						// 출고실적등록 인터페이스 호출
						String insertErrMsg = iFService.dataInsertIF(ifRow);
						//log.debug("dataInsertIF - Msg : " + insertErrMsg);

					}

				}

			}

		}
		
		// 실적 인터페이스 저장
		@Transactional
		public void dataInsertIF(DataMap map, List<String> rcpttyList, DataMap rcpttyMap, List<DataMap> ifidList, boolean passType)
				throws SQLException {

			List<DataMap> ifList = new ArrayList<DataMap>(); // IF 실적 : 실적 인터페이스 목록

			DataMap ifidMap = new DataMap(); // IFID : 출고유형별 LOGSEQNO
			DataMap ifMap = new DataMap();
			DataMap ifRow = new DataMap();

			// 실적 인터페이스 저장
			for (int i = 0; i < rcpttyList.size(); i++) {

				ifMap = new DataMap();

				String RCPTTY = rcpttyList.get(i);
				String RECVKYS = rcpttyMap.getString(RCPTTY);
				String IFIDS = rcpttyMap.getString(RCPTTY + "_IFID");
				String OWNRKY = rcpttyMap.getString(RCPTTY + "_OWNRKY");
					

				String[] IFID_ARR = IFIDS.split(",");

				String[] RECVKY_ARR = RECVKYS.split(",");

				List<String> RECVKY_LIST = Arrays.asList(RECVKY_ARR);

				for (int j = 0; j < IFID_ARR.length; j++) {

					int limitCnt = 1000;
					int totCnt = RECVKY_ARR.length;
					int loopSize = (totCnt / limitCnt);
					int rmSize = (totCnt % limitCnt);

					if (loopSize == 0) {
						loopSize = 1;
						rmSize = 0;
					}

					String RECVKYIN = "";
					String IFID = IFID_ARR[j];
					String LOGSEQNO = "";
					
					if(IFID.equals("WMS_IF_320") && passType){continue;}
					
					if (!ifidList.contains(IFID)) {
						ifidMap = new DataMap();

						LOGSEQNO = getLOGSEQNO();
						
						
						
						ifidMap.put("IFID", IFID); // IFID 목록
						ifidMap.put("LOGSEQNO", LOGSEQNO); // IF 호출 유니크 키 생성
						ifidMap.put("RCPTTY", RCPTTY); // IF 호출 유니크 키 생성
						ifidList.add(ifidMap);
					} else {
						for (DataMap item : ifidList) {
							if (IFID.equals(item.getMap("map").getString("IFID"))) {
								LOGSEQNO = item.getMap("map").getString("LOGSEQNO");
								break;
							}
						}
					}

					for (int k = 0; k < loopSize; k++) {

						if (totCnt < limitCnt) {
							RECVKYIN = getRecvkyArrToInString(RECVKY_LIST);
							//log.debug(k + " - RECVKYS IF send batch - start : " + (k * limitCnt) + " - end : " + RECVKY_LIST.size());
						} else {
							RECVKYIN = getRecvkyArrToInString(RECVKY_LIST.subList((k * limitCnt), (((k + 1) * limitCnt))));
							//log.debug(k + " - RECVKYS IF send batch - start : " + (k * limitCnt) + " - end : " + (((k + 1) * limitCnt)));
						}

						if (k == (loopSize - 1) && rmSize != 0) {
							RECVKYIN = getRecvkyArrToInString(
									RECVKY_LIST.subList(((k + 1) * limitCnt), RECVKY_LIST.size()));
							//log.debug(k + " - RECVKYS IF send batch last - start : " + ((k + 1) * limitCnt) + " - end : " + RECVKY_LIST.size());
						}

						//log.debug("dataInsertIF - loopSize : " + k + " - IFID : " + IFID);
						ifMap.put("LOGSEQNO", LOGSEQNO);
						ifMap.put("RECVKYIN", RECVKYIN);

						ifList = new ArrayList();
						ifList.add(ifMap);

						String RFCTYPE;
						// SAP MES구분
						if (IFID.equals("WMS_IF_317") || IFID.equals("WMS_IF_307") || IFID.equals("WMS_IF_308")
								|| IFID.equals("WMS_IF_320") || IFID.equals("WMS_IF_321") || IFID.equals("WMS_IF_311")
								|| IFID.equals("WMS_IF_313") || IFID.equals("WMS_IF_314") || IFID.equals("WMS_IF_315")
								|| IFID.equals("WMS_IF_323")) {
							RFCTYPE = "SAP";
							
						} else {
							RFCTYPE = "MES";
						}

						ifRow = new DataMap();

						ifRow.put("list", ifList);

						ifRow.put("ownrky", OWNRKY);
						ifRow.put("IFID", IFID);
						ifRow.put("LOGSEQNO", ifMap.getString("LOGSEQNO"));
						ifRow.put("RCPTTY", RCPTTY);
						ifRow.put("RFCTYPE", RFCTYPE);
						ifRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
						
						ifidMap.put("RFCTYPE", RFCTYPE);					
						
											
						
						// 출고실적등록 인터페이스 호출
						String insertErrMsg = iFService.dataInsertIF(ifRow);
						//log.debug("dataInsertIF - Msg : " + insertErrMsg);

					}

				}

			}

		}
		
//	// 실적 인터페이스 저장
//	@Transactional
//	public void dataCancelIF(DataMap map, List<String> rcpttyList, DataMap rcpttyMap, List<DataMap> ifidList)
//			throws SQLException {
//
//		List<DataMap> ifList = new ArrayList<DataMap>(); // IF 실적 : 실적 인터페이스 목록
//
//		DataMap ifidMap = new DataMap(); // IFID : 입고유형별 LOGSEQNO
//		DataMap ifMap = new DataMap();
//		DataMap ifRow = new DataMap();
//
//		// 실적 인터페이스 저장
//		for (int i = 0; i < rcpttyList.size(); i++) {
//
//			ifMap = new DataMap();
//
//			String RCPTTY = rcpttyList.get(i);
//			String RECVKYS = rcpttyMap.getString(RCPTTY);
//			String IFIDS = rcpttyMap.getString(RCPTTY + "_IFID");
//			String OWNRKY = rcpttyMap.getString(RCPTTY + "_OWNRKY");
//
//			String[] IFID_ARR = IFIDS.split(",");
//
//			String[] RECVKY_ARR = RECVKYS.split(",");
//
//			List<String> RECVKY_LIST = Arrays.asList(RECVKY_ARR);
//
//			for (int j = 0; j < IFID_ARR.length; j++) {
//
//				int limitCnt = 1000;
//				int totCnt = RECVKY_ARR.length;
//				int loopSize = (totCnt / limitCnt);
//				int rmSize = (totCnt % limitCnt);
//
//				if (loopSize == 0) {
//					loopSize = 1;
//					rmSize = 0;
//				}
//
//				String RECVKYIN = "";
//				String IFID = IFID_ARR[j];
//				String LOGSEQNO = "";
//
//				if (!ifidList.contains(IFID)) {
//					ifidMap = new DataMap();
//
//					LOGSEQNO = getLOGSEQNO();
//
//					ifidMap.put("IFID", IFID); // IFID 목록
//					ifidMap.put("LOGSEQNO", LOGSEQNO); // IF 호출 유니크 키 생성
//					ifidMap.put("RCPTTY", RCPTTY); // IF 호출 유니크 키 생성
//					ifidMap.put("SHPOKY", RECVKYS); // IF 호출 유니크 키 생성			
//					
//					ifidList.add(ifidMap);
//				} else {
//					for (DataMap item : ifidList) {
//						if (IFID.equals(item.getMap("map").getString("IFID"))) {
//							LOGSEQNO = item.getMap("map").getString("LOGSEQNO");
//							break;
//						}
//					}
//				}
//
//				for (int k = 0; k < loopSize; k++) {
//
//					if (totCnt < limitCnt) {
//						RECVKYIN = getRecvkyArrToInString(RECVKY_LIST);
//						//log.debug(k + " - RECVKYS IF send batch - start : " + (k * limitCnt) + " - end : "
//								+ RECVKY_LIST.size());
//					} else {
//						RECVKYIN = getRecvkyArrToInString(RECVKY_LIST.subList((k * limitCnt), (((k + 1) * limitCnt))));
//						//log.debug(k + " - RECVKYS IF send batch - start : " + (k * limitCnt) + " - end : "
//								+ (((k + 1) * limitCnt)));
//					}
//
//					if (k == (loopSize - 1) && rmSize != 0) {
//						RECVKYIN = getRecvkyArrToInString(
//								RECVKY_LIST.subList(((k + 1) * limitCnt), RECVKY_LIST.size()));
//						//log.debug(k + " - RECVKYS IF send batch last - start : " + ((k + 1) * limitCnt) + " - end : "
//								+ RECVKY_LIST.size());
//					}
//
//					//log.debug("dataInsertIF - loopSize : " + k + " - IFID : " + IFID);
//					ifMap.put("LOGSEQNO", LOGSEQNO);
//					ifMap.put("RECVKYIN", RECVKYIN);
//
//					ifList = new ArrayList();
//					ifList.add(ifMap);
//
//					String RFCTYPE;
//					// SAP MES구분
//					if (IFID.equals("WMS_IF_317") || IFID.equals("WMS_IF_307") || IFID.equals("WMS_IF_308")
//							|| IFID.equals("WMS_IF_320") || IFID.equals("WMS_IF_321") || IFID.equals("WMS_IF_311")
//							|| IFID.equals("WMS_IF_313") || IFID.equals("WMS_IF_314") || IFID.equals("WMS_IF_315")
//							|| IFID.equals("WMS_IF_323")) {
//						RFCTYPE = "SAP";
//					} else {
//						RFCTYPE = "MES";
//					}
//
//					ifRow = new DataMap();
//
//					ifRow.put("list", ifList);
//
//					ifRow.put("ownrky", OWNRKY);
//					ifRow.put("IFID", IFID);
//					ifRow.put("LOGSEQNO", ifMap.getString("LOGSEQNO"));
//					ifRow.put("RCPTTY", RCPTTY);
//					ifRow.put("RFCTYPE", RFCTYPE);
//					ifRow.put("RECVKYIN", ifMap.get("RECVKYIN"));
//					ifRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
//					
//					ifidMap.put("RFCTYPE", RFCTYPE);
//
//				}
//
//			}
//
//		}
//
//	}
		@Transactional
		public void dataCancelIF(DataMap map, List<String> rcpttyList, DataMap rcpttyMap, List<DataMap> ifidList)
				throws SQLException {

			List<DataMap> ifList = new ArrayList<DataMap>(); // IF 실적 : 실적 인터페이스 목록

			DataMap ifidMap = new DataMap(); // IFID : 출고유형별 LOGSEQNO
			DataMap ifMap = new DataMap();
			DataMap ifRow = new DataMap();

			// 실적 인터페이스 저장
			for (int i = 0; i < rcpttyList.size(); i++) {

				ifMap = new DataMap();

				String RCPTTY = rcpttyList.get(i);
				String RECVKYS = rcpttyMap.getString(RCPTTY);
				String IFIDS = rcpttyMap.getString(RCPTTY + "_IFID");
				String OWNRKY = rcpttyMap.getString(RCPTTY + "_OWNRKY");
					

				String[] IFID_ARR = IFIDS.split(",");

				String[] RECVKY_ARR = RECVKYS.split(",");

				List<String> RECVKY_LIST = Arrays.asList(RECVKY_ARR);

				for (int j = 0; j < IFID_ARR.length; j++) {

					int limitCnt = 1000;
					int totCnt = RECVKY_ARR.length;
					int loopSize = (totCnt / limitCnt);
					int rmSize = (totCnt % limitCnt);

					if (loopSize == 0) {
						loopSize = 1;
						rmSize = 0;
					}

					String RECVKYIN = "";
					String IFID = IFID_ARR[j];
					String LOGSEQNO = "";
										
					if (!ifidList.contains(IFID)) {
						ifidMap = new DataMap();

						LOGSEQNO = getLOGSEQNO();
						
						
						//log.debug("LOGSEQNO::::" + LOGSEQNO);
						ifidMap.put("IFID", IFID); // IFID 목록
						ifidMap.put("LOGSEQNO", LOGSEQNO); // IF 호출 유니크 키 생성
						ifidMap.put("RCPTTY", RCPTTY); // IF 호출 유니크 키 생성
						ifidList.add(ifidMap);
					} else {
						for (DataMap item : ifidList) {
							if (IFID.equals(item.getMap("map").getString("IFID"))) {
								LOGSEQNO = item.getMap("map").getString("LOGSEQNO");
								break;
							}
						}
					}

					for (int k = 0; k < loopSize; k++) {

						if (totCnt < limitCnt) {
							RECVKYIN = getRecvkyArrToInString(RECVKY_LIST);
							//log.debug(k + " - RECVKYS IF send batch - start : " + (k * limitCnt) + " - end : " + RECVKY_LIST.size());
						} else {
							RECVKYIN = getRecvkyArrToInString(RECVKY_LIST.subList((k * limitCnt), (((k + 1) * limitCnt))));
							//log.debug(k + " - RECVKYS IF send batch - start : " + (k * limitCnt) + " - end : " + (((k + 1) * limitCnt)));
						}

						if (k == (loopSize - 1) && rmSize != 0) {
							RECVKYIN = getRecvkyArrToInString(
									RECVKY_LIST.subList(((k + 1) * limitCnt), RECVKY_LIST.size()));
							//log.debug(k + " - RECVKYS IF send batch last - start : " + ((k + 1) * limitCnt) + " - end : " + RECVKY_LIST.size());
						}

						//log.debug("dataInsertIF - loopSize : " + k + " - IFID : " + IFID);
						ifMap.put("LOGSEQNO", LOGSEQNO);
						ifMap.put("RECVKYIN", RECVKYIN);

						ifList = new ArrayList();
						ifList.add(ifMap);

						String RFCTYPE;
						// SAP MES구분
						if (IFID.equals("WMS_IF_317") || IFID.equals("WMS_IF_307") || IFID.equals("WMS_IF_308")
								|| IFID.equals("WMS_IF_320") || IFID.equals("WMS_IF_321") || IFID.equals("WMS_IF_311")
								|| IFID.equals("WMS_IF_313") || IFID.equals("WMS_IF_314") || IFID.equals("WMS_IF_315")
								|| IFID.equals("WMS_IF_323")) {														
							
							RFCTYPE = "SAP";
							
						} else {
							RFCTYPE = "MES";
						}

						ifRow = new DataMap();

						ifRow.put("list", ifList);

						ifRow.put("ownrky", OWNRKY);
						ifRow.put("IFID", IFID);
						ifRow.put("LOGSEQNO", ifMap.getString("LOGSEQNO"));
						ifRow.put("RCPTTY", RCPTTY);
						ifRow.put("RFCTYPE", RFCTYPE);
						ifRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
						
						ifidMap.put("RFCTYPE", RFCTYPE);					
						
																	
						// 출고실적등록 인터페이스 호출
						String insertErrMsg = iFService.dataInsertIF(ifRow);
						////log.debug("dataInsertIF - Msg : " + insertErrMsg);

					}

				}

			}

		}
	@Transactional
	public String getRecvkyArrToInString(List<String> IFID_LIST) {
		String rs = "";

		if (IFID_LIST != null) {
			for (int i = 0; i < IFID_LIST.size(); i++) {
				if (!"".equals(rs)) {
					rs += ",";
				}
				rs += "'" + IFID_LIST.get(i) + "'";
			}
		}

		return rs;
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
	public DataMap DL08POPSave(DataMap map) throws SQLException {
		
		DataMap rsMap = new DataMap();
		
		
		List<DataMap> list = map.getList("list");
		/*
		 * String SIGNI = map.getString("SIGNI"); String EXTI2 = map.getString("EXTI2");
		 */
		String ZLGORT = map.getString("ZLGORT");
		
		
		int count = 0;
		
		for(DataMap itemRow : list) {
			
			DataMap row = itemRow.getMap("map");
			
			if(!row.get("SHPMTY").equals("208")) {continue;}
			
			
			
			// SHPDH UPDATE
			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			/*
			 * row.put("SIGNI", SIGNI); row.put("EXTI2", EXTI2);
			 */
			row.put("ZLGORT", ZLGORT);
			
			row.setModuleCommand("WmsOutbound_COPY", "DL08POPSAVE");
			commonDao.update(row);
			
			count++;
		}
		
		rsMap.put("CNT", count);
		
		return rsMap;
	}
	
	@Transactional
	public DataMap TaskLockReset(DataMap map) throws SQLException {
		
		DataMap rsMap = new DataMap();
		
		
		List<DataMap> list = map.getList("list");
		
		String MSG = "OK";
	
		for(DataMap itemRow : list) {
			
			DataMap row = itemRow.getMap("map");
			
			row.put("INOUTCHK", "OUT");
			
			row.setModuleCommand("WmsMobileOutboundPicking_COPY", "TASDI_LOCK_INFO");
			commonDao.update(row);
			
		}
		
		rsMap.put("MSG", MSG);
		
		return rsMap;
	}
	
@Transactional	
public DataMap sendEcmsNoReal(DataMap map) throws SQLException {				
        
		DataMap rsMap = new DataMap();
		String MSG = "OK";
		int count=0;
		
		rsMap.put("MSG", MSG);
			int headModifyIndex = -1;
			List<DataMap> itemList = null;
			if (map.containsKey("headModifyIndex")) {
				headModifyIndex = map.getInt("headModifyIndex");
				itemList = map.getList("list");
			}

			List<DataMap> headList = map.getList("loopHeadList");

			DataMap row;

			List docNumList = new ArrayList();
			List<DataMap> paramItemList = new ArrayList<DataMap>();
			DataMap copy = new DataMap();
			DataMap itemMap = new DataMap();
			String headINDCHK = "";			

			for (int i = 0; i < headList.size(); i++) {
				
		        
		        List<DataMap> ifidList = new ArrayList<DataMap>(); // IFID : 인터페이스 전송 목록
				List<String> rcpttyList = new ArrayList<String>(); // IF 실적 : 출고유형
				DataMap rcpttyMap = new DataMap(); // IF 실적 : 출고유형별 출고헤더키

					
					if (map.containsKey("param")) {
						row = new DataMap(map.getMap("param").getMap("map"));
					} else {
						row = new DataMap();
					}

				map.clonSessionData(row);
				row.append(headList.get(i).getMap("map"));
				docNumList.add(headList.get(i).getMap("map").get("SHPOKY"));

				paramItemList = new ArrayList<DataMap>();
				if (headModifyIndex == i) {
					for (int j = 0; j < itemList.size(); j++) {
						copy = new DataMap(itemList.get(j).getMap("map"));
						paramItemList.add(copy);
					}
				} else {
					row.setModuleCommand("WmsOutbound_COPY", "DL08Sub");
					List<DataMap> dl08subList = commonDao.getList(row);
					for (int j = 0; j < dl08subList.size(); j++) {
						copy = new DataMap(dl08subList.get(j));
						paramItemList.add(copy);
					}
				}

				for (int j = 0; j < paramItemList.size(); j++) {
					itemMap = paramItemList.get(j);

					// 팔렛트는 제외 YSO
					if (itemMap.getString("SHPOIT").substring(0, 1).equals("1")) {
						continue;
					}

					map.clonSessionData(itemMap);

					itemMap.setModuleCommand("WmsOutbound_COPY", "DL08SHPDI");
					commonDao.update(itemMap);

					// 해당 Gropky에 대한 list
					if (itemMap.getString("REFDKY") != null && !itemMap.getString("REFDKY").equals("")
							&& !itemMap.getString("REFDKY").equals(" ")) {
						itemMap.put("SHPOKY", itemMap.get("REFDKY"));
						itemMap.put("SHPOIT", itemMap.get("REFDIT"));
						commonDao.update("WmsOutbound_COPY.DL08SHPDI", itemMap);
					}
				}

				row.setModuleCommand("WmsOutbound_COPY", "DL08SHPDH");
				commonDao.update(row);

				// 5. 전송 프로시저 호출
				// row.put("DOCUKY", row.get("SHPOKY"));
				// row.put("DOFLAG", "Z");
				// row.setModuleCommand("WmsOutbound_COPY", "CALL_AIRWMS_DOCUMENT_PROCESS");
				// commonDao.getMap(row);

				// 6. 그룹오더인지 체크하여 원출고문서 출하완료 처리
				row.setModuleCommand("WmsOutbound_COPY", "CNTGRPDOC");
				int countCHK = commonDao.getMap(row).getInt("CNT");
				if (countCHK > 0) {
					commonDao.getMap("WmsOutbound_COPY.CALL_BATCH_COMP_TOT", row);
				}

				if ("221".equals(row.getString("SHPMTY"))) {
					commonDao.getMap("WmsOutbound_COPY.CALL_COA_STO", row);
				}

				// 출고완료시 실적 IF 데이터 추출
				String I_RCPTTY = row.getString("SHPMTY");
				String I_IFID = row.getString("IFID");
				String I_OWNRKY = row.getString("OWNRKY");
				String I_RECVKY = row.getString("SHPOKY");

				// IF 실적 출고유형별 입고키 넣기
				if (!rcpttyList.contains(I_RCPTTY)) {
					rcpttyList.add(I_RCPTTY);
					rcpttyMap.put(I_RCPTTY, "" + I_RECVKY + "");
					rcpttyMap.put(I_RCPTTY + "_IFID", I_IFID);
					rcpttyMap.put(I_RCPTTY + "_OWNRKY", I_OWNRKY);
				} else {
					String docNums = rcpttyMap.getString(I_RCPTTY);
					if (docNums != null && !"".contentEquals(docNums.trim())) {
						docNums += "," + I_RECVKY + "";
					} else {
						docNums += "" + I_RECVKY + "";
					}
					rcpttyMap.put(I_RCPTTY, docNums);
				}
				count++;
			
			}	
		rsMap.put("ERRNUM", 0);
		
		return rsMap;
	}
	

public DataMap sendEcmsNoFor(DataMap map) throws SQLException {
	long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
    
	DataMap rsMap = new DataMap();
	String MSG = "OK";
	int count = 0;

	int headModifyIndex = -1;
	List < DataMap > itemList = null;

	if (map.containsKey("headModifyIndex")) {
	    headModifyIndex = map.getInt("headModifyIndex");
	    itemList = map.getList("list");
	}

	List < DataMap > headList = map.getList("loopHeadList");

	DataMap row;

	List docNumList = new ArrayList();
	List < DataMap > paramItemList = new ArrayList < DataMap > ();
	DataMap copy = new DataMap();
	DataMap itemMap = new DataMap();
	String headINDCHK = "";				
    
	TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
    TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
    Object transactionSavepoint = transactionStatus.createSavepoint();
    
    
    List < DataMap > ifidList = new ArrayList < DataMap > (); // IFID : 인터페이스 전송 목록
    List < String > rcpttyList = new ArrayList < String > (); // IF 실적 : 출고유형
    DataMap rcpttyMap = new DataMap(); // IF 실적 : 출고유형별 출고헤더키
    try {
    
	for (int i = 0; i < headList.size(); i++) {
		
	    
	    
	    
	    
	    String shpoky =headList.get(i).getMap("map").getString("SHPOKY");

	        if (map.containsKey("param")) {
	            row = new DataMap(map.getMap("param").getMap("map"));
	        } else {
	            row = new DataMap();
	        }

	        map.clonSessionData(row);
	        row.append(headList.get(i).getMap("map"));
	        docNumList.add(headList.get(i).getMap("map").get("SHPOKY"));

	        paramItemList = new ArrayList < DataMap > ();
	        if (headModifyIndex == i) {
	            for (int j = 0; j < itemList.size(); j++) {
	                copy = new
	                DataMap(itemList.get(j).getMap("map"));
	                paramItemList.add(copy);
	            }
	        } else {
	            row.setModuleCommand("WmsOutbound_COPY", "DL08Sub");
	            List < DataMap > dl08subList =
	                commonDao.getList(row);
	            for (int j = 0; j < dl08subList.size(); j++) {
	                copy =
	                    new DataMap(dl08subList.get(j));
	                paramItemList.add(copy);
	            }
	        }

	        for (int j = 0; j < paramItemList.size(); j++) {
	            itemMap =
	                paramItemList.get(j);

	            // 팔렛트는 제외 YSO 
	            if (isEmpty(itemMap.getString("SHPOIT"))) {		            			            	
	            	continue;
	            }else if(itemMap.getString("SHPOIT").substring(0, 1).equals("1")){
	            	continue;
	            }

	        map.clonSessionData(itemMap);

	        itemMap.setModuleCommand("WmsOutbound_COPY", "DL08SHPDI");
	        commonDao.update(itemMap);

	        // 해당 Gropky에 대한 list 
	        if (itemMap.getString("REFDKY") != null &&
	        !itemMap.getString("REFDKY").equals("") &&
	            !itemMap.getString("REFDKY").equals(" ")) {
	        itemMap.put("SHPOKY",
	            itemMap.get("REFDKY"));
	        itemMap.put("SHPOIT", itemMap.get("REFDIT"));
	        commonDao.update("WmsOutbound_COPY.DL08SHPDI", itemMap);
	        }
        }

	row.setModuleCommand("WmsOutbound_COPY", "DL08SHPDH");
	commonDao.update(row);

	// 5. 전송 프로시저 호출
	// row.put("DOCUKY", row.get("SHPOKY")); 
	//row.put("DOFLAG", "Z"); 
	// row.setModuleCommand("WmsOutbound_COPY","CALL_AIRWMS_DOCUMENT_PROCESS"); // commonDao.getMap(row);

	// 6. 그룹오더인지 체크하여 원출고문서 출하완료 처리 
	row.setModuleCommand("WmsOutbound_COPY",
	"CNTGRPDOC");
	int countCHK = commonDao.getMap(row).getInt("CNT");
	if (countCHK > 0) {
	    commonDao.getMap("WmsOutbound_COPY.CALL_BATCH_COMP_TOT", row);
	}

	if ("221".equals(row.getString("SHPMTY"))) {
	    commonDao.getMap("WmsOutbound_COPY.CALL_COA_STO", row);
	}

	// 출고완료시 실적 IF 데이터 추출 
	String I_RCPTTY = row.getString("SHPMTY"); String
	I_IFID = row.getString("IFID");
	String I_OWNRKY = row.getString("OWNRKY");
	String I_RECVKY = row.getString("SHPOKY");

	// IF 실적 출고유형별 입고키 넣기 
	if (!rcpttyList.contains(I_RCPTTY)) {
	rcpttyList.add(I_RCPTTY);
	rcpttyMap.put(I_RCPTTY, "" + I_RECVKY + "");
	rcpttyMap.put(I_RCPTTY + "_IFID", I_IFID);
	rcpttyMap.put(I_RCPTTY +
	    "_OWNRKY", I_OWNRKY);
	}
	else {
	    String docNums =
	        rcpttyMap.getString(I_RCPTTY);
	    if (docNums != null &&
	        !"".contentEquals(docNums.trim())) {
	        docNums += "," + I_RECVKY + "";
	    } else {
	        docNums += "" + I_RECVKY + "";
	    }
	    rcpttyMap.put(I_RCPTTY, docNums);
	}
	count++;
	} //head for end
	    
	// 실적 인터페이스 저장 
		dataInsertIF(map, rcpttyList, rcpttyMap, ifidList, false);
		
	if (null != ifidList && ifidList.size() > 0) {
		DataMap sendMap = new DataMap();
		sendMap.putAll(ifidList.get(0));
		
	    // 인터페이스 전송 
		DataMap resultMap;
		String sendErrMsg = iFService.sendIF(sendMap);
		
	    
	}
	
    }catch (Exception e) {
    	transactionStatus.rollbackToSavepoint(transactionSavepoint);
    	transactionManager.commit(transactionStatus);
    }
	transactionManager.commit(transactionStatus);
	
	// 실적 인터페이스 전송 목록
	rsMap.put("ifidList", ifidList);
	rsMap.put("ERRNUM", 0);
		
		//실험할 코드 추가
        
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
		System.out.println("전체 걸린시간(m) : "+secDiffTime);
		
		return rsMap;
	}

@Transactional
public DataMap sendEcmsEX(DataMap map) throws SQLException {
    
	DataMap rsMap = new DataMap();
	String MSG = " ";
	int count = 0;

	int headModifyIndex = -1;
	List < DataMap > itemList = null;

	if (map.containsKey("headModifyIndex")) {
	    headModifyIndex = map.getInt("headModifyIndex");
	    itemList = map.getList("list");
	}

	List < DataMap > headList = map.getList("loopHeadList");

	DataMap row;

	List docNumList = new ArrayList();
	List < DataMap > paramItemList = new ArrayList < DataMap > ();
	DataMap copy = new DataMap();
	DataMap itemMap = new DataMap();
	String headINDCHK = "";				
    
    List < DataMap > ifidList = new ArrayList < DataMap > (); // IFID : sap인터페이스 전송 목록
    List < DataMap > ifidList2 = new ArrayList < DataMap > (); // IFID : mes 인터페이스 전송 목록
    List < String > rcpttyList = new ArrayList < String > (); // IF 실적 : 출고유형

    DataMap rcpttyMap = new DataMap(); // IF 실적 : 출고유형별 출고헤더키
    
    int sucessCnt = 0;
	int errorCnt = 0;
	String ErrMsg = "";
	
    try {
    
	for (int i = 0; i < headList.size(); i++) {
		
	    
	    String shpoky =headList.get(i).getMap("map").getString("SHPOKY");

	        if (map.containsKey("param")) {
	            row = new DataMap(map.getMap("param").getMap("map"));
	        } else {
	            row = new DataMap();
	        }

	        map.clonSessionData(row);
	        row.append(headList.get(i).getMap("map"));
	        docNumList.add(headList.get(i).getMap("map").get("SHPOKY"));

	        paramItemList = new ArrayList < DataMap > ();
	        if (headModifyIndex == i) {
	            for (int j = 0; j < itemList.size(); j++) {
	                copy = new
	                DataMap(itemList.get(j).getMap("map"));
	                paramItemList.add(copy);
	            }
	        } else {
	            row.setModuleCommand("WmsOutbound_COPY", "DL08Sub");
	            List < DataMap > dl08subList =
	                commonDao.getList(row);
	            for (int j = 0; j < dl08subList.size(); j++) {
	                copy =
	                    new DataMap(dl08subList.get(j));
	                paramItemList.add(copy);
	            }
	        }

	        for (int j = 0; j < paramItemList.size(); j++) {
	            itemMap =
	                paramItemList.get(j);

	            // 팔렛트는 제외 YSO 
	            if (isEmpty(itemMap.getString("SHPOIT"))) {		            			            	
	            	continue;
	            }else if(itemMap.getString("SHPOIT").substring(0, 1).equals("1")){
	            	continue;
	            }

	        map.clonSessionData(itemMap);

	        itemMap.setModuleCommand("WmsOutbound_COPY", "DL08SHPDI");
	        commonDao.update(itemMap);

	        // 해당 Gropky에 대한 list 
	        if (itemMap.getString("REFDKY") != null &&
	        !itemMap.getString("REFDKY").equals("") &&
	            !itemMap.getString("REFDKY").equals(" ")) {
	        itemMap.put("SHPOKY",
	            itemMap.get("REFDKY"));
	        itemMap.put("SHPOIT", itemMap.get("REFDIT"));
	        commonDao.update("WmsOutbound_COPY.DL08SHPDI", itemMap);
	        }
        }

	row.setModuleCommand("WmsOutbound_COPY", "DL08SHPDH");
	commonDao.update(row);

	// 5. 전송 프로시저 호출
	// row.put("DOCUKY", row.get("SHPOKY")); 
	//row.put("DOFLAG", "Z"); 
	// row.setModuleCommand("WmsOutbound_COPY","CALL_AIRWMS_DOCUMENT_PROCESS"); // commonDao.getMap(row);

	// 6. 그룹오더인지 체크하여 원출고문서 출하완료 처리 
	row.setModuleCommand("WmsOutbound_COPY",
	"CNTGRPDOC");
	int countCHK = commonDao.getMap(row).getInt("CNT");
	if (countCHK > 0) {
	    commonDao.getMap("WmsOutbound_COPY.CALL_BATCH_COMP_TOT", row);
	}

	if ("221".equals(row.getString("SHPMTY"))) {
	    commonDao.getMap("WmsOutbound_COPY.CALL_COA_STO", row);
	}

	// 출고완료시 실적 IF 데이터 추출 
	String I_RCPTTY = row.getString("SHPMTY"); String
	I_IFID = row.getString("IFID");
	String I_OWNRKY = row.getString("OWNRKY");
	String I_RECVKY = row.getString("SHPOKY");

	// IF 실적 출고유형별 입고키 넣기 
	if (!rcpttyList.contains(I_RCPTTY)) {
	rcpttyList.add(I_RCPTTY);
	rcpttyMap.put(I_RCPTTY, "" + I_RECVKY + "");
	rcpttyMap.put(I_RCPTTY + "_IFID", I_IFID);
	rcpttyMap.put(I_RCPTTY +
	    "_OWNRKY", I_OWNRKY);
	}
	else {
	    String docNums =
	        rcpttyMap.getString(I_RCPTTY);
	    if (docNums != null &&
	        !"".contentEquals(docNums.trim())) {
	        docNums += "," + I_RECVKY + "";
	    } else {
	        docNums += "" + I_RECVKY + "";
	    }
	    rcpttyMap.put(I_RCPTTY, docNums);
	}
	count++;
	} //head for end
		
		ifidList2.addAll(ifidList);
	
		//실적 인터페이스 저장 
		sapDataInsertIF(map, rcpttyList, rcpttyMap, ifidList, false);
		mesDataInsertIF(map, rcpttyList, rcpttyMap, ifidList2, false);	
//		dataInsertIF(map, rcpttyList, rcpttyMap, ifidList, false);
	
		List < String > sucessShpoky = new ArrayList < String > (); // IF 성공 실적 출하문서번호
		List < String > failShpoky = new ArrayList < String > (); // IF 실패 실적 출하문서번호				
		
	for(int k=0; k <ifidList.size(); k++) {
		
		if(ifidList.get(k).getString("RFCTYPE").equals("MES")) {
			continue;
		}		
		
		DataMap sendMap = new DataMap();		
		
		sendMap.putAll(ifidList.get(k));
		iFService.sendIF(sendMap); //SAP SEND						
		
		DataMap ifwms213Map = new DataMap();
		
		ifwms213Map.putAll(ifidList.get(k));
		ifwms213Map.setModuleCommand("WmsOutbound_COPY","IFWMS213_MES");
		commonDao.delete(ifwms213Map);
		
		ifwms213Map.setModuleCommand("WmsOutbound_COPY","IFWMS213_MES");
		List<DataMap> resultIfwms213List = commonDao.getList(ifwms213Map);
		
		if(resultIfwms213List.size() > 0) {
			
			for(DataMap sendMesMap : resultIfwms213List) {

				//WMS_IF_320 성공시 WMS_IF_309 호출 
				iFService.sendIF(sendMesMap);//MES SEND		    
			}
			
		}
		
		
		
		//SAP SUCESS건 
		ifwms213Map.setModuleCommand("WmsOutbound_COPY","IFWMS213_SAP_SUCESS");
		List<DataMap> sapSucessList = commonDao.getList(ifwms213Map);
		
		//SAP ERROR건 SHP원복
		ifwms213Map.setModuleCommand("WmsOutbound_COPY","IFWMS213_SAP_ERROR");
		List<DataMap> sapErrorList = commonDao.getList(ifwms213Map);
		
		sucessCnt += sapSucessList.size();
		errorCnt += sapErrorList.size();								
		
		for(DataMap cancelShp : sapErrorList) {

					
				
				String IFID = cancelShp.getString("IFID");
				String RECPTY = cancelShp.getString("RECPTY");								
				String PRCETX = cancelShp.getString("PRCETX");
				
				MSG += "\n"+PRCETX;
				ErrMsg = PRCETX;
				DataMap cancelRow = new DataMap();
				
				cancelRow.put("SHPOKY", cancelShp.getString("ZDOC_NO"));
											
				//출고취소
				cancelRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				cancelRow.put("OWNRKY", map.get(CommonConfig.SES_USER_OWNER_KEY));
				cancelRow.put("WAREKY", map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

				commonDao.getMap("WmsOutbound_COPY.CALL_SHP_CONFIRM_CANCEL", cancelRow); // 출고취소
				
				
				if(!IFID.equals("WMS_IF_320") && !RECPTY.equals("205")) {

				//상차완료
				commonDao.update("WmsOutbound_COPY.DL05ZERO", cancelRow);
				cancelRow.put("LSTTFL", "V");
				cancelRow.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				
				//TASDI UPDATE
				commonDao.update("WmsOutbound_COPY.SAP_CANCEL_TASDI", cancelRow);
				//SPHDI UPDATE
				commonDao.update("WmsOutbound_COPY.SAP_CANCEL_SHPDI", cancelRow);
				}
			
				    
		}
	}
		
	
	
    }catch (Exception e) {
    	
    }
	
    
    if(errorCnt > 0) {
    	MSG = ErrMsg+"...";
		if (ErrMsg != null && ErrMsg.length() > 200) { MSG = ErrMsg.substring(0, 200); }		
	}
	
	// 실적 인터페이스 전송 목록
	rsMap.put("errorCnt", errorCnt);
	rsMap.put("sucessCnt", sucessCnt);
	rsMap.put("MSG", MSG);
		
		return rsMap;
	}
/**
 * RFC CALL CONNECTION SETTING
 * @return baseMap
 */
public DataMap RfcCallConnSet() {
	DataMap baseMap = new DataMap();
	
	baseMap.put(DestinationDataProvider.JCO_ASHOST, 	PropertyUtil.getProperty("sap.properties", "sap.jco.ASHOST")); // SAP 호스트 정보
	baseMap.put(DestinationDataProvider.JCO_SYSNR, 		PropertyUtil.getProperty("sap.properties", "sap.jco.SYSNUM")); // 인스턴스번호
//	baseMap.put(CommonConfig.SAP_JCO_SYSID, 			PropertyUtil.getProperty("sap.properties", "sap.jco.SYSID")); // 인스턴스아이디
	baseMap.put("jco.client.sysid", 					PropertyUtil.getProperty("sap.properties", "sap.jco.SYSID")); // 인스턴스아이디
	
	baseMap.put(DestinationDataProvider.JCO_CLIENT, 	PropertyUtil.getProperty("sap.properties", "sap.jco.CLIENT")); // SAP 클라이언트
	baseMap.put(DestinationDataProvider.JCO_USER, 		PropertyUtil.getProperty("sap.properties", "sap.jco.USERID")); // SAP유저명
	baseMap.put(DestinationDataProvider.JCO_PASSWD, 	PropertyUtil.getProperty("sap.properties", "sap.jco.PASSWD")); // SAP 패스워드
	baseMap.put(DestinationDataProvider.JCO_LANG, 		PropertyUtil.getProperty("sap.properties", "sap.jco.LANGKY")); // 언어

	//log.debug("-----------------------------------------------");
	//log.debug("SAP 접속정보");
	//log.debug("ASHOST : " + PropertyUtil.getProperty("sap.properties", "sap.jco.ASHOST"));
	//log.debug("SYSNUM : " + PropertyUtil.getProperty("sap.properties", "sap.jco.SYSNUM"));
	//log.debug("SYSID : " + PropertyUtil.getProperty("sap.properties", "sap.jco.SYSID"));
	
	//log.debug("CLIENT : " + PropertyUtil.getProperty("sap.properties", "sap.jco.CLIENT"));
	//log.debug("USERID : " + PropertyUtil.getProperty("sap.properties", "sap.jco.USERID"));
	//log.debug("PASSWD : " + PropertyUtil.getProperty("sap.properties", "sap.jco.PASSWD"));
	//log.debug("LANGKY : " + PropertyUtil.getProperty("sap.properties", "sap.jco.LANGKY"));
	//log.debug("-----------------------------------------------");
	
	return baseMap;
}
public DataMap sendEcmsOneConn(DataMap map) throws SQLException {
	long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
    
	DataMap rsMap = new DataMap();
	String MSG = "OK";
	int count = 0;

	int headModifyIndex = -1;
	List < DataMap > itemList = null;

	if (map.containsKey("headModifyIndex")) {
	    headModifyIndex = map.getInt("headModifyIndex");
	    itemList = map.getList("list");
	}

	List < DataMap > headList = map.getList("loopHeadList");

	DataMap row;
	
	//DataMap baseMap = RfcCallConnSet(); 			// RFC CALL CONNECTION SET
	
	// ... it always has a reference to a metadata repository    
    
	
    
	List docNumList = new ArrayList();
	List < DataMap > paramItemList = new ArrayList < DataMap > ();
	DataMap copy = new DataMap();
	DataMap itemMap = new DataMap();
	String headINDCHK = "";				
    
	for (int i = 0; i < headList.size(); i++) {
		TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
	    TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
	    Object transactionSavepoint = transactionStatus.createSavepoint();
	    
	    
	    List < DataMap > ifidList = new ArrayList < DataMap > (); // IFID : 인터페이스 전송 목록
	    List < String > rcpttyList = new ArrayList < String > (); // IF 실적 : 출고유형
	    DataMap rcpttyMap = new DataMap(); // IF 실적 : 출고유형별 출고헤더키
	    
	    String shpoky =headList.get(i).getMap("map").getString("SHPOKY");
	    
	    try {

	        if (map.containsKey("param")) {
	            row = new DataMap(map.getMap("param").getMap("map"));
	        } else {
	            row = new DataMap();
	        }

	        map.clonSessionData(row);
	        row.append(headList.get(i).getMap("map"));
	        docNumList.add(headList.get(i).getMap("map").get("SHPOKY"));

	        paramItemList = new ArrayList < DataMap > ();
	        if (headModifyIndex == i) {
	            for (int j = 0; j < itemList.size(); j++) {
	                copy = new
	                DataMap(itemList.get(j).getMap("map"));
	                paramItemList.add(copy);
	            }
	        } else {
	            row.setModuleCommand("WmsOutbound_COPY", "DL08Sub");
	            List < DataMap > dl08subList =
	                commonDao.getList(row);
	            for (int j = 0; j < dl08subList.size(); j++) {
	                copy =
	                    new DataMap(dl08subList.get(j));
	                paramItemList.add(copy);
	            }
	        }

	        for (int j = 0; j < paramItemList.size(); j++) {
	            itemMap =
	                paramItemList.get(j);

	            // 팔렛트는 제외 YSO 
	            if (isEmpty(itemMap.getString("SHPOIT"))) {		            			            	
	            	continue;
	            }else if(itemMap.getString("SHPOIT").substring(0, 1).equals("1")){
	            	continue;
	            }

	        map.clonSessionData(itemMap);

	        itemMap.setModuleCommand("WmsOutbound_COPY", "DL08SHPDI");
	        commonDao.update(itemMap);

	        // 해당 Gropky에 대한 list 
	        if (itemMap.getString("REFDKY") != null &&
	        !itemMap.getString("REFDKY").equals("") &&
	            !itemMap.getString("REFDKY").equals(" ")) {
	        itemMap.put("SHPOKY",
	            itemMap.get("REFDKY"));
	        itemMap.put("SHPOIT", itemMap.get("REFDIT"));
	        commonDao.update("WmsOutbound_COPY.DL08SHPDI", itemMap);
	    }
	}

	row.setModuleCommand("WmsOutbound_COPY", "DL08SHPDH");
	commonDao.update(row);

	// 5. 전송 프로시저 호출
	// row.put("DOCUKY", row.get("SHPOKY")); 
	//row.put("DOFLAG", "Z"); 
	// row.setModuleCommand("WmsOutbound_COPY","CALL_AIRWMS_DOCUMENT_PROCESS"); // commonDao.getMap(row);

	// 6. 그룹오더인지 체크하여 원출고문서 출하완료 처리 
	row.setModuleCommand("WmsOutbound_COPY",
	"CNTGRPDOC");
	int countCHK = commonDao.getMap(row).getInt("CNT");
	if (countCHK > 0) {
	    commonDao.getMap("WmsOutbound_COPY.CALL_BATCH_COMP_TOT", row);
	}

	if ("221".equals(row.getString("SHPMTY"))) {
	    commonDao.getMap("WmsOutbound_COPY.CALL_COA_STO", row);
	}

	// 출고완료시 실적 IF 데이터 추출 
	String I_RCPTTY = row.getString("SHPMTY"); String
	I_IFID = row.getString("IFID");
	String I_OWNRKY = row.getString("OWNRKY");
	String I_RECVKY = row.getString("SHPOKY");

	// IF 실적 출고유형별 입고키 넣기 
	if (!rcpttyList.contains(I_RCPTTY)) {
	rcpttyList.add(I_RCPTTY);
	rcpttyMap.put(I_RCPTTY, "" + I_RECVKY + "");
	rcpttyMap.put(I_RCPTTY + "_IFID", I_IFID);
	rcpttyMap.put(I_RCPTTY +
	    "_OWNRKY", I_OWNRKY);
	}
	else {
	    String docNums =
	        rcpttyMap.getString(I_RCPTTY);
	    if (docNums != null &&
	        !"".contentEquals(docNums.trim())) {
	        docNums += "," + I_RECVKY + "";
	    } else {
	        docNums += "" + I_RECVKY + "";
	    }
	    rcpttyMap.put(I_RCPTTY, docNums);
	}
	count++;

	// 실적 인터페이스 저장 
	dataInsertIF(map, rcpttyList, rcpttyMap, ifidList, false);

	if (null != ifidList && ifidList.size() > 0) {

	    // 인터페이스 전송 
		DataMap resultMap;

	    for (DataMap item: ifidList) {
	        item.put("ownrky", map.getString("OWNRKY"));
	        item.put("IFID", item.getString("IFID"));
	        item.put(CommonConfig.SES_USER_ID_KEY,
	            map.get(CommonConfig.SES_USER_ID_KEY));
	        //item.put("baseMap", baseMap);
	        item.put("THIS_CNT", count);
	        	resultMap = sendIf(item);
	        
        	if (ifidList.size() == 1 && item.getString("RFCTYPE").equals("SAP") && ("S".equals(resultMap.getString("sendMsg")) || "SUCCESS".equals(resultMap.getString("sendMsg")))) {

	            transactionManager.commit(transactionStatus);

	        }else if (ifidList.size() > 1 && item.getString("RFCTYPE").equals("SAP") && ("S".equals(resultMap.getString("sendMsg")) || "SUCCESS".equals(resultMap.getString("sendMsg")))) {

	        	transactionSavepoint = transactionStatus.createSavepoint();

	        } else if (!"S".equals(resultMap.getString("sendMsg")) && !"SUCCESS".equals(resultMap.getString("sendMsg"))) {
	        	
	            String RFCTYPE;

	            if (item.getString("RFCTYPE").equals("SAP")) {

	            	transactionStatus.rollbackToSavepoint(transactionSavepoint);

	                RFCTYPE = "SAP : ";
	                MSG = RFCTYPE + resultMap.getString("sendMsg");
	                rsMap.put("MSG", MSG);		                
	                rsMap.put("ERRTXT", resultMap.getString("sendMsg"));
					rsMap.put("ZDOCNO", shpoky);										
					rsMap.put("WAREKY", map.getString(CommonConfig.SES_USER_WHAREHOUSE_KEY));
					rsMap.put("PROGID", map.getString("menuId"));
					rsMap.put(CommonConfig.SES_USER_ID_KEY, map.getString(CommonConfig.SES_USER_ID_KEY));

					rsMap.setModuleCommand("WmsOutbound_COPY", "IFSAPLOG");
					commonDao.insert(rsMap);
					transactionManager.commit(transactionStatus);
	                break;
	            } else {
	            	
	                RFCTYPE = "MES";
	                MSG = resultMap.getString("sendMsg");
	                
	                rsMap.put("MSG", RFCTYPE+":"+MSG);
	                rsMap.put("MSG_TYPE", RFCTYPE);
	                rsMap.put("MSG_DOC_NO", shpoky);	
	                transactionManager.commit(transactionStatus);
	            }
	        }else if (item.getString("RFCTYPE").equals("MES") && ("S".equals(resultMap.getString("sendMsg")) || "SUCCESS".equals(resultMap.getString("sendMsg")))) {

	            transactionManager.commit(transactionStatus);

	        }

	    } //send for

	}

	// 실적 인터페이스 전송 목록
	rsMap.put("ifidList", ifidList);

	} catch (Exception e) {
		transactionStatus.rollbackToSavepoint(transactionSavepoint);
		transactionManager.commit(transactionStatus);
		rsMap.put("MSG", e.getMessage());
		return rsMap;
	}
	    
	} // head for

		rsMap.put("ERRNUM", 0);
		
		//실험할 코드 추가
        
		long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
		long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
		System.out.println("전체 거린시간(m) : "+secDiffTime);
		
		return rsMap;
	}

	@Transactional
	public DataMap cancelDL082(DataMap map) throws SQLException {
		
        
		List<DataMap> headList = map.getList("headList");
		DataMap row;

		DataMap rsMap = new DataMap();
		DataMap rsRowMap = new DataMap();
		DataMap rqMap = new DataMap();		
		
		int count = 0;
		String MSG = "OK";		

		List<DataMap> reqList = new ArrayList<DataMap>();
		DataMap reqMap = new DataMap();
		
		for (int i = 0; i < headList.size(); i++) {
			
	        
			row = headList.get(i).getMap("map");

			row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
			row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));					
			
			
			commonDao.getMap("WmsOutbound_COPY.CALL_SHP_CONFIRM_CANCEL", row); // 출고취소
			
			if(row.getString("SHPMTY").equals("221")) {
				
				row.put("VBELN", row.getString("SVBELN"));									
				row.setModuleCommand("WmsOutbound_COPY", "CANCEL_IFWMS103");
				
				commonDao.delete(row);
			}
				
			count++;
		}		

		rsMap.put("CNT", count);

		return rsMap;

	}
	
	@Transactional
		public DataMap cancelDL08EX(DataMap map) throws SQLException {			
        
			List<DataMap> headList = map.getList("headList");
			DataMap row;

			DataMap rsMap = new DataMap();
			DataMap rsRowMap = new DataMap();
			DataMap rqMap = new DataMap();		
			
			int count = 0;
			String MSG = " ";		
			
			DataMap reqMap = new DataMap();
			int sucessCnt = 0;
			int errorCnt = 0;
			
			
		  try {
			  
			  	List<DataMap> ifidList = new ArrayList<DataMap>(); // IFID : 인터페이스 전송 목록
				List<DataMap> ifidList2 = new ArrayList<DataMap>(); // IFID : 인터페이스 전송 목록
				List<String> rcpttyList = new ArrayList<String>(); // IF 실적 : 출고유형
				DataMap rcpttyMap = new DataMap(); // IF 실적 : 출고유형별 출고헤더키
		        
			for (int i = 0; i < headList.size(); i++) {								      							
				
				
				row = headList.get(i).getMap("map");

				row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
				row.put(CommonConfig.SES_USER_OWNER_KEY, map.get(CommonConfig.SES_USER_OWNER_KEY));
				row.put(CommonConfig.SES_USER_WHAREHOUSE_KEY, map.get(CommonConfig.SES_USER_WHAREHOUSE_KEY));

				commonDao.getMap("WmsOutbound_COPY.CALL_SHP_CONFIRM_CANCEL", row); // 출고취소
				
				String shpoky = headList.get(i).getMap("map").getString("SHPOKY"); 
				
				
				// 출고취소시 실적 IF 데이터 추출
				String I_RCPTTY = row.getString("SHPMTY");
				String I_IFID = row.getString("IFID");
				String I_OWNRKY = row.getString("OWNRKY");
				String I_RECVKY = row.getString("SHPOKY");
				
				// IF 실적 출고유형별 입고키 넣기
				if(!rcpttyList.contains(I_RCPTTY)) {
					rcpttyList.add(I_RCPTTY);
					rcpttyMap.put(I_RCPTTY,""+I_RECVKY+"");
					rcpttyMap.put(I_RCPTTY+"_IFID",I_IFID);
					rcpttyMap.put(I_RCPTTY+"_OWNRKY",I_OWNRKY);

				}else {
					String docNums = rcpttyMap.getString(I_RCPTTY);
					if(docNums!=null && !"".contentEquals(docNums.trim())) {
						docNums += ","+I_RECVKY+"";
					}else {
						docNums += ""+I_RECVKY+"";
					}
					rcpttyMap.put(I_RCPTTY,docNums);
				}								
				
	        }
					//dataCancelIF(map, rcpttyList, rcpttyMap, ifidList);
					dataCancelIF(reqMap, rcpttyList, rcpttyMap, ifidList);
					
					DataMap pMap = new DataMap();
					String rfcfn = ""; // FUNCTION
					String rfctbl = ""; // IF_RETBL										
					// 전송결과 처리
					
					
				for(DataMap item : ifidList) {
					
					int resFailCnt = 0;
					String resFailLastMsg = "";
					String errMsg = "";	
					
					if("MES".equals(item.get("RFCTYPE"))) {continue;}
					
					List<DataMap> reqList = new ArrayList<DataMap>();

						//로그 쌓기
						DataMap logMap = new DataMap();
						String logseq;																													
						
						DataMap rfcinfo = new DataMap(); 
						 		 
						 rfcinfo.setModuleCommand("WmsOutbound_COPY","CANCEL_RFCINFO");
						 rfcinfo.put("IFID", item.get("IFID"));
						 		 
						 DataMap rfcrst = commonDao.getMap(rfcinfo);
						 
						 pMap.put("I_IF_KEY", item.get("LOGSEQNO"));
						 
						 logseq = item.getString("LOGSEQNO");
						 						 
						// 로그시퀀스번호 채번
						logMap.setModuleCommand("Erpif", "IFWMSLOG_RESULT");
						logMap.put("IFID", item.get("IFID"));
						logMap.put("COMSTAT", "C");
						logMap.put("SEQNO", item.get("LOGSEQNO"));
						logMap.put("REMARK", "출고취소 진행중");
						logMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
						commonDao.insert(logMap);
							
					if("SAP".equals(item.get("RFCTYPE"))){
				
						   rfcfn = rfcrst.getString("RFCFN"); // FUNCTION
						   rfctbl = "T_INDATA";
						   
						   List<DataMap> sapIfList = new ArrayList<DataMap>();
						   
						   item.setModuleCommand("WmsOutbound_COPY","SAP_CANCEL");
						   sapIfList = commonDao.getList(item);
						   
						   for(DataMap spaifRow : sapIfList) {
							   DataMap reqSapMap = new DataMap();
							   
								if(rfcfn.equals("Z_WMS_DELIVERY_CANCEL")) {
									
									reqSapMap.put("VBELN", spaifRow.getString("SVBELN"));
									reqSapMap.put("WADAT_IST", spaifRow.getString("DOCDAT"));
									reqSapMap.put("BWART", spaifRow.get("SAPSTS"));
									reqSapMap.put("WMSDN", spaifRow.get("SHPOKY"));
									pMap.put("I_IF_NAM", "WMS_IF_777");	
								}else if(rfcfn.equals("Z_WMS_MAT_DOC_CANCEL")){
									
									reqSapMap.put("WMSDN", spaifRow.getString("SHPOKY"));
									reqSapMap.put("MBLNR", spaifRow.getString("SXBLNR"));
									reqSapMap.put("BUDAT", spaifRow.getString("DOCDAT"));
									reqSapMap.put("MJAHR", spaifRow.getString("DOCDAT").substring(0, 4));		
									pMap.put("I_IF_NAM", "WMS_IF_778");	
	
								}
								
								reqList.add(reqSapMap);
						   }
								
							 					
							
						
						// RFC통신 
							RfcCallFunction sapConn = new RfcCallFunction(); 
							DataMap sapRes = sapConn.rfcCancel(rfcfn, rfctbl, pMap, reqList);
																															
							
							List<String> noReturnList = new ArrayList<String>();
						
							//보낸데이터와 받은데이터 결과값 다름
							if(reqList.size() != sapRes.getMapList("head").size()) {
								
								
									for(int i= 0 ; i < sapRes.getMapList("head").size(); i++) {																		
										
										for(int j=0; j < reqList.size(); j++) {
											
										String sendShpoky = reqList.get(j).getString("WMSDN");
										String returnShpoky = sapRes.getMapList("head").get(i).getString("WMSDN");
										
											if(sendShpoky.equals(returnShpoky)) {
												
												noReturnList.removeAll(Arrays.asList(sendShpoky));         
												continue;
											}else {
												noReturnList.add(sendShpoky);
											}
										
										}
									
									}
									
									
									//전체 리턴결과 없을시 
									if(0 == sapRes.getMapList("head").size()) {
										
										for(int m = 0; m < reqList.size(); m++) {
											noReturnList.add(reqList.get(m).getString("WMSDN"));
										}
										
									}

								System.out.println("최종noReturnList::"+noReturnList); 
							}
							
							for(int k = 0; k < noReturnList.size(); k++) {							
								DataMap iflogMap = new DataMap();
								errMsg = "SAP : I/F에서 받은 결과가 존재하지않습니다.";
		
								//통신결과 update
								iflogMap.setModuleCommand("Erpif", "IFWMS213multi_FLG");
								iflogMap.put("PRCFLG", "E");
								iflogMap.put("PRCETX", errMsg);	
								iflogMap.put("IFID", item.get("IFID"));									
								iflogMap.put("LOGSEQNO", item.get("LOGSEQNO"));
								iflogMap.put("REMARK", "출고취소");
								iflogMap.put("WMSDN", noReturnList.get(k));
								commonDao.update(iflogMap);	
								
								}
							
							errMsg = "";
									
							for (DataMap reMap : sapRes.getMapList("head")) {
								
								if("S".equals(reMap.getString("EV_CODE")) || "S".equals(reMap.getString("TYPE"))) {
									
									DataMap iflogMap = new DataMap();
									
									//통신결과 update
									iflogMap.setModuleCommand("Erpif", "IFWMS213multi_FLG");
									iflogMap.put("PRCFLG", "C");
									iflogMap.put("IFID", item.get("IFID"));									
									iflogMap.put("LOGSEQNO", item.get("LOGSEQNO"));
									iflogMap.put("WMSDN", reMap.get("WMSDN"));
									iflogMap.put("MBLNR", reMap.get("MBLNR"));
									iflogMap.put("REMARK", "출고취소");
//									logMap.put("COMSTAT", "S"); //호출
									commonDao.update(iflogMap);
									
									
									if(reMap.getString("BWART").equals("648")) {
										
											for(DataMap vbelnMap : reqList) {
												
												if(reMap.get("WMSDN").equals(vbelnMap.get("WMSDN"))) {
												
													reMap.put("VBELN", vbelnMap.getString("VBELN"));									
													reMap.setModuleCommand("WmsOutbound_COPY", "CANCEL_IFWMS103");
												
													commonDao.delete(reMap);
												}
											}
									}
									
									reMap.put("MBLNR", " ");				
									reMap.put("WMSDN", reMap.get("WMSDN"));
									reMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));

									reMap.setModuleCommand("Erpif", "IFWMS213_SHPDI");	
									commonDao.update(reMap);
									
									reMap.setModuleCommand("Erpif", "IFWMS213_SHPDH");	
									commonDao.update(reMap);
							
								}else{

									if ("E".equals(reMap.getString("EV_CODE"))) {
										errMsg = reMap.getString("EV_MSG");
									} else {
										errMsg = reMap.getString("MESSAGE");
									}
									
									DataMap iflogMap = new DataMap();
									
									//통신결과 update
									iflogMap.setModuleCommand("Erpif", "IFWMS213multi_FLG");
									iflogMap.put("PRCFLG", "E");
									iflogMap.put("PRCETX", errMsg);	
									iflogMap.put("IFID", item.get("IFID"));									
									iflogMap.put("LOGSEQNO", item.get("LOGSEQNO"));
									iflogMap.put("WMSDN", reMap.get("WMSDN"));
									iflogMap.put("REMARK", "출고취소");
									commonDao.update(iflogMap);																	
									
									
								}
														
																								
							}														
						
				
					}		
					
							
					
					DataMap ifwms213Map = new DataMap();
					
					ifwms213Map.putAll(item);
					ifwms213Map.setModuleCommand("WmsOutbound_COPY","IFWMS213_MES");
					commonDao.delete(ifwms213Map);
					
					ifwms213Map.setModuleCommand("WmsOutbound_COPY","IFWMS213_MES");
					List<DataMap> resultIfwms213List = commonDao.getList(ifwms213Map);
					
					if(resultIfwms213List.size() > 0) {
						
						for(DataMap sendMesMap : resultIfwms213List) {

							//WMS_IF_320 성공시 WMS_IF_309 호출 
							iFService.sendIF(sendMesMap);//MES SEND		    
						}
						
					}
					
					
					
					//SAP SUCESS건 
					ifwms213Map.setModuleCommand("WmsOutbound_COPY","IFWMS213_SAP_SUCESS");
					List<DataMap> sapSucessList = commonDao.getList(ifwms213Map);
					
					//SAP ERROR건 SHP원복
					ifwms213Map.setModuleCommand("WmsOutbound_COPY","IFWMS213_SAP_ERROR");
					List<DataMap> sapErrorList = commonDao.getList(ifwms213Map);
					
					sucessCnt += sapSucessList.size();
					errorCnt += sapErrorList.size();				
					
					for(DataMap cancelShp : sapErrorList) {

								
							
							String IFID = cancelShp.getString("IFID");
							String RECPTY = cancelShp.getString("RECPTY");								
							String PRCETX = cancelShp.getString("PRCETX");
							
							resFailCnt++;
							resFailLastMsg = PRCETX;
							
							MSG += "\n"+PRCETX;
							
							DataMap cancelRow = new DataMap();																				

							
								cancelRow.put("SHPOKY", cancelShp.getString("ZDOC_NO"));																																					

								cancelRow.setModuleCommand("WmsOutbound_COPY","IFWMS213_SAP_SHP");
								List<DataMap> repairList = commonDao.getList(cancelRow);
								
								for(DataMap repairMap : repairList) {
									
									if(!IFID.equals("WMS_IF_320") && !RECPTY.equals("205")) {
										
									//상차완료
									repairMap.put("LSTTFL", "V");
									repairMap.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
									
									//TASDI UPDATE
									commonDao.update("WmsOutbound_COPY.SAP_CANCEL_TASDI", repairMap);
									//SPHDI UPDATE
									commonDao.update("WmsOutbound_COPY.SAP_CANCEL_SHPDI", repairMap);
									}
									
									//출고완료
									commonDao.update("WmsOutbound_COPY.SAP_CANCEL_SHPDI_REPAIR", repairMap);
									commonDao.update("WmsOutbound_COPY.SAP_CANCEL_SHPDH_REPAIR", repairMap);
								}
							
							}
					
					
					logMap.setModuleCommand("Erpif", "IFWMSLOG");
					logMap.put("REMARK", "출고취소");
					
					if(resFailCnt > 0) {
						errMsg = "실패:"+resFailCnt+"건 - "+resFailLastMsg+"...";
						if (errMsg != null && errMsg.length() > 200) { errMsg = errMsg.substring(0, 200); }
						// LOG 결과처리
															
						logMap.put("COMSTAT", "E");
						logMap.put("ERRTXT", errMsg);
						
					}else {
						logMap.put("COMSTAT", "S");
					}
					
					commonDao.update(logMap);
					
				}
																	
		        
					count++;
				
		    }catch (Exception e) {
		    	
		    }
			
			
			// 실적 인터페이스 전송 목록
			rsMap.put("errorCnt", errorCnt);
			rsMap.put("sucessCnt", sucessCnt);
			rsMap.put("MSG", MSG);
			return rsMap;

		}
		
}
