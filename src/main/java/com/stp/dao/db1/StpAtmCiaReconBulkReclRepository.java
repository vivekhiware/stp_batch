package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpAtmCiaReconBulkRecl;

@Repository
@Transactional
public interface StpAtmCiaReconBulkReclRepository extends JpaRepository<StpAtmCiaReconBulkRecl, Integer> {

	List<StpAtmCiaReconBulkRecl> findByBatchidAndStatus(String batchid, String status);
}
