package com.rhymthwave.Utilities.Cookie;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class CookiesUntils {

	private  final HttpServletResponse response;
    @Value("${jwt.expiration}")
    private int JWT_EXPIRATION;
    public  Cookie add(String value) {
		Cookie cookie =new Cookie("token",value);
		cookie.setMaxAge(JWT_EXPIRATION);
		cookie.setPath("/");
		response.addCookie(cookie);
		return cookie;
	}

	public static String get(String name,  HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if(cookies!=null) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return "Cookie doesn't exist or Cookie is expirate";
	}
}
