package game;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.ArrayList;

public class LowerSectionPanel extends JPanel {

    private JLabel titleLbl;

    private ButtonTextFieldPanel threeOfAKindPnl;
    private ButtonTextFieldPanel fourOfAKindPnl;
    private ButtonTextFieldPanel fullHousePnl;
    private ButtonTextFieldPanel smallStraightPnl;
    private ButtonTextFieldPanel largeStraightPnl;
    private ButtonTextFieldPanel chancePnl;
    private ButtonTextFieldPanel yahtzeePnl;

    private LabelTextFieldPanel yahtzeeBonusPnl;
    private LabelTextFieldPanel totalOfLowerSectionPnl;

    private ArrayList<ButtonTextFieldPanel> pnlList;

    public LowerSectionPanel() {

        titleLbl = new JLabel("Lower Section");

        threeOfAKindPnl = new ButtonTextFieldPanel("Three of A Kind");
        fourOfAKindPnl = new ButtonTextFieldPanel("Four of A Kind");
        fullHousePnl = new ButtonTextFieldPanel("Full House");
        smallStraightPnl = new ButtonTextFieldPanel("Small Straight");
        largeStraightPnl = new ButtonTextFieldPanel("Large Straight");
        chancePnl = new ButtonTextFieldPanel("Chance");
        yahtzeePnl = new ButtonTextFieldPanel("Yahtzee");

        yahtzeeBonusPnl = new LabelTextFieldPanel("Yahtzee Bonus");
        totalOfLowerSectionPnl = new LabelTextFieldPanel("Total of Lower Section");

        pnlList = new ArrayList<ButtonTextFieldPanel>();
        pnlList.add(threeOfAKindPnl);
        pnlList.add(fourOfAKindPnl);
        pnlList.add(fullHousePnl);
        pnlList.add(smallStraightPnl);
        pnlList.add(largeStraightPnl);
        pnlList.add(chancePnl);
        pnlList.add(yahtzeePnl);

        titleLbl.setPreferredSize(new Dimension(400, 20));
        titleLbl.setSize(new Dimension(400, 20));

        setPreferredSize(new Dimension(400, 20 * 10));
        setSize(400, 20 * 10);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        add(titleLbl);

        for (int i = 0; i < pnlList.size(); i++)
            add(pnlList.get(i));

        add(yahtzeeBonusPnl);
        add(totalOfLowerSectionPnl);

        for (Component comp : getComponents()) {
            ((JComponent) comp).setAlignmentX(Component.CENTER_ALIGNMENT);
        }


        for (int i = 0; i < pnlList.size(); i++) {
            ButtonTextFieldPanel pnl = pnlList.get(i);
            pnl.getBtn().addMouseListener(new MouseListener() {

                @Override
                public void mouseReleased(MouseEvent e) {
                    clear();
                    totalOfLowerSectionUpdate();
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
        } // end of for loop to addMouseListener to button
        
    }

    public void clear() {
        for (ButtonTextFieldPanel pnl : pnlList) {
            if (pnl.isEnabled()) pnl.clear();
        }
    }

    public void update(int[] scores) {
        for (int i = 0; i < pnlList.size(); i++) {
            if (pnlList.get(i).isEnabled()) {
                pnlList.get(i).setValue(scores[i]);
            }
        }
        totalOfLowerSectionUpdate();
    }

    public void yahtzeeBonusUpdate() {
        yahtzeeBonusPnl.setValue(yahtzeeBonusPnl.getValue() + 100);
    }

    public void totalOfLowerSectionUpdate() {
        int sum = 0;
        for (ButtonTextFieldPanel pnl : pnlList) {
            if (!pnl.isEnabled()) {
                sum += pnl.getValue();
            }
        }
        sum += yahtzeeBonusPnl.getValue();
        totalOfLowerSectionPnl.setValue(sum);
    }

    public ArrayList<ButtonTextFieldPanel> getPnlList() {
        return pnlList;
    }

    public static void main(String[] argv) {
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        mainFrame.setSize(280, 400);
        LowerSectionPanel lowerSectionPanel = new LowerSectionPanel();
        mainFrame.add(lowerSectionPanel);
    }
}
