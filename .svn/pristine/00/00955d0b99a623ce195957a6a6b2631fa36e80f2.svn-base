package project.common.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.common.bean.BaseUser;
import project.common.bean.CommonConfig;
import project.common.bean.CommonLabel;
import project.common.bean.CommonMenu;
import project.common.bean.CommonSearch;
import project.common.bean.DataMap;
import project.common.bean.FileRepository;
import project.common.bean.SystemConfig;
import project.common.dao.CommonDAO;
import project.common.util.FileUtil;
import project.common.util.Util;

@Service("commonService")
public class CommonService extends BaseService {
	
	static final Logger log = LogManager.getLogger(CommonService.class.getName());
	
	@Autowired
	public CommonDAO commonDao;
	
	@Autowired
	private CommonLabel commonLabel;
	
	@Autowired
	private CommonSearch commonSearch;
	
	@Autowired
	private CommonMenu commonMenu;
	
	@Autowired
	private FileRepository respository;
	
	@Autowired
	private SystemConfig systemConfig;
	
	@Autowired
	private Util util;

	private FileUtil fileUtil = new FileUtil();

	public int getCount(DataMap map) throws SQLException {
		return commonDao.getCount(map);
	}
	public int getCount(String sqlId, DataMap map) throws SQLException {
		return commonDao.getCount(sqlId, map);
	}
	public List getPagingList(DataMap map) throws SQLException {
		return commonDao.getPagingList(map);
	}
	public List getList(DataMap map) throws SQLException {
		return commonDao.getList(map);
	}
	public List getList(String sqlId, DataMap map) throws SQLException {
		return commonDao.getList(sqlId, map);
	}
	public List getList(String module, String command) throws SQLException {
		DataMap map = new DataMap(module, command);
		return commonDao.getList(map);
	}
	public DataMap getMap(String sqlId, DataMap map) throws SQLException {
		return commonDao.getMap(sqlId, map);
	}
	public DataMap getMap(DataMap map) throws SQLException {
		return commonDao.getMap(map);
	}
	public Object getObj(DataMap map) throws SQLException {
		return commonDao.getObj(map);
	}
	public Object insert(String sqlId, DataMap map) throws SQLException {
		return commonDao.insert(sqlId, map);
	}
	public Object insert(DataMap map) throws SQLException {
		return commonDao.insert(map);
	}
	public int update(String sqlId, DataMap map) throws SQLException {
		return commonDao.update(sqlId, map);
	}
	public int update(DataMap map) throws SQLException {
		return commonDao.update(map);
	}
	public int delete(String sqlId, DataMap map) throws SQLException {
		return commonDao.delete(sqlId, map);
	}
	public int delete(DataMap map) throws SQLException {
		return commonDao.delete(map);
	}
	
	public List getExcel(DataMap map) throws FileNotFoundException, IOException {
		return commonDao.getExcel(map);
	}
	
	public List getExcelCollist(DataMap map) throws FileNotFoundException, IOException {
		return commonDao.getExcelCollist(map);
	}
	
	@Transactional
	public DataMap gridSave(DataMap map) throws SQLException {
		
		DataMap rsMap = new DataMap();
		
		List<DataMap> list = map.getList("list");
		
		int count = 0;
		DataMap row;
		
		if(map.containsKey(CommonConfig.GRID_REQUEST_VALIDATION_KEY)){
			String validationSql = util.createValidationSql(map);
			if(validationSql.length() > 0){
				map.put(CommonConfig.VALIDATION_SQL_KEY, validationSql);
				
				List<DataMap> vList = commonDao.getValidation(map);
				if(vList.size() > 0){
					rsMap = vList.get(0);
					if(!rsMap.getString("MSG").equals("OK")){
						return rsMap;
					}					
				}
			}			
		}
		
		for(int i=0;i<list.size();i++){			
			if(map.containsKey("param")){
				row = new DataMap(map.getMap("param"));
			}else{
				row = new DataMap();
			}				
			
			map.clonModule(row);
			map.clonSessionData(row);
			row.append(list.get(i).getMap("map"));			
			//row.put(CommonConfig.SES_USER_ID_KEY, map.get(CommonConfig.SES_USER_ID_KEY));
			//row.put(CommonConfig.MODULE_ATT_KEY, map.get(CommonConfig.MODULE_ATT_KEY));
			//row.put(CommonConfig.COMMAND_ATT_KEY, map.get(CommonConfig.COMMAND_ATT_KEY));
			String rowState = row.getString(CommonConfig.GRID_ROW_STATE_ATT);
			switch(rowState.charAt(0)){
				case 'C':
					commonDao.insert(row);
					break;
				case 'U':
					System.out.println("167 >>" + row);
					commonDao.update(row);
					break;
				case 'D':
					commonDao.delete(row);
					break;
			}
			count++;
		}
		
		rsMap.put("data", count);
		
		return rsMap;
	}
	
	@Transactional
	public int insertExcel(DataMap map, List<DataMap> list) throws SQLException {
		int count = 0;
		
		for(int i=0;i<list.size();i++){
			commonDao.insertExcel(map, list.get(i));
			count++;
		}
		
		return count;
	}
	
	public String getSqlGridList(String sql) throws SQLException {
		return commonDao.getJdbcData(sql);
	}
	
	public DataMap getGridString(TreeMap gridMap, DataMap dataMap){
		StringBuilder cols = new StringBuilder();
		StringBuilder head = new StringBuilder();
		StringBuilder rows = new StringBuilder();
		
		Iterator<Integer> it = gridMap.keySet().iterator();
		int width;
		String tabs = "\t\t\t\t\t\t\t\t\t\t\t";
		cols.append("<col width='40' />\r\n");
		cols.append(tabs).append("<col width='40' />\r\n");
		
		head.append("<th CL='STD_NUMBER'></th>\r\n");
		head.append(tabs).append("\t<th GBtnCheck='true'></th>\r\n");
		
		rows.append("<td GCol='rownum'></td>\r\n");
		rows.append(tabs).append("\t<td GCol='rowCheck'></td>\r\n");
		
		DataMap data;
		int key;
		while(it.hasNext()){
			key = it.next();
			data = dataMap.getMap(gridMap.get(key));
			width = data.getInt("OUTLEN");
			if(width < 50){
				width = 50;
			}else if(width < 0){
				width = 150;
			}
			cols.append(tabs).append("<col width='").append(width).append("' />\r\n");
			
			head.append(tabs).append("\t<th CL='").append(data.getString("LABLGR")).append("_").append(data.getString("LABLKY")).append("'></th>\r\n");
			
			rows.append(tabs).append("\t<td GCol='");
			
			if(data.getString("COLTY").equals("SELECT")){
				
			}else{
				if(data.getString("COLTY").equals("TEXT")){
					rows.append("text,").append(data.getString("DDICKY"));
				}else if(data.getString("COLTY").equals("CHECK")){
					rows.append("check,").append(data.getString("DDICKY"));
				}else if(data.getString("COLTY").equals("BTN")){
					rows.append("btn,").append(data.getString("DDICKY"));
				}else if(data.getString("COLTY").equals("HTML")){
					rows.append("html,").append(data.getString("DDICKY"));
				}else if(data.getString("COLTY").equals("INPUT")){
					rows.append("input,").append(data.getString("DDICKY"));
					if(!data.getString("SHLPKY").trim().equals("")){
						rows.append(",").append(data.getString("SHLPKY"));
					}
				}
				
				if(data.getString("OBJETY").equals("CAL")){
					rows.append("' GF='C");
				}else if(data.getString("OBJETY").equals("DAT")){
					rows.append("' GF='D");
				}else if(data.getString("OBJETY").equals("TIM")){
					rows.append("' GF='T");
				}else if(data.getString("OBJETY").equals("NFL")){
					rows.append("' GF='N ").append(data.getString("DBLENG"));
					if(data.getInt("DBDECP") > 0){
						rows.append(",").append(data.getString("DBDECP"));
					}
				}
				
				rows.append("'></td>\r\n");
			}
		}
		
		DataMap rsMap = new DataMap();
		
		rsMap.put("COLS", cols.toString());
		
		rsMap.put("HEAD", head.toString());
		
		rsMap.put("ROWS", rows.toString());
		
		return rsMap;
	}
	
	public String getListString(DataMap map) throws SQLException{
		return commonDao.getListString(map);
	}

	public String getTextList(DataMap map) throws SQLException, InterruptedException{
		return commonDao.getTextList(map);
	}

	public String getJdbcData(DataMap map) throws SQLException {
		return commonDao.getJdbcData(map);
	}
	
	public int executeUpdate(String sql) throws SQLException {
		return commonDao.executeUpdate(sql);
	}
	
	public File getFile(DataMap map) throws IOException{
		return fileUtil.getFile(map.getString("PATH"), map.getString("FNAME"));
	}
	
	public void loadLabel() throws SQLException, IOException{
		List list = this.getList("Common", "SYSLABEL_LANGUAGE");
		DataMap requestMap = null;
		DataMap map;
		commonLabel.resetLabel();
		for(int i=0;i<list.size();i++){
			map = (DataMap)list.get(i);		
			requestMap = new DataMap(map);
			requestMap.setModuleCommand("Common", "SYSLABEL_JS");
			requestMap.put("LABLGR", "MENU");
			List dataList = commonDao.getList(requestMap);
			commonLabel.setLabel(map.getString("LANGCODE"), dataList);
		}
		
		FileUtil fileUtil = new FileUtil();
		List labelLangList = commonLabel.getLabelLang();
		DataMap label;
		for(int i=0;i<labelLangList.size();i++){
			StringBuilder sb = new StringBuilder();
			sb.append("var labelObj = ");
			String langKey = labelLangList.get(i).toString();
			label = commonLabel.getLabel(langKey);
			if(label.size() >0){
				Iterator it = label.keySet().iterator();
				sb.append("{\r\n");
				int count = 0;
				while(it.hasNext()){
					String key = it.next().toString();
					while(key.indexOf(" ") != -1){
						key = key.replace(' ', '_');
					}
					String value = label.getString(key);
					while(value.indexOf("\n") != -1){
						value = value.replace('\n', ' ');
					}
					while(value.indexOf("\"") != -1){
						value = value.replace('\"', '\'');
					}
					if(count > 0){
						sb.append(",\r\n");
					}
					sb.append(key).append(" : \"").append(value).append("\"");
					count++;
				}
				sb.append("\r\n};");
				fileUtil.writeStringFile(respository.getLang(), "label-"+langKey+".js", sb.toString());
			}else{
				sb.append("new Object();");
			}
		}		
	}
	
	public void loadLabelMap() throws SQLException, IOException{
		List list = this.getList("Common", "JLBLM_LANG");
		DataMap requestMap = null;
		DataMap map;
		commonLabel.resetLabel();
		for(int i=0;i<list.size();i++){
			map = (DataMap)list.get(i);		
			requestMap = new DataMap(map);
			requestMap.setModuleCommand("Common", "JLBLM");
			List dataList = commonDao.getList(requestMap);
			commonLabel.setLabel(map.getString("LANGKY"), dataList);
		}
		
		FileUtil fileUtil = new FileUtil();
		List labelLangList = commonLabel.getLabelLang();
		DataMap label;
		for(int i=0;i<labelLangList.size();i++){
			StringBuilder sb = new StringBuilder();
			String langKey = labelLangList.get(i).toString();
			label = commonLabel.getLabel(langKey);
			if(label.size() >0){
				Iterator it = label.keySet().iterator();
				while(it.hasNext()){
					Object key = it.next();
					String value = label.getString(key);
					while(value.indexOf("\n") != -1){
						value = value.replace('\n', ' ');
					}
					while(value.indexOf("\"") != -1){
						value = value.replace('\"', '\'');
					}
					sb.append("commonLabel.label.put(\"").append(key).append("\",\"").append(value).append("\");\r\n");
				}
				fileUtil.writeStringFile(respository.getLang(), "label-"+langKey+".js", sb.toString());
			}
		}		
	}
	
	public void loadMessageObj() throws SQLException, IOException{
		List list = this.getList("Common", "JMSGM_LANG");
		DataMap requestMap = null;
		DataMap map;
		commonLabel.resetMessage();
		for(int i=0;i<list.size();i++){
			map = (DataMap)list.get(i);
			requestMap = new DataMap(map);
			requestMap.setModuleCommand("Common", "JMSGM");
			List dataList = commonDao.getList(requestMap);
			commonLabel.setMessage(map.getString("LANGKY"), dataList);
		}
		
		FileUtil fileUtil = new FileUtil();
		List messageLangList = commonLabel.getMessageLang();
		DataMap message;
		for(int i=0;i<messageLangList.size();i++){
			StringBuilder sb = new StringBuilder();
			sb.append("var messageObj = ");
			String langKey = messageLangList.get(i).toString();
			message = commonLabel.getMessage(langKey);
			if(message.size() > 0){
				Iterator it = message.keySet().iterator();
				sb.append("{\r\n");
				int count = 0;
				while(it.hasNext()){
					String key = it.next().toString();
					while(key.indexOf(" ") != -1){
						key = key.replace(' ', '_');
					}
					String value = message.getString(key);
					while(value.indexOf("\n") != -1){
						value = value.replace('\n', ' ');
					}
					while(value.indexOf("\"") != -1){
						value = value.replace('\"', '\'');
					}
					if(count > 0){
						sb.append(",\r\n");
					}
					sb.append(key).append(" : '").append(value).append("'");
					count++;
				}
				sb.append("\r\n};");
				fileUtil.writeStringFile(respository.getLang(), "message-"+langKey+".js", sb.toString());
			}else{
				sb.append("new Object();");
			}		
		}		
	}
	
	public void loadMessage() throws SQLException, IOException{
		List list = this.getList("Common", "SYSMESSAGE_LANGUAGE");
		DataMap requestMap = null;
		DataMap map;
		commonLabel.resetMessage();
		for(int i=0;i<list.size();i++){
			map = (DataMap)list.get(i);
			requestMap = new DataMap(map);
			requestMap.setModuleCommand("Common", "SYSMESSAGE_JS");
			List dataList = commonDao.getList(requestMap);
			commonLabel.setMessage(map.getString("LANGCODE"), dataList);
		}
		
		FileUtil fileUtil = new FileUtil();
		List messageLangList = commonLabel.getMessageLang();
		DataMap message;
		for(int i=0;i<messageLangList.size();i++){
			StringBuilder sb = new StringBuilder();
			String langKey = messageLangList.get(i).toString();
			message = commonLabel.getMessage(langKey);
			if(message.size() > 0){
				Iterator it = message.keySet().iterator();
				while(it.hasNext()){
					Object key = it.next();
					String value = message.getString(key);
					while(value.indexOf("\n") != -1){
						value = value.replace('\n', ' ');
					}
					while(value.indexOf("\"") != -1){
						value = value.replace('\"', '\'');
					}
					sb.append("commonMessage.message.put(\"").append(key).append("\",\"").append(value).append("\");\r\n");
				}
				fileUtil.writeStringFile(respository.getLang(), "message-"+langKey+".js", sb.toString());
			}			
		}		
	}
	
	public void loadSearch() throws SQLException, IOException{
		List list = this.getList("Common", "SYSCOMMPOP");
		DataMap map;
		commonSearch.resetSearch();
		for(int i=0;i<list.size();i++){
			map = (DataMap)list.get(i);
			commonSearch.setHead(map);
			map.setModuleCommand("Common", "SYSCPOPITEM");
			List dataList = commonDao.getList(map);
			commonSearch.setItem(map.getString("COMMPOPID"), dataList);
			
			//2022.02.27 CommonSearch의 itemMap 메모리에 팝업 아이템 정보 put
			DataMap row;
			DataMap itemMap = new DataMap();
			for(int j=0;j<dataList.size();j++){
				row = (DataMap) dataList.get(j);
				itemMap.put(row.getString("CPOPITEMID"),row);
			}
			commonSearch.setItemMap(map.getString("COMMPOPID"), itemMap);
		}
	}
	
	public void loadMenu() throws SQLException, IOException{
		List list = this.getList("Common", "MENUGROUP");
		DataMap map;
		commonMenu.resetMenu();
		for(int i=0;i<list.size();i++){
			map = (DataMap)list.get(i);
			commonMenu.setGroup(map);
			map.setModuleCommand("Common", "MENUITEM");
			List dataList = commonDao.getList(map);
			commonMenu.setItem(map.getString("MENUGID"), dataList);
		}
	}

	@Transactional
	public List variantInsert(DataMap map) throws SQLException {
		
		List rsList = new ArrayList();
		
		DataMap saveVariant = map.getMap("saveVariant");
		
		if(map.getString("DEFCHK").equals("V")){
			map.setModuleCommand("Common", "USRPHDEF");
			commonDao.update(map);
		}
		
		map.setModuleCommand("Common", "USRPH");
		if(map.containsKey("UCOUNT")){
			commonDao.update(map);
			map.setModuleCommand("Common", "USRPI");
			commonDao.delete(map);
		}else{
			commonDao.insert(map);
		}
		
		map.setModuleCommand("Common", "USRPI");
		Iterator it = saveVariant.keySet().iterator();
		int count = 0;
		while(it.hasNext()){
			count++;
			
			String key = it.next().toString();
			String value = saveVariant.getString(key);
			
			DataMap newMap = new DataMap(map);
			
			String snum = String.valueOf(count);
			String inum = String.valueOf("000000").substring(0,(6-snum.length()))+snum;
			newMap.put("ITEMNO", inum);
			
			newMap.put("CTRLID", key);
			newMap.put("CTRVAL", value);
			
			commonDao.insert(newMap);
			
			rsList.add(newMap);
		}
		
		return rsList;
	}
	
	@Transactional
	public DataMap variantDelete(DataMap map) throws SQLException {
		map.setModuleCommand("Common", "USRPH");
		DataMap data = commonDao.getMap(map);
		
		commonDao.delete(map);
		
		map.setModuleCommand("Common", "USRPI");
		commonDao.delete(map);
		
		return data;
	}
	
	@Transactional
	public boolean excelDataValidation(DataMap map) throws SQLException {
		//log.info(map.getList("list").size());
		List<DataMap> list = map.getList("list");
		DataMap row;
		boolean result = true;
		for(int i=0;i<list.size();i++){
			row = list.get(i);
			if(i%2==0){
				row.put(CommonConfig.EXCEL_DATA_VALIDATION_RESULT_CODE, "S");
				row.put(CommonConfig.EXCEL_DATA_VALIDATION_RESULT_MSG, "SUCCESS");
			}else{
				row.put(CommonConfig.EXCEL_DATA_VALIDATION_RESULT_CODE, "E");
				row.put(CommonConfig.EXCEL_DATA_VALIDATION_RESULT_MSG, "ERROR");
				result = false;
			}
		}
		return result;
	}
	
	@Transactional
	public void usrloinsert(DataMap map, BaseUser user) throws SQLException {
		//map.setModuleCommand("Common", "SYSGRIDCOL");
		//commonDao.insert(map);
		
		map.setModuleCommand("Common", "SYSGRIDCOL_MERGE");
		commonDao.update(map);
		
		DataMap usrlo = user.getUsrlo();
		String progid = map.getString("PROGID");
		
		if(usrlo.containsKey(progid)){
			List list = usrlo.getList(progid);
			list.add(map);
		}else{
			List newData = new ArrayList();
			newData.add(map);
			usrlo.put(progid, newData);
		}
	}
	
	@Transactional
	public void usrloupdate(DataMap map, BaseUser user) throws SQLException {
		map.setModuleCommand("Common", "SYSGRIDCOL");
		commonDao.update(map);
		
		DataMap usrlo = user.getUsrlo();
		String progid = map.getString("PROGID");
		
		if(usrlo.containsKey(progid)){
			List list = usrlo.getList(progid);
			list.add(map);
		}else{
			List newData = new ArrayList();
			newData.add(map);
			usrlo.put(progid, newData);
		}
	}
	
	@Transactional
	public int updateAttachTbYn(String uuid,String value) throws SQLException {
		DataMap map = new DataMap();
		map.put("UUID", uuid);
		map.put("UPTBYN", value);
		map.setModuleCommand("Common", "FWCMFL0010_UPTBYN");
		
		int count = commonDao.update(map);
		
		return count;
	}
	
	@Transactional
	public int deleteAttachFile(String uuid) throws SQLException {
		String path ="", name = "";
		
		DataMap map = new DataMap();
		map.put("UUID", uuid);
		map.setModuleCommand("Common", "FWCMFL0010");
		
		DataMap attachMap = commonDao.getMap(map);
		if(attachMap != null && !attachMap.isEmpty()){
			path = attachMap.getString("PATH");
			name = attachMap.getString("FNAME");
		}
		
		try {
			fileUtil.deleteFile(path, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int count = commonDao.delete(map);
		
		return count;
	}
	
	/*
	 * 주소 기반 위도/경도 수집
	 * - list 내 각 거래처 주소(ADDR01 + ADDR02)를 기준으로 Geocode API 호출
	 * - 조회 성공 시 LATITUDE / LONGITUDE 를 세팅 후 BZPTN_DETAIL 업데이트
	 */
	@Transactional
	public DataMap setGeocode(DataMap map) throws SQLException, IOException, JSONException {
	    DataMap rsMap = new DataMap();
	    List<DataMap> list = map.getList("list");
	    int count = 0;

	    // 처리 대상이 없으면 종료
	    if (list == null || list.isEmpty()) {
	        rsMap.put("data", 0);
	        return rsMap;
	    }

	    for (int i = 0; i < list.size(); i++) {
	        DataMap item = list.get(i);

	        // list item null 체크
	        if (item == null) {
	            //System.out.println("[GEOCODE] item is null. index=" + i);
	            continue;
	        }

	        DataMap gridMap = item.getMap("map");

	        // 그리드 row 데이터 null 체크
	        if (gridMap == null) {
	            //System.out.println("[GEOCODE] gridMap is null. index=" + i);
	            continue;
	        }

	        // 처리용 row 생성
	        DataMap row = new DataMap();
	        row.append(gridMap);

	        // 공통 모듈 / 세션 정보 복사
	        map.clonModule(row);
	        map.clonSessionData(row);

	        // 주소 검색용 문자열 생성
	        String addr01 = row.getString("ADDR01");
	        String addr02 = row.getString("ADDR02");

	        addr01 = (addr01 == null) ? "" : addr01.trim();
	        addr02 = (addr02 == null) ? "" : addr02.trim();

	        String query = (addr01 + " " + addr02).trim();

	        // 주소가 비어 있으면 skip
	        if (query.length() == 0) {
	            //System.out.println("[GEOCODE] empty query. PTNRKY=" + row.getString("PTNRKY"));
	            continue;
	        }

	        try {
	            // Geocode API URL 생성
	            String apiUrl = CommonConfig.NAVER_API_GEOCODE+"?query=" + URLEncoder.encode(query, "UTF-8");

	            // 공통 GET 호출 메소드 사용
	            String responseBody = callGet(apiUrl);

	            JSONObject json = new JSONObject(responseBody);
	            JSONArray addresses = json.optJSONArray("addresses");

	            // 검색 결과 없음
	            if (addresses == null || addresses.length() == 0) {
	                //System.out.println("[GEOCODE EMPTY] PTNRKY=" + row.getString("PTNRKY") + " / 검색 결과 없음");
	                continue;
	            }

	            JSONObject first = addresses.getJSONObject(0);

	            // Geocode 응답 기준
	            // x = 경도(LONGITUDE), y = 위도(LATITUDE)
	            row.put("LATITUDE", first.optString("y"));
	            row.put("LONGITUDE", first.optString("x"));
	            //row.put("commandKey", "BZPTN_DETAIL_XY");

	            commonDao.update(row);
	            count++;

	        } catch (Exception e) {
	            // 개별 row 실패 시 전체 중단하지 않고 다음 row 계속 처리
	            //System.out.println("[GEOCODE ERROR] PTNRKY=" + row.getString("PTNRKY") + " / msg=" + e.getMessage());
	            continue;
	        }
	    }

	    rsMap.put("data", count);
	    return rsMap;
	}


	/*
	 * 2개 납품처간 거리 수집
	 * */
	@Transactional
	public DataMap setRoute(DataMap map) throws SQLException, IOException, JSONException {
		DataMap rsMap = new DataMap();
		rsMap.put("data", 0);
		rsMap.put("message", "");

		List<DataMap> list = map.getList("list");
		int count = 0;

		// 처리 대상이 없으면 종료
		if (list == null || list.size() == 0) {
		    rsMap.put("data", 0);
		    return rsMap;
		}

		// 경로 옵션 : 최적 경로
		String option = "traoptimal";

		for (int i = 0; i < list.size(); i++) {
		    DataMap item1 = list.get(i);
		    if (item1 == null) {
		        continue;
		    }

		    DataMap gridMap1 = item1.getMap("map");
		    if (gridMap1 == null) {
		        continue;
		    }

		    DataMap row1 = new DataMap();
		    row1.append(gridMap1);

		    String ptnrky1 = row1.getString("PTNRKY");
		    String ptnrty1 = row1.getString("PTNRTY");
		    String lat1 = row1.getString("LATITUDE");
		    String lon1 = row1.getString("LONGITUDE");
		    String name1 = row1.getString("NAME01");

		    // 좌표 없으면 제외
		    if (lat1.length() == 0 || lon1.length() == 0) {
		        //System.out.println("좌표 없음 #1 : PTNRKY=" + ptnrky1 + ", NAME01=" + name1);
		        continue;
		    }

		    for (int j = i + 1; j < list.size(); j++) {
		        DataMap item2 = list.get(j);
		        if (item2 == null) {
		            //System.out.println("item2 is null. index=" + j);
		            continue;
		        }

		        DataMap gridMap2 = item2.getMap("map");
		        if (gridMap2 == null) {
		            //System.out.println("gridMap2 is null. index=" + j);
		            continue;
		        }

		        DataMap row2 = new DataMap();
		        row2.append(gridMap2);

		        String ptnrky2 = row2.getString("PTNRKY");
		        String ptnrty2 = row2.getString("PTNRTY");
		        String lat2 = row2.getString("LATITUDE");
		        String lon2 = row2.getString("LONGITUDE");
		        String name2 = row2.getString("NAME01");

		        // 좌표 없으면 제외
		        if (lat2.length() == 0 || lon2.length() == 0) {
		            //System.out.println("좌표 없음 #2 : PTNRKY=" + ptnrky2 + ", NAME01=" + name2);
		            continue;
		        }

		        // 같은 거래처면 제외
		        if (ptnrky1.equals(ptnrky2) && ptnrty1.equals(ptnrty2)) {
		            continue;
		        }

		        // 같은 좌표면 제외
		        if (lat1.equals(lat2) && lon1.equals(lon2)) {
		            continue;
		        }

		        /*
		         * 납품처 코드 기준으로 작은 코드 -> 큰 코드 방향으로 고정
		         * 예)
		         *   100026, 100105 비교 시
		         *   start = 100026
		         *   goal  = 100105
		         */
		        String fromPtnrky;
		        String fromPtnrty;
		        String fromLat;
		        String fromLon;
		        String fromName;

		        String toPtnrky;
		        String toPtnrty;
		        String toLat;
		        String toLon;
		        String toName;

		        /*
		         * 거래처코드 정렬 로직 개선
		         * 1) PTNRKY 둘 다 숫자이면 숫자 비교
		         * 2) 하나라도 문자가 포함되면 문자열 비교
		         * 3) PTNRKY가 같으면 PTNRTY로 2차 비교
		         *
		         * 목적:
		         * - 문자 포함 거래처코드(P1100 등)에서도 NumberFormatException 방지
		         * - FROM / TO 저장 순서를 항상 동일하게 고정
		         */
		        String comparePtnrky1 = ptnrky1 == null ? "" : ptnrky1.trim();
		        String comparePtnrky2 = ptnrky2 == null ? "" : ptnrky2.trim();
		        String comparePtnrty1 = ptnrty1 == null ? "" : ptnrty1.trim();
		        String comparePtnrty2 = ptnrty2 == null ? "" : ptnrty2.trim();

		        boolean isNumeric1 = comparePtnrky1.matches("\\d+");
		        boolean isNumeric2 = comparePtnrky2.matches("\\d+");

		        int compareResult;

		        if (isNumeric1 && isNumeric2) {
		            // 둘 다 숫자면 숫자 크기 비교
		            java.math.BigInteger no1 = new java.math.BigInteger(comparePtnrky1);
		            java.math.BigInteger no2 = new java.math.BigInteger(comparePtnrky2);
		            compareResult = no1.compareTo(no2);
		        } else {
		            // 문자 포함 시 문자열 비교
		            compareResult = comparePtnrky1.compareToIgnoreCase(comparePtnrky2);
		        }

		        /*
		         * PTNRKY가 같으면 PTNRTY로 2차 비교
		         * (같은 거래처코드인데 유형이 다른 경우에도 순서를 고정하기 위함)
		         */
		        if (compareResult == 0) {
		            compareResult = comparePtnrty1.compareToIgnoreCase(comparePtnrty2);
		        }

		        if (compareResult <= 0) {
		            fromPtnrky = ptnrky1;
		            fromPtnrty = ptnrty1;
		            fromLat = lat1;
		            fromLon = lon1;
		            fromName = name1;

		            toPtnrky = ptnrky2;
		            toPtnrty = ptnrty2;
		            toLat = lat2;
		            toLon = lon2;
		            toName = name2;
		        } else {
		            fromPtnrky = ptnrky2;
		            fromPtnrty = ptnrty2;
		            fromLat = lat2;
		            fromLon = lon2;
		            fromName = name2;

		            toPtnrky = ptnrky1;
		            toPtnrty = ptnrty1;
		            toLat = lat1;
		            toLon = lon1;
		            toName = name1;
		        }


		        // Directions5 좌표 형식 : 경도,위도
		        String start = fromLon + "," + fromLat;
		        String goal = toLon + "," + toLat;

		        try {
		        	
		        	DataMap rowI = new DataMap();
		        	map.clonModule(rowI);
			        map.clonSessionData(rowI);
			        
			        //rowI.put("commandKey", "BZPTN_DETAIL_KM");
		            rowI.put("PTNRKY_FROM", fromPtnrky);
		            rowI.put("PTNRKY_TO", toPtnrky);
		            
		            // 수집된 경로가 있는경우 CONTINUE 구현하기
		            // 강제 수집이 필요한경우 주석 후 continue 로직 제거
		            if (commonDao.getCount(rowI) > 0) {
	 					continue;
	 				}
		        	
		        	
		            DataMap routeResult = directions(start, goal, option);

		            if (!"true".equalsIgnoreCase(routeResult.getString("success"))) {
		                continue;
		            }

		            Object distanceObj = routeResult.get("distance");
		            Object durationObj = routeResult.get("duration");

		            double distanceKm = ((Number) distanceObj).doubleValue() / 1000.0;
		            double durationMin = ((Number) durationObj).doubleValue() / 1000.0 / 60.0;

		            rowI.put("DISTANCE", distanceObj);
		            rowI.put("DURATION", durationMin);
		            
		            // DB INSERT UPDATE 구문생성
		            commonDao.update(rowI);
		            
		            count++;

		        } catch (Exception e) {
		            //System.out.println("[ROUTE ERROR] " + fromPtnrky + " -> " + toPtnrky + " / msg=" + e.getMessage());
		            log.error("[ROUTE] 거리계산 실패 :" +e.getMessage());
		            
		            rsMap.put("data",0);
		            rsMap.put("message", e.getMessage());
		        }
		    }
		}

		rsMap.put("data", count);
		return rsMap;

	}
	
	/*
	 * Directions API 호출
	 * - start, goal 형식: "경도,위도"
	 * - option 예: traoptimal, trafast
	 */
	private DataMap directions(String start, String goal, String option) throws Exception {
	    DataMap result = new DataMap();

	    String apiUrl = CommonConfig.NAVER_API_DIRECTION + "?start=" + URLEncoder.encode(start, "UTF-8") + "&goal=" + URLEncoder.encode(goal, "UTF-8") + "&option=" + URLEncoder.encode(option, "UTF-8");

	    String responseBody = callGet(apiUrl);
	    JSONObject json = new JSONObject(responseBody);
	    JSONObject route = json.optJSONObject("route");

	    // route 객체 자체가 없으면 실패 처리
	    if (route == null) {
	        result.put("success", "false");
	        result.put("raw", responseBody);
	        return result;
	    }

	    JSONArray optionRoutes = route.optJSONArray(option);

	    // 선택한 옵션의 경로가 없으면 실패 처리
	    if (optionRoutes == null || optionRoutes.length() == 0) {
	        result.put("success", "false");
	        result.put("raw", responseBody);
	        return result;
	    }

	    JSONObject firstRoute = optionRoutes.getJSONObject(0);
	    JSONObject summary = firstRoute.optJSONObject("summary");

	    result.put("success", "true");
	    result.put("distance", summary != null ? summary.optInt("distance") : 0);   // meter
	    result.put("duration", summary != null ? summary.optInt("duration") : 0);   // ms
	    result.put("tollFare", summary != null ? summary.optInt("tollFare") : 0);
	    result.put("taxiFare", summary != null ? summary.optInt("taxiFare") : 0);
	    result.put("fuelPrice", summary != null ? summary.optInt("fuelPrice") : 0);
	    // result.put("path", firstRoute.optJSONArray("path"));
	    result.put("raw", responseBody);

	    return result;
	}
	
	/*
	 * 공통 GET 호출
	 * - Naver Maps API 공통 호출 메소드
	 * - 응답코드가 2xx가 아니면 RuntimeException 발생
	 */
	private String callGet(String apiUrl) throws Exception {
	    HttpURLConnection conn = null;
	    BufferedReader br = null;

	    try {
	        URL url = new URL(apiUrl);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setConnectTimeout(5000);
	        conn.setReadTimeout(10000);
	        conn.setDoInput(true);
	        conn.setUseCaches(false);

	        // API 인증 헤더 설정
	        conn.setRequestProperty("Accept", "application/json");
	        conn.setRequestProperty("x-ncp-apigw-api-key-id", CommonConfig.NAVER_CLIENT_ID);
	        conn.setRequestProperty("x-ncp-apigw-api-key", CommonConfig.NAVER_CLIENT_SECRET);

	        int responseCode = conn.getResponseCode();

	        InputStream is = (responseCode >= 200 && responseCode < 300)
	                ? conn.getInputStream()
	                : conn.getErrorStream();

	        if (is == null) {
	            throw new RuntimeException("NCP response stream is null. HTTP=" + responseCode);
	        }
	        
	        // 응답 스트림 읽기
	        br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = br.readLine()) != null) {
	            sb.append(line);
	        }

	        String responseBody = sb.toString();

	        // HTTP 오류 응답은 예외 처리
	        if (responseCode < 200 || responseCode >= 300) {
	            throw new RuntimeException("NCP API HTTP " + responseCode + " / " + responseBody);
	        }

	        return responseBody;

	    } finally {
	        try {
	            if (br != null) {
	                br.close();
	            }
	        } catch (Exception ignore) {
	        }

	        try {
	            if (conn != null) {
	                conn.disconnect();
	            }
	        } catch (Exception ignore) {
	        }
	    }
	}
}