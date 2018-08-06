package com.ufgov.ip.listener;

import java.security.KeyPair;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;



import com.ufgov.ip.utils.RSAUtils;

import uap.web.core.ContextHolder;
import uap.web.httpsession.cache.SessionCacheManager;


public class CustomServletContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent arg0) {
    }

    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        ContextHolder.setContext(wac);


        try {            
            // 初始化keypair
            SessionCacheManager cm = (SessionCacheManager)wac.getBean("cacheManager");
            String keypairKey = "EXAMPLE_KEY_PAIR";
            if(cm.exists(keypairKey)){
            	KeyPair kp = (KeyPair)cm.get(keypairKey);
            	RSAUtils.setKeyPair(kp);
            	System.out.println("get keypair from redis ...");
            } else {
            	KeyPair gk = RSAUtils.generateKeyPair();
            	cm.set(keypairKey, gk);
            	System.out.println("generate keypair and put it into redis ...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}