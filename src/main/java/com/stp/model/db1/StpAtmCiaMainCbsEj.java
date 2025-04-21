package com.stp.model.db1;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "stp_atmcia_main_cbs_ej", indexes = {
		@Index(columnList = "valuedate,refnumber,batchid", name = "stp_atmcia_main_cbs_ej_idx") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StpAtmCiaMainCbsEj extends CommonColumns {

}
