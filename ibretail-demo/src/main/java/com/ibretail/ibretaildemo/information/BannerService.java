package com.ibretail.ibretaildemo.information;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerService {
	
	private final BannerJpaRepository bannerJpaRepository;
	
	@Autowired
	public BannerService(BannerJpaRepository bannerJpaRepository) {
		super();
		this.bannerJpaRepository = bannerJpaRepository;
	}
	
	public List<Banner> findAll() {
		return bannerJpaRepository.findAll();
	}
}
