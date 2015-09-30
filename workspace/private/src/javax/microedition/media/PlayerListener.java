package javax.microedition.media;

public interface PlayerListener
{
	static final String END_OF_MEDIA = "endOfMedia";
	
	public void playerUpdate(Player player, String event, Object eventData);
}
