package com.rhymthwave.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rhymthwave.entity.Report;

public interface ReportDAO extends JpaRepository<Report, Integer>{
	
	
	@Query("SELECT COUNT(r.usertype.userTypeId) AS Reports FROM Report r where r.usertype.account.email = ?1")
	int countReportByAccount(String idAccount);




}
