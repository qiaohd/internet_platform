package com.ufgov.ip.dao.systemimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ufgov.ip.dao.system.IMenuJdbcDao;
import com.ufgov.ip.entity.system.IpMenu;
@Repository
public class IMenuJdbcDaoImpl implements IMenuJdbcDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<IpMenu> getIpMenuListByUser(String userId) {
		String existsCondition=" not exists";
		// TODO 自动生成的方法存根
		return getMenuForUser(userId, existsCondition);
	}
	
	@Override
	public List<IpMenu> getIpHideMenuListByUser(String userId) {
		String existsCondition=" exists";
		// TODO 自动生成的方法存根
		return getMenuForUser(userId, existsCondition);
	}
	
	/**
	 * 查询用户下的一级菜单
	 * @param userId  用户ID
	 * @param existsCondition  
	 * 				existsCondition="not exists" 查询不在隐藏表ip_user_hide_menu 的菜单
	 * 				existsCondition=" exists" 	  查询在隐藏表ip_user_hide_menu 的菜单
	 * @return
	 */
	private List<IpMenu> getMenuForUser(String userId, String existsCondition) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select * from ip_menu m where m.LEVEL_NUM='1' ")
			 .append(" and m.MENU_ID in (select menu_id from ip_role_menu where role_id in (select role_id from ip_user_role where user_id =?))")
			 .append(" and ").append(existsCondition)
			 .append(" (select 1 from ip_user_hide_menu um where um.menu_id=m.menu_id and um.user_id=? )")
			 .append(" order by DISP_ORDER asc");
		     
//		stringSql="select * from ip_menu m where m.LEVEL_NUM='1' and m.MENU_ID in (select menu_id from ip_role_menu "
//				+ "	where role_id in (select role_id from ip_user_role where user_id =?))"
//				+ " and not exists (select 1 from ip_user_hide_menu um where um.menu_id=m.menu_id and um.user_id=? ) "
//				+ " order by DISP_ORDER asc ";
		List result = jdbcTemplate.query(sbSql.toString(), new Object[] { userId,userId}, BeanPropertyRowMapper.newInstance(IpMenu.class));
		return result;
	}
	
	/**
	 * 查询用户ID下的所有菜单 
	 */
	@Override
	public List<IpMenu> getMenuByParentId(String userId) {
		// TODO 自动生成的方法存根
		String	stringSql="select * from ip_menu where  MENU_ID in (select menu_id from ip_role_menu "
				+ "	where role_id in (select role_id from ip_user_role where user_id =?)) order by LEVEL_NUM,DISP_ORDER asc";
		List result = jdbcTemplate.query(stringSql, new Object[] {userId }, BeanPropertyRowMapper.newInstance(IpMenu.class));
		return result;
	}
	
	@Override
	public List<IpMenu> getIpMenuListByHirer(String hirerId) {
		// TODO 自动生成的方法存根
		String existsCondition=" not exists";
		return getMenuForHirer(hirerId, existsCondition);
	}
	
	@Override
	public List<IpMenu> getIpHideMenuListByHirer(String hirerId) {
		String existsCondition=" exists";
		return getMenuForHirer(hirerId, existsCondition);
	}
	/**
	 * 查询租户下的一级菜单
	 * @param hirerId  租户ID
	 * @param existsCondition  
	 * 				existsCondition="not exists" 查询不在隐藏表ip_user_hide_menu 的菜单
	 * 				existsCondition=" exists" 	  查询在隐藏表ip_user_hide_menu 的菜单
	 * @return
	 */
	private List<IpMenu> getMenuForHirer(String hirerId, String existsCondition) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select * from ip_menu m ")
			 .append(" where level_num='1' and (AUTH_LEVEL='1' or AUTH_LEVEL='2')")
			 .append(" and ").append(existsCondition)
			 .append(" (select 1 from ip_user_hide_menu um where um.menu_id=m.menu_id and um.user_id=? )")
			 .append(" order by  DISP_ORDER asc");
//		String stringSql="select * from ip_menu m "
//				+ " where level_num='1' and (AUTH_LEVEL='1' or AUTH_LEVEL='2')  "
//				+ " and not exists (select 1 from ip_user_hide_menu um where um.menu_id=m.menu_id and um.user_id=? )"
//				+ " order by  DISP_ORDER asc ";
		List result = jdbcTemplate.query(sbSql.toString(),new Object[] {hirerId}, BeanPropertyRowMapper.newInstance(IpMenu.class));
		return result;
	}
	@Override
	public void deleteUserMenu(String userId, String menuId) {
		// TODO 自动生成的方法存根
		String stringSql="delete from ip_user_hide_menu where user_id=? and menu_id=?";
		this.jdbcTemplate.update(stringSql, new Object[] {userId,menuId});
	}
	@Override
	public void saveUserMenu(String userId, String menuId) {
		// TODO 自动生成的方法存根
		String stringSql="insert into  ip_user_hide_menu (user_id,menu_id) VALUES (?,?)";
		this.jdbcTemplate.update(stringSql, new Object[] {userId,menuId});
	}	

}
