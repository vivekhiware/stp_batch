package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsMasterCardDomIssSurchargeDebit;

@Repository
@Transactional
public interface StpCardsMasterCardDomIssSurchargeDebitRepository
		extends JpaRepository<StpCardsMasterCardDomIssSurchargeDebit, Integer> {

	List<StpCardsMasterCardDomIssSurchargeDebit> findByBatchidAndStatus(String batchid, String status);
}
