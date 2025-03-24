package com.iso.config;

public enum MsgId {
	MTID_0200("0200"), MTID_0210("0210"), MTID_1200("1200"), MTID_1210("1210"), MTID_0220("0220"), MTID_0221("0221"),
	MTID_0230("0230"), MTID_0400("0400"), MTID_0420("0420"), MTID_0421("0421"), MTID_0430("0430"), MTID_0800("0800"),
	MTID_0810("0810"), MTID_0820("0820"), MTID_1420("1420"), MTID_1430("1430"), MTID_1201("1201");

	private String mtid;

	private MsgId(String mtid) {
		this.mtid = mtid;
	}

	public String getValue() {
		return mtid;
	}

	public static MsgId getMsgId(String mtid) {
		MsgId msgId = null;
		for (MsgId id : MsgId.values()) {
			if (id.getValue().equals(mtid)) {
				msgId = id;
			}
		}
		return msgId;
	}

}