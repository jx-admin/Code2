package cz.honzovysachy.awt;

import java.applet.Applet;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;

import cz.honzovysachy.resouces.S;

@SuppressWarnings("serial")
class ChessPanel extends Panel {
	public ChessPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		ChessStringBox output = new ChessStringBox();
		Sachovnice s = new Sachovnice(40, 10, output); 
		add(s);
		add(output);
		addKeyListener(s);
	}
}

@SuppressWarnings("serial")
class SachFrame extends Frame implements WindowListener {
	boolean mInApplet;
	public SachFrame(String string, boolean inApplet) {
		super(string);
		mInApplet = inApplet;
		Panel p = new ChessPanel();
		add(p);
		pack();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if (mInApplet) 
			this.dispose();
		else
			System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}
}

@SuppressWarnings("serial")
public class Starter extends Applet  {

	
	public static void otevriOkno(boolean inApplet) {
		S.init("en");
		
		SachFrame f = new SachFrame(S.g("HONZOVY_SACHY"), inApplet);
		f.addWindowListener(f);
		f.setVisible(true);
	}
	
	public static void main(String[] args) {
		otevriOkno(false);
	}
	
	public void init()
    {
		Panel p = new ChessPanel();
		add(p);
    }
}
