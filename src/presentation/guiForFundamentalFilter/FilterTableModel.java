/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.guiForFundamentalFilter;

import business.fundamentalModel.AssetStat;
import dataAccess.databaseManagement.entity.PriceEntity;
import dataAccess.databaseManagement.manager.PriceManager;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author TDINH
 */
public class FilterTableModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("rawtypes")
    
    public static Class[] types = new Class[]{
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
    };
    public static boolean[] canEdit = new boolean[]{
        false, false, false, false, false, false
    };
    public static String[] columnNames = {
        "Symbol", "LatestPrice", "P/E", "P/B", "ROE", "RevenueGrowth"
    };
    
    private ArrayList<Object[]> data = new ArrayList<Object[]>();

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
    }

    public void addRow(AssetStat assetStat) {
        Object[] newRow = convertAssetStat(assetStat);
        data.add(newRow);
    }

    public void insertRow(int rowIndex, AssetStat assetStat) {
        Object[] newRow = convertAssetStat(assetStat);
        data.add(rowIndex, newRow);
    }

    public void deleteRow(int rowIndex) {
        data.remove(rowIndex);
    }

    public void deleteRows(int[] rowIndices) {
        int count = 0;
        for (int rowIndex : rowIndices) {
            data.remove(rowIndex - count);
            ++count;
        }
    }

    public void deleteAlLData() {
        data.clear();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data.get(rowIndex)[columnIndex] = aValue;
    }

    public void setData(ArrayList<AssetStat> assetStatList) {
        data.clear();

        for (AssetStat assetStat : assetStatList) {
            addRow(assetStat);
        }

    }

    private Object[] convertAssetStat(AssetStat assetStat) {
        Object[] newRow = new Object[columnNames.length];

        newRow[0] = assetStat.getAsset().getSymbol();
        ArrayList<PriceEntity> priceList = (new PriceManager()).getPriceByAssetID(assetStat.getAsset().getAssetID());
        newRow[1] = priceList.get(priceList.size()-1).getClose() + "";
        for (int i = 2; i < columnNames.length; ++i) {
            if ("RevenueGrowth".equals(columnNames[i])) {
                Double growth = (assetStat.getStatList().get(columnNames[i]));
                growth = growth*10000;
                growth = (double) Math.round(growth);
                newRow[i] = growth/100 + "%";                
                continue;
            }
            
            double temp = assetStat.getStatList().get(columnNames[i]);
            temp *= 10000;
            temp = Math.round(temp);
            temp /= 10000;
            newRow[i] = temp;
        }
        return newRow;
    }
}
