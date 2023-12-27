package com.rhymthwave.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Image;

public interface EpisodeService{
	Episode snapEpisode(Episode episode,Map<?,?> recordAudio, Image coverImg);
	
	List<Episode> findAllEpisodeByPodcast(Long podcastId, Boolean status);
	
	Episode findLatestEpisodeByPodcast(Long podcastId);
	
	List<Episode> findByName(String keyword);
	
	List<Episode> top50EpForYou(Boolean ispublic, Optional<List<Integer>> tags);
}
