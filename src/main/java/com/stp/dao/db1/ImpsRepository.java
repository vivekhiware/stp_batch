package com.stp.dao.db1;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.STP_IMPS;

@Transactional
@Repository
public interface ImpsRepository extends JpaRepository<STP_IMPS, Integer> {

	List<STP_IMPS> findByMakeridIsNotNullAndCheckeridIsNullAndValuedateBetweenAndStatus(Date str, Date end,
			String status);

	List<STP_IMPS> findByValuedateBetweenAndStatus(Date str, Date end, String status);

}
