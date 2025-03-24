package com.stp.job.upi;

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
import com.stp.model.db1.STP_UPI_DRC_NPCI;
import com.stp.service.ServiceUpi;

@Service
public class STP_UPI_DRC_NPCIscheduledTask {

	private static final Logger logger = LoggerFactory.getLogger(STP_UPI_DRC_NPCIscheduledTask.class);
	@Autowired
	private static ServiceUpi serviceUpi;

	// Executes every minute
//	@Scheduled(cron = "0 * * * * ?")
	public void executeTask() {
		logger.info("STP_UPI_DRC_NPCIscheduledTask Scheduled task  start executed at: " + new Date());
	}

	public static void PROCESSSTP_UPI_DRC_NPCI() {
		ISOCommunicationExample serviceNetwork = null;
		int dbresponse = 0;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
			// Process different request types using a helper method
			dbresponse += processRequestSTP_UPI_DRC_NPCI("FRESH", "L4", serviceNetwork, dbresponse); // New
																										// requests
			System.out.println("NEW REQUEST END");

			dbresponse += processRequestSTP_UPI_DRC_NPCI("ENQUIRY", "L5", serviceNetwork, dbresponse); // Enquiry
			// requests
			System.out.println("ENQUIRED REQUEST END");

			dbresponse += processRequestSTP_UPI_DRC_NPCI("REPEAT", "L6", serviceNetwork, dbresponse); // Repeat
			// requests
			System.out.println("REPEAT REQUEST END");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int processRequestSTP_UPI_DRC_NPCI(String requestType, String condition,
			ISOCommunicationExample serviceNetwork, int dbresponse) {
		List<STP_UPI_DRC_NPCI> requestList = null;
		List<IsoV93Message> request1200 = null;
		IsoV93Message isoV93Message = null;
		IsoV93MessageRes isoV93MessageRes = null;
		byte[] requestmsgBytes = null;
		byte[] responsemsgBytes = null;
		try {
			// Fetch the relevant request list based on the condition
			if ("FRESH".equals(requestType)) {
				requestList = serviceUpi.processSTP_UPI_DRC_NPCI("L4");
				request1200 = IsoPackagerGlobal.request1200(requestList);
			} else if ("ENQUIRY".equals(requestType)) {
				requestList = serviceUpi.processSTP_UPI_DRC_NPCI("L5");
				request1200 = IsoPackagerGlobal.request1200Enquiry(requestList);
			} else if ("REPEAT".equals(requestType)) {
				requestList = serviceUpi.processSTP_UPI_DRC_NPCI("L6");
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
				dbresponse += serviceUpi.updateSTP_UPI_DRC_NPCI(isoV93MessageRes, requestType);
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
