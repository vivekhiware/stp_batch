package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpImpsRccRepraiseReport;

@Repository
@Transactional
public interface StpImpsRccRepraiseReportRepository extends JpaRepository<StpImpsRccRepraiseReport, Integer> {

	List<StpImpsRccRepraiseReport> findByBatchidAndStatus(String batchid, String status);
}
