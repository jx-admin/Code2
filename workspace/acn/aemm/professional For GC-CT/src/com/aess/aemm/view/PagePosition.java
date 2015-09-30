package com.aess.aemm.view;

public class PagePosition {
	public int index=-1;
	public int page,row,coloum;
	public PagePosition(){}
	public PagePosition(int page, int row, int coloum) {
		this.page=page;
		this.row=row;
		this.coloum=coloum;
	}
	public boolean equals(PagePosition pagePosition){
		if(page==pagePosition.page&&row==pagePosition.row&&coloum==pagePosition.coloum){
			return true;
		}else{
			return false;
		}
	}
	public void moveStep(int pRow,int pColoum,int step){
//		log.d("move to next s: "+getInfo());
		index+=step;
		coloum +=step;
		if (coloum >= pColoum) {
			coloum = 0;
			row++;
			if (row >= pRow) {
				row = 0;
				page++;
			}
		}else if (coloum < 0) {
			coloum = pColoum-1;
			row--;
			if (row <0) {
				row = pRow-1;
				page--;
			}
		}
//		log.d("move to next e: "+getInfo());
	}
	public int getIndex(int pRow,int pColoum){
		return page*pRow*pColoum+row*pColoum+coloum;
	}
	public void set(int index,int pRow,int pColoum) {
		this.index=index;
		page = index / pColoum / pRow;
		row =page <= 0 ? index / pColoum : (index % (pColoum * pRow))/ pColoum;
		coloum = index % pColoum;
	}
	public void set(PagePosition position){
//		index=index;
		page=position.page;
		row=position.row;
		coloum=position.coloum;
	}
	public void setIndex(int index){
		this.index=index;
	}
	public int getIndex(){
		return index;
	}
	public String getInfo(){
		return "index:"+index+"("+page+","+row+","+coloum+")";
	}
}
