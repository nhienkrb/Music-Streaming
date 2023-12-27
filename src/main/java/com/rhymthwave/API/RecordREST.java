package com.rhymthwave.API;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.ArtistService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.CloudinaryService;
import com.rhymthwave.Service.ImageService;
import com.rhymthwave.Service.RecordService;
import com.rhymthwave.Service.SongService;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.Utilities.Cookie.CookiesUntils;
import com.rhymthwave.Utilities.JWT.JwtTokenCreate;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Artist;
import com.rhymthwave.entity.Image;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.Song;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class RecordREST {

	private final CRUD<Recording, Long> crudRecord;

	private final CloudinaryService cloudinary;

	private final RecordService recordSer;

	private final GetHostByRequest host;

	private final CRUD<Account, String> crudAccount;

	private final ArtistService artistSer;

	@GetMapping("/api/v1/record")
	public ResponseEntity<MessageResponse> getAllRecord() {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudRecord.findAll()));
	}

	@GetMapping("/api/v1/record/{id}")
	public ResponseEntity<MessageResponse> getOneRecord(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudRecord.findOne(id)));
	}

	@GetMapping("/api/v1/my-record")
	public ResponseEntity<MessageResponse> getMyRecord(HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		return ResponseEntity.ok(new MessageResponse(true, "success", recordSer.findListRecordNotRaw(owner)));
	}

	@GetMapping("/api/v1/my-record-not-raw")
	public ResponseEntity<MessageResponse> getOneRecord(HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		return ResponseEntity.ok(new MessageResponse(true, "success", recordSer.findRawRecordByCreater(owner)));
	}

	@PostMapping(value = "/api/v1/record", consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> createRecord(@ModelAttribute Recording record, HttpServletRequest req,
			@PathParam("fileRecord") MultipartFile fileRecord, @PathParam("fileLyrics") MultipartFile fileLyrics) {
		String owner = host.getEmailByRequest(req);
		Account account = crudAccount.findOne(owner);
		Map<String, Object> respRecord = cloudinary.Upload(fileRecord, "Records", account.getArtist().getArtistName());
		if (fileLyrics != null) {
			Map<String, Object> respLyrics = cloudinary.Upload(fileLyrics, "Lyrics",
					account.getArtist().getArtistName());
			record.setLyricsUrl((String) respLyrics.get("url"));
			record.setPublicIdLyrics((String) respLyrics.get("public_id"));
		}

		record.setAudioFileUrl((String) respRecord.get("url"));
		record.setPublicIdAudio((String) respRecord.get("public_id"));
		record.setEmailCreate(owner);
		return ResponseEntity.ok(new MessageResponse(true, "success", crudRecord.create(record)));
	}

	@PutMapping(value = "/api/v1/record-file/{id}", consumes = { "multipart/form-data" })
	public ResponseEntity<MessageResponse> updateRecordFile(@PathVariable("id") Long id, HttpServletRequest req,
			@PathParam("fileRecord") MultipartFile fileRecord, @PathParam("fileLyrics") MultipartFile fileLyrics) {
		String owner = host.getEmailByRequest(req);
		Artist artist = artistSer.findByEmail(owner);
		Recording record = crudRecord.findOne(id);
		if (fileRecord != null) {
			Map<String, Object> respRecord = cloudinary.Upload(fileRecord, "Records", artist.getArtistName());
			record.setAudioFileUrl((String) respRecord.get("url"));
			record.setPublicIdAudio((String) respRecord.get("public_id"));
		}

		if (fileLyrics != null) {
			Map<String, Object> respLyrics = cloudinary.Upload(fileLyrics, "Lyrics", artist.getArtistName());
			record.setLyricsUrl((String) respLyrics.get("url"));
			record.setPublicIdLyrics((String) respLyrics.get("public_id"));
		}

		return ResponseEntity.ok(new MessageResponse(true, "success", crudRecord.update(record)));
	}

	@PutMapping("/api/v1/record")
	public ResponseEntity<MessageResponse> updateRecord(@RequestBody Recording record) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudRecord.update(record)));
	}

	@DeleteMapping("/api/v1/record/{id}")
	public ResponseEntity<MessageResponse> deleteRecord(@PathVariable("id") Long id) {
		return ResponseEntity.ok(new MessageResponse(true, "success", crudRecord.delete(id)));
	}

	@GetMapping("/api/v1/record-song/{songId}")
	public ResponseEntity<MessageResponse> findListRecordBySong(@PathVariable("songId") Long songId) {
		return ResponseEntity.ok(new MessageResponse(true, "success", recordSer.findRecordBySong(songId)));
	}

	@GetMapping("/api/v1/record-artist")
	public ResponseEntity<MessageResponse> findListRecordByArtist(HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		return ResponseEntity.ok(new MessageResponse(true, "success", recordSer.findRecordByCreater(owner)));
	}

	@GetMapping("/api/v1/record-delete")
	public ResponseEntity<MessageResponse> findListRecordDelete(HttpServletRequest req) {
		String owner = host.getEmailByRequest(req);
		return ResponseEntity.ok(new MessageResponse(true, "success", recordSer.findRecordDelete(owner)));
	}

	@GetMapping("/api/v1/record-random")
	public ResponseEntity<MessageResponse> findListRecordRandom() {
		return ResponseEntity.ok(new MessageResponse(true, "success", recordSer.findListRecordRandom()));
	}

	@GetMapping("/api/v1/record-statistics")
	public ResponseEntity<MessageResponse> statisticsRecord(HttpServletRequest req,
			@RequestParam("duration") Integer duration) {
		String owner = host.getEmailByRequest(req);
		return ResponseEntity.ok(new MessageResponse(true, "success", recordSer.statisticsByDate(owner, duration)));
	}
	
	@GetMapping("/api/v1/top-record-listen")
	public ResponseEntity<MessageResponse> findTopRecordListen(@RequestParam("country") String country){
		return ResponseEntity.ok(new MessageResponse(true,"success",recordSer.top50SongByAreaListened(country, false)));
	}
	
	@GetMapping("/api/v1/top-new-record")
	public ResponseEntity<MessageResponse> findTopNewRecord(@RequestParam("country") String country){
		return ResponseEntity.ok(new MessageResponse(true,"success",recordSer.top50SongByDate(country, false)));
	}
	
	@GetMapping("/api/v1/record-byart/{email}")
	public ResponseEntity<MessageResponse> findListRecordByEmailArtist(@PathVariable("email") String email) {
		return ResponseEntity.ok(new MessageResponse(true, "success", recordSer.findRecordByCreater(email)));
	}
}
