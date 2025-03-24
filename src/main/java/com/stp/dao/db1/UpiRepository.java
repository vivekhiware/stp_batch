package com.stp.dao.db1;

import java.util.*;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.STP_UPI;
import com.stp.model.db1.STP_UPI_NSTL_NETSET_TTUM;

@Repository
@Transactional
public interface UpiRepository extends JpaRepository<STP_UPI, Integer> {

	List<STP_UPI> findByMakeridIsNotNullAndCheckeridIsNullAndValuedateBetweenAndStatus(Date str, Date end,
			String status);

	List<STP_UPI> findByValuedateBetweenAndStatusIn(Date str, Date end, List<String> status);

}