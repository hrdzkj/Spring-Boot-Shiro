package org.gxqfy.exam.shiro;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.gxqfy.exam.po.ResponseBean;
import org.gxqfy.exam.util.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends BasicHttpAuthenticationFilter {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private boolean haveToken(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return !TextUtil.isEmpty(authorization);
	}
    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
         return haveToken(request);
    }

   
    //onAccessDenied:表示当访问拒绝时是否已经处理了;如果返回true表示需要继续处理;如果返回false表示该拦截器实例已经处理了,将直接返回即可
    // 这是"————开始身份认证————" 打印两次的原因，原因super.onAccessDenied(request, response)执行了isLoginAttempt
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		return false;//super.onAccessDenied(request, response);
	}
	
    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    	// 目前通过这个过虑器的，都要求必须登录
    	if (!isLoginAttempt(request, response)) {
    		responseError(request, response,"您尚未登录，请先登录");
    		return false;
    	}

        try {
        	// 委托给 Realm 进行登录，如果没有引发异常，证明登录成功
            executeLogin(request, response); 
            return true;
        } catch (Exception e) { //捕获提交给realm进行登入的引发的异常
        	responseError(request, response,e.getMessage());
            return false;
        }
    }
    
    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        JWTToken token = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被isAccessAllowed捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }



    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
    
    /**
     * 将非法请求跳转到 /401
     */
    private void responseError(ServletRequest req, ServletResponse response,String message) {
    	response.setCharacterEncoding("UTF-8");  
    	response.setContentType("application/json; charset=utf-8");
    	PrintWriter out = null ;
    	try{
    		out = response.getWriter();
            ResponseBean bean = new ResponseBean(401, message, null); 
    	    out.append(bean.toString());
    	    out.flush();
    	}catch (Exception e){
    	    e.printStackTrace();
    	}finally {
    		if (out!=null) {
    			out.close();
    		}
		}   
	
    }
 
}
