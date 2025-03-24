/*
 * package com.test.repository;
 * 
 * import static org.junit.jupiter.api.Assertions.assertEquals; import static
 * org.junit.jupiter.api.Assertions.assertFalse; import static
 * org.junit.jupiter.api.Assertions.assertTrue;
 * 
 * import java.util.List; import java.util.Optional;
 * 
 * import javax.persistence.EntityManager; import
 * javax.transaction.Transactional;
 * 
 * import org.junit.jupiter.api.Test; import org.slf4j.Logger; import
 * org.slf4j.LoggerFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.boot.test.context.SpringBootTest;
 * 
 * import com.stp.STP_Application; import com.stp.dao.db1.LoginRepository;
 * import com.stp.model.db1.STP_Login;
 * 
 * @SpringBootTest(classes = STP_Application.class)
 * 
 * public class LoginRepositoryTest { private static final Logger logger =
 * LoggerFactory.getLogger(LoginRepositoryTest.class);
 * 
 * @Autowired private LoginRepository loginRepository;
 * 
 * @Test
 * 
 * @Transactional public void findById() { logger.info(" call findById");
 * STP_Login findByEmplycd = loginRepository.findByEmplycd(6346);
 * assertEquals(6346, findByEmplycd.getEmplycd());
 * 
 * }
 * 
 * @Test public void findall() { logger.info(" call findall"); List<STP_Login>
 * findAll = loginRepository.findAll(); logger.info("Total employees found: {}",
 * findAll.size()); findAll.stream().forEach(login ->
 * System.out.println("emp list  " + login.getEmplycd()));
 * assertFalse(findAll.isEmpty()); } }
 */