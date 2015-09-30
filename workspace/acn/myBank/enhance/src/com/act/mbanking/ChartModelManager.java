package com.act.mbanking;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.AggregatedAccount;
import com.act.mbanking.bean.ChartModel;
import com.act.mbanking.utils.ColorManager;
import com.act.mbanking.view.ChartModelMapTool;
import com.act.mbanking.view.ChartView;
import com.custom.view.RectLD;

public final class ChartModelManager {
	private static final List<ChartModelMapTool> baseAccountChartModels = new ArrayList<ChartModelMapTool>();
	
	public static List<ChartModelMapTool> getBaseAccountChartModels(){
		if(baseAccountChartModels.size()<=0){
			
//			StringBuffer str=new StringBuffer();
//			StringBuffer str1=new StringBuffer();
			List<AccountsModel> list = Contants.baseAccounts;
			
			for (int i = 0; i < list.size(); i++) {
//				str.append(i);
//				str1.append(i);
				AccountsModel accountsModel = list.get(i);
//				str.append(" account:"+accountsModel.getAccountAlias()+"\n");
//				str1.append(" account:"+accountsModel.getAccountAlias()+"\n");
				List<AggregatedAccount> chartAccounts = accountsModel.chartAggregatedAccountsList;
				for (int j = 0; j < chartAccounts.size(); j++) {
					ChartModelMapTool chartModelMapTool = new ChartModelMapTool();
					chartModelMapTool.title = accountsModel.getAccountAlias();
					AggregatedAccount aggregatedAccount = chartAccounts.get(0);
					chartModelMapTool.setSrcList(aggregatedAccount.getCharts());
					baseAccountChartModels.add(chartModelMapTool);
					chartModelMapTool.color = ColorManager.getAccountColor(i + 2);
					chartModelMapTool.type = ChartModelMapTool.type_accounts;
					chartModelMapTool.level = i + 2;
					
//					str.append(j+"\n");
//					str=toString(str,chartModelMapTool.getSrcList());
//					str1.append(j+"\n");
//					str1=toString(str1,chartModelMapTool.getYearList());
				}
				
			}
//			writeSdcardFile("/sdcard/getBaseAccountChartModels_src.txt", str.toString().getBytes());
//			writeSdcardFile("/sdcard/getBaseAccountChartModels_days.txt", str1.toString().getBytes());
		}
		return baseAccountChartModels;
	}
	
	private static final List<ChartModelMapTool> cardChartModels = new ArrayList<ChartModelMapTool>();
	public static List<ChartModelMapTool> getCardChartModels(){
		if(cardChartModels.size()<=0){
			
//			StringBuffer str=new StringBuffer();
//			StringBuffer str1=new StringBuffer();
			List<AccountsModel> list = Contants.prepaidCardAccounts;
			
			for (int i = 0; i < list.size(); i++) {
//				str.append(i);
//				str1.append(i);
				
				AccountsModel accountsModel = list.get(i);
//				str.append(" account:"+accountsModel.getAccountAlias()+"\n");
//				str1.append(" account:"+accountsModel.getAccountAlias()+"\n");
				List<AggregatedAccount> chartAccounts = accountsModel.chartAggregatedAccountsList;
				for (int j = 0; j < chartAccounts.size(); j++) {
					ChartModelMapTool chartModelMapTool = new ChartModelMapTool();
					AggregatedAccount aggregatedAccount = chartAccounts.get(0);
					chartModelMapTool.setSrcList(aggregatedAccount.getCharts());
					chartModelMapTool.title = accountsModel.getAccountAlias();
					cardChartModels.add(chartModelMapTool);
					chartModelMapTool.color = ColorManager.getPrepaidCardsColor(i + 2);
					chartModelMapTool.level = i + 2;
					chartModelMapTool.type = ChartModelMapTool.type_prepaieds;
					
//					str.append(j+"\n");
//					str=toString(str,chartModelMapTool.getSrcList());
//					str1.append(j+"\n");
//					str1=toString(str1,chartModelMapTool.getOutList());
				}
				
			}
			list = Contants.creditCardAccounts;
			
			for (int i = 0; i < list.size(); i++) {
//				str.append(i);
//				str1.append(i);
				
				AccountsModel accountsModel = list.get(i);
//				str.append(" account:"+accountsModel.getAccountAlias()+"\n");
//				str1.append(" account:"+accountsModel.getAccountAlias()+"\n");
				List<AggregatedAccount> chartAccounts = accountsModel.chartAggregatedAccountsList;
				for (int j = 0; j < chartAccounts.size(); j++) {
					ChartModelMapTool chartModelMapTool = new ChartModelMapTool();
					AggregatedAccount aggregatedAccount = chartAccounts.get(0);
					chartModelMapTool.setSrcList(aggregatedAccount.getCharts());
					chartModelMapTool.title = accountsModel.getAccountAlias();
					cardChartModels.add(chartModelMapTool);
					chartModelMapTool.color = ColorManager.getCreditCardsColor((i + 2));
					chartModelMapTool.level = i + 2;
					chartModelMapTool.type = ChartModelMapTool.type_credit;
					
//					str.append(j+"\n");
//					str=toString(str,chartModelMapTool.getSrcList());
//					str1.append(j+"\n");
//					str1=toString(str1,chartModelMapTool.getOutList());
				}
				
			}
//			writeSdcardFile("/sdcard/getCardChartModels_src.txt", str.toString().getBytes());
//			writeSdcardFile("/sdcard/getCardChartModels_days.txt", str1.toString().getBytes());
		}
		return cardChartModels;
	}
	
	private static final List<ChartModelMapTool> investmentChartModels = new ArrayList<ChartModelMapTool>();
	
	public static List<ChartModelMapTool> getInvestmentChartModels() {
		if (investmentChartModels.size() <= 0) {
//			StringBuffer str=new StringBuffer();
//			StringBuffer str1=new StringBuffer();
			List<AccountsModel> list = Contants.investmentAccounts;
			for (int i = 0; i < list.size(); i++) {
//				str.append(i);
//				str1.append(i);
				AccountsModel accountsModel = list.get(i);
//				str.append(" account:"+accountsModel.getAccountAlias()+"\n");
//				str1.append(" account:"+accountsModel.getAccountAlias()+"\n");
				List<AggregatedAccount> chartAccounts = accountsModel.chartAggregatedAccountsList;
				for (int j = 0; j < chartAccounts.size(); j++) {
					ChartModelMapTool chartModelMapTool = new ChartModelMapTool();
					AggregatedAccount aggregatedAccount = chartAccounts.get(0);
					chartModelMapTool.setSrcList(aggregatedAccount.getCharts());
					chartModelMapTool.title = accountsModel.getAccountAlias();
					investmentChartModels.add(chartModelMapTool);
					chartModelMapTool.color = ColorManager
					.getInvestmentColor(i + 2);
					chartModelMapTool.level = i + 2;
					chartModelMapTool.type = ChartModelMapTool.type_investments;
					
//					str.append(j+"\n");
//					str=toString(str,chartModelMapTool.getSrcList());
//					str1.append(j+"\n");
//					str1=toString(str1,chartModelMapTool.getOutList());
				}
				
			}
//			writeSdcardFile("/sdcard/InvestmentChartModels_src.txt", str.toString().getBytes());
//			writeSdcardFile("/sdcard/InvestmentChartModels_days.txt", str1.toString().getBytes());
		}
		return investmentChartModels;}
	
	private static final List<ChartModelMapTool> loansChartModels = new ArrayList<ChartModelMapTool>();
	
	public static List<ChartModelMapTool> getLoansChartModels() {
		if (loansChartModels.size() <= 0) {
//			StringBuffer str=new StringBuffer();
//			StringBuffer str1=new StringBuffer();
			
			List<AccountsModel> list = Contants.loansAccounts;
			for (int i = 0; i < list.size(); i++) {
//				str.append(i);
//				str1.append(i);
				
				AccountsModel accountsModel = list.get(i);
//				str.append(" account:"+accountsModel.getAccountAlias()+"\n");
//				str1.append(" account:"+accountsModel.getAccountAlias()+"\n");
				List<AggregatedAccount> chartAccounts = accountsModel.chartAggregatedAccountsList;
				for (int j = 0; j < chartAccounts.size(); j++) {
					ChartModelMapTool chartModelMapTool = new ChartModelMapTool();
					AggregatedAccount aggregatedAccount = chartAccounts.get(j);
					chartModelMapTool.setSrcList(aggregatedAccount.getCharts());
					chartModelMapTool.title = accountsModel.getAccountAlias();
					loansChartModels.add(chartModelMapTool);
					chartModelMapTool.color = ColorManager.getLoansColor(i + 2);
					chartModelMapTool.level = i + 2;
					chartModelMapTool.type = ChartModelMapTool.type_loans;
//					
//					str.append(j+"\n");
//					str=toString(str,chartModelMapTool.getSrcList());
//					str1.append(j+"\n");
//					str1=toString(str1,chartModelMapTool.getOutList());
				}
				
			}
//			writeSdcardFile("/sdcard/getLoansChartModels_src.txt", str.toString().getBytes());
//			writeSdcardFile("/sdcard/getLoansChartModels_days.txt", str1.toString().getBytes());
		}
		return loansChartModels;
	}
	
	

    private static List<ChartModelMapTool> allChartModelMapTools;

    public static List<ChartModelMapTool> getAllChartModelMapTools(Context cxt) {

        if (allChartModelMapTools == null||allChartModelMapTools.size()==0) {
            allChartModelMapTools = generateChartModelMapTool(cxt);
        }
        return allChartModelMapTools;
    }

    public static List<ChartModelMapTool> generateChartModelMapTool(Context cxt) {
        List<ChartModel> accountChartModels = new ArrayList<ChartModel>();
        List<ChartModel> prepaidCardChartModels = new ArrayList<ChartModel>();
        List<ChartModel> creditCardChartModels = new ArrayList<ChartModel>();
        List<ChartModel> investmentsChartModels = new ArrayList<ChartModel>();
        List<ChartModel> loansChartModels = new ArrayList<ChartModel>();
        List<ChartModel> pushChartModels = new ArrayList<ChartModel>();

        accountChartModels = getChartModel(getBaseAccountChartModels());
        prepaidCardChartModels = getChartModels(Contants.prepaidCardAccounts);
        creditCardChartModels = getChartModels(Contants.creditCardAccounts);
        investmentsChartModels = getChartModel(getInvestmentChartModels());
        loansChartModels = getChartModel(getLoansChartModels());
        pushChartModels = getChartModels(Contants.pushAccounts);

        List<ChartModelMapTool> currentChartModelMapTools = new ArrayList<ChartModelMapTool>();
        ChartModelMapTool chartModelMapTool = new ChartModelMapTool();
        chartModelMapTool.setSrcList(accountChartModels);
        chartModelMapTool.color = ColorManager.getAccountColor(0);
        chartModelMapTool.type = ChartModelMapTool.type_accounts;
        chartModelMapTool.title = cxt.getString(R.string.legend_accounts);
        currentChartModelMapTools.add(chartModelMapTool);

        chartModelMapTool = new ChartModelMapTool();
        chartModelMapTool.setSrcList(loansChartModels);
        chartModelMapTool.color = ColorManager.getLoansColor(0);
        chartModelMapTool.type = ChartModelMapTool.type_loans;
        chartModelMapTool.title = cxt.getString(R.string.legend_total_loans);
        currentChartModelMapTools.add(chartModelMapTool);

        chartModelMapTool = new ChartModelMapTool();
        chartModelMapTool.setSrcList(investmentsChartModels);
        currentChartModelMapTools.add(chartModelMapTool);
        chartModelMapTool.color = ColorManager.getInvestmentColor(0);
        chartModelMapTool.type = ChartModelMapTool.type_investments;
        chartModelMapTool.title = cxt.getString(R.string.legend_investments);

        chartModelMapTool = new ChartModelMapTool();
        chartModelMapTool.setSrcList(prepaidCardChartModels);
        currentChartModelMapTools.add(chartModelMapTool);
        chartModelMapTool.color = ColorManager.getPrepaidCardsColor(0);
        chartModelMapTool.type = ChartModelMapTool.type_prepaieds;
        chartModelMapTool.title = cxt.getString(R.string.legend_prepaid_cards);

        chartModelMapTool = new ChartModelMapTool();
        chartModelMapTool.setSrcList(creditCardChartModels);
        currentChartModelMapTools.add(chartModelMapTool);
        chartModelMapTool.color = ColorManager.getCreditCardsColor(0);
        chartModelMapTool.type = ChartModelMapTool.type_credit;
        chartModelMapTool.title = cxt.getString(R.string.legend_credit_cards);

        return currentChartModelMapTools;
    }

    public static List<ChartModel> getChartModels(List<AccountsModel> accountModels) {
        List<ChartModel> chartModels = new ArrayList<ChartModel>();
        if (accountModels != null) {
            for (int j = 0; j < accountModels.size(); j++) {
                AccountsModel acountModel = accountModels.get(j);
                List<AggregatedAccount> list = acountModel.getChartAggregatedAccountsList();
                if (list != null) {
                    List<AggregatedAccount> aggregatedAccounts = list;
                    for (int i = 0; i < aggregatedAccounts.size(); i++) {
                        List<ChartModel> newPrepaidChartChartModel = list.get(i).getCharts();
                        chartModels.addAll(newPrepaidChartChartModel);
                    }
                }
            }
        }
        return chartModels;
    }
    


    public static List<ChartModel> getChartModel( List<ChartModelMapTool> chartModelMapTools) {
        List<ChartModel> chartModels = new ArrayList<ChartModel>();
        
        if (chartModelMapTools != null) {
            for (int j = 0; j < chartModelMapTools.size(); j++) {
            	ChartModelMapTool chartModelMapTool =chartModelMapTools.get(j);
            	if(chartModelMapTool!=null&&chartModelMapTool.getYearList()!=null){
            		chartModels.addAll(chartModelMapTool.getYearList());
            	}
            }
        }
        return chartModels;
    }
	
	public static StringBuffer toString(StringBuffer str,List<ChartModel>list){
		if(str==null){
			str=new StringBuffer();
		}
		if(list!=null){
			int size=list.size();
			for(int i=0;i<size;i++){str.append(i);
			ChartModel cm=list.get(i);
			if(cm==null){
				str.append(" is null \n");
				continue;
			}
			str.append(" "+cm.getDate()+" "+cm.getValue()+" "+cm.getBeforeAdded()+" "+cm.getAfterAdded()+"\n");
			}
		}
		return str;
	}
	
	public static void writeSdcardFile(String name,byte[]data){
		try {
			FileOutputStream fos=new FileOutputStream(name);
			fos.write(data);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void logout(){
		baseAccountChartModels.clear();
		cardChartModels.clear();
		investmentChartModels.clear();
		loansChartModels.clear();
		if(allChartModelMapTools!=null)
		allChartModelMapTools.clear();
	}
}
