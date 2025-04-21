package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsNfsIssReconTtumProactive;

@Repository
@Transactional
public interface StpCardsNfsIssReconTtumProactiveRepository
		extends JpaRepository<StpCardsNfsIssReconTtumProactive, Integer> {

	List<StpCardsNfsIssReconTtumProactive> findByBatchidAndStatus(String batchid, String status);
}
