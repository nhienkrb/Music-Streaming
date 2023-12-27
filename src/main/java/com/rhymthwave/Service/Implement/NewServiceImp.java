package com.rhymthwave.Service.Implement;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rhymthwave.DAO.NewsDAO;
import com.rhymthwave.Request.DTO.NewDTO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.CloudinaryService;
import com.rhymthwave.Service.NewService;
import com.rhymthwave.ServiceAdmin.INotification;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Image;
import com.rhymthwave.entity.News;
import com.rhymthwave.entity.TypeEnum.EROLE;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class NewServiceImp implements NewService, CRUD<News, Integer>{
	
	private final CRUD<Image, String> crudImage;
	
	private final CRUD<Account, String> crudAccount;
	
	private final NewsDAO newDao; 
	
	private final GetHostByRequest getHostByRequest;
	
	private final CloudinaryService cloudinaryService;
	
	@Qualifier("sendNotificationOfNews")
	private final INotification<NewDTO> notification;
	
	private String FOLDER_CONTAINING_IMAGE_NEWS  = "ImageManager";

	private String toURL  = "http://localhost:8080/new/home/";
	
	@Transactional
	@Override
	public News create(News entity) {
		return newDao.save(entity);
	}
	@Transactional
	@Override
	public News update(News entity) {

		return newDao.save(entity);
	}
	@Transactional
	@Override
	public Boolean delete(Integer key) {
		if(key instanceof Integer && key > 0) {
			Optional<News> news = newDao.findById(key);
			if(news.isPresent()) {
				newDao.delete(news.get());
				cloudinaryService.deleteFile(news.get().getImage().getPublicId());
				return true;
			}else {
				return false;
			}
			
		}
		return false;
	}

	@Override
	public News findOne(Integer key) {
		if(key instanceof Integer && key > 0) {
			Optional<News> news = newDao.findById(key);
			if(news.isPresent()) {
				return news.get();
			}else {
				return null;
			}
			
		}
		return null;
	}

	@Override
	public List<News> findAll() {
		
		return newDao.findAll();
	}

	@Transactional
	@Override
	public News saveNews(NewDTO newDTO,HttpServletRequest request) {
		
		String email = getHostByRequest.getEmailByRequest(request);
		if(email == null || email.equals("")) {
			return null;
		}
		
		try {
			Account account = crudAccount.findOne(email);
			Map<String, Object> mapCloudinary = cloudinaryService.Upload(newDTO.img(),FOLDER_CONTAINING_IMAGE_NEWS , newDTO.ImageLocation());
			String urlImage = (String) mapCloudinary.get("url");
			String accessId = (String) mapCloudinary.get("asset_id");
			String public_id = (String) mapCloudinary.get("public_id");
			// save image
			Image img = new Image();
			img.setUrl(urlImage);
			img.setAccessId(accessId);
			img.setPublicId(public_id);
			crudImage.create(img);
			
			// save news
			News news = new News();
			news.setTitle(newDTO.title());
			news.setContent(newDTO.content());
			news.setAccount(account);
			news.setPublishDate(getTimeNow());
			news.setLastModified(getTimeNow());
			news.setImage(img);
			news.setCreateDate(getTimeNow());
			news.setCreateFor(newDTO.role());
			news.setModifiDate(getTimeNow());
			news.setModifiedBy(email);
			create(news);
			News newUpdateUrl =	findOne(news.getNewsId());
			news.setToUrl(toURL+newUpdateUrl.getNewsId());
			update(newUpdateUrl);
			notification.sendNotification(newDTO,urlImage+";"+news.getToUrl());
			
			return news;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	

	@Override
	public News updateNews(Integer id, NewDTO newDTO, HttpServletRequest request) {
		String email = getHostByRequest.getEmailByRequest(request);
		if(email == null || email.equals("")) {
			return null;
		}
		
		News news = findOne(id);
		
		if( news == null) {
			return null;
		}
		
		if( newDTO.img() == null|| newDTO.img().isEmpty()) {
			news.setTitle(newDTO.title());
			news.setContent(newDTO.content());
			news.setLastModified(getTimeNow());
			news.setModifiDate(getTimeNow());
			news.setModifiedBy(email);
			update(news);
		}else {
			Image img = crudImage.findOne(news.getImage().getAccessId());
			Map<String, Object> mapCloudinary = cloudinaryService.Upload(newDTO.img(),FOLDER_CONTAINING_IMAGE_NEWS , newDTO.ImageLocation());
			String urlImage = (String) mapCloudinary.get("url");
			String public_id = (String) mapCloudinary.get("public_id");
			img.setUrl(urlImage);
			img.setPublicId(public_id);
			crudImage.update(img);
			
			news.setImage(img);
			news.setTitle(newDTO.title());
			news.setContent(newDTO.content());
			news.setLastModified(getTimeNow());
			news.setModifiDate(getTimeNow());
			news.setModifiedBy(email);
			update(news);
		}
		
		return news;
	}


	@Override
	public List<String> getAllstorageForImage() {
	
		return cloudinaryService.getCloudinaryChildFolder(FOLDER_CONTAINING_IMAGE_NEWS);
	}
	
	public Date getTimeNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		return new Date(calendar.getTime().getTime());
	}
	@Override
	public List<Integer> getAllYear() {
		
		return  newDao.getAllYearInDB();
	}
	@Override
	public List<News> findNewsByYearAndMonth(Integer year, Integer month) {
	
		return newDao.findNewsByYearAndMonth(year, month);
	}
	@Override
	public List<News> getAllNewForRole(EROLE createfor) {
		
		return newDao.findAllNewsCreateFor(createfor);
	}



}