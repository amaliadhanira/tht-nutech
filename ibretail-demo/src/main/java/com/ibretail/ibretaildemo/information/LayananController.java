package com.ibretail.ibretaildemo.information;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibretail.ibretaildemo.response.ApiResponse;
import com.ibretail.ibretaildemo.security.JwtUtil;

@RestController
@RequestMapping("/service")
public class LayananController {
	@Autowired
	private LayananService layananService;
	
	@GetMapping("/services")
	public ResponseEntity<?> retrieveAllServices(@RequestHeader(value = "Authorization", required = false) String authHeader) {
		
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
		
	    List<Layanan> srvcs = layananService.findAll();
		List<Map<String, Object>> layananList = new ArrayList<>();
		
		 for (Layanan srvc : srvcs) {
	            Map<String, Object> layananMap = new HashMap<>();
	            layananMap.put("service_code", srvc.getServiceCode());
	            layananMap.put("service_name", srvc.getServiceName());
	            layananMap.put("service_icon", srvc.getServiceIcon());
	            layananMap.put("tariff", srvc.getTariff());
	            layananList.add(layananMap);
	        }
	      
	    ApiResponse response = new ApiResponse(
	            0,  
	            "Sukses",  
	            layananList 
	        );

	    return ResponseEntity.ok(response);
	}
}
