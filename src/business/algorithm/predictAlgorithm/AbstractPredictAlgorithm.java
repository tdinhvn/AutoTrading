package business.algorithm.predictAlgorithm;

import java.util.ArrayList;
import java.util.TreeMap;

import dataAccess.databaseManagement.entity.AssetEntity;
import dataAccess.databaseManagement.entity.PriceEntity;

public abstract class AbstractPredictAlgorithm {
	protected TreeMap<AssetEntity, ArrayList<PriceEntity>> priceList;
	protected Integer futureInterval;

	public TreeMap<AssetEntity, ArrayList<PriceEntity>> getPriceList() {
		return priceList;
	}

	public void setPriceEntityList(
			TreeMap<AssetEntity, ArrayList<PriceEntity>> priceList) {
		this.priceList = priceList;
	}

	public Integer getFutureInterval() {
		return futureInterval;
	}

	public void setFutureInterval(Integer futureInterval) {
		this.futureInterval = futureInterval;
	}

	@SuppressWarnings("rawtypes")
	public TreeMap<String, Class> getParametersList() {
		TreeMap<String, Class> map = new TreeMap<String, Class>();
		map.put("Future interval", Integer.class);
		return map;
	}

	public void setParametersValue(TreeMap<String, Object> map) {
		this.futureInterval = (Integer) map.get("Future interval");
	}

	public AbstractPredictAlgorithm(
			TreeMap<AssetEntity, ArrayList<PriceEntity>> priceList,
			Integer futureInterval) {
		super();
		this.priceList = priceList;
		this.futureInterval = futureInterval;
	}

	public abstract OutputForPredictionAlgorithm runAlgorithm()
			throws Exception;

    public TreeMap<String, Object> getDefaultValuesList() {
        TreeMap<String, Object> map = new TreeMap<String, Object>();
	map.put("Future interval", new Integer(4));
	return map;
    }
}
