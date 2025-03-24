package com.stp.dao.db1;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.STP_REPORTQUERY;

@Transactional
@Repository
public interface TTUMReportQueryRepository extends JpaRepository<STP_REPORTQUERY, Integer> {

}
