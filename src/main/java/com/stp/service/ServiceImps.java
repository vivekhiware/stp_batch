package com.stp.service;

import java.util.List;

import com.iso.config.IsoV93Message;
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

public interface ServiceImps {

	public List<IsoV93Message> getProcesRequest(String tableName, String requestType, String batchId, String status);

	List<StpImpsNoncbsIw> processStpImpsNoncbsIw(String batchid, String status);

	List<StpImpsNoncbsOw> processStpImpsNoncbsOw(String batchid, String status);

	List<StpImpsNonNpciIw> processStpImpsNonNpciIw(String batchid, String status);

	List<StpImpsNonNpciOw> processStpImpsNonNpciOw(String batchid, String status);

	List<StpImpsNetworkDeclineIw> processStpImpsNetworkDeclineIw(String batchid, String status);

	List<StpImpsNetworkDeclineOw> processStpImpsNetworkDeclineOw(String batchid, String status);

	List<StpImpsNtslNetSetTtum> processStpImpsNtslNetSetTtum(String batchid, String status);

	List<StpImpsPrearbitrationReport> processStpImpsPrearbitrationReport(String batchid, String status);

	List<StpImpsRccReport> processStpImpsRccReport(String batchid, String status);

	List<StpImpsRccRepraiseReport> processStpImpsRccRepraiseReport(String batchid, String status);

	List<StpImpsTccDataIw> processStpImpsTccDataIw(String batchid, String status);

	List<StpImpsTccDataIwRet> processStpImpsTccDataIwRet(String batchid, String status);

	List<StpImpsChargebackReport> processStpImpsChargebackReport(String batchid, String status);

	List<StpImpsCustomerComp> processStpImpsCustomerComp(String batchid, String status);

}
