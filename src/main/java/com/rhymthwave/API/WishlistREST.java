package com.rhymthwave.API;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.WishlistService;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.UserType;
import com.rhymthwave.entity.Wishlist;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class WishlistREST {
	private final CRUD<Wishlist, Long> crudWishlist;

	private final CRUD<Account, String> crudAccount;

	private final CRUD<Episode, Long> crudEpisode;

	private final CRUD<Recording, Long> crudRecording;

	private final WishlistService wishlistSer;

	private final GetHostByRequest host;

	@GetMapping("/api/v1/wishlist/{id}")
	public ResponseEntity<MessageResponse> findOne(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudWishlist.findOne(id)));
	}

	@GetMapping("/api/v1/my-wishlist")
	public ResponseEntity<MessageResponse> findMyWishlist(HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		UserType basic = account.getUserType().get(0);
		UserType premium = account.getUserType().get(1);
		List<Wishlist> list = wishlistSer.myWishlist(basic);
		if (premium != null) {
			list.addAll(wishlistSer.myWishlist(premium));
		}
		if (!list.isEmpty()) {
			return ResponseEntity.ok(new MessageResponse(true, "success", list));
		}
		return ResponseEntity.ok(new MessageResponse(false, "Your wishlist is empty", null));
	}

	@PostMapping("/api/v1/wishlist")
	public ResponseEntity<MessageResponse> createWishlist(HttpServletRequest req, @RequestBody Wishlist wishlist) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		UserType basic = account.getUserType().get(0);
		Episode episode = wishlist.getEpisode();
		Recording recording = wishlist.getRecording();
		UserType premium=null;
		if (basic.getWishlists().size() <= 50) {
			Wishlist wishlistData = wishlistSer.create(wishlist, basic, wishlist.getEpisode(), wishlist.getRecording());
			if (wishlistData != null) {
				return ResponseEntity.ok(new MessageResponse(true, "success", wishlistData));
			} else {
				return ResponseEntity.ok(new MessageResponse(false, "Existed", null));
			}
		} else {
			 premium = account.getUserType().size() > 1 ? account.getUserType().get(1) : null;
			if (premium != null) {
				if (wishlistSer.checkExtist(basic, episode, recording) == null
						&& wishlistSer.checkExtist(premium, episode, recording) == null) {
					if (premium.getEndDate().after(new Date())) {
						return ResponseEntity.ok(new MessageResponse(false, "Your subscription is expired!", null));
					} else {
						Wishlist wishlistData = wishlistSer.create(wishlist, premium, episode, recording);
						if (wishlistData != null) {
							return ResponseEntity.ok(new MessageResponse(true, "success", wishlistData));
						} else {
							return ResponseEntity.ok(new MessageResponse(false, "fail", null));
						}
					}
				} else {
					return ResponseEntity.ok(new MessageResponse(false, "Existed", null));
				}

			} else {
				return ResponseEntity.ok(new MessageResponse(false, "Join Premium with us <3", null));
			}
		}
	}

	@GetMapping("/api/v1/exist-my-wishlist")
	public ResponseEntity<MessageResponse> checkExist(HttpServletRequest req,
			@RequestParam("episode") Optional<Long> episodeId, @RequestParam("recording") Optional<Long> recordingId) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		Episode episode = crudEpisode.findOne(episodeId.orElse(null));
		Recording recording = crudRecording.findOne(recordingId.orElse(null));
		UserType premium = account.getUserType().size() > 1 ? account.getUserType().get(1) : null;;
		UserType basic = account.getUserType().get(0);
		if (premium!=null) {
			if ((wishlistSer.checkExtist(basic, episode, recording) != null)
					|| (wishlistSer.checkExtist(premium, episode, recording) != null)) {
				return ResponseEntity.ok(new MessageResponse(true, "No Existed", true));
			}
		} else if (wishlistSer.checkExtist(basic, episode, recording) != null) {
			return ResponseEntity.ok(new MessageResponse(true, "No Existed", true));
		}
		return ResponseEntity.ok(new MessageResponse(true, "Existed", false));
	}

	@DeleteMapping("/api/v1/wishlist/{id}")
	public ResponseEntity<MessageResponse> deleteWishlist(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudWishlist.delete(id)));
	}

	@GetMapping("/api/v1/find-wishlist")
	public ResponseEntity<MessageResponse> findWishlist(HttpServletRequest req,
			@RequestParam("episode") Optional<Long> episodeId, @RequestParam("recording") Optional<Long> recordingId) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		Episode episode = crudEpisode.findOne(episodeId.orElse(null));
		Recording recording = crudRecording.findOne(recordingId.orElse(null));
		UserType premium = account.getUserType().get(1);
		UserType basic = account.getUserType().get(0);
		Wishlist wlb = wishlistSer.checkExtist(basic, episode, recording);
		Wishlist wlp = wishlistSer.checkExtist(premium, episode, recording);

		if (wlb != null) {
			return ResponseEntity.ok(new MessageResponse(true, "success", wlb));
		} else if (wlp != null) {
			return ResponseEntity.ok(new MessageResponse(true, "success", wlp));
		}
		return ResponseEntity.ok(new MessageResponse(false, "Existed", null));
	}
}
