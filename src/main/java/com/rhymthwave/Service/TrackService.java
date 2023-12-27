package com.rhymthwave.Service;

import java.util.List;

import com.rhymthwave.entity.Track;

public interface TrackService {
	List<Track> getTrackByAlbum(Integer albumId);
}
