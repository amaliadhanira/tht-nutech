package com.ibretail.ibretaildemo.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountBalanceJpaRepository extends JpaRepository<AccountBalance, Long>{
	

}
