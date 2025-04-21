package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpAtmCiaOnusSrlMsdCmpl;

@Repository
@Transactional
public interface StpAtmCiaOnusSrlMsdCmplRepository extends JpaRepository<StpAtmCiaOnusSrlMsdCmpl, Integer> {

	List<StpAtmCiaOnusSrlMsdCmpl> findByBatchidAndStatus(String batchid, String status);
}
