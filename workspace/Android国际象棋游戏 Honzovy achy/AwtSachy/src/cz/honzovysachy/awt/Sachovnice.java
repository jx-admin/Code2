package cz.honzovysachy.awt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
//import java.io.FileOutputStream;
//import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.swing.SwingUtilities;

import cz.honzovysachy.mysleni.Minimax;
import cz.honzovysachy.mysleni.ThinkingOutput;
import cz.honzovysachy.pravidla.PawnPromotionGUI;
import cz.honzovysachy.pravidla.Pozice;
import cz.honzovysachy.pravidla.Task;
import cz.honzovysachy.pravidla.ZobrazPole;

class PawnPromotionGUIMsgBx implements PawnPromotionGUI {

	@Override
	public int promotion() {
		return MessageBox.messageBox("Pawn promotion", "", MessageBox.MB_PROMENA);
	}
	
}

@SuppressWarnings("serial")
public class Sachovnice extends Component  implements KeyListener, ZobrazPole, MouseListener {
	static final Color mCerna = new Color(100, 100, 100); 
	static final Color mBila = new Color(200, 200, 200);
	static final Color mModra = new Color(0, 0, 255);
	static final Color mZelena = new Color(0, 255, 0);
	
	ThinkingOutput mOutput;
	int mPoleXY;
	int mOdstup;
	int mHrana;
	Task mTask = new Task(null);
	Image bk, ck, bp, cp, bs, cs, bj, cj, bd, cd, bv, cv;
	boolean mOtoceno;
	int mcx = 4;
	int mcy = 4;
	int mox = -1;
	int moy = -1;
//	byte[] mSchPriMysleni = new byte[Pozice.h8 + 1];
	boolean mBilyClovek = true;
	boolean mCernyClovek = false;
	boolean mPremyslim = false;
	
	protected Image loadImage(String img) {
		URL url = this.getClass().getResource("figury/" + img + ".png");
		return Toolkit.getDefaultToolkit().getImage(url);
	}

	
	public Sachovnice(int poleXY, int odstup, ThinkingOutput output) {
		mOutput = output; 
		addMouseListener(this);
		addKeyListener(this);
		mPoleXY = poleXY;
		mOdstup = odstup;
		mHrana = 8 * poleXY + 2 * odstup;
		mOtoceno = false;
		bk = loadImage("bk");
		bd = loadImage("bd");
		bv = loadImage("bv");
		bs = loadImage("bs");
		bj = loadImage("bj");
		bp = loadImage("bp");
		ck = loadImage("ck");
		cd = loadImage("cd");
		cv = loadImage("cv");
		cs = loadImage("cs");
		cj = loadImage("cj");
		cp = loadImage("cp");

		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(bk, 0);
		mediaTracker.addImage(ck, 1);
		mediaTracker.addImage(bp, 2);
		mediaTracker.addImage(cp, 3);
		mediaTracker.addImage(bs, 4);
		mediaTracker.addImage(cs, 5);
		mediaTracker.addImage(bj, 6);
		mediaTracker.addImage(cj, 7);
		mediaTracker.addImage(bd, 8);
		mediaTracker.addImage(cd, 9);
		mediaTracker.addImage(bv, 10);
		mediaTracker.addImage(cv, 11);
		try
		{
		    mediaTracker.waitForAll();
		}
		catch (InterruptedException ie)
		{
		    System.exit(1);
		}
	}
	
	public void otoc() {
		mOtoceno = !mOtoceno;
		repaint();
	}
	
	private int getX(int i) {
		if (mOtoceno) i = 7 - i;
		return mOdstup + mPoleXY * i;
	}
	
	private int getY(int j) {
		if (!mOtoceno) j = 7 - j;
		return mOdstup + mPoleXY * j;
	}
	
	private int getI(int x) {
		int zaklad = (x - mOdstup) / mPoleXY;
		if (mOtoceno) zaklad = 7 - zaklad;
		return zaklad;
	}
	
	private int getJ(int y) {
		int zaklad = (y - mOdstup) / mPoleXY;
		if (!mOtoceno) zaklad = 7 - zaklad;
		return zaklad;
	}
	
	public boolean isPremyslim() {
	  	return mPremyslim;
	}
	protected boolean hrajeClovek() {
		return !isPremyslim() && (mBilyClovek && mTask.mBoard.bily ||
			mCernyClovek && !mTask.mBoard.bily);
	}



	private void zobrazPole(int x, int y, boolean clovek, Graphics g) {
		Color c;
        if (clovek && mox == x && moy == y) {
       		c = mZelena;
       	} else
       	if (clovek && mcx == x && mcy == y) {
       		c = mModra;
       	} else {
       		c = ((((x + y) & 1) == 1) ? mBila : mCerna);
       	}
       	g.setColor(c);
        g.fillRect(getX(x), getY(y), mPoleXY, mPoleXY);
        int p = Pozice.a1 + x + y * 10;
        byte f = mTask.mBoard.sch[p];
        if (f == 0) return;
      
        int xx = getX(x) + (mPoleXY - bp.getWidth(null)) / 2;
        int yy = getY(y) + (mPoleXY - bp.getHeight(null)) / 2;
        
        switch (f) {
        case 1:
          g.drawImage(bp, xx, yy, null);
          break;
        case 2:
          g.drawImage(bj, xx, yy, null);
          break;
        case 3:
          g.drawImage(bs, xx, yy, null);
          break;
        case 4:
          g.drawImage(bv, xx, yy, null);
          break;
        case 5:
          g.drawImage(bd, xx, yy, null);
          break;
        case 6:
          g.drawImage(bk, xx, yy, null);
          break;
        case -1:
          g.drawImage(cp, xx, yy, null);
          break;
        case -2:
          g.drawImage(cj, xx, yy, null);
          break;
        case -3:
          g.drawImage(cs, xx, yy, null);
          break;
        case -4:
          g.drawImage(cv, xx, yy, null);
          break;
        case -5:
          g.drawImage(cd, xx, yy, null);
          break;
        case -6:
          g.drawImage(ck, xx, yy, null);
          break;
        }
	}
	
	public void paint(Graphics g) {
	    g.clearRect(0, 0, mHrana, mHrana);
	    
	    boolean clovek = hrajeClovek();
	    
	    //g.setColor(mCerna);
	    for (int i = 0; i < 8; i++) {
	    	for (int j = 0; j < 8; j++) {
	    		zobrazPole(i, j, clovek, g);
	        }
	    }
	    if (mTask.mEnd != 0) {
	    	g.setFont(new Font("Dialog", Font.BOLD, mPoleXY / 3));
	    	g.setColor(new Color(0xFF, 0, 0));
	    	g.drawString(mTask.getEndOfGameString(mTask.mEnd), 50, 50);
	    	
	    	/*
	    	try{
	    	mTask.savePNG(new FileOutputStream("/home/honza/moje.pgn"), true);
	    	} catch (IOException e) {};
	    	*/
	    }
	  }
	
	 public Dimension getMaximumSize() {
		 return getPreferredSize();
	}
		  
	public Dimension getMinimumSize() {
	    return getPreferredSize();
	}
		  
	public Dimension getPreferredSize() {
		return new Dimension(mHrana, mHrana);
	}

	@Override
	public void keyPressed(KeyEvent k) {
		int code = k.getKeyCode();
		switch (code) {
		case 37:
			if (mOtoceno && mcx < 7 || !mOtoceno && mcx > 0) {
				int x = mcx;
    			if (mOtoceno) mcx++; else mcx--;
    			zobrazPole(Pozice.a1 + x + mcy * 10);
    			zobrazPole();
    		}
			break;
		case 38:
			if (!mOtoceno && mcy < 7 || mOtoceno && mcy > 0) {
				int y = mcy;
    			if (mOtoceno) mcy--; else mcy++;
    			zobrazPole(Pozice.a1 + mcx + y * 10);
    			zobrazPole();
    		}
			break;
		case 39:
			if (!mOtoceno && mcx < 7 || mOtoceno && mcx > 0) {
				int x = mcx;
    			if (mOtoceno) mcx--; else mcx++;
    			zobrazPole(Pozice.a1 + x + mcy * 10);
    			zobrazPole();
    		}
			break;
		case 40:
			if (mOtoceno && mcy < 7 || !mOtoceno && mcy > 0) {
				int y = mcy;
    			if (mOtoceno) mcy++; else mcy--;
    			zobrazPole(Pozice.a1 + mcx + y * 10);
    			zobrazPole();
    		}
			break;
		case 10:
			if (!hrajeClovek()) return;
    		Vector t = mTask.nalezTahyVector();
    		int pole = Pozice.a1 + mcx + 10 * mcy;
    		if (mTask.JeTam1(t, pole)) {
    			int stare = Pozice.a1 + mox + moy * 10;
    			mox = mcx;
    			moy = mcy;
    			zobrazPole(stare);
    			zobrazPole();
    			return;
    		}
    		int pole1 = Pozice.a1 + mox + 10 * moy;
    		if (mTask.JeTam2(t, pole1, pole)) {
    			int tah = mTask.makeMove(t, pole1, pole, new PawnPromotionGUIMsgBx());
    			if (tah != 0) tahni(tah);
    			return;
    		}
  		}
	}
	
	public void tahni(int tah) {
    	mox = -1;
    	moy = -1;
    	mTask.tahni(tah, true, true, this);
    	pripravTah();
    	if (hrajeClovek()) zobrazPole();
    	if (mTask.mEnd != 0) repaint();
    }

	public void pripravTah() {
    	SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pripravTahHned();
			}
    	});
    }
	
	protected void pripravTahHned() {
    	if (hrajeClovek()) {
    	} else {
			tahniPrograme();
    	}
    }
	
	protected void tahniPrograme() {
    	mPremyslim = true;
    	Thread t = new Thread() {
    		 public void run() {
    			// mTask.board.nalezTahy();
    			 final int tah;
    			 tah = Minimax.minimax(mTask, 5000, mOutput); 
    			 SwingUtilities.invokeLater(
    					 new Runnable() {

							public void run() {
								mPremyslim = false;
								if (tah != 0) 
									tahni(tah);
								}}
    					 );
    		 }
    	};
    	t.start();
     }
	
	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}


	@Override // ZobrazPole
	public void zobrazPole(int pole) {
		Graphics g = getGraphics();
		pole -= Pozice.a1;
		if (pole < 0) return;
		int x = pole % 10;
		int y = pole / 10;
		if (x >= 0 && x < 8 && y >= 0 && y < 8)
		zobrazPole(x, y, hrajeClovek(), g);
	}
	
	public void zobrazPole() {
		zobrazPole(Pozice.a1 + mcx + 10 * mcy);
	}


	@Override
	public void mouseClicked(MouseEvent me) {
		requestFocus();
		if (me.getButton() == 3) {
			PopupMenu m = new PopupMenu();
			MenuItem flipBoard = new MenuItem("Flip board");
			flipBoard.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					otoc();
				}
			});
			m.add(flipBoard);
			if (!mPremyslim) {
				MenuItem newGame = new MenuItem("New game");
				newGame.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						mTask = new Task(null);
						repaint();
					}
				});
				m.add(newGame);
				MenuItem move = new MenuItem("Move");
				move.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						mBilyClovek = !mBilyClovek;
						mCernyClovek = !mCernyClovek;
						repaint();
						tahniPrograme();
					}
				});
				m.add(move);
				
				MenuItem undo = new MenuItem("Undo");
				undo.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (mTask.mIndexInGame >= 0) {
							mTask.tahniZpet(0, true, null);
							mBilyClovek = mTask.mBoard.bily;
							mCernyClovek = !mTask.mBoard.bily;
							repaint();
						}
					}
				});
				m.add(undo);
				
				MenuItem redo = new MenuItem("Redo");
				redo.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (mTask.mIndexInGame + 1 < mTask.mGame.size()) {
							mTask.tahni(0, true, false, null);
							mBilyClovek = mTask.mBoard.bily;
							mCernyClovek = !mTask.mBoard.bily;
							repaint();
						}
					}
				});
				m.add(redo);
			}

			this.add(m);
			m.show(this, me.getX(), me.getY());
		}
		if (me.getButton() == 1) {
			if (mPremyslim) return;
			int i = getI(me.getX());
			int j = getJ(me.getY());
			if (i < 0 || i > 7 || j < 0 || j > 7) return;
			Vector t = mTask.nalezTahyVector();
    		int pole = Pozice.a1 + i + 10 * j;
    		if (mTask.JeTam1(t, pole)) {
    			int stare = Pozice.a1 + mcx + mcy * 10;
    			mcx = i;
    			mcy = j;
    			zobrazPole(stare);
    			stare = Pozice.a1 + mox + moy * 10;
    			mox = mcx;
    			moy = mcy;
    			zobrazPole(stare);
    			zobrazPole();
    			return;
    		}
    		int pole1 = Pozice.a1 + mox + 10 * moy;
    		if (mTask.JeTam2(t, pole1, pole)) {
    			int tah = mTask.makeMove(t, pole1, pole, new PawnPromotionGUIMsgBx());
    			if (tah != 0) tahni(tah);
    			return;
    		}
		}
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
