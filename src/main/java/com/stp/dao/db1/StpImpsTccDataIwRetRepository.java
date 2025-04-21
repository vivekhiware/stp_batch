package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpImpsTccDataIwRet;

@Repository
@Transactional
public interface StpImpsTccDataIwRetRepository extends JpaRepository<StpImpsTccDataIwRet, Integer> {

	List<StpImpsTccDataIwRet> findByBatchidAndStatus(String batchid, String status);
}
