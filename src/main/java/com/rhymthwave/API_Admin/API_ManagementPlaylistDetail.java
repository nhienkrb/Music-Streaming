package com.rhymthwave.API_Admin;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Request.DTO.UpdatePlaylistDTO;
import com.rhymthwave.ServiceAdmin.IPlayListServiceAdmin;
import com.rhymthwave.entity.Playlist;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/playlist")
@RequiredArgsConstructor
public class API_ManagementPlaylistDetail {

    private final IPlayListServiceAdmin playListServiceAdmin;


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlaylistDetail(@PathVariable("id") Long idPlaylist, @ModelAttribute UpdatePlaylistDTO playlistDTO, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Playlist playlist = playListServiceAdmin.updatePlaylistDetail(idPlaylist, playlistDTO, imageFile);

        if (playlist != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Update Success", playlist));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Playlist does not exist"));

    }

    @DeleteMapping("/{id}/detail")
    public ResponseEntity<MessageResponse> removeRecordFromPlaylist(@PathVariable("id") Long idRecord) {
        boolean status = playListServiceAdmin.removeRecordFromPlaylist(idRecord);

        if (status) {
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successful"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Removed fail. Playlist does not exist"));
    }

    @PutMapping("/{idPlaylist}/ispublic")
    public ResponseEntity<MessageResponse> isPublicPlaylist(@PathVariable("idPlaylist") Long idPlaylist, @RequestParam("isPublic") Boolean isPublic) {
        playListServiceAdmin.setIsPublicPlaylist(idPlaylist, isPublic);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Public success"));
    }


}

