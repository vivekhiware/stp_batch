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
import com.iso.config.IsoV93MessageRes;
import com.iso.config.MsgId;

public class IsoPackagerGlobal {
	private static final Logger logger = LoggerFactory.getLogger(IsoPackagerGlobal.class);
	public static String processCode1200 = "402010";
	public static String processCode1200Enq = "970000";
	public static String functionalCode1200 = "200";
	public static String acqinstitutionCode1200 = "11111111111";
	public static String txnamtEnq = "000000000000";
	public static String currencyCode1200 = "356";
	public static String channelCode = "RCN";
	public static String field125Req = "01ENQU";

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

	public static Date parseDate(Object dateObj, String pattern) {
		if (dateObj == null) {
			return null;
		}
		String dateString = dateObj.toString(); // Convert the Object to String
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			return dateFormat.parse(dateString); // Convert the string to a Date object
		} catch (Exception e) {
			System.err.println("Failed to parse date: " + dateString);
			return null;
		}
	}

	public static String generateDateFormatBk(Object dateObj, String pattern) {
		Date date = parseDate(dateObj, pattern);
		if (date != null) {
			SimpleDateFormat outputFormat = new SimpleDateFormat(pattern);
			return outputFormat.format(date); // Format and return the date as string
		}
		return null; // Return null if the date could not be parsed
	}

	public static String generateDateFormat(Object dateObj, String pattern) {
		SimpleDateFormat outputFormat = new SimpleDateFormat(pattern);
		return outputFormat.format(dateObj); // Format and return the date as string
	}

	public static String appendAndFixLength(String amount) {
		String result = amount;
		if (result.length() > 12) {
			result = result.substring(0, 12);
		}
		while (result.length() < 12) {
			result = "0" + result;
		}
		return result;
	}

	private static Object invokeGetterMethod(Object obj, String methodName) {
		try {
			Method method = obj.getClass().getMethod(methodName);
			return method.invoke(obj);
		} catch (Exception e) {
			System.err.println("Failed to invoke method " + methodName + ": " + e.getMessage());
			return null;
		}
	}

	//////////////////// FORMAT FOR FRESH REQUEST//////////////////////
	public static <T> List<IsoV93Message> request1200(List<T> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<>();
		IsoV93Message obj1200 = null;
		for (T req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, invokeGetterMethod(req, "getAccountnumber").toString());
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(invokeGetterMethod(req, "getTxnamount").toString()));
				obj1200.addFields(12, generateDateFormat(invokeGetterMethod(req, "getValuedate"), "yyMMddHHmmss"));
				obj1200.addFields(17, generateDateFormat(invokeGetterMethod(req, "getValuedate"), "MMdd"));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, invokeGetterMethod(req, "getRefnumber").toString());
				obj1200.addFields(49, currencyCode1200);
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateDateFormat(invokeGetterMethod(req, "getValuedate"), "yyyyMMdd"));
				obj1200.addFields(102, invokeGetterMethod(req, "getDebitpoolacc").toString());
				obj1200.addFields(103, invokeGetterMethod(req, "getCreditpoolacc").toString());
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, invokeGetterMethod(req, "getDebittrn").toString());
				obj1200.addFields(126, invokeGetterMethod(req, "getCredtrn").toString());
				obj1200.addFields(127, invokeGetterMethod(req, "getTtumid").toString());
				reqList.add(obj1200);
			} catch (Exception e) {
				handleException(e);
			} finally {
				obj1200 = null;
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
				obj1200.addFields(2, invokeGetterMethod(req, "getAccountnumber").toString());
				obj1200.addFields(3, processCode1200Enq);
				obj1200.addFields(4, txnamtEnq);
				obj1200.addFields(12, generateDateFormat(invokeGetterMethod(req, "getValuedate"), "yyMMddHHmmss"));
				obj1200.addFields(17, generateDateFormat(invokeGetterMethod(req, "getValuedate"), "MMdd"));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, invokeGetterMethod(req, "getRefnumber").toString());
				obj1200.addFields(49, currencyCode1200);
//			obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
//			obj1200.addFields(59, invokeGetterMethod(req, "getBnkmakerid").toString().concat("|").concat(invokeGetterMethod(req, "getBnkcheckerid").toString()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateDateFormat(invokeGetterMethod(req, "getValuedate"), "yyyyMMdd"));
				status = invokeGetterMethod(req, "getApprovestatus").toString();
				if (status.equalsIgnoreCase("Debit_Exception")) {
					obj1200.addFields(102, invokeGetterMethod(req, "getDebitpoolacc").toString());
				} else if (status.equalsIgnoreCase("Credit_Exception")) {
					obj1200.addFields(102, invokeGetterMethod(req, "getCreditpoolacc").toString());
				}
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, field125Req);
				obj1200.addFields(126, invokeGetterMethod(req, "getCredtrn").toString());
				obj1200.addFields(127, invokeGetterMethod(req, "getTtumid").toString());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				handleException(e);
			} finally {
				obj1200 = null;
			}
		}
		return reqList;
	}
//////////////  FORMAT FOR ENQUIRY  REQUEST////////////////////

////////////  FORMAT FOR REPEAT  REQUEST//////////////////////
	public static <T> List<IsoV93Message> request1201Repeat(List<T> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		String res125 = null;
		String res126 = null;
		String status = null;
		for (T req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1201);
				obj1200.addFields(2, invokeGetterMethod(req, "getAccountnumber").toString());
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(invokeGetterMethod(req, "getTxnamount").toString()));
				obj1200.addFields(12, generateDateFormat(invokeGetterMethod(req, "getValuedate"), "yyMMddHHmmss"));
				obj1200.addFields(17, generateDateFormat(invokeGetterMethod(req, "getValuedate"), "MMdd"));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, invokeGetterMethod(req, "getRefnumber").toString());
				obj1200.addFields(49, currencyCode1200);
//				obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateDateFormat(invokeGetterMethod(req, "getValuedate"), "yyyyMMdd"));
				res125 = invokeGetterMethod(req, "getRes125").toString();
				status = invokeGetterMethod(req, "getApprovestatus").toString();
				if (status.equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, invokeGetterMethod(req, "getDebitpoolacc").toString());
					obj1200.addFields(103, substring.concat("3171160"));
				} else if (status.equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, substring.concat("571139A"));
					obj1200.addFields(103, invokeGetterMethod(req, "getCreditpoolacc").toString());
				} else {
					obj1200.addFields(102, invokeGetterMethod(req, "getDebitpoolacc").toString());
					obj1200.addFields(103, invokeGetterMethod(req, "getCreditpoolacc").toString());
				}
				obj1200.addFields(123, channelCode);
				res126 = invokeGetterMethod(req, "getRes126").toString();
				if (status.equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, invokeGetterMethod(req, "getDebittrn").toString());
					obj1200.addFields(126, invokeGetterMethod(req, "getCredtrn").toString().concat("|")
							.concat(invokeGetterMethod(req, "getCreditpoolacc").toString()).concat(substring));
				} else if (status.equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, invokeGetterMethod(req, "getDebittrn").toString().concat("|")
							.concat(invokeGetterMethod(req, "getDebitpoolacc").toString()).concat(substring));
					obj1200.addFields(126, invokeGetterMethod(req, "getCredtrn").toString());
				} else {
					obj1200.addFields(125, invokeGetterMethod(req, "getDebittrn").toString());
					obj1200.addFields(126, invokeGetterMethod(req, "getCredtrn").toString());
				}
				obj1200.addFields(127, invokeGetterMethod(req, "getTtumid").toString());
				reqList.add(obj1200);

			} catch (Exception e) {
				handleException(e);
			} finally {
				obj1200 = null;
				res125 = null;
				res126 = null;
				status = null;
			}
		}
		return reqList;
	}
//////////FORMAT FOR REPEAT  REQUEST//////////////////////

////////CONDITION FOR RESPOSE START//////////////////////
	public static String[] responseCode(String cond) {
		String approvestatus = "NA";
		String status = "NA";
		ArrayList<String> notFailedTransaction = notFailedTransaction();
		ArrayList<String> creditTransaction2 = creditTransaction();
		ArrayList<String> debitTransaction2 = debitTransaction();
		ArrayList<String> repostTransaction3 = repostTransaction();
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
			approvestatus = "Credit_Exception";
		} else if (debitTransaction2.contains(condition)) {
			// reposting
			status = "L6";
			approvestatus = "Debit_Exception";
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

	public static void main(String[] args) {
		IsoPackagerGlobal global = new IsoPackagerGlobal();
		String dt = global.generateDateFormat("2025-03-02", "yyMMddHHmmss");
		System.out.println(dt);
//		global.request1200(null);
//		global.request1200Enquiry(null);
//		global.request1200Repeat(null);
//		String[] responseCode = global.responseCode("114|SUCCESS");
//		System.out.println(responseCode[0]);
//		System.out.println(responseCode[1]);
	}

	public static IsoV93MessageRes responseList1210(IsoV93MessageRes res) {
		ArrayList<IsoV93MessageRes> reslist = new ArrayList<IsoV93MessageRes>();
		int count1 = 1;
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
			count1++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
