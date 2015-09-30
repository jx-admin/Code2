
package com.accenture.mbank.view;

import java.util.List;

import com.accenture.mbank.R;
import com.accenture.mbank.model.TableContentList;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ContactNewRequestChannelPhone extends ExpandedContainer implements OnItemClickListener {
    private EditText phone;

    private Button phone_select;
    
    private String contactTime;

    private boolean bInitialized = false;
    
    private List<TableContentList> tableContentLists = null;
    
    private AlertDialog alertDialog = null;
    
    public ContactNewRequestChannelPhone(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void init(final List<TableContentList> tableContentList) {
        if (!bInitialized) {
            this.tableContentLists = tableContentList;
            phone = (EditText)findViewById(R.id.request_details_phone);
            phone.setText(Contants.getUserInfo.getReferenceTelephoneNumber1());
            phone.addTextChangedListener(new TextWatcher() {
                
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    expandResultChange(String.valueOf(s));
                }
                
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    
                }
                
                @Override
                public void afterTextChanged(Editable s) {
                    
                }
            });
            phone_select = (Button)findViewById(R.id.request_details_phone_hours);
            phone_select.setText(tableContentList.get(0).getDescription());
            phone_select.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialog = DialogManager.createHoursDialog(getContext(), ContactNewRequestChannelPhone.this, tableContentLists);
                    alertDialog.show();
                }
            });
            contactTime = tableContentList.get(0).getDescription();
            bInitialized = true;
        }
    }

    public String getPhone() {
        return phone.getText().toString();
    }

    public String getContactTime() {
        return contactTime;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        contactTime = tableContentLists.get(position).getDescription();
        phone_select.setText(tableContentLists.get(position).getDescription());
        alertDialog.dismiss();
    }
}
