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
import com.stp.dao.db1.ImpsRepository;
import com.stp.dao.db1.ImpsStpIMPSNonCbsIwTtumRepository;
import com.stp.dao.db1.ImpsStpImpsChargebackReportRepository;
import com.stp.dao.db1.ImpsStpImpsCustomerCompRepository;
import com.stp.dao.db1.ImpsStpImpsNtslNetsetTtumRepository;
import com.stp.dao.db1.ImpsStpImpsPreArbitrationReportRepository;
import com.stp.dao.db1.ImpsStpImpsRccReportRepository;
import com.stp.dao.db1.ImpsStpImpsRccReportRepraiseRepository;
import com.stp.dao.db1.ImpsStpIwNetworkDeclineRepository;
import com.stp.dao.db1.ImpsStpNonCbsOwTtumRepository;
import com.stp.dao.db1.ImpsStpNonNpciIwTtumRepository;
import com.stp.dao.db1.ImpsStpNonNpciOwTtumRepository;
import com.stp.dao.db1.ImpsStpOwNetworkDeclineRepository;
import com.stp.dao.db1.ImpsStpTccDataIwRettumRepository;
import com.stp.dao.db1.ImpsStpTccDataIwTtumRepository;
import com.stp.exception.DetailNotFoundException;
import com.stp.model.db1.STP_IMPS;
import com.stp.model.db1.STP_IMPS_CHARGEBACK_REPORT;
import com.stp.model.db1.STP_IMPS_CUSTOMER_COMP;
import com.stp.model.db1.STP_IMPS_IW_NETWORK_DECLINE;
import com.stp.model.db1.STP_IMPS_NONCBS_IW;
import com.stp.model.db1.STP_IMPS_NONCBS_OW;
import com.stp.model.db1.STP_IMPS_NONNPCI_IW;
import com.stp.model.db1.STP_IMPS_NONNPCI_OW;
import com.stp.model.db1.STP_IMPS_NTSL_NETSET_TTUM;
import com.stp.model.db1.STP_IMPS_OW_NETWORK_DECLINE;
import com.stp.model.db1.STP_IMPS_PREARBITRATION_REPORT;
import com.stp.model.db1.STP_IMPS_RCC_REPORT;
import com.stp.model.db1.STP_IMPS_RCC_REPORT_REPRAISE;
import com.stp.model.db1.STP_IMPS_TCC_DATA_IW;
import com.stp.model.db1.STP_IMPS_TCC_DATA_IW_RET;
import com.stp.service.ServiceImps;
import com.stp.utility.TTumRequest;
import com.stp.utility.TypeEnum;

@Service
public class ServiceImpsImpl implements ServiceImps {
	private static final Logger logger = LoggerFactory.getLogger(ServiceImpsImpl.class);

	@Autowired
	private ImpsRepository repository;

	@Autowired
	private ImpsStpIMPSNonCbsIwTtumRepository netsetTtumRepository;

	@Autowired
	private ImpsStpTccDataIwTtumRepository impsStpTccDataIwTtumRepository;

	@Autowired
	private ImpsStpNonNpciIwTtumRepository impsStpNonNpciIwTtumRepository;

	@Autowired
	private ImpsStpNonNpciOwTtumRepository impsStpNonNpciOwTtumRepository;

	@Autowired
	private ImpsStpIwNetworkDeclineRepository impsStpIwNetworkDeclineRepository;

	@Autowired
	private ImpsStpOwNetworkDeclineRepository impsStpOwNetworkDeclineRepository;

	@Autowired
	private ImpsStpImpsNtslNetsetTtumRepository impsNtslNetsetTtumRepository;

	@Autowired
	private ImpsStpNonCbsOwTtumRepository impsStpNonCbsOwTtumRepository;

	@Autowired
	private ImpsStpImpsChargebackReportRepository impsStpImpsChargebackReportRepository;

	@Autowired
	private ImpsStpImpsRccReportRepository impsStpImpsRccReportRepository;

	@Autowired
	private ImpsStpImpsPreArbitrationReportRepository impsStpImpsPreArbitrationReportRepository;

	@Autowired
	private ImpsStpImpsCustomerCompRepository impsStpImpsCustomerCompRepository;

	@Autowired
	private ImpsStpImpsRccReportRepraiseRepository impsStpImpsRccReportRepraiseRepository;

	@Autowired
	private ImpsStpTccDataIwRettumRepository impsStpTccDataIwRettumRepository;

	@Override
	public List<STP_IMPS> insertImpsRecord(List<STP_IMPS> stpImpsList) {
		// TODO Auto-generated method stub
		System.out.println("stpImpsList" + stpImpsList.size());
		return repository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_NONCBS_IW> addSTP_IMPS_NONCBS_IW(List<STP_IMPS_NONCBS_IW> stpUpiList) {
		// TODO Auto-generated method stub
		return netsetTtumRepository.saveAll(stpUpiList);
	}

	@Override
	public List<STP_IMPS_NONCBS_IW> viewSTP_IMPS_NONCBS_IW(TTumRequest accObject) {
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
		List<STP_IMPS_NONCBS_IW> findByValuedateBetweenAndStatus = null;
		Pageable pageable = PageRequest.of(0, Integer.parseInt(accObject.getPagination()));

		try {
			findByValuedateBetweenAndStatus = netsetTtumRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_NONCBS_IW> processIsoSTP_IMPS_NONCBS_IW(String status) {
		// TODO Auto-generated method stub
		return netsetTtumRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_IMPS_NONCBS_IW(ArrayList<IsoV93MessageRes> reslist, String type) {
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
			// System.out.println("condition" + condition);
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
				upcount = upcount + netsetTtumRepository.updateSTP_IMPS_NONCBS_IW(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + netsetTtumRepository.updateRepeatSTP_IMPS_NONCBS_IW(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + netsetTtumRepository.updateEnquirySTP_IMPS_NONCBS_IW(status, res039, res125, res126,
						approvestatus, ref);
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
	public List<STP_IMPS_TCC_DATA_IW> addSTP_IMPS_TCC_DATA_IW(List<STP_IMPS_TCC_DATA_IW> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpTccDataIwTtumRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_TCC_DATA_IW> viewSTP_IMPS_TCC_DATA_IW(TTumRequest accObject) {
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
		List<STP_IMPS_TCC_DATA_IW> findByValuedateBetweenAndStatus = null;
		Pageable pageable = PageRequest.of(0, Integer.parseInt(accObject.getPagination()));
		try {
			findByValuedateBetweenAndStatus = impsStpTccDataIwTtumRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_TCC_DATA_IW> processSTP_IMPS_TCC_DATA_IW(String status) {
		// TODO Auto-generated method stub
		return impsStpTccDataIwTtumRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_IMPS_TCC_DATA_IW(ArrayList<IsoV93MessageRes> reslist, String type) {
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
			// System.out.println("condition" + condition);
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
			} else if (creditTransaction2.contains(condition)) {
				// reposting
				status = "L6";
				approvestatus = "Credit_Exception";
			} else if (debitTransaction2.contains(condition)) {
				// reposting
				status = "L6";
				approvestatus = "Debit_Exception";
			} else if (repostTransaction3.contains(condition)) {
				status = "L6";
				approvestatus = "Enquiry_Exception";
			} else if (condition.equalsIgnoreCase("114")) {
				// reposting after account number update
				status = "L7";
				approvestatus = "Invalid";
			} else {
				status = "L11";
				approvestatus = "NA";
			}
			res039 = res.getRes039();
			res125 = res.getRes125();
			res126 = res.getRes126();
			ref = res.getRes037();
			if (type.contains("Fresh")) {
				upcount = upcount + impsStpTccDataIwTtumRepository.updateSTP_IMPS_TCC_DATA_IW(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + impsStpTccDataIwTtumRepository.updateRepeatSTP_IMPS_TCC_DATA_IW(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + impsStpTccDataIwTtumRepository.updateEnquirySTP_IMPS_TCC_DATA_IW(status, res039,
						res125, res126, approvestatus, ref);
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

//STP_IMPS_NONNPCI_IW
	@Override
	public List<STP_IMPS_NONNPCI_IW> addSTP_IMPS_NONNPCI_IW(List<STP_IMPS_NONNPCI_IW> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpNonNpciIwTtumRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_NONNPCI_IW> viewSTP_IMPS_NONNPCI_IW(TTumRequest accObject) {
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
		List<STP_IMPS_NONNPCI_IW> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsStpNonNpciIwTtumRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_NONNPCI_IW> processSTP_IMPS_NONNPCI_IW(String status) {
		// TODO Auto-generated method stub
		return impsStpNonNpciIwTtumRepository.findByStatus(status);

	}

	@Override
	public int ReqRespSTP_IMPS_NONNPCI_IW(ArrayList<IsoV93MessageRes> reslist, String type) {
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
			// System.out.println("condition" + condition);
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
				upcount = upcount + impsStpNonNpciIwTtumRepository.updateSTP_IMPS_NONNPCI_IW(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + impsStpNonNpciIwTtumRepository.updateRepeatSTP_IMPS_NONNPCI_IW(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + impsStpNonNpciIwTtumRepository.updateEnquirySTP_IMPS_NONNPCI_IW(status, res039,
						res125, res126, approvestatus, ref);
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
	public List<STP_IMPS_NONNPCI_OW> addSTP_IMPS_NONNPCI_OW(List<STP_IMPS_NONNPCI_OW> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpNonNpciOwTtumRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_NONNPCI_OW> viewSTP_IMPS_NONNPCI_OW(TTumRequest accObject) {
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
		List<STP_IMPS_NONNPCI_OW> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsStpNonNpciOwTtumRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_NONNPCI_OW> processSTP_IMPS_NONNPCI_OW(String status) {
		// TODO Auto-generated method stub
		return impsStpNonNpciOwTtumRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_IMPS_NONNPCI_OW(ArrayList<IsoV93MessageRes> reslist, String type) {

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
			// System.out.println("condition" + condition);
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
				upcount = upcount + impsStpNonNpciOwTtumRepository.updateSTP_IMPS_NONNPCI_OW(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + impsStpNonNpciOwTtumRepository.updateRepeatSTP_IMPS_NONNPCI_OW(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + impsStpNonNpciOwTtumRepository.updateEnquirySTP_IMPS_NONNPCI_OW(status, res039,
						res125, res126, approvestatus, ref);
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
	public List<STP_IMPS_IW_NETWORK_DECLINE> addSTP_IMPS_IW_NETWORK_DECLINE(
			List<STP_IMPS_IW_NETWORK_DECLINE> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpIwNetworkDeclineRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_IW_NETWORK_DECLINE> viewSTP_IMPS_IW_NETWORK_DECLINE(TTumRequest accObject) {

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
		List<STP_IMPS_IW_NETWORK_DECLINE> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsStpIwNetworkDeclineRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_IW_NETWORK_DECLINE> processSTP_IMPS_IW_NETWORK_DECLINE(String status) {
		// TODO Auto-generated method stub
		return impsStpIwNetworkDeclineRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_IMPS_IW_NETWORK_DECLINE(ArrayList<IsoV93MessageRes> reslist, String type) {

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
			// System.out.println("condition" + condition);
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
				upcount = upcount + impsStpIwNetworkDeclineRepository.updateSTP_IMPS_IW_NETWORK_DECLINE(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + impsStpIwNetworkDeclineRepository.updateRepeatSTP_IMPS_IW_NETWORK_DECLINE(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + impsStpIwNetworkDeclineRepository.updateEnquirySTP_IMPS_IW_NETWORK_DECLINE(status,
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

//STP_IMPS_OW_NETWORK_DECLINE
	@Override
	public List<STP_IMPS_OW_NETWORK_DECLINE> addSTP_IMPS_OW_NETWORK_DECLINE(
			List<STP_IMPS_OW_NETWORK_DECLINE> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpOwNetworkDeclineRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_OW_NETWORK_DECLINE> viewSTP_IMPS_OW_NETWORK_DECLINE(TTumRequest accObject) {
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
		List<STP_IMPS_OW_NETWORK_DECLINE> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsStpOwNetworkDeclineRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_OW_NETWORK_DECLINE> processSTP_IMPS_OW_NETWORK_DECLINE(String status) {
		// TODO Auto-generated method stub
		return impsStpOwNetworkDeclineRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_IMPS_OW_NETWORK_DECLINE(ArrayList<IsoV93MessageRes> reslist, String type) {

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
			// System.out.println("condition" + condition);
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
				upcount = upcount + impsStpOwNetworkDeclineRepository.updateSTP_IMPS_OW_NETWORK_DECLINE(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + impsStpOwNetworkDeclineRepository.updateRepeatSTP_IMPS_OW_NETWORK_DECLINE(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + impsStpOwNetworkDeclineRepository.updateEnquirySTP_IMPS_OW_NETWORK_DECLINE(status,
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

//STP_IMPS_NTSL_NETSET_TTUM
	@Override
	public List<STP_IMPS_NTSL_NETSET_TTUM> addSTP_IMPS_NTSL_NETSET_TTUM(List<STP_IMPS_NTSL_NETSET_TTUM> stpImpsList) {
		// TODO Auto-generated method stub
		return impsNtslNetsetTtumRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_NTSL_NETSET_TTUM> viewSTP_IMPS_NTSL_NETSET_TTUM(TTumRequest accObject) {

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
		List<STP_IMPS_NTSL_NETSET_TTUM> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsNtslNetsetTtumRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_NTSL_NETSET_TTUM> processSTP_IMPS_NTSL_NETSET_TTUM(String status) {
		// TODO Auto-generated method stub
		return impsNtslNetsetTtumRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_IMPS_NTSL_NETSET_TTUM(ArrayList<IsoV93MessageRes> reslist, String type) {
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
			// System.out.println("condition" + condition);
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
				upcount = upcount + impsNtslNetsetTtumRepository.updateSTP_IMPS_NTSL_NETSET_TTUM(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + impsNtslNetsetTtumRepository.updateRepeatSTP_IMPS_NTSL_NETSET_TTUM(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + impsNtslNetsetTtumRepository.updateEnquirySTP_IMPS_NTSL_NETSET_TTUM(status, res039,
						res125, res126, approvestatus, ref);
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
	public List<STP_IMPS_NONCBS_OW> addSTP_IMPS_NONCBS_OW(List<STP_IMPS_NONCBS_OW> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpNonCbsOwTtumRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_NONCBS_OW> viewSTP_IMPS_NONCBS_OW(TTumRequest accObject) {

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
		List<STP_IMPS_NONCBS_OW> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsStpNonCbsOwTtumRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_NONCBS_OW> processSTP_IMPS_NONCBS_OW(String status) {
		// TODO Auto-generated method stub
		return impsStpNonCbsOwTtumRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_IMPS_NONCBS_OW(ArrayList<IsoV93MessageRes> reslist, String type) {

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
			// System.out.println("condition" + condition);
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
				upcount = upcount + impsStpNonCbsOwTtumRepository.updateEnquirySTP_IMPS_NONCBS_OW(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + impsStpNonCbsOwTtumRepository.updateRepeatSTP_IMPS_NONCBS_OW(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + impsStpNonCbsOwTtumRepository.updateEnquirySTP_IMPS_NONCBS_OW(status, res039,
						res125, res126, approvestatus, ref);
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
	public int updateSTP_IMPS_TCC_DATA_IW(IsoV93MessageRes res, String type) {
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
				updatecount = impsStpTccDataIwTtumRepository.updateSTP_IMPS_TCC_DATA_IW(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpTccDataIwTtumRepository.updateRepeatSTP_IMPS_TCC_DATA_IW(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpTccDataIwTtumRepository.updateEnquirySTP_IMPS_TCC_DATA_IW(status, res039, res125,
						res126, approvestatus, ref);
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

	@Override
	public int updateSTP_IMPS_NONCBS_IW(IsoV93MessageRes res, String type) {
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
				updatecount = netsetTtumRepository.updateSTP_IMPS_NONCBS_IW(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + netsetTtumRepository.updateRepeatSTP_IMPS_NONCBS_IW(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = netsetTtumRepository.updateEnquirySTP_IMPS_NONCBS_IW(status, res039, res125, res126,
						approvestatus, ref);
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

	@Override
	public int updateSTP_IMPS_NONNPCI_IW(IsoV93MessageRes res, String type) {

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
				updatecount = impsStpNonNpciIwTtumRepository.updateSTP_IMPS_NONNPCI_IW(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpNonNpciIwTtumRepository.updateRepeatSTP_IMPS_NONNPCI_IW(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpNonNpciIwTtumRepository.updateEnquirySTP_IMPS_NONNPCI_IW(status, res039, res125,
						res126, approvestatus, ref);
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

	@Override
	public int updateSTP_IMPS_NONNPCI_OW(IsoV93MessageRes res, String type) {

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
				updatecount = impsStpNonNpciOwTtumRepository.updateSTP_IMPS_NONNPCI_OW(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpNonNpciOwTtumRepository.updateRepeatSTP_IMPS_NONNPCI_OW(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpNonNpciOwTtumRepository.updateEnquirySTP_IMPS_NONNPCI_OW(status, res039, res125,
						res126, approvestatus, ref);
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

	@Override
	public int updateSTP_IMPS_IW_NETWORK_DECLINE(IsoV93MessageRes res, String type) {

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
				updatecount = impsStpIwNetworkDeclineRepository.updateSTP_IMPS_IW_NETWORK_DECLINE(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpIwNetworkDeclineRepository
						.updateRepeatSTP_IMPS_IW_NETWORK_DECLINE(status, res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpIwNetworkDeclineRepository.updateEnquirySTP_IMPS_IW_NETWORK_DECLINE(status, res039,
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

	@Override
	public int updateSTP_IMPS_OW_NETWORK_DECLINE(IsoV93MessageRes res, String type) {

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
				updatecount = impsStpOwNetworkDeclineRepository.updateSTP_IMPS_OW_NETWORK_DECLINE(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpOwNetworkDeclineRepository
						.updateRepeatSTP_IMPS_OW_NETWORK_DECLINE(status, res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpOwNetworkDeclineRepository.updateEnquirySTP_IMPS_OW_NETWORK_DECLINE(status, res039,
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

	@Override
	public int updateSTP_IMPS_NONCBS_OW(IsoV93MessageRes res, String type) {

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
				updatecount = impsStpNonCbsOwTtumRepository.updateSTP_IMPS_NONCBS_OW(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpNonCbsOwTtumRepository.updateRepeatSTP_IMPS_NONCBS_OW(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpNonCbsOwTtumRepository.updateEnquirySTP_IMPS_NONCBS_OW(status, res039, res125,
						res126, approvestatus, ref);
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

	@Override
	public int updateSTP_IMPS_NTSL_NETSET_TTUM(IsoV93MessageRes res, String type) {

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
				updatecount = impsNtslNetsetTtumRepository.updateSTP_IMPS_NTSL_NETSET_TTUM(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsNtslNetsetTtumRepository.updateRepeatSTP_IMPS_NTSL_NETSET_TTUM(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsNtslNetsetTtumRepository.updateEnquirySTP_IMPS_NTSL_NETSET_TTUM(status, res039,
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

	@Override
	public List<STP_IMPS_CHARGEBACK_REPORT> addSTP_IMPS_CHARGEBACK_REPORT(
			List<STP_IMPS_CHARGEBACK_REPORT> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpImpsChargebackReportRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_CHARGEBACK_REPORT> viewSTP_IMPS_CHARGEBACK_REPORT(TTumRequest accObject) {

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
		List<STP_IMPS_CHARGEBACK_REPORT> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsStpImpsChargebackReportRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_CHARGEBACK_REPORT> processSTP_IMPS_CHARGEBACK_REPORT(String status) {
		// TODO Auto-generated method stub
		return impsStpImpsChargebackReportRepository.findByStatus(status);
	}

	@Override
	public int updateSTP_IMPS_CHARGEBACK_REPORT(IsoV93MessageRes res, String type) {
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
				updatecount = impsStpImpsChargebackReportRepository.updateSTP_IMPS_CHARGEBACK_REPORT(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpImpsChargebackReportRepository
						.updateRepeatSTP_IMPS_CHARGEBACK_REPORT(status, res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpImpsChargebackReportRepository.updateEnquirySTP_IMPS_NONCBS_IW(status, res039,
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

	@Override
	public List<STP_IMPS_RCC_REPORT> addSTP_IMPS_RCC_REPORT(List<STP_IMPS_RCC_REPORT> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpImpsRccReportRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_RCC_REPORT> viewSTP_IMPS_RCC_REPORT(TTumRequest accObject) {
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
		List<STP_IMPS_RCC_REPORT> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsStpImpsRccReportRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_RCC_REPORT> processSTP_IMPS_RCC_REPORT(String status) {
		// TODO Auto-generated method stub
		return impsStpImpsRccReportRepository.findByStatus(status);
	}

	@Override
	public int updateSTP_IMPS_RCC_REPORT(IsoV93MessageRes res, String type) {

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
				updatecount = impsStpImpsRccReportRepository.updateSTP_IMPS_RCC_REPORT(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpImpsRccReportRepository.updateRepeatSTP_IMPS_RCC_REPORT(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpImpsRccReportRepository.updateEnquirySTP_IMPS_RCC_REPORT(status, res039, res125,
						res126, approvestatus, ref);
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

	@Override
	public List<STP_IMPS_PREARBITRATION_REPORT> addSTP_IMPS_PREARBITRATION_REPORT(
			List<STP_IMPS_PREARBITRATION_REPORT> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpImpsPreArbitrationReportRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_PREARBITRATION_REPORT> viewSTP_IMPS_PREARBITRATION_REPORT(TTumRequest accObject) {

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
		List<STP_IMPS_PREARBITRATION_REPORT> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsStpImpsPreArbitrationReportRepository
					.findByValuedateBetweenAndStatusIn(accObject.getFromdate(), accObject.getTodate(),
							getStatusList(status), pageable);
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
	public List<STP_IMPS_PREARBITRATION_REPORT> processSTP_IMPS_PREARBITRATION_REPORT(String status) {
		// TODO Auto-generated method stub
		return impsStpImpsPreArbitrationReportRepository.findByStatus(status);
	}

	@Override
	public int updateSTP_IMPS_PREARBITRATION_REPORT(IsoV93MessageRes res, String type) {

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
				updatecount = impsStpImpsPreArbitrationReportRepository.updateSTP_IMPS_PREARBITRATION_REPORT(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpImpsPreArbitrationReportRepository
						.updateRepeatSTP_IMPS_PREARBITRATION_REPORT(status, res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpImpsPreArbitrationReportRepository.updateEnquirySTP_IMPS_PREARBITRATION_REPORT(
						status, res039, res125, res126, approvestatus, ref);
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

	@Override
	public List<STP_IMPS_CUSTOMER_COMP> addSTP_IMPS_CUSTOMER_COMP(List<STP_IMPS_CUSTOMER_COMP> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpImpsCustomerCompRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_CUSTOMER_COMP> viewSTP_IMPS_CUSTOMER_COMP(TTumRequest accObject) {

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
		List<STP_IMPS_CUSTOMER_COMP> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsStpImpsCustomerCompRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_CUSTOMER_COMP> processSTP_IMPS_CUSTOMER_COMP(String status) {
		// TODO Auto-generated method stub
		return impsStpImpsCustomerCompRepository.findByStatus(status);
	}

	@Override
	public int updateSTP_IMPS_CUSTOMER_COMP(IsoV93MessageRes res, String type) {

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
				updatecount = impsStpImpsCustomerCompRepository.updateSTP_IMPS_CUSTOMER_COMP(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpImpsCustomerCompRepository.updateRepeatSTP_IMPS_CUSTOMER_COMP(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpImpsCustomerCompRepository.updateEnquirySTP_IMPS_CUSTOMER_COMP(status, res039,
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

	@Override
	public List<STP_IMPS_RCC_REPORT_REPRAISE> addSTP_IMPS_RCC_REPORT_REPRAISE(
			List<STP_IMPS_RCC_REPORT_REPRAISE> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpImpsRccReportRepraiseRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_RCC_REPORT_REPRAISE> viewSTP_IMPS_RCC_REPORT_REPRAISE(TTumRequest accObject) {

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
		List<STP_IMPS_RCC_REPORT_REPRAISE> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsStpImpsRccReportRepraiseRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_RCC_REPORT_REPRAISE> processSTP_IMPS_RCC_REPORT_REPRAISE(String status) {
		// TODO Auto-generated method stub
		return impsStpImpsRccReportRepraiseRepository.findByStatus(status);
	}

	@Override
	public int updateSTP_IMPS_RCC_REPORT_REPRAISE(IsoV93MessageRes res, String type) {

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
				updatecount = impsStpImpsRccReportRepraiseRepository.updateSTP_IMPS_RCC_REPORT_REPRAISE(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpImpsRccReportRepraiseRepository
						.updateRepeatSTP_IMPS_RCC_REPORT_REPRAISE(status, res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpImpsRccReportRepraiseRepository.updateEnquirySTP_IMPS_RCC_REPORT_REPRAISE(status,
						res039, res125, res126, approvestatus, ref);
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

	@Override
	public List<STP_IMPS_TCC_DATA_IW_RET> addSTP_IMPS_TCC_DATA_IW_RET(List<STP_IMPS_TCC_DATA_IW_RET> stpImpsList) {
		// TODO Auto-generated method stub
		return impsStpTccDataIwRettumRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_IMPS_TCC_DATA_IW_RET> viewSTP_IMPS_TCC_DATA_IW_RET(TTumRequest accObject) {

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
		List<STP_IMPS_TCC_DATA_IW_RET> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = impsStpTccDataIwRettumRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_IMPS_TCC_DATA_IW_RET> processSTP_IMPS_TCC_DATA_IW_RET(String status) {
		// TODO Auto-generated method stub
		return impsStpTccDataIwRettumRepository.findByStatus(status);
	}

	@Override
	public int updateSTP_IMPS_TCC_DATA_IW_RET(IsoV93MessageRes res, String type) {

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
				updatecount = impsStpTccDataIwRettumRepository.updateSTP_IMPS_TCC_DATA_IW_RET(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + impsStpTccDataIwRettumRepository
						.updateRepeatSTP_IMPS_TCC_DATA_IW_RET(status, res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = impsStpTccDataIwRettumRepository.updateEnquirySTP_IMPS_TCC_DATA_IW_RET(status, res039,
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
