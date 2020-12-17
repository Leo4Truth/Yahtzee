package game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dice extends JPanel {
	
	// file pathes of the six different dice faces
	private static final String[] DICE_IMG_PATH = new String[] {
		"resource/die1.png",
		"resource/die2.png",
		"resource/die3.png",
		"resource/die4.png",
		"resource/die5.png",
		"resource/die6.png"
	};
	
	private Image[] diceImages = new Image[6]; // Images of 6 different number dice face
	
	private Image img; // the image of the current dice
	private JLabel lbl; // Jlabel component contain the dice image
	private JCheckBox keepChkBox; // JCheckBox determine whether the dice is kept
	//private List<Integer> imageSequence;
	
	private int num; // current dice number

	public Dice() {
		// Load the images
		for (int i = 0; i < 6; i++) {
			ImageIcon icon = new ImageIcon(DICE_IMG_PATH[i]);
			Image img = icon.getImage();
			diceImages[i] = img.getScaledInstance(70, 70, Image.SCALE_DEFAULT);
		}

		// initialize the panel with a random dice number
		Random rnd = new Random();
		num = rnd.nextInt(6) + 1;
		img = diceImages[num - 1];

		// create the components
		lbl = new JLabel(new ImageIcon(img));
		keepChkBox = new JCheckBox("Keep");

		// set layout and add components
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(lbl);
		add(keepChkBox);
		
		repaint();
	}

	public int getNum() { return num; }
	public void setNum(int x) {
		num = x;
		if (num < 0) {
			setVisible(false);
			return;
		}
		setVisible(true);
		img = diceImages[num - 1];
		lbl.setIcon(new ImageIcon(img));
		repaint();
	}

	public boolean isKept() { return keepChkBox.isSelected(); }
	public void setKept(boolean keep) { keepChkBox.setSelected(keep); }
	public JCheckBox getCheckBox() { return keepChkBox; }
	
	public void roll() {
		Random rnd = new Random();
		num = rnd.nextInt(6) + 1;
		setNum(num);
		repaint();
	}

	public void reset() {
		num = -1;
		roll();
		setKept(false);
		setVisible(false);
	}
	
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		lbl.repaint();
	}
	
    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new FlowLayout());
		mainFrame.setSize(1000, 400);
		mainFrame.getContentPane().setBackground(Color.blue);
		mainFrame.getContentPane().setVisible(true);//如果改为true那么就变成了红色
        mainFrame.setVisible(true);
        Dice dice1 = new Dice();
        Dice dice2 = new Dice();
        Dice dice3 = new Dice();
        Dice dice4 = new Dice();
        Dice dice5 = new Dice();
        Dice dice6 = new Dice();
        mainFrame.add(dice1);
        mainFrame.add(dice2);
        mainFrame.add(dice3);
        mainFrame.add(dice4);
        mainFrame.add(dice5);
		mainFrame.add(dice6);
    }
}
