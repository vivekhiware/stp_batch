package com.iso.config;

public class IsoField implements Comparable<IsoField> {

	private int index;			
	private String name;	
	private IsoType isoType;
	private int maxLength;
	private String value;
	
	public IsoField(
			int index,			
			String name,			
			IsoType isoType,
			int size,
			String value
			)
	{			
		this.index = index;
		this.name = name;
		this.isoType = isoType;	
		this.setMaxLength(size);
		this.value = value;
	}
	
	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public IsoType getIsoType() {
		return isoType;
	}

	public String getValue() {
		return value;
	}

	public int compareTo(IsoField isoField) {
		
		if(this.index > isoField.index)
			return 1;
		else if(this.index < isoField.index)
			return -1;
		else
			return 0;	
	}
	
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMaxLength() {
		return maxLength;
	}
	
}
