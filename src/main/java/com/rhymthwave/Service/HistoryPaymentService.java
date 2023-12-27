package com.rhymthwave.Service;

import com.rhymthwave.entity.Payment;

public interface HistoryPaymentService {
	Payment payment(String email, Integer subId, String paymentType);
}
