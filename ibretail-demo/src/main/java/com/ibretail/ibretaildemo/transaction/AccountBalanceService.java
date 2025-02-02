package com.ibretail.ibretaildemo.transaction;

import java.math.BigDecimal;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountBalanceService {
	 	@Autowired
	    private AccountBalanceJpaRepository accountBalanceRepository;
	 	
	 	@Autowired
	 	public AccountBalanceService(AccountBalanceJpaRepository accountBalanceRepository) {
	 	    this.accountBalanceRepository = accountBalanceRepository;
	 	}

	    public BigDecimal getBalanceById(Long id) {
	        Optional<AccountBalance> accountBalance = accountBalanceRepository.findById(id);
	        return accountBalance.map(AccountBalance::getBalance)
	                .orElseThrow(() -> new RuntimeException("Account Balance not found with id: " + id));
	    }
	    
	    public AccountBalance topUpBalance(Long id, BigDecimal topUpAmount) throws AccountNotFoundException {
	    	AccountBalance accountBalance = accountBalanceRepository.findById(id).orElseThrow(() -> 
            new AccountNotFoundException("Account with ID " + id + " not found"));
	    	
	    	System.out.println("Current balance: " + accountBalance.getBalance());
	    	
	        accountBalance.setBalance(accountBalance.getBalance().add(topUpAmount));
	        
	        System.out.println("New balance: " + accountBalance.getBalance());

	        return accountBalanceRepository.save(accountBalance);
	    }
	    
	    public AccountBalance decreaseBalance(Long id, BigDecimal tariff) throws AccountNotFoundException {
	    	AccountBalance accountBalance = accountBalanceRepository.findById(id).orElseThrow(() -> 
            new AccountNotFoundException("Account with ID " + id + " not found"));
	    	
	    	System.out.println("Current balance: " + accountBalance.getBalance());
	    	
	        accountBalance.setBalance(accountBalance.getBalance().subtract(tariff));
	        
	        System.out.println("New balance: " + accountBalance.getBalance());

	        return accountBalanceRepository.save(accountBalance);
	    }
}
