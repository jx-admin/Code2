
package com.accenture.mbank.view.table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.accenture.mbank.R;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.DashBoardModel;
import com.accenture.mbank.model.DashboardDataModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.Utils;

public class CardsBarGraphic extends FormView {

    private Paint mPaint = new Paint();

    private static final int INVALID_BAR_INDEX = -1;

    private static final String TAG = "CardsBarGraphic";

    /**
     * the following two SimpleDateFormat are used to convert the format.
     */
    private static SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.US);

    public static final String outputDateFormater="MMM yyyy";
    private static SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);

    /**
     * data model
     */
    private List<DashboardDataModel> dataList;

    /**
     * bar list
     */
    private List<Bar> barList = new ArrayList<Bar>();

    /**
     * minimum withdrawal
     */
    private double minimumWithdrawal = 0;

    /**
     * the average withdrawals
     */
    private double averageWithdrawal = 0;

    private String averageText = null;

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

    float title_second_size = 30f / 577;

    int detailText1Size = 0;

    int detailText2Size = 0;

    int titleSecondSize = 0;

    int titleSecondY = 0;

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

    private final String dollar = getContext().getString(R.string.dollar);

    private RectF detailedRect = new RectF(0, 0, 0, 0);

    private static final int NO_SIGN_SIZE = 5;

    private static final int SIGN_SIZE = 10;

    private static final int HALF_TRANSPARENT_BLUE = 0xC80000FF;

    // private static final String TAG = "CardsBarGraphic";

    public CardsBarGraphic(Context context, DashBoardModel model) {
        super(context);
        this.dataList = model.getDashboardDataList();
        setTextLeft(getContext().getString(R.string.sa_withdrawals));

        String num = "";
        for (AccountsModel accountsModel : Contants.cardAccounts) {

            if (model.getAccountCode().equals(accountsModel.getAccountCode())) {
                num = accountsModel.getCardNumber();
            }
        }

        setTextRight(getContext().getString(R.string.card_number_str) + num);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChart(canvas);
        drawAxis(canvas);

        if (touchedBarIndex != INVALID_BAR_INDEX) {
            Bar bar = barList.get(touchedBarIndex);
            mPaint.setColor(Color.BLUE);
            bar.drawSelf(canvas, mPaint);
            textPaint.setTextSize(detailText1Size);
            float text1Length = textPaint.measureText(bar.yValue) + detailText1Size;
            textPaint.setTextSize(detailText2Size);
            float text2Length = textPaint.measureText(bar.xValue) + detailText2Size;
            int detailWidth = (int)(Math.max(text1Length, text2Length) * 1.5f);
            int detailHeight = (int)(1.2f * (detailText1Size + detailText2Size));
            float start_x = (bar.middle - 0.5f * detailWidth);
            float end_x = start_x + detailWidth;
            float start_y = 0;
            float end_y = 0;
            if (bar.withdrawal >= 0) {
                start_y = bar.top - detailHeight;
                end_y = bar.top;
            } else {
                start_y = bar.bottom;
                end_y = bar.bottom + detailHeight;
            }

            detailedRect.set(start_x, start_y, end_x, end_y);
            mPaint.setColor(HALF_TRANSPARENT_BLUE);
            canvas.drawRoundRect(detailedRect, detailRoundR, detailRoundR, mPaint);

            textPaint.setColor(Color.WHITE);
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setTextSize(detailText1Size);
            float text1Y = start_y + 1f * detailText1Size;
            canvas.drawText(bar.yValue, bar.middle, text1Y, textPaint);

            textPaint.setTextSize(detailText2Size);
            float text2Y = end_y - 0.5f * detailText2Size;
            canvas.drawText(bar.xValue, bar.middle, text2Y, textPaint);
        }
    }

    private void drawAxis(Canvas canvas) {
        mPaint.setAntiAlias(true);

        // draw y axis
        mPaint.setColor(yAxisColor);
        mPaint.setStrokeWidth(yAxisScale);
        canvas.drawLine(yAxisXPosition, yAxisStartYPosition, yAxisXPosition, yAxisEndYPosition,
                mPaint);

        // draw x axis
        mPaint.setColor(xAxisColor);
        mPaint.setStrokeWidth(xAxisScale);
        canvas.drawLine(xAxisStartXPosition, xAxisYPosition, xAxisEndXPosition, xAxisYPosition,
                mPaint);

        // draw average line
        int y = (int)(averageWithdrawal / maximumWithdrawal * (yAxisEndYPosition - yAxisStartYPosition));
        y = yAxisEndYPosition - 1 - y;
        canvas.drawLine(yAxisXPosition, y, xAxisEndXPosition, y, mPaint);

        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.SERIF);
        textPaint.setColor(Color.BLACK);

        // draw the second title
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(titleSecondSize);
        canvas.drawText(averageText, titleSecondSize, titleSecondY, textPaint);

        // draw the y axis valus
        textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setTextSize(yPaintScale);
        int x = yAxisXPosition - 2 * yAxisScale;
        y = yAxisEndYPosition;
        for (int i = 0, count = yValues.size(); i < count; i++) {
            canvas.drawText(yValues.get(i), x, y, textPaint);
            y -= yCellLength;
        }

        // draw the x axis values
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(xPaintScale);
        x = yAxisXPosition + xCellLength;
        for (int count = dataList.size(), i = count - 1; i >= 0; i--) {

            // draw the vertical line
            canvas.drawLine(x, yAxisStartYPosition, x, yAxisEndYPosition, mPaint);

            mpath.reset();
            String temp = convertDateFormat(dataList.get(i).getLastUpdate());
            String[] strs = temp.split(" ");
            mpath.moveTo(x - yPaintScale / 2, yAxisEndYPosition + 2 * yPaintScale);
            mpath.lineTo(x + 3 * yPaintScale, yAxisEndYPosition);

            canvas.drawTextOnPath(strs[0], mpath, 0, 0, textPaint);
            canvas.drawTextOnPath(strs[1], mpath, 0, yPaintScale, textPaint);

            x += xCellLength;
        }
    }

    private void drawChart(Canvas canvas) {
        mPaint.setColor(Color.GRAY);
        for (int i = 0, count = barList.size(); i < count; i++) {
            barList.get(i).drawSelf(canvas, mPaint);
        }
    }

    private void generateBarList() {
        int x = yAxisXPosition + xCellLength;
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
            Bar bar = new Bar(x, top, bottom);
            bar.withdrawal = model.getWithdrawals();
            bar.xValue = convertDateFormat(model.getLastUpdate());
            bar.yValue = Utils.generateFormatMoney(dollar, model.getWithdrawals());
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
        double min = Integer.MAX_VALUE;
        double max = -Integer.MAX_VALUE;
        for (int i = 0; i < count; i++) {
            withdrawal = dataList.get(i).getWithdrawals();
            sum += withdrawal;
            if (withdrawal > max) {
                max = withdrawal;
            }
            if (withdrawal < min) {
                min = withdrawal;
            }
        }
        averageWithdrawal = (int)sum / count;

        averageText =getContext().getString(R.string.card_rotate_average_str)/* "Average Expenditure on Card: "*/
                + Utils.generateFormatMoney(dollar, averageWithdrawal);

        xPaintScale = (int)(screen_height * x_paint_scale);
        yPaintScale = (int)(screen_height * y_paint_scale);

        detailText1Size = (int)(detail_text_1_size * screen_height);
        detailText2Size = (int)(detail_text_2_size * screen_height);
        titleSecondSize = (int)(title_second_size * screen_height);
        titleSecondY = titleLineY + (int)(titleSecondSize * 1.2F);

        yAxisStartYPosition = titleSecondY + padding;
        yAxisEndYPosition = screen_height - padding - 2 * yPaintScale;

        initYValues(max, min);

        yAxisXPosition = getMaxYValueLen() + padding;

        xAxisStartXPosition = yAxisXPosition;
        xAxisEndXPosition = screen_width - padding;

        xCellLength = (int)((float)(xAxisEndXPosition - xAxisStartXPosition) / (count + 1));

        detailRoundR = (int)(detail_round_r * screen_height);

        generateBarList();
    }

    private void initYValues(double max, double min) {
        yValues.clear();
        if (min >= 0) {
            double yCell = getYCell(max, NO_SIGN_SIZE);
            for (int i = 0; i <= NO_SIGN_SIZE; i++) {
                yValues.add(Utils.generateFormatMoney(dollar, i * yCell));
            }
            yCellLength = (int)((float)(yAxisEndYPosition - yAxisStartYPosition) / NO_SIGN_SIZE);
            xAxisYPosition = yAxisEndYPosition;
            maximumWithdrawal = NO_SIGN_SIZE * yCell;
            minimumWithdrawal = 0;
        } else if (min < 0 && max > 0) {
            double mm = Math.max(Math.abs(min), Math.abs(max));
            double yCell = getYCell(mm, SIGN_SIZE / 2);
            for (int i = -SIGN_SIZE / 2; i <= SIGN_SIZE / 2; i++) {
                yValues.add(Utils.generateFormatMoney(dollar, i * yCell));
            }
            yCellLength = (int)((float)(yAxisEndYPosition - yAxisStartYPosition) / SIGN_SIZE);
            xAxisYPosition = yAxisEndYPosition - yCellLength * (SIGN_SIZE / 2);
            maximumWithdrawal = SIGN_SIZE / 2 * yCell;
            minimumWithdrawal = -SIGN_SIZE / 2 * yCell;
        } else {
            double mm = Math.abs(min);
            double yCell = getYCell(mm, NO_SIGN_SIZE);
            for (int i = -NO_SIGN_SIZE; i <= 0; i++) {
                yValues.add(Utils.generateFormatMoney(dollar, i * yCell));
            }
            yCellLength = (int)((float)(yAxisEndYPosition - yAxisStartYPosition) / NO_SIGN_SIZE);
            xAxisYPosition = yAxisStartYPosition;
            maximumWithdrawal = 0;
            minimumWithdrawal = -NO_SIGN_SIZE * yCell;
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
        int result = (int)(bb * Math.pow(10, str.length() - 1));

        double cell = result / (double)size;
        return cell;
    }

    private String convertDateFormat(final String inputDate) {
        String outputDate = null;
        try {
        	outputDateFormat=new SimpleDateFormat(outputDateFormater,getContext().getResources().getConfiguration().locale);
            outputDate = outputDateFormat.format(inputDateFormat.parse(inputDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int index = findTouchedBar(new Point((int)event.getX(), (int)event.getY()));
                Log.d(TAG, "touched bar: " + index);
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

    private void hideTouchedBar() {
        touchedBarIndex = INVALID_BAR_INDEX;
        invalidate();
    }

    private void showTouchedBar(int index) {
        touchedBarIndex = index;
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
}
