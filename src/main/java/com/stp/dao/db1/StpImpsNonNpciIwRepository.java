package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpImpsNonNpciIw;

@Repository
@Transactional
public interface StpImpsNonNpciIwRepository extends JpaRepository<StpImpsNonNpciIw, Integer> {

	List<StpImpsNonNpciIw> findByBatchidAndStatus(String batchid, String status);
}
