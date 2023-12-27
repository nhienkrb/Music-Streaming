package com.rhymthwave.API;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.SongGenreService;
import com.rhymthwave.entity.SongGenre;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class SongGenreREST {
	private final CRUD<SongGenre, Long> crudSongGenre;
	
	private final SongGenreService songGenreSer;
	
	@GetMapping("/api/v1/song-genre/{id}")
	public ResponseEntity<MessageResponse> getSongGenreById(@PathVariable("id") Long id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudSongGenre.findOne(id)));
	}
	
	@GetMapping("/api/v1/song-genre-record/{id}")
	public ResponseEntity<MessageResponse> getListSongGenreByRecord(@PathVariable("id") Long id){
		return ResponseEntity.ok(new MessageResponse(true,"success",songGenreSer.findListSongGenreByRecord(id)));
	}
	
	@PostMapping("/api/v1/song-genre")
	public ResponseEntity<MessageResponse> createSongGenre(@RequestBody SongGenre songGenre){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudSongGenre.create(songGenre)));
	}
	
	@DeleteMapping("/api/v1/song-genre/{id}")
	public ResponseEntity<MessageResponse> deleteSongGenre(@PathVariable("id") Long id){
		return ResponseEntity.ok(new MessageResponse(true,"success",crudSongGenre.delete(id)));
	}
}
