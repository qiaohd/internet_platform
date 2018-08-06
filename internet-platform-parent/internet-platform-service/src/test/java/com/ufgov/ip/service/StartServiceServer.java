package com.ufgov.ip.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartServiceServer {
	public static void main(String[] args) {
		new Thread() {
			private ClassPathXmlApplicationContext classPathXmlApplicationContext;

			public void run() {
				classPathXmlApplicationContext = new ClassPathXmlApplicationContext(
						new String[] { "applicationContext.xml","applicationContext-persistence.xml",
								"applicationContext-dubbo-provider.xml"});
				classPathXmlApplicationContext.start();
				while (true) {
				}
			};
		}.start();

	}
}
