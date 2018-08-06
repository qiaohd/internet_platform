package com.ufgov.ip.service.message.phoneverity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.message.phoneverity.SendMessageServiceI;
import com.ufgov.ip.utils.PropertyUtilSys;
import com.yonyou.uap.entity.content.EmailContent;
import com.yonyou.uap.entity.content.MessageContent;
import com.yonyou.uap.entity.content.SMSContent;
import com.yonyou.uap.entity.receiver.EmailReceiver;
import com.yonyou.uap.entity.receiver.MessageReceiver;
import com.yonyou.uap.entity.response.MessageResponse;
import com.yonyou.uap.service.MessageSend;
import com.yonyou.uap.service.impl.mail.EMailSend;

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
@Service(version = "0.0.1")
public class SendMessageService implements SendMessageServiceI{
	/****
	 * 
	 * @param phoneName 
	 * 				电话号码，如果是多个请用英文逗号分割，分开 比如：135380038000,135380038001,135380038002
	 * @param datas
	 * 				可选参数 内容数据，用于替换模板中{序号}
	 * @throws UnsupportedEncodingException 
	 */
	public  List<MessageResponse> phoneMessageSend(String phoneName,Object ... arguments) throws UnsupportedEncodingException{
		
		
			MessageReceiver msgReceivers = new MessageReceiver(phoneName);
			String content=PropertyUtilSys.getMessagePropertyByKey("content");
			if (arguments != null) {
				content =MessageFormat.format(content, arguments);
			}
			MessageContent msgContent = new SMSContent("title", content, 0);
			List<MessageResponse> responseList = new MessageSend(msgReceivers, msgContent).send();
			return responseList;
		}
		
	
	public  List<MessageResponse> emailMessageSend(String emailName,Object ... arguments) throws UnsupportedEncodingException{
		MessageReceiver emailReceivers = new MessageReceiver(emailName);
		String content=PropertyUtilSys.getMessagePropertyByKey("content");
		if (arguments != null) {
			content =MessageFormat.format(content, arguments);
		}
		MessageContent emailContent = new EmailContent("title", content);
		List<MessageResponse> responseList = new MessageSend(emailReceivers, emailContent).send();
		return responseList;
	}
	
	
	
	/*public static void main(String[] args) {
		new SendMessageService().phoneMessageSend("13580013800","12345","90","op");
	}*/
}
