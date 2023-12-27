package com.rhymthwave.API_Admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.ServiceAdmin.IArtistService;
import com.rhymthwave.entity.Artist;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/artist")
@RequiredArgsConstructor
public class API_Artist {
	
	private final IArtistService artistService;
	
	
	@GetMapping("/{email}")
	public ResponseEntity<?> getOneArtistByEmail(@PathVariable("email") String email) {

		Artist artist = artistService.getOneArtistByEmail(email);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", artist));
	}
	
	@GetMapping("/{idAccount}/statistic")
	public ResponseEntity<?> countStatistic(@PathVariable("idAccount") String idAccount) {
		Object count = artistService.TotalAlbumAndSong(idAccount);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", count));
	}

	@GetMapping("/{idAccount}/sumListened")
	public ResponseEntity<?> sumListened(@PathVariable("idAccount") String idAccount) {
		Long sumListened = artistService.sumListenedArtist(idAccount);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", sumListened));
	}

	@GetMapping("/{idAccount}/{idRole}/follower")
	public ResponseEntity<?> followerArtist(@PathVariable("idAccount") String idAccount,@PathVariable("idRole") Integer idRole) {
		int followerArtist = artistService.followerArtist(idRole,idAccount);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", followerArtist));
	}

}
