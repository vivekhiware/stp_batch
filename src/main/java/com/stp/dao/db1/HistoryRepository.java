package com.stp.dao.db1;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpHistory;

@Repository
@Transactional
public interface HistoryRepository extends JpaRepository<StpHistory, Integer> {

	boolean existsByEmplycdAndOuttimeIsNull(int emplycd);

	@Modifying
	@Transactional
	@Query("UPDATE StpHistory h SET h.outtime =CURRENT_TIMESTAMP WHERE h.emplycd = :emplycd")
	int updateOutTimeByEmplycd(int emplycd);
}
