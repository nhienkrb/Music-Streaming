package com.rhymthwave.Controller;

import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.paypal.orders.Order;
import com.rhymthwave.Service.AdvertisementService;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.HistoryPaymentService;
import com.rhymthwave.Service.UserTypeService;
import com.rhymthwave.Service.Payment.PaymentService;
import com.rhymthwave.entity.Advertisement;
import com.rhymthwave.entity.Payment;
import com.rhymthwave.entity.UserType;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentSer;

	private final CRUD<UserType, Long> crudUserType;

	private final UserTypeService usertypeSer;

	private final CRUD<Payment, Long> crudPayment;

	private final CRUD<Advertisement, Long> crudAds;

	private final HistoryPaymentService historyPayment;

	private final AdvertisementService adsSer;

	@GetMapping("/payment")
	public String completedPayment() {
		return "User/pay";
	}

	@GetMapping("/cancelled")
	public String cancelledPayment() {
		return "User/payfail";
	}

	@GetMapping("/complete-payment-vnpay")
	public String completePaymentVNPay(@RequestParam("subcriptionId") Integer subcriptionId,
			@RequestParam("paymentName") String paymentName, @RequestParam("email") String email,
			@RequestParam("ads") Optional<Long> ads, @RequestParam("packages") String packages,
			@RequestParam("vnp_ResponseCode") String vnp_ResponseCode) {
		if (vnp_ResponseCode.equals("00")) {
			if (packages.equalsIgnoreCase("ACCOUNT")) {
				crudUserType.create(usertypeSer.generateEntity(email, subcriptionId, 1));
				crudPayment.create(historyPayment.payment(email, subcriptionId, paymentName));
			} else {
				adsSer.updateStatusAds(ads.orElse(null), false, 1);
			}
			return "redirect:/payment";
		} else {
			return "redirect:/cancelled";
		}
		
	}

	@GetMapping("/complete-payment-paypal")
	public String completePayment(@RequestParam("token") String token, @RequestParam("email") String email,
			@RequestParam("paymentName") String paymentName, @RequestParam("total") Float total,
			@RequestParam("PayerID") String PayerID, @RequestParam("packages") String packages,
			@RequestParam("subcriptionId") Integer subscriptionId, @RequestParam("ads") Optional<Long> adsId) {
		if (packages.equalsIgnoreCase("ACCOUNT")) {
			Order order = paymentSer.billing(token);
			crudUserType.create(usertypeSer.generateEntity(email, subscriptionId, 1));
			crudPayment.create(historyPayment.payment(email, subscriptionId, paymentName));
		} else {
			adsSer.updateStatusAds(adsId.orElse(null), false, 1);
		}
		return "redirect:/payment";
	}

	@GetMapping("/completed-payment-stripe")
	public String completePayment(@RequestParam("subscription") Integer subscriptionId,
			@RequestParam("paymentName") String paymentName, @RequestParam("email") String email,
			@RequestParam("total") Float total, @RequestParam("packages") String packages,
			@RequestParam("ads") Optional<Long> adsId) {
		if (packages.equalsIgnoreCase("ACCOUNT")) {
			crudUserType.create(usertypeSer.generateEntity(email, subscriptionId, 1));
			crudPayment.create(historyPayment.payment(email, subscriptionId, paymentName));
		} else {
			adsSer.updateStatusAds(adsId.orElse(null), false, 1);
		}
		return "redirect:/payment";
	}
}
