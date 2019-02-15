package org.gxqfy.exam.shiro;


import org.apache.shiro.authc.AuthenticationToken;

public class JWTToken implements AuthenticationToken {
    private String token;  // token


    public JWTToken(String token) {
        this.token = token;
    }

    
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
