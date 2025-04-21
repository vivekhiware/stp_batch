package com.iso.config;

import static com.iso.config.Isov93Constant.isoFieldNameMap;
import static com.iso.config.Isov93Constant.isoFieldSizeMap;
import static com.iso.config.Isov93Constant.isoFieldTypeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stp.exception.InvalidIsoMessageException;

public class IsoV93Message implements IsoMessage {
	private static final Logger logger = LoggerFactory.getLogger(IsoV93Message.class);
	static Map<Integer, IsoType> isoFieldTypeMap = null;
	static Map<Integer, String> isoFieldNameMap = null;
	static Map<Integer, Integer> isoFieldSizeMap = null;
	static {
		isoFieldTypeMap = isoFieldTypeMap();
		isoFieldNameMap = isoFieldNameMap();
		isoFieldSizeMap = isoFieldSizeMap();
	}

	private final List<IsoField> fields = new ArrayList<>();
	private MsgId msgId;
	private String binaryBitMap = "10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	private String dataString;

	public IsoV93Message(MsgId msgId) {
		this.msgId = msgId;
	}

	private void parseMessage(byte[] msgBytes, int msgIdStart, int bitmapStart, int dataStart) {
		byte[] byteMsgId = ArrayUtils.subarray(msgBytes, msgIdStart, msgIdStart + 4);
		byte[] byteBitMap = ArrayUtils.subarray(msgBytes, bitmapStart, bitmapStart + 16);
		byte[] byteData = ArrayUtils.subarray(msgBytes, dataStart, msgBytes.length);
		int currIndex = 0;
		ArrayUtils.reverse(byteBitMap);
		this.msgId = MsgId.getMsgId(new String(byteMsgId));
		this.binaryBitMap = BinaryCodec.toAsciiString(byteBitMap);
		this.dataString = new String(byteData);

		for (int i : getValuesIndexInBitMap()) {
			if (i == 1)
				continue;
			String fieldValue = "";
			int fieldSize = 0;
			IsoType fieldType = isoFieldTypeMap.get(i);

			if (fieldType == IsoType.FIXED || fieldType == IsoType.AMOUNT) {
				fieldSize = isoFieldSizeMap.get(i);
				fieldValue = this.dataString.substring(currIndex, currIndex + fieldSize);
				currIndex += fieldSize;
			} else if (fieldType == IsoType.LVAR) {
				fieldSize = Integer.parseInt(this.dataString.substring(currIndex, currIndex + 1));
				currIndex += 1;
				fieldValue = this.dataString.substring(currIndex, currIndex + fieldSize);
				currIndex += fieldSize;
			} else if (fieldType == IsoType.LLVAR) {
				fieldSize = Integer.parseInt(this.dataString.substring(currIndex, currIndex + 2));
				currIndex += 2;
				fieldValue = this.dataString.substring(currIndex, currIndex + fieldSize);
				currIndex += fieldSize;
			} else if (fieldType == IsoType.LLLVAR) {
				fieldSize = Integer.parseInt(this.dataString.substring(currIndex, currIndex + 3));
				currIndex += 3;
				fieldValue = this.dataString.substring(currIndex, currIndex + fieldSize);
				currIndex += fieldSize;
			}

			this.fields.add(new IsoField(i, isoFieldNameMap.get(i), fieldType, fieldSize, fieldValue));
		}
	}

	public IsoV93Message(byte[] msgBytes) {
		parseMessage(msgBytes, 4, 8, 24);
	}

	public IsoV93Message(byte[] msgBytes, String response) {
		logger.info("response: {}", response);
		parseMessage(msgBytes, 12, 16, 32);
	}

	public void setMsgId(MsgId msgId) {
		this.msgId = msgId;
	}

	public MsgId getMsgId() {
		return msgId;
	}

	public void addFields(int index, String value) {
		if (value == null
				|| (isoFieldTypeMap.get(index).equals(IsoType.FIXED) && value.length() != isoFieldSizeMap.get(index)))

			throw new InvalidIsoMessageException("Invalid Field Value or Index");
		else
			this.fields.add(new IsoField(index, isoFieldNameMap.get(index), isoFieldTypeMap.get(index),
					isoFieldSizeMap.get(index), value));
	}

	public IsoField getFieldbyNumber(int no) {
		IsoField field = null;
		for (IsoField f : fields)
			if (no == f.getIndex())
				field = f;
		return field;
	}

	public List<Integer> getValuesIndexInBitMap() {
		String str = this.binaryBitMap;
		List<Integer> listArr = new ArrayList<>();
		char[] arrChar = str.toCharArray();
		int i = 1;
		for (char c : arrChar) {
			if (c == '1' || c == '0') {
				if (c == '1') {
					listArr.add(i);
				}
				i++;
			} else {

				throw new InvalidIsoMessageException("Bitmap String is not correct");
			}
		}
		return listArr;
	}

	public byte[] generateMessage() {
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

				throw new InvalidIsoMessageException("SO Type Not Defined");
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

		return msgByte;

	}

	public void printMessage(String stan) {
		Collections.sort(fields);
		logger.info("*************** FINACLE START REQUEST {} ***************", stan);
		fields.forEach(f -> logger.info("Field {} - {}", f.getIndex(), f.getValue()));
		logger.info("*************** FINACLE END REQUEST {} ***************", stan);
	}

	public void printMessageResponse(String stan) {
		Collections.sort(fields);
		logger.info("*************** FINACLE START RESPONSE {} ***************", stan);
		fields.forEach(f -> logger.info("Field {} - {}", f.getIndex(), f.getValue()));
		logger.info("*************** FINACLE END RESPONSE {} ***************", stan);
	}

	private String res002;
	private String res003;
	private String res004;
	private String res012;
	private String res017;
	private String res024;
	private String res032;
	private String res037;
	private String res038;
	private String res039;
	private String res048;
	private String res049;
	private String res059;
	private String res102;
	private String res123;
	private String res125;
	private String res126;
	private String res127;

	public String getRes002() {
		return res002;
	}

	public void setRes002(String res002) {
		this.res002 = res002;
	}

	public String getRes003() {
		return res003;
	}

	public void setRes003(String res003) {
		this.res003 = res003;
	}

	public String getRes004() {
		return res004;
	}

	public void setRes004(String res004) {
		this.res004 = res004;
	}

	public String getRes012() {
		return res012;
	}

	public void setRes012(String res012) {
		this.res012 = res012;
	}

	public String getRes017() {
		return res017;
	}

	public void setRes017(String res017) {
		this.res017 = res017;
	}

	public String getRes024() {
		return res024;
	}

	public void setRes024(String res024) {
		this.res024 = res024;
	}

	public String getRes032() {
		return res032;
	}

	public void setRes032(String res032) {
		this.res032 = res032;
	}

	public String getRes037() {
		return res037;
	}

	public void setRes037(String res037) {
		this.res037 = res037;
	}

	public String getRes038() {
		return res038;
	}

	public void setRes038(String res038) {
		this.res038 = res038;
	}

	public String getRes039() {
		return res039;
	}

	public void setRes039(String res039) {
		this.res039 = res039;
	}

	public String getRes048() {
		return res048;
	}

	public void setRes048(String res048) {
		this.res048 = res048;
	}

	public String getRes049() {
		return res049;
	}

	public void setRes049(String res049) {
		this.res049 = res049;
	}

	public String getRes059() {
		return res059;
	}

	public void setRes059(String res059) {
		this.res059 = res059;
	}

	public String getRes102() {
		return res102;
	}

	public void setRes102(String res102) {
		this.res102 = res102;
	}

	public String getRes123() {
		return res123;
	}

	public void setRes123(String res123) {
		this.res123 = res123;
	}

	public String getRes125() {
		return res125;
	}

	public void setRes125(String res125) {
		this.res125 = res125;
	}

	public String getRes126() {
		return res126;
	}

	public void setRes126(String res126) {
		this.res126 = res126;
	}

	public String getRes127() {
		return res127;
	}

	public void setRes127(String res127) {
		this.res127 = res127;
	}

}
