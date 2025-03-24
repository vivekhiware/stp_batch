package com.stp.autojob;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.jpos.iso.ISOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iso.config.IsoV93Message;
import com.iso.config.IsoV93MessageRes;
import com.iso.config.MsgId;
import com.stp.model.db1.STP_IMPS_IW_NETWORK_DECLINE;
import com.stp.model.db1.STP_IMPS_NONCBS_IW;
import com.stp.model.db1.STP_IMPS_NONCBS_OW;
import com.stp.model.db1.STP_IMPS_NONNPCI_IW;
import com.stp.model.db1.STP_IMPS_NONNPCI_OW;
import com.stp.model.db1.STP_IMPS_NTSL_NETSET_TTUM;
import com.stp.model.db1.STP_IMPS_OW_NETWORK_DECLINE;
import com.stp.model.db1.STP_IMPS_TCC_DATA_IW;

public class IsoFormatter {
	private static final Logger logger = LoggerFactory.getLogger(IsoFormatter.class);
	public static String processCode1200 = "402010";
	public static String processCode1200Enq = "970000";
	public static String functionalCode1200 = "200";
	public static String acqinstitutionCode1200 = "11111111111";
	public static String txnamtEnq = "000000000000";
	public static String currencyCode1200 = "356";
	public static String channelCode = "RCN";
	public static String field125Req = "01ENQU";

	public static String appendAndFixLength(String amount) {
		// Step 1: Append "0" to the amount
		String result = amount;
		// Step 2: If the length exceeds 12 characters, truncate it to 12 characters
		if (result.length() > 12) {
			result = result.substring(0, 12);
		}
		// Step 3: If the length is less than 12 characters, pad with leading zeros
		while (result.length() < 12) {
			result = "0" + result;
		}
		return result;
	}

	public static String generateYYMMDDHHMMSS() {
		LocalDateTime now = LocalDateTime.now();
		// Define the custom format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
		// Format the current date and time
		String formattedDate = now.format(formatter);
		return formattedDate;
	}

	public static String generateMMDD() {
		LocalDateTime now = LocalDateTime.now();
		// Define the custom format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");

		// Format the current date and time
		String formattedDate = now.format(formatter);
		return formattedDate;
	}

	public static String generateYYYYMMDD() {
		LocalDateTime now = LocalDateTime.now();
		// Define the custom format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYYMMdd");

		// Format the current date and time
		String formattedDate = now.format(formatter);
		return formattedDate;
	}

//	 passing date as input
//	  format req YYMMDDHHMMSS INPUT FROM  "2025-02-12"
//	 format req MMDD INPUT FROM  "2025-02-12"
//	format req YYMMDD INPUT FROM  "2025-02-12"
	public static String generateYYMMDDHHMMSS(Date input) {
		String formattedDate = null;
		try {
			SimpleDateFormat inputformat = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = inputformat.parse(input);
			SimpleDateFormat outputformat = new SimpleDateFormat("yyMMddHHmmss");
			formattedDate = outputformat.format(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formattedDate;
	}

	public static String generateMMDD(Date input) {
		String formattedDate = null;
		try {
			SimpleDateFormat inputformat = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = inputformat.parse(input);
			SimpleDateFormat outputformat = new SimpleDateFormat("MMdd");
			formattedDate = outputformat.format(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formattedDate;
	}

	public static String generateYYYYMMDD(Date input) {
		String formattedDate = null;
		try {
			SimpleDateFormat inputformat = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = inputformat.parse(input);
			SimpleDateFormat outputformat = new SimpleDateFormat("yyyyMMdd");
			formattedDate = outputformat.format(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formattedDate;
	}

	public static void main(String[] args) {
		IsoFormatter test = new IsoFormatter();
//		System.out.println("" + test.generateYYMMDDHHMMSS("2025-02-12");
//		System.out.println("" + test.generateYYMM("2025-02-12"));
//		System.out.println("" + test.generateYYYYMMDD("2025-02-12"));
	}

	public static void responsePrint(List<IsoV93MessageRes> reslist, String type) {

		for (IsoV93MessageRes isoV93MessageRes : reslist) {
			String responseCode002 = isoV93MessageRes.getFieldbyNumber(2).getValue();
			String responseCode003 = isoV93MessageRes.getFieldbyNumber(3).getValue();
			String responseCode004 = isoV93MessageRes.getFieldbyNumber(4).getValue();
			String responseCode012 = isoV93MessageRes.getFieldbyNumber(12).getValue();
			String responseCode017 = isoV93MessageRes.getFieldbyNumber(17).getValue();
			String responseCode024 = isoV93MessageRes.getFieldbyNumber(24).getValue();
			String responseCode032 = isoV93MessageRes.getFieldbyNumber(32).getValue();
			String responseCode037 = isoV93MessageRes.getFieldbyNumber(37).getValue();
			String responseCode038 = isoV93MessageRes.getFieldbyNumber(38).getValue();
			String responseCode039 = isoV93MessageRes.getFieldbyNumber(39).getValue();
			String responseCode048 = isoV93MessageRes.getFieldbyNumber(48).getValue();
			String responseCode049 = isoV93MessageRes.getFieldbyNumber(49).getValue();
			String responseCode059 = isoV93MessageRes.getFieldbyNumber(59).getValue();
			String responseCode102 = isoV93MessageRes.getFieldbyNumber(102).getValue();
			String responseCode123 = isoV93MessageRes.getFieldbyNumber(123).getValue();
			String responseCode125 = isoV93MessageRes.getFieldbyNumber(125).getValue();
			String responseCode126 = isoV93MessageRes.getFieldbyNumber(126).getValue();
			String responseCode127 = isoV93MessageRes.getFieldbyNumber(127).getValue();
			System.err.println("****** FINACLE START RESPONSE " + type + " *********" + responseCode037);
			System.out.println("Field 002 - " + responseCode002);
			System.out.println("Field 003 - " + responseCode003);
			System.out.println("Field 004 - " + responseCode004);
			System.out.println("Field 012 - " + responseCode012);
			System.out.println("Field 024 - " + responseCode024);
			System.out.println("Field 032 - " + responseCode032);
			System.out.println("Field 037 - " + responseCode037);
			System.out.println("Field 038 - " + responseCode038);
			System.out.println("Field 039 - " + responseCode039);
			System.out.println("Field 048 - " + responseCode048);
			System.out.println("Field 049 - " + responseCode049);
			System.out.println("Field 059 - " + responseCode059);
			System.out.println("Field 102 - " + responseCode102);
			System.out.println("Field 123 - " + responseCode123);
			System.out.println("Field 125 - " + responseCode125);
			System.out.println("Field 126 - " + responseCode126);
			System.out.println("Field 127 - " + responseCode127);
			System.err.println("******  END FINACLE RESPONSE " + type + "*********");

		}
	}

	public static List<IsoV93Message> request1200STP_IMPS_NONCBS_IW(List<STP_IMPS_NONCBS_IW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONCBS_IW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber().trim());
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()).trim());
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber().trim());
				obj1200.addFields(49, currencyCode1200);
//				obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				obj1200.addFields(102, req.getDebitpoolacc().trim());
				obj1200.addFields(103, req.getCreditpoolacc().trim());
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, req.getDebittrn().trim());
				obj1200.addFields(126, req.getCredtrn().trim());
				obj1200.addFields(127, req.getTtumid().trim());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static ArrayList<IsoV93MessageRes> responseList1210(List<IsoV93MessageRes> responseIso) {
		ArrayList<IsoV93MessageRes> reslist = new ArrayList<IsoV93MessageRes>();
		int count1 = 1;
		for (IsoV93MessageRes res : responseIso) {
			try {
				// System.err.println(" response " + count1);
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
		}
		return reslist;
	}

//	 repeat transaction format
	public static List<IsoV93Message> request201STP_IMPS_NONCBS_IWRepeat(List<STP_IMPS_NONCBS_IW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONCBS_IW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1201);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()));
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//				obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate()));
				String res125 = req.getRes125() != null ? req.getRes125() : "NA";
				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, substring.concat("3171160"));
				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, substring.concat("571139A"));
					obj1200.addFields(103, req.getCreditpoolacc());
				} else {
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, req.getCreditpoolacc());
				}
				obj1200.addFields(123, channelCode);

				String res126 = req.getRes126() != null ? req.getRes126() : "NA";
				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126,
							req.getCredtrn().concat("|").concat(req.getCreditpoolacc()).concat(substring));

				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125,
							req.getDebittrn().concat("|").concat(req.getDebitpoolacc()).concat(substring));
					obj1200.addFields(126, req.getCredtrn());
				} else {
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126, req.getCredtrn());
				}
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

//	 repeat transaction format
	public static List<IsoV93Message> reques1200STP_IMPS_NONCBS_IWEnquiry(List<STP_IMPS_NONCBS_IW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONCBS_IW req : array) {
			try {
				try {
					obj1200 = new IsoV93Message(MsgId.MTID_1200);
					obj1200.addFields(2, req.getAccountnumber());
					obj1200.addFields(3, processCode1200Enq);
					obj1200.addFields(4, txnamtEnq);
					obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
					obj1200.addFields(17, generateMMDD(req.getValuedate()));
					obj1200.addFields(24, functionalCode1200);
					obj1200.addFields(32, acqinstitutionCode1200);
					obj1200.addFields(37, req.getRefnumber());
					obj1200.addFields(49, currencyCode1200);
//					obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
					obj1200.addFields(59, "5115502|5115612");
					obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
					if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
						obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
					} else if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
						obj1200.addFields(102, req.getCreditpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
					} else {
						obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
					}
					obj1200.addFields(123, channelCode);
					obj1200.addFields(125, field125Req);
					obj1200.addFields(126, req.getCredtrn());
					obj1200.addFields(127, req.getTtumid());
					reqList.add(obj1200);
					obj1200 = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static void logMessage(int count, byte[] msgBytes) {
		logger.info("******************** REQUEST START " + count + "********************");
		System.out.println(Hex.encodeHex(msgBytes));
		String hexdump = ISOUtil.hexdump(msgBytes);
		logger.info(hexdump);
		logger.info("******************** REQUEST END " + count + "********************");
	}

	// tcc ttum
	public static List<IsoV93Message> request1200STP_IMPS_TCC_DATA_IW(List<STP_IMPS_TCC_DATA_IW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_TCC_DATA_IW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber().trim());
				// obj1200.addFields(3, processCode1200);
				obj1200.addFields(3, processCode1200);
				System.out.println("datavalue " + req.getValuedate());
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()).trim());
//				obj1200.addFields(12, generateYYMMDDHHMMSS());
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
//				obj1200.addFields(17, generateMMDD());
				obj1200.addFields(17, generateMMDD(req.getValuedate()));

				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber().trim());
				obj1200.addFields(49, currencyCode1200);
//				obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
//				obj1200.addFields(72, generateYYYYMMDD());
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate()));
				obj1200.addFields(102, req.getDebitpoolacc().trim());
				obj1200.addFields(103, req.getCreditpoolacc().trim());
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, req.getDebittrn().trim());
				obj1200.addFields(126, req.getCredtrn().trim());
				obj1200.addFields(127, req.getTtumid().trim());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1200STP_IMPS_TCC_DATA_IWEnquiry(List<STP_IMPS_TCC_DATA_IW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_TCC_DATA_IW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200Enq);
				obj1200.addFields(4, txnamtEnq);
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//				obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					obj1200.addFields(102, req.getCreditpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				}
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, field125Req);
				obj1200.addFields(126, req.getCredtrn());
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1201STP_IMPS_TCC_DATA_IWRepeat(List<STP_IMPS_TCC_DATA_IW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_TCC_DATA_IW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1201);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()));
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//				obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate()));
				String res125 = req.getRes125() != null ? req.getRes125() : "NA";

				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, substring.concat("3171160"));
				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, substring.concat("571139A"));
					obj1200.addFields(103, req.getCreditpoolacc());
				} else {
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, req.getCreditpoolacc());
				}
				obj1200.addFields(123, channelCode);

				String res126 = req.getRes126() != null ? req.getRes126() : "NA";
				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126,
							req.getCredtrn().concat("|").concat(req.getCreditpoolacc()).concat(substring));

				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125,
							req.getDebittrn().concat("|").concat(req.getDebitpoolacc()).concat(substring));
					obj1200.addFields(126, req.getCredtrn());
				} else {
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126, req.getCredtrn());
				}
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	// STP_IMPS_NONNPCI_IW
	public static List<IsoV93Message> request1200STP_IMPS_NONNPCI_IW(List<STP_IMPS_NONNPCI_IW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONNPCI_IW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber().trim());
				// obj1200.addFields(3, processCode1200);
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()).trim());
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber().trim());
				obj1200.addFields(49, currencyCode1200);
//					obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				obj1200.addFields(102, req.getDebitpoolacc().trim());
				obj1200.addFields(103, req.getCreditpoolacc().trim());
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, req.getDebittrn().trim());
				obj1200.addFields(126, req.getCredtrn().trim());
				obj1200.addFields(127, req.getTtumid().trim());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1200STP_IMPS_NONNPCI_IWEnquiry(List<STP_IMPS_NONNPCI_IW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONNPCI_IW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200Enq);
				obj1200.addFields(4, txnamtEnq);
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//					obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					obj1200.addFields(102, req.getCreditpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				}
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, field125Req);
				obj1200.addFields(126, req.getCredtrn());
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1201STP_IMPS_NONNPCI_IWRepeat(List<STP_IMPS_NONNPCI_IW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONNPCI_IW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1201);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()));
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//					obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate()));
				String res125 = req.getRes125() != null ? req.getRes125() : "NA";

				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, substring.concat("3171160"));
				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, substring.concat("571139A"));
					obj1200.addFields(103, req.getCreditpoolacc());
				} else {
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, req.getCreditpoolacc());
				}
				obj1200.addFields(123, channelCode);

				String res126 = req.getRes126() != null ? req.getRes126() : "NA";
				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126,
							req.getCredtrn().concat("|").concat(req.getCreditpoolacc()).concat(substring));

				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125,
							req.getDebittrn().concat("|").concat(req.getDebitpoolacc()).concat(substring));
					obj1200.addFields(126, req.getCredtrn());
				} else {
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126, req.getCredtrn());
				}
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}
	//

//	 NPCI_oW

	// STP_IMPS_NONNPCI_OW
	public static List<IsoV93Message> request1200STP_IMPS_NONNPCI_OW(List<STP_IMPS_NONNPCI_OW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONNPCI_OW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber().trim());
				// obj1200.addFields(3, processCode1200);
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()).trim());
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber().trim());
				obj1200.addFields(49, currencyCode1200);
//					obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				obj1200.addFields(102, req.getDebitpoolacc().trim());
				obj1200.addFields(103, req.getCreditpoolacc().trim());
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, req.getDebittrn().trim());
				obj1200.addFields(126, req.getCredtrn().trim());
				obj1200.addFields(127, req.getTtumid().trim());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1200STP_IMPS_NONNPCI_OWEnquiry(List<STP_IMPS_NONNPCI_OW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONNPCI_OW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200Enq);
				obj1200.addFields(4, txnamtEnq);
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//					obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					obj1200.addFields(102, req.getCreditpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				}
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, field125Req);
				obj1200.addFields(126, req.getCredtrn());
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1201STP_IMPS_NONNPCI_OWRepeat(List<STP_IMPS_NONNPCI_OW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONNPCI_OW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1201);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()));
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//					obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate()));
				String res125 = req.getRes125() != null ? req.getRes125() : "NA";

				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, substring.concat("3171160"));
				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, substring.concat("571139A"));
					obj1200.addFields(103, req.getCreditpoolacc());
				} else {
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, req.getCreditpoolacc());
				}
				obj1200.addFields(123, channelCode);

				String res126 = req.getRes126() != null ? req.getRes126() : "NA";
				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126,
							req.getCredtrn().concat("|").concat(req.getCreditpoolacc()).concat(substring));

				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125,
							req.getDebittrn().concat("|").concat(req.getDebitpoolacc()).concat(substring));
					obj1200.addFields(126, req.getCredtrn());
				} else {
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126, req.getCredtrn());
				}
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

// STP_IMPS_IW_NETWORK_DECLINE
	public static List<IsoV93Message> request1200STP_IMPS_IW_NETWORK_DECLINE(List<STP_IMPS_IW_NETWORK_DECLINE> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_IW_NETWORK_DECLINE req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber().trim());
				// obj1200.addFields(3, processCode1200);
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()).trim());
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber().trim());
				obj1200.addFields(49, currencyCode1200);
//					obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				obj1200.addFields(102, req.getDebitpoolacc().trim());
				obj1200.addFields(103, req.getCreditpoolacc().trim());
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, req.getDebittrn().trim());
				obj1200.addFields(126, req.getCredtrn().trim());
				obj1200.addFields(127, req.getTtumid().trim());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1200STP_IMPS_IW_NETWORK_DECLINEEnquiry(
			List<STP_IMPS_IW_NETWORK_DECLINE> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_IW_NETWORK_DECLINE req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200Enq);
				obj1200.addFields(4, txnamtEnq);
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//					obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					obj1200.addFields(102, req.getCreditpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				}
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, field125Req);
				obj1200.addFields(126, req.getCredtrn());
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1201STP_IMPS_IW_NETWORK_DECLINERepeat(
			List<STP_IMPS_IW_NETWORK_DECLINE> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_IW_NETWORK_DECLINE req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1201);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()));
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//					obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate()));
				String res125 = req.getRes125() != null ? req.getRes125() : "NA";

				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, substring.concat("3171160"));
				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, substring.concat("571139A"));
					obj1200.addFields(103, req.getCreditpoolacc());
				} else {
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, req.getCreditpoolacc());
				}
				obj1200.addFields(123, channelCode);

				String res126 = req.getRes126() != null ? req.getRes126() : "NA";
				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126,
							req.getCredtrn().concat("|").concat(req.getCreditpoolacc()).concat(substring));

				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125,
							req.getDebittrn().concat("|").concat(req.getDebitpoolacc()).concat(substring));
					obj1200.addFields(126, req.getCredtrn());
				} else {
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126, req.getCredtrn());
				}
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	//

	// STP_IMPS_OW_NETWORK_DECLINE
	public static List<IsoV93Message> request1200STP_IMPS_OW_NETWORK_DECLINE(List<STP_IMPS_OW_NETWORK_DECLINE> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_OW_NETWORK_DECLINE req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber().trim());
				// obj1200.addFields(3, processCode1200);
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()).trim());
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber().trim());
				obj1200.addFields(49, currencyCode1200);
//						obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				obj1200.addFields(102, req.getDebitpoolacc().trim());
				obj1200.addFields(103, req.getCreditpoolacc().trim());
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, req.getDebittrn().trim());
				obj1200.addFields(126, req.getCredtrn().trim());
				obj1200.addFields(127, req.getTtumid().trim());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1200STP_IMPS_OW_NETWORK_DECLINEEnquiry(
			List<STP_IMPS_OW_NETWORK_DECLINE> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_OW_NETWORK_DECLINE req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200Enq);
				obj1200.addFields(4, txnamtEnq);
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//						obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					obj1200.addFields(102, req.getCreditpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				}
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, field125Req);
				obj1200.addFields(126, req.getCredtrn());
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1201STP_IMPS_OW_NETWORK_DECLINERepeat(
			List<STP_IMPS_OW_NETWORK_DECLINE> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_OW_NETWORK_DECLINE req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1201);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()));
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//						obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate()));
				String res125 = req.getRes125() != null ? req.getRes125() : "NA";

				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, substring.concat("3171160"));
				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, substring.concat("571139A"));
					obj1200.addFields(103, req.getCreditpoolacc());
				} else {
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, req.getCreditpoolacc());
				}
				obj1200.addFields(123, channelCode);

				String res126 = req.getRes126() != null ? req.getRes126() : "NA";
				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126,
							req.getCredtrn().concat("|").concat(req.getCreditpoolacc()).concat(substring));

				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125,
							req.getDebittrn().concat("|").concat(req.getDebitpoolacc()).concat(substring));
					obj1200.addFields(126, req.getCredtrn());
				} else {
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126, req.getCredtrn());
				}
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////

	// STP_IMPS_NTSL_NETSET_TTUM
	public static List<IsoV93Message> request1200STP_IMPS_NTSL_NETSET_TTUM(List<STP_IMPS_NTSL_NETSET_TTUM> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NTSL_NETSET_TTUM req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber().trim());
				// obj1200.addFields(3, processCode1200);
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()).trim());
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber().trim());
				obj1200.addFields(49, currencyCode1200);
//						obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				obj1200.addFields(102, req.getDebitpoolacc().trim());
				obj1200.addFields(103, req.getCreditpoolacc().trim());
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, req.getDebittrn().trim());
				obj1200.addFields(126, req.getCredtrn().trim());
				obj1200.addFields(127, req.getTtumid().trim());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1200STP_IMPS_NTSL_NETSET_TTUMEnquiry(
			List<STP_IMPS_NTSL_NETSET_TTUM> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NTSL_NETSET_TTUM req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200Enq);
				obj1200.addFields(4, txnamtEnq);
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//						obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					obj1200.addFields(102, req.getCreditpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				}
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, field125Req);
				obj1200.addFields(126, req.getCredtrn());
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1201STP_IMPS_NTSL_NETSET_TTUMRepeat(
			List<STP_IMPS_NTSL_NETSET_TTUM> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NTSL_NETSET_TTUM req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1201);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()));
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//						obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate()));
				String res125 = req.getRes125() != null ? req.getRes125() : "NA";

				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, substring.concat("3171160"));
				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, substring.concat("571139A"));
					obj1200.addFields(103, req.getCreditpoolacc());
				} else {
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, req.getCreditpoolacc());
				}
				obj1200.addFields(123, channelCode);

				String res126 = req.getRes126() != null ? req.getRes126() : "NA";
				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126,
							req.getCredtrn().concat("|").concat(req.getCreditpoolacc()).concat(substring));

				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125,
							req.getDebittrn().concat("|").concat(req.getDebitpoolacc()).concat(substring));
					obj1200.addFields(126, req.getCredtrn());
				} else {
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126, req.getCredtrn());
				}
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////

	// STP_IMPS_NONCBS_OW
	public static List<IsoV93Message> request1200STP_IMPS_NONCBS_OW(List<STP_IMPS_NONCBS_OW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONCBS_OW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber().trim());
				// obj1200.addFields(3, processCode1200);
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()).trim());
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber().trim());
				obj1200.addFields(49, currencyCode1200);
//						obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				obj1200.addFields(102, req.getDebitpoolacc().trim());
				obj1200.addFields(103, req.getCreditpoolacc().trim());
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, req.getDebittrn().trim());
				obj1200.addFields(126, req.getCredtrn().trim());
				obj1200.addFields(127, req.getTtumid().trim());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1200STP_IMPS_NONCBS_OWEnquiry(List<STP_IMPS_NONCBS_OW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONCBS_OW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1200);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200Enq);
				obj1200.addFields(4, txnamtEnq);
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//						obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate())); // 20230704
				if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					obj1200.addFields(102, req.getCreditpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				} else {
					obj1200.addFields(102, req.getDebitpoolacc()); // CUSTOMER (DR/CR) ACCOUNT NUMBER
				}
				obj1200.addFields(123, channelCode);
				obj1200.addFields(125, field125Req);
				obj1200.addFields(126, req.getCredtrn());
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

	public static List<IsoV93Message> request1201STP_IMPS_NONCBS_OWRepeat(List<STP_IMPS_NONCBS_OW> array) {
		ArrayList<IsoV93Message> reqList = new ArrayList<IsoV93Message>();
		IsoV93Message obj1200 = null;
		for (STP_IMPS_NONCBS_OW req : array) {
			try {
				obj1200 = new IsoV93Message(MsgId.MTID_1201);
				obj1200.addFields(2, req.getAccountnumber());
				obj1200.addFields(3, processCode1200);
				obj1200.addFields(4, appendAndFixLength(req.getTxnamount()));
				obj1200.addFields(12, generateYYMMDDHHMMSS(req.getValuedate()));
				obj1200.addFields(17, generateMMDD(req.getValuedate()));
				obj1200.addFields(24, functionalCode1200);
				obj1200.addFields(32, acqinstitutionCode1200);
				obj1200.addFields(37, req.getRefnumber());
				obj1200.addFields(49, currencyCode1200);
//						obj1200.addFields(59, req.getBnkmakerid().concat("|").concat(req.getBnkcheckerid()));
				obj1200.addFields(59, "5115502|5115612");
				obj1200.addFields(72, generateYYYYMMDD(req.getValuedate()));
				String res125 = req.getRes125() != null ? req.getRes125() : "NA";

				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, substring.concat("3171160"));
				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res125.lastIndexOf("|");
					String substring = res125.substring(lastIndexOf + 1);
					obj1200.addFields(102, substring.concat("571139A"));
					obj1200.addFields(103, req.getCreditpoolacc());
				} else {
					obj1200.addFields(102, req.getDebitpoolacc());
					obj1200.addFields(103, req.getCreditpoolacc());
				}
				obj1200.addFields(123, channelCode);

				String res126 = req.getRes126() != null ? req.getRes126() : "NA";
				if (req.getApprovestatus().equalsIgnoreCase("Credit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126,
							req.getCredtrn().concat("|").concat(req.getCreditpoolacc()).concat(substring));

				} else if (req.getApprovestatus().equalsIgnoreCase("Debit_Exception")) {
					int lastIndexOf = res126.lastIndexOf("|");
					String substring = res126.substring(lastIndexOf + 1);
					obj1200.addFields(125,
							req.getDebittrn().concat("|").concat(req.getDebitpoolacc()).concat(substring));
					obj1200.addFields(126, req.getCredtrn());
				} else {
					obj1200.addFields(125, req.getDebittrn());
					obj1200.addFields(126, req.getCredtrn());
				}
				obj1200.addFields(127, req.getTtumid());
				reqList.add(obj1200);
				obj1200 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reqList;
	}

}
