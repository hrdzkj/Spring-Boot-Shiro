package org.gxqfy.exam.shiro;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.gxqfy.exam.component.JWTComponent;
import org.gxqfy.exam.component.UserComponent;
import org.gxqfy.exam.po.StudentInfo;
import org.gxqfy.exam.service.StudentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
 * 继承AuthorizingRealm，实现用户授权的验证和权限的验证
 * 测试发现，不能@Autowired变量，要用方法。
 */
@Service
public class MyRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LogManager.getLogger(MyRealm.class);
	private StudentInfoService studentInfoService;
	@Autowired
	private UserComponent userComponent;
	@Autowired
	private JWTComponent jwtComponent;
    
    @Autowired
    public void setUserService(StudentInfoService studentInfoService) {
        this.studentInfoService = studentInfoService;
    }
    
    
    /**
     * 必须重写此方法，不然Shiro会报错
     **/
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**   
     * Authorization 授权       
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     * PrincipalCollection是一个身份集合，因为我们可以在Shiro中同时配置多个Realm，所以呢身份信息可能就有多个；
            *  因此其提供了PrincipalCollection用于聚合这些身份信息
     * https://blog.csdn.net/yifansj/article/details/77513047 
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	System.out.println("————权限认证————");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole("admin"); //角色
        return simpleAuthorizationInfo;
    }

    /**
     * Authentication：认证
          * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
    	System.out.println("————开始身份认证————");
    	String token = (String) auth.getCredentials();
 
        if (token == null) {
            throw new AuthenticationException("没有token参数");
        }
        
        String username = jwtComponent.getUsernameFromToken(token);;
        if (username == null) {
            throw new AuthenticationException("token 不合法");
        }

        // 解密获得username，和已经登录的进行对比
        StudentInfo studentInfo = userComponent.getStudent(username);
        if (studentInfo==null) {
        	throw new AuthenticationException("用户尚未登录");
        }
        
       // 是否过期
    	if(jwtComponent.isTokenExpired(token)) {
    		throw new AuthenticationException("token已经过期");
    	}
    	System.out.println("————身份认证成功————");
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
