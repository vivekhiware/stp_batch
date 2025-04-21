package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpUpiMultiReversal;

@Repository
@Transactional
public interface StpUpiMultiReversalRepository extends JpaRepository<StpUpiMultiReversal, Integer> {

	List<StpUpiMultiReversal> findByBatchidAndStatus(String batchid, String status);
}
