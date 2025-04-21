package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpImpsNonNpciOw;

@Repository
@Transactional
public interface StpImpsNonNpciOwRepository extends JpaRepository<StpImpsNonNpciOw, Integer> {

	List<StpImpsNonNpciOw> findByBatchidAndStatus(String batchid, String status);
}
