package com.ufgov.ip.dao.sysmanagerimpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ufgov.ip.dao.sysmanager.IPRoleJdbcDao;
import com.ufgov.ip.entity.sysmanager.IpRoleMenu;
import com.ufgov.ip.entity.sysmanager.IpUserRole;

@Repository
public  class IPRoleJdbcDaoImp implements IPRoleJdbcDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<IpRoleMenu> findMenuForRole(String roleId){
		return null;
		
	}

	@Override
	public List getMenuForRole(String roleId) {
		// 查询语句 
		String sql = "SELECT  MENU_ID AS menuId,PARENT_MENU_NAME AS parentMenuName,PARENT_MENU_ID AS parentMenuId,"
				+ " MENU_NAME AS menuName,LEVEL_NUM AS levelNum,DISP_ORDER AS dispOrder,IS_LEAF AS isLeaf,URL AS url,"
				+ " AUTH_LEVEL AS authLevel,IS_SHOW AS isShow,MENU_DESC AS menuDesc,IS_JUMP AS isJump,"
				+ " case when (select count(1) from IP_ROLE_MENU m where m.menu_Id=p.menu_Id and m.role_Id=?)>0 "
				+ " then '1' ELSE '0' end  as isSelect"
				+ " from Ip_Menu p order by level_Num, disp_Order asc";
		List list =jdbcTemplate.queryForList(sql,roleId);		
		return list;
	}

	@Override
	public List getInitPermissionMenu(String roleId) {
		// TODO 自动生成的方法存根
		// 查询语句 
		String sql = "SELECT  MENU_ID AS menuId,PARENT_MENU_NAME AS parentMenuName,PARENT_MENU_ID AS parentMenuId,"
				+ " MENU_NAME AS menuName,LEVEL_NUM AS levelNum,DISP_ORDER AS dispOrder,IS_LEAF AS isLeaf,URL AS url,"
				+ " AUTH_LEVEL AS authLevel,IS_SHOW AS isShow,MENU_DESC AS menuDesc,IS_JUMP AS isJump "
				+ " from Ip_Menu m where "
				+ " exists (select 1 from ip_role_menu p where p.MENU_ID=m.MENU_ID and p.role_id=?)"
				+ " order by level_Num, disp_Order asc";
		List list =jdbcTemplate.queryForList(sql,roleId);		
		return list;
	}

	@Override
	public List getUserByCoId(String coId) {
		// TODO 自动生成的方法存根
		String sqlString="Select USER_ID as userId,USER_NAME as userName from ip_user m "
				+ "where m.IS_ENABLED=1 and exists (select 1 from ip_user_company p where p.user_id = m.user_id and p.co_id=? )";
		List list =jdbcTemplate.queryForList(sqlString,coId);
		return list;
	}

	@Override
	public List<IpUserRole> findRoleUserByRoleIdList(String roleId) {
		String sqlString="select the_id as theId,user_id as userId,"
			+ "(select USER_NAME from ip_user ipUser where (ipUserRole.USER_ID = ipUser.USER_ID)) AS userName,"
			+ "(select LOGIN_NAME from ip_user ipUser where (ipUserRole.USER_ID = ipUser.USER_ID)) AS loginName,"
			+ "(select CO_NAME from ip_company ipCompany where (ipUserRole.CO_ID = ipCompany.CO_ID)) AS coName,"
			+ "(select ROLE_NAME from ip_role ipRole where (ipUserRole.ROLE_ID = ipRole.ROLE_ID)) AS roleName,"
			+ " is_Part_Time as isPartTime,role_id roleId from ip_user_role ipUserRole where ipUserRole.role_id=?  order by ipUserRole.DISP_ORDER";
		// List list =jdbcTemplate.queryForList(sqlString,roleId);
		List<IpUserRole> ipUserRoles =jdbcTemplate.query(sqlString,new Object[] {roleId}, BeanPropertyRowMapper.newInstance(IpUserRole.class));
		return ipUserRoles;
	}

	@Override
	public Map<String, String> findCountRoleUserById(String roleId) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void batchUpadteRole(final List idList) {
		// TODO 自动生成的方法存根
		StringBuffer updateSb=new StringBuffer();
		updateSb.append("update ip_role set disp_order=? where role_id=? ");
		if(idList.size()==0){
			return ;
		}
		final int count=idList.size();
		jdbcTemplate.batchUpdate(updateSb.toString(),   new BatchPreparedStatementSetter() {   
            	//为prepared statement设置参数。这个方法将在整个过程中被调用的次数   
            	public void setValues(PreparedStatement ps, int i) throws SQLException {   
	                ps.setString(1, String.valueOf(i + 1));   
	                ps.setString(2, idList.get(i).toString());  
            	}
              //返回更新的结果集条数   
	          public int getBatchSize() {   
	                   return count;
	              } 
            });
		}

	@Override
	public void batchUpadteRoleUser(final List idList, final String roleId) {
		// TODO 自动生成的方法存根
		StringBuffer updateSb=new StringBuffer();
		updateSb.append("update ip_user_role set disp_order=? where user_id=? and role_id=? ");
		if(idList.size()==0){
			return ;
		}
		final int count=idList.size();
		jdbcTemplate.batchUpdate(updateSb.toString(),   new BatchPreparedStatementSetter() {   
            	//为prepared statement设置参数。这个方法将在整个过程中被调用的次数   
            	public void setValues(PreparedStatement ps, int i) throws SQLException {   
	                ps.setString(1, String.valueOf(i + 1));   
	                ps.setString(2, idList.get(i).toString());  
	                ps.setString(3, roleId);  
            	}
              //返回更新的结果集条数   
	          public int getBatchSize() {   
	                   return count;
	              } 
            });
	}
}
