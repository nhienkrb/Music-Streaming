package com.rhymthwave.API;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.CloudinaryService;
import com.rhymthwave.Service.ImageService;
import com.rhymthwave.Service.PlaylistService;
import com.rhymthwave.Service.RecordService;
import com.rhymthwave.Service.SubscriptionService;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Image;
import com.rhymthwave.entity.Playlist;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.UserType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class PlaylistREST {
	private final PlaylistService playlistSer;

	private final CRUD<Playlist, Long> crudPlaylist;

	private final CRUD<Image, String> crudImage;

	private final CRUD<Account, String> crudAccount;

	private final CRUD<UserType, Long> crudUserType;

	private final SubscriptionService subSer;
	
	private final RecordService recordSer;

	private final ImageService imgSer;

	private final CloudinaryService cloudinary;
		
	private final GetHostByRequest host;

	@GetMapping("/api/v1/playlist")
	public ResponseEntity<MessageResponse> getAllPlaylist() {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudPlaylist.findAll()));
	}

	@GetMapping("/api/v1/playlist/{id}")
	public ResponseEntity<MessageResponse> getPlaylist(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudPlaylist.findOne(id)));
	}

	@GetMapping("/api/v1/my-playlist/{email}")
	public ResponseEntity<MessageResponse> getMyPlaylist(@PathVariable("email") String email) {
		Account acc = crudAccount.findOne(email);
		List<Playlist> list = new ArrayList<>();
		acc.getUserType().forEach(item -> list.addAll(playlistSer.findMyPlaylist(item)));
		return ResponseEntity.ok(new MessageResponse(true, "success", list));
	}

	@PostMapping("/api/v1/playlist")
	public ResponseEntity<MessageResponse> createPlaylist(@RequestBody Playlist playlist, HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		if (account.getUserType().get(0).getPlaylists().toArray().length < subSer.getSubByName("BASIC").getPlaylistAllow()) {
			playlist.setUsertype(account.getUserType().get(0));
			return ResponseEntity.ok(new MessageResponse(true, "success", crudPlaylist.create(playlist)));
		} else {
			if (account.getUserType().toArray().length > 1) {
				if (account.getUserType().get(1).getEndDate().after(new Date())) {
					return ResponseEntity.ok(new MessageResponse(false, "success", "The premium account has expired."));
				} else {
					playlist.setUsertype(account.getUserType().get(1));
					return ResponseEntity.ok(new MessageResponse(true, "success", crudPlaylist.create(playlist)));
				}
			} else {
				return ResponseEntity
						.ok(new MessageResponse(false, "success", "Please upgrade your account package to premium."));
			}

		}
	}

	@PutMapping("/api/v1/playlist")
	public ResponseEntity<MessageResponse> updatePlaylist(@RequestBody Playlist playlist) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudPlaylist.update(playlist)));
	}

	@PutMapping(value = "/api/v1/playlist-image", consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> updateImagePlaylist(@RequestParam("id") Long id, HttpServletRequest req,
			@PathParam("coverImg") MultipartFile coverImg) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		Playlist playlist = crudPlaylist.findOne(id);
		if (coverImg != null) {
			Map<?, ?> respImg = cloudinary.Upload(coverImg, "ImagePlaylist", account.getUsername());
			Image image = imgSer.getEntity(respImg);
			crudImage.create(image);
			playlist.setImage(image);
		}

		return ResponseEntity.ok(new MessageResponse(true, "success", crudPlaylist.update(playlist)));
	}

	@DeleteMapping("/api/v1/playlist/{id}")
	public ResponseEntity<MessageResponse> deletePlaylist(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudPlaylist.delete(id)));
	}

	@PostMapping("/api/v1/similar-playlist")
	public ResponseEntity<MessageResponse> createSimilarPlaylist(@RequestParam("email") String email,@RequestParam("genre") Optional<String> genre,
			 @RequestParam("culture") Optional<String> culture, @RequestParam("instrument") Optional<String> instrument,
			 @RequestParam("mood") Optional<String> mood, @RequestParam("songstyle") Optional<String> songstyle, 
			 @RequestParam("versions") Optional<String> versions){
		Account account = crudAccount.findOne(email);
		Playlist playlist = new Playlist();
		List<Recording> listRecord = recordSer.findListRandomFavorite(genre.orElse("''"), culture.orElse(" "), instrument.orElse(" "), mood.orElse(" "), songstyle.orElse(" "), versions.orElse(" "));
		if (account.getUserType().get(0).getPlaylists().toArray().length < subSer.getSubByName("BASIC")
				.getPlaylistAllow()) {
			playlist.setUsertype(account.getUserType().get(0));
			Playlist playlistData = crudPlaylist.create(playlist);
			return ResponseEntity.ok(new MessageResponse(true, "success", playlistSer.createSimilarPlaylist(playlistData, listRecord)));
		} else {
			if (account.getUserType().toArray().length > 1) {
				if (account.getUserType().get(1).getEndDate().before(new Date())) {
					return ResponseEntity.ok(new MessageResponse(false, "success", "The premium account has expired."));
				} else {
					playlist.setUsertype(account.getUserType().get(1));
					Playlist playlistData = crudPlaylist.create(playlist);
					return ResponseEntity.ok(new MessageResponse(true, "success",  playlistSer.createSimilarPlaylist(playlistData, listRecord)));
				}
			} else {
				return ResponseEntity
						.ok(new MessageResponse(false, "success", "Please upgrade your account package to premium."));
			}

		}
	}
	
	@GetMapping("/api/v1/top-playlist-new")
	public ResponseEntity<MessageResponse> findTopNewPlaylist() {
		return ResponseEntity.ok(new MessageResponse(true, "success", playlistSer.top50PlaylistLatest(true)));
	}
	
	@GetMapping("/api/v1/top-playlist-recent-listen")
	public ResponseEntity<MessageResponse> findTopRecentPlaylist(@RequestParam("genre") Optional<List<String>> genre,
			@RequestParam("culture") Optional<String> culture, @RequestParam("instrument") Optional<String> instrument,
			@RequestParam("mood") Optional<String> mood, @RequestParam("songstyle") Optional<String> songstyle, 
			@RequestParam("versions") Optional<String> versions) {
		return ResponseEntity.ok(new MessageResponse(true, "success", playlistSer.top50PlaylistRecentListen(true, genre, culture, instrument, mood, songstyle, versions)));
	}
}
