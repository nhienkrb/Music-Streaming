package com.rhymthwave.Service;

import java.util.List;
import java.util.Optional;

import com.rhymthwave.entity.Playlist;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.UserType;

public interface PlaylistService {

	List<Playlist> findMyPlaylist(UserType userType);

	Playlist createSimilarPlaylist(Playlist playlist, List<Recording> list);

	List<Playlist> findPublicPlaylist(UserType userType, Boolean isPublic);

	List<Playlist> findPlaylistFeaturingArtist(Long artistId, List<Integer> roleId);

	List<Playlist> findDiscoverArtist(Long artistId, List<Integer> roleId);

	List<Playlist> top50PlaylistLatest(Boolean isPublic);

	List<Playlist> top50PlaylistRecentListen(Boolean isPublic, Optional<List<String>> nameGenre,
			Optional<String> culture, Optional<String> instrument, Optional<String> mood, Optional<String> songstyle,Optional<String> versions);
}
