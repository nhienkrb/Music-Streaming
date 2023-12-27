package com.rhymthwave.Service.Implement;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.ImageDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.ImageService;
import com.rhymthwave.entity.Image;

@Service
public class ImageServiceImpl implements ImageService, CRUD<Image, String>{

	@Autowired
	ImageDAO dao;
	
	@Override
	public Image create(Image entity) {
		return dao.save(entity);
	}

	@Override
	public Image update(Image entity) {
		return dao.save(entity);
	}

	@Override
	public Boolean delete(String key) {
		dao.deleteById(key);
		return true;
	}

	@Override
	public Image findOne(String key) {
		
		Optional<Image> image = dao.findById(key);
		if(image.isPresent()) {
			return image.get();
		}
		return null;
	}

	@Override
	public List<Image> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getEntity(Map<?,?> respImg) {
		Image image = new Image();
		image.setAccessId((String) respImg.get("asset_id"));
		image.setUrl((String) respImg.get("url"));
		image.setPublicId((String) respImg.get("public_id"));
		image.setWeight((Integer) respImg.get("weight"));
		image.setHeight((Integer) respImg.get("height"));
		return image;
	}
	
}
