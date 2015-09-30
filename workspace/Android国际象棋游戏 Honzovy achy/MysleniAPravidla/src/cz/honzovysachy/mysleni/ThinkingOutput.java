package cz.honzovysachy.mysleni;

public interface ThinkingOutput {
	public void depth(int depth);
	public void bestMove(String move, int value);
}
