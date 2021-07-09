package com.example.filedemo.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.filedemo.exception.FileNotFoundException;
import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.property.FileStorageProperties;

@Service
public class FileService {

	private final Path fileLocation;

	@Autowired
	public FileService(FileStorageProperties fileStorageProperties) {
		
		this.fileLocation = Paths.get(fileStorageProperties.getUploadDirectory()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileLocation);
		} catch (Exception e) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
		}
	}

	public String storeFile(MultipartFile file) {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {

			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			Path targetLocation = this.fileLocation.resolve(fileName);
			
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
			
		} catch (IOException e) {			
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
		}
	}

	public Resource loadFileAsResource(String fileName) {
		
		try {
			
			Path filePath = this.fileLocation.resolve(fileName).normalize();
			
			Resource resource = new UrlResource(filePath.toUri());
			
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileNotFoundException("File not found " + fileName);
			}
			
		} catch (MalformedURLException e) {
			throw new FileNotFoundException("File not found " + fileName, e);
		}
	}
}