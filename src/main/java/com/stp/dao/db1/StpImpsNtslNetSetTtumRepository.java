package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpImpsNtslNetSetTtum;

@Repository
@Transactional
public interface StpImpsNtslNetSetTtumRepository extends JpaRepository<StpImpsNtslNetSetTtum, Integer> {

	List<StpImpsNtslNetSetTtum> findByBatchidAndStatus(String batchid, String status);
}
