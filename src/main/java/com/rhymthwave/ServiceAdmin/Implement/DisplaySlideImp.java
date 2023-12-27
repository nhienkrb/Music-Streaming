package com.rhymthwave.ServiceAdmin.Implement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.ImageDAO;
import com.rhymthwave.DAO.SlideDAO;
import com.rhymthwave.Request.DTO.NewDTO;
import com.rhymthwave.Request.DTO.SlideDTO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.CloudinaryService;
import com.rhymthwave.ServiceAdmin.IDisplaySlide;
import com.rhymthwave.ServiceAdmin.INotification;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Image;
import com.rhymthwave.entity.Slide;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DisplaySlideImp implements IDisplaySlide{

private final SlideDAO slideDAO;
	
	private final ImageDAO imageDAO;
	
	private final CloudinaryService cloudinaryService;
	
	private String FOLDER_CONTAINING_IMAGE_NEWS  = "ImageManager";
	
	private final CRUD<Image, String> crudImage;
	
	private final GetHostByRequest getHostByRequest;
	
	
	@Qualifier("sendNotificationOfNews")
	private final INotification<NewDTO> notification;
	


	@Override
	public List<String> getAccessIDInImage(String position) {
		List<Slide> slides = slideDAO.getSlidePostion(position);
		List<String> accessID = new ArrayList<>();
		for (Slide slide : slides) {
			accessID.add(slide.getListImage());
		}
		
		return accessID;
	}
	
	@Override
	public List<Image> getURLImage( List<String> accessID){
		List<Image> listImage = new ArrayList<>();
		
		for (String accessid : accessID) {
			Image img = new Image();
			img = findImgByID(accessid);
			listImage.add(img);
		}
		
		return listImage;
	}
	
	

	@Transactional
	@Override
	public Slide createSlide(SlideDTO slideRequest, HttpServletRequest req, Account account) {	
		try {
			Map<String, Object> mapCloudinary = cloudinaryService.Upload(slideRequest.img(),FOLDER_CONTAINING_IMAGE_NEWS , "Slide Image");
			String urlImage = (String) mapCloudinary.get("url");
			String accessId = (String) mapCloudinary.get("asset_id");
			String public_id = (String) mapCloudinary.get("public_id");
			// save image
			Image img = new Image();
			img.setUrl(urlImage);
			img.setAccessId(accessId);
			img.setPublicId(public_id);
			crudImage.create(img);
			
			// save slide
			Slide newslide = new Slide();
			newslide.setCreateBy(account.getEmail());
			newslide.setCreateDate(getTimeNow());
			newslide.setListImage((String) mapCloudinary.get("asset_id"));
			newslide.setPosition(slideRequest.position());
			newslide.setModifiedBy(account.getEmail());
			newslide.setModifiDate(getTimeNow());
			newslide.setUrl(slideRequest.urlSlide());
			create(newslide);
			
			return newslide;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional
	@Override
	public Slide create(Slide entity) {
		return slideDAO.save(entity);
	}

	@Override
	public List<Slide> findAll() {
		
		return slideDAO.findAll();
	}

	public Date getTimeNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		return new Date(calendar.getTime().getTime());
	}



	@Override
	public List<Image> getAllImages() {
		return imageDAO.findAll();
	}



	@Override
	public Slide updateSlide(String idAccess, SlideDTO slideRequest, HttpServletRequest request) {
		String email = getHostByRequest.getEmailByRequest(request);
		if(email == null || email.equals("")) {
			return null;
		}
		Slide slides = slideDAO.findIDSlideByAccessIDToUpdate(idAccess);
		if( slides == null) {
			return null;
		}
		if( slideRequest.img() == null|| slideRequest.img().isEmpty()) {
			slides.setModifiedBy(email);
			slides.setModifiDate(getTimeNow());
			slides.setPosition(slideRequest.position());
			slides.setUrl(slideRequest.urlSlide());
			slideDAO.save(slides); 
			}else {
				Image img = findImgByID(idAccess);
				Map<String, Object> mapCloudinary = cloudinaryService.Upload(slideRequest.img(),FOLDER_CONTAINING_IMAGE_NEWS , "Slide Image");
				String urlImage = (String) mapCloudinary.get("url");
				String public_id = (String) mapCloudinary.get("public_id");
				img.setUrl(urlImage);
				img.setPublicId(public_id);
				crudImage.update(img);
				
				slides.setModifiedBy(email);
				slides.setModifiDate(getTimeNow());
				slides.setPosition(slideRequest.position());
				slides.setUrl(slideRequest.urlSlide());
				slideDAO.save(slides); 
			}
			
		return slides;
	}
	public Image findImgByID(String idaccess) {
		Optional<Image> img = imageDAO.findById(idaccess);
		return img.get();

	}





	public Slide findOne(Integer key) {
		if(key instanceof Integer && key > 0) {
			Optional<Slide> slides = slideDAO.findById(key);
			if(slides.isPresent()) {
				return slides.get();
			}else {
				return null;
			}
			
		}
		return null;
	}



	@Transactional
	@Override
	public Boolean deleteSlide(String idAccess) {
		Optional<Slide> slides = slideDAO.findIDSlideByAccessID(idAccess);
		String img = findPublicID(idAccess);
		if(slides.isPresent()) {
			slideDAO.delete(slides.get());
			cloudinaryService.deleteFile(img);
			return true;
		}else {
			return false;
		}
	}

	private String findPublicID(String idaccess) {
		Optional<Image> img = imageDAO.findById(idaccess);
		return img.get().getPublicId();
	}


	@Override
	public List<Slide> findListImg() {
		List<Slide> slides = slideDAO.findAll();
		return slides;
	}

	@Override
	public List<String> getAllPositionSlidesShowing() {
		// TODO Auto-generated method stub
		return null;
	}

}
