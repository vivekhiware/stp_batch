package com.stp.dao.db1;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stp.model.db1.STP_CARDS_NFS_ISS_RECON_TTUM;
import com.stp.model.db1.STP_IMPS_TCC_DATA_IW;
import com.stp.model.db1.STP_UPI_ADJUSTMENT_REPORT;
import com.stp.model.db1.STP_UPI_MULTIREVERSAL;
import com.stp.model.db1.STP_UPI_TCC_DATA;

@Repository
@Transactional
public interface CardsStpNfsIssReconTtumRepository extends JpaRepository<STP_CARDS_NFS_ISS_RECON_TTUM, Integer> {

	List<STP_CARDS_NFS_ISS_RECON_TTUM> findByMakeridIsNotNullAndCheckeridIsNullAndValuedateBetweenAndStatus(String str,
			String end, String status);

	@Query(value = "select * FROM stp_cards_nfs_iss_recon_ttum "
			+ "where  DATE_FORMAT(valuedate, '%d-%b-%Y') BETWEEN :str  AND :end	 AND status IN :status", nativeQuery = true)
	List<STP_CARDS_NFS_ISS_RECON_TTUM> findByValuedateBetweenAndStatusIn(@Param("str") String str,
			@Param("end") String end, @Param("status") List<String> status,Pageable pageable);

	List<STP_CARDS_NFS_ISS_RECON_TTUM> findByStatus(String status);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update stp_upi_tcc_data set status=:status, res039=:str039 , res125=:str125, res126=:str126 , approvestatus=:approvestatus  where refnumber=:ref  ")
	int updateSTP_CARDS_NFS_ISS_RECON_TTUM(@Param("status") String status, @Param("str039") String str039,
			@Param("str125") String str125, @Param("str126") String str126,
			@Param("approvestatus") String approvestatus, @Param("ref") String ref);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update stp_cards_nfs_iss_recon_ttum set status=:status, res039reinit=:str039 , res125reinit=:str125, res126reinit=:str126 , approvestatus=:approvestatus  where refnumber=:ref  ")
	int updateRepeatSTP_CARDS_NFS_ISS_RECON_TTUM(@Param("status") String status, @Param("str039") String str039,
			@Param("str125") String str125, @Param("str126") String str126,
			@Param("approvestatus") String approvestatus, @Param("ref") String ref);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update stp_cards_nfs_iss_recon_ttum set status=:status, res039enq=:str039 , res125enq=:str125, res126enq=:str126 , approvestatus=:approvestatus  where refnumber=:ref  ")
	int updateEnquirySTP_CARDS_NFS_ISS_RECON_TTUM(@Param("status") String status, @Param("str039") String str039,
			@Param("str125") String str125, @Param("str126") String str126,
			@Param("approvestatus") String approvestatus, @Param("ref") String ref);

}