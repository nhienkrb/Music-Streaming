package com.rhymthwave.Utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rhymthwave.Utilities.JWT.JwtAuthentitationFilter;
import com.rhymthwave.Utilities.JWT.JwtTokenCreate;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class GetHostByRequest {

	@Autowired
	private JwtTokenCreate JwtTokenCreate;

	public String getEmailByRequest(HttpServletRequest request) {
		try {
			String jwt = JwtAuthentitationFilter.getJwtFromRequest(request);
			if (StringUtils.hasText(jwt) && JwtTokenCreate.validateToken(jwt)) {
				// lay username from String jwt
				String email = JwtTokenCreate.getUserNameJWT(jwt);
				return email;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;

	}

}
