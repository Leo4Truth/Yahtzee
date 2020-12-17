package game;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.StyledEditorKit.BoldAction;

public class ButtonTextFieldPanel extends JPanel {

    private JButton btn;
    private JTextField txtField;

    public ButtonTextFieldPanel(String btnStr) {

        btn = new JButton(btnStr);
        txtField = new JTextField("");

        txtField.setEditable(false);

        Dimension size = new Dimension(400, 20);
        setPreferredSize(size);
        setSize(size);

        btn.setMargin(new Insets(2, 2, 2, 2));
        setLayout(new GridLayout(0, 2));
        add(btn);
        add(txtField);
        
    }

    public JButton getBtn() { return btn; }
    public JTextField getTxtField() { return txtField; }

    public boolean isEnabled() { return btn.isEnabled(); }

    public void clear() {
        txtField.setText("");
    }

    public void reset() {
        btn.setEnabled(true);
        txtField.setText("");
    }

    public void setEnabled(boolean b) {
        btn.setEnabled(b);
    }

    public void setValue(int x) {
        if (x >= 0) txtField.setText(String.valueOf(x));
        else txtField.setText("");
    }
    
    public int getValue() {
        String str = txtField.getText();
        if (str.equals("")) return -1;
        return Integer.parseInt(str);
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(280, 150);
        mainFrame.setLayout(new GridLayout(0, 1));

        ButtonTextFieldPanel threeOfAKindPnl = new ButtonTextFieldPanel("Three Of A Kind");
        ButtonTextFieldPanel fourOfAKindPnl = new ButtonTextFieldPanel("Four Of A Kind");
        ButtonTextFieldPanel fullHousePnl = new ButtonTextFieldPanel("Full House");

        mainFrame.add(new JTextField("Lower Section"));
        mainFrame.add(threeOfAKindPnl);
        mainFrame.add(fourOfAKindPnl);
        mainFrame.add(fullHousePnl);

        mainFrame.setVisible(true);
    }
}
