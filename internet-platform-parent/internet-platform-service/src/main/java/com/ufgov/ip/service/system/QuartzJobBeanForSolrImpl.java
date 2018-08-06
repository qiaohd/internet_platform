package com.ufgov.ip.service.system;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.system.QuartzJobBeanForSolrI;

@Service(version = "0.0.1")
public class QuartzJobBeanForSolrImpl implements QuartzJobBeanForSolrI {

	 @Value("${solr.synchronized.url}")
	    private String solrSynchronizedUrl;
	
	public QuartzJobBeanForSolrImpl(){
		System.out.println("执行构造方法！");
	}
	
	@Override
	//任务调度需要执行的类
	public void executeInternal(){
        //构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        //创建GET方法的实例 　　
        GetMethod getMethod = new GetMethod(solrSynchronizedUrl);
        //使用系统提供的默认的恢复策略 　　
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		
        try {
            //执行getMethod 　　
            int statusCode = httpClient.executeMethod(getMethod);
            System.out.println("HttpStatus.SC_OK = "+HttpStatus.SC_OK);
            if (statusCode != HttpStatus.SC_OK) {
                    System.err.println("Method failed: "+ getMethod.getStatusLine());
            }
            //读取内容 　　
            byte[] responseBody = getMethod.getResponseBody();
            //处理内容 　　
            System.out.println("返回内容 = "+new String(responseBody));
        } catch (Exception e) {
        //发生致命的异常，可能是协议不对或者返回的内容有问题 　　
        System.out.println("Please check your provided http address!");
        e.printStackTrace();
           
        }
		
		
	}

}
