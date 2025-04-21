package com.iso.opt;

import static com.stp.utility.GenericCLass.creditTransaction;
import static com.stp.utility.GenericCLass.debitTransaction;
import static com.stp.utility.GenericCLass.notFailedTransaction;
import static com.stp.utility.GenericCLass.repostTransaction;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iso.config.IsoV93Message;
import com.iso.config.MsgId;

public class IsoPackagerGlobal {

	private static final Logger logger = LoggerFactory.getLogger(IsoPackagerGlobal.class);
	private static final String PROCESSCODE1200 = "402010";
	private static final String PROCESSCODE1200ENQ = "970000";
	private static final String FUNCTIONALCODE1200 = "200";
	private static final String ACQINSTITUTIONCODE1200 = "11111111111";
	private static final String TXNAMTENQ = "000000000000";
	private static final String CURRENCYCODE1200 = "356";
	private static final String CHANNELCODE = "RCN";
	private static final String FIELD125REQ = "01ENQU";

	private static final String YYYYMMDD = "yyyyMMdd";
	private static final String YYMMDDHHMMSS = "yyMMddHHmmss";
	private static final String MMDD = "MMdd";

	private static final String GETTXNAMOUNT = "getTxnamount";
	private static final String GETVALUEDATE = "getValuedate";
	private static final String GETTTUMID = "getTtumid";
	private static final String GETREFNUMBER = "getRefnumber";
	private static final String GETDEBITTRN = "getDebittrn";
	private static final String GETDEBITPOOLACC = "getDebitpoolacc";
	private static final String GETCREDTRN = "getCredtrn";
	private static final String GETCREDITPOOLACC = "getCreditpoolacc";
	private static final String GETACCOUNTNUMBER = "getAccountnumber";
	private static final String DEBIT_EXCEPTION = "Debit_Exception";
	private static final String CREDIT_EXCEPTION = "Credit_Exception";
	private static final String GETBNKMAKERID = "getBnkmakerid";
	private static final String GETBNKCHECKERID = "getBnkcheckerid";

	private IsoPackagerGlobal() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	private static void handleException(Exception e) {
		logger.error("Exception occurred: " + e.getMessage(), e);
	}

	public static String generateDateFormat(Date input, String pattern) {
		String formattedDate = null;
		try {
			SimpleDateFormat outputformat = new SimpleDateFormat(pattern);
			formattedDate = outputformat.format(input);
		} catch (Exception e) {
			handleException(e);
		}
		return formattedDate;
	}

	public static String generateDateFormat(Object dateObj, String pattern) {
		SimpleDateFormat outputFormat = new SimpleDateFormat(pattern);
		return outputFormat.format(dateObj); // Format and return the date as string
	}

	public static String appendAndFixLength(String amount) {
		StringBuilder result = new StringBuilder();

		if (amount.length() > 12) {
			result.append(amount.substring(0, 12));
		} else {
			// Prepend zeroes
			for (int i = amount.length(); i < 12; i++) {
				result.append('0');
			}
			result.append(amount);
		}

		return result.toString();
	}

	private static Object invokeGetterMethod(Object obj, String methodName) {
		try {
			Method method = obj.getClass().getMethod(methodName);
			return method.invoke(obj);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to invoke method: " + methodName, e);
		}
	}

	//////////////////// FORMAT FOR FRESH REQUEST//////////////////////
	public static <T> List<IsoV93Message> request1200(List<T> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<>();
		IsoV93Message obj1200 = null;
		for (T req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, invokeGetterMethod(req, GETACCOUNTNUMBER).toString());
				obj1200.addFields(3, PROCESSCODE1200);
				obj1200.addFields(4, appendAndFixLength(invokeGetterMethod(req, GETTXNAMOUNT).toString()));
				obj1200.addFields(12, generateDateFormat(invokeGetterMethod(req, GETVALUEDATE), YYMMDDHHMMSS));
				obj1200.addFields(17, generateDateFormat(invokeGetterMethod(req, GETVALUEDATE), MMDD));
				obj1200.addFields(24, FUNCTIONALCODE1200);
				obj1200.addFields(32, ACQINSTITUTIONCODE1200);
				obj1200.addFields(37, invokeGetterMethod(req, GETREFNUMBER).toString());
				obj1200.addFields(49, CURRENCYCODE1200);
				obj1200.addFields(59, invokeGetterMethod(req, GETBNKMAKERID).toString().concat("|")
						.concat(invokeGetterMethod(req, GETBNKCHECKERID).toString()));
				obj1200.addFields(72, generateDateFormat(invokeGetterMethod(req, GETVALUEDATE), YYYYMMDD));
				obj1200.addFields(102, invokeGetterMethod(req, GETDEBITPOOLACC).toString());
				obj1200.addFields(103, invokeGetterMethod(req, GETCREDITPOOLACC).toString());
				obj1200.addFields(123, CHANNELCODE);
				obj1200.addFields(125, invokeGetterMethod(req, GETDEBITTRN).toString());
				obj1200.addFields(126, invokeGetterMethod(req, GETCREDTRN).toString());
				obj1200.addFields(127, invokeGetterMethod(req, GETTTUMID).toString());
				reqList.add(obj1200);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return reqList;
	}
//////////////////  FORMAT FOR FRESH  REQUEST//////////////////////

////////////////  FORMAT FOR ENQUIRY  REQUEST//////////////////////
	public static <T> List<IsoV93Message> request1200Enquiry(List<T> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<>();
		IsoV93Message obj1200 = null;
		String status = null;
		for (T req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, invokeGetterMethod(req, GETACCOUNTNUMBER).toString());
				obj1200.addFields(3, PROCESSCODE1200ENQ);
				obj1200.addFields(4, TXNAMTENQ);
				obj1200.addFields(12, generateDateFormat(invokeGetterMethod(req, GETVALUEDATE), YYMMDDHHMMSS));
				obj1200.addFields(17, generateDateFormat(invokeGetterMethod(req, GETVALUEDATE), MMDD));
				obj1200.addFields(24, FUNCTIONALCODE1200);
				obj1200.addFields(32, ACQINSTITUTIONCODE1200);
				obj1200.addFields(37, invokeGetterMethod(req, GETREFNUMBER).toString());
				obj1200.addFields(49, CURRENCYCODE1200);
				obj1200.addFields(59, invokeGetterMethod(req, GETBNKMAKERID).toString().concat("|")
						.concat(invokeGetterMethod(req, GETBNKCHECKERID).toString()));
				obj1200.addFields(72, generateDateFormat(invokeGetterMethod(req, GETVALUEDATE), YYYYMMDD));
				status = invokeGetterMethod(req, "getApprovestatus").toString();
				if (status.equalsIgnoreCase(DEBIT_EXCEPTION)) {
					obj1200.addFields(102, invokeGetterMethod(req, GETDEBITPOOLACC).toString());
				} else if (status.equalsIgnoreCase(CREDIT_EXCEPTION)) {
					obj1200.addFields(102, invokeGetterMethod(req, GETCREDITPOOLACC).toString());
				}
				obj1200.addFields(123, CHANNELCODE);
				obj1200.addFields(125, FIELD125REQ);
				obj1200.addFields(126, invokeGetterMethod(req, GETCREDTRN).toString());
				obj1200.addFields(127, invokeGetterMethod(req, GETTTUMID).toString());
				reqList.add(obj1200);

			} catch (Exception e) {
				handleException(e);
			}
		}
		return reqList;
	}
//////////////  FORMAT FOR ENQUIRY  REQUEST////////////////////

////////////  FORMAT FOR REPEAT  REQUEST//////////////////////
	public static <T> List<IsoV93Message> request1201Repeat(List<T> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<>();
		IsoV93Message obj1200 = null;
		String res125 = null;
		String res126 = null;
		String status = null;
		for (T req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1201);
				obj1200.addFields(2, invokeGetterMethod(req, GETACCOUNTNUMBER).toString());
				obj1200.addFields(3, PROCESSCODE1200);
				obj1200.addFields(4, appendAndFixLength(invokeGetterMethod(req, GETTXNAMOUNT).toString()));
				obj1200.addFields(32, ACQINSTITUTIONCODE1200);
				obj1200.addFields(37, invokeGetterMethod(req, GETREFNUMBER).toString());
				obj1200.addFields(49, CURRENCYCODE1200);
				obj1200.addFields(59, invokeGetterMethod(req, GETBNKMAKERID).toString().concat("|")
						.concat(invokeGetterMethod(req, GETBNKCHECKERID).toString()));
				obj1200.addFields(72, generateDateFormat(invokeGetterMethod(req, GETVALUEDATE), YYYYMMDD));
				res125 = invokeGetterMethod(req, "getRes125").toString();
				status = invokeGetterMethod(req, "getApprovestatus").toString();
				if (status.equalsIgnoreCase(CREDIT_EXCEPTION)) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, invokeGetterMethod(req, GETDEBITPOOLACC).toString());
					obj1200.addFields(103, substring.concat("3171160"));
				} else if (status.equalsIgnoreCase(DEBIT_EXCEPTION)) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, substring.concat("571139A"));
					obj1200.addFields(103, invokeGetterMethod(req, GETCREDITPOOLACC).toString());
				} else {
					obj1200.addFields(102, invokeGetterMethod(req, GETDEBITPOOLACC).toString());
					obj1200.addFields(103, invokeGetterMethod(req, GETCREDITPOOLACC).toString());
				}
				obj1200.addFields(123, CHANNELCODE);
				res126 = invokeGetterMethod(req, "getRes126").toString();
				if (status.equalsIgnoreCase(CREDIT_EXCEPTION)) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, invokeGetterMethod(req, GETDEBITTRN).toString());
					obj1200.addFields(126, invokeGetterMethod(req, GETCREDTRN).toString().concat("|")
							.concat(invokeGetterMethod(req, GETCREDITPOOLACC).toString()).concat(substring));
				} else if (status.equalsIgnoreCase(DEBIT_EXCEPTION)) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, invokeGetterMethod(req, GETDEBITTRN).toString().concat("|")
							.concat(invokeGetterMethod(req, GETDEBITPOOLACC).toString()).concat(substring));
					obj1200.addFields(126, invokeGetterMethod(req, GETCREDTRN).toString());
				} else {
					obj1200.addFields(125, invokeGetterMethod(req, GETDEBITTRN).toString());
					obj1200.addFields(126, invokeGetterMethod(req, GETCREDTRN).toString());
				}
				obj1200.addFields(127, invokeGetterMethod(req, GETTTUMID).toString());
				reqList.add(obj1200);
			} catch (Exception e) {
				handleException(e);
			}
		}
		return reqList;
	}
//////////FORMAT FOR REPEAT  REQUEST//////////////////////

////////CONDITION FOR RESPOSE START//////////////////////
	public static String[] responseCode(String cond) {
		String approvestatus = null;
		String status = null;
		List<String> notFailedTransaction = notFailedTransaction();
		List<String> creditTransaction2 = creditTransaction();
		List<String> debitTransaction2 = debitTransaction();
		List<String> repostTransaction3 = repostTransaction();
		// Extract the first three characters of the condition
		String condition = cond.substring(0, 3);

		// Perform checks based on the condition
		if (!notFailedTransaction.contains(condition)) {
			status = "L10";
			approvestatus = "Failed";
		} else if (condition.equalsIgnoreCase("000")) {
			status = "L10";
			approvestatus = "Success";
		} else if (condition.equalsIgnoreCase("913")) {
			status = "L10";
			approvestatus = "Duplicate";
		} else if (condition.equalsIgnoreCase("NULL") || condition.equalsIgnoreCase("911")) {
			// raise for enquiry
			status = "L5";
			approvestatus = "Timeout";
		} else if (creditTransaction2.contains(condition)) {
			// reposting
			status = "L6";
			approvestatus = CREDIT_EXCEPTION;
		} else if (debitTransaction2.contains(condition)) {
			// reposting
			status = "L6";
			approvestatus = DEBIT_EXCEPTION;
		} else if (repostTransaction3.contains(condition)) {
			status = "L6";
			approvestatus = "Enquiry_Exception";
		} else if (condition.equalsIgnoreCase("114")) {
			// reposting after account number update
			status = "L7";
			approvestatus = "Invalid";
		} else {
			status = "L11";
			approvestatus = "NA";
		}
		// Return the status and approval status as an array of strings
		return new String[] { status, approvestatus };
	}
//////CONDITION FOR RESPOSE END //////////////////////

	public static IsoV93Message responseList1210(IsoV93Message res) {
		ArrayList<IsoV93Message> reslist = new ArrayList<>();
		try {
			res.setRes002(res.getFieldbyNumber(2).getValue());
			res.setRes003(res.getFieldbyNumber(3).getValue());
			res.setRes004(res.getFieldbyNumber(4).getValue());
			res.setRes012(res.getFieldbyNumber(12).getValue());
			res.setRes017(res.getFieldbyNumber(17).getValue());
			res.setRes024(res.getFieldbyNumber(24).getValue());
			res.setRes032(res.getFieldbyNumber(32).getValue());
			res.setRes037(res.getFieldbyNumber(37).getValue());
			res.setRes038(res.getFieldbyNumber(38).getValue());
			res.setRes039(res.getFieldbyNumber(39).getValue());
			res.setRes048(res.getFieldbyNumber(48).getValue());
			res.setRes049(res.getFieldbyNumber(49).getValue());
			res.setRes059(res.getFieldbyNumber(59).getValue());
			res.setRes102(res.getFieldbyNumber(102).getValue());
			res.setRes123(res.getFieldbyNumber(123).getValue());
			res.setRes125(res.getFieldbyNumber(125).getValue());
			res.setRes126(res.getFieldbyNumber(126).getValue());
			res.setRes127(res.getFieldbyNumber(127).getValue());
			reslist.add(res);
		} catch (Exception e) {
			handleException(e);
		}
		return res;
	}

	public static <T> List<IsoV93Message> createisoFormat(List<T> requestList, String requestType) {
		List<IsoV93Message> request1200 = null;
		if (requestList != null && !requestList.isEmpty()) {
			if (requestType.equalsIgnoreCase("FRESH")) {
				request1200 = request1200(requestList);
			} else if (requestType.equalsIgnoreCase("ENQUIRY")) {
				request1200 = request1200Enquiry(requestList);
			} else if (requestType.equalsIgnoreCase("REPEAT")) {
				request1200 = request1201Repeat(requestList);
			}
		}
		return request1200;
	}

}
