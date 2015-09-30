package javax.microedition.lcdui;

import java.util.Vector;

public abstract class Displayable {

	private Display display;

	private Vector cmds;

	private String title;

	private CommandListener listener;

	public void addCommand(Command cmd) {
	}

//	Ticker getTicker(){
//	}

	public String getTitle() {
		return null;
	}

	public int getWidth() {
		return 0;
	}

	public int getHeight() {
		return 0;
	}

	public boolean isShown() {
		return false;
	}

	public void removeCommand(Command cmd) {
	}

	public void setCommandListener(CommandListener l) {
	}

//	void setTicker(Ticker ticker){
//	}

	public void setTitle(String s) {
	}

	protected void sizeChanged(int w, int h) {
	}

	protected int messageAction(int id, int param1, int param2) {
		return 0;
	}
}
