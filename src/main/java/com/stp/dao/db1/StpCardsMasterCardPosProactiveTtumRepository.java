package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsMasterCardPosProactiveTtum;

@Repository
@Transactional
public interface StpCardsMasterCardPosProactiveTtumRepository
		extends JpaRepository<StpCardsMasterCardPosProactiveTtum, Integer> {

	List<StpCardsMasterCardPosProactiveTtum> findByBatchidAndStatus(String batchid, String status);
}
