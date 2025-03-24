package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.STP_IMPS_RCC_REPORT_REPRAISE;

@Repository
@Transactional
public interface ImpsStpImpsRccReportRepraiseRepository extends JpaRepository<STP_IMPS_RCC_REPORT_REPRAISE, Integer> {

	List<STP_IMPS_RCC_REPORT_REPRAISE> findByMakeridIsNotNullAndCheckeridIsNullAndValuedateBetweenAndStatus(String str,
			String end, String status);

	@Query(value = "select * FROM stp_imps_rcc_report_repraise "
			+ "where  DATE_FORMAT(valuedate, '%d-%b-%Y') BETWEEN :str  AND :end	 AND status IN :status", nativeQuery = true)
	List<STP_IMPS_RCC_REPORT_REPRAISE> findByValuedateBetweenAndStatusIn(@Param("str") String str,
			@Param("end") String end, @Param("status") List<String> status, Pageable pageable);

	List<STP_IMPS_RCC_REPORT_REPRAISE> findByStatus(String status);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update stp_imps_rcc_report_repraise set status=:status, res039=:str039 , res125=:str125, res126=:str126 , approvestatus=:approvestatus  where refnumber=:ref  ")
	int updateSTP_IMPS_RCC_REPORT_REPRAISE(@Param("status") String status, @Param("str039") String str039,
			@Param("str125") String str125, @Param("str126") String str126,
			@Param("approvestatus") String approvestatus, @Param("ref") String ref);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update stp_imps_rcc_report_repraise set status=:status, res039reinit=:str039 , res125reinit=:str125, res126reinit=:str126 , approvestatus=:approvestatus  where refnumber=:ref  ")
	int updateRepeatSTP_IMPS_RCC_REPORT_REPRAISE(@Param("status") String status, @Param("str039") String str039,
			@Param("str125") String str125, @Param("str126") String str126,
			@Param("approvestatus") String approvestatus, @Param("ref") String ref);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update stp_imps_rcc_report_repraise set status=:status, res039enq=:str039 , res125enq=:str125, res126enq=:str126 , approvestatus=:approvestatus  where refnumber=:ref  ")
	int updateEnquirySTP_IMPS_RCC_REPORT_REPRAISE(@Param("status") String status, @Param("str039") String str039,
			@Param("str125") String str125, @Param("str126") String str126,
			@Param("approvestatus") String approvestatus, @Param("ref") String ref);

}