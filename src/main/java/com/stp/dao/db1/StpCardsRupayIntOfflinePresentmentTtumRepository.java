package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayIntOfflinePresentmentTtum;

@Repository
@Transactional
public interface StpCardsRupayIntOfflinePresentmentTtumRepository
		extends JpaRepository<StpCardsRupayIntOfflinePresentmentTtum, Integer> {

	List<StpCardsRupayIntOfflinePresentmentTtum> findByBatchidAndStatus(String batchid, String status);
}
