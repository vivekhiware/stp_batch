package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsVisaIssDropTtum;

@Repository
@Transactional
public interface StpCardsVisaIssDropTtumRepository extends JpaRepository<StpCardsVisaIssDropTtum, Integer> {

	List<StpCardsVisaIssDropTtum> findByBatchidAndStatus(String batchid, String status);
}
