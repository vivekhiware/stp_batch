package com.stp.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	public List<Object[]> getDashBoardDynamic(String type) {
		Map<String, String[]> tableMap = new HashMap<>();
		tableMap.put("IMPS",
				new String[] { "stp_imps_noncbs_iw", "stp_imps_noncbs_ow", "stp_imps_nonnpci_iw", "stp_imps_nonnpci_ow",
						"stp_imps_ntsl_netset_ttum", "stp_imps_prearbitration_report", "stp_imps_rcc_report",
						"stp_imps_rcc_repraise_report", "stp_imps_tcc_data_iw", "stp_imps_tcc_data_iw_ret" });
		tableMap.put("UPI", new String[] { "stp_upi_tcc_data", "stp_upi_ret_data" });
		tableMap.put("CARDS", new String[] { "stp_cards_mc_dom_iss_cr_surch", "stp_cards_mc_dom_iss_dr_surch" });
		tableMap.put("ATMCIA", new String[] { "stp_cards_mc_settlement_int_ttum", "stp_cards_mc_iss_pos_drop_ttum" });

		String[] statuses = { "L1", "L2", "L3", "L4", "L5", "L6", "L7", "L10", "R1", "R2" };

		String[] tables = tableMap.get(type.toUpperCase());
		if (tables == null) {
			throw new IllegalArgumentException("Unsupported type: " + type);
		}

		String statusColumns = Arrays.stream(statuses)
				.map(status -> String.format("count(case when status='%s' then 1 end) as %s", status, status))
				.collect(Collectors.joining(", "));

		String queryStr = Arrays.stream(tables)
				.map(table -> String.format("select '%s' as name, %s from %s", table, statusColumns, table))
				.collect(Collectors.joining(" union all "));

		Query query = entityManager.createNativeQuery(queryStr);
		return query.getResultList();
	}

}
