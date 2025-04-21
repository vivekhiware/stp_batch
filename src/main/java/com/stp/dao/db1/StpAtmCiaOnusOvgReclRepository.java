package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpAtmCiaOnusOvgRecl;

@Repository
@Transactional
public interface StpAtmCiaOnusOvgReclRepository extends JpaRepository<StpAtmCiaOnusOvgRecl, Integer> {

	List<StpAtmCiaOnusOvgRecl> findByBatchidAndStatus(String batchid, String status);
}
