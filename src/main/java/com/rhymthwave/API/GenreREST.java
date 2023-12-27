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
import com.rhymthwave.entity.Genre;

@RestController
@CrossOrigin("*")
public class GenreREST {
	@Autowired
	CRUD<Genre, Integer> crudGenre;
	
	@GetMapping("/api/v1/genre")
	public ResponseEntity<MessageResponse> getAllGenre(){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudGenre.findAll()));
	}
	
	@GetMapping("/api/v1/genre/{id}")
	public ResponseEntity<MessageResponse> getGenreById(@PathVariable("id") Integer id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudGenre.findOne(id)));
	}
	
	@PostMapping("/api/v1/genre")
	public ResponseEntity<MessageResponse> createGenre(@RequestBody Genre genre){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudGenre.create(genre)));
	}
}
