package com.rhymthwave.Service_LR;

import com.rhymthwave.Request.DTO.LoginDTO;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface ILoginService {

	int checkIsVerified(LoginDTO loginRequest);

	Object checkLoginWithSocial(OAuth2AuthenticationToken token);
}
