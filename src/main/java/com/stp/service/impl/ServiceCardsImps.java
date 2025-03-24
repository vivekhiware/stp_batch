package com.stp.service.impl;

import static com.stp.utility.GenericCLass.creditTransaction;
import static com.stp.utility.GenericCLass.debitTransaction;
import static com.stp.utility.GenericCLass.notFailedTransaction;
import static com.stp.utility.GenericCLass.repostTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.iso.config.IsoV93MessageRes;
import com.iso.opt.IsoPackagerGlobal;
import com.stp.dao.db1.CardsStpNfsIssReconTtumRepository;
import com.stp.exception.DetailNotFoundException;
import com.stp.model.db1.STP_CARDS_NFS_ISS_RECON_TTUM;
import com.stp.service.ServiceCards;
import com.stp.utility.TTumRequest;
import com.stp.utility.TypeEnum;

@Service
public class ServiceCardsImps implements ServiceCards {
	private static final Logger logger = LoggerFactory.getLogger(ServiceCardsImps.class);

	@Autowired
	private CardsStpNfsIssReconTtumRepository crdNfsIssReconTtumRepository;

	public List<String> getStatusList(String status) {
		List<String> statuses = null;
		if (status.equalsIgnoreCase("L1")) {
			statuses = Arrays.asList("L1", "R1");
		} else if (status.equalsIgnoreCase("L2")) {
			statuses = Arrays.asList("L2", "R2");
		} else if (status.equalsIgnoreCase("L3")) {
			statuses = Arrays.asList("L3", "R3");
		} else if (status.equalsIgnoreCase("L4")) {
			statuses = Arrays.asList("L4");
		} else if (status.equalsIgnoreCase("L5")) {
			statuses = Arrays.asList("L5", "R5");
		} else if (status.equalsIgnoreCase("L6")) {
			statuses = Arrays.asList("L6", "R6");
		} else if (status.equalsIgnoreCase("L7")) {
			statuses = Arrays.asList("L7", "R7");
		} else if (status.equalsIgnoreCase("L8")) {
			statuses = Arrays.asList("L8", "R8");
		} else if (status.equalsIgnoreCase("L9")) {
			statuses = Arrays.asList("L9", "R9");
		} else if (status.equalsIgnoreCase("A1")) {
			statuses = Arrays.asList("L10", "L5", "L6");
		}
		System.out.println("statuses:" + statuses);
		return statuses;
	}

	@Override
	public List<STP_CARDS_NFS_ISS_RECON_TTUM> addNFS_ISS_RECON_TTUM(List<STP_CARDS_NFS_ISS_RECON_TTUM> stpcardsList) {
		// TODO Auto-generated method stub
		return crdNfsIssReconTtumRepository.saveAll(stpcardsList);
	}

	@Override
	public List<STP_CARDS_NFS_ISS_RECON_TTUM> viewNFS_ISS_RECON_TTUM(TTumRequest accObject) {

		// Validate input dates
		if (accObject.getFromdate() == null || accObject.getTodate() == null) {
			logger.error("From date or To date is null");
			throw new IllegalArgumentException("From date or To date must not be null");
		}
		if (accObject.getQueryid() == null) {
			throw new IllegalArgumentException("Fetch Type must not be null");
		}
		String status = accObject.getStatus();
		TypeEnum data = TypeEnum.valueOf(status);
		Pageable pageable = PageRequest.of(0, Integer.parseInt(accObject.getPagination()));
		List<STP_CARDS_NFS_ISS_RECON_TTUM> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = crdNfsIssReconTtumRepository.findByValuedateBetweenAndStatusIn(
					accObject.getFromdate(), accObject.getTodate(), getStatusList(status), pageable);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (findByValuedateBetweenAndStatus.isEmpty()) {
			logger.error(data.getDescription() + " Detail Not Found");
			throw new DetailNotFoundException(data.getDescription() + " Detail Not Found");
		}
		return findByValuedateBetweenAndStatus;

	}

	@Override
	public List<STP_CARDS_NFS_ISS_RECON_TTUM> processNFS_ISS_RECON_TTUM(String status) {
		// TODO Auto-generated method stub
		return crdNfsIssReconTtumRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_CARDS_NFS_ISS_RECON_TTUM(ArrayList<IsoV93MessageRes> reslist, String type) {

		int upcount = 0;
		String status = null;
		String res039 = null;
		String res125 = null;
		String res126 = null;
		String ref = null;
		String approvestatus = "NA";
		ArrayList<String> notFailedTransaction = notFailedTransaction();
		ArrayList<String> creditTransaction2 = creditTransaction();
		ArrayList<String> debitTransaction2 = debitTransaction();
		ArrayList<String> repostTransaction3 = repostTransaction();
		for (IsoV93MessageRes res : reslist) {
			String cond = res.getRes126();
			String condition = cond.substring(0, 3);
			System.out.println("condition" + condition);
			if (!notFailedTransaction.contains(condition)) {
				status = "L10";
				approvestatus = "Failed";
			} else if (condition.equalsIgnoreCase("000")) {
				status = "L10";
				approvestatus = "Success";
			} else if (condition.equalsIgnoreCase("913")) {
				status = "L10";
				approvestatus = "Duplicate";
			} else if (condition.equalsIgnoreCase("NULL")) {
				// raise for enquiry
				status = "L5";
				approvestatus = "Timeout";
			} else if (condition.equalsIgnoreCase("911")) {
				// raise for enquiry
				status = "L5";
				approvestatus = "Timeout";
			} else if (condition.equalsIgnoreCase("114")) {
				// reposting after account number update
				status = "L7";
				approvestatus = "Invalid";
			} else if (creditTransaction2.contains(condition)) {
				// reposting after account number update
				status = "L6";
				approvestatus = "Credit_Exception";
			} else if (debitTransaction2.contains(condition)) {
				// reposting after account number update
				status = "L6";
				approvestatus = "Debit_Exception";
			} else if (repostTransaction3.contains(condition)) {
				status = "L6";
				approvestatus = "Enquiry_Exception";
			} else {
				status = "L11";
				approvestatus = "NA";
			}
			res039 = res.getRes039();
			res125 = res.getRes125();
			res126 = res.getRes126();
			ref = res.getRes037();
			if (type.contains("Fresh")) {
				upcount = upcount + crdNfsIssReconTtumRepository.updateSTP_CARDS_NFS_ISS_RECON_TTUM(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + crdNfsIssReconTtumRepository.updateRepeatSTP_CARDS_NFS_ISS_RECON_TTUM(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + crdNfsIssReconTtumRepository.updateEnquirySTP_CARDS_NFS_ISS_RECON_TTUM(status,
						res039, res125, res126, approvestatus, ref);
			}

			res039 = null;
			res125 = null;
			res126 = null;
			ref = null;
			approvestatus = null;
		}

		notFailedTransaction = null;
		creditTransaction2 = null;
		debitTransaction2 = null;
		repostTransaction3 = null;
		return upcount;

	}

	@Override
	public int updateSTP_CARDS_NFS_ISS_RECON_TTUM(IsoV93MessageRes res, String type) {
		int updatecount = 0;
		String approvestatus = "NA";
		String status = null;
		String res039 = null;
		String res125 = null;
		String res126 = null;
		String ref = null;
		String cond = null;
		String condition = null;
		try {
			cond = res.getRes126();
			condition = cond.substring(0, 3);
			String[] responseCode = IsoPackagerGlobal.responseCode(condition);
			status = responseCode[0];
			approvestatus = responseCode[1];
			res039 = res.getRes039();
			res125 = res.getRes125();
			res126 = res.getRes126();
			ref = res.getRes037();
			if (type.equalsIgnoreCase("FRESH")) {
				updatecount = crdNfsIssReconTtumRepository.updateSTP_CARDS_NFS_ISS_RECON_TTUM(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + crdNfsIssReconTtumRepository
						.updateRepeatSTP_CARDS_NFS_ISS_RECON_TTUM(status, res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = crdNfsIssReconTtumRepository.updateEnquirySTP_CARDS_NFS_ISS_RECON_TTUM(status, res039,
						res125, res126, approvestatus, ref);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			res039 = null;
			res125 = null;
			res126 = null;
			ref = null;
			cond = null;
			condition = null;
		}
		return updatecount;

	}

}
