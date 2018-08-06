package com.ufgov.ip.dao.systemimpl;

import java.util.List;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ufgov.ip.dao.system.IuapUserJdbcDao;

@Repository
public class IuapUserJdbcDaoImpl implements IuapUserJdbcDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List getInitUserByHirerIdAndCoId(String hirerId,String coId,String isEnabled,String loginNameLike) {

		StringBuffer sbSql=new StringBuffer();
		sbSql.append("select distinct iu.user_id as userId,iu.user_name as userName,iu.user_sex as userSex,iu.HIRER_ID as hirerId,")
			 .append(" co.co_name as coName,co.level_num  as coLevelNum,co.disp_order as coOrder,")
			 .append(" ro.role_name  as roleName,(case when ro.DISP_ORDER  is null then 999999999 else ro.DISP_ORDER end) as roleOrder,")
			 .append(" (select case when ro.DISP_ORDER  is null then 999999999 else ro.DISP_ORDER end from ip_user_role iur where iur.USER_ID=iu.USER_ID and iur.IS_PART_TIME='0') as roleUserOrder,")
			 .append(" iu.login_name as loginName,iu.user_email as userEmail,iu.cellphone_no as cellphoneNo,iu.IS_ENABLED as isEnabled")
			 .append(" from ip_user iu")
			 .append(" left join (select ic.co_name,ic.level_num,ic.disp_order,iup.USER_ID ")
			 .append("            from ip_company ic,ip_user_company iup where  iup.co_id=ic.CO_ID ) co")
			 .append(" on iu.USER_ID=co.USER_ID")
			 .append(" left join (select ir.role_name ,ir.DISP_ORDER,iur.USER_ID ")
			 .append("            from ip_role ir,ip_user_role iur where iur.ROLE_ID=ir.ROLE_ID  and iur.IS_PART_TIME='0') ro")
			 .append(" on iu.USER_ID=ro.USER_ID")
			 .append("  where iu.hirer_id=? and iu.IS_ENABLED=? and (user_type='1' or user_type is null or user_type ='' )");
			if(!"".equals(loginNameLike) && loginNameLike!=null){
				sbSql.append(" and (user_email like '%").append(loginNameLike).append("%' ")
					.append(" or login_name like '%").append(loginNameLike).append("%' ")
					.append(" or cellphone_no like '%").append(loginNameLike).append("%' ")
					.append(" or user_name like '%").append(loginNameLike).append("%') ");
			}
		if("".equals(coId)|| coId==null){
			StringBuffer querySql=new StringBuffer();
			querySql.append(" select * from (")
			.append(sbSql)
			.append(" ) mn order by coLevelNum,coOrder");
				List list =jdbcTemplate.queryForList(querySql.toString(),hirerId,isEnabled);		
					return list;
		}else{
			StringBuffer querySql=new StringBuffer();
			querySql.append(" select * from (").append(sbSql)
			.append(" and EXISTS(select 1 from ip_user_company iuc   ")
			.append(" where co_id in (select co_id from ip_company c where c.co_id=?) ")
			.append(" and iuc.user_id=iu.user_id)")
			.append(" ) mn order by coLevelNum,coOrder ");
			List list =jdbcTemplate.queryForList(querySql.toString(),hirerId,isEnabled,coId);
			
			return list;
		}
	}
}
