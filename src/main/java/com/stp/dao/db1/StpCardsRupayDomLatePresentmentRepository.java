package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayDomLatePresentment;

@Repository
@Transactional
public interface StpCardsRupayDomLatePresentmentRepository
		extends JpaRepository<StpCardsRupayDomLatePresentment, Integer> {

	List<StpCardsRupayDomLatePresentment> findByBatchidAndStatus(String batchid, String status);
}
