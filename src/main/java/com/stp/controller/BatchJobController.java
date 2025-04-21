package com.stp.controller;

import static com.stp.utility.GenericCLass.STATUS_FAILED;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stp.dao.db1.TTUMQueryRepository;
import com.stp.exception.DetailNotFoundException;
import com.stp.job.global.GlobalScheduledTask;
import com.stp.model.db1.StpTtumQuery;
import com.stp.utility.ResponseBean;

@RestController
@RequestMapping("/scheduled")
public class BatchJobController {

	private static final Logger logger = LoggerFactory.getLogger(BatchJobController.class);

	private final TTUMQueryRepository queryRepository;
	private final GlobalScheduledTask globalScheduledTask;

	@Autowired
	public BatchJobController(TTUMQueryRepository queryRepository, GlobalScheduledTask globalScheduledTask) {
		super();
		this.queryRepository = queryRepository;
		this.globalScheduledTask = globalScheduledTask;
	}

	@PostMapping("/run-batch-job")
//	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "queryRateLimiterFallbackBatch")
	@Async
	public void runBatchJob(@RequestParam(required = false) String type, @RequestParam(required = false) String level,
			@RequestParam(required = false) String process) {
		logger.info("query id: {}", type);
		logger.info("level: {}", level);
		logger.info("process: {}", process);
		Integer queryid = Integer.parseInt(type);
		StpTtumQuery stpquery = queryRepository.findById(queryid)
				.orElseThrow(() -> new DetailNotFoundException("STP TABLE  not found"));
		globalScheduledTask.processBatch(stpquery.getQuerytbl(), "L4", process, "FRESH");
	}

	public CompletableFuture<ResponseBean> queryRateLimiterFallbackBatch(Throwable t) {
		logger.error("Rate limit exceeded   BatchJobController due to: {}", t.getMessage());
		ResponseBean fallbackResponse = new ResponseBean();
		fallbackResponse.setStatus(STATUS_FAILED);
		fallbackResponse.setMessage("Rate limit exceeded. Please try again later.");
		return CompletableFuture.completedFuture(fallbackResponse);
	}
}
