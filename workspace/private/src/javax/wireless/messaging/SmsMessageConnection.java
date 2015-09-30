package javax.wireless.messaging;

import java.io.IOException;

public class SmsMessageConnection implements MessageConnection
{
	private String defAddress;
	
	public SmsMessageConnection(String address)
	{ }
	
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	public TextMessage newMessage(String text_message) {
		// TODO Auto-generated method stub
		return null;
	}

	public void send(TextMessage sms) {
		// TODO Auto-generated method stub

	}

}
