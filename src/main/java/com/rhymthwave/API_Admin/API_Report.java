package com.rhymthwave.API_Admin;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DAO.ReportDAO;
import com.rhymthwave.DAO.SlideDAO;
import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Request.DTO.NewDTO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.Implement.AccountServiceImpl;
import com.rhymthwave.ServiceAdmin.IDisplaySlide;
import com.rhymthwave.ServiceAdmin.INotification;
import com.rhymthwave.ServiceAdmin.IReportServiceAdmin;
import com.rhymthwave.ServiceAdmin.Implement.DisplaySlideImp;
import com.rhymthwave.ServiceAdmin.Implement.ReportServiceAdminImp;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Album;
import com.rhymthwave.entity.Report;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/report")
@RequiredArgsConstructor
public class API_Report {
	

	private final ReportServiceAdminImp reportServiceAdminImp;
	
	
	@Qualifier("sendNotificationOfNews")
	private final INotification<NewDTO> notification;

//	@GetMapping
//	public ResponseEntity<?> getAllReport() {
//		List<Report> list = reportServiceAdminImp.findAllReport();
//		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", list));
//	}
	
	@PutMapping("/seenreport/{reportId}")
	public ResponseEntity<?> seenReport(@PathVariable("reportId") Integer reportId){
		reportServiceAdminImp.seenReport(reportId);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
		
	}
	@PostMapping("/sendemailwarring/{email}")
	public ResponseEntity<?> sendEmailWarring(@PathVariable("email") String email) throws UnsupportedEncodingException, MessagingException{
		notification.sendEmailWarring(email);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
		
	}
	@PutMapping("/banArtist/{artistId}")
	public ResponseEntity<?> ban(@PathVariable("artistId") Long artistId) throws UnsupportedEncodingException, MessagingException{
		reportServiceAdminImp.banArtist(artistId);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
		
	}
	@PutMapping("/banEpisodes/{episodeId}")
	public ResponseEntity<?> banEpisodes(@PathVariable("episodeId") Long episodeId) throws UnsupportedEncodingException, MessagingException{
		reportServiceAdminImp.banEpisodes(episodeId);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
		
	}
	@PutMapping("/banrecording/{recordingId}")
	public ResponseEntity<?> banRecording(@PathVariable("recordingId") Long recordingId) throws UnsupportedEncodingException, MessagingException{
		reportServiceAdminImp.banrecordingId(recordingId);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
		
	}
	@PutMapping("/banpodcast/{podcastId}")
	public ResponseEntity<?> banPodcast(@PathVariable("podcastId") Long podcastId) throws UnsupportedEncodingException, MessagingException{
		reportServiceAdminImp.banPodcast(podcastId);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
		
	}
	@PutMapping("/unbanArtist/{artistId}")
	public ResponseEntity<?> unBan(@PathVariable("artistId") Long artistId) throws UnsupportedEncodingException, MessagingException{
		reportServiceAdminImp.unbanArtist(artistId);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
		
	}
//	@PutMapping("/deletereport/{reportId}")
//	public ResponseEntity<?> deleteReport(@PathVariable("reportId") Long reportId) throws UnsupportedEncodingException, MessagingException{
//		reportServiceAdminImp.deleteReport(reportId);
//		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
//		
//	}
	@PutMapping("/unbanEpisodes/{episodeId}")
	public ResponseEntity<?> unbanEpisodes(@PathVariable("episodeId") Long episodeId) throws UnsupportedEncodingException, MessagingException{
		reportServiceAdminImp.unbanEpisodes(episodeId);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
		
	}
	@PutMapping("/unbanrecording/{recordingId}")
	public ResponseEntity<?> unbanRecording(@PathVariable("recordingId") Long recordingId) throws UnsupportedEncodingException, MessagingException{
		reportServiceAdminImp.unbanrecordingId(recordingId);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
		
	}
	@PutMapping("/unbanpodcast/{podcastId}")
	public ResponseEntity<?> unbanPodcast(@PathVariable("podcastId") Long podcastId) throws UnsupportedEncodingException, MessagingException{
		reportServiceAdminImp.unbanPodcast(podcastId);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", null));
		
	}
}
