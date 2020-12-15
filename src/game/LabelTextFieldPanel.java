package game;

import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class LabelTextFieldPanel extends JPanel {

    private JLabel lbl;
    private JTextField txtField;

    public LabelTextFieldPanel(String lblStr) {

        lbl = new JLabel(lblStr, JLabel.CENTER);
        txtField = new JTextField();

        txtField.setEditable(false);

        Dimension size = new Dimension(400, 20);
        setPreferredSize(size);
        setSize(size);

        setLayout(new GridLayout(0, 2));
        add(lbl);
        add(txtField);
    }
    
    public JLabel getLbl() { return lbl; }
    public JTextField getTxtField() { return txtField; }

    public void clear() {
        txtField.setText("");
        System.out.println(lbl.getText() + " clear");
    }

    public void setValue(int x) {
        if (x >= 0) txtField.setText(String.valueOf(x));
        else txtField.setText("");
    }
    
    public int getValue() {
        System.out.println(lbl.getText() +  " getValue");
        String str = txtField.getText();
        if (str.equals("")) return 0;
        return Integer.parseInt(str);
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(280, 150);
        mainFrame.setLayout(new GridLayout(0, 1));

        LabelTextFieldPanel yahtzeeBonusPnl = new LabelTextFieldPanel("Yahtzee Bonus");
        LabelTextFieldPanel totalOfLowerSectionPnl = new LabelTextFieldPanel("Total of Lower Section");
        LabelTextFieldPanel grandTotalPnl = new LabelTextFieldPanel("Grand Total");

        mainFrame.add(yahtzeeBonusPnl);
        mainFrame.add(totalOfLowerSectionPnl);
        mainFrame.add(grandTotalPnl);

        System.out.println(yahtzeeBonusPnl.getValue());

        mainFrame.setVisible(true);
    }
}