package com.rhymthwave.Service.Implement;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rhymthwave.DAO.ImageDAO;
import com.rhymthwave.DAO.NotificationDAO;
import com.rhymthwave.Service.CloudinaryService;
import com.rhymthwave.Service.NotificationService;
import com.rhymthwave.Utilities.GetCurrentTime;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Image;
import com.rhymthwave.entity.Notification;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDAO notificationDAO;

    private final GetHostByRequest getHostByRequest;

    private final CloudinaryService cloudinaryService;

    private final ImageDAO imageDAO;

    private static String FOLDER_CONTAINING_IMAGE_NEWS  = "ImageManager";

    @Override
    public Notification create(Notification entity) {
        return notificationDAO.save(entity);
    }

    @Override
    public Notification update(Notification entity) {
        return null;
    }

    @Override
    public Boolean delete(Integer key) {
        notificationDAO.delete(findOne(key));
        return true;
    }

    @Override
    public Notification findOne(Integer key) {
        return notificationDAO.findById(key).orElse(null);
    }

    @Override
    public List<Notification> findAll() {
        return notificationDAO.findAll();
    }

    @Override
    public Notification save(Notification notification, MultipartFile multipartFile, HttpServletRequest request) {

        Map<String, Object> mapCloudinary = cloudinaryService.Upload(multipartFile,FOLDER_CONTAINING_IMAGE_NEWS , "Notification");
        String urlImage = (String) mapCloudinary.get("url");
        String accessId = (String) mapCloudinary.get("asset_id");
        String public_id = (String) mapCloudinary.get("public_id");
        Image image = new Image();
        image.setUrl(urlImage);
        image.setPublicId(public_id);
        image.setAccessId(accessId);
        imageDAO.save(image);

        notification.setCreateBy(getHostByRequest.getEmailByRequest(request));
        notification.setCreateDate(GetCurrentTime.getTimeNow());
        notification.setPublicIdImage(image.getPublicId());
        notification.setUrlImage(image.getUrl());
        notification.setActive(false);
        notificationDAO.save(notification);

        return notification;
    }

    @Override
    public boolean update(Notification notification, HttpServletRequest request) {
        notification.setModifiedBy(getHostByRequest.getEmailByRequest(request));
        notification.setModifiDate(GetCurrentTime.getTimeNow());
        notificationDAO.save(notification);
        return true;
    }
    
    @Override
	public Notification notifyLatest() {
		return notificationDAO.findNotificationLatest();
	}
}
