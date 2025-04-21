package com.stp.model.db1;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "stp_cards_visa_dom_iss_settlement_report", indexes = {
		@Index(columnList = "valuedate,refnumber,batchid", name = "stp_cards_visa_dom_iss_settlement_report_idx") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StpCardsVisaIssDomSettlementTtum extends CommonColumns {

}
