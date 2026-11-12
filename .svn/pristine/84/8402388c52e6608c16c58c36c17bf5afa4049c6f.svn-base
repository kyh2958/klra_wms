package project.common.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import project.common.bean.CommonConfig;
import project.common.bean.DataMap;
import project.common.util.FileUtil;
import project.common.util.SqlUtil;


@Repository
public class WcsDAO extends BaseDAO {	
	
	static final Logger log = LogManager.getLogger(WcsDAO.class.getName());
	
	@Autowired
	@Qualifier("dataSourceWcs")
	DataSource dataSource;
	
	@Autowired
	@Qualifier("sqlSessionWcs")
	private SqlSession sqlSession;
	
	public int getCount(DataMap map) throws SQLException {
		map.setSqlType(true);
		return (Integer)sqlSession.selectOne(map.getCountCommand(), map);
	}
	public int getCount(String sqlId, DataMap map) throws SQLException {
		map.setSqlType(true);
		String command = sqlId + CommonConfig.COMMAND_COUNT_TAIL;
		return (Integer)sqlSession.selectOne(command, map);
	}
	public List getPagingList(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.selectList(map.getListCommand(), map);
	}
	public List getList(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.selectList(map.getListCommand(), (HashMap)map);
	}
	public List getList(String sqlId, DataMap map) throws SQLException {
		//log.debug(map);
		map.setSqlType(true);
		String command = sqlId + CommonConfig.COMMAND_LIST_TAIL;
		//log.debug(command);
		return sqlSession.selectList(command, (HashMap)map);
	}
	public DataMap getMap(DataMap map) throws SQLException {
		map.setSqlType(true);
		return (DataMap)sqlSession.selectOne(map.getMapCommand(), map);
	}
	public DataMap getMap(String sqlId, DataMap map) throws SQLException {
		map.setSqlType(true);
		String command = sqlId + CommonConfig.COMMAND_MAP_TAIL;
		//log.debug(command);
		return (DataMap)sqlSession.selectOne(command, map);
	}
	public Object getObj(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.selectOne(map.getObjectCommand(), map);
	}
	public Object getObj(String sqlId, DataMap map) throws SQLException {
		map.setSqlType(true);
		String command = sqlId + CommonConfig.COMMAND_OBJECT_TAIL;
		//log.debug(command);
		return sqlSession.selectOne(command, map);
	}
	public Object insert(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.insert(map.getInsertCommand(), map);
	}
	
	public Object insert(String sqlId, List list) throws SQLException {
		//map.setSqlType(true);
		String command = sqlId + CommonConfig.COMMAND_INSERT_TAIL;
		//log.debug(command);
		return sqlSession.insert(command, list);
	}
	
	public Object insert(String sqlId, DataMap map) throws SQLException {
		map.setSqlType(true);
		String command = sqlId + CommonConfig.COMMAND_INSERT_TAIL;
		//log.debug(command);
		return sqlSession.insert(command, map);
	}
	public int listInsert(DataMap map, List<DataMap> list) throws SQLException {
		map.setSqlType(true);
		int count = 0;
		DataMap row;
		
		for(int i=0;i<list.size();i++){			
			if(map.containsKey("param")){
				row = new DataMap(map.getMap("param"));
			}else{
				row = new DataMap();
			}
			map.clonModule(row);
			row.append(list.get(i).getMap("map"));			
			insert(row);
			count ++; 
		}
		
		return count;
	}
	public int insertRsnum(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.update(map.getInsertCommand(), map);
	}
	public int update(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.update(map.getUpdateCommand(), map);
	}
	public int update(String sqlId, DataMap map) throws SQLException {
		map.setSqlType(true);
		String command = sqlId + CommonConfig.COMMAND_UPDATE_TAIL;
		//log.debug(command);
		return sqlSession.update(command, map);
	}
	public int listUpdate(DataMap map, List<DataMap> list) throws SQLException {
		map.setSqlType(true);
		int count = 0;
		DataMap row;
		
		for(int i=0;i<list.size();i++){			
			if(map.containsKey("param")){
				row = new DataMap(map.getMap("param"));
			}else{
				row = new DataMap();
			}				
			map.clonModule(row);
			row.append(list.get(i).getMap("map"));			
			count += update(row);
		}
		
		return count;
	}
	public int delete(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.delete(map.getDeleteCommand(), map);
	}
	public int delete(String sqlId, DataMap map) throws SQLException {
		map.setSqlType(true);
		String command = sqlId + CommonConfig.COMMAND_DELETE_TAIL;
		//log.debug(command);
		return sqlSession.delete(command, map);
	}
	public int listDelete(DataMap map, List<DataMap> list) throws SQLException {
		map.setSqlType(true);
		int count = 0;
		DataMap row;
		
		for(int i=0;i<list.size();i++){			
			if(map.containsKey("param")){
				row = new DataMap(map.getMap("param"));
			}else{
				row = new DataMap();
			}				
			map.clonModule(row);
			row.append(list.get(i).getMap("map"));	
			count += delete(row);
		}
		
		return count;
	}
	public Object insertExcel(DataMap param, DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.insert(param.getInsertCommand(), map);
	}
	public Object getObject(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.selectOne(map.getObjectCommand(), map);
	}
	public List getValidation(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.selectList(map.getValidationCommand(), map);
	}
	
	public List getExcel(DataMap map) throws FileNotFoundException, IOException{
		map.setSqlType(true);
		map.put(CommonConfig.MODULE_ATT_KEY, CommonConfig.FILE_MODULE);
		map.put(CommonConfig.COMMAND_ATT_KEY, CommonConfig.FILE_TABLE_NAME);
		
		DataMap data = (DataMap)sqlSession.selectOne(map.getMapCommand(), map);
		FileUtil fileUtil = new FileUtil();
		File file = fileUtil.getFile(data.getString("PATH"), data.getString("FNAME"));
		
		List list = null;
		int colNameRowNum = 0;
		if(map.containsKey(CommonConfig.DATA_EXCEL_COLNAME_ROWNUM_KEY)){
			colNameRowNum = map.getInt(CommonConfig.DATA_EXCEL_COLNAME_ROWNUM_KEY);
		}
		if(data.getString("MIME").equals("xls")){
			list = fileUtil.getXlsFile(file, colNameRowNum);
		}else if(data.getString("MIME").equals("xlsx")){
			list = fileUtil.getXlsxFile(file, colNameRowNum);
		}
		
		file.delete();
		//fileUtil.deleteFile(data.getString("PATH"), data.getString("FNAME"));
   
		return list;
	}
	
	public List getExcelCollist(DataMap map) throws FileNotFoundException, IOException{
		map.setSqlType(true);
		map.put(CommonConfig.MODULE_ATT_KEY, CommonConfig.FILE_MODULE);
		map.put(CommonConfig.COMMAND_ATT_KEY, CommonConfig.FILE_TABLE_NAME);
		
		DataMap data = (DataMap)sqlSession.selectOne(map.getMapCommand(), map);
		FileUtil fileUtil = new FileUtil();
		File file = fileUtil.getFile(data.getString("PATH"), data.getString("FNAME"));
		
		List list = null;
		int colNameRowNum = 0;
		if(map.containsKey(CommonConfig.DATA_EXCEL_COLNAME_ROWNUM_KEY)){
			colNameRowNum = map.getInt(CommonConfig.DATA_EXCEL_COLNAME_ROWNUM_KEY);
		}
		if(data.getString("MIME").equals("xls")){
			list = fileUtil.getXlsFileCollist(file, colNameRowNum);
		}else if(data.getString("MIME").equals("xlsx")){
			list = fileUtil.getXlsxFileCollist(file, colNameRowNum);
		}
		
		return list;
	}
		
	public String getListString(DataMap map) throws SQLException {
		//map.setSqlType(true);
		SqlUtil sqlUtil = new SqlUtil();
		//String sql = sqlUtil.getListSql(sqlMapClientTemplate, map);
		DataMap pdata = sqlUtil.getListSqlP(sqlSession, map);
		//String listData =  sqlUtil.getJdbcData(dataSource, sql);
		String listData =  sqlUtil.getJdbcDataP(dataSource, pdata);
		
		return listData;
	}

	
	public String getTextList(DataMap map) throws SQLException {
		//map.setSqlType(true);
		SqlUtil sqlUtil = new SqlUtil();
		//String sql = sqlUtil.getListSql(sqlMapClientTemplate, map);
		DataMap pdata = sqlUtil.getListSqlP(sqlSession, map);
		//String listData =  sqlUtil.getJdbcData(dataSource, sql);
		String listData =  "END "+sqlUtil.getJdbcDataP(dataSource, pdata);
		
		return listData;
	}
	
	public String getJdbcData(DataMap map) throws SQLException {
		//map.setSqlType(true);
		SqlUtil sqlUtil = new SqlUtil();
		//String sql = sqlUtil.getListSql(sqlMapClientTemplate, map);
		DataMap pdata = sqlUtil.getListSqlP(sqlSession, map);
		//String listData =  sqlUtil.getJdbcData(dataSource, sql);
		String listData =  sqlUtil.getJdbcDataP(dataSource, pdata);

		return listData;
	}
	
	public String getJdbcData(String sql) throws SQLException {
		SqlUtil sqlUtil = new SqlUtil();
		String listData = sqlUtil.getJdbcData(dataSource, sql);

		return listData;
	}

	public void reloadSql() throws Exception{
		//sqlMapClientFactoryBean.reload();
	}
	
	public int executeUpdate(String sql) throws SQLException {
		Connection conn = null;
		PreparedStatement psmt = null;
		int count = 0;
		try{
			conn = dataSource.getConnection();
			psmt = conn.prepareStatement(sql);
			count = psmt.executeUpdate(sql);

		}catch(Exception e){
			//log.error("getJdbcData : ", e);
			throw new SQLException(e.getMessage());
		}finally{
			try {
				if(psmt != null){
					psmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(conn != null){
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return count;
	}
}