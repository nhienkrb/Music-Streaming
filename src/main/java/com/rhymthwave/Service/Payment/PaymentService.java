package com.rhymthwave.Service.Payment;

import java.util.Optional;

import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;
import com.paypal.orders.OrdersGetRequest;
import com.rhymthwave.DTO.payment.StripeChargeDTO;
import com.rhymthwave.DTO.payment.StripeTokenDTO;
import com.rhymthwave.DTO.payment.SubscriptionDTO;
import com.rhymthwave.entity.payment.CompletedOrder;
import com.rhymthwave.entity.payment.Payment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface PaymentService {
	Payment	vnpay(Integer total,String email,Integer subscriptionId,String packages,Long adsId);
	
	Payment createPaypal(Float fee,Integer subscription, String email,HttpServletRequest req, String pathReturn, String pathCancel,String packages);
	CompletedOrder paypal(String token);
	Order billing(String token);
	
	StripeTokenDTO createCardStripe(StripeTokenDTO stripe);
	StripeChargeDTO chargeStripe(StripeChargeDTO chargeRequest);
	Payment checkoutStripe(SubscriptionDTO subscription,String email,HttpServletRequest req,String pathReturn, String pathCancel,String packages);
}
