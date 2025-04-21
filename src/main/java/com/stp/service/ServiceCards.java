package com.stp.service;

import java.util.List;

import com.iso.config.IsoV93Message;
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

public interface ServiceCards {

	public List<IsoV93Message> getProcesRequest(String tableName, String requestType, String batchId, String status);

	public List<IsoV93Message> getVisa(String tableName, String requestType, String batchId, String status);

	public List<IsoV93Message> getRupay(String tableName, String requestType, String batchId, String status);

	public List<IsoV93Message> getRupayInternation(String tableName, String requestType, String batchId, String status);

	public List<IsoV93Message> getNfs(String tableName, String requestType, String batchId, String status);

	public List<IsoV93Message> getMasterCard(String tableName, String requestType, String batchId, String status);

	List<StpCardsVisaIssSurchargeDebit> processStpCardsVisaIssSurchargeDebit(String batchid, String status);

	List<StpCardsVisaIssSurchargeCredit> processStpCardsVisaIssSurchargeCredit(String batchid, String status);

	List<StpCardsVisaIssProactiveTtum> processStpCardsVisaIssProactiveTtum(String batchid, String status);

	List<StpCardsVisaIssOrgWdlTtum> processStpCardsVisaIssOrgWdlTtum(String batchid, String status);

	List<StpCardsVisaIssLatePresentmentTtum> processStpCardsVisaIssLatePresentmentTtum(String batchid, String status);

	List<StpCardsVisaIssDropTtum> processStpCardsVisaIssDropTtum(String batchid, String status);

	List<StpCardsVisaIssDomSettlementTtum> processStpCardsVisaIssDomSettlementTtum(String batchid, String status);

	List<StpCardsRupayProactivTtum> processStpCardsRupayProactivTtum(String batchid, String status);

	List<StpCardsRupayPosDomSettlement> processStpCardsRupayPosDomSettlement(String batchid, String status);

	List<StpCardsRupayIntSurchargeDebit> processStpCardsRupayIntSurchargeDebit(String batchid, String status);

	List<StpCardsRupayIntSurchargeCredit> processStpCardsRupayIntSurchargeCredit(String batchid, String status);

	List<StpCardsRupayIntProactiveTtum> processStpCardsRupayIntProactiveTtum(String batchid, String status);

	List<StpCardsRupayIntPosSettlementTtum> processStpCardsRupayIntPosSettlementTtum(String batchid, String status);

	List<StpCardsRupayIntOfflinePresentmentTtum> processStpCardsRupayIntOfflinePresentmentTtum(String batchid,
			String status);

	List<StpCardsRupayIntLatepresentmentTtum> processStpCardsRupayIntLatepresentmentTtum(String batchid, String status);

	List<StpCardsRupayIntDropTtum> processStpCardsRupayIntDropTtum(String batchid, String status);

	List<StpCardsRupayIntDeclineTtum> processStpCardsRupayIntDeclineTtum(String batchid, String status);

	List<StpCardsRupayIntAdjustmentTtum> processStpCardsRupayIntAdjustmentTtum(String batchid, String status);

	List<StpCardsRupayDomSurchargeDebit> processStpCardsRupayDomSurchargeDebit(String batchid, String status);

	List<StpCardsRupayDomSurchargeCredit> processStpCardsRupayDomSurchargeCredit(String batchid, String status);

	List<StpCardsRupayDomLatePresentment> processStpCardsRupayDomLatePresentment(String batchid, String status);

	List<StpCardsRupayDomAdjustmentTtum> processStpCardsRupayDomAdjustmentTtum(String batchid, String status);

	List<StpCardsRupayDeclineTtum> processStpCardsRupayDeclineTtum(String batchid, String status);

	List<StpCardsNfsIssReconTtumProactive> processStpCardsNfsIssReconTtumProactive(String batchid, String status);

	List<StpCardsNfsIssReconTtumLateReversal> processStpCardsNfsIssReconTtumLateReversal(String batchid, String status);

	List<StpCardsNfsIssReconTtumDrop> processStpCardsNfsIssReconTtumDrop(String batchid, String status);

	List<StpCardsNfsAcqDebitReconTtum> processStpCardsNfsAcqDebitReconTtum(String batchid, String status);

	List<StpCardsNfsAcqCreditReconTtum> processStpCardsNfsAcqCreditReconTtum(String batchid, String status);

	List<StpCardsMasterCardSettlement> processStpCardsMasterCardSettlement(String batchid, String status);

	List<StpCardsMasterCardPosProactiveTtum> processStpCardsMasterCardPosProactiveTtum(String batchid, String status);

	List<StpCardsMasterCardPosIssLatePresentmentTtum> processStpCardsMasterCardPosIssLatePresentmentTtum(String batchid,
			String status);

	List<StpCardsMasterCardPosIssDropTtum> processStpCardsMasterCardPosIssDropTtum(String batchid, String status);

	List<StpCardsMasterCardInternationSettlement> processStpCardsMasterCardInternationSettlement(String batchid,
			String status);

	List<StpCardsMasterCardDomIssSurchargeDebit> processStpCardsMasterCardDomIssSurchargeDebit(String batchid,
			String status);

	List<StpCardsMasterCardDomIssSurchargeCredit> processStpCardsMasterCardDomIssSurchargeCredit(String batchid,
			String status);
}
