package com.ufgov.ip.common;

import java.util.HashMap;
import java.util.Map;

public class ContextThreadLocal {

	final public static String TENANT_ID = "tenant_id";

	private static ThreadLocal<Map<String, String>> currentContext = new ThreadLocal<Map<String, String>>() {
		protected java.util.Map<String, String> initialValue() {
			return new HashMap<String, String>();
		};
	};

	public static void setCurrentContext(Map<String, String> context) {
		currentContext.get().putAll(context);
	}

	public static Map<String, String> getCurrentContext() {
		return currentContext.get();
	}

	public static String getCurrentTenant() {
		return currentContext.get().get("TENANT_ID");
	}

	public static void setCurrentTenant(String tenant) {
		currentContext.get().put("TENANT_ID", tenant);
	}

}
