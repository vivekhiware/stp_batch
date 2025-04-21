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
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author int6346 vivek
 */
@DynamicUpdate
@Entity
@Table(name = "stp_application")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class StpAccess implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name", length = 50)
	private String appname;
	@Column(name = "banktype", length = 50)
	private String banktype;
	@JsonIgnore
	@Column(name = "url", length = 50)
	private String url;
	@Column(name = "status", length = 2)
	private String status = "A";
	private int entryby;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "entrydate")
	@Temporal(TemporalType.DATE)
	private Date entrydate;

	private int removeby;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "removedate")
	@Temporal(TemporalType.DATE)
	private Date removedate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getBanktype() {
		return banktype;
	}

	public void setBanktype(String banktype) {
		this.banktype = banktype;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public int getRemoveby() {
		return removeby;
	}

	public void setRemoveby(int removeby) {
		this.removeby = removeby;
	}

	public Date getRemovedate() {
		return removedate;
	}

	public void setRemovedate(Date removedate) {
		this.removedate = removedate;
	}

}
