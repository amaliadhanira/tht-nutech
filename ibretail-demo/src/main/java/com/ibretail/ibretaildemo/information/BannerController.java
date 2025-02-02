package com.ibretail.ibretaildemo.information;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibretail.ibretaildemo.response.ApiResponse;


@RestController
@RequestMapping("/banner")
public class BannerController {
	@Autowired
	private BannerService bannerService;
	
	@GetMapping("/banner")
	public ResponseEntity<ApiResponse> retrieveBanners(){
		List<Banner> banners = bannerService.findAll();
		List<Map<String, Object>> bannerList = new ArrayList<>();
		
		 for (Banner banner : banners) {
	            Map<String, Object> bannerMap = new HashMap<>();
	            bannerMap.put("banner_name", banner.getBannerName());
	            bannerMap.put("banner_image", banner.getBannerImg());
	            bannerMap.put("description", banner.getBannerDscp());
	            bannerList.add(bannerMap);
	        }

	        ApiResponse response = new ApiResponse(0, "Sukses", bannerList);
	        return ResponseEntity.ok(response);
	}

}
