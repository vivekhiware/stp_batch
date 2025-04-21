package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsNfsAcqCreditReconTtum;

@Repository
@Transactional
public interface StpCardsNfsAcqCreditReconTtumRepository extends JpaRepository<StpCardsNfsAcqCreditReconTtum, Integer> {

	List<StpCardsNfsAcqCreditReconTtum> findByBatchidAndStatus(String batchid, String status);
}
