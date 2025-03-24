package com.iso.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.lang3.ArrayUtils;

public class IsoV93Message implements IsoMessage {

	static Logger log = Logger.getLogger(IsoV93Message.class.getName());

	static Map<Integer, IsoType> ISO_FIELD_TYPE_MAP = null;
	static Map<Integer, String> ISO_FIELD_NAME_MAP = null;
	static Map<Integer, Integer> ISO_FIELD_SIZE_MAP = null;

	public List<IsoField> fields = new ArrayList<IsoField>();
	private MsgId msgId;
	private String binaryBitMap = "10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	private String dataString;

	public IsoV93Message(MsgId msgId) throws Exception {
		this.msgId = msgId;
	}

	public IsoV93Message(byte[] msgBytes) throws Exception {
		byte[] byteMsgId = ArrayUtils.subarray(msgBytes, 4, 8);
		byte[] byteBitMap = ArrayUtils.subarray(msgBytes, 8, 24);
		byte[] byteData = ArrayUtils.subarray(msgBytes, 24, msgBytes.length);
		int currIndex = 0;
		ArrayUtils.reverse(byteBitMap);
		this.msgId = MsgId.getMsgId(new String(byteMsgId));
		this.binaryBitMap = BinaryCodec.toAsciiString(byteBitMap);
		// log.info("Response >> Msg Id:"+this.msgId+">>BitMAp:"+this.binaryBitMap);
		this.dataString = new String(byteData);

		List<Integer> bitIntArr = getValuesIndexInBitMap();
		// log.info(bitIntArr);
		for (int i : bitIntArr) {
			if (i == 1)
				continue;
			String fieldValue = "";
			int fieldSize = 0;
			IsoType fieldType = ISO_FIELD_TYPE_MAP.get(i);
			// log.info("Field No: " + i + " Field Name: " + ISO_FIELD_NAME_MAP.get(i) + "
			// Field Type: " + ISO_FIELD_TYPE_MAP.get(i) + " Current Index: " + currIndex );
			if (fieldType.equals(IsoType.FIXED)) {
				fieldSize = ISO_FIELD_SIZE_MAP.get(i);
				fieldValue = this.dataString.substring(currIndex, currIndex + fieldSize);
				currIndex = currIndex + fieldSize;
				// log.info("Field Size: "+fieldSize+" Field Value: "+fieldValue);
			} else if (fieldType.equals(IsoType.AMOUNT)) {
				fieldSize = ISO_FIELD_SIZE_MAP.get(i);
				fieldValue = this.dataString.substring(currIndex, currIndex + fieldSize);
				currIndex = currIndex + fieldSize;
				// log.info("Field Size: "+fieldSize+" Field Value: "+fieldValue);
			} else if (fieldType.equals(IsoType.LVAR)) {
				fieldSize = Integer.parseInt(this.dataString.substring(currIndex, currIndex + 2));
				currIndex = currIndex + 1;
				fieldValue = this.dataString.substring(currIndex, currIndex + fieldSize);
				currIndex = currIndex + fieldSize;
				// log.info("Field Size: "+fieldSize+" Field Value: "+fieldValue);
			} else if (fieldType.equals(IsoType.LLVAR)) {
				fieldSize = Integer.parseInt(this.dataString.substring(currIndex, currIndex + 2));
				currIndex = currIndex + 2;
				fieldValue = this.dataString.substring(currIndex, currIndex + fieldSize);
				currIndex = currIndex + fieldSize;
				// log.info("Field Size: "+fieldSize+" Field Value: "+fieldValue);
			} else if (fieldType.equals(IsoType.LLLVAR)) {
				fieldSize = Integer.parseInt(this.dataString.substring(currIndex, currIndex + 3));
				currIndex = currIndex + 3;
				fieldValue = this.dataString.substring(currIndex, currIndex + fieldSize);
				currIndex = currIndex + fieldSize;
				// log.info("Field Size: "+fieldSize+" Field Value: "+fieldValue);
			}
			this.fields
					.add(new IsoField(i, ISO_FIELD_NAME_MAP.get(i), ISO_FIELD_TYPE_MAP.get(i), fieldSize, fieldValue));
		}
	}

	public void setMsgId(MsgId msgId) {
		this.msgId = msgId;
	}

	public MsgId getMsgId() {
		return msgId;
	}

	public void addFields(int index, String value) throws Exception {
		if (value == null || (ISO_FIELD_TYPE_MAP.get(index).equals(IsoType.FIXED)
				&& value.length() != ISO_FIELD_SIZE_MAP.get(index)))
			throw new Exception("Invalid Field Value or Index");
		else
			this.fields.add(new IsoField(index, ISO_FIELD_NAME_MAP.get(index), ISO_FIELD_TYPE_MAP.get(index),
					ISO_FIELD_SIZE_MAP.get(index), value));
	}

	public IsoField getFieldbyNumber(int no) {
		IsoField field = null;
		for (IsoField f : fields)
			if (no == f.getIndex())
				field = f;
		return field;
	}

	public List<Integer> getValuesIndexInBitMap() throws Exception {
		String str = this.binaryBitMap;
		List<Integer> listArr = new ArrayList<Integer>();
		char[] arrChar = str.toCharArray();
		int i = 1;
		for (char c : arrChar) {
			if (c == '1' || c == '0') {
				if (c == '1') {
					listArr.add(i);
				}
				i++;
			} else {
				throw new Exception("Bitmap String is not correct");
			}
		}
		return listArr;
	}

	public byte[] generateMessage() throws Exception {
		byte[] msgByte = null;
		Collections.sort(fields);
		StringBuilder sbMsgData = new StringBuilder();
		char[] charArrBitMap = binaryBitMap.toCharArray();

		for (IsoField f : fields) {
			charArrBitMap[f.getIndex() - 1] = '1';

			if (f.getIsoType().equals(IsoType.FIXED)) {
				sbMsgData.append(f.getValue());
			} else if (f.getIsoType().equals(IsoType.AMOUNT)) {
				String value = String.format("%16s", f.getValue()).replace(" ", "0");
				sbMsgData.append(value);
			} else if (f.getIsoType().equals(IsoType.LLLVAR)) {
				String length = String.format("%3s", f.getValue().length()).replace(" ", "0");
				sbMsgData.append(length + f.getValue());
			} else if (f.getIsoType().equals(IsoType.LLVAR)) {
				String length = String.format("%2s", f.getValue().length()).replace(" ", "0");
				sbMsgData.append(length + f.getValue());
			} else if (f.getIsoType().equals(IsoType.LVAR)) {
				String length = String.format("%1s", f.getValue().length()).replace(" ", "0");
				sbMsgData.append(length + f.getValue());
			} else {
				throw new Exception("ISO Type Not Defined");
			}
		}
		binaryBitMap = new String(charArrBitMap);
		dataString = sbMsgData.toString();
		byte[] baMsgId = msgId.getValue().getBytes();
		byte[] bytesHeader = "ISO015000080".getBytes();

		BinaryCodec obj = new BinaryCodec();
		byte[] baBinaryBitMap = obj.toByteArray(binaryBitMap);

		ArrayUtils.reverse(baBinaryBitMap);
		// test
		byte[] baDataString = dataString.getBytes();
		msgByte = new byte[baMsgId.length + baBinaryBitMap.length + baDataString.length];
		msgByte = ArrayUtils.addAll(bytesHeader, baMsgId);
		msgByte = ArrayUtils.addAll(msgByte, baBinaryBitMap);
		msgByte = ArrayUtils.addAll(msgByte, baDataString);
		System.out.println(msgByte.length);
		// test
//		byte[] baDataString = dataString.getBytes();
//		msgByte = new byte[baMsgId.length + baBinaryBitMap.length + baDataString.length];
//		msgByte = ArrayUtils.addAll(baMsgId, baBinaryBitMap);
//		msgByte = ArrayUtils.addAll(msgByte, baDataString);

//		System.err.println("Data String :  " + "0" + msgByte.length + dataString);

		return msgByte;
	}

	public void printMessage(String stan) throws Exception {
		Collections.sort(fields);
		System.out.println("****** FINACLE START REQUEST " + stan + " *********");
		for (IsoField f : fields) {
			System.out.println("Field" + f.getIndex() + " - " + f.getValue());
//			System.out.println(stan + " >> " + f.getIndex() + ". " + f.getName() + ":" + f.getValue());
//			log.info(" >> " + f.getIndex() + ". " + f.getName() + ":" + f.getValue());
		}
//		System.out.println("****** FINACLE START REQUEST " + stan + " *********");
		System.out.println("*************** FINACLE END REQUEST ***************");
	}

	static {
		ISO_FIELD_TYPE_MAP = new HashMap<Integer, IsoType>();
		ISO_FIELD_NAME_MAP = new HashMap<Integer, String>();
		ISO_FIELD_SIZE_MAP = new HashMap<Integer, Integer>();

		ISO_FIELD_TYPE_MAP.put(1, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(2, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(3, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(4, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(5, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(6, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(7, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(8, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(9, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(10, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(11, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(12, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(13, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(14, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(15, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(16, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(17, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(18, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(19, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(20, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(21, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(22, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(23, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(24, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(25, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(26, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(27, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(28, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(29, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(30, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(31, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(32, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(33, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(34, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(35, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(36, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(37, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(38, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(39, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(40, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(41, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(42, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(43, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(44, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(45, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(46, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(47, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(48, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(49, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(50, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(51, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(52, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(53, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(54, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(55, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(56, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(57, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(58, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(59, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(60, IsoType.LVAR);
		ISO_FIELD_TYPE_MAP.put(61, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(62, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(63, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(64, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(65, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(66, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(67, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(68, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(69, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(70, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(71, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(72, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(73, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(74, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(75, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(76, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(77, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(78, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(79, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(80, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(81, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(82, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(83, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(84, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(85, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(86, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(87, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(88, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(89, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(90, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(91, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(92, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(93, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(94, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(95, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(96, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(97, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(98, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(99, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(100, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(101, IsoType.FIXED);
		ISO_FIELD_TYPE_MAP.put(102, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(103, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(104, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(105, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(106, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(107, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(108, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(109, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(110, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(111, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(112, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(113, IsoType.LLVAR);
		ISO_FIELD_TYPE_MAP.put(114, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(115, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(116, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(117, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(118, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(119, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(120, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(121, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(122, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(123, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(124, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(125, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(126, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(127, IsoType.LLLVAR);
		ISO_FIELD_TYPE_MAP.put(128, IsoType.FIXED);

		ISO_FIELD_NAME_MAP.put(1, "Bit Map Extended");
		ISO_FIELD_NAME_MAP.put(2, "Primary account number (PAN)");
		ISO_FIELD_NAME_MAP.put(3, "Processing code");
		ISO_FIELD_NAME_MAP.put(4, "Amount, transaction");
		ISO_FIELD_NAME_MAP.put(5, "Amount, Settlement");
		ISO_FIELD_NAME_MAP.put(6, "Amount, cardholder billing");
		ISO_FIELD_NAME_MAP.put(7, "Transmission date & time");
		ISO_FIELD_NAME_MAP.put(8, "Amount, Cardholder billing fee");
		ISO_FIELD_NAME_MAP.put(9, "Conversion rate, Settlement");
		ISO_FIELD_NAME_MAP.put(10, "Conversion rate, cardholder billing");
		ISO_FIELD_NAME_MAP.put(11, "Systems trace audit number");
		ISO_FIELD_NAME_MAP.put(12, "Time, Local transaction");
		ISO_FIELD_NAME_MAP.put(13, "Date, Local transaction");
		ISO_FIELD_NAME_MAP.put(14, "Date, Expiration");
		ISO_FIELD_NAME_MAP.put(15, "Date, Settlement");
		ISO_FIELD_NAME_MAP.put(16, "Date, conversion");
		ISO_FIELD_NAME_MAP.put(17, "Date, capture");
		ISO_FIELD_NAME_MAP.put(18, "Merchant type");
		ISO_FIELD_NAME_MAP.put(19, "Acquiring institution country code");
		ISO_FIELD_NAME_MAP.put(20, "PAN Extended, country code");
		ISO_FIELD_NAME_MAP.put(21, "Forwarding institution. country code");
		ISO_FIELD_NAME_MAP.put(22, "Point of service entry mode");
		ISO_FIELD_NAME_MAP.put(23, "Application PAN number");
		ISO_FIELD_NAME_MAP.put(24, "Network International identifier");
		ISO_FIELD_NAME_MAP.put(25, "Point of service condition code");
		ISO_FIELD_NAME_MAP.put(26, "Point of service capture code");
		ISO_FIELD_NAME_MAP.put(27, "Authorising identification response length");
		ISO_FIELD_NAME_MAP.put(28, "Amount, transaction fee");
		ISO_FIELD_NAME_MAP.put(29, "Amount. settlement fee");
		ISO_FIELD_NAME_MAP.put(30, "Amount, transaction processing fee");
		ISO_FIELD_NAME_MAP.put(31, "Amount, settlement processing fee");
		ISO_FIELD_NAME_MAP.put(32, "Acquiring institution identification code");
		ISO_FIELD_NAME_MAP.put(33, "Forwarding institution identofication code");
		ISO_FIELD_NAME_MAP.put(34, "Primary account number, extended");
		ISO_FIELD_NAME_MAP.put(35, "Track 2 data");
		ISO_FIELD_NAME_MAP.put(36, "Track 3 data");
		ISO_FIELD_NAME_MAP.put(37, "Retrieval reference number");
		ISO_FIELD_NAME_MAP.put(38, "Authorisation identification response");
		ISO_FIELD_NAME_MAP.put(39, "Response code");
		ISO_FIELD_NAME_MAP.put(40, "Service restriction code");
		ISO_FIELD_NAME_MAP.put(41, "Card acceptor terminal identification");
		ISO_FIELD_NAME_MAP.put(42, "Card acceptor identification code");
		ISO_FIELD_NAME_MAP.put(43, "Card acceptor name/location");
		ISO_FIELD_NAME_MAP.put(44, "Additional response data");
		ISO_FIELD_NAME_MAP.put(45, "Track 1 Data");
		ISO_FIELD_NAME_MAP.put(46, "Additional data - ISO");
		ISO_FIELD_NAME_MAP.put(47, "Additional data - National");
		ISO_FIELD_NAME_MAP.put(48, "Additional data - Private");
		ISO_FIELD_NAME_MAP.put(49, "Currency code, transaction");
		ISO_FIELD_NAME_MAP.put(50, "Currency code, settlement");
		ISO_FIELD_NAME_MAP.put(51, "Currency code, cardholder billing");
		ISO_FIELD_NAME_MAP.put(52, "Personal Identification number data");
		ISO_FIELD_NAME_MAP.put(53, "Security related control information");
		ISO_FIELD_NAME_MAP.put(54, "Additional amounts");
		ISO_FIELD_NAME_MAP.put(55, "Reserved ISO");
		ISO_FIELD_NAME_MAP.put(56, "Reserved ISO");
		ISO_FIELD_NAME_MAP.put(57, "Reserved National");
		ISO_FIELD_NAME_MAP.put(58, "Reserved National");
		ISO_FIELD_NAME_MAP.put(59, "Reserved for national use");
		ISO_FIELD_NAME_MAP.put(60, "Advice/reason code (private reserved)");
		ISO_FIELD_NAME_MAP.put(61, "Reserved Private");
		ISO_FIELD_NAME_MAP.put(62, "Reserved Private");
		ISO_FIELD_NAME_MAP.put(63, "Reserved Private");
		ISO_FIELD_NAME_MAP.put(64, "Message authentication code (MAC)");
		ISO_FIELD_NAME_MAP.put(65, "Bit map, tertiary");
		ISO_FIELD_NAME_MAP.put(66, "Settlement code");
		ISO_FIELD_NAME_MAP.put(67, "Extended payment code");
		ISO_FIELD_NAME_MAP.put(68, "Receiving institution country code");
		ISO_FIELD_NAME_MAP.put(69, "Settlement institution county code");
		ISO_FIELD_NAME_MAP.put(70, "Network management Information code");
		ISO_FIELD_NAME_MAP.put(71, "Message number");
		ISO_FIELD_NAME_MAP.put(72, "Message number, last");
		ISO_FIELD_NAME_MAP.put(73, "Date, Action");
		ISO_FIELD_NAME_MAP.put(74, "Credits, number");
		ISO_FIELD_NAME_MAP.put(75, "Credits, reversal number");
		ISO_FIELD_NAME_MAP.put(76, "Debits, number");
		ISO_FIELD_NAME_MAP.put(77, "Debits, reversal number");
		ISO_FIELD_NAME_MAP.put(78, "Transfer number");
		ISO_FIELD_NAME_MAP.put(79, "Transfer, reversal number");
		ISO_FIELD_NAME_MAP.put(80, "Inquiries number");
		ISO_FIELD_NAME_MAP.put(81, "Authorisations, number");
		ISO_FIELD_NAME_MAP.put(82, "Credits, processsing fee amount");
		ISO_FIELD_NAME_MAP.put(83, "Credits, transaction fee amount");
		ISO_FIELD_NAME_MAP.put(84, "Debits, processing fee amount");
		ISO_FIELD_NAME_MAP.put(85, "Debits, transaction fee amount");
		ISO_FIELD_NAME_MAP.put(86, "Credits, amount");
		ISO_FIELD_NAME_MAP.put(87, "Credits, reversal amount");
		ISO_FIELD_NAME_MAP.put(88, "Debits, amount");
		ISO_FIELD_NAME_MAP.put(89, "Debits, reversal amount");
		ISO_FIELD_NAME_MAP.put(90, "Original data elements");
		ISO_FIELD_NAME_MAP.put(91, "File update code");
		ISO_FIELD_NAME_MAP.put(92, "File security code");
		ISO_FIELD_NAME_MAP.put(93, "Response indicator");
		ISO_FIELD_NAME_MAP.put(94, "Service indicator");
		ISO_FIELD_NAME_MAP.put(95, "Replacement amounts");
		ISO_FIELD_NAME_MAP.put(96, "Message security code");
		ISO_FIELD_NAME_MAP.put(97, "Amount, net settlement");
		ISO_FIELD_NAME_MAP.put(98, "Payee");
		ISO_FIELD_NAME_MAP.put(99, "Settlement institution identification code");
		ISO_FIELD_NAME_MAP.put(100, "Receiving institution identification code");
		ISO_FIELD_NAME_MAP.put(101, "File name");
		ISO_FIELD_NAME_MAP.put(102, "Account identification 1");
		ISO_FIELD_NAME_MAP.put(103, "Account identification 2");
		ISO_FIELD_NAME_MAP.put(104, "Transaction description");
		ISO_FIELD_NAME_MAP.put(105, "Reserved for ISO use");
		ISO_FIELD_NAME_MAP.put(106, "Reserved for ISO use");
		ISO_FIELD_NAME_MAP.put(107, "Reserved for ISO use");
		ISO_FIELD_NAME_MAP.put(108, "Reserved for ISO use");
		ISO_FIELD_NAME_MAP.put(109, "Reserved for ISO use");
		ISO_FIELD_NAME_MAP.put(110, "Reserved for ISO use");
		ISO_FIELD_NAME_MAP.put(111, "Reserved for ISO use");
		ISO_FIELD_NAME_MAP.put(112, "Reserved for national use");
		ISO_FIELD_NAME_MAP.put(113, "Authorising agent institution id code");
		ISO_FIELD_NAME_MAP.put(114, "Reserved for national use");
		ISO_FIELD_NAME_MAP.put(115, "Reserved for national use");
		ISO_FIELD_NAME_MAP.put(116, "Reserved for national use");
		ISO_FIELD_NAME_MAP.put(117, "Reserved for national use");
		ISO_FIELD_NAME_MAP.put(118, "Reserved for national use");
		ISO_FIELD_NAME_MAP.put(119, "Reserved for national use");
		ISO_FIELD_NAME_MAP.put(120, "Reserved for private use");
		ISO_FIELD_NAME_MAP.put(121, "Reserved for private use");
		ISO_FIELD_NAME_MAP.put(122, "Reserved for private use");
		ISO_FIELD_NAME_MAP.put(123, "Reserved for private use");
		ISO_FIELD_NAME_MAP.put(124, "Info Text");
		ISO_FIELD_NAME_MAP.put(125, "Network management information");
		ISO_FIELD_NAME_MAP.put(126, "Issuer trace id");
		ISO_FIELD_NAME_MAP.put(127, "Reserved for private use");
		ISO_FIELD_NAME_MAP.put(128, "Message Authentication code");

		ISO_FIELD_SIZE_MAP.put(1, 128);
		ISO_FIELD_SIZE_MAP.put(2, 19);
		ISO_FIELD_SIZE_MAP.put(3, 6);
		ISO_FIELD_SIZE_MAP.put(4, 12);
		ISO_FIELD_SIZE_MAP.put(5, 12);
		ISO_FIELD_SIZE_MAP.put(6, 12);
		ISO_FIELD_SIZE_MAP.put(7, 10);
		ISO_FIELD_SIZE_MAP.put(8, 8);
		ISO_FIELD_SIZE_MAP.put(9, 8);
		ISO_FIELD_SIZE_MAP.put(10, 8);
		ISO_FIELD_SIZE_MAP.put(11, 12);
		ISO_FIELD_SIZE_MAP.put(12, 12);
		ISO_FIELD_SIZE_MAP.put(13, 4);
		ISO_FIELD_SIZE_MAP.put(14, 4);
		ISO_FIELD_SIZE_MAP.put(15, 4);
		ISO_FIELD_SIZE_MAP.put(16, 4);
		ISO_FIELD_SIZE_MAP.put(17, 4);
		ISO_FIELD_SIZE_MAP.put(18, 4);
		ISO_FIELD_SIZE_MAP.put(19, 3);
		ISO_FIELD_SIZE_MAP.put(20, 3);
		ISO_FIELD_SIZE_MAP.put(21, 3);
		ISO_FIELD_SIZE_MAP.put(22, 3);
		ISO_FIELD_SIZE_MAP.put(23, 3);
		ISO_FIELD_SIZE_MAP.put(24, 3);
		ISO_FIELD_SIZE_MAP.put(25, 2);
		ISO_FIELD_SIZE_MAP.put(26, 2);
		ISO_FIELD_SIZE_MAP.put(27, 1);
		ISO_FIELD_SIZE_MAP.put(28, 8);
		ISO_FIELD_SIZE_MAP.put(29, 8);
		ISO_FIELD_SIZE_MAP.put(30, 8);
		ISO_FIELD_SIZE_MAP.put(31, 8);
		ISO_FIELD_SIZE_MAP.put(32, 11);
		ISO_FIELD_SIZE_MAP.put(33, 11);
		ISO_FIELD_SIZE_MAP.put(34, 28);
		ISO_FIELD_SIZE_MAP.put(35, 37);
		ISO_FIELD_SIZE_MAP.put(36, 104);
		ISO_FIELD_SIZE_MAP.put(37, 12);
		ISO_FIELD_SIZE_MAP.put(38, 6);
		ISO_FIELD_SIZE_MAP.put(39, 3);
		ISO_FIELD_SIZE_MAP.put(40, 3);
		ISO_FIELD_SIZE_MAP.put(41, 3);
		ISO_FIELD_SIZE_MAP.put(42, 15);
		ISO_FIELD_SIZE_MAP.put(43, 40);
		ISO_FIELD_SIZE_MAP.put(44, 25);
		ISO_FIELD_SIZE_MAP.put(45, 76);
		ISO_FIELD_SIZE_MAP.put(46, 999);
		ISO_FIELD_SIZE_MAP.put(47, 999);
		ISO_FIELD_SIZE_MAP.put(48, 999);
		ISO_FIELD_SIZE_MAP.put(49, 3);
		ISO_FIELD_SIZE_MAP.put(50, 3);
		ISO_FIELD_SIZE_MAP.put(51, 3);
		ISO_FIELD_SIZE_MAP.put(52, 16);
		ISO_FIELD_SIZE_MAP.put(53, 18);
		ISO_FIELD_SIZE_MAP.put(54, 120);
		ISO_FIELD_SIZE_MAP.put(55, 999);
		ISO_FIELD_SIZE_MAP.put(56, 999);
		ISO_FIELD_SIZE_MAP.put(57, 999);
		ISO_FIELD_SIZE_MAP.put(58, 999);
		ISO_FIELD_SIZE_MAP.put(59, 999);
		ISO_FIELD_SIZE_MAP.put(60, 7);
		ISO_FIELD_SIZE_MAP.put(61, 999);
		ISO_FIELD_SIZE_MAP.put(62, 999);
		ISO_FIELD_SIZE_MAP.put(63, 999);
		ISO_FIELD_SIZE_MAP.put(64, 16);
		ISO_FIELD_SIZE_MAP.put(65, 16);
		ISO_FIELD_SIZE_MAP.put(66, 1);
		ISO_FIELD_SIZE_MAP.put(67, 2);
		ISO_FIELD_SIZE_MAP.put(68, 3);
		ISO_FIELD_SIZE_MAP.put(69, 3);
		ISO_FIELD_SIZE_MAP.put(70, 3);
		ISO_FIELD_SIZE_MAP.put(71, 8);
		ISO_FIELD_SIZE_MAP.put(72, 8);
		ISO_FIELD_SIZE_MAP.put(73, 6);
		ISO_FIELD_SIZE_MAP.put(74, 10);
		ISO_FIELD_SIZE_MAP.put(75, 10);
		ISO_FIELD_SIZE_MAP.put(76, 10);
		ISO_FIELD_SIZE_MAP.put(77, 10);
		ISO_FIELD_SIZE_MAP.put(78, 10);
		ISO_FIELD_SIZE_MAP.put(79, 10);
		ISO_FIELD_SIZE_MAP.put(80, 10);
		ISO_FIELD_SIZE_MAP.put(81, 10);
		ISO_FIELD_SIZE_MAP.put(82, 12);
		ISO_FIELD_SIZE_MAP.put(83, 12);
		ISO_FIELD_SIZE_MAP.put(84, 12);
		ISO_FIELD_SIZE_MAP.put(85, 12);
		ISO_FIELD_SIZE_MAP.put(86, 15);
		ISO_FIELD_SIZE_MAP.put(87, 15);
		ISO_FIELD_SIZE_MAP.put(88, 15);
		ISO_FIELD_SIZE_MAP.put(89, 15);
		ISO_FIELD_SIZE_MAP.put(90, 42);
		ISO_FIELD_SIZE_MAP.put(91, 1);
		ISO_FIELD_SIZE_MAP.put(92, 2);
		ISO_FIELD_SIZE_MAP.put(93, 5);
		ISO_FIELD_SIZE_MAP.put(94, 7);
		ISO_FIELD_SIZE_MAP.put(95, 42);
		ISO_FIELD_SIZE_MAP.put(96, 8);
		ISO_FIELD_SIZE_MAP.put(97, 16);
		ISO_FIELD_SIZE_MAP.put(98, 25);
		ISO_FIELD_SIZE_MAP.put(99, 11);
		ISO_FIELD_SIZE_MAP.put(100, 11);
		ISO_FIELD_SIZE_MAP.put(101, 17);
		ISO_FIELD_SIZE_MAP.put(102, 28);
		ISO_FIELD_SIZE_MAP.put(103, 28);
		ISO_FIELD_SIZE_MAP.put(104, 100);
		ISO_FIELD_SIZE_MAP.put(105, 999);
		ISO_FIELD_SIZE_MAP.put(106, 999);
		ISO_FIELD_SIZE_MAP.put(107, 999);
		ISO_FIELD_SIZE_MAP.put(108, 999);
		ISO_FIELD_SIZE_MAP.put(109, 999);
		ISO_FIELD_SIZE_MAP.put(110, 999);
		ISO_FIELD_SIZE_MAP.put(111, 999);
		ISO_FIELD_SIZE_MAP.put(112, 999);
		ISO_FIELD_SIZE_MAP.put(113, 11);
		ISO_FIELD_SIZE_MAP.put(114, 999);
		ISO_FIELD_SIZE_MAP.put(115, 999);
		ISO_FIELD_SIZE_MAP.put(116, 999);
		ISO_FIELD_SIZE_MAP.put(117, 999);
		ISO_FIELD_SIZE_MAP.put(118, 999);
		ISO_FIELD_SIZE_MAP.put(119, 999);
		ISO_FIELD_SIZE_MAP.put(120, 999);
		ISO_FIELD_SIZE_MAP.put(121, 999);
		ISO_FIELD_SIZE_MAP.put(122, 999);
		ISO_FIELD_SIZE_MAP.put(123, 999);
		ISO_FIELD_SIZE_MAP.put(124, 255);
		ISO_FIELD_SIZE_MAP.put(125, 999);
		ISO_FIELD_SIZE_MAP.put(126, 6);
		ISO_FIELD_SIZE_MAP.put(127, 999);
		ISO_FIELD_SIZE_MAP.put(128, 16);

	}

}
