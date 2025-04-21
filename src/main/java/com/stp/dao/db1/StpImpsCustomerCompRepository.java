package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpImpsCustomerComp;

@Repository
@Transactional
public interface StpImpsCustomerCompRepository extends JpaRepository<StpImpsCustomerComp, Integer> {

	List<StpImpsCustomerComp> findByBatchidAndStatus(String batchid, String status);
}
