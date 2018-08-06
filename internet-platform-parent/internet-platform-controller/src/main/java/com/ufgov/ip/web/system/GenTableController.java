package com.ufgov.ip.web.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ufgov.ip.api.system.GenTableServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.apiUtils.StringUtils;
import com.ufgov.ip.entity.base.IpDictionaryDetail;
import com.ufgov.ip.entity.system.Dict;
import com.ufgov.ip.entity.system.GenConfig;
import com.ufgov.ip.entity.system.GenTable;
import com.ufgov.ip.entity.system.GenTableColumn;
import com.ufgov.ip.utils.CopyPropertiesUtil;
import com.uggov.ip.web.tmp.GenUtils;



/**
 * 业务表Controller
 * @author ThinkGem
 * @version 2013-10-15
 */
@Controller
@RequestMapping(value = "gen/genTable")
public class GenTableController {

	 @Autowired
	private GenTableServiceI genTableService;
	
	 @Autowired
	private UserAccountServiceI userAccountService;
	
	@Value("${context.name}")
	protected String basePath;
	
	/**
	 * 业务表查询
	 * @param genTable
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value ="list")
	public @ResponseBody List<GenTable> list(@RequestBody GenTable genTable, HttpServletRequest request, HttpServletResponse response, Model model) {
		String cuser = (String) SecurityUtils.getSubject().getPrincipal();
	    genTable.setCreateBy(cuser);
        List<GenTable> page = genTableService.find( genTable); 
		return page;
	}

	/**
	 * 业务表数据回显/数据库中数据表查询
	 * @param id
	 * @param tableName
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value = "form")
	public @ResponseBody Map<String,Object> form(@RequestParam String id,@RequestParam String tableName, Model model) {
		GenTable genTable = new GenTable();
		genTable.setId(id);
		genTable.setTableName(tableName);
		Map<String,Object> reg=new HashMap<String,Object>();
		// 获取物理表列表
		if(!StringUtils.isBlank(genTable.getId())){
			 genTable = genTableService.get(genTable.getId());
		}
		
		List<GenTable> tableList = genTableService.findTableListFormDb(new GenTable());
		List<GenTable> tableListInfo = new ArrayList<GenTable>();
		for (GenTable genTable2 : tableList) {
			CopyPropertiesUtil.setProperty(genTable2);
			if(genTable2.getId()==null){
				genTable2.setId("");
			}
			tableListInfo.add(genTable2);
		}
		reg.put("tableList", tableListInfo);
		// 验证表是否存在
		if (StringUtils.isBlank(genTable.getId()) && !genTableService.checkTableName(genTable.getTableName())){
			addMessage(reg, "下一步失败！" + genTable.getTableName() + " 表已经添加！");
			reg.put("result", "false");
			genTable.setIsGen("0");
			genTable = genTableService.findByTableNameAndGen(genTable);
			//genTable = genTableService.getTableFormDb(genTable);
			//genTable.setTableName("");//因为选完业务表并点击下一步之后，经验证该表已经添加，所以为了能让前台界面还能显示下一步页面，就把tablename设置为空即可，前台通过这个变量判断页面的展示
			
		}
		// 获取物理表字段
		else{
			genTable = genTableService.getTableFormDb(genTable);
			reg.put("result", "true");
		}
		reg.put("genTable", genTable);
		reg.put("config", GenUtils.getConfig());
		return reg;
	}
	
	
	/**
	 * 获得当前表的列属性
	 * @param id
	 * @param tableName
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value = "getColumnProperties")
	public @ResponseBody Map<String,Object> getColumnProperties(@RequestParam String id,@RequestParam String tableName, Model model) {
		GenTable genTable = new GenTable();
		genTable.setId(id);
		genTable.setTableName(tableName);
		Map<String,Object> reg=new HashMap<String,Object>();
		genTable = genTableService.findByTableName(tableName);
		getFilterColumn(genTable);
		reg.put("genTable", genTable);
		return reg;
	}
	
	/**
	 * js模板获得列属性json
	 * @param isGen
	 * @param tableName
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value = "getColForTemplate")
	public @ResponseBody Map<String,Object> getColForTemplate(@RequestParam String tableName, Model model) {
		GenTable genTable = new GenTable();
		genTable.setTableName(tableName);
		Map<String,Object> reg=new HashMap<String,Object>();
		genTable = genTableService.findByTableName(tableName);
		reg.put("genTable", genTable);
		return reg;
	}
	
	
	
	
	/**
	 * 动态获得已启用的表单元素
	 * @param tableName
	 * @param model
	 * @return  data.ColumnList-集合信息
	 * 接口：gen/genTable/getRestColumnProperties
	 * action:点击部门编辑/添加按钮调用接口
	 */
	@RequestMapping(method = RequestMethod.GET,value = "getRestColumnProperties")
	public @ResponseBody Map<String,Object> getRestColumnProperties(@RequestParam String hirerId,@RequestParam String tableName, Model model) {
		GenTable genTable = new GenTable();
	    genTable.setTableName(tableName);
	    genTable.setIsGen("1");
	    genTable.setHirerId(hirerId);
		Map<String,Object> reg=new HashMap<String,Object>();
		genTable = genTableService.findByTableNameAndIsGenAndHirerId(genTable);
		if(genTable==null){
			reg.put("ColumnList", null);
			return reg;
		}else{
			reg.put("ColumnList", genTable.getColumnList());
			return reg;
		}
		
	}
	
	
	
	/**
	 * 部门预留字段
	 * @param id
	 * @param tableName : ip_company
	 * @param model
	 * @return data.config:集合信息     data.ColumnList:集合信息
	 * 接口：gen/genTable/getCompanyRestCol
	 * action:点击页签即可访问（新增/编辑）
	 */
	@RequestMapping(method = RequestMethod.GET,value = "getCompanyRestCol")
	public @ResponseBody Map<String,Object> getCompanyRestCol(@RequestParam String hirerId,@RequestParam String tableName, Model model) {
		
	    //先看业务表里面是否存在该表(isGen)
		   //存在——查询——colList
		    GenTable genTable = new GenTable();
		    genTable.setTableName(tableName);
		    genTable.setIsGen("1");
		    genTable.setHirerId(hirerId);
		    Map<String,Object> reg=new HashMap<String,Object>();
		    //GenTable findByTableNameAndIsGen = genTableService.findByTableNameAndIsGenAndIsUse(genTable); 
		    GenTable findByTableNameAndIsGen = genTableService.findByTableNameAndIsGenAndAllUseAndHirerId(genTable); 
		    if(findByTableNameAndIsGen==null){
		    	findByTableNameAndIsGen = genTableService.getTableFormDb(genTable);
		    	findByTableNameAndIsGen.setHirerId(hirerId);
		    }
		
		getFilterColumn(findByTableNameAndIsGen);
		GenConfig config = GenUtils.getConfig();
		List<Dict> showTypeList = config.getShowTypeList();
		List<Dict> newShowTypeList =new ArrayList<Dict>();
		for (Dict dict : showTypeList) {
			if("input".equals(dict.getValue()) || "textarea".equals(dict.getValue()) || "select".equals(dict.getValue())){
				newShowTypeList.add(dict);
			}
		}
		
		reg.put("config", newShowTypeList);
		reg.put("ColumnList", findByTableNameAndIsGen.getColumnList());
		reg.put("id", findByTableNameAndIsGen.getId()==null?"":findByTableNameAndIsGen.getId());
		reg.put("hirerId", findByTableNameAndIsGen.getHirerId());
		
		return reg;
	}

	private void getFilterColumn(GenTable findByTableNameAndIsGen) {
		//过滤原有的列
		Map<String,String> colReg=new HashMap<String,String>();
		colReg.put("agcfsDwType", "agcfsDwType");
		colReg.put("hold1", "hold1");
		colReg.put("hold2", "hold2");
		colReg.put("hold3", "hold3");
		colReg.put("hold4", "hold4");
		colReg.put("hold5", "hold5");
		colReg.put("hold6", "hold6");
		colReg.put("hold7", "hold7");
		colReg.put("hold8", "hold8");
		colReg.put("hold9", "hold9");
		colReg.put("hold10", "hold10");
		
		List<GenTableColumn> columnList = findByTableNameAndIsGen.getColumnList();
		List<GenTableColumn> finalCol=new ArrayList<GenTableColumn>();
		for (GenTableColumn genTableColumn : columnList) {
              if(genTableColumn.getJavaField().equals(colReg.get(genTableColumn.getJavaField()))){
            	  finalCol.add(genTableColumn);
              }
		}
		findByTableNameAndIsGen.setColumnList(finalCol);
	}
	
	/**
	 * 保存部门预留字段的配置
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * 参数：id(添加业务表的id)
	 *      tableName(ip_company)
	 *      isGen(是否用于代码生成或部门设置-1)
	 *      
	 *      以下封装在一起最为列属性信息
	 *      columnName（数据库字段）
	 *      javaField(字段显示名称)
	 *      showType(展示形式)
	 *      dictType(字典类型)
	 *      isUse(是否启用 "Y"启用，"N"停用)
	 *      id(当前字段属性的id)
	 *   
	 * 接口：gen/genTable/restColumnSave
	 * action:编辑/保存
	 */
	@RequestMapping(method = RequestMethod.POST,value = "restColumnSave")
	public @ResponseBody Map<String, Object> restColumnSave(Model model,HttpServletRequest request, HttpServletResponse response) {
		
		Map<String,Object> reg=new HashMap<String,Object>();
		GenTable genTable = new GenTable();
		try {
			BeanUtils.populate(genTable, request.getParameterMap());
			String columnListInfo = request.getParameter("column_List");
			Gson gson = new Gson();
			List<GenTableColumn> fromJson = (List<GenTableColumn>)gson.fromJson(columnListInfo, new TypeToken<ArrayList<GenTableColumn>>() {}.getType());
			 //根据停用/启用设置curd属性
			   for (GenTableColumn genTableColumn : fromJson) {
				  if(genTableColumn.getIsUse().endsWith("N")){//字段停用
					  genTableColumn.setIsEdit("N");
					  genTableColumn.setIsInsert("N");
					  genTableColumn.setIsList("N");
					  genTableColumn.setIsNull("N");
					  genTableColumn.setIsPk("N");
					  genTableColumn.setIsQuery("N");
				  }else{
					  genTableColumn.setIsEdit("Y");
					  genTableColumn.setIsInsert("Y");
					  genTableColumn.setIsList("Y");
					  genTableColumn.setIsNull("N");
					  genTableColumn.setIsPk("N");
					  genTableColumn.setIsQuery("Y");
				  }
			   }
			   
			   //保存的时候设置每个字段的默认顺序
			   for (GenTableColumn genTableColumn : fromJson) {
				   
				   if(genTableColumn.getJavaField().equals("agcfsDwType")){
					   genTableColumn.setSort(0);
				   }else if(genTableColumn.getJavaField().equals("hold1")){
					   genTableColumn.setSort(1);
				   }else if(genTableColumn.getJavaField().equals("hold2")){
					   genTableColumn.setSort(2);
				   }else if(genTableColumn.getJavaField().equals("hold3")){
					   genTableColumn.setSort(3);
				   }else if(genTableColumn.getJavaField().equals("hold4")){
					   genTableColumn.setSort(4);
				   }else if(genTableColumn.getJavaField().equals("hold5")){
					   genTableColumn.setSort(5);
				   }else if(genTableColumn.getJavaField().equals("hold6")){
					   genTableColumn.setSort(6);
				   }else if(genTableColumn.getJavaField().equals("hold7")){
					   genTableColumn.setSort(7);
				   }else if(genTableColumn.getJavaField().equals("hold8")){
					   genTableColumn.setSort(8);
				   }else if(genTableColumn.getJavaField().equals("hold9")){
					   genTableColumn.setSort(9);
				   }else if(genTableColumn.getJavaField().equals("hold10")){
					   genTableColumn.setSort(10);
				   }else{
					   
				   }
			   }
			   
			genTable.setColumnList(fromJson);
			String cuser = (String) SecurityUtils.getSubject().getPrincipal();
		    genTable.setCreateBy(cuser);
			genTableService.saveRestColumn(genTable);
			addMessage(reg, "保存业务表'" + genTable.getTableName() + "'成功");
			reg.put("result", "true");
			return reg;
		
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(reg, "保存业务表'" + genTable.getTableName() + "失败");
			reg.put("result", "false");
			return reg;
		}
	}
	
	
	
	/**
	 * 获得下拉枚举数据
	 * @param id
	 * @param tableName
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value = "getColumnSel")
	public @ResponseBody List<IpDictionaryDetail> getColumnSel(@RequestParam String dicType,@RequestParam String dicName, Model model) {
		
		List<IpDictionaryDetail> IpDictionaryDetail=genTableService.findDicAndDetail(dicType,dicName);
		
		return IpDictionaryDetail;
	}
	
	
	/**
	 * 获得预留字段的枚举数据
	 * @param dicName
	 * @param model
	 * @return
	 * 接口：gen/genTable/getRestColumnSel
	 * 参数：根据dicName字典类型，查询对应的数据信息
	 * 返回参数：data.IpDictionaryDetail-集合信息
	 */
	@RequestMapping(method = RequestMethod.GET,value = "getRestColumnSel")
	public @ResponseBody Map<String,Object> getRestColumnSel(@RequestParam String dicName, Model model) {
		Map<String,Object> reg=new HashMap<String,Object>();
		List<IpDictionaryDetail> IpDictionaryDetail=genTableService.findDicAndDetail("",dicName);
		reg.put("IpDictionaryDetail", IpDictionaryDetail);
		return reg;
	}
	
	
	

	/**
	 * 业务表保存
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "save")
	public @ResponseBody Map<String, Object> save(Model model,HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> reg=new HashMap<String,Object>();
		GenTable genTable = new GenTable();
		try {
			BeanUtils.populate(genTable, request.getParameterMap());
			String columnListInfo = request.getParameter("column_List");
			Gson gson = new Gson();
			List<GenTableColumn> fromJson = (List<GenTableColumn>)gson.fromJson( columnListInfo, new TypeToken<ArrayList<GenTableColumn>>() {}.getType());
			genTable.setColumnList(fromJson);
			// 验证表是否已经存在
			if (StringUtils.isBlank(genTable.getId()) && !genTableService.checkTableName(genTable.getTableName())){
				addMessage(reg, "保存失败！" + genTable.getTableName() + " 表已经存在！");
				reg.put("result", "false");
				genTable.setTableName("");
				return form(genTable.getId(),genTable.getTableName(), model);
			}
			String cuser = (String) SecurityUtils.getSubject().getPrincipal();
		    genTable.setCreateBy(cuser);
			genTableService.save(genTable);
			addMessage(reg, "保存业务表'" + genTable.getTableName() + "'成功");
			reg.put("result", "true");
			return reg;
		
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(reg, "保存业务表'" + genTable.getTableName() + "失败");
			reg.put("result", "false");
			return reg;
			
		}
	}
	
	/**
	 * 业务表删除
	 * @param genTable
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "delete")
	public @ResponseBody Map<String,Object> delete(@RequestBody GenTable genTable, RedirectAttributes redirectAttributes) {
		Map<String,Object> reg=new HashMap<String,Object>();
		try{
			genTableService.delete(genTable);
			addMessage(reg, "删除业务表'" + genTable.getTableName() + "成功");
			reg.put("result", "true");
			return reg;
		}catch(Exception e){
			e.printStackTrace();
			addMessage(reg, "删除业务表'" + genTable.getTableName() + "失败");
			reg.put("result", "false");
			return reg;
		}
	}
	
	
	protected void addMessage(Map<String,Object> model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.put("reason", sb.toString());
	}

}
