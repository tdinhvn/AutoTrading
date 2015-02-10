/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package presentation.guiForVirtualTrading;

import business.virtualTrading.PortfolioEntry;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dinh
 */
public class PortfolioTableModel extends AbstractTableModel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	Class[] types = new Class [] {
        java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
    };
    
    boolean[] canEdit = new boolean [] {
        false, false, false, false, false
    };
    
    private String[] columnNames = {
        "Symbol", "Buy Price", "Current Price", "Volume", "Gain/Loss (%)"
    };

    private ArrayList<Object[]> data = new ArrayList<Object[]>();
    private ArrayList<PortfolioEntry> portfolioEntryList = new ArrayList<PortfolioEntry>();

    public ArrayList<PortfolioEntry> getPortfolioEntryList() {
        return portfolioEntryList;
    }

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

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Class getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
    }

    private Object[] convertPortfolioEntry(PortfolioEntry portfolioEntry) {
        Object[] newRow = new Object[columnNames.length];
        newRow[0] = portfolioEntry.getAsset().getSymbol();
        newRow[1] = portfolioEntry.getBuyPrice();
        newRow[2] = portfolioEntry.getCurrentPrice();
        newRow[3] = portfolioEntry.getVolume();
        newRow[4] = portfolioEntry.getProfit();
        return newRow;
    }

    public void addRow(PortfolioEntry portfolioEntry) {
        portfolioEntryList.add(portfolioEntry);
        Object[] newRow = convertPortfolioEntry(portfolioEntry);
        data.add(newRow);
    }

    public void insertRow(int rowIndex, PortfolioEntry portfolioEntry) {
        portfolioEntryList.add(rowIndex, portfolioEntry);
        Object[] newRow = convertPortfolioEntry(portfolioEntry);
        data.add(rowIndex, newRow);
    }

    public void deleteRow(int rowIndex) {
        portfolioEntryList.remove(rowIndex);
        data.remove(rowIndex);
    }

    public void deleteRows(int[] rowIndices) {
        int count = 0;
        for (int i = 0; i < rowIndices.length-1; ++i) {
            for (int j = i+1; j < rowIndices.length; ++j) {
                if (rowIndices[i] > rowIndices[j]) {
                    int temp = rowIndices[j];
                    rowIndices[j] = rowIndices[i];
                    rowIndices[i] = temp;
                }
            }
        }
        for (int rowIndex : rowIndices) {
            portfolioEntryList.remove(rowIndex-count);
            data.remove(rowIndex - count);
            ++count;
        }
    }

    public void deleteAllData() {
        portfolioEntryList.clear();
        data.clear();
    }

    public void setData(ArrayList<PortfolioEntry> portfolioEntryList) {
        this.portfolioEntryList.clear();
        data.clear();

        for (PortfolioEntry portfolioEntry : portfolioEntryList) {
            addRow(portfolioEntry);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data.get(rowIndex)[columnIndex] = aValue;
    }

}
