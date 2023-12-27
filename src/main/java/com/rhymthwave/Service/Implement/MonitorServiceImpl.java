package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.MonitorDAO;
import com.rhymthwave.DTO.AnalysisDTO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.MonitorService;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Monitor;
import com.rhymthwave.entity.Recording;

import jakarta.transaction.Transactional;

@Service
public class MonitorServiceImpl implements CRUD<Monitor, Long>, MonitorService{

	@Autowired
	MonitorDAO dao;
	
	@Override
	@Transactional
	public Monitor create(Monitor entity) {
		Monitor monitor = entity;
		return dao.save(monitor);
	}

	@Override
	@Transactional
	public Monitor update(Monitor entity) {
		Monitor monitor = entity;
		return dao.save(monitor);
	}

	@Override
	@Transactional
	public Boolean delete(Long key) {
		dao.deleteById(key);
		return true;
	}

	@Override
	public Monitor findOne(Long key) {
		return dao.findById(key).get();
	}

	@Override
	public List<Monitor> findAll() {
		return dao.findAll();
	}

	@Override
	public Monitor checkExist(Recording recording, Account account) {
		Monitor monitor = dao.findByRecordingAndAccount(recording, account);
		if(monitor!=null) {
			return monitor;
		}
		return null;
	}

	@Override
	public List<AnalysisDTO> resultMonitorAgeRecording(List<Long> recordingid, Integer dateMonitor) {
		return dao.analysisRecordingAge(recordingid, dateMonitor);
	}

	@Override
	public List<AnalysisDTO> resultMonitorGenderRecording(List<Long> recordingid, Integer dateMonitor) {
		return dao.analysisRecordingGender(recordingid, dateMonitor);
	}

	@Override
	public List<AnalysisDTO> resultMonitorCountryRecording(List<Long> recordingid, Integer dateMonitor) {
		return dao.analysisRecordingCountry(recordingid, dateMonitor);
	}

	@Override
	public List<Monitor> getNewListener(Long recordingId, Integer date) {
		return dao.getNewListener(recordingId, date);
	}

	@Override
	public List<Object[]> getFanAlsoLiked(List<Long> listRecord, Integer date) {
		Pageable pageable = PageRequest.of(0, 10);
		return dao.findAccountFrequency(listRecord,date,pageable);
	}
}
