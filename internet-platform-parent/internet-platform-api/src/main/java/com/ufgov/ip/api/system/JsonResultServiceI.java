package com.ufgov.ip.api.system;

import java.util.Map;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface JsonResultServiceI {

    public String toJson(Object obj);

    public <T> T toObject(String json, Class<T> cls);

    public ArrayNode createArrayNode();

    public ObjectNode createObjectNode();

    public Map<String, Object> buildMap(boolean success, String msg, Object data);

    public Map<String, Object> successMap(String msg, Object data);

    public Map<String, Object> failMap(String msg, Object data);

    public String successMapJson(String msg, Object data);

    public String failMapJson(String msg, Object data);
	
}
