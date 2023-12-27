package com.rhymthwave.Service;

import com.rhymthwave.entity.Email;

public interface EmailService {
	void send(Email mail);
	void send(String to, String subject, String body);
	void enqueue(Email mail);
	void queue(String to, String subject, String body);
}
