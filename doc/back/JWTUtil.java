/*
package org.gxqfy.exam.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//JWT加密，校验工具，并且使用用户自己的密码充当加密密钥
public class JWTUtil {

    // 过期时间5分钟
     // private static final long EXPIRE_TIME = 5*60*1000;
	//测试改为24小时过期时间
	 private static final long EXPIRE_TIME =  60 * 24 * 60 * 1000;


    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }


    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    public static String sign(String username, String secret) {
        try {
        	// header Map
            Map<String, Object> map = new HashMap<>();
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create().withHeader(map) // header头部
                    .withClaim("username", username) //payload 载荷
                    .withExpiresAt(date) // 过期时间
                    .sign(algorithm); //签名
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
*/
