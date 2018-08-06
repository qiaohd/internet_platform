package com.ufgov.ip.service.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ufgov.ip.entity.database.ConnectionInfo;
import com.ufgov.ip.entity.database.Database;
import com.ufgov.ip.utils.Constant;

/**
 * 实际上这里的Connection是以库为单位的。
 * 
 * @author Enbandari
 * 
 */
public class MysqlConnection {

	private JdbcTemplate jdbcTemplate;
	private ConnectionInfo info;

	private BasicDataSource ds;

	public MysqlConnection(ConnectionInfo info) {
		this.info = info;

		ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://" + info.getHost() + ":" + info.getPort() + "/" + info.getDbname()+"?connectTimeout=3000&socketTimeout=20000");
		ds.setUsername(info.getUsername());
		ds.setPassword(info.getPassword());
		ds.setMaxWait(5000);
		ds.setMinIdle(2);
	
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.setQueryTimeout(5000);
	}

	public void close() {
		try {
			ds.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private JdbcTemplate getJdbcTemplate() {
		if(ds == null || ds.isClosed()){
			ds = new BasicDataSource();
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			ds.setUrl("jdbc:mysql://" + info.getHost() + ":" + info.getPort() + "/" + info.getDbname());
			ds.setUsername(info.getUsername());
			ds.setPassword(info.getPassword());
			jdbcTemplate = new JdbcTemplate(ds);
		}
		return jdbcTemplate;
	}

	public ConnectionInfo getInfo() {
		return info;
	}

	public void setInfo(ConnectionInfo info) {
		this.info = info;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof ConnectionInfo && (this == o || this.info.equals(((MysqlConnection) o).info))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	
	public List<Database> showDbs() {
		String sql = "show databases";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);

		debug(sql, list);
		List<Database> dblist = new ArrayList<Database>();
		Iterator<Map<String, Object>> it = list.iterator();
		while (it.hasNext()) {
			Map<String, Object> map = it.next();
			Database db = new Database();
			db.setName(map.get(Constant.key.DATABASE).toString());
			dblist.add(db);
		}
		return dblist;
	}

	

	public void useDb(String dbname) {
		String sql = "use " + dbname;
		jdbcTemplate.execute(sql);
		debug(sql);
	}
	
	public Boolean createSchema(String schemaName){
		try {
			String sql = "create schema " + schemaName;
			jdbcTemplate.execute(sql);
			return true;
		} catch (DataAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
		
	}

	

	public List<Map<String, Object>> queryAll(String tablename , int page, int pagesize) {
		int offset = (page - 1) * pagesize;
		String sql = "select * from " + tablename + " limit "+ pagesize + " offset "+offset;
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);

		debug(sql, list);
		return list;
	}
	
	
	
	private String getValue(Map<String, Object> map, String key){
		return map.get(key) == null? "" : map.get(key).toString();
	}

	private void debug(String sql, List<Map<String, Object>> list) {
		System.out.println("***************************************");
		System.out.println(sql);
		System.out.println("---------------------------------------");
		for (Map<String, Object> map : list) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				System.out.println(entry.getKey() + "-->" + entry.getValue());
			}
		}
		System.out.println("***************************************");
	}

	private void debug(String sql) {
		System.out.println(sql);
	}
}
