package business.fundamentalModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;

public class PFG extends AbstractFundamentalModel{
		
	public PFG () {
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public TreeMap<String, Class> getParameterList() {
		// TODO Auto-generated method stub
		TreeMap<String, Class> map = super.getParameterList();
		map.put("P/E", Double.class);
		map.put("P/B", Double.class);
		map.put("ROE", Double.class);
		map.put("RevenueGrowth", Double.class);
		return map;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void setParameterValue(TreeMap<String, Object> map) {
		// TODO Auto-generated method stub

		ArrayList<AssetEntity> assetList = (ArrayList<AssetEntity>) map
				.get("Asset List");
		assetStatList = new ArrayList<AssetStat>();
		
		Date date = (Date) map.get("Date");
		for (AssetEntity curAsset : assetList) {
			AssetStat asStat = new AssetStat(curAsset, date);
                        if (asStat.reportList.isEmpty()) {
                            continue;
                        }
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

		assetStatList = AbstractFundamentalModel.filter(assetStatList, "ROE",
				(Double) map.get("ROE"), getMaxStat(assetStatList, "ROE"));
		assetStatList = AbstractFundamentalModel.filter(assetStatList, "P/B",
				getMinStat(assetStatList, "P/B"), (Double) map.get("P/B"));
		assetStatList = AbstractFundamentalModel.filter(assetStatList, "P/E",
				getMinStat(assetStatList, "P/E"), (Double) map.get("P/E"));
		assetStatList = AbstractFundamentalModel.filter(assetStatList,
				"RevenueGrowth", (Double) map.get("RevenueGrowth"), getMaxStat(
						assetStatList, "RevenueGrowth"));
		
		

	}
	
	
	@Override
	public String toString() {
		String s = new String();
		for (AssetStat curAssetStat : assetStatList) {
			s += curAssetStat.getAsset().getSymbol() + ","
			+ curAssetStat.getLatestPrice()+ ","
			+ curAssetStat.getStatList().get("ROE") + ","
			+ curAssetStat.getStatList().get("P/E") + ","
			+ curAssetStat.getStatList().get("P/B") + ","
			+ curAssetStat.getStatList().get("RevenueGrowth") + ","
			+ curAssetStat.getLatestReportUntilDate().getYear() + "\n";
		}
		return s;
	}
	
}
