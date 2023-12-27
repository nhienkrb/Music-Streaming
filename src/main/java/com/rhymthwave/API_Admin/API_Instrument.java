package com.rhymthwave.API_Admin;

import com.rhymthwave.DAO.*;
import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.ServiceAdmin.IInstrumentServiceAdmin;
import com.rhymthwave.Utilities.ExcelExportService;
import com.rhymthwave.Utilities.ImportEx;
import com.rhymthwave.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/category/instrument")
@RequiredArgsConstructor
public class API_Instrument {

	private final IInstrumentServiceAdmin iinstrumentService;

	
    private final ExcelExportService excelExportService;
	
	private final ImportEx importEx;
	
	@GetMapping
	public ResponseEntity<?> getAllMood(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "sortBy", required = false, defaultValue = "asc") String sortBy,
			@RequestParam(value = "sortfield", required = false, defaultValue = "instrumentId") String sortField) {

		Page<Instrument> pageMood = iinstrumentService.getInstrumentPage(page, sortBy, sortField);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", pageMood));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findOneMood(@PathVariable("id") Integer id) {

		Instrument instrument = iinstrumentService.findById(id);

		if (instrument == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Mood does exists", instrument));
		}

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", instrument));
	}

	@PostMapping()
	public ResponseEntity<?> createMood(@RequestBody Instrument instrumentDTO, final HttpServletRequest request) {

		Instrument instrument = iinstrumentService.create(instrumentDTO,request );
		if (instrument == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "Mood exists", instrument));
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(true, "Successfully", instrument));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateMood(@PathVariable("id") Integer id,  @RequestBody Instrument instrumentDTO,final HttpServletRequest request) {
		
		Instrument instrument = iinstrumentService.update(instrumentDTO,request);
		
		if (instrument == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Mood does exists", instrument));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", instrument));
	}
	
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMood(@PathVariable("id") Integer id) {
		
		boolean mood = iinstrumentService.delete(id);
		
		if (mood == false) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Mood does exists", mood));
		}

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", mood));
	}
	
	@GetMapping("/export-excel")
    public ResponseEntity<?> exportToExcel(HttpServletResponse response) {
        List<Instrument> Instrument =  iinstrumentService.findAllInstrument();
        try {
        	
            excelExportService.exportToExcel(Instrument,response);
            return  ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Export excel successfully",""));
        } catch (Exception e) {
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "Export excel Error",""));
        }
    }
	
	
	private final MoodDAO moodDao;
	private final GenreDAO genreDAO;
	private  final InstrumentDAO instrumentDAO;
	private  final CultureDAO cultureDAO;
	private  final TagDAO tagDAO;
	private  final CountryDAO countryDAO;
	private  final  AccountDAO accountDAO;
	private  final  ImageDAO imageDAO;
	private  final AuthorDAO authorDAO;
	private  final  RoleDAO roleDAO;

	private  final  ArtistDAO artistDAO;
	@GetMapping("/import")
	public ResponseEntity<?> deleteMood(@RequestParam("excel") MultipartFile file) {
		try {
			List<Artist> instruments = convertExceltoInstrument(file.getInputStream());
			instruments.forEach(s -> System.out.println(s.getArtistName() + " -- " + s.getAccount().getEmail()));
			artistDAO.saveAll(instruments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", "OK"));
	}
	
	public List<Artist>  convertExceltoInstrument(InputStream inputStream){
		
		List<Artist> list = new ArrayList<>();
		try {
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheet("Artist");
		int rowNum = 0;
		Iterator<Row> iterator = sheet.iterator();
		while (iterator.hasNext()) {
			Row row = iterator.next();
			if(rowNum == 0) {
				rowNum++;
				continue; 
			}
			Iterator<Cell> cells = row.iterator();
			
			int cid = 0;

			var instrument = new Artist();
			Image image = imageDAO.findById("1").orElse(null);
			while (cells.hasNext()) {
				Cell cell = cells.next();
				switch(cid) {

					case 0:
						instrument.setArtistName( cell.getStringCellValue());
						break;
					case 1:
						instrument.setFullName( cell.getStringCellValue());
						break;

					case 2:
						instrument.setDateOfBirth(cell.getDateCellValue());
						break;
					case 3:
						instrument.setPlaceOfBirth( cell.getStringCellValue());
						break;
					case 4:
						instrument.setBio( cell.getStringCellValue());
						break;
					case 5:
						Account account = accountDAO.findByEmail(cell.getStringCellValue());
						instrument.setAccount( account);
						break;
					default:
						break;
				}
				cid ++; 
			}
			list.add(instrument);
		}
		workbook.close();
		 inputStream.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
       
		
		return list;
	}

}
