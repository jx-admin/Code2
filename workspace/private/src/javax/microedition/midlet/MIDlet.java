package javax.microedition.midlet;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Display;

public abstract class MIDlet {
	
	private Display display;
	
	public String getAppProperty(String key) {
		return null;
	}
	
	public final boolean platformRequest(String URL) throws ConnectionNotFoundException
    {
		return false;
    }
	
	public int checkPermission(String permission) {
		return 0;
	}
	
	public void notifyDestroyed() {}
	
	public void notifyPaused() {}
	
	public void resumeRequest() {}
	
	protected abstract void startApp() throws MIDletStateChangeException;
	
	protected abstract  void pauseApp();
	
	protected abstract void destroyApp(boolean unconditional) throws MIDletStateChangeException;
}
