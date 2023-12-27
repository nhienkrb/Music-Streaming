package com.rhymthwave.API_LR;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.Implement.CustemerOAuth2User;
import com.rhymthwave.Service_LR.ILoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginWithSocialNetworks {

    private final ILoginService loginSuccess;

    @GetMapping("/api/v1/auth/success")
    public String loginSuccess(OAuth2AuthenticationToken token) {
        loginSuccess.checkLoginWithSocial(token);
        return "redirect:/";
    }

    @GetMapping("/api/v1/auth/fail")
    public String loginFail() {
        return "";
    }

    @GetMapping("/api/v1/auth/logout")
    public String logout() {
        return "redirect:/signin";
    }

}
