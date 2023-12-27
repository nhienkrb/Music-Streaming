package com.rhymthwave.API_Admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.ServiceAdmin.IMoodServiceAdmin;
import com.rhymthwave.Utilities.ExcelExportService;
import com.rhymthwave.entity.Mood;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/category/mood")
@RequiredArgsConstructor
public class API_Mood {

	private final IMoodServiceAdmin iMoodService;
	
    private final ExcelExportService excelExportService;
	
	@GetMapping
	public ResponseEntity<?> getAllMood(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "sortBy", required = false, defaultValue = "asc") String sortBy,
			@RequestParam(value = "sortfield", required = false, defaultValue = "moodid") String sortField) {

		Page<Mood> pageMood = iMoodService.getMoodPage(page, sortBy, sortField);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", pageMood));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findOneMood(@PathVariable("id") Integer id) {

		Mood mood = iMoodService.findById(id);

		if (mood == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Mood does exists", mood));
		}

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", mood));
	}

	@PostMapping()
	public ResponseEntity<?> createMood(@RequestBody Mood moodRequest, final HttpServletRequest request) {

		Mood mood = iMoodService.create(moodRequest,request);
		if (mood == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "Mood exists", mood));
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(true, "Successfully", mood));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateMood(@PathVariable("id") Integer id,  @RequestBody Mood moodRequest, final HttpServletRequest request) {
		
		Mood mood = iMoodService.update(moodRequest,request);
		if (mood == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Mood does exists", mood));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", mood));
	}
	
	
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMood(@PathVariable("id") Integer id) {
		
		boolean mood = iMoodService.delete(id);
		
		if (mood == false) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Mood does exists", mood));
		}

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", mood));
	}
	
	
	@GetMapping("/export-excel")
    public ResponseEntity<?> exportToExcel(HttpServletResponse response) {
        List<Mood> mood =  iMoodService.findAllMood();
        try {
        	
            excelExportService.exportToExcel(mood,response);
            return  ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Export excel successfully",""));
        } catch (Exception e) {
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "Export excel Error",""));
        }
    }

}
