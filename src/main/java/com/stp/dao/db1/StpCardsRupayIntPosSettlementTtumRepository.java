package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayIntPosSettlementTtum;

@Repository
@Transactional
public interface StpCardsRupayIntPosSettlementTtumRepository
		extends JpaRepository<StpCardsRupayIntPosSettlementTtum, Integer> {

	List<StpCardsRupayIntPosSettlementTtum> findByBatchidAndStatus(String batchid, String status);
}
