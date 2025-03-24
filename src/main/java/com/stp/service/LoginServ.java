package com.stp.service;

import javax.servlet.http.HttpServletRequest;

import com.stp.model.db1.STP_History;

public interface LoginServ {

	void addHistory(HttpServletRequest request, Integer emplycd);

	boolean checkAlreadyLogin(Integer emplycd);

	void updateHistory(Integer emplycd);

}
