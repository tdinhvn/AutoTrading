package business.fundamentalModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.FinanceReportEntity;
import dataAccess.databaseManagement.manager.ExchangeManager;
import dataAccess.databaseManagement.manager.FinanceReportManager;
import dataAccess.databaseManagement.manager.PriceManager;

public class AssetStat implements Comparable<AssetStat>{
	AssetEntity assetEntity;
	TreeMap<String, Double> statList;
	ArrayList<FinanceReportEntity> reportList;
	Date date;
	FinanceReportEntity latestReport;
	

	public AssetStat() {		
	}
	
	public AssetStat(AssetEntity assetEntity, Date date) {
		this.assetEntity = assetEntity;
		this.reportList = (new FinanceReportManager()).getReportByAssetID(assetEntity.getAssetID());
                if (reportList.isEmpty()) {
                    return;
                }
		this.date = date;
		this.latestReport = getLatestReportUntilDate();
		
		Collections.sort(reportList);
		Collections.reverse(reportList);
	
//		System.out.println(assetEntity.getSymbol());
		
		// initial variable
		statList = new TreeMap<String, Double>();
		double latestPrice = getLatestPrice(); 	
		
		/*
		 * Basic statistics
		 */
		double sharesOutstanding = latestReport.getSharesOutstanding();
		statList.put("AverageVolume", getAverageVolume());
		statList.put("MarketCapital", latestPrice * sharesOutstanding);
		statList.put("SharesOutstanding", sharesOutstanding);
		
		
		/*
		 * Valuation statistics
		 */
		statList.put("EPS", latestReport.getEPS());
		if (latestReport.getEPS() == 0)
			statList.put("P/E", 0.0);
		else
			statList.put("P/E", latestPrice * 1000 /latestReport.getEPS());
		
		if (sharesOutstanding != 0)	{
			double bookValue = latestReport.getOwnersEquity() * 1000/ sharesOutstanding;
			statList.put("P/B", latestPrice/bookValue);
		}
		else 
			statList.put("P/B", 0.0);

		
		/*
		 * Management Effectiveness
		 */
		double netIncome = getNetIncomeOf4LatestQuarter(latestReport);
		statList.put("ROA", netIncome / latestReport.getTotalAsset());
		statList.put("ROE", netIncome / latestReport.getOwnersEquity());
		double investmentCapital = latestReport.getTotalAsset() - latestReport.getShortTermLiabilities();
		double ebit = latestReport.getGrossProfit() - latestReport.getSellingExpenses() - latestReport.getManagingExpenses();
		statList.put("ROIC", ebit * .75 / investmentCapital);
		
		
		/*
		 * Growth Rate
		 */
		statList.put("EPSGrowth", getGrowthEPS(3));
		statList.put("RevenueGrowth", getGrowthRevenue(3));
		statList.put("AssetGrowth", getGrowthAsset(3));
		
		
		/*
		 * Financial Strength
		 */
		statList.put("CurrentRatio", latestReport.getCurrentAssets() / latestReport.getShortTermLiabilities());
		statList.put("QuickRatio", (latestReport.getCurrentAssets() - latestReport.getInventory())  / latestReport.getShortTermLiabilities());
		statList.put("TotalDebt/Equity", latestReport.getLiabilities() / latestReport.getOwnersEquity());
		statList.put("TotalDebt/Asset", latestReport.getLiabilities() / latestReport.getTotalAsset());
		statList.put("Equity/Asset", latestReport.getOwnersEquity() / latestReport.getTotalAsset());
		statList.put("EBITDA/Revenue", (latestReport.getTotalProfitBeforeTax() + latestReport.getFinancialExpenses()) / latestReport.getGrossSaleRevenues());
		
		statList.put("Score", 1.0);
	}
	
	
	
	
	public FinanceReportEntity getLatestReport() {
		FinanceReportEntity latestReport = reportList.get(0);
		for (FinanceReportEntity curReport : reportList) {
			if ((curReport.getYear() * 4 + curReport.getQuater()) > (latestReport
					.getYear() * 4 + latestReport.getQuater())) {
				latestReport = curReport;
			}
		}
		return latestReport;
	}
	
	
	public FinanceReportEntity getLatestReportUntilDate() {
		
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		int quater = (ca.get(Calendar.MONTH) / 3) + 1;
		FinanceReportEntity latestReport = reportList.get(reportList.size() -1) ;
		for (FinanceReportEntity curReport : reportList) {
			if ( (curReport.getYear() < year)  ||
					((curReport.getYear() == year) && (curReport.getQuater() < quater)) &&
					(curReport.compareTo(latestReport) > 0)) {
				latestReport = curReport;
			}
		}
		return latestReport;
	}
	
	
	
//	public double getLatestPrice() {
//		PriceManager priceManager  = new PriceManager();
//		double latestPrice = priceManager.getPriceByAssetIDAndDate(
//				assetEntity.getAssetID(),
//				priceManager.getLatestDateOfAsset(assetEntity.getAssetID()))
//				.getClose();
//		return latestPrice;
//	}
	
	
	public double getLatestPrice() {
		PriceManager priceManager  = new PriceManager();
		java.sql.Date latestDate = priceManager.getLatestDateOfAssetIDUntiltDate(assetEntity.getAssetID(),new java.sql.Date(date.getTime()));
		double latestPrice = 0;
		if (latestDate != null)
			latestPrice = priceManager.getPriceByAssetIDAndDate(
				assetEntity.getAssetID(), latestDate).getClose();	
		return latestPrice;
	}
	
	public double getAverageVolume() {
		PriceManager priceManager  = new PriceManager();
		java.sql.Date latestDate = priceManager.getLatestDateOfAssetIDUntiltDate(assetEntity.getAssetID(),new java.sql.Date(date.getTime()));
		double averageVolume = 0;
		if (latestDate != null)
			averageVolume = priceManager.getPriceByAssetIDAndDate(
				assetEntity.getAssetID(), latestDate).getVolume();	
		return averageVolume;
	}
	
	
	public double getNetIncomeOf4LatestQuarter(FinanceReportEntity latestReport) {
		double netIncome = 0;
		if(latestReport.getQuater() == 5)
			return latestReport.getProfitAfterCorporateIncomeTax();
		else {
			int quarterIndex = 0;
			for (FinanceReportEntity curReport : reportList) {
				if (((curReport.getYear()*4 + curReport.getQuater()) > (latestReport
						.getYear()*4
						+ latestReport.getQuater() - 4))
						&& (curReport.getQuater() != 5) && (curReport.compareTo(latestReport) <= 0)) {
					netIncome += curReport.getProfitAfterCorporateIncomeTax();
					quarterIndex ++;
				}
			}
	
			if (quarterIndex != 4) { // not found enough 4 quarters  --> return report of latest year
				for (FinanceReportEntity curReport : reportList) {
					if ((curReport.getQuater() == 5) && (curReport.getYear() == (latestReport.getYear() - 1)))
						return curReport.getProfitAfterCorporateIncomeTax();
				}
				return 0; // invalid report
			}
		}
		return netIncome;
	}
	
	
	public Double getGrowthRevenue(int years) {
		
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		TreeMap<Integer, FinanceReportEntity> reportYearList = new TreeMap<Integer, FinanceReportEntity>();
		for (FinanceReportEntity curReport : reportList) {
			if ((curReport.getQuater() == 5) && curReport.getYear() <= year)
				reportYearList.put(curReport.getYear(), curReport);
		}

		if (isValid("RevenueGrowth") == 1) {
			Double roe = 0.0;
			for (Integer y : reportYearList.descendingKeySet()){
				if (reportYearList.get(y - 1).getGrossSaleRevenues() == 0) 
					return 0.0;
				

				double growth = (reportYearList.get(y).getGrossSaleRevenues()
						- reportYearList.get(y - 1).getGrossSaleRevenues())
						/ reportYearList.get(y - 1).getGrossSaleRevenues();
//				if (growth <= 1)
					roe += growth;
//				else
//					roe += 0;
				
				
				
				if (y == reportYearList.lastKey() - 2)
					break;
			}
			return roe / years;
		}
		return 0.0;
	}
	
	
	
	public Double getGrowthEPS(int years) {
		
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		TreeMap<Integer, FinanceReportEntity> reportYearList = new TreeMap<Integer, FinanceReportEntity>();
		for (FinanceReportEntity curReport : reportList) {
			if ((curReport.getQuater() == 5) && curReport.getYear() <= year)
				reportYearList.put(curReport.getYear(), curReport);
		}

		if (isValid("RevenueGrowth") == 1) {
			Double roe = 0.0;
			for (Integer y : reportYearList.descendingKeySet()){
				if (reportYearList.get(y - 1).getEPS() == 0) 
					return 0.0;
				

				double growth = (reportYearList.get(y).getEPS()
						- reportYearList.get(y - 1).getEPS())
						/ reportYearList.get(y - 1).getEPS();
//				if (growth <= 1)
					roe += growth;
//				else
//					roe += 0;
				
				
				
				if (y == reportYearList.lastKey() - 2)
					break;
			}
			return roe / years;
		}
		return 0.0;
	}
	
	public Double getGrowthAsset(int years) {
		
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		TreeMap<Integer, FinanceReportEntity> reportYearList = new TreeMap<Integer, FinanceReportEntity>();
		for (FinanceReportEntity curReport : reportList) {
			if ((curReport.getQuater() == 5) && curReport.getYear() <= year)
				reportYearList.put(curReport.getYear(), curReport);
		}

		if (isValid("RevenueGrowth") == 1) {
			Double roe = 0.0;
			for (Integer y : reportYearList.descendingKeySet()){
				if (reportYearList.get(y - 1).getCurrentAssets() == 0) 
					return 0.0;
				

				double growth = (reportYearList.get(y).getCurrentAssets()
						- reportYearList.get(y - 1).getCurrentAssets())
						/ reportYearList.get(y - 1).getCurrentAssets();
//				if (growth <= 1)
					roe += growth;
//				else
//					roe += 0;
				
				
				
				if (y == reportYearList.lastKey() - 2)
					break;
			}
			return roe / years;
		}
		return 0.0;
	}
	
	
	public int profitGrowth() {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		TreeMap<Integer, FinanceReportEntity> reportYearList = new TreeMap<Integer, FinanceReportEntity>();
		for (FinanceReportEntity curReport : reportList) {
			if ((curReport.getQuater() == 5) && curReport.getYear() <= year)
				reportYearList.put(curReport.getYear(), curReport);
		}

		if (isValid("RevenueGrowth") == 1) {
			for (Integer y : reportYearList.descendingKeySet()){
				if (reportYearList.get(y - 1).getProfitAfterCorporateIncomeTax() < 0) 
					return 0;

				double growth = reportYearList.get(y).getProfitAfterCorporateIncomeTax()
									/ reportYearList.get(y - 1).getProfitAfterCorporateIncomeTax();
				if (growth > 3)
					return 0;
				
				if (y == reportYearList.lastKey() - 2)
					break;
			}
			return 1;
		}
		return 0; 
	}
	
	
	
	
	public AssetEntity getAsset() {
		return assetEntity;
	}

	public void setAsset(AssetEntity assetEntity) {
		this.assetEntity = assetEntity;
	}

	public TreeMap<String, Double> getStatList() {
		return statList;
	}

	public void setStatList(TreeMap<String, Double> statList) {
		this.statList = statList;
	}
	
	// TODO
	/**  
	 * 	@return 1 : if there are reports in at least 3 nearest years
	 *    				no invalid statistic
	 * 
	 */
	public int isValid (String key) {
		Collections.sort(reportList);
		
		/*
		 *  return 1 : if there are reports in at least 3 nearest years
		 */
                if (statList == null) {
                    return 0;
                }
		Double pe = statList.get("P/E");
		if (pe <= 0)
			return 0;
		
		if (latestReport.getOwnersEquity() <= 0)
			return 0;
		
		if (key == "RevenueGrowth") {
			Calendar ca = Calendar.getInstance();
			ca.setTime(date);
			int year = ca.get(Calendar.YEAR);
			int numberOfYear = 0;
			for (FinanceReportEntity curReport : reportList) {
				if ((curReport.getYear() < year)
						&& (curReport.getYear() >= year - 4)
						&& curReport.getQuater() == 5)
					numberOfYear++;
			}
			if (numberOfYear < 4)
				return 0;
		}
		
		if (key == "Revenue") {
			Double revenue = latestReport.getGrossSaleRevenues();
			if (revenue == 0)
				return 0;
		}
		
		return 1;
	}

	
	public static int isHOSEorHASTC(AssetEntity asset) {
		String exchage = (new ExchangeManager()).getExchangeByID(asset.getExchangeID()).getName();
		if (exchage.equals("HOSE") || exchage.equals("HASTC"))
			return 1;
		return 0;
	}
	
	
	@Override
	public int compareTo(AssetStat o) {
		return new Integer(this.getAsset().getSymbol().compareTo(o.getAsset().getSymbol()));
	}
	
	@Override
	public String toString() {
		String s = new String();
			s += this.getAsset().getSymbol() + ","
//			+ this.getLatestPrice()+ ","
			+ this.getStatList().get("ROE") + ","
			+ this.getStatList().get("P/E") + ","
			+ this.getStatList().get("ROA") + ","
			+ this.getStatList().get("EBITDA/Revenue") + ","
			+ this.getStatList().get("TotalDebt/Equity") + ","
			+ this.getLatestReportUntilDate().getYear() ;
		return s;
	}
	
	public static void main(String[] args) {
		ArrayList<FinanceReportEntity> aList = new ArrayList<FinanceReportEntity>(); 
		FinanceReportEntity a = new FinanceReportEntity(2013, 1, 22.2);
		aList.add(a);
		FinanceReportEntity b = new FinanceReportEntity(2012, 5, 22.4);
		aList.add(b);
		
		Collections.sort(aList);
		
		
		for (FinanceReportEntity c : aList) {
			System.out.println(c);
		}
		
		if (b.compareTo(a) == -1)
		System.out.println(b.compareTo(a));
	}

	
	
	
}
