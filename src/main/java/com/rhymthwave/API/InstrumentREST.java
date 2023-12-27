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
import com.rhymthwave.entity.Instrument;

@RestController
@CrossOrigin("*")
public class InstrumentREST {

	@Autowired
	CRUD<Instrument, Integer> crudInstrument;
	
	@GetMapping("/api/v1/instrument")
	public ResponseEntity<MessageResponse> getAllInstrument(){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudInstrument.findAll()));
	}
	
	@GetMapping("/api/v1/instrument/{id}")
	public ResponseEntity<MessageResponse> getInstrumentById(@PathVariable("id") Integer id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudInstrument.findOne(id)));
	}
	
	@PostMapping("/api/v1/instrument")
	public ResponseEntity<MessageResponse> createInstrument(@RequestBody Instrument instrument){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudInstrument.create(instrument)));
	}
}
