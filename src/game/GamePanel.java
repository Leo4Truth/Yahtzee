package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private JPanel dicesPnl;
    private JButton rollDiceBtn;

    private ArrayList<Dice> diceList;

    public GamePanel() {

        dicesPnl = new JPanel();
        rollDiceBtn = new JButton("Roll Dice");

        dicesPnl.setPreferredSize(new Dimension(400, 90));
        dicesPnl.setSize(400, 90);
        dicesPnl.setAlignmentX(Component.CENTER_ALIGNMENT);
        dicesPnl.setLayout(new GridLayout(0, 5));

        rollDiceBtn.setPreferredSize(new Dimension(100, 20));
        rollDiceBtn.setSize(100, 20);
        rollDiceBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        // rollDiceBtn.setAlignmentY(20);

        diceList = new ArrayList<Dice>();
        for (int i = 0; i < 5; i++) {
            Dice dice = new Dice();
            diceList.add(dice);
            dicesPnl.add(dice);
        }

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(400, 120));
        setSize(400, 120);

        add(dicesPnl);
        add(rollDiceBtn);

        rollDiceBtn.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                rollDice();
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

        for (Dice dice : diceList) {
            dice.getCheckBox().addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean allKept = true;
                    for (Dice d : diceList) {
                        if (!d.isKept()) allKept = false;
                    }
                    if (allKept) rollDiceBtn.setEnabled(false);
                    else rollDiceBtn.setEnabled(true);
                }
                
            });
        }
    }

    public void rollDice() {
        for (Dice dice : diceList) {
            if (!dice.isKept()) {
                dice.roll();
            }
        }
    }

    public JButton getRollDiceBtn() { return rollDiceBtn; }
    public int[] getDices() {
        int[] dices = new int[5];
        for (int i = 0; i < 5; i++) dices[i] = diceList.get(i).getNum();
        return dices;
    }

    public void reset() {
        for (Dice dice : diceList) {
            dice.reset();
        }
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        mainFrame.setPreferredSize(new Dimension(500, 400));
        mainFrame.setSize(500, 400);
        mainFrame.getContentPane().setBackground(Color.red);
        GamePanel gamePnl = new GamePanel();
        mainFrame.add(gamePnl);
    }
}
