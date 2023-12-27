package com.rhymthwave.ServiceAdmin;

import java.util.List;

import com.rhymthwave.Request.DTO.SlideDTO;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Image;
import com.rhymthwave.entity.Slide;

import jakarta.servlet.http.HttpServletRequest;

public interface IDisplaySlide {
	List<String>getAllPositionSlidesShowing();


	List<Slide> findAll();

	Slide createSlide(SlideDTO slideRequest, HttpServletRequest req, Account account);

	Slide create(Slide entity);

	
	List<Image> getAllImages();

	Slide findOne(Integer key);

	List<Slide> findListImg();

	List<String> getAccessIDInImage(String position);


	List<Image> getURLImage(List<String> accessID);


	Boolean deleteSlide(String idAccess);


	Slide updateSlide(String idAccess, SlideDTO slideRequest, HttpServletRequest request);
}
