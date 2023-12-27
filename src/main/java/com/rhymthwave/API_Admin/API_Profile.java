package com.rhymthwave.API_Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Request.DTO.ChangePasswordDTO;
import com.rhymthwave.Service.AccountService;
import com.rhymthwave.Service.Implement.AccountServiceImpl;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/profile")
@RequiredArgsConstructor
public class API_Profile {
		
	private final AccountService accountService;
	
	@Autowired
	private AccountServiceImpl accountServiceImpl;
	
	@Autowired
	private GetHostByRequest host;
	
	@GetMapping
	public ResponseEntity<?> getProfile(final HttpServletRequest request) {

		Account admin = accountService.findAdminByEmail(request);
		
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", admin));
	}
	
	@PutMapping("/changepassword")
	public ResponseEntity<?> changeadminpass(@RequestBody ChangePasswordDTO changepasswordDTO,final HttpServletRequest request) {

		String owner = host.getEmailByRequest(request);
		Account account = accountServiceImpl.findOne(owner);
		
		if(account == null) {
			return ResponseEntity.ok(new MessageResponse(false, "Account admin is exist!!!"));
		}
		accountServiceImpl.changePass(changepasswordDTO,request,account);
		
		return ResponseEntity.ok(new MessageResponse(true, "Password changed successfully"));
	}
	@PutMapping("/logout")
    public ResponseEntity<MessageResponse> logout(final HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		Account account = accountServiceImpl.findOne(owner);
		accountServiceImpl.logout(req,account);	
        return ResponseEntity.ok(new MessageResponse(true, "Logout account successfully"));
    }

}
