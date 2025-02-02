package com.ibretail.ibretaildemo.information;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerJpaRepository extends JpaRepository<Banner, Long> {
	public List<Banner> findAll();
}
