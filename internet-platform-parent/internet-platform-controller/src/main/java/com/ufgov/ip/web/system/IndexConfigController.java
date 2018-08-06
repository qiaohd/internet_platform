package com.ufgov.ip.web.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import uap.iweb.exception.WebRuntimeException;

import com.ufgov.ip.api.system.IndexConfigI;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.system.IndexConfigEntity;
import com.ufgov.ip.web.sysmanager.OrganController;
import com.yonyou.iuap.iweb.entity.DataTable;

@Component("org.IndexConfigController")
@Scope("prototype")
@RequestMapping(value = "/indexConfig")
public class IndexConfigController {

	public static final int HASH_INTERATIONS = 1024;
	public static final int COOKIES_MAXAGE = 60 * 60 * 24;
	private static final int SALT_SIZE = 8;
	private final Logger logger = LoggerFactory
			.getLogger(IndexConfigController.class);
	
	@Autowired
	private IndexConfigI indexConfig;
	
	/**
	 * 保存索引配置信息
	 * 接口：indexConfig/saveIndexConfig
	 * 参数：menuName  菜单名称
	 *      menuId    菜单id
	 *      catalog   分类名称
	 *      routerAddr 路由地址
	 *      interfaceAddr 接口地址
	 *      interParam  参数
	 *      isUse       是否启用
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value="saveIndexConfig")
	public @ResponseBody Map<String,Object> saveIndexConfig(HttpServletRequest request, HttpServletResponse response, Model model){
		IndexConfigEntity indexConfigEntity = new IndexConfigEntity();
		Map<String,Object> reg=new HashMap<String,Object>();
		try {
			
			BeanUtils.populate(indexConfigEntity, request.getParameterMap());
			indexConfig.saveIndexConfig(indexConfigEntity);
			reg.put("result", "true");
			return reg;
		} catch (Exception e) {
			e.printStackTrace();
			reg.put("result", "false");
			return reg;
		}
	}
	
	/**
	 * 回显配置信息
	 * 接口：indexConfig/backShowIndexConfig
	 * 参数：configId
	 * @param configId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="backShowIndexConfig")
	public @ResponseBody Map<String,Object> backShowIndexConfig(@RequestParam String configId,HttpServletRequest request, HttpServletResponse response, Model model){
		IndexConfigEntity indexConfigEntity=indexConfig.backShowIndexConfig(configId);
		Map<String,Object> reg=new HashMap<String,Object>();
		reg.put("indexConfigEntity", indexConfigEntity);
		return reg;
	}
	
	/**
	 * 删除配置信息
	 * 接口：indexConfig/deleteIndexConfig
	 * 参数：configId
	 * @param noticeId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="deleteIndexConfig")
	public @ResponseBody Map<String,Object> deleteIndexConfig(@RequestParam String configId,HttpServletRequest request, HttpServletResponse response, Model model){
		Map<String,Object> reg=new HashMap<String,Object>();
		try {
			indexConfig.deleteIndexConfig(configId);
			reg.put("result", "true");
			return reg;
		} catch (Exception e) {
			e.printStackTrace();
			reg.put("result", "false");
			return reg;
		}
		
	}
	
	/**
	 * 获得所有的配置信息
	 * 接口：indexConfig/getAllIndexConfig
	 * 参数：无参数
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	/*public void getAllIndexConfig(){
		try {
			List<IndexConfigEntity> list=indexConfig.getAllIndexConfig();
			solrConfigDataTable.remove(solrConfigDataTable.getAllRow());
			solrConfigDataTable.set(list.toArray(new IndexConfigEntity[0]));
		}catch(Exception ex){
			logger.error("查询数据失败!", ex);
			throw new WebRuntimeException("查询数据失败!");
		}
	}*/
	
	@RequestMapping(method = RequestMethod.GET,value="getAllIndexConfig")
 	public @ResponseBody Map<String,Object> getAllIndexConfig(HttpServletRequest request, HttpServletResponse response, Model model){
 		Map<String,Object> reg=new HashMap<String,Object>();
 		List<IndexConfigEntity> list=indexConfig.getAllIndexConfig();
 		reg.put("indexConfigList", list);
 		return reg;
 	}
	
}
