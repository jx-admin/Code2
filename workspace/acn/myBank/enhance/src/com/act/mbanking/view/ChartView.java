package com.act.mbanking.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.act.mbanking.ChartModelManager;
import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.bean.ChartModel;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.LogManager;
import com.act.mbanking.utils.Number_scale;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.custom.view.RectLD;

public class ChartView extends ChartViewWidthPath {

	boolean isNew = true;

	private static final String TAG = "ChartView";

	// public HorizontalChartView horizontalChartView;

	public static final int YEAR = 0;

	public static final int HALF_YEAR = 1;

	public static final int MONTH = 2;

	public static final int WEEK = 3;

	public static final int DAY = 4;

	public List<Integer> colors = new ArrayList<Integer>();

	Handler handler;

	private int showStatus = show_0;

	public static final int show_0 = 0;

	public static final int show_1 = 1;

	public static final int show_2 = 2;

	public static final int DEFAULT_STATUS = 0;

	public static final int DOWN_1 = 1;

	public static final int DOWN_2 = 3;

	public static final int MOVE_1 = 2;

	public static final int MOVE_2 = 4;

	private int mTouchState;

	/**
	 * x轴上每像素代表的距离
	 */
	private double xCellLength;

	private int currentStyle;

	// public List<List<ChartModel>> srcXYValuesList = new
	// ArrayList<List<ChartModel>>();

	private List<ChartModelMapTool> currentChartModelMapTools = new ArrayList<ChartModelMapTool>();

	/**
	 * 时间范围里的数据
	 */

	/**
	 * y轴上每像素代表的距离
	 */
	private double yCellLength;

	// private long from;

	// private long to;

	public static final int NO_SIGN_SIZE = 4;

	float x0, x1, y0, y1;

	// public static final int SIGN_SIZE = 10;

	/**
	 * 每毫秒代表的长度
	 */
	double cellLengthX = 0;

	/**
	 * 每元所代表的长度
	 */
	double cellLengthY = 0;

	/**
	 * 0为上正下负，1上正，-1为下正
	 */
	@Deprecated
	int yValuesStyle = 0;

	// public void generateLinePath() {
	//
	// if (yValues == null||yValues[0]==null||yValues[yValues.length - 1]==null)
	// {
	// return;
	// }
	// cellLengthX = xAxisLength / (float)(to - from-TimeUtil.ONE_DAY);
	// cellLengthY = Math .abs(yAxisLength*0.9 / (yValues[0].value -
	// yValues[yValues.length - 1].value));
	// float oo_y=(float) (yAxisLength*(0d-yValues[yValues.length -
	// 1].value)/(yValues[0].value - yValues[yValues.length - 1].value));
	//
	// // 先排序
	// if(currentChartModelMapTools == null && currentChartModelMapTools.size()
	// ==0){
	// return;
	// }
	// ChartModel[] xYValues;
	// for (int j = 0; j < currentChartModelMapTools.size(); j++) {
	// ChartModelMapTool mChartModelMapTool=currentChartModelMapTools.get(j);
	// if(mChartModelMapTool==null){
	// continue;
	// }
	// xYValues = mChartModelMapTool.xyValueList;
	// if (xYValues== null || xYValues.length == 0) {
	// continue;
	// }
	// Path path = new Path();
	// mChartModelMapTool.path = path;
	// if (yValuesStyle == 0) {
	// path.moveTo(o_x,o_y- oo_y);
	// } else if (yValuesStyle == 1) {
	// path.moveTo(o_x, o_y);
	// } else if (yValuesStyle == -1) {
	// path.moveTo(o_x, y_end);
	// }
	// float xx = 0;
	// for (int i = 0; i < xYValues.length; i++) {
	//
	// ChartModel chartModel = xYValues[i];
	// if (chartModel == null) {
	// continue;
	// }
	//
	// long time = chartModel.getTimeMills();
	//
	// //
	// xx = (float)(o_x + (time - from) * cellLengthX);
	// if(chartModel==null||yValues==null||
	// yValues.length<=0||yValues[0]==null){
	// continue;
	// }
	// float yy = (float)(o_y - (chartModel.getAfterAdded() - yValues[0].value)
	// * cellLengthY);
	//
	// if (i == 0) {
	// path.lineTo(o_x, yy);
	//
	// if (xYValues.length == 1) {
	// xx = o_x + xAxisLength;
	// path.lineTo(xx, yy);
	// } else {
	// // long timeT = time + TimeUtil.ONE_DAY;
	// // float oneday = (float)(o_x + (timeT - from) * cellLengthX);
	//
	// // path.lineTo(oneday, yy);
	// }
	// } else {
	// path.lineTo(xx, yy);
	// // long timeT = time + TimeUtil.ONE_DAY;
	// // float oneday = (float)(o_x + (timeT - from) * cellLengthX);
	// // xx = oneday;
	// // path.lineTo(oneday, yy);
	// }
	// // LogManager.d("sort" + yy);
	// Log.d(TAG,"time "+chartModel.getDate()+" "+chartModel.getValue());
	// }
	//
	// if (yValuesStyle == 0) {
	// path.lineTo(xx,o_y- oo_y);
	// } else if (yValuesStyle == 1) {
	// path.lineTo(xx, o_y);
	// } else if (yValuesStyle == -1) {
	//
	// path.lineTo(xx, y_end);
	// }
	// path.close();
	// }
	// }

	Path paths[];

	RectLD dataArea;

	int s;

	int e;

	ViewConfiguration viewConfiguration;

	public static final int SIGN_SIZE = 9;
	
	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			invalidate();
			super.handleMessage(msg);
		}
	};

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		handler = new Handler();
		viewConfiguration = new ViewConfiguration();
	}

	private RectLD generateChartModelwjx(int s, int e,
			List<ChartModelMapTool> currentChartModelMapTools, RectLD rectLD) {
		Log.d(TAG, "generateChartModelwjx");

		if (rectLD == null) {
			rectLD = new RectLD();
		}
		if (currentChartModelMapTools == null
				&& currentChartModelMapTools.size() < 0) {
			Log.d(TAG, "currentChartModelMapTools null");
			return rectLD;
		}
		rectLD.top = Double.MAX_VALUE;
		rectLD.bottom = Double.MIN_VALUE;

		int yNum = currentChartModelMapTools.size();
		float yp = 0, yp2 = 0;
		for (int i = s; i < e; ++i) {
			yp = 0;
			yp2 = 0;
			for (int j = 0; j < yNum; j++) {
				ChartModelMapTool chartModelMapTool = currentChartModelMapTools
						.get(j);
				if (chartModelMapTool != null) {
					ChartModel mChartModel = null;
					if (chartModelMapTool.getYearList() != null
							&& i < chartModelMapTool.getYearList().size()) {
						mChartModel = chartModelMapTool.getYearList().get(i);
					}
					if (mChartModel != null) {
						if (mChartModel.getValue() > 0) {
							mChartModel.setBeforeAdded(yp);
							yp += mChartModel.getValue();
							mChartModel.setAfterAdded(yp);
						} else {
							mChartModel.setBeforeAdded(yp2);
							yp2 += mChartModel.getValue();
							mChartModel.setAfterAdded(yp2);
						}
						rectLD.top = Math.min(mChartModel.getAfterAdded(),
								rectLD.top);
						rectLD.bottom = Math.max(mChartModel.getAfterAdded(),
								rectLD.bottom);
					}
				}

			}
		}

		return rectLD;
		// StringBuffer str=new StringBuffer();
		// for(int j=0;j<yNum;j++){
		// ChartModelMapTool chartModelMapTool=currentChartModelMapTools.get(j);
		// str.append(j+"  "+chartModelMapTool.title+"\n");
		// ChartModelManager.toString(str,chartModelMapTool.getYearList());
		// }
		// ChartModelManager.writeSdcardFile("/sdcard/generateChartModelwjx.txt",str.toString().getBytes());
	}
	
	public void postRefresh(){
		new Thread(){
			public void run(){
				refresh();
				mHandler.sendEmptyMessage(0);
			}
		}.start();
	}

	public void refresh() {

		// ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
		// progressOverlay.show("", new OnProgressEvent() {
		//
		// @Override
		// public void onProgress() {

		dataArea = getTimeStyleArea(getTimeStyle(), dataArea);
		Log.d(TAG,
				"Time from1:"
						+ TimeUtil.getDateString(dataArea.left,
								TimeUtil.dateFormat2)
						+ " to:"
						+ TimeUtil.getDateString(dataArea.right,
								TimeUtil.dateFormat2));

		initXValue(dataArea.left, dataArea.right, getTimeStyle());
		// printValues(xValues);

		// int size = (int)((dataArea.width()+TimeUtil.ONE_DAY) /
		// TimeUtil.ONE_DAY);
		// if(size==0){
		// size=1;
		// }
		// s=366-size;
		// e=366;

		dataArea = generateChartModelwjx(s, e, currentChartModelMapTools,
				dataArea);
		initYValues(dataArea.top, dataArea.bottom);
		createChartPath(s, e, currentChartModelMapTools);
		// calcDataArea(dataArea);
//		if (!isNew) {
//			 generateLinePath();
//		} else {
			// calcuAreaData(from, to, currentChartModelMapTools);
			// generateYValuesWjx();
//		}

		// invalidate();
		// if (horizontalChartView != null) {
		// horizontalChartView.refresh();
		// }
		// handler.post(new Runnable(){
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// postInvalidate();
		// }});

		// }
		// });

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * generate X values
	 * 
	 * @see #YEAR;
	 * @see #HALF_YEAR;
	 * @see #MONTH;
	 * @see #WEEK;
	 * @see #DAY;
	 * @param style
	 */
	private void initXValue(long from, long to, int style) {
		// xValues = new ValueWidthName[];
		// currentStyle = style;
		Calendar temp = Calendar.getInstance();
		temp.setTimeInMillis(from);
		int size = 6;
		switch (style) {
		case YEAR:
			xValues = new ValueWidthName[size];
			temp.set(Calendar.MONTH, temp.get(Calendar.MONTH) + 2);
			for (int i = 0; i < xValues.length; i++) {

				xValues[i] = new ValueWidthName(TimeUtil.getDateString(getContext(),
						temp.getTimeInMillis(), TimeUtil.dateFormat12),
						temp.getTimeInMillis());
				temp.set(Calendar.MONTH, temp.get(Calendar.MONTH) + 2);
			}
			break;
		case HALF_YEAR:
			xValues = new ValueWidthName[size];
			 temp.set(Calendar.MONTH, temp.get(Calendar.MONTH) + 1);
			for (int i = 0; i < xValues.length; i++) {

				xValues[i] = new ValueWidthName(TimeUtil.getDateString(getContext(),
						temp.getTimeInMillis(), TimeUtil.dateFormat12),
						temp.getTimeInMillis());
				temp.set(Calendar.MONTH, temp.get(Calendar.MONTH) + 1);
			}

			break;
		case MONTH:
			size = 5;
			xValues = new ValueWidthName[size];
			temp.set(Calendar.WEEK_OF_MONTH, temp.get(Calendar.WEEK_OF_MONTH));
			for (int i = 0; i < xValues.length; i++) {

				xValues[i] = new ValueWidthName(TimeUtil.getDateString(getContext(),
						temp.getTimeInMillis(), TimeUtil.dateFormat14),
						temp.getTimeInMillis());
				temp.set(Calendar.WEEK_OF_MONTH,
						temp.get(Calendar.WEEK_OF_MONTH) + 1);
			}

			break;
		case WEEK:
			size = 7;
			xValues = new ValueWidthName[size];
			temp.set(Calendar.DAY_OF_MONTH, temp.get(Calendar.DAY_OF_MONTH));
			for (int i = 0; i < xValues.length; i++) {

				xValues[i] = new ValueWidthName(TimeUtil.getDateString(getContext(),
						temp.getTimeInMillis(), TimeUtil.dateFormat14),
						temp.getTimeInMillis());
				temp.set(Calendar.DAY_OF_MONTH,
						temp.get(Calendar.DAY_OF_MONTH) + 1);
			}

			break;
		case DAY:
			size = 1;
			xValues = new ValueWidthName[size];
			temp.set(Calendar.DAY_OF_MONTH, temp.get(Calendar.DAY_OF_MONTH));
			for (int i = 0; i < xValues.length; i++) {

				xValues[i] = new ValueWidthName(TimeUtil.getDateString(getContext(),
						temp.getTimeInMillis(), TimeUtil.dateFormat14),
						temp.getTimeInMillis());
				temp.set(Calendar.DAY_OF_MONTH,
						temp.get(Calendar.DAY_OF_MONTH) + 1);
			}

			break;
		default:
			break;
		}
	}

	public static RectLD getTimeStyleArea(int style, RectLD r) {
		if (r == null) {
			r = new RectLD();
		}
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.set(Calendar.HOUR_OF_DAY, 23);
		fromCalendar.set(Calendar.MINUTE, 59);
		fromCalendar.set(Calendar.SECOND, 59);
		r.right = fromCalendar.getTimeInMillis();
		switch (style) {
		case YEAR:
			fromCalendar.set(Calendar.MONTH,
					fromCalendar.get(Calendar.MONTH) - 12);
			fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
			fromCalendar.set(Calendar.MINUTE, 0);
			fromCalendar.set(Calendar.SECOND, 0);
			break;
		case HALF_YEAR:
			fromCalendar.set(Calendar.MONTH,
					fromCalendar.get(Calendar.MONTH) - 6);
			fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
			fromCalendar.set(Calendar.MINUTE, 0);
			fromCalendar.set(Calendar.SECOND, 0);
			break;
		case MONTH:
			fromCalendar.set(Calendar.MONTH,
					fromCalendar.get(Calendar.MONTH) - 1);

			fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
			fromCalendar.set(Calendar.MINUTE, 0);
			fromCalendar.set(Calendar.SECOND, 0);

			break;
		case WEEK:
			fromCalendar.set(Calendar.DAY_OF_MONTH,
					fromCalendar.get(Calendar.DAY_OF_MONTH) - 6);

			fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
			fromCalendar.set(Calendar.MINUTE, 0);
			fromCalendar.set(Calendar.SECOND, 0);

			break;
		case DAY:
			fromCalendar.set(Calendar.DAY_OF_MONTH,
					fromCalendar.get(Calendar.DAY_OF_MONTH));

			fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
			fromCalendar.set(Calendar.MINUTE, 0);
			fromCalendar.set(Calendar.SECOND, 0);

			break;
		default:
			break;
		}
		r.left = fromCalendar.getTimeInMillis();
		Log.d(TAG,
				"Time from1:"
						+ TimeUtil.getDateString(r.left, TimeUtil.dateFormat2)
						+ " to:"
						+ TimeUtil.getDateString(r.right, TimeUtil.dateFormat2));
		return r;
	}

	public static int parseDays(long from, long to) {
		return (int) ((to - from + TimeUtil.ONE_DAY) / TimeUtil.ONE_DAY);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (currentChartModelMapTools == null
				|| currentChartModelMapTools.size() <= 0) {
			return;
		}

		for (int i = currentChartModelMapTools.size() - 1; i >= 0; i--) {
			if (!isNew) {
				Path path = currentChartModelMapTools.get(i).path;
				if (path != null) {
					pathPaint.setColor(currentChartModelMapTools.get(i).color);

					canvas.drawPath(path, pathPaint);
				}
			} else {
				if (paths != null && i < paths.length) {
					Path path = paths[i];
					if (path != null) {
						pathPaint
								.setColor(currentChartModelMapTools.get(i).color);
						canvas.drawPath(path, pathPaint);
					}
				}
			}

		}
		if (showStatus == show_1) {
			drawSelect(x0, canvas);
			List<NodeTextItem> list = drawNodes(x0, canvas);
			drawNodeText(list, canvas);
		} else if (showStatus == show_2) {
			drawSelect(x0, canvas);
			drawSelect(x1, canvas);
			drawArea(x0, x1, canvas);
			

			List<NodeTextItem> list = drawNodes(x0, canvas);
			drawNodeText(list, canvas);

			List<NodeTextItem> list1 = drawNodes(x1, canvas);
			drawNodeText(list1, canvas);
/*
			float minX = Math.min(x1, x0);
			float maxX = Math.max(x1, x0);

			List<NodeTextItem> minList = drawNodes(minX, canvas);
			List<NodeTextItem> maxList = drawNodes(maxX, canvas);

			List<NodeTextItem2> nodeTextItem2s = new ArrayList<ChartView.NodeTextItem2>();
			for (int i = 0; i < maxList.size(); i++) {

				NodeTextItem minNodeTextItem = minList.get(i);
				NodeTextItem maxNodeTextItem = maxList.get(i);

				NodeTextItem2 nodeTextItem = new NodeTextItem2();
				if (i == 0) {
					nodeTextItem.finalTag = "FINAL TOTAL:";
					nodeTextItem.initialTag = "INITIAL TOTAL:";
				} else {
					nodeTextItem.finalTag = "FINAL:";
					nodeTextItem.initialTag = "INITIAL:";
				}
				nodeTextItem.x = maxNodeTextItem.x;
				nodeTextItem.y = maxNodeTextItem.y;
				nodeTextItem.from = minNodeTextItem.chartModel;
				nodeTextItem.to = maxNodeTextItem.chartModel;
				nodeTextItem.chartView = this;
				nodeTextItem2s.add(nodeTextItem);

			}

			drawNodeText2(nodeTextItem2s, canvas);
			*/
		}

	}

	private void drawNodeText(List<NodeTextItem> list, Canvas canvas) {
		Collections.sort(list);

		for (int i = 0; i < list.size(); i++) {
			NodeTextItem nodeTextItem = list.get(i);

			float parentY = nodeTextItem.draw(getContext(),canvas,i);
			if (i + 1 < list.size()) {
				NodeTextItem nextNodeTextItem = list.get(i + 1);
				nextNodeTextItem.parentY = parentY;
			}

		}
	}

	private void drawNodeText2(List<NodeTextItem2> list, Canvas canvas) {
		Collections.sort(list);

		for (int i = 0; i < list.size(); i++) {
			NodeTextItem2 nodeTextItem2 = list.get(i);

			float parentY = nodeTextItem2.draw(canvas);
			if (i + 1 < list.size()) {
				NodeTextItem nextNodeTextItem = list.get(i + 1);
				nextNodeTextItem.parentY = parentY;
			}

		}
	}

	private float getSaftyX(float beginX) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) getContext()
				.getResources().getDrawable(R.drawable.node);

		float end = o_x + xAxisLength - bitmapDrawable.getBitmap().getWidth()
				/ 2;
		float x = beginX;
		float start = o_x + bitmapDrawable.getBitmap().getWidth() / 2;
		if (x < start) {
			x = start;
		} else if (beginX > end) {
			x = end;
		}
		return x;
	}

	/**
	 * @param x0
	 * @param x1
	 * @param canvas
	 */
	private void drawArea(float x0, float x1, Canvas canvas) {

		Paint paint = new Paint();
		paint.setColor(Color.argb(50, 255, 255, 255));
		int top = 0;
		int bottom = (int) o_y;
		int left = (int) getSaftyX(x0);
		int right = (int) getSaftyX(x1);
		Rect r = new Rect(left, top, right, bottom);
		canvas.drawRect(r, paint);
	}

	/**
	 * 画节点，同时返回文字列表
	 * 
	 * @param beginX
	 * @param canvas
	 * @return
	 */
	private List<NodeTextItem> drawNodes(float beginX, Canvas canvas) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) getContext()
				.getResources().getDrawable(R.drawable.node);

		float end = o_x + xAxisLength - bitmapDrawable.getBitmap().getWidth()
				/ 2;
		float x = beginX;
		float start = o_x + bitmapDrawable.getBitmap().getWidth() / 2;
		if (x < start) {
			x = start;
		} else if (beginX > end) {
			x = end;
		}

		//
		// to-from time-from
		// --------- = ------------
		// xAxisLength beginX-o_x
		String fromStr = TimeUtil.getDateString(dataArea.left,
				TimeUtil.dateFormat2);
		String toStr = TimeUtil.getDateString(dataArea.right,
				TimeUtil.dateFormat2);

		LogManager.d("from" + fromStr);
		LogManager.d("to" + toStr);
		// long time = (long)((x - o_x) * (to - from) / xAxisLength + from);
		int index = (int) ((beginX - o_x) * (e - s) / xAxisLength);
		if (index < 0) {
			index = 0;
		}
		index += s;
		// String key = TimeUtil.getDateString(time, TimeUtil.dateFormat4);
		List<NodeTextItem> nodeTextItems = new ArrayList<ChartView.NodeTextItem>();
		double totalin = 0;
		double totalfinal = 0;

		int totalLeft = (int) x - bitmapDrawable.getBitmap().getWidth() / 2;
		int totalRight = totalLeft + bitmapDrawable.getBitmap().getWidth();
		NodeTextItem totalNodeTextItem = new NodeTextItem();
		totalNodeTextItem.x = totalRight;
		totalNodeTextItem.y = y_end;
		totalNodeTextItem.chartView = this;
		nodeTextItems.add(totalNodeTextItem);
		for (int i = 0; i < currentChartModelMapTools.size(); i++) {

			ChartModelMapTool chartModelMapTool = currentChartModelMapTools
					.get(i);
			if (chartModelMapTool == null
					|| chartModelMapTool.getYearList() == null) {
				continue;
			}

			// to-from time-from
			// --------- = ------------
			// yAxisLength beginX-o_x

			// value-yvalues[0] yvalues[1]-yvalues[0]
			// -------------=---------
			// y-o_y cellLengthY
			ChartModel chartModel = null;
			if (index < chartModelMapTool.getYearList().size()) {
				chartModel = chartModelMapTool.getYearList().get(index);
			} else {
				chartModel = chartModelMapTool.getYearList().get(
						chartModelMapTool.getYearList().size() - 1);
			}
			if (chartModel != null && yValues[0] != null) {
				//value is zero not shown
				if(chartModel.getValue()==0){
					continue;
				}

				// float yy = (float)(o_y - (chartModel.getAfterAdded() -
				// yValues[0].value)
				// * cellLengthY);

				float yy = (float) (o_y - (chartModel.getAfterAdded() - yValues[0].value)
						* cellLengthY);

				totalfinal = totalfinal + chartModel.getValue();
				// canvas.drawCircle(beginX, yy, 2, new Paint());

				int left = (int) x - bitmapDrawable.getBitmap().getWidth() / 2;
				int top = (int) yy - bitmapDrawable.getBitmap().getHeight() / 2;
				int right = left + bitmapDrawable.getBitmap().getWidth();
				int bottom = top + bitmapDrawable.getBitmap().getHeight();
				Rect rect = new Rect(left, top, right, bottom);
				NodeTextItem nodeTextItem = new NodeTextItem();
				nodeTextItem.x = right;
				nodeTextItem.y = (bottom + top) / 2;
				nodeTextItem.chartView = this;
				// nodeTextItem.text = TimeUtil.getDateString(time,
				// TimeUtil.dateFormat2);
				// nodeTextItem.text = Utils.generateFormatMoney("$",
				// chartModel.getValue());
				nodeTextItem.chartModel = chartModel;

				nodeTextItems.add(nodeTextItem);
				bitmapDrawable.setBounds(rect);
				bitmapDrawable.draw(canvas);
			}

		}

		totalNodeTextItem.chartModel = new ChartModel();
		totalNodeTextItem.chartModel.setValue(totalfinal);

		return nodeTextItems;
	}

	float selectedWidth = 0;

	float selectedHeight = 0;

	/**
	 * draw selected bar
	 * 
	 * @param beginX
	 * @param canvas
	 */
	private void drawSelect(float beginX, Canvas canvas) {
		//timescale_bar
		BitmapDrawable drawable = (BitmapDrawable) getContext().getResources()
				.getDrawable(R.drawable.timescale_select);

		float end = o_x + xAxisLength - drawable.getBitmap().getWidth() / 2;
		float x = beginX;
		float start = o_x + drawable.getBitmap().getWidth() / 2;
		if (x < start) {
			x = start;
		} else if (beginX > end) {
			x = end;
		}
		int left = (int) x - drawable.getBitmap().getWidth() / 2;
		int right = left + drawable.getBitmap().getWidth();
		Rect rect = new Rect(left, (int) 0, right, (int) o_y);
		selectedWidth = right - left;
		selectedHeight = o_y;
		drawable.setBounds(rect);

		drawable.draw(canvas);
	}

	float down_x0;

	float down_y0;

	private int showStatusBeforeDown = show_0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		x0 = event.getX(0);
		y0 = event.getY(0);

		if (event.getPointerCount() == 2) {

			x1 = event.getX(1);
			y1 = event.getY(1);

		}
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			LogManager.d("Touch-ACTION_DOWN");
			mTouchState = DOWN_1;
			showStatusBeforeDown = showStatus;
			if (showStatus == show_1 || showStatus == show_2) {

			} else if (showStatus == show_0) {

				down_x0 = x0;
				down_y0 = y0;
				showStatus = show_1;
				invalidate();
			}
			break;
		case MotionEvent.ACTION_POINTER_2_DOWN: {
			LogManager.d("Touch-ACTION_POINTER_2_DOWN");
			mTouchState = DOWN_2;
			showStatus = show_2;
			invalidate();

			break;
		}
		case MotionEvent.ACTION_MOVE:

			// 启动滚动
			if (mTouchState == DOWN_1) {
				float distance = Math.abs(down_x0 - x0);
				// 滚动
				if (distance > viewConfiguration.getScaledTouchSlop()) {

					mTouchState = MOVE_1;
					showStatus = show_1;
					invalidate();
				}
			} else if (mTouchState == DOWN_2) {

				mTouchState = MOVE_2;
				showStatus = show_2;
				invalidate();
			} else {
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
			LogManager.d("Touch-只有一个的时候放手");
			// 如果为按下状态就代表是onclick
			if (mTouchState == DOWN_1) {

				if (showStatusBeforeDown == show_1
						|| showStatusBeforeDown == show_2) {
					showStatus = show_0;
					invalidate();
				} else if (showStatusBeforeDown == show_0) {
					showStatus = show_1;
					invalidate();
				}

			} else {

			}
			mTouchState = DEFAULT_STATUS;
			break;
		case MotionEvent.ACTION_CANCEL:
			// 如果为按下状态就代表是onclick
			if (mTouchState == DOWN_1) {

				if (showStatus == show_1) {
					showStatus = show_0;
					invalidate();
				}
				mTouchState = DEFAULT_STATUS;
			}
			break;
		case MotionEvent.ACTION_POINTER_1_UP: {

			LogManager.d("Touch-先放第一个");

			x1 = x0;
			y1 = y0;
			break;
		}
		case MotionEvent.ACTION_POINTER_2_UP: {
			LogManager.d("Touch-先放第二个");
			break;
		}

		}
		return true;
	}

	/**
	 * 创建Path
	 */
	private void createChartPath(int s, int e,
			List<ChartModelMapTool> currentChartModelMapTools) {
		Log.d(TAG, "createChartPath");
		int yNum;
		if (currentChartModelMapTools == null
				|| currentChartModelMapTools.size() < 0) {
			Log.d(TAG, "currentChartModelMapTools null");
			return;
		}
		if (yValues == null || yValues[0] == null
				|| yValues[yValues.length - 1] == null) {
			return;
		}
		yNum = currentChartModelMapTools.size();
		paths = new Path[yNum];

		double y_area = yValues[yValues.length - 1].value - yValues[0].value;
		cellLengthX = xAxisLength
				/ (float) (dataArea.width() - TimeUtil.ONE_DAY);
		cellLengthY = Math.abs(yAxisLength / (y_area));
		float y_d = (float) (cellLengthY * (0d - yValues[0].value));
		float oo_y = o_y - y_d;

		float xp = o_x, yp = oo_y, yp2 = oo_y;
		for (int i = s; i < e; i++) {
			// if (yValuesStyle == 0) {
			// yp=o_y - yAxisLength / 2;
			// } else if (yValuesStyle == 1) {
			// yp=o_y;
			// } else if (yValuesStyle == -1) {
			// yp=y_end;
			// }
			yp = oo_y;
			yp2 = oo_y;
			float v = 0;
			for (int j = 0; j < yNum; j++) {
				if (paths[j] == null) {
					paths[j] = new Path();
				}
				{
					ChartModelMapTool chartModelMapTool = currentChartModelMapTools
							.get(j);
					if (chartModelMapTool != null) {
						List<ChartModel> ls = chartModelMapTool.getYearList();
						ChartModel mChartModel = null;// chartModelMapTool.xyValueList[i];
						if (ls != null && i < ls.size()) {
							mChartModel = ls.get(i);
						}
						if (mChartModel != null) {
							xp = (float) (o_x + (mChartModel.getTimeMills() - dataArea.left)
									* cellLengthX);
							// if(mChartModel.getValue()<0){
							// yp-=mChartModel.getValue();
							// v=yp;
							// }else{
							// yp2+=mChartModel.getValue();
							// v=yp2;
							// }
							//
							// Log.d(TAG,j+" "+i+" paths "+mChartModel.toString());
							// v=y_end+(float)(cellLengthY*(mChartModel.getAfterAdded()-yValues[yValues.length
							// - 1].value));
							v = o_y
									- (float) (cellLengthY * (mChartModel
											.getAfterAdded() - yValues[0].value));
						}
					}

					if (i == s) {
						xp = o_x;
						paths[j].moveTo(xp, oo_y);
					}
					paths[j].lineTo(xp, v);
					// paths2[j].lineTo(xp, yp2);
					Log.d(TAG, j + " " + i + " paths " + xp + " " + yp);
				}
				if (i == e - 1) {
					if (e - s == 1) {
						xp = o_x + xAxisLength;
						paths[j].lineTo(xp, v);
					}
					paths[j].lineTo(xp, oo_y);
					paths[j].close();
				}
			}
		}
	}

	@Deprecated
	private void printValues(ValueWidthName[] valueWidthNames) {
		if (valueWidthNames == null) {
			LogManager.d("xValues=null");
			return;
		}
		for (ValueWidthName valueWidthName : valueWidthNames) {
			LogManager.d(valueWidthName.getValue() + ""
					+ valueWidthName.getName());
		}
	}

	@Deprecated
	private RectLD calcDataArea(RectLD rectLD) {
		if (rectLD == null) {
			rectLD = new RectLD();
		}
		rectLD.top = Double.MAX_VALUE;
		rectLD.bottom = Double.MIN_VALUE;
		for (int j = 0; j < currentChartModelMapTools.size(); j++) {
			List<ChartModel> models = currentChartModelMapTools.get(j)
					.getYearList();
			if (models == null) {
				continue;
			}
			for (int i = 0; i < models.size(); i++) {
				rectLD.top = Math
						.min(models.get(i).getAfterAdded(), rectLD.top);
				rectLD.bottom = Math.max(models.get(i).getAfterAdded(),
						rectLD.bottom);
			}
		}
		return rectLD;
	}

	@Deprecated
	private void generateYValues(double min, double max) {
		/*
		 * if (min >= 0) {
		 * 
		 * yValuesStyle = 1; yValues = new ValueWidthName[NO_SIGN_SIZE]; double
		 * cell = getCell(max, NO_SIGN_SIZE); // 计算Y轴上的数 for (int i = 0; i <
		 * NO_SIGN_SIZE; i++) { String value = Utils.formatMoney(i * cell, "",
		 * false, true, false, false, true); yValues[i] = new
		 * ValueWidthName(value, (i * cell)); }
		 * 
		 * } else if (min < 0 && max > 0) { yValuesStyle = 0; yValues = new
		 * ValueWidthName[SIGN_SIZE]; double mm = Math.max(Math.abs(min),
		 * Math.abs(max)); double cell = getCell(mm, SIGN_SIZE / 2); for (int i
		 * = -SIGN_SIZE / 2; i <= SIGN_SIZE / 2; i++) { String value =
		 * Utils.formatMoney(i * cell, "", false, true, false, false, true);
		 * yValues[i + SIGN_SIZE / 2] = new ValueWidthName(value, (i * cell)); }
		 * 
		 * } else if (max <= 0) { yValuesStyle = -1; yValues = new
		 * ValueWidthName[NO_SIGN_SIZE]; double mm = Math.abs(min); double cell
		 * = getCell(mm, NO_SIGN_SIZE);
		 * 
		 * for (int i = -NO_SIGN_SIZE+1; i <= 0; i++) { String value =
		 * Utils.formatMoney(i * cell, "", false, true, false, false, true);
		 * yValues[i + NO_SIGN_SIZE] = new ValueWidthName(value, (i * cell)); }
		 * 
		 * }
		 */
//		min = Math.min(min, 0);
//		max = Math.max(max, 0);
//		double values[] = Number_scale.getVales(min, max, NO_SIGN_SIZE << 1,
//				0.01);
//		yValues = new ValueWidthName[values.length];
//		for (int i = 0; i < values.length; i++) {
//			String value = Utils.formatMoney(values[i], "", false, true, false,
//					false, true);
//			yValues[i] = new ValueWidthName(value, values[i]);
//		}
//
//		Log.d(TAG, "double area from:" + min + " ----" + max);
//		initOXY();
		// printValues(yValues);

	}

	/**
	 * generate Y values
	 * 
	 * @param min
	 * @param max
	 */
	private void initYValues(double min, double max) {

		min = Math.min(min, 0);
		max = Math.max(max, 0);
		double values[] = Number_scale.getValesWithZero(min, max, NO_SIGN_SIZE << 1,
				0.01);
		yValues = new ValueWidthName[values.length];
		for (int i = 0; i < yValues.length; i++) {
			String value = Utils.formatMoney(values[i], Contants.COUNTRY, false, true, false,
					false, true);
			yValues[i] = new ValueWidthName(value, values[i]);
		}

		Log.d(TAG, "double area from:" + min + " ----" + max);
		initOXY();

	}

	/**
	 * 拿到每个y轴的涨幅
	 * 
	 * @param max
	 * @param size
	 * @return
	 */
	@Deprecated
	private double getCell(double max, int size) {
		max = Math.ceil(max);

		String str = String.valueOf((int) max);
		int aa = Integer.valueOf(str);
		int bb = (int) (aa / Math.pow(10, str.length() - 1)) + 1;
		int result = (int) (bb * Math.pow(10, str.length() - 1));

		double cell = result / (double) size;
		return cell;
	}

	/**
	 * 设置显示风格
	 * 
	 * @param style
	 */
	public void setTimeStyle(int s, int e, int style) {
		if (this.s == s && this.e == e && currentStyle == style) {
			return;
		}
		this.s = s;
		this.e = e;
		currentStyle = style;
		// if(style==currentStyle){
		// return;
		// }
		// currentStyle=style;
		setShowState(show_0);
		refresh();
	}

	public int getTimeStyle() {
		return currentStyle;
	}

	/**
	 * @see #show_0
	 * @see #show_1
	 * @see #show_2
	 * @param state
	 */
	public void setShowState(int state) {
		showStatus = state;
	}

	/**
	 * @param list
	 *            source of data for draw
	 */
	public void setDatas(List<ChartModelMapTool> list) {
		currentChartModelMapTools = list;
	}

	public static class NodeTextItem implements Comparable<NodeTextItem> {

		public ChartModel chartModel;

		boolean isRight;

		float x;

		float y;

		float parentY = -1;

		Paint paint;

		String text;

		ChartView chartView;

		public NodeTextItem() {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setTextSize(18);

		}

		public float draw(Context context,Canvas canvas,int position) {

			FontMetrics fontMetrics = paint.getFontMetrics();
			isRight = true;
			text = Utils.formatMoney(chartModel.getValue(), "$", true, true,
					false, false, true);

			if(position==0){
				text=context.getString(R.string.horizontal_chart_total, text);
			}
			float length = paint.measureText(text);
			paint.setTextAlign(Align.LEFT);
			float x0 = x;
			float y0 = y + fontMetrics.descent;
			if (x + length > chartView.x_end) {

				isRight = false;
				paint.setTextAlign(Align.RIGHT);
				x0 = x0 - chartView.selectedWidth;
			}
			if (parentY >= 0) {
				if (y0 + fontMetrics.ascent < parentY + fontMetrics.descent) {
					y0 = parentY + fontMetrics.descent - fontMetrics.ascent;
				}
			}
			// RectF rectF = new RectF(left, top, right, bottom);
			canvas.drawText(text, x0, y0, paint);

			return y0;
		}

		@Override
		public int compareTo(NodeTextItem another) {

			if (another == null) {
				return -1;
			}
			if (y < another.y) {
				return -1;
			} else if (y == another.y) {
				return 0;
			} else if (y > another.y) {
				return 1;
			}
			return 0;
		}
	}

	public static class NodeTextItem2 extends NodeTextItem {

		public String initialTag;

		public String finalTag;

		ChartModel from;

		ChartModel to;

		public float draw(Canvas canvas) {

			FontMetrics fontMetrics = paint.getFontMetrics();
			isRight = true;
			String fromText = initialTag
					+ Utils.formatMoney(from.getValue(), "$", true, true,
							false, false, true);

			float fromLength = paint.measureText(fromText);

			String toText = finalTag
					+ Utils.formatMoney(to.getValue(), "$", true, true, false,
							false, true);

			if (finalTag.equals("FINAL TOTAL:")) {
				double present = 100 * (to.getValue() - from.getValue())
						/ to.getValue();

				toText = toText + Utils.generateFormatMoney("", present) + "%";
			}
			float toLength = paint.measureText(toText);
			float length = Math.max(fromLength, toLength);

			paint.setTextAlign(Align.LEFT);
			float x0 = x;
			float y0 = y;
			if (x + length > chartView.x_end) {

				isRight = false;
				paint.setTextAlign(Align.RIGHT);
				x0 = x0 - chartView.selectedWidth;
			}
			if (parentY >= 0) {
				if (y0 + fontMetrics.ascent < parentY + fontMetrics.descent) {
					y0 = parentY + fontMetrics.descent - fontMetrics.ascent;
				}
			}
			canvas.drawText(fromText, x0, y0, paint);

			y0 = y0 + fontMetrics.descent - fontMetrics.ascent;
			canvas.drawText(toText, x0, y0, paint);
			return y0;
		}
	}
}
