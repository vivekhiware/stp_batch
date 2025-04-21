package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpAtmCiaMainProactive;

@Repository
@Transactional
public interface StpAtmCiaMainProactiveRepository extends JpaRepository<StpAtmCiaMainProactive, Integer> {

	List<StpAtmCiaMainProactive> findByBatchidAndStatus(String batchid, String status);
}
