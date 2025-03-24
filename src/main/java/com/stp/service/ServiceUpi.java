package com.stp.service;

import java.util.ArrayList;
import java.util.List;

import com.iso.config.IsoV93MessageRes;
import com.stp.model.db1.STP_UPI;
import com.stp.model.db1.STP_UPI_ADJUSTMENT_REPORT;
import com.stp.model.db1.STP_UPI_DRC_NPCI;
import com.stp.model.db1.STP_UPI_MULTIREVERSAL;
import com.stp.model.db1.STP_UPI_NONCBS_IW;
import com.stp.model.db1.STP_UPI_NONCBS_OW;
import com.stp.model.db1.STP_UPI_NSTL_NETSET_TTUM;
import com.stp.model.db1.STP_UPI_RET_DATA;
import com.stp.model.db1.STP_UPI_TCC_DATA;
import com.stp.utility.TTumRequest;

public interface ServiceUpi {

	List<STP_UPI> insertUpiRecord(List<STP_UPI> stpUpiList);

	List<STP_UPI> globalUPIDetail(TTumRequest accObject);

	// STP_UPI_ADJUSTMENT_REPORT table
	List<STP_UPI_NSTL_NETSET_TTUM> addSTP_UPI_NSTL_NETSET_TTUM(List<STP_UPI_NSTL_NETSET_TTUM> stpUpiList);

	List<STP_UPI_NSTL_NETSET_TTUM> viewSTP_UPI_NSTL_NETSET_TTUM(TTumRequest accObject);

	List<STP_UPI_NSTL_NETSET_TTUM> processSTP_UPI_NSTL_NETSET_TTUM(String status);

	public int ReqRespSTP_UPI_NSTL_NETSET_TTUM(ArrayList<IsoV93MessageRes> reslist, String type);

	// STP_UPI_ADJUSTMENT_REPORT table
	List<STP_UPI_ADJUSTMENT_REPORT> addSTP_UPI_ADJUSTMENT_REPORT(List<STP_UPI_ADJUSTMENT_REPORT> stpImpsList);

	List<STP_UPI_ADJUSTMENT_REPORT> viewSTP_UPI_ADJUSTMENT_REPORT(TTumRequest accObject);

	List<STP_UPI_ADJUSTMENT_REPORT> processSTP_UPI_ADJUSTMENT_REPORT(String status);

	public int ReqRespSTP_UPI_ADJUSTMENT_REPORT(ArrayList<IsoV93MessageRes> reslist, String type);

//	STP_UPI_MULTIREVERSAL
	List<STP_UPI_MULTIREVERSAL> addSTP_UPI_MULTIREVERSAL(List<STP_UPI_MULTIREVERSAL> stpImpsList);

	List<STP_UPI_MULTIREVERSAL> viewSTP_UPI_MULTIREVERSAL(TTumRequest accObject);

	List<STP_UPI_MULTIREVERSAL> processSTP_UPI_MULTIREVERSAL(String status);

	public int ReqRespSTP_UPI_MULTIREVERSAL(ArrayList<IsoV93MessageRes> reslist, String type);

//	STP_UPI_TCC_DATA
	List<STP_UPI_TCC_DATA> addSTP_UPI_TCC_DATA(List<STP_UPI_TCC_DATA> stpImpsList);

	List<STP_UPI_TCC_DATA> viewSTP_UPI_TCC_DATA(TTumRequest accObject);

	List<STP_UPI_TCC_DATA> processSTP_UPI_TCC_DATA(String status);

	public int ReqRespSTP_UPI_TCC_DATA(ArrayList<IsoV93MessageRes> reslist, String type);

//	STP_UPI_RET_DATA
	List<STP_UPI_RET_DATA> addSTP_UPI_RET_DATA(List<STP_UPI_RET_DATA> stpImpsList);

	List<STP_UPI_RET_DATA> viewSTP_UPI_RET_DATA(TTumRequest accObject);

	List<STP_UPI_RET_DATA> processSTP_UPI_RET_DATA(String status);

	public int ReqRespSTP_UPI_RET_DATA(ArrayList<IsoV93MessageRes> reslist, String type);

//	STP_UPI_DRC_NPCI
	List<STP_UPI_DRC_NPCI> addSTP_UPI_DRC_NPCI(List<STP_UPI_DRC_NPCI> stpImpsList);

	List<STP_UPI_DRC_NPCI> viewSTP_UPI_DRC_NPCI(TTumRequest accObject);

	List<STP_UPI_DRC_NPCI> processSTP_UPI_DRC_NPCI(String status);

	public int ReqRespSTP_UPI_DRC_NPCI(ArrayList<IsoV93MessageRes> reslist, String type);

//	STP_UPI_NONCBS_IW
	List<STP_UPI_NONCBS_IW> addSTP_UPI_NONCBS_IW(List<STP_UPI_NONCBS_IW> stpImpsList);

	List<STP_UPI_NONCBS_IW> viewSTP_UPI_NONCBS_IW(TTumRequest accObject);

	List<STP_UPI_NONCBS_IW> processSTP_UPI_NONCBS_IW(String status);

	public int ReqRespSTP_UPI_NONCBS_IW(ArrayList<IsoV93MessageRes> reslist, String type);

//	STP_UPI_NONCBS_OW
	List<STP_UPI_NONCBS_OW> addSTP_UPI_NONCBS_OW(List<STP_UPI_NONCBS_OW> stpImpsList);

	List<STP_UPI_NONCBS_OW> viewSTP_UPI_NONCBS_OW(TTumRequest accObject);

	List<STP_UPI_NONCBS_OW> processSTP_UPI_NONCBS_OW(String status);

	public int ReqRespSTP_UPI_NONCBS_OW(ArrayList<IsoV93MessageRes> reslist, String type);

	public int updateSTP_UPI_NSTL_NETSET_TTUM(IsoV93MessageRes res, String type);

	public int updateSTP_UPI_ADJUSTMENT_REPORT(IsoV93MessageRes res, String type);

	public int updateSTP_UPI_MULTIREVERSAL(IsoV93MessageRes res, String type);

	public int updateSTP_UPI_TCC_DATA(IsoV93MessageRes res, String type);

	public int updateSTP_UPI_RET_DATA(IsoV93MessageRes res, String type);

	public int updateSTP_UPI_DRC_NPCI(IsoV93MessageRes res, String type);

	public int updateSTP_UPI_NONCBS_IW(IsoV93MessageRes res, String type);
	
	public int updateSTP_UPI_NONCBS_OW(IsoV93MessageRes res, String type);
}
