package com.rhymthwave.Service.Implement;

import java.util.List;

import com.rhymthwave.entity.TypeEnum.ESubscription;
import com.rhymthwave.entity.TypeEnum.EUserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.SubscriptionDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.SubscriptionService;
import com.rhymthwave.Utilities.GetCurrentTime;
import com.rhymthwave.entity.Subscription;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

	@Autowired
	SubscriptionDAO dao;

	@Override
	public Subscription getSubByName(String name) {
		return dao.findBySubscriptionType(name);
	}

	@Override
	public Subscription create(Subscription entity) {
		if(entity.getSubscriptionCategory() == ESubscription.ACCOUNT){
			entity.setSubscriptionType("PREMIUM");
		}
		entity.setActive(true);
		entity.setCreateDate(GetCurrentTime.getTimeNow());
		return dao.save(entity);
	}

	@Override
	public Subscription update(Subscription entity) {

		return dao.save(entity);
	}

	@Override
	public Boolean delete(Integer key) {
		if (findOne(key) != null) {
			dao.delete(findOne(key));
			return true;
		}
		return false;
	}

	@Override
	public Subscription findOne(Integer key) {
		return dao.findById(key).orElse(null);
	}

	@Override
	public List<Subscription> findAll() {

		return dao.findAll();
	}

	@Override
	public List<Subscription> findByCategory(ESubscription cate,Boolean active) {
		return dao.findBySubscriptionCategoryAndActive(cate,active);
	}
}
