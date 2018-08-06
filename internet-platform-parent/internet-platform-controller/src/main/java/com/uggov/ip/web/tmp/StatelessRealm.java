package com.uggov.ip.web.tmp;

import java.util.Properties;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import uap.web.auth.StatelessToken;
import uap.web.esapi.EncryptException;
import uap.web.httpsession.cache.SessionCacheManager;

import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.utils.TokenGenerator;
import com.uggov.ip.web.tmp.ShiroDbRealm.ShiroUser;


public class StatelessRealm extends AuthorizingRealm {
	
	private static final Logger logger = LoggerFactory.getLogger(StatelessRealm.class);
	
	
    @Autowired
    private SessionCacheManager sessionCacheManager;
	
    @Override
    public boolean supports(AuthenticationToken token) {
        //仅支持StatelessToken类型的Token
        return token instanceof StatelessToken;
    }
    @Override
	public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		//MgrUser user = accountService.findUserByLoginName(shiroUser.loginName);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// info.addRoles(user.getRoleList());
		return info;
    }
    
    @Override
	public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String uname = (String) token.getPrincipal();
        String tokenPass = genTokenPass(uname);
        if (tokenPass == null) {
            logger.error("User [{}] not exists in System", uname);
            throw new AuthenticationException("User " + uname + " not exists in System");
        }
        return new SimpleAuthenticationInfo(uname, tokenPass, getName());
    }

    /**
     * 查找用户
     *
     * @param uname
     * @return
     */
    public String genTokenPass(String uname) {

        try {
            String seed = sessionCacheManager.findSeed();
           
            //begin_zhangbch_iuap_20160312
           // MgrUser user = sessionCacheManager.getCurUser(uname);
            Object obj = sessionCacheManager.getCurUser(uname);
            if(obj instanceof IpHirer){
            	IpHirer iPHirer=sessionCacheManager.getCurUser(uname);
            	if (iPHirer != null && iPHirer.getLoginTs() != 0) {
                    return TokenGenerator.genToken(uname, iPHirer.getLoginTs(), seed);
                }
            }else if(obj instanceof IpUser){
            	IpUser iPUser=sessionCacheManager.getCurUser(uname);
            	if (iPUser != null && iPUser.getLoginTs() != 0) {
                    return TokenGenerator.genToken(uname, iPUser.getLoginTs(), seed);
                }
            }else if(uname.equals("superman_yyzw")){
            	Properties pro=sessionCacheManager.getCurUser(uname);
                String ts = pro.getProperty("ts");
            	return TokenGenerator.genToken(uname,Long.valueOf(ts) , seed);
            }else{
            	
            }
            
        } catch (EncryptException e) {
            logger.error("Fail to calculate Token Seed!", e);
        }
        return null;
    }
}