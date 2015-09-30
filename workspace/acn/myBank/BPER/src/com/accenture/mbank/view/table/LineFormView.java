
package com.accenture.mbank.view.table;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
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
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.model.DashboardDataModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;

/**
 * 超炫拆线图 
 * 
 * For Account, IBAN Card
 * 
 * @author seekting.x.zhang
 */
@SuppressLint("NewApi")
public class LineFormView extends FormView {

    /**
     * data model
     */
    private DashBoardModel dashBoardModel;

    private boolean isIbanCard = false;
    
    boolean moveSlidBarEnable = true;

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
     * X轴单元格格数
     */
    int xCellCount=0;

    /**
     * X轴上的标尺值
     */
    private List<String> xValue;

    /**
     * Y轴上的标尺值
     */
    private List<Double> yValue;

    private List<Double> xYValues;

    
	List<Double> listWithDrawals;
	List<Double> listDeposits;
	List<Double> listAccountBalance;
	
	boolean bHaveMovements = true;
	
	/*
	 * Defines the Y range when there are no movements
	 */
	int NO_MOVEMENTS_TOP_VALUE = 8;

    /**
     * y轴方向每个像素所占的值
     */
    float yCell = 0;

    int xyColor;

    /**
     * 折点有焦点时的颜色
     */
    int xyTouchColor;

    /**
     * 圆矩形的颜色
     */
    int xyDetailColor;

    List<XY> xys;

    /**
     * 画拆线的画笔宽度比例
     */
    float x_y_paint_scale = 11f / 577;

    int xyPaintScale = 0;

    /**
     * 拆线转折点的半径比例
     */
    float x_o_y_r = 9f / 577;

    int XOYR = 0;


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

    /**
     * 双手触摸时出现slidBar的大小
     */
    float slid_bar_r = 26f / 577;

    int slidBarR = 0;

    String twoPointDetailText1 = "";
    String twoPointDetailText2 = "";
    String fromMonth = "";
    String toMonth = "";

    int mAverageY = 0;
    int mZeroY = 0;

    double mAverageValue = 0;
    double boundBalance = 0; 
    
    public void setBoundBalance(double boundBalance) {
    	this.boundBalance = boundBalance;
    	drawChartTitleBar();
    }

    public void setIsPreferred(boolean isPreferred) {
		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();
		
		ImageView isPreferredStar =(ImageView) chartActivity.chartsWindow.findViewById(R.id.isPreferredStar);
		if (isPreferred)
			isPreferredStar.setVisibility(View.VISIBLE);
		else
			isPreferredStar.setVisibility(View.GONE);
    }

    /**
     * dynamically measured in onMeasure
     */
    private int screen_width;

    /**
     * dynamically measured in onMeasure
     */
    private int screen_height;
    
    int WIDTH_POPUP = 360;
    int HEIGHT_POPUP = 140;
    int HEIGHT_POPUP_TWO_POINT = 80;
    
    int WIDTH_STANDARD = 800;
    int HEIGHT_STANDARD = 442;
    
    private int getWidthPopup() {
    	return WIDTH_POPUP * screen_width / WIDTH_STANDARD ;
    }
    
    private int getHeightPopup() {
    	return HEIGHT_POPUP * screen_height / HEIGHT_STANDARD ;
    }
    
    private int getHeightPopupTwoPoint() {
    	return HEIGHT_POPUP_TWO_POINT * screen_height / HEIGHT_STANDARD ;
    }

	/*
	 * For warning reduce purpose, without this there will be a warning at top
	 * of the file
	 */
	private LineFormView(Context context) {
		super(context);
	}

	public LineFormView(Context context, String serviceCode, DashBoardModel model) {
		/*
		 * First call general one, then assign special color if IBAN card
		 * This is only used for IBAN card.
		 * Modify LineFormView(Context) for other purpose.
		 */
		this(context, model);

		if (serviceCode.equals(Contants.IBAN_CARD_CODE)) {
			isIbanCard = true;

			xyColor = context.getResources().getColor(R.color.xycolor_card);
			xyTouchColor = context.getResources().getColor(R.color.xytouchcolor_card);
			xyDetailColor = context.getResources().getColor(R.color.xydetailcolor_card);
			
	        drawChartTitleBar();
		}
	}

    public LineFormView(Context context, DashBoardModel model) {
        super(context);

        this.dashBoardModel = model;

        xyColor = context.getResources().getColor(R.color.xycolor_account);
        xyTouchColor = context.getResources().getColor(R.color.xytouchcolor_account);
        xyDetailColor = context.getResources().getColor(R.color.xydetailcolor_account);

        if (BaseActivity.isOffline) {
            xValue = new ArrayList<String>();
            xValue.add("may");
            xValue.add("jun");
            xValue.add("jul");
            xValue.add("aug");
            xValue.add("set");

            xYValues = new ArrayList<Double>();
            xYValues.add(34d);
            xYValues.add(173d);
            xYValues.add(60d);
            xYValues.add(250d);
            xYValues.add(20d);
            
            this.dashBoardModel = new DashBoardModel();
            this.dashBoardModel.setPersonalizedName("Test personized name");
            this.dashBoardModel.setAccountCode("1078281");
            this.dashBoardModel.setAvailableBalance(5000d);
            
            List<DashboardDataModel> dashboardDataList = new ArrayList<DashboardDataModel>();
            DashboardDataModel data1 = new DashboardDataModel();
            data1.setAccountBalance(100d);
            data1.setDeposits(0);
            data1.setWithdrawals(-100d);
            data1.setLastUpdate("2014-02-10 03:30:34");
            dashboardDataList.add(data1);
            
            DashboardDataModel data2 = new DashboardDataModel();
            data2.setAccountBalance(0);
            data2.setDeposits(0);
            data2.setWithdrawals(0);
            data2.setLastUpdate("2014-01-31 23:59:59");
            dashboardDataList.add(data2);
            
            DashboardDataModel data3 = new DashboardDataModel();
            data3.setAccountBalance(200d);
            data3.setDeposits(0);
            data3.setWithdrawals(0);
            data3.setLastUpdate("2013-12-31 23:59:59");
            dashboardDataList.add(data3);
            
            DashboardDataModel data4 = new DashboardDataModel();
            data4.setAccountBalance(200d);
            data4.setDeposits(0);
            data4.setWithdrawals(-27);
            data4.setLastUpdate("2013-11-30 23:59:59");
            dashboardDataList.add(data4);

            DashboardDataModel data5 = new DashboardDataModel();
            data5.setAccountBalance(200d);
            data5.setDeposits(0);
            data5.setWithdrawals(-100);
            data5.setLastUpdate("2013-10-31 23:59:59");
            dashboardDataList.add(data5);

            this.dashBoardModel.setDashboardDataList(dashboardDataList);

            initValues();
        }
        drawChartTitleBar();
    }

	public void initValues() {
		List<DashboardDataModel> dashboardList = dashBoardModel
				.getDashboardDataList();

		List<Double> yValue = new ArrayList<Double>();
		List<String> xValue = new ArrayList<String>();
		List<Double> listWithDrawals = new ArrayList<Double>();
		List<Double> listDeposits = new ArrayList<Double>();
		List<Double> listAccountBalance = new ArrayList<Double>();

		for (int i = dashboardList.size() - 1; i >= 0; i--) {
			DashboardDataModel dashboardDataModel = dashboardList.get(i);
			double money = Utils.changeMoney(dashboardDataModel
					.getWithdrawals() + dashboardDataModel.getDeposits());
			yValue.add(money);
			String time = TimeUtil.changeChartFormattrString(context,
					dashboardDataModel.getLastUpdate(), TimeUtil.dateFormat2,
					TimeUtil.detaFormat6);
			xValue.add(time);
			
			double moneyWithDrawals = Utils.changeMoney(dashboardDataModel.getWithdrawals());
			listWithDrawals.add(moneyWithDrawals);
			
			double moneyDeposits = Utils.changeMoney(dashboardDataModel.getDeposits());
			listDeposits.add(moneyDeposits);
			
			double monneyAccountBalance = Utils.changeMoney(dashboardDataModel.getAccountBalance());
			listAccountBalance.add(monneyAccountBalance);
		}
		this.xYValues = yValue;
		this.xValue = xValue;
		this.listWithDrawals = listWithDrawals;
		this.listDeposits = listDeposits;
		this.listAccountBalance = listAccountBalance;
		
		bHaveMovements = haveMovements();
		initYValue();
	};

    private void initYValue() {
    	final int NO_SIGN_SIZE = 4;
        final int SIGN_SIZE = 4;

        double min = Integer.MAX_VALUE;
        double max = -Integer.MAX_VALUE;
        for (int i = 0; i < xYValues.size(); i++) {
            min = Math.min(xYValues.get(i), min);
            max = Math.max(xYValues.get(i), max);
        }

        yValue = new ArrayList<Double>();
        if (min >= 0) {

            double cell = getCell(max, NO_SIGN_SIZE);

            for (int i = 0; i <= NO_SIGN_SIZE; i++) {
                yValue.add(i * cell);
            }

        } else if (min < 0 && max > 0) {

            double mm = Math.max(Math.abs(min), Math.abs(max));
            double cell = getCell(mm, SIGN_SIZE / 2);
            for (int i = -SIGN_SIZE / 2; i <= SIGN_SIZE / 2; i++) {

                yValue.add(i * cell);
            }

        } else if (max <= 0) {

            double mm = Math.abs(min);
            double cell = getCell(mm, NO_SIGN_SIZE);

            for (int i = -NO_SIGN_SIZE; i <= 0; i++) {
                yValue.add(i * cell);
            }

        }

    }

	private boolean haveMovements() {
		boolean ret = false;
		if (this.listWithDrawals == null || this.listWithDrawals.size() == 0)
			return false;

		if (this.listDeposits == null || this.listDeposits.size() == 0)
			return false;

		for (int i = 0; i < this.listWithDrawals.size(); i++)
			if (this.listWithDrawals.get(i) != 0)
				return true;

		for (int i = 0; i < this.listDeposits.size(); i++)
			if (this.listDeposits.get(i) != 0)
				return true;
		return ret;
	}

    private double getCell(double max, int size) {
        max = Math.ceil(max);

        String str = String.valueOf((int)max);
        int aa = Integer.valueOf(str);
        int bb = (int)(aa / Math.pow(10, str.length() - 1)) + 1;
        
        /*
         * If no movements, assign the largest top number 
         */
        if (!bHaveMovements)
        	bb = NO_MOVEMENTS_TOP_VALUE;
        
        int result = (int)(bb * Math.pow(10, str.length() - 1));

        double cell = result / (double)size;
        return cell;
    }

    @Override
    protected void initPosition() {
        super.initPosition();
        // 算出Y轴坐标值需要的最大值
        int height = getMeasuredHeight();

        screen_height = getMeasuredHeight();
        screen_width = getMeasuredWidth();

        mPaint.setTextSize(yPaintScale);
        int maxLength = 0;
        for (double value : yValue) {
            String str = Utils.generateFormatMoneyInt(Contants.COUNTRY, value);
            maxLength = (int)Math.max(mPaint.measureText(str), maxLength);
        }
        yAxisXPosition = maxLength + padding;
        yAxisStartYPosition = chartTitleY + yPaintScale;
        yAxisEndYPosition = getMeasuredHeight() - padding - 2 * yPaintScale;
        xAxisEndXPosition = getMeasuredWidth() - padding;
        xAxisStartXPosition = yAxisXPosition;

        // 每等分的长度
        yCellLength = (int)((float)(yAxisEndYPosition - yAxisStartYPosition)
                / ((float)yValue.size()) - 1);

        // y轴每
        yCell = (float)((yValue.get(1) - yValue.get(0)) / yCellLength);
        
        xCellCount=xValue.size()-1;

        xCellLength = (int)((float)(xAxisEndXPosition - xAxisStartXPosition) / (float)(xCellCount+0.5));

        // 初始化拆线的点的坐标
        double sum = 0;
        xys = new ArrayList<LineFormView.XY>();
        for (int i = 0; i < xYValues.size(); i++) {
        	sum += xYValues.get(i);
            //int x = yAxisXPosition + (i + 1) * xCellLength;
        	int x = yAxisXPosition + i * xCellLength;
            // Y轴长度
            float y = yAxisEndYPosition - (int)((xYValues.get(i) - yValue.get(0)) / yCell);
            XY xy = new XY();
            xy.x = x;
            xy.y = y;
            xys.add(xy);
        }
        
        /*
        * average value
		*
		* the requested is that the mean value calculated by the app is truncated.
		* example:
		* 12,4567 ---> 12,45
		* 12,309    --> 12,30
		* 12,999999 --> 12,99
		*/
        if (xYValues.size() == 0 ) {
        	mAverageValue = 0;
        } else if (sum == 0 ) {
        	mAverageValue = 0;
        } else {
			mAverageValue = sum / xYValues.size();
			mAverageValue = mAverageValue - 0.005;
		}

        mAverageY =  yAxisEndYPosition - (int)((mAverageValue - yValue.get(0)) / yCell);

        mZeroY = yAxisEndYPosition - (int)((0 - yValue.get(0)) / yCell);

        xyPaintScale = (int)(x_y_paint_scale * height);
        XOYR = (int)(x_o_y_r * height);
        xOYRTouch = (int)(x_o_y_r_touch * height);

        detailTouchY = (int)(detail_touch_y * height);
        slidBarR = (int)(slid_bar_r * height);
    }

	Path mpath = new Path();
	PathEffect dash_effect = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setTypeface(mFontFace);
        
		drawBackgroundColor(canvas);
		drawAxis(canvas);
        
        drawZeroLine(canvas);
        
        if (bHaveMovements) {
        	drawAverageLine(canvas);
        }

        drawVerticalLines(canvas);
        drawTextMonths(canvas);
        drawYAxis(canvas);
        
        drawConnectedLines(canvas);

        drawTitleText(canvas, textPaint, getResources().getString(R.string.chart_title_accounts));
        
        if (!bHaveMovements) {
        	drawTitleTextNoMovements(canvas);
        	return;
        }

        mPaint.setColor(xyColor);
        mPaint.setStyle(Paint.Style.FILL);

        // 画实心圆
        for (int i = 0; i < xys.size(); i++) {

            XY xy = xys.get(i);
            canvas.drawCircle(xy.x, xy.y, XOYR, mPaint);
        }

        // 画点击时获取焦点的大圆

        if (touchPoint > -1) {
            mPaint.setColor(xyTouchColor);
            XY xy = xys.get(touchPoint);

			/*
			 * Draw new circle design
			 */
            drawSlidbar(canvas, xy.x, xy.y, 0);
        }

        // 画slidbar
        if (isTwoPointTouch) {
            drawSlidbar(canvas, x0, y0, 0);
            drawSlidbar(canvas, x1, y1, 1);
        }
        drawAverageText(canvas);
    }

	private void drawZeroLine(Canvas canvas) {
		// Draw zero line
		mpath.reset();
		mpath.moveTo(yAxisXPosition, mZeroY);
		mpath.lineTo(yAxisXPosition + xCellCount * xCellLength, mZeroY);

		mPaint.setColor(Color.GRAY);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAntiAlias(true);

		canvas.drawPath(mpath, mPaint);
	}

    private void drawAverageLine(Canvas canvas) {
        mPaint.setPathEffect(null);

        /*
         * Draw average dot line
         */
        mpath.reset();
        mpath.moveTo(yAxisXPosition, mAverageY);
        mpath.lineTo(yAxisXPosition + xCellCount * xCellLength, mAverageY);

        mPaint.setColor(xyColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setPathEffect(dash_effect);
        mPaint.setStrokeWidth(1);
        canvas.drawPath(mpath, mPaint);
        mPaint.setPathEffect(null);
    }

    private void drawYAxis(Canvas canvas) {
        // 画纵坐标的数值
        textPaint.setTextSize(xPaintScale);
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
        
        //draw One More
        int y = yAxisEndYPosition -  yValue.size() * yCellLength;
        int x = yAxisXPosition - 2 * yAxisScale;
        int size = yValue.size();
        double value = yValue.get(size - 1) + yValue.get(size-1) - yValue.get(size-2);
        String text = Utils.generateFormatMoney(Contants.COUNTRY, value);
        canvas.drawText(text, x, y, textPaint);
    }

	private void drawBackgroundColor(Canvas canvas) {
        canvas.drawColor(bgColor2);

        mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);

		int color = bgColor2;

		// Draw rect background
		for (int i = 0; i <=xCellCount; i++) {
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
//		mPaint.setAntiAlias(true);
//		mPaint.setColor(xAxisColor);
//		mPaint.setStrokeWidth(yAxisScale);
//		// 画Y坐标轴
//		canvas.drawLine(yAxisXPosition, 0, yAxisXPosition, yAxisEndYPosition,
//				mPaint);
//		// 画X坐标轴
//		canvas.drawLine(yAxisXPosition, yAxisEndYPosition, yAxisXPosition
//				+  xAxisEndXPosition - xAxisStartXPosition/*xCellCount * xCellLength*/, yAxisEndYPosition, mPaint);
        
        
        /*
         * Draw average dot line
         */
        mpath.reset();
        mpath.moveTo(yAxisXPosition, 0);
        mpath.lineTo( yAxisXPosition, yAxisEndYPosition);
        
        mpath.moveTo(yAxisXPosition, yAxisEndYPosition);
        mpath.lineTo(yAxisXPosition
				+  getMeasuredWidth() - xAxisStartXPosition/*xCellCount * xCellLength*/, yAxisEndYPosition);

        mPaint.setColor(xAxisColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setPathEffect(dash_effect);
		mPaint.setStrokeWidth(yAxisScale);
        canvas.drawPath(mpath, mPaint);
        mPaint.setPathEffect(null);
	}

	private void drawVerticalLines(Canvas canvas) {
		// 画竖线
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.GRAY);
		mPaint.setStrokeWidth(yAxisScale / 2);
		for (int i = 1; i <= xCellCount; i++) {
			float xx = yAxisXPosition + i * xCellLength;
			canvas.drawLine(xx, 0, xx, yAxisEndYPosition, mPaint);
		}
	}

	private void drawTitleTextNoMovements(Canvas canvas) {
        textPaint.setTextSize(titleTextSize);
        textPaint.setTypeface(mFontFace);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(xyColor);

        String strNoMovements = getResources().getString(R.string.no_movements_in_period);
        canvas.drawText(strNoMovements, getMeasuredWidth()/2 - textPaint.measureText(strNoMovements)/2 , getMeasuredHeight()/2, textPaint);
	}

	private void drawAverageText(Canvas canvas) {
		textPaint.setTextSize(xPaintScale);
		
		/*
		 * Draw average value.
		 * Deal with the average value overlap the line.
		 */
		int y = mAverageY - (int) yPaintScale / 2;
		
		if (mAverageValue < 0 && Math.abs(mAverageY - mZeroY) < yPaintScale *3 ) {
			y = mAverageY + (int)(yPaintScale * 2.5);
		}

		int x = yAxisXPosition + 4 * yAxisScale;
		textPaint.setTextAlign(Paint.Align.LEFT);

		textPaint.setColor(Color.GRAY);
		String average = context.getResources().getString(
				R.string.card_rotate_average_str);
		
		/*
		 * Draw average text.
		 * Deal with the average value overlap the line.
		 */
		int yAverageText = y - (int) (yPaintScale * 1.3f);
		canvas.drawText(average, x, yAverageText, textPaint);

		/*
		 * Average value
		 */
		String averageText = Utils.generateFormatMoney(context.getResources()
				.getString(R.string.eur), mAverageValue);
		canvas.drawText(averageText, x, y, textPaint);
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
 
            canvas.drawTextOnPath(str1, mpath, xCellLength/10 , 0, textPaint);
            canvas.drawTextOnPath(str2, mpath, xCellLength/10 , 1.5f * yPaintScale, textPaint);
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
            	mpath.lineTo(xy.x, yAxisEndYPosition);  // right bottom corner
            	mpath.lineTo(yAxisXPosition, yAxisEndYPosition); // left bottom corner
            }
        }

        mpath.close();
		if (isIbanCard) {
	        mPaint.setColor(0x4dd21d7a);
		} else {
	        mPaint.setColor(0x4d3798b7);
		}
			 
        /*
         * Add 30% transparency of chart
         */
        mPaint.setAlpha((int)(255 * 0.3));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(xyPaintScale);

        canvas.save();
        canvas.drawPath(mpath, mPaint);
        canvas.restore();
        mpath.reset();

	}

    private void drawSlidbar(Canvas canvas, float x, float y, int index) {
        XY xy = xys.get(xys.size() -1);
        if (x > xy.x + slidBarR)
        	return;
        
        mPaint.setColor(Color.rgb(150, 150, 150));
        canvas.drawCircle(x, y, slidBarR, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, slidBarR - 1, mPaint);
        mPaint.setColor(xyDetailColor);
        canvas.drawCircle(x, y, slidBarR * 0.8f, mPaint);
    }

    /**
     * 双点触摸开关
     */
    boolean isTwoPointTouch = false;

    /**
     * 是否显示双点
     */
    boolean showTwoPoint = false;

    /**
     * 是否显示双点详情
     */
    boolean showTwoPointDetail = false;


    /**
     * x0在x1在右边x0大于x1
     */
    float x0 = -100, x1 = -100, y0 = -100, y1 = -100;

    boolean touchSlid0;

    boolean touchSlid1;

    boolean up0 = false;

    int f = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (!bHaveMovements)
    		return true;
 
        int action = event.getAction();
        int pointerCount = event.getPointerCount();
        float x = event.getX();
        float y = event.getY();
        int touch = touchThePoint(x, y);

        LogManager.d("touch" + action + "count" + pointerCount);
        float minX = xys.get(0).x;
        float maxX = xys.get(xys.size() - 1).x;

        if (action != MotionEvent.ACTION_UP && (x > maxX + slidBarR || x < minX - slidBarR)) {
            return true;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (up0) {
                    up0 = false;
 
                    LogManager.d("bug fix");
                    return true;
                }
                // 如果已经显示twoPointDetail
                if (showTwoPointDetail) {
                    touchSlid0 = isTouchSlid0Bar(x, y);
                    touchSlid1 = isTouchSlid1Bar(x, y);

                    if (pointerCount == 1) {
                        if (!moveSlidBarEnable && touch == from || touch == to) {
                            return true;
                        }
                        if (touchSlid0) {
                            x0 = x;
                            y0 = getSlidBarY(x0);
                        } else if (touchSlid1) {
                            x1 = x;
                            y1 = getSlidBarY(x1);
                        }
                    }

                    if (touchSlid0 || touchSlid1) {

                        show2Point();
                        hideDetail();
                        hideTouchPoint();
                    } else if (touch > -1) {
                        showTwoPointDetail = false;
                        downTouchThePoint = true;
                        isTwoPointTouch = false;
                        hide2Point();

                        showTouchPoint(touch, x, y);
                    }
                

                } else {

                    // 两点没显示
                    touchSlid0 = isTouchSlid0Bar(x, y);
                    touchSlid1 = isTouchSlid1Bar(x, y);

                    if (pointerCount == 1) {
                        if (touchSlid0) {
                            x0 = x;
                            y0 = getSlidBarY(x0);
                        } else if (touchSlid1) {
                            x1 = x;
                            y1 = getSlidBarY(x1);
                        }
                    }

                    if (touch > -1) {
                        showTwoPointDetail = false;
                        downTouchThePoint = true;
                        isTwoPointTouch = false;
                        hide2Point();

                        showTouchPoint(touch, x, y);

                    } else if (touchSlid0 || touchSlid1) {

                        show2Point();
                        hideDetail();
                        hideTouchPoint();
                    } else {
                        if (touch > -1) {
                            showTwoPointDetail = false;
                            downTouchThePoint = true;
                            isTwoPointTouch = false;
                            hide2Point();

                            showTouchPoint(touch, x, y);

                        }
                    }

                }

                break;

            case MotionEvent.ACTION_POINTER_2_DOWN: {
                if (moveSlidBarEnable) {
                    hideTouchPoint();
                    isTwoPointTouch = true;
                    showTwoPoint = true;
                    hideDetail();
                    float xx0 = event.getX(0);
                    float xx1 = event.getX(1);
                    x1 = Math.min(xx0, xx1);
                    y1 = getSlidBarY(x1);
                    x0 = Math.max(xx0, xx1);
                    y0 = getSlidBarY(x0);

                    break;
                } else {
                    break;
                }

            }
            case MotionEvent.ACTION_MOVE: {

                if (!moveSlidBarEnable) {
                    return true;
                }
                if (x > maxX || x < minX) {
                    return true;
                }
                if (isTwoPointTouch) {
                    showTwoPointDetail = false;
                    if (pointerCount == 1) {
                        if (touchSlid0) {
                            if (x < xys.get(0).x + slidBarR) {
                                return true;
                            }
                            x0 = x;
                            y0 = getSlidBarY(x0);
                            if (x0 - slidBarR < x1) {
                                x1 = x0 - slidBarR;
                                y1 = getSlidBarY(x1);
                            }
                        } else if (touchSlid1) {
                            if (x > xys.get(xys.size() - 1).x - slidBarR) {
                                return true;
                            }
                            x1 = x;
                            y1 = getSlidBarY(x1);
                            if (x1 + slidBarR > x0) {
                                x0 = x1 + slidBarR;
                                y0 = getSlidBarY(x0);
                            }
                        }

                    } else if (pointerCount == 2) {
                        float xx0 = event.getX(0);

                        float xx1 = event.getX(1);
                        x1 = Math.min(xx0, xx1);
                        x0 = Math.max(xx0, xx1);
                        if (x1 < minX) {
                            x1 = minX;
                        }
                        if (x1 > maxX) {
                            x1 = maxX;
                        }
                        if (x0 < minX) {
                            x0 = minX;
                        }
                        if (x0 > maxX) {
                            x0 = maxX;
                        }
                        y1 = getSlidBarY(x1);

                        y0 = getSlidBarY(x0);
                    }
                    hideDetail();
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {

                if (touch > -1 && downTouchThePoint) {

//                    showDeatil(touch, x, y);
                } else {
                    hideTouchPoint();
                    hideDetail();
                }
                downTouchThePoint = false;
                if (isTwoPointTouch) {
                    showTwoPointDetail = true;
                    if (touchSlid0) {
                        int to = adsorptionPoint0();

                        int from = adsorptionPoint1();
                        performSlid(from, to);
                    } else if (touchSlid1) {

                        int from = adsorptionPoint1();
                        int to = adsorptionPoint0();
                        performSlid(from, to);
                    } else {
                        // 双指触摸先放1后放0时
                        int to = adsorptionPoint0();
                        int from = adsorptionPoint1();
                        performSlid(from, to);
                    }
                    touchSlid0 = false;
                    touchSlid1 = false;

                    invalidate();
                }

                break;
            }
            case MotionEvent.ACTION_POINTER_1_UP: {
                // 先放0时
                up0 = true;
                int from = adsorptionPoint1();
                int to = adsorptionPoint0();
                performSlid(from, to);

                invalidate();

                break;
            }
            case MotionEvent.ACTION_POINTER_2_UP: {
                adsorptionPoint1();
                invalidate();
                break;
            }

            default:
                break;
        }

        return true;
    }

    int from = -1;

    int to = -1;

    private int adsorptionPoint1() {
        int result = -1;
        float deta = Float.MAX_VALUE;
        int minIndex1 = -1;
        for (int i = 0; i < xys.size(); i++) {

            XY xy = xys.get(i);
            float deta1 = Math.abs(xy.x - x1);

            if (deta1 < deta) {
                deta = deta1;
                minIndex1 = i;
            }
        }

        float x = xys.get(minIndex1).x;
        
        /*
         * add minIndex1!=0 to avoid array out of bound. 
         */
        if (x0 <= x && minIndex1 != 0) {
            x1 = xys.get(minIndex1 - 1).x;
            y1 = getSlidBarY(x1);
            result = minIndex1 - 1;
        } else {
            x1 = xys.get(minIndex1).x;
            y1 = getSlidBarY(x1);
            result = minIndex1;
        }
        return result;

    }

    private int adsorptionPoint0() {
        int result = -1;
        float deta = Float.MAX_VALUE;
        int minIndex1 = -1;
        for (int i = 0; i < xys.size(); i++) {

            XY xy = xys.get(i);
            float deta1 = Math.abs(xy.x - x0);

            if (deta1 < deta) {
                deta = deta1;
                minIndex1 = i;
            }
        }

        float x = xys.get(minIndex1).x;

        /*
         * Add (minIndex1 +1) < xys.size() to avoid array out of bound.
         */
        if (x1 >= x && (minIndex1 + 1) < xys.size()  ) {
            x0 = xys.get(minIndex1 + 1).x;
            y0 = getSlidBarY(x0);
            result = minIndex1 + 1;
        } else {
            x0 = xys.get(minIndex1).x;
            y0 = getSlidBarY(x0);
            result = minIndex1;
        }

        return result;
    }

    private void show2Point() {

        isTwoPointTouch = true;

		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();
        if (popupwindow != null)
        	popupwindow.dismiss();
        
		View root = View.inflate(chartActivity, R.layout.chart_popup, null);

		popupwindow = new PopupWindow(root, getWidthPopup(), getHeightPopupTwoPoint());
		popupwindow.showAtLocation(root, Gravity.NO_GRAVITY, (int)(x0+ x1)/2 - getWidthPopup()/2,
				(int)Math.min(y0, y1) - getHeightPopupTwoPoint());
		drawChartPopupWindowTwoPoint(root);

		chartActivity.setPopupWindow(popupwindow);
    }

    private void hide2Point() {
        from = -1;
        to = -1;
        isTwoPointTouch = false;
        if (popupwindow != null)
        	popupwindow.dismiss();
    }

    /**
     * 隐藏被点击的节点
     */
    private void hideTouchPoint() {
        touchPoint = -1;
        if (popupwindow != null)
        	popupwindow.dismiss();
        invalidate();
    }

    int touchPoint = -1;

    /**
     * 显示被点击的节点
     * 
     * @param touch
     */
    private void showTouchPoint(int touch , float x, float y) {

        touchPoint = touch;
        showDeatil(touch, x, y);
        invalidate();
    }

    boolean showDetail = false;

    /**
     * 显示详情
     */
    private void showDeatil(int touch, float x, float y) {
		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();

        showDetail = true;
        
        if (popupwindow != null)
        	popupwindow.dismiss();
        
        if (touch <0 || touch>=xys.size())
        	return;
        XY xy = xys.get(touch);
        
		View root = View.inflate(chartActivity, R.layout.chart_popup, null);

		popupwindow = new PopupWindow(root, getWidthPopup(), getHeightPopup());
		if (ViewConfiguration.get(context).hasPermanentMenuKey() || touch != xys.size()-1) {
			popupwindow.showAtLocation(root, Gravity.NO_GRAVITY, (int)xy.x - getWidthPopup()/2,
					(int)xy.y - getHeightPopup());
		} else{
			popupwindow.showAtLocation(root, Gravity.NO_GRAVITY, (int)xy.x - getWidthPopup()/2 -300,
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

    /**
     * @param srcx
     * @param srcy
     * @param x 手指位置
     * @param y
     * @return
     */
    private boolean isTouchSlidBar(float srcx, float srcy, float x, float y) {

        float beginX = srcx - 1.2f * xOYRTouch;
        float endX = srcx + 1.2f * xOYRTouch;
        float beginY = srcy - 1.2f * xOYRTouch;
        float endY = srcy + 1.2f * xOYRTouch;

        if (x > beginX && x < endX && y > beginY && y < endY) {

            return true;

        }
        return false;

    }

    private boolean isTouchSlid1Bar(float x, float y) {

        return isTouchSlidBar(x1, y1, x, y);

    }

    private boolean isTouchSlid0Bar(float x, float y) {
        return isTouchSlidBar(x0, y0, x, y);
    }

    private float getSlidBarY(float x) {

        int index = -1;
        float result = -1;
        for (int i = 0; i < xys.size(); i++) {
            XY xy = xys.get(i);
            float detaX = xy.x - x;
            
            /*
             * Add logic so that would found if detaX is in some range. 
             */
            if (detaX >= 0 || Math.abs(detaX) <= slidBarR) {
                index = i;
                break;
            }

        }

        /*
         * If not find, return the most right point value.
         */
        if (index == -1) {
            return xys.get(xys.size() - 1).y;
        }
        
        /*
         * If index is zero, return avoid array out of bound.
         */
        if (index == 0) {
            return xys.get(0).y;
        }

        XY xy0 = xys.get(index - 1);
        XY xy1 = xys.get(index);

        float k = (xy1.y - xy0.y) / (xy1.x - xy0.x);

        result = xy1.y - k * (xy1.x - x);

        return result;
    }

    public static class XY {
        float x;
        float y;
    }

    private void performSlid(int from, int to) {

        this.from = from;
        this.to = to;
        LogManager.d("from=" + from + "to=" + to);

        double fromXY = xYValues.get(from);

        double toXY = xYValues.get(to);

        double deta = toXY - fromXY;

        double min = Integer.MAX_VALUE;
        double max = -Integer.MAX_VALUE;
        for (int i = 0; i < xYValues.size(); i++) {
            min = Math.min(xYValues.get(i), min);
            max = Math.max(xYValues.get(i), max);
        }

        /*
         * Create pop up window text, money
         */
        StringBuffer sb = new StringBuffer();
        sb.append(Utils.generateFormatMoney(Contants.COUNTRY, deta));
        twoPointDetailText1 = sb.toString();
        
        /*
         * Create pop up window text, date
         */
        fromMonth = xValue.get(from);
        toMonth = xValue.get(to);
        fromMonth = fromMonth.replace(".", " ");
        toMonth = toMonth.replace(".", " ");
        twoPointDetailText2 = "-";
        
        if (mOnSlidListener != null) {
            mOnSlidListener.onSlidTwoBar(this, from, to);
        }
        
        show2Point();
    }

    OnSlidListener mOnSlidListener;

    public void setOnSlidListener(OnSlidListener onSlidListener) {
        this.mOnSlidListener = onSlidListener;

    }

    public static interface OnSlidListener {
        void onSlidTwoBar(View v, int from, int to);
    }

    public static interface onPointClickListener {

        void onClick();
    }

    private void setRoundRectBackGround(View root) {
		LinearLayout layout = (LinearLayout)root.findViewById(R.id.chart_popup_window);
		Drawable shape;
		if (!isIbanCard)
			shape = context.getResources().getDrawable(R.drawable.shape_roundrect_account);
		else 
			shape = context.getResources().getDrawable(R.drawable.shape_roundrect_card);
		layout.setBackgroundDrawable(shape);
    }
    
	public void drawChartPopupWindowTwoPoint(View root) {
		setRoundRectBackGround(root);
		setPopupWindowFont(root);

		if (isIbanCard) {
			View seperator = (LinearLayout) root.findViewById(R.id.chart_popup_seperator_iban);
			seperator.setVisibility(View.VISIBLE);
		}
		else {
			View seperator = (LinearLayout) root.findViewById(R.id.chart_popup_seperator);
			seperator.setVisibility(View.VISIBLE);
		}

		/*
		 * Title
		 */
		TextView tv = (TextView) root.findViewById(R.id.chart_popup_title);
		tv.setText(twoPointDetailText2);

		TextView tvLeft = (TextView)root.findViewById(R.id.chart_popup_title_left);
		tvLeft.setText(fromMonth);
		tvLeft.setVisibility(View.VISIBLE);

		TextView tvRight = (TextView)root.findViewById(R.id.chart_popup_title_right);
		tvRight.setText(toMonth);
		tvRight.setVisibility(View.VISIBLE);

		/*
		 * Difference
		 */
		FillText(root, R.id.row1_title, R.string.difference, R.id.row1_value,
				twoPointDetailText1);
		
		root.findViewById(R.id.tableRow2).setVisibility(View.GONE);
		root.findViewById(R.id.tableRow3).setVisibility(View.GONE);
	}

	public void drawChartPopupWindow(View root, int index) {
		setRoundRectBackGround(root);
		setPopupWindowFont(root);

		if (isIbanCard) {
			View seperator = (LinearLayout) root.findViewById(R.id.chart_popup_seperator_iban);
			seperator.setVisibility(View.VISIBLE);
		}
		else {
			View seperator = (LinearLayout) root.findViewById(R.id.chart_popup_seperator);
			seperator.setVisibility(View.VISIBLE);
		}
		
		TextView tv = (TextView) root.findViewById(R.id.chart_popup_title);
		String month = this.xValue.get(index);
		month = month.replace('.', ' ');
		tv.setText(month);

		/*
		 * Deposits
		 */
		String strDeposits = Utils.generateFormatMoney(context
				.getResources().getString(R.string.eur), this.listDeposits.get(index));
		FillText(root, R.id.row1_title,
				R.string.deposits, R.id.row1_value,strDeposits);
		
		/*
		 * Withdrawl
		 */
		String strWithdrawl = Utils.generateFormatMoney(context
				.getResources().getString(R.string.eur), this.listWithDrawals.get(index));
		FillText(root, R.id.row2_title, R.string.withdrawals, R.id.row2_value, strWithdrawl);
		
		/*
		 * Diff
		 */
		double diff = this.listDeposits.get(index) + this.listWithDrawals.get(index);
		String strDiffernce = Utils.generateFormatMoney(context
				.getResources().getString(R.string.eur), diff);
		FillText(root, R.id.row3_title, R.string.difference, R.id.row3_value, strDiffernce);
		
		root.findViewById(R.id.tableRow2).setVisibility(View.VISIBLE);
		root.findViewById(R.id.tableRow3).setVisibility(View.VISIBLE);
	}

	/*
	 * Draw dashboard on chart header
	 */
	public void drawChartTitleBar() {
		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();

		setTitleBarFont(chartActivity.chartsWindow);
		/*
		 * Update personalized name
		 */
		String accountName = dashBoardModel.getPersonalizedName();
		FillText(chartActivity.chartsWindow, R.id.accountType,
				R.string.account2, R.id.accountName, accountName);

		/*
		 * show card Type (in this case Conto)
		 */
		TextView tvAccountType = (TextView)chartActivity.chartsWindow.findViewById(R.id.accountType);
		tvAccountType.setText(context.getResources().getString(R.string.account2));

		/*
		 * Update Available Balance
		 */
		String availableBalance = Utils.generateFormatMoney(context
				.getResources().getString(R.string.eur), dashBoardModel
				.getAvailableBalance());
		FillText(chartActivity.chartsWindow, R.id.mid_title,
				R.string.available_balance, R.id.mid_value, availableBalance);

		/*
		 * Update Account Balance
		 */
		String accountBalance = Utils.generateFormatMoney(context
				.getResources().getString(R.string.eur), dashBoardModel
				.getDashboardDataList().get(0).getAccountBalance());
		String accountBalanceTitle = getResources().getString(
				R.string.balance_account);
		accountBalanceTitle = Utils.toUppString(accountBalanceTitle);

		FillText(chartActivity.chartsWindow, R.id.row1_title,
				accountBalanceTitle, R.id.row1_value, accountBalance);

		/*
		 * Locked value dipiuBalance 
		 */
		String dipiuBalance =  Utils.generateFormatMoney(context
				.getResources().getString(R.string.eur), this.boundBalance);
		String dipiuBalanceTitle = getResources().getString(
				R.string.dipiu_account);
		
		dipiuBalanceTitle = Utils.toUppString(dipiuBalanceTitle);
		FillText(chartActivity.chartsWindow, R.id.row2_title,
				dipiuBalanceTitle, R.id.row2_value, dipiuBalance);

		if (this.boundBalance <= 0) {
			chartActivity.chartsWindow.findViewById(R.id.tableRow2).setVisibility(View.GONE);
		}
		else {
			chartActivity.chartsWindow.findViewById(R.id.tableRow2).setVisibility(View.VISIBLE);
		}
		
		/*
		 * Update Last Updated
		 */
		String nowTime = TimeUtil.getDateString(System.currentTimeMillis(),
				TimeUtil.dateFormat5);
		FillText(chartActivity.chartsWindow, R.id.row3_title,
				R.string.updated_date, R.id.row3_value, nowTime);

		/*
		 * Assign text colors
		 */
		tvAccountType.setTextColor(xyColor);

		TextView tvAccountName = (TextView) chartActivity.chartsWindow
				.findViewById(R.id.accountName);
		tvAccountName.setTextColor(xyColor);

		TextView tvMidTitle = (TextView) chartActivity.chartsWindow
				.findViewById(R.id.mid_title);
		tvMidTitle.setTextColor(xyColor);
		TextView tvMidValue = (TextView) chartActivity.chartsWindow
				.findViewById(R.id.mid_value);
		tvMidValue.setTextColor(xyColor);


		if (isIbanCard) {
			drawChartTitleBarIBAN();
	        /*
	         * Icon 
	         */
	        ImageView iv = (ImageView) chartActivity.chartsWindow.findViewById(R.id.chart_icon);
	        iv.setImageResource(R.drawable.top_icons_cards);
	        
			ImageView isPreferredStar =(ImageView) chartActivity.chartsWindow.findViewById(R.id.isPreferredStar);
			isPreferredStar.setImageResource(R.drawable.icona_carta_stella);
		}
		else
		{
	        /*
	         * Icon 
	         */
	        ImageView iv = (ImageView) chartActivity.chartsWindow.findViewById(R.id.chart_icon);
	        iv.setImageResource(R.drawable.top_icons_account);
	        
			ImageView isPreferredStar =(ImageView) chartActivity.chartsWindow.findViewById(R.id.isPreferredStar);
			isPreferredStar.setImageResource(R.drawable.icona_conti_stella);
		}
		
		if (BaseActivity.isOffline) {
			drawChartTitleBarIBAN();
		}
	}
	
	private void drawChartTitleBarIBAN() {
		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();

		/*
		 * show card Type (in this case Carta IBAN)
		 */
		TextView tvAccountType = (TextView)chartActivity.chartsWindow.findViewById(R.id.accountType);
		tvAccountType.setText(context.getResources().getString(R.string.carta_iban));

		/*
		 * IBAN Number
		 */
		String cardNumber = "";
		for (AccountsModel accountsModel : Contants.cardAccounts) {

			if (this.dashBoardModel.getAccountCode().equals(
					accountsModel.getAccountCode())) {
				cardNumber = accountsModel.getIbanCode();
			}
		}

		if (BaseActivity.isOffline)
			cardNumber = "IT09P0538712905000000589608";

		if (cardNumber.length() >= 12)
			cardNumber = "..." + cardNumber.substring(cardNumber.length() - 12);

		FillText(chartActivity.chartsWindow, R.id.row2_title,
				R.string.iban_card, R.id.row2_value, cardNumber);
		
		TextView tvCardNumber = (TextView)chartActivity.chartsWindow.findViewById(R.id.row2_value);
		tvCardNumber.setPadding(0, 0, 0, 0);

		/*
		 * Possible the 2nd row is set gone because of diupiu balance handle
		 * Show it at this case
		 */
		chartActivity.chartsWindow.findViewById(R.id.tableRow2).setVisibility(View.VISIBLE);
	}

}
