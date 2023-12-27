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
import com.rhymthwave.entity.Mood;

@RestController
@CrossOrigin("*")
public class MoodREST {
	@Autowired
	CRUD<Mood, Integer> crudMood;
	
	@GetMapping("/api/v1/mood")
	public ResponseEntity<MessageResponse> getAllMood(){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudMood.findAll()));
	}
	
	@GetMapping("/api/v1/mood/{id}")
	public ResponseEntity<MessageResponse> getMoodById(@PathVariable("id") Integer id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudMood.findOne(id)));
	}
	
	@PostMapping("/api/v1/mood")
	public ResponseEntity<MessageResponse> createMood(@RequestBody Mood mood){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudMood.create(mood)));
	}
}
