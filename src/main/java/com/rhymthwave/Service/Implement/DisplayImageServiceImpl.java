package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.SlideDAO;
import com.rhymthwave.DTO.SlideDTO;
import com.rhymthwave.Service.DisplayImageService;

@Service
public class DisplayImageServiceImpl implements DisplayImageService{

	@Autowired
	SlideDAO dao;
	
	@Override
	public List<SlideDTO> displayImageByPosition(String position) {
		return dao.getByPosition(position);
	}

}
