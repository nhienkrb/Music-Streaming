package com.rhymthwave.Request.DTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rhymthwave.entity.Recording;

import lombok.Data;

@Data
public class RPlaylistDTO {
	private String playlistName;
	private List<Recording> listRecord;
	private MultipartFile file;
}
