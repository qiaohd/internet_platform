package com.ufgov.ip.dao.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Sort;

import com.ufgov.ip.entity.system.IpMenu;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

@MyBatisRepository
public interface IMenuMapper {

	List<IpMenu> findAll();
	List<IpMenu> findMenuByLevelNum(String levelNum);
	List<IpMenu> findMenuByLevelNumAndIsShow(@Param("levelNum") String levelNum,@Param("isShow") String isShow);
	void updateMenuByMenuId(HashMap<String, String> map);
	IpMenu findMenuByMenuName(String parentMenuName);
	void save(IpMenu ipMenu);
	void delete(String id);
	List<IpMenu> findByParentMenuId(String parentMenuId);
	IpMenu findByMenuName(String menuName);
	List<IpMenu> findMenuByMenuNameLike(String menuName);
	List<IpMenu> findMenuAll();
	IpMenu findByMenuId(String menuId);
	void updateMenuByparentMenuId(HashMap<String, String> map);
	List<IpMenu> findMenuForLevelNum(@Param("levelNum1") String levelNum1,@Param("levelNum2")  String levelNum2,@Param("levelNum3")  String levelNum3);
	
	List<IpMenu> findAll(HashMap<String,Sort> map);
	
	List<IpMenu> findMenuByAuthLevel();
	
	List<IpMenu> findMenuByConditionSql(@Param("conditionSql")String conditionSql);
	List<IpMenu> findIpUserMngMenuList();
	
}
