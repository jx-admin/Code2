// $ANTLR 2.7.1: "pgn.g" -> "PGNParser.java"$

package de.java_chess.javaChess.pgn;

import antlr.*;
import antlr.collections.*;  

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

/**
 * A parser for PGN (Portable Game Notation) files.
 */
public class PGNParser extends antlr.LLkParser
       implements PGNTokenTypes
 {

protected PGNParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public PGNParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected PGNParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public PGNParser(TokenStream lexer) {
  this(lexer,2);
}

public PGNParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void pgnFile() throws RecognitionException, TokenStreamException {
		
		
		tagPairSection();
		moveTextSection();
	}
	
	public final void tagPairSection() throws RecognitionException, TokenStreamException {
		
		
		{
		_loop4:
		do {
			if ((LA(1)==EOF||LA(1)==MOVE_INDEX) && (_tokenSet_0.member(LA(2)))) {
				tagPair();
			}
			else {
				break _loop4;
			}
			
		} while (true);
		}
	}
	
	public final void moveTextSection() throws RecognitionException, TokenStreamException {
		
		
		{
		_loop16:
		do {
			if ((LA(1)==MOVE_INDEX)) {
				move();
			}
			else {
				break _loop16;
			}
			
		} while (true);
		}
	}
	
	public final void tagPair() throws RecognitionException, TokenStreamException {
		
		
		{
		if ((LA(1)==EOF||LA(1)==MOVE_INDEX) && (_tokenSet_0.member(LA(2)))) {
			eventTag();
		}
		else if ((LA(1)==EOF||LA(1)==MOVE_INDEX) && (_tokenSet_0.member(LA(2)))) {
			siteTag();
		}
		else if ((LA(1)==EOF||LA(1)==MOVE_INDEX) && (_tokenSet_0.member(LA(2)))) {
			dateTag();
		}
		else if ((LA(1)==EOF||LA(1)==MOVE_INDEX) && (_tokenSet_0.member(LA(2)))) {
			roundTag();
		}
		else if ((LA(1)==EOF||LA(1)==MOVE_INDEX) && (_tokenSet_0.member(LA(2)))) {
			whiteTag();
		}
		else if ((LA(1)==EOF||LA(1)==MOVE_INDEX) && (_tokenSet_0.member(LA(2)))) {
			blackTag();
		}
		else if ((LA(1)==EOF||LA(1)==MOVE_INDEX) && (_tokenSet_0.member(LA(2)))) {
			resultTag();
		}
		else {
			throw new NoViableAltException(LT(1), getFilename());
		}
		
		}
	}
	
	public final void eventTag() throws RecognitionException, TokenStreamException {
		
		
	}
	
	public final void siteTag() throws RecognitionException, TokenStreamException {
		
		
	}
	
	public final void dateTag() throws RecognitionException, TokenStreamException {
		
		
	}
	
	public final void roundTag() throws RecognitionException, TokenStreamException {
		
		
	}
	
	public final void whiteTag() throws RecognitionException, TokenStreamException {
		
		
	}
	
	public final void blackTag() throws RecognitionException, TokenStreamException {
		
		
	}
	
	public final void resultTag() throws RecognitionException, TokenStreamException {
		
		
	}
	
	public final void move() throws RecognitionException, TokenStreamException {
		
		
		match(MOVE_INDEX);
		ply();
		ply();
	}
	
	public final void ply() throws RecognitionException, TokenStreamException {
		
		
		{
		switch ( LA(1)) {
		case FIGURINE_LETTER_CODE:
		{
			match(FIGURINE_LETTER_CODE);
			break;
		}
		case LITERAL_x:
		case SQUARE_INDEX:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case LITERAL_x:
		{
			match(LITERAL_x);
			break;
		}
		case SQUARE_INDEX:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(SQUARE_INDEX);
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"DOT",
		"STRING_LITERAL",
		"SQUARE_NAME",
		"MOVE_INDEX",
		"FIGURINE_LETTER_CODE",
		"\"x\"",
		"SQUARE_INDEX",
		"SL_COMMENT"
	};
	
	private static final long _tokenSet_0_data_[] = { 1794L, 0L };
	public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);
	
	}
