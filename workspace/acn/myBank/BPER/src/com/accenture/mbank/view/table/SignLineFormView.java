
package com.accenture.mbank.view.table;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.ChartActivity;
import com.accenture.mbank.model.GetFinancingInfoModel;
import com.accenture.mbank.model.InstallmentsModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;

/**
 * @author seekting.x.zhang
 * For loans chart
 */
@SuppressLint("NewApi")

public class SignLineFormView extends FormView {
    public List<InstallmentsModel> installments;

    public String residualCapital;
    private String mPersonizedName = "";

    Paint mPaint = new Paint();
    Paint textPaint = new Paint();

    /**
     * X轴上每等分的距离
     */
    int xCellLength = 0;

    /**
     * Y轴上每等分的距离
     */
    int yCellLength = 0;

    /**
     * X轴上的标尺值
     */
    public List<String> xValue;

    /**
     * Y轴上的标尺值
     */
    public List<Double> yValue;

    public List<Double> xYValues;

    public GetFinancingInfoModel getFinancingInfoModel;

    /**
     * y轴方向每个像素所占的值
     */
    float yCell = 0;

    int xyColor;

    int yFocusColor;

    /**
     * 折点有焦点时的颜色
     */
    int xyTouchColor;

    /**
     * 圆矩形的颜色
     */
    int xyDetailColor;

    public static class XY {
        float x;
        float y;
    }

    List<XY> xys;

    /**
     * 画拆线的画笔宽度比例
     */
    float x_y_paint_scale = 5f / 577;

    int xyPaintScale = 0;

    float y_paint_width = 2f / 577;

    int yPaintWidth = 0;

    float y_paint_width_focus = 4f / 577;

    int ypaintWidthFoucs = 0;

    /**
     * 拆线转折点的半径比例
     */
    float x_o_y_r = 9f / 577;

    int XOYR = 0;

    /**
     * 详情矩形宽度
     */
    int detailWidth = 0;

    /**
     * 详情矩形高度
     */
    int detailHeight = 0;

    float detail_round_r = 10f / 577;

    int detailRoundR = 0;

    /**
     * 拆线转折点有焦点时显示 的半径比例
     */
    float x_o_y_r_touch = 30f / 577;

    int xOYRTouch = 0;

    /**
     * 圆与矩形 的间隔
     */
    float detail_touch_y = 5f / 577;

    int detailTouchY = 0;

    float detail_text_1_size = 40f / 577;

    float detail_text_2_size = 30f / 577;

    int detailText1Size = 0;

    int detailText2Size = 0;

    /**
     * 双手触摸时出现slidBar的大小
     */
    float slid_bar_r = 26f / 577;

    int slidBarR = 0;

    String twoPointDetailText1 = "-$3.000,99(-1,5%)";

    String twoPointDetailText2 = "01 feb 2012 -115 apr 2012";

    float two_point_detail_text1_size = 30f / 577;

    int twoPointDetailText1Size = 0;

    float two_point_detail_text2_size = 20f / 577;

    int twoPointDetailText2Size = 0;

    /**
     * 详情显示时上下margin
     */
    float two_point_detail_rect_margin = 10f / 577;

    int twoPointDetailRectMargin = 0;

    /**
     * 下三角边长
     */
    float two_point_detail_sanjiao_length = 20f / 577;

    int twoPointDetailSanjiaoLength = 0;

    float residual_capital_size = 29f / 577;

    int residualCapitalSize = 0;

    String left = "cap.residue";
    String right = "amount install";

    /**
     * dynamically measured in onMeasure
     */
    private int screen_width;

    /**
     * dynamically measured in onMeasure
     */
    private int screen_height;

    public SignLineFormView(Context context) {
        super(context);

        left = context.getResources().getString(R.string.residual_capital1);
        right = context.getResources().getString(R.string.installment_amount);

        xyColor = context.getResources().getColor(R.color.xycolor_loan);
        yFocusColor = context.getResources().getColor(R.color.xycolor_loan);
        /**
         * 折点有焦点时的颜色
         */
        xyTouchColor = context.getResources().getColor(R.color.xytouchcolor_loan);
        /**
         * 圆矩形的颜色
         */
        xyDetailColor = context.getResources().getColor(R.color.xydetailcolor_loan);

        if (BaseActivity.isOffline) {
            xValue = new ArrayList<String>();
            xValue.add("may.2013");
            xValue.add("jun.2013");
            xValue.add("jul.2013");
            xValue.add("aug.2013");
            xValue.add("set.2013");

            // this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            xYValues = new ArrayList<Double>();
            xYValues.add(220000d);
            xYValues.add(215000d);
            xYValues.add(210000d);
            xYValues.add(205000d);
            xYValues.add(200000d);

            this.getFinancingInfoModel = new GetFinancingInfoModel();
            this.getFinancingInfoModel.setTotalAmountl("300000");
            this.getFinancingInfoModel.setResidueAmount("20000");
            this.getFinancingInfoModel.setEndDate("15/03/2014 00:00:00");
            
            installments = new ArrayList<InstallmentsModel>();

            InstallmentsModel data1 = new InstallmentsModel();
            data1.setAmount(5000d);
            data1.setDeadlineDate(TimeUtil.getTimeByString("2014-02-10 03:30:34", TimeUtil.dateFormat2));
            installments.add(data1);
            
            InstallmentsModel data2 = new InstallmentsModel();
            data2.setAmount(5000d);
            data2.setDeadlineDate(TimeUtil.getTimeByString("2014-02-10 03:30:34", TimeUtil.dateFormat2));
            installments.add(data2);
            
            InstallmentsModel data3 = new InstallmentsModel();
            data3.setAmount(5000d);
            data3.setDeadlineDate(TimeUtil.getTimeByString("2014-02-10 03:30:34", TimeUtil.dateFormat2));
            installments.add(data3);
            
            InstallmentsModel data4 = new InstallmentsModel();
            data4.setAmount(5000d);
            data4.setDeadlineDate(TimeUtil.getTimeByString("2014-02-10 03:30:34", TimeUtil.dateFormat2));
            installments.add(data4);
            
            InstallmentsModel data5 = new InstallmentsModel();
            data5.setAmount(5000d);
            data5.setDeadlineDate(TimeUtil.getTimeByString("2014-02-10 03:30:34", TimeUtil.dateFormat2));
            installments.add(data5);
            initYValue();
        }

        drawChartTitleBar();
    }

    public static final int NO_SIGN_SIZE = 5;

    public void initYValue() {
        double min = Integer.MAX_VALUE;
        double max = -Integer.MAX_VALUE;
        for (int i = 0; i < xYValues.size(); i++) {
            min = Math.min(xYValues.get(i), min);
            max = Math.max(xYValues.get(i), max);
        }

        yValue = new ArrayList<Double>();

		min = Math.max(0, min);
		double cell = getCell(max, min, NO_SIGN_SIZE);
		for (int i = 0; i <= NO_SIGN_SIZE; i++) {
			yValue.add(begin + i * cell);
		}
    }

    double begin = 0;

    private double getCell(double max, double min, int size) {
        double resultMax = getMaxValue(max, min);
        double resultMin = 0;

        double cell = (resultMax - resultMin) / size;
        begin = resultMin;
        return cell;
    }

    private double getMaxValue(double max, double min) {
    	double top = max;
        top = Math.max(top * 1.2, 0.01);
        double bottom = 0;

        double distance = Math.max(Math.abs(top), Math.abs(bottom));
        distance = Math.abs(top - bottom);

		return distance;
    }

    @Override
    protected void initPosition() {
        super.initPosition();
        // 算出Y轴坐标值需要的最大值
        int height = getMeasuredHeight();

        screen_height = getMeasuredHeight();
        screen_width = getMeasuredWidth();

        detailText1Size = (int)(detail_text_1_size * height);

        detailText2Size = (int)(detail_text_2_size * height);

        mPaint.setTextSize(yPaintScale);
        residualCapitalSize = (int)(residual_capital_size * height);
        int maxLength = 0;
        for (double value : yValue) {
            String str = Utils.generateFormatMoneyInt(Contants.COUNTRY, value);
            // String str = String.valueOf(value);
            maxLength = (int)Math.max(mPaint.measureText(str), maxLength);
        }

        yAxisXPosition = maxLength + padding;
        yAxisStartYPosition = titleLineY + padding;

        yAxisEndYPosition = getMeasuredHeight() - padding - 2 * yPaintScale;
        xAxisEndXPosition = getMeasuredWidth() - padding;
        xAxisStartXPosition = yAxisXPosition;

        // 每等分的长度
        yCellLength = (int)((float)(yAxisEndYPosition - yAxisStartYPosition)
                / ((float)yValue.size()) - 1);

        // y轴每
        yCell = (float)((yValue.get(1) - yValue.get(0)) / yCellLength);

        xCellLength = (int)((float)(xAxisEndXPosition - xAxisStartXPosition) / ((float)xValue
                .size()));

        // 初始化拆线的点的坐标

        xys = new ArrayList<SignLineFormView.XY>();
        for (int i = 0; i < xYValues.size(); i++) {

            double xyValue = xYValues.get(i);
            int x = yAxisXPosition + (i + 1) * xCellLength - xCellLength / 2;
            // Y轴长度
            float y = yAxisEndYPosition - (int)((xyValue - yValue.get(0)) / yCell);
            XY xy = new XY();
            xy.x = x;
            xy.y = y;
            xys.add(xy);
        }

        xyPaintScale = (int)(x_y_paint_scale * height);
        XOYR = (int)(x_o_y_r * height);

        xOYRTouch = (int)(x_o_y_r_touch * height);

        detailTouchY = (int)(detail_touch_y * height);

        detailRoundR = (int)(detail_round_r * height);

        slidBarR = (int)(slid_bar_r * height);

        twoPointDetailText1Size = (int)(two_point_detail_text1_size * height);
        twoPointDetailText2Size = (int)(two_point_detail_text2_size * height);
        twoPointDetailRectMargin = (int)(two_point_detail_rect_margin * height);
        twoPointDetailSanjiaoLength = (int)(two_point_detail_sanjiao_length * height);
        yAxisStartYPosition = yAxisStartYPosition + twoPointDetailText1Size
                + twoPointDetailText2Size;

        // 竖线画笔宽度
        yPaintWidth = (int)(y_paint_width * height);
        ypaintWidthFoucs = (int)(y_paint_width_focus * height);

    }

    Path mpath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        textPaint.setTypeface(mFontFace);

        drawBackgroundColor(canvas);
        drawAxis(canvas);

        mPaint.setStrokeWidth(1);
        mPaint.setPathEffect(null);

        drawVerticalLines(canvas);
        drawTextMonths(canvas);

        drawYAxis(canvas);

        drawConnectedLines(canvas);
        drawTitleText(canvas, textPaint, getResources().getString(R.string.chart_title_loan));
        
        mPaint.setColor(xyColor);
        mPaint.setStyle(Paint.Style.FILL);

        // 画实心圆
        for (int i = 0; i < xys.size(); i++) {

            XY xy = xys.get(i);
           	canvas.drawCircle(xy.x, xy.y, XOYR, mPaint);
        }

        if (showDetail) {
        	drawPopupText(canvas);
        }
    }

	private void drawBackgroundColor(Canvas canvas) {
        canvas.drawColor(bgColor2);

        mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);

		int color = bgColor2;

		// Draw rect background
		for (int i = 1; i <= xValue.size(); i++) {
			if (color == bgColor2) {
				color = bgColor1;
			} else {
				color = bgColor2;
			}
			mPaint.setColor(color);

			float left = yAxisXPosition + (i - 1) * xCellLength;
			float bottom = getMeasuredHeight();
			float right = yAxisXPosition + i * xCellLength;
			float top = 0;
			canvas.drawRect(left, top, right, bottom, mPaint);
		}
	}

	private void drawAxis(Canvas canvas) {
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.GRAY);
		mPaint.setStrokeWidth(yAxisScale);
		// 画Y坐标轴
		canvas.drawLine(yAxisXPosition, 0, yAxisXPosition, yAxisEndYPosition,
				mPaint);
		// 画X坐标轴
		canvas.drawLine(yAxisXPosition, yAxisEndYPosition, yAxisXPosition
				+ xValue.size() * xCellLength, yAxisEndYPosition, mPaint);
	}

	private void drawYAxis(Canvas canvas) {
        // 画纵坐标的数值
        textPaint.setTextSize(yPaintScale);
        for (int i = 0; i < yValue.size(); i++) {

            String text = Utils.generateFormatMoney(Contants.COUNTRY, yValue.get(i));

            /*
             *  remove the "+" near 0,00 at the y axis.
             */
            if (yValue.get(i) == 0) {
            	text = Utils.noSignGenerateFormatMoney(Contants.COUNTRY, yValue.get(i));
            }

            int y = yAxisEndYPosition - i * yCellLength;
            int x = yAxisXPosition - 2 * yAxisScale;
            textPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(text, x, y, textPaint);
        }
	}

	private void drawVerticalLines(Canvas canvas) {
		// 画竖线
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.GRAY);
		mPaint.setStrokeWidth(yAxisScale / 2);
		for (int i = 1; i <= xValue.size(); i++) {
			float xx = yAxisXPosition + i * xCellLength;
			canvas.drawLine(xx, 0, xx, yAxisEndYPosition, mPaint);
		}
	}

	private void drawTextMonths(Canvas canvas)
	{
        // 画横坐标的数值
        for (int i = 0; i < xValue.size(); i++) {
            mpath.reset();

            String text = xValue.get(i);

            String str1 = text;
            String str2 = "";
            if (text.contains(TimeUtil.detaFormat6Split)) {
                str1 = text.substring(0, text.indexOf(TimeUtil.detaFormat6Split));
                /*
                 * show upper case for month
                 */
                str1 = str1.toUpperCase(Locale.US);
                str2 = text.substring(text.indexOf(TimeUtil.detaFormat6Split) + 1, text.length());
            }

            int x = yAxisXPosition + (i + 1) * xCellLength;

            textPaint.setAntiAlias(true);
            textPaint.setColor(Color.GRAY);
            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.setTypeface(mFontFace);
            textPaint.setTextSize(xPaintScale);
            
    		if (xValue.size() >= Contants.CHART_THRESHOLD_MONTHS || xCellLength <= textPaint.measureText(str1)) {
    			str1 = str1.substring(0, 3);
    		}

    		mpath.moveTo(x - xCellLength, yAxisEndYPosition + 1.5f * yPaintScale);
            mpath.lineTo(x , yAxisEndYPosition + 1.5f * yPaintScale);
 
            canvas.drawTextOnPath(str1, mpath, xCellLength/2 - textPaint.measureText(str1) /2 , 0, textPaint);
            canvas.drawTextOnPath(str2, mpath, xCellLength/2 - textPaint.measureText(str2) /2 , 1.5f * yPaintScale, textPaint);
        }
	}

	private void drawConnectedLines(Canvas canvas) {
        mpath.reset();
        // 画折线
        for (int i = 0; i < xys.size(); i++) {

            XY xy = xys.get(i);

            if (i == 0) {
                mpath.moveTo(xy.x, xy.y);
            } else {
                mpath.lineTo(xy.x, xy.y);
            }
            
            if ( i== xys.size() -1) {
            	mpath.lineTo(xy.x + xCellLength /2, xy.y);
            	mpath.lineTo(xy.x + xCellLength /2, yAxisEndYPosition);  // right bottom corner
            	mpath.lineTo(yAxisXPosition, yAxisEndYPosition); // left bottom corner
            	mpath.lineTo(yAxisXPosition, xys.get(0).y);
            }
        }

        mpath.close();
        mPaint.setColor(xyDetailColor);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(xyPaintScale);

        canvas.save();
        canvas.drawPath(mpath, mPaint);
        canvas.restore();
        mpath.reset();
	}

	private void drawPopupText(Canvas canvas)
	{
        mPaint.setStrokeWidth(ypaintWidthFoucs);

        XY xy = xys.get(touchPoint);

        mPaint.setColor(xyTouchColor);
		/*
		 * Draw new circle design
		 */
       	drawSlidbar(canvas, xy.x, xy.y, 0);

        mPaint.setShader(null);
	}

    private void drawSlidbar(Canvas canvas, float x, float y, int index) {
        mPaint.setColor(Color.rgb(150, 150, 150));
        canvas.drawCircle(x, y, slidBarR, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, slidBarR - 1, mPaint);
        mPaint.setColor(xyDetailColor);
        canvas.drawCircle(x, y, slidBarR * 0.8f, mPaint);
    }

    /**
     * x0在x1在右边x0大于x1
     */
    float x0 = -100, x1 = -100, y0 = -100, y1 = -100;

    boolean up0 = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int pointerCount = event.getPointerCount();
		float x = event.getX();
		float y = event.getY();
		int touch = touchThePoint(x, y);

		LogManager.d("touch" + action + "count" + pointerCount);
		float minX = xys.get(0).x;
		float maxX = xys.get(xys.size() - 1).x;

		if (action != MotionEvent.ACTION_UP
				&& (x > maxX + slidBarR || x < minX - slidBarR)) {
			return true;
		}
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (touch > -1) {
				downTouchThePoint = true;
				showTouchPoint(touch);
			}

			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL: {

			if (touch > -1 && downTouchThePoint) {
//				showDeatil(touch);
			} else {
				hideTouchPoint();
				hideDetail();
			}
			downTouchThePoint = false;
			break;
		}

		default:
			break;
		}

		return true;
	}

    /**
     * 隐藏被点击的节点
     */
    private void hideTouchPoint() {
        touchPoint = -1;
        invalidate();
    }

    int touchPoint = -1;

    /**
     * 显示被点击的节点
     * 
     * @param touch
     */
    private void showTouchPoint(int touch) {

        touchPoint = touch;
        showDeatil(touch);
        invalidate();
    }

    boolean showDetail = false;

    int WIDTH_POPUP = 440;
    int HEIGHT_POPUP = 140;

    int WIDTH_STANDARD = 800;
    int HEIGHT_STANDARD = 442;

    private int getWidthPopup() {
    	return  WIDTH_POPUP * screen_width / WIDTH_STANDARD;
    }
    
    private int getHeightPopup() {
    	return HEIGHT_POPUP * screen_height / HEIGHT_STANDARD ;
    }

    /**
     * 显示详情
     */
    private void showDeatil(int touch) {

		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();
		
        showDetail = true;
        if (popupwindow != null)
        	popupwindow.dismiss();

        XY xy = xys.get(touch);
        
		View root = View.inflate(chartActivity, R.layout.chart_popup, null);

		popupwindow = new PopupWindow(root, getWidthPopup(), getHeightPopup());
		if (ViewConfiguration.get(context).hasPermanentMenuKey() || (touch != xys.size()-1 && touch != xys.size()-2)) {
			popupwindow.showAtLocation(root, Gravity.NO_GRAVITY, (int)xy.x - getWidthPopup()/2,
					(int)xy.y - getHeightPopup());
		} else{
			int delta = 0;
			if (touch == xys.size()-1) {
				delta = 300;
			} else{
				delta = 200;
			}
			popupwindow.showAtLocation(root, Gravity.NO_GRAVITY, (int)xy.x - getWidthPopup()/2 - delta,
					(int)xy.y - getHeightPopup());
		}
		
		drawChartPopupWindow(root, touch);

		chartActivity.setPopupWindow(popupwindow);

        invalidate();
    }

    private void hideDetail() {

        showDetail = false;
        if (popupwindow != null)
        	popupwindow.dismiss();

        invalidate();
    }

    boolean downTouchThePoint = false;

    /**
     * 判断手势是否碰到了拆线点
     * 
     * @return
     */
    private int touchThePoint(float x, float y) {
        int result = -1;
        for (int i = 0; i < xys.size(); i++) {
            XY xy = xys.get(i);

            float beginX = xy.x - 1.2f * xOYRTouch;
            float endX = xy.x + 1.2f * xOYRTouch;
            float beginY = xy.y - 1.2f * xOYRTouch;
            float endY = xy.y + 1.2f * xOYRTouch;

            if (x > beginX && x < endX && y > beginY && y < endY) {

                result = i;
                return result;
            }

        }
        return result;

    }

    public void setPersonizedName(String personizedName) {
    	this.mPersonizedName = personizedName;
    	drawChartTitleBar();
    }

    public void setIsPreferred(boolean isPreferred) {
		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();
		
		ImageView isPreferredStar =(ImageView) chartActivity.chartsWindow.findViewById(R.id.isPreferredStar);
		
		isPreferredStar.setImageResource(R.drawable.icone_star_red);

		if (isPreferred)
			isPreferredStar.setVisibility(View.VISIBLE);
		else
			isPreferredStar.setVisibility(View.GONE);
    }

	public void drawChartPopupWindow(View root, int index) {
		InstallmentsModel mInstallmentsModel=installments.get(index);
		LinearLayout layout = (LinearLayout)root.findViewById(R.id.chart_popup_window);
		layout.setBackgroundResource(R.drawable.shape_roundrect_loan);

		setPopupWindowFont(root);

			View seperator = (LinearLayout) root.findViewById(R.id.chart_popup_seperator_loan);
			seperator.setVisibility(View.VISIBLE);

		TextView tv = (TextView) root.findViewById(R.id.chart_popup_title);
		String month = this.xValue.get(index);
		month=TimeUtil.dateFomateLocale(mInstallmentsModel.getDeadlineDate(), TimeUtil.detaFormat7);
		if(month!=null&&month.length()>1){
			String firtChar=month.substring(0, 1);
			firtChar=firtChar.toUpperCase();
			month=firtChar+month.substring(1);
		}
		tv.setText(month);

		/*
		 * Capital residue
		 */
        double residual = xYValues.get(index);
        String residualCapital = Utils.generateFormatMoney(Contants.COUNTRY, residual);
        
		FillText(root, R.id.row1_title,
				R.string.residual_capital1, R.id.row1_value,residualCapital);
		
		/*
		 * installment amount
		 */
	       String amount = Utils
	                .generateFormatMoney(Contants.COUNTRY, mInstallmentsModel.getAmount());
		FillText(root, R.id.row2_title, R.string.installment_amount, R.id.row2_value, amount);
		
		/*
		 * percent of residue/total capital
		 */
		double percent;
		double total = 0;
		try {
			total = Double.parseDouble(getFinancingInfoModel.getTotalAmountl());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (total == 0)
			percent = 0;
		else
			percent = residual * 100 / total ;

		String strPercent = Utils.formatPercentDouble(percent);
		
		String row3Title = getResources().getString(R.string.residual_capital1)  + "/" + getResources().getString(R.string.loans_total_amount);
		FillText(root, R.id.row3_title, row3Title, R.id.row3_value, strPercent);
	}

	private void drawChartTitleBar() {
		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();

		setTitleBarFont(chartActivity.chartsWindow);
		/*
		 * Label for the names of the loan
		 */
		FillText(chartActivity.chartsWindow, R.id.accountType,
				R.string.financing_title_name, R.id.accountName, mPersonizedName);
		
		TextView tvAccountType = (TextView) chartActivity.chartsWindow
				.findViewById(R.id.accountType);
		tvAccountType.setTextColor(getResources().getColor(R.color.xycolor_loan));
		TextView tvAccountName = (TextView) chartActivity.chartsWindow
				.findViewById(R.id.accountName);
		tvAccountName.setTextColor(getResources().getColor(R.color.xycolor_loan));

		if (getFinancingInfoModel == null)
			return;
		/*
		 * residue amount
		 */
        String residu = Utils.generateFormatMoney(getResources()
                .getString(R.string.eur), getFinancingInfoModel.getResidueAmount());
		FillText(chartActivity.chartsWindow, R.id.mid_title, R.string.residual_capital1, R.id.mid_value, residu);
		
		TextView tvMidTitle = (TextView) chartActivity.chartsWindow
				.findViewById(R.id.mid_title);
		tvMidTitle.setTextColor(getResources().getColor(R.color.xycolor_loan));
		TextView tvMidValue = (TextView) chartActivity.chartsWindow
				.findViewById(R.id.mid_value);
		tvMidValue.setTextColor(getResources().getColor(R.color.xycolor_loan));
		
		/*
		 * total amount
		 */
        String totalamount = Utils.generateFormatMoney(getResources()
                .getString(R.string.eur), getFinancingInfoModel.getTotalAmountl());
        FillText(chartActivity.chartsWindow, R.id.row1_title, R.string.loans_total_amount, R.id.row1_value, totalamount);
        
        /*
         * end date
         */
        String enddate = getFinancingInfoModel.getEndDate();
        String date = "";
        if (!enddate.equals("")) {
        	date = TimeUtil.changeFormattrString(enddate,TimeUtil.dateFormat2a, TimeUtil.dateFormat5);
        } else {
        	date = getResources().getString(R.string.not_able);
        }
        FillText(chartActivity.chartsWindow, R.id.row2_title, R.string.end_date, R.id.row2_value, date);
        
        /*
         * Updated date
         */
        String nowTime = TimeUtil.getDateString(System.currentTimeMillis(), TimeUtil.dateFormat5);
        FillText(chartActivity.chartsWindow, R.id.row3_title, R.string.data_updated_on, R.id.row3_value, nowTime);
        
        /*
         * Icon 
         */
        ImageView iv = (ImageView) chartActivity.chartsWindow.findViewById(R.id.chart_icon);
        iv.setImageResource(R.drawable.top_icons_financing);
	}
}
