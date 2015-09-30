package com.custom.view;

public final class RectLD{
    public long left,right;
    public double top,bottom;
    
    public double height(){
        return bottom-top;
    }
    
    public long width(){
        return right-left;
    }
    
    public void reset(){
        left=0;
        right=0;
        top=0;
        bottom=0;
    }
}
