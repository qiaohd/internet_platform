/*    */package com.uggov.ip.web.tmp;

/*    */
import com.ufgov.ip.api.sysmanager.IpRoleMenuServiceI;
import com.ufgov.ip.api.sysmanager.IpUserRoleServiceI;
import com.ufgov.ip.api.system.MenuServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.entity.sysmanager.IpRoleMenu;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpMenu;
import com.ufgov.ip.entity.system.IpUser;
import com.yonyou.iuap.auth.session.SessionManager;
import com.yonyou.iuap.auth.shiro.StatelessToken;
/*    */
import com.yonyou.iuap.auth.token.ITokenProcessor;
/*    */
import com.yonyou.iuap.auth.token.TokenFactory;
/*    */
import com.yonyou.iuap.auth.token.TokenParameter;



/*    */
import java.util.ArrayList;
/*    */
import java.util.List;

import org.apache.commons.lang3.StringUtils;
/*    */
import org.apache.shiro.authc.AuthenticationException;
/*    */
import org.apache.shiro.authc.AuthenticationInfo;
/*    */
import org.apache.shiro.authc.AuthenticationToken;
/*    */
import org.apache.shiro.authc.SimpleAuthenticationInfo;
/*    */
import org.apache.shiro.authz.AuthorizationInfo;
/*    */
import org.apache.shiro.authz.SimpleAuthorizationInfo;
/*    */
import org.apache.shiro.realm.AuthorizingRealm;
/*    */
import org.apache.shiro.subject.PrincipalCollection;
/*    */
import org.slf4j.Logger;
/*    */
import org.slf4j.LoggerFactory;
/*    */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*    */
/*    */@Component
/*    */public class myRealm
/*    */extends AuthorizingRealm
/*    */{
	/* 25 */private static final Logger logger = LoggerFactory
			.getLogger(myRealm.class);
	/*    */
	/*    */@Autowired
	/*    */TokenFactory tokenFactory;
	/*    */
	@Autowired
	protected UserAccountServiceI userAccountService;

	@Autowired
	protected IpRoleMenuServiceI ipRoleMenuService;

	@Autowired
	protected IpUserRoleServiceI ipUserRoleService;
	
	@Autowired
	protected MenuServiceI menuService;

	/*    */@Autowired
	/*    */private SessionManager sessionManager;

	/*    */
	/*    */public boolean supports(AuthenticationToken token)
	/*    */{
		/* 35 */return token instanceof StatelessToken;
		/*    */}

	/*    */
	/*    */protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals)
	/*    */{
		String primaryPrincipal = (String) principals.getPrimaryPrincipal();
		//Principal principal = (Principal) getAvailablePrincipal(principals);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		IpUser user = userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(primaryPrincipal);
		List<IpMenu> menuList = new ArrayList<IpMenu>();
		if(null==user){
			return null;
		}else if("0".equals(user.getUserType())){
			return info;
		}else{
			List<IpUserRole> ipUserRoleList = ipUserRoleService
					.showUserRoleByUserId(user.getUserId());
			for (IpUserRole ipUserRole : ipUserRoleList) {
				List<IpRoleMenu> list = ipRoleMenuService.findMenuByRole(ipUserRole
						.getRoleId());
				// 添加用户角色信息
				info.addRole(ipUserRole.getRoleName());
				for (IpRoleMenu ipRoleMenu : list) {
					if (!menuList.contains(ipRoleMenu)) {
						//找到ipmenu中的菜单信息
						IpMenu ipMenu=menuService.findByMenuId(ipRoleMenu.getMenuId());
						menuList.add(ipMenu);
					}
				}
			}
			for (IpMenu menu : menuList) {
				if (StringUtils.isNotBlank(menu.getPermission())) {
					// 添加基于Permission的权限信息
					info.addStringPermission(menu.getPermission());
				}
			}
			/*// 添加用户权限
			info.addStringPermission("user");*/
			return info;
		} 
		/*    */}

	/*    */
	/*    */protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken atoken) throws AuthenticationException
	/*    */{
		/* 48 */StatelessToken token = (StatelessToken) atoken;
		/* 49 */TokenParameter tp = token.getTp();
		/* 50 */String uname = (String) token.getPrincipal();
		/* 51 */ITokenProcessor tokenProcessor = token.getTokenProcessor();
		/* 52 */String tokenStr = tokenProcessor.generateToken(tp);
		/* 53 */if ((tokenStr == null)
				|| (!sessionManager.validateOnlineSession(uname, tokenStr))) {
			/* 54 */logger
					.error("User [{}] authenticate fail in System, maybe session timeout!",
							uname);
			/* 55 */throw new AuthenticationException("User " + uname
					+ " authenticate fail in System");
			/*    */}
		/*    */
		/* 58 */return new SimpleAuthenticationInfo(uname, tokenStr, getName());
		/*    */}
	/*    */
}

/*
 * Location:
 * D:\DevTool\repository\Maven\Maven3.2.2\local\repo-yy\com\yonyou\iuap
 * \iuap-auth\3.0.0-RELEASE\iuap-auth-3.0.0-RELEASE.jar Qualified Name:
 * com.yonyou.iuap.auth.shiro.StatelessRealm Java Class Version: 7 (51.0)
 * JD-Core Version: 0.7.1
 */