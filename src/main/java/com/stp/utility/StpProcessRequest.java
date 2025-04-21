package com.stp.utility;

import org.springframework.stereotype.Component;

@Component
public class StpProcessRequest {
	private String valuedate;
	private String batchid;
	private String batchcount;
	private String txmamount;

	private String userid;
	private String userdate;
	private String userremark;
	private String status;

	public String getValuedate() {
		return valuedate;
	}

	public void setValuedate(String valuedate) {
		this.valuedate = valuedate;
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

	public String getTxmamount() {
		return txmamount;
	}

	public void setTxmamount(String txmamount) {
		this.txmamount = txmamount;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserdate() {
		return userdate;
	}

	public void setUserdate(String userdate) {
		this.userdate = userdate;
	}

	public String getUserremark() {
		return userremark;
	}

	public void setUserremark(String userremark) {
		this.userremark = userremark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
