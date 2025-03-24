package com.stp.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stp.auth.JwtTokenUtil;
import com.stp.model.db1.STP_Login;
import com.stp.service.LoginServ;
import com.stp.service.RegistrationServ;
import com.stp.utility.ResponseBean;

@RestController
@RequestMapping(value = "/api_login")
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	private final LoginServ service;

	private final RegistrationServ registrationServ;

	@Autowired
	public LoginController(LoginServ service, RegistrationServ registrationServ) {
		super();
		this.service = service;
		this.registrationServ = registrationServ;
	}

	@PostMapping(value = "updateAccess")
	public ResponseBean updateAccess(@RequestParam Integer emplycd, HttpServletRequest request) {
		ResponseBean bean = new ResponseBean();
		try {
			STP_Login fetchAccessList = registrationServ.fetchAccessList(emplycd);
			if (fetchAccessList == null) {
				bean.setStatus("FAILED");
				bean.setMessage("Application access entry not found.");
				return bean;
			}
			service.updateHistory(emplycd);
			bean.setData(fetchAccessList);
			bean.setStatus("SUCCESS");
			bean.setMessage("Application access entry found and Updated.");
		} catch (Exception e) {
			logger.error("Error in api_login updateAccess mapping ", e);
			bean.setStatus("FAILED");
			bean.setMessage("Error fetching access: " + e.getMessage());
		}
		return bean;
	}

	@PostMapping(value = "/fetchAccess")
	public ResponseBean fetchAccess(@RequestParam(required = false) String emplycd, HttpServletRequest request) {
		ResponseBean bean = new ResponseBean();
		System.out.println("emplycd" + emplycd);
		Integer emplycd1 = Integer.parseInt(emplycd);
		try {
			STP_Login fetchAccessList = registrationServ.fetchAccessList(emplycd1);
			if (fetchAccessList == null) {
				bean.setStatus("FAILED");
				bean.setMessage("Application access entry not found.");
				return bean;
			}
			// Check if the status is Active ('A')
			if ("A".equalsIgnoreCase(fetchAccessList.getStatus())) {
				boolean checkAlreadyLogin = service.checkAlreadyLogin(emplycd1);
				System.out.println("checkAlreadyLogin" + checkAlreadyLogin);
				if (checkAlreadyLogin) {
					bean.setData(fetchAccessList);
					bean.setStatus("ALREADY");
					bean.setMessage("User is already logged in.");
				} else {
					service.addHistory(request, emplycd1);
					bean.setData(fetchAccessList);
					bean.setStatus("SUCCESS");
					bean.setMessage("Application access entry found.");
					request.getSession().setAttribute("LoginSession", fetchAccessList);
					STP_Login access = (STP_Login) request.getSession().getAttribute("LoginSession");
					System.out.println("access" + access.getEmplycd());
				}
			} else {
				bean.setStatus("FAILED");
				bean.setMessage("User does not have active access.");
			}
		} catch (Exception e) {
			logger.error("Error in api_login  fetchAccess mapping ", e);
			bean.setStatus("FAILED");
			bean.setMessage("Error fetching access: " + e.getMessage());
		}
		return bean;
	}
}
