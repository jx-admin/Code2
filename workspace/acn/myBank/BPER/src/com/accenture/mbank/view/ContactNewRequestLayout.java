
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.accenture.mbank.logic.InsertCommunicationJson;
import com.accenture.mbank.logic.InsertCommunicationJson.UserInfo;
import com.accenture.mbank.logic.TablesJson;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.model.TableContentList;
import com.accenture.mbank.model.TableWrapperList;
import com.accenture.mbank.model.TablesResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.view.ContactNewRequestChannelContainer.OnConfirmEnableListener;
import com.accenture.mbank.view.ContactNewRequestChannelContainer.Type;
import com.accenture.mbank.view.ContactNewRequestObjectContainer.OnObjectSelected;


public class ContactNewRequestLayout extends ExpandedContainer{
    /**
     * Item: object
     */
    private ItemExpander itemObject;

    /**
     * Item: channel
     */
    private ItemExpander itemChannel;

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


    private ContactNewRequestChannelContainer channelContainer;
//    private ContactNewRequestChannelEmail channelEmailContainer;

//    private ContactNewRequestChannelPhone channelPhoneContainer;

//    private ContactNewRequestChannelBranch channelBranchContainer;

    private ContactNewRequestDescription descriptionContainer;

    private static TablesResponseModel tablesResponseModel;

    private Handler myHandler = new Handler();
    
    private static final String TAG = "ContactNewRequestLayout";

    private static final String COMM_TOPIC = "COMM_TOPIC";
    private static final String COMM_CONTACTMODE = "COMM_CONTACTMODE";
    private static final String COMM_SCHEDULEPHONE = "COMM_SCHEDULEPHONE"; 
    private static final String COMM_SCHEDULEBRANCH = "COMM_SCHEDULEBRANCH";

    
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
                    Toast.makeText(getContext(), R.string.object_cannot_empty, Toast.LENGTH_LONG) .show();
                    return;
                }

                switch(channelContainer.type){
                case Email:
                	userInfo.user_email = channelContainer.getEmail();
                	contactMode=InsertCommunicationJson.CONTACT_MODE_EMAIL;
                	break;
                case Phone:
                	userInfo.user_phone = channelContainer.getPhone();
                    contactTime = channelContainer.getContactTime();
                	contactMode=InsertCommunicationJson.CONTACT_MODE_PHONE;
                	break;
                case Branch:
                	userInfo.user_email = channelContainer.getEmail();
                	userInfo.user_phone = channelContainer.getPhone();
                    contactTime = channelContainer.getContactTime();
                    contactDate = channelContainer.getBranchContactDate();
                	contactMode=InsertCommunicationJson.CONTACT_MODE_BRANCH;
                	break;
                case Null:
                	Toast.makeText(getContext(), R.string.how_contact_empty, Toast.LENGTH_LONG) .show();
                    return;
                }
                
                
                if (channelContainer.type==Type.Email || channelContainer.type==Type.Branch) {
                    if ((userInfo.user_email.isEmpty() && channelContainer.type==Type.Email)
                    		||
                    		(userInfo.user_email.isEmpty() && channelContainer.getBranch()==InsertCommunicationJson.BRANCH_EMAIL)) {
                        Toast.makeText(getContext(), R.string.email_cannot_empty, Toast.LENGTH_LONG) .show();
                        return;
                    }
                }

                if (channelContainer.type==Type.Phone || channelContainer.type==Type.Branch) {
                    if ((userInfo.user_phone.isEmpty() && channelContainer.type==Type.Phone)
                    		||
                    		(userInfo.user_phone.isEmpty() && channelContainer.getBranch()==InsertCommunicationJson.BRANCH_PHONE)) {
                        Toast.makeText(getContext(), R.string.phone_cannot_empty, Toast.LENGTH_LONG) .show();
                        return;
                    }
                }
                branch="";
                if (channelContainer.type==Type.Branch) {
                    if (contactDate == null || contactDate.isEmpty()) {
                        Toast.makeText(getContext(),R.string.date_cannot_empty, Toast.LENGTH_LONG) .show();
                        return;
                    }
                    contactDate=TimeUtil.changeFormattrString(contactDate, TimeUtil.dateFormat5, TimeUtil.dateFormat2);
                    branch = Contants.getUserInfo.getUserprofileHb().getBranchName() + ", "
                            + Contants.getUserInfo.getUserprofileHb().getBranchAddress() + ", "
                    		+ Contants.getUserInfo.getUserprofileHb().getBranchPostalCode() + ", "
                            + Contants.getUserInfo.getUserprofileHb().getBranchCity();
                }
//                branch = channelContainer.getBranch();

                description = descriptionContainer.getDescription();
                userInfo.user_name = Contants.getUserInfo.getUserprofileHb().getCustomerName() + " "
                        + Contants.getUserInfo.getUserprofileHb().getCustomerSurname();
                ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
                progressOverlay.show("", new OnProgressEvent() {

                    @Override
                    public void onProgress() {
                        // TODO Auto-generated method stub
                        String postData = InsertCommunicationJson.InsertCommunicationProtocol(
                                Contants.publicModel, contactMode, topicOfInterest, description,
                                userInfo, branch, contactTime, contactDate,channelContainer.getEmail(),channelContainer.getPhone(),channelContainer.getBranch());
                        HttpConnector httpConnector = new HttpConnector();
                        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                                postData, getContext());
                        final ResponsePublicModel insertTransferresponse = InsertCommunicationJson
                                .ParseInsertCommunicationResponse(httpResult);
                        myHandler.post(new Runnable() {
                        	
                        	@Override
                        	public void run() {
                        		String errorCode=null;
                        		if(insertTransferresponse!=null&&insertTransferresponse.eventManagement!=null){
                        			errorCode=insertTransferresponse.eventManagement.getErrorCode();
                        		}
                        		if("91301".equals(errorCode)){
                        			DialogManager.createMessageDialog(R.string.new_requast_resultCode_91301, getContext()).show();
                        		}else if("91175".equals(errorCode)){
                        			DialogManager.createMessageDialog(R.string.new_requast_resultCode_91175, getContext()).show();
                        		}else if("91167".equals(errorCode)){
                        			DialogManager.createMessageDialog(R.string.new_requast_resultCode_91167, getContext()).show();
                        		}else if("91169".equals(errorCode)){
                        			DialogManager.createMessageDialog(R.string.new_requast_resultCode_91169, getContext()).show();
                        		}else if(insertTransferresponse!=null&&insertTransferresponse.isSuccess()){
                        			DialogManager.createMessageDialog(R.string.request_successfully, getContext()).show();
                        		}else{
                        			com.accenture.mbank.model.EventManagement mEventManagement=null;
                        			if(insertTransferresponse!=null){
                        				mEventManagement=insertTransferresponse.eventManagement;
                        			}
                        			DialogManager.displayErrorMessage(mEventManagement, 0, getContext()).show();
                        		}
                        	}
                        });
                    }
                });

            }
        });
        bInitialized = true;
        initConfirmButton();
    }
    
    private boolean isObject,isChennel;
    private void initConfirmButton(){
    	if(isObject&&isChennel/*&&descriptionContainer.getDescription().length()>0*/){
    		send_request.setVisibility(View.VISIBLE);
    	}else{
    		send_request.setVisibility(View.GONE);
    	}
    }

    private void getTableService() {
        final List<String> tableNameList = new ArrayList<String>();
        tableNameList.add(COMM_TOPIC);
        tableNameList.add(COMM_CONTACTMODE);
        tableNameList.add(COMM_SCHEDULEPHONE);
        tableNameList.add(COMM_SCHEDULEBRANCH);
        // tableNameList.add("COMM_CHANNEL");
        ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                String postData = TablesJson.GetTablesReportProtocal(Contants.publicModel,
                        tableNameList);
                String httpResult = new HttpConnector().requestByHttpPost(Contants.mobile_url,
                        postData, getContext());
//                Log.i(TAG, httpResult);
                tablesResponseModel = TablesJson.ParseTablesResponse(httpResult);

                myHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        initObject();
                        initChannel();
//                        initDetails();
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
        itemObject.setResult(R.string.tap_to_set_as);
        itemObject.setTypeface(Typeface.DEFAULT);
        itemObject.setResultVisible(true);
        objectContainer = (ContactNewRequestObjectContainer)LayoutInflater.from(getContext())
                .inflate(R.layout.contact_new_request_object, null);
        if(tablesResponseModel!=null){
        	TableWrapperList mTableWrapperList=tablesResponseModel.getTableWrapper("COMM_TOPIC");
        	if(mTableWrapperList!=null){
        		List<TableContentList>mTableContentList=mTableWrapperList .getTableContentList();
        		if(mTableContentList!=null){
        			objectContainer.init(mTableContentList);
        		}
        	}
        }
        objectContainer.setOnObjectSelected(new OnObjectSelected() {
			
			@Override
			public void onObjectSelected(boolean isSelected) {
				// TODO Auto-generated method stub
				isObject=true;
				initConfirmButton();
			}
		});
        itemObject.setExpandedContainer(objectContainer);
        itemObject.setExpandable(true);
        itemObject.setExpand(false);
    }

    private void initChannel() {
        itemChannel = (ItemExpander)findViewById(R.id.new_request_channel);
        itemChannel.setLevel(1);
        itemChannel.setTitle(getContext().getString(R.string.channel));
        itemChannel.setTypeface(Typeface.DEFAULT);
        itemChannel.setResultVisible(true);
        itemChannel.setResult(R.string.tap_to_set_as);
        channelContainer = (ContactNewRequestChannelContainer)LayoutInflater .from(getContext()).inflate(R.layout.contact_new_request_channel, null);
        if(tablesResponseModel!=null){
        	List<TableContentList>mTableContentList=tablesResponseModel.getTableWrapper("COMM_CONTACTMODE").getTableContentList();
        	if(mTableContentList!=null){
        		channelContainer.init(mTableContentList);
        	}
        	mTableContentList=tablesResponseModel.getTableWrapper("COMM_SCHEDULEPHONE").getTableContentList();
        	if(mTableContentList!=null){
        		channelContainer.initPhone(mTableContentList);
        	}
        	mTableContentList=tablesResponseModel.getTableWrapper("COMM_SCHEDULEBRANCH").getTableContentList();
        	if(mTableContentList!=null){
        		channelContainer.setBranchTable(mTableContentList);
        	}
        }
//        channelContainer.addRadioGroupListener(this);
        itemChannel.setExpandedContainer(channelContainer);
        itemChannel.setExpandable(true);
        itemChannel.setExpand(false);
        
        if(Contants.getUserInfo != null){
        	if(Contants.getUserInfo.getUserprofileHb().getBranchName() !=null){
        		String address = Contants.getUserInfo.getUserprofileHb().getBranchName() + " "
        				+ Contants.getUserInfo.getUserprofileHb().getBranchAddress() + " "
        				+ Contants.getUserInfo.getUserprofileHb().getBranchPostalCode() + " "
        				+ Contants.getUserInfo.getUserprofileHb().getBranchCity();
        		channelContainer.setInformations(address);
        	}
        }
        channelContainer.setOnConfirmEnableListener(new OnConfirmEnableListener() {
        	
        	@Override
        	public void onChennelSelected(boolean islected) {
        		// TODO Auto-generated method stub
        		isChennel=islected;
				initConfirmButton();
        	}
        });
    }


    private void initDescription() {
        itemDescription = (ItemExpander)findViewById(R.id.new_request_description);
        itemDescription.setLevel(1);
        itemDescription.setTitle(getContext().getString(R.string.description1));
        itemDescription.setTypeface(Typeface.DEFAULT);
        itemDescription.setResultVisible(true);
        itemDescription.setResult(R.string.tap_to_set1_as);
        descriptionContainer = (ContactNewRequestDescription)LayoutInflater.from(getContext())
                .inflate(R.layout.contact_new_request_description, null);
        itemDescription.setExpandedContainer(descriptionContainer);
        itemDescription.setExpandable(true);
        itemDescription.setExpand(false);
    }

}
