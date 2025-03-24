package com.stp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.stp.service.DashBoardService;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	@Autowired
	@Qualifier("db1EntityManager")
	private EntityManager entityManager;

	@Override
	public List<Object[]> getDashBoard() {
		StringBuilder str = new StringBuilder();
		str.append(
				"select 'STP_IMPS_NONCBS_IW' name  , count(case when  status=\"L1\"  then 1 end )as L1,count(case when  status=\"L2\"  then 1 end )as L2,count(case when  status=\"L3\"  then 1 end )as L3, count(case when  status=\"L4\"  then 1 end )as L4 ,count(case when  status=\"L5\"  then 1 end )as L5,count(case when  status=\"L6\"  then 1 end )as L6 ,count(case when  status=\"L7\"  then 1 end )as L7, count(case when  status=\"L10\"  then 1 end )as L10 ,count(case when  status=\"R1\"  then 1 end )as R1,count(case when  status=\"R2\"  then 1 end )as R2 from  stp_imps_noncbs_iw ");
		str.append("  union all ");
		str.append(
				"select 'STP_IMPS_NONNPCI_IW' name  , count(case when  status=\"L1\"  then 1 end )as L1,count(case when  status=\"L2\"  then 1 end )as L2,count(case when  status=\"L3\"  then 1 end )as L3, count(case when  status=\"L4\"  then 1 end )as L4 ,count(case when  status=\"L5\"  then 1 end )as L5,count(case when  status=\"L6\"  then 1 end )as L6 ,count(case when  status=\"L7\"  then 1 end )as L7,  count(case when  status=\"L10\"  then 1 end )as L10 ,count(case when  status=\"R1\"  then 1 end )as R1,count(case when  status=\"R2\"  then 1 end )as R2 from  stp_imps_nonnpci_iw ");
		str.append("  union all ");
		str.append(
				"select 'STP_IMPS_NONNPCI_OW' name  , count(case when  status=\"L1\"  then 1 end )as L1,count(case when  status=\"L2\"  then 1 end )as L2,count(case when  status=\"L3\"  then 1 end )as L3, count(case when  status=\"L4\"  then 1 end )as L4 ,count(case when  status=\"L5\"  then 1 end )as L5,count(case when  status=\"L6\"  then 1 end )as L6 ,count(case when  status=\"L7\"  then 1 end )as L7,  count(case when  status=\"L10\"  then 1 end )as L10 ,count(case when  status=\"R1\"  then 1 end )as R1,count(case when  status=\"R2\"  then 1 end )as R2 from  stp_imps_nonnpci_ow ");
		str.append("  union all ");
		str.append(
				"select 'STP_IMPS_TCC_DATA_IW' name  , count(case when  status=\"L1\"  then 1 end )as L1,count(case when  status=\"L2\"  then 1 end )as L2,count(case when  status=\"L3\"  then 1 end )as L3, count(case when  status=\"L4\"  then 1 end )as L4 ,count(case when  status=\"L5\"  then 1 end )as L5,count(case when  status=\"L6\"  then 1 end )as L6 ,count(case when  status=\"L7\"  then 1 end )as L7,  count(case when  status=\"L10\"  then 1 end )as L10 ,count(case when  status=\"R1\"  then 1 end )as R1,count(case when  status=\"R2\"  then 1 end )as R2 from  stp_imps_tcc_data_iw ");
		str.append("  union all ");
		str.append(
				"select 'STP_IMPS_IW_NETWORK_DECLINE' name  , count(case when  status=\"L1\"  then 1 end )as L1,count(case when  status=\"L2\"  then 1 end )as L2,count(case when  status=\"L3\"  then 1 end )as L3, count(case when  status=\"L4\"  then 1 end )as L4 ,count(case when  status=\"L5\"  then 1 end )as L5,count(case when  status=\"L6\"  then 1 end )as L6 ,count(case when  status=\"L7\"  then 1 end )as L7,  count(case when  status=\"L10\"  then 1 end )as L10 ,count(case when  status=\"R1\"  then 1 end )as R1,count(case when  status=\"R2\"  then 1 end )as R2 from  stp_imps_iw_network_decline ");
		str.append("  union all ");
		str.append(
				"select 'STP_IMPS_OW_NETWORK_DECLINE' name  , count(case when  status=\"L1\"  then 1 end )as L1,count(case when  status=\"L2\"  then 1 end )as L2,count(case when  status=\"L3\"  then 1 end )as L3, count(case when  status=\"L4\"  then 1 end )as L4 ,count(case when  status=\"L5\"  then 1 end )as L5,count(case when  status=\"L6\"  then 1 end )as L6 ,count(case when  status=\"L7\"  then 1 end )as L7,  count(case when  status=\"L10\"  then 1 end )as L10 ,count(case when  status=\"R1\"  then 1 end )as R1,count(case when  status=\"R2\"  then 1 end )as R2 from  stp_imps_ow_network_decline ");
		str.append("  union all ");
		str.append(
				"select 'STP_IMPS_NONCBS_OW' name  , count(case when  status=\"L1\"  then 1 end )as L1,count(case when  status=\"L2\"  then 1 end )as L2,count(case when  status=\"L3\"  then 1 end )as L3, count(case when  status=\"L4\"  then 1 end )as L4 ,count(case when  status=\"L5\"  then 1 end )as L5,count(case when  status=\"L6\"  then 1 end )as L6 , count(case when  status=\"L7\"  then 1 end )as L7, count(case when  status=\"L10\"  then 1 end )as L10 ,count(case when  status=\"R1\"  then 1 end )as R1,count(case when  status=\"R2\"  then 1 end )as R2 from  stp_imps_noncbs_ow ");
		str.append("  union all ");
		str.append(
				"select 'STP_IMPS_NTSL_NETSET_TTUM' name  , count(case when  status=\"L1\"  then 1 end )as L1,count(case when  status=\"L2\"  then 1 end )as L2,count(case when  status=\"L3\"  then 1 end )as L3, count(case when  status=\"L4\"  then 1 end )as L4 ,count(case when  status=\"L5\"  then 1 end )as L5,count(case when  status=\"L6\"  then 1 end )as L6 ,count(case when  status=\"L7\"  then 1 end )as L7,  count(case when  status=\"L10\"  then 1 end )as L10 ,count(case when  status=\"R1\"  then 1 end )as R1,count(case when  status=\"R2\"  then 1 end )as R2 from  stp_imps_ntsl_netset_ttum ");

		Query nativeQuery = entityManager.createNativeQuery(str.toString());
		return nativeQuery.getResultList();
	}

}
