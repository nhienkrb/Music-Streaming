package com.rhymthwave.API_GraphQL_Admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.ServiceAdmin.IArtistService;
import com.rhymthwave.ServiceAdmin.IReportServiceAdmin;
import com.rhymthwave.entity.Artist;
import com.rhymthwave.entity.Report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class API_GraphQL_Report {

	private  final  IReportServiceAdmin iReportServiceAdmin;
		
	@QueryMapping("findAllReport")
	public List<Report> findAllReportFalse() {
		List<Report> reports = iReportServiceAdmin.findAllReport();
	    
	    // Filter reports with status=false
	    List<Report> filteredReports = reports.stream()
	            .filter(report -> !report.isStatus()) // Assuming status is a boolean field
	            .collect(Collectors.toList());

	    return filteredReports;
	}
	
	@QueryMapping("findAllReportStatusTrue")
	public List<Report> findAllReportTrue() {
		List<Report> reports = iReportServiceAdmin.findAllReport();
	    
	    // Filter reports with status=false
	    List<Report> filteredReports = reports.stream()
	            .filter(report -> report.isStatus()) // Assuming status is a boolean field
	            .collect(Collectors.toList());

	    return filteredReports;
	}

}
