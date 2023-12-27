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
import com.rhymthwave.entity.Tag;

@RestController
@CrossOrigin("*")
public class TagREST {
	@Autowired
	CRUD<Tag, Integer> crudTag;
	
	@GetMapping("/api/v1/tag")
	public ResponseEntity<MessageResponse> getAllTag(){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudTag.findAll()));
	}
	
	@GetMapping("/api/v1/tag/{id}")
	public ResponseEntity<MessageResponse> getTagById(@PathVariable("id") Integer id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudTag.findOne(id)));
	}
	
	@PostMapping("/api/v1/tag")
	public ResponseEntity<MessageResponse> createTag(@RequestBody Tag tag){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudTag.create(tag)));
	}
}
