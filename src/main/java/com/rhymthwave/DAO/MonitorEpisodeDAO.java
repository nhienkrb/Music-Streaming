package com.rhymthwave.DAO;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.DTO.AnalysisDTO;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Monitor;
import com.rhymthwave.entity.MonitorEpisode;

@Repository
public interface MonitorEpisodeDAO extends JpaRepository<MonitorEpisode, Long>{
	MonitorEpisode findByEpisodeAndAccount(Episode episode, Account account);
	
	@Query("select o from MonitorEpisode o where o.episode.podcast.podcastId = :id")
	List<MonitorEpisode> findMonitorEpisodeByPodcast(@Param("id") Long id);
	
	@Query(value ="select count(*) as quantity, (YEAR(GETDATE()) - YEAR(a.birthday)) as age from monitorepisode m "
			+ "join accounts a on a.email= m.account "
			+ "join episodes ep on ep.episodesid = m.episodeid "
			+ "where ep.episodesid in :episodeid and  GETDATE() - :dateMonitor <= m.datemonitor and m.datemonitor <= GETDATE() "
			+ "group by (YEAR(GETDATE()) - YEAR(a.birthday)) order by quantity desc",nativeQuery = true)
	List<AnalysisDTO> analysisEpisodeAge(@Param("episodeid") List<Long> episodeid, @Param("dateMonitor") Integer dateMonitor);
	
	@Query(value ="select count(*) as quantity, a.country as country from monitorepisode m "
			+ "join accounts a on a.email= m.account "
			+ "join episodes ep on ep.episodesid = m.episodeid "
			+ "where ep.episodesid in :episodeid and  GETDATE() - :dateMonitor <= m.datemonitor and m.datemonitor <= GETDATE() "
			+ "group by a.country order by quantity desc",nativeQuery = true)
	List<AnalysisDTO> analysisEpisodeCountry(@Param("episodeid") List<Long> episodeid, @Param("dateMonitor") Integer dateMonitor);
	
	@Query(value ="select count(*) as quantity, a.gender as gender from monitorepisode m "
			+ "join accounts a on a.email= m.account "
			+ "join episodes ep on ep.episodesid = m.episodeid "
			+ "where ep.episodesid in :episodeid and  GETDATE() - :dateMonitor <= m.datemonitor and m.datemonitor <= GETDATE() "
			+ "group by a.gender order by quantity desc",nativeQuery = true)
	List<AnalysisDTO> analysisEpisodeGender(@Param("episodeid") List<Long> episodeid, @Param("dateMonitor") Integer dateMonitor);
	
	@Query(value="select top 10 * from monitorepisode where episodeid = :episodesid and "
			+ "datemonitor >= GETDATE() - :date order by datemonitor desc",nativeQuery = true)
	List<MonitorEpisode> getNewListener(@Param("episodesid") Long episodeId, @Param("date") Integer date);
	
	@Query("SELECT a, COUNT(m) " +
	           "FROM MonitorEpisode m " +
	           "JOIN m.account a " +
	           "WHERE m.episode.episodeId IN :listEpisode " +
	           "AND m.dateMonitor >= GETDATE() - :date "+
	           "GROUP BY a " +
	           "ORDER BY COUNT(m) DESC")
	List<Object[]> findAccountFrequency(@Param("listEpisode") List<Long> listEpisode,@Param("date") Integer date, Pageable pageable);
}
