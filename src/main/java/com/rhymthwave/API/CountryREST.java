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
import com.rhymthwave.entity.Country;

@RestController
@CrossOrigin("*")
public class CountryREST {
	@Autowired
	CRUD<Country, String> crudCountry;
	
	@GetMapping("/api/v1/country")
	public ResponseEntity<MessageResponse> getAllCountry(){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudCountry.findAll()));
	}
	
	@GetMapping("/api/v1/country/{id}")
	public ResponseEntity<MessageResponse> getCountryById(@PathVariable("id") String id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudCountry.findOne(id)));
	}
	
	@PostMapping("/api/v1/country")
	public ResponseEntity<MessageResponse> createCountry(@RequestBody Country country){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudCountry.create(country)));
	}
}
