package com.rhymthwave.Config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
	private final String CLOUD_NAME ="div9ldpou";
	private final String API_KEY ="847854723289299";
	private final String API_SECRET ="ILgc7bNO6zbJyWt_FhFoQL_r8eY";
	
	@Bean
	public Cloudinary cloudinary() {
		Map<String,String> config = new HashMap<>();
		config.put("cloud_name", CLOUD_NAME);
		config.put("api_key", API_KEY);
		config.put("api_secret", API_SECRET);
		return new Cloudinary(config);
	}
}
