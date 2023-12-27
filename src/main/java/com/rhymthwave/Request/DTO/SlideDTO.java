package com.rhymthwave.Request.DTO;

import org.springframework.web.multipart.MultipartFile;

public record SlideDTO(String position,String urlSlide, MultipartFile img) {

}
