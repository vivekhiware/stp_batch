package com.stp.model.db1;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "stp_cards_rupay_pos_dom_settlement", indexes = {
		@Index(columnList = "valuedate,refnumber,batchid", name = "stp_cards_rupay_pos_dom_settlement_idx") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StpCardsRupayPosDomSettlement extends CommonColumns {

}
