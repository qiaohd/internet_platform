package com.ufgov.ip.utils;

public class PathUtil {

	
	public static String getURL(String path){
		
		  int indexOf = path.indexOf("upload");
		  String substring = path.substring(indexOf);
		  String newPath=substring.replaceAll("\\\\","/");
		  return newPath;
	}
	
	
	
}
