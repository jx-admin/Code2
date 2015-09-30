package net.ds.effect.framework;

import java.util.List;

import net.ds.effect.R;
import net.ds.effect.core.EffectFactory;
import net.ds.effect.core.EffectInfo;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EffectsOverview extends android.widget.FrameLayout implements OnClickListener {
    
    private EffectAdapter mEffectAdapter;

    private List<EffectInfo> mEffectList;

    private int mCurrentEffectIdentity;

    private ListView mListView;

    public EffectsOverview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        mListView = (ListView) findViewById(R.id.effect_list_listview);
    }
    
    protected int mCurrentIndex = 0;
    
    protected int mTop = 0;

    public void onShow() {
        mEffectList = this.createEffectList();
        mCurrentEffectIdentity = getCurrentEffectIdentity();

        mEffectAdapter = new EffectAdapter(this.getContext());
        mListView.setAdapter(mEffectAdapter);
        mListView.setOnScrollListener(new OnScrollListener() {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    mCurrentIndex = view.getFirstVisiblePosition();
                    mTop = view.getChildAt(0) == null ? 0 : view.getChildAt(0).getTop();
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                
            }
        });
    }

    public void onExit() {
    }

    public void onResume() {
        if (mEffectList == null) {
            return;
        }

        int currentEffectIdentity = getCurrentEffectIdentity();
        if (currentEffectIdentity == mCurrentEffectIdentity) {
            return;
        }

        mCurrentEffectIdentity = currentEffectIdentity;

        mEffectAdapter.notifyDataSetInvalidated();
        
        if (mListView != null) {
            mListView.setSelectionFromTop(mCurrentIndex, mTop);
        }
    }

    protected List<EffectInfo> createEffectList() {
        List<EffectInfo> effectList = EffectFactory.getAllEffects(EffectInfo.EFFECT_ON_HOME);
        return effectList;
    }
    
    public static String loadString(Context context, String resName) {
        return context.getResources().getString(loadResourceId(context, "string", resName));
    }
    
    public static int loadResourceId(Context context, String defType, String resName) {
        return context.getResources().getIdentifier(resName, defType, context.getPackageName());
    }

    protected int getCurrentEffectIdentity() {
        return 0;
    }

    protected void onClickPreview(EffectInfo effectObject) {
        Intent intent = new Intent(this.getContext(), PreviewActivity.class);
        intent.putExtra(PreviewActivity.SELECTED_EFFECT_ID, effectObject.type);
        this.getContext().startActivity(intent);
    }
    
    
    private class EffectAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;

        private final int mLineCount;

        private EffectAdapter(Context context) {
            mInflater = LayoutInflater.from(context);

            mLineCount = (mEffectList.size() - 1) / 3 + 1;
        }

        @Override
        public int getCount() {
            return mLineCount;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
                convertView = mInflater.inflate(R.layout.effect_list_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            for (int i = 0; i < 3; i++) {
                int index = position * 3 + i;
                if (index < mEffectList.size()) {
                    final EffectInfo effect = mEffectList.get(index);
                    holder.show(effect, i);
                } else {
                    holder.hide(i);
                }
            }
            return convertView;
        }

        public class ViewHolder {
            public View[] mContainers = new View[3];

            public TextView[] mTextViews = new TextView[3];

            public ImageView[] mImageViews = new ImageView[3];

            public ViewHolder(View convertView) {
                mTextViews[0] = (TextView) convertView.findViewById(R.id.local_theme_list_detail_second_txt1);
                mTextViews[1] = (TextView) convertView.findViewById(R.id.local_theme_list_detail_second_txt2);
                mTextViews[2] = (TextView) convertView.findViewById(R.id.local_theme_list_detail_second_txt3);

                mImageViews[0] = (ImageView) convertView.findViewById(R.id.local_theme_list_detail_second_img1);
                mImageViews[1] = (ImageView) convertView.findViewById(R.id.local_theme_list_detail_second_img2);
                mImageViews[2] = (ImageView) convertView.findViewById(R.id.local_theme_list_detail_second_img3);

                mContainers[0] = convertView.findViewById(R.id.local_theme_list_detail_second_container1);
                mContainers[1] = convertView.findViewById(R.id.local_theme_list_detail_second_container2);
                mContainers[2] = convertView.findViewById(R.id.local_theme_list_detail_second_container3);

            }

            public void show(final EffectInfo effect, int i) {

                mTextViews[i].setText(loadString(getContext(), effect.title));
                mImageViews[i].setImageResource(effect.resourceID);
                mContainers[i].setTag(effect);
                mContainers[i].setVisibility(View.VISIBLE);
                mContainers[i].setOnClickListener(EffectsOverview.this);
            }

            public void hide(int i) {
                mTextViews[i].setText(null);
                mImageViews[i].setImageBitmap(null);
                mContainers[i].setTag(null);
                mContainers[i].setVisibility(View.INVISIBLE);
                mContainers[i].setOnClickListener(null);
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof EffectInfo) {
            onClickPreview((EffectInfo) v.getTag());
        }
    }
}
