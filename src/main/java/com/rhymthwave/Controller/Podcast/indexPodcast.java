package com.rhymthwave.Controller.Podcast;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexPodcast {
	
	@GetMapping("getstarted")
	public String getstartedPodcast() {
		return "Podcaster/Information";
	}
	
	@GetMapping("podcast-browse")
	public String browsePodcast() {
		return "Podcaster/SelectPodcast";
	}
	
	@GetMapping("podcaster")
	public String podcaster() {
		return "Podcaster/PodcastControl";
	}
	
	@GetMapping("podcast/home")
	public String layoutPodcastHome() {
		return "Podcaster/Podcast";
	}
}
