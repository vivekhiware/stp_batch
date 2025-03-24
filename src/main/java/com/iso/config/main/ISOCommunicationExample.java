package com.iso.config.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.codec.binary.Hex;

import com.iso.config.IsoV93Message;
import com.iso.config.IsoV93MessageRes;
import com.iso.config.MsgId;

public class ISOCommunicationExample {

	private static final String HOST = "10.192.3.82"; // Remote host (e.g., ATM, POS)
	private static final int PORT = 27000; // Port for the communication
	private Socket socket;
	private static DataInputStream inputStream;
	private static DataOutputStream outputStream;

	public ISOCommunicationExample(String host, int port) throws IOException {
		socket = new Socket(host, port);
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());
	}

	public static void main(String[] args) {
		request();
//		requestRepeat();
	}

	static void request() {
		try {
			ISOCommunicationExample service = new ISOCommunicationExample(HOST, PORT);
			IsoV93Message obj1200 = new IsoV93Message(MsgId.MTID_1200);
			obj1200.addFields(2, "999999999999999");
			obj1200.addFields(3, "402010");
			obj1200.addFields(4, "000000000010");
			obj1200.addFields(12, "250120201918"); // 133528 230704133528
			obj1200.addFields(17, "0120");
			obj1200.addFields(24, "200");
			obj1200.addFields(32, "1111111111");
			obj1200.addFields(37, "150000451534");
			obj1200.addFields(49, "356");
//			obj1200.addFields(59, "5115502|5115612");
			obj1200.addFields(59, "5115612|5115502");
			obj1200.addFields(72, "20250120"); // 20230704
//			obj1200.addFields(102, "5165005139920");
//			obj1200.addFields(103, "00012010005690");
			obj1200.addFields(102, "8729000100014459");
			obj1200.addFields(103, "5165003182545");
			obj1200.addFields(123, "RCN");
			obj1200.addFields(125, "RRRR/434012268789/A21/14122024|OSE15122410272852");
			obj1200.addFields(126, "RRRR/434012268789/A21/14122024|OSE15122410272852");
			obj1200.addFields(127, "VCH1512240001455091");

			/*
			 * obj1200.addFields(2, "999999999999999"); obj1200.addFields(3, "402010");
			 * obj1200.addFields(4, "000000935000"); obj1200.addFields(12, "241214134953");
			 * // 133528 230704133528 obj1200.addFields(17, "1214"); obj1200.addFields(24,
			 * "200"); obj1200.addFields(32, "11111111111"); obj1200.addFields(37,
			 * "150000451534"); obj1200.addFields(49, "356"); obj1200.addFields(59,
			 * "5115502|5115612"); obj1200.addFields(72, "20241214"); // 20230704 //
			 * obj1200.addFields(102, "5165005139920"); // obj1200.addFields(103,
			 * "00012010005690"); obj1200.addFields(102, "8729000100014459");
			 * obj1200.addFields(103, "00012010005690"); obj1200.addFields(123, "RCN");
			 * obj1200.addFields(125, "RRRR/434012268789/A21/14122024|OSE15122410272852");
			 * obj1200.addFields(126, "RRRR/434012268789/A21/14122024|OSE15122410272852");
			 * obj1200.addFields(127, "VCH1512240001455091");
			 */
			byte[] msgBytes = obj1200.generateMessage();
			obj1200.printMessage("");
			System.out.println("******************** REQUEST START ********************");
			System.out.println(Hex.encodeHex(msgBytes));
			System.out.println("******************** REQUEST END **********************");
			System.out.println("******************** SERVER Response START **********************");
			byte[] response = service.networkTransportByte(msgBytes);
			IsoV93MessageRes obj1210 = new IsoV93MessageRes(response);
			obj1210.setRes002(obj1210.getFieldbyNumber(2).getValue());
			obj1210.setRes003(obj1210.getFieldbyNumber(3).getValue());
			obj1210.setRes004(obj1210.getFieldbyNumber(4).getValue());
			obj1210.setRes012(obj1210.getFieldbyNumber(12).getValue());
			obj1210.setRes017(obj1210.getFieldbyNumber(17).getValue());
			obj1210.setRes024(obj1210.getFieldbyNumber(24).getValue());
			obj1210.setRes032(obj1210.getFieldbyNumber(32).getValue());
			obj1210.setRes037(obj1210.getFieldbyNumber(37).getValue());
			obj1210.setRes038(obj1210.getFieldbyNumber(38).getValue());
			obj1210.setRes039(obj1210.getFieldbyNumber(39).getValue());
			obj1210.setRes048(obj1210.getFieldbyNumber(48).getValue());
			obj1210.setRes049(obj1210.getFieldbyNumber(49).getValue());
			obj1210.setRes059(obj1210.getFieldbyNumber(59).getValue());
			obj1210.setRes102(obj1210.getFieldbyNumber(102).getValue());
			obj1210.setRes123(obj1210.getFieldbyNumber(123).getValue());
			obj1210.setRes125(obj1210.getFieldbyNumber(125).getValue());
			obj1210.setRes126(obj1210.getFieldbyNumber(126).getValue());
			obj1210.setRes127(obj1210.getFieldbyNumber(127).getValue());
			if (obj1210.getRes039().equalsIgnoreCase("000")) {
				System.out.println(" SUCCESS TRANSACTION");
			} else if (obj1210.getRes039().equalsIgnoreCase("913")) {
				System.out.println(" DUPLICATE TRANSACTION TRANSACTION");
			}
			{
				String responseCode002 = obj1210.getFieldbyNumber(2).getValue();
				String responseCode003 = obj1210.getFieldbyNumber(3).getValue();
				String responseCode004 = obj1210.getFieldbyNumber(4).getValue();
				String responseCode012 = obj1210.getFieldbyNumber(12).getValue();
				String responseCode017 = obj1210.getFieldbyNumber(17).getValue();
				String responseCode024 = obj1210.getFieldbyNumber(24).getValue();

				String responseCode032 = obj1210.getFieldbyNumber(32).getValue();
				String responseCode037 = obj1210.getFieldbyNumber(37).getValue();
				String responseCode038 = obj1210.getFieldbyNumber(38).getValue();
				String responseCode039 = obj1210.getFieldbyNumber(39).getValue();
				String responseCode048 = obj1210.getFieldbyNumber(48).getValue();
				String responseCode049 = obj1210.getFieldbyNumber(49).getValue();

				String responseCode059 = obj1210.getFieldbyNumber(59).getValue();
				String responseCode102 = obj1210.getFieldbyNumber(102).getValue();
				String responseCode123 = obj1210.getFieldbyNumber(123).getValue();
				String responseCode125 = obj1210.getFieldbyNumber(125).getValue();
				String responseCode126 = obj1210.getFieldbyNumber(126).getValue();
				String responseCode127 = obj1210.getFieldbyNumber(127).getValue();
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

			}

			System.out.println("******************** SERVER Response END **********************");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static byte[] networkTransportByte(byte[] isoMessage) throws UnknownHostException, IOException {

		byte[] data = null;
		try {
			int messageLength = isoMessage.length;
			System.out.println(messageLength);
			outputStream.writeShort(messageLength);
			// Send the actual message
			outputStream.write(isoMessage);
			outputStream.flush();
			System.out.println("Message Send Success");

			int messageLengthRes = inputStream.readShort();
			// Read the message data
			if (messageLengthRes > 0) {
				System.out.println("Message Received Success");
				data = new byte[messageLengthRes];
				inputStream.readFully(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
// enquiry 

	static void requestRepeat() {
		try {
			ISOCommunicationExample service = new ISOCommunicationExample(HOST, PORT);
			IsoV93Message obj1200 = new IsoV93Message(MsgId.MTID_1201);
			obj1200.addFields(2, "999999999999999");
			obj1200.addFields(3, "402010");
			obj1200.addFields(4, "000000000010");
			obj1200.addFields(12, "250203104313"); // 133528 230704133528
			obj1200.addFields(17, "0203");
			obj1200.addFields(24, "200");
			obj1200.addFields(32, "1111111111");
			obj1200.addFields(37, "502092890076");
			obj1200.addFields(49, "356");
			obj1200.addFields(59, "5115502|5115612");
			obj1200.addFields(72, "20250203"); // 20230704
			obj1200.addFields(102, "2864001700249296");
//			obj1200.addFields(103, "00012010005690");
			obj1200.addFields(103, "1580003171160");
//			obj1200.addFields(103, "20019854258");
			obj1200.addFields(123, "RCN");
			obj1200.addFields(125, "noncbs502092890076");
			obj1200.addFields(126, "noncbs502092890076|1580001700068950/DORMANT");
			
			obj1200.addFields(127, "VCH1512240001455091");

			byte[] msgBytes = obj1200.generateMessage();
			obj1200.printMessage("");
			System.out.println("******************** REQUEST START ********************");
			System.out.println(Hex.encodeHex(msgBytes));
			System.out.println("******************** REQUEST END **********************");
			System.out.println("******************** SERVER Response START **********************");
			byte[] response = service.networkTransportByte(msgBytes);
			IsoV93MessageRes obj1210 = new IsoV93MessageRes(response);
			obj1210.setRes002(obj1210.getFieldbyNumber(2).getValue());
			obj1210.setRes003(obj1210.getFieldbyNumber(3).getValue());
			obj1210.setRes004(obj1210.getFieldbyNumber(4).getValue());
			obj1210.setRes012(obj1210.getFieldbyNumber(12).getValue());
			obj1210.setRes017(obj1210.getFieldbyNumber(17).getValue());
			obj1210.setRes024(obj1210.getFieldbyNumber(24).getValue());
			obj1210.setRes032(obj1210.getFieldbyNumber(32).getValue());
			obj1210.setRes037(obj1210.getFieldbyNumber(37).getValue());
			obj1210.setRes038(obj1210.getFieldbyNumber(38).getValue());
			obj1210.setRes039(obj1210.getFieldbyNumber(39).getValue());
			obj1210.setRes048(obj1210.getFieldbyNumber(48).getValue());
			obj1210.setRes049(obj1210.getFieldbyNumber(49).getValue());
			obj1210.setRes059(obj1210.getFieldbyNumber(59).getValue());
			obj1210.setRes102(obj1210.getFieldbyNumber(102).getValue());
			obj1210.setRes123(obj1210.getFieldbyNumber(123).getValue());
			obj1210.setRes125(obj1210.getFieldbyNumber(125).getValue());
			obj1210.setRes126(obj1210.getFieldbyNumber(126).getValue());
			obj1210.setRes127(obj1210.getFieldbyNumber(127).getValue());
			if (obj1210.getRes039().equalsIgnoreCase("000")) {
				System.out.println(" SUCCESS TRANSACTION");
			} else if (obj1210.getRes039().equalsIgnoreCase("913")) {
				System.out.println(" DUPLICATE TRANSACTION TRANSACTION");
			}
			{
				String responseCode002 = obj1210.getFieldbyNumber(2).getValue();
				String responseCode003 = obj1210.getFieldbyNumber(3).getValue();
				String responseCode004 = obj1210.getFieldbyNumber(4).getValue();
				String responseCode012 = obj1210.getFieldbyNumber(12).getValue();
				String responseCode017 = obj1210.getFieldbyNumber(17).getValue();
				String responseCode024 = obj1210.getFieldbyNumber(24).getValue();

				String responseCode032 = obj1210.getFieldbyNumber(32).getValue();
				String responseCode037 = obj1210.getFieldbyNumber(37).getValue();
				String responseCode038 = obj1210.getFieldbyNumber(38).getValue();
				String responseCode039 = obj1210.getFieldbyNumber(39).getValue();
				String responseCode048 = obj1210.getFieldbyNumber(48).getValue();
				String responseCode049 = obj1210.getFieldbyNumber(49).getValue();

				String responseCode059 = obj1210.getFieldbyNumber(59).getValue();
				String responseCode102 = obj1210.getFieldbyNumber(102).getValue();
				String responseCode123 = obj1210.getFieldbyNumber(123).getValue();
				String responseCode125 = obj1210.getFieldbyNumber(125).getValue();
				String responseCode126 = obj1210.getFieldbyNumber(126).getValue();
				String responseCode127 = obj1210.getFieldbyNumber(127).getValue();
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

			}

			System.out.println("******************** SERVER Response END **********************");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
