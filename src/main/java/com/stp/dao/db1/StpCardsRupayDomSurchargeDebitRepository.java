package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayDomSurchargeDebit;

@Repository
@Transactional
public interface StpCardsRupayDomSurchargeDebitRepository
		extends JpaRepository<StpCardsRupayDomSurchargeDebit, Integer> {

	List<StpCardsRupayDomSurchargeDebit> findByBatchidAndStatus(String batchid, String status);
}
