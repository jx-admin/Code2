package cz.honzovysachy;

import java.io.Serializable;

public class SavedTaskAndroid implements Serializable {

	private static final long serialVersionUID = -5285979235163176289L;
	
	public int mcx = 4;
	public int mcy = 4;
	public int mox = -1;
	public int moy = -1;
	public int mPole = -1;
	public boolean mFlipped = false;
	public boolean mWhitePerson = true;
	public boolean mBlackPerson = false;
	boolean mSetup = false;
}
