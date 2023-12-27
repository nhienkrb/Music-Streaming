package com.rhymthwave.Service.Implement;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.UserTypeDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.UserTypeService;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Subscription;
import com.rhymthwave.entity.UserType;

import jakarta.transaction.Transactional;

@Service
public class UserTypeServiceImpl implements CRUD<UserType, Long>, UserTypeService{

	@Autowired
	UserTypeDAO dao;
	
	@Autowired
	CRUD<Account, String> account;
	
	@Autowired
	CRUD<Subscription, Integer> subscription;
	
	@Override
	@Transactional
	public UserType create(UserType entity) {
		return dao.save(entity);
	}

	@Override
	@Transactional
	public UserType update(UserType entity) {
		return dao.save(entity);
	}

	@Override
	@Transactional
	public Boolean delete(Long key) {
		dao.deleteById(key);
		return true;
	}

	@Override
	public UserType findOne(Long key) {
		return dao.findById(key).get();
	}

	@Override
	public List<UserType> findAll() {
		return null;
	}

	@Override
	public UserType generateEntity(String email, Integer subscriptionId,Integer paymentStatus) {
		Account acc = account.findOne(email);
		Subscription sub = subscription.findOne(subscriptionId);
		if(acc.getUserType().toArray().length>1) {
			UserType usertype = acc.getUserType().get(1);
			usertype.setSubscription(sub);
			usertype.setPaymentStatus(paymentStatus);
			usertype.setStartDate(new Date());
			LocalDate startDate = usertype.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			usertype.setEndDate(java.sql.Date.valueOf(startDate.plusDays(sub.getDuration())));
			return usertype;
		}else {
			UserType usertype = new UserType();
			usertype.setNameType("PREMIUM");
			usertype.setAccount(acc);
			usertype.setSubscription(sub);
			usertype.setPaymentStatus(paymentStatus);
			usertype.setStartDate(new Date());
			LocalDate startDate = usertype.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			usertype.setEndDate(java.sql.Date.valueOf(startDate.plusDays(sub.getDuration())));
			return usertype;
		}
	}
	
}
