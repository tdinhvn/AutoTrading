package business.algorithm.decisionAlgorithm;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;
import java.util.ArrayList;
import java.util.TreeMap;

public abstract class AbstractDecisionAlgorithm {

	public abstract OutputForDecisionAlgorithm runAlgorithm();

	public AbstractDecisionAlgorithm(
			TreeMap<AssetEntity, ArrayList<PriceEntity>> priceList) {
		super();
		this.priceList = priceList;
	}

	protected TreeMap<AssetEntity, ArrayList<PriceEntity>> priceList;

	public TreeMap<AssetEntity, ArrayList<PriceEntity>> getPriceList() {
		return priceList;
	}

	public void setPriceList(
			TreeMap<AssetEntity, ArrayList<PriceEntity>> priceList) {
		this.priceList = priceList;
	}

	@SuppressWarnings("rawtypes")
	public TreeMap<String, Class> getParameterList() {
		TreeMap<String, Class> map = new TreeMap<String, Class>();
		return map;
	}
        
        public TreeMap<String, Object> getDefaultValuesList() {
        TreeMap<String, Object> map = new TreeMap<String, Object>();
	return map;
    }

	public void setParameterValue(TreeMap<String, Object> map) {
	}
}
