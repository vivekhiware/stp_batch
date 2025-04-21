package com.stp.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iso.config.IsoV93Message;
import com.iso.opt.IsoPackagerGlobal;
import com.stp.dao.db1.StpCardsMasterCardDomIssSurchargeCreditRepository;
import com.stp.dao.db1.StpCardsMasterCardDomIssSurchargeDebitRepository;
import com.stp.dao.db1.StpCardsMasterCardInternationSettlementRepository;
import com.stp.dao.db1.StpCardsMasterCardPosIssDropTtumRepository;
import com.stp.dao.db1.StpCardsMasterCardPosIssLatePresentmentTtumRepository;
import com.stp.dao.db1.StpCardsMasterCardPosProactiveTtumRepository;
import com.stp.dao.db1.StpCardsMasterCardSettlementRepository;
import com.stp.dao.db1.StpCardsNfsAcqCreditReconTtumRepository;
import com.stp.dao.db1.StpCardsNfsAcqDebitReconTtumRepository;
import com.stp.dao.db1.StpCardsNfsIssReconTtumDropRepository;
import com.stp.dao.db1.StpCardsNfsIssReconTtumLateReversalRepository;
import com.stp.dao.db1.StpCardsNfsIssReconTtumProactiveRepository;
import com.stp.dao.db1.StpCardsRupayDeclineTtumRepository;
import com.stp.dao.db1.StpCardsRupayDomAdjustmentTtumRepository;
import com.stp.dao.db1.StpCardsRupayDomLatePresentmentRepository;
import com.stp.dao.db1.StpCardsRupayDomSurchargeCreditRepository;
import com.stp.dao.db1.StpCardsRupayDomSurchargeDebitRepository;
import com.stp.dao.db1.StpCardsRupayIntAdjustmentTtumRepository;
import com.stp.dao.db1.StpCardsRupayIntDeclineTtumRepository;
import com.stp.dao.db1.StpCardsRupayIntDropTtumRepository;
import com.stp.dao.db1.StpCardsRupayIntLatepresentmentTtumRepository;
import com.stp.dao.db1.StpCardsRupayIntOfflinePresentmentTtumRepository;
import com.stp.dao.db1.StpCardsRupayIntPosSettlementTtumRepository;
import com.stp.dao.db1.StpCardsRupayIntProactiveTtumRepository;
import com.stp.dao.db1.StpCardsRupayIntSurchargeCreditRepository;
import com.stp.dao.db1.StpCardsRupayIntSurchargeDebitRepository;
import com.stp.dao.db1.StpCardsRupayPosDomSettlementRepository;
import com.stp.dao.db1.StpCardsRupayProactivTtumRepository;
import com.stp.dao.db1.StpCardsVisaIssDomSettlementTtumRepository;
import com.stp.dao.db1.StpCardsVisaIssDropTtumRepository;
import com.stp.dao.db1.StpCardsVisaIssLatePresentmentTtumRepository;
import com.stp.dao.db1.StpCardsVisaIssOrgWdlTtumRepository;
import com.stp.dao.db1.StpCardsVisaIssProactiveTtumRepository;
import com.stp.dao.db1.StpCardsVisaIssSurchargeCreditRepository;
import com.stp.dao.db1.StpCardsVisaIssSurchargeDebitRepository;
import com.stp.model.db1.StpCardsMasterCardDomIssSurchargeCredit;
import com.stp.model.db1.StpCardsMasterCardDomIssSurchargeDebit;
import com.stp.model.db1.StpCardsMasterCardInternationSettlement;
import com.stp.model.db1.StpCardsMasterCardPosIssDropTtum;
import com.stp.model.db1.StpCardsMasterCardPosIssLatePresentmentTtum;
import com.stp.model.db1.StpCardsMasterCardPosProactiveTtum;
import com.stp.model.db1.StpCardsMasterCardSettlement;
import com.stp.model.db1.StpCardsNfsAcqCreditReconTtum;
import com.stp.model.db1.StpCardsNfsAcqDebitReconTtum;
import com.stp.model.db1.StpCardsNfsIssReconTtumDrop;
import com.stp.model.db1.StpCardsNfsIssReconTtumLateReversal;
import com.stp.model.db1.StpCardsNfsIssReconTtumProactive;
import com.stp.model.db1.StpCardsRupayDeclineTtum;
import com.stp.model.db1.StpCardsRupayDomAdjustmentTtum;
import com.stp.model.db1.StpCardsRupayDomLatePresentment;
import com.stp.model.db1.StpCardsRupayDomSurchargeCredit;
import com.stp.model.db1.StpCardsRupayDomSurchargeDebit;
import com.stp.model.db1.StpCardsRupayIntAdjustmentTtum;
import com.stp.model.db1.StpCardsRupayIntDeclineTtum;
import com.stp.model.db1.StpCardsRupayIntDropTtum;
import com.stp.model.db1.StpCardsRupayIntLatepresentmentTtum;
import com.stp.model.db1.StpCardsRupayIntOfflinePresentmentTtum;
import com.stp.model.db1.StpCardsRupayIntPosSettlementTtum;
import com.stp.model.db1.StpCardsRupayIntProactiveTtum;
import com.stp.model.db1.StpCardsRupayIntSurchargeCredit;
import com.stp.model.db1.StpCardsRupayIntSurchargeDebit;
import com.stp.model.db1.StpCardsRupayPosDomSettlement;
import com.stp.model.db1.StpCardsRupayProactivTtum;
import com.stp.model.db1.StpCardsVisaIssDomSettlementTtum;
import com.stp.model.db1.StpCardsVisaIssDropTtum;
import com.stp.model.db1.StpCardsVisaIssLatePresentmentTtum;
import com.stp.model.db1.StpCardsVisaIssOrgWdlTtum;
import com.stp.model.db1.StpCardsVisaIssProactiveTtum;
import com.stp.model.db1.StpCardsVisaIssSurchargeCredit;
import com.stp.model.db1.StpCardsVisaIssSurchargeDebit;
import com.stp.service.ServiceCards;

@Service
public class ServiceCardsImpl implements ServiceCards {

	private static final Logger logger = LoggerFactory.getLogger(ServiceCardsImpl.class);

	private final StpCardsMasterCardDomIssSurchargeCreditRepository stpCardsMasterCardDomIssSurchargeCreditRepository;
	private final StpCardsMasterCardDomIssSurchargeDebitRepository stpCardsMasterCardDomIssSurchargeDebitRepository;
	private final StpCardsMasterCardInternationSettlementRepository stpCardsMasterCardInternationSettlementRepository;
	private final StpCardsMasterCardPosIssDropTtumRepository stpCardsMasterCardPosIssDropTtumRepository;

	private final StpCardsMasterCardPosIssLatePresentmentTtumRepository stpCardsMasterCardPosIssLatePresentmentTtumRepository;
	private final StpCardsMasterCardPosProactiveTtumRepository stpCardsMasterCardPosProactiveTtumRepository;
	private final StpCardsMasterCardSettlementRepository stpCardsMasterCardSettlementRepository;

	private final StpCardsNfsAcqCreditReconTtumRepository stpCardsNfsAcqCreditReconTtumRepository;
	private final StpCardsNfsAcqDebitReconTtumRepository stpCardsNfsAcqDebitReconTtumRepository;
	private final StpCardsNfsIssReconTtumDropRepository stpCardsNfsIssReconTtumDropRepository;
	private final StpCardsNfsIssReconTtumLateReversalRepository stpCardsNfsIssReconTtumLateReversalRepository;

	private final StpCardsNfsIssReconTtumProactiveRepository stpCardsNfsIssReconTtumProactiveRepository;
	private final StpCardsRupayDeclineTtumRepository stpCardsRupayDeclineTtumRepository;
	private final StpCardsRupayDomAdjustmentTtumRepository stpCardsRupayDomAdjustmentTtumRepository;
	private final StpCardsRupayDomLatePresentmentRepository stpCardsRupayDomLatePresentmentRepository;
	private final StpCardsRupayDomSurchargeCreditRepository stpCardsRupayDomSurchargeCreditRepository;
	private final StpCardsRupayDomSurchargeDebitRepository stpCardsRupayDomSurchargeDebitRepository;
	private final StpCardsRupayIntAdjustmentTtumRepository stpCardsRupayIntAdjustmentTtumRepository;
	private final StpCardsRupayIntDeclineTtumRepository stpCardsRupayIntDeclineTtumRepository;
	private final StpCardsRupayIntDropTtumRepository stpCardsRupayIntDropTtumRepository;
	private final StpCardsRupayIntLatepresentmentTtumRepository stpCardsRupayIntLatepresentmentTtumRepository;
	private final StpCardsRupayIntOfflinePresentmentTtumRepository stpCardsRupayIntOfflinePresentmentTtumRepository;
	private final StpCardsRupayIntPosSettlementTtumRepository stpCardsRupayIntPosSettlementTtumRepository;
	private final StpCardsRupayIntProactiveTtumRepository stpCardsRupayIntProactiveTtumRepository;
	private final StpCardsRupayIntSurchargeCreditRepository stpCardsRupayIntSurchargeCreditRepository;
	private final StpCardsRupayIntSurchargeDebitRepository stpCardsRupayIntSurchargeDebitRepository;
	private final StpCardsRupayPosDomSettlementRepository stpCardsRupayPosDomSettlementRepository;
	private final StpCardsRupayProactivTtumRepository stpCardsRupayProactivTtumRepository;
	private final StpCardsVisaIssDomSettlementTtumRepository stpCardsVisaIssDomSettlementTtumRepository;
	private final StpCardsVisaIssDropTtumRepository stpCardsVisaIssDropTtumRepository;
	private final StpCardsVisaIssLatePresentmentTtumRepository stpCardsVisaIssLatePresentmentTtumRepository;
	private final StpCardsVisaIssOrgWdlTtumRepository stpCardsVisaIssOrgWdlTtumRepository;
	private final StpCardsVisaIssProactiveTtumRepository stpCardsVisaIssProactiveTtumRepository;
	private final StpCardsVisaIssSurchargeCreditRepository stpCardsVisaIssSurchargeCreditRepository;
	private final StpCardsVisaIssSurchargeDebitRepository stpCardsVisaIssSurchargeDebitRepository;

	@Autowired
	public ServiceCardsImpl(
			StpCardsMasterCardDomIssSurchargeCreditRepository stpCardsMasterCardDomIssSurchargeCreditRepository,
			StpCardsMasterCardDomIssSurchargeDebitRepository stpCardsMasterCardDomIssSurchargeDebitRepository,
			StpCardsMasterCardInternationSettlementRepository stpCardsMasterCardInternationSettlementRepository,
			StpCardsMasterCardPosIssDropTtumRepository stpCardsMasterCardPosIssDropTtumRepository,
			StpCardsMasterCardPosIssLatePresentmentTtumRepository stpCardsMasterCardPosIssLatePresentmentTtumRepository,
			StpCardsMasterCardPosProactiveTtumRepository stpCardsMasterCardPosProactiveTtumRepository,
			StpCardsMasterCardSettlementRepository stpCardsMasterCardSettlementRepository,
			StpCardsNfsAcqCreditReconTtumRepository stpCardsNfsAcqCreditReconTtumRepository,
			StpCardsNfsAcqDebitReconTtumRepository stpCardsNfsAcqDebitReconTtumRepository,
			StpCardsNfsIssReconTtumDropRepository stpCardsNfsIssReconTtumDropRepository,
			StpCardsNfsIssReconTtumLateReversalRepository stpCardsNfsIssReconTtumLateReversalRepository,
			StpCardsNfsIssReconTtumProactiveRepository stpCardsNfsIssReconTtumProactiveRepository,
			StpCardsRupayDeclineTtumRepository stpCardsRupayDeclineTtumRepository,
			StpCardsRupayDomAdjustmentTtumRepository stpCardsRupayDomAdjustmentTtumRepository,
			StpCardsRupayDomLatePresentmentRepository stpCardsRupayDomLatePresentmentRepository,
			StpCardsRupayDomSurchargeCreditRepository stpCardsRupayDomSurchargeCreditRepository,
			StpCardsRupayDomSurchargeDebitRepository stpCardsRupayDomSurchargeDebitRepository,
			StpCardsRupayIntAdjustmentTtumRepository stpCardsRupayIntAdjustmentTtumRepository,
			StpCardsRupayIntDeclineTtumRepository stpCardsRupayIntDeclineTtumRepository,
			StpCardsRupayIntDropTtumRepository stpCardsRupayIntDropTtumRepository,
			StpCardsRupayIntLatepresentmentTtumRepository stpCardsRupayIntLatepresentmentTtumRepository,
			StpCardsRupayIntOfflinePresentmentTtumRepository stpCardsRupayIntOfflinePresentmentTtumRepository,
			StpCardsRupayIntPosSettlementTtumRepository stpCardsRupayIntPosSettlementTtumRepository,
			StpCardsRupayIntProactiveTtumRepository stpCardsRupayIntProactiveTtumRepository,
			StpCardsRupayIntSurchargeCreditRepository stpCardsRupayIntSurchargeCreditRepository,
			StpCardsRupayIntSurchargeDebitRepository stpCardsRupayIntSurchargeDebitRepository,
			StpCardsRupayPosDomSettlementRepository stpCardsRupayPosDomSettlementRepository,
			StpCardsRupayProactivTtumRepository stpCardsRupayProactivTtumRepository,
			StpCardsVisaIssDomSettlementTtumRepository stpCardsVisaIssDomSettlementTtumRepository,
			StpCardsVisaIssDropTtumRepository stpCardsVisaIssDropTtumRepository,
			StpCardsVisaIssLatePresentmentTtumRepository stpCardsVisaIssLatePresentmentTtumRepository,
			StpCardsVisaIssOrgWdlTtumRepository stpCardsVisaIssOrgWdlTtumRepository,
			StpCardsVisaIssProactiveTtumRepository stpCardsVisaIssProactiveTtumRepository,
			StpCardsVisaIssSurchargeCreditRepository stpCardsVisaIssSurchargeCreditRepository,
			StpCardsVisaIssSurchargeDebitRepository stpCardsVisaIssSurchargeDebitRepository) {
		this.stpCardsMasterCardDomIssSurchargeCreditRepository = stpCardsMasterCardDomIssSurchargeCreditRepository;
		this.stpCardsMasterCardDomIssSurchargeDebitRepository = stpCardsMasterCardDomIssSurchargeDebitRepository;
		this.stpCardsMasterCardInternationSettlementRepository = stpCardsMasterCardInternationSettlementRepository;
		this.stpCardsMasterCardPosIssDropTtumRepository = stpCardsMasterCardPosIssDropTtumRepository;
		this.stpCardsMasterCardPosIssLatePresentmentTtumRepository = stpCardsMasterCardPosIssLatePresentmentTtumRepository;
		this.stpCardsMasterCardPosProactiveTtumRepository = stpCardsMasterCardPosProactiveTtumRepository;
		this.stpCardsMasterCardSettlementRepository = stpCardsMasterCardSettlementRepository;
		this.stpCardsNfsAcqCreditReconTtumRepository = stpCardsNfsAcqCreditReconTtumRepository;
		this.stpCardsNfsAcqDebitReconTtumRepository = stpCardsNfsAcqDebitReconTtumRepository;
		this.stpCardsNfsIssReconTtumDropRepository = stpCardsNfsIssReconTtumDropRepository;
		this.stpCardsNfsIssReconTtumLateReversalRepository = stpCardsNfsIssReconTtumLateReversalRepository;
		this.stpCardsNfsIssReconTtumProactiveRepository = stpCardsNfsIssReconTtumProactiveRepository;
		this.stpCardsRupayDeclineTtumRepository = stpCardsRupayDeclineTtumRepository;
		this.stpCardsRupayDomAdjustmentTtumRepository = stpCardsRupayDomAdjustmentTtumRepository;
		this.stpCardsRupayDomLatePresentmentRepository = stpCardsRupayDomLatePresentmentRepository;
		this.stpCardsRupayDomSurchargeCreditRepository = stpCardsRupayDomSurchargeCreditRepository;
		this.stpCardsRupayDomSurchargeDebitRepository = stpCardsRupayDomSurchargeDebitRepository;
		this.stpCardsRupayIntAdjustmentTtumRepository = stpCardsRupayIntAdjustmentTtumRepository;
		this.stpCardsRupayIntDeclineTtumRepository = stpCardsRupayIntDeclineTtumRepository;
		this.stpCardsRupayIntDropTtumRepository = stpCardsRupayIntDropTtumRepository;
		this.stpCardsRupayIntLatepresentmentTtumRepository = stpCardsRupayIntLatepresentmentTtumRepository;
		this.stpCardsRupayIntOfflinePresentmentTtumRepository = stpCardsRupayIntOfflinePresentmentTtumRepository;
		this.stpCardsRupayIntPosSettlementTtumRepository = stpCardsRupayIntPosSettlementTtumRepository;
		this.stpCardsRupayIntProactiveTtumRepository = stpCardsRupayIntProactiveTtumRepository;
		this.stpCardsRupayIntSurchargeCreditRepository = stpCardsRupayIntSurchargeCreditRepository;
		this.stpCardsRupayIntSurchargeDebitRepository = stpCardsRupayIntSurchargeDebitRepository;
		this.stpCardsRupayPosDomSettlementRepository = stpCardsRupayPosDomSettlementRepository;
		this.stpCardsRupayProactivTtumRepository = stpCardsRupayProactivTtumRepository;
		this.stpCardsVisaIssDomSettlementTtumRepository = stpCardsVisaIssDomSettlementTtumRepository;
		this.stpCardsVisaIssDropTtumRepository = stpCardsVisaIssDropTtumRepository;
		this.stpCardsVisaIssLatePresentmentTtumRepository = stpCardsVisaIssLatePresentmentTtumRepository;
		this.stpCardsVisaIssOrgWdlTtumRepository = stpCardsVisaIssOrgWdlTtumRepository;
		this.stpCardsVisaIssProactiveTtumRepository = stpCardsVisaIssProactiveTtumRepository;
		this.stpCardsVisaIssSurchargeCreditRepository = stpCardsVisaIssSurchargeCreditRepository;
		this.stpCardsVisaIssSurchargeDebitRepository = stpCardsVisaIssSurchargeDebitRepository;
	}

	@Override
	public List<IsoV93Message> getProcesRequest(String tableName, String requestType, String batchId, String status) {
		List<IsoV93Message> request1200 = null;
		try {
			if (tableName.contains("stp_cards_visa")) {
				request1200 = getVisa(tableName, requestType, batchId, status);
			} else if (tableName.contains("stp_cards_rupay_int")) {
				request1200 = getRupayInternation(tableName, requestType, batchId, status);
			} else if (tableName.contains("stp_cards_rupay")) {
				request1200 = getRupay(tableName, requestType, batchId, status);
			} else if (tableName.contains("stp_cards_nfs")) {
				request1200 = getNfs(tableName, requestType, batchId, status);
			} else if (tableName.contains("stp_cards_mc")) {
				request1200 = getMasterCard(tableName, requestType, batchId, status);
			}
		} catch (Exception e) {
			logger.error("Executor getProcesRequest while waiting: {}", e.getMessage(), e);
		}
		return request1200;
	}

	@Override
	public List<StpCardsVisaIssSurchargeDebit> processStpCardsVisaIssSurchargeDebit(String batchid, String status) {

		return stpCardsVisaIssSurchargeDebitRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsVisaIssSurchargeCredit> processStpCardsVisaIssSurchargeCredit(String batchid, String status) {

		return stpCardsVisaIssSurchargeCreditRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsVisaIssProactiveTtum> processStpCardsVisaIssProactiveTtum(String batchid, String status) {

		return stpCardsVisaIssProactiveTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsVisaIssOrgWdlTtum> processStpCardsVisaIssOrgWdlTtum(String batchid, String status) {

		return stpCardsVisaIssOrgWdlTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsVisaIssLatePresentmentTtum> processStpCardsVisaIssLatePresentmentTtum(String batchid,
			String status) {

		return stpCardsVisaIssLatePresentmentTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsVisaIssDropTtum> processStpCardsVisaIssDropTtum(String batchid, String status) {

		return stpCardsVisaIssDropTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsVisaIssDomSettlementTtum> processStpCardsVisaIssDomSettlementTtum(String batchid,
			String status) {

		return stpCardsVisaIssDomSettlementTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayProactivTtum> processStpCardsRupayProactivTtum(String batchid, String status) {

		return stpCardsRupayProactivTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayPosDomSettlement> processStpCardsRupayPosDomSettlement(String batchid, String status) {

		return stpCardsRupayPosDomSettlementRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayIntSurchargeDebit> processStpCardsRupayIntSurchargeDebit(String batchid, String status) {

		return stpCardsRupayIntSurchargeDebitRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayIntSurchargeCredit> processStpCardsRupayIntSurchargeCredit(String batchid, String status) {

		return stpCardsRupayIntSurchargeCreditRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayIntProactiveTtum> processStpCardsRupayIntProactiveTtum(String batchid, String status) {

		return stpCardsRupayIntProactiveTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayIntPosSettlementTtum> processStpCardsRupayIntPosSettlementTtum(String batchid,
			String status) {

		return stpCardsRupayIntPosSettlementTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayIntOfflinePresentmentTtum> processStpCardsRupayIntOfflinePresentmentTtum(String batchid,
			String status) {

		return stpCardsRupayIntOfflinePresentmentTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayIntLatepresentmentTtum> processStpCardsRupayIntLatepresentmentTtum(String batchid,
			String status) {

		return stpCardsRupayIntLatepresentmentTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayIntDropTtum> processStpCardsRupayIntDropTtum(String batchid, String status) {

		return stpCardsRupayIntDropTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayIntDeclineTtum> processStpCardsRupayIntDeclineTtum(String batchid, String status) {

		return stpCardsRupayIntDeclineTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayIntAdjustmentTtum> processStpCardsRupayIntAdjustmentTtum(String batchid, String status) {

		return stpCardsRupayIntAdjustmentTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayDomSurchargeDebit> processStpCardsRupayDomSurchargeDebit(String batchid, String status) {

		return stpCardsRupayDomSurchargeDebitRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayDomSurchargeCredit> processStpCardsRupayDomSurchargeCredit(String batchid, String status) {

		return stpCardsRupayDomSurchargeCreditRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayDomLatePresentment> processStpCardsRupayDomLatePresentment(String batchid, String status) {

		return stpCardsRupayDomLatePresentmentRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayDomAdjustmentTtum> processStpCardsRupayDomAdjustmentTtum(String batchid, String status) {

		return stpCardsRupayDomAdjustmentTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsRupayDeclineTtum> processStpCardsRupayDeclineTtum(String batchid, String status) {

		return stpCardsRupayDeclineTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsNfsIssReconTtumProactive> processStpCardsNfsIssReconTtumProactive(String batchid,
			String status) {

		return stpCardsNfsIssReconTtumProactiveRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsNfsIssReconTtumLateReversal> processStpCardsNfsIssReconTtumLateReversal(String batchid,
			String status) {

		return stpCardsNfsIssReconTtumLateReversalRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsNfsIssReconTtumDrop> processStpCardsNfsIssReconTtumDrop(String batchid, String status) {

		return stpCardsNfsIssReconTtumDropRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsNfsAcqDebitReconTtum> processStpCardsNfsAcqDebitReconTtum(String batchid, String status) {

		return stpCardsNfsAcqDebitReconTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsNfsAcqCreditReconTtum> processStpCardsNfsAcqCreditReconTtum(String batchid, String status) {

		return stpCardsNfsAcqCreditReconTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsMasterCardSettlement> processStpCardsMasterCardSettlement(String batchid, String status) {

		return stpCardsMasterCardSettlementRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsMasterCardPosProactiveTtum> processStpCardsMasterCardPosProactiveTtum(String batchid,
			String status) {

		return stpCardsMasterCardPosProactiveTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsMasterCardPosIssLatePresentmentTtum> processStpCardsMasterCardPosIssLatePresentmentTtum(
			String batchid, String status) {

		return stpCardsMasterCardPosIssLatePresentmentTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsMasterCardPosIssDropTtum> processStpCardsMasterCardPosIssDropTtum(String batchid,
			String status) {

		return stpCardsMasterCardPosIssDropTtumRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsMasterCardInternationSettlement> processStpCardsMasterCardInternationSettlement(String batchid,
			String status) {

		return stpCardsMasterCardInternationSettlementRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsMasterCardDomIssSurchargeDebit> processStpCardsMasterCardDomIssSurchargeDebit(String batchid,
			String status) {

		return stpCardsMasterCardDomIssSurchargeDebitRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<StpCardsMasterCardDomIssSurchargeCredit> processStpCardsMasterCardDomIssSurchargeCredit(String batchid,
			String status) {

		return stpCardsMasterCardDomIssSurchargeCreditRepository.findByBatchidAndStatus(batchid, status);
	}

	@Override
	public List<IsoV93Message> getVisa(String tableName, String requestType, String batchId, String status) {
		List<IsoV93Message> request1200 = null;
		if (tableName.equalsIgnoreCase("stp_cards_visa_iss_dr_surch_ttum")) {
			List<StpCardsVisaIssSurchargeDebit> requestList = processStpCardsVisaIssSurchargeDebit(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_visa_iss_cr_surch_ttum")) {
			List<StpCardsVisaIssSurchargeCredit> requestList = processStpCardsVisaIssSurchargeCredit(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_visa_iss_proactive_ttum")) {
			List<StpCardsVisaIssProactiveTtum> requestList = processStpCardsVisaIssProactiveTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_visa_iss_org_wdl_ttum")) {
			List<StpCardsVisaIssOrgWdlTtum> requestList = processStpCardsVisaIssOrgWdlTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_visa_iss_late_presentment_ttum")) {
			List<StpCardsVisaIssLatePresentmentTtum> requestList = processStpCardsVisaIssLatePresentmentTtum(batchId,
					status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_visa_iss_drop_ttum")) {
			List<StpCardsVisaIssDropTtum> requestList = processStpCardsVisaIssDropTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_visa_dom_iss_settlement_report")) {
			List<StpCardsVisaIssDomSettlementTtum> requestList = processStpCardsVisaIssDomSettlementTtum(batchId,
					status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		}
		return request1200;
	}

	@Override
	public List<IsoV93Message> getRupay(String tableName, String requestType, String batchId, String status) {
		List<IsoV93Message> request1200 = null;
		if (tableName.equalsIgnoreCase("stp_cards_rupay_proactiv_ttum")) {
			List<StpCardsRupayProactivTtum> requestList = processStpCardsRupayProactivTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_pos_dom_settlement")) {
			List<StpCardsRupayPosDomSettlement> requestList = processStpCardsRupayPosDomSettlement(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		}

		else if (tableName.equalsIgnoreCase("stp_cards_rupay_dom_surcharge_debit")) {
			List<StpCardsRupayDomSurchargeDebit> requestList = processStpCardsRupayDomSurchargeDebit(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_dom_surcharge_credit")) {
			List<StpCardsRupayDomSurchargeCredit> requestList = processStpCardsRupayDomSurchargeCredit(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_dom_latepresentment")) {
			List<StpCardsRupayDomLatePresentment> requestList = processStpCardsRupayDomLatePresentment(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_dom_adjustment_ttum")) {
			List<StpCardsRupayDomAdjustmentTtum> requestList = processStpCardsRupayDomAdjustmentTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_decl_ttum")) {
			List<StpCardsRupayDeclineTtum> requestList = processStpCardsRupayDeclineTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		}
		return request1200;
	}

	@Override
	public List<IsoV93Message> getNfs(String tableName, String requestType, String batchId, String status) {
		List<IsoV93Message> request1200 = null;
		if (tableName.equalsIgnoreCase("stp_cards_nfs_iss_recon_proactive_ttum")) {
			List<StpCardsNfsIssReconTtumProactive> requestList = processStpCardsNfsIssReconTtumProactive(batchId,
					status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_nfs_iss_recon_late_rvsrl_ttum")) {
			List<StpCardsNfsIssReconTtumLateReversal> requestList = processStpCardsNfsIssReconTtumLateReversal(batchId,
					status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_nfs_iss_recon_drop_ttum")) {
			List<StpCardsNfsIssReconTtumDrop> requestList = processStpCardsNfsIssReconTtumDrop(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_nfs_acq_recon_dr_ttum")) {
			List<StpCardsNfsAcqDebitReconTtum> requestList = processStpCardsNfsAcqDebitReconTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_nfs_acq_recon_cr_ttum")) {
			List<StpCardsNfsAcqCreditReconTtum> requestList = processStpCardsNfsAcqCreditReconTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		}
		return request1200;
	}

	@Override
	public List<IsoV93Message> getMasterCard(String tableName, String requestType, String batchId, String status) {
		List<IsoV93Message> request1200 = null;
		if (tableName.equalsIgnoreCase("stp_cards_mc_settlement_ttum")) {
			List<StpCardsMasterCardSettlement> requestList = processStpCardsMasterCardSettlement(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_mc_pos_proactive_ttum")) {
			List<StpCardsMasterCardPosProactiveTtum> requestList = processStpCardsMasterCardPosProactiveTtum(batchId,
					status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_mc_pos_iss_latepres_ttum")) {
			List<StpCardsMasterCardPosIssLatePresentmentTtum> requestList = processStpCardsMasterCardPosIssLatePresentmentTtum(
					batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_mc_iss_pos_drop_ttum")) {
			List<StpCardsMasterCardPosIssDropTtum> requestList = processStpCardsMasterCardPosIssDropTtum(batchId,
					status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_mc_int_settlement_ttum")) {
			List<StpCardsMasterCardInternationSettlement> requestList = processStpCardsMasterCardInternationSettlement(
					batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_mc_dom_iss_dr_surch")) {
			List<StpCardsMasterCardDomIssSurchargeDebit> requestList = processStpCardsMasterCardDomIssSurchargeDebit(
					batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_mc_dom_iss_cr_surch")) {
			List<StpCardsMasterCardDomIssSurchargeCredit> requestList = processStpCardsMasterCardDomIssSurchargeCredit(
					batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		}
		return request1200;
	}

	@Override
	public List<IsoV93Message> getRupayInternation(String tableName, String requestType, String batchId,
			String status) {
		List<IsoV93Message> request1200 = null;
		if (tableName.equalsIgnoreCase("stp_cards_rupay_int_dr_surch_ttum")) {
			List<StpCardsRupayIntSurchargeDebit> requestList = processStpCardsRupayIntSurchargeDebit(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_int_cr_surch_ttum")) {
			List<StpCardsRupayIntSurchargeCredit> requestList = processStpCardsRupayIntSurchargeCredit(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_int_proactiv_ttum")) {
			List<StpCardsRupayIntProactiveTtum> requestList = processStpCardsRupayIntProactiveTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_int_pos_ttum")) {
			List<StpCardsRupayIntPosSettlementTtum> requestList = processStpCardsRupayIntPosSettlementTtum(batchId,
					status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_int_ofline_pres_ttum")) {
			List<StpCardsRupayIntOfflinePresentmentTtum> requestList = processStpCardsRupayIntOfflinePresentmentTtum(
					batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_int_latepresentment")) {
			List<StpCardsRupayIntLatepresentmentTtum> requestList = processStpCardsRupayIntLatepresentmentTtum(batchId,
					status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_int_drop_ttum")) {
			List<StpCardsRupayIntDropTtum> requestList = processStpCardsRupayIntDropTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_int_decl_ttum")) {
			List<StpCardsRupayIntDeclineTtum> requestList = processStpCardsRupayIntDeclineTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		} else if (tableName.equalsIgnoreCase("stp_cards_rupay_int_adjustment_ttum")) {
			List<StpCardsRupayIntAdjustmentTtum> requestList = processStpCardsRupayIntAdjustmentTtum(batchId, status);
			request1200 = IsoPackagerGlobal.createisoFormat(requestList, requestType);
		}
		return request1200;
	}
}
