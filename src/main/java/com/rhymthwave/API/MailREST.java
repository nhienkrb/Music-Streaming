package com.rhymthwave.API;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.EmailService;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.Utilities.SendMailTemplateService;
import com.rhymthwave.entity.Email;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class MailREST {
	
	private final EmailService mailService;
	
	private final SendMailTemplateService sendMailTemplateSer;
	
	private final GetHostByRequest host;
	
	@PostMapping("/api/v1/email-confirm-podcast/{email}")
	public ResponseEntity<MessageResponse> sendMail(HttpServletRequest req,@PathVariable("email") String email) {
		Email mail = new Email();
		mail.setFrom("musicstreaming2023@gmail.com");
		mail.setTo(email);
		mail.setSubject("RTHYMEWAVE: CONFIRM YOUR EMAIL");
		mail.setBody(sendMailTemplateSer.getContentForConfirm(email, "templateMail", "podcast", applicationUrl(req,"/confirm-email-podcaster/"+email)));
		mailService.enqueue(mail);
		return ResponseEntity.ok(new MessageResponse(true, "succeess", mail));
	}
	
	@PostMapping("/api/v1/email-request-artist/{email}")
	public ResponseEntity<MessageResponse> sendMailRequestArtist(@PathVariable("email") String email) {
		Email mail = new Email();
		mail.setFrom("musicstreaming2023@gmail.com");
		mail.setTo(email);
		mail.setSubject("RTHYMEWAVE: CONFIRM YOUR REQUEST");
		mail.setBody(sendMailTemplateSer.getContentForConfirm(email, "templateRoleArtist", "Artist", ""));
		mailService.enqueue(mail);
		return ResponseEntity.ok(new MessageResponse(true, "succeess", mail));
	}
	
	private String applicationUrl(HttpServletRequest request, String path) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + path;
	}
}
