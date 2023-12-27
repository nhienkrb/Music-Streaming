package com.rhymthwave.DAO;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.DTO.AnalysisDTO;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Monitor;
import com.rhymthwave.entity.Recording;

@Repository
public interface MonitorDAO extends JpaRepository<Monitor, Long>{
	Monitor findByRecordingAndAccount(Recording recording, Account account);
	
	@Query(value ="select count(*) as quantity, (YEAR(GETDATE()) - YEAR(a.birthday)) as age from monitor m "
			+ "join accounts a on a.email= m.account "
			+ "join recording r on r.recordingid = m.recordingid "
			+ "where r.recordingid in :recordingid and  GETDATE() - :dateMonitor <= m.datemonitor and m.datemonitor <= GETDATE() "
			+ "group by (YEAR(GETDATE()) - YEAR(a.birthday)) order by quantity desc",nativeQuery = true)
	List<AnalysisDTO> analysisRecordingAge(@Param("recordingid") List<Long> recordingid, @Param("dateMonitor") Integer dateMonitor);
	
	@Query(value ="select count(*) as quantity, a.country as country from monitor m "
			+ "join accounts a on a.email= m.account "
			+ "join recording r on r.recordingid = m.recordingid "
			+ "where r.recordingid in :recordingid and  GETDATE() - :dateMonitor <= m.datemonitor and m.datemonitor <= GETDATE() "
			+ "group by a.country order by quantity desc",nativeQuery = true)
	List<AnalysisDTO> analysisRecordingCountry(@Param("recordingid") List<Long> recordingid, @Param("dateMonitor") Integer dateMonitor);
	
	@Query(value ="select count(*) as quantity, a.gender as gender from monitor m "
			+ "join accounts a on a.email= m.account "
			+ "join recording r on r.recordingid = m.recordingid "
			+ "where r.recordingid in :recordingid and  GETDATE() - :dateMonitor <= m.datemonitor and m.datemonitor <= GETDATE() "
			+ "group by a.gender order by quantity desc",nativeQuery = true)
	List<AnalysisDTO> analysisRecordingGender(@Param("recordingid") List<Long> recordingid, @Param("dateMonitor") Integer dateMonitor);
	
	@Query(value="select top 10 * from monitor where recordingid = :recordingId and "
				+ "datemonitor >= GETDATE() - :date order by datemonitor desc",nativeQuery = true)
	List<Monitor> getNewListener(@Param("recordingId") Long recordingId, @Param("date") Integer date);
	
	@Query("SELECT a, COUNT(m) " +
	           "FROM Monitor m " +
	           "JOIN m.account a " +
	           "WHERE m.recording.recordingId IN :listRecord " +
	           "AND m.dateMonitor >= GETDATE() - :date "+
	           "GROUP BY a " +
	           "ORDER BY COUNT(m) DESC")
	List<Object[]> findAccountFrequency(@Param("listRecord") List<Long> listRecord,@Param("date") Integer date, Pageable pageable);
	
}
