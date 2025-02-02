package com.ibretail.ibretaildemo.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TransactionHistoryService {

	@Autowired
	private TransactionRepository trxRepository;
	
	public void saveTransaction(Transaction trx) {
	    trxRepository.save(trx); 
	}
	
	public List<Transaction> findAll() {
		return trxRepository.findAll();
	}
	
	public List<Transaction> getTransactionHistory(int offset, int limit) {
        return trxRepository.findAllByOrderByCreatedDateDesc()
                                    .stream()
                                    .skip(offset)
                                    .limit(limit)
                                    .toList();
    }

}
