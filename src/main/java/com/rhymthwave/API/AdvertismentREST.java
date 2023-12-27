package com.rhymthwave.API;

import java.util.Map;

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

import com.rhymthwave.DTO.AdvertismentDTO;
import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Request.DTO.AdvertisementDTO;
import com.rhymthwave.Service.AdvertisementService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.CloudinaryService;
import com.rhymthwave.Service.ImageService;
import com.rhymthwave.entity.Advertisement;
import com.rhymthwave.entity.Image;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class AdvertismentREST {
	private final CRUD<Advertisement, Long> crudAds;

	private final CRUD<Image, String> crudImg;

	private final CloudinaryService cloudinary;

	private final ImageService imgSer;

	private final AdvertisementService adsSer;

	@GetMapping("/api/v1/ads-email")
	public ResponseEntity<MessageResponse> findAllAdsByEmail(@RequestParam("email") String email) {
		return ResponseEntity.ok(new MessageResponse(true, "success", adsSer.findAdsByEmail(email)));
	}

	@PostMapping(value = "/api/v1/ads-file", consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> postAds(@ModelAttribute Advertisement ads,
			@PathParam("bannerFile") MultipartFile banner, @PathParam("audio") MultipartFile audio) {
		if (banner != null) {
			Map<String, Object> respBg = cloudinary.Upload(banner, "Banner-Ads", ads.getAccount().getUsername());
			Image imgBanner = imgSer.getEntity(respBg);
			crudImg.create(imgBanner);
			ads.setImage(imgBanner);
		}
		if (audio != null) {
			Map<String, Object> respAudio = cloudinary.Upload(audio, "Advertisement", ads.getAccount().getUsername());
			ads.setAudioFile((String) respAudio.get("url"));
			ads.setPublicIdAudio((String) respAudio.get("public_id"));
		}
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAds.create(ads)));
	}

	@PutMapping("/api/v1/ads")
	public ResponseEntity<MessageResponse> updateAds(@RequestBody AdvertismentDTO adsDto) {
		Advertisement ads = crudAds.findOne(adsDto.getAdId());
		ads.setListened(adsDto.getListened());
		ads.setClicked(adsDto.getClicked());
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAds.update(ads)));
	}
	
	@PutMapping("/api/v1/advertisement")
	public ResponseEntity<MessageResponse> post(@RequestBody Advertisement ads) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAds.create(ads)));
	}
	
	@DeleteMapping("/api/v1/ads/{id}")
	public ResponseEntity<MessageResponse> deleteAds(@PathVariable("id") Long id){
		Advertisement ads = crudAds.findOne(id);
		cloudinary.deleteFile(ads.getImage().getAccessId());
		cloudinary.deleteFile(ads.getPublicIdAudio());
		return ResponseEntity.ok(new MessageResponse(true,"success", crudAds.delete(id)));
	}

	@PutMapping(value = "/api/v1/ads-file", consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> putAdsFile(@RequestParam("id") Long id,
			@PathParam("bannerFile") MultipartFile banner, @PathParam("audio") MultipartFile audio) {
		Advertisement ads = crudAds.findOne(id);
		cloudinary.deleteFile(ads.getPublicIdAudio());
		cloudinary.deleteFile(ads.getImage().getAccessId());
		if (banner != null) {
			Map<String, Object> respBg = cloudinary.Upload(banner, "Banner-Ads", ads.getAccount().getUsername());
			Image imgBanner = imgSer.getEntity(respBg);
			crudImg.create(imgBanner);
			ads.setImage(imgBanner);
		}
		if (audio != null) {
			Map<String, Object> respAudio = cloudinary.Upload(audio, "Advertisement", ads.getAccount().getUsername());
			ads.setAudioFile((String) respAudio.get("url"));
			ads.setPublicIdAudio((String) respAudio.get("public_id"));
		}
		return ResponseEntity.ok(new MessageResponse(true, "success", crudAds.update(ads)));
	}
	
	@GetMapping("/api/v1/ads-running")
	public ResponseEntity<MessageResponse> findAllAdsRunning() {
		return ResponseEntity.ok(new MessageResponse(true, "success", adsSer.findAdsRunning(true,2)));
	}
	
	@PostMapping(value = "/api/v1/buy-ads", consumes = {"multipart/form-data"})
	public ResponseEntity<MessageResponse> buyAds(@ModelAttribute AdvertisementDTO ads,HttpServletRequest req) {
		return ResponseEntity.ok(new MessageResponse(true, "success", adsSer.buyAds(ads, req)));
	}
}
