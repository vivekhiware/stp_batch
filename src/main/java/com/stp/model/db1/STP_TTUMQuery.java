package com.stp.model.db1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Entity
@Table(name = "stp_ttumquery")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class STP_TTUMQuery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "type", length = 10)
	private String type;
	@Column(name = "subtype", length = 50)
	private String subtype;
	@Column(name = "query", length = 1000)
	private String query;
	private Integer paramcount;
	@Column(name = "status", length = 10)
	private String status = "A";
	@Column(name = "processlevel", length = 10)
	private String processlevel = "MAKER";

	private String queryforCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProcesslevel() {
		return processlevel;
	}

	public void setProcesslevel(String processlevel) {
		this.processlevel = processlevel;
	}

	public Integer getParamcount() {
		return paramcount;
	}

	public void setParamcount(Integer paramcount) {
		this.paramcount = paramcount;
	}

	public String getQueryforCount() {
		return queryforCount;
	}

	public void setQueryforCount(String queryforCount) {
		this.queryforCount = queryforCount;
	}

}
