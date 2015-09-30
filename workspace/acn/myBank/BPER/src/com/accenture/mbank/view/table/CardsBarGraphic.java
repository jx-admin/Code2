
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
import android.graphics.Point;
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

/*
 * Chart Card
 */
@SuppressLint("NewApi")
public class CardsBarGraphic extends FormView {

    private Paint mPaint = new Paint();

    private static final int INVALID_BAR_INDEX = -1;

    private boolean isPrepaidCard = false;

    /**
     * data model
     */
    private List<DashboardDataModel> dataList;

    private DashBoardModel dashBoardModel;
    /**
     * bar list
     */
    private List<Bar> barList = new ArrayList<Bar>();

	boolean bHaveMovements = true;
	
	/*
	 * Defines the Y range when there are no movements
	 */
	int NO_MOVEMENTS_TOP_VALUE = 10;

    /**
     * minimum withdrawal
     */
    private double minimumWithdrawal = 0;

    /**
     * the average withdrawals
     */
    private double averageWithdrawal = 0;
    private int mAverageY = 0;
    /**
     * the maxmimum withdrawal
     */
    private double maximumWithdrawal = 0;

    /**
     * dynamically measured in onMeasure
     */
    private int screen_width;

    /**
     * dynamically measured in onMeasure
     */
    private int screen_height;

    private int touchedBarIndex = INVALID_BAR_INDEX;

    private List<String> yValues = new ArrayList<String>();

    float detail_text_1_size = 40f / 577;

    float detail_text_2_size = 30f / 577;


    int detailText1Size = 0;

    int detailText2Size = 0;

    float detail_round_r = 10f / 577;

    int detailRoundR = 0;

    /**
     * X轴上每等分的距离
     */
    int xCellLength = 0;

    /**
     * Y轴上每等分的距离
     */
    int yCellLength = 0;

    Path mpath = new Path();

    Paint textPaint = new Paint();

    private final String eur = getContext().getString(R.string.eur);

    private static final int NO_SIGN_SIZE = 5;

    private static final int SIGN_SIZE = 8;

    double m_min;
    double m_max;
    
    private int HALF_TRANSPARENT_COLOR_CARD;
    private int COLOR_CARD;
    private int COLOR_CARD_CIRCLE;

    float x_o_y_r = 9f / 577;
    float slid_bar_r = 26f / 577;
	/*
	 * For warning reduce purpose, without this there will be a warning at top
	 * of the file
	 */
	private CardsBarGraphic(Context context) {
		super(context);
	}

    public CardsBarGraphic(Context context, DashBoardModel model) {
        super(context);
        
        this.dashBoardModel = model;
        
        if (BaseActivity.isOffline) {
            
            this.dashBoardModel = new DashBoardModel();
            this.dashBoardModel.setPersonalizedName("Test personized personized name");
            this.dashBoardModel.setAccountCode("1078281");
            this.dashBoardModel.setAvailableBalance(5000d);
            
            List<DashboardDataModel> dashboardDataList = new ArrayList<DashboardDataModel>();
            DashboardDataModel data1 = new DashboardDataModel();
            data1.setWithdrawals(3);
            data1.setLastUpdate("2014-02-10 03:30:34");
            dashboardDataList.add(data1);
            
            DashboardDataModel data2 = new DashboardDataModel();
            data2.setWithdrawals(3);
            data2.setLastUpdate("2014-01-31 23:59:59");
            dashboardDataList.add(data2);
            
            DashboardDataModel data3 = new DashboardDataModel();
            data3.setWithdrawals(3);
            data3.setLastUpdate("2013-12-31 23:59:59");
            dashboardDataList.add(data3);
            
            DashboardDataModel data4 = new DashboardDataModel();
            data4.setWithdrawals(3);
            data4.setLastUpdate("2013-11-30 23:59:59");
            dashboardDataList.add(data4);

            DashboardDataModel data5 = new DashboardDataModel();
            data5.setWithdrawals(3d);
            data5.setLastUpdate("2013-9-30 23:59:59");
            dashboardDataList.add(data5);

            this.dashBoardModel.setDashboardDataList(dashboardDataList);
            model = this.dashBoardModel;
        }

        this.dataList = model.getDashboardDataList();

        HALF_TRANSPARENT_COLOR_CARD = context.getResources().getColor(R.color.xydetailcolor_card);
        COLOR_CARD = context.getResources().getColor(R.color.xycolor_card);
        COLOR_CARD_CIRCLE = context.getResources().getColor(R.color.color_card_circle);
        
        drawChartTitleBar();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setTypeface(mFontFace);
        drawBackgroundColor(canvas);
        drawAxis(canvas);

        drawTitleText(canvas, textPaint, getResources().getString(R.string.chart_title_card));
        
        if (!bHaveMovements) {
        	drawTitleTextNoMovements(canvas);
        	return;
        }
        
        /*
         * Below will be get called only if have movements.
         */
        drawChart(canvas);
        drawAverageText(canvas);

        if (touchedBarIndex != INVALID_BAR_INDEX) {
        	
            mPaint.setColor(HALF_TRANSPARENT_COLOR_CARD);
            Bar bar = barList.get(touchedBarIndex);

			/*
			 * Draw new circle design. Circle at bottom if the value less than zero.
			 */
            if (bar.withdrawal >= 0) 
            	drawSlidbar(canvas, bar.middle, bar.top, 0);
            else 
            	drawSlidbar(canvas, bar.middle, bar.bottom, 0);
        }
    }
    
	private void drawSlidbar(Canvas canvas, float x, float y, int index) {

		float slidBarR = (int) (slid_bar_r * getMeasuredHeight());

		mPaint.setColor(Color.rgb(150, 150, 150));
		canvas.drawCircle(x, y, slidBarR, mPaint);
		mPaint.setColor(Color.WHITE);
		canvas.drawCircle(x, y, slidBarR - 1, mPaint);
		mPaint.setColor(HALF_TRANSPARENT_COLOR_CARD);
		canvas.drawCircle(x, y, slidBarR * 0.8f, mPaint);
	}

	private void drawBackgroundColor(Canvas canvas) {
        canvas.drawColor(bgColor2);

        mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);

		int color = bgColor2;

		// Draw rect background
		for (int i = 1; i <= dataList.size(); i++) {
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

	PathEffect dash_effect = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);

    private void drawAxis(Canvas canvas) {
        mPaint.setAntiAlias(true);

        // draw y axis
        mPaint.setColor(yAxisColor);
        mPaint.setStrokeWidth(yAxisScale);
        canvas.drawLine(yAxisXPosition, 0, yAxisXPosition, yAxisEndYPosition,
                mPaint);

        // draw x axis
        mPaint.setColor(xAxisColor);
        mPaint.setStrokeWidth(xAxisScale);
        canvas.drawLine(xAxisStartXPosition, xAxisYPosition, yAxisXPosition + dataList.size() * xCellLength, xAxisYPosition,
                mPaint);

        if (xAxisYPosition != yAxisEndYPosition) {
        	canvas.drawLine(xAxisStartXPosition, yAxisEndYPosition, yAxisXPosition + dataList.size() * xCellLength, yAxisEndYPosition,
                mPaint);
        }
 
        if (bHaveMovements) {
        	drawAverageLine(canvas);
        }

        textPaint.setAntiAlias(true);
        textPaint.setTypeface(mFontFace);
        // draw the y axis valus
        textPaint.setColor(Color.GRAY);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setTextSize(yPaintScale);
        int x = yAxisXPosition - 2 * yAxisScale;
        int y = yAxisEndYPosition;
        for (int i = 0, count = yValues.size(); i < count; i++) {
            canvas.drawText(yValues.get(i), x, y, textPaint);
            y -= yCellLength;
        }

        // draw the x axis values
        mPaint.setColor(yAxisColor);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(xPaintScale);
        x = yAxisXPosition + xCellLength;
        for (int count = dataList.size(), i = count - 1; i >= 0; i--) {
    		mPaint.setStrokeWidth(yAxisScale / 2);
            // draw the vertical line
            canvas.drawLine(x, 0, x, yAxisEndYPosition, mPaint);

            mpath.reset();
            
    		String textMonth = TimeUtil.changeChartFormattrString(context,
    				dataList.get(i).getLastUpdate(), TimeUtil.dateFormat2,
    				TimeUtil.detaFormat6);
    		drawTextMonths(canvas, textMonth, x, dataList.size());

            x += xCellLength;
        }
    }

	private void drawTitleTextNoMovements(Canvas canvas) {
        textPaint.setTextSize(titleTextSize);
        textPaint.setTypeface(mFontFace);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(COLOR_CARD);

        String strNoMovements = getResources().getString(R.string.no_movements_in_period);
        canvas.drawText(strNoMovements, getMeasuredWidth()/2 - textPaint.measureText(strNoMovements)/2 , getMeasuredHeight()/2, textPaint);
	}

    private void drawAverageLine(Canvas canvas) {
        // draw average line
        mPaint.setColor(COLOR_CARD);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setPathEffect(dash_effect);
        mPaint.setStrokeWidth(1);

        /*
         * Should consider the case of all values below zero.
         */
        if (m_min < 0 || averageWithdrawal < 0) {
        	mAverageY = (int)(averageWithdrawal / minimumWithdrawal * (xAxisYPosition - yAxisStartYPosition));
        	mAverageY = xAxisYPosition + mAverageY;
        }
        else {
        	mAverageY= (int)(averageWithdrawal / maximumWithdrawal * (yAxisEndYPosition - yAxisStartYPosition));
        	mAverageY = yAxisEndYPosition - mAverageY;
        }
        mpath.reset();
        mpath.moveTo(yAxisXPosition, mAverageY);
        mpath.lineTo(yAxisXPosition + dataList.size() * xCellLength, mAverageY);

        if (averageWithdrawal != 0)
            canvas.drawPath(mpath, mPaint);

        mPaint.setPathEffect(null);
        mPaint.setStyle(Paint.Style.FILL);
    }

	private void drawAverageText(Canvas canvas) {
		textPaint.setTextSize(xPaintScale);
		
		/*
		 * Deal with the average value overlap the line.
		 */
		int y = mAverageY - yPaintScale / 2;
		if (averageWithdrawal < 0 && Math.abs(mAverageY - xAxisYPosition) < yPaintScale *2 ) {
			y = xAxisYPosition - yPaintScale / 2;
		}

		int x = yAxisXPosition + 4 * yAxisScale;
		textPaint.setTextAlign(Paint.Align.LEFT);

		textPaint.setColor(Color.GRAY);
		
		/*
		 * Average text
		 * Deal with the average value overlap the line.
		 */
		int yAverageText = y - (int) (yPaintScale * 1.3f);
		if (averageWithdrawal < 0 && Math.abs(yAverageText -xAxisYPosition) < yPaintScale) {
			yAverageText = yAverageText - yPaintScale / 2;
		}

		String average = context.getResources().getString(R.string.card_rotate_average_str);
		canvas.drawText(average, x, yAverageText, textPaint);

		/*
		 * Average value
		 */
		String averageText = Utils.generateFormatMoney(context.getResources()
				.getString(R.string.eur), averageWithdrawal);
		canvas.drawText(averageText, x, y, textPaint);
	}
	
    private void drawTextMonths(Canvas canvas, String text, int xRightBond, int numberMonths)
    {
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
        
		if (numberMonths >= Contants.CHART_THRESHOLD_MONTHS || xCellLength <= textPaint.measureText(str1)) {
			str1 = str1.substring(0, 3);
		}

        mpath.moveTo(xRightBond - xCellLength, yAxisEndYPosition + 1.5f * yPaintScale);
        mpath.lineTo(xRightBond , yAxisEndYPosition + 1.5f * yPaintScale);
        
        canvas.drawTextOnPath(str1, mpath, xCellLength/2 - textPaint.measureText(str1) /2 , 0, textPaint);
        canvas.drawTextOnPath(str2, mpath, xCellLength/2 - textPaint.measureText(str2) /2 , 1.5f * yPaintScale, textPaint);
    }

    private void drawChart(Canvas canvas) {
        float XOYR = (int)(x_o_y_r * getMeasuredHeight());

        for (int i = 0, count = barList.size(); i < count; i++) {
        	barList.get(i).XOYR = XOYR;

            mPaint.setColor(COLOR_CARD);
            barList.get(i).drawSelf(canvas, mPaint);

            mPaint.setColor(COLOR_CARD_CIRCLE);
            
            /*
             * shows the circle at bottom if value less than zero
             */
            if (barList.get(i).withdrawal >=0 )
            	canvas.drawCircle(barList.get(i).middle,barList.get(i).top, XOYR, mPaint);
            else
            	canvas.drawCircle(barList.get(i).middle,barList.get(i).bottom, XOYR, mPaint);
        }
    }

    private void generateBarList() {
        int x = yAxisXPosition + xCellLength;

        /*
         * Increase the bar width than before
         */
        Bar.setBarWidth(xCellLength / 3);
        
        mPaint.setColor(Color.GRAY);
        barList.clear();
        int top;
        int bottom;
        for (int count = dataList.size(), i = count - 1; i >= 0; i--) {
            DashboardDataModel model = dataList.get(i);
            int y = (int)(model.getWithdrawals() / (maximumWithdrawal - minimumWithdrawal) * (yAxisEndYPosition - yAxisStartYPosition));
            if (y >= 0) {
                top = xAxisYPosition - y;
                bottom = xAxisYPosition;
            } else {
                top = xAxisYPosition;
                bottom = xAxisYPosition - y;
            }
            Bar bar = new Bar(x - xCellLength /2 , top, bottom);
            bar.withdrawal = model.getWithdrawals();
            bar.xValue = TimeUtil.changeChartFormattrString(context,
    				dataList.get(i).getLastUpdate(), TimeUtil.dateFormat2,
    				TimeUtil.detaFormat6);
            
            if (isPrepaidCard) {
            	bar.yValue = Utils.noSignGenerateFormatMoney(eur, model.getWithdrawals());
            }
            else {
                bar.yValue = Utils.generateFormatMoney(eur, model.getWithdrawals());
            }
            barList.add(bar);
            x += xCellLength;
        }
    }

    @Override
    protected void initPosition() {
        super.initPosition();
        screen_height = getMeasuredHeight();
        screen_width = getMeasuredWidth();
        int count = dataList.size();
        double sum = 0;
        double withdrawal = 0;
        m_min = Integer.MAX_VALUE;
        m_max = -Integer.MAX_VALUE;

        for (int i = 0; i < count; i++) {
            withdrawal = dataList.get(i).getWithdrawals();
            sum += withdrawal;
            if (withdrawal > m_max) {
                m_max = withdrawal;
            }
            if (withdrawal < m_min) {
                m_min = withdrawal;
            }
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
		if (count == 0) {
			averageWithdrawal = 0;
		} else if (sum == 0){
			averageWithdrawal = 0;
		} else {
			averageWithdrawal = sum / count;
			averageWithdrawal = averageWithdrawal - 0.005;
		}
        
        xPaintScale = (int)(screen_height * x_paint_scale);
        yPaintScale = (int)(screen_height * y_paint_scale);

        detailText1Size = (int)(detail_text_1_size * screen_height);
        detailText2Size = (int)(detail_text_2_size * screen_height);

        yAxisStartYPosition = chartTitleY + yPaintScale;
        yAxisEndYPosition = screen_height - padding - 2 * yPaintScale;

        initYValues(m_max, m_min);

        yAxisXPosition = getMaxYValueLen() + padding;

        xAxisStartXPosition = yAxisXPosition;
        xAxisEndXPosition = screen_width - padding;

        xCellLength = (int)((float)(xAxisEndXPosition - xAxisStartXPosition) / (count));

        detailRoundR = (int)(detail_round_r * screen_height);

        generateBarList();
		bHaveMovements = haveMovements();
    }

	private boolean haveMovements() {
		boolean ret = false;
		if (this.dataList == null || this.dataList.size() == 0)
			return false;

		for (int i = 0; i < this.dataList.size(); i++)
			if (this.dataList.get(i).getWithdrawals() != 0)
				return true;

		return ret;
	}

    private void initYValues(double max, double min) {
        yValues.clear();
        if (min >= 0) {
            double yCell = getYCell(max, NO_SIGN_SIZE);
            for (int i = 0; i <= NO_SIGN_SIZE; i++) {
            	
            	/*
            	 *  remove the "+" near 0,00 at the y axis.
            	 */
            	if (i == 0) {
                    yValues.add(Utils.noSignGenerateFormatMoney(eur, i * yCell));
            	} else {
            		yValues.add(Utils.generateFormatMoney(eur, i * yCell));
            	}
            }
            yCellLength = (int)((float)(yAxisEndYPosition - yAxisStartYPosition) / NO_SIGN_SIZE);
            xAxisYPosition = yAxisEndYPosition;
            maximumWithdrawal = NO_SIGN_SIZE * yCell;
            minimumWithdrawal = 0;
        } else {
            double mm = Math.max(Math.abs(min), Math.abs(max));
            double yCell = getYCell(mm, SIGN_SIZE / 2);
            for (int i = -SIGN_SIZE / 2; i <= SIGN_SIZE / 2; i++) {

            	/*
            	 *  remove the "+" near 0,00 at the y axis.
            	 */
            	if (i == 0) {
                    yValues.add(Utils.noSignGenerateFormatMoney(eur, i * yCell));
            	} else {
                    yValues.add(Utils.generateFormatMoney(eur, i * yCell));
            	}
            }
            yCellLength = (int)((float)(yAxisEndYPosition - yAxisStartYPosition) / SIGN_SIZE);
            xAxisYPosition = yAxisEndYPosition - yCellLength * (SIGN_SIZE / 2);
            maximumWithdrawal = SIGN_SIZE / 2 * yCell;
            minimumWithdrawal = -SIGN_SIZE / 2 * yCell;
        } 
    }

    private int getMaxYValueLen() {
        int maxYValueLen = 0;
        mPaint.setTextSize(yPaintScale);
        for (int i = 0, count = yValues.size(); i < count; i++) {
            int yValueLen = (int)mPaint.measureText(yValues.get(i));
            if (yValueLen > maxYValueLen) {
                maxYValueLen = yValueLen;
            }
        }

        return maxYValueLen;
    }

    private double getYCell(double maximumWithdrawal, int size) {
        maximumWithdrawal = Math.ceil(maximumWithdrawal);

        String str = String.valueOf((int)maximumWithdrawal);
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
    public boolean onTouchEvent(MotionEvent event) {
    	if (!bHaveMovements)
    		return true;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int index = findTouchedBar(new Point((int)event.getX(), (int)event.getY()));
                LogManager.d("touched bar: " + index);
                if (index != touchedBarIndex) {
                    if (index > -1) {
                        showTouchedBar(index);
                    } else {
                        hideTouchedBar();
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    int WIDTH_POPUP = 360;
    int HEIGHT_POPUP = 80;

    int WIDTH_STANDARD = 800;
    int HEIGHT_STANDARD = 442;
    
    private int getWidthPopup() {
    	return WIDTH_POPUP * screen_width / WIDTH_STANDARD ;
    }
    
    private int getHeightPopup() {
    	return HEIGHT_POPUP * screen_height / HEIGHT_STANDARD ;
    }

    private void hideTouchedBar() {
        touchedBarIndex = INVALID_BAR_INDEX;
        
        if (popupwindow != null)
        	popupwindow.dismiss();

        invalidate();
    }

    private void showTouchedBar(int index) {
		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();
		
        if (popupwindow != null)
        	popupwindow.dismiss();
        
        touchedBarIndex = index;
        
        Bar bar = barList.get(touchedBarIndex);
        
		View root = View.inflate(chartActivity, R.layout.chart_popup, null);
		
		popupwindow = new PopupWindow(root, getWidthPopup(), getHeightPopup());
		if (ViewConfiguration.get(context).hasPermanentMenuKey() || (index != barList.size()-1 && index != barList.size()-2)) {
			popupwindow.showAtLocation(root, Gravity.NO_GRAVITY, (int)bar.middle - getWidthPopup()/2,
					(int)bar.top - getHeightPopup());
		} else{
			int delta = 0;
			if (index == barList.size()-1) {
				delta = 250;
			} else{
				delta = 150;
			}
			popupwindow.showAtLocation(root, Gravity.NO_GRAVITY, (int)bar.middle - getWidthPopup()/2-delta,
					(int)bar.top - getHeightPopup());
		}
		
		drawChartPopupWindow(root, index);

		chartActivity.setPopupWindow(popupwindow);

        invalidate();
    }

    /**
     * @param point the touched point
     * @return the index of the touched bar, -1 if non
     */
    private int findTouchedBar(Point point) {
        for (int i = 0, count = barList.size(); i < count; i++) {
            Bar bar = barList.get(i);
            if (bar.isPointInBar(point)) {
                return i;
            }
        }
        return INVALID_BAR_INDEX;
    }

	public void drawChartPopupWindow(View root, int index) {
		LinearLayout layout = (LinearLayout)root.findViewById(R.id.chart_popup_window);
		Drawable shape = context.getResources().getDrawable(R.drawable.shape_roundrect_card);
		layout.setBackgroundDrawable(shape);

		setPopupWindowFont(root);
		
		View seperator = (LinearLayout) root.findViewById(R.id.chart_popup_seperator_iban);
		seperator.setVisibility(View.VISIBLE);

        Bar bar = barList.get(touchedBarIndex);
		TextView tv = (TextView) root.findViewById(R.id.chart_popup_title);
		String month = bar.xValue;
		month = month.replace(".", " ");
		tv.setText(month);
		
		FillText(root, R.id.row1_title,
				R.string.withdrawal_month, R.id.row1_value,bar.yValue);
		
		root.findViewById(R.id.tableRow2).setVisibility(View.GONE);
		root.findViewById(R.id.tableRow3).setVisibility(View.GONE);
	}

	public void setIsPrepaidCard(boolean isPrepaidCard) {
		this.isPrepaidCard = isPrepaidCard;
		drawChartTitleBar();
		generateBarList();
	}

    public void setIsPreferred(boolean isPreferred) {
		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();
		
		ImageView isPreferredStar =(ImageView) chartActivity.chartsWindow.findViewById(R.id.isPreferredStar);
		
		isPreferredStar.setImageResource(R.drawable.icona_carta_stella);

		if (isPreferred)
			isPreferredStar.setVisibility(View.VISIBLE);
		else
			isPreferredStar.setVisibility(View.GONE);
    }

	private void drawChartTitleBar() {
		ChartActivity chartActivity = (ChartActivity) ChartActivity
				.getContext();
		LogManager.i(dashBoardModel.toString());

		setTitleBarFont(chartActivity.chartsWindow);
		/*
		 * Update Account Name
		 */
		String accountName = dashBoardModel.getPersonalizedName();
		FillText(chartActivity.chartsWindow, R.id.accountName,
				R.string.account2, R.id.accountName, accountName);

		TextView tvAccountName = (TextView) chartActivity.chartsWindow
				.findViewById(R.id.accountName);
		tvAccountName.setTextColor(getResources().getColor(R.color.xycolor_card));

		/*
		 * Account Type Prepaid card -- Carta Prepagata, Credit card -- Carta di Credito
		 */
		TextView tvAccountType = (TextView)chartActivity.chartsWindow.findViewById(R.id.accountType);
		tvAccountType.setText(getResources().getString(R.string.credit_cards));
		tvAccountType.setTextColor(getResources().getColor(R.color.xycolor_card));

		if (isPrepaidCard)
			tvAccountType.setText(getResources().getString(R.string.prepaid_cards_1));

		/*
		 * Update Total Withdrawals of current month
		 */
		String text;
		if (dashBoardModel != null
				&& dashBoardModel.getDashboardDataList() != null
				&& dashBoardModel.getDashboardDataList().size() > 0) {
			if (isPrepaidCard) {
				text = Utils.noSignGenerateFormatMoney(
					getResources().getString(R.string.eur), dashBoardModel
							.getDashboardDataList().get(0).getWithdrawals());
			}
			else {
				text = Utils.generateFormatMoney(
						getResources().getString(R.string.eur), dashBoardModel
								.getDashboardDataList().get(0).getWithdrawals());
			}
		} else {
			text = context.getResources().getString(R.string.not_able);
		}

		String totalWithdrawalTitle = getResources().getString(R.string.total_withdrawals);
		totalWithdrawalTitle = Utils.toUppString(totalWithdrawalTitle);
		FillText(chartActivity.chartsWindow, R.id.mid_title,
				totalWithdrawalTitle, R.id.mid_value, text);
		
		TextView tvMidTitle = (TextView) chartActivity.chartsWindow.findViewById(R.id.mid_title);
		tvMidTitle.setTextColor(getResources().getColor(R.color.xycolor_card));
		TextView tvMid = (TextView) chartActivity.chartsWindow.findViewById(R.id.mid_value);
		tvMid.setTextColor(getResources().getColor(R.color.xycolor_card));
		
		/*
		 * AvailableBalance
		 */
		String availableBalance = Utils.generateFormatMoney(context
				.getResources().getString(R.string.eur), dashBoardModel
				.getAvailableBalance());
		FillText(chartActivity.chartsWindow, R.id.row1_title,
				R.string.available_balance, R.id.row1_value,
				availableBalance);
		
		/*
		 * Card Number
		 */
        String cardNumber = "";
        for (AccountsModel accountsModel : Contants.cardAccounts) {

            if (this.dashBoardModel.getAccountCode().equals(accountsModel.getAccountCode())) {
                cardNumber = accountsModel.getCardNumber();
            }
        }
        
        if (BaseActivity.isOffline)
        	cardNumber = "XXXXXXXXXXXX1234";
        
		if (cardNumber.length() >= 12)
			cardNumber = "..." + cardNumber.substring(cardNumber.length() - 12);
		
		FillText(chartActivity.chartsWindow, R.id.row2_title,
				R.string.card_number1, R.id.row2_value,
				cardNumber);
		TextView tvCardNumber = (TextView)chartActivity.chartsWindow.findViewById(R.id.row2_value);
		tvCardNumber.setPadding(0, 0, 0, 0);

		/*
		 * Last Updated
		 */
        String nowTime = TimeUtil.getDateString(System.currentTimeMillis(), TimeUtil.dateFormat5);
        FillText(chartActivity.chartsWindow, R.id.row3_title, R.string.data_updated_on, R.id.row3_value, nowTime);
        
        /*
         * Icon 
         */
        ImageView iv = (ImageView) chartActivity.chartsWindow.findViewById(R.id.chart_icon);
        iv.setImageResource(R.drawable.top_icons_cards);
	}
}
