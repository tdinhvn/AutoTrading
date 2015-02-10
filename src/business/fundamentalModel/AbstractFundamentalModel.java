package business.fundamentalModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.ExchangeEntity;
import dataAccess.databaseManagement.manager.AssetManager;

/*
 * // initial model (all parameters have been initiallied with default values) 
 * String model_name = comboBox.getText(); // get text in Model combo box
 * AbstractFundamentalModel abstractFModel =  fundamentalModel.getFundamentalModel(model_name);
 * 
 * ArrayList<AssetStat> table = abstractFModel.getAssetStatList();
 * 
 * 
 * 
 * // when a parameter is updated
 * abstractModel.setParametersValue(map);
 * table = abstractFModel.getAssetStatList();
 * 
 */


public abstract class AbstractFundamentalModel {
	ArrayList<AssetStat> assetStatList;
	TreeMap<String, Object> paramList;
	
	public abstract void setParameterValue(TreeMap<String, Object> map);
	

	@SuppressWarnings("rawtypes")
	public TreeMap<String, Class> getParameterList() {
		TreeMap<String, Class> map = new TreeMap<String, Class>();
		map.put("Asset List", ArrayList.class);
		map.put("Date", Date.class);
		return map;
	}
	
		
	static ArrayList<AssetStat> filter(ArrayList<AssetStat> assetList,
			String keyStat, double min, double max) {
		ArrayList<AssetStat> filterList = new ArrayList<AssetStat>();
		for(AssetStat curAsset : assetList) {
			
			double curStat = curAsset.getStatList().get(keyStat);
			if ((curStat >= min) && (curStat <= max)) {
				filterList.add(curAsset);
			}
		}
		return filterList;
	}
	
	
	
	/*
	 * return maximum value of a statistic
	 */
	static double getMaxStat(ArrayList<AssetStat> assetList,
			String keyStat) {
		double max = assetList.get(0).getStatList().get(keyStat);
		for(AssetStat curAsset : assetList) {
			double curStat = curAsset.getStatList().get(keyStat);
			if (curStat > max) {
				max = curStat;
			}
		}
		return max;
	}
	
	
	/*
	 * return minimum value of a statistic
	 */
	static double getMinStat(ArrayList<AssetStat> assetList,
			String keyStat) {
		double min = assetList.get(0).getStatList().get(keyStat);
		for(AssetStat curAsset : assetList) {
			double curStat = curAsset.getStatList().get(keyStat);
			if (curStat < min) {
				min = curStat;
			}
		}
		return min;
	}
	
	
	public ArrayList<AssetStat> getAssetStatList() {
		return assetStatList;
	}
	
	
	public static ArrayList<AssetEntity> assList(ExchangeEntity exchange ) {
		AssetManager assetManager= new AssetManager();
	
		ArrayList<AssetEntity> assetListTemp = assetManager.getAssetsByExchange(exchange.getExchangeID());
		ArrayList<AssetEntity> assetList = new ArrayList<AssetEntity>();
		
		for(AssetEntity curAsset : assetListTemp) {
			if (curAsset.getSymbol().equals("VNINDEX") 
					|| curAsset.getSymbol().equals("HASTCINDEX") 
					|| curAsset.getSymbol().equals("MAFPF1") 
					|| curAsset.getSymbol().equals("PRUBF1") 
					|| curAsset.getSymbol().equals("VFMVF1") 
					|| curAsset.getSymbol().equals("VFMVF4") 
					|| curAsset.getSymbol().equals("VFMVFA") 
					|| curAsset.getSymbol().equals("ASA") 
					|| curAsset.getSymbol().equals("TV3") 
//					|| curAsset.getSymbol().equals("FPC") 
//					|| curAsset.getSymbol().equals("HT2") 
//					|| curAsset.getSymbol().equals("NKD") 
//					|| curAsset.getSymbol().equals("NTP") 
//					|| curAsset.getSymbol().equals("S96") 
//					|| curAsset.getSymbol().equals("BTC")
					){
			}
			else {
					assetList.add(curAsset);
			}
		}
		return assetList;
	}
	
	
	
	@Override
	public String toString() {
		String assetList = new String();
		for (AssetStat curAsset : assetStatList)
			assetList += ("\t" + curAsset.getAsset().getSymbol()); 
		return assetList;
	}
	
}
