package com.stp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stp.service.DashBoardService;
import com.stp.utility.ResponseBean;

@RestController
@RequestMapping(value = "/api_dash")
public class DashBoardController {

	@Autowired
	private DashBoardService dashBoardService;

	@PostMapping(value = "dash")
	public ResponseBean DashBoardView() {
		System.out.println(" calling dashboard ");
		ResponseBean bean = new ResponseBean();
		List<Object[]> dashBoard = dashBoardService.getDashBoard();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("IMPS", dashBoard);
		if (dashBoard.size() > 0) {
			bean.setData(map);
			bean.setStatus("Success");
			bean.setMessage("Found");
		} else {
			bean.setStatus("Failed");
			bean.setMessage("NotFound");
		}

		return bean;
	}
}
