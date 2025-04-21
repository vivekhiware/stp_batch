package com.stp.model.db1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "stp_ttumquery")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class StpTtumQuery extends CommonColumnsTTum {

	@Column(name = "querytbl", length = 300)
	private String querytbl;
	@Column(name = "queryforCount", length = 1000)
	private String queryforCount;

	public String getQuerytbl() {
		return querytbl;
	}

	public void setQuerytbl(String querytbl) {
		this.querytbl = querytbl;
	}

	public String getQueryforCount() {
		return queryforCount;
	}

	public void setQueryforCount(String queryforCount) {
		this.queryforCount = queryforCount;
	}

}
