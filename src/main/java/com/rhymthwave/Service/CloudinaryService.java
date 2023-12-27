package com.rhymthwave.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface CloudinaryService {
	
	Map<String,Object> Upload(MultipartFile file, String parentFolder, String childFolder);
	
	Map<String,Object> UploadResizeImage(MultipartFile file, String parentFolder, String childFolder, Integer width, Integer Height);
	
	Map<?, ?> uploadMultipleFiles(MultipartFile[] files, String parentFolder, String childFolder);
	
	List<Map> findListImageByFolder(String parentFolder, String childFolder);
	
	String downloadFile(String publicId, String format);
	
	Boolean deleteFile (String publicID);
	
	String readLrc(String lrcUrl);
	
	List<String> getCloudinaryParentFolder();
	
	List<String> getCloudinaryChildFolder(String ChildFolder);
	
}
