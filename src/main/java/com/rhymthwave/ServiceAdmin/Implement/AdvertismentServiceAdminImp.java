package com.rhymthwave.ServiceAdmin.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.AdvertismentDAO;
import com.rhymthwave.ServiceAdmin.IAdvertismentServiceAdmin;
import com.rhymthwave.entity.Advertisement;

@Service
public class AdvertismentServiceAdminImp implements IAdvertismentServiceAdmin{

	@Autowired
	AdvertismentDAO dao;
	
	@Override
	public List<Advertisement> listAdvertismentAudio() {
		List<Advertisement> list = dao.findAdsAudioNotNull();
		if(list != null) {
			return list;
		}
		return null;
	}
	
	
}
