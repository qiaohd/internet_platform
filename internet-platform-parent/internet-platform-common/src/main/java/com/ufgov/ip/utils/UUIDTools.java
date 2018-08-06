package com.ufgov.ip.utils;

import java.util.UUID;

/**
 * filename:UUIDTools.java
 *
 * Version:1.0
 *
 * Date:2016-03-28
 *
 * Copyright  2016 by yonyou.ufgov
 */
public class UUIDTools {
	/**
	   * 取得一个UUID的方法
	   * @return 一个UUID
	   */
	  public static String uuidRandom()
	  {
			String id = UUID.randomUUID().toString();
		    return id;   
	  }
	  
	  /**
	   * <p>Description:通过Random数字生成, 中间无-分割 </p>
	   * @return 
	   */
	  public static String uuid() {
			return UUID.randomUUID().toString().replaceAll("-", "");
		}
	  
	  public static void main(String[] args) {
		String aaString=uuidRandom();
		System.out.println(aaString);
	}
}
