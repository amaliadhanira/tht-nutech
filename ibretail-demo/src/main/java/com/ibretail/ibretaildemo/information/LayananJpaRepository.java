package com.ibretail.ibretaildemo.information;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LayananJpaRepository extends JpaRepository<Layanan, Long> {
	public List<Layanan> findAll();
	Optional<Layanan> findByServiceCode(String serviceCode);
}
