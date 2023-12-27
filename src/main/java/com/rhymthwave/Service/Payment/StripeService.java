package com.rhymthwave.Service.Payment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;

import com.rhymthwave.DTO.payment.StripeChargeDTO;
import com.rhymthwave.DTO.payment.StripeTokenDTO;
import com.rhymthwave.DTO.payment.SubscriptionDTO;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.Utilities.Cookie.CookiesUntils;
import com.rhymthwave.entity.payment.Payment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StripeService {

	@Value("${stripe.secretKey}")
	private String stripeSecretKey;

	@PostConstruct
	public void init() {
		Stripe.apiKey = stripeSecretKey;
	}

	public StripeTokenDTO createCardToken(StripeTokenDTO stripe) {
		try {
			Map<String, Object> card = new HashMap<>();
			card.put("number", stripe.getCardNumber());
			card.put("exp_month", stripe.getCardNumber());
			card.put("exp_year", stripe.getCardNumber());
			card.put("cvc", stripe.getCardNumber());

			Map<String, Object> params = new HashMap<>();
			params.put("card", card);
			Token token = Token.create(params);
			if (token != null && token.getId() != null) {
				stripe.setSuccess(true);
				stripe.setToken(token.getId());
			}
			return stripe;
		} catch (StripeException e) {
			log.error("StripeService(createCardToken)", e);
			stripe.setSuccess(false);
			return stripe;
		}
	}

	public StripeChargeDTO chargeStripe(StripeChargeDTO chargeRequest) {
		try {
			chargeRequest.setSuccess(false);
			Map<String, Object> chargeParams = new HashMap<>();
			chargeParams.put("amount", Integer.parseInt(chargeRequest.getAmount()) * 100);
			chargeParams.put("currency", "USD");
			chargeParams.put("description", chargeRequest.getAdditionalInfor());
			chargeParams.put("source", chargeRequest.getStripeToken());

			Map<String, Object> metaData = new HashMap<>();
			metaData.put("id", chargeRequest.getChargeId());
			metaData.putAll(chargeRequest.getAdditionalInfor());
			chargeParams.put("metaData", metaData);
			Charge charge = Charge.create(chargeParams);
			chargeRequest.setMessage(charge.getOutcome().getSellerMessage());
			if (charge.getPaid()) {
				chargeRequest.setChargeId(charge.getId());
				chargeRequest.setSuccess(true);
			}
			return chargeRequest;
		} catch (Exception e) {
			log.error("StripeService (charge)", e);
			return chargeRequest;
		}

	}

	// phải tạo product trên stripe
	public Payment checkoutPayment(SubscriptionDTO subscription, String email, HttpServletRequest req,
			String pathReturn, String pathCancel, String packages) {
		try {
			SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
					.setSuccessUrl(applicationUrl(req, pathReturn)).setCancelUrl(applicationUrl(req, pathCancel))
					.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L)
							.setPrice(subscription.getPrdStripeId()).build())
					.build();
			Session session = Session.create(params);
			return new Payment("00", "success", session.getUrl());
		} catch (Exception e) {
			return new Payment("01", "fail", "");
		}
	}

	private String applicationUrl(HttpServletRequest req, String path) {
		return "http://" + req.getServerName() + ":" + req.getServerPort() + path;
	}
}
