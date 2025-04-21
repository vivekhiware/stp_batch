package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpAtmCiaOnusCmpl;

@Repository
@Transactional
public interface StpAtmCiaOnusCmplRepository extends JpaRepository<StpAtmCiaOnusCmpl, Integer> {

	List<StpAtmCiaOnusCmpl> findByBatchidAndStatus(String batchid, String status);
}
