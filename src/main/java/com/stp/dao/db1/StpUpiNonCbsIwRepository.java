package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpUpiNonCbsIw;

@Repository
@Transactional
public interface StpUpiNonCbsIwRepository extends JpaRepository<StpUpiNonCbsIw, Integer> {

	List<StpUpiNonCbsIw> findByBatchidAndStatus(String batchid, String status);
}
