package com.rhymthwave.API;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.AlbumService;
import com.rhymthwave.Service.ArtistService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.CloudinaryService;
import com.rhymthwave.Service.ImageService;
import com.rhymthwave.Service.SongService;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Album;
import com.rhymthwave.entity.Image;
import com.rhymthwave.entity.Song;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin("*")
@RequiredArgsConstructor
public class AlbumREST {

	private final ArtistService artistSer;

	private final AlbumService albumSer;

	private final CRUD<Album, Long> crudAlbum;

	private final CloudinaryService cloudinary;

	private final CRUD<Image, String> crudImage;

	private final ImageService imgSer;

	private final GetHostByRequest host;

	private final CRUD<Account, String> crudAccount;

	@GetMapping("/api/v1/album")
	public ResponseEntity<MessageResponse> getAllAlbum() {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAlbum.findAll()));
	}

	@GetMapping("/api/v1/album/{id}")
	public ResponseEntity<MessageResponse> getAlbumByID(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAlbum.findOne(id)));
	}

	@PostMapping(value = "/api/v1/album", consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> createAlbum(@ModelAttribute Album album, HttpServletRequest req,
			@RequestParam("coverImg") MultipartFile coverImg) {

		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		if (coverImg != null) {
			Map<String, Object> respImg = cloudinary.Upload(coverImg, "CoverImage",
					account.getArtist().getArtistName());
			Image cover = imgSer.getEntity(respImg);
			crudImage.create(cover);
			album.setImage(cover);
		}
		album.setArtist(artistSer.findByEmail(owner));
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAlbum.create(album)));
	}

	@PutMapping(value = "/api/v1/album")
	public ResponseEntity<MessageResponse> updateAlbum(@RequestBody Album album) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAlbum.update(album)));
	}
	
	@PutMapping(value = "/api/v1/album-image/{id}", consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> updateAlbumImage(@PathVariable("id") Long id,HttpServletRequest req,@RequestParam("coverImg") MultipartFile coverImg) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		Album album = crudAlbum.findOne(id);
		if (coverImg != null) {
			Map<String, Object> respImg = cloudinary.Upload(coverImg, "CoverImage",account.getArtist().getArtistName());
			Image cover = imgSer.getEntity(respImg);
			crudImage.create(cover);
			album.setImage(cover);
		}
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAlbum.update(album)));
	}

	@GetMapping("/api/v1/album/up-coming")
	public ResponseEntity<MessageResponse> albumUpcoming(HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		return ResponseEntity.ok(new MessageResponse(true, "success", albumSer.findAlbumNotRecord(owner)));
	}
	
	@DeleteMapping("/api/v1/album/{id}")
	public ResponseEntity<MessageResponse> deleteAlbum(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAlbum.delete(id)));
	}

	//Get All album if your account is artist
	@GetMapping("/api/v1/album-artist-released")
	public ResponseEntity<MessageResponse> albumReleasedByArtist(HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		return ResponseEntity.ok(new MessageResponse(true, "success",albumSer.findAlbumByArtist(account.getArtist().getArtistId())));
	}
	
	//get all album if your account is user
	@GetMapping("/api/v1/album-artist")
	public ResponseEntity<MessageResponse> albumByArtist(@RequestParam("artistId") Long artistId) {
		return ResponseEntity.ok(new MessageResponse(true, "success",albumSer.findAlbumReleasedByArtist(artistId)));
	}
	
	@GetMapping("/api/v1/album-pl/{keyword}")
	public ResponseEntity<MessageResponse> getAlbumByName(@PathVariable("keyword") String keyword) {
		return ResponseEntity.ok(new MessageResponse(true, "success", albumSer.findByName(keyword)));
	}
	
	@GetMapping("/api/v1/top-album-latest")
	public ResponseEntity<MessageResponse> findTopAlbumLatest() {
		return ResponseEntity.ok(new MessageResponse(true, "success", albumSer.top50AlbumLatest()));
	}
	
	@GetMapping("/api/v1/top-album-popular")
	public ResponseEntity<MessageResponse> findTopAlbumPopular() {
		return ResponseEntity.ok(new MessageResponse(true, "success", albumSer.top50AlbumListenest()));
	}
}
