package com.rhymthwave.Service;

import java.util.List;

import com.rhymthwave.Request.DTO.NewDTO;
import com.rhymthwave.entity.News;
import com.rhymthwave.entity.TypeEnum.EROLE;

import jakarta.servlet.http.HttpServletRequest;

public interface NewService {

	News saveNews(NewDTO newDTO,HttpServletRequest request);

	News updateNews(Integer idNews, NewDTO newDTO, HttpServletRequest request);
	
	List<String> getAllstorageForImage();
	
	List<Integer> getAllYear();
	
	List<News> findNewsByYearAndMonth(Integer year, Integer  month);

	List<News> getAllNewForRole(EROLE createfor);
}
