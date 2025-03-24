package com.stp.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stp.model.db1.STP_Login;
import com.stp.service.LoginServ;
import com.stp.service.RegistrationServ;
import com.stp.utility.ResponseBean;

@RestController
@RequestMapping("/token")
public class AuthenticationController implements CORSInterface {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private RegistrationServ registrationServ;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	private LoginServ service;

//login
	@RequestMapping(value = "/generate-tokenApi", method = RequestMethod.POST)
	public String generateTokenApi(@RequestParam(required = false) String emplycd)
			throws AuthenticationException, IOException {
		JSONObject res = null;
		try {
			STP_Login fetchAccessList = registrationServ.fetchAccessList(Integer.parseInt(emplycd));
			final String token = jwtTokenUtil.generateToken(fetchAccessList);
			res = new JSONObject();
			res.put("token", token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res.toString();
	}

	@PostMapping(value = "/generate-token")
	public ResponseBean fetchAccess(@RequestParam(required = false) String emplycd, HttpServletRequest request)
			throws AuthenticationException, IOException {
		ResponseBean bean = new ResponseBean();
		try {
			STP_Login fetchAccessList = registrationServ.fetchAccessList(Integer.parseInt(emplycd));
			if (fetchAccessList == null) {
				bean.setStatus("FAILED");
				bean.setMessage("Application access entry not found.");
				return bean;
			}
			// Check if the status is Active ('A')
			if ("A".equalsIgnoreCase(fetchAccessList.getStatus())) {
				boolean checkAlreadyLogin = service.checkAlreadyLogin(fetchAccessList.getEmplycd());
				System.out.println("checkAlreadyLogin" + checkAlreadyLogin);
				if (checkAlreadyLogin) {
					bean.setData(fetchAccessList);
					bean.setStatus("ALREADY");
					bean.setMessage("User is already logged in.");
				} else {
					final String token = jwtTokenUtil.generateToken(fetchAccessList);
					service.addHistory(request, fetchAccessList.getEmplycd());
					bean.setData(fetchAccessList);
					bean.setStatus("SUCCESS");
					bean.setMessage(token);
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
}
