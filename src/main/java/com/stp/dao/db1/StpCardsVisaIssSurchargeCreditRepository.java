package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsVisaIssSurchargeCredit;

@Repository
@Transactional
public interface StpCardsVisaIssSurchargeCreditRepository
		extends JpaRepository<StpCardsVisaIssSurchargeCredit, Integer> {

	List<StpCardsVisaIssSurchargeCredit> findByBatchidAndStatus(String batchid, String status);
}
