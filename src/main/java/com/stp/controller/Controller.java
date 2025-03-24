package com.stp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping(value = "work")
	public String CheckAPi() {
		return " working for menu controller";
	}

	@GetMapping("/workParam")
	public String handleWorkRequest(@RequestParam(name = "paramA", required = true) String param,
			@RequestParam(name = "paramB", required = true) String param1) {
		return "Received parameter: " + param;
	}

}
