package com.rhymthwave.Service;

import java.util.List;

import com.rhymthwave.DTO.AnalysisDTO;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Monitor;
import com.rhymthwave.entity.Recording;

public interface MonitorService {
	Monitor checkExist(Recording recording, Account account);
	
	List<AnalysisDTO> resultMonitorAgeRecording(List<Long> recordingid, Integer dateMonitor);
	
	List<AnalysisDTO> resultMonitorGenderRecording(List<Long> recordingid, Integer dateMonitor);
	
	List<AnalysisDTO> resultMonitorCountryRecording(List<Long> recordingid, Integer dateMonitor);
	
	List<Monitor> getNewListener(Long recordingId, Integer date);
	
	List<Object[]> getFanAlsoLiked(List<Long> listRecord, Integer date);
}
