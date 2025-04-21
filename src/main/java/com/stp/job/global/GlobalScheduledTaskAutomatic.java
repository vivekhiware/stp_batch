	package com.stp.job.global;
	
	import java.util.List;
	
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.scheduling.annotation.Scheduled;
	import org.springframework.stereotype.Component;
	
	import com.stp.dao.db1.TTUMQueryRepository;
	import com.stp.model.db1.StpTtumQuery;
	
	@Component
	public class GlobalScheduledTaskAutomatic {
	
		private static final Logger logger = LoggerFactory.getLogger(GlobalScheduledTaskAutomatic.class);
		private static final String ENQUIRY = "ENQUIRY";
		private static final String REPEAT = "REPEAT";
		private final TTUMQueryRepository queryRepository;
		private final GlobalScheduledTask globalScheduledTask;
	
		@Autowired
		public GlobalScheduledTaskAutomatic(TTUMQueryRepository queryRepository, GlobalScheduledTask globalScheduledTask) {
			this.queryRepository = queryRepository;
			this.globalScheduledTask = globalScheduledTask;
		}
	
		@Scheduled(cron = "0 0 * * * ?")
		public void executeTaskIMPS() {
			executeTaskByType("IMPS", "L5", ENQUIRY);
		}
	
		@Scheduled(cron = "0 0 * * * ?")
		public void executeTaskUPI() {
			executeTaskByType("UPI", "L5", ENQUIRY);
		}
	
		@Scheduled(cron = "0 0 * * * ?")
		public void executeTaskCARDS() {
			executeTaskByType("CARDS", "L5", ENQUIRY);
		}
	
		@Scheduled(cron = "0 0 * * * ?")
		public void executeTaskIMPSRepeat() {
			executeTaskByType("IMPS", "L6", REPEAT);
		}
	
		@Scheduled(cron = "0 0 * * * ?")
		public void executeTaskUPIRepeat() {
			executeTaskByType("UPI", "L6", REPEAT);
		}
	
		@Scheduled(cron = "0 0 * * * ?")
		public void executeTaskCARDSRepeat() {
			executeTaskByType("CARDS", "L6", REPEAT);
		}
	
		private void executeTaskByType(String type, String level, String requestType) {
			try {
				List<StpTtumQuery> activeQueries = queryRepository.findAll();
				activeQueries.stream()
						.filter(query -> "A".equalsIgnoreCase(query.getStatus()) && type.equalsIgnoreCase(query.getType()))
						.forEach(query -> {
							logger.info("Query Table: {}, TYPE: {}", query.getQuerytbl(), type);
							globalScheduledTask.processBatch(query.getQuerytbl(), level, type, requestType);
						});
			} catch (Exception e) {
				logger.error("Error while processing {} tasks: {}", type, e.getMessage(), e);
			}
		}
	}
