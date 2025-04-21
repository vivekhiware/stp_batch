package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpImpsNoncbsIw;

@Repository
@Transactional
public interface StpImpsNoncbsIwRepository extends JpaRepository<StpImpsNoncbsIw, Integer> {

	List<StpImpsNoncbsIw> findByBatchidAndStatus(String batchid, String status);

}
