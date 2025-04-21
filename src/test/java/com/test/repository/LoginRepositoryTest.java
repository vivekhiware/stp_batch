
package com.test.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.stp.StpApplication;
import com.stp.dao.db1.LoginRepository;
import com.stp.model.db1.StpLogin;

@SpringBootTest(classes = StpApplication.class)
class LoginRepositoryTest {
	private static final Logger logger = LoggerFactory.getLogger(LoginRepositoryTest.class);

	@Autowired
	private LoginRepository loginRepository;

	@Test

	@Transactional
	void findById() {
		logger.info(" call findById");
		StpLogin findByEmplycd = loginRepository.findByEmplycd(100);
		assertEquals(100, findByEmplycd.getEmplycd());

	}

	@Test
	void findall() {
		logger.info(" call findall");
		List<StpLogin> findAll = loginRepository.findAll();
		logger.info("Total employees found: {}", findAll.size());
		findAll.stream().forEach(login -> System.out.println("emp list  " + login.getEmplycd()));
		assertFalse(findAll.isEmpty());
	}
}
