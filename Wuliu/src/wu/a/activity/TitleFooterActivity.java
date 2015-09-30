package wu.a.activity;

import wu.a.wuliu.BookActivity;
import wu.a.wuliu.BookManagerActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import baidumapsdk.demo.R;

/**
 * <pre>
 * 带有标题栏和菜单栏的Activity基类
 * 标题栏结构：左边按钮、标题、右边按钮
 * 底部菜单：
 * 内容容器：设置方法 {@link #setContentLayout(int)} or {@link #setContentLayout(View)}
 * </pre>
 * 
 * @author junxu.wang
 *
 */
public class TitleFooterActivity extends BaseActivity {
	private View toppanel;
	private ImageView title_left;
	private ImageView title_right;
	private TextView title_right_text;
	private TextView title_left_text;
	private TextView title_text;
	private ImageView title_img;
	private FrameLayout content_view;
	protected LayoutInflater lf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lf = LayoutInflater.from(this);
		setContentView(R.layout.activity_page_content);
		toppanel=findViewById(R.id.toppanel);
		title_left = (ImageView) findViewById(R.id.title_left);
		title_right = (ImageView) findViewById(R.id.title_right);
		title_right_text = (TextView) findViewById(R.id.title_right_text);
		title_left_text = (TextView) findViewById(R.id.title_left_text);
		title_text = (TextView) findViewById(R.id.title_text);
		title_img = (ImageView) findViewById(R.id.title_img);
		content_view = (FrameLayout) findViewById(R.id.content_view);
	}
	
	public void setToppanel(int visibility){
		toppanel.setVisibility(visibility);
	}

	/**
	 * <pre>
	 * 设置标题栏右侧按钮图表显示
	 * @param visibility
	 * </pre>
	 */
	public void setBottomMenu(int visibility) {
		findViewById(R.id.footer_include).setVisibility(visibility);
	}

	/**
	 * <pre>
	 * 设置标题栏左侧图表显示
	 * @param visibility
	 * </pre>
	 */
	public void setTitleLeftButton(int visibility) {
		title_left.setVisibility(visibility);
	}

	/**
	 * <pre>
	 * 设置标题栏左侧按钮图表
	 * @param imgId
	 * </pre>
	 */
	public void setTitleLeftButtonImage(int imgId) {
		title_left.setImageResource(imgId);
		title_left.setVisibility(View.VISIBLE);
	}

	/**
	 * <pre>
	 * 设置标题栏右侧按钮图表显示
	 * @param visibility
	 * </pre>
	 */
	public void setTitleRightButton(int visibility) {
		title_right.setVisibility(visibility);
		title_right_text.setVisibility(visibility);
	}

	/**
	 * <pre>
	 * 设置标题栏右侧按钮图表
	 * @param imgRes
	 * </pre>
	 */
	public void setTitleRightButtonImage(int imgRes) {
		title_right.setImageResource(imgRes);
		title_right.setVisibility(View.VISIBLE);
	}

	public void setTitleRightButtonText(int txtRes) {
		title_right_text.setText(txtRes);
		title_right_text.setVisibility(View.VISIBLE);
	}

	public void setTitleLeftButtonText(int txtRes) {
		title_left_text.setText(txtRes);
		title_left_text.setVisibility(View.VISIBLE);
	}

	public void setTitleLeftButtonText(String txtRes) {
		title_left_text.setText(txtRes);
		title_left_text.setVisibility(View.VISIBLE);
	}

	/**
	 * <pre>
	 * 设置标栏题显示
	 * @param visibility
	 * </pre>
	 */
	public void setTitleVisibility(int visibility) {
		title_text.setVisibility(visibility);
	}

	/**
	 * <pre>
	 * 设置标栏题名称
	 * @param titleRes
	 * </pre>
	 */
	public void setTitleText(int titleRes) {
		title_text.setText(titleRes);
		title_text.setVisibility(View.VISIBLE);
		title_img.setVisibility(View.GONE);
	}
	
	public void setTitleText(int titleRes,boolean bold) {
		title_text.getPaint().setFakeBoldText(bold);
		setTitleText(titleRes);
	}

	/**
	 * <pre>
	 * 设置标栏题名称
	 * @param imgRes
	 * </pre>
	 */
	public void setTitleImage(int imgRes) {
		title_img.setImageResource(imgRes);
		title_img.setVisibility(View.VISIBLE);
		title_text.setVisibility(View.GONE);
	}

	/**
	 * <pre>
	 * 设置标栏题名称
	 * @param text
	 * </pre>
	 */
	public void setTitleText(String text) {
		title_text.setText(text);
		title_text.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置内容布局
	 * 
	 * @param view
	 *            内容布局
	 */
	public void setContentLayout(View view) {
		content_view.removeAllViews();
		content_view.addView(view);
	}

	/**
	 * 设置内容布局
	 * 
	 * @param viewId
	 *            内容布局ID
	 * @return 内容布局
	 */
	public View setContentLayout(int viewId) {
		return lf.inflate(viewId, content_view);
	}

	/**
	 * <pre>
	 * 清除正文容器子View
	 * </pre>
	 */
	public void clearContentLayout() {
		content_view.removeAllViews();
	}

	/**
	 * <pre>
	 * 正文容器
	 * @return
	 * </pre>
	 */
	public View getContentLayout() {
		return content_view;
	}

	/**
	 * <pre>
	 * 标题栏左侧按钮点击回调方法，基类为默认退出当前页面，子类可从写
	 * @param v
	 * </pre>
	 */
	public void onTitleLeftButtonClick(View v) {
		finish();
	}

	/**
	 * <pre>
	 * 标题栏右侧按钮点击事件回调方法，基类不作处理子类重写实现
	 * @param v
	 * </pre>
	 */
	public void onTitleRightButtonClick(View v) {

	}
	
	public static final int MENU_BOOK=0;
	public static final int MENU_BOOK_MANAGER=1;
	public static final int MENU_ACTIVITY=2;
	public static final int MENU_USER_INFO=3;
//	public void setMenuStatus(final Activity act, int type, final boolean needFinish){
//		TextView menu_book = (TextView)act.findViewById(R.id.menu_book);
//		TextView menu_book_manager = (TextView)act.findViewById(R.id.menu_book_manager);
//		TextView menu_activity = (TextView)act.findViewById(R.id.menu_activity);
//		TextView menu_user_info = (TextView)act.findViewById(R.id.menu_user_info);
//		
//		
//		if(MENU_BOOK==type){
//			menu_book.setBackgroundResource(R.color.menu_selected);
//			menu_book.setClickable(false);
//			menu_book.setTextColor(0xffffffff);
//		}else{
//			menu_book.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					BookActivity.start(TitleFooterActivity.this);
//				}});
//		}
//		if(MENU_BOOK_MANAGER==type){
//			menu_book_manager.setBackgroundResource(R.color.menu_selected);
//			menu_book_manager.setClickable(false);
//			menu_book_manager.setTextColor(0xffffffff);
//		}else{
//			menu_book_manager.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					BookManagerActivity.start(TitleFooterActivity.this);
//				}});
//		}
//		if(MENU_ACTIVITY==type){
//			menu_activity.setBackgroundResource(R.color.menu_selected);
//			menu_activity.setClickable(false);
//			menu_activity.setTextColor(0xffffffff);
//		}else{
//			menu_activity.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					
//				}});
//		}
//		if(MENU_ACTIVITY==type){
//			menu_user_info.setBackgroundResource(R.color.menu_selected);
//			menu_user_info.setClickable(false);
//			menu_user_info.setTextColor(0xffffffff);
//		}else{
//			menu_user_info.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					
//				}});
//		}
//		
//	}
	
	

}
