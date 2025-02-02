package com.ibretail.ibretaildemo.membership;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibretail.ibretaildemo.response.ApiResponse;
import com.ibretail.ibretaildemo.security.JwtTokenProvider;
import com.ibretail.ibretaildemo.security.JwtUtil;
import com.ibretail.ibretaildemo.security.SecurityConstants;
import com.ibretail.ibretaildemo.transaction.AccountBalanceService;

import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.util.Base64;



@RestController
@RequestMapping("/users")
public class UserController {
	private UserService service;
	private final JwtTokenProvider tokenProvider;
	private AccountBalanceService acctBalanceService;
	
	public UserController(UserService service, JwtTokenProvider tokenProvider, AccountBalanceService acctBalanceService) {
		this.service = service;
		this.tokenProvider = tokenProvider;
		this.acctBalanceService = acctBalanceService;
	}
	
	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return service.findAll();
	}
		
	@PostMapping("/registration")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result) {
		
		 if (result.hasErrors()) {
			String errorMessage = result.getFieldError("email").getDefaultMessage();
	        ApiResponse response = new ApiResponse(
	                102,  
	                errorMessage,  
	                null  
	        );
	        return ResponseEntity.badRequest().body(response);
	    }
		 
		service.save(user);
					
		ApiResponse response = new ApiResponse(
	            0,  
	            "Registrasi Sukses",  
	            null  
	    );

	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestBody User user, BindingResult result) {
		
		if (result.hasErrors()) {
			String errorMessage = result.getFieldError("email").getDefaultMessage();
	        ApiResponse response = new ApiResponse(
	                102,  
	                errorMessage,  
	                null  
	        );
	        return ResponseEntity.badRequest().body(response);
	    }
		
		User foundUser = service.findByEmailAndPassword(user.getEmail(), user.getPassword());
		System.out.println("User Found: " + foundUser);
		
		if(foundUser==null) {
			throw new WrongCredentialException(HttpStatus.UNAUTHORIZED, "Username atau password salah");
		}
		
		String token = tokenProvider.createToken(foundUser.getEmail());
		
		foundUser.setToken(token);
	    service.save(foundUser);
		
		ApiResponse response = new ApiResponse(
	            0,  
	            "Login Sukses", 
	            new HashMap<String, String>() {{
	                put("token", token);  
	            }}
	        );
		
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<?> retrieveUserProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authorization token is required");
	    }
		
		String token = authHeader.substring(7);
		System.out.println("Received Token: " + token);
		
		String email = JwtUtil.extractEmail(token);
		
		
	    if (email == null) {
	        ApiResponse response = new ApiResponse(
	            108,  
	            "Token tidak valid atau kadaluwarsa",  
	            null  
	        );
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
		
		User user = service.findByEmail(email);
		
		Map<String, Object> profile = new HashMap<>();
		profile.put("email", user.getEmail());  
	    profile.put("first_name", user.getFirstName()); 
	    profile.put("last_name", user.getLastName());  
	    profile.put("profile_image", user.getProfileImage());
	      
	    ApiResponse response = new ApiResponse(
	            0,  
	            "Sukses",  
	            profile 
	        );

	    return ResponseEntity.ok(response);
	}
	
	@PutMapping("/profile/update")
	public ResponseEntity<?> updateUserProfile(@RequestHeader("Authorization") String authHeader, @RequestBody User newUserData) {
		
		String token = authHeader.substring(7);
		System.out.println("Received Token: " + token);
		
		String email = JwtUtil.extractEmail(token);
		System.out.println("Received Token: " + email);
		
	    if (email == null) {
	        ApiResponse response = new ApiResponse(
	            108,  
	            "Token tidak valid atau kadaluwarsa",  
	            null  
	        );
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	
		Map<String, Object> profile = new HashMap<>();
		Optional<User> userOptional = Optional.ofNullable(service.findByEmail(email));
		
		System.out.println("Email: " + userOptional);
		
		if (userOptional.isPresent()) {
	        User user = userOptional.get();
	        user.setFirstName(newUserData.getFirstName());
	        user.setLastName(newUserData.getLastName());
	        
	        service.save(user);
	        
	        profile.put("email", user.getEmail());  
		    profile.put("first_name", user.getFirstName()); 
		    profile.put("last_name", user.getLastName());  
		    profile.put("profile_image", user.getProfileImage());
	        
	        ApiResponse response = new ApiResponse(
		            0,  
		            "Update Pofile berhasil",  
		            profile 
		        );
	        
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(104, "User tidak ditemukan", null));
	    }
		
	}
	
	@PutMapping("/profile/image")
	public ResponseEntity<?> updateUserImage(@RequestHeader("Authorization") String authHeader, @RequestParam("file") MultipartFile file) throws IOException {
		String token = authHeader.substring(7);
		System.out.println("Received Token: " + token);
		
		String email = JwtUtil.extractEmail(token);
		System.out.println("Received Token: " + email);
		
	    if (email == null) {
	        ApiResponse response = new ApiResponse(
	            108,  
	            "Token tidak valid atau kadaluwarsa",  
	            null  
	        );
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	    
	    String fileType = file.getContentType();
	    System.out.println("Hello " +fileType);
	    
	    if (!fileType.equals("image/jpeg") && !fileType.equals("image/png")) {
	        ApiResponse response = new ApiResponse(
	                109,
	                "Format Image tidak sesuai",
	                null
	        );
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	    
	    String fileName = service.updateProfileImage(email, file);
	    System.out.println(fileName);
	    
	    
		
		Map<String, Object> profile = new HashMap<>();
		Optional<User> userOptional = Optional.ofNullable(service.findByEmail(email));
		
		if (userOptional.isPresent()) {
	        User user = userOptional.get();
	        
	        service.save(user);
	        
	        profile.put("email", email);  
		    profile.put("first_name", user.getFirstName()); 
		    profile.put("last_name", user.getLastName());  
		    profile.put("profile_image", fileName);
	        
	        ApiResponse response = new ApiResponse(
		            0,  
		            "Update Profile Image berhasil",  
		            profile 
		        );
	        
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(104, "User tidak ditemukan", null));
	    }
		
	}
}
