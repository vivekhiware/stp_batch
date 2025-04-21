package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayDomAdjustmentTtum;

@Repository
@Transactional
public interface StpCardsRupayDomAdjustmentTtumRepository
		extends JpaRepository<StpCardsRupayDomAdjustmentTtum, Integer> {

	List<StpCardsRupayDomAdjustmentTtum> findByBatchidAndStatus(String batchid, String status);
}
