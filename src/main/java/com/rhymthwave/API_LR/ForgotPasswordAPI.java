package com.rhymthwave.API_LR;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Request.DTO.ForgotPasswordDTO;
import com.rhymthwave.entity.Account;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/v1/accounts")
public class ForgotPasswordAPI {

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JavaMailSender mailSender;
	

	@PostMapping("/forgotpassword")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) throws UnsupportedEncodingException {
		String email = forgotPasswordDTO.email();
		Account account = accountDAO.findByEmail(email);

		if (account == null) {
			return ResponseEntity.ok(new MessageResponse(true, "This email does not exist"));
		}

		// Tạo đoạn code 8 ký tự
		String code = generateRandomCode();
		account.setPassword(encoder.encode(code));
		accountDAO.save(account);
		// Lưu đoạn code vào cơ sở dữ liệu (ví dụ: account.setForgotPasswordCode(code)
		// và accountDAO.save(account))

		// Gửi đoạn code vào email của người dùng
		try {
			sendPassWordEmail(email,code);
		} catch (MessagingException e) {
			return ResponseEntity.ok(new MessageResponse(true, "Failed to send email"));
		}
		return ResponseEntity.ok(new MessageResponse(true, "Code sent to your email"));
	}

	public void sendPassWordEmail(String email,String code) throws MessagingException, UnsupportedEncodingException {
		String subject = "Email ForgotPassWord";
		String senderName = "User Registration Portal Service";
		String mailContent = "<p> Hi, "+ "</p>" + "<p>This is your temporary password :" + code
				+ "" + "<br> Please change new password</p>"  ;
		MimeMessage message = mailSender.createMimeMessage();
		var messageHelper = new MimeMessageHelper(message);
		messageHelper.setFrom("musicstreaming2023@gmail.com", senderName);
		messageHelper.setTo(email);
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
	}

	private String generateRandomCode() {
		// Số ký tự bạn muốn tạo
	    int length = 8;
	    
	    // Danh sách ký tự có thể xuất hiện trong chuỗi kết quả
	    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    
	    // Sử dụng đối tượng Random để tạo mã ngẫu nhiên
	    Random random = new Random();
	    StringBuilder code = new StringBuilder(length);
	    
	    for (int i = 0; i < length; i++) {
	        // Chọn một ký tự ngẫu nhiên từ danh sách characters
	        int index = random.nextInt(characters.length());
	        char randomChar = characters.charAt(index);
	        code.append(randomChar);
	    }
	    
	    return code.toString();
	}
}
