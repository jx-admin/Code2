package javax.wireless.messaging;

import java.io.IOException;

import javax.microedition.io.Connection;

public interface MessageConnection extends Connection
{

	String TEXT_MESSAGE = null;

	void send(TextMessage sms);

	TextMessage newMessage(String text_message);

	void close() throws IOException;

}
