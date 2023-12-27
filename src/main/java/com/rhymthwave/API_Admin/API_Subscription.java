package com.rhymthwave.API_Admin;

import java.util.List;

import com.rhymthwave.entity.TypeEnum.ESubscription;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.SubscriptionService;
import com.rhymthwave.entity.Subscription;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/subscription")
@RequiredArgsConstructor
public class API_Subscription {

	private final SubscriptionService subscriptionService;
	
	@PostMapping
	public ResponseEntity<?> createSubscription(@RequestBody Subscription subscription) {

		subscriptionService.create(subscription);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(true, "Successfully", subscription));
	}
	
	@GetMapping()
	public ResponseEntity<?> getAllSubscription() {

		List<Subscription> subscription =  subscriptionService.findAll();
		
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", subscription));
	}

	@GetMapping("/type")
	public ResponseEntity<?> getAllSubscriptionByType(@RequestParam("category") ESubscription eSubscription, @RequestParam("active") boolean active) {
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", subscriptionService.findByCategory(eSubscription,active)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSubscription(@PathVariable("id") Integer id ) {
		boolean status =  subscriptionService.delete(id);
		if(status == false) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "don't exist"));

		}
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully"));
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOneSubscription(@PathVariable("id") Integer id ) {
	
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully",subscriptionService.findOne(id)));
	}
	
	@PutMapping()
	public ResponseEntity<?> updateSubscription(@RequestBody Subscription subscription) {
	
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully",subscriptionService.update(subscription)));
	}
	
	
	
}
