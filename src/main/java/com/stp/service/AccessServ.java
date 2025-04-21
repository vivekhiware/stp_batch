package com.stp.service;

import java.util.List;

import com.stp.model.db1.StpAccess;

public interface AccessServ {

	public List<StpAccess> fetchAccessList();

	public List<StpAccess> fetchAccessListAll();

	public StpAccess addAccessDetail(StpAccess access);

	public StpAccess removeAccessDetail(String appname);

}
