package com.rhymthwave.API_LR;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Request.DTO.AccountDTO;
import com.rhymthwave.Request.DTO.ChangePasswordDTO;
import com.rhymthwave.Service.Implement.AccountServiceImpl;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/account")
public class ChangePasswordAPI {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private AccountDAO accountDAO;
	@Autowired
	private AccountServiceImpl accountServiceImpl;
	@Autowired
	private GetHostByRequest host;
	
	@PutMapping("/changepassword")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changepasswordDTO,final HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		Account account = accountServiceImpl.findOne(owner);
		accountServiceImpl.changePass(changepasswordDTO,req,account);
		return ResponseEntity.ok(new MessageResponse(true, "Password changed successfully"));
	}
	
	@PutMapping("/updateprofile")
    public ResponseEntity<MessageResponse> updateProfile(@RequestBody AccountDTO accountRequest,HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		Account account = accountServiceImpl.findOne(owner);
		accountServiceImpl.update(accountRequest,req,account);	
        return ResponseEntity.ok(new MessageResponse(true, "Profile updated successfully"));
    }
}
