
package com.accenture.mbank.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import com.accenture.mbank.R;
import com.accenture.mbank.logic.InsertCommunicationJson;
import com.accenture.mbank.model.TableContentList;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.view.DatePickerDialog.DatePickListener;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;

public class ContactNewRequestChannelBranch extends ExpandedContainer implements DatePickListener,
        OnItemClickListener {

    private TextView chanelBranchText;

    private RadioButton email_selected;

    private RadioButton phone_selected;

    private EditText email;

    private EditText phone;

    private TextView date;

    private Button hours;

    private boolean bInitialized = false;

    private String contactTime;

    private String branch;

    private String branchName;

    private String address;

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address 要设置的 address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return branchName
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * @param branchName 要设置的 branchName
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    private MyTextWatcher textWatcher = new MyTextWatcher();

    private List<TableContentList> tableContentLists = null;

    private AlertDialog alertDialog = null;

    public ContactNewRequestChannelBranch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void init(final List<TableContentList> tableContentList) {
        if (!bInitialized) {
            this.tableContentLists = tableContentList;
            chanelBranchText = (TextView)findViewById(R.id.channel_branch_text);
            chanelBranchText.setText(getAddress());
            email_selected = (RadioButton)findViewById(R.id.request_details_branch_email);
            phone_selected = (RadioButton)findViewById(R.id.request_details_branch_phone);
            email = (EditText)findViewById(R.id.request_details_branch_email_input);
            email.addTextChangedListener(textWatcher);
            email.setText(Contants.getUserInfo.getReferenceEmail());
            phone = (EditText)findViewById(R.id.request_details_branch_phone_input);
            phone.addTextChangedListener(textWatcher);
            phone.setText(Contants.getUserInfo.getReferenceTelephoneNumber1());
            setBranchName(Contants.getUserInfo.getBranchName());
            date = (TextView)findViewById(R.id.request_details_branch_date_input);
            date.setText(getCurrentDay());
            date.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    DatePickerDialog.getInstance().showDateDialog(getContext(),
                            ContactNewRequestChannelBranch.this);
                }
            });
            hours = (Button)findViewById(R.id.request_details_branch_hours);
            email_selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        email.setEnabled(true);
                        phone_selected.setChecked(false);
                        phone.setEnabled(false);
                        branch = InsertCommunicationJson.BRANCH_EMAIL;
                        expandResultChange(email.getText().toString());
                    }
                }
            });
            phone_selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        email_selected.setChecked(false);
                        email.setEnabled(false);
                        phone.setEnabled(true);
                        branch = InsertCommunicationJson.BRANCH_PHONE;
                        expandResultChange(phone.getText().toString());
                    }
                }
            });
            hours.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialog = DialogManager.createHoursDialog(getContext(),
                            ContactNewRequestChannelBranch.this, tableContentLists);
                    alertDialog.show();
                }
            });
            hours.setText(tableContentList.get(0).getDescription());
            contactTime = tableContentList.get(0).getDescription();
        }
        bInitialized = true;
    }

    public String getEmail() {
        return email.getText().toString();
    }

    public String getPhone() {
        return phone.getText().toString();
    }

    public String getContactDate() {
        return String.valueOf(date.getText());
    }

    public String getContactTime() {
        return contactTime;
    }

    public String getBranch() {
        return branch;
    }

    @Override
    public void onDatePick(CharSequence year, CharSequence month, CharSequence day, int yearint,
            int monthint, int dayint) {
        // format: MM.dd.yy
        date.setText(new StringBuffer().append(monthint + 1).append('.').append(dayint).append('.')
                .append(yearint % 100).toString());
    }

    int oneDay = 24 * 60 * 60 * 1000;

    private String getCurrentDay() {

        Calendar calendar = Calendar.getInstance();
        long nextWeekTime = calendar.getTimeInMillis();

        calendar.setTimeInMillis(nextWeekTime);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            nextWeekTime = nextWeekTime + 9 * oneDay;
        } else if (dayOfWeek == 2) {
            nextWeekTime = nextWeekTime + 9 * oneDay;
        } else if (dayOfWeek == 3) {
            nextWeekTime = nextWeekTime + 9 * oneDay;
        } else if (dayOfWeek == 4) {
            nextWeekTime = nextWeekTime + 9 * oneDay;
        } else if (dayOfWeek == 5) {
            nextWeekTime = nextWeekTime + 11 * oneDay;
        } else if (dayOfWeek == 6) {
            nextWeekTime = nextWeekTime + 11 * oneDay;
        } else if (dayOfWeek == 7) {
            // 周六加10天
            nextWeekTime = nextWeekTime + 10 * oneDay;

        }
        calendar.setTimeInMillis(nextWeekTime);
        SimpleDateFormat format = new SimpleDateFormat(TimeUtil.dateFormat5, Locale.US);
        return format.format(calendar.getTime());
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            expandResultChange(String.valueOf(s));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        contactTime = tableContentLists.get(position).getDescription();
        hours.setText(tableContentLists.get(position).getDescription());
        alertDialog.dismiss();
    }
}
