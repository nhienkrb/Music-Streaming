package com.rhymthwave.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class MessageResponse {

	private boolean success;
	private String message;
	private Object data;

	public MessageResponse(boolean success, String message, Object data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}
	public MessageResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
		this.data = "";
	};
}
