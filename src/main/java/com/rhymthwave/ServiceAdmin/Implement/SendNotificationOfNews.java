package com.rhymthwave.ServiceAdmin.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.Request.DTO.NewDTO;
import com.rhymthwave.Service.EmailService;
import com.rhymthwave.ServiceAdmin.INotification;
import com.rhymthwave.Utilities.SendMailTemplateService;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Email;

import lombok.extern.slf4j.Slf4j;

@Service("sendNotificationOfNews")
@Primary
@Slf4j
public class SendNotificationOfNews implements INotification<NewDTO> {

	@Autowired
	private EmailService mailService;

	@Autowired
	private SendMailTemplateService sendMailTemplateSer;

	@Autowired
	private AccountDAO accountDAO;


	@Override
	public void sendNotification(NewDTO noti,String urlImg) {

		List<Account> listAccounts = accountDAO.findAllAccountRole(noti.role());
		for (Account account : listAccounts) {
			log.info(">>>>>>>>> Email: {} accout with role: {}",account.getEmail(),noti.role());
			Email email = new Email();
			email.setFrom("musicstreaming2023@gmail.com");
			email.setTo(account.getEmail());
			email.setSubject(noti.summary().toUpperCase());
			email.setBody(sendMailTemplateSer.getContentForNews(noti.title(), "templateNews", noti.summary(),
					urlImg));
			log.info(">>>>>>>>> Email dang hoat dong");
			mailService.enqueue(email);
		}

	}
	
	@Override
	public void sendEmailBan(String email,String message) {
		Account accounts = accountDAO.findByEmail(email);
		Email emails = new Email();
		emails.setFrom("musicstreaming2023@gmail.com");
		emails.setTo(email);
		emails.setSubject(message);
		emails.setBody(sendMailTemplateSer.getContentForReport("templateReportAdmin"));
		mailService.enqueue(emails);
	}
	@Override
	public void sendEmailWarring(String email) {
		Account accounts = accountDAO.findByEmail(email);
		Email emails = new Email();
		emails.setFrom("musicstreaming2023@gmail.com");
		emails.setTo(email);
		emails.setSubject("Warring Account");
		emails.setBody(sendMailTemplateSer.getContentForReport("templateWarring"));
		mailService.enqueue(emails);
	}
	@Override
	public void sendEmailComfirmUser(String url,String email) {
		Account accounts = accountDAO.findByEmail(email);
		Email emails = new Email();
		emails.setFrom("musicstreaming2023@gmail.com");
		emails.setTo(email);
		emails.setSubject("Email comfirm account");
		emails.setBody(sendMailTemplateSer.getContentForConfirmAccount("templateComfirmUserSuccess",url,email));
		mailService.enqueue(emails);
	}
}
