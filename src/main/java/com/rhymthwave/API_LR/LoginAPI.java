package com.rhymthwave.API_LR;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Request.DTO.LoginDTO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.Implement.AccountServiceImpl;
import com.rhymthwave.Service.Implement.CustomUserDetails;
import com.rhymthwave.Service_LR.ILoginService;
import com.rhymthwave.Utilities.JWT.JwtTokenCreate;
import com.rhymthwave.entity.Account;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/accounts")
public class LoginAPI {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ILoginService loginService;

    @Autowired
    private JwtTokenCreate jwtTokenCreate;

    @Autowired
    private AccountServiceImpl accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginRequest) {

        int checkUserLogin = loginService.checkIsVerified(loginRequest);

        if (checkUserLogin == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(false, "Error Login", null));
        }
        if (checkUserLogin == 1) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
            System.out.println(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            Account user = accountService.findOne(customUserDetails.getEmail());
            customUserDetails.setEmail(user.getEmail());
            user.setRefreshToken(jwtTokenCreate.createRefreshToken(customUserDetails));
            accountService.update(user);

            String jwt = jwtTokenCreate.createToken(customUserDetails);

            Map<String, String> list = new HashMap<>();
            list.put("accessToken", jwt);
            list.put("refreshToken", user.getRefreshToken());
            return ResponseEntity.ok(new MessageResponse(true, "User Tokens", list));
        }

        if (checkUserLogin == 2) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(false, "Confirmation has been sent to your email", null));
        }

        if (checkUserLogin == 3) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(false, "Your account has been locked", null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(false, "Error Login", null));
    }

//	@GetMapping("/loginGG")
//	public ResponseEntity<String> hello(){
//		return ResponseEntity.ok("Hello");
//	}

//	@GetMapping
//	public ResponseEntity<?> login() {
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(new MessageResponse(true, "Error Login", accountService.findAll()));
//	}


}