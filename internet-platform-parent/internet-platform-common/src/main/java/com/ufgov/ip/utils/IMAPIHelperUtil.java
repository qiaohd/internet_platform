package com.ufgov.ip.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import net.sf.json.JSONObject;

public class IMAPIHelperUtil {
	 private final Logger logger = LoggerFactory.getLogger(getClass());
	
	 private HashMap<String, String> getParam() {

			HashMap<String, String> map = new HashMap<String, String>();
			try {

				Resource resource = new ClassPathResource("application.properties");
				Properties props = PropertiesLoaderUtils.loadProperties(resource);
			
				String eptId = (String) props.get("im.eptid");
				String appId = (String) props.get("im.appid");
				String clientID = (String) props.get("im.clientid");
				String clientSecret = (String) props.get("im.clientsecret");
				String imClient = "http://"+(String) props.get("im.client")+"/sysadmin/rest/";
				
				map.put("eptId", eptId);
				map.put("appId", appId);
				map.put("clientID", clientID);
				map.put("clientSecret", clientSecret);
				map.put("imClient", imClient);

				// System.out.println(user +"====="+ password);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			return map;
		}
		Map<String, String> paramMap = getParam();
		// 企业ID
		private final String eptId = paramMap.get("eptId");
		// 应用ID
		private final String appId = paramMap.get("appId");
		// Client ID
		private final String clientID = paramMap.get("clientID");
		// Client Secret
		private final String clientSecret = paramMap.get("clientSecret");
		// im Client
		private final String imClient = paramMap.get("imClient");

	 private volatile static IMAPIHelperUtil imAPIHelperUtil;  
	 private IMAPIHelperUtil (){}  
	 public static IMAPIHelperUtil getSingletonIMAPIHelperUtil() {  
	    if (imAPIHelperUtil == null) {  
	        synchronized (IMAPIHelperUtil.class) {  
	        if (imAPIHelperUtil == null) {  
	        	imAPIHelperUtil = new IMAPIHelperUtil();  
	        }  
	        }  
	    }  
	    return imAPIHelperUtil;  
	    }  
	/**
	 * 发送ajax 请求，传输json数据
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * return 
	 */
	public String postJson(String url,String method, String[] arr, String... params) {
		String result = "";
		try {
			JSONObject obj = new JSONObject();
			if (params.length > 0) {
				for (String param : params) {
					String key = param.split("=")[0];
					String value=" ";
					if(param.lastIndexOf('=')+1 == param.length())
					{
						continue;
					}
					value=param.split("=")[1];
					obj.element(key, value);
				}
			}
			 if(!(arr==null||arr.length==0))
			 {
				 obj.element("operand",arr);
			 }
			System.out.println(obj);
			// 创建url资源
			URL urlHttp = new URL(url);
			// 建立http连接
			HttpURLConnection conn = (HttpURLConnection) urlHttp
					.openConnection();
			// 设置允许输出
			conn.setDoOutput(true);

			conn.setDoInput(true);

			// 设置不用缓存
			conn.setUseCaches(false);
			// 设置传递方式
			conn.setRequestMethod(method);
			// 设置维持长连接
			conn.setRequestProperty("Connection", "Keep-Alive");
			// 设置文件字符集:
			conn.setRequestProperty("Charset", "UTF-8");
			// 转换为字节数组
			byte[] data = (obj.toString()).getBytes();
			// 设置文件长度
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.length));

			// 设置文件类型:
			conn.setRequestProperty("Content-Type", "application/json");

			// 开始连接请求
			conn.connect();
			OutputStream out = conn.getOutputStream();	
			// 写入请求的字符串
			out.write((obj.toString()).getBytes());
			out.flush();
			out.close();

			System.out.println(conn.getResponseCode());

			// 请求返回的状态
			if (conn.getResponseCode() == 200) {
				System.out.println("连接成功");
				// 请求返回的数据
				InputStream in = conn.getInputStream();
				String a = null;
				try {
					byte[] data1 = new byte[in.available()];
					in.read(data1);
					// 转成字符串
					a = new String(data1);
					result = a;
					System.out.println(a);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				System.out.println("n");
			}
		} catch (Exception e) {
			logger.error("请求失败！", e);
			System.out.println(e.getMessage());
		}
		return result;
	}
	 /* 获取AppToken
	 * @param eptId
	 * @param appId
	 * @param clientID
	 * @param clientSecret
	 * @return
	 */
	public JSONObject getAPPToken(String eptId,String appId,String clientID,String clientSecret)
	{
		JSONObject json=null;
		String url = imClient+ eptId + "/" + appId
				+ "/token";
		String[] params = new String[] { "clientId=" + clientID,
				"clientSecret=" + clientSecret};
		String resStr = postJson(url, "POST",null,params);
		json = JSONObject.fromObject(resStr);
		return json;
	}
	/**
	 * 如果用户名带@ 去掉@及后面的内容 
	 * 注意：此处可能会出现 去掉@后用户一样的情况
	 * @param list
	 * @return
	 */
	public List<String> handleOldUserLoginName(List<String> list)
	{
		if(list.size()>0)
		{
			for(int i = 0; i < list.size(); i++)  
	        {  
	           String str=list.get(i); 
	           int index=str.indexOf('@');
	           if(index>0){
	        	   list.set(i, str.substring(0, index));
	           }
	        }  
		}
		return list;
	}
}
