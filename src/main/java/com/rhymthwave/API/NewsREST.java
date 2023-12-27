package com.rhymthwave.API;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.NewService;
import com.rhymthwave.Service.Implement.NewServiceImp;
import com.rhymthwave.entity.News;
import com.rhymthwave.entity.TypeEnum.EROLE;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsREST {
	
	private final NewService newService;
	private final NewServiceImp newServiceImp;

	@GetMapping()
	public ResponseEntity<MessageResponse> getAllNewsByCreateFor(
			@RequestParam(value = "createfor", required = false, defaultValue = "USER") EROLE createfor) {
		
		List<News> listnew = newService.getAllNewForRole(createfor);
		
		return ResponseEntity.ok(new MessageResponse(true, "success",listnew));
	}

	@GetMapping("/{id}")
	public ResponseEntity<MessageResponse> getNewsById(@PathVariable("id") Integer id) {
		return ResponseEntity.ok(new MessageResponse(true, "success",newServiceImp.findOne(id)));
	}
	
	@GetMapping("/all")
	public ResponseEntity<MessageResponse> getNewsAll() {
		return ResponseEntity.ok(new MessageResponse(true, "success",newServiceImp.findAll()));
	}
}
