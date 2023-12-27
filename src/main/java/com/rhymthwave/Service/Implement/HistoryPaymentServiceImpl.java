package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.PaymentDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.HistoryPaymentService;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Payment;
import com.rhymthwave.entity.Subscription;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryPaymentServiceImpl implements HistoryPaymentService, CRUD<Payment, Long>{
	private final CRUD<Account, String>  crudAcc;
	
	private final CRUD<Subscription, Integer> crudSub;
	
	private final PaymentDAO paymentDao;

	@Override
	public Payment payment(String email, Integer subId, String paymentName) {
		Payment payment = new Payment();
		payment.setAccount(crudAcc.findOne(email));
		payment.setSubscription(crudSub.findOne(subId));
		payment.setPaymentName(paymentName);
		return payment;
	}

	@Override
	public Payment create(Payment entity) {
		return paymentDao.save(entity);
	}

	@Override
	public Payment update(Payment entity) {
		return paymentDao.save(entity);
	}

	@Override
	public Boolean delete(Long key) {
		paymentDao.deleteById(key);
		return true;
	}

	@Override
	public Payment findOne(Long key) {
		return paymentDao.findById(key).get();
	}

	@Override
	public List<Payment> findAll() {
		return paymentDao.findAll();
	}
	
}
