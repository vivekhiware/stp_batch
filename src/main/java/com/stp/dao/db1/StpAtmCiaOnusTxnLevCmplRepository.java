package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpAtmCiaOnusTxnLevCmpl;

@Repository
@Transactional
public interface StpAtmCiaOnusTxnLevCmplRepository extends JpaRepository<StpAtmCiaOnusTxnLevCmpl, Integer> {

	List<StpAtmCiaOnusTxnLevCmpl> findByBatchidAndStatus(String batchid, String status);
}
