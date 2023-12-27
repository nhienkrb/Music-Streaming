package com.rhymthwave.API;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CloudinaryService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class CloudinaryREST {

	private final CloudinaryService cloudinarSer;
	
	@GetMapping("/api/v1/cloudinary/{parentFolder}/{childFolder}")
	public ResponseEntity<MessageResponse> findListFileByFolder(@PathVariable("parentFolder") String parentFolder,
			@PathVariable("childFolder") String childFolder) {
		return ResponseEntity
				.ok(new MessageResponse(true, "success", cloudinarSer.findListImageByFolder(parentFolder, childFolder)));
	}
	
	@DeleteMapping("/api/v1/cloudinary")
	public ResponseEntity<MessageResponse> deleteFile(@RequestParam("public_id") String public_id) {
		return ResponseEntity.ok(new MessageResponse(true, "success",cloudinarSer.deleteFile(public_id)));
	}
	
	@GetMapping("/api/v1/cloudinary/download")
	public ResponseEntity<MessageResponse> downloadFile(@RequestParam("public_id") String public_id){
		return ResponseEntity.ok(new MessageResponse(true,"success",cloudinarSer.downloadFile(public_id,"jpg")));
	}

	@GetMapping("/api/v1/cloudinary/read-lyrics")
	public ResponseEntity<MessageResponse> readLyrics(@RequestParam("url") String url){
		return ResponseEntity.ok(new MessageResponse(true,"success",cloudinarSer.readLrc(url)));
	}
}
