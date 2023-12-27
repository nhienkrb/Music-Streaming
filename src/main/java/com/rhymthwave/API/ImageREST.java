package com.rhymthwave.API;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Image;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ImageREST {
	private final CRUD<Image, String> crudImg;
	
	@GetMapping("/api/v1/image/{id}")
	public ResponseEntity<MessageResponse> findImage(@PathVariable("id") String id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudImg.findOne(id)));
	}
	
	@DeleteMapping("/api/v1/image/{id}")
	public ResponseEntity<MessageResponse> deleteImage(@PathVariable("id") String id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudImg.delete(id)));
	}
}
