package com.stp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stp.service.DashBoardService;
import com.stp.utility.ResponseBean;

@RestController
@RequestMapping(value = "/api_dash")
public class DashBoardController {
	private static final Logger logger = LoggerFactory.getLogger(DashBoardController.class);

	private DashBoardService dashBoardService;

	public DashBoardController(DashBoardService dashBoardService) {

		this.dashBoardService = dashBoardService;
	}

	@PostMapping(value = "dash")
	public ResponseBean dashBoardDetail() {
		logger.info(" calling dashboard ");
		ResponseBean bean = new ResponseBean();
		List<Object[]> dashBoard = dashBoardService.getDashBoardDynamic("IMPS");
		List<Object[]> dashBoard1 = dashBoardService.getDashBoardDynamic("UPI");
		List<Object[]> dashBoard2 = dashBoardService.getDashBoardDynamic("CARDS");
		List<Object[]> dashBoard3 = dashBoardService.getDashBoardDynamic("ATMCIA");
		Map<String, Object> map = new HashMap<>();
		map.put("IMPS", dashBoard);
		map.put("UPI", dashBoard1);
		map.put("CARDS", dashBoard2);
		map.put("ATMCIA", dashBoard3);
		if (!dashBoard.isEmpty()) {
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
