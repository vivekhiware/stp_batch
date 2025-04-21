package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpUpiNonCbsOw;

@Repository
@Transactional
public interface StpUpiNonCbsOwRepository extends JpaRepository<StpUpiNonCbsOw, Integer> {
	List<StpUpiNonCbsOw> findByBatchidAndStatus(String batchid, String status);
}
