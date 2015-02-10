package business.fundamentalModel;

import dataAccess.databaseManagement.entity.AssetEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class Custom extends AbstractFundamentalModel{

	public static final String[] STAT_LIST = {"AverageVolume" , "MarketCapital", "SharesOutstanding" , "EPS", "P/E", "P/B" , "ROA", "ROE", "ROIC", "EPSGrowth", "RevenueGrowth", "AssetGrowth", "CurrentRatio", "QuickRatio", "TotalDebt/Equity", "TotalDebt/Asset"  }; 
	
//	initmin = AbstractFundamentalModel.getMinStat(assetList, "ROE"); 
// 	initmax = AbstractFundamentalModel.getMinStat(assetList, "ROE");
	
	public Custom() {
		
	}
	
	@Override
	public void setParameterValue(TreeMap<String, Object> map) {
		// TODO Auto-generated method stub
		
		
		@SuppressWarnings("unchecked")
		ArrayList<AssetEntity> assetList = (ArrayList<AssetEntity>) map.get("Asset List");
		assetStatList = new ArrayList<AssetStat>();
		
		Date date = (Date) map.get("Date");
		for (AssetEntity curAsset : assetList) {
			AssetStat asStat = new AssetStat(curAsset, date);
			if (asStat.isValid("RevenueGrowth") ==1 
					&& asStat.profitGrowth() == 1
					)
				assetStatList.add(asStat);
		}

		
//		for (AssetStat curAssetStat : assetStatList) {
//			System.out.println(curAssetStat.getAsset().getSymbol() + " "
//					+ curAssetStat.getStatList().get("P/E") + " "
//					+ curAssetStat.getStatList().get("P/B") + " "
//					+ curAssetStat.getStatList().get("ROE"));
//		}
		
		for (String curKey: map.keySet()) {
			for (String curStat : STAT_LIST) {
				String minStat = curStat + "Min";
				String maxStat = curStat + "Max";
				if (curKey.equals(minStat))
					assetStatList = AbstractFundamentalModel.filter(assetStatList, curStat,
							(Double) map.get(minStat), (Double) map.get(maxStat));
			}
		}
	}
	

}
