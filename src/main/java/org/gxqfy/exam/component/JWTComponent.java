package org.gxqfy.exam.component;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.gxqfy.exam.po.StudentInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

@Component
public class JWTComponent implements Serializable {
    // 过期时间5分钟
    // private static final long EXPIRE_TIME = 5*60*1000;
	//测试改为24小时过期时间
	private static final long EXPIRE_TIME =  60 * 24 * 60 * 1000;
	
	private static SecretKey mKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary("ExampleKey"), SignatureAlgorithm.HS512.getJcaName()); //Keys.secretKeyFor(SignatureAlgorithm.HS256); 
	private static final long serialVersionUID = 1L;
	
	@Value("${springbootwebfluxjjwt.jjwt.secret}")
	private String secret;
	
	public Claims getAllClaimsFromToken(String token) {
		//return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes())).parseClaimsJws(token).getBody();
		return Jwts.parser().setSigningKey(mKey).parseClaimsJws(token).getBody();
	}
	
	public String getUsernameFromToken(String token) {
		try {
		return getAllClaimsFromToken(token).getSubject();
		}catch (Exception e) {
			return null;
		}
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}
	
	public Boolean isTokenExpired(String token) {
		 Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	public String generateToken(StudentInfo studentInfo) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", "admin");
		return doGenerateToken(claims, studentInfo.getStudentAccount());
	}


	
	private String doGenerateToken(Map<String, Object> claims, String username) {
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + EXPIRE_TIME);
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(createdDate)
				.setExpiration(expirationDate)
				.signWith(mKey)
				.compact();
	}
	


}
