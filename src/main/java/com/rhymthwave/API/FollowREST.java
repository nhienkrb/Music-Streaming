package com.rhymthwave.API;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.DTO.AuthorDTO;
import com.rhymthwave.Service.AuthorService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.FollowService;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Author;
import com.rhymthwave.entity.Follow;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class FollowREST {
	private final FollowService followSer;
	
	private final AuthorService authorSer;
		
	private final CRUD<Follow, Long> crudFollow;
		
	private final GetHostByRequest host;
	
	@GetMapping("/api/v1/my-list-follow")
	public ResponseEntity<MessageResponse> following(HttpServletRequest req){
		String main = host.getEmailByRequest(req);
		Author accountA = authorSer.findAuthor(1, main);
		return ResponseEntity.ok(new MessageResponse(true,"success",followSer.findMyListFollow(accountA)));
	}
	
	@PostMapping("/api/v1/follow")
	public ResponseEntity<MessageResponse> following(HttpServletRequest req, @RequestBody AuthorDTO author){
		String main = host.getEmailByRequest(req);
		Author accountA = authorSer.findAuthor(1, main);
		Author accountB = authorSer.findAuthor(author.getType(), author.getEmail());
		Follow followRaw = new Follow();
		Follow followData = followSer.snapFollow(followRaw, accountA, accountB);
		return ResponseEntity.ok(new MessageResponse(true,"success",crudFollow.create(followData)));
	}
	
	@DeleteMapping("/api/v1/follow")
	public ResponseEntity<MessageResponse> cancelFollow(HttpServletRequest req, @RequestParam("email") String email,@RequestParam("type") Integer type){
		String main = host.getEmailByRequest(req);
		Author accountA = authorSer.findAuthor(1, main);
		Author accountB = authorSer.findAuthor(type, email);
		Follow follow = followSer.findFollowByAccount(accountA, accountB);
		return ResponseEntity.ok(new MessageResponse(true,"success",crudFollow.delete(follow.getFollowerId())));
	}
	
	@GetMapping("/api/v1/check-follow")
	public ResponseEntity<MessageResponse> checkFollow(@RequestParam("email") String email,@RequestParam("type") Integer type, HttpServletRequest req){
		String main = host.getEmailByRequest(req);
		Author accountA = authorSer.findAuthor(1, main);
		Author accountB = authorSer.findAuthor(type, email);
		Follow follow = followSer.findFollowByAccount(accountA, accountB);
		if (follow!=null) {
			return ResponseEntity.ok(new MessageResponse(true,"success",follow));
		}
		return ResponseEntity.ok(new MessageResponse(false,"fail",null)); 
	}
	
	@GetMapping("/api/v1/quantity-follow")
	public ResponseEntity<MessageResponse> getQuantityFollow(@RequestParam("email") String email,@RequestParam("type") Integer type,@RequestParam("days") Integer days){
		Author account = authorSer.findAuthor(type, email);
		Integer quantity = followSer.getQuantityFollowByDate(account.getAuthorId(), days);
		return ResponseEntity.ok(new MessageResponse(true,"success",quantity)); 
	}
	
	@DeleteMapping("/api/v1/unfollow/{id}")
	public ResponseEntity<MessageResponse> unFollow(@PathVariable("id") Long id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudFollow.delete(id)));
	}
	
	@GetMapping("/api/v1/monitor-follow")
	public ResponseEntity<MessageResponse> monitorFollow(@RequestParam("email") String email,@RequestParam("role") Integer role,@RequestParam("duration") Integer duration){
		return ResponseEntity.ok(new MessageResponse(true,"success",followSer.monitorFollower(email, role, duration))); 
	}
}
