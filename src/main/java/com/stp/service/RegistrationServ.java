package com.stp.service;

import java.util.List;

import com.stp.model.db1.StpLogin;

public interface RegistrationServ {
	public StpLogin addAccessDetail(StpLogin access);

	public StpLogin fetchAccessList(Integer emplycd);

	public StpLogin updateAccess(StpLogin access);

	public List<StpLogin> fetchAccessListAll();

	public StpLogin removeAccessDetail(Integer emplycd);

}
