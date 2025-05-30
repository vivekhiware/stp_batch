package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpUpiNtlsNetSetTtum;

@Repository
@Transactional
public interface StpUpiNtlsNetSetTtumRepository extends JpaRepository<StpUpiNtlsNetSetTtum, Integer> {

	List<StpUpiNtlsNetSetTtum> findByBatchidAndStatus(String batchid, String status);
}
