package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayIntDeclineTtum;

@Repository
@Transactional
public interface StpCardsRupayIntDeclineTtumRepository extends JpaRepository<StpCardsRupayIntDeclineTtum, Integer> {

	List<StpCardsRupayIntDeclineTtum> findByBatchidAndStatus(String batchid, String status);
}
