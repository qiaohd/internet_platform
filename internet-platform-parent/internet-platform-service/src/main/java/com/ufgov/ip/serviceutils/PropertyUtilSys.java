package com.ufgov.ip.serviceutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

public class PropertyUtilSys {
	
	/************消息配置信息**********************/
    private static Properties prop = null;

    static {
        prop = new Properties();
        InputStream in = null;
        try {
            in =new ClassPathResource("ipconfig/message.properties").getInputStream();
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    public static String getMessagePropertyByKey(String key) {
        String value = prop.getProperty(key);
        return StringUtils.isBlank(value) ? "" : value;
    }
    /***************end************************/
    
    
    /************运维管理配置信息**********************/
    private static Properties operProp = null;

    static {
    	operProp = new Properties();
        InputStream in = null;
        try {
            in =new ClassPathResource("operation.properties").getInputStream();
            operProp.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    public static String getOperPropertyByKey(String key) {
        String value = operProp.getProperty(key);
        return StringUtils.isBlank(value) ? "" : value;
    }
    /***************end************************/
    
    /************工作流开关**********************/
    
    private static Properties bpmEnabled = null;

    static {
    	bpmEnabled = new Properties();
        InputStream in = null;
        try {
            in =new ClassPathResource("application.properties").getInputStream();
            bpmEnabled.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    public static String getbpmEnabledByKey(String key) {
        String value = bpmEnabled.getProperty(key);
        return StringUtils.isBlank(value) ? "" : value;
    }
    
    /***************end************************/

}
