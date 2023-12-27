package com.rhymthwave.API;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.rhymthwave.Service.PodcastService;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Image;
import com.rhymthwave.entity.Podcast;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class PodcastREST {

	private final CRUD<Podcast, Long> crudPobcast;

	private final CRUD<Image, String> crudImage;

	private final CloudinaryService cloudinarySer;

	private final ImageService imageSer;

	private final PodcastService podcastSer;

	private final CRUD<Account, String> crudAccount;

	private final GetHostByRequest host;

	@GetMapping("/api/v1/podcast/{id}")
	public ResponseEntity<MessageResponse> findPobcast(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudPobcast.findOne(id)));
	}
	
	@GetMapping("/api/v1/podcast")
	public ResponseEntity<MessageResponse> findAllPobcast() {
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudPobcast.findAll()));
	}

	@GetMapping("/api/v1/my-podcast")
	public ResponseEntity<MessageResponse> findMyPobcast(HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		return ResponseEntity.ok(new MessageResponse(true, "successs", podcastSer.findMyPodcast(owner)));
	}
	
	@PostMapping(value = "/api/v1/podcast", consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> createPobcast(@ModelAttribute Podcast podcast, HttpServletRequest req,
														@PathParam("coverImg") MultipartFile coverImg) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		if (coverImg != null) {
			Map<?, ?> respImage = cloudinarySer.UploadResizeImage(coverImg, "ImagePobcast",account.getUsername(), 250,250);
			Image image = imageSer.getEntity(respImage);
			podcast.setImage(crudImage.create(image));
		}
		podcast.setAuthorName(account.getUsername());
		podcast.setAccount(account);
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudPobcast.create(podcast)));
	}

	@PutMapping(value="/api/v1/podcast")
	public ResponseEntity<MessageResponse> updateImage(@RequestBody Podcast podcast){
		System.out.println(podcast.toString());
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudPobcast.update(podcast)));
	}
	
	@PutMapping(value="/api/v1/podcast-image/{podcastId}",consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> updateImage(@PathVariable("podcastId") Long podcastId,HttpServletRequest req,@PathParam("coverImg") MultipartFile coverImg){
		Podcast podcast = crudPobcast.findOne(podcastId);
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		if(coverImg != null) {
			Map<?,?> respImage = cloudinarySer.UploadResizeImage(coverImg,"ImagePobcast",account.getUsername(), 250, 250);
			Image image = imageSer.getEntity(respImage);
			podcast.setImage(crudImage.create(image));
			podcast.setImage(image);
		}
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudPobcast.update(podcast)));
	}
	
	@DeleteMapping(value="/api/v1/podcast/{id}")
	public ResponseEntity<MessageResponse> deletePodcast(@PathVariable("id") Long id){
		Podcast podcast = crudPobcast.findOne(id);
		cloudinarySer.deleteFile(podcast.getImage().getPublicId());
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudPobcast.delete(id)));
	}
	
	@GetMapping(value="/api/v1/top-new-podcast")
	public ResponseEntity<MessageResponse> findTopNewPodcast(@RequestParam("country") Optional<String> country){
		return ResponseEntity.ok(new MessageResponse(true, "successs", podcastSer.top50NewPodcast(country)));
	}
	
	@GetMapping(value="/api/v1/top-podcast-popular")
	public ResponseEntity<MessageResponse> findTopPodcastPopular(@RequestParam("country") Optional<String> country){
		return ResponseEntity.ok(new MessageResponse(true, "successs", podcastSer.top50PodcastPopular(country)));
	}
	
	@GetMapping("/api/v1/profile-podcast")
	public ResponseEntity<MessageResponse> profilePobcast(HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		return ResponseEntity.ok(new MessageResponse(true, "successs", podcastSer.checkPocastRole(owner)));
	}
	
	@GetMapping("/api/v1/top3-podcast")
	public ResponseEntity<MessageResponse> top3Podcast() {
		return ResponseEntity.ok(new MessageResponse(true, "successs", podcastSer.top3podcast()));
	}
}
