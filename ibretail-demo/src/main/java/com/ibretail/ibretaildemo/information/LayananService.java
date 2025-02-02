package com.ibretail.ibretaildemo.information;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LayananService {
	private final LayananJpaRepository layananJpaRepository;
	
	@Autowired
	public LayananService(LayananJpaRepository layananJpaRepository) {
		super();
		this.layananJpaRepository = layananJpaRepository;
	}
	
	public List<Layanan> findAll() {
		return layananJpaRepository.findAll();
	}
	
	public BigDecimal getServiceRate(String serviceCode) {
		Optional<Layanan> layananOptional = layananJpaRepository.findByServiceCode(serviceCode);
		 return layananOptional.map(Layanan::getTariff)
                 .orElseThrow(() -> new NoSuchElementException("Service code not found"));
	}
	
	public Optional<Layanan> getLayananByCode(String serviceCode) {
        return layananJpaRepository.findByServiceCode(serviceCode);
    }
}
