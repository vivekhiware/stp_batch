package com.stp.job.imps;

import static com.stp.utility.GenericCLass.HOST;
import static com.stp.utility.GenericCLass.PORT;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iso.config.IsoV93Message;
import com.iso.config.IsoV93MessageRes;
import com.iso.config.main.ISOCommunicationExample;
import com.iso.opt.IsoPackagerGlobal;
import com.stp.model.db1.STP_IMPS_NONNPCI_OW;
import com.stp.service.ServiceImps;

@Service
public class STP_IMPS_NONNPCI_OWscheduledTask {

	private static final Logger logger = LoggerFactory.getLogger(STP_IMPS_NONNPCI_OWscheduledTask.class);
	@Autowired
	private static ServiceImps serviceImps;

	// Executes every minute
//	@Scheduled(cron = "0 * * * * ?")
	public void executeTask() {
		logger.info("STP_IMPS_NONNPCI_OWscheduledTask Scheduled task  start executed at: " + new Date());
	}

	public static void PROCESSSTP_IMPS_NONNPCI_OW() {
		ISOCommunicationExample serviceNetwork = null;
		int dbresponse = 0;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
			// Process different request types using a helper method
			dbresponse += processRequestSTP_IMPS_NONNPCI_OW("FRESH", "L4", serviceNetwork, dbresponse); // New requests
			System.out.println("NEW REQUEST END");

			dbresponse += processRequestSTP_IMPS_NONNPCI_OW("ENQUIRY", "L5", serviceNetwork, dbresponse); // Enquiry
																											// requests
			System.out.println("ENQUIRED REQUEST END");

			dbresponse += processRequestSTP_IMPS_NONNPCI_OW("REPEAT", "L6", serviceNetwork, dbresponse); // Repeat
																											// requests
			System.out.println("REPEAT REQUEST END");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int processRequestSTP_IMPS_NONNPCI_OW(String requestType, String condition,
			ISOCommunicationExample serviceNetwork, int dbresponse) {
		List<STP_IMPS_NONNPCI_OW> requestList = null;
		List<IsoV93Message> request1200 = null;
		IsoV93Message isoV93Message = null;
		IsoV93MessageRes isoV93MessageRes = null;
		byte[] requestmsgBytes = null;
		byte[] responsemsgBytes = null;
		try {
			// Fetch the relevant request list based on the condition
			if ("FRESH".equals(requestType)) {
				requestList = serviceImps.processSTP_IMPS_NONNPCI_OW("L4");
				request1200 = IsoPackagerGlobal.request1200(requestList);
			} else if ("ENQUIRY".equals(requestType)) {
				requestList = serviceImps.processSTP_IMPS_NONNPCI_OW("L5");
				request1200 = IsoPackagerGlobal.request1200Enquiry(requestList);
			} else if ("REPEAT".equals(requestType)) {
				requestList = serviceImps.processSTP_IMPS_NONNPCI_OW("L6");
				request1200 = IsoPackagerGlobal.request1201Repeat(requestList);
			}
			requestList = null;
			// Loop through the messages, send them, and process the response
			for (Iterator<IsoV93Message> iterator = request1200.iterator(); iterator.hasNext();) {
				isoV93Message = iterator.next();
				requestmsgBytes = isoV93Message.generateMessage();
				isoV93Message.printMessage(requestType); // Print message type (FRESH, ENQUIRY, REPEAT)
				isoV93Message = null;

				// Send the message and get the response
				responsemsgBytes = serviceNetwork.networkTransportByte(requestmsgBytes);
				requestmsgBytes = null;
				isoV93MessageRes = new IsoV93MessageRes(responsemsgBytes);
				responsemsgBytes = null;
				isoV93MessageRes.printMessage(requestType);

				// Update the database with the response
				dbresponse += serviceImps.updateSTP_IMPS_NONNPCI_OW(isoV93MessageRes, requestType);
				isoV93MessageRes = null;
				// Remove the message from the iterator
				iterator.remove();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			requestList = null;
			request1200 = null;
			isoV93Message = null;
			isoV93MessageRes = null;
			System.out.println("UPDATED IN DATABASE " + requestType + ": " + dbresponse);
		}

		return dbresponse;
	}

}
