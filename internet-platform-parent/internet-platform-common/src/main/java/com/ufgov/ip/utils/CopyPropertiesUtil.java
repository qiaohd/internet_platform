package com.ufgov.ip.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
/**
 * 对实体属性操作的工具
 * @author zhangbch
 *
 */
public class CopyPropertiesUtil {

	public static <T> void copyProperty(T copyClass1, T copyClass2) {

		@SuppressWarnings("unchecked")
		Class<? extends T> class2 = (Class<? extends T>) copyClass2.getClass();

		Method[] methods = class2.getDeclaredMethods();
		List<String> exceptList = new ArrayList<String>();
		for (Method method : methods) {
			if (method.getName().startsWith("get")) {
				try {

					Field[] declaredFields = class2.getDeclaredFields();

					for (Field field : declaredFields) {
						String propertyName = field.getName();
						String name = method.getName().substring(3);
						String firstWord = name.substring(0, 1).toLowerCase();
						String restWords = name.substring(1);
						name = firstWord + restWords;
						if (method.invoke(copyClass2, new Object[] {}) == null
								&& name.equals(propertyName)) {
							exceptList.add(propertyName);
						}
					}
 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		String[] exceptelements = new String[exceptList.size()];
		BeanUtils.copyProperties(copyClass2, copyClass1,
				exceptList.toArray(exceptelements));
	}
	
	
	public static <T> Object getValue(T fromClass,String fieldName) {

		@SuppressWarnings("unchecked")
		Class<? extends T> class2 = (Class<? extends T>) fromClass.getClass();

		Method[] methods = class2.getDeclaredMethods();
		for (Method method : methods) {
			String substring = method.getName().substring(3);
			String substring2 = substring.substring(0, 1).toLowerCase();
			String substring3 = substring.substring(1);
			
			if (method.getName().startsWith("get") && (substring2+substring3).equals(fieldName)) {
				try {
					
					return method.invoke(fromClass, new Object[] {});
 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	   }
	
	
	public static <T> List<String> getSolrValue(T fromClass) {

		@SuppressWarnings("unchecked")
		Class<? extends T> class2 = (Class<? extends T>) fromClass.getClass();
        List<String> list=new ArrayList<String>();
		try {
			Method declaredMethod1 = class2.getDeclaredMethod("getKeywords");
			String keyWords =(String)declaredMethod1.invoke(fromClass, new Object[] {});
			if(keyWords==null){
				keyWords="";
			}
			Method declaredMethod2 = class2.getDeclaredMethod("getHirerId");
			String hirerId =(String)declaredMethod2.invoke(fromClass, new Object[] {});
			
			
			Method declaredMethod3 = class2.getDeclaredMethod("getPageNo");
			String pageNo =(String)declaredMethod3.invoke(fromClass, new Object[] {});
			
			
			
			Method declaredMethod4 = class2.getDeclaredMethod("getIsPublish");
			String isPublish =(String)declaredMethod4.invoke(fromClass, new Object[] {});
			
			
			list.add(keyWords);
			list.add(hirerId);
			list.add(pageNo);
			list.add(isPublish);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	   }
	

	
	//将空的值赋值为""
	public static <T> void setProperty(T copyClass) {
		@SuppressWarnings("unchecked")
		Class<? extends T> classType = (Class<? extends T>) copyClass
				.getClass();
		Method[] declaredMethods = classType.getDeclaredMethods();
        String user="";
		try {
			for (Method method : declaredMethods) {

				String methodName = method.getName();
				if (methodName.startsWith("get")) {

					Object value = method.invoke(copyClass, new Object[] {});
					if (value == null) {
						String name = methodName.substring(3);
						String firstWord = name.substring(0, 1).toLowerCase();
						String restWords = name.substring(1);
						String fieldName = firstWord + restWords;// 属性名称
						user=fieldName;
						Field declaredField = classType
								.getDeclaredField(fieldName);
						Class<?> type = declaredField.getType();
						if(type==Date.class){
							Method setMethod = classType.getDeclaredMethod("set"
									+ name, new Class[] { type });
							Object invoke = setMethod.invoke(copyClass,
									new Object[] { null });
						}else if(type==Long.class){
							
							Method setMethod = classType.getDeclaredMethod("set"
									+ name, new Class[] { type });
							Object invoke = setMethod.invoke(copyClass,
									new Object[] { System.currentTimeMillis() });
							
						}else if(type==String.class){
							
							Method setMethod = classType.getDeclaredMethod("set"
									+ name, new Class[] { type });
							Object invoke = setMethod.invoke(copyClass,
									new Object[] { "" });
						}else{
							;
						}
						
					}
				}
			}

		} catch (Exception ex) {
			
			System.out.println(user);
			
			ex.printStackTrace();
		}
	}
	
	public static <T> void copyPropertyWithoutDate(T copyClass1, T copyClass2) {
		
		@SuppressWarnings("unchecked")
		Class<? extends T> class2 = (Class<? extends T>) copyClass2.getClass();

		Method[] methods = class2.getDeclaredMethods();
		List<String> exceptList = new ArrayList<String>();
		for (Method method : methods) {
			if (method.getName().startsWith("get")) {
				try {

					Field[] declaredFields = class2.getDeclaredFields();

					for (Field field : declaredFields) {
						String propertyName = field.getName();
						String name = method.getName().substring(3);
						String firstWord = name.substring(0, 1).toLowerCase();
						String restWords = name.substring(1);
						name = firstWord + restWords;
						if (name.equals(propertyName) && field.getType()==Date.class) {
							exceptList.add(propertyName);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		String[] exceptelements = new String[exceptList.size()];
		BeanUtils.copyProperties(copyClass2, copyClass1,
				exceptList.toArray(exceptelements));
		
		
		
	}
	
	
	/**
	 * 通过报名获得对象，并赋值属性,name、content
	 * @param packageName
	 */
	public static void getObjectByClassName(String packageName){
		
		
		try {
			Class<?> forName = Class.forName(packageName);
			
			
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
public static <T> void copyJdbcProperty(T copyClass1, Map mapInfo) {
		
		@SuppressWarnings("unchecked")
		Class<? extends T> class2 = (Class<? extends T>) copyClass1.getClass();

		Method[] methods = class2.getDeclaredMethods();
		List<String> exceptList = new ArrayList<String>();
		for (Method method : methods) {
			if (method.getName().startsWith("set")) {
				try {
						String name = method.getName().substring(3);
						String firstWord = name.substring(0, 1).toLowerCase();
						String restWords = name.substring(1);
						name = firstWord + restWords;
						Field declaredField = class2
								.getDeclaredField(name);
						if (declaredField.getType()==Date.class) {
							method.invoke(copyClass1,new Object[]{null});
						}else{
							if("userSex".equals(name)){
								if("1".equals((String)mapInfo.get(name))){//男
									method.invoke(copyClass1, new Object[]{"男"});
								}else{
									method.invoke(copyClass1, new Object[]{"女"});
								}
							}else{
								method.invoke(copyClass1, new Object[]{(String)mapInfo.get(name)});
							}
						}
					}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
