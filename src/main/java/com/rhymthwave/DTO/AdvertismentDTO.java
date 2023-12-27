package com.rhymthwave.DTO;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Image;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class AdvertismentDTO {
	private Long adId;

	private String title;

	private String content;

	private String url;

	private String targetAudience;

	private Long clicked;
	
	private Long listened;

	private Integer priority;

	private String audioFile;
	
	private String tag;
	
	private Image image;

}
