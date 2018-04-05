package business.dataUpdate;

import dataAccess.databaseManagement.entity.AssetEntity;
import java.time.LocalDate;
import java.util.ArrayList;

public abstract class AbstractDataUpdate {
	protected String description;
	protected ArrayList<String> exchangeNameList;
	//protected ArrayList<String> fileNameList;
        public String completePercentage = "0.0%";

	public AbstractDataUpdate() {

	}

	public AbstractDataUpdate(String description,
			ArrayList<String> exchangeNameList, ArrayList<String> fileNameList) {
		super();
		this.description = description;
		this.exchangeNameList = exchangeNameList;
	//	this.fileNameList = fileNameList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<String> getExchangeNameList() {
		return exchangeNameList;
	}

	public void setExchangeNameList(ArrayList<String> exchangeNameList) {
		this.exchangeNameList = exchangeNameList;
	}

//	public ArrayList<String> getFileNameList() {
//		return fileNameList;
//	}
//
//	public void setFileNameList(ArrayList<String> fileNameList) {
//		this.fileNameList = fileNameList;
//	}

//	public abstract boolean updateHistoricalData();
//
//	public abstract boolean updateData();

	public abstract boolean updateDataFromDateToDate(LocalDate fromDate, LocalDate toDate);
	
	public abstract boolean updateDataFromDateToDate(AssetEntity assetEntity,
			LocalDate fromDate, LocalDate toDate);
        
        public abstract boolean updateData(AssetEntity assetEntity);
}
