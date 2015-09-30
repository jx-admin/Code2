
package com.act.mbanking.manager.contactus;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.bean.ResponsePublicModel;
import com.act.mbanking.bean.TablesResponseModel;
import com.act.mbanking.logic.InsertCommunicationJson;
import com.act.mbanking.logic.InsertCommunicationJson.UserInfo;
import com.act.mbanking.logic.TablesJson;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.DialogManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class ContactNewRequestLayout extends ExpandedContainer implements
        ContactNewRequestChannelContainer.onChannelChangedListener {
    /**
     * Item: object
     */
    private ItemExpander itemObject;

    /**
     * Item: channel
     */
    private ItemExpander itemChannel;

    /**
     * Item: details
     */
    private ItemExpander itemDetails;

    /**
     * Item: description
     */
    private ItemExpander itemDescription;

    /**
     * Button: send request
     */
    private Button send_request;

    /**
     * initilization flag
     */
    private boolean bInitialized = false;

    private String description;

    private String branch;

    private String contactDate;

    private String contactTime;

    private String topicOfInterest;

    private String contactMode = InsertCommunicationJson.CONTACT_MODE_EMAIL;

    private UserInfo userInfo = new InsertCommunicationJson.UserInfo();

    private ContactNewRequestObjectContainer objectContainer;

    private ContactNewRequestChannelEmail channelEmailContainer;

    private ContactNewRequestChannelPhone channelPhoneContainer;

    private ContactNewRequestChannelBranch channelBranchContainer;

    private ContactNewRequestDescription descriptionContainer;

    private static TablesResponseModel tablesResponseModel;

    private Handler myHandler = new Handler();

    private static final String TAG = "ContactNewRequestLayout";
    
    private GoogleAnalytics mGaInstance;
    
	private Tracker mGaTracker1;

    public ContactNewRequestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void resetResult() {
        super.resetResult();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        if (!bInitialized) {
            getTableService();

        }
    }

    private void initge() {
        send_request = (Button)findViewById(R.id.new_request_send);
        send_request.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                topicOfInterest = objectContainer.getTopicOfInterest();
                if (topicOfInterest == null) {
                    Toast.makeText(getContext(), R.string.object_cannot_empty, Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (contactMode.equals(InsertCommunicationJson.CONTACT_MODE_EMAIL)
                        || contactMode.equals(InsertCommunicationJson.CONTACT_MODE_BRANCH)) {
                    if (contactMode.equals(InsertCommunicationJson.CONTACT_MODE_EMAIL)) {
                        userInfo.user_email = channelEmailContainer.getEmail();
                    } else if (contactMode.equals(InsertCommunicationJson.CONTACT_MODE_BRANCH)) {
                        userInfo.user_email = channelBranchContainer.getEmail();
                    }

                    if (userInfo.user_email==null||userInfo.user_email.isEmpty()) {
                        Toast.makeText(getContext(), R.string.email_cannot_empty, Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                }

                if (contactMode.equals(InsertCommunicationJson.CONTACT_MODE_PHONE)
                        || contactMode.equals(InsertCommunicationJson.CONTACT_MODE_BRANCH)) {
                    if (contactMode.equals(InsertCommunicationJson.CONTACT_MODE_PHONE)) {
                        userInfo.user_phone = channelPhoneContainer.getPhone();
                        contactTime = channelPhoneContainer.getContactTime();
                    } else if (contactMode.equals(InsertCommunicationJson.CONTACT_MODE_BRANCH)) {
                        userInfo.user_phone = channelBranchContainer.getPhone();
                        contactTime = channelBranchContainer.getContactTime();
                    }

                    if (userInfo.user_phone.isEmpty()) {
                        Toast.makeText(getContext(), R.string.phone_cannot_empty, Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                }

                if (contactMode.equals(InsertCommunicationJson.CONTACT_MODE_BRANCH)) {
                    contactDate = channelBranchContainer.getContactDate();
                    if (contactDate == null || contactDate.isEmpty()) {
                        Toast.makeText(getContext(),R.string.date_cannot_empty, Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    branch = Contants.getUserInfo.getBranchName() + ", "
                            + Contants.getUserInfo.getBranchAddress() + ", "
                            + Contants.getUserInfo.getBranchCity();
                }

                description = descriptionContainer.getDescription();
                branch = channelBranchContainer.getBranch();
                userInfo.user_name = Contants.getUserInfo.getCustomerName() + " "
                        + Contants.getUserInfo.getCustomerSurname();
                ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
                progressOverlay.show("", new OnProgressEvent() {

                    @Override
                    public void onProgress() {
                        // TODO Auto-generated method stub
                        String postData = InsertCommunicationJson.InsertCommunicationProtocol(
                                Contants.publicModel, contactMode, topicOfInterest, description,
                                userInfo, branch, contactTime, contactDate);
                        HttpConnector httpConnector = new HttpConnector();
                        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                                postData, getContext());
                        final ResponsePublicModel insertTransferresponse = InsertCommunicationJson
                                .ParseInsertCommunicationResponse(httpResult);
                        if (insertTransferresponse.isSuccess()) {
                            myHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                	mGaInstance = GoogleAnalytics.getInstance(getContext());
                                    mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
                                	mGaTracker1.sendView("event.contact.us.new.request.sent");
                                    DialogManager
                                            .createMessageDialog(
                                                    getResources().getString(
                                                            R.string.request_successfully),
                                                    getContext()).show();
                                }
                            });
                        } else {
                        	mGaInstance = GoogleAnalytics.getInstance(getContext());
                            mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
                        	mGaTracker1.sendView("event.contact.us.new.request.sent");
                            myHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    DialogManager.createMessageDialog(
                                            insertTransferresponse.eventManagement
                                                    .getErrorDescription(), getContext()).show();
                                }
                            });
                        }
                    }
                });

            }
        });
        bInitialized = true;
    }

    private void getTableService() {
        final List<String> tableNameList = new ArrayList<String>();
        tableNameList.add("COMM_TOPIC");
        tableNameList.add("COMM_CONTACTMODE");
        tableNameList.add("COMM_SCHEDULEPHONE");
        tableNameList.add("COMM_SCHEDULEBRANCH");
        // tableNameList.add("COMM_CHANNEL");
        ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                String postData = TablesJson.GetTablesReportProtocal(Contants.publicModel,
                        tableNameList);
                String httpResult = new HttpConnector().requestByHttpPost(Contants.mobile_url,
                        postData, getContext());
                Log.i(TAG, httpResult);
                tablesResponseModel = TablesJson.ParseTablesResponse(httpResult);

                myHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        initObject();
                        initChannel();
                        initDetails();
                        initDescription();
                        initge();
                    }
                });
            }

        });

    }

    private void initObject() {
        itemObject = (ItemExpander)findViewById(R.id.new_request_object);
        itemObject.setLevel(1);
        // itemObject.setTitle("OBJECT");
        itemObject.setTitle(getContext().getString(R.string.object));
        itemObject.setTypeface(Typeface.DEFAULT);
        itemObject.setResultVisible(true);
        objectContainer = (ContactNewRequestObjectContainer)LayoutInflater.from(getContext())
                .inflate(R.layout.contact_new_request_object, null);
        if(tablesResponseModel!=null&&tablesResponseModel.getTableWrapper("COMM_TOPIC")!=null){       
        	objectContainer.init(tablesResponseModel.getTableWrapper("COMM_TOPIC") .getTableContentList());
        }
        itemObject.setExpandedContainer(objectContainer);
        itemObject.setExpandable(true);
    }

    private void initChannel() {
        itemChannel = (ItemExpander)findViewById(R.id.new_request_channel);
        itemChannel.setLevel(1);
        itemChannel.setTitle(getContext().getString(R.string.channel));
        itemChannel.setTypeface(Typeface.DEFAULT);
        itemChannel.setResultVisible(true);
        ContactNewRequestChannelContainer channelContainer = (ContactNewRequestChannelContainer)LayoutInflater
                .from(getContext()).inflate(R.layout.contact_new_request_channel, null);
        if(tablesResponseModel!=null&&tablesResponseModel.getTableWrapper("COMM_CONTACTMODE")!=null){
        channelContainer.init(tablesResponseModel.getTableWrapper("COMM_CONTACTMODE")
                .getTableContentList());}
        channelContainer.addRadioGroupListener(this);
        itemChannel.setExpandedContainer(channelContainer);
        itemChannel.setExpandable(true);
    }

    private void initDetails() {
        itemDetails = (ItemExpander)findViewById(R.id.new_request_details);
        itemDetails.setLevel(1);
        itemDetails.setTitle(getContext().getString(R.string.details));
        itemDetails.setTypeface(Typeface.DEFAULT);
        itemDetails.setResultVisible(true);
        itemDetails.setExpandable(true);

        channelEmailContainer = (ContactNewRequestChannelEmail)LayoutInflater.from(getContext())
                .inflate(R.layout.contact_new_request_channel_email, null);

        channelPhoneContainer = (ContactNewRequestChannelPhone)LayoutInflater.from(getContext())
                .inflate(R.layout.contact_new_request_channel_phone, null);
        if(tablesResponseModel!=null&&tablesResponseModel.getTableWrapper("COMM_SCHEDULEPHONE")!=null){
        channelPhoneContainer.init(tablesResponseModel.getTableWrapper("COMM_SCHEDULEPHONE")
                .getTableContentList());
        }

        channelBranchContainer = (ContactNewRequestChannelBranch)LayoutInflater.from(getContext())
                .inflate(R.layout.contact_new_request_channel_branch, null);
        String address = Contants.getUserInfo.getBranchName() + " "
                + Contants.getUserInfo.getBranchAddress() + " "
                + Contants.getUserInfo.getBranchPostalCode() + " "
                + Contants.getUserInfo.getBranchCity();
        channelBranchContainer.setAddress(address);

        if(tablesResponseModel!=null&&tablesResponseModel.getTableWrapper("COMM_SCHEDULEBRANCH")!=null){
        channelBranchContainer.init(tablesResponseModel.getTableWrapper("COMM_SCHEDULEBRANCH")
                .getTableContentList());
        }
    }

    private void initDescription() {
        itemDescription = (ItemExpander)findViewById(R.id.new_request_description);
        itemDescription.setLevel(1);
        itemDescription.setTitle(getContext().getString(R.string.description_));
        itemDescription.setTypeface(Typeface.DEFAULT);
        itemDescription.setResultVisible(true);
        descriptionContainer = (ContactNewRequestDescription)LayoutInflater.from(getContext())
                .inflate(R.layout.contact_new_request_description, null);
        itemDescription.setExpandedContainer(descriptionContainer);
        itemDescription.setExpandable(true);
    }

    private void channelEmail() {
        itemDetails.setTitle(getContext().getString(R.string.email));
        itemDetails.setExpandedContainer(channelEmailContainer);
        itemDetails.onChange(channelEmailContainer.getEmail());
        // channelDesc();
    }

    private void channelPhone() {
        itemDetails.setTitle(getContext().getString(R.string.phone));
        itemDetails.setExpandedContainer(channelPhoneContainer);
        itemDetails.onChange(channelPhoneContainer.getPhone());
        // channelDesc();
    }

    private void channelBranch() {
        String address = Contants.getUserInfo.getBranchName() + " "
                + Contants.getUserInfo.getBranchAddress() + " "
                + Contants.getUserInfo.getBranchPostalCode() + " "
                + Contants.getUserInfo.getBranchCity();
        channelBranchContainer.setAddress(address);

        itemDetails.setTitle(getContext().getString(R.string.branch));
        itemDetails.setExpandedContainer(channelBranchContainer);
        itemDetails.onChange(channelBranchContainer.getBranchName());
        // channelDesc();
    }

    // private void channelDesc() {
    // String desc = Contants.getUserInfo.getBranchName() + " "
    // + Contants.getUserInfo.getBranchAddress() + " "
    // + Contants.getUserInfo.getBranchPostalCode() + " "
    // + Contants.getUserInfo.getBranchCity();
    //
    // descriptionContainer.setDesc(desc);
    // itemDescription.setExpandedContainer(descriptionContainer);
    // itemDescription.onChange(descriptionContainer.getDescription());
    // }

    @Override
    public void onModeChanged(final String code, final String description) {
        if (description.equals("Mail")) {
            contactMode = InsertCommunicationJson.CONTACT_MODE_EMAIL;
            channelEmail();
        } else if (description.equals("Phone")) {
            contactMode = InsertCommunicationJson.CONTACT_MODE_PHONE;
            channelPhone();
        } else if (description.equals("Branch")) {
            contactMode = InsertCommunicationJson.CONTACT_MODE_BRANCH;
            channelBranch();
        }
    }
}
