
package com.act.mbanking.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;

import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Time_scale;

public class AccountChartView extends ChartViewWidthPath {
	long MINSTEP=1000*60*60*24;
	int minScales=6;
    Paint mPaint;
    List<PointLD> datas;
    double xScale=1,yScale=1;
    int riseColor=0xff039254;
    int descentColor=0xffAA0832;
    RectLD dataArea=new RectLD();
    Path path=new Path();
    Path path2=new Path();
    

   
    public AccountChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        drawArea = false;
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
    }
    
    protected void drawLine(Canvas canvas) {
        mPaint.setColor(riseColor);
        // canvas.drawLine(o_x, o_y, x_end, y_end, mPaint);
        canvas.save();
        canvas.translate((float)(o_x-dataArea.left*xScale),(float)(o_y -dataArea.top*yScale));
        canvas.drawPath(path, mPaint);
        mPaint.setColor(descentColor);
        canvas.drawPath(path2, mPaint);
        // canvas.drawLine(0 , 0, xAxisLength, -yAxisLength, mPaint);
        canvas.restore();
    }
    
    public void setData(List<PointLD> datas){
        this.datas=datas;
        invalidate();
    }
    
    private RectLD culcDataArea(List<PointLD> datas){
        if(datas!=null){
            if(datas.size()>0){
                PointLD p=datas.get(0);
                dataArea.left=p.x;
                dataArea.right=p.x;
                dataArea.top=p.y;
                dataArea.bottom=p.y;
            }
            for(PointLD p:datas){
                if(dataArea.left>p.x){
                    dataArea.left=p.x;
                }else if(dataArea.right<p.x){
                    dataArea.right=p.x;
                }
                if(dataArea.top>p.y){
                    dataArea.top=p.y;
                }else if(dataArea.bottom<p.y){
                    dataArea.bottom=p.y;
                }
            }
        }
        return dataArea;
    }
    
    private void culcTable(){
        double yValue=dataArea.top;
        double yValueStep=dataArea.height()/yValues.length ;
        maxYAxisTextWidth=0;
        for (int i = 0; i < yValues.length; i++) {
            ValueWidthName valueWidthName = new ValueWidthName();
            valueWidthName.value = yValue;//i * cellYHeight/yScale;
            valueWidthName.name =String.format("%.2f", valueWidthName.value);
            yValues[i] = valueWidthName;
            float textWidth = textPaint.measureText(valueWidthName.getName());
            maxYAxisTextWidth = Math.max(maxYAxisTextWidth, textWidth);
            yValue+=yValueStep;
        }
        
        long xValue=dataArea.left;
        long xValueStep=dataArea.width()/minScales;
        if(xValueStep<MINSTEP){
        	xValueStep=MINSTEP;
        }
        int tmp=(int) (dataArea.width()/xValueStep+1);
        tmp=Math.max(tmp, minScales);
        long times[]=Time_scale.getListTime_scale(dataArea.left,dataArea.right,Time_scale.ONEDAY,false,tmp);
		
        xValues=new ValueWidthName [times.length];
        for (int i = 0; i < xValues.length; i++) {
            ValueWidthName valueWidthName = new ValueWidthName();
            valueWidthName.value = times[i];//i * cellYHeight/yScale;
            valueWidthName.name =TimeUtil.getDateString((long)valueWidthName.value, TimeUtil.dateFormat14);
            xValues[i] = valueWidthName;
        }
        
    }
    
    public void invalidate(){
        if(datas!=null&&datas.size()>0){
        dataArea=culcDataArea(datas);
        culcTable();
        initOXY();
        xScale=(xAxisLength)/dataArea.width();
        yScale=(yAxisLength)/dataArea.height();
        if(yScale>0){
            yScale=-yScale;
        }
        path.reset();
        path2.reset();
        PointLD p=new PointLD(datas.get(0));
        p.x*=xScale;
        p.y*=yScale;
        PointLD p2;
        boolean rase=false;
        path2.moveTo(p.x, (float)p.y);
        for(int i=1;i<datas.size();i++){
            p2=new PointLD(datas.get(i));
            p2.x*=xScale;
            p2.y*=yScale;
//          path2.lineTo(p2.x, p2.y);
          
            if(p2.y<p.y){
                if(!rase){
                    path.moveTo(p.x, (float)p.y);
                }
                rase=true;
                path.lineTo(p2.x, (float)p2.y);
            }else{
                if(rase){
                    path2.moveTo(p.x, (float)p.y);
                }
                rase=false;
                path2.lineTo(p2.x, (float)p2.y);
            }
            p=p2;
        }
        }
        super.invalidate();
    }
    
    public void test(){
        List<PointLD> datas=new ArrayList<PointLD>();
        PointLD p;
        int position=0;
        for(int i=0;i<20;i++){
            p=new PointLD();
            p.y=(Math.random()*350.1234)-100;
            position+=(Math.random()*100);
            p.x=System.currentTimeMillis()-i*24*60*60*1000;//position;
            datas.add(p);
        }
        setData(datas);
//        invalidate();
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        invalidate();
    }
    

    /**
     * 画x轴上的文字
     * 
     * @param canvas
     */
    protected void drawXaxisText(Canvas canvas) {
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setTextSize(xAxisTextSize);
        float x=o_x;
        int size=xValues.length;
        for (int i = 0; i <size; i++) {
            ValueWidthName valueWidthName = xValues[i];
            if(i==0){
                textPaint.setTextAlign(Align.LEFT);
            }else if(i==size-1){
                textPaint.setTextAlign(Align.RIGHT);
            	x+=cellXWidth;
            }else{
                textPaint.setTextAlign(Align.CENTER);
            	 x+=cellXWidth;
            }
            if (valueWidthName != null) {
            	String names[]=valueWidthName.getName().split("\n");
            	for(int j=0;j<line;j++){
                canvas.drawText(names[j],x,
                        o_y + xAxisValueSpacing + xAxisTextSize*(j+1), textPaint);
            	}
            }
        }
    }
    
    protected void initOXY() {
    	super.initOXY();
        cellXWidth = xAxisLength / ((float)xValues.length-1); 
    }

    public static class PointLD implements Comparable<PointLD>{
		@Override
		public int compareTo(PointLD another) {
			if(x>another.x){
				return 1;
			}else if(x<another.x){
				return -1;
			}
			return 0;
		}
		public PointLD(){}
		public PointLD(long x,double y){
			this.x=x;
			this.y=y;
		}
		public PointLD(PointLD pointLD) {
			this.x=pointLD.x;
			this.y=pointLD.y;
		}
		public long x;
		public double y;
    }
    
    public static class RectLD{
        public long left,right;
        public double top,bottom;
        
        public double height(){
            return bottom-top;
        }
        
        public long width(){
            return right-left;
        }
    }
}
