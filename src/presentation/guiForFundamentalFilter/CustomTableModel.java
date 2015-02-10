/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.guiForFundamentalFilter;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author TDINH
 */
public class CustomTableModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("rawtypes")
    Class[] types = new Class[]{
        java.lang.String.class, java.lang.Double.class, java.lang.Double.class
    };
    boolean[] canEdit = new boolean[]{
        false, true, true
    };
    private String[] columnNames = {
        "Name", "Min", "Max",
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
    
    public void addRow(String name, Double min, Double max) {
        Object[] newRow = new Object[columnNames.length];
        newRow[0] = name;
        newRow[1] = min;
        newRow[2] = max;
        data.add(newRow);
    }

    public void insertRow(int rowIndex, String name, Double min, Double max) {
        Object[] newRow = new Object[columnNames.length];
        newRow[0] = name;
        newRow[1] = min;
        newRow[2] = max;
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

//    public void setData(ArrayList<AssetStat> assetStatList) {
//        data.clear();
//
//        for (AssetStat assetStat : assetStatList) {
//            addRow(assetStat);
//        }
//
//    }

//    private Object[] convertAssetStat(AssetStat assetStat) {
//        Object[] newRow = new Object[columnNames.length];
//
//        newRow[0] = assetStat.getAsset().getSymbol();
//        for (int i = 1; i < columnNames.length-1; ++i) {
//            newRow[i] = assetStat.getStatList().get(columnNames[i]).toString();
//        }
//        Double growth = (assetStat.getStatList().get(columnNames[columnNames.length-1]));
//        growth = growth*10000;
//        growth = (double) Math.round(growth);
//        newRow[columnNames.length-1] = growth/100 + "%";
//        return newRow;
//    }
}
