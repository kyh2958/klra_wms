package project.wcs.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import project.common.bean.DataMap;
import project.common.dao.BaseDAO;
import project.common.util.SqlUtil;

@Repository
public class WcsIFDAO  extends BaseDAO {	
	@Autowired
	@Qualifier("dataSourceWcs")
	DataSource dataSource;
	
	@Autowired
	@Qualifier("sqlSessionWcs")
	private SqlSession sqlSession;
	
	
	public List<DataMap> getList(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.selectList(map.getListCommand(), (HashMap<?, ?>)map);
	}
	public DataMap getMap(DataMap map) throws SQLException {
		map.setSqlType(true);
		return (DataMap)sqlSession.selectOne(map.getMapCommand(), map);
	}
	public Object getObj(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.selectOne(map.getObjectCommand(), map);
	}
	public Object insert(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.insert(map.getInsertCommand(), map);
	}
	public int insertRsnum(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.update(map.getInsertCommand(), map);
	}
	public int update(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.update(map.getUpdateCommand(), map);
	}
	public int delete(DataMap map) throws SQLException {
		map.setSqlType(true);
		return sqlSession.delete(map.getDeleteCommand(), map);
	}
	public String getListString(DataMap map) throws SQLException {
		SqlUtil sqlUtil = new SqlUtil();
		DataMap sqlData = sqlUtil.getListSqlP(sqlSession, map);
		String listData =  sqlUtil.getJdbcDataP(dataSource, sqlData);
		
		return listData;
	}
	public String getJdbcData(DataMap map) throws SQLException {
		SqlUtil sqlUtil = new SqlUtil();
		DataMap pdata = sqlUtil.getListSqlP(sqlSession, map);
		String listData =  sqlUtil.getJdbcDataP(dataSource, pdata);

		return listData;
	}
}