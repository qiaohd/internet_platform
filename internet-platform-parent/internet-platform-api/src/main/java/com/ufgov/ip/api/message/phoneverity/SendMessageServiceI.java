package com.ufgov.ip.api.message.phoneverity;

import java.io.UnsupportedEncodingException;
import java.util.List;


import com.yonyou.uap.entity.response.MessageResponse;

/**
 * filename:SendTemplateSMSService.java
 *
 * Version:1.0
 *
 * author:qiaohd
 *
 * Date:2016-04-14
 *
 * Copyright  2016 by yonyou.ufgov
 */
public interface SendMessageServiceI {
	/****
	 * 
	 * @param phoneName 
	 * 				电话号码，如果是多个请用英文逗号分割，分开 比如：135380038000,135380038001,135380038002
	 * @param datas
	 * 				可选参数 内容数据，用于替换模板中{序号}
	 * @throws UnsupportedEncodingException 
	 */
	public  List<MessageResponse> phoneMessageSend(String phoneName,Object ... arguments) throws UnsupportedEncodingException;
	
	public  List<MessageResponse> emailMessageSend(String emailName,Object ... arguments) throws UnsupportedEncodingException;

}
