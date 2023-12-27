package com.rhymthwave.DTO.payment;

import org.springframework.web.bind.annotation.RequestParam;

import lombok.Data;

@Data
public class VNPayChargeDTO {
	private Float vnp_Amount;
	private String vnp_OrderInfo;
	private String vnp_BankCode;
	private String vnp_PayDate;
	private String vnp_TxnRef;
	private String vnp_BankTranNo;
	private String vnp_CardType;
	private String vnp_TransactionStatus;
}
