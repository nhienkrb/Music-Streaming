package com.rhymthwave.Service.Implement;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.Request.DTO.AccountDTO;
import com.rhymthwave.Request.DTO.ChangePasswordDTO;
import com.rhymthwave.Service.AccountService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

	private final AccountDAO dao;

	private final GetHostByRequest getHostByRequest;

		
	private final PasswordEncoder encoder;
	
	@Override
	public Account create(Account entity) {
		return null;
	}

	@Override
	public Account update(Account entity) {

		return dao.save(entity);
	}
	
	@Override
	public Boolean delete(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account findOne(String email) {
		Optional<Account> account = dao.findById(email);
		if (account.isEmpty()) {
			return null;
		}

		return account.get();
	}

	@Override
	public List<Account> findAll() {
		return dao.findAll();
	}

	@Override
	public Account findAdminByEmail(HttpServletRequest request) {
		String email = getHostByRequest.getEmailByRequest(request);
		Account admin = findOne(email);
		return admin;
	}

	public Account update(AccountDTO accountRequest, HttpServletRequest req, Account account) {
		account.setUsername(accountRequest.newusername());
		account.setGender(accountRequest.newgender());
		account.setBirthday(accountRequest.newbirthday());
		account.setCountry(accountRequest.newcountry());
		return dao.save(account);
	}

	public Account changePass(ChangePasswordDTO changepasswordDTO, HttpServletRequest req, Account account) {
		String passwordCurrent = changepasswordDTO.passwordCurrent();
		if (!encoder.matches(passwordCurrent, account.getPassword())) {
			return null;
		}

		// Kiểm tra mật khẩu mới và xác nhận mật khẩu mới
		String newPassword = changepasswordDTO.newpass();
		String confirmPassword = changepasswordDTO.confirmpass();

		if (!newPassword.equals(confirmPassword)) {
			return null;

		}

		String encodedNewPassword = encoder.encode(newPassword);
		account.setPassword(encodedNewPassword);

		return dao.save(account);
		// TODO Auto-generated method stub
	}

	public Account logout(HttpServletRequest req, Account account) {
		account.setRefreshToken(null);
		return dao.save(account);
		
	}

	public Account findAccountByUsername(String username) {
		Account account = dao.findByUsername(username);
		return account;
	}

	@Override
	public List<Object> search(String keyword) {
		return dao.search(keyword);
	}

	@Override
	public List<Object> searchGr(String keyword) {
		return dao.searchGr(keyword);
	}

}
