package com.rhymthwave.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.DTO.checkPodcastRole;
import com.rhymthwave.Request.DTO.Top3PodcastDTO;
import com.rhymthwave.entity.Podcast;
@Repository
public interface PodcastDAO extends JpaRepository<Podcast, Long>{
	@Query("select o from Podcast o where o.account.email = :email")
	List<Podcast> findMyPobcast(@Param("email") String email);
	
	@Query(value="select top 50 p.* from podcast p "
			+ "join accounts acc on acc.email = p.accountid "
			+ "where p.releasedate <= GETDATE() and acc.country like :country  "
			+ "and EXISTS (SELECT 1 FROM episodes ep WHERE ep.podcastid = p.podcastid) "
			+ "order by p.releasedate desc",nativeQuery = true)
	List<Podcast> top50NewPodcast(@Param("country") String country);
	
	@Query(value="select top 50 p.* from podcast p "
			+ "join accounts acc on acc.email = p.accountid "
			+ "join episodes ep on ep.podcastid = p.podcastid "
			+ "where p.releasedate <= GETDATE() and acc.country like :country and EXISTS (SELECT 1 FROM episodes ep WHERE ep.podcastid = p.podcastid) "
			+ "group by p.accountid, p.authorname, p.bio, p.category, p.imgageid, p.language, p.podcastid, p.podcastname, p.rate, "
			+ "p.releasedate, p.socialmedialink "
			+ "order by sum(ep.listened) desc",nativeQuery = true)
	List<Podcast> top50PodcastPopular(@Param("country") String country);

	
	@Query(value = "select * from podcast where podcastid = :podcastId", nativeQuery = true)
	Podcast findByID(Long podcastId);


	@Query("select count(p.podcastId) from  Podcast  p")
	int countPodcast();

	@Query(value="select au.authorid, au.idrole\r\n"
			+ "from author au \r\n"
			+ "inner join accounts a on a.email = au.email\r\n"
			+ "where a.email = :email and au.idrole = 3 and a.isblocked = 0 and a.isverify = 1", nativeQuery = true)
	checkPodcastRole CheckPodcastRole(String email);
	
	@Query(value="select top 3  a.email, a.usename, a.imageid, sum(e.listened) as totallistened \r\n"
			+ "from episodes e\r\n"
			+ "join podcast p on p.podcastid = e.podcastid\r\n"
			+ "join accounts a on a.email = p.accountid\r\n"
			+ "where e.isdeleted = 0\r\n"
			+ "group by a.email, a.usename, a.imageid\r\n"
			+ "order by totallistened desc",nativeQuery = true)
	List<Top3PodcastDTO> top3Podcast();
}
