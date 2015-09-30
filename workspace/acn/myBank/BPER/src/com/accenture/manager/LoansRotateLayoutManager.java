
package com.accenture.manager;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.accenture.manager.protocol.RotateBankViewManager;
import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.LoansDetailActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.logic.GetFinancingInfoJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.GetFinancingInfoModel;
import com.accenture.mbank.model.InstallmentsModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.RotateTitleView;
import com.accenture.mbank.view.table.LoansRotateTableView;
import com.accenture.mbank.view.table.RotatTableView;
import com.accenture.mbank.view.table.RotatTableView.OnSlidListener;
import com.accenture.mbank.view.table.RotateTableViewWithButton.OnButtonClickListener;
import com.accenture.mbank.view.table.SignLineFormView;

public class LoansRotateLayoutManager extends RotateBankViewManager {

    Handler handler;

    public LoansRotateLayoutManager() {
        handler = new Handler();
    }

    static boolean reloadData = true;
    int dashboardAccounts = 0;

    @Override
    public void onShow() {

        int accounts = 13;
        if (hashMap.size() <= 0 || reloadData == true) {

            if (BaseActivity.isOffline) {

                if (container.getChildCount() <= 0) {
                    LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
                    final RotateTitleView rTitleView = (RotateTitleView)layoutInflater.inflate(
                            R.layout.rotate_title_view, null);
                    rTitleView.init();
                    rTitleView.setTitleText("Offline");
                    rTitleView.setTitleIcon(R.drawable.funding_50x35);

                    String time = TimeUtil.getDateString(System.currentTimeMillis(),
                            TimeUtil.dateFormat5);
                    rTitleView.setUpdateTitle(container.getContext().getResources()
                            .getString(R.string.loans_update_to) + " "
                            + time);

                    container.addView(rTitleView);
                    
                    LoansRotateTableView rotatTableView0 = new LoansRotateTableView(container.getContext());
                    container.addView(rotatTableView0);
                    container.setPadding(RotatTableView.margin, 0, 0, 0);
                    rotatTableView0.setCount(accounts);
                    rotatTableView0.setRotatResource(R.drawable.loans_slider, R.drawable.loans_cycle,
                            -1);
                    rotatTableView0.setButtonImage(R.drawable.loans_btn_details);
                    rotatTableView0.setButtonImageOver(R.drawable.loans_btn_details_over);
                    rotatTableView0.parentScrollView = (ScrollView)container.getParent();

                    LayoutInflater layoutInflater1 = LayoutInflater.from(container.getContext());
                    final RotateTitleView rTitleView1 = (RotateTitleView)layoutInflater1.inflate(
                            R.layout.rotate_title_view, null);
                    rTitleView1.init();
                    rTitleView1.setTitleText("Offline");
                    rTitleView1.setTitleIcon(R.drawable.funding_50x35);
                    rTitleView.setUpdateTitle(container.getContext().getResources()
                            .getString(R.string.loans_update_to) + " "
                            + time);
                    container.addView(rTitleView1);

                    startAnimation(rotatTableView0);
                }
            } else {
                getDashBoardData();
                dashboardAccounts = accounts;
            }
        }
    }

    List<AccountsModel> list;

    HashMap<AccountsModel, GetFinancingInfoModel> hashMap = new HashMap<AccountsModel, GetFinancingInfoModel>();

    private boolean needShowChart;

    public void getDashBoardData() {
        container.removeAllViews();
        hashMap.clear();
        setCanOrientation();

        ProgressOverlay overlay = new ProgressOverlay(container.getContext());
        overlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                list = Contants.loansAccounts;

                for (AccountsModel accountsModel : list) {
                    String postData = GetFinancingInfoJson.getFinancingInfoReportProtocal(
                            Contants.publicModel, accountsModel.getAccountCode(),
                            accountsModel.getFinanceType());

                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                            postData, container.getContext());
                    GetFinancingInfoModel getFinancingInfo = GetFinancingInfoJson
                            .paresgetFinancingInfoResponse(httpResult);

                    if (getFinancingInfo != null && getFinancingInfo.responsePublicModel.isSuccess()) {
                    	/*
                    	 * Get data successfully
                    	 */
                    	hashMap.put(accountsModel, getFinancingInfo);
                    	reloadData = false;
                    } else {
						if (httpResult == null) {
							/*
							 *  A connection error happened
							 */
							reloadData = true;
							handler.post(new Runnable() {
								@Override
								public void run() {
									final MainActivity mainActivity = (MainActivity)container.getContext();
									mainActivity.showTab(0);
								}
							});
							break;
						}
						
						else if (getFinancingInfo != null &&
								!getFinancingInfo.responsePublicModel.isSuccess() &&
								!getFinancingInfo.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_1) &&
								!getFinancingInfo.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_2)) {
							/*
							 * maybe the 90000 happens
							 */
							reloadData = true;
							handler.post(new Runnable() {
								@Override
								public void run() {
									onError(container.getContext());
								}
							});
							break;
						}
                    }
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        setAccount();
                        setCanOrientation();

                        if (needShowChart) {
                            showChart();
                            needShowChart = false;
                        }
                    }
                });
            }
        });

    }

    void setAccount() {
        if (list != null && list.size() > 0) {
            container.removeAllViews();

            for (AccountsModel dashBoardModel : list) {
                final AccountsModel dashBoardModel2 = dashBoardModel;
                LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
                final RotateTitleView rTitleView = (RotateTitleView)layoutInflater.inflate(
                        R.layout.rotate_title_view, null);
                rTitleView.init();
                // rTitleView.setTitleIcon(R.drawable.accounts_icon);
                rTitleView.setTitleIcon(R.drawable.funding_50x35);
                rTitleView.setTitleText(dashBoardModel.getAccountAlias());
                String time = TimeUtil.getDateString(System.currentTimeMillis(),
                        TimeUtil.dateFormat5);
                rTitleView.setUpdateTitle(container.getContext().getResources()
                        .getString(R.string.loans_update_to) + " "
                        + time);

                container.addView(rTitleView);
                final LoansRotateTableView rotatTableView0 = new LoansRotateTableView(
                        container.getContext());
                container.addView(rotatTableView0);
                final GetFinancingInfoModel getFinancingInfoModel = hashMap.get(dashBoardModel);
                if (getFinancingInfoModel == null) {
                    continue;
                }

                rotatTableView0.setCount(dashboardAccounts);
                rotatTableView0.setRotatResource(R.drawable.loans_slider, R.drawable.loans_cycle,
                        -1);
                rotatTableView0.parentScrollView = (ScrollView)container.getParent();
                rotatTableView0.setButtonImage(R.drawable.loans_btn_details);
                rotatTableView0.setButtonImageOver(R.drawable.loans_btn_details_over);
                if (dashBoardModel2 != null && getFinancingInfoModel.getInstallments() != null
                        && getFinancingInfoModel.getInstallments().size() > 0) {
                    setDashboardUi(getFinancingInfoModel, rotatTableView0, rTitleView, 0);
                }
                rotatTableView0.setOnSlidListener(new OnSlidListener() {

                    @Override
                    public void onSlid(View v, int index) {

                        setDashboardUi(getFinancingInfoModel, rotatTableView0, rTitleView, index);
                    }

                });
                setOnButtonClick(rotatTableView0);

                startAnimation(rotatTableView0);
            }
        }
    }

	protected void startAnimation(final RotatTableView rotview) {
		updateArrowState();

		if (Contants.DASHBOARD_ROTATE_ANIMATION_LOANS == false)
			return;

		Contants.DASHBOARD_ROTATE_ANIMATION_LOANS = false;
		
		handler.postDelayed(new Runnable() {
			public void run() {
				rotview.startAnimation();
			}
		}, 500);

	}

	private void setOnButtonClick(LoansRotateTableView rotatTableView) {
		rotatTableView.setOnButtonClickListener(new OnButtonClickListener() {

			@Override
			public void onClick(View v) {

				int index = getVisiableRotateViewIndex();

				if (list!=null && index >= 0 && index < list.size()) {
					AccountsModel accountsModel = list.get(index);
					GetFinancingInfoModel getFinancingInfoModel = hashMap.get(accountsModel);
					if (getFinancingInfoModel != null) {
						Intent intent = new Intent(container.getContext(),LoansDetailActivity.class);
						intent.putExtra("GETFINANCINGINFO",getFinancingInfoModel);
						intent.putExtra("ACCOUNT_MODEL", accountsModel);
						container.getContext().startActivity(intent);

				        MainActivity mainActivity = (MainActivity)container.getContext();
				        mainActivity.overridePendingTransition(R.anim.zoomin, 0);
					}
				}
			}
		});
	}

    private void setDashboardUi(final GetFinancingInfoModel getFinancingInfoModel,
            final LoansRotateTableView rotatTableView0, RotateTitleView titleView, int index) {
        List<InstallmentsModel> list = getFinancingInfoModel.getInstallments();
        if (list == null) {
            return;
        }

    	Calendar now = Calendar.getInstance();
    	now.setTimeInMillis(System.currentTimeMillis());
    	now.add(Calendar.MONTH, -index);
    	int year = now.get(Calendar.YEAR);
    	int month = now.get(Calendar.MONTH);
    	
        int aimIndex = getYearMonthIndex(list , year, month);
        int todayIndex = getTodayIndex(list);
        
        LogManager.d("index"+ Integer.toString(index) +  "aimIndex"+ Integer.toString(aimIndex) + "todayIndex" + Integer.toString(todayIndex) 
        		+ "YEAR" + Integer.toString(year) + "MONTH" + Integer.toString(month));

        InstallmentsModel install = getInstallmentByIndex(list, aimIndex);

        /*
         *  The value at top
         */
        String amount="";
        if (install != null) {
        	amount = Utils.generateFormatMoney(container.getContext().getResources()
                .getString(R.string.eur), install.getAmount());
        } else {
        	amount = container.getContext().getResources().getString(R.string.not_able);
        }
        rotatTableView0.setInstallToValue(amount);

        /*
         * Deadline at top
         */
        String deadLine = "";
        if (install != null) {
        	deadLine = TimeUtil.getDateString(install.getDeadlineDate(), TimeUtil.dateFormat5);
//        	deadLine=TimeUtil.getDateString(now.getTimeInMillis(), TimeUtil.dateFormat5);
        }
        else {
        	deadLine = TimeUtil.getDateString(now.getTimeInMillis(), TimeUtil.dateFormat5);
        }
        rotatTableView0.setInstallToDate(deadLine);  //Rata al: “dd.mm.yy”	deadlineDate

        
        if (install != null) {
	        String paid = install.getPaidState();
	        boolean ispaid = Contants.PAID_STATUS.equals(paid);
	        rotatTableView0.setPaid(ispaid);
	    }

        /*
         * Prossima rata, The value ‘Prossima Rata’ must be fixed and don’t change during the rotation. 
		 * The fixed value to show is the initial value.
		 *
         */
        InstallmentsModel install_next = getInstallmentByIndex(list, todayIndex);
        if (install_next != null) {
        	String amount_next = Utils.generateFormatMoney(container.getContext().getResources()
                .getString(R.string.eur), install_next.getAmount());
        	rotatTableView0.setNext_InstallmentValue(amount_next);
        }
        else {
        	rotatTableView0.setNext_InstallmentValue(container.getContext()
					.getResources().getString(R.string.not_able));
        }

        /*
         * End Date. date at bottom.
         * The field must show the expiration date of the next installment.
         * EXAMPLE: 
			- If today is April, and next installment expires on 22. May; the field must show (under the value of next installment) the expiration date "22.05.14"
			- If next installment doesn't exist, the field must show: "n.d."
		 * When user moves the "time cursor" on the ring, the field doesn't change value
         */
        if (install_next != null) {
        	String enddate = TimeUtil.getDateString(install_next.getDeadlineDate(),TimeUtil.dateFormat5);
        	rotatTableView0.setDeadLine(enddate); 	     //Scad: “dd/mm/yy”	endDate
        } else {
        	rotatTableView0.setDeadLine(container.getContext()
					.getResources().getString(R.string.not_able));
        }
        
        /*
         * ResidualCapital
         */
        String residu = Utils.generateFormatMoney(container.getContext().getResources()
                .getString(R.string.eur), getFinancingInfoModel.getResidueAmount());
        rotatTableView0.setResidualCapitalValue(residu);

    }


    private int getTodayIndex(List<InstallmentsModel> installments) {
        int result = -1;
        int size = installments.size();
        for (int i = 0; i < size; i++) {
        	InstallmentsModel inModel = installments.get(i);
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTimeInMillis(inModel.getDeadlineDate());
        	Calendar now = Calendar.getInstance();
        	now.setTimeInMillis(System.currentTimeMillis());
        	if (now.before(calendar)) {
        		result = Math.min(i, size - 1);
        		break;
        	}
        	
        }
        return result;
    }

    private InstallmentsModel getInstallmentByIndex(List<InstallmentsModel> installments, int todayIndex) {
    	InstallmentsModel install = null;
    	
    	if (installments != null && todayIndex >= 0 && todayIndex< installments.size()) 
    		install = installments.get(todayIndex);
    	
    	return install;
    }
    
    private int getYearMonthIndex(List<InstallmentsModel> installments, int year, int month) {
        int result = -1;
        
        int size = installments.size();
        int rcent=-1;
        for (int i = 0; i < size; i++) {
        	InstallmentsModel inModel = installments.get(i);
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTimeInMillis(inModel.getDeadlineDate());

        	int rcent_tmp=(calendar.get(Calendar.YEAR)-year)*12+(calendar.get(Calendar.MONTH)-month);
        	if(rcent_tmp==0){
        		result=i;
        		rcent=0;
        		break;
        	}else if(rcent_tmp>0&&(rcent_tmp<rcent||rcent<0)){
        		result=i;
        		rcent=rcent_tmp;
        	}
        	
//        	if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month) {
//        		result = i;
//        	}
        }

        return result;
    }
    
    private InstallmentsModel getYearMonthInstallmentsModel(List<InstallmentsModel> installments, int year, int month) {
    	InstallmentsModel mInstallmentsModel=null;
        
        int size = installments.size();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < size; i++) {
        	InstallmentsModel inModel = installments.get(i);
        	calendar.setTimeInMillis(inModel.getDeadlineDate());

        	if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month) {
        		mInstallmentsModel = inModel;
        		break;
        	}
        }

        return mInstallmentsModel;
    }
    
    private InstallmentsModel getAfterYearMonthIndex(List<InstallmentsModel> installments, int year, int month) {
    	InstallmentsModel tempInstallmentsModel=null;
//        int result = -1;
        
        int size = installments.size();
        int rcent=-1;
        for (int i = 0; i < size; i++) {
        	InstallmentsModel inModel = installments.get(i);
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTimeInMillis(inModel.getDeadlineDate());
        	
        	int rcent_tmp=(calendar.get(Calendar.YEAR)-year)*12+(calendar.get(Calendar.MONTH)-month);
        	if(rcent_tmp==0){
//        		result=i;
        		rcent=0;
        		tempInstallmentsModel=inModel;
        		break;
        	}else if(rcent_tmp>0&&(rcent_tmp<rcent||rcent<0)){
//        		result=i;
        		rcent=rcent_tmp;
        		tempInstallmentsModel=inModel;
        	}
        }

        return tempInstallmentsModel;
    }
    
    private InstallmentsModel getPrevYearMonthIndex(List<InstallmentsModel> installments, int year, int month) {
    	InstallmentsModel tempInstallmentsModel=null;
//    	int result =-1;
        
        int size = installments.size();
        int rcent=1;
        for (int i = 0; i < size; i++) {
        	InstallmentsModel inModel = installments.get(i);
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTimeInMillis(inModel.getDeadlineDate());
        	
        	int rcent_tmp=(calendar.get(Calendar.YEAR)-year)*12+(calendar.get(Calendar.MONTH)-month);
        	if(rcent_tmp==0){
//        		result=i;
        		rcent=0;
        		tempInstallmentsModel=inModel;
        		break;
        	}else if(rcent_tmp<=0&&(rcent_tmp>rcent||rcent>0)){
//        		result=i;
        		rcent=rcent_tmp;
        		tempInstallmentsModel=inModel;
        	}
        	
//        	if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month) {
//        		result = i;
//        	}
        }

        return tempInstallmentsModel;
    }
    
    @Override
    public void showChart() {
         if (BaseActivity.isOffline) {
             SignLineFormView sinFormView = new SignLineFormView(chartLayout.getContext());
             sinFormView.setPersonizedName("franks' accont");
             sinFormView.setIsPreferred(true);
             chartLayout.removeAllViews();
             chartLayout.addView(sinFormView);

             return;
         }

        if (list==null || (list!=null && list.size() == 0)) {
            needShowChart = true;
            return;
        }
        if (chartLayout != null) {
            chartLayout.removeAllViews();

            SignLineFormView sinFormView = new SignLineFormView(chartLayout.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            List<String> xValues = new ArrayList<String>();
            int index = getVisiableRotateViewIndex();

            if (index >= 0 && index < list.size()) {
                AccountsModel accountsModel = list.get(index);
                List<Double> xYValues = new ArrayList<Double>();
                GetFinancingInfoModel getFinancingInfoModel = hashMap.get(accountsModel);
                if (getFinancingInfoModel == null) {
                    needShowChart = true;
                    return;
                }
                List<InstallmentsModel> installments = getFinancingInfoModel.getInstallments();
                // 91021 Not valid account code
                if (installments == null) {
                    return;
                }
                int todayIndex = getTodayIndex(installments);
                if (todayIndex == -1)
                	todayIndex = installments.size() - 1;

                List<InstallmentsModel> newInstallments = new ArrayList<InstallmentsModel>();
                Calendar now = Calendar.getInstance();
                now.setTimeInMillis(System.currentTimeMillis());
            	int year = now.get(Calendar.YEAR);
            	int month = now.get(Calendar.MONTH);
            	//find today record
            	InstallmentsModel curInstallmentsModel=getAfterYearMonthIndex(installments, year, month);
            	if(curInstallmentsModel!=null){
            		newInstallments.add(0,curInstallmentsModel);
            		String time=TimeUtil.getDateString(curInstallmentsModel.getDeadlineDate(), TimeUtil.detaFormat6);
            		xValues.add(0,time);
            	}
            	//find prev 12 records
                for (int i = 0; i <12; i++) {
                	now.add(Calendar.MONTH, -1);
                	year = now.get(Calendar.YEAR);
                	month = now.get(Calendar.MONTH);
                	InstallmentsModel findInstallmentsModel=getPrevYearMonthIndex(installments, year, month);
                	if(findInstallmentsModel!=null){
                			curInstallmentsModel=findInstallmentsModel;
                			newInstallments.add(0,curInstallmentsModel);
                			String time=TimeUtil.getDateString(curInstallmentsModel.getDeadlineDate(), TimeUtil.detaFormat6);
                			xValues.add(0,time);
                    		now.setTimeInMillis(curInstallmentsModel.getDeadlineDate());
                	}
                }
				// 今天剩下的
				double nowRes = 0;
				if (getFinancingInfoModel.getResidueAmount() != null
						&& !getFinancingInfoModel.getResidueAmount().equals("")) {
					nowRes = Double.parseDouble(getFinancingInfoModel
							.getResidueAmount());
				}
				
				
                // 过去还钱累积数量
                double[] yValuesss = new double[newInstallments.size()];
                for (int i = newInstallments.size() - 1; i >= 0; i--) {

                    InstallmentsModel inModel = newInstallments.get(i);
                    // The most right value is ResidueAmount
                    if (i == newInstallments.size() - 1) {
                        yValuesss[newInstallments.size() - 1] = nowRes;

                    } else {
                        double returns = yValuesss[i + 1];
                        if (inModel.getPaidState().equals(Contants.PAID_STATUS)) {
                            returns = returns + inModel.getAmount();
                        }
                        yValuesss[i] = returns;
                    }
                }
//                for (int i = 0; i < newInstallments.size(); i++) {
//                    InstallmentsModel inModel = newInstallments.get(i);
//                    // xYValues.add(inModel.getAmountCapitalShare());
//
//                    String time = TimeUtil.changeChartFormattrString(chartLayout.getContext(),inModel.getDeadlineDate(),
//                            TimeUtil.dateFormat2, TimeUtil.detaFormat6);
//                    xValues.add(time);
//                }
                for (int i = 0; i < yValuesss.length; i++) {
                    xYValues.add(yValuesss[i]);
                }
                sinFormView.xValue = xValues;
                sinFormView.xYValues = xYValues;
                String residu = Utils.generateFormatMoney(container.getContext()
                        .getResources().getString(R.string.eur),
                        getFinancingInfoModel.getResidueAmount());
                sinFormView.residualCapital = residu;
                sinFormView.initYValue();
                sinFormView.installments = newInstallments;
                sinFormView.getFinancingInfoModel = getFinancingInfoModel; 
                sinFormView.setPersonizedName(accountsModel.getAccountAlias());
                sinFormView.setIsPreferred(accountsModel.getIsPreferred());
                chartLayout.addView(sinFormView, params);
            }

        }

    }
    
	public void setCanOrientation() {
		MainActivity mainActivity = (MainActivity) container.getContext();

		if (BaseActivity.isOffline) {
			mainActivity.setCanOrientation(true);
			return;
		}

		if (hashMap == null || hashMap.size() == 0) {
			mainActivity.setCanOrientation(false);
		} else {
			mainActivity.setCanOrientation(true);
		}

	}
}
