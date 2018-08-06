package com.ufgov.ip.service.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.system.GenSchemeServiceI;
import com.ufgov.ip.apiUtils.StringUtils;
import com.ufgov.ip.dao.system.GenSchemeMapper;
import com.ufgov.ip.dao.system.GenTableColumnMapper;
import com.ufgov.ip.dao.system.GenTableMapper;
import com.ufgov.ip.dao.system.IuapUserDao;
import com.ufgov.ip.entity.system.GenConfig;
import com.ufgov.ip.entity.system.GenScheme;
import com.ufgov.ip.entity.system.GenTable;
import com.ufgov.ip.entity.system.GenTableColumn;
import com.ufgov.ip.entity.system.GenTemplate;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.serviceutils.GenUtils;

@Service(version = "0.0.1")
public class GenSchemeService implements GenSchemeServiceI {

	@Value("${jdbc.type}")
	private String dbType;
	@Autowired
	private GenSchemeMapper genSchemeMapper;
	@Autowired
	private GenTableMapper genTableMapper;
	@Autowired
	private GenTableColumnMapper genTableColumnMapper;
	
	@Autowired
	private IuapUserDao userDao;
	
	public GenScheme get(String id) {
		return genSchemeMapper.get(id);
	}
	
	public List<GenScheme> find(GenScheme genScheme) {
		return genSchemeMapper.findList(genScheme,dbType);
	}
	
	@Transactional(readOnly = false)
	public String save(GenScheme genScheme,String flag,String cuser) {
		if (StringUtils.isBlank(genScheme.getId())){
			genScheme.preInsert(cuser);
			genSchemeMapper.insert(genScheme);
		}else{
			genScheme.preUpdate();
			genSchemeMapper.update(genScheme);
		}
		// 生成代码
		if ("1".equals(genScheme.getFlag())){
			return generateCode(genScheme,flag);
		}
		return "";
	}
	
	@Transactional(readOnly = false)
	public void delete(GenScheme genScheme) {
		genSchemeMapper.delete(genScheme);
	}
	
	private String generateCode(GenScheme genScheme,String flag){

		StringBuilder result = new StringBuilder();
		// 查询主表及字段列
		GenTable genTable = genTableMapper.get(genScheme.getGenTable().getId());
		genTable.setClassName(genTable.getClassName());
		genTable.setColumnList(genTableColumnMapper.findList(new GenTableColumn(new GenTable(genTable.getId()))));
		
		// 获取所有代码模板及所有java类型、查询类型、字段显示类型（这些信息都写在xml配置文件里）
		GenConfig config = GenUtils.getConfig();
		
		// 获取模板列表(GenTemplate维护了生成前后台代码的模板内容信息)
		List<GenTemplate> templateList = GenUtils.getTemplateList(config, genScheme.getCategory(), false);
		//List<GenTemplate> childTableTemplateList = GenUtils.getTemplateList(config, genScheme.getCategory(), true);
		
		// 如果有子表模板，则需要获取子表列表
		/*if (childTableTemplateList.size() > 0){
			GenTable parentTable = new GenTable();
			parentTable.setParentTable(genTable.getTableName());
			genTable.setChildList(genTableMapper.findList(parentTable));
		}*/
		
		// 生成子表模板代码
		/*for (GenTable childTable : genTable.getChildList()){
			childTable.setParent(genTable);
			childTable.setColumnList(genTableColumnMapper.findList(new GenTableColumn(new GenTable(childTable.getId()))));
			genScheme.setGenTable(childTable);
			Map<String, Object> childTableModel = GenUtils.getDataModel(genScheme);
			for (GenTemplate tpl : childTableTemplateList){
				result.append(GenUtils.generateToFile(tpl, childTableModel, genScheme.getReplaceFile()));
			}
		}*/
		
		// 生成主表模板代码
		genScheme.setGenTable(genTable);
		Map<String, Object> model = GenUtils.getDataModel(genScheme);
		for (GenTemplate tpl : templateList){//遍历出前后台代码模板的基本信息（通过模板生成的文件路径及模板内容等信息）
			/*if(!"3".equals(flag)){
                  if(tpl.getName().equals("viewForm") || tpl.getName().equals("viewList")){
                	  continue;
				}
                 result.append(GenUtils.generateToFile(tpl, model, genScheme.getReplaceFile()));
			}else{
				result.append(GenUtils.generateToFile(tpl, model, genScheme.getReplaceFile()));
			}*/
			
			if("3".equals(flag) || "2".equals(flag)){
				result.append(GenUtils.generateToFile(tpl, model, genScheme.getReplaceFile()));
			}else if("1".equals(flag)){
				/*if(!tpl.getName().equals("viewJS") || !tpl.getName().equals("viewHtml") || !tpl.getName().equals("viewCss")){
					continue;
				}*/
				//begin_只生成前端代码_20161101
                    if(tpl.getName().equals("viewJS") || tpl.getName().equals("viewHtml") || tpl.getName().equals("viewCss")){
                    	result.append(GenUtils.generateToFile(tpl, model, genScheme.getReplaceFile()));
                    }else{
                    	continue;
                    } 
				//end_只生成前端代码_20161101
				//result.append(GenUtils.generateToFile(tpl, model, genScheme.getReplaceFile()));
			}else{
				if(tpl.getName().equals("viewJS") || tpl.getName().equals("viewHtml") || tpl.getName().equals("viewCss")){
              	  continue;
				}
               result.append(GenUtils.generateToFile(tpl, model, genScheme.getReplaceFile()));
			}
		}
		return result.toString();
	}
}
