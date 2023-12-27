	package com.rhymthwave.DAO;

import com.rhymthwave.Request.DTO.Top10ArtistDTO;
import com.rhymthwave.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistDAO extends JpaRepository<Artist, Long>{
	@Query("Select o from Artist o where o.account.email = :email")
	Artist findByEmail(@Param("email") String email);
	
	@Query("Select o from Artist o where o.isVerify = :verify")
	List<Artist> findAllIsVerify(@Param("verify") Boolean verify);
	
	@Query("select o from Artist o where o.artistId <> :id and o.artistName like :artistName and o.isVerify = true")
	List<Artist> findAllArtistVerify(@Param("id") Long id, @Param("artistName") String artistName);
	

	@Query(value = "select * from artist where artistid= :artistId", nativeQuery = true)
	Artist findbyID(@Param("artistId") Long artistId);


	@Query(value = "select art.*, imgart.url from Artist art left join images imgart on art.profileimage = imgart.accessid where art.artistName like %:keyword%",nativeQuery = true)
	List<Object> findByName(@Param("keyword") String keyword);

	@Query(value = "exec sp_Statistic_forManagerArtist :idAccount",nativeQuery = true)
	Object totalAlbumAndSong(@Param("idAccount") String idAccount);
	
	@Query(value = "select SUM(recording.listened) from recording left join songs on  songs.songsid = recording.songsid\r\n"
			+ "						left join writter on writter.songsid = songs.songsid\r\n"
			+ "						left join artist on artist.artistid = writter.artistid\r\n"
			+ "						where artist.email = ?1", nativeQuery = true)
	Long sumListenedArtist(String idAccount);
	
	@Query("SELECT COUNT(*) FROM Follow f WHERE f.authorsAccountB.authorId = ?1")
	int countFollowerArtist(Long author);
	
	@Query(value="select top 50 a.* from artist a "
			+ "join accounts acc on a.email = acc.email "
			+ "join writter w on w.artistid = a.artistid "
			+ "join songs s on s.songsid = w.songsid "
			+ "join recording r on r.songsid = s.songsid "
			+ "where acc.country like :country and a.active = :active and a.verify = :verify "
			+ "group by a.active, a.artistid ,a.artistname,a.backgroudimage,a.bio, a.dateofbirth, a.datestarted, a.email, a.expirepermission, a.fullname, "
			+ "a.imagegallery, a.placeofbirth, a.profileimage, a.publicidimagegallery, a.socialmedialinks, a.verify "
			+ "order by sum(r.listened) desc",nativeQuery=true)
	List<Artist> top50ArtistByListener(@Param("country") String country, @Param("active") Boolean active, @Param("verify") Boolean verify);
	
	@Query("SELECT f.authorsAccountB.account.artist FROM Follow f "
			+ "where f.authorsAccountB.role.roleId = :role and f.authorsAccountB.account.country like :country and f.authorsAccountB.account.artist.isVerify = :verify "
			+ "ORDER BY COUNT(f.authorsAccountB) desc")
	List<Artist> top50ArtistByFollow(@Param("role") Integer role, @Param("country") String country, @Param("verify") Boolean verify);


	@Query(value = """
			SELECT top 10  a.artistid, a.artistname,i.url, a.email, SUM(r.listened) AS totalListened\s
  			       FROM Recording r\s
  			       JOIN songs s on s.songsid = r.songsid
  			       JOIN writter w on w.songsid = s.songsid
  			       JOIN artist a on a.artistid = w.artistid
  				   left JOIN images i on i.accessid = a.profileimage
  			      WHERE r.isDeleted = 0
  			      GROUP BY a.artistid, a.artistname,i.url, a.email
  			      ORDER BY totalListened DESC
					""",nativeQuery = true)
	List<Top10ArtistDTO> top10ArtistByListened();
	
	@Query(value = "SELECT top 3  a.artistid, a.artistname,a.profileimage, a.email, SUM(r.listened) AS totalListened \n" +
			"       FROM Recording r \n" +
			"       JOIN songs s on s.songsid = r.songsid\n" +
			"       JOIN writter w on w.songsid = s.songsid\n" +
			"\t     JOIN artist a on a.artistid = w.artistid\n" +
			"       WHERE r.isDeleted = 0\n" +
			"       GROUP BY a.artistid, a.artistname,a.profileimage, a.email" +
			"       ORDER BY totalListened DESC",nativeQuery = true)
	List<Top10ArtistDTO> top3ArtistByListened();
}
