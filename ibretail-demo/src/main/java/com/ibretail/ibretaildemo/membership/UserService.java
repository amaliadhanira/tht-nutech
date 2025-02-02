package com.ibretail.ibretaildemo.membership;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class UserService {
	
	private final UserJpaRepository userRepository;
	
	@Autowired
    private FileStorageService fileStorageService;
	
//	@Value("${image.upload.dir}")
	private static final String UPLOAD_DIR = "D://tht-nutech";
//	@PersistenceContext
//	private EntityManager entityManager;
	@Autowired
    public UserService(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public User findById(long id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public User findByEmailAndPassword(String email, String password) {
		 return userRepository.findByEmailAndPassword(email, password)
		           .orElse(null);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}
	
	public String getTokenByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getToken();  
        } else {
            return null; 
        }
    }
	
	public String saveImage(MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR, fileName);
        
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }

	
	public String updateProfileImage(String email, MultipartFile file) throws IOException {
		String fileName = fileStorageService.storeFile(file);

		Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User tidak ditemukan");
        }
        
        User user = userOptional.get();
        user.setProfileImage(fileName);
        userRepository.save(user);

        return fileName;
    }
	
	private String encodeImageToBase64(MultipartFile file) throws IOException {
        byte[] imageBytes = file.getBytes();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
