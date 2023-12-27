package com.rhymthwave.API;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.ArtistService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.CloudinaryService;
import com.rhymthwave.Service.ImageService;
import com.rhymthwave.Service.RecordService;
import com.rhymthwave.Service.Implement.CloudinaryServiceImpl;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Artist;
import com.rhymthwave.entity.Image;
import com.rhymthwave.entity.Recording;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ArtistREST {

	private final CRUD<Artist, Long> crud;

	private final CloudinaryService cloudinary;

	private final CRUD<Image, String> crudImg;

	private final ImageService imgSer;

	private final CRUD<Account, String> crudAccount;
	
	private final ArtistService artistSer;
	
	private final GetHostByRequest host;
	

	@GetMapping("/api/v1/artist")
	public ResponseEntity<MessageResponse> getAll() {
		return ResponseEntity.ok(new MessageResponse(true, "succeess", crud.findAll()));
	}

	@GetMapping("/api/v1/artist/{id}")
	public ResponseEntity<MessageResponse> getById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new MessageResponse(true, "succeess", crud.findOne(id)));
	}
	
	@GetMapping("/api/v1/artist-verified")
	public ResponseEntity<MessageResponse> getListArtistVerified() {
		return ResponseEntity.ok(new MessageResponse(true, "succeess", artistSer.findIsVerify(true)));
	}

	@GetMapping("/api/v1/artist-none-verified")
	public ResponseEntity<MessageResponse> getListArtistNoneVerified() {
		return ResponseEntity.ok(new MessageResponse(true, "succeess", artistSer.findIsVerify(false)));
	}
	
	@PostMapping(value="/api/v1/artist",consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> creatArtist(@ModelAttribute Artist artist,HttpServletRequest req,
			@PathParam("avatar") MultipartFile avatar, @PathParam("background") MultipartFile background) {
		String owner =host.getEmailByRequest(req);
		
		if(avatar !=null) {
			Map<String,Object> respAvatar = cloudinary.UploadResizeImage(avatar,"ProfilePicture",artist.getArtistName(),512,512);
			Image imgAvatar = imgSer.getEntity(respAvatar);
			crudImg.create(imgAvatar);
			artist.setImagesProfile(imgAvatar);
		}
		if(background !=null) {
			Map<String,Object> respBg = cloudinary.UploadResizeImage(background,"Background",artist.getArtistName(),1500,500);
			Image imgBackground = imgSer.getEntity(respBg);
			crudImg.create(imgBackground);
			artist.setBackgroundImage(imgBackground);
		}
		
		artist.setAccount(crudAccount.findOne(owner));
		return ResponseEntity.ok(new MessageResponse(true, "succeess", crud.create(artist)));
	}
	
	@PutMapping(value="/api/v1/artist")
	public ResponseEntity<MessageResponse> updateProfile(@RequestBody Artist artist){
		return ResponseEntity.ok(new MessageResponse(true, "succeess", crud.update(artist)));
	}
		
	@PutMapping(value="/api/v1/artist-image",consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> updateImageArtist(HttpServletRequest req, @PathParam("gallery") MultipartFile[] gallery,
			@PathParam("avatar") MultipartFile avatar, @PathParam("background") MultipartFile background) {
		String owner =host.getEmailByRequest(req);
		Artist artist =artistSer.findByEmail(owner);
		if(avatar !=null) {
			Map<String,Object> respAvatar = cloudinary.UploadResizeImage(avatar,"ProfilePicture",artist.getArtistName(),512,512);
			Image imgAvatar = imgSer.getEntity(respAvatar);
			crudImg.create(imgAvatar);
			artist.setImagesProfile(imgAvatar);
		}
		if(background !=null) {
			Map<String,Object> respBg = cloudinary.UploadResizeImage(background,"Background",artist.getArtistName(),1500,500);
			Image imgBackground = imgSer.getEntity(respBg);
			crudImg.create(imgBackground);
			artist.setBackgroundImage(imgBackground);
		}
		
		if(gallery !=null) {
			Map<?,?> respGallery = cloudinary.uploadMultipleFiles(gallery,"ImageGallery",artist.getArtistName());
			String[] urls = (String[]) respGallery.get("uploadedUrls");
			String[] publicIds= (String[]) respGallery.get("uploadedPublicid");
			
			if(artist.getImagesGallery()!=null && urls != null) {
			String[] newUrls = new String[artist.getImagesGallery().length+urls.length];
			System.arraycopy(urls, 0, newUrls, 0, urls.length);
		    System.arraycopy(artist.getImagesGallery(), 0, newUrls, urls.length, artist.getImagesGallery().length);
			artist.setImagesGallery(newUrls);
			}else {
				artist.setImagesGallery(urls);
			}

			if(artist.getPublicIdImageGallery()!=null && publicIds != null) {
			String[] newPublicId = new String[artist.getImagesGallery().length+publicIds.length];
			System.arraycopy(publicIds, 0, newPublicId, 0, publicIds.length);
		    System.arraycopy(artist.getImagesGallery(), 0, newPublicId, publicIds.length, artist.getImagesGallery().length);
			artist.setPublicIdImageGallery(newPublicId);
			}else {
				artist.setPublicIdImageGallery(publicIds);
			}
		}
		return ResponseEntity.ok(new MessageResponse(true, "succeess", crud.update(artist)));
	}
	
	@GetMapping("/api/v1/profile")
	public ResponseEntity<MessageResponse> findProfile(HttpServletRequest req){
		String owner = host.getEmailByRequest(req);
		Artist artist = artistSer.findByEmail(owner);
		return ResponseEntity.ok(new MessageResponse(true,"success", artist));
	}
	
	@GetMapping("/api/v1/find-artist/{email}")
	public ResponseEntity<MessageResponse> findArtist(@PathVariable("email") String email){
		return ResponseEntity.ok(new MessageResponse(true,"success",artistSer.findByEmail(email)));
	}
	
	
	@GetMapping("/api/v1/confirm-account-artist")
	public ResponseEntity<MessageResponse> findAccount(HttpServletRequest req){
		String owner = host.getEmailByRequest(req);
		return ResponseEntity.ok(new MessageResponse(true,"success",crudAccount.findOne(owner)));
	}
	
	@GetMapping("/api/v1/search-artist/{artistName}")
	public ResponseEntity<MessageResponse> findListArtistName(HttpServletRequest req, @PathVariable("artistName") String artistName){
		String owner = host.getEmailByRequest(req);
		Artist artist = artistSer.findByEmail(owner);
		List<Artist> list = artistSer.findAllArtistNameisVerify(artist.getArtistId(), "%"+artistName+"%");
		return ResponseEntity.ok(new MessageResponse(true,"success",list));
	}
	
	@GetMapping("/api/v1/artist-pl/{keyword}")
	public ResponseEntity<MessageResponse> getArtByName(@PathVariable("keyword") String keyword) {
		return ResponseEntity.ok(new MessageResponse(true, "succeess", artistSer.findByName(keyword)));
	}
	
	@GetMapping("/api/v1/top-artist-listen")
	public ResponseEntity<MessageResponse> findTopArtistByListen(@RequestParam("country") String country) {
		return ResponseEntity.ok(new MessageResponse(true, "succeess", artistSer.top50ArtistByListener(country, true, true)));
	}
	
	@GetMapping("/api/v1/top-artist-follow")
	public ResponseEntity<MessageResponse> findTopArtistByFollow(@RequestParam("country") String country) {
		return ResponseEntity.ok(new MessageResponse(true, "succeess", artistSer.top50ArtistByFollow(2,country,true)));
	}
	
	@GetMapping("/api/v1/top3-artist")
	public ResponseEntity<MessageResponse> top3ArtistByEntityListened(){
		return ResponseEntity.ok(new MessageResponse(true,"success",artistSer.top3ArtistByByListener()));
	}
}
