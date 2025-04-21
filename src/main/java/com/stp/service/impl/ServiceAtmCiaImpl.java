package com.stp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iso.config.IsoV93Message;
import com.iso.opt.IsoPackagerGlobal;
import com.stp.dao.db1.StpAtmCiaBnaDepProactiveRepository;
import com.stp.dao.db1.StpAtmCiaMainCbsEjRepository;
import com.stp.dao.db1.StpAtmCiaMainCbsEjRrbFailedRepository;
import com.stp.dao.db1.StpAtmCiaMainProactiveRepository;
import com.stp.dao.db1.StpAtmCiaOnusCmplRepository;
import com.stp.dao.db1.StpAtmCiaOnusErrorCmplRepository;
import com.stp.dao.db1.StpAtmCiaOnusOvgReclRepository;
import com.stp.dao.db1.StpAtmCiaOnusPenaltyRepository;
import com.stp.dao.db1.StpAtmCiaOnusSrlMsdCmplRepository;
import com.stp.dao.db1.StpAtmCiaOnusTxnLevCmplRepository;
import com.stp.dao.db1.StpAtmCiaReconBulkReclRepository;
import com.stp.model.db1.StpAtmCiaBnaDepProactive;
import com.stp.model.db1.StpAtmCiaMainCbsEj;
import com.stp.model.db1.StpAtmCiaMainCbsEjRrbFailed;
import com.stp.model.db1.StpAtmCiaMainProactive;
import com.stp.model.db1.StpAtmCiaOnusCmpl;
import com.stp.model.db1.StpAtmCiaOnusErrorCmpl;
import com.stp.model.db1.StpAtmCiaOnusOvgRecl;
import com.stp.model.db1.StpAtmCiaOnusPenalty;
import com.stp.model.db1.StpAtmCiaOnusSrlMsdCmpl;
import com.stp.model.db1.StpAtmCiaOnusTxnLevCmpl;
import com.stp.model.db1.StpAtmCiaReconBulkRecl;
import com.stp.service.ServiceAtmCia;

@Service
public class ServiceAtmCiaImpl implements ServiceAtmCia {

	private static final Logger logger = LoggerFactory.getLogger(ServiceAtmCiaImpl.class);

	private final StpAtmCiaBnaDepProactiveRepository stpAtmCiaBnaDepProactiveRepository;
	private final StpAtmCiaMainCbsEjRepository stpAtmCiaMainCbsEjRepository;
	private final StpAtmCiaMainCbsEjRrbFailedRepository stpAtmCiaMainCbsEjRrbFailedRepository;
	private final StpAtmCiaMainProactiveRepository stpAtmCiaMainProactiveRepository;
	private final StpAtmCiaOnusCmplRepository stpAtmCiaOnusCmplRepository;
	private final StpAtmCiaOnusErrorCmplRepository stpAtmCiaOnusErrorCmplRepository;
	private final StpAtmCiaOnusOvgReclRepository stpAtmCiaOnusOvgReclRepository;
	private final StpAtmCiaOnusPenaltyRepository stpAtmCiaOnusPenaltyRepository;
	private final StpAtmCiaOnusSrlMsdCmplRepository stpAtmCiaOnusSrlMsdCmplRepository;
	private final StpAtmCiaOnusTxnLevCmplRepository stpAtmCiaOnusTxnLevCmplRepository;
	private final StpAtmCiaReconBulkReclRepository stpAtmCiaReconBulkReclRepository;

	@Autowired
	public ServiceAtmCiaImpl(StpAtmCiaBnaDepProactiveRepository stpAtmCiaBnaDepProactiveRepository,
			StpAtmCiaMainCbsEjRepository stpAtmCiaMainCbsEjRepository,
			StpAtmCiaMainCbsEjRrbFailedRepository stpAtmCiaMainCbsEjRrbFailedRepository,
			StpAtmCiaMainProactiveRepository stpAtmCiaMainProactiveRepository,
			StpAtmCiaOnusCmplRepository stpAtmCiaOnusCmplRepository,
			StpAtmCiaOnusErrorCmplRepository stpAtmCiaOnusErrorCmplRepository,
			StpAtmCiaOnusOvgReclRepository stpAtmCiaOnusOvgReclRepository,
			StpAtmCiaOnusPenaltyRepository stpAtmCiaOnusPenaltyRepository,
			StpAtmCiaOnusSrlMsdCmplRepository stpAtmCiaOnusSrlMsdCmplRepository,
			StpAtmCiaOnusTxnLevCmplRepository stpAtmCiaOnusTxnLevCmplRepository,
			StpAtmCiaReconBulkReclRepository stpAtmCiaReconBulkReclRepository) {
		super();
		this.stpAtmCiaBnaDepProactiveRepository = stpAtmCiaBnaDepProactiveRepository;
		this.stpAtmCiaMainCbsEjRepository = stpAtmCiaMainCbsEjRepository;
		this.stpAtmCiaMainCbsEjRrbFailedRepository = stpAtmCiaMainCbsEjRrbFailedRepository;
		this.stpAtmCiaMainProactiveRepository = stpAtmCiaMainProactiveRepository;
		this.stpAtmCiaOnusCmplRepository = stpAtmCiaOnusCmplRepository;
		this.stpAtmCiaOnusErrorCmplRepository = stpAtmCiaOnusErrorCmplRepository;
		this.stpAtmCiaOnusOvgReclRepository = stpAtmCiaOnusOvgReclRepository;
		this.stpAtmCiaOnusPenaltyRepository = stpAtmCiaOnusPenaltyRepository;
		this.stpAtmCiaOnusSrlMsdCmplRepository = stpAtmCiaOnusSrlMsdCmplRepository;
		this.stpAtmCiaOnusTxnLevCmplRepository = stpAtmCiaOnusTxnLevCmplRepository;
		this.stpAtmCiaReconBulkReclRepository = stpAtmCiaReconBulkReclRepository;
	}

	@Override
	public List<IsoV93Message> getProcesRequest(String tableName, String requestType, String batchId, String status) {
		List<IsoV93Message> request1200 = null;
		try {
			if (tableName.equalsIgnoreCase("stp_atmcia_bna_dep")) {
				List<StpAtmCiaBnaDepProactive> requestList = processStpAtmCiaBnaDepProactive(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_atmcia_main_cbs_ej")) {
				List<StpAtmCiaMainCbsEj> requestList = processStpAtmCiaMainCbsEj(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_atmcia_main_cbs_ej_rrb_failed")) {
				List<StpAtmCiaMainCbsEjRrbFailed> requestList = processStpAtmCiaMainCbsEjRrbFailed(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_atmcia_main_proactive")) {
				List<StpAtmCiaMainProactive> requestList = processStpAtmCiaMainProactive(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_atmcia_onus_cmpl")) {
				List<StpAtmCiaOnusCmpl> requestList = processStpAtmCiaOnusCmpl(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_atmcia_onus_error_cmpl")) {
				List<StpAtmCiaOnusErrorCmpl> requestList = processStpAtmCiaOnusErrorCmpl(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_atmcia_onus_ovg_recl")) {
				List<StpAtmCiaOnusOvgRecl> requestList = processStpAtmCiaOnusOvgRecl(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_atmcia_onus_penalty")) {
				List<StpAtmCiaOnusPenalty> requestList = processStpAtmCiaOnusPenalty(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_atmcia_onus_srl_msd_cmpl")) {
				List<StpAtmCiaOnusSrlMsdCmpl> requestList = processStpAtmCiaOnusSrlMsdCmpl(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_atmcia_onus_txn_lev_cmpl")) {
				List<StpAtmCiaOnusTxnLevCmpl> requestList = processStpAtmCiaOnusTxnLevCmpl(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			} else if (tableName.equalsIgnoreCase("stp_atmcia_recon_bulk_recl")) {
				List<StpAtmCiaReconBulkRecl> requestList = processStpAtmCiaReconBulkRecl(batchId, status);
				request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
			}
		} catch (Exception e) {
			logger.error("Executor getProcesRequest while waiting: {}", e.getMessage(), e);
		}
		return request1200;
	}

	@Override
	public List<StpAtmCiaBnaDepProactive> processStpAtmCiaBnaDepProactive(String batchid, String status) {
		return stpAtmCiaBnaDepProactiveRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpAtmCiaMainCbsEj> processStpAtmCiaMainCbsEj(String batchid, String status) {
		return stpAtmCiaMainCbsEjRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpAtmCiaMainCbsEjRrbFailed> processStpAtmCiaMainCbsEjRrbFailed(String batchid, String status) {
		return stpAtmCiaMainCbsEjRrbFailedRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpAtmCiaMainProactive> processStpAtmCiaMainProactive(String batchid, String status) {
		return stpAtmCiaMainProactiveRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpAtmCiaOnusCmpl> processStpAtmCiaOnusCmpl(String batchid, String status) {
		return stpAtmCiaOnusCmplRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpAtmCiaOnusErrorCmpl> processStpAtmCiaOnusErrorCmpl(String batchid, String status) {
		return stpAtmCiaOnusErrorCmplRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpAtmCiaOnusOvgRecl> processStpAtmCiaOnusOvgRecl(String batchid, String status) {
		return stpAtmCiaOnusOvgReclRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpAtmCiaOnusPenalty> processStpAtmCiaOnusPenalty(String batchid, String status) {
		return stpAtmCiaOnusPenaltyRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpAtmCiaOnusSrlMsdCmpl> processStpAtmCiaOnusSrlMsdCmpl(String batchid, String status) {
		return stpAtmCiaOnusSrlMsdCmplRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpAtmCiaOnusTxnLevCmpl> processStpAtmCiaOnusTxnLevCmpl(String batchid, String status) {
		return stpAtmCiaOnusTxnLevCmplRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpAtmCiaReconBulkRecl> processStpAtmCiaReconBulkRecl(String batchid, String status) {
		return stpAtmCiaReconBulkReclRepository.findByBatchidAndStatus(batchid, status);
	}

}
