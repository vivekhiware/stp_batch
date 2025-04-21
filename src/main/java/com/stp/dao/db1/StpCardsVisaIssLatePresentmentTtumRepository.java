package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsVisaIssLatePresentmentTtum;

@Repository
@Transactional
public interface StpCardsVisaIssLatePresentmentTtumRepository
		extends JpaRepository<StpCardsVisaIssLatePresentmentTtum, Integer> {

	List<StpCardsVisaIssLatePresentmentTtum> findByBatchidAndStatus(String batchid, String status);
}
