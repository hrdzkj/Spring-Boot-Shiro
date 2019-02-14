package org.gxqfy.exam.shiro;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.gxqfy.exam.database.UserBean;
import org.gxqfy.exam.database.UserService;
import org.gxqfy.exam.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
 * 继承AuthorizingRealm，实现用户授权的验证和权限的验证
 */
@Service
public class MyRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LogManager.getLogger(MyRealm.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
           * 必须重写此方法，不然Shiro会报错
     **/
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**          
            * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     * PrincipalCollection是一个身份集合，因为我们可以在Shiro中同时配置多个Realm，所以呢身份信息可能就有多个；
            *  因此其提供了PrincipalCollection用于聚合这些身份信息
     * https://blog.csdn.net/yifansj/article/details/77513047 
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	System.out.println("————权限认证————");
        String username = JWTUtil.getUsername(principals.toString());
        UserBean user = userService.getUser(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole(user.getRole()); //角色
        Set<String> permission = new HashSet<>(Arrays.asList(user.getPermission().split(",")));
        simpleAuthorizationInfo.addStringPermissions(permission);//权限
        return simpleAuthorizationInfo;
    }

    /**
          * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
    	System.out.println("————身份认证方法————");
    	String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token 不合法");
        }

        UserBean userBean = userService.getUser(username);
        if (userBean == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (! JWTUtil.verify(token, username, userBean.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }

        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
