package com.rhymthwave.Service;


import com.rhymthwave.Request.DTO.AccountDTO;

import java.util.List;

import com.rhymthwave.entity.Account;
import jakarta.servlet.http.HttpServletRequest;

public interface AccountService extends CRUD<Account, String>{

	Account findAdminByEmail(HttpServletRequest request);
	
	List<Object> search(String keyword);
	
	List<Object> searchGr(String keyword);

	Account findAccountByUsername(String username);


	Account update(AccountDTO accountRequest, HttpServletRequest req, Account account);
	

}
