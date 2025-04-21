package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsVisaIssOrgWdlTtum;

@Repository
@Transactional
public interface StpCardsVisaIssOrgWdlTtumRepository
		extends JpaRepository<StpCardsVisaIssOrgWdlTtum, Integer> {

	List<StpCardsVisaIssOrgWdlTtum> findByBatchidAndStatus(String batchid, String status);
}
