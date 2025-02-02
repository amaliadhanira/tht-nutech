package com.ibretail.ibretaildemo.information;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "BANNER")
public class Banner {
	@Id
	private int id;
	private String bannerName;
	private String bannerImg;
	private String bannerDscp;
	
	public Banner() {
		
	}

	public Banner(int id, String bannerName, String bannerImg, String bannerDscp) {
		super();
		this.id = id;
		this.bannerName = bannerName;
		this.bannerImg = bannerImg;
		this.bannerDscp = bannerDscp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}

	public String getBannerImg() {
		return bannerImg;
	}

	public void setBannerImg(String bannerImg) {
		this.bannerImg = bannerImg;
	}

	public String getBannerDscp() {
		return bannerDscp;
	}

	public void setBannerDscp(String bannerDscp) {
		this.bannerDscp = bannerDscp;
	}
}
