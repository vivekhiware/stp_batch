package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpUpiAdjustmentReport;

@Repository
@Transactional
public interface StpUpiAdjustmentReportRepository extends JpaRepository<StpUpiAdjustmentReport, Integer> {

	List<StpUpiAdjustmentReport> findByBatchidAndStatus(String batchid, String status);
}
