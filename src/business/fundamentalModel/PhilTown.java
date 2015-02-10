package business.fundamentalModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;

public class PhilTown extends AbstractFundamentalModel{

	ArrayList<Double> predictList;
	
	public PhilTown() {
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public TreeMap<String, Class> getParameterList() {
		TreeMap<String, Class> map = super.getParameterList();
		map.put("ROE", Double.class);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setParameterValue(TreeMap<String, Object> map) {

		ArrayList<AssetEntity> assetList = (ArrayList<AssetEntity>) map
				.get("Asset List");
		assetStatList = new ArrayList<AssetStat>();
		
		Date date = (Date) map.get("Date");
		ArrayList<AssetStat> assetStatList2 = new ArrayList<AssetStat>();
		for (AssetEntity curAsset : assetList) {
			
//			System.out.println(curAsset.getSymbol()); 

			AssetStat asStat = new AssetStat(curAsset, date);
                        if (asStat.reportList.isEmpty()) {
                            continue;
                        }
			if (asStat.isValid("RevenueGrowth") ==1
					&& asStat.profitGrowth() == 1
				)
				assetStatList2.add(asStat);
		}
		
		
		predictList = new ArrayList<Double>();
		for (AssetStat curAssetStat : assetStatList2) {

			// ROEacceptable = 1.27
			Double predictPrice = 2 * curAssetStat.getStatList().get("ROE")
					* curAssetStat.getStatList().get("EPS")
					* Math.pow((1 + curAssetStat.getStatList().get("ROE")), 10)
					/ Math.pow(1.27, 10);
			

			if (curAssetStat.getLatestPrice() < predictPrice / 2000) {
				assetStatList.add(curAssetStat);
				predictList.add(predictPrice);
			}
		}
		
	}
	
	
	@Override
	public String toString() {
		String s = new String();
		for (AssetStat curAssetStat : assetStatList) {
			s += curAssetStat.getAsset().getSymbol() + ","
			+ curAssetStat.getLatestPrice()+ ","
			+ predictList.get(assetStatList.indexOf(curAssetStat))/1000 + ","
			+ curAssetStat.getStatList().get("ROE") + ","
			+ curAssetStat.getStatList().get("EPS") + ","
			+ curAssetStat.getLatestReportUntilDate().getYear() + "\n";
		}
		return s;
	}
	

}
