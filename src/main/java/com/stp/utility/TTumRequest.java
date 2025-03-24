package com.stp.utility;

import org.springframework.stereotype.Component;

@Component
public class TTumRequest {
	private String userid;
	private String userrole;
	private String type;
	private String fromdate;
	private String todate;
	private Integer queryid;
	private String queryDetail;
	private String status;
	private String pagination;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserrole() {
		return userrole;
	}

	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFromdate() {
		return fromdate;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public Integer getQueryid() {
		return queryid;
	}

	public void setQueryid(Integer queryid) {
		this.queryid = queryid;
	}

	public String getQueryDetail() {
		return queryDetail;
	}

	public void setQueryDetail(String queryDetail) {
		this.queryDetail = queryDetail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPagination() {
		return pagination;
	}

	public void setPagination(String pagination) {
		this.pagination = pagination;
	}

}
