package com.rhymthwave.event;

import org.springframework.context.ApplicationEvent;

import com.rhymthwave.entity.Account;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpCompleteEvent extends ApplicationEvent {
	private Account account;
	private String applicationUrl;
	
	
	public SignUpCompleteEvent(Account account, String applicationUrl) {
		super(account);
		this.account = account;
		this.applicationUrl = applicationUrl;
	}
	
	
}
