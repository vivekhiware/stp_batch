package com.stp.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stp.dao.db1.HistoryRepository;
import com.stp.model.db1.STP_History;
import com.stp.service.LoginServ;

@Service
public class LoginServImpl implements LoginServ {

	@Autowired
	private HistoryRepository historyRepository;

	@Override
//	@Cacheable(value = "addHistoryCache")
	public void addHistory(HttpServletRequest request, Integer emplycd) {

		STP_History history = new STP_History();
		history.setEmplycd(emplycd);
		history.setIntime(new Date());
		history.setSessionid(request.getSession().getId());
		history.setIpaddress(request.getRemoteAddr());
		historyRepository.save(history);
	}

	@Override
//	@Cacheable(value = "checkAlreadyLoginCache", key = "#emplycd ")
	public boolean checkAlreadyLogin(Integer emplycd) {
		// TODO Auto-generated method stub
		return historyRepository.existsByEmplycdAndOuttimeIsNull(emplycd);
	}

	@Override
//	@Cacheable(value = "updateHistoryCache", key = "#emplycd ")
	public void updateHistory(Integer emplycd) {
		historyRepository.updateOutTimeByEmplycd(emplycd);
	}

}
