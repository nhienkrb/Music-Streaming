package com.rhymthwave.ServiceAdmin;

public interface INotification<T> {
	
	void sendNotification(T noti, String urlImage);

	void sendEmailBan(String email,String message);

	void sendEmailWarring(String email);

	void sendEmailComfirmUser(String url, String email);

}
