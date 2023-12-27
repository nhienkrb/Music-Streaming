package com.rhymthwave.Utilities.JWT;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rhymthwave.Service.Implement.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenCreate {

	@Value("${jwt.secret}")
	private String JWT_SECRET; // Chữ ký

	@Value("${jwt.expiration}")
	private int JWT_EXPIRATION; // thời gian sống 1 ngay

	@Value("${jwt.refreshtoken.expiration}")
	private long JWT_REFRESH_EXPIRATION; // thời gian sống 1 nam

	
	// Tạo Token

	public String createToken(CustomUserDetails customUserDetails) {
		Date now = new Date();
		Date dateExpiration = new Date(now.getTime() + JWT_EXPIRATION); // thời gian hiện tại + 24h
		// Trả về 1 Token
		return Jwts.builder()
				.setSubject(customUserDetails.getUsername())
				.claim("role", customUserDetails.getAuthorities())// dữ
				.setIssuedAt(now) // Ngày start
				.setExpiration(dateExpiration) // Ngày end
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET) // Giải thuật mã hóa , key (Chữ ký )
				.compact();
	}

	public String createRefreshToken(CustomUserDetails customUserDetails) {

		Date now = new Date();
		Date dateExpiration = new Date(now.getTime() + JWT_REFRESH_EXPIRATION);
		// Trả về 1 Token
		return Jwts.builder()
				.setSubject(customUserDetails.getUsername()) // dữ liệu
				.claim("role", customUserDetails.getAuthorities())
				.setIssuedAt(now) // Ngày start
				.setExpiration(dateExpiration)
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET) // Giải thuật mã hóa , key (Chữ ký )
				.compact();
	}

	// Lấy data user từ JWT (Claims)
	public String getUserNameJWT(String token) {
		// Lấy ra toàn bộ Claims
		Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
		// Trả về Username
		return claims.getSubject();
	}
	
	
	// Validate thông tin của JWT -> hết hạn, lỗi....
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
			return true;
		} catch (MalformedJwtException ex) { // không hợp lệ
			log.error("Invalid JWT Token");
		} catch (ExpiredJwtException ex) { // hết hạn
			log.error("Expired JWT Token");
		} catch (UnsupportedJwtException ex) { // khong sp
			log.error("Unsupported JWT Token");
		} catch (IllegalArgumentException ex) { // chuỗi anti
			log.error("IllegalArgument JWT Token");
		}
		return false;
	}
}