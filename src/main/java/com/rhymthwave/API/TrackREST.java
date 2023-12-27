package com.rhymthwave.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.TrackService;
import com.rhymthwave.entity.Track;

@RestController
@CrossOrigin("*")
public class TrackREST {
	
	@Autowired
	TrackService trackSer;
	
	@Autowired
	CRUD<Track, Integer> crudTrack;
	
	@GetMapping("/api/v1/track")
	public ResponseEntity<MessageResponse> getAllTrack(){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudTrack.findAll()));
	}
	
	@GetMapping("/api/v1/track/{id}")
	public ResponseEntity<MessageResponse> getOneTrack(@PathVariable("id") Integer id){
		return ResponseEntity.ok(new MessageResponse(true, "success", crudTrack.findOne(id)));
	}
	
	@PostMapping("/api/v1/track")
	public ResponseEntity<MessageResponse> createTrack(@RequestBody Track track){
		return ResponseEntity.ok(new MessageResponse(true, "success", crudTrack.create(track)));
	}
	
	@GetMapping("/api/v1/track-album/{album-id}")
	public ResponseEntity<MessageResponse> getTrackByAlbum(@PathVariable("album-id") Integer albumId){
		return ResponseEntity.ok(new MessageResponse(true,"success",trackSer.getTrackByAlbum(albumId)));
	}
	
	@DeleteMapping("/api/v1/track/{id}")
	public ResponseEntity<MessageResponse> deleteTrack(@PathVariable("id") Integer id){
		return ResponseEntity.ok(new MessageResponse(true, "success", crudTrack.delete(id)));
	}
}
