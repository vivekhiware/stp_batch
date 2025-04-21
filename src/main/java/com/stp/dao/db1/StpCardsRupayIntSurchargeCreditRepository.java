package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayIntSurchargeCredit;

@Repository
@Transactional
public interface StpCardsRupayIntSurchargeCreditRepository
		extends JpaRepository<StpCardsRupayIntSurchargeCredit, Integer> {

	List<StpCardsRupayIntSurchargeCredit> findByBatchidAndStatus(String batchid, String status);
}
