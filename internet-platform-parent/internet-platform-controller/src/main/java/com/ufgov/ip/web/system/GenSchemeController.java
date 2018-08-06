package com.ufgov.ip.web.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.system.GenSchemeServiceI;
import com.ufgov.ip.api.system.GenTableServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.apiUtils.StringUtils;
import com.ufgov.ip.entity.system.GenScheme;
import com.ufgov.ip.entity.system.GenTable;
import com.ufgov.ip.entity.system.IpUser;
import com.uggov.ip.web.tmp.GenUtils;


/**
 * 生成方案Controller
 * @author ThinkGem
 * @version 2013-10-15
 */
@Controller
@RequestMapping(value = "gen/genScheme")
public class GenSchemeController{

	@Value("${context.name}")
	protected String basePath;
	
	 @Autowired
	private GenSchemeServiceI genSchemeService;
	
	 @Autowired
	private GenTableServiceI genTableService;
	
	 @Autowired
	private UserAccountServiceI userAccountService;
	
	
	 /**
		 * 生成方案查询
		 * @param genScheme
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 */
		@RequestMapping(method = RequestMethod.POST,value ="list")
		public @ResponseBody List<GenScheme> list(@RequestBody GenScheme genScheme, HttpServletRequest request, HttpServletResponse response, Model model) {
			String cuser = (String) SecurityUtils.getSubject().getPrincipal();
			genScheme.setCreateBy(cuser);
	        List<GenScheme> page = genSchemeService.find(genScheme); 
			return page;
		}

		/**
		 * 生成方案编辑
		 * @param id
		 * @param schemeName
		 * @param model
		 * @return
		 */
		@RequestMapping(method = RequestMethod.GET,value = "form")
		public @ResponseBody Map<String,Object> form(@RequestParam String id,@RequestParam String schemeName, Model model) {
			Map<String,Object> reg=new HashMap<String,Object>();
			GenScheme genScheme = new GenScheme();
			genScheme.setId(id);
			genScheme.setSchemeName(schemeName);
			if(!StringUtils.isBlank(genScheme.getId())){
				genScheme = genSchemeService.get(genScheme.getId());
			}
			
			String cuser = (String) SecurityUtils.getSubject().getPrincipal();
			if (StringUtils.isBlank(genScheme.getPackageName())){
				genScheme.setPackageName("com.ufgov.ip.modules");
			}
			if (StringUtils.isBlank(genScheme.getFunctionAuthor())){
				genScheme.setFunctionAuthor(cuser);
			}
			GenTable genTable = genScheme.getGenTable();
			if(genTable!=null){
				genTable.setId(genScheme.getGenTableId());
				genScheme.setGenTable(genTableService.get(genTable.getId()));
			}
			reg.put("genScheme", genScheme);
			reg.put("tableList", genTableService.findAll());
			return reg;
		}

		
		/**
		 * 代码自动生成及保存生成方案
		 * @param genScheme
		 * @param model
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping(method = RequestMethod.POST,value = "save")
		public  @ResponseBody Map<String,Object> save(@RequestBody GenScheme genScheme, Model model, HttpServletRequest request, HttpServletResponse response) {
			Map<String,Object> reg=new HashMap<String,Object>();
			String cuser = (String) SecurityUtils.getSubject().getPrincipal();
			try{
				GenTable genTable = new GenTable();
				genTable.setId(genScheme.getGenTableId());
				genScheme.setGenTable(genTable);
				if("3".equals(genScheme.getSchemeType())){
					//前后台代码都生成
					String result = genSchemeService.save(genScheme,"3",cuser);
					reg.put("result", "true");
					reg.put("reason", "操作生成方案'" + genScheme.getSchemeName() + "'成功<br/>"+result);
					return reg;
				}else if("1".equals(genScheme.getSchemeType())){
					//只生成后台代码
					String result = genSchemeService.save(genScheme,"1",cuser);
					reg.put("result", "true");
					reg.put("reason", "操作生成方案'" + genScheme.getSchemeName() + "'成功<br/>"+result);
					return reg;
				}else if("0".equals(genScheme.getSchemeType())){
					String result = genSchemeService.save(genScheme,"0",cuser);
					reg.put("result", "true");
					reg.put("reason", "操作生成方案'" + genScheme.getSchemeName() + "'成功<br/>"+result);
					return reg;
				}else{
					String result = genSchemeService.save(genScheme,"2",cuser);
					reg.put("result", "true");
					reg.put("reason", "操作生成方案'" + genScheme.getSchemeName() + "'成功<br/>"+result);
					return reg;
				}
			}catch(Exception e){
				e.printStackTrace();
				reg.put("result", "false");
				reg.put("reason", "操作生成方案'" + genScheme.getSchemeName() + "'失败<br/>");
				return reg;
			}
			
		}
		
		
		
		
		
		/**
		 * 删除生成方案
		 * @param genScheme
		 * @param redirectAttributes
		 * @return
		 */
		@RequestMapping(method = RequestMethod.POST,value = "delete")
		public @ResponseBody Map<String,Object> delete(@RequestBody GenScheme genScheme, RedirectAttributes redirectAttributes) {
			Map<String,Object> reg=new HashMap<String,Object>();
			try{
				genSchemeService.delete(genScheme);
				reg.put("result", "true");
				reg.put("reason", "删除生成方案成功");
				return reg;
			}catch(Exception e){
				e.printStackTrace();
				reg.put("result", "false");
				reg.put("reason", "删除生成方案失败");
				return reg;
			}
			
		}

}
