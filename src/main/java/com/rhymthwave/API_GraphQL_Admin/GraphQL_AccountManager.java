package com.rhymthwave.API_GraphQL_Admin;

import java.util.List;

import com.rhymthwave.DAO.AccountDAO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.ServiceAdmin.IAccountServiceAdmin;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.TypeEnum.EROLE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class GraphQL_AccountManager {

	private final IAccountServiceAdmin accountServiceAdmin;
	private final AccountDAO accountDAO;
	@QueryMapping("getAllAccountByRole")
	public List<Account> getAllAccountByRole(@Argument("role") EROLE role) {
		return accountServiceAdmin.findAllAccountByRole(null, null, null, role);
	}

	@QueryMapping("findAccountByEmail")
	public Account findAccountByEmail(@Argument("id") String email) {
		return accountServiceAdmin.findById(email);
	}

	@MutationMapping("lockAccount")
	public boolean lockAccount(@Argument("id") String email,  @Argument("block") Boolean isBlock){
		Account account = accountServiceAdmin.findById(email);
		account.setBlocked(isBlock);
		accountDAO.save(account);
		return true;
	}


}
