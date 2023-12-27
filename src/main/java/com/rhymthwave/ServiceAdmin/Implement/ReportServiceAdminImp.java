package com.rhymthwave.ServiceAdmin.Implement;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.DAO.ArtistDAO;
import com.rhymthwave.DAO.EpisodeDAO;
import com.rhymthwave.DAO.PodcastDAO;
import com.rhymthwave.DAO.RecordDAO;
import com.rhymthwave.DAO.ReportDAO;
import com.rhymthwave.Request.DTO.NewDTO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.ServiceAdmin.INotification;
import com.rhymthwave.ServiceAdmin.IReportServiceAdmin;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Artist;
import com.rhymthwave.entity.Email;
import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Podcast;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.Report;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceAdminImp implements IReportServiceAdmin, CRUD<Report, Integer> {
	
	private final ReportDAO reportDAO;
	
	private final AccountDAO accountDAO;
	
	private final ArtistDAO artistDAO;
	
	private final EpisodeDAO episodeDAO;
	
	private final RecordDAO recordDAO;
	
	private final PodcastDAO podcastDAO;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Qualifier("sendNotificationOfNews")
	private final INotification<NewDTO> notification;
	
	
	public List<Report> findAllReport() {
		// TODO Auto-generated method stub
		return reportDAO.findAll();
	}

	public void seenReport(Integer reportId) {
		Report newReport = reportDAO.getById(reportId);
		newReport.setStatus(true);
		reportDAO.save(newReport);
	}

	@Override
	public Report create(Report entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Report update(Report entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Report findOne(Integer key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Report> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendEmailWarring(String email) throws MessagingException, UnsupportedEncodingException{
		String subject = "Email Warning";
		String senderName = "Notify users that they have violated community rules";
		String mailContent = "This is a warning email that if the user continues to violate, he or she will be permanently banned";
		MimeMessage message = mailSender.createMimeMessage();
		var messageHelper = new MimeMessageHelper(message);
		messageHelper.setFrom("musicstreaming2023@gmail.com", senderName);
		messageHelper.setTo(email);
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
		
	}

	public void banArtist(Long artistId) throws UnsupportedEncodingException, MessagingException {
		Artist newArtist = artistDAO.findbyID(artistId);
		newArtist.setActive(false);
		newArtist.setExpirePermission(new Date());
		artistDAO.save(newArtist);
		Account newAccount = newArtist.getAccount();
		newAccount.setBlocked(true);
		accountDAO.save(newAccount);
//		sendEmailBan(newAccount.getEmail());
		notification.sendEmailBan(newArtist.getAccount().getEmail(),"Your Account Artist has been banned");
	}

	public void sendEmailBan(String email) throws MessagingException, UnsupportedEncodingException {
		

		String subject = "Email Band Account";
		String senderName = "Baned Account";
		String mailContent = "You violated the community rules so we decided to lock your account";
		MimeMessage message = mailSender.createMimeMessage();
		var messageHelper = new MimeMessageHelper(message);
		messageHelper.setFrom("musicstreaming2023@gmail.com", senderName);
		messageHelper.setTo(email);
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
	}

	public void banEpisodes(Long episodeId) throws UnsupportedEncodingException, MessagingException {
		Episode newEpisode = episodeDAO.findAllById(episodeId);
		newEpisode.setPublic(false);
		episodeDAO.save(newEpisode);
		Podcast newPodcast = newEpisode.getPodcast();
		Account email = newPodcast.getAccount();
//		sendEmailBan(email.getEmail());
		notification.sendEmailBan(email.getEmail(),"Your Episodes has been banned");
	}

	public void banrecordingId(Long recordingId) throws UnsupportedEncodingException, MessagingException {
		Recording newRecording = recordDAO.findAllById(recordingId);
		newRecording.setIsDeleted(true);
		recordDAO.save(newRecording);
		notification.sendEmailBan(newRecording.getEmailCreate(),"Your Record has been banned");
		
	}

	public void banPodcast(Long podcastId) throws UnsupportedEncodingException, MessagingException {
		Podcast newPodcast = podcastDAO.findByID(podcastId);
		Account newAccount = accountDAO.findByEmail(newPodcast.getAccount().getEmail());
		newAccount.setBlocked(true);
		accountDAO.save(newAccount);	
		notification.sendEmailBan(newAccount.getEmail(),"Your Podcast has been banned");
	}

	public void unbanArtist(Long artistId) {
		Artist newArtist = artistDAO.findbyID(artistId);
		newArtist.setActive(true);
		artistDAO.save(newArtist);
		Account newAccount = newArtist.getAccount();
		newAccount.setBlocked(false);
		accountDAO.save(newAccount);	
		notification.sendEmailBan(newArtist.getAccount().getEmail(),"Your Account Artist has been unbanned");
		
	}

	public void unbanEpisodes(Long episodeId) {
		Episode newEpisode = episodeDAO.findAllById(episodeId);
		newEpisode.setDelete(false);
		newEpisode.setPublic(true);
		episodeDAO.save(newEpisode);
		Podcast newPodcast = newEpisode.getPodcast();
		Account email = newPodcast.getAccount();
//		sendEmailBan(email.getEmail());
		notification.sendEmailBan(email.getEmail(),"Your Episodes has been unbanned");
		
	}

	public void unbanrecordingId(Long recordingId) {
		Recording newRecording = recordDAO.findAllById(recordingId);
		newRecording.setIsDeleted(false);
		recordDAO.save(newRecording);
		notification.sendEmailBan(newRecording.getEmailCreate(),"Your Record has been banned");
		
	}

	public void unbanPodcast(Long podcastId) {
		Podcast newPodcast = podcastDAO.findByID(podcastId);
		Account newAccount = accountDAO.findByEmail(newPodcast.getAccount().getEmail());
		newAccount.setBlocked(false);
		accountDAO.save(newAccount);
		notification.sendEmailBan(newAccount.getEmail(),"Your Podcast has been unbanned");
		
	}

}
