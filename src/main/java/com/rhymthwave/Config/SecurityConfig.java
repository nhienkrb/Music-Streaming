package com.rhymthwave.Config;

import com.rhymthwave.Service.Implement.CustomOAuth2UserService;
import com.rhymthwave.Service.Implement.CustomUserDetailsService;
import com.rhymthwave.Utilities.JWT.JwtAuthentitationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthentitationFilter jwtAuthentitationFilter() {
		return new JwtAuthentitationFilter();
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	public AuthenticationProvider AuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    	http.formLogin().loginPage("/signin").loginProcessingUrl("/api/v1/accounts/login").defaultSuccessUrl("/", false)
//		.failureUrl("/signin").usernameParameter("username").passwordParameter("password");

		http.cors().and().csrf().disable().authorizeHttpRequests((authz) -> {
			try {
				authz
				
						// All
						.requestMatchers("/webjars/**", "/swagger-ui/**", "/v2/api-docs", "/v3/api-docs",
								"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
								"/configuration/security")
						.permitAll()

//						.requestMatchers(HttpMethod.GET, "/**").permitAll()
//						.requestMatchers(HttpMethod.POST, "/**").permitAll()
//						.requestMatchers(HttpMethod.PUT, "/**").permitAll()
//						.requestMatchers(HttpMethod.DELETE, "/**").permitAll()
//						.requestMatchers(HttpMethod.PATCH, "/**").permitAll()

						.requestMatchers("/api/v1/top-playlist-new","/signin", "/subscription/**", "/", "/error/404", "/getstarted/**",
								"/api/v1/accounts/**", "/api/v1/auth/**", "/api/v1/search/**", "/podcast/home", "/home",
								"/graphiql/**", "/artist/home")
						.permitAll()
						.requestMatchers("/podcaster", "/podcast-browse").hasAnyAuthority("PODCAST")
						.requestMatchers("/artist").hasAnyAuthority("ARTIST")
						.requestMatchers("/api/v1/admin/**", "/admin").hasAnyAuthority("MANAGER", "STAFF")

						.requestMatchers("/static/**").permitAll().anyRequest().permitAll().and().exceptionHandling()
						.accessDeniedPage("/signin");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).authenticationProvider(AuthenticationProvider()).addFilterBefore(jwtAuthentitationFilter(),
				UsernamePasswordAuthenticationFilter.class);
		http.logout().logoutSuccessUrl("/api/v1/auth/logout").addLogoutHandler(new SecurityContextLogoutHandler())
				.clearAuthentication(true);

		http.oauth2Login().loginPage("/signin").defaultSuccessUrl("/api/v1/auth/success", true)
				.failureUrl("/api/v1/auth/fail").authorizationEndpoint().baseUri("/oauth2");
		return http.build();
	}

}
