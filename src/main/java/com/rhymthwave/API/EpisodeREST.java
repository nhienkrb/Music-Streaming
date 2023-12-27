package com.rhymthwave.API;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.rhymthwave.Service.EpisodeService;
import com.rhymthwave.Service.ImageService;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Image;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class EpisodeREST {
	private final CRUD<Episode, Long> crudEpisode;

	private final CRUD<Image, String> crudImage;

	private final CRUD<Account, String> crudAccount;

	private final EpisodeService episodeSer;

	private final ImageService imgSer;

	private final CloudinaryService cloudinarySer;

	private final GetHostByRequest host;

	@GetMapping("/api/v1/episode")
	public ResponseEntity<MessageResponse> findAllEpisode() {
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudEpisode.findAll()));
	}
	
	@GetMapping("/api/v1/episode/{id}")
	public ResponseEntity<MessageResponse> findEpisode(@PathVariable("id") Long id ) {
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudEpisode.findOne(id)));
	}

	@GetMapping("/api/v1/podcast-episode/{podcastId}")
	public ResponseEntity<MessageResponse> findAllEpisodeByPodcast(@PathVariable("podcastId") Long podcastId) {
		return ResponseEntity.ok(new MessageResponse(true, "successs", episodeSer.findAllEpisodeByPodcast(podcastId,false)));
	}
	
	@GetMapping("/api/v1/podcast-episode-deleted/{podcastId}")
	public ResponseEntity<MessageResponse> findAllEpisodeByPodcastDeleted(@PathVariable("podcastId") Long podcastId) {
		return ResponseEntity.ok(new MessageResponse(true, "successs", episodeSer.findAllEpisodeByPodcast(podcastId,true)));
	}

	@PostMapping(value = "/api/v1/episode", consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> createEpisode(@ModelAttribute Episode episode, HttpServletRequest req,
			@PathParam("fileAudio") MultipartFile fileAudio, @PathParam("coverImg") MultipartFile coverImg) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		if (coverImg != null && fileAudio !=null) {
			Map<?, ?> respAudio = cloudinarySer.Upload(fileAudio, "EpisodeRecord", account.getUsername());
			Map<?, ?> respImg = cloudinarySer.UploadResizeImage(coverImg, "EpisodeImage", account.getUsername(), 350,350);
			Image image = imgSer.getEntity(respImg);
			episode = episodeSer.snapEpisode(episode, respAudio, crudImage.create(image));
		}
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudEpisode.create(episode)));
	}

	@PutMapping(value = "/api/v1/episode-file/{id}", consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> updateFileEpisode(@PathVariable("id") Long id,HttpServletRequest req,
			@PathParam("fileAudio") MultipartFile fileAudio, @PathParam("coverImg") MultipartFile coverImg) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		Episode episode= crudEpisode.findOne(id); 
		Image image = episode.getImage();
		if(coverImg != null) {
			Map<?, ?> respImg = cloudinarySer.UploadResizeImage(coverImg, "EpisodeImage", account.getUsername(), 350,350);
			image = imgSer.getEntity(respImg);
			crudImage.create(image);
			episode.setImage(image);
			crudEpisode.update(episode);
		}
		if (fileAudio !=null) {
			Map<?, ?> respAudio = cloudinarySer.Upload(fileAudio, "EpisodeRecord", account.getUsername());
			episode = episodeSer.snapEpisode(episode, respAudio,image);
		}
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudEpisode.update(episode)));
	}
	
	@PutMapping(value = "/api/v1/episode")
	public ResponseEntity<MessageResponse> updateEpisode(@RequestBody Episode episode) {
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudEpisode.update(episode)));
	}

	@DeleteMapping(value = "/api/v1/episode/{id}")
	public ResponseEntity<MessageResponse> updateEpisode(@PathVariable("id") Long id) {
		Episode episode= crudEpisode.findOne(id);
		cloudinarySer.deleteFile(episode.getPublicIdFile());
		cloudinarySer.deleteFile(episode.getImage().getPublicId());
		return ResponseEntity.ok(new MessageResponse(true, "successs", crudEpisode.delete(id)));
	}
	
	@GetMapping("/api/v1/latest-episode-podcast/{id}")
	public ResponseEntity<MessageResponse> findLatestEpisodeByPodcast(@PathVariable("id") Long id){
		return ResponseEntity.ok(new MessageResponse(true,"success",episodeSer.findLatestEpisodeByPodcast(id)));
	}
	
	@GetMapping("/api/v1/episode-pl/{keyword}")
	public ResponseEntity<MessageResponse> findEpisodeByName(@PathVariable("keyword") String keyword) {
		return ResponseEntity.ok(new MessageResponse(true, "successs", episodeSer.findByName(keyword)));
	}
	
	@GetMapping("/api/v1/episode-for-you")
	public ResponseEntity<MessageResponse> findEpisodeForYou(@RequestParam("tag") Optional<List<Integer>> tag) {
		return ResponseEntity.ok(new MessageResponse(true, "successs", episodeSer.top50EpForYou(true, tag)));
	}
}
