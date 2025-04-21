package com.iso.config;

import java.util.List;

public interface IsoMessage 
{
	public void setMsgId(MsgId msgId);
	public MsgId getMsgId();
	public void addFields(int index, String value) ;
	public IsoField getFieldbyNumber(int no);
	public List<Integer> getValuesIndexInBitMap();
	public byte[] generateMessage();
	public void printMessage(String stan) ;
}
