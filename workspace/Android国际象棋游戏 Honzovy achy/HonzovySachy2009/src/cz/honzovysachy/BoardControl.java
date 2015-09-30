/*
 This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package cz.honzovysachy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import cz.honzovysachy.mysleni.Minimax;
import cz.honzovysachy.mysleni.ThinkingOutput;
import cz.honzovysachy.pravidla.PGNHeaderData;
import cz.honzovysachy.pravidla.PawnPromotionGUIQueen;
import cz.honzovysachy.pravidla.Pozice;
import cz.honzovysachy.pravidla.Task;
import cz.honzovysachy.resouces.S;



public class BoardControl extends View {
	SavedTaskAndroid mSavedTaskAndroid;
	boolean mThinking = false;

	int mFieldFrom;
	int mFieldTo;
	
	
	Drawable mFigury[][];
	Task mTask;
	final Handler mHandler = new Handler();
	AktivitaSachovnice mActivity;
	
	protected boolean hrajeClovek() {
		return !isPremyslim() && (mSavedTaskAndroid.mWhitePerson && mTask.mBoardComputing.bily ||
				mSavedTaskAndroid.mBlackPerson && !mTask.mBoardComputing.bily);
	}
	
	public boolean trySave () {
		try {
	    	FileOutputStream f = getContext().openFileOutput("soubor", 0);
	    	ObjectOutputStream o = new ObjectOutputStream(f);
	    	o.writeObject(new SavedTaskChessAndroid(mSavedTaskAndroid, mTask.getSavedTask()));
	    	o.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private void init() {
		SavedTaskChessAndroid saved = tryLoad();
        if (saved == null) {
        	mSavedTaskAndroid = new SavedTaskAndroid();
        	mTask = new Task(null);
        } else {
        	mSavedTaskAndroid = saved.mSavedTaskAndroid;
        	mTask = new Task(saved.mSavedTaskChess);
        }
        setFocusable(true);
        mFigury = new Drawable[2][];
        mFigury[0] = new Drawable[6];
        mFigury[1] = new Drawable[6];
        mFigury[0][0] = getContext().getResources().getDrawable(R.drawable.cp);
        mFigury[0][1] = getContext().getResources().getDrawable(R.drawable.cj);
        mFigury[0][2] = getContext().getResources().getDrawable(R.drawable.cs);
        mFigury[0][3] = getContext().getResources().getDrawable(R.drawable.cv);
        mFigury[0][4] = getContext().getResources().getDrawable(R.drawable.cd);
        mFigury[0][5] = getContext().getResources().getDrawable(R.drawable.ck);
        mFigury[1][0] = getContext().getResources().getDrawable(R.drawable.bp);
        mFigury[1][1] = getContext().getResources().getDrawable(R.drawable.bj);
        mFigury[1][2] = getContext().getResources().getDrawable(R.drawable.bs);
        mFigury[1][3] = getContext().getResources().getDrawable(R.drawable.bv);
        mFigury[1][4] = getContext().getResources().getDrawable(R.drawable.bd);
        mFigury[1][5] = getContext().getResources().getDrawable(R.drawable.bk);
        pripravTahHned();
	}
	
	
	public SavedTaskChessAndroid tryLoad() {
		SavedTaskChessAndroid task = null;
		try {
			FileInputStream f = getContext().openFileInput("soubor");
			ObjectInputStream o = new ObjectInputStream(f);
			task = (SavedTaskChessAndroid)o.readObject();
			o.close();
	       } catch (Exception e) {};
	       return task;
	}
	
	public BoardControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public BoardControl(Context context) {
        super(context);
        init();
    }
    
    public void pripravTah() {
    	mHandler.post(new Runnable() {
			public void run() {
				pripravTahHned();
			}
    	});
    }
    
    public void tahni(int tah) {
    	mSavedTaskAndroid.mox = -1;
    	mSavedTaskAndroid.moy = -1;
    	mTask.tahni(tah, true, true, null);
    	if (mTask.mEnd != 0) dlg(mTask.getEndOfGameString(mTask.mEnd));
    	invalidate();
    	pripravTah();
    	//dlg("Pozic bylo " + mTask.hodPos);
    }
    
    protected void pripravTahHned() {
    	if (hrajeClovek()) {
    	} else {
			tahniPrograme();
    	}
    	
    }
    
    public void otoc() {
    	mSavedTaskAndroid.mFlipped = !mSavedTaskAndroid.mFlipped;
    	invalidate();
    }
    
    public void replayPromotion(int type) {
    	Vector t = mTask.nalezTahyVector();
    	int tah = mTask.makeMove(t, mFieldFrom, mFieldTo, new PawnPromotionGUIQueen(type + 2));
		tahni(tah);
    }
    
    public boolean onTouchEvent(MotionEvent event)  {
    	if (isPremyslim()) return false;
    	if (mSavedTaskAndroid.mSetup) return false;
     	if (event.getAction() != MotionEvent.ACTION_DOWN) return false;
    	if (mSavedTaskAndroid.mPole <= 0 || !hrajeClovek()) return false;
    	int x = (int)((event.getX() + 0.5) / mSavedTaskAndroid.mPole);
    	int y = (int)((event.getY() + 0.5) / mSavedTaskAndroid.mPole);
    	if (x > 7 || x < 0 || y > 7 || y < 0) return false;
    	if (mSavedTaskAndroid.mFlipped) {
    		x = 7 - x;
    	} else {
    		y = 7 - y;
    	}

    	Vector t = mTask.nalezTahyVector();
		int pole = Pozice.a1 + x + 10 * y;
		if (mTask.JeTam1(t, pole)) {
			mSavedTaskAndroid.mcx = mSavedTaskAndroid.mox = x;
			mSavedTaskAndroid.mcy = mSavedTaskAndroid.moy = y;
			invalidate();
			return true;
		}
		int pole1 = Pozice.a1 + mSavedTaskAndroid.mox + 10 * mSavedTaskAndroid.moy;
		if (mTask.JeTam2(t, pole1, pole)) {
			if (Math.abs(mTask.mBoardComputing.sch[pole1]) == 1 && (y == 7 || y == 0)) {
				mFieldFrom = pole1;
				mFieldTo = pole;
				Intent result = new Intent();
		        result.setClass(getContext(), PromotionActivity.class);
				((Activity)(getContext())).startActivityForResult(result, 0);
				return true;
			}
			int tah = mTask.makeMove(t, pole1, pole, new PawnPromotionGUIQueen());
			tahni(tah);
			return true;
		}
		mSavedTaskAndroid.mcx = x;
		mSavedTaskAndroid.mcy = y;
		
		return true;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	byte[] sch = null;
   		sch = mTask.mBoard.sch;

    	Rect r = new Rect();
    	getDrawingRect(r);
  		int w = r.right - r.left;
   		int h = r.bottom - r.top;
   		mSavedTaskAndroid.mPole = (w < h ? w : h);
   		mSavedTaskAndroid.mPole >>= 3;
        
        Paint bila = new Paint();
        bila.setARGB(255, 200, 200, 200);
        Paint cerna = new Paint();
        cerna.setARGB(255, 100, 100, 100);
        Paint modra = new Paint();
        modra.setARGB(255, 0, 0, 255);
        Paint zelena = new Paint();
        zelena.setARGB(255, 0, 255, 0);
        Paint bilaf = new Paint();
        bilaf.setARGB(255, 255, 255, 255);
        Paint cernaf = new Paint();
        cernaf.setARGB(255, 0, 0, 0);
        boolean clovek = hrajeClovek() || mSavedTaskAndroid.mSetup;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
            	Paint p;
            	if (clovek && mSavedTaskAndroid.mox == i && mSavedTaskAndroid.moy == j) {
            		p = zelena;
            	} else
            	if (clovek && mSavedTaskAndroid.mcx == i && mSavedTaskAndroid.mcy == j) {
            		p = modra;
            	} else {
            		p = ((((i + j) & 1) == 1) ? bila : cerna);
            	}
            	int sx = (mSavedTaskAndroid.mFlipped ? 7 - i : i) * mSavedTaskAndroid.mPole;
            	int sy = (mSavedTaskAndroid.mFlipped ? j : 7 - j) * mSavedTaskAndroid.mPole;
                canvas.drawRect(new Rect(sx, sy, sx + mSavedTaskAndroid.mPole, sy + mSavedTaskAndroid.mPole), p);
                byte co = sch[Pozice.a1 + i + 10 * j];
                
                if (co != 0 && co > -7 && co < 7) {
                	Drawable dr = mFigury[co > 0 ? 1 : 0][co > 0 ? co -1 : -co - 1];
                	dr.setBounds(sx, sy, sx + mSavedTaskAndroid.mPole, sy + mSavedTaskAndroid.mPole);
                	dr.draw(canvas);
                }
            }
    }

	
	public void dlg(String co) {
		Dialog d = new Dialog(this.getContext());
		d.setTitle(co);
		d.show();
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
    	if (isPremyslim()) return false;
    	int pole = Pozice.a1 + mSavedTaskAndroid.mcx + 10 * mSavedTaskAndroid.mcy;
    	switch (keyCode) {
    	case KeyEvent.KEYCODE_DPAD_UP:
    		if (!mSavedTaskAndroid.mFlipped && mSavedTaskAndroid.mcy < 7 ||
    			mSavedTaskAndroid.mFlipped && mSavedTaskAndroid.mcy > 0) {
    			if (mSavedTaskAndroid.mFlipped)
    				mSavedTaskAndroid.mcy--;
    			else mSavedTaskAndroid.mcy++;
    			invalidate();
    		}
    		return true;
    	
    	case KeyEvent.KEYCODE_DPAD_DOWN:
    		if (mSavedTaskAndroid.mFlipped && mSavedTaskAndroid.mcy < 7 ||
    			!mSavedTaskAndroid.mFlipped && mSavedTaskAndroid.mcy > 0) {
    			if (mSavedTaskAndroid.mFlipped) mSavedTaskAndroid.mcy++; else mSavedTaskAndroid.mcy--;
    			invalidate();
    		}
    		return true;
    	case KeyEvent.KEYCODE_DPAD_RIGHT:
    		if (!mSavedTaskAndroid.mFlipped && mSavedTaskAndroid.mcx < 7 || mSavedTaskAndroid.mFlipped && mSavedTaskAndroid.mcx > 0) {
    			if (mSavedTaskAndroid.mFlipped) mSavedTaskAndroid.mcx--; else mSavedTaskAndroid.mcx++;
    			invalidate();
    		}
    		return true;
    	case KeyEvent.KEYCODE_DPAD_LEFT:
    		if (mSavedTaskAndroid.mFlipped && mSavedTaskAndroid.mcx < 7 ||
    			!mSavedTaskAndroid.mFlipped && mSavedTaskAndroid.mcx > 0) {
    			if (mSavedTaskAndroid.mFlipped)
    				mSavedTaskAndroid.mcx++; else mSavedTaskAndroid.mcx--;
    			invalidate();
    		}
    		return true;
    	case KeyEvent.KEYCODE_DPAD_CENTER:
    		if (mSavedTaskAndroid.mSetup) {
    			return true;
    		}
    		if (!hrajeClovek()) return true;
    		Vector t = mTask.nalezTahyVector();
    		//int pole = Pozice.a1 + mSavedTaskAndroid.mcx + 10 * mSavedTaskAndroid.mcy;
    		if (mTask.JeTam1(t, pole)) {
    			mSavedTaskAndroid.mox = mSavedTaskAndroid.mcx;
    			mSavedTaskAndroid.moy = mSavedTaskAndroid.mcy;
    			invalidate();
    			return true;
    		}
    		int pole1 = Pozice.a1 + mSavedTaskAndroid.mox + 10 * mSavedTaskAndroid.moy;
    		if (mTask.JeTam2(t, pole1, pole)) {
    			if (Math.abs(mTask.mBoardComputing.sch[pole1]) == 1 && (mSavedTaskAndroid.mcy == 7 || mSavedTaskAndroid.mcy == 0)) {
    				mFieldFrom = pole1;
    				mFieldTo = pole;
    				Intent result = new Intent();
    		        result.setClass(getContext(), PromotionActivity.class);
    				((Activity)(getContext())).startActivityForResult(result, 0);
    				return true;
    			} else {
    				int tah = mTask.makeMove(t, pole1, pole, new PawnPromotionGUIQueen());
    				tahni(tah);
    				return true;
    			}
    		}
    		
    		return true;
    	case KeyEvent.KEYCODE_0:
    	case KeyEvent.KEYCODE_1:
    	case KeyEvent.KEYCODE_2:
    	case KeyEvent.KEYCODE_3:
    	case KeyEvent.KEYCODE_4:
    	case KeyEvent.KEYCODE_5:
    	case KeyEvent.KEYCODE_6:
    		if (!mSavedTaskAndroid.mSetup) return true;
    		mTask.mBoard.sch[pole] = (byte)(keyCode - KeyEvent.KEYCODE_0);
    		invalidate();
    		return true;
    	case KeyEvent.KEYCODE_Q:
    		if (!mSavedTaskAndroid.mSetup) return true;
    		mTask.mBoard.sch[pole] = -1;
    		invalidate();
    		return true;
    	case KeyEvent.KEYCODE_W:
    		if (!mSavedTaskAndroid.mSetup) return true;
    		mTask.mBoard.sch[pole] = -2;
    		invalidate();
    		return true;
    	case KeyEvent.KEYCODE_E:
    		if (!mSavedTaskAndroid.mSetup) return true;
    		mTask.mBoard.sch[pole] = -3;
    		invalidate();
    		return true;
    	case KeyEvent.KEYCODE_R:
    		if (!mSavedTaskAndroid.mSetup) return true;
    		mTask.mBoard.sch[pole] = -4;
    		invalidate();
    		return true;
    	case KeyEvent.KEYCODE_T:
    		if (!mSavedTaskAndroid.mSetup) return true;
    		mTask.mBoard.sch[pole] = -5;
    		invalidate();
    		return true;
    	case KeyEvent.KEYCODE_Y:
    		if (!mSavedTaskAndroid.mSetup) return true;
    		mTask.mBoard.sch[pole] = -6;
    		invalidate();
    		return true;
    	/*case KeyEvent.KEYCODE_BACK:
    		if (!mSetup) return false;
    		setupBoardOK(true);
    		return true;*/
    		
    	}
    	return false;
    }
    
    public boolean isPremyslim() {
    	return mThinking;
    }
    
    protected void tahniPrograme() {
    	mThinking = true;
    	Thread t = new Thread() {
    		public void run() {
    			mTask.nalezTahyVector();
    			final int tah;
    			
    			if (mActivity == null) try {Thread.sleep(500, 0);} catch (Exception e) {} // TODO fix better !!!!
    			SharedPreferences pref = mActivity.getSharedPreferences(AktivitaSachovnice.SETTINGS, 0);
    			int time = 5000;
    			tah = Minimax.minimax(mTask, pref == null ? time : pref.getInt(AktivitaSachovnice.TIME_PER_MOVE, time), new ThinkingOutput() {
    				int mDepth = 0;
    				String mMove = "";
    				int mValue = 0;
    				
    				private void draw() {
    					mHandler.post(
    		    				new Runnable() {
    								public void run() {
    									TextView v = (TextView)mActivity.findViewById(R.id.move);
    			    					v.setText(mMove + " (" + mDepth +") " + mValue);
    									}}
    		    					 );
    					
    				}
    				
					@Override
					public void bestMove(String move, int value) {
						mMove = move;
						mValue = value;
						draw();
					}

					@Override
					public void depth(int depth) {
						mDepth = depth;
						draw();						
					}
    				
    			}); 
    			mHandler.post(
    				new Runnable() {
						public void run() {
							mThinking = false;
							if (tah != 0) 
								tahni(tah);
							}}
    					 );
    		 }
    	};
    	t.start();
     }
    
    protected void newGame() {
    	mTask = new Task(null);
    	mSavedTaskAndroid.mox = -1;
    	mSavedTaskAndroid.moy = -1;
    	mSavedTaskAndroid.mcx = 0;
    	mSavedTaskAndroid.mcy = 0;
    	if (!(mSavedTaskAndroid.mBlackPerson && mSavedTaskAndroid.mWhitePerson)) {
    		mSavedTaskAndroid.mBlackPerson = false;
    		mSavedTaskAndroid.mWhitePerson = true;
    	}
    	invalidate();
    }
    
    protected void setPersonsAfterUndoRedo() {
    	boolean hh = mSavedTaskAndroid.mWhitePerson && mSavedTaskAndroid.mBlackPerson;
		if (!hh) {
			if (mTask.mBoard.bily) {
				mSavedTaskAndroid.mWhitePerson = true;
				mSavedTaskAndroid.mBlackPerson = false;
			} else {
				mSavedTaskAndroid.mBlackPerson = true;
				mSavedTaskAndroid.mWhitePerson = false;
			}
		}
    }
    
    protected void undo() {
    	if (mTask.mIndexInGame >= 0) {
    		mTask.tahniZpet(0, true, null);
    		setPersonsAfterUndoRedo();
    		invalidate();
    		pripravTah();
    	}
    }
    
    protected void redo() {
    	if (mTask.mIndexInGame + 1 < mTask.mGame.size()) {
			mTask.tahni(0, true, false, null);
			setPersonsAfterUndoRedo();
			invalidate();
			pripravTah();
		}
    }
    
    protected void hrajTed() {
    	if (mTask.mBoardComputing.bily) {
    		mSavedTaskAndroid.mWhitePerson = false;
    		mSavedTaskAndroid.mBlackPerson = true;
    	} else {
    		mSavedTaskAndroid.mWhitePerson = true;
    		mSavedTaskAndroid.mBlackPerson = false;
    	}
    	invalidate();
    	tahniPrograme();
    }

    public void save(Intent data) {
    	try {
    		
    		PGNHeaderData pgnData;
    		try {
    		 pgnData = (PGNHeaderData) data.getSerializableExtra("PGNHeader");
    		} catch (Exception e) {
    			return;
    		}
    		if (pgnData == null) return;
    		mTask.savePNG(
				new FileOutputStream(new File(pgnData.mFileName)), true, pgnData);
    		dlg(S.g("SAVED_AS") + pgnData.mFileName + ".");
		} catch (IOException e) {
			dlg(S.g("SAVE_ERROR"));
		}
    }
    
    public void hh() {
    	mSavedTaskAndroid.mBlackPerson = true;
    	mSavedTaskAndroid.mWhitePerson = true;
    }
    
    public void save() {
   		Intent result = new Intent();
        result.setClass(getContext(), PGNSaveActivity.class);
		((Activity)(getContext())).startActivityForResult(result, 0);
    }
    public void settings() {
   		//Intent result = new Intent();
        //result.setClass(getContext(), SettingsActivity.class);
		//((Activity)(getContext())).startActivityForResult(result, 0);
    	
    	Intent result = new Intent();
        result.setClass(getContext(), TabSettingsActivity.class);
        ((Activity)(getContext())).startActivityForResult(result, 0);
    	
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      	int width = MeasureSpec.getSize(widthMeasureSpec);
    	int height = MeasureSpec.getSize(heightMeasureSpec);
    	width = Math.min(width, height);
        setMeasuredDimension(width, width);
    }
    
    public void setupBoard(boolean useBoardComputing) {
    	View setting = mActivity.findViewById(R.id.board_setting_panel);
    	View normal = mActivity.findViewById(R.id.normal_panel);
    	normal.setVisibility(GONE);
    	setting.setVisibility(VISIBLE);
    	mSavedTaskAndroid.mSetup = true;
    	CheckBox bWhite = (CheckBox)mActivity.findViewById(R.id.board_setting_white);
    	bWhite.setChecked(mTask.mBoard.bily);
    	if (!useBoardComputing) {
    		mTask.mBoardComputing = new Pozice(mTask.mBoard);
    	}
    	mTask.mGame = new Vector();
    	mTask.mIndexInGame = -1;
    	mTask.mEnd = Task.NO_END;
    	
    	mSavedTaskAndroid.mcx = 0;
    	mSavedTaskAndroid.mcy = 0;
    	
    	mSavedTaskAndroid.mox = -1;
    	mSavedTaskAndroid.moy = -1;
    	
    	invalidate();
    }
    
    
    
    public void setupBoardOK(boolean cancel) {
      	View setting = mActivity.findViewById(R.id.board_setting_panel);
    	View normal = mActivity.findViewById(R.id.normal_panel);
    	CheckBox bWhite = (CheckBox)mActivity.findViewById(R.id.board_setting_white);
    	if (cancel) {
    		mTask.mBoard = new Pozice(mTask.mBoardComputing);
    	} else {
    		mTask.mBoard.bily = bWhite.isChecked();
    		mTask.mBoard.mimoch = 0;
    		mTask.mBoard.roch = 0;
    		/* binarne 00...00vmVM */
    		if (mTask.mBoard.sch[Pozice.e1] == 6) {
    			if (mTask.mBoard.sch[Pozice.h1] == 4) mTask.mBoard.roch |= 1;
    			if (mTask.mBoard.sch[Pozice.a1] == 4) mTask.mBoard.roch |= 2;
    		}
    		if (mTask.mBoard.sch[Pozice.e8] == -6) {
    			if (mTask.mBoard.sch[Pozice.h8] == -4) mTask.mBoard.roch |= 4;
    			if (mTask.mBoard.sch[Pozice.a8] == -4) mTask.mBoard.roch |= 8;
    		}
        	if (mTask.mBoard.correctBoard() != 0) {
        		dlg(S.g("INCORRECT_BOARD"));
        		return;
        	}
        	mTask.mBoardComputing = new Pozice(mTask.mBoard);
    	}
    	normal.setVisibility(VISIBLE);
    	setting.setVisibility(GONE);
    	mSavedTaskAndroid.mSetup = false;
    	if (!mSavedTaskAndroid.mBlackPerson || !mSavedTaskAndroid.mWhitePerson) {
    		if (mTask.mBoard.bily) {
    			mSavedTaskAndroid.mBlackPerson = false;
    			mSavedTaskAndroid.mWhitePerson = true;
    		} else {
    			mSavedTaskAndroid.mBlackPerson = true;
    			mSavedTaskAndroid.mWhitePerson = false;
    		}
    	}
    	invalidate();
    }
    void moveNow() {
    	mTask.mExitThinking = true;
    }

 }
