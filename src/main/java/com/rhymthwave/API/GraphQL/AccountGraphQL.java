package com.rhymthwave.API.GraphQL;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.Service.AccountService;
import com.rhymthwave.entity.Account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountGraphQL {
	
	private final AccountService accountSer;
	
	@QueryMapping("accountByUsername")
	public Account findByUsername(@Argument("username") String username) {
		return accountSer.findAccountByUsername(username);
	}
}
