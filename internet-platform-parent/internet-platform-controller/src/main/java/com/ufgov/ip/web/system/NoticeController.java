package com.ufgov.ip.web.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.NoticeServiceI;
import com.ufgov.ip.api.system.QuartzJobBeanForSolrI;
import com.ufgov.ip.api.system.SolrServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.entity.base.SolrEntity;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpNotice;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.Page;
import com.ufgov.ip.utils.PathUtil;
import com.ufgov.ip.utils.PropertyUtilSys;
import com.ufgov.ip.utils.UploadFileURLUtils;

@Component("org.NoticeController")
@Scope("prototype")
@RequestMapping(value = "/notice")

/**
 * 平台公告控制层
 * @author zhangbch
 *
 */
public class NoticeController {

	public static final int HASH_INTERATIONS = 1024;
		// 默认一天
	public static final int COOKIES_MAXAGE = 60 * 60 * 24;
	private static final int SALT_SIZE = 8;
	
	@Autowired
	protected HirerAccountServiceI hirerAccountService;
	
	@Autowired
	protected UserAccountServiceI userAccountService;
	
	@Autowired
	protected NoticeServiceI noticeServiceI;
	
	@Autowired
	   private SolrServiceI solrServiceI;
	@Autowired
	private QuartzJobBeanForSolrI quartzJobBeanForSolrI;
	
	/**
	 * 保存公告
	 * 接口：notice/saveNotice
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value="saveNotice")
	public @ResponseBody Map<String,Object> saveNotice(@RequestParam String oldFile,HttpServletRequest request, HttpServletResponse response, Model model){
		IpNotice ipNotice = new IpNotice();
		Map<String,Object> reg=new HashMap<String,Object>();
		try {
			//获得公告属性数据
			BeanUtils.populate(ipNotice, request.getParameterMap());
			//完成附件上传
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile pictureFile = multipartRequest.getFile("attachmentUrl");
			if(pictureFile!=null){
				String originalFilename = pictureFile.getOriginalFilename();//取到上传附件名称
				response.addHeader("Content-Disposition","attachment;   filename="+java.net.URLEncoder.encode(originalFilename, "UTF-8"));
				//一个租户一个文件夹，文件名和上传文件名保持一致
				uploadAttachment(request, pictureFile, originalFilename,ipNotice);
			}else{
				if(oldFile==null || "".equals(oldFile)){
					ipNotice.setAttachmentUrl("");
				}else{
					IpNotice noticeDetail = noticeServiceI.getNoticeDetail(ipNotice.getNoticeId());
					ipNotice.setAttachmentUrl(noticeDetail.getAttachmentUrl());
				}
				IpHirer ipHirerByUser = getCurHirerId();
				ipNotice.setHirerId(ipHirerByUser.getHirerId());
				
			}
			
			//保存公告
			noticeServiceI.saveNotice(ipNotice);
			synSolrCore();
			reg.put("result", "true");
			return reg;
		} catch (Exception e) {
			e.printStackTrace();
			reg.put("result", "false");
			return reg;
		}
	}


	
	/**
	 * 保存公告(兼容IE8)
	 * 接口：notice/saveNoticeForIE
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value="saveNoticeForIE")
	public String saveNoticeForIE(@RequestParam String oldFile,HttpServletRequest request, HttpServletResponse response, Model model){
		IpNotice ipNotice = new IpNotice();
		Map<String,Object> reg=new HashMap<String,Object>();
		try {
			
			//response.setContentType("text/html");
			//获得公告属性数据
			BeanUtils.populate(ipNotice, request.getParameterMap());
			//CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
			//if(multipartResolver.isMultipart(request)){
				//完成附件上传
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile pictureFile = multipartRequest.getFile("attachmentUrl");
				String originalFilename = pictureFile.getOriginalFilename();//取到上传附件名称
				if(!"".equals(originalFilename)){
					response.addHeader("Content-Disposition","attachment;   filename="+java.net.URLEncoder.encode(originalFilename, "UTF-8"));
					//一个租户一个文件夹，文件名和上传文件名保持一致
					uploadAttachment(request, pictureFile, originalFilename,ipNotice);
				}else{
					if(oldFile==null || "".equals(oldFile)){
						ipNotice.setAttachmentUrl("");
					}else{
						IpNotice noticeDetail = noticeServiceI.getNoticeDetail(ipNotice.getNoticeId());
						ipNotice.setAttachmentUrl(noticeDetail.getAttachmentUrl());
					}
					IpHirer ipHirerByUser = getCurHirerId();
					ipNotice.setHirerId(ipHirerByUser.getHirerId());
				}
				
			//}else{
				
			//}
			
			//保存公告
			noticeServiceI.saveNotice(ipNotice);
			synSolrCore();
			reg.put("result", "true");
			//response.getWriter().write("true");
			return "redirect";
		} catch (Exception e) {
			e.printStackTrace();
			reg.put("result", "false");
			return "redirect";
			
		}
	}
	
	
	
	
	
	
	


	private void synSolrCore() {
		String getbpmEnabledByKey = PropertyUtilSys.getSolrEnabledByKey("solrEnabled");
		if("true".equals(getbpmEnabledByKey)){
			quartzJobBeanForSolrI.executeInternal();
		}
	}
	
	
	
	
	/**
	 * 获得当前租户的所有公告
	 * 接口：notice/getAllNotice
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="getAllNotice")
	public @ResponseBody Map<String,Object> getAllNotice(HttpServletRequest request, HttpServletResponse response, Model model){
		Map<String,Object> reg=new HashMap<String,Object>();
		IpNotice ipNotice = new IpNotice();
		IpHirer ipHirerByUser = getCurHirerId();
		ipNotice.setHirerId(ipHirerByUser.getHirerId());
		List<IpNotice> allNotice = noticeServiceI.getAllNotice(ipNotice);
		
		int totalCount = allNotice.size();//总记录数
		if(totalCount==0){
			reg.put("allNotice", allNotice);
			reg.put("isPublish", 0);
			reg.put("isSave", 0);
			return reg;
		}else{
			for (IpNotice notice : allNotice) {
				notice.setLinkman(ipHirerByUser.getLinkman());
			}
			reg.put("allNotice", allNotice);
			reg.put("isPublish", allNotice.size());
			reg.put("isSave", allNotice.get(0).getCount()-allNotice.size());
			return reg;
		}
	}
	
	
	
	/**
	 * 获得公告详情
	 * 接口：notice/getNoticeDetail
	 * 参数：noticeId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="getNoticeDetail")
	public @ResponseBody Map<String,Object> getNoticeDetail(@RequestParam String noticeId,HttpServletRequest request, HttpServletResponse response, Model model){
		Map<String,Object> reg=new HashMap<String,Object>();
		IpHirer curHirerId = getCurHirerId();
		IpNotice noticeDetail = noticeServiceI.getNoticeDetail(noticeId);
		getAttachmentName(noticeDetail);
		if(curHirerId==null){
			noticeDetail.setLinkman("管理员");
		}else{
			noticeDetail.setLinkman(curHirerId.getLinkman());
		}
		
		if(noticeDetail.getAttachmentUrl()==null){
			noticeDetail.setAttachmentUrl("");
		}
		reg.put("noticeDetail", noticeDetail);
		return reg;
	}
	

	
	/**
	 * 删除当前公告
	 * 接口：notice/deleteNotice
	 * @param noticeId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="deleteNotice")
	public @ResponseBody Map<String,Object> deleteNotice(@RequestParam String noticeId,HttpServletRequest request, HttpServletResponse response, Model model){
		Map<String,Object> reg=new HashMap<String,Object>();
		try {
			noticeServiceI.deleteNotice(noticeId);
			//synSolrCore();
			reg.put("result", "true");
			return reg;
		} catch (Exception e) {
			e.printStackTrace();
			reg.put("result", "false");
			return reg;
		}
		
	}


	private void getAttachmentName(IpNotice noticeDetail) {
		String attachmentUrl = noticeDetail.getAttachmentUrl();
		if(attachmentUrl!=null && !"".equals(attachmentUrl)){
			int lastIndexOf = attachmentUrl.lastIndexOf("/");
			attachmentUrl=attachmentUrl.substring(lastIndexOf+1);
			noticeDetail.setAttachmentUrl(attachmentUrl);
		}
		
	}
	
	
	
	
	/**
	 * 下载当前公告附件
	 * 接口：notice/downLoadAttachment
	 * 参数：noticeId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="downLoadAttachment")
	public @ResponseBody Map<String,Object> downLoadAttachment(@RequestParam String noticeId,HttpServletRequest request, HttpServletResponse response, Model model) throws FileNotFoundException{
		
		Map<String,Object> reg=new HashMap<String,Object>();
		IpNotice noticeDetail = noticeServiceI.getNoticeDetail(noticeId);
		String attachmentUrl = noticeDetail.getAttachmentUrl();
		String path = request.getServletContext().getRealPath("/");
		String fullFileName =  path+"/"+attachmentUrl;
		getAttachmentName(noticeDetail);
		
		try {
				response.setContentType(request.getServletContext().getMimeType(noticeDetail.getAttachmentUrl()));  
		        response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(noticeDetail.getAttachmentUrl(), "UTF-8"));
						//+ new String(noticeDetail.getAttachmentUrl().getBytes("utf-8"), "ISO-8859-1")); 
				
				InputStream in;
		        OutputStream out;
			
				in = new FileInputStream(fullFileName);
				out = response.getOutputStream();
		        int b;  
		        while((b=in.read())!= -1)  
		        {  
		            out.write(b);  
		        }  
		          
		        //in.close();  
		        out.close();
		        reg.put("result", "true");
			} catch (Exception e) {
				e.printStackTrace();
				reg.put("result", "false");
				
			}
		return reg;
	 }
	
	
    /**
     * 当前公告附件上传
     * @param request
     * @param pictureFile
     * @param originalFilename
     * @param ipNotice
     * @throws IOException
     */
	private void uploadAttachment(HttpServletRequest request,
			MultipartFile pictureFile, String originalFilename,IpNotice ipNotice)
			throws IOException {
		String noticeBaseUrl = UploadFileURLUtils.getUrl("noticeAttachment",
				"attachmentFilePath");
		IpHirer ipHirerByUser = getCurHirerId();
		
		ipNotice.setHirerId(ipHirerByUser.getHirerId());
		String path = request.getServletContext().getRealPath("/");
		File file = new File(path + File.separator + noticeBaseUrl+File.separator+ipHirerByUser.getHirerId());
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] listFiles = file.listFiles();
		if (listFiles != null) {
			for (File file2 : listFiles) {
				if (file2.isFile()
						&& file2.getName().startsWith(
								originalFilename)) {
					file2.delete();
					break;
				}
			}
		}
		String url = path + File.separator + noticeBaseUrl+File.separator+ipHirerByUser.getHirerId()+File.separator+originalFilename;
		url=url.replace("\\", "/");
		ipNotice.setAttachmentUrl(PathUtil.getURL(url));
		pictureFile.transferTo(new File(url));
	}
	
	
	
	/**
	 * solr查询关键字
	 * 接口：notice/getNoticeSolrResult
	 * 参数：keywords  
	 *      isPublish
	 * @param index_catalog
	 * @param keywords
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value = "getNoticeSolrResult")
	public @ResponseBody Map<String,Object> getNoticeSolrResult(IpNotice ipNotice,
			                                                     HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,Object> reg=new HashMap<String,Object>();
		IpHirer curHirerId = getCurHirerId();
		ipNotice.setHirerId(curHirerId.getHirerId());
		String linkman = curHirerId.getLinkman();
		//判断solr服务是否开启，反之则查询数据库
		 String getbpmEnabledByKey = PropertyUtilSys.getSolrEnabledByKey("solrEnabled");
		// List<IpNotice> ipNoticeList=noticeServiceI.findBykeywords(ipNotice);
		 String keyWords = ipNotice.getKeywords();
		 if (StringUtils.isNotBlank(keyWords)) {
			 try {
				keyWords = URLDecoder.decode(keyWords, "UTF-8");
				ipNotice.setKeywords(keyWords);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		 }
		 
		int totalCount=noticeServiceI.findRecordNum(ipNotice);
		 if("false".equals(getbpmEnabledByKey)){
			 Page page = getPageInfo(ipNotice,totalCount);
			//计算startNum和endNum
			 Integer startNum = page.getStartNum();
			 Integer endNum = page.getEndNum();
			//设置到ipNotice中
			 ipNotice.setStartNum(startNum);
			 ipNotice.setEndNum(endNum);
			//查询当前条件下的结果集
			 List<IpNotice> pageIpNoticeList=noticeServiceI.findBykeywords(ipNotice);
			 if(pageIpNoticeList.size()!=0){
				 for (IpNotice ipNotice2 : pageIpNoticeList) {
					 ipNotice2.setLinkman(linkman);
				}
			 }
			 
			 List<IpNotice> allNoticeWithSP = noticeServiceI.getAllNoticeWithSP(ipNotice);
			 //返回发布数量/未发布数量
			 int isSave=0;
			 int isPub=0;
			 if(ipNotice.getIsPublish().equals("1")){//初始发布
				  isSave=allNoticeWithSP.size()-totalCount;//未发布
				  isPub=totalCount;//发布
			 }else{//初始未发布
				  isSave=totalCount;//未发布
				  isPub=allNoticeWithSP.size()-totalCount;//发布
			 }
			 
			 return getReturn(reg, totalCount, page, pageIpNoticeList, isSave, isPub);
		 }else{
		  //查询索引库，并高亮显示
			 ipNotice.setHirerId(curHirerId.getHirerId());
			 ipNotice.setLinkman(linkman);
			 
			 List<SolrEntity> solrNoticeListWithPage = noticeServiceI.getSolrNoticeList("公告",ipNotice,true);
			 List<SolrEntity> solrNoticeListWithOutPage = noticeServiceI.getSolrNoticeList("公告",ipNotice,false);
			 Integer allCount = 0;
			 if(solrNoticeListWithOutPage.size()!=0){
				 allCount = Integer.valueOf(solrNoticeListWithOutPage.get(0).getAllCount());
			 }
			 
			 Page page = getPageInfo(ipNotice,allCount);
			 
			 Integer sizeOfAll = noticeServiceI.getAllSolrNoticeResult("公告", ipNotice);//当前类别及租户下的所有记录
			 //int sizeWithOutPage = solrNoticeListWithOutPage.size();//当前分类下满足过滤条件的所有记录(所有分页)
			 int isSave=0;
			 int isPub=0;
			 if(ipNotice.getIsPublish().equals("1")){//初始发布
				  isSave=sizeOfAll-allCount;//未发布
				  isPub=allCount;//发布
			 }else{//初始未发布
				  isSave=allCount;//未发布
				  isPub=sizeOfAll-allCount;//发布
			 }
			 return getReturnInfo(reg, solrNoticeListWithPage, page,
					 allCount, isSave, isPub);
				
		 }
		
	}




	private Map<String, Object> getReturn(Map<String, Object> reg, int totalCount, Page page,
			List<IpNotice> pageIpNoticeList, int isSave, int isPub) {
		reg.put("currentPage", page.getPageNo());
		 reg.put("pageSize", page.getPageSize());
		 reg.put("totalPages", page.getTotalPage());
		 reg.put("totalCount", totalCount);
		 reg.put("resultList", pageIpNoticeList);
		 reg.put("isSolr", "false");
		 reg.put("isSave", String.valueOf(isSave));
		 reg.put("isPub", String.valueOf(isPub));
		 return reg;
	}




	private Map<String, Object> getReturnInfo(Map<String, Object> reg,
			List<SolrEntity> solrNoticeListWithPage, Page page,
			int sizeWithOutPage, int isSave, int isPub) {
		
		if(solrNoticeListWithPage.size()<10){
			 page.setTotalCount(solrNoticeListWithPage.size());
			 reg.put("totalCount", solrNoticeListWithPage.size());
		}else{
			 page.setTotalCount(sizeWithOutPage);
			 reg.put("totalCount", sizeWithOutPage);
		 }
		
		
		
		reg.put("currentPage", page.getPageNo());
		reg.put("pageSize", page.getPageSize());
		reg.put("totalPages", page.getTotalPage());
		//reg.put("totalCount", sizeWithOutPage);
		reg.put("resultList", solrNoticeListWithPage);
		reg.put("isSolr", "true");
		reg.put("isSave", String.valueOf(isSave));
		reg.put("isPub", String.valueOf(isPub));
		return reg;
	}




	private Page getPageInfo(IpNotice ipNotice,int totalCount) {
		Page page = new Page();
		 page.setPageNo(Integer.valueOf(ipNotice.getPageNo()));
		 page.setTotalCount(totalCount);
		return page;
	}




	private IpHirer getCurHirerId() {
		String cuser = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
			IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser); //普通用户
			IpHirer ipHirerByUser=null;
			 if(ipUser!=null){
				 ipHirerByUser = hirerAccountService.findHirerByHirerId(ipUser.getHirerId());
			}
		return ipHirerByUser;
	}
	
	
}
