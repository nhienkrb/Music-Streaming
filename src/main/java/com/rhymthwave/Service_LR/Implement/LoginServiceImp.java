package com.rhymthwave.Service_LR.Implement;

import com.rhymthwave.Request.DTO.LoginDTO;
import com.rhymthwave.Request.DTO.SignUpDTO;
import com.rhymthwave.Service.AccountService;
import com.rhymthwave.Service.Implement.CustomUserDetails;
import com.rhymthwave.Service.Implement.CustomUserDetailsService;
import com.rhymthwave.Service_LR.ILoginService;
import com.rhymthwave.Service_LR.ISignUpService;
import com.rhymthwave.Utilities.Cookie.CookiesUntils;
import com.rhymthwave.Utilities.GetCurrentTime;
import com.rhymthwave.Utilities.JWT.JwtTokenCreate;
import com.rhymthwave.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginServiceImp implements ILoginService {


    private final AccountService accountService;

    private final PasswordEncoder encoder;

    private final JwtTokenCreate jwtTokenCreate;

    private final ISignUpService signUpService;

    private  final CookiesUntils cookiesUntils;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public int checkIsVerified(LoginDTO loginRequest) {
        Account user = accountService.findOne(loginRequest.email());
        if (user == null) {
            return 0;
        }

        if (encoder.matches(loginRequest.password(), user.getPassword()) && user.isVerify() && user.isBlocked() == false) {
            return 1;
        }
        if (user.isBlocked() == true) {
            return 3;
        }
        if (user.isVerify() == false) {
            return 2;
        }

        return 4;
    }


    @Override
    public Object checkLoginWithSocial(OAuth2AuthenticationToken token) {
        String email = token.getPrincipal().getAttribute("email");
        Account account = accountService.findOne(email);
        if (account == null) {
            String userName = token.getPrincipal().getAttribute("name");
            String password = (GetCurrentTime.getTimeNow().toString());
            SignUpDTO signUpDTO = new SignUpDTO(email, password, userName, null, 0, "VN");
            Account account1 = signUpService.signUp(signUpDTO);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(account1.getEmail());
            // Neu user hop le thong tin cho security context
            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            customUserDetails.setEmail(account.getEmail());
            account.setRefreshToken(jwtTokenCreate.createRefreshToken(customUserDetails));
            accountService.update(account);
            String jwt = jwtTokenCreate.createToken(customUserDetails);
            cookiesUntils.add(jwt);
            return jwt;
        } else {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            // Neu user hop le thong tin cho security context
            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            customUserDetails.setEmail(account.getEmail());
            account.setRefreshToken(jwtTokenCreate.createRefreshToken(customUserDetails));
            accountService.update(account);
            String jwt = jwtTokenCreate.createToken(customUserDetails);
            cookiesUntils.add(jwt);
            return jwt;
        }
    }

}
