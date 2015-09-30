package com.accenture.mbank.model;

public class AccountsModel extends Account implements Comparable<AccountsModel> {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private String accountId;

	private String accountCode;

	private String ibanCode;

	private String isInformative;

	private String accountAlias;
	
	private String title;

	private String branchCode;

	private String bankCode;

	private String currencyAcc;

	private String frontendCategory;

	private String cabCode;

	private String mortageType;

	private String financeType;

	private String cardHolder;

	private String cardName;

	private String cardState;

	private String phoneNumber;

	private String notes;

	private String benchmarksDesc;

	private double benchmarksValue;

	private double rate;

	private String openingDate;

	private String prodAccount;

	private String prodAccountDesc;

	private String preferred;

	private BankServiceType bankServiceType;

	private String typeClient;

	private String aggregatedCardCode;

	private String CardRelations;

	private String portafoglio;

	private boolean isPreferred;

	private double plafond;

	private String expirationDate;

	private String accountType;

	private String relatedNdg;

	private String ndg;

	private String overridePerim;

	private String overrideIsInfo;

	private String onlinePost;

	private String cardExpirationDate;

	private String overrideCode;

	private double accountBalance;

	private double availableBalance;

	/**
	 * @return the expirationDate
	 */
	public String getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * xuegang added 20130131 card number optional
	 */
	private String cardNumber;

	public String getTypeClient() {
		return typeClient;
	}

	public void setTypeClient(String typeClient) {
		this.typeClient = typeClient;
	}

	public String getAggregatedCardCode() {
		return aggregatedCardCode;
	}

	public void setAggregatedCardCode(String aggregatedCardCode) {
		this.aggregatedCardCode = aggregatedCardCode;
	}

	public String getCardRelations() {
		return CardRelations;
	}

	public void setCardRelations(String cardRelations) {
		CardRelations = cardRelations;
	}

	public String getPortafoglio() {
		return portafoglio;
	}

	public void setPortafoglio(String portafoglio) {
		this.portafoglio = portafoglio;
	}

	public boolean getIsPreferred() {
		return isPreferred;
	}

	public void setIsPreferred(boolean isPreferred) {
		this.isPreferred = isPreferred;
	}

	/**
	 * @return the cardHolder
	 */
	public String getCardHolder() {
		return cardHolder;
	}

	/**
	 * @param cardHolder
	 *            the cardHolder to set
	 */
	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	/**
	 * @return the cardName
	 */
	public String getCardName() {
		return cardName;
	}

	/**
	 * @param cardName
	 *            the cardName to set
	 */
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	/**
	 * @return the cardState
	 */
	public String getCardState() {
		return cardState;
	}

	/**
	 * @param cardState
	 *            the cardState to set
	 */
	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	public double getPlafond() {
		return plafond;
	}

	public void setPlafond(double plafond) {
		this.plafond = plafond;
	}

	public String getFinanceType() {
		return financeType;
	}

	public void setFinanceType(String financeType) {
		this.financeType = financeType;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(final String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getMortageType() {
		return mortageType;
	}

	public void setMortageType(String mortageType) {
		this.mortageType = mortageType;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getIbanCode() {
		return ibanCode;
	}

	public void setIbanCode(String ibanCode) {
		this.ibanCode = ibanCode;
	}

	public String getIsInformative() {
		return isInformative;
	}

	public void setIsInformative(String isInformative) {
		this.isInformative = isInformative;
	}

	public String getAccountAlias() {
		return accountAlias;
	}

	public void setAccountAlias(String accountAlias) {
		this.accountAlias = accountAlias;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCurrencyAcc() {
		return currencyAcc;
	}

	public void setCurrencyAcc(String currencyAcc) {
		this.currencyAcc = currencyAcc;
	}

	public String getFrontendCategory() {
		return frontendCategory;
	}

	public void setFrontendCategory(String frontendCategory) {
		this.frontendCategory = frontendCategory;
	}

	public String getCabCode() {
		return cabCode;
	}

	public void setCabCode(String cabCode) {
		this.cabCode = cabCode;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}

	public String getProdAccount() {
		return prodAccount;
	}

	public void setProdAccount(String prodAccount) {
		this.prodAccount = prodAccount;
	}

	public String getProdAccountDesc() {
		return prodAccountDesc;
	}

	public void setProdAccountDesc(String prodAccountDesc) {
		this.prodAccountDesc = prodAccountDesc;
	}

	public String getPreferred() {
		return preferred;
	}

	public void setPreferred(String preferred) {
		this.preferred = preferred;
	}

	public BankServiceType getBankServiceType() {
		return bankServiceType;
	}

	public void setBankServiceType(BankServiceType bankServiceType) {
		this.bankServiceType = bankServiceType;
	}

	public String getBenchmarksDesc() {
		return benchmarksDesc;
	}

	public void setBenchmarksDesc(String benchmarksDesc) {
		this.benchmarksDesc = benchmarksDesc;
	}

	public double getBenchmarksValue() {
		return benchmarksValue;
	}

	public void setBenchmarksValue(double benchmarksValue) {
		this.benchmarksValue = benchmarksValue;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountsModel other = (AccountsModel) obj;
		if (accountAlias == null) {
			if (other.accountAlias != null)
				return false;
		} else if (!accountAlias.equals(other.accountAlias))
			return false;
		if (accountCode == null) {
			if (other.accountCode != null)
				return false;
		} else if (!accountCode.equals(other.accountCode))
			return false;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (bankCode == null) {
			if (other.bankCode != null)
				return false;
		} else if (!bankCode.equals(other.bankCode))
			return false;
		if (bankServiceType == null) {
			if (other.bankServiceType != null)
				return false;
		} else if (!bankServiceType.equals(other.bankServiceType))
			return false;
		if (branchCode == null) {
			if (other.branchCode != null)
				return false;
		} else if (!branchCode.equals(other.branchCode))
			return false;
		if (cabCode == null) {
			if (other.cabCode != null)
				return false;
		} else if (!cabCode.equals(other.cabCode))
			return false;
		if (currencyAcc == null) {
			if (other.currencyAcc != null)
				return false;
		} else if (!currencyAcc.equals(other.currencyAcc))
			return false;
		if (frontendCategory == null) {
			if (other.frontendCategory != null)
				return false;
		} else if (!frontendCategory.equals(other.frontendCategory))
			return false;
		if (ibanCode == null) {
			if (other.ibanCode != null)
				return false;
		} else if (!ibanCode.equals(other.ibanCode))
			return false;
		if (isInformative == null) {
			if (other.isInformative != null)
				return false;
		} else if (!isInformative.equals(other.isInformative))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (mortageType == null) {
			if (other.mortageType != null)
				return false;
		} else if (!mortageType.equals(other.mortageType))
			return false;
		if (openingDate == null) {
			if (other.openingDate != null)
				return false;
		} else if (!openingDate.equals(other.openingDate))
			return false;
		if (preferred == null) {
			if (other.preferred != null)
				return false;
		} else if (!preferred.equals(other.preferred))
			return false;
		if (prodAccount == null) {
			if (other.prodAccount != null)
				return false;
		} else if (!prodAccount.equals(other.prodAccount))
			return false;
		if (prodAccountDesc == null) {
			if (other.prodAccountDesc != null)
				return false;
		} else if (!prodAccountDesc.equals(other.prodAccountDesc))
			return false;
		return true;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getRelatedNdg() {
		return relatedNdg;
	}

	public void setRelatedNdg(String relatedNdg) {
		this.relatedNdg = relatedNdg;
	}

	public String getNdg() {
		return ndg;
	}

	public void setNdg(String ndg) {
		this.ndg = ndg;
	}

	public String getOverridePerim() {
		return overridePerim;
	}

	public void setOverridePerim(String overridePerim) {
		this.overridePerim = overridePerim;
	}

	public String getOverrideIsInfo() {
		return overrideIsInfo;
	}

	public void setOverrideIsInfo(String overrideIsInfo) {
		this.overrideIsInfo = overrideIsInfo;
	}

	public String getOnlinePost() {
		return onlinePost;
	}

	public void setOnlinePost(String onlinePost) {
		this.onlinePost = onlinePost;
	}

	public String getCardExpirationDate() {
		return cardExpirationDate;
	}

	public void setCardExpirationDate(String cardExpirationDate) {
		this.cardExpirationDate = cardExpirationDate;
	}

	public String getOverrideCode() {
		return overrideCode;
	}

	public void setOverrideCode(String overrideCode) {
		this.overrideCode = overrideCode;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	@Override
	public int compareTo(AccountsModel another) {
		if(getAccountCode()==null){
			return -1;
		}
		return getAccountCode().compareTo(another.getAccountCode());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
