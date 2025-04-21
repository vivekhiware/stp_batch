package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpImpsNoncbsOw;

@Repository
@Transactional
public interface StpImpsNoncbsOwRepository extends JpaRepository<StpImpsNoncbsOw, Integer> {

	List<StpImpsNoncbsOw> findByBatchidAndStatus(String batchid, String status);
}
