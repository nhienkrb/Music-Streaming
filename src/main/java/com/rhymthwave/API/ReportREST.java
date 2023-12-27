package com.rhymthwave.API;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.UserTypeService;
import com.rhymthwave.Service.Implement.ReportServiceImpl;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Artist;
import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Podcast;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.Report;
import com.rhymthwave.entity.UserType;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReportREST {

	@Autowired
	ReportServiceImpl reportService;

	@Autowired
	GetHostByRequest getHostByRequest;

	@Autowired
	UserTypeService userTypeService;

	private final CRUD<Account, String> crudAccount;
	private final CRUD<Artist, Long> crudArtist;
	private final CRUD<Episode, Long> crudEpisode;
	private final CRUD<Podcast, Long> crudPodcast;
	private final CRUD<Recording, Long> crudRecording;
	private final CRUD<UserType, Long> crudUserType;

	@PostMapping(value = "/api/v1/report/{option}/{id}")
	public ResponseEntity<MessageResponse> createReport(HttpServletRequest req, @PathVariable("option") String option,
			@PathVariable("id") Long id, @RequestParam("description") String description) {
		Report report = new Report();
		report.setDescription(description);
		report.setReportDate(new Date());
		report.setStatus(false);
		Account account = crudAccount.findOne(getHostByRequest.getEmailByRequest(req));
		List<UserType> ustList = account.getUserType();
		Long userTypeId = (long) 0;
		for (UserType ust : ustList) {
			userTypeId = (long) ust.getUserTypeId();
			break;
		}
		System.out.println("userTypeId: " + userTypeId);
		UserType userType = crudUserType.findOne(userTypeId);
		report.setUsertype(userType);
		switch (option.trim()) {
		case "artist":
			Artist artist = crudArtist.findOne(id);
			report.setArtist(artist);
			break;
		case "episode":
			Episode episode = crudEpisode.findOne(id);
			report.setEpisode(episode);
			break;
		case "podcast":
			Podcast podcast = crudPodcast.findOne(id);
			report.setPodcast(podcast);

			break;
		case "recording":
			Recording recording = crudRecording.findOne(id);
			report.setRecording(recording);
			break;
		}

		return ResponseEntity.ok(new MessageResponse(true, "succeess", reportService.create(report)));
	}
}
