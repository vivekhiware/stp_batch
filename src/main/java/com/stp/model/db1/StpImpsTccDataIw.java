package com.stp.model.db1;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "stp_imps_tcc_data_iw", indexes = {
		@Index(columnList = "valuedate,refnumber,batchid", name = "stp_imps_tcc_data_iw_idx") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StpImpsTccDataIw extends CommonColumns {

}
