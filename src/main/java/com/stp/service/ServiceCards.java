package com.stp.service;

import java.util.ArrayList;
import java.util.List;

import com.iso.config.IsoV93MessageRes;
import com.stp.model.db1.STP_CARDS_NFS_ISS_RECON_TTUM;
import com.stp.utility.TTumRequest;

public interface ServiceCards {
	// STP_CARDS_NFS_ISS_RECON_TTUM
	List<STP_CARDS_NFS_ISS_RECON_TTUM> addNFS_ISS_RECON_TTUM(List<STP_CARDS_NFS_ISS_RECON_TTUM> stpcardsList);

	List<STP_CARDS_NFS_ISS_RECON_TTUM> viewNFS_ISS_RECON_TTUM(TTumRequest accObject);

	List<STP_CARDS_NFS_ISS_RECON_TTUM> processNFS_ISS_RECON_TTUM(String status);

	public int ReqRespSTP_CARDS_NFS_ISS_RECON_TTUM(ArrayList<IsoV93MessageRes> reslist, String type);

	public int updateSTP_CARDS_NFS_ISS_RECON_TTUM(IsoV93MessageRes res, String type);
}
