package project.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import project.common.bean.CommonConfig;
import project.common.bean.DataMap;
import project.common.bean.DataWmsMap;

public class Util {
	
	private static Logger log = Logger.getLogger(Util.class);
	
	private String sqlType;
	
	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String createValidationSql(DataMap map){
		List<DataMap> list = map.getList("list");
		
		DataMap row;
		
		StringBuilder validationSql = new StringBuilder();
		if(map.containsKey(CommonConfig.GRID_REQUEST_VALIDATION_KEY)){
			
			String[] valCols = map.getString(CommonConfig.GRID_REQUEST_VALIDATION_KEY).split(",");
			String valType = map.getString(CommonConfig.GRID_REQUEST_VALIDATION_TYPE);
			if(valType.equals("")){
				valType = "CU";
			}
			for(int i=0;i<list.size();i++){
				row = list.get(i).getMap("map");
				String rowState = row.getString(CommonConfig.GRID_ROW_STATE_ATT);
				if(rowState.charAt(0) ==  'D'){
					continue;
				}
				if(valType.indexOf(rowState.charAt(0)) == -1){
					continue;
				}
				if(validationSql.length() != 0){
					validationSql.append("UNION ALL \n");
				}
				validationSql.append("SELECT \n");
				for(int j=0;j<valCols.length;j++){
					if(j != 0){
						validationSql.append(" ,");
					}
					validationSql.append(" '").append(row.getString(valCols[j])).append("' AS ").append(valCols[j]);
					
					validationSql.append(" \n");
				}
				
				if( !"mssql".equals(sqlType) ){
					validationSql.append("FROM DUAL \n");
				}
			}
		}
		
		return validationSql.toString();
	}
	
	public String createValidationSql(DataMap map, String key){
		List<DataMap> list = map.getList("list");
		
		DataMap row;
		
		StringBuilder validationSql = new StringBuilder();
			
		String[] valCols = key.split(",");
		for(int i=0;i<list.size();i++){
			row = list.get(i).getMap("map");
			String rowState = row.getString(CommonConfig.GRID_ROW_STATE_ATT);
			if(rowState.length() > 0 && rowState.charAt(0) ==  'D'){
				continue;
			}
			if(validationSql.length() != 0){
				validationSql.append("UNION ALL \n");
			}
			validationSql.append("SELECT \n");
			for(int j=0;j<valCols.length;j++){
				if(j != 0){
					validationSql.append(" ,");
				}
				validationSql.append(" '").append(row.getString(valCols[j])).append("' AS ").append(valCols[j]);
				
				validationSql.append(" \n");
			}

			if( !"mssql".equals(sqlType) ){
				validationSql.append("FROM DUAL \n");	
			} 				
		}
		
		return validationSql.toString();
	}
	
	public String createValidationSqlMb(DataMap map, String key){
		List<DataMap> list = map.getList("data");
		
		DataMap row;
		
		StringBuilder validationSql = new StringBuilder();
			
		String[] valCols = key.split(",");
		for(int i=0;i<list.size();i++){
			row = list.get(i).getMap("map");
			String rowState = row.getString(CommonConfig.GRID_ROW_STATE_ATT);
			if(rowState.length() > 0 && rowState.charAt(0) ==  'D'){
				continue;
			}
			if(validationSql.length() != 0){
				validationSql.append("UNION ALL \n");
			}
			validationSql.append("SELECT \n");
			for(int j=0;j<valCols.length;j++){
				if(j != 0){
					validationSql.append(" ,");
				}
				validationSql.append(" '").append(row.getString(valCols[j])).append("' AS ").append(valCols[j]);
				
				validationSql.append(" \n");
			}

			if( !"mssql".equals(sqlType) ){
				validationSql.append("FROM DUAL \n");	
			} 				
		}
		
		return validationSql.toString();
	}
	
	/**
	 * 화면에서 net.send로 넘어온다.
	 * @param map
	 * @param key
	 * @return
	 */
	public String urlCreateValidationSql(DataMap map, String key){
		List<DataMap> list = map.getList("list");
		DataMap row;
		StringBuilder validationSql = new StringBuilder();
		if(map.containsKey("key")){
			String[] valCols = map.getString("key").split(",");
			for(int i=0;i<list.size();i++){
				row = list.get(i).getMap("map");
				if(i != 0){
					validationSql.append("UNION ALL \n");
				}
				validationSql.append("SELECT \n");
				for(int j=0;j<valCols.length;j++){
					if(j != 0){
						validationSql.append(" ,");
					}
					validationSql.append(" '").append(row.getString(valCols[j])).append("' AS ").append(valCols[j]);
					
					validationSql.append(" \n");
				}
				
				if( !"mssql".equals(sqlType) ){
					validationSql.append("FROM DUAL \n");	
				}			
			}
		}
		
		return validationSql.toString();
	}
	
	/**
	 * 서비스 단에서 밸리데이션 쿼리생성. 
	 * HashMap 으로 받는다.
	 * @param map
	 * @param key
	 * @return
	 */
	public String itemCreateValidationSql(DataMap map, String key){
		List<DataMap> list = map.getList("list");
		HashMap row;
		StringBuilder validationSql = new StringBuilder();
		if(map.containsKey("key")){
			String[] valCols = map.getString("key").split(",");
			for(int i=0;i<list.size();i++){
				row = list.get(i);
				if(i != 0){
					validationSql.append("UNION ALL \n");
				}
				validationSql.append("SELECT \n");
				for(int j=0;j<valCols.length;j++){
					if(j != 0){
						validationSql.append(" ,");
					}
					validationSql.append(" '").append(row.get(valCols[j])).append("' AS ").append(valCols[j]);
					
					validationSql.append(" \n");
				}

				if( !"mssql".equals(sqlType) ){
					validationSql.append("FROM DUAL \n");
				} 			
			}
		}
		
		return validationSql.toString();
	}
	
	public Object executeBeanObject(HttpServletRequest request, String beanName, String funcName, Object param) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		//HttpSession 객체 가져오기
		 HttpSession session = request.getSession();
		 
		 //ServletContext 객체 가져오기
		 ServletContext conext = session.getServletContext();
		 
		 //Spring Context 가져오기
		 WebApplicationContext wContext = WebApplicationContextUtils.getWebApplicationContext(conext);
		 
		 //스프링 빈 가져오기 & casting
		 Object sBean = wContext.getBean(beanName);
		 
		 Method method = sBean.getClass().getMethod(funcName, new Class[]{DataMap.class});
		 
		 return method.invoke(sBean, new Object[]{param});
	}
	
	/**
	 * string to date : 문자형을 데이트형으로 형변환
	 * @param str
	 * @param format
	 * @return
	 */
	public Date stringToDate(String str, String format){
		Date date = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		try {
			date = sdf.parse(str);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return date;
	}
	
	/**
	 * date to string : 날짜형을 문자형으로 형변환
	 * @param str
	 * @param format
	 * @return
	 */
	public String dateToString(Date date, String format){
		String str = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		try {
			str = sdf.format(date);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return str;
	}
	
	public long getMonthsDifference(String strStartDate, String strEndDate, String format) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date startDate = sdf.parse(strStartDate);
        Date endDate = sdf.parse(strEndDate);

        long diffDay = (startDate.getTime() - endDate.getTime()) / (24*60*60*1000);
        
        return diffDay;
	}
	
	//240129 신규 기능 추가(map 데이터를 Json으로 변경)
	public static String mapToJson(DataMap params) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(params);
        return jsonStr;
    }
	
	public static DataMap getMap(JSONObject json) {
        DataMap paramMap = new DataMap();
        Iterator it = json.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (json.isNull(key)) {
                continue;
            }
            try {
                Object obj = json.get(key);
                if (obj instanceof JSONObject) {
                    JSONObject jObject = (JSONObject) obj;
                    paramMap.put(key, getMap(jObject));
                } else if (obj instanceof ArrayList) {
                    paramMap.put(key, obj);
                } else if (obj instanceof JSONArray) {
                    JSONArray array = (JSONArray) obj;
                    List list = new ArrayList();
                    Object tmpObj;
                    for (int i = 0; i < array.length(); i++) {
                        tmpObj = array.get(i);
                        if (tmpObj instanceof JSONObject) {
                            list.add(getMap(array.getJSONObject(i)));
                        } else {
                            list.add(tmpObj);
                        }
                    }

                    paramMap.put(key, list);
                } else {
                    paramMap.put(key, obj);
                }
            } catch (Exception e) {
                log.error("Error mapping JSON map", e);
            }
        }
        return paramMap;
    }
	
	public static DataWmsMap convertAll(DataMap param) {
        if (param == null) return null;
        DataWmsMap rsMap = new DataWmsMap();
        for (Object keyObj : param.keySet()) {
            String key = (String) keyObj;
            DataMap params = null;
            SqlUtil sqlUtil = new SqlUtil();
            if (key.startsWith("{")) {
                try {
                    JSONObject jsonObject = new JSONObject(key);
                    params = getMap(jsonObject);
                    for (Object pk : params.keySet()) {
                        if (CommonConfig.RNAGE_DATA_PARAM_KEY.equals(pk)) {
                            sqlUtil.setRangeSql(params);
                            System.out.println(params);
                            String sql = params.get(CommonConfig.RANGE_SQL_KEY).toString().replaceAll("\"", "\'");
                            rsMap.put(CommonConfig.RNAGE_DATA_PARAM_KEY, sql);
                        } else {
                            rsMap.put(pk, params.get(pk));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                rsMap.put(key, param.get(key));
            }
        }
        return rsMap;
    }
}