package com.ibretail.ibretaildemo.membership;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
	private final Path location;

    @Autowired
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) throws IOException {
    	 if (uploadDir == null || uploadDir.isEmpty()) {
             throw new IllegalArgumentException("Direktori upload file tidak ditentukan");
         }
         
         this.location = Paths.get(uploadDir).toAbsolutePath().normalize();
         Files.createDirectories(this.location);
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        Path targetLocation = this.location.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
