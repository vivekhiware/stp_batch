package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpImpsPrearbitrationReport;

@Repository
@Transactional
public interface StpImpsPrearbitrationReportRepository extends JpaRepository<StpImpsPrearbitrationReport, Integer> {

	List<StpImpsPrearbitrationReport> findByBatchidAndStatus(String batchid, String status);
}
