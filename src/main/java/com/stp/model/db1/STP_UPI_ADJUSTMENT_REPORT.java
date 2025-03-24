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
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@DynamicUpdate
@Entity
@Table(name = "stp_upi_adjustment_report", indexes = {
		@Index(columnList = "refnumber,accountnumber,valuedate", name = "stp_upi_adjustment_report_idx") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class STP_UPI_ADJUSTMENT_REPORT {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "accountnumber", length = 20)
	private String accountnumber;
	@Column(name = "currencycode", length = 10)
	private String currencycode;
	@Column(name = "txnamount", length = 100)
	private String txnamount;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "valuedate")
	private Date valuedate;
	@Column(name = "refnumber", length = 20)
	private String refnumber;
	@Column(name = "debitpoolacc", length = 20)
	private String debitpoolacc;
	@Column(name = "creditpoolacc", length = 20)
	private String creditpoolacc;
	@Column(name = "debittrn", length = 255)
	private String debittrn;
	@Column(name = "credtrn", length = 255)
	private String credtrn;
	@Column(name = "ttumid", length = 255)
	private String ttumid;
//  new column added for stp process
	@Column(name = "makerid", length = 20)
	private String makerid;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "makerdate")
	@Temporal(TemporalType.DATE)
	private Date makerdate;
	@Column(name = "makerremarks", length = 255)
	private String makerremarks;

	@Column(name = "makeridrej", length = 20)
	private String makeridrej;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "makerdaterej")
	@Temporal(TemporalType.DATE)
	private Date makerdaterej;
	@Column(name = "makerremarksrej", length = 255)
	private String makerremarksrej;

	@Column(name = "checkerid", length = 20)
	private String checkerid;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "checkerdate")
	@Temporal(TemporalType.DATE)
	private Date checkerdate;
	@Column(name = "checkerremarks", length = 255)
	private String checkerremarks;

	@Column(name = "checkeridrej", length = 20)
	private String checkeridrej;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "checkerdaterej")
	@Temporal(TemporalType.DATE)
	private Date checkerdaterej;
	@Column(name = "checkerremarksrej", length = 255)
	private String checkerremarksrej;

	private String bnkmakerid;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	private Date bnkmakerdate;
	@Column(name = "bnkmakerremarks", length = 255)
	private String bnkmakerremarks;

	private String bnkmakeridrej;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	private Date bnkmakerdaterej;
	@Column(name = "bnkmakerremarksrej", length = 255)
	private String bnkmakerremarksrej;

	private String bnkcheckerid;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	private Date bnkcheckerdate;
	@Column(name = "bnkcheckerremarks", length = 255)
	private String bnkcheckerremarks;

	private String bnkcheckeridrej;
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	private Date bnkcheckerdaterej;
	@Column(name = "bnkcheckerremarksrej", length = 255)
	private String bnkcheckerremarksrej;

//for api approval success
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
	@Column(name = "approvedate")
	@Temporal(TemporalType.DATE)
	private Date approvedate;
	@Column(name = "approvestatus", length = 20)
	private String approvestatus;

	// new column for stp process end
	private String status;

	@Column(name = "res039", length = 255)
	private String res039;

	@Column(name = "res125", length = 255)
	private String res125;

	@Column(name = "res126", length = 255)
	private String res126;

	@Column(name = "res039enq", length = 255)
	private String res039enq;

	@Column(name = "res125enq", length = 255)
	private String res125enq;

	@Column(name = "res126enq", length = 255)
	private String res126enq;

	@Column(name = "res039reinit", length = 255)
	private String res039reinit;

	@Column(name = "res125reinit", length = 255)
	private String res125reinit;

	@Column(name = "res126reinit", length = 255)
	private String res126reinit;

	@Column(name = "txnid", length = 50)
	private String txnid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getTxnamount() {
		return txnamount;
	}

	public void setTxnamount(String txnamount) {
		this.txnamount = txnamount;
	}

	public Date getValuedate() {
		return valuedate;
	}

	public void setValuedate(Date valuedate) {
		this.valuedate = valuedate;
	}

	public String getRefnumber() {
		return refnumber;
	}

	public void setRefnumber(String refnumber) {
		this.refnumber = refnumber;
	}

	public String getDebitpoolacc() {
		return debitpoolacc;
	}

	public void setDebitpoolacc(String debitpoolacc) {
		this.debitpoolacc = debitpoolacc;
	}

	public String getCreditpoolacc() {
		return creditpoolacc;
	}

	public void setCreditpoolacc(String creditpoolacc) {
		this.creditpoolacc = creditpoolacc;
	}

	public String getDebittrn() {
		return debittrn;
	}

	public void setDebittrn(String debittrn) {
		this.debittrn = debittrn;
	}

	public String getCredtrn() {
		return credtrn;
	}

	public void setCredtrn(String credtrn) {
		this.credtrn = credtrn;
	}

	public String getTtumid() {
		return ttumid;
	}

	public void setTtumid(String ttumid) {
		this.ttumid = ttumid;
	}

	public String getMakerid() {
		return makerid;
	}

	public void setMakerid(String makerid) {
		this.makerid = makerid;
	}

	public Date getMakerdate() {
		return makerdate;
	}

	public void setMakerdate(Date makerdate) {
		this.makerdate = makerdate;
	}

	public String getMakerremarks() {
		return makerremarks;
	}

	public void setMakerremarks(String makerremarks) {
		this.makerremarks = makerremarks;
	}

	public String getMakeridrej() {
		return makeridrej;
	}

	public void setMakeridrej(String makeridrej) {
		this.makeridrej = makeridrej;
	}

	public Date getMakerdaterej() {
		return makerdaterej;
	}

	public void setMakerdaterej(Date makerdaterej) {
		this.makerdaterej = makerdaterej;
	}

	public String getMakerremarksrej() {
		return makerremarksrej;
	}

	public void setMakerremarksrej(String makerremarksrej) {
		this.makerremarksrej = makerremarksrej;
	}

	public String getCheckerid() {
		return checkerid;
	}

	public void setCheckerid(String checkerid) {
		this.checkerid = checkerid;
	}

	public Date getCheckerdate() {
		return checkerdate;
	}

	public void setCheckerdate(Date checkerdate) {
		this.checkerdate = checkerdate;
	}

	public String getCheckerremarks() {
		return checkerremarks;
	}

	public void setCheckerremarks(String checkerremarks) {
		this.checkerremarks = checkerremarks;
	}

	public String getCheckeridrej() {
		return checkeridrej;
	}

	public void setCheckeridrej(String checkeridrej) {
		this.checkeridrej = checkeridrej;
	}

	public Date getCheckerdaterej() {
		return checkerdaterej;
	}

	public void setCheckerdaterej(Date checkerdaterej) {
		this.checkerdaterej = checkerdaterej;
	}

	public String getCheckerremarksrej() {
		return checkerremarksrej;
	}

	public void setCheckerremarksrej(String checkerremarksrej) {
		this.checkerremarksrej = checkerremarksrej;
	}

	public String getBnkmakerid() {
		return bnkmakerid;
	}

	public void setBnkmakerid(String bnkmakerid) {
		this.bnkmakerid = bnkmakerid;
	}

	public Date getBnkmakerdate() {
		return bnkmakerdate;
	}

	public void setBnkmakerdate(Date bnkmakerdate) {
		this.bnkmakerdate = bnkmakerdate;
	}

	public String getBnkmakerremarks() {
		return bnkmakerremarks;
	}

	public void setBnkmakerremarks(String bnkmakerremarks) {
		this.bnkmakerremarks = bnkmakerremarks;
	}

	public String getBnkmakeridrej() {
		return bnkmakeridrej;
	}

	public void setBnkmakeridrej(String bnkmakeridrej) {
		this.bnkmakeridrej = bnkmakeridrej;
	}

	public Date getBnkmakerdaterej() {
		return bnkmakerdaterej;
	}

	public void setBnkmakerdaterej(Date bnkmakerdaterej) {
		this.bnkmakerdaterej = bnkmakerdaterej;
	}

	public String getBnkmakerremarksrej() {
		return bnkmakerremarksrej;
	}

	public void setBnkmakerremarksrej(String bnkmakerremarksrej) {
		this.bnkmakerremarksrej = bnkmakerremarksrej;
	}

	public String getBnkcheckerid() {
		return bnkcheckerid;
	}

	public void setBnkcheckerid(String bnkcheckerid) {
		this.bnkcheckerid = bnkcheckerid;
	}

	public Date getBnkcheckerdate() {
		return bnkcheckerdate;
	}

	public void setBnkcheckerdate(Date bnkcheckerdate) {
		this.bnkcheckerdate = bnkcheckerdate;
	}

	public String getBnkcheckerremarks() {
		return bnkcheckerremarks;
	}

	public void setBnkcheckerremarks(String bnkcheckerremarks) {
		this.bnkcheckerremarks = bnkcheckerremarks;
	}

	public String getBnkcheckeridrej() {
		return bnkcheckeridrej;
	}

	public void setBnkcheckeridrej(String bnkcheckeridrej) {
		this.bnkcheckeridrej = bnkcheckeridrej;
	}

	public Date getBnkcheckerdaterej() {
		return bnkcheckerdaterej;
	}

	public void setBnkcheckerdaterej(Date bnkcheckerdaterej) {
		this.bnkcheckerdaterej = bnkcheckerdaterej;
	}

	public String getBnkcheckerremarksrej() {
		return bnkcheckerremarksrej;
	}

	public void setBnkcheckerremarksrej(String bnkcheckerremarksrej) {
		this.bnkcheckerremarksrej = bnkcheckerremarksrej;
	}

	public Date getApprovedate() {
		return approvedate;
	}

	public void setApprovedate(Date approvedate) {
		this.approvedate = approvedate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRes039() {
		return res039;
	}

	public void setRes039(String res039) {
		this.res039 = res039;
	}

	public String getRes125() {
		return res125;
	}

	public void setRes125(String res125) {
		this.res125 = res125;
	}

	public String getRes126() {
		return res126;
	}

	public void setRes126(String res126) {
		this.res126 = res126;
	}

	public String getApprovestatus() {
		return approvestatus;
	}

	public void setApprovestatus(String approvestatus) {
		this.approvestatus = approvestatus;
	}

	public String getRes039enq() {
		return res039enq;
	}

	public void setRes039enq(String res039enq) {
		this.res039enq = res039enq;
	}

	public String getRes125enq() {
		return res125enq;
	}

	public void setRes125enq(String res125enq) {
		this.res125enq = res125enq;
	}

	public String getRes126enq() {
		return res126enq;
	}

	public void setRes126enq(String res126enq) {
		this.res126enq = res126enq;
	}

	public String getRes039reinit() {
		return res039reinit;
	}

	public void setRes039reinit(String res039reinit) {
		this.res039reinit = res039reinit;
	}

	public String getRes125reinit() {
		return res125reinit;
	}

	public void setRes125reinit(String res125reinit) {
		this.res125reinit = res125reinit;
	}

	public String getRes126reinit() {
		return res126reinit;
	}

	public void setRes126reinit(String res126reinit) {
		this.res126reinit = res126reinit;
	}

	public String getTxnid() {
		return txnid;
	}

	public void setTxnid(String txnid) {
		this.txnid = txnid;
	}

}
