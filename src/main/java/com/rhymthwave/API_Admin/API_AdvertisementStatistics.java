package com.rhymthwave.API_Admin;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.AdvertisementService;
import com.rhymthwave.Request.DTO.ResultsADS_DTO;
import com.rhymthwave.entity.Advertisement;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/advertisement/statistics")
@RequiredArgsConstructor
public class API_AdvertisementStatistics {

	private  final AdvertisementService advertisementService;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getAdvertisementRuOrComplete(@PathVariable("id") Integer idAdvertisementService) {

		Advertisement advertisement = advertisementService.getById(idAdvertisementService);

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully",advertisement));
	}

	@GetMapping("/{id}/results")
	public ResponseEntity<?> getResults(@PathVariable("id") Integer idADS) {

		List<ResultsADS_DTO> resultsADS = advertisementService.getResultsADS(idADS);

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully",resultsADS));
	}


	@PutMapping("/{id}/send/results")
	public ResponseEntity<?> getSendResults(@PathVariable("id") Integer id,HttpServletRequest request) {

		advertisementService.sendResultsADS(id, request);

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully"));
	}

}
