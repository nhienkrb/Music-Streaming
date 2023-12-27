package com.rhymthwave.entity.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment{
	private String status;
	private String message;
	private String URL;
	
	
}
