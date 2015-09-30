import java.awt.Color;
class LNode{
	private int row;
	private int col;
	private Color chessColor;
	private LNode next;
	
	public LNode(int row,int col,Color chessColor){
		this.row=row;
		this.col=col;
		this.chessColor=chessColor;
	}
	public LNode(){
		this.next=null;
	}
	public void setRow(int row){
		this.row=row;
	}
	public int getRow(){
		return this.row;
	}
	public void setCol(int col){
		this.col=col;
	}
	public int getCol(){
		return this.col;
	}
	public void setChessColor(Color chessColor){
		this.chessColor=chessColor;
	}
	public Color getChessColor(){
		return this.chessColor;
	}
	public void setNext(LNode next){
		this.next=next;
	}
	public LNode getNext(){
		return this.next;
	}
}