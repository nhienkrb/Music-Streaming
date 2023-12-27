package com.rhymthwave.DTO.payment;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class StripeChargeDTO {
	private String stripeToken;
	private String username;
	private String amount;
	private Boolean success;
	private String message;
	private String chargeId;
	private Map<String, Object> additionalInfor = new HashMap<>();
}
