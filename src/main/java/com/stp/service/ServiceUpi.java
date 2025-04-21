package com.stp.service;

import java.util.List;

import com.iso.config.IsoV93Message;
import com.stp.model.db1.StpUpiAdjustmentReport;
import com.stp.model.db1.StpUpiDrcNpci;
import com.stp.model.db1.StpUpiMultiReversal;
import com.stp.model.db1.StpUpiNonCbsIw;
import com.stp.model.db1.StpUpiNonCbsOw;
import com.stp.model.db1.StpUpiNtlsNetSetTtum;
import com.stp.model.db1.StpUpiRetData;
import com.stp.model.db1.StpUpiTccData;

public interface ServiceUpi {

	public List<IsoV93Message> getProcesRequest(String tableName, String requestType, String batchId, String status);

	List<StpUpiTccData> processStpUpiTccData(String batchid, String status);

	List<StpUpiRetData> processStpUpiRetData(String batchid, String status);

	List<StpUpiNtlsNetSetTtum> processStpUpiNtlsNetSetTtum(String batchid, String status);

	List<StpUpiNonCbsOw> processStpUpiNonCbsOw(String batchid, String status);

	List<StpUpiNonCbsIw> processStpUpiNonCbsIw(String batchid, String status);

	List<StpUpiMultiReversal> processStpUpiMultiReversal(String batchid, String status);

	List<StpUpiDrcNpci> processStpUpiDrcNpci(String batchid, String status);
	
	List<StpUpiAdjustmentReport> processStpUpiAdjustmentReport(String batchid, String status);

}
