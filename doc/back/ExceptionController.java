package org.gxqfy.exam.handler;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.gxqfy.exam.UnauthorizedException;
import org.gxqfy.exam.po.ResponseBean;
import org.gxqfy.exam.po.StudentInfo;
import org.gxqfy.exam.util.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionController {

    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ResponseBean handle401(ShiroException e) {
        return new ResponseBean(401, e.getMessage(), null);
    }
    
    // 捕捉UnauthorizedException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseBean handle401() {
        return new ResponseBean(401, "Unauthorized", null);
    }

    /*
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseBean handleAuthenticationException()
    {
    	System.out.println("--------->UnauthenticatedException");
    	 return new ResponseBean(401, "UnauthenticatedException", null);	
    }
   */ 
    // 捕捉其他所有异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBean globalException(HttpServletRequest request, Throwable ex) {
    	System.out.println("--------->globalException");
        return new ResponseBean(getStatus(request).value(), ex.getMessage(), null);
    }

    
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}

