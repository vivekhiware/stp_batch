package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpAtmCiaMainCbsEj;

@Repository
@Transactional
public interface StpAtmCiaMainCbsEjRepository extends JpaRepository<StpAtmCiaMainCbsEj, Integer> {

	List<StpAtmCiaMainCbsEj> findByBatchidAndStatus(String batchid, String status);
}
