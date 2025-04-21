package com.stp.model.db1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "stp_reportquery")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class StpReportQuery extends CommonColumnsTTum {

	@Column(name = "queryheader", length = 1000)
	private String queryheader;
	private Integer requestparam;

	public String getQueryheader() {
		return queryheader;
	}

	public void setQueryheader(String queryheader) {
		this.queryheader = queryheader;
	}

	public Integer getRequestparam() {
		return requestparam;
	}

	public void setRequestparam(Integer requestparam) {
		this.requestparam = requestparam;
	}

}
