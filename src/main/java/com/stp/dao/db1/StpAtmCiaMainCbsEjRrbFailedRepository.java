package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpAtmCiaMainCbsEjRrbFailed;

@Repository
@Transactional
public interface StpAtmCiaMainCbsEjRrbFailedRepository extends JpaRepository<StpAtmCiaMainCbsEjRrbFailed, Integer> {

	List<StpAtmCiaMainCbsEjRrbFailed> findByBatchidAndStatus(String batchid, String status);
}
