package cz.honzovysachy.pravidla;

public interface PawnPromotionGUI {
	/**
	 * 
	 * @return 0 = canceled, 2, 3, 4, 5 = knight, bishop, rook, queen
	 */
	public int promotion();
}
