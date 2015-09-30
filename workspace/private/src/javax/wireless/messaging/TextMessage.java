package javax.wireless.messaging;

public interface TextMessage
{

	void setAddress(String string);

	void setPayloadText(String text);

	String getAddress();

	String getPayloadText();

}
