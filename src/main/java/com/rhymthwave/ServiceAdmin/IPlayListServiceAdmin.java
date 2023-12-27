package com.rhymthwave.ServiceAdmin;

import java.util.List;

import com.rhymthwave.Request.DTO.UpdatePlaylistDTO;
import org.springframework.web.multipart.MultipartFile;

import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Playlist;
import com.rhymthwave.entity.Recording;

import jakarta.servlet.http.HttpServletRequest;

public interface IPlayListServiceAdmin {

	List<Recording> getListRecordRandom(String nameGenre,String culture,String mood,String songstyle,String instrument);

	List<Episode> getListEpisodeRandom(List<String> tag);
	
	Playlist createPlayListForSongs(MultipartFile file, String playlistName, String description, List<Recording> listRecord,
			HttpServletRequest request);

	Playlist createPlayListForPodcast(MultipartFile file, String playlistName, String description, List<Episode> listRecord,
			HttpServletRequest request);
	
	List<Playlist> getAllSongsPlaylist();
	
	List<Playlist> getAllPodcastPlaylist();

	Playlist findById(Long id);
    boolean removeRecordFromPlaylist(Long idRecord);
	void setIsPublicPlaylist(Long idPlaylist, Boolean isPublic);
    Playlist updatePlaylistDetail(Long idPlaylist, UpdatePlaylistDTO playlistDTO, MultipartFile imageFile);
}
