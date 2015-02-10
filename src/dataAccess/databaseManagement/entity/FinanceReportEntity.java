package dataAccess.databaseManagement.entity;

import java.io.Serializable;

public class FinanceReportEntity implements Serializable, Comparable<FinanceReportEntity>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2983176085754760429L;

	long reportID;
	long assetID;
	int year;
	int quater;
	long sharesOutstanding;
	double EPS;
	/*
	 * Balance Sheet
	 */
	double currentAssets;
	double cashAndCashEquivalents;
	double shortTermFinancialInvestment;
	double shortTermAccountReceivables;
	double inventory;
	double otherCurrentAssets;
	double nonCurrentAssets;
	double longTermAccountReceivable;
	double fixedAssets;
	double tangibleFixedAssetsAccumulatedDepreciation;
	double leasingFixedAssetsAccumulatedDepreciation; 
	double intangibleFixedAssetsAccumulatedDepreciation;
	double goodwills;
	double realEstateInvestment;
	double longTermFinancialInvestments;
	double otherLongTermAssets;
	double totalAsset;
	double liabilities;
	double shortTermLiabilities;
	double longTermLiabilities;
	double provision; 
	double otherPayable;
	double ownersEquity;
	double expendituresAndOtherFunds;
	double ownersCapital;
	double minorityInterest;
	double totalEquity;
	
	/*
	 * Income Statement
	 */
	double grossSaleRevenues;
	double deductionRevenues;
	double netSales;
	double costOfGoodsSold;
	double grossProfit;
	double financialActivitiesRevenues;
	double financialExpenses;
	double sellingExpenses;
	double managingExpenses;
	double netProfitFromOperatingActivities;
	double otherIncomes;
	double otherExpenses;
	double otherProfits;
	double totalProfitBeforeTax;
	double corporateIncomeTaxExpenses;
	double profitAfterCorporateIncomeTax;
	double benefitsOfMinorityShareholders;

	public FinanceReportEntity(){
		
	}
	
	public FinanceReportEntity(int year, int quater, double currentAssets) {
		this.year = year;
		this.quater = quater;
		this.currentAssets = currentAssets;
	}

	

	public FinanceReportEntity(long assetID, int year,
			int quater, long sharesOutstanding, double EPS, double currentAssets,
			double cashAndCashEquivalents, double shortTermFinancialInvestment,
			double shortTermAccountReceivables, double inventory,
			double otherCurrentAssets, double nonCurrentAssets,
			double longTermAccountReceivable, double fixedAssets,
			double tangibleFixedAssetsAccumulatedDepreciation,
			double leasingFixedAssetsAccumulatedDepreciation,
			double intangibleFixedAssetsAccumulatedDepreciation,
			double commerceAdvantage, double realEstateInvestment,
			double longTermFinacialInvestments, double otherLongTermAssets,
			double totalAsset, double liabilities, double shortTermLiabilities,
			double longTermLiabilities, double provision, double otherPayable,
			double ownersEquity, double expendituresAndOtherFunds,
			double ownersCapital, double minorityInterest, double totalEquity,
			double grossSaleRevenues, double deductionRevenues,
			double netSales, double costOfGoodsSold, double grossProfit,
			double financialActivitiesRevenues, double financialExpenses,
			double sellingExpenses, double managingExpenses,
			double netProfitFromOperatingActivities, double otherIncomes,
			double otherExpenses, double otherProfits,
			double totalProfitBeforeTax, double corporateIncomeTaxExpenses,
			double profitAfterCorporateIncomeTax,
			double benefitsOfMinitoryShareholders) {
		super();
		
		this.assetID = assetID;
		this.year = year;
		this.quater = quater;
		this.sharesOutstanding = sharesOutstanding;
		this.EPS = EPS;
		this.currentAssets = currentAssets;
		this.cashAndCashEquivalents = cashAndCashEquivalents;
		this.shortTermFinancialInvestment = shortTermFinancialInvestment;
		this.shortTermAccountReceivables = shortTermAccountReceivables;
		this.inventory = inventory;
		this.otherCurrentAssets = otherCurrentAssets;
		this.nonCurrentAssets = nonCurrentAssets;
		this.longTermAccountReceivable = longTermAccountReceivable;
		this.fixedAssets = fixedAssets;
		this.tangibleFixedAssetsAccumulatedDepreciation = tangibleFixedAssetsAccumulatedDepreciation;
		this.leasingFixedAssetsAccumulatedDepreciation = leasingFixedAssetsAccumulatedDepreciation;
		this.intangibleFixedAssetsAccumulatedDepreciation = intangibleFixedAssetsAccumulatedDepreciation;
		this.goodwills = commerceAdvantage;
		this.realEstateInvestment = realEstateInvestment;
		this.longTermFinancialInvestments = longTermFinacialInvestments;
		this.otherLongTermAssets = otherLongTermAssets;
		this.totalAsset = totalAsset;
		this.liabilities = liabilities;
		this.shortTermLiabilities = shortTermLiabilities;
		this.longTermLiabilities = longTermLiabilities;
		this.provision = provision;
		this.otherPayable = otherPayable;
		this.ownersEquity = ownersEquity;
		this.expendituresAndOtherFunds = expendituresAndOtherFunds;
		this.ownersCapital = ownersCapital;
		this.minorityInterest = minorityInterest;
		this.totalEquity = totalEquity;
		this.grossSaleRevenues = grossSaleRevenues;
		this.deductionRevenues = deductionRevenues;
		this.netSales = netSales;
		this.costOfGoodsSold = costOfGoodsSold;
		this.grossProfit = grossProfit;
		this.financialActivitiesRevenues = financialActivitiesRevenues;
		this.financialExpenses = financialExpenses;
		this.sellingExpenses = sellingExpenses;
		this.managingExpenses = managingExpenses;
		this.netProfitFromOperatingActivities = netProfitFromOperatingActivities;
		this.otherIncomes = otherIncomes;
		this.otherExpenses = otherExpenses;
		this.otherProfits = otherProfits;
		this.totalProfitBeforeTax = totalProfitBeforeTax;
		this.corporateIncomeTaxExpenses = corporateIncomeTaxExpenses;
		this.profitAfterCorporateIncomeTax = profitAfterCorporateIncomeTax;
		this.benefitsOfMinorityShareholders = benefitsOfMinitoryShareholders;
	}

	public long getReportID() {
		return reportID;
	}

	public void setReportID(long reportID) {
		this.reportID = reportID;
	}

	public long getAssetID() {
		return assetID;
	}

	public void setAssetID(long assetID) {
		this.assetID = assetID;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getQuater() {
		return quater;
	}

	public void setQuater(int quater) {
		this.quater = quater;
	}

	public long getSharesOutstanding() {
		return sharesOutstanding;
	}

	public void setSharesOutstanding(long sharesOutstanding) {
		this.sharesOutstanding = sharesOutstanding;
	}
	
	public double getEPS() {
		return EPS;
	}

	public void setEPS(double EPS) {
		this.EPS = EPS;
	}


	public double getCurrentAssets() {
		return currentAssets;
	}

	public void setCurrentAssets(double currentAssets) {
		this.currentAssets = currentAssets;
	}

	public double getCashAndCashEquivalents() {
		return cashAndCashEquivalents;
	}

	public void setCashAndCashEquivalents(double cashAndCashEquivalents) {
		this.cashAndCashEquivalents = cashAndCashEquivalents;
	}

	public double getShortTermFinancialInvestment() {
		return shortTermFinancialInvestment;
	}

	public void setShortTermFinancialInvestment(double shortTermFinancialInvestment) {
		this.shortTermFinancialInvestment = shortTermFinancialInvestment;
	}

	public double getShortTermAccountReceivables() {
		return shortTermAccountReceivables;
	}

	public void setShortTermAccountReceivables(double shortTermAccountReceivables) {
		this.shortTermAccountReceivables = shortTermAccountReceivables;
	}

	public double getInventory() {
		return inventory;
	}

	public void setInventory(double inventory) {
		this.inventory = inventory;
	}

	public double getOtherCurrentAssets() {
		return otherCurrentAssets;
	}

	public void setOtherCurrentAssets(double otherCurrentAssets) {
		this.otherCurrentAssets = otherCurrentAssets;
	}

	public double getNonCurrentAssets() {
		return nonCurrentAssets;
	}

	public void setNonCurrentAssets(double nonCurrentAssets) {
		this.nonCurrentAssets = nonCurrentAssets;
	}

	public double getLongTermAccountReceivable() {
		return longTermAccountReceivable;
	}

	public void setLongTermAccountReceivable(double longTermAccountReceivable) {
		this.longTermAccountReceivable = longTermAccountReceivable;
	}

	public double getFixedAssets() {
		return fixedAssets;
	}

	public void setFixedAssets(double fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	public double getTangibleFixedAssetsAccumulatedDepreciation() {
		return tangibleFixedAssetsAccumulatedDepreciation;
	}

	public void setTangibleFixedAssetsAccumulatedDepreciation(
			double tangibleFixedAssetsAccumulatedDepreciation) {
		this.tangibleFixedAssetsAccumulatedDepreciation = tangibleFixedAssetsAccumulatedDepreciation;
	}

	public double getLeasingFixedAssetsAccumulatedDepreciation() {
		return leasingFixedAssetsAccumulatedDepreciation;
	}

	public void setLeasingFixedAssetsAccumulatedDepreciation(
			double leasingFixedAssetsAccumulatedDepreciation) {
		this.leasingFixedAssetsAccumulatedDepreciation = leasingFixedAssetsAccumulatedDepreciation;
	}

	public double getIntangibleFixedAssetsAccumulatedDepreciation() {
		return intangibleFixedAssetsAccumulatedDepreciation;
	}

	public void setIntangibleFixedAssetsAccumulatedDepreciation(
			double intangibleFixedAssetsAccumulatedDepreciation) {
		this.intangibleFixedAssetsAccumulatedDepreciation = intangibleFixedAssetsAccumulatedDepreciation;
	}

	public double getGoodwills() {
		return goodwills;
	}

	public void setGoodwills(double goodwills) {
		this.goodwills = goodwills;
	}

	public double getRealEstateInvestment() {
		return realEstateInvestment;
	}

	public void setRealEstateInvestment(double realEstateInvestment) {
		this.realEstateInvestment = realEstateInvestment;
	}

	public double getLongTermFinancialInvestments() {
		return longTermFinancialInvestments;
	}

	public void setLongTermFinancialInvestments(double longTermFinancialInvestments) {
		this.longTermFinancialInvestments = longTermFinancialInvestments;
	}

	public double getOtherLongTermAssets() {
		return otherLongTermAssets;
	}

	public void setOtherLongTermAssets(double otherLongTermAssets) {
		this.otherLongTermAssets = otherLongTermAssets;
	}

	public double getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(double totalAsset) {
		this.totalAsset = totalAsset;
	}

	public double getLiabilities() {
		return liabilities;
	}

	public void setLiabilities(double liabilities) {
		this.liabilities = liabilities;
	}

	public double getShortTermLiabilities() {
		return shortTermLiabilities;
	}

	public void setShortTermLiabilities(double shortTermLiabilities) {
		this.shortTermLiabilities = shortTermLiabilities;
	}

	public double getLongTermLiabilities() {
		return longTermLiabilities;
	}

	public void setLongTermLiabilities(double longTermLiabilities) {
		this.longTermLiabilities = longTermLiabilities;
	}

	public double getProvision() {
		return provision;
	}

	public void setProvision(double provision) {
		this.provision = provision;
	}

	public double getOtherPayable() {
		return otherPayable;
	}

	public void setOtherPayable(double otherPayable) {
		this.otherPayable = otherPayable;
	}

	public double getOwnersEquity() {
		return ownersEquity;
	}

	public void setOwnersEquity(double ownersEquity) {
		this.ownersEquity = ownersEquity;
	}

	public double getExpendituresAndOtherFunds() {
		return expendituresAndOtherFunds;
	}

	public void setExpendituresAndOtherFunds(double expendituresAndOtherFunds) {
		this.expendituresAndOtherFunds = expendituresAndOtherFunds;
	}

	public double getOwnersCapital() {
		return ownersCapital;
	}

	public void setOwnersCapital(double ownersCapital) {
		this.ownersCapital = ownersCapital;
	}

	public double getMinitoryInterest() {
		return minorityInterest;
	}

	public void setMinorityInterest(double minorityInterest) {
		this.minorityInterest = minorityInterest;
	}

	public double getTotalEquity() {
		return totalEquity;
	}

	public void setTotalEquity(double totalEquity) {
		this.totalEquity = totalEquity;
	}

	public double getGrossSaleRevenues() {
		return grossSaleRevenues;
	}

	public void setGrossSaleRevenues(double grossSaleRevenues) {
		this.grossSaleRevenues = grossSaleRevenues;
	}

	public double getDeductionRevenues() {
		return deductionRevenues;
	}

	public void setDeductionRevenues(double deductionRevenues) {
		this.deductionRevenues = deductionRevenues;
	}

	public double getNetSales() {
		return netSales;
	}

	public void setNetSales(double netSales) {
		this.netSales = netSales;
	}

	public double getCostOfGoodsSold() {
		return costOfGoodsSold;
	}

	public void setCostOfGoodsSold(double costOfGoodsSold) {
		this.costOfGoodsSold = costOfGoodsSold;
	}

	public double getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(double grossProfit) {
		this.grossProfit = grossProfit;
	}

	public double getFinancialActivitiesRevenues() {
		return financialActivitiesRevenues;
	}

	public void setFinancialActivitiesRevenues(double financialActivitiesRevenues) {
		this.financialActivitiesRevenues = financialActivitiesRevenues;
	}

	public double getFinancialExpenses() {
		return financialExpenses;
	}

	public void setFinancialExpenses(double financialExpenses) {
		this.financialExpenses = financialExpenses;
	}

	public double getSellingExpenses() {
		return sellingExpenses;
	}

	public void setSellingExpenses(double sellingExpenses) {
		this.sellingExpenses = sellingExpenses;
	}

	public double getManagingExpenses() {
		return managingExpenses;
	}

	public void setManagingExpenses(double managingExpenses) {
		this.managingExpenses = managingExpenses;
	}

	public double getNetProfitFromOperatingActivities() {
		return netProfitFromOperatingActivities;
	}

	public void setNetProfitFromOperatingActivities(
			double netProfitFromOperatingActivities) {
		this.netProfitFromOperatingActivities = netProfitFromOperatingActivities;
	}

	public double getOtherIncomes() {
		return otherIncomes;
	}

	public void setOtherIncomes(double otherIncomes) {
		this.otherIncomes = otherIncomes;
	}

	public double getOtherExpenses() {
		return otherExpenses;
	}

	public void setOtherExpenses(double otherExpenses) {
		this.otherExpenses = otherExpenses;
	}

	public double getOtherProfits() {
		return otherProfits;
	}

	public void setOtherProfits(double otherProfits) {
		this.otherProfits = otherProfits;
	}

	public double getTotalProfitBeforeTax() {
		return totalProfitBeforeTax;
	}

	public void setTotalProfitBeforeTax(double totalProfitBeforeTax) {
		this.totalProfitBeforeTax = totalProfitBeforeTax;
	}

	public double getCorporateIncomeTaxExpenses() {
		return corporateIncomeTaxExpenses;
	}

	public void setCorporateIncomeTaxExpenses(double corporateIncomeTaxExpenses) {
		this.corporateIncomeTaxExpenses = corporateIncomeTaxExpenses;
	}

	public double getProfitAfterCorporateIncomeTax() {
		return profitAfterCorporateIncomeTax;
	}

	public void setProfitAfterCorporateIncomeTax(
			double profitAfterCorporateIncomeTax) {
		this.profitAfterCorporateIncomeTax = profitAfterCorporateIncomeTax;
	}

	public double getBenefitsOfMinitoryShareholders() {
		return benefitsOfMinorityShareholders;
	}

	public void setBenefitsOfMinitoryShareholders(
			double benefitsOfMinitoryShareholders) {
		this.benefitsOfMinorityShareholders = benefitsOfMinitoryShareholders;
	}

	
	@Override
	// return true if this > o
	public int compareTo(FinanceReportEntity o) {
		return new Integer(this.year * 5 + this.quater).compareTo(o.getYear() * 5 + o.getQuater());
	}
	
	
	@Override
	public String toString() {
		return this.year + "\t" + this.quater;
	}
	

}
