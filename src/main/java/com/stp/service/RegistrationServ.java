package com.stp.service;

import java.util.List;

import com.stp.model.db1.STP_Access;
import com.stp.model.db1.STP_Login;

public interface RegistrationServ {
	public STP_Login addAccessDetail(STP_Login access);

	public STP_Login fetchAccessList(Integer emplycd);

	public STP_Login updateAccess(STP_Login access);

	public List<STP_Login> fetchAccessListAll();

	public STP_Login removeAccessDetail(Integer emplycd);

}
