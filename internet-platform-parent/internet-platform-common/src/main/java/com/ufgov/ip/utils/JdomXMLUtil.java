package com.ufgov.ip.utils;

import java.io.FileWriter;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.stereotype.Service;

/**
 * 
 * @author qiaohd
 *
 */
@Service
public class JdomXMLUtil {
	
	 // 将doc对象输出到文件
	 public  void saveXML(Document doc,String xmlPath) {
         try {
             // 创建xml文件输出流
             XMLOutputter xmlopt = new XMLOutputter();
 
             // 创建文件输出流
             FileWriter writer = new FileWriter(xmlPath);
 
            // 指定文档格式
            Format fm = Format.getPrettyFormat();
 
            xmlopt.setFormat(fm);

            // 将doc写入到指定的文件中
            xmlopt.output(doc, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
