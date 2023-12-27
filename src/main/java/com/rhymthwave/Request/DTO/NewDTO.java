package com.rhymthwave.Request.DTO;

import org.springframework.web.multipart.MultipartFile;

import com.rhymthwave.entity.TypeEnum.EROLE;

public record NewDTO(String title, String content,String summary,EROLE role, String ImageLocation, MultipartFile img) {

}
