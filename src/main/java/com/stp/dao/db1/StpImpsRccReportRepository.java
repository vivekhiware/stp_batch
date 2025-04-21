package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpImpsRccReport;

@Repository
@Transactional
public interface StpImpsRccReportRepository extends JpaRepository<StpImpsRccReport, Integer> {

	List<StpImpsRccReport> findByBatchidAndStatus(String batchid, String status);
}
