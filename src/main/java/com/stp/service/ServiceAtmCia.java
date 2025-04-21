package com.stp.service;

import java.util.List;

import com.iso.config.IsoV93Message;
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

public interface ServiceAtmCia {

	public List<IsoV93Message> getProcesRequest(String tableName, String requestType, String batchId, String status);

	public List<StpAtmCiaBnaDepProactive> processStpAtmCiaBnaDepProactive(String batchid, String status);

	public List<StpAtmCiaMainCbsEj> processStpAtmCiaMainCbsEj(String batchid, String status);

	public List<StpAtmCiaMainCbsEjRrbFailed> processStpAtmCiaMainCbsEjRrbFailed(String batchid, String status);

	public List<StpAtmCiaMainProactive> processStpAtmCiaMainProactive(String batchid, String status);
	
	public List<StpAtmCiaOnusCmpl> processStpAtmCiaOnusCmpl(String batchid, String status);
	
	public List<StpAtmCiaOnusErrorCmpl> processStpAtmCiaOnusErrorCmpl(String batchid, String status);
	
	public List<StpAtmCiaOnusOvgRecl> processStpAtmCiaOnusOvgRecl(String batchid, String status);
	
	public List<StpAtmCiaOnusPenalty> processStpAtmCiaOnusPenalty(String batchid, String status);
	
	public List<StpAtmCiaOnusSrlMsdCmpl> processStpAtmCiaOnusSrlMsdCmpl(String batchid, String status);
	
	public List<StpAtmCiaOnusTxnLevCmpl> processStpAtmCiaOnusTxnLevCmpl(String batchid, String status);
	
	public List<StpAtmCiaReconBulkRecl> processStpAtmCiaReconBulkRecl(String batchid, String status);

}
