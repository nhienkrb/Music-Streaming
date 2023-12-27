package com.rhymthwave.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Playlist;
import com.rhymthwave.entity.UserType;

@Repository
public interface PlaylistDAO extends JpaRepository<Playlist, Long> {
	List<Playlist> findByUsertype(UserType usertype);

	@Query(value = "select playlists.* from usertype\r\n"
			+ "						join accounts on usertype.accountid = accounts.email\r\n"
			+ "						join author on accounts.email = author.email\r\n"
			+ "						join playlists on playlists.usertypeid = usertype.usertypeid\r\n"
			+ "						join playlist_recording on  playlist_recording.playlistsid = playlists.playlistid\r\n"
			+ "					where author.idrole = 4 or author.idrole = 5\r\n"
			+ "					group by playlists.playlistid, \r\n"
			+ "						playlists.createdate,\r\n" + "						playlists.description,\r\n"
			+ "						playlists.ispublic,\r\n" + "						playlists.playlistname,\r\n"
			+ "						playlists.quantity,\r\n" + "						playlists.image,\r\n"
			+ "						playlists.usertypeid", nativeQuery = true)
	List<Playlist> getplayListSongAdmin();

	@Query(value = "select playlists.* from usertype\r\n"
			+ "						join accounts on usertype.accountid = accounts.email\r\n"
			+ "						join author on accounts.email = author.email\r\n"
			+ "						join playlists on playlists.usertypeid = usertype.usertypeid\r\n"
			+ "						join playlist_podcast on  playlist_podcast.playlistid = playlists.playlistid\r\n"
			+ "					where author.idrole = 4 or author.idrole = 5\r\n"
			+ "					group by playlists.playlistid, \r\n"
			+ "						playlists.createdate,\r\n" + "						playlists.description,\r\n"
			+ "						playlists.ispublic,\r\n" + "						playlists.playlistname,\r\n"
			+ "						playlists.quantity,\r\n" + "						playlists.image,\r\n"
			+ "						playlists.usertypeid", nativeQuery = true)
	List<Playlist> getPodcastPlayListAdmin();

	List<Playlist> findByUsertypeAndIsPublic(UserType usertype, Boolean isPublic);

	@Query(value = "select pl.* from playlists pl " + "join playlist_recording plr on pl.playlistid = plr.playlistsid "
			+ "join recording r on r.recordingid = plr.recordingid " + "join songs s on s.songsid = r.songsid "
			+ "join writter w on w.songsid = s.songsid " + "join artist a on w.artistid = a.artistid "
			+ "join accounts acc on acc.email = a.email " + "join author auth on auth.email = acc.email "
			+ "where w.artistid = :artistId and auth.idrole in :roleId and pl.ispublic = 1 "
			+ "group by pl.playlistid, pl.playlistname,pl.createdate,pl.description,pl.ispublic,pl.quantity,pl.image,pl.usertypeid", nativeQuery = true)
	List<Playlist> findPlaylistFeaturingByArtist(@Param("artistId") Long artistId,
			@Param("roleId") List<Integer> roleId);

	@Query(value = "select top 50 pl.* from playlists pl "
			+ "join playlist_recording plr on pl.playlistid = plr.playlistsid "
			+ "join recording r on r.recordingid = plr.recordingid " + "join songs s on s.songsid = r.songsid "
			+ "join writter w on w.songsid = s.songsid " + "join artist a on w.artistid = a.artistid "
			+ "join accounts acc on acc.email = a.email " + "join author auth on auth.email = acc.email "
			+ "where w.artistid = :artistId and auth.idrole in :roleId and pl.ispublic = 1 "
			+ "group by pl.playlistid, pl.playlistname,pl.createdate,pl.description,pl.ispublic,pl.quantity,pl.image,pl.usertypeid "
			+ "order by NEWID()", nativeQuery = true)
	List<Playlist> findPlaylistDiscoverByArtist(@Param("artistId") Long artistId,@Param("roleId") List<Integer> roleId);

	@Query(value = "select top 50 pl.* from playlists pl join usertype ust on pl.usertypeid = ust.usertypeid "
			+ "join accounts acc on acc.email = ust.accountid join author auth on auth.email = acc.email "
			+ "where auth.idrole in (4,5) and pl.ispublic= :public "
			+ "order by pl.createdate desc", nativeQuery = true)
	List<Playlist> top50PlaylistLatest(@Param("public") Boolean isPublic);

	@Query(value = "select top 50 pl.* from playlists pl "
			+ "join usertype ust on pl.usertypeid = ust.usertypeid "
			+ "join accounts acc on acc.email = ust.accountid "
			+ "join author auth on auth.email = acc.email "
			+ "join playlist_recording plr on plr.playlistsid = pl.playlistid "
			+ "join recording r on r.recordingid = plr.recordingid "
			+ "join songs s on s.songsid = r.songsid "
			+ "join songgenre sg on sg.idrecord = r.recordingid join genre g on sg.idgenre = g.id "
			+ "where auth.idrole in (4,5) and pl.ispublic= :public and (g.namegenre in :nameGenre "
			+ "or r.culture like :culture or r.instrument like :instrument "
			+ "or r.mood like :mood or r.songstyle like :songstyle or r.versions like :versions) "
			+ "group by pl.playlistid, pl.playlistname,pl.createdate,pl.description,pl.ispublic,pl.quantity,pl.image,pl.usertypeid "
			+ "order by pl.createdate desc", nativeQuery = true)
	List<Playlist> top50PlaylistRecentListen(@Param("public") Boolean isPublic, @Param("nameGenre") List<String> nameGenre,
			@Param("culture") String culture, @Param("instrument") String instrument, @Param("mood") String mood,
			@Param("songstyle") String songstyle,@Param("versions") String versions);

	@Query("select count(p.playlistId) from  Playlist  p")
	int countPlaylist();
}
