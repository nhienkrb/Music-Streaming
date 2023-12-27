package com.rhymthwave.DAO;

import java.util.List;

import com.rhymthwave.DTO.NumberCreateRecordAndEpisodeByDate;
import com.rhymthwave.entity.Recording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Episode;

@Repository
public interface EpisodeDAO extends JpaRepository<Episode, Long>{
	@Query("select o from Episode o where o.podcast.podcastId = :podcastId and o.isDelete = :status")
	List<Episode> findAllEpisodeByPodcast(@Param("podcastId") Long podcastId,@Param("status") Boolean status);
	
	@Query(value="SELECT TOP 1 * FROM EPISODES WHERE PUBLISHDATE < GETDATE() AND ISPUBLIC= 1 AND ISDELETED = 0 AND PODCASTID = :podcastId ORDER BY PUBLISHDATE DESC",nativeQuery = true)
	Episode findLatestByPodcast(@Param("podcastId") Long podcastId);
	
	@Query(value = "select e.* from episodes e where e.episodestitle like %:keyword% and e.ispublic = 1 and e.publishdate < getdate() and e.isdeleted = 0",nativeQuery = true)
	List<Episode> findByName(@Param("keyword") String keyword);

	@Query(value ="select top 10  episodes.* from podcast \r\n"
			+ "						left join tags on podcast.category = tags.tagid\r\n"
			+ "						left join  episodes on podcast.podcastid = episodes.podcastid\r\n"
			+ "					where tags.nametag in (:tags) \r\n"
			+ "					and episodes.ispublic = 1  and episodes.publishdate < GETDATE()\r\n"
			+ "					ORDER BY NEWID()",nativeQuery = true)
	List<Episode> getRandomPodcasts(@Param("tags") List<String> tags);
	
	@Query(value = "select top 50 ep.* from episodes ep "
			+ "where ep.publishdate <= GETDATE() and ep.ispublic = :ispublic and ep.isdeleted = 0 "
			+ "and ep.podcastid in (:tags) "
			+ "group by ep.content, ep.descriptions, ep.duration, ep.episodesid, "
			+ "ep.episodestitle, ep.epnumber, ep.eptype, ep.fileurl, ep.imageep, ep.isdeleted, "
			+ "ep.ispublic, ep.listened, ep.podcastid, ep.publicidfile, ep.publishdate, ep.publicidfile, ep.sessionnumber "
			+ "order by NEWID()",nativeQuery = true)
	List<Episode> top50EpForYou(@Param("ispublic") Boolean ispublic,@Param("tags") List<Integer> tags);

	
	@Query(value = "select * from episodes where episodesid= :episodeId", nativeQuery = true)
	Episode findAllById(@Param("episodeId") Long episodeId);


	@Query("select count(e.episodeId) from  Episode e")
	int countEpisode();

	List<Episode> findTop100ByOrderByListenedDesc();

	@Query(value = "exec sp_getEpisodesCount :datetime", nativeQuery = true)
	List<NumberCreateRecordAndEpisodeByDate> countCreateEpisodeByDay(@Param("datetime") String day );
}
