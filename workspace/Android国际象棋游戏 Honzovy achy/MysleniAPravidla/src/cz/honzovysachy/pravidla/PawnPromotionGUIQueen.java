package cz.honzovysachy.pravidla;

public class PawnPromotionGUIQueen implements PawnPromotionGUI {

	private int mType;
	public static final PawnPromotionGUIQueen gui = new PawnPromotionGUIQueen();
	
	public PawnPromotionGUIQueen() {
		mType = 5;
	}
	
	public PawnPromotionGUIQueen(int type) {
		mType = type;
	}
	
	@Override
	public int promotion() {
		return mType;
	}
	
}
