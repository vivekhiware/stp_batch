package com.stp.service.impl;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stp.dao.db1.LoginRepository;
import com.stp.exception.DetailNotFoundException;
import com.stp.model.db1.StpLogin;
import com.stp.service.RegistrationServ;

@Service(value = "userService")
public class RegistrationServImpl implements RegistrationServ, UserDetailsService {

	private final LoginRepository loginRepository;

	@Autowired
	public RegistrationServImpl(LoginRepository loginRepository) {
		super();
		this.loginRepository = loginRepository;
	}

	public StpLogin addAccessDetail(StpLogin access) {
		// Adds a new access detail record to the database
		return loginRepository.save(access);
	}

	@Override
//	@Cacheable(value = "fetchAccessListCache", key = "#emplycd")
	public StpLogin fetchAccessList(Integer emplycd) {
		// Fetches the access details for a specific employee code
		return loginRepository.findByEmplycd(emplycd);
	}

	@Override
//	@Cacheable(value = "fetchAccessListAllCache")
	public List<StpLogin> fetchAccessListAll() {
		// Fetches all access details from the database
		return loginRepository.findAll();
	}

	@Override
	@Transactional
//	@Cacheable(value = "removeAccessDetailCache", key = "#emplycd")
	public StpLogin removeAccessDetail(Integer emplycd) {
		StpLogin accessDetail = loginRepository.findByEmplycd(emplycd);

		if (accessDetail == null) {
			// Throw a custom exception if employee not found
			throw new DetailNotFoundException("Employee not found for ID: " + emplycd);
		}

		if (!"N".equalsIgnoreCase(accessDetail.getStatus())) {
			// Modify the relevant fields to mark access as inactive
			accessDetail.setStatus("N");
			accessDetail.setUpdateDate(Date.from(Instant.now()));

			// Save the updated entity back to the database
			return loginRepository.save(accessDetail);
		} else {
			// Throw a custom exception if access detail is already inactive
			throw new DetailNotFoundException("Access detail already inactive for employee: " + emplycd);
		}
	}

	@Override
	@Transactional
	public StpLogin updateAccess(StpLogin access) {
		StpLogin accessDetail = loginRepository.findByEmplycd(access.getEmplycd());
		if (accessDetail == null) {
			// Throw a custom exception if employee not found
			throw new DetailNotFoundException("Employee not found for ID: " + access.getEmplycd());
		}
		if (!"N".equalsIgnoreCase(accessDetail.getStatus())) {
			// Modify the relevant fields to mark access as inactive
			accessDetail.setStatus("N");
			accessDetail.setReason(access.getReason());
			accessDetail.setUpdateDate(Date.from(Instant.now()));
			accessDetail.setUpdateBy(access.getUpdateBy());
			// Save the updated entity back to the database
			return loginRepository.save(accessDetail);
		} else {
			// Modify the relevant fields to mark access as inactive
			accessDetail.setStatus("A");
			accessDetail.setReason(access.getReason());
			accessDetail.setUpdateDate(Date.from(Instant.now()));
			accessDetail.setUpdateBy(access.getUpdateBy());
			// Save the updated entity back to the database
			return loginRepository.save(accessDetail);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		StpLogin user = loginRepository.findByEmplycd(Integer.parseInt(username));
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmplycd().toString(),
				user.getCredential(), getAuthority());

	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

}
