package com.tcts.foresight.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.service.impl.OTPServiceImpl;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class OTPController {

	@Autowired
	private OTPServiceImpl lOTPServiceImpl;

	@GetMapping("forgetPassword/{userID}")
	public HashMap<String, String> fetchOTP(@PathVariable String userID) {
		HashMap<String, String> otpMap = lOTPServiceImpl.sendOTP(userID);

		return otpMap;
	}

	@PostMapping("forgetPassword/validateOtp/{otp}/{userID}")
	public HashMap<String, String> fetchOTP(@PathVariable String userID, @PathVariable String otp) {
		HashMap<String, String> validation = lOTPServiceImpl.validateOtp(userID, otp);

		return validation;
	}

	@PostMapping("forgetPassword/setPassword")
	public HashMap<String, String> forgetPassword(@RequestBody String jsonPayLoadMap) {
		HashMap<String, String> status = lOTPServiceImpl.forgetPassword(jsonPayLoadMap);
		return status;
	}

}
