package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.STP_Access;

@Repository
@Transactional
public interface AccessRepository extends JpaRepository<STP_Access, Integer> {

	STP_Access findByAppname(String appname);

	List<STP_Access> findByStatus(String status);
}
