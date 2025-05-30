package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpImpsChargebackReport;

@Repository
@Transactional
public interface StpImpsChargebackReportRepository extends JpaRepository<StpImpsChargebackReport, Integer> {

	List<StpImpsChargebackReport> findByBatchidAndStatus(String batchid, String status);
}
