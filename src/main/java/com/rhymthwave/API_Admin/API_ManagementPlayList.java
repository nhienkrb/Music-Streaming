package com.rhymthwave.API_Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.ServiceAdmin.IPlayListServiceAdmin;
import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Playlist;
import com.rhymthwave.entity.Recording;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/playlist")
@RequiredArgsConstructor
public class API_ManagementPlayList {

    private final IPlayListServiceAdmin playListService;

    private  final CRUD<Recording,Long> crud;

    private  final CRUD<Episode,Long> crudEpisode;

    @GetMapping("/all-record")
    public ResponseEntity<?> getAllRecord()  {
        List<Recording> recordingList = crud.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "success", recordingList));
    }

    @GetMapping("/all-episode")
    public ResponseEntity<?> getAllEpisode()  {
        List<Episode> episodeList = crudEpisode.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "success", episodeList));
    }

    @GetMapping("/record-random")
    public ResponseEntity<?> getSongByStyle(@RequestParam(value = "nameGenre", required = false) String nameGenre,
                                            @RequestParam(value = "culture", required = false) String culture, @RequestParam(value = "mood", required = false) String mood,
                                            @RequestParam(value = "songstyle", required = false) String songstyle, @RequestParam(value = "instrument", required = false) String instrument) {
        List<Recording> list = playListService.getListRecordRandom(nameGenre, culture, mood, songstyle, instrument);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "success", list));
    }

    @GetMapping("/episode-random")
    public ResponseEntity<?> getSongByStyle(@RequestParam(value = "tag", required = false) List<String> tags) {
        List<Episode> list = playListService.getListEpisodeRandom(tags);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "success", list));
    }

    @PostMapping("/songs")
    public ResponseEntity<?> createPlaylistForSong(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("playlistName") String playlistName, @RequestParam("listRecord") String listRecordJson,
                                                   @RequestParam("description") String description, HttpServletRequest request) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Recording> listRecord = objectMapper.readValue(listRecordJson, new TypeReference<List<Recording>>() {
            });
            Playlist playlist = playListService.createPlayListForSongs(file, playlistName, description, listRecord, request);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "success", playlist));
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(false, "Error handle objectMapper"));

        }

    }

    @PostMapping("/podcast")
    public ResponseEntity<?> createPlaylistForPodcast(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("playlistName") String playlistName, @RequestParam("listEpisode") String listEpisodeJson,
                                                      @RequestParam("description") String description, HttpServletRequest request) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Episode> listRecord = objectMapper.readValue(listEpisodeJson, new TypeReference<List<Episode>>() {
            });
            Playlist playlist = playListService.createPlayListForPodcast(file, playlistName, description, listRecord, request);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "success", playlist));
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(false, "Error handle objectMapper"));

        }

    }
}
