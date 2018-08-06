package com.ufgov.ip.service.system;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.SolrServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.dao.system.IndexConfigMapper;
import com.ufgov.ip.dao.system.IpHirerMapper;
import com.ufgov.ip.dao.system.IuapUserDao;
import com.ufgov.ip.entity.base.SolrEntity;
import com.ufgov.ip.entity.system.IndexConfigEntity;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.serviceutils.HtmlRegexpUtil;
import com.ufgov.ip.serviceutils.NGramUtil;
import com.ufgov.ip.utils.CopyPropertiesUtil;
import com.yonyou.iuap.search.processor.SearchClient;
import com.yonyou.iuap.search.processor.impl.HttpSearchClient;
import com.yonyou.iuap.search.query.component.Criteria;
import com.yonyou.iuap.search.query.component.SolrRestrictions;
import com.yonyou.iuap.search.query.exception.SearchException;
import com.yonyou.iuap.search.query.pojo.FacetParam;
import com.yonyou.iuap.search.query.pojo.HighlightParam;
import com.yonyou.iuap.search.query.pojo.SearchResult;

@Service(version = "0.0.1")
public class SolrService implements SolrServiceI {
	
	@Value("${solr.url}")
    private String solrUrl;
	
	@Autowired
	private IndexConfigMapper indexConfigMapper;
	
	@Autowired
	protected HirerAccountServiceI hirerAccountService;
	
	@Autowired
	protected UserAccountServiceI userAccountService;
	
	

	public <T> List<SolrEntity> getSolrResult(String index_catalog,T copyClass1) {
		   SearchResult searchResult=null;
		   getSolrResultInfo(index_catalog,copyClass1,searchResult,true);
		   SolrDocumentList results = searchResult.getResults();
		  List<SolrEntity> indexList=new ArrayList<SolrEntity>();//存放
		  for (SolrDocument solrDocument : results) {
			    SolrEntity solrEntity = new SolrEntity();
				Map<String, Map<String, List<String>>> highlightMap = searchResult.getHighlightMap();
		        List<String> list = highlightMap.get(solrDocument.get("id")).get("solrKeywords");
		        String name="";
		        if ( null != list) {
		        	name  =list.get(0);
				} else {
					name=((List<String>) solrDocument.get("solrKeywords")).get(0);
				}
		        
		    	Collection<Object> fieldValues = solrDocument.getFieldValues("content");//获得内容，有可能是多个域的集合
		    	  //将集合内容集合瓶装成字符串
		    	StringBuffer stringBuffer=new StringBuffer();
		    	for (Object contentEle : fieldValues) {
		    		contentEle=(String)contentEle;
		    		stringBuffer.append(contentEle+",");
				}
		    	
		    	solrEntity.setName(name);
		    	solrEntity.setContent(stringBuffer.toString());
		    	indexList.add(solrEntity);
		  }
		  return indexList;
        
    }

	 public <T> Map<String,Object> getSolrResultInfo(String index_catalog, T copyClass1,SearchResult searchResult,boolean isPage) {
		        List<String> solrValue = CopyPropertiesUtil.getSolrValue(copyClass1);
				SearchClient searchClient = new SearchClient();
				HttpSearchClient client = searchClient.createHttpSearchClient(solrUrl);
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				//查询所有的索引信息
			      HighlightParam highlightParam = new HighlightParam();
				  Criteria<Object> criteria = client.createCriteria(Object.class);
				  if("".equals(solrValue.get(0)) || solrValue.get(0)==null){
					  if(isPage){
						  criteria.addQuery(SolrRestrictions.eq("solrKeywords", ClientUtils.escapeQueryChars(solrValue.get(0)).equals("")?"*":ClientUtils.escapeQueryChars(solrValue.get(0)))).addFilterQuery(SolrRestrictions.eq("index_catalog", index_catalog))
                          .addFilterQuery(SolrRestrictions.eq("hirerId", solrValue.get(1))).addFilterQuery(SolrRestrictions.eq("isPublish", solrValue.get(3)))
                          .addPageParam(new PageRequest((Integer.valueOf(solrValue.get(2))-1), 10, new Sort(new Sort.Order(Sort.Direction.DESC, "updateDate"))));
					  }else{
						  criteria.addQuery(SolrRestrictions.eq("solrKeywords", ClientUtils.escapeQueryChars(solrValue.get(0)).equals("")?"*":ClientUtils.escapeQueryChars(solrValue.get(0)))).addFilterQuery(SolrRestrictions.eq("index_catalog", index_catalog))
                          .addFilterQuery(SolrRestrictions.eq("hirerId", solrValue.get(1))).addFilterQuery(SolrRestrictions.eq("isPublish", solrValue.get(3)));
					  }
					  
				  }else{
					  if(isPage){
						  criteria.addQuery(SolrRestrictions.eq("solrKeywords", ClientUtils.escapeQueryChars(solrValue.get(0)).equals("")?"*":ClientUtils.escapeQueryChars(solrValue.get(0)))).addFilterQuery(SolrRestrictions.eq("index_catalog", index_catalog))
                          .addFilterQuery(SolrRestrictions.eq("hirerId", solrValue.get(1))).addFilterQuery(SolrRestrictions.eq("isPublish", solrValue.get(3)))
                          .addPageParam(new PageRequest((Integer.valueOf(solrValue.get(2))-1), 10, new Sort(new Sort.Order(Sort.Direction.DESC, "updateDate"))));
					  }else{
						 
						  criteria.addQuery(SolrRestrictions.eq("solrKeywords", ClientUtils.escapeQueryChars(solrValue.get(0)).equals("")?"*":ClientUtils.escapeQueryChars(solrValue.get(0)))).addFilterQuery(SolrRestrictions.eq("index_catalog", index_catalog))
                          .addFilterQuery(SolrRestrictions.eq("hirerId", solrValue.get(1))).addFilterQuery(SolrRestrictions.eq("isPublish", solrValue.get(3)));//.addFacetParam(facetParam)
					  }
				  }
				  highlightParam.setHlFields("name,content").setHlPrefix("<font color='red'>").setHlPost("</font>")
		            .setHighlightSnippets(3).setHighlightFragsize(900);
		          criteria.addHighlightParam(highlightParam);
				  try {
					Page<Object> result = client.search(criteria);
					Long totalElements = result.getTotalElements();
					searchResult = client.searchForResult(criteria);
					hashMap.put("searchResult", searchResult);
					hashMap.put("totalElements", totalElements);
					return hashMap;
				} catch (SearchException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				  return hashMap;
	}

	 
	 
	@Override
	public <T> Map<String,Object> getAllSolrNoticeResult(String index_catalog,
			T copyClass1, SearchResult searchResult) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		List<String> solrValue = CopyPropertiesUtil.getSolrValue(copyClass1);
		SearchClient searchClient = new SearchClient();
		HttpSearchClient client = searchClient.createHttpSearchClient(solrUrl);
		  Criteria<Object> criteria = client.createCriteria(Object.class);
		  criteria.addQuery(solrValue.get(0).equals("")?SolrRestrictions.like("solrKeywords", solrValue.get(0)):SolrRestrictions.eq("solrKeywords", solrValue.get(0)))
		  .addFilterQuery(SolrRestrictions.like("hirerId", solrValue.get(1)))
		  .addFilterQuery(SolrRestrictions.eq("index_catalog", index_catalog));
		  try {
			    Page<Object> result = client.search(criteria);
				Long totalElements = result.getTotalElements();
				searchResult = client.searchForResult(criteria);
				hashMap.put("totalElements", totalElements);
				hashMap.put("searchResult", searchResult);
				return hashMap;
			} catch (SearchException e) {
				e.printStackTrace();
			}
		  return hashMap;
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> findSolrResult(String index_catalog,
			String keywords, String searchDate, String searchResult,String pageNo,String userId, String hirerId) {
		    SearchClient searchClient = new SearchClient();
		    HttpSearchClient client = searchClient.createHttpSearchClient(solrUrl);
		  //查询所有的索引信息
		      HighlightParam highlightParam = new HighlightParam();
			  Criteria<SolrEntity> criteria = client.createCriteria(SolrEntity.class);
		  //获得当前分类的配置信息
			    String interParam="";
				String interfaceAddr="";
				String routerAddr="";
				List<SolrEntity> indexList=new ArrayList<SolrEntity>();//存放
				Map<String,Object> reg=new HashMap<String,Object>();
			  if(!"".equals(index_catalog)){
				  IndexConfigEntity indexConfig = indexConfigMapper.getIndexConfig(index_catalog);
				   if(indexConfig==null){
					   reg.put("indexList", null);
					   return reg;
				   }
				   interParam = indexConfig.getInterParam();//参数
				   interfaceAddr = indexConfig.getInterfaceAddr();//接口地址
				   routerAddr = indexConfig.getRouterAddr();//路由地址
			  }
			 
			  
			  //处理时间过滤条件
			  Date startDate=null;
			  Date endDate=null;
			  if(!"".equals(searchDate)){
				   startDate = StrToDate(searchDate);
				   String now = dateTransforToString(new Date());
				   endDate = StrToDate(now);
			  }
			  
			  
		  //默认查所有的
	          try {
	        	  //criteria=criteria.addQuery(keywords.equals("")?SolrRestrictions.like("solrKeywords", keywords):SolrRestrictions.like("solrKeywords", keywords)).addFilterQuery(
	        	  criteria=criteria.addQuery(SolrRestrictions.eq("solrKeywords", ClientUtils.escapeQueryChars(keywords).equals("")?"*":ClientUtils.escapeQueryChars(keywords))).addFilterQuery(
	        	  SolrRestrictions.or(SolrRestrictions.eq("userId", userId),
                                  SolrRestrictions.eq("hirerId", hirerId)))
	                              .addFilterQuery(SolrRestrictions.eq("index_catalog", index_catalog.equals("")?"*":index_catalog))
	                              .addPageParam(new PageRequest((Integer.valueOf(pageNo)-1), 10,new Sort(new Sort.Order(Sort.Direction.DESC, "updateDate"))));
	        	  
	        		  if(!"".equals(searchDate)){
	        			 // criteria.addFilterQuery(SolrRestrictions.between("updateDate", startDate, endDate));
	        			  criteria.addFilterQuery(SolrRestrictions.betweenEq("updateDate", startDate, endDate));
	        		  }
	          
	        	 Page<Object> result = client.search(criteria);
	  			 Long totalElements = result.getTotalElements();
	  			 reg.put("totalElements", Integer.valueOf(String.valueOf(totalElements)));
	  			 highlightParam.setHlFields("name,content").setHlPrefix("<font color='red'>").setHlPost("</font>")
	  	            .setHighlightSnippets(3).setHighlightFragsize(900);
	  	          criteria.addHighlightParam(highlightParam);
				SearchResult searchForResult = client.searchForResult(criteria);
		//重构结果集
				SolrDocumentList results = searchForResult.getResults();
				if(results.size()==0){
					reg.put("indexList", null);
					return reg;
				}
				
				
				for (SolrDocument solrDocument : results) {
					if(index_catalog.equals("")){
		        		  IndexConfigEntity indexConfig = indexConfigMapper.getIndexConfig((String)solrDocument.get("index_catalog"));
		        		  if(indexConfig!=null){
		        			  interParam = indexConfig.getInterParam();//参数
			      			   interfaceAddr = indexConfig.getInterfaceAddr();//接口地址
			      			   routerAddr = indexConfig.getRouterAddr();//路由地址
		        		  }
		      			  
		        		}
					
					SolrEntity solrEntity = new SolrEntity();
					Map<String, Map<String, List<String>>> highlightMap = searchForResult.getHighlightMap();
			        //begin_高亮bug修复_20161223_v1.0
					   List<String> list = highlightMap.get(solrDocument.get("id")).get("name");//包含了多个关键字片段，并且用字段标记
			           List<String> contentlist = highlightMap.get(solrDocument.get("id")).get("content");//包含了多个关键字片段，并且用字段标记
			        //end_高亮bug修复_20161223_v1.0
			           
			           
			        StringBuffer catelogLink=new StringBuffer();
			        StringBuffer contentLink=new StringBuffer();
			        StringBuffer paramLink=new StringBuffer();
			        Map<String,String> paramKV=new HashMap<String, String>();
			        String name=((String) solrDocument.get("name")).split(":")[1];//标题名称
			        //List<String> solrContent= ((List<String>) solrDocument.get("content"));
			        
			        //封装查询参数
			        List<String> solrQParam = (List<String>)solrDocument.get("queryParams");
					for (String paramValue : solrQParam) {
						String[] split = paramValue.split(":");
						String key=split[0];
						if(!"NA".equals(key)){
							String value=split[1];
								paramKV.put(key, value);
						}
						
					}
			        
			        if(!"".equals(keywords)){//防止全红标记
			        	
			        	if( null != list || contentlist!=null){
			        		if(list!=null){
			        			for (String key : list) {
						        	String[] kv= key.split(":");
						        	if(!kv[0].contains("<font color='red'>".toString()) 
						        			&& !kv[0].contains("</font>".toString())
						        			&& kv[1].contains("<font color='red'>".toString())
						        			&& kv[1].contains("</font>".toString())){
						        		if(kv[0].equals("name")){//标题名称
							        		name=kv[1];
							        	}else{
							        		String[] split = kv[0].split("/");
							        		if(split[1].equals("catalog")){
							        			catelogLink.append("【"+kv[1]+"】 ");
							        		}else{
							        			//begin_solr_查询参数
								        				contentLink.append(kv[1]+" ");
							        			//end_solr_查询参数
							        		}
							        	}
						        	}
						        	
								}
				        		contentLink.setLength(0);//清空数据
			        		}
			        		
			        		if(contentlist!=null){
			        			//begin_高亮bug修复_20161223_v1.0_
				        		for (String key : contentlist) {
						        	String[] kv= key.split(":");
						        	if(!kv[0].contains("<font color='red'>".toString()) 
						        			&& !kv[0].contains("</font>".toString())
						        			&& kv[1].contains("<font color='red'>".toString())
						        			&& kv[1].contains("</font>".toString())){
						        		if(kv[0].equals("name")){//标题名称
						        			name=kv[1];;
							        	}else{
							        		String[] split = kv[0].split("/");
							        		if(split[1].equals("catalog")){
							        			catelogLink.append("【"+kv[1]+"】 ");
							        		}else{
							        			//begin_solr_查询参数
								        				contentLink.append(kv[1]+" ");
							        			//end_solr_查询参数
							        		}
							        	}
						        	}
						        	
								}
				        		//end_高亮bug修复_20161223_v1.0_		
			        		}
			        		
			        		if(contentLink.toString().equals("")){
			        			getSolrResultInfo(interParam, solrDocument,
										catelogLink, contentLink, paramKV);
			        		}
			        		
			        		setSolrEntity(interParam, interfaceAddr,
									routerAddr, solrDocument, solrEntity, name,
									catelogLink, contentLink, paramLink,
									paramKV,index_catalog,userId,hirerId);
			        		indexList.add(solrEntity);
			        	}else{//处理高亮信息为空的，但是结果集有数据
			        		
			        		String high_name = HtmlRegexpUtil.filterHtml((String)solrDocument.get("name"));
			        		List<String> high_content = (List<String>)solrDocument.get("content");
			        		Map<String,String> nameMap=new HashMap<String,String>();
			        		Map<String,String> contentMap=new HashMap<String,String>();
			        		
			        		String[] split_name = high_name.split(":");
			        		
			        		//获得smartcn的分词结果
			        		List<String> wordList=NGramUtil.testtokenizer(keywords);
			        		for (String word : wordList) {
			        			if(word.length()==1 && !word.contains(" ")){
			        				word=word.toLowerCase();
			        				name=name.toLowerCase();
			        				String split_content_info="";
			        				if(name.contains(word)){
			        					//name=name.replace(word, "<font color='red'>"+word+"</font>");
			        					nameMap.put(word, "<font color='red'>"+word+"</font>");
			        				}
			        				contentLink.setLength(0);
			        				for (String content : high_content) {
			        					/*String[] split_content = content.split(":");
			        					String[] split=split_content[0].split("/");
			        					split_content_info=split_content[1];
			        					
			        					if(split[1].equals("catalog")){
						        			catelogLink.append("【"+split_content[1]+"】 ");
						        		}else{
						        			if(split_content[1].contains(word)){
						        				split_content_info=split_content_info.replace(word, "<font color='red'>"+word+"</font>");
				        						contentLink.append(split_content_info+" ");
				        					}
						        		}*/
									
			        					content=HtmlRegexpUtil.filterHtml(content);//过滤所有的html标签
			        					String[] split_content = content.split(":");
			        					String[] split_flag=split_content[0].split("/");
			        					for(int i=1;i<split_content.length;i++){
			        						split_content_info+=split_content[i];
			        					}
			        					if(split_flag[1].equals("catalog")){
			        						if(catelogLink.length()==0){
			        							catelogLink.append("【"+split_content[1]+"】 ");
			        						}
						        			
						        		}else{
						        			split_content_info=split_content_info.toLowerCase();
						        			if(split_content_info.contains(word)){
						        				contentMap.put(word, "<font color='red'>"+word+"</font>");
				        					}
						        			contentLink.append(split_content_info+" ");
						        		}
			        				}
			        			}
							}
			        		
			        		if(contentLink.toString().equals("") && (name.contains("<font color='red'>")||name.contains("</font>"))){
			        			getSolrResultInfo(interParam, solrDocument,
										catelogLink, contentLink, paramKV);
			        		}else{
			        			
			        			for(Map.Entry<String, String> entry:nameMap.entrySet()){
			        				
			        				if((!name.contains("<font color='red'>") && !name.contains("</font>"))){
			        					name=name.replace(entry.getKey(),entry.getValue());
			        				}else{
			        					
			        					//将标签转成另外一种形式
			        					name=name.replace("<font color='red'>", "%*#!");
			        					name=name.replace("</font>", "!#*%");
			        					name=name.replace(entry.getKey(),entry.getValue());
			        					name=name.replace( "%*#!","<font color='red'>");
			        					name=name.replace("!#*%","</font>");
			        				}
			        			} 
			        			
			        			String contentLink_tmp = contentLink.toString();
			        			for(Map.Entry<String, String> entry:contentMap.entrySet()){
			        				if((!contentLink_tmp.contains("<font color='red'>") && !contentLink_tmp.contains("</font>"))){
			        					contentLink_tmp=contentLink_tmp.replace(entry.getKey(),entry.getValue());
			        				}else{
			        					contentLink_tmp=contentLink_tmp.replace("<font color='red'>", "%*#!");
			        					contentLink_tmp=contentLink_tmp.replace("</font>", "!#*%");
			        					contentLink_tmp=contentLink_tmp.replace(entry.getKey(),entry.getValue());
			        					contentLink_tmp=contentLink_tmp.replace( "%*#!","<font color='red'>");
			        					contentLink_tmp=contentLink_tmp.replace("!#*%","</font>");
			        				}
			        			} 
			        			contentLink.setLength(0);
			        			contentLink.append(contentLink_tmp);
			        		}
			        		
			        		if((name.contains("<font color='red'>")||name.contains("</font>")) || (contentLink.toString().contains("<font color='red'>")||contentLink.toString().contains("</font>"))){
			        			setSolrEntity(interParam, interfaceAddr,
										routerAddr, solrDocument, solrEntity, name,
										catelogLink, contentLink, paramLink,
										paramKV,index_catalog,userId,hirerId);
				        		indexList.add(solrEntity);
			        		}
			        	}
			        }else{
			        	
			        	name = getSolrResultInfo(interParam, solrDocument,
								catelogLink, contentLink, paramKV);
			        	if(index_catalog.equals("")){
			        		  IndexConfigEntity indexConfig = indexConfigMapper.getIndexConfig((String)solrDocument.get("index_catalog"));
			      			   if(indexConfig!=null){
			      				 interParam = indexConfig.getInterParam();//参数
				      			   interfaceAddr = indexConfig.getInterfaceAddr();//接口地址
				      			   routerAddr = indexConfig.getRouterAddr();//路由地址
			      			   }
			        		   
			        		}
			        	setSolrEntity(interParam, interfaceAddr, routerAddr,
								solrDocument, solrEntity, name, catelogLink,
								contentLink, paramLink, paramKV,index_catalog,userId,hirerId);
		        		indexList.add(solrEntity);
			        }
				}
				reg.put("indexList", indexList);
				return reg;
			} catch (SearchException e) {
				e.printStackTrace();
				reg.put("indexList", null);
				return reg;
			}
			
		
	}

	private String getSolrResultInfo(String interParam,
			SolrDocument solrDocument, StringBuffer catelogLink,
			StringBuffer contentLink, Map<String, String> paramKV) {
		String name;
		String solrTitle = (String) solrDocument.get("name");
		List<String> solrContent= ((List<String>) solrDocument.get("content"));
		String[] solrTitleArr = solrTitle.split(":");
		name=solrTitleArr[1];//标题
		for (String contentValue : solrContent) {
			String[] split = contentValue.split(":");
			String[] split2 = split[0].split("/");
			if(split2[1].equals("catalog")){
				if(catelogLink.length()==0){
					catelogLink.append("【"+split[1]+"】 ");
				}
			}else{
					contentLink.append(split[1]+" ");
			}
		}
		return name;
	}

	private void setSolrEntity(String interParam, String interfaceAddr,
			String routerAddr, SolrDocument solrDocument,
			SolrEntity solrEntity, String name, StringBuffer catelogLink,
			StringBuffer contentLink, StringBuffer paramLink,
			Map<String, String> paramKV,String index_catalog,String userId,String hirerId) {
		String solr_content=catelogLink.toString()+contentLink.toString();//内容
		String solr_updatetime=null;
		if((Date)solrDocument.get("updateDate")!=null){
			solr_updatetime = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)solrDocument.get("updateDate"))).toString();
				String[] timeArr = solr_updatetime.split("\\s+");
				solr_updatetime=timeArr[0];
		}
		
		
		String[] params = interParam.split(",");
		//判断参数
		for (int i=0;i<params.length;i++) {
			
			String paramValue = paramKV.get(params[i]);
			if(paramValue!=null){
				if(i<params.length-1){
					paramLink.append(params[i]+"="+(paramValue==null?"":paramValue)+"&");
				}else{
					paramLink.append(params[i]+"="+(paramValue==null?"":paramValue));
				}
			}
		}
		String solr_params = paramLink.toString();//参数
		String solr_PK=(String)solrDocument.get("id");//主键
		
		//标题
		solrEntity.setName(name);
		//内容
		solrEntity.setContent(solr_content);
		//接口地址
		solrEntity.setSearchUrl(interfaceAddr);//接口地址
		//参数
		solrEntity.setParams(solr_params);
		//路由地址
		solrEntity.setRouterUrl(routerAddr);
		//更新日期
		if((Date)solrDocument.get("updateDate")==null && (Date)solrDocument.get("createDate")!=null){
			solr_updatetime = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)solrDocument.get("createDate"))).toString();
			String[] timeArr = solr_updatetime.split("\\s+");
			solr_updatetime=timeArr[0];
		}
		solrEntity.setUpDateDate(solr_updatetime);
		//创建人
		if("行政管理".equals(index_catalog)){
			IpUser findUserByUserId = userAccountService.findUserByUserId(userId);
			solrEntity.setLinkMan(findUserByUserId.getUserName());
		}else{
			IpHirer findHirerByHirerId = hirerAccountService.findHirerByHirerId(hirerId);
			solrEntity.setLinkMan(findHirerByHirerId.getLinkman());
		}
		
		//主键
		solrEntity.setPk(solr_PK);
	}

	
	private IpUser getCurUserId() {
		String cuser = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
			IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser); //普通用户
			
		return ipUser;
	}

	
	/**
	 * 将日期字符串转成Date对象
	 * @param str
	 * @return
	 */
	public Date StrToDate(String str) {
		  
		   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		   Date date = null;
		   try {
		    date = format.parse(str);
		   } catch (Exception e) {
		    e.printStackTrace();
		   }
		   return date;
		}
	
	/**
	 * 将Date对象转成字符串
	 * @param now
	 * @return
	 */
	public String dateTransforToString(Date now){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		String nowTimeToString = sdf.format(now);
		return nowTimeToString;
	}

	@Override
	public Integer findSolrResultNum(String index_catalog,
			String keywords, String searchDate, String searchResult,
			String pageNo, String userId, String hirerId, boolean isPage) {
		
		SearchClient searchClient = new SearchClient();
	    HttpSearchClient client = searchClient.createHttpSearchClient(solrUrl);
	    Criteria<SolrEntity> criteria = client.createCriteria(SolrEntity.class);
	  //处理时间过滤条件
		  Date startDate=null;
		  Date endDate=null;
		  if(!"".equals(searchDate)){
			   startDate = StrToDate(searchDate);
			   String now = dateTransforToString(new Date());
			   endDate = StrToDate(now);
		  }
		  
		  if("".equals(searchDate)){
			  criteria.addQuery(keywords.equals("")?SolrRestrictions.like("solrKeywords", keywords):SolrRestrictions.eq("solrKeywords", keywords)).addFilterQuery(
                      SolrRestrictions.or(SolrRestrictions.eq("userId", userId),
                              SolrRestrictions.eq("hirerId", hirerId)))
                              .addFilterQuery(index_catalog.equals("")?SolrRestrictions.like("index_catalog", index_catalog):SolrRestrictions.eq("index_catalog", index_catalog));
		  }else{
			  criteria.addQuery(keywords.equals("")?SolrRestrictions.like("solrKeywords", keywords):SolrRestrictions.eq("solrKeywords", keywords)).addFilterQuery(
                      SolrRestrictions.or(SolrRestrictions.eq("userId", userId),
                              SolrRestrictions.eq("hirerId", hirerId)))
                              .addFilterQuery(index_catalog.equals("")?SolrRestrictions.like("index_catalog", index_catalog):SolrRestrictions.eq("index_catalog", index_catalog))
                              .addFilterQuery(SolrRestrictions.between("updateDate", startDate, endDate));
		  }
		  
		    
			try {
				Page<Object> result = client.search(criteria);
				Long totalElements = result.getTotalElements();
				return Integer.valueOf(String.valueOf(totalElements));
			} catch (SearchException e) {
				e.printStackTrace();
				return 0;
			}
		
	}

	@Override
	public void deleteSolrIndex(String id) {
		// TODO 自动生成的方法存根
		SearchClient searchClient = new SearchClient();
		HttpSearchClient client = searchClient.createHttpSearchClient(solrUrl);
		
		try {
			SolrClient createSolrClient = client.createSolrClient();
			createSolrClient.deleteById(id);
			createSolrClient.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
