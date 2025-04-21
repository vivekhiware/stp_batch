package com.stp.model.db1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "stp_upi_multireversal", indexes = {
		@Index(columnList = "valuedate,refnumber,batchid", name = "stp_upi_multireversal_idx") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StpUpiMultiReversal extends CommonColumns {

	@Column(name = "txnid", length = 50)
	private String txnid;

	public String getTxnid() {
		return txnid;
	}

	public void setTxnid(String txnid) {
		this.txnid = txnid;
	}
}
