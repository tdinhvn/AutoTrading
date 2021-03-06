/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ViewOrderJPanel.java
 *
 * Created on Jun 24, 2011, 5:36:36 PM
 */

package presentation.guiForDecisionAlgorithmEvaluation;

import presentation.guiForVirtualTrading.OrderTableModel;

/**
 *
 * @author Dinh
 */
public class ViewOrderJPanel extends javax.swing.JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrderTableModel orderTableModel;

    /** Creates new form ViewOrderJPanel */
    public ViewOrderJPanel(OrderTableModel orderTableModel) {
        this.orderTableModel = orderTableModel;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        orderJTable = new javax.swing.JTable();

        orderJTable.setModel(orderTableModel);
        jScrollPane1.setViewportView(orderJTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable orderJTable;
    // End of variables declaration//GEN-END:variables

}
