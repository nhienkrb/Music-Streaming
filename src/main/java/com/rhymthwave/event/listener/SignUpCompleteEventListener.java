package com.rhymthwave.event.listener;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.Request.DTO.NewDTO;
import com.rhymthwave.ServiceAdmin.INotification;
import com.rhymthwave.Service_LR.Implement.SignUpServiceImpl;
import com.rhymthwave.entity.Account;
import com.rhymthwave.event.SignUpCompleteEvent;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class SignUpCompleteEventListener implements ApplicationListener<SignUpCompleteEvent> {
	
	private static final int EXPIRATION_TIME = 15;
	
	@Autowired
	private SignUpServiceImpl signUpServiceImpl;
	
	
	@Autowired
	private JavaMailSender mailSender;
	
	private Account account;
	@Autowired
	private AccountDAO accountDAO;
	
	@Qualifier("sendNotificationOfNews")
	private final INotification<NewDTO> notification;
	
	@Override
	public void onApplicationEvent(SignUpCompleteEvent event) {
		account = event.getAccount();
		String verificationToken = UUID.randomUUID().toString();
		account.setVerificationCode(verificationToken);
		account.setVerificationCodeExpires(getTokenExpirationTime());
		accountDAO.save(account);
		
		String url = event.getApplicationUrl()+"/api/v1/accounts/verifyEmail?token="+verificationToken;
		
		try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
		
		log.info("Click the link to verify your SignUp : {}",url);
		
	}
	
	public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
		notification.sendEmailComfirmUser(url,account.getEmail());
//		String subject = "Email Verification";
//        String senderName = "User Registration Portal Service";
//        String mailContent = "<p> Hi, "+ account.getUsername()+ ", </p>"+
//                "<p>Thank you for registering with us,"+"" +
//                "Please, follow the link below to complete your registration.</p>"+
//                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
//                "<p> Thank you <br> Users Registration Portal Service";
//        MimeMessage message = mailSender.createMimeMessage();
//        var messageHelper = new MimeMessageHelper(message);
//        messageHelper.setFrom("nguyenkhoalolm@gmail.com", senderName);
//        messageHelper.setTo(account.getEmail());
//        messageHelper.setSubject(subject);
//        messageHelper.setText(mailContent, true);
//        mailSender.send(message);
    }


	public Date getTokenExpirationTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
		return new Date(calendar.getTime().getTime());
	}
}
