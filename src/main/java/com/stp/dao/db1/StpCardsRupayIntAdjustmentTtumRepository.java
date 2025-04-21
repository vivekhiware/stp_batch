package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayIntAdjustmentTtum;

@Repository
@Transactional
public interface StpCardsRupayIntAdjustmentTtumRepository
		extends JpaRepository<StpCardsRupayIntAdjustmentTtum, Integer> {

	List<StpCardsRupayIntAdjustmentTtum> findByBatchidAndStatus(String batchid, String status);
}
