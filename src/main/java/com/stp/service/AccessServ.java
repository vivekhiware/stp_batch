package com.stp.service;

import java.util.List;

import com.stp.model.db1.STP_Access;

public interface AccessServ {

	public List<STP_Access> fetchAccessList();

	public List<STP_Access> fetchAccessListAll();

	public STP_Access addAccessDetail(STP_Access access);

	public STP_Access removeAccessDetail(String appname);

}
