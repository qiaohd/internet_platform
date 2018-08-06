package com.ufgov.ip.dao.sysmanagerimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ufgov.ip.entity.sysmanager.IpCompany;
@Repository
public class ICompanyDaoImpl {

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
	//此方法mysql中不要使用
	 public List findChildByCoId(String coId){
		 String sql = "select co_Id as coId from IP_COMPANY start with co_id=? connect by prior co_id=parent_co_id";
		 List list=jdbcTemplate.queryForList(sql,coId);
		 return list;
	 }
	 public List findChildCompanyByCoId(String coId){
		 String sql = "select co_Id as coId,hirer_id as hirerId,co_name as coName from IP_COMPANY where parent_co_id=?";
		 List list=jdbcTemplate.queryForList(sql,coId);
		 return list;
	 }
}
