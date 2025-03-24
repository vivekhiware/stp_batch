package com.stp.model.db1;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

@DynamicUpdate
@Entity
@Table(name = "stp_imps", indexes = { @Index(columnList = "accountnumber,valuedate", name = "stp_imps_idx") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class STP_IMPS {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "stp_impsSeq")
	@SequenceGenerator(name = "stp_impsSeq", sequenceName = "stp_impsSeq", allocationSize = 1, initialValue = 1)
	private int id;
	@Column(name = "accountnumber", length = 255)
	private String accountnumber;
	@Column(name = "currencycode", length = 50)
	private String currencycode;
	@Column(name = "parttrantype", length = 255)
	private String parttrantype;
	@Column(name = "refamount", length = 255)
	private String refamount;
	@Column(name = "refcurrencycode", length = 255)
	private String refcurrencycode;
	@Column(name = "refnumber", length = 255)
	private String refnumber;
	@Column(name = "remarks", length = 255)
	private String remarks;
	@Column(name = "serviceoutlet", length = 255)
	private String serviceoutlet;
	@Column(name = "settlementdate", length = 255)
	private String settlementdate;
	@Column(name = "txnamount", length = 255)
	private String txnamount;
	@Column(name = "txnparticular", length = 255)
	private String txnparticular;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "valuedate")
	private Date valuedate;

//  new column added for stp process

//  new column added for stp process
	@Column(name = "makerid", length = 20)
	private String makerid;

	@Column(name = "checkerid", length = 20)
	private String checkerid;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "makerdate")
	@Temporal(TemporalType.DATE)
	private Date makerdate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "checkerdate")
	@Temporal(TemporalType.DATE)
	private Date checkerdate;

	@Column(name = "makerremarks", length = 255)
	private String makerremarks;

	@Column(name = "checkerremarks", length = 255)
	private String checkerremarks;

	private String bnkmakerid;
	private String bnkmakerdate;
	@Column(name = "bnkmakerremarks", length = 255)
	private String bnkmakerremarks;

	private String bnkcheckerid;
	private String bnkcheckerdate;
	@Column(name = "bnkcheckerremarks", length = 255)
	private String bnkcheckerremarks;

//	  for api approval success
	@Column(name = "approveid", length = 20)
	private String approveid;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "approvedate")
	@Temporal(TemporalType.DATE)
	private Date approvedate;

	private String status;

	// new column for stp process end
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}

	public String getParttrantype() {
		return parttrantype;
	}

	public void setParttrantype(String parttrantype) {
		this.parttrantype = parttrantype;
	}

	public String getRefamount() {
		return refamount;
	}

	public void setRefamount(String refamount) {
		this.refamount = refamount;
	}

	public String getRefcurrencycode() {
		return refcurrencycode;
	}

	public void setRefcurrencycode(String refcurrencycode) {
		this.refcurrencycode = refcurrencycode;
	}

	public String getRefnumber() {
		return refnumber;
	}

	public void setRefnumber(String refnumber) {
		this.refnumber = refnumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getServiceoutlet() {
		return serviceoutlet;
	}

	public void setServiceoutlet(String serviceoutlet) {
		this.serviceoutlet = serviceoutlet;
	}

	public String getSettlementdate() {
		return settlementdate;
	}

	public void setSettlementdate(String settlementdate) {
		this.settlementdate = settlementdate;
	}

	public String getTxnamount() {
		return txnamount;
	}

	public void setTxnamount(String txnamount) {
		this.txnamount = txnamount;
	}

	public String getTxnparticular() {
		return txnparticular;
	}

	public void setTxnparticular(String txnparticular) {
		this.txnparticular = txnparticular;
	}

	public Date getValuedate() {
		return valuedate;
	}

	public void setValuedate(Date valuedate) {
		this.valuedate = valuedate;
	}

	public String getMakerid() {
		return makerid;
	}

	public void setMakerid(String makerid) {
		this.makerid = makerid;
	}

	public String getCheckerid() {
		return checkerid;
	}

	public void setCheckerid(String checkerid) {
		this.checkerid = checkerid;
	}

	public Date getMakerdate() {
		return makerdate;
	}

	public void setMakerdate(Date makerdate) {
		this.makerdate = makerdate;
	}

	public Date getCheckerdate() {
		return checkerdate;
	}

	public void setCheckerdate(Date checkerdate) {
		this.checkerdate = checkerdate;
	}

	public String getApproveid() {
		return approveid;
	}

	public void setApproveid(String approveid) {
		this.approveid = approveid;
	}

	public Date getApprovedate() {
		return approvedate;
	}

	public void setApprovedate(Date approvedate) {
		this.approvedate = approvedate;
	}

	public String getMakerremarks() {
		return makerremarks;
	}

	public void setMakerremarks(String makerremarks) {
		this.makerremarks = makerremarks;
	}

	public String getCheckerremarks() {
		return checkerremarks;
	}

	public void setCheckerremarks(String checkerremarks) {
		this.checkerremarks = checkerremarks;
	}

	public String getBnkmakerid() {
		return bnkmakerid;
	}

	public void setBnkmakerid(String bnkmakerid) {
		this.bnkmakerid = bnkmakerid;
	}

	public String getBnkmakerdate() {
		return bnkmakerdate;
	}

	public void setBnkmakerdate(String bnkmakerdate) {
		this.bnkmakerdate = bnkmakerdate;
	}

	public String getBnkmakerremarks() {
		return bnkmakerremarks;
	}

	public void setBnkmakerremarks(String bnkmakerremarks) {
		this.bnkmakerremarks = bnkmakerremarks;
	}

	public String getBnkcheckerid() {
		return bnkcheckerid;
	}

	public void setBnkcheckerid(String bnkcheckerid) {
		this.bnkcheckerid = bnkcheckerid;
	}

	public String getBnkcheckerdate() {
		return bnkcheckerdate;
	}

	public void setBnkcheckerdate(String bnkcheckerdate) {
		this.bnkcheckerdate = bnkcheckerdate;
	}

	public String getBnkcheckerremarks() {
		return bnkcheckerremarks;
	}

	public void setBnkcheckerremarks(String bnkcheckerremarks) {
		this.bnkcheckerremarks = bnkcheckerremarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
