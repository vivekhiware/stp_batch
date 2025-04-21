package com.stp.utility;

// Enum class definition
public enum TypeEnum {
	L0,L00,L1, L2, L3, L4, L5, L6, L7, L10, A1, TTUM4;

	public String getDescription() {
		switch (this) {
		case L0:
			return "Pending Maker";
		case L00:
			return "Pending Maker";
		case L1:
			return "Maker";
		case L2:
			return "Checker";
		case L3:
			return "Bank Maker";
		case L4:
			return "Bank Checker";
		case L5:
			return "Enquiry ";
		case L6:
			return "Enquiry ";
		case L10:
			return "Reposting";
		case A1:
			return "Approved";
		case TTUM4:
			return "STP_IMPS_TCC_DATA_IW  Posted to CBS";
		default:
			throw new IllegalArgumentException("Unknown type: " + this);
		}
	}
}

// Usage of enum in a switch statement:
