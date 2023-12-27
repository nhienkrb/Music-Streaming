package com.rhymthwave.Service.Implement;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.ReportDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.ReportService;
import com.rhymthwave.entity.Report;

import jakarta.transaction.Transactional;

@Service
public class ReportServiceImpl implements CRUD<Report, Long>, ReportService {

	@Autowired
	ReportDAO dao;

	@Override
	public Report create(Report entity) {
		if (entity != null) {
			entity.setReportDate(new Date());
			Report reportData = dao.save(entity);
			return reportData;
		}
		return null;
	}

	@Override
	public Report update(Report entity) {
		return null;
	}

	@Override
	public Boolean delete(Long key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Report findOne(Long key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Report> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
