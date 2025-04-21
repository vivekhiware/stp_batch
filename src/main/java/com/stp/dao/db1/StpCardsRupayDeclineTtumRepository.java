package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayDeclineTtum;

@Repository
@Transactional
public interface StpCardsRupayDeclineTtumRepository
		extends JpaRepository<StpCardsRupayDeclineTtum, Integer> {

	List<StpCardsRupayDeclineTtum> findByBatchidAndStatus(String batchid, String status);
}
