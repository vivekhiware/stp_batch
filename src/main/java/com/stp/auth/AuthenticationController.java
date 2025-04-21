package com.stp.auth;

import static com.stp.utility.GenericCLass.STATUS_FAILED;
import static com.stp.utility.GenericCLass.STATUS_SUCCESS;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stp.model.db1.StpLogin;
import com.stp.service.LoginServ;
import com.stp.service.RegistrationServ;
import com.stp.utility.ResponseBean;

@RestController
@RequestMapping("/token")
public class AuthenticationController implements CORSInterface {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	private final JwtTokenUtil jwtTokenUtil;

	private final RegistrationServ registrationServ;

	private final LoginServ service;

	@Autowired
	public AuthenticationController(JwtTokenUtil jwtTokenUtil, RegistrationServ registrationServ, LoginServ service) {
		super();
		this.jwtTokenUtil = jwtTokenUtil;
		this.registrationServ = registrationServ;
		this.service = service;
	}

	// login
	@PostMapping("/generate-tokenApi")
	public String generateTokenApi(@RequestParam(required = false) String emplycd) {
		JSONObject res = new JSONObject(); // Initialize at start to avoid null
		try {
			StpLogin fetchAccessList = registrationServ.fetchAccessList(Integer.parseInt(emplycd));
			final String token = jwtTokenUtil.generateToken(fetchAccessList);
			if (token != null && !token.isEmpty()) {
				res.put("token", token);
			} else {
				res.put("token", "NA"); // Proper key usage
			}
		} catch (JSONException e) {
			res.put("error", "Token generation failed");
		} catch (NumberFormatException e) {
			res.put("error", "Invalid employee code format");
		}
		return res.toString();
	}

	@PostMapping(value = "/generate-token")
	public ResponseBean fetchAccess(@RequestParam(required = false) String emplycd, HttpServletRequest request) {
		ResponseBean bean = new ResponseBean();
		try {
			StpLogin fetchAccessList = registrationServ.fetchAccessList(Integer.parseInt(emplycd));
			if (fetchAccessList == null) {
				bean.setStatus(STATUS_FAILED);
				bean.setMessage("Application access entry not found.");
				return bean;
			}
			// Check if the status is Active ('A')
			if ("A".equalsIgnoreCase(fetchAccessList.getStatus())) {
				boolean checkAlreadyLogin = service.checkAlreadyLogin(fetchAccessList.getEmplycd());
				logger.info("checkAlreadyLogin : {}", checkAlreadyLogin);

				if (checkAlreadyLogin) {
					bean.setData(fetchAccessList);
					bean.setStatus("ALREADY");
					bean.setMessage("User is already logged in.");
				} else {
					final String token = jwtTokenUtil.generateToken(fetchAccessList);
					service.addHistory(request, fetchAccessList.getEmplycd());
					bean.setData(fetchAccessList);
					bean.setStatus(STATUS_SUCCESS);
					bean.setMessage(token);
					request.getSession().setAttribute("LoginSession", fetchAccessList);
					StpLogin access = (StpLogin) request.getSession().getAttribute("LoginSession");
					logger.info("access : {}", access.getEmplycd());

				}
			} else {
				bean.setStatus(STATUS_FAILED);
				bean.setMessage("User does not have active access.");
			}
		} catch (Exception e) {
			logger.error("Error in api_login  fetchAccess mapping", e);
			bean.setStatus(STATUS_FAILED);
			bean.setMessage("Error fetching access: " + e.getMessage());
		}
		return bean;
	}

	@PostMapping(value = "updateAccess")
	public ResponseBean updateAccess(@RequestParam Integer emplycd, HttpServletRequest request) {
		ResponseBean bean = new ResponseBean();
		try {
			StpLogin fetchAccessList = registrationServ.fetchAccessList(emplycd);
			if (fetchAccessList == null) {
				bean.setStatus(STATUS_FAILED);
				bean.setMessage("Application access entry not found.");
				return bean;
			}
			service.updateHistory(emplycd);
			bean.setData(fetchAccessList);
			bean.setStatus(STATUS_SUCCESS);
			bean.setMessage("Application access entry found and Updated.");
		} catch (Exception e) {
			logger.error("Error in api_login updateAccess mapping ", e);
			bean.setStatus(STATUS_FAILED);
			bean.setMessage("Error fetching access: " + e.getMessage());
		}
		return bean;
	}

}
