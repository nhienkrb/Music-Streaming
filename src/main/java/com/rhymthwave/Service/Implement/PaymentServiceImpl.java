package com.rhymthwave.Service.Implement;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;
import com.paypal.orders.OrdersGetRequest;
import com.rhymthwave.DTO.payment.StripeChargeDTO;
import com.rhymthwave.DTO.payment.StripeTokenDTO;
import com.rhymthwave.DTO.payment.SubscriptionDTO;
import com.rhymthwave.Service.Payment.PaymentService;
import com.rhymthwave.Service.Payment.PaypalService;
import com.rhymthwave.Service.Payment.StripeService;
import com.rhymthwave.Service.Payment.VNPayService;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.payment.CompletedOrder;
import com.rhymthwave.entity.payment.Payment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	PaypalService paypalSer;

	@Autowired
	VNPayService vnpaySer;

	@Autowired
	StripeService stripeSer;

	@Autowired
	PayPalHttpClient payPalHttpClient;

	@Override
	public Payment vnpay(Integer total, String email, Integer subscriptionId, String packages,Long adsId) {
		return vnpaySer.vnpay(total, email, subscriptionId, packages,adsId);
	}

	@Override
	public Payment createPaypal(Float fee, Integer subscription, String email, HttpServletRequest req,
			String pathReturn, String pathCancel, String packages) {
		return paypalSer.createPayment(fee, subscription, email, req, pathReturn, pathCancel, packages);
	}

	@Override
	public CompletedOrder paypal(String token) {
		return paypalSer.completePayment(token);
	}

	@Override
	public Order billing(String token) {
		try {
			OrdersGetRequest request = new OrdersGetRequest(token);
			HttpResponse<Order> response = payPalHttpClient.execute(request);
			Order order = response.result();
			return order;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public StripeTokenDTO createCardStripe(StripeTokenDTO stripe) {
		return stripeSer.createCardToken(stripe);
	}

	@Override
	public StripeChargeDTO chargeStripe(StripeChargeDTO chargeRequest) {
		return stripeSer.chargeStripe(chargeRequest);
	}

	@Override
	public Payment checkoutStripe(SubscriptionDTO subscription, String email, HttpServletRequest req,String pathReturn, String pathCancel,String packages) {
		return stripeSer.checkoutPayment(subscription, email, req,pathReturn,pathCancel,packages);
	}

}
