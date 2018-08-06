package com.ufgov.ip.dao.baseimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ufgov.ip.dao.base.IPDictDetailJdbcDao;
import com.ufgov.ip.entity.base.IpDictionaryDetail;

@Repository
public class IPDictDetailJdbcDaoImpl implements IPDictDetailJdbcDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public List<IpDictionaryDetail> findDicAndDetail(String dicType,String dicName){
		
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("select * from (")
			.append("select n.the_id as theId,n.dic_id as dicId,n.detail_key as detailKey,")
			.append(" n.detail_info as detailInfo,m.dic_type as dicType,m.dic_name as dicName")
			.append(" from ip_dictionary m ,ip_dictionary_detail n where m.dic_id=n.dic_id ");
			if(!("").equals(dicType) && dicType!=null){
				sbSql.append(" and m.dic_Type ='").append(dicType).append("'");
			}
			if(!("").equals(dicName) && dicName!=null){
				sbSql.append(" and m.dic_Name like '%").append(dicName).append("%'");
			}
			sbSql.append(" ) mn order by mn.dicType,mn.detailKey ");
		List<IpDictionaryDetail> result =new ArrayList<IpDictionaryDetail>();
		result = jdbcTemplate.query(sbSql.toString(), BeanPropertyRowMapper.newInstance(IpDictionaryDetail.class));
		
		
		return result;
		
	}
}
