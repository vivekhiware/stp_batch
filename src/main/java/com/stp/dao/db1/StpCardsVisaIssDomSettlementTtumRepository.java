package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsVisaIssDomSettlementTtum;

@Repository
@Transactional
public interface StpCardsVisaIssDomSettlementTtumRepository
		extends JpaRepository<StpCardsVisaIssDomSettlementTtum, Integer> {

	List<StpCardsVisaIssDomSettlementTtum> findByBatchidAndStatus(String batchid, String status);
}
