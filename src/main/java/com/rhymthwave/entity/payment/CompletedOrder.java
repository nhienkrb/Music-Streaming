package com.rhymthwave.entity.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletedOrder {
	private String status;
	private String token;

	public CompletedOrder(String status) {
		this.status = status;
	}
}
