package com.ufgov.ip.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

public class PropertyUtil {

	private static Properties prop = null;

	/*static {
		prop = new Properties();
		// InputStream in =
		// Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
		try {
			FileInputStream in = new FileInputStream(new File(
					"/etc/ecconfig/application.properties"));
			prop.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	public static String getPropertiesKey(String proFile, String key) {
		prop = new Properties();
		InputStream in = null;
		try {
			in = new ClassPathResource(proFile).getInputStream();
			prop.load(in);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}

		}
		return prop.getProperty(key);
	}

	/*public static String getPropertyByKey(String key) {
		String value = prop.getProperty(key);
		return StringUtils.isBlank(value) ? "" : value;
	}*/
}
