package com.rhymthwave.API;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Request.DTO.AccountDTO;
import com.rhymthwave.Service.AccountService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.CloudinaryService;
import com.rhymthwave.Service.ImageService;
import com.rhymthwave.Service.Implement.AccountServiceImpl;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Image;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class AccountREST {

	private final CRUD<Account, String> crudAccount;

	private final AccountServiceImpl accountServiceImpl;

	private final AccountService accountService;

	private final CRUD<Image, String> crudImage;

	private final CloudinaryService cloudinary;

	private final ImageService imgSer;

	private final GetHostByRequest host;

	@GetMapping("/api/v1/account")
	public ResponseEntity<MessageResponse> findAccountByJWT(HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAccount.findOne(owner)));
	}

	@GetMapping("/api/v1/account/{id}")
	public ResponseEntity<MessageResponse> findAccount(@PathVariable("id") String id) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAccount.findOne(id)));
	}

	@Transactional
	@GetMapping("/api/v1/search/{keyword}")
	public ResponseEntity<MessageResponse> search(@PathVariable("keyword") String keyword) {
		return ResponseEntity.ok(new MessageResponse(true, "success", accountService.search(keyword)));
	}

	@Transactional
	@GetMapping("/api/v1/search/gr/{keyword}")
	public ResponseEntity<MessageResponse> searchGr(@PathVariable("keyword") String keyword) {
		return ResponseEntity.ok(new MessageResponse(true, "success", accountService.searchGr(keyword)));
	}

	@PutMapping(value = "/api/v1/account")
	public ResponseEntity<MessageResponse> updateProfile(@RequestBody Account account) {
		return ResponseEntity.ok(new MessageResponse(true, "succeess", crudAccount.update(account)));
	}

	@PutMapping(value = "/api/v1/account-image", consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> updateImageAccount(HttpServletRequest req,
			@PathParam("avatar") MultipartFile avatar) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		Image imgOld = account.getImage();
		if (avatar != null) {
			Map<String, Object> respAvatar = cloudinary.Upload(avatar, "ProfilePicture", account.getUsername());
			Image imgAvatar = imgSer.getEntity(respAvatar);
			crudImage.create(imgAvatar);
			account.setImage(imgAvatar);
			cloudinary.deleteFile(imgOld.getPublicId());
		}
		return ResponseEntity.ok(new MessageResponse(true, "succeess", crudAccount.update(account)));
	}

//	@PutMapping("/api/v1/account/updateprofile")
//    public ResponseEntity<MessageResponse> updateProfile(@RequestBody AccountDTO accountRequest,HttpServletRequest req) {
//		String owner = host.getEmailByRequest(req);
//		Account account = accountService.findOne(owner);
//		accountService.update(accountRequest,req,account);	
//        return ResponseEntity.ok(new MessageResponse(true, "Profile updated successfully"));
//    }
	@PutMapping("/api/v1/account/logout")
	public ResponseEntity<MessageResponse> logout(HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		Account account = accountServiceImpl.findOne(owner);
		accountServiceImpl.logout(req, account);
		return ResponseEntity.ok(new MessageResponse(true, "Logout account successfully"));
	}
}