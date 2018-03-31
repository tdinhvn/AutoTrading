/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * DataUpdateJPanel.java
 *
 * Created on Jun 8, 2011, 10:22:49 AM
 */
package presentation.guiForDataUpdate;

import business.dataUpdate.AbstractDataUpdate;
import business.dataUpdate.DataUpdateAPI;
import dataAccess.databaseManagement.entity.ExchangeEntity;
import dataAccess.databaseManagement.manager.ExchangeManager;
import dataAccess.databaseManagement.manager.PriceManager;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import presentation.ComboKeyHandler;
import presentation.mainJFrame;

/**
 *
 * @author Dinh
 */
public class DataUpdateJPanel extends javax.swing.JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -2907142624791836040L;
    private static PriceManager priceManager = new PriceManager();
    private static ExchangeManager exchangeManager = new ExchangeManager();
    private Thread updateThread = null;
    private TimerTask task1 = null;
    private boolean isUpdating = false;

    /**
     * Creates new form DataUpdateJPanel
     */
    public DataUpdateJPanel() {
        initComponents();
        initOtherComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dataStatusJPanel = new javax.swing.JPanel();
        exchangeJLabel = new javax.swing.JLabel();
        exchangeJComboBox = new javax.swing.JComboBox();
        dataStatusJLabel = new javax.swing.JLabel();
        updateJPanel = new javax.swing.JPanel();
        updateJButton = new javax.swing.JButton();
        sourceJComboBox = new javax.swing.JComboBox();
        sourceJLabel = new javax.swing.JLabel();
        fromDatejSpinner = new javax.swing.JSpinner();
        fromDateJLabel = new javax.swing.JLabel();
        toDateJLabel = new javax.swing.JLabel();
        toDatejSpinner = new javax.swing.JSpinner();
        updateStatusjLabel = new javax.swing.JLabel();
        exchangeUpdateJLabel = new javax.swing.JLabel();
        exchangeUpdateJComboBox = new javax.swing.JComboBox();

        dataStatusJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DATA STATUS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        exchangeJLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        exchangeJLabel.setText("Exchange:");

        exchangeJComboBox.setEditable(true);
        exchangeJComboBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                exchangeJComboBoxActionPerformed(evt);
            }
        });

        dataStatusJLabel.setText("We have data from ...");

        javax.swing.GroupLayout dataStatusJPanelLayout = new javax.swing.GroupLayout(dataStatusJPanel);
        dataStatusJPanel.setLayout(dataStatusJPanelLayout);
        dataStatusJPanelLayout.setHorizontalGroup(
            dataStatusJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataStatusJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataStatusJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dataStatusJPanelLayout.createSequentialGroup()
                        .addComponent(exchangeJLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exchangeJComboBox, 0, 423, Short.MAX_VALUE))
                    .addComponent(dataStatusJLabel))
                .addContainerGap())
        );
        dataStatusJPanelLayout.setVerticalGroup(
            dataStatusJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataStatusJPanelLayout.createSequentialGroup()
                .addGroup(dataStatusJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exchangeJLabel)
                    .addComponent(exchangeJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataStatusJLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        updateJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "UPDATE DATA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        updateJButton.setText("Update");
        updateJButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateJButtonActionPerformed(evt);
            }
        });

        sourceJComboBox.setModel(new javax.swing.DefaultComboBoxModel(business.dataUpdate.DataUpdateAPI.ONLINE_RESOURCES));
        sourceJComboBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceJComboBoxActionPerformed(evt);
            }
        });

        sourceJLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        sourceJLabel.setText("Source:");

        fromDatejSpinner.setModel(new javax.swing.SpinnerDateModel());
        fromDatejSpinner.setEditor(new JSpinner.DateEditor(fromDatejSpinner, "MM/dd/yyyy"));
        fromDatejSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fromDatejSpinnerStateChanged(evt);
            }
        });

        fromDateJLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        fromDateJLabel.setText("From:");

        toDateJLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        toDateJLabel.setText("To:");

        toDatejSpinner.setModel(new javax.swing.SpinnerDateModel());
        toDatejSpinner.setEditor(new JSpinner.DateEditor(toDatejSpinner, "MM/dd/yyyy"));
        toDatejSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                toDatejSpinnerStateChanged(evt);
            }
        });

        updateStatusjLabel.setText("0.0% complete");

        exchangeUpdateJLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        exchangeUpdateJLabel.setText("Exchange:");

        exchangeUpdateJComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        exchangeUpdateJComboBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                exchangeUpdateJComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updateJPanelLayout = new javax.swing.GroupLayout(updateJPanel);
        updateJPanel.setLayout(updateJPanelLayout);
        updateJPanelLayout.setHorizontalGroup(
            updateJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(updateJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(updateStatusjLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(updateJPanelLayout.createSequentialGroup()
                        .addGroup(updateJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fromDateJLabel)
                            .addComponent(sourceJLabel)
                            .addComponent(exchangeUpdateJLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(updateJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(updateJPanelLayout.createSequentialGroup()
                                .addComponent(fromDatejSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(toDateJLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toDatejSpinner))
                            .addGroup(updateJPanelLayout.createSequentialGroup()
                                .addGroup(updateJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(exchangeUpdateJComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(sourceJComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(updateJButton)))))
                .addContainerGap())
        );

        updateJPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {fromDatejSpinner, toDatejSpinner});

        updateJPanelLayout.setVerticalGroup(
            updateJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(updateJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toDateJLabel)
                    .addComponent(toDatejSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromDatejSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromDateJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(updateJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourceJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sourceJLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(updateJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exchangeUpdateJLabel)
                    .addComponent(exchangeUpdateJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateJButton))
                .addGap(18, 18, 18)
                .addComponent(updateStatusjLabel)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(updateJPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dataStatusJPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dataStatusJPanel, updateJPanel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dataStatusJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(updateJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void updateJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateJButtonActionPerformed
        if (!isUpdating) {
            updateThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    updateStatusjLabel.setVisible(true);

                    Timer timer = new Timer();
                    task1 = new TimerTask() {

                        @Override
                        public void run() {
                            updateStatusjLabel.setText(dataUpdate.completePercentage + " complete");
                        }
                    };

                    timer.schedule(task1, 1000, 1000);

                    dataUpdate.updateDataFromDateToDate((String) exchangeUpdateJComboBox.getSelectedItem(), fromDate, toDate);

                    task1.cancel();
                    updateDataStatus();
                    updateStatusjLabel.setVisible(false);
                    updateJButton.setText("Update");
                    isUpdating = false;
                }
            });
            updateThread.start();
            isUpdating = true;
            updateJButton.setText("Stop Updating");
        } else {
            updateThread.stop();
            task1.cancel();
            updateDataStatus();
            updateStatusjLabel.setVisible(false);
            updateJButton.setText("Update");
            isUpdating = false;
        }
    }//GEN-LAST:event_updateJButtonActionPerformed

    private void updateDataStatus() {
        dataStatus = new TreeMap<ExchangeEntity, String>();
        for (int i = 0; i < exchangeJComboBox.getModel().getSize(); ++i) {
            ExchangeEntity exchangeEntity = (ExchangeEntity) exchangeJComboBox.getModel().getElementAt(i);
            Date minDate = priceManager.getOldestDateOfExchange(exchangeEntity.getExchangeID());
            Date maxDate = priceManager.getLatestDateOfExchange(exchangeEntity.getExchangeID());
            String status = "We have data FROM DATE: " + minDate + " - TO DATE: " + maxDate;
            dataStatus.put(exchangeEntity, status);
        }
        dataStatusJLabel.setText(dataStatus.get(exchangeJComboBox.getSelectedItem()));
    }


    private void exchangeJComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exchangeJComboBoxActionPerformed
        if (exchangeJComboBox.getSelectedIndex() == -1) {
            return;
        }

        dataStatusJLabel.setText(dataStatus.get(exchangeJComboBox.getSelectedItem()));
}//GEN-LAST:event_exchangeJComboBoxActionPerformed

    private void fromDatejSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fromDatejSpinnerStateChanged
        fromDate = (Date) fromDatejSpinner.getValue();
}//GEN-LAST:event_fromDatejSpinnerStateChanged

    private void toDatejSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_toDatejSpinnerStateChanged
        toDate = (Date) toDatejSpinner.getValue();
}//GEN-LAST:event_toDatejSpinnerStateChanged

    private void sourceJComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceJComboBoxActionPerformed
        dataUpdate = DataUpdateAPI.getDataUpdate((String) sourceJComboBox.getSelectedItem());

        String[] listExchangeName = new String[dataUpdate.getExchangeNameList().size() + 1];
        listExchangeName[0] = "All";
        Date minDate = new Date();
        Date tempDate;
        for (int i = 1; i < listExchangeName.length; ++i) {
            listExchangeName[i] = dataUpdate.getExchangeNameList().get(i - 1);
            tempDate = new Date(priceManager.getLatestDateOfExchange(exchangeManager.getExchangeByName(listExchangeName[i]).getExchangeID()).getTime());
            if (tempDate.before(minDate)) {
                minDate = tempDate;
            }
        }

        exchangeUpdateJComboBox.setModel(new DefaultComboBoxModel(listExchangeName));
        exchangeUpdateJComboBox.updateUI();

        fromDatejSpinner.getModel().setValue(minDate);
    }//GEN-LAST:event_sourceJComboBoxActionPerformed

    private void exchangeUpdateJComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exchangeUpdateJComboBoxActionPerformed
        String exchangeName = (String) exchangeUpdateJComboBox.getSelectedItem();
        if (exchangeName.equals("All")) {
            String[] listExchangeName = new String[dataUpdate.getExchangeNameList().size()];
            Date minDate = new Date();
            Date tempDate;
            for (int i = 0; i < listExchangeName.length; ++i) {
                listExchangeName[i] = dataUpdate.getExchangeNameList().get(i);
                tempDate = new Date(priceManager.getLatestDateOfExchange(exchangeManager.getExchangeByName(listExchangeName[i]).getExchangeID()).getTime());
                if (tempDate.before(minDate)) {
                    minDate = tempDate;
                }
            }
            fromDatejSpinner.getModel().setValue(minDate);
        } else {
            long exchangeID = exchangeManager.getExchangeByName(exchangeName).getExchangeID();
            fromDatejSpinner.getModel().setValue(new Date(priceManager.getLatestDateOfExchange(exchangeID).getTime()));
        }
    }//GEN-LAST:event_exchangeUpdateJComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dataStatusJLabel;
    private javax.swing.JPanel dataStatusJPanel;
    private javax.swing.JComboBox exchangeJComboBox;
    private javax.swing.JLabel exchangeJLabel;
    private javax.swing.JComboBox exchangeUpdateJComboBox;
    private javax.swing.JLabel exchangeUpdateJLabel;
    private javax.swing.JLabel fromDateJLabel;
    private javax.swing.JSpinner fromDatejSpinner;
    private javax.swing.JComboBox sourceJComboBox;
    private javax.swing.JLabel sourceJLabel;
    private javax.swing.JLabel toDateJLabel;
    private javax.swing.JSpinner toDatejSpinner;
    private javax.swing.JButton updateJButton;
    private javax.swing.JPanel updateJPanel;
    private javax.swing.JLabel updateStatusjLabel;
    // End of variables declaration//GEN-END:variables
//    private final JFileChooser fileChooser = new JFileChooser();
//    private File selectedFile = null;

    private Date fromDate;
    private Date toDate;
    private TreeMap<ExchangeEntity, String> dataStatus;
    private ComboKeyHandler exchangeComboKeyHandler;
    private AbstractDataUpdate dataUpdate;

    private void initOtherComponents() {
        updateStatusjLabel.setVisible(false);
        exchangeJComboBox.setModel(new DefaultComboBoxModel(mainJFrame.mappingExchangeID_Assets.keySet().toArray()));
        exchangeComboKeyHandler = new ComboKeyHandler(exchangeJComboBox);
        JTextField fieldExchange = (JTextField) exchangeJComboBox.getEditor().getEditorComponent();
        fieldExchange.addKeyListener(exchangeComboKeyHandler);

        dataUpdate = DataUpdateAPI.getDataUpdate((String) sourceJComboBox.getSelectedItem());

        String[] listExchangeName = new String[dataUpdate.getExchangeNameList().size() + 1];
        listExchangeName[0] = "All";
        Date minDate = new Date();
        Date tempDate;
        for (int i = 1; i < listExchangeName.length; ++i) {
            listExchangeName[i] = dataUpdate.getExchangeNameList().get(i - 1);
            java.sql.Date latestDate = priceManager.getLatestDateOfExchange(exchangeManager.getExchangeByName(listExchangeName[i]).getExchangeID());
            if (latestDate != null) {
                tempDate = new Date(latestDate.getTime());
                if (tempDate.before(minDate)) {
                    minDate = tempDate;
                }
            }
        }

        exchangeUpdateJComboBox.setModel(new DefaultComboBoxModel(listExchangeName));
        exchangeUpdateJComboBox.updateUI();

        fromDatejSpinner.getModel().setValue(minDate);
        toDatejSpinner.getModel().setValue(new Date());

        new Thread(new Runnable() {

            @Override
            public void run() {
                updateDataStatus();
            }
        }).start();
    }
}
