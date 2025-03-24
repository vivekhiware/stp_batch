package com.iso.config;

import java.util.List;

public interface IsoMessage 
{
	public void setMsgId(MsgId msgId);
	public MsgId getMsgId();
	public void addFields(int index, String value)  throws Exception;
	public IsoField getFieldbyNumber(int no);
	public List<Integer> getValuesIndexInBitMap()  throws Exception;
	public byte[] generateMessage() throws Exception;
	public void printMessage(String stan) throws Exception;
}
