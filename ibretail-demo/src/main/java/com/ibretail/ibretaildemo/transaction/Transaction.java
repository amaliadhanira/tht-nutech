package com.ibretail.ibretaildemo.transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "TRX_HISTORY")
public class Transaction {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String invoiceNumber;
	private String trxType;
	private String description;
	private BigDecimal totalAmount;
	private String serviceCode;
	
	 @Column(name = "created_date", nullable = false, updatable = false)
	 @Temporal(TemporalType.TIMESTAMP)
	private Timestamp createdDate;
	 
	 public Transaction() {
		 
	  }

	public Transaction(String invoiceNumber, String trxType, String description, BigDecimal totalAmount,
			Timestamp createdDate, String serviceCode) {
		super();
		this.invoiceNumber = invoiceNumber;
		this.trxType = trxType;
		this.description = description;
		this.totalAmount = totalAmount;
		this.createdDate = new Timestamp(System.currentTimeMillis());
		this.serviceCode = serviceCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getTrxType() {
		return trxType;
	}

	public void setTrxType(String trxType) {
		this.trxType = trxType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
}
