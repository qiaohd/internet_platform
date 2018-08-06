package com.ufgov.ip.service.system;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.system.NoticeServiceI;
import com.ufgov.ip.api.system.SolrServiceI;
import com.ufgov.ip.dao.system.INoticeMapper;
import com.ufgov.ip.entity.base.SolrEntity;
import com.ufgov.ip.entity.system.IpNotice;
import com.ufgov.ip.serviceutils.HtmlRegexpUtil;
import com.ufgov.ip.serviceutils.NGramUtil;
import com.yonyou.iuap.search.query.pojo.SearchResult;
@Service(version = "0.0.1")
public class NoticeService implements NoticeServiceI {
	 
	@Value("${jdbc.type}")
	 private  String dbType;
	 @Autowired
	 private INoticeMapper noticeMapper;
	 
	 @Autowired
	   private SolrServiceI solrServiceI;
	
	
	
	@Override
	public void saveNotice(IpNotice ipNotice) {
		if(ipNotice.getNoticeId()==null || "".equals(ipNotice.getNoticeId())){
			ipNotice.preInsert();
			noticeMapper.saveNotice(ipNotice);
		}else{
			ipNotice.preUpdate();
			noticeMapper.updateNotice(ipNotice);
		}
		
		
	}

	@Override
	public void updateNotice(IpNotice ipNotice) {
		ipNotice.preUpdate();
		noticeMapper.updateNotice(ipNotice);
	}

	@Override
	public List<IpNotice> getAllNotice(IpNotice ipNotice) {
		return noticeMapper.getAllNotice(ipNotice,dbType);
		
		
		
	}

	@Override
	public IpNotice getNoticeDetail(String noticeId) {
		return noticeMapper.getNoticeDetail(noticeId);
	}

	@Override
	public void deleteNotice(String noticeId) {

		noticeMapper.deleteNotice(noticeId);
		solrServiceI.deleteSolrIndex(noticeId);
	}

	
	@Override
	//public List<SolrEntity> getSolrNoticeList(SolrDocumentList solrDocumentList,SearchResult searchResult) {
		public List<SolrEntity> getSolrNoticeList(String catalog, IpNotice ipNotice,boolean isPage) {
		SearchResult searchResult = new SearchResult();
		Map<String, Object> hashMap = solrServiceI.getSolrResultInfo(catalog, ipNotice, searchResult,isPage);
		searchResult = (SearchResult)hashMap.get("searchResult");
		Long totalElements = (Long) hashMap.get("totalElements");
		Long allCount=0L;
		if(!isPage){
			allCount=totalElements;
		}
		
		SolrDocumentList results = searchResult.getResults();
		List<SolrEntity> indexList=new ArrayList<SolrEntity>();//存放
		for (SolrDocument solrDocument : results) {
			StringBuffer contentLink=new StringBuffer();
			String solrIsPublish = (String) solrDocument.get("isPublish");
			if(!ipNotice.getIsPublish().equals(solrIsPublish)){
				continue;
			}
			SolrEntity solrEntity = new SolrEntity();
			Map<String, Map<String, List<String>>> highlightMap = searchResult.getHighlightMap();
	        List<String> list = highlightMap.get(solrDocument.get("id")).get("name");//包含了多个关键字片段，并且用字段标记
	        List<String> contentlist = highlightMap.get(solrDocument.get("id")).get("content");//包含了多个关键字片段，并且用字段标记
	        List<String> allKey= ((List<String>) solrDocument.get("solrKeywords"));
	        //String name="";
	        String name=((String) solrDocument.get("name")).split(":")[1];
	        if(!"".equals(ipNotice.getKeywords())){//防止全红标记
	        	if( null != list || contentlist!=null){
	        		if(list!=null){
	        			String[] kv= list.get(0).split(":");
        				name=kv[1];
	        		}
	        		if(contentlist!=null){
	        			for (String key : contentlist) {
	        				String[] kv= key.split(":");
	        				String[] split2 = kv[0].split("/");
	        				if(split2[1].equals("noticeContent")){
			        			contentLink.append(kv[1]+" ");
			        		}
						}
	        		}
		        	/*for (String key : list) {
			        	String[] kv= key.split(":");
			        	if(kv[0].equals("name")){
			        		name=kv[1];
			        	}else{
			        		String[] split2 = kv[0].split("/");
			        		if(split2[1].equals("noticeContent")){
			        			contentLink.append(kv[1]+" ");
			        		}
			        	} 
					}*/
		        	
		        	if(contentLink.toString().equals("")){
		        		getSolrResultInfo(solrDocument, contentLink);
		        	}
		        	String solrUpdatetime = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)solrDocument.get("updateDate"))).toString();
		        	String[] timeArr = solrUpdatetime.split("\\s+");
		        	solrUpdatetime=timeArr[0];
		        	solrEntity.setName(name);//标题
			    	solrEntity.setContent(contentLink.toString());//内容
			    	solrEntity.setSearchUrl("notice/getNoticeDetail");//接口地址
			    	//solrEntity.setRouterUrl("#/ip/workspace/noticeNew/noticeNew");//路由地址
			    	solrEntity.setParams("noticeId="+solrDocument.get("id"));//参数
			    	solrEntity.setRouterUrl("#/ip/workspace/noticeDetail/noticeDetail?"+solrEntity.getParams());//路由地址
			    	solrEntity.setUpDateDate(solrUpdatetime);//更新日期
			    	solrEntity.setLinkMan(ipNotice.getLinkman());//创建人
			    	solrEntity.setPk((String)solrDocument.get("id"));//主键
			    	solrEntity.setAllCount(String.valueOf(allCount));
			    	indexList.add(solrEntity);
		        }else{//补充搜索
		        	//处理高亮信息为空的，但是结果集有数据
	        		String high_name = HtmlRegexpUtil.filterHtml((String)solrDocument.get("name"));
	        		List<String> high_content = (List<String>)solrDocument.get("content");
	        		Map<String,String> nameMap=new HashMap<String,String>();
	        		Map<String,String> contentMap=new HashMap<String,String>();
	        		String[] split_name = high_name.split(":");
	        		//获得smartcn的分词结果
	        		List<String> wordList=NGramUtil.testtokenizer(ipNotice.getKeywords());
	        		for (String word : wordList) {
	        			if(word.length()==1 && !word.contains(" ")){
	        				word=word.toLowerCase();
	        				String split_content_info="";
	        				if(name.contains(word)){
	        					nameMap.put(word, "<font color='red'>"+word+"</font>");
	        				}
	        				contentLink.setLength(0);
	        				for (String content : high_content) {
	        					content=HtmlRegexpUtil.filterHtml(content);//过滤所有的html标签
	        					String[] split_content = content.split(":");
	        					String[] split_flag=split_content[0].split("/");
	        					for(int i=1;i<split_content.length;i++){
	        						split_content_info+=split_content[i];
	        					}
	        					if(split_flag[1].equals("noticeContent")){
	        						if(split_content_info.contains(word)){
				        				contentMap.put(word, "<font color='red'>"+word+"</font>");
		        					}
	        						contentLink.append(split_content_info+" ");
	        					}
	        				}
	        			}
					}
	        		
	        		if(contentLink.toString().equals("") && (name.contains("<font color='red'>")||name.contains("</font>"))){
	        			getSolrResultInfo(solrDocument, contentLink);
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
	        			String solrUpdatetime = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)solrDocument.get("updateDate"))).toString();
			        	String[] timeArr = solrUpdatetime.split("\\s+");
			        	solrUpdatetime=timeArr[0];
			        	solrEntity.setName(name);//标题
				    	solrEntity.setContent(contentLink.toString());//内容
				    	solrEntity.setSearchUrl("notice/getNoticeDetail");//接口地址
				    	//solrEntity.setRouterUrl("#/ip/workspace/noticeNew/noticeNew");//路由地址
				    	solrEntity.setParams("noticeId="+solrDocument.get("id"));//参数
				    	solrEntity.setRouterUrl("#/ip/workspace/noticeDetail/noticeDetail?"+solrEntity.getParams());//路由地址
				    	solrEntity.setUpDateDate(solrUpdatetime);//更新日期
				    	solrEntity.setLinkMan(ipNotice.getLinkman());//创建人
				    	solrEntity.setPk((String)solrDocument.get("id"));//主键
				    	solrEntity.setAllCount(String.valueOf(allCount));
				    	indexList.add(solrEntity);
	        		}
		        }
	        }else{
	        	
	        	String solrTitle = getSolrResultInfo(solrDocument, contentLink);
	        	String[] solrTitleArr = solrTitle.split(":");
	        	String solrUpdatetime = Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)solrDocument.get("updateDate"))).toString();
	        	String[] timeArr = solrUpdatetime.split("\\s+");
	        	solrUpdatetime=timeArr[0];
	        	solrEntity.setName(solrTitleArr[1]);//标题
		    	solrEntity.setContent(contentLink.toString());//内容
		    	solrEntity.setSearchUrl("notice/getNoticeDetail");//接口地址
		    	//solrEntity.setRouterUrl("#/ip/workspace/noticeNew/noticeNew");//路由地址
		    	solrEntity.setParams("noticeId="+solrDocument.get("id"));//参数
		    	solrEntity.setRouterUrl("#/ip/workspace/noticeDetail/noticeDetail?"+solrEntity.getParams());//路由地址
		    	solrEntity.setUpDateDate(solrUpdatetime);//更新日期
		    	solrEntity.setLinkMan(ipNotice.getLinkman());//创建人
		    	solrEntity.setPk((String)solrDocument.get("id"));//主键
		    	solrEntity.setAllCount(String.valueOf(allCount));
		    	indexList.add(solrEntity);
	        }
		}
		return indexList;
	}

	private String getSolrResultInfo(SolrDocument solrDocument,
			StringBuffer contentLink) {
		String solrTitle = (String) solrDocument.get("name");
		List<String> solrContent= ((List<String>) solrDocument.get("content"));
		for (String contentValue : solrContent) {
			String[] split = contentValue.split(":");
			String[] split2 = split[0].split("/");
			if(split2[1].equals("noticeContent")){
				contentLink.append(split[1]+" ");
			}
		}
		return solrTitle;
	}

	@Override
	public List<IpNotice> findBykeywords(IpNotice ipNotice) {
		
		return noticeMapper.findBykeywords(ipNotice, dbType);
		
		
	}

	@Override
	public Integer findRecordNum(IpNotice ipNotice) {
		
		return noticeMapper.findRecordNum(ipNotice,dbType);
	}
	
	@Override
	public List<IpNotice> getAllNoticeWithSP(IpNotice ipNotice){
		
		return noticeMapper.getAllNoticeWithSP(ipNotice, dbType);
	}

	
	
	@Override
	public <T> Integer getAllSolrNoticeResult(String index_catalog,
			T copyClass1) {
		SearchResult searchResult = new SearchResult();
		Map<String, Object> allSolrNoticeResult = solrServiceI.getAllSolrNoticeResult(index_catalog, copyClass1, searchResult);
		searchResult = (SearchResult)allSolrNoticeResult.get("searchResult");
		Long totalElements = (Long) allSolrNoticeResult.get("totalElements");
		SolrDocumentList results = searchResult.getResults();
		if(results==null || results.size()==0){
			return 0;
		}else{
			return Integer.valueOf(String.valueOf(totalElements));
		}
		
	}

}
