/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author Dinh
 */
public class ComboKeyHandler extends KeyAdapter {

    private JComboBox comboBox;
    private Vector<Object> list = new Vector<Object>();
    private boolean shouldHide = false;

    public ComboKeyHandler(JComboBox combo) {
        this.comboBox = combo;
        for (int i = 0; i < comboBox.getModel().getSize(); i++) {
            list.addElement(comboBox.getItemAt(i));
        }
    }

    public void updateListObjects() {
        list.removeAllElements();
        for (int i = 0; i < comboBox.getModel().getSize(); i++) {
            list.addElement(comboBox.getItemAt(i));
        }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                String text = ((JTextField) e.getSource()).getText();
                if (text.length() == 0) {
                    setSuggestionModel(comboBox, new DefaultComboBoxModel(list), "");
                    comboBox.hidePopup();
                } else {
                    ComboBoxModel m = getSuggestedModel(list, text);
                    if (m.getSize() == 0 || shouldHide) {
                        comboBox.hidePopup();
                    } else {
                        setSuggestionModel(comboBox, m, text);
                        comboBox.showPopup();
                    }
                }
            }
        });
    }

    @Override
    public void keyPressed(KeyEvent e) {
        JTextField textField = (JTextField) e.getSource();
        String text = textField.getText();
        shouldHide = false;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                for (Object s : list) {
                    if (s.toString().toLowerCase().startsWith(text.toLowerCase())) {
                        textField.setText(s.toString());
                        return;
                    }
                }
                break;
            case KeyEvent.VK_ENTER:
                comboBox.setModel(new DefaultComboBoxModel(list));
                shouldHide = true;
                for (Object s : list) {
                    if (s.toString().equalsIgnoreCase(text)) {
                        comboBox.setSelectedItem(s);
                        return;
                    }
                }
                comboBox.setSelectedIndex(0);
                break;
            case KeyEvent.VK_ESCAPE:
                comboBox.setModel(new DefaultComboBoxModel(list));
                comboBox.setSelectedIndex(0);
                shouldHide = true;
                break;
        }
    }

    private static void setSuggestionModel(JComboBox comboBox, ComboBoxModel mdl, String str) {
        comboBox.setModel(mdl);
        comboBox.setSelectedIndex(-1);
        ((JTextField) comboBox.getEditor().getEditorComponent()).setText(str);
    }

    private static ComboBoxModel getSuggestedModel(Vector<Object> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (Object s : list) {
            if (s.toString().toLowerCase().startsWith(text.toLowerCase())) {
                m.addElement(s);
            }
        }
        return m;
    }
}
