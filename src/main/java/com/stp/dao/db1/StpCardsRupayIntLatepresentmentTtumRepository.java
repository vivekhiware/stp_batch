package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayIntLatepresentmentTtum;

@Repository
@Transactional
public interface StpCardsRupayIntLatepresentmentTtumRepository
		extends JpaRepository<StpCardsRupayIntLatepresentmentTtum, Integer> {

	List<StpCardsRupayIntLatepresentmentTtum> findByBatchidAndStatus(String batchid, String status);
}
