package com.rhymthwave.Service;

import java.util.List;

import com.rhymthwave.DTO.SlideDTO;

public interface DisplayImageService {

	List<SlideDTO> displayImageByPosition(String position);
}
