package com.rhymthwave.API;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.MonitorEpisodeService;
import com.rhymthwave.entity.MonitorEpisode;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class MonitorEpisodeREST {
	
	private final CRUD<MonitorEpisode, Long> crudMonitorEpisode;
	
	private final MonitorEpisodeService monitorEpSer;
	
	@GetMapping("/api/v1/monitor-episode/{id}")
	public ResponseEntity<MessageResponse> findMonitorId(@PathVariable("id") Long id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudMonitorEpisode.findOne(id)));
	}
	
	@GetMapping("/api/v1/monitor-episode-podcast/{id}")
	public ResponseEntity<MessageResponse> findMonitorPodcast(@PathVariable("id") Long id){
		return ResponseEntity.ok(new MessageResponse(true,"success",monitorEpSer.findMonitorEpisodeByPodcast(id)));
	}
	
	@PostMapping("/api/v1/monitor-episode")
	public ResponseEntity<MessageResponse> createMonitor(@RequestBody MonitorEpisode monitor){
		if(monitorEpSer.checkExist(monitor.getEpisode(), monitor.getAccount())==null) {
			return ResponseEntity.ok(new MessageResponse(true,"success",crudMonitorEpisode.create(monitor)));
		}
		return ResponseEntity.ok(new MessageResponse(false,"success","existed"));
	}
	
	@DeleteMapping("/api/v1/monitor-episode/{id}")
	public ResponseEntity<MessageResponse> deleteMonitor(@PathVariable("id") Long id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudMonitorEpisode.delete(id)));
	}
	
	@GetMapping("/api/v1/monitor-episode/age")
	public ResponseEntity<MessageResponse> MonitorAge(@RequestParam("id") List<Long> episodeId, @RequestParam("duration") Integer duration){
		return ResponseEntity.ok(new MessageResponse(true,"success", monitorEpSer.resultMonitorAgeEpisode(episodeId, duration)));
	}
	
	@GetMapping("/api/v1/monitor-episode/country")
	public ResponseEntity<MessageResponse> MonitorCountry(@RequestParam("id") List<Long> episodeId, @RequestParam("duration") Integer duration){
		return ResponseEntity.ok(new MessageResponse(true,"success", monitorEpSer.resultMonitorCountryEpisode(episodeId,duration)));
	}
	
	@GetMapping("/api/v1/monitor-episode/gender")
	public ResponseEntity<MessageResponse> MonitorGender(@RequestParam("id") List<Long> episodeId, @RequestParam("duration") Integer duration){
		return ResponseEntity.ok(new MessageResponse(true,"success", monitorEpSer.resultMonitorGenderEpisode(episodeId, duration)));
	}
	
	@GetMapping("/api/v1/monitor-episode/fan-also-liked")
	public ResponseEntity<MessageResponse> MonitorAlsoLiked(@RequestParam("listEpisode") List<Long> listEpisode, @RequestParam("duration") Integer duration){
		return ResponseEntity.ok(new MessageResponse(true,"success", monitorEpSer.getFanAlsoLiked(listEpisode, duration)));
	}
}
