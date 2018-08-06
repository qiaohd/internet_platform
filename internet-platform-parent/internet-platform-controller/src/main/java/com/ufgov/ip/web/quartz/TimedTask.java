package com.ufgov.ip.web.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import com.ufgov.ip.api.system.QuartzJobBeanForSolrI;

public class TimedTask {  
  
	@Autowired
	private QuartzJobBeanForSolrI quartzJobBeanForSolrI;
    /**  
     * 业务逻辑处理  
     */  
    public void execute() {  
    	quartzJobBeanForSolrI.executeInternal();
        System.out.println("定时任务.......");  
    }  
}  