package com.ufgov.ip.utils;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class UploadFileURLUtils {

	public static String xmlRead(File f,String rootStr,String filepath){
		  try{
				SAXReader reader=new SAXReader();
				Document doc=reader.read(f);
				Element root=doc.getRootElement();
				Element foo;
			    String path=null;
				for(Iterator i = root.elementIterator(rootStr); i.hasNext();) {
					foo=(Element)i.next();
					path = foo.elementText(filepath);
				}
				return path;			
			  }
			  catch(Exception e){
				  e.printStackTrace();
				  return null;
			  }		
	   }
	
	
	public static String getUrl(String rootStr,String filepath){
		 String path2 = UploadFileURLUtils.class.getClassLoader().getResource("uploadFile.xml").getPath();
	     File f= new File(path2);
	     String basepath = xmlRead(f, rootStr, filepath);
		 return basepath;
	}
	
	
	
	
	
	
	
	
	
}
