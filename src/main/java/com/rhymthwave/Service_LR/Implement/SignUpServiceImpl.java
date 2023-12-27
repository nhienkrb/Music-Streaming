package com.rhymthwave.Service_LR.Implement;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.DAO.AuthorDAO;
import com.rhymthwave.DAO.RoleDAO;
import com.rhymthwave.DAO.UserTypeDAO;
import com.rhymthwave.DTO.UserAlreadyExistsException;
import com.rhymthwave.Request.DTO.SignUpDTO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.ImageService;
import com.rhymthwave.Service.SubscriptionService;
import com.rhymthwave.Service_LR.ISignUpService;
//import com.rhymthwave.Utilities.JWT.VerificationTokenRepository;
//import com.rhymthwave.Utilities.JWT.VerifyToken;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Author;
import com.rhymthwave.entity.Image;
import com.rhymthwave.entity.Role;
import com.rhymthwave.entity.Subscription;
import com.rhymthwave.entity.UserType;
import com.rhymthwave.entity.TypeEnum.EROLE;
import com.rhymthwave.entity.TypeEnum.EUserType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements ISignUpService {

private final AccountDAO dao;

	
	private final RoleDAO roleDAO;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private SubscriptionService subscriptionService;

	@Autowired
	private AuthorDAO authorDAO;
	
	private final UserTypeDAO userTypeDAO;
	
	private final CRUD<Image, String> crudImage;
	
	private static final int EXPIRATION_TIME = 15;

//	@Autowired
//	private VerificationTokenRepository tokenRepository;

	@Override
	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public Account signUp(@RequestBody SignUpDTO request) {
		Account account = dao.findByEmail(request.email());
		if (account != null) {
			return null;
		}
		Subscription subscription = subscriptionService.getSubByName("BASIC");
		Image image = crudImage.findOne("1");
		Account newAccount = new Account();
		newAccount.setEmail(request.email());
		newAccount.setUsername(request.username());
		newAccount.setPassword(encoder.encode(request.password()));
		newAccount.setBirthday(request.birthday());
		newAccount.setCountry(request.country());
		newAccount.setGender(request.gender());
        newAccount.setVerify(request.birthday() == null ? true : false);
		newAccount.setRemainingVerification(3);
		newAccount.setBlocked(false);
		newAccount.setImage(image);
		dao.save(newAccount);
		Author newAuthor = new Author();
		newAuthor.setRole(roleDAO.findByRole(EROLE.USER));
		newAuthor.setAccount(newAccount);
		authorDAO.save(newAuthor);
		UserType newUserType = new UserType();
		newUserType.setAccount(newAccount);
		newUserType.setNameType("BASIC");
		newUserType.setStartDate(getTimeNow());
		newUserType.setSubscription(subscription);
		userTypeDAO.save(newUserType);
		
		return newAccount ;
	}


	public ResponseEntity<String> verifyEmail(Account account) {
		if (account == null) {
			return ResponseEntity.badRequest().body("Invalid verification token");
		}

		Calendar calendar = Calendar.getInstance();
		int remainingVerification = account.getRemainingVerification();
		
		if (account.getVerificationCodeExpires().getTime() - calendar.getTime().getTime() <= 0) {
			remainingVerification--;
			account.setRemainingVerification(remainingVerification);
			account.setVerificationCodeExpires(getTokenExpirationTime());
			dao.save(account);
			return ResponseEntity.badRequest().body("The token has expired, please click on the verification link again");
		}

		if (remainingVerification == 0) {
			account.setVerificationCode(null);
			account.setVerificationCodeExpires(null);
			account.setBlocked(true);
			dao.save(account);
			return ResponseEntity.badRequest().body("Email is blocked");
		}

		account.setVerificationCode(null);
		account.setVerificationCodeExpires(null);
		account.setVerify(true);
		dao.save(account);
		return ResponseEntity.ok("Verified successfully");
		
	}
	
	
	public Date getTimeNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		return new Date(calendar.getTime().getTime());
	}
	
	public Date getTokenExpirationTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
		return new Date(calendar.getTime().getTime());
	}
}
