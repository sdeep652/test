package com.tcts.foresight.service.impl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tcts.foresight.entity.ConfigEntityCached;
import com.tcts.foresight.entity.UserDetailsEntity;
import com.tcts.foresight.repository.UserRepo;
import com.tcts.foresight.util.JSONUtil;

@Service
public class OTPServiceImpl {
	Logger logger = LoggerFactory.getLogger(OTPServiceImpl.class);

	@Autowired
	private UserManagementServiceImpl lUserManagementServiceImpl;

	@Autowired
	private UserRepo lUserRepo;
	
	@Autowired
	ConfigEntityCached lConfigEntityCached;

	public HashMap<String, String> sendOTP(String userID) {
		HashMap<String, String> map = new HashMap<String, String>();
		UserDetailsEntity lUserDetailsEntity = lUserRepo.findByEmail1(userID);
		String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));

		if (lUserDetailsEntity != null) {
			lUserDetailsEntity.setOtp(otp);
			String phoneNumber = lUserDetailsEntity.getContactNo();
			// store the OTP in DB
			if (lUserDetailsEntity.getOtp_count() == null || lUserDetailsEntity.getOtp_count() == 0L) {
				lUserDetailsEntity.setOtp_count(1L);
			} else if (lUserDetailsEntity.getOtp_count() == 5) {

				map.put("message", "Your OTP Limit is Exceeded!");
				return map;
			} else {
				lUserDetailsEntity.setOtp_count(lUserDetailsEntity.getOtp_count() + 1);
			}

			lUserRepo.save(lUserDetailsEntity);
			// RestTemplate Call
			Thread t = new Thread() {
				public void run() {
					try {

						String email = lUserDetailsEntity.getEmail();
					String url = lConfigEntityCached.getValue("scheduler.api.url")+"/Otp/sendOtp/" + phoneNumber + "/" + otp+"/"+email;
//						String url = "http://localhost:8080/Otp/sendOtp/" + phoneNumber + "/" + otp+"/"+email;
						RestTemplate restTemplate = new RestTemplate();
						restTemplate.postForEntity(url, HttpMethod.POST, String.class);

					} catch (Exception e) {
						logger.error("Exception occur while in Send OTP Rest Template  " + e.getMessage(), e);
					}

				}
			};
			t.start();

			map.put("username", lUserDetailsEntity.getEmail());
			map.put("phoneNumber", lUserDetailsEntity.getContactNo());
			map.put("email", lUserDetailsEntity.getEmail());
			map.put("message", "");

		} else {
			map.put("message", "User Name does not exist!");
			return map;
		}

		return map;

	}

	// validateOTP

	public HashMap<String, String> validateOtp(String userID, String otp) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			UserDetailsEntity lUserDetailsEntity = lUserManagementServiceImpl.fetchUserDataByEmail(userID);
			if (lUserDetailsEntity.getOtp().equalsIgnoreCase(otp)) {
				map.put("isValid", "true");
			} else {
				map.put("isValid", "false");
			}
		} catch (Exception e) {
			logger.error("Exception occur while in validateOtp " + e.getMessage(), e);
		}
		return map;
	}

	// Reset password
	public HashMap<String, String> forgetPassword(String jsonPayLoadMap) {
		UserDetailsEntity userData = new UserDetailsEntity();
		HashMap<String, String> filter = new HashMap<String, String>();
		HashMap<String, String> responseMap = new HashMap<String, String>();
		if (jsonPayLoadMap != null && !jsonPayLoadMap.trim().equals("")) {
			filter = JSONUtil.jsonpayloadMapToHashMap(jsonPayLoadMap);

			if (filter != null && filter.size() > 0) {
				String userName = filter.get("username");
				String password = filter.get("password");
				userData = lUserRepo.findByEmail1(userName);
				userData.setNewpass(password);
				responseMap = lUserManagementServiceImpl.reserPasswordAtBackend(userData);

			}
		}
		return responseMap;
	}

}
