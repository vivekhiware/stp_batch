package com.iso.config;

import java.util.Objects;

public class IsoField implements Comparable<IsoField> {

	private int index;
	private String name;
	private IsoType isoType;
	private int maxLength;
	private String value;

	public IsoField(int index, String name, IsoType isoType, int size, String value) {
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

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	@Override
	public int compareTo(IsoField isoField) {
		return Integer.compare(this.index, isoField.index);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		IsoField other = (IsoField) obj;
		return index == other.index;
	}

	@Override
	public int hashCode() {
		return Objects.hash(index);
	}
}
