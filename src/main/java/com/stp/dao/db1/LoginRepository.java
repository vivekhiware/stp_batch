package com.stp.dao.db1;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.STP_Login;

@Repository
@Transactional
public interface LoginRepository extends JpaRepository<STP_Login, Integer> {

	STP_Login findByEmplycd(Integer emplycd);

	@Modifying
	STP_Login deleteByEmplycd(Integer emplycd);

	@Modifying
	@Query(value = " update  stp_login set  password=?1  where  emplycd =?2", nativeQuery = true)
	int updateUserDetailBypassword(String password, Integer username);
}
