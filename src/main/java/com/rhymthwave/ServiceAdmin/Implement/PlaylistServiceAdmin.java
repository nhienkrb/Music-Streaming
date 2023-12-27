package com.rhymthwave.ServiceAdmin.Implement;

import com.rhymthwave.DAO.EpisodeDAO;
import com.rhymthwave.DAO.PlaylistDAO;
import com.rhymthwave.DAO.RecordDAO;
import com.rhymthwave.DAO.UserTypeDAO;
import com.rhymthwave.Request.DTO.UpdatePlaylistDTO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.CloudinaryService;
import com.rhymthwave.ServiceAdmin.IPlayListServiceAdmin;
import com.rhymthwave.Utilities.GetCurrentTime;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlaylistServiceAdmin implements IPlayListServiceAdmin {

    private final RecordDAO recordDAO;

    private final EpisodeDAO episodeDAO;

    private final UserTypeDAO userTypeDAO;

    private final PlaylistDAO playlistDAO;

    private final CloudinaryService cloudinaryService;

    private final CRUD<Image, String> crudImage;

    private final CRUD<PlaylistRecord, Long> playlistRecordService;

    private final CRUD<Playlist_Podcast, Long> playlistPodcastService;

    private final CRUD<Playlist, Long> playlistService;

    private final GetHostByRequest getEmailByRequest;

    private final String FOLDER_CONTAINING_IMAGE_PLAYLIST = "ImagePlaylist";

    private final String FOLDER_CONTAINING_IMAGE_PLAYLIST_SYSTEM = "System";

    @Override
    public List<Recording> getListRecordRandom(String nameGenre, String culture, String mood, String songstyle, String instrument) {
        return recordDAO.findListRecordRandom(nameGenre, culture, instrument, mood, songstyle);
    }

    @Override
    public Playlist createPlayListForSongs(MultipartFile file, String playlistName, String description, List<Recording> listRecord, HttpServletRequest request) {
        UserType userType = userTypeDAO.findUserTypeEmail(getEmailByRequest.getEmailByRequest(request));

        Playlist playlist = new Playlist();

        Map<String, Object> mapCloudinary = cloudinaryService.Upload(file, FOLDER_CONTAINING_IMAGE_PLAYLIST, FOLDER_CONTAINING_IMAGE_PLAYLIST_SYSTEM);

        String urlImage = (String) mapCloudinary.get("url");
        String accessId = (String) mapCloudinary.get("asset_id");
        String public_id = (String) mapCloudinary.get("public_id");
        // save image
        Image img = new Image();
        img.setUrl(urlImage);
        img.setAccessId(accessId);
        img.setPublicId(public_id);
        crudImage.create(img);

        // create playlist
        playlist.setPlaylistName(playlistName);
        playlist.setDescription(description);
        playlist.setQuantity(listRecord.size());
        playlist.setIsPublic(false);
        playlist.setCreateDate(GetCurrentTime.getTimeNow());
        playlist.setImage(img);
        playlist.setUsertype(userType);
        playlistService.create(playlist);
        // create a playlist record

//		
        listRecord.forEach(recording -> {
            PlaylistRecord playlistRecord = new PlaylistRecord();
            playlistRecord.setRecording(recording);
            playlistRecord.setPlaylist(playlist);
            playlistRecordService.create(playlistRecord);
        });

        return playlist;
    }


    @Override
    public Playlist createPlayListForPodcast(MultipartFile file, String playlistName, String description, List<Episode> listPodcast, HttpServletRequest request) {
        UserType userType = userTypeDAO.findUserTypeEmail(getEmailByRequest.getEmailByRequest(request));

        Playlist playlist = new Playlist();

        Map<String, Object> mapCloudinary = cloudinaryService.Upload(file, FOLDER_CONTAINING_IMAGE_PLAYLIST, FOLDER_CONTAINING_IMAGE_PLAYLIST_SYSTEM);

        String urlImage = (String) mapCloudinary.get("url");
        String accessId = (String) mapCloudinary.get("asset_id");
        String public_id = (String) mapCloudinary.get("public_id");
        // save image
        Image img = new Image();
        img.setUrl(urlImage);
        img.setAccessId(accessId);
        img.setPublicId(public_id);
        crudImage.create(img);

        // create playlist
        playlist.setPlaylistName(playlistName);
        playlist.setDescription(description);
        playlist.setQuantity(listPodcast.size());
        playlist.setIsPublic(false);
        playlist.setCreateDate(GetCurrentTime.getTimeNow());
        playlist.setImage(img);
        playlist.setUsertype(userType);
        playlistService.create(playlist);
        // create a playlist Podcast

//		
        listPodcast.forEach(episode -> {
            var playlist_Podcast = new Playlist_Podcast();
            playlist_Podcast.setEpisode(episode);
            playlist_Podcast.setPlaylist(playlist);
            playlistPodcastService.create(playlist_Podcast);
        });
        return playlist;
    }


    @Override
    public List<Playlist> getAllSongsPlaylist() {

        return playlistDAO.getplayListSongAdmin();
    }

    @Override
    public List<Playlist> getAllPodcastPlaylist() {

        return playlistDAO.getPodcastPlayListAdmin();
    }

    @Override
    public Playlist findById(Long id) {

        return playlistDAO.findById(id).orElse(null);
    }

    @Override
    public boolean removeRecordFromPlaylist(Long idRecord) {
        PlaylistRecord playlistRecord = playlistRecordService.findOne(idRecord);
        if (playlistRecord != null) {
            int totalRecordInPlaylist =  playlistRecord.getPlaylist().getQuantity() - 1;
            playlistRecord.getPlaylist().setQuantity(totalRecordInPlaylist);
            playlistRecordService.update(playlistRecord);
            playlistRecordService.delete(idRecord);
            return true;
        }
        return false;
    }

    @Override
    public void setIsPublicPlaylist(Long idPlaylist, Boolean isPublic) {
        Playlist playlist = playlistService.findOne(idPlaylist);
        if(playlist !=null){
            playlist.setIsPublic(isPublic);
            playlistService.update(playlist);
        }
    }

    @Override
    public Playlist updatePlaylistDetail(Long idPlaylist, UpdatePlaylistDTO playlistDTO, MultipartFile imageFile) {
        Playlist playlist = playlistService.findOne(idPlaylist);
        if(playlist != null){
            if (imageFile != null) {
                Image image = crudImage.findOne(playlist.getImage().getAccessId());
                cloudinaryService.deleteFile(image.getPublicId());
                Map<String, Object> mapCloudinary = cloudinaryService.Upload(imageFile, FOLDER_CONTAINING_IMAGE_PLAYLIST, FOLDER_CONTAINING_IMAGE_PLAYLIST_SYSTEM);
                String urlImage = (String) mapCloudinary.get("url");
                String public_id = (String) mapCloudinary.get("public_id");
                image.setUrl(urlImage);
                image.setPublicId(public_id);
                crudImage.update(image);

            }
            playlist.setPlaylistName(playlistDTO.getPlaylistName());
            playlist.setDescription(playlistDTO.getPlaylistDescription());
            playlistService.update(playlist);
            return playlist;

        }
        return null;
    }

    @Override
    public List<Episode> getListEpisodeRandom(List<String> tag) {
        return episodeDAO.getRandomPodcasts(tag);
    }


}
