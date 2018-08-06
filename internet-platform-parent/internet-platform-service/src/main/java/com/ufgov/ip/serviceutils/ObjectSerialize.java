package com.ufgov.ip.serviceutils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;
/**
 * 进行对象序列化及反序列化
 * @author zhangbch
 *
 */
public class ObjectSerialize {
	 public static byte[] serialize(List value) {  
         if (value == null) {  
             throw new NullPointerException("对象信息为空,不能序列化");  
         }  
         byte[] rv=null;  
         ByteArrayOutputStream bos = null;  
         ObjectOutputStream os = null;  
         try {  
             bos = new ByteArrayOutputStream();  
             os = new ObjectOutputStream(bos);  
             
             os.writeObject((List<IpUser>)value.get(0));
             os.writeObject((List<IpUserCompany>)value.get(1));
             os.writeObject((List<IpUserRole>)value.get(2));
             
             os.writeObject(null);  
             os.close();  
             bos.close();  
             rv = bos.toByteArray();  
         } catch (IOException e) {  
             throw new IllegalArgumentException("该对象不能序列化", e);  
         }
         return rv;  
     }
	 
	 
	 public static List<String> deserialize(byte[] in) {  
         List list = new ArrayList();  
         ByteArrayInputStream bis = null;  
         ObjectInputStream is = null;  
         try {  
             if(in != null) {  
                 bis=new ByteArrayInputStream(in);  
                 is=new ObjectInputStream(bis);  
                 
                 List<IpUser> byte_userList=(List<IpUser>)is.readObject();
                 List<IpUserCompany> byte_userCOList=(List<IpUserCompany>)is.readObject();
                 List<IpUserRole> byte_userRoleList=(List<IpUserRole>)is.readObject();
                 list.add(byte_userList);
                 list.add(byte_userCOList);
                 list.add(byte_userRoleList);
                 
                 is.close();  
                 bis.close();  
             }  
         } catch (Exception e) {  
                    
        	 
         } 
         return list;  
     }  
}
