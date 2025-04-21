package com.stp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iso.config.IsoV93Message;
import com.iso.opt.IsoPackagerGlobal;
import com.stp.dao.db1.StpUpiAdjustmentReportRepository;
import com.stp.dao.db1.StpUpiDrcNpciRepository;
import com.stp.dao.db1.StpUpiMultiReversalRepository;
import com.stp.dao.db1.StpUpiNonCbsIwRepository;
import com.stp.dao.db1.StpUpiNonCbsOwRepository;
import com.stp.dao.db1.StpUpiNtlsNetSetTtumRepository;
import com.stp.dao.db1.StpUpiRetDataRepository;
import com.stp.dao.db1.StpUpiTccDataRepository;
import com.stp.model.db1.StpUpiAdjustmentReport;
import com.stp.model.db1.StpUpiDrcNpci;
import com.stp.model.db1.StpUpiMultiReversal;
import com.stp.model.db1.StpUpiNonCbsIw;
import com.stp.model.db1.StpUpiNonCbsOw;
import com.stp.model.db1.StpUpiNtlsNetSetTtum;
import com.stp.model.db1.StpUpiRetData;
import com.stp.model.db1.StpUpiTccData;
import com.stp.service.ServiceUpi;

@Service
public class ServiceUpiImpl implements ServiceUpi {

	private static final Logger logger = LoggerFactory.getLogger(ServiceUpiImpl.class);

	private final StpUpiTccDataRepository stpUpiTccDataRepository;
	private final StpUpiRetDataRepository stpUpiRetDataRepository;
	private final StpUpiNtlsNetSetTtumRepository stpUpiNtlsNetSetTtumRepository;
	private final StpUpiNonCbsOwRepository stpUpiNonCbsOwRepository;
	private final StpUpiNonCbsIwRepository stpUpiNonCbsIwRepository;
	private final StpUpiMultiReversalRepository stpUpiMultiReversalRepository;
	private final StpUpiDrcNpciRepository stpUpiDrcNpciRepository;
	private final StpUpiAdjustmentReportRepository stpUpiAdjustmentReportRepository;

	@Autowired
	public ServiceUpiImpl(StpUpiTccDataRepository stpUpiTccDataRepository,
			StpUpiRetDataRepository stpUpiRetDataRepository,
			StpUpiNtlsNetSetTtumRepository stpUpiNtlsNetSetTtumRepository,
			StpUpiNonCbsOwRepository stpUpiNonCbsOwRepository, StpUpiNonCbsIwRepository stpUpiNonCbsIwRepository,
			StpUpiMultiReversalRepository stpUpiMultiReversalRepository,
			StpUpiDrcNpciRepository stpUpiDrcNpciRepository,
			StpUpiAdjustmentReportRepository stpUpiAdjustmentReportRepository) {
		this.stpUpiTccDataRepository = stpUpiTccDataRepository;
		this.stpUpiRetDataRepository = stpUpiRetDataRepository;
		this.stpUpiNtlsNetSetTtumRepository = stpUpiNtlsNetSetTtumRepository;
		this.stpUpiNonCbsOwRepository = stpUpiNonCbsOwRepository;
		this.stpUpiNonCbsIwRepository = stpUpiNonCbsIwRepository;
		this.stpUpiMultiReversalRepository = stpUpiMultiReversalRepository;
		this.stpUpiDrcNpciRepository = stpUpiDrcNpciRepository;
		this.stpUpiAdjustmentReportRepository = stpUpiAdjustmentReportRepository;
	}

	public List<IsoV93Message> getProcesRequest(String tableName, String requestType, String batchId, String status) {
		List<IsoV93Message> request1200 = null;
		try {
			if (tableName.equalsIgnoreCase("stp_upi_tcc_data")) {
				List<StpUpiTccData> requestList = processStpUpiTccData(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_upi_ret_data")) {
				List<StpUpiRetData> requestList = processStpUpiRetData(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_upi_ntsl_netset_ttum")) {
				List<StpUpiNtlsNetSetTtum> requestList = processStpUpiNtlsNetSetTtum(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_upi_noncbs_ow")) {
				List<StpUpiNonCbsOw> requestList = processStpUpiNonCbsOw(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_upi_noncbs_iw")) {
				List<StpUpiNonCbsIw> requestList = processStpUpiNonCbsIw(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_upi_multireversal")) {
				List<StpUpiMultiReversal> requestList = processStpUpiMultiReversal(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_upi_drc_npci")) {
				List<StpUpiDrcNpci> requestList = processStpUpiDrcNpci(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_upi_adjustment_report")) {
				List<StpUpiAdjustmentReport> requestList = processStpUpiAdjustmentReport(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			}
		} catch (Exception e) {
			logger.error("Executor getProcesRequest while waiting: {}", e.getMessage(), e);
		}
		return request1200;
	}

	@Override
	public List<StpUpiTccData> processStpUpiTccData(String batchid, String status) {
		return stpUpiTccDataRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpUpiRetData> processStpUpiRetData(String batchid, String status) {

		return stpUpiRetDataRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpUpiNtlsNetSetTtum> processStpUpiNtlsNetSetTtum(String batchid, String status) {
		return stpUpiNtlsNetSetTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpUpiNonCbsOw> processStpUpiNonCbsOw(String batchid, String status) {
		return stpUpiNonCbsOwRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpUpiNonCbsIw> processStpUpiNonCbsIw(String batchid, String status) {
		return stpUpiNonCbsIwRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpUpiMultiReversal> processStpUpiMultiReversal(String batchid, String status) {
		return stpUpiMultiReversalRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpUpiDrcNpci> processStpUpiDrcNpci(String batchid, String status) {
		return stpUpiDrcNpciRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpUpiAdjustmentReport> processStpUpiAdjustmentReport(String batchid, String status) {
		return stpUpiAdjustmentReportRepository.findByBatchidAndStatus(batchid, status);
	}
}
