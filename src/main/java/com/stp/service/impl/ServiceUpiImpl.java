package com.stp.service.impl;

import static com.stp.utility.GenericCLass.convertStringToDate;
import static com.stp.utility.GenericCLass.creditTransaction;
import static com.stp.utility.GenericCLass.debitTransaction;
import static com.stp.utility.GenericCLass.notFailedTransaction;
import static com.stp.utility.GenericCLass.repostTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.iso.config.IsoV93MessageRes;
import com.iso.opt.IsoPackagerGlobal;
import com.stp.dao.db1.UpiNtslNetsetTtumRepository;
import com.stp.dao.db1.UpiRepository;
import com.stp.dao.db1.UpiStpAdjustmentReportRepository;
import com.stp.dao.db1.UpiStpUpiDrcNpciRepository;
import com.stp.dao.db1.UpiStpUpiMultiReversalRepository;
import com.stp.dao.db1.UpiStpUpiNonCbsIwRepository;
import com.stp.dao.db1.UpiStpUpiNonCbsOwRepository;
import com.stp.dao.db1.UpiStpUpiRetDataRepository;
import com.stp.dao.db1.UpiStpUpiTccDataRepository;
import com.stp.exception.DetailNotFoundException;
import com.stp.model.db1.STP_UPI;
import com.stp.model.db1.STP_UPI_ADJUSTMENT_REPORT;
import com.stp.model.db1.STP_UPI_DRC_NPCI;
import com.stp.model.db1.STP_UPI_MULTIREVERSAL;
import com.stp.model.db1.STP_UPI_NONCBS_IW;
import com.stp.model.db1.STP_UPI_NONCBS_OW;
import com.stp.model.db1.STP_UPI_NSTL_NETSET_TTUM;
import com.stp.model.db1.STP_UPI_RET_DATA;
import com.stp.model.db1.STP_UPI_TCC_DATA;
import com.stp.service.ServiceUpi;
import com.stp.utility.TTumRequest;
import com.stp.utility.TypeEnum;

@Service
public class ServiceUpiImpl implements ServiceUpi {
	private static final Logger logger = LoggerFactory.getLogger(ServiceUpiImpl.class);

	@Autowired
	private UpiRepository repository;

	@Autowired
	private UpiNtslNetsetTtumRepository netsetTtumRepository;

	@Autowired
	private UpiStpAdjustmentReportRepository upiStpAdjustmentReportRepository;
	@Autowired
	private UpiStpUpiMultiReversalRepository upiMultiReversalRepository;

	@Autowired
	private UpiStpUpiTccDataRepository upiStpUpiTccDataRepository;

	@Autowired
	private UpiStpUpiRetDataRepository upiStpRetDataRepository;

	@Autowired
	private UpiStpUpiDrcNpciRepository upiDrcNpciRepository;

	@Autowired
	private UpiStpUpiNonCbsIwRepository upiStpUpiNonCbsIwRepository;

	@Autowired
	private UpiStpUpiNonCbsOwRepository upiStpUpiNonCbsOwRepository;

//	STP_UPI_RET_DATA
	@Override
	public List<STP_UPI> insertUpiRecord(List<STP_UPI> stpUpiList) {
		// TODO Auto-generated method stub
		return repository.saveAll(stpUpiList);
	}

	@Override
	public List<STP_UPI> globalUPIDetail(TTumRequest accObject) {
		// Validate input dates
		if (accObject.getFromdate() == null || accObject.getTodate() == null) {
			logger.error("From date or To date is null");
			throw new IllegalArgumentException("From date or To date must not be null");
		}
		if (accObject.getQueryid() == null) {
			throw new IllegalArgumentException("Fetch Type must not be null");
		}
		Date str = convertStringToDate(accObject.getFromdate());
		Date end = convertStringToDate(accObject.getTodate());
		// Log the date range for debugging purposes
		logger.info("Searching for records with from date: {} and to date: {}", str, end);
		// The status to filter by
		String status = accObject.getStatus();
		Integer queryid = accObject.getQueryid();

		TypeEnum data = TypeEnum.valueOf(status);
		List<STP_UPI> findByValuedateBetweenAndStatus = repository.findByValuedateBetweenAndStatusIn(str, end,
				getStatusList(status));
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
			statuses = Arrays.asList("L10", "L5", "L6", "L7");
		}
		System.out.println("statuses:" + statuses);
		return statuses;
	}

	@Override
	public List<STP_UPI_NSTL_NETSET_TTUM> addSTP_UPI_NSTL_NETSET_TTUM(List<STP_UPI_NSTL_NETSET_TTUM> stpUpiList) {
		// TODO Auto-generated method stub
		return netsetTtumRepository.saveAll(stpUpiList);
	}

	@Override
	public List<STP_UPI_NSTL_NETSET_TTUM> viewSTP_UPI_NSTL_NETSET_TTUM(TTumRequest accObject) {
		List<STP_UPI_NSTL_NETSET_TTUM> findByValuedateBetweenAndStatus = null;
		try {
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return findByValuedateBetweenAndStatus;

	}

	@Override
	public List<STP_UPI_NSTL_NETSET_TTUM> processSTP_UPI_NSTL_NETSET_TTUM(String status) {
		// TODO Auto-generated method stub
		return netsetTtumRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_UPI_NSTL_NETSET_TTUM(ArrayList<IsoV93MessageRes> reslist, String type) {

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
				upcount = upcount + netsetTtumRepository.updateSTP_UPI_NSTL_NETSET_TTUM(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + netsetTtumRepository.updateRepeatSTP_UPI_NSTL_NETSET_TTUM(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + netsetTtumRepository.updateEnquirySTP_UPI_NSTL_NETSET_TTUM(status, res039, res125,
						res126, approvestatus, ref);
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

	// STP_UPI_ADJUSTMENT_REPORT
	@Override
	public List<STP_UPI_ADJUSTMENT_REPORT> addSTP_UPI_ADJUSTMENT_REPORT(List<STP_UPI_ADJUSTMENT_REPORT> stpUpiList) {
		// TODO Auto-generated method stub
		return upiStpAdjustmentReportRepository.saveAll(stpUpiList);
	}

	@Override
	public List<STP_UPI_ADJUSTMENT_REPORT> viewSTP_UPI_ADJUSTMENT_REPORT(TTumRequest accObject) {

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
		List<STP_UPI_ADJUSTMENT_REPORT> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = upiStpAdjustmentReportRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_UPI_ADJUSTMENT_REPORT> processSTP_UPI_ADJUSTMENT_REPORT(String status) {
		// TODO Auto-generated method stub
		return upiStpAdjustmentReportRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_UPI_ADJUSTMENT_REPORT(ArrayList<IsoV93MessageRes> reslist, String type) {

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
				upcount = upcount + upiStpAdjustmentReportRepository.updateEnquirySTP_UPI_ADJUSTMENT_REPORT(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + upiStpAdjustmentReportRepository.updateRepeatSTP_UPI_ADJUSTMENT_REPORT(status,
						res039, res125, res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + upiStpAdjustmentReportRepository.updateEnquirySTP_UPI_ADJUSTMENT_REPORT(status,
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
	public List<STP_UPI_MULTIREVERSAL> addSTP_UPI_MULTIREVERSAL(List<STP_UPI_MULTIREVERSAL> stpImpsList) {
		// TODO Auto-generated method stub
		return upiMultiReversalRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_UPI_MULTIREVERSAL> viewSTP_UPI_MULTIREVERSAL(TTumRequest accObject) {

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
		List<STP_UPI_MULTIREVERSAL> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = upiMultiReversalRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_UPI_MULTIREVERSAL> processSTP_UPI_MULTIREVERSAL(String status) {
		// TODO Auto-generated method stub
		return upiMultiReversalRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_UPI_MULTIREVERSAL(ArrayList<IsoV93MessageRes> reslist, String type) {
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
				upcount = upcount + upiMultiReversalRepository.updateSTP_UPI_MULTIREVERSAL(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + upiMultiReversalRepository.updateRepeatSTP_UPI_MULTIREVERSAL(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + upiMultiReversalRepository.updateEnquirySTP_UPI_MULTIREVERSAL(status, res039,
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
	public List<STP_UPI_TCC_DATA> addSTP_UPI_TCC_DATA(List<STP_UPI_TCC_DATA> stpImpsList) {
		// TODO Auto-generated method stub
		return upiStpUpiTccDataRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_UPI_TCC_DATA> viewSTP_UPI_TCC_DATA(TTumRequest accObject) {
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
		List<STP_UPI_TCC_DATA> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = upiStpUpiTccDataRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_UPI_TCC_DATA> processSTP_UPI_TCC_DATA(String status) {
		// TODO Auto-generated method stub
		return upiStpUpiTccDataRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_UPI_TCC_DATA(ArrayList<IsoV93MessageRes> reslist, String type) {

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
				upcount = upcount + upiStpUpiTccDataRepository.updateSTP_UPI_TCC_DATA(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + upiStpUpiTccDataRepository.updateRepeatSTP_UPI_TCC_DATA(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + upiStpUpiTccDataRepository.updateEnquirySTP_UPI_TCC_DATA(status, res039, res125,
						res126, approvestatus, ref);
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
	public List<STP_UPI_RET_DATA> addSTP_UPI_RET_DATA(List<STP_UPI_RET_DATA> stpImpsList) {
		// TODO Auto-generated method stub
		return upiStpRetDataRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_UPI_RET_DATA> viewSTP_UPI_RET_DATA(TTumRequest accObject) {

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
		List<STP_UPI_RET_DATA> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = upiStpRetDataRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_UPI_RET_DATA> processSTP_UPI_RET_DATA(String status) {
		// TODO Auto-generated method stub
		return upiStpRetDataRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_UPI_RET_DATA(ArrayList<IsoV93MessageRes> reslist, String type) {

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
				upcount = upcount + upiStpRetDataRepository.updateSTP_UPI_RET_DATA(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + upiStpRetDataRepository.updateRepeatSTP_UPI_RET_DATA(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + upiStpRetDataRepository.updateEnquirySTP_UPI_RET_DATA(status, res039, res125,
						res126, approvestatus, ref);
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
	public List<STP_UPI_DRC_NPCI> addSTP_UPI_DRC_NPCI(List<STP_UPI_DRC_NPCI> stpImpsList) {
		// TODO Auto-generated method stub
		return upiDrcNpciRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_UPI_DRC_NPCI> viewSTP_UPI_DRC_NPCI(TTumRequest accObject) {

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
		List<STP_UPI_DRC_NPCI> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = upiDrcNpciRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_UPI_DRC_NPCI> processSTP_UPI_DRC_NPCI(String status) {
		// TODO Auto-generated method stub
		return upiDrcNpciRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_UPI_DRC_NPCI(ArrayList<IsoV93MessageRes> reslist, String type) {
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
				upcount = upcount + upiDrcNpciRepository.updateSTP_UPI_DRC_NPCI(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + upiDrcNpciRepository.updateRepeatSTP_UPI_DRC_NPCI(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + upiDrcNpciRepository.updateEnquirySTP_UPI_DRC_NPCI(status, res039, res125, res126,
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
	public List<STP_UPI_NONCBS_IW> addSTP_UPI_NONCBS_IW(List<STP_UPI_NONCBS_IW> stpImpsList) {
		// TODO Auto-generated method stub
		return upiStpUpiNonCbsIwRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_UPI_NONCBS_IW> viewSTP_UPI_NONCBS_IW(TTumRequest accObject) {

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
		List<STP_UPI_NONCBS_IW> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = upiStpUpiNonCbsIwRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_UPI_NONCBS_IW> processSTP_UPI_NONCBS_IW(String status) {
		// TODO Auto-generated method stub
		return upiStpUpiNonCbsIwRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_UPI_NONCBS_IW(ArrayList<IsoV93MessageRes> reslist, String type) {

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
				upcount = upcount + upiStpUpiNonCbsIwRepository.updateSTP_UPI_NONCBS_IW(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + upiStpUpiNonCbsIwRepository.updateRepeatSTP_UPI_NONCBS_IW(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + upiStpUpiNonCbsIwRepository.updateEnquirySTP_UPI_NONCBS_IW(status, res039, res125,
						res126, approvestatus, ref);
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
	public List<STP_UPI_NONCBS_OW> addSTP_UPI_NONCBS_OW(List<STP_UPI_NONCBS_OW> stpImpsList) {
		// TODO Auto-generated method stub
		return upiStpUpiNonCbsOwRepository.saveAll(stpImpsList);
	}

	@Override
	public List<STP_UPI_NONCBS_OW> viewSTP_UPI_NONCBS_OW(TTumRequest accObject) {

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
		List<STP_UPI_NONCBS_OW> findByValuedateBetweenAndStatus = null;
		try {
			findByValuedateBetweenAndStatus = upiStpUpiNonCbsOwRepository.findByValuedateBetweenAndStatusIn(
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
	public List<STP_UPI_NONCBS_OW> processSTP_UPI_NONCBS_OW(String status) {
		// TODO Auto-generated method stub
		return upiStpUpiNonCbsOwRepository.findByStatus(status);
	}

	@Override
	public int ReqRespSTP_UPI_NONCBS_OW(ArrayList<IsoV93MessageRes> reslist, String type) {

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
				upcount = upcount + upiStpUpiNonCbsOwRepository.updateSTP_UPI_NONCBS_OW(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.contains("Repeat")) {
				upcount = upcount + upiStpUpiNonCbsOwRepository.updateRepeatSTP_UPI_NONCBS_OW(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.contains("Enquiry")) {
				upcount = upcount + upiStpUpiNonCbsOwRepository.updateEnquirySTP_UPI_NONCBS_OW(status, res039, res125,
						res126, approvestatus, ref);
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
	public int updateSTP_UPI_NSTL_NETSET_TTUM(IsoV93MessageRes res, String type) {
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
				updatecount = netsetTtumRepository.updateSTP_UPI_NSTL_NETSET_TTUM(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + netsetTtumRepository.updateRepeatSTP_UPI_NSTL_NETSET_TTUM(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = netsetTtumRepository.updateEnquirySTP_UPI_NSTL_NETSET_TTUM(status, res039, res125, res126,
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
	public int updateSTP_UPI_ADJUSTMENT_REPORT(IsoV93MessageRes res, String type) {

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
				updatecount = upiStpAdjustmentReportRepository.updateSTP_UPI_ADJUSTMENT_REPORT(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + upiStpAdjustmentReportRepository
						.updateRepeatSTP_UPI_ADJUSTMENT_REPORT(status, res039, res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = upiStpAdjustmentReportRepository.updateEnquirySTP_UPI_ADJUSTMENT_REPORT(status, res039,
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
	public int updateSTP_UPI_MULTIREVERSAL(IsoV93MessageRes res, String type) {

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
				updatecount = upiMultiReversalRepository.updateSTP_UPI_MULTIREVERSAL(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + upiMultiReversalRepository.updateRepeatSTP_UPI_MULTIREVERSAL(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = upiMultiReversalRepository.updateEnquirySTP_UPI_MULTIREVERSAL(status, res039, res125,
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
	public int updateSTP_UPI_TCC_DATA(IsoV93MessageRes res, String type) {

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
				updatecount = upiStpUpiTccDataRepository.updateSTP_UPI_TCC_DATA(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + upiStpUpiTccDataRepository.updateRepeatSTP_UPI_TCC_DATA(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = upiStpUpiTccDataRepository.updateEnquirySTP_UPI_TCC_DATA(status, res039, res125, res126,
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
	public int updateSTP_UPI_RET_DATA(IsoV93MessageRes res, String type) {

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
				updatecount = upiStpRetDataRepository.updateSTP_UPI_RET_DATA(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + upiStpRetDataRepository.updateRepeatSTP_UPI_RET_DATA(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = upiStpRetDataRepository.updateEnquirySTP_UPI_RET_DATA(status, res039, res125, res126,
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
	public int updateSTP_UPI_DRC_NPCI(IsoV93MessageRes res, String type) {
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
				updatecount = upiStpRetDataRepository.updateSTP_UPI_RET_DATA(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + upiStpRetDataRepository.updateRepeatSTP_UPI_RET_DATA(status, res039, res125,
						res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = upiStpRetDataRepository.updateEnquirySTP_UPI_RET_DATA(status, res039, res125, res126,
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
	public int updateSTP_UPI_NONCBS_IW(IsoV93MessageRes res, String type) {
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
				updatecount = upiStpUpiNonCbsIwRepository.updateSTP_UPI_NONCBS_IW(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + upiStpUpiNonCbsIwRepository.updateRepeatSTP_UPI_NONCBS_IW(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = upiStpUpiNonCbsIwRepository.updateEnquirySTP_UPI_NONCBS_IW(status, res039, res125, res126,
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
	public int updateSTP_UPI_NONCBS_OW(IsoV93MessageRes res, String type) {
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
				updatecount = upiStpUpiNonCbsOwRepository.updateSTP_UPI_NONCBS_OW(status, res039, res125, res126,
						approvestatus, ref);
			} else if (type.equalsIgnoreCase("REPEAT")) {
				updatecount = updatecount + upiStpUpiNonCbsOwRepository.updateRepeatSTP_UPI_NONCBS_OW(status, res039,
						res125, res126, approvestatus, ref);
			} else if (type.equalsIgnoreCase("ENQUIRY")) {
				updatecount = upiStpUpiNonCbsOwRepository.updateEnquirySTP_UPI_NONCBS_OW(status, res039, res125, res126,
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

}
