package com.ibretail.ibretaildemo.transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.ibretail.ibretaildemo.information.Banner;
import com.ibretail.ibretaildemo.information.Layanan;
import com.ibretail.ibretaildemo.information.LayananService;
import com.ibretail.ibretaildemo.response.ApiResponse;
import com.ibretail.ibretaildemo.response.TransactionData;
import com.ibretail.ibretaildemo.security.JwtUtil;

@RestController
@RequestMapping("/purchase")
public class BillPurchaseController {
	
	@Autowired
	private LayananService layananService;
	
	@Autowired
	private AccountBalanceService accountBalanceService;
	
	@Autowired
	private TransactionHistoryService trxHistoryService;
	
	 @Value("${transaction.default.offset}")
	    private int defaultOffset;

	 @Value("${transaction.default.limit}")
	    private int defaultLimit;
	
	private static final Set<String> VALID_SERVICE_CODES = new HashSet<>(Arrays.asList("PULSA", "PLN", "PDAM", "PAJAK", "PGN", "MUSIK", "TV", "PAKET_DATA", "VOUCHER_GAME", "VOUCHER_MAKANAN", "QURBAN", "ZAKAT"));
	
	@PostMapping("/transaction/{id}")
	public ResponseEntity<ApiResponse> doBillPurchase(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestBody Map<String, String> request, @PathVariable Long id) throws AccountNotFoundException{
		String token = authHeader.substring(7);
		System.out.println("Received Token: " + token);
		
		//Transaction trans = new Transaction();		
		String email = JwtUtil.extractEmail(token);
		
		
	    if (email == null) {
	        ApiResponse response = new ApiResponse(
	            108,  
	            "Token tidak valid atau kadaluwarsa",  
	            null  
	        );
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	    
	    String serviceCode = request.get("service_code");
	    System.out.println("Service Code: " + serviceCode);
	    
	    
	    
	    if(VALID_SERVICE_CODES.contains(serviceCode)) {
	    	Optional<Layanan> layananOptional = layananService.getLayananByCode(serviceCode);
	    	BigDecimal serviceRate = layananService.getServiceRate(serviceCode);
	    	BigDecimal topUpVoucher = serviceRate;
	    	
	    	accountBalanceService.decreaseBalance(id, topUpVoucher);
	    	
	    	Timestamp createdDate = new Timestamp(System.currentTimeMillis());
	        String invoiceNumber = generateInvoiceNumber();
	    	
	        Transaction invoice = new Transaction(invoiceNumber, 
	                "PAYMENT", 
	                layananOptional.get().getServiceName(),
	                serviceRate, 
	                createdDate,
	                serviceCode);
	        
	        trxHistoryService.saveTransaction(invoice);
	        
	        List<Transaction> trxs = trxHistoryService.findAll();
			
			Transaction latestTrx = trxs.get(trxs.size() - 1); // Ambil transaksi terakhir
		    List<Map<String, Object>> trxList = new ArrayList<>();
	        
	            Map<String, Object> trxMap = new HashMap<>();
	            trxMap.put("invoice_number", latestTrx.getInvoiceNumber());  
	            trxMap.put("service_code", latestTrx.getServiceCode()); 
	            trxMap.put("service_name", latestTrx.getDescription()); 
	            trxMap.put("transaction_type", latestTrx.getTrxType());  
	            trxMap.put("total_amount", latestTrx.getTotalAmount());
	            trxMap.put("created_on", latestTrx.getCreatedDate());
	            trxList.add(trxMap);
			
			
			ApiResponse response = new ApiResponse(0, "Transaksi berhasil", trxList);
	        
	        return ResponseEntity.status(HttpStatus.OK).body(response);
	    }else {
	        ApiResponse response = new ApiResponse(
	                111,  
	                "Layanan tidak valid",  
	                 null
	            );
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	    
	}
	
	private String generateInvoiceNumber() {
		LocalDateTime createdOn = LocalDateTime.now();
        String datePart = createdOn.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy"));
        String serialNumber = String.format("%03d", (int) (Math.random() * 1000)); 
        return "INV" + datePart + "-" + serialNumber;
    }
	
	@GetMapping("/transaction/history/1")
	public ResponseEntity<ApiResponse> doTransactionHistory(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestParam(required = false) Integer offset,  @RequestParam(required = false) Integer limit) throws AccountNotFoundException{
		
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
	    
	   

	    int finalOffset = (offset != null) ? offset : defaultOffset;
	    int finalLimit = (limit != null) ? limit : defaultLimit;
		
	    List<Transaction> trx = trxHistoryService.getTransactionHistory(finalOffset, finalLimit);
		List<Map<String, Object>> trxList = new ArrayList<>();
		
		 for (Transaction trxs : trx) {
	            Map<String, Object> trxMap = new HashMap<>();
	            trxMap.put("invoice_number", trxs.getInvoiceNumber());  
	            trxMap.put("description", trxs.getDescription()); 
	            trxMap.put("transaction_type", trxs.getTrxType());  
	            trxMap.put("total_amount", trxs.getTotalAmount());
	            trxMap.put("created_on", trxs.getCreatedDate());
	            trxList.add(trxMap);
	        }
		 
		 TransactionData data = new TransactionData(finalOffset, finalLimit, trxList);
	      
	    ApiResponse response = new ApiResponse(
	            0,  
	            "Get History Berhasil",  
	            data 
	        );

	    return ResponseEntity.ok(response);
	}
	
}
