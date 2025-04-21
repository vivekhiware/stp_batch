package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.StpCardsRupayIntProactiveTtum;

@Repository
@Transactional
public interface StpCardsRupayIntProactiveTtumRepository extends JpaRepository<StpCardsRupayIntProactiveTtum, Integer> {

	List<StpCardsRupayIntProactiveTtum> findByBatchidAndStatus(String batchid, String status);
}
