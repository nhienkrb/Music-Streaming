package com.rhymthwave.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rhymthwave.Service.AuthorService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Artist;
import com.rhymthwave.entity.Author;
import com.rhymthwave.entity.Role;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MailResponseController {
	private final CRUD<Author, Long> crudAuthor;

	private final AuthorService authorSer;

	private final CRUD<Account, String> crudAccount;

	private final CRUD<Role, Integer> crudRole;

	@GetMapping("/confirm-success")
	public String uiresp() {
		return "Mail/templateComfirmSuccess";
	}

	@GetMapping("/confirm-fail")
	public String uirespFail() {
		return "Mail/templateComfirmFail";
	}

	// Test ==> Move to controller
	@GetMapping("/confirm-email-podcaster/{email}")
	public String confirmPodcast(@PathVariable("email") String email) {
		Account account = crudAccount.findOne(email);
		Role role = crudRole.findOne(3);
		if (authorSer.findAuthor(3, email) == null) {
			Author author = new Author();
			author.setAccount(account);
			author.setRole(role);
			crudAuthor.create(author);
			return "redirect:/confirm-success";
		}
		return "redirect:/confirm-fail";
	}
}
