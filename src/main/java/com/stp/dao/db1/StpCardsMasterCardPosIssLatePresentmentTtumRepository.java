package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsMasterCardPosIssLatePresentmentTtum;

@Repository
@Transactional
public interface StpCardsMasterCardPosIssLatePresentmentTtumRepository
		extends JpaRepository<StpCardsMasterCardPosIssLatePresentmentTtum, Integer> {

	List<StpCardsMasterCardPosIssLatePresentmentTtum> findByBatchidAndStatus(String batchid, String status);
}
