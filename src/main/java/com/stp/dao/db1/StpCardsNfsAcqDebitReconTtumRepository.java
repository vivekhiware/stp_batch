package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsNfsAcqDebitReconTtum;

@Repository
@Transactional
public interface StpCardsNfsAcqDebitReconTtumRepository extends JpaRepository<StpCardsNfsAcqDebitReconTtum, Integer> {

	List<StpCardsNfsAcqDebitReconTtum> findByBatchidAndStatus(String batchid, String status);
}
