package com.aess.aemm.view;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.widget.TextView;

public class EllipsizingTextView extends TextView {

	private static final String ELLIPSIS = "...";

	public interface EllipsizeListener {
		void ellipsizeStateChanged(boolean ellipsized);
	}

	private final List<EllipsizeListener> ellipsizeListeners = new ArrayList<EllipsizeListener>();

	private boolean isEllipsized=true;
	private boolean isStale;
	private boolean programmaticChange;
	private String fullText;
	private int maxLinesss;
	private float lineSpacingMultiplier = 1.0f;
	private float lineAdditionalVerticalPadding = 0.0f;

	public EllipsizingTextView(Context context) {
		super(context);
	}

	public EllipsizingTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EllipsizingTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void addEllipsizeListener(EllipsizeListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		ellipsizeListeners.add(listener);
	}

	public void removeEllipsizeListener(EllipsizeListener listener) {
		ellipsizeListeners.remove(listener);
	}

	public boolean isEllipsized() {
		return isEllipsized;
	}

	@Override
	public void setMaxLines(int maxLines) {
		super.setMaxLines(maxLines);
		maxLinesss = maxLines;
		isStale = true;
	}

	public int getMaxLines() {
		return maxLinesss;
	}

	@Override
	public void setLineSpacing(float add, float mult) {
		this.lineAdditionalVerticalPadding = add;
		this.lineSpacingMultiplier = mult;
		super.setLineSpacing(add, mult);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int before,
			int after) {
		super.onTextChanged(text, start, before, after);
		if (!programmaticChange) {
			fullText = text.toString();
			isStale = true;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isStale) {
			super.setEllipsize(null);
			resetText();
		}
		super.onDraw(canvas);
	}

	private void resetText() {
//		int maxLines = getMaxLines();
		String workingText = fullText;
		boolean ellipsized = false;
		if (maxLinesss >0) {
			Layout layout = createWorkingLayout(workingText);
			if (layout.getLineCount() > maxLinesss) {

				System.out.println(layout.getLineCount() + "\t" + maxLinesss);
				int end=layout.getLineEnd(maxLinesss - 1)-1;
				String last=fullText.substring(end,end+1);
				
				if(getPaint().measureText(last)<getPaint().measureText(ELLIPSIS)){
					end--;
				}
				workingText = fullText.substring(0,
						end).trim();
				Layout layout2 = createWorkingLayout(workingText + ELLIPSIS);
				while (layout2.getLineCount() > maxLinesss) {
					System.out.println(layout2.getLineCount() + "\t" + maxLinesss);
					int lastSpace = workingText.lastIndexOf(' ');
					System.out.println(lastSpace);
					if (lastSpace == -1) {
						break;
					}
					workingText = workingText.substring(0, lastSpace);
				}
				workingText = workingText + ELLIPSIS;
				ellipsized = true;
			}
		}
		if (!workingText.equals(getText())) {
			programmaticChange = true;
			try {
				setText(workingText);
			} finally {
				programmaticChange = false;
			}
		}
		isStale = false;
		if (ellipsized != isEllipsized) {
			isEllipsized = ellipsized;
			for (EllipsizeListener listener : ellipsizeListeners) {
				listener.ellipsizeStateChanged(ellipsized);
			}
		}
	}

	private Layout createWorkingLayout(String workingText) {
		return new StaticLayout(workingText, getPaint(), getWidth()
				- getPaddingLeft() - getPaddingRight()+1, Alignment.ALIGN_NORMAL,
				lineSpacingMultiplier, lineAdditionalVerticalPadding, false);
	}

}
