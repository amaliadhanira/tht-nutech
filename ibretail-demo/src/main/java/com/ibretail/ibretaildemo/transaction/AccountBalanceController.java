package com.ibretail.ibretaildemo.transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibretail.ibretaildemo.response.ApiResponse;
import com.ibretail.ibretaildemo.security.JwtUtil;

@RestController
@RequestMapping("/account")
public class AccountBalanceController {
	@Autowired
    private AccountBalanceService accountBalanceService;
	private TransactionHistoryService trxHistoryService;

    @GetMapping("/balance/{id}")
    public ResponseEntity<ApiResponse> getBalanceById(@RequestHeader(value = "Authorization", required = false) String authHeader, @PathVariable Long id) {
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
	    BigDecimal balance = accountBalanceService.getBalanceById(id);
        
	    Map<String, Object> data = new HashMap<>();
        data.put("balance", balance);
        ApiResponse response = new ApiResponse(0, "Get Balance Berhasil", data);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/topup/{id}")
    public ResponseEntity<ApiResponse> topUpBalance(@RequestHeader(value = "Authorization", required = false) String authHeader, @PathVariable Long id, @RequestBody Map<String, Integer> request)  throws AccountNotFoundException {
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
	    
	   
    	Integer amount = request.get("top_up_amount");
    	BigDecimal topUpAmount = new BigDecimal(amount);
    	//System.out.println("Top Up: " + topUpAmount);
    	
    	Timestamp createdDate = new Timestamp(System.currentTimeMillis());
        String invoiceNumber = generateInvoiceNumber();
    	
        Transaction invoice = new Transaction(invoiceNumber, 
                "TOPUP", 
                "Top Up Balance",
                topUpAmount, 
                createdDate,
                "Balance");
        
        trxHistoryService.saveTransaction(invoice);
    	
    	if (amount < 0) {
			String errorMessage = "Paramter amount hanya boleh angka dan tidak boleh lebih kecil dari 0";
	        ApiResponse response = new ApiResponse(
	                102,  
	                errorMessage,  
	                null  
	        );
	        return ResponseEntity.badRequest().body(response);
	    }
    	
        AccountBalance updatedBalance = accountBalanceService.topUpBalance(id, topUpAmount);
        

        Map<String, Object> data = new HashMap<>();
        data.put("balance", updatedBalance.getBalance());

        ApiResponse response = new ApiResponse(0, "Top Up Berhasil", data);
        return ResponseEntity.ok(response);
    }
    
    private String generateInvoiceNumber() {
		LocalDateTime createdOn = LocalDateTime.now();
        String datePart = createdOn.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy"));
        String serialNumber = String.format("%03d", (int) (Math.random() * 1000)); 
        return "INV" + datePart + "-" + serialNumber;
    }
}
