/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.guiForTechnicalFilter;

import dataAccess.databaseManagement.entity.AssetEntity;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author TDINH
 */
public class RankingTableModel extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("rawtypes")
    
    public static Class[] types = new Class[]{
        java.lang.String.class, java.lang.String.class
    };
    public static boolean[] canEdit = new boolean[]{
        false, false
    };
    public static String[] columnNames = {
        "Symbol", "Score"
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

    public void addRow(String symbol, Double rate) {
        Object[] newRow = new Object[2];
        newRow[0] = symbol;
        
        rate = rate*100;
        rate = (double) Math.round(rate);
        newRow[1] = rate/100;                

        data.add(newRow);
    }

    public void insertRow(int rowIndex, String symbol, Double rate) {
        Object[] newRow = new Object[2];
        newRow[0] = symbol;
        rate = rate*100;
        rate = (double) Math.round(rate);
        newRow[1] = rate/100;                
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

    public void setData(TreeMap<Double, ArrayList<Object>> output) {
        data.clear();

        for (Double rate : output.keySet()) {
            addRow(((AssetEntity)output.get(rate).get(0)).getSymbol(), rate);
        }

    }
}
