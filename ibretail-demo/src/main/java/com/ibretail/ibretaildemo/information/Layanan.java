package com.ibretail.ibretaildemo.information;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LAYANAN")

public class Layanan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String serviceCode;
	private String serviceName;
	private String serviceIcon;
	private BigDecimal tariff;
	
	public Layanan() {
		
	}

	public Layanan(String serviceCode, String serviceName, String serviceIcon, BigDecimal tariff) {
		super();
//		this.id = id;
		this.serviceCode = serviceCode;
		this.serviceName = serviceName;
		this.serviceIcon = serviceIcon;
		this.tariff = tariff;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceIcon() {
		return serviceIcon;
	}

	public void setServiceIcon(String serviceIcon) {
		this.serviceIcon = serviceIcon;
	}

	public BigDecimal getTariff() {
		return tariff;
	}

	public void setTariff(BigDecimal tariff) {
		this.tariff = tariff;
	}
}
