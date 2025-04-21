package com.stp.service;

import javax.servlet.http.HttpServletRequest;

public interface LoginServ {

	void addHistory(HttpServletRequest request, Integer emplycd);

	boolean checkAlreadyLogin(Integer emplycd);

	void updateHistory(Integer emplycd);

}
