
package com.accenture.mbank.view.table;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.accenture.mbank.util.LogManager;

public class FormView extends BankTableView {

    /**
     * Y轴坐标值画笔的宽度
     */
    float y_paint_scale = 20f / 577;

    /**
     * Y坐标的文字的长度
     */
    int yPaintScale = 0;

    float x_paint_scale = 20f / 577;

    int xPaintScale = 0;
 
    /**
     * title文字的大小比例
     */
    float title_text_size = 29f / 577;

    float title_line_size = 2f / 577;

    int titleLineSize = 0;

    /**
     * title文字的实际大小
     */
    int titleTextSize = 0;

    /**
     * 标题的下划线位置
     */
    float title_line_y = 47f / 577;

    int titleLineY = 0;

    float chartTitleY_rate = 155f /577;
    int chartTitleY = 0;
    /**
     * 图表的上下左右的边距
     */
    float padding_ = 50f / 577;

    int padding = 0;

    /**
     * x轴的x方向初始位置
     */
    int xAxisStartXPosition = 0;

    /**
     * x轴的x方向终位置
     */
    int xAxisEndXPosition = 0;

    /**
     * x轴的Y方向的位置
     */
    int xAxisYPosition = 0;

    /**
     * y轴的y方向初始位置
     */
    int yAxisStartYPosition = 0;

    /**
     * y轴的y方向终位置
     */
    int yAxisEndYPosition = 0;

    /**
     * y轴的X方向的位置
     */
    int yAxisXPosition = 0;

    /**
     * y轴的粗细比例
     */
    float y_axis_scale = 2f / 577;

    /**
     * y轴的粗细值
     */
    int yAxisScale = 0;

    /**
     * X轴的粗细比例
     */
    float x_axis_scale = 2f / 577;

    /**
     * X轴的粗细值
     */
    int xAxisScale = 0;

    /**
     * 画X坐标的画笔颜色
     */
    int xAxisColor = Color.rgb(100, 100, 100);

    /**
     * 画Y坐标的画笔颜色
     */
    int yAxisColor = Color.rgb(100, 100, 100);

    Context context;
    protected PopupWindow popupwindow = null;

	public PopupWindow getPopupWindow() {
		return popupwindow;
	}

    // Chart background color
    int bgColor1;
    int bgColor2;
    Typeface mFontFace = null;
    
    public FormView(Context context) {
        super(context);
    }

    public FormView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FormView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
     * Will be called by constructor, see base class.
     */
    protected void init(Context context) {
        this.context = context;
        
		bgColor1 = context.getResources().getColor(R.color.chart_bg_1);
		bgColor2 = context.getResources().getColor(R.color.chart_bg_2);
		createFont(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        LogManager.d("FormView::onMeasure" + width + " " + height );
        setMeasuredDimension(Math.max(width, height), Math.min(width, height));

        initPosition();

    }

    protected void initPosition() {

        int height = getMeasuredHeight();

        titleTextSize = (int)(height * title_text_size);
        titleLineSize = (int)(height * title_line_size);
        titleLineY = (int)(height * title_line_y) + titleLineSize;
        chartTitleY = (int)(height * chartTitleY_rate) + titleLineSize;

        padding = (int)(height * padding_);

        xAxisScale = (int)(height * x_axis_scale);
        yAxisScale = (int)(height * y_axis_scale);
        
        xPaintScale = (int)(height * x_paint_scale);
        yPaintScale = (int)(height * y_paint_scale);

    }

   Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        LogManager.d("FormView::onDraw" );
        super.onDraw(canvas);

        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(titleTextSize);

        paint.setStrokeWidth(titleLineSize);
        canvas.drawLine(0, titleLineY, getMeasuredWidth(), titleLineY, paint);
    }
    
	void FillText(View context, int intIdTitle, String title,
			int intIdValue, String value) {
		TextView tvTitle = (TextView) context.findViewById(intIdTitle);
		tvTitle.setText(title);

		TextView tvValue = (TextView) context.findViewById(intIdValue);
		tvValue.setText(value);
	}

	void FillText(View context, int intIdTitle, int idStringTitle,
			int intIdValue, String value) {
		String title = context.getResources().getString(idStringTitle);
		FillText(context, intIdTitle, title, intIdValue, value);
	}

	protected void drawTitleText(Canvas canvas, Paint textPaint,  String titleChart)
	{
        textPaint.setColor(getResources().getColor(R.color.chart_title));
        textPaint.setTextSize(titleTextSize);
        textPaint.setTypeface(mFontFace);
        textPaint.setTextAlign(Paint.Align.LEFT);

        canvas.drawText(titleChart, getMeasuredWidth()/2 - textPaint.measureText(titleChart)/2 , chartTitleY, textPaint);
	}
	
	private void createFont(Context context) {
		if (mFontFace == null) {
			mFontFace = Typeface.createFromAsset(context.getAssets(), "Futurabt.ttf");
		}
	}

	public void setTitleBarFont(View root) {
		createFont(context);

		TextView tv;
		tv= (TextView)root.findViewById(R.id.accountType);
		tv.setTypeface(mFontFace);
		
		tv= (TextView)root.findViewById(R.id.accountName);
		tv.setTypeface(mFontFace);
		
		tv= (TextView)root.findViewById(R.id.mid_title);
		tv.setTypeface(mFontFace);
		
		tv= (TextView)root.findViewById(R.id.mid_value);
		tv.setTypeface(mFontFace);
		
		setTableFont(root);
	}
	
	protected void setPopupWindowFont(View root) {
		createFont(context);

		TextView tv;

		tv= (TextView)root.findViewById(R.id.chart_popup_title);
		tv.setTypeface(mFontFace);

		tv= (TextView)root.findViewById(R.id.chart_popup_title_left);
		tv.setTypeface(mFontFace);
		
		tv= (TextView)root.findViewById(R.id.chart_popup_title_right);
		tv.setTypeface(mFontFace);

		setTableFont(root);
	}
	
	private void setTableFont(View root) {
		createFont(context);

		TextView tv;
		tv= (TextView)root.findViewById(R.id.row1_title);
		tv.setTypeface(mFontFace);
		
		tv= (TextView)root.findViewById(R.id.row1_value);
		tv.setTypeface(mFontFace);
		
		tv= (TextView)root.findViewById(R.id.row2_title);
		tv.setTypeface(mFontFace);
		
		tv= (TextView)root.findViewById(R.id.row2_value);
		tv.setTypeface(mFontFace);
		
		tv= (TextView)root.findViewById(R.id.row3_title);
		tv.setTypeface(mFontFace);
		
		tv= (TextView)root.findViewById(R.id.row3_value);
		tv.setTypeface(mFontFace);
	}

}
