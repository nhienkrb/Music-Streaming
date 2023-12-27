package com.rhymthwave.API;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotificationREST {
	
	private final NotificationService notifySer;
	
	@GetMapping("/api/v1/notify-latest")
	public ResponseEntity<MessageResponse> findNotiLatest(){
		return ResponseEntity.ok(new MessageResponse(true,"success",notifySer.notifyLatest()));
	}
}
