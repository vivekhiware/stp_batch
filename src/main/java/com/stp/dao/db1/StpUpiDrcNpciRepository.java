package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpUpiDrcNpci;

@Repository
@Transactional
public interface StpUpiDrcNpciRepository extends JpaRepository<StpUpiDrcNpci, Integer> {

	List<StpUpiDrcNpci> findByBatchidAndStatus(String batchid, String status);
}
