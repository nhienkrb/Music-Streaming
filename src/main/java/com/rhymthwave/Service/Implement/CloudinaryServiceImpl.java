package com.rhymthwave.Service.Implement;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.rhymthwave.Service.CloudinaryService;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
	@Autowired
	Cloudinary cloudinary;

	@Override
	public Map<String, Object> Upload(MultipartFile file, String parentFolder, String childFolder) {
		try {
			cloudinary.api().createFolder(parentFolder, ObjectUtils.emptyMap());
			cloudinary.api().createFolder(parentFolder + "/" + childFolder, ObjectUtils.emptyMap());
			String path = parentFolder + "/" + childFolder;
			Map<String, Object> params = new HashMap<>();
			params.put("public_id", path + "/" + file.getOriginalFilename());
			params.put("resource_type", "auto");
			params.put("overwrite", true);
			Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, Object> UploadResizeImage(MultipartFile file, String parentFolder, String childFolder,
			Integer Width, Integer Height) {
		try {
			cloudinary.api().createFolder(parentFolder, ObjectUtils.emptyMap());
			cloudinary.api().createFolder(parentFolder + "/" + childFolder, ObjectUtils.emptyMap());
			String path = parentFolder + "/" + childFolder;
			Transformation transformation = new Transformation().width(Width).height(Height).crop("fit");
			Map<String, Object> params = new HashMap<>();
			params.put("public_id", path + "/" + file.getOriginalFilename());
			params.put("transformation", transformation);
			params.put("overwrite", true);
			Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), params);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<?, ?> uploadMultipleFiles(MultipartFile[] files, String parentFolder, String childFolder) {
		try {
			cloudinary.api().createFolder(parentFolder, ObjectUtils.emptyMap());
			cloudinary.api().createFolder(parentFolder + "/" + childFolder, ObjectUtils.emptyMap());
			String path = parentFolder + "/" + childFolder;
			String[] uploadedUrls = new String[files.length];
			String[] uploadedPublicid = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				Map<String, Object> params = new HashMap<>();
				params.put("public_id", (path + "/" + files[i].getOriginalFilename()));
				params.put("resource_type", "auto");
				Map<?, ?> result = cloudinary.uploader().upload(files[i].getBytes(), params);
				uploadedUrls[i] = String.valueOf(result.get("url"));
				uploadedPublicid[i] =String.valueOf(result.get("public_id"));
			}

			Map<String, Object> response = new HashMap<>();
			response.put("uploadedUrls", uploadedUrls);
			response.put("uploadedPublicid", uploadedPublicid);

			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Map> findListImageByFolder(String parentFolder, String childFolder) {
		try {
			String folderPath = parentFolder + "/" + childFolder;
			Map<String, Object> listParams = ObjectUtils.asMap("type", "upload", "prefix", folderPath);
			Map result = cloudinary.api().resources(listParams);
			return (List<Map>) result.get("resources");
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public Boolean deleteFile(String publicID) {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("public_id", publicID);
			cloudinary.uploader().destroy(publicID, params);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String readLrc(String lrcUrl) {
		try {
			URL url = new URL(lrcUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder content = new StringBuilder();
				while ((inputLine = reader.readLine()) != null) {
					content.append(inputLine);
					//duy trì ký tự xuống dòng
					content.append(System.lineSeparator());
				}
				reader.close();
				String fileContent = content.toString();
				return fileContent;
			} else {
				System.out.println("Failed to retrieve the file. Response code: " + responseCode);
			}
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lrcUrl;
	}

	@Override
	public String downloadFile(String publicId, String format) {
		try {
			Map<String, Object> options = ObjectUtils.asMap("attachment", true);
			String result = cloudinary.privateDownload(publicId, format, options);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getCloudinaryParentFolder() {
		List<String> folderNames = new ArrayList<>();
		try {
			ApiResponse result = cloudinary.api().rootFolders(ObjectUtils.emptyMap());
			List<Map> subFolders = (List<Map>) result.get("folders");
			for (Map folder : subFolders) {
				String folderName = (String) folder.get("name");
				folderNames.add(folderName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folderNames;
	}

	@Override
	public List<String> getCloudinaryChildFolder(String ChildFolder) {
		List<String> folderNames = new ArrayList<>();
		try {
			ApiResponse result = cloudinary.api().subFolders(ChildFolder, ObjectUtils.emptyMap());
			List<Map> subFolders = (List<Map>) result.get("folders");
			for (Map folder : subFolders) {
				String folderName = (String) folder.get("name");
				folderNames.add(folderName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folderNames;
	}

	private String getDownloadPath() {
		String os = System.getProperty("os.name").toLowerCase();
		String downloadPath = System.getProperty("user.home");

		if (os.contains("win")) {
			downloadPath += File.separator + "Downloads";
		} else if (os.contains("mac")) {
			downloadPath += File.separator + "Downloads";
		} else {
			downloadPath += File.separator + "Downloads";
		}

		return downloadPath;
	}
}
