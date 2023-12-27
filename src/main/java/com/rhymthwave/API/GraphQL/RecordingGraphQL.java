package com.rhymthwave.API.GraphQL;

import java.util.List;
import java.util.Optional;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.RecordService;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Playlist;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.Song;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RecordingGraphQL {
	private final CRUD<Recording, Long> crudRecording;
	
	private final RecordService recordSer;
	
	private final GetHostByRequest host;
	
	@QueryMapping("recordingById")
	public Recording findOne(@Argument("recordingId") Long id) {
		return crudRecording.findOne(id);
	}
	
	@QueryMapping("recommendedListRecording")
	public List<Recording> findListRandom() {
		return recordSer.findListRecordRandom();
	}
	
	@QueryMapping("mySongProject")
	public List<Recording> listSongProject(@Argument("artistid") Long id){
		return recordSer.findMyProject(id);
	}
	
	@QueryMapping("getListSongReleased")
	public List<Recording> getListSongReleased(@Argument("email") String email){
		return recordSer.findRecordByCreater(email);
	}
	
	@QueryMapping("findSongPl")
	public List<Recording> findSongPl(@Argument("songName") String songName){
		return recordSer.findSongPl(songName);
	}
	
	@QueryMapping("getListRecordByFavorite")
	public List<Recording> getListRecordByFavorite(@Argument("genre") Optional<String> genre,
			@Argument("culture") Optional<String> culture, @Argument("instrument") Optional<String> instrument,
			@Argument("mood") Optional<String> mood, @Argument("songstyle") Optional<String> songstyle, 
			@Argument("versions") Optional<String> versions){
		return recordSer.findListRandomFavorite(genre.orElse("''"), culture.orElse(""), instrument.orElse(""), mood.orElse(""), songstyle.orElse(""), versions.orElse(""));
	}
	
	@QueryMapping("findListPopularByArtist")
	public List<Recording> findListPopularByArtist(@Argument("artistId") Long artist){
		return recordSer.findListPopularByArtist(artist);
	}
	
	@QueryMapping("findRecordingAppearOnByArtist")
	public List<Recording> findAppearOnByArtist(@Argument("artistId") Long artist){
		return recordSer.findAppearOn(artist);
	}
	
	@QueryMapping("top50SongByAreaListened")
	public List<Recording> top50SongByAreaListened(@Argument("country") String country){
		return recordSer.top50SongByAreaListened(country,false);
	}
	
	@QueryMapping("top50SongByDate")
	public List<Recording> top50SongByDate(@Argument("country") String country){
		return recordSer.top50SongByDate(country,false);
	}
	
	@QueryMapping("findRecordingBySongId")
	public Recording findRecordingBySongId(@Argument("songId") Long id) {
		return crudRecording.findOne(id);
	}
}
