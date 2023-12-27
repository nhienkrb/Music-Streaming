package com.rhymthwave.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.DisplayImageService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class DisplayImageREST {

	@Autowired
	DisplayImageService displayImageService;

	@GetMapping("/api/v1/display/{position}")
	public ResponseEntity<MessageResponse> getAll(@PathVariable("position") String position) {
		return ResponseEntity
				.ok(new MessageResponse(true, "succeess", displayImageService.displayImageByPosition(position)));
	}
}
