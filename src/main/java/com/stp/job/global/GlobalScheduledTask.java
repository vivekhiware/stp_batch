package com.stp.job.global;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iso.config.IsoV93Message;
import com.iso.config.main.ISOCommunicationExample;
import com.stp.dao.db1.NativeQueryRepository;
import com.stp.service.ServiceAtmCia;
import com.stp.service.ServiceCards;
import com.stp.service.ServiceImps;
import com.stp.service.ServiceUpi;

@Component
public class GlobalScheduledTask {
	private static final int THREAD_POOL_SIZE = 20;
	private static final Logger logger = LoggerFactory.getLogger(GlobalScheduledTask.class);

	private final NativeQueryRepository nativeQueryRepository;
	private final ServiceImps serviceImps;
	private final ServiceUpi serviceUpi;
	private final ServiceCards serviceCards;
	private final ServiceAtmCia serviceAtmCia;

	@Autowired
	public GlobalScheduledTask(NativeQueryRepository nativeQueryRepository, ServiceImps serviceImps,
			ServiceUpi serviceUpi, ServiceCards serviceCards, ServiceAtmCia serviceAtmCia) {
		this.nativeQueryRepository = nativeQueryRepository;
		this.serviceImps = serviceImps;
		this.serviceUpi = serviceUpi;
		this.serviceCards = serviceCards;
		this.serviceAtmCia = serviceAtmCia;
	}

	public void processBatch(String tableName, String status, String type, String requestType) {
		logger.info("Running GlobalScheduledTask job at {}", new Date());
		List<Object[]> batchDetails = nativeQueryRepository.getBatchDetail(tableName, status);
		if (batchDetails.isEmpty()) {
			logger.info("No records found  status.");
			return;
		}
		String dynamicQueryForUpdate = nativeQueryRepository.updateDynamicQueryForIso(tableName, status);
		logger.info("DynamicqueryforIso Query:  {}", dynamicQueryForUpdate);

		// Thread pool
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

		for (Object[] row : batchDetails) {
			String countId = String.valueOf(row[0]);
			String batchId = String.valueOf(row[1]);
			executorService.submit(() -> {
				logBatchProcessingStart(countId, batchId);
				if (requestType.equalsIgnoreCase("FRESH")) {
					boolean checkbatchExistDetail = nativeQueryRepository.checkbatchExistDetail(requestType, type,
							tableName, countId, batchId);
					if (checkbatchExistDetail) {
						processSingleBatch(tableName, batchId, status, dynamicQueryForUpdate, requestType, type);
					}
				} else {
					processSingleBatch(tableName, batchId, status, dynamicQueryForUpdate, requestType, type);

				}

			});
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(10, TimeUnit.MINUTES); // Wait for all tasks
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			logger.error("Executor interrupted while waiting: {}", e.getMessage(), e);
		}
	}

	private void logBatchProcessingStart(String countId, String batchId) {
		logger.info("Thread {} - Processing Batch Count: {}, Batch ID: {}", Thread.currentThread().getName(), countId,
				batchId);
	}

	private void processSingleBatch(String tableName, String batchId, String status, String dynamicQueryForUpdate,
			String requestType, String type) {
		try {
			ISOCommunicationExample serviceNetwork = new ISOCommunicationExample();

			logger.info(
					"Processing logic for table: {}, batchId: {}, status: {} , dynamicQueryForUpdate:{} , requestType:{} , type:{}",
					tableName, batchId, status, dynamicQueryForUpdate, requestType, type);
			List<IsoV93Message> request1200 = null;
			if (type.equalsIgnoreCase("IMPS")) {
				request1200 = serviceImps.getProcesRequest(tableName, requestType, batchId, status);
			} else if (type.equalsIgnoreCase("UPI")) {
				request1200 = serviceUpi.getProcesRequest(tableName, requestType, batchId, status);
			} else if (type.equalsIgnoreCase("CARDS")) {
				request1200 = serviceCards.getProcesRequest(tableName, requestType, batchId, status);
			} else if (type.equalsIgnoreCase("ATMCIA")) {
				request1200 = serviceAtmCia.getProcesRequest(tableName, requestType, batchId, status);
			}
			if (request1200 != null && !request1200.isEmpty()) {
				for (IsoV93Message isoMsg : request1200) {
					processSingleTransaction(serviceNetwork, isoMsg, dynamicQueryForUpdate, requestType);
				}
			} else {
				logger.info("No ISO 1200 messages generated for batchId: {}", batchId);
			}

		} catch (Exception e) {
			logger.error(" interrupted while processSingleBatch: {}", e.getMessage(), e);
		}

	}

	private void processSingleTransaction(ISOCommunicationExample serviceNetwork, IsoV93Message isoMsg,
			String dynamicQueryForUpdate, String requestType) {
		try {
			byte[] message = isoMsg.generateMessage();
			isoMsg.printMessage(requestType);

			byte[] resmessage = serviceNetwork.networkTransportByte(message);
			IsoV93Message isoMsgRes = new IsoV93Message(resmessage, "RESPONSE");
			isoMsgRes.printMessageResponse(requestType);

			processSingleTransactionUpdate(dynamicQueryForUpdate, isoMsgRes);

		} catch (Exception e) {
			logger.error(" interrupted while processSingleTransaction: {}", e.getMessage(), e);
		}

	}

	private void processSingleTransactionUpdate(String dynamicQueryForUpdate, IsoV93Message isoMsg) {
		try {
			nativeQueryRepository.processLevelIsoStatusUpdate(dynamicQueryForUpdate, isoMsg);
		} catch (Exception e) {
			logger.error(" interrupted while processSingleTransactionUpdate: {}", e.getMessage(), e);
		}
	}

}
