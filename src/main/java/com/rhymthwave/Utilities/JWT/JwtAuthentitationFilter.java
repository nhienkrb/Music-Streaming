package com.rhymthwave.Utilities.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rhymthwave.Service.Implement.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthentitationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenCreate JwtTokenCreate;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	public static String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		// Lấy và check Authorizaton đã có JWT chưa
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		try {
			// Lấy thông tin từ request
			String jwt = getJwtFromRequest(request);
			if (StringUtils.hasText(jwt) && JwtTokenCreate.validateToken(jwt)) {
				// lay username from String jwt
				String username = JwtTokenCreate.getUserNameJWT(jwt);
				
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
				if (userDetails != null) {
					// Neu user hop le thong tin cho security context
					UsernamePasswordAuthenticationToken authentication
					 = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("fail on set authentication",e);
		}

		filterChain.doFilter(request, response);
	}

}