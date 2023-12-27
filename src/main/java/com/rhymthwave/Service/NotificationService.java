package com.rhymthwave.Service;

import com.rhymthwave.entity.Notification;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface NotificationService extends CRUD<Notification,Integer>{
    Notification save(Notification notification, MultipartFile multipartFile, HttpServletRequest request);

    boolean update(Notification notification,HttpServletRequest request);
    
	Notification notifyLatest();
}
