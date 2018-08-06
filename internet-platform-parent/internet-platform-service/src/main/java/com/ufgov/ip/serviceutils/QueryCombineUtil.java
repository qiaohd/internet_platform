package com.ufgov.ip.serviceutils;

import java.util.Map;

public class QueryCombineUtil {
	public static String QueryConditionCombine(Map<String, Object> searchParams){	
		String conditionSql="";
		for (Map.Entry<String, Object> searchParam : searchParams.entrySet()) { 
			if(searchParam.getKey().startsWith("EQ")){
				conditionSql+="AND "+searchParam.getKey().substring(3).replaceAll("[A-Z]", "_$0")+" = '"+searchParam.getValue()+"'";
			}else if(searchParam.getKey().startsWith("LIKE")){
				conditionSql+="AND "+searchParam.getKey().substring(5).replaceAll("[A-Z]", "_$0")+" LIKE '"+searchParam.getValue()+"'";
			}else if(searchParam.getKey().startsWith("GT")){
				conditionSql+="AND "+searchParam.getKey().substring(3).replaceAll("[A-Z]", "_$0")+" > '"+searchParam.getValue()+"'";
			}else if(searchParam.getKey().startsWith("LT")){
				conditionSql+="AND "+searchParam.getKey().substring(3).replaceAll("[A-Z]", "_$0")+" < '"+searchParam.getValue()+"'";
			}else if(searchParam.getKey().startsWith("GTE")){
				conditionSql+="AND "+searchParam.getKey().substring(4).replaceAll("[A-Z]", "_$0")+" >= '"+searchParam.getValue()+"'";
			}else if(searchParam.getKey().startsWith("LTE")){
				conditionSql+="AND "+searchParam.getKey().substring(4).replaceAll("[A-Z]", "_$0")+" <= '"+searchParam.getValue()+"'";
			}else if(searchParam.getKey().startsWith("IN")){
				conditionSql+="AND "+searchParam.getKey().substring(3).replaceAll("[A-Z]", "_$0")+" IN "+searchParam.getValue();
			}
		}
		return conditionSql;
	}
}
