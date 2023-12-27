package com.rhymthwave.Service;

import java.util.List;

import com.rhymthwave.DTO.AnalysisDTO;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Monitor;
import com.rhymthwave.entity.MonitorEpisode;

public interface MonitorEpisodeService {
	MonitorEpisode checkExist(Episode episode, Account account);
	
	List<MonitorEpisode> findMonitorEpisodeByPodcast(Long id);
	
	List<AnalysisDTO> resultMonitorAgeEpisode(List<Long> episodeId, Integer dateMonitor);
	
	List<AnalysisDTO> resultMonitorGenderEpisode(List<Long> episodeId, Integer dateMonitor);
	
	List<AnalysisDTO> resultMonitorCountryEpisode(List<Long> episodeId, Integer dateMonitor);
	
	List<MonitorEpisode> getNewListener(Long episodeId, Integer date);
	
	List<Object[]> getFanAlsoLiked(List<Long> listEpisode, Integer date);
}
