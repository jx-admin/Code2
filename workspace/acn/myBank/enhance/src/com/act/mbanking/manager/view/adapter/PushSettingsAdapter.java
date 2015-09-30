
package com.act.mbanking.manager.view.adapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.act.mbanking.Contants;
import com.act.mbanking.GCMIntentService;
import com.act.mbanking.R;
import com.act.mbanking.bean.PushCategoryModel;
import com.act.mbanking.bean.PushSettingModel;
import com.act.mbanking.bean.ResponsePublicModel;
import com.act.mbanking.logic.SetPushPreferencesJson;
import com.act.mbanking.manager.OnPushNotificationClick;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.LogManager;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.view.OnChangedListener;
import com.act.mbanking.view.SlipButton;

public class PushSettingsAdapter implements ListAdapter, OnClickListener {
    LayoutInflater inflater;

    private boolean showLog = true;

    private static final String Tag = "sendPushRequest";

    PushCategoryModel pushCategoryModel;

    PushSettingModel sendSettingModel;

    List<PushSettingModel> sendpushSettingList;

    /**
     * 通过该对象进行画
     */
    private List<PushCategoryModel> _pushCategoryList;

    public PushSettingsAdapter(Context cxt, ListView lv, List<PushCategoryModel> pushCategoryList) {
        if (pushCategoryList != null) {
            _pushCategoryList = pushCategoryList;
        }

        this.cxt = cxt;
        parent = new SoftReference<ListView>(lv);
        inflater = (LayoutInflater)cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sendpushSettingList = new ArrayList<PushSettingModel>();
    }

    @Override
    public int getCount() {
        return _pushCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.push_settings, null);
            holder = new ViewHolder();

            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.iv = (ImageView)convertView.findViewById(R.id.imagehint);
            holder.pushlayout = (LinearLayout)convertView.findViewById(R.id.pushLayout);
            holder.button = (Button)convertView.findViewById(R.id.buttonsave);
            convertView.setOnClickListener(this);
            convertView.setTag(holder);
        }

        holder = (ViewHolder)convertView.getTag();
        holder.postion = position;
        pushCategoryModel = _pushCategoryList.get(position);

        String category = pushCategoryModel.getCategory();
        if ((category != "expand")) {
            holder.title.setText(category);

            List<PushSettingModel> pushSettingList = pushCategoryModel.getPushSettingList();
            holder.pushlayout.removeAllViews();
            for (PushSettingModel pushSettingModel : pushSettingList) {
                String itemName = pushSettingModel.getPushDescription();
                int messageType = pushSettingModel.getPushMessageType();
                int settingValue = pushSettingModel.getPushSetting();
                View v = inflater.inflate(R.layout.push_settings_item, null);
                TextView tv = (TextView)v.findViewById(R.id.pushname);
                tv.setText(itemName);
                SlipButton buttonSetting = (SlipButton) v.findViewById(R.id.toggleButton1);
                buttonSetting.setTag(pushSettingModel);
                buttonSetting.SetOnChangedListener(new OnChangedListener() {
                    @Override
                    public void OnChanged(View v, boolean CheckState) {
                        PushSettingModel pushSettingModel = (PushSettingModel) v.getTag();
                        SlipButton buttonSetting=(SlipButton) v;
                        if(buttonSetting.isChecked()){
                            pushSettingModel.setPushSetting(PushSettingModel.ENABLED);                                              
                        }else{
                            pushSettingModel.setPushSetting(PushSettingModel.DISABLED);  
                        }
                        if(!sendpushSettingList.contains(pushSettingModel)){
                            sendpushSettingList.add(pushSettingModel);
                        }
                    }
                });

                if (settingValue == 1) {
                    buttonSetting.setChecked(true);
                } else {
                    buttonSetting.setChecked(false);
                }
                holder.pushlayout.addView(v);
            }
            Button buttonSave = (Button)convertView.findViewById(R.id.buttonsave);
            buttonSave.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (Utils.isInitRegisterPushNotification(cxt)) {
                        setPushSetting(sendpushSettingList);
                    } else {
                        notification();
                    }
                }

            });
        }
        int eValue = pushCategoryModel.getExpand();
        if (eValue == 1) {
            holder.pushlayout.setVisibility(View.VISIBLE);
            holder.button.setVisibility(View.VISIBLE);
            holder.iv.setImageResource(R.drawable.btnarrow_up);
        } else {
            holder.pushlayout.setVisibility(View.GONE);
            holder.button.setVisibility(View.GONE);
            holder.iv.setImageResource(R.drawable.btnarrow_down);
        }
        return convertView;
    }

    private void notification() {
        final SharedPreferences sp = cxt.getSharedPreferences(Contants.SETTING_FILE_NAME,
                cxt.MODE_PRIVATE);
        LayoutInflater inflater = LayoutInflater.from(cxt);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(
                R.layout.exit_message_dialog_layout, null);

        final Dialog dialog = new Dialog(cxt, R.style.selectorDialog);
        dialog.setContentView(linearLahyout);
        Button yesButton = (Button)dialog.findViewById(R.id.yes_btn);
        Button noButton = (Button)dialog.findViewById(R.id.no_btn);
        TextView text = (TextView)dialog.findViewById(R.id.exit_message_text);
        text.setText(R.string.is_receive_notifictions);
        dialog.show();
        noButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Contants.updataInitSetting(sp.edit(), true);
                GCMIntentService.isSavePush(true);
                GCMIntentService.setOnPushNotificationClick(new OnPushNotificationClick() {
                    public void onPushNotification(boolean register) {
                        if (register) {
                            setPushSetting(sendpushSettingList);
                        }
                    }
                });
                Utils.registerGCM(cxt);
            }
        });
    }

    private void setPushSetting(final List<PushSettingModel> pushsettinglist) {
        ProgressOverlay progressOverlay = new ProgressOverlay(this.cxt);
        progressOverlay.show(this.cxt.getResources().getString(R.string.waiting),
                new OnProgressEvent() {
                    @Override
                    public void onProgress() {
                        String postData = SetPushPreferencesJson.setPushPreferencesReportProtocal(
                                Contants.publicModel, pushsettinglist);
                        HttpConnector httpConnector = new HttpConnector();
                        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                                postData, cxt);
                        ResponsePublicModel response = SetPushPreferencesJson
                                .ParseSetPushPreferencesResponse(httpResult);
                        Message msg = new Message();
                        msg.obj = response;
                        mHandler.sendMessage(msg);
                        if (response.isSuccess()) {
                            // TODO alert .....
                            log("update:" + "success");
                        } else {
                            // TODO failure ...
                            log("update:" + "fail");
                        }
                    }
                });
    }

    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            ResponsePublicModel response = (ResponsePublicModel)msg.obj;
            if (response.isSuccess()) {
                Toast.makeText(cxt, "save successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(cxt, "save fail", Toast.LENGTH_SHORT).show();
            }
        }

    };

    private void log(String message) {
        if (showLog) {
            LogManager.d(Tag + message);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return _pushCategoryList.size() < 1;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        dso.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        dso.unregisterObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return _pushCategoryList.size() > 0;
    }

    @Override
    public boolean isEnabled(int arg0) {

        return false;
    }

    public List<PushCategoryModel> getList() {
        return _pushCategoryList;
    }

    public static class ViewHolder {
        public TextView title = null;

        public TextView item = null;

        public TextView value = null;

        public TextView detail = null;

        public Button button = null;

        public LinearLayout pushlayout = null;

        public ImageView iv = null;

        public int postion = -1;

        public int expand = 0;
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder)view.getTag();
        int newValue = holder.postion;

        if (expanded > -1) {
            pushCategoryModel = _pushCategoryList.get(expanded);
            pushCategoryModel.setExpand(0);
        }
        if (newValue == expanded) {
            expanded = -1;
        } else {
            pushCategoryModel = _pushCategoryList.get(newValue);
            pushCategoryModel.setExpand(1);
            if (sendpushSettingList == null) {
                sendpushSettingList = new ArrayList<PushSettingModel>();
            } else {
                sendpushSettingList.clear();
            }
            expanded = newValue;
        }
        notifyChanged();
    };

    public void notifyChanged() {
        dso.notifyChanged();

        ListView lv = parent.get();
        if (null != lv) {
            setListViewHeightBasedOnChildren(lv);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }

    private DataSetObservable dso = new DataSetObservable();

    private Context cxt;

    private int expanded = -1;

    private SoftReference<ListView> parent;

    private int settag = 1;
}
