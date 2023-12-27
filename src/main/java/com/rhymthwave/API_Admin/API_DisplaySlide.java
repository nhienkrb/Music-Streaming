package com.rhymthwave.API_Admin;

import java.io.File;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DAO.SlideDAO;
import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Request.DTO.SlideDTO;
import com.rhymthwave.Service.Implement.AccountServiceImpl;
import com.rhymthwave.ServiceAdmin.IDisplaySlide;
import com.rhymthwave.ServiceAdmin.Implement.DisplaySlideImp;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Image;
import com.rhymthwave.entity.Slide;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/display-slide")
@RequiredArgsConstructor
public class API_DisplaySlide {
	private final IDisplaySlide displaySlide;

	private final GetHostByRequest host;

	private final AccountServiceImpl accountService;

	private final DisplaySlideImp displaySlideImp;
	

	@GetMapping()
	public ResponseEntity<?> getAllDisplaySlide() {
		List<String> list = displaySlide.getAllPositionSlidesShowing();
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", list));
	}

	@GetMapping("/slideshowing")
	public ResponseEntity<?> getAllPositionSlides() {
		List<Slide> list = displaySlideImp.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", list));
	}

	@GetMapping("/slideshowing/{positon}")
	public ResponseEntity<?> getPositionSlidesShowing(@PathVariable("positon") String positon) {
		List<String> list = displaySlide.getAccessIDInImage(positon);
		List<Image> listUrl = displaySlide.getURLImage(list);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", listUrl));
	}

	@PostMapping
	public ResponseEntity<?> createSlide(@RequestBody SlideDTO slideRequest, final HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		Account account = accountService.findOne(owner);
		displaySlideImp.createSlide(slideRequest, req, account);
		return ResponseEntity.ok(new MessageResponse(true, "Create Slide successfully"));
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateSlide(@PathVariable("id") String idAccess,@RequestBody SlideDTO slideRequest, final HttpServletRequest request){
	

		Slide slides = displaySlide.updateSlide(idAccess,slideRequest,request);
		
		if(slides == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Slide with id not found ", slides));

		}
		
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Update Slide Successfully", slides));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSlides(@PathVariable("id") String idAccess){
		
		boolean status = displaySlide.deleteSlide(idAccess);
		
		if(status == false) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(status, "Delete Fail", status));

		}
		
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(status, "Delete Successfully", status));
	}
}
