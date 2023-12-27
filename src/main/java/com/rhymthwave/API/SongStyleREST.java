package com.rhymthwave.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.SongStyle;

@RestController
@CrossOrigin("*")
public class SongStyleREST {
	@Autowired
	CRUD<SongStyle, Integer> crudStyle;
	
	@GetMapping("/api/v1/song-style")
	public ResponseEntity<MessageResponse> getAllSongStyle(){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudStyle.findAll()));
	}
	
	@GetMapping("/api/v1/song-style/{id}")
	public ResponseEntity<MessageResponse> getSongStyleById(@PathVariable("id") Integer id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudStyle.findOne(id)));
	}
	
	@PostMapping("/api/v1/song-style")
	public ResponseEntity<MessageResponse> createSongStyle(@RequestBody SongStyle songStyle){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudStyle.create(songStyle)));
	}
}
