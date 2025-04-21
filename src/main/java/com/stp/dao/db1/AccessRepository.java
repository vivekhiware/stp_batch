package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpAccess;

@Repository
@Transactional
public interface AccessRepository extends JpaRepository<StpAccess, Integer> {

	StpAccess findByAppname(String appname);

	List<StpAccess> findByStatus(String status);
}
