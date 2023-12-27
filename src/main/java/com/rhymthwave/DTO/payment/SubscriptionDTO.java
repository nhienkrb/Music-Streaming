package com.rhymthwave.DTO.payment;

import lombok.Data;

@Data
public class SubscriptionDTO {
	private Integer subscriptionId;
	private String prdStripeId;
	private String prdPaypalId;
	private Float price;
	private String packages;
}
