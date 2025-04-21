package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsNfsIssReconTtumLateReversal;

@Repository
@Transactional
public interface StpCardsNfsIssReconTtumLateReversalRepository
		extends JpaRepository<StpCardsNfsIssReconTtumLateReversal, Integer> {

	List<StpCardsNfsIssReconTtumLateReversal> findByBatchidAndStatus(String batchid, String status);
}
