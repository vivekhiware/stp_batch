package com.iso.config.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ISOCommunicationExample {
	private static final Logger logger = LoggerFactory.getLogger(ISOCommunicationExample.class);

	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	public ISOCommunicationExample() {
	}

	public ISOCommunicationExample(String host, int port) throws IOException {
		socket = new Socket(host, port);
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());
	}

	public byte[] networkTransportByte(byte[] isoMessage) {
		byte[] data = null;
		try {
			int messageLength = isoMessage.length;
			logger.info("Sending message of length: {}", messageLength);

			outputStream.writeShort(messageLength);
			outputStream.write(isoMessage);
			outputStream.flush();

			int messageLengthRes = inputStream.readShort();
			if (messageLengthRes > 0) {
				data = new byte[messageLengthRes];
				inputStream.readFully(data);
				logger.info("Message sent successfully: {}", messageLengthRes);

			}
		} catch (IOException e) {
			logger.error("Error closing resources", e);
		}
		return data;
	}

	public void close() {
		try {
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
			if (socket != null && !socket.isClosed())
				socket.close();
		} catch (IOException e) {
			logger.error("ISO Message sent to server.");

		}
	}
}
