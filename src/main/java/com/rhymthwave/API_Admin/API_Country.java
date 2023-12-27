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

import com.rhymthwave.DAO.CountryDAO;
import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.ServiceAdmin.ICountryServiceAdmin;
import com.rhymthwave.Utilities.ExcelExportService;
import com.rhymthwave.entity.Country;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/category/country")
@RequiredArgsConstructor
public class API_Country {

	private final ICountryServiceAdmin countryServiceAdmin;

	private final CountryDAO countryDAO;

	private final ExcelExportService excelExportService;
	
	@GetMapping
	public ResponseEntity<?> getAllSongType(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "sortBy", required = false, defaultValue = "asc") String sortBy,
			@RequestParam(value = "sortfield", required = false, defaultValue = "id") String sortField) {

		Page<Country> pages = countryServiceAdmin.getCountryPage(page, sortBy, sortField);
		
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", countryDAO.findAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findOneMood(@PathVariable("id") String id) {

		Country country = countryServiceAdmin.findById(id);

		if (country == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageResponse(false, "Mood does exists", country));
		}

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", country));
	}

	@PostMapping()
	public ResponseEntity<?> createMood( @RequestBody @Valid Country countryRequest, final HttpServletRequest request) {

		Country country = countryServiceAdmin.create(countryRequest,request);
		if (country == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new MessageResponse(false, "Mood exists", country));
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(true, "Successfully", country));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateMood(@PathVariable("id") String id, @RequestBody Country countryRequest,final HttpServletRequest request) {

		Country country = countryServiceAdmin.update(countryRequest,request);

		if (country == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageResponse(false, "Mood does exists", country));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", country));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMood(@PathVariable("id") String id) {

		boolean country = countryServiceAdmin.delete(id);

		if (country == false) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageResponse(false, "Mood does exists", null));
		}

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
	}
	
	@GetMapping("/export-excel")
    public ResponseEntity<?> exportToExcel(HttpServletResponse response) {
        List<Country> countries =  countryServiceAdmin.findAllCountry();
        try {
        	
            excelExportService.exportToExcel(countries,response);
            return  ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Export excel successfully",""));
        } catch (Exception e) {
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "Export excel Error",""));
        }
    }


}