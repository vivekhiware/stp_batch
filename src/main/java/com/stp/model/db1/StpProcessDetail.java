package com.stp.model.db1;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author int6346 vivek
 */
@DynamicUpdate
@Entity
@Table(name = "stp_process_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class StpProcessDetail implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "type", length = 50)
	private String type;

	@Column(name = "tablename", length = 50)
	private String tablename;

	@Column(name = "batchid", length = 50)
	private String batchid;

	@Column(name = "batchcount", length = 50)
	private String batchcount;

	@Column(name = "requesttype", length = 10)
	private String requesttype;

	private int entryby;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "entrydate")
	@Temporal(TemporalType.DATE)
	private Date entrydate = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public String getBatchcount() {
		return batchcount;
	}

	public void setBatchcount(String batchcount) {
		this.batchcount = batchcount;
	}

	public String getRequesttype() {
		return requesttype;
	}

	public void setRequesttype(String requesttype) {
		this.requesttype = requesttype;
	}

	public int getEntryby() {
		return entryby;
	}

	public void setEntryby(int entryby) {
		this.entryby = entryby;
	}

	public Date getEntrydate() {
		return entrydate;
	}

	public void setEntrydate(Date entrydate) {
		this.entrydate = entrydate;
	}

}
