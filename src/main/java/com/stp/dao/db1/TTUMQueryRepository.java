package com.stp.dao.db1;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.STP_TTUMQuery;

@Transactional
@Repository
public interface TTUMQueryRepository extends JpaRepository<STP_TTUMQuery, Integer> {

}
