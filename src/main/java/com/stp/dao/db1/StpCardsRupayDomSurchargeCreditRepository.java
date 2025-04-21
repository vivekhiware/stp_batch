package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayDomSurchargeCredit;

@Repository
@Transactional
public interface StpCardsRupayDomSurchargeCreditRepository
		extends JpaRepository<StpCardsRupayDomSurchargeCredit, Integer> {

	List<StpCardsRupayDomSurchargeCredit> findByBatchidAndStatus(String batchid, String status);
}
