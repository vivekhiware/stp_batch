package com.stp.service;

import java.util.ArrayList;
import java.util.List;

import com.iso.config.IsoV93MessageRes;
import com.stp.model.db1.STP_IMPS;
import com.stp.model.db1.STP_IMPS_CHARGEBACK_REPORT;
import com.stp.model.db1.STP_IMPS_CUSTOMER_COMP;
import com.stp.model.db1.STP_IMPS_IW_NETWORK_DECLINE;
import com.stp.model.db1.STP_IMPS_NONCBS_IW;
import com.stp.model.db1.STP_IMPS_NONCBS_OW;
import com.stp.model.db1.STP_IMPS_NONNPCI_IW;
import com.stp.model.db1.STP_IMPS_NONNPCI_OW;
import com.stp.model.db1.STP_IMPS_NTSL_NETSET_TTUM;
import com.stp.model.db1.STP_IMPS_OW_NETWORK_DECLINE;
import com.stp.model.db1.STP_IMPS_PREARBITRATION_REPORT;
import com.stp.model.db1.STP_IMPS_RCC_REPORT;
import com.stp.model.db1.STP_IMPS_RCC_REPORT_REPRAISE;
import com.stp.model.db1.STP_IMPS_TCC_DATA_IW;
import com.stp.model.db1.STP_IMPS_TCC_DATA_IW_RET;
import com.stp.utility.TTumRequest;

public interface ServiceImps {

	List<STP_IMPS> insertImpsRecord(List<STP_IMPS> stpImpsList);

	List<STP_IMPS_NONCBS_IW> addSTP_IMPS_NONCBS_IW(List<STP_IMPS_NONCBS_IW> stpImpsList);

	List<STP_IMPS_NONCBS_IW> viewSTP_IMPS_NONCBS_IW(TTumRequest accObject);

	List<STP_IMPS_NONCBS_IW> processIsoSTP_IMPS_NONCBS_IW(String status);

	public int ReqRespSTP_IMPS_NONCBS_IW(ArrayList<IsoV93MessageRes> reslist, String type);

	// STP_TCC_DATA_IW

	List<STP_IMPS_TCC_DATA_IW> addSTP_IMPS_TCC_DATA_IW(List<STP_IMPS_TCC_DATA_IW> stpImpsList);

	List<STP_IMPS_TCC_DATA_IW> viewSTP_IMPS_TCC_DATA_IW(TTumRequest accObject);

	List<STP_IMPS_TCC_DATA_IW> processSTP_IMPS_TCC_DATA_IW(String status);

	public int ReqRespSTP_IMPS_TCC_DATA_IW(ArrayList<IsoV93MessageRes> reslist, String type);

	// STP_IMPS_NONNPCI_IW

	List<STP_IMPS_NONNPCI_IW> addSTP_IMPS_NONNPCI_IW(List<STP_IMPS_NONNPCI_IW> stpImpsList);

	List<STP_IMPS_NONNPCI_IW> viewSTP_IMPS_NONNPCI_IW(TTumRequest accObject);

	List<STP_IMPS_NONNPCI_IW> processSTP_IMPS_NONNPCI_IW(String status);

	public int ReqRespSTP_IMPS_NONNPCI_IW(ArrayList<IsoV93MessageRes> reslist, String type);

	// STP_IMPS_NONNPCI_OW
	List<STP_IMPS_NONNPCI_OW> addSTP_IMPS_NONNPCI_OW(List<STP_IMPS_NONNPCI_OW> stpImpsList);

	List<STP_IMPS_NONNPCI_OW> viewSTP_IMPS_NONNPCI_OW(TTumRequest accObject);

	List<STP_IMPS_NONNPCI_OW> processSTP_IMPS_NONNPCI_OW(String status);

	public int ReqRespSTP_IMPS_NONNPCI_OW(ArrayList<IsoV93MessageRes> reslist, String type);

	// STP_IMPS_IW_NETWORK_DECLINE
	List<STP_IMPS_IW_NETWORK_DECLINE> addSTP_IMPS_IW_NETWORK_DECLINE(List<STP_IMPS_IW_NETWORK_DECLINE> stpImpsList);

	List<STP_IMPS_IW_NETWORK_DECLINE> viewSTP_IMPS_IW_NETWORK_DECLINE(TTumRequest accObject);

	List<STP_IMPS_IW_NETWORK_DECLINE> processSTP_IMPS_IW_NETWORK_DECLINE(String status);

	public int ReqRespSTP_IMPS_IW_NETWORK_DECLINE(ArrayList<IsoV93MessageRes> reslist, String type);

	// STP_IMPS_OW_NETWORK_DECLINE
	List<STP_IMPS_OW_NETWORK_DECLINE> addSTP_IMPS_OW_NETWORK_DECLINE(List<STP_IMPS_OW_NETWORK_DECLINE> stpImpsList);

	List<STP_IMPS_OW_NETWORK_DECLINE> viewSTP_IMPS_OW_NETWORK_DECLINE(TTumRequest accObject);

	List<STP_IMPS_OW_NETWORK_DECLINE> processSTP_IMPS_OW_NETWORK_DECLINE(String status);

	public int ReqRespSTP_IMPS_OW_NETWORK_DECLINE(ArrayList<IsoV93MessageRes> reslist, String type);

//	STP_IMPS_NTSL_NETSET_TTUM
	List<STP_IMPS_NTSL_NETSET_TTUM> addSTP_IMPS_NTSL_NETSET_TTUM(List<STP_IMPS_NTSL_NETSET_TTUM> stpImpsList);

	List<STP_IMPS_NTSL_NETSET_TTUM> viewSTP_IMPS_NTSL_NETSET_TTUM(TTumRequest accObject);

	List<STP_IMPS_NTSL_NETSET_TTUM> processSTP_IMPS_NTSL_NETSET_TTUM(String status);

	public int ReqRespSTP_IMPS_NTSL_NETSET_TTUM(ArrayList<IsoV93MessageRes> reslist, String type);

//	STP_IMPS_NONCBS_OW

	List<STP_IMPS_NONCBS_OW> addSTP_IMPS_NONCBS_OW(List<STP_IMPS_NONCBS_OW> stpImpsList);

	List<STP_IMPS_NONCBS_OW> viewSTP_IMPS_NONCBS_OW(TTumRequest accObject);

	List<STP_IMPS_NONCBS_OW> processSTP_IMPS_NONCBS_OW(String status);

	public int ReqRespSTP_IMPS_NONCBS_OW(ArrayList<IsoV93MessageRes> reslist, String type);

//	  new update testing 
	public int updateSTP_IMPS_NONCBS_IW(IsoV93MessageRes reslist, String type);

	public int updateSTP_IMPS_TCC_DATA_IW(IsoV93MessageRes reslist, String type);

	public int updateSTP_IMPS_NONNPCI_IW(IsoV93MessageRes reslist, String type);

	public int updateSTP_IMPS_NONNPCI_OW(IsoV93MessageRes reslist, String type);

	public int updateSTP_IMPS_IW_NETWORK_DECLINE(IsoV93MessageRes reslist, String type);

	public int updateSTP_IMPS_OW_NETWORK_DECLINE(IsoV93MessageRes reslist, String type);

	public int updateSTP_IMPS_NONCBS_OW(IsoV93MessageRes reslist, String type);

	public int updateSTP_IMPS_NTSL_NETSET_TTUM(IsoV93MessageRes reslist, String type);

	// STP_IMPS_CHARGEBACK_REPORT
	List<STP_IMPS_CHARGEBACK_REPORT> addSTP_IMPS_CHARGEBACK_REPORT(List<STP_IMPS_CHARGEBACK_REPORT> stpImpsList);

	List<STP_IMPS_CHARGEBACK_REPORT> viewSTP_IMPS_CHARGEBACK_REPORT(TTumRequest accObject);

	List<STP_IMPS_CHARGEBACK_REPORT> processSTP_IMPS_CHARGEBACK_REPORT(String status);

	public int updateSTP_IMPS_CHARGEBACK_REPORT(IsoV93MessageRes reslist, String type);

	// STP_IMPS_RCC_REPORT
	List<STP_IMPS_RCC_REPORT> addSTP_IMPS_RCC_REPORT(List<STP_IMPS_RCC_REPORT> stpImpsList);

	List<STP_IMPS_RCC_REPORT> viewSTP_IMPS_RCC_REPORT(TTumRequest accObject);

	List<STP_IMPS_RCC_REPORT> processSTP_IMPS_RCC_REPORT(String status);

	public int updateSTP_IMPS_RCC_REPORT(IsoV93MessageRes reslist, String type);

	// STP_IMPS_PREARBITRATION_REPORT
	List<STP_IMPS_PREARBITRATION_REPORT> addSTP_IMPS_PREARBITRATION_REPORT(
			List<STP_IMPS_PREARBITRATION_REPORT> stpImpsList);

	List<STP_IMPS_PREARBITRATION_REPORT> viewSTP_IMPS_PREARBITRATION_REPORT(TTumRequest accObject);

	List<STP_IMPS_PREARBITRATION_REPORT> processSTP_IMPS_PREARBITRATION_REPORT(String status);

	public int updateSTP_IMPS_PREARBITRATION_REPORT(IsoV93MessageRes reslist, String type);

//	STP_IMPS_CUSTOMER_COMP
	List<STP_IMPS_CUSTOMER_COMP> addSTP_IMPS_CUSTOMER_COMP(List<STP_IMPS_CUSTOMER_COMP> stpImpsList);

	List<STP_IMPS_CUSTOMER_COMP> viewSTP_IMPS_CUSTOMER_COMP(TTumRequest accObject);

	List<STP_IMPS_CUSTOMER_COMP> processSTP_IMPS_CUSTOMER_COMP(String status);

	public int updateSTP_IMPS_CUSTOMER_COMP(IsoV93MessageRes reslist, String type);

//	STP_IMPS_RCC_REPORT_REPRAISE
	List<STP_IMPS_RCC_REPORT_REPRAISE> addSTP_IMPS_RCC_REPORT_REPRAISE(List<STP_IMPS_RCC_REPORT_REPRAISE> stpImpsList);

	List<STP_IMPS_RCC_REPORT_REPRAISE> viewSTP_IMPS_RCC_REPORT_REPRAISE(TTumRequest accObject);

	List<STP_IMPS_RCC_REPORT_REPRAISE> processSTP_IMPS_RCC_REPORT_REPRAISE(String status);

	public int updateSTP_IMPS_RCC_REPORT_REPRAISE(IsoV93MessageRes reslist, String type);

	// STP_TCC_DATA_IW_RET
	List<STP_IMPS_TCC_DATA_IW_RET> addSTP_IMPS_TCC_DATA_IW_RET(List<STP_IMPS_TCC_DATA_IW_RET> stpImpsList);

	List<STP_IMPS_TCC_DATA_IW_RET> viewSTP_IMPS_TCC_DATA_IW_RET(TTumRequest accObject);

	List<STP_IMPS_TCC_DATA_IW_RET> processSTP_IMPS_TCC_DATA_IW_RET(String status);

	public int updateSTP_IMPS_TCC_DATA_IW_RET(IsoV93MessageRes reslist, String type);
}
