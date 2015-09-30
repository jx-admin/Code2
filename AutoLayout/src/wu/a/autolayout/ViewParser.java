package wu.a.autolayout;

import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import org.xmlpull.v1.XmlPullParser;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public abstract class ViewParser<T extends View> implements XmlParser<T> {
	static final String LAYOUT_MARGIN_TOP = "layout_marginTop";
	static final String LAYOUT_MARGIN_LEFT = "layout_marginLeft";
	static final String PX = "px";
	static final String DP = "dp";
	static final String FILL_PARENT = "fill_parent";
	static final String MATCH_PARENT = "match_parent";
	static final String WRAP_CONTENT = "wrap_content";
	static final String REGULAR_EXPRESSION = "/";
	static final String PREFIX_AT = "@";
	static final String PREFIX_NUM = "#";
	public static final String PREFIX_DRAWABLE="@drawable/";

	private static final String ATT_PADDING = "padding";
	static final String ATT_CLICKABLE = "clickable";
	static final String ATT_BACKGROUND = "background";
	static final String ATT_LAYOUT_Y = "layout_y";
	static final String ATT_LAYOUT_X = "layout_x";
	static final String ATT_LAYOUT_MARGIN = "layout_margin";
	static final String ATT_LAYOUT_HEIGHT = "layout_height";
	static final String ATT_LAYOUT_WIDTH = "layout_width";
	public static final String ATT_SRC="src";
	public static final String ATT_TEXT="text";
	public static final String ATT_ID="id";
	
	T view;
	FrameLayout.LayoutParams lp;

	public ViewParser(T view) {
		this.view = view;
		lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
	}

	/** (non-Javadoc)
	 * @see wu.a.autolayout.XmlParser#parse(java.io.InputStream)
	 */
	@Override
	public T parse(InputStream is) throws Exception {
		return null;
	}

	@Override
	public String serialize() throws Exception {
		return null;
	}

	/**
	 * <pre>
	 * 属性解析
	 * @param parser
	 * @return
	 * </pre>
	 */
	public T parse(XmlPullParser parser) {
		for (int i = 0, e = parser.getAttributeCount(); i < e; i++) {
			String an = parser.getAttributeName(i);
			String av = parser.getAttributeValue(i);
			Log.d("ddd", i + " att:" + an + "=" + av);
			parse(an, av);
		}
		return view;
	}

	/**
	 * <pre>
	 * 属性解析
	 * @param an
	 * @param av
	 * </pre>
	 */
	void parse(String an, String av) {
		if (ATT_ID.equals(an)) {
			String[] strs = av.substring(1).split(REGULAR_EXPRESSION);
			int id = view.getResources().getIdentifier(strs[1], strs[0],view.getContext().getPackageName());
			 view.setId(id);
		} else if (ATT_LAYOUT_WIDTH.equals(an)) {
			view.getLayoutParams().width = getPx(av);
		} else if (ATT_LAYOUT_HEIGHT.equals(an)) {
			view.getLayoutParams().height = getPx(av);
		} else if (ATT_LAYOUT_MARGIN.equals(an)) {
			int px=getPx(av);
			 lp.setMargins(px,px,px,px);
		} else if (LAYOUT_MARGIN_LEFT.equals(an)) {
			lp.leftMargin=getPx(av);
		} else if (LAYOUT_MARGIN_TOP.equals(an)) {
			lp.topMargin=getPx(av);
		} else if (ATT_PADDING.equals(an)) {
			int px=getPx(av);
			view.setPadding(px, px, px, px);
		} else if ("visibility".equals(an)) {
			if("gone".equals(av)){
				
			}else if("invisible".equals(av)){
				
			}else{
				
			}
			int px=getPx(av);
			view.setPadding(px, px, px, px);
		}else if (ATT_BACKGROUND.equals(an)) {
			if (av != null && av.startsWith(PREFIX_NUM)) {
				view.setBackgroundColor((int)Long.parseLong(av.substring(1), 16));
//				StateListDrawable sld=new StateListDrawable();
//				sld.addState(new int []{android.R.attr.state_pressed , android.R.attr.state_window_focused}, new ColorDrawable(0xff0000ff));
//				sld.addState(new int []{android.R.attr.state_window_focused}, new ColorDrawable(0xff00ff00));
//				view.setBackgroundDrawable(sld);
			} else if (av.startsWith(PREFIX_AT)) {
				String[] strs = av.substring(1).split(REGULAR_EXPRESSION);
				int id = view.getResources().getIdentifier(strs[1], strs[0],view.getContext().getPackageName());
				view.setBackgroundResource(id);
			}
		} else if(ATT_CLICKABLE.equals(an)){
			view.setClickable(Boolean.parseBoolean(av));
		}
	}

	/**
	 * <pre>
	 * Android 硬件运行环境单位转换
	 * @param av
	 * @return
	 * </pre>
	 */
	public int getPx(String av) {
		int px = 0;
		if (WRAP_CONTENT.equals(av)) {
			px = LayoutParams.WRAP_CONTENT;
		} else if (MATCH_PARENT.equals(av) || FILL_PARENT.equals(av)) {
			px = LayoutParams.MATCH_PARENT;
		} else if (av != null && av.endsWith(DP)) {
			int dp = Integer.parseInt(av.substring(0, av.length() - 2));
			px = (int) (getDisPlayMerics().density * dp);
		} else if (av != null && av.endsWith(PX)) {
			px = Integer.parseInt(av);
		}
		return px;
	}

	WeakReference <DisplayMetrics>disPlayMerics;
	public DisplayMetrics getDisPlayMerics() {
		if(disPlayMerics==null||disPlayMerics.get()==null){
			disPlayMerics=new WeakReference<DisplayMetrics>(view.getResources().getDisplayMetrics());
		}
		return disPlayMerics.get();
	}
}
