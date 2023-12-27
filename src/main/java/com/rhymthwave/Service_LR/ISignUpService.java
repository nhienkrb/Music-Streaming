package com.rhymthwave.Service_LR;

import java.util.List;
import java.util.Optional;

import com.rhymthwave.Request.DTO.SignUpDTO;
import com.rhymthwave.entity.Account;

public interface ISignUpService {
	List<Account> getAccounts();
	Account signUp(SignUpDTO request);
//	void saveAccountVericationToken(Account acc,String verificationToken);
//	String validateToken(String verificationCode);
}
