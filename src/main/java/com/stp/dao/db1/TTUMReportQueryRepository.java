package com.stp.dao.db1;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpReportQuery;

@Transactional
@Repository
public interface TTUMReportQueryRepository extends JpaRepository<StpReportQuery, Integer> {

}
