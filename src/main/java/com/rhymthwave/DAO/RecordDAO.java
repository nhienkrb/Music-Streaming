package com.rhymthwave.DAO;

import com.rhymthwave.DTO.NumberCreateRecordAndEpisodeByDate;
import com.rhymthwave.entity.Recording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RecordDAO extends JpaRepository<Recording, Long> {
	@Query("Select o from Recording o where o.song is null and o.emailCreate = :creater")
	List<Recording> getListRecordNotRaw(@Param("creater") String creater);

	@Query("select o from Recording o where o.song is not null and o.emailCreate =:creater")
	List<Recording> getListRawRecord(@Param("creater") String creater);

	@Query("select o from Recording o where o.song.songId = :songId")
	List<Recording> getListRecordBySong(@Param("songId") Long songId);

	@Query("Select o from Recording o where o.emailCreate = :creater and o.isDeleted = false")
	List<Recording> getRecordByCreater(@Param("creater") String creater);

	@Query("Select o from Recording o where o.isDeleted = true and o.emailCreate = :creater")
	List<Recording> getRecordDelete(@Param("creater") String creater);
	
	@Query("Select o from Recording o where o.emailCreate = :creater and o.recordingdate >= GETDATE() - :date")
	List<Recording> statisticsByDate(@Param("creater") String creater, @Param("date") Integer date);

	@Query(value = "SELECT r.* FROM RECORDING r join SONGS s on r.SONGSID = s.SONGSID ORDER BY NEWID()", nativeQuery = true)
	List<Recording> findListRandom();

	@Query(value = "SELECT recording.* FROM recording INNER JOIN songs s ON recording.songsid = s.songsid WHERE s.songname LIKE %:songName% AND s.isdeleted = 0 AND s.realeaseday < GETDATE()", nativeQuery = true)
	List<Recording> findSongPl(@Param("songName") String songName);

	@Query(value = "select * from recording where recordingid = :recordingId", nativeQuery = true)
	Recording findAllById(@Param("recordingId") Long recordingId);


	@Query(value = "select top 50 r.* from recording r "
			+ "join songs s on r.songsid = s.songsid "
			+ "join songgenre sg on r.recordingid = sg.idrecord join genre g on g.id = sg.idgenre "
			+ "where g.namegenre in (:nameGenre) or r.culture like :culture or r.instrument like :instrument "
			+ "or r.mood like :mood or r.songstyle like :songstyle " 
			+ "and s.realeaseday < GETDATE() and r.isdeleted = 0 "
			+ "group by r.recordingid, r.audiofileurl,r.culture,r.instrument,r.emailcreate, r.recordingname,r.recordingdate,r.songstyle,r.duration, "
			+ "r.idmv, r.isdeleted,r.listened,r.lyricsurl,r.mood, r.produce,r.publicidaudiofile,r.publicidlyrics,r.studio,r.versions,r.songsid "
			+ "ORDER BY NEWID();", nativeQuery = true)
	List<Recording> findListRecordRandom(@Param("nameGenre") String nameGenre, @Param("culture") String culture,
			@Param("instrument") String instrument, @Param("mood") String mood, @Param("songstyle") String songstyle);

	@Query(value="select top 50 r.* from recording r "
			+ "join songs s on r.songsid = s.songsid "
			+ "join songgenre sg on r.recordingid = sg.idrecord "
			+ "join genre g on g.id = sg.idgenre "
			+ "where g.namegenre in (:nameGenre) "
			+ "or r.culture like :culture or r.instrument like :instrument "
			+ "or r.mood like :mood or r.songstyle like :songstyle "
			+ "or r.versions like :versions "
			+ "and s.realeaseday < GETDATE() and r.isdeleted = 0 "
			+ "group by r.recordingid, r.audiofileurl,r.culture,r.instrument,r.emailcreate, r.recordingname,r.recordingdate,r.songstyle,r.duration, "
			+ "r.idmv, r.isdeleted,r.listened,r.lyricsurl,r.mood, r.produce,r.publicidaudiofile,r.publicidlyrics,r.studio,r.versions,r.songsid "
			+ "ORDER BY NEWID();",nativeQuery = true)
	
	List<Recording> findListRandomFavorite(@Param("nameGenre") String nameGenre, @Param("culture") String culture
										,@Param("instrument") String instrument,@Param("mood") String mood
										,@Param("songstyle") String songstyle,@Param("versions") String versions);
	
	@Query(value="select RECORDING.* from RECORDING "
			+ "join SONGS on RECORDING.SONGSID = SONGS.SONGSID "
			+ "join writter w on w.songsid = songs.songsid "
			+ "join artist a on a.artistid = w.artistid "
			+ "where a.artistid= :artistid and a.artistid != songs.artistcreate",nativeQuery = true)
	List<Recording> getMyProject(@Param("artistid") Long artistid);
	
	@Query(value="select r.* from recording r "
			+ "join songs s on s.songsid = r.songsid "
			+ "join writter w on w.songsid = r.songsid "
			+ "join artist a on a.artistid=w.artistid "
			+ "where a.artistid = :artistId and s.realeaseday < GETDATE() and r.isdeleted = 0 "
			+ "order by r.listened desc",nativeQuery = true)
	List<Recording> findListPopularByArtist(@Param("artistId") Long artistId);
	
	@Query(value="select RECORDING.* from RECORDING "
			+ "join SONGS on RECORDING.SONGSID = SONGS.SONGSID "
			+ "join writter w on w.songsid = songs.songsid "
			+ "join artist a on a.artistid = w.artistid "
			+ "where a.artistid= :artistid and a.artistid != songs.artistcreate and songs.realeaseday < GETDATE() and RECORDING.isdeleted = 0",nativeQuery = true)
	List<Recording> findListAppearOn(@Param("artistid") Long artistid);
	
	@Query(value= "select top 50 r.* from artist a "
			+ "join accounts acc on a.email = acc.email "
			+ "join writter w on w.artistid = a.artistid "
			+ "join songs s on s.songsid = w.songsid "
			+ "join recording r on r.songsid = s.songsid "
			+ "where acc.country like :country and r.isdeleted = :deleted and s.realeaseday <= GETDATE() "
			+ "order by r.listened desc",nativeQuery = true)
	List<Recording> top50SongByAreaListened(@Param("country") String country,@Param("deleted") Boolean deleted);
	
	@Query(value= "select top 50 r.* from artist a "
			+ "join accounts acc on a.email = acc.email "
			+ "join writter w on w.artistid = a.artistid "
			+ "join songs s on s.songsid = w.songsid "
			+ "join recording r on r.songsid = s.songsid "
			+ "where acc.country like :country and r.isdeleted = :deleted and s.realeaseday <= GETDATE() "
			+ "order by s.realeaseday desc",nativeQuery = true)
	List<Recording> top50SongByDate(@Param("country") String country,@Param("deleted") Boolean deleted);


	@Query("select count(r.recordingId) from  Recording  r")
	int countRecording();

	@Query(value = "select top 100 * from recording order by listened desc", nativeQuery = true)
	List<Recording> findTop100ByOrderByListenedDesc();


	@Query(value = """
		    SELECT TOP 10 *
			FROM recording
			WHERE DATEDIFF(day, recordingdate, GETDATE()) <= :day
			AND songsid IS NOT NULL
			ORDER BY listened DESC
									""", nativeQuery = true)
	List<Recording> findTop10Trending( @Param("day") int day );

	@Query(value = "exec sp_getRecordingsCount :datetime", nativeQuery = true)
	List<NumberCreateRecordAndEpisodeByDate> countCreateRecordsByDay(@Param("datetime") String datetime );

}
