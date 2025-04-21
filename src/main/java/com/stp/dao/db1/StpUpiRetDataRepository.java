package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpUpiRetData;

@Repository
@Transactional
public interface StpUpiRetDataRepository extends JpaRepository<StpUpiRetData, Integer> {

	List<StpUpiRetData> findByBatchidAndStatus(String batchid, String status);
}
