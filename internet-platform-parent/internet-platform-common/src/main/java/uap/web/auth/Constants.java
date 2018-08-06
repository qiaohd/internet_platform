package uap.web.auth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

import com.ufgov.ip.utils.PropertyUtil;

public class Constants {
	
    public static final String PARAM_DIGEST = "digest";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_TOKEN = "token";

    public static String COOKIES_PATH = PropertyUtil.getPropertiesKey("application.properties","context.name");
    public static final int HTTP_STATUS_AUTH = 306;
    public static final String[] esc = new String[] { "/logout", "/login", "/formLogin", "/libs", "/css", "/js","/uformLogin",
    	"/register","/formRegister","/getImage","/checkImage","/phoneLogin","/phoneFormLogin","/mailFormLogin",
    	"/mailLogin","/checkPhoneisExist","/regCheckUserName","/checkMailisExist","/userLogout","getMobileCode","checkAuthCode","/upload","/template","/temp.xlsx","/organizationSort","/sortPage"};
    public static final String USER_INFO_LOGIN = "user.info.login.example";
}