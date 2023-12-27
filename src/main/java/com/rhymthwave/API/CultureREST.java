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
import com.rhymthwave.entity.Culture;

@RestController
@CrossOrigin("*")
public class CultureREST {
	@Autowired
	CRUD<Culture, Integer> crudCulture;
	
	@GetMapping("/api/v1/culture")
	public ResponseEntity<MessageResponse> getAllCulture(){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudCulture.findAll()));
	}
	
	@GetMapping("/api/v1/culture/{id}")
	public ResponseEntity<MessageResponse> getCultureById(@PathVariable("id") Integer id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudCulture.findOne(id)));
	}
	
	@PostMapping("/api/v1/culture")
	public ResponseEntity<MessageResponse> createCulture(@RequestBody Culture culture){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudCulture.create(culture)));
	}
}
