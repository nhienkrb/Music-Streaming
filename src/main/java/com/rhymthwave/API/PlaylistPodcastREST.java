package com.rhymthwave.API;

import java.util.Date;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Playlist_Podcast;
import com.rhymthwave.entity.UserType;
import com.rhymthwave.entity.Wishlist;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class PlaylistPodcastREST {
	private final CRUD<Account, String> crudAccount;

	private final CRUD<Playlist_Podcast, Long> crudPlaylistPodcast;

	private final GetHostByRequest host;

	@PostMapping("/api/v1/playlist-episode")
	public ResponseEntity<MessageResponse> additionEpisodeIntoPlaylist(HttpServletRequest req,
			@RequestBody Playlist_Podcast playlistPodcast, @RequestParam("quantity") Optional<Integer> quantity) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		UserType basic = account.getUserType().get(0);
		UserType premium = account.getUserType().size() > 1 ? account.getUserType().get(1) : null;
		Integer lengthPlaylist = quantity.orElse(0);
		if (lengthPlaylist <= basic.getSubscription().getNip()) {
			return ResponseEntity.ok(new MessageResponse(true, "success", crudPlaylistPodcast.create(playlistPodcast)));
		} else {
			if (premium != null) {
				if (premium.getEndDate().after(new Date())) {
					return ResponseEntity.ok(new MessageResponse(false, "Your subscription is expired!", null));
				} else {
					return ResponseEntity.ok(new MessageResponse(true, "success", crudPlaylistPodcast.create(playlistPodcast)));
				}
			} else {
				return ResponseEntity.ok(new MessageResponse(false, "Join Premium with us <3", null));
			}
		}

	}

	@DeleteMapping("/api/v1/playlist-episode/{id}")
	public ResponseEntity<MessageResponse> deleteEpisodeFromPlaylist(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudPlaylistPodcast.delete(id)));
	}
}
