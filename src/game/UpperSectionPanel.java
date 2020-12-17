package game;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

class UpperSectionPanel extends JPanel {

    private JLabel titleLbl;

    private ButtonTextFieldPanel acesPnl;
    private ButtonTextFieldPanel twosPnl;
    private ButtonTextFieldPanel threesPnl;
    private ButtonTextFieldPanel foursPnl;
    private ButtonTextFieldPanel fivesPnl;
    private ButtonTextFieldPanel sixesPnl;

    private LabelTextFieldPanel bonusPnl;
    private LabelTextFieldPanel totalOfUpperSectionPnl;

    private ArrayList<ButtonTextFieldPanel> pnlList;

    public UpperSectionPanel() {

        titleLbl = new JLabel("Upper Section");

        acesPnl = new ButtonTextFieldPanel("Ones");
        twosPnl = new ButtonTextFieldPanel("Twos");
        threesPnl = new ButtonTextFieldPanel("Threes");
        foursPnl = new ButtonTextFieldPanel("Fours");
        fivesPnl = new ButtonTextFieldPanel("Fives");
        sixesPnl = new ButtonTextFieldPanel("Sixes");

        bonusPnl = new LabelTextFieldPanel("Bonus");
        totalOfUpperSectionPnl = new LabelTextFieldPanel("Total of Upper Section");

        pnlList = new ArrayList<ButtonTextFieldPanel>();
        pnlList.add(acesPnl);
        pnlList.add(twosPnl);
        pnlList.add(threesPnl);
        pnlList.add(foursPnl);
        pnlList.add(fivesPnl);
        pnlList.add(sixesPnl);

        titleLbl.setPreferredSize(new Dimension(400, 20));
        titleLbl.setSize(new Dimension(400, 20));

        setPreferredSize(new Dimension(400, 20 * 9));
        setSize(400, 20 * 9);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        add(titleLbl);

        for (int i = 0; i < pnlList.size(); i++)
            add(pnlList.get(i));

        add(bonusPnl);
        add(totalOfUpperSectionPnl);

        for (Component comp : getComponents()) {
            ((JComponent) comp).setAlignmentX(Component.CENTER_ALIGNMENT);
        }

    }

    public void clear() {
        for (ButtonTextFieldPanel pnl : pnlList) {
            if (pnl.isEnabled()) pnl.clear();
        }
    }

    public void reset() {
        for (ButtonTextFieldPanel pnl : pnlList) pnl.reset();
        bonusPnl.reset();
        totalOfUpperSectionPnl.reset();
    }

    public void update(int[] scores) {
        for (int i = 0; i < pnlList.size(); i++) {
            if (pnlList.get(i).isEnabled()) {
                pnlList.get(i).setValue(scores[i]);
            }
        }
        sumUp();
    }

    public void sumUp() {
        int sum = 0;
        for (ButtonTextFieldPanel pnl : pnlList) {
            if (!pnl.isEnabled()) {
                sum += pnl.getValue();
            }
        }
        if (sum >= 63) {
            bonusPnl.setValue(35);
            sum += 35;
        }
        totalOfUpperSectionPnl.setValue(sum);
    }

    public int getTotal() { return totalOfUpperSectionPnl.getValue(); }

    public ArrayList<ButtonTextFieldPanel> getPnlList() {
        return pnlList;
    }

    public void loadData(int[] upperSectionPnlData) {
        for (int i = 0; i < pnlList.size(); i++) {
            pnlList.get(i).setValue(upperSectionPnlData[i]);
            if (upperSectionPnlData[i] < 0) pnlList.get(i).setEnabled(true);
            else pnlList.get(i).setEnabled(false);
        }
        bonusPnl.setValue(upperSectionPnlData[6]);
        totalOfUpperSectionPnl.setValue(upperSectionPnlData[7]);
    }

    public int[] getData() {
        int[] scores = new int[8];
        scores[0] = acesPnl.getValue();
        scores[1] = twosPnl.getValue();
        scores[2] = threesPnl.getValue();
        scores[3] = foursPnl.getValue();
        scores[4] = fivesPnl.getValue();
        scores[5] = sixesPnl.getValue();
        scores[6] = bonusPnl.getValue();
        scores[7] = totalOfUpperSectionPnl.getValue();
        return scores;
    }
    
    public static void main(String[] argv) {
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        mainFrame.setSize(280, 400);
        UpperSectionPanel upperSectionPanel = new UpperSectionPanel();
        mainFrame.add(upperSectionPanel);
        JButton btn = new JButton("update");
        mainFrame.add(btn);
        btn.setSize(80, 20);
        btn.setLocation(100, upperSectionPanel.getHeight());;
        
        
        btn.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                Random rnd = new Random();
                int[] scores = new int[6];
                for (int i = 0; i < 6; i++) {
                    scores[i] = rnd.nextInt(1000);
                }
                System.out.println(scores);
                upperSectionPanel.update(scores);
            }

			@Override
			public void mouseClicked(MouseEvent e) { ; }
			@Override
			public void mousePressed(MouseEvent e) { ; }
			@Override
			public void mouseEntered(MouseEvent e) { ; }
			@Override
			public void mouseExited(MouseEvent e) { ; }

        });
    }
}