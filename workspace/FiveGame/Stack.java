import java.awt.Color;

class Stack{
	private LNode headLNode;
	
	public Stack(){
		initStack();
	}
	public void initStack(){
		headLNode=new LNode();
	}
	public LNode getHeadNode(){
		return this.headLNode;
	}
	
	public int StackLength(){
		LNode p=headLNode.getNext();
		int count=0;
		while(p!=null){
			count++;
			p=p.getNext();
		}
		return count;
	}
	
	public void Push(int row,int col,Color chessColor){
		LNode p=new LNode(row,col,chessColor);
		p.setNext(headLNode.getNext());
		headLNode.setNext(p);
	}
	public boolean Pop(){
		LNode p;
		if(headLNode.getNext()==null){
			return false;
		}
		p=headLNode.getNext();
		headLNode.setNext(p.getNext());
		return true;
	}
	public LNode getStackTop(){
		if(headLNode.getNext()==null){
			return null;
		}
		else{
			return headLNode.getNext();
		}
	}
	public void clearStack(){
		headLNode.setNext(null);
	}
}