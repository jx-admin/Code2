package cz.honzovysachy.awt;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import cz.honzovysachy.mysleni.ThinkingOutput;

@SuppressWarnings("serial")
public class ChessStringBox extends Panel implements ThinkingOutput {
	Label mDepth;
	Label mMove;
	ChessStringBox() {
		Label bDepth = new Label("Depth / move");
		bDepth.setFont(new Font("Dialog", Font.BOLD, 12));
		setLayout(new GridLayout(1, 3));
		add(bDepth);
		add(mDepth = new Label(""));
		add(mMove = new Label(""));
	}

	@Override
	public void depth(int depth) {
		mDepth.setText("" + depth);		
	}
	@Override
	public void bestMove(String move, int value) {
		mMove.setText(move + " (" + value + ")");
	}
}
