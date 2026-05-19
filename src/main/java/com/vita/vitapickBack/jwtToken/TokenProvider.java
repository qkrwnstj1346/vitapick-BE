package com.vita.vitapickBack.jwtToken;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenProvider {

	private static final String SECRET_KEY=
			"1234567890123456789012345678901234567890";
	//토큰발행
	public String createToken(Map<String, Object> claimList, int min) {
		
		SecretKey key = null;
		try {
			key = Keys.hmacShaKeyFor(
					TokenProvider.SECRET_KEY.getBytes("UTF-8"));
		}catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}//try
		
		return Jwts.builder()
				.setHeader(Map.of("typ", "JWT"))
				.setClaims(claimList)
				.setIssuer("vitepick app")
				.setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
				.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
				.signWith(key)
				.compact();
	}//createToken
	
	//토큰검증
	public Map<String, Object> validateToken(String token){
		
	    Map<String, Object> claim = null;
	    try {
	        SecretKey key = Keys.hmacShaKeyFor(
	            TokenProvider.SECRET_KEY.getBytes("UTF-8"));
	        claim = Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	    } catch (MalformedJwtException e) {
	        throw new CustomJWTException("MalFormed");
	    } catch (ExpiredJwtException e) {
	        throw new CustomJWTException("Expired");
	    } catch (InvalidClaimException e) {
	        throw new CustomJWTException("Invalid");
	    } catch (JwtException e) {
	        throw new CustomJWTException("JWTError");
	    } catch (Exception e) {
	        throw new CustomJWTException("Error");
	    }//try
	    return claim;
	}//validateToken
	
}//TokenProvider
