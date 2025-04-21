package com.stp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iso.config.IsoV93Message;
import com.iso.opt.IsoPackagerGlobal;
import com.stp.dao.db1.StpImpsChargebackReportRepository;
import com.stp.dao.db1.StpImpsCustomerCompRepository;
import com.stp.dao.db1.StpImpsNetworkDeclineIwRepository;
import com.stp.dao.db1.StpImpsNetworkDeclineOwRepository;
import com.stp.dao.db1.StpImpsNonNpciIwRepository;
import com.stp.dao.db1.StpImpsNonNpciOwRepository;
import com.stp.dao.db1.StpImpsNoncbsIwRepository;
import com.stp.dao.db1.StpImpsNoncbsOwRepository;
import com.stp.dao.db1.StpImpsNtslNetSetTtumRepository;
import com.stp.dao.db1.StpImpsPrearbitrationReportRepository;
import com.stp.dao.db1.StpImpsRccReportRepository;
import com.stp.dao.db1.StpImpsRccRepraiseReportRepository;
import com.stp.dao.db1.StpImpsTccDataIwRepository;
import com.stp.dao.db1.StpImpsTccDataIwRetRepository;
import com.stp.model.db1.StpImpsChargebackReport;
import com.stp.model.db1.StpImpsCustomerComp;
import com.stp.model.db1.StpImpsNetworkDeclineIw;
import com.stp.model.db1.StpImpsNetworkDeclineOw;
import com.stp.model.db1.StpImpsNonNpciIw;
import com.stp.model.db1.StpImpsNonNpciOw;
import com.stp.model.db1.StpImpsNoncbsIw;
import com.stp.model.db1.StpImpsNoncbsOw;
import com.stp.model.db1.StpImpsNtslNetSetTtum;
import com.stp.model.db1.StpImpsPrearbitrationReport;
import com.stp.model.db1.StpImpsRccReport;
import com.stp.model.db1.StpImpsRccRepraiseReport;
import com.stp.model.db1.StpImpsTccDataIw;
import com.stp.model.db1.StpImpsTccDataIwRet;
import com.stp.service.ServiceImps;

@Service
public class ServiceImpsImpl implements ServiceImps {

	private static final Logger logger = LoggerFactory.getLogger(ServiceImpsImpl.class);
	private final StpImpsNoncbsIwRepository stpImpsNoncbsIwRepository;
	private final StpImpsNoncbsOwRepository stpImpsNoncbsOwRepository;
	private final StpImpsNonNpciIwRepository stpImpsNonNpciIwRepository;
	private final StpImpsNonNpciOwRepository stpImpsNonNpciOwRepository;
	private final StpImpsNetworkDeclineIwRepository stpImpsNetworkDeclineIwRepository;
	private final StpImpsNetworkDeclineOwRepository stpImpsNetworkDeclineOwRepository;
	private final StpImpsNtslNetSetTtumRepository stpImpsNtslNetSetTtumRepository;
	private final StpImpsPrearbitrationReportRepository stpImpsPrearbitrationReportRepository;
	private final StpImpsRccReportRepository stpImpsRccReportRepository;
	private final StpImpsRccRepraiseReportRepository stpImpsRccRepraiseReportRepository;
	private final StpImpsTccDataIwRepository stpImpsTccDataIwRepository;
	private final StpImpsTccDataIwRetRepository stpImpsTccDataIwRetRepository;
	private final StpImpsChargebackReportRepository stpImpsChargebackReportRepository;
	private final StpImpsCustomerCompRepository stpImpsCustomerCompRepository;

	@Autowired
	public ServiceImpsImpl(StpImpsNoncbsIwRepository stpImpsNoncbsIwRepository,
			StpImpsNoncbsOwRepository stpImpsNoncbsOwRepository, StpImpsNonNpciIwRepository stpImpsNonNpciIwRepository,
			StpImpsNonNpciOwRepository stpImpsNonNpciOwRepository,
			StpImpsNetworkDeclineIwRepository stpImpsNetworkDeclineIwRepository,
			StpImpsNetworkDeclineOwRepository stpImpsNetworkDeclineOwRepository,
			StpImpsNtslNetSetTtumRepository stpImpsNtslNetSetTtumRepository,
			StpImpsPrearbitrationReportRepository stpImpsPrearbitrationReportRepository,
			StpImpsRccReportRepository stpImpsRccReportRepository,
			StpImpsRccRepraiseReportRepository stpImpsRccRepraiseReportRepository,
			StpImpsTccDataIwRepository stpImpsTccDataIwRepository,
			StpImpsTccDataIwRetRepository stpImpsTccDataIwRetRepository,
			StpImpsChargebackReportRepository stpImpsChargebackReportRepository,
			StpImpsCustomerCompRepository stpImpsCustomerCompRepository) {
		this.stpImpsNoncbsIwRepository = stpImpsNoncbsIwRepository;
		this.stpImpsNoncbsOwRepository = stpImpsNoncbsOwRepository;
		this.stpImpsNonNpciIwRepository = stpImpsNonNpciIwRepository;
		this.stpImpsNonNpciOwRepository = stpImpsNonNpciOwRepository;
		this.stpImpsNetworkDeclineIwRepository = stpImpsNetworkDeclineIwRepository;
		this.stpImpsNetworkDeclineOwRepository = stpImpsNetworkDeclineOwRepository;
		this.stpImpsNtslNetSetTtumRepository = stpImpsNtslNetSetTtumRepository;
		this.stpImpsPrearbitrationReportRepository = stpImpsPrearbitrationReportRepository;
		this.stpImpsRccReportRepository = stpImpsRccReportRepository;
		this.stpImpsRccRepraiseReportRepository = stpImpsRccRepraiseReportRepository;
		this.stpImpsTccDataIwRepository = stpImpsTccDataIwRepository;
		this.stpImpsTccDataIwRetRepository = stpImpsTccDataIwRetRepository;
		this.stpImpsChargebackReportRepository = stpImpsChargebackReportRepository;
		this.stpImpsCustomerCompRepository = stpImpsCustomerCompRepository;
	}

	public List<IsoV93Message> getProcesRequest(String tableName, String requestType, String batchId, String status) {
		List<IsoV93Message> request1200 = null;
		try {
			if (tableName.equalsIgnoreCase("stp_imps_noncbs_iw")) {
				List<StpImpsNoncbsIw> requestList = processStpImpsNoncbsIw(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_noncbs_ow")) {
				List<StpImpsNoncbsIw> requestList = processStpImpsNoncbsIw(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_nonnpci_iw")) {
				List<StpImpsNonNpciIw> requestList = processStpImpsNonNpciIw(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_nonnpci_ow")) {
				List<StpImpsNonNpciOw> requestList = processStpImpsNonNpciOw(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_network_decline_iw")) {
				List<StpImpsNetworkDeclineIw> requestList = processStpImpsNetworkDeclineIw(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_network_decline_ow")) {
				List<StpImpsNetworkDeclineOw> requestList = processStpImpsNetworkDeclineOw(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_ntsl_netset_ttum")) {
				List<StpImpsNtslNetSetTtum> requestList = processStpImpsNtslNetSetTtum(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_prearbitration_report")) {
				List<StpImpsPrearbitrationReport> requestList = processStpImpsPrearbitrationReport(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_rcc_report")) {
				List<StpImpsRccReport> requestList = processStpImpsRccReport(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_rcc_repraise_report")) {
				List<StpImpsRccRepraiseReport> requestList = processStpImpsRccRepraiseReport(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_tcc_data_iw")) {
				List<StpImpsTccDataIw> requestList = processStpImpsTccDataIw(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_tcc_data_iw_ret")) {
				List<StpImpsTccDataIwRet> requestList = processStpImpsTccDataIwRet(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_chargeback_report")) {
				List<StpImpsChargebackReport> requestList = processStpImpsChargebackReport(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_imps_customer_comp")) {
				List<StpImpsCustomerComp> requestList = processStpImpsCustomerComp(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			}
		} catch (Exception e) {
			logger.error("Executor getProcesRequest while waiting: {}", e.getMessage(), e);
		}
		return request1200;
	}

	@Override
	public List<StpImpsNoncbsIw> processStpImpsNoncbsIw(String batchid, String status) {
		return stpImpsNoncbsIwRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsNoncbsOw> processStpImpsNoncbsOw(String batchid, String status) {
		return stpImpsNoncbsOwRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsNonNpciIw> processStpImpsNonNpciIw(String batchid, String status) {
		return stpImpsNonNpciIwRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsNonNpciOw> processStpImpsNonNpciOw(String batchid, String status) {
		return stpImpsNonNpciOwRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsNetworkDeclineIw> processStpImpsNetworkDeclineIw(String batchid, String status) {
		return stpImpsNetworkDeclineIwRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsNetworkDeclineOw> processStpImpsNetworkDeclineOw(String batchid, String status) {
		return stpImpsNetworkDeclineOwRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsNtslNetSetTtum> processStpImpsNtslNetSetTtum(String batchid, String status) {
		return stpImpsNtslNetSetTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsPrearbitrationReport> processStpImpsPrearbitrationReport(String batchid, String status) {
		return stpImpsPrearbitrationReportRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsRccReport> processStpImpsRccReport(String batchid, String status) {
		return stpImpsRccReportRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsRccRepraiseReport> processStpImpsRccRepraiseReport(String batchid, String status) {
		return stpImpsRccRepraiseReportRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsTccDataIw> processStpImpsTccDataIw(String batchid, String status) {
		return stpImpsTccDataIwRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsTccDataIwRet> processStpImpsTccDataIwRet(String batchid, String status) {
		return stpImpsTccDataIwRetRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsChargebackReport> processStpImpsChargebackReport(String batchid, String status) {
		return stpImpsChargebackReportRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpImpsCustomerComp> processStpImpsCustomerComp(String batchid, String status) {
		return stpImpsCustomerCompRepository.findByBatchidAndStatus(batchid, status);
	}

}
