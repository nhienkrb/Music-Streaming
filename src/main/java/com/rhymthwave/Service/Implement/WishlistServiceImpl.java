package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.WishlistDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.WishlistService;
import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.UserType;
import com.rhymthwave.entity.Wishlist;

import jakarta.transaction.Transactional;

@Service
public class WishlistServiceImpl implements WishlistService, CRUD<Wishlist, Long>{
	
	@Autowired
	WishlistDAO dao;
	
	@Override
	@Transactional
	public Wishlist create(Wishlist entity) {
		Wishlist wishlist = entity;
		return dao.save(wishlist);
	}

	@Override
	@Transactional
	public Wishlist update(Wishlist entity) {
		Wishlist wishlist = entity;
		return dao.save(wishlist);
	}

	@Override
	@Transactional
	public Boolean delete(Long key) {
		dao.deleteById(key);
		return true;
	}

	@Override
	public Wishlist findOne(Long key) {
		return dao.findById(key).get();
	}

	@Override
	public List<Wishlist> findAll() {
		return dao.findAll();
	}

	@Override
	public Wishlist checkExtist(UserType usertype, Episode episode, Recording recording) {
		Wishlist wishlist = dao.findByUsertypeAndEpisodeAndRecording(usertype, episode, recording);
		if(wishlist !=null ) {
			return wishlist;
		}
		return null;
	}

	@Override
	@Transactional
	public Wishlist create(Wishlist wishlist, UserType usertype, Episode episode, Recording recording) {
		if(checkExtist(usertype, episode, recording)==null) {
			wishlist.setUsertype(usertype);
			wishlist.setEpisode(episode);
			wishlist.setRecording(recording);
			return create(wishlist);
		}
		return null;
	}

	@Override
	public List<Wishlist> myWishlist(UserType userType) {
		if(userType!=null) {
			return dao.findAllByUsertype(userType);
		}
		return null;
	}
	
	
}
