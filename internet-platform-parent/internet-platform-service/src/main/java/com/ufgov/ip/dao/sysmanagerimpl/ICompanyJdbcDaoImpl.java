package com.ufgov.ip.dao.sysmanagerimpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ufgov.ip.dao.sysmanager.ICompanyJdbcDao;
import com.ufgov.ip.entity.sysmanager.IpCompany;
@Repository
public class ICompanyJdbcDaoImpl implements ICompanyJdbcDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void saveCompany(IpCompany ipCompany) {

		String sql = "insert into ip_company(CO_ID,HIRER_ID,CO_CODE,CO_FULLNAME) values (?,?,?,?)";
		Object[] args = { ipCompany.getCoId(),
				ipCompany.getHirerId(), ipCompany.getCoCode(),
				ipCompany.getCoFullname() };
		jdbcTemplate.update(sql, args);

	}

	@Override
	public int[] batchUpadteCompany(final List list,final String coPId) {
		// TODO 自动生成的方法存根
		StringBuffer updateSb=new StringBuffer();
		updateSb.append("update ip_company set disp_order=? where co_id=? and PARENT_CO_ID=?");
		if(list.size()==0){
			return null;
		}
		final int count=list.size();
		int[] resultCount= jdbcTemplate.batchUpdate(updateSb.toString(),   new BatchPreparedStatementSetter() {   
	            	//为prepared statement设置参数。这个方法将在整个过程中被调用的次数   
	            	public void setValues(PreparedStatement ps, int i) throws SQLException {   
		                ps.setString(1, String.valueOf(i + 1)); 
		                System.out.println(String.valueOf(i + 1));
		                ps.setString(2, list.get(i).toString()); 
		                System.out.println(list.get(i).toString());
		                ps.setString(3, coPId);
		                System.out.println(coPId);
	            	}
	              //返回更新的结果集条数   
		          public int getBatchSize() {   
		                   return count;
		              } 
	            });   
		return resultCount;
	}
}
