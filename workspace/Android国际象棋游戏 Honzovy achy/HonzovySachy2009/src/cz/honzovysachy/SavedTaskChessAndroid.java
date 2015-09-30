package cz.honzovysachy;

import java.io.Serializable;

import cz.honzovysachy.pravidla.SavedTask;

public class SavedTaskChessAndroid implements Serializable {

	private static final long serialVersionUID = -1693344356611714151L;
	
	public SavedTaskChessAndroid(
			SavedTaskAndroid savedTaskAndroid,
			SavedTask savedTaskChess) {
		mSavedTaskAndroid = savedTaskAndroid;
		mSavedTaskChess = savedTaskChess;
	}
	public SavedTaskAndroid mSavedTaskAndroid;
	public SavedTask mSavedTaskChess;

}
