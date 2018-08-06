package com.ufgov.ip.utils;

import java.util.Random;

public class ProduceCodeUtil {

	//生成随机码(待改进)
	public static String getCode(){
				Random random = new Random();
			      String code="";
			       for(int i=0;i<6;i++){
			         code+=random.nextInt(10);
				    }
		   return code;
	}
	
	
	
	
	
	
	
}
