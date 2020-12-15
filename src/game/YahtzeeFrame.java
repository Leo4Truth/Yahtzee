package game;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class YahtzeeFrame extends JFrame {

	// UI
	private JMenuBar menuBar;
	private JMenu gameMenu;
	private JMenuItem loadGameMenuItem;
	private JMenuItem saveGameMenuItem;
	private JMenuItem exitGameMenuItem;

	private GamePanel gamePnl;
	private UpperSectionPanel upperSectionPnl;
	private LowerSectionPanel lowerSectionPnl;

	private LabelTextFieldPanel totalScorePnl;

	private JButton resetBtn;

	// data
	private int[] dices;
	private boolean[] kept;
	private int ones;
	private int twos;
	private int threes;
	private int fours;
	private int fives;
	private int sixes;
	private int bonus;
	private int totalOfUpperSection;
	private int threeOfAKind;
	private int fourOfAKind;
	private int fullHouse;
	private int smallStraight;
	private int largeStraight;
	private int chance;
	private int yahtzee;
	private int yahtzeeBonus;
	private int totalOfLowerSection;
	private int totalScore;

	public YahtzeeFrame() {

		// Menu
		menuBar = new JMenuBar();
		gameMenu = new JMenu("Game");
		loadGameMenuItem = new JMenuItem("Load Game");
		saveGameMenuItem = new JMenuItem("Save Game");
		exitGameMenuItem = new JMenuItem("Exit");

		setJMenuBar(menuBar);
		menuBar.add(gameMenu);
		gameMenu.add(loadGameMenuItem);
		gameMenu.add(saveGameMenuItem);
		gameMenu.add(exitGameMenuItem);

		gamePnl = new GamePanel();
		upperSectionPnl = new UpperSectionPanel();
		lowerSectionPnl = new LowerSectionPanel();
		totalScorePnl = new LabelTextFieldPanel("Total Score");
		resetBtn = new JButton("Reset");

		// getContentPane().setLayout(new BoxLayout(getContentPane(),
		// BoxLayout.PAGE_AXIS));
		getContentPane().setLayout(null);
		add(gamePnl);
		add(upperSectionPnl);
		add(lowerSectionPnl);
		add(totalScorePnl);
		add(resetBtn);

		gamePnl.setLocation(0, 0);
		upperSectionPnl.setLocation(0, gamePnl.getHeight());
		lowerSectionPnl.setLocation(0, gamePnl.getHeight() + upperSectionPnl.getHeight());
		totalScorePnl.setLocation(0, gamePnl.getHeight() + upperSectionPnl.getHeight() + lowerSectionPnl.getHeight());
		resetBtn.setLocation(150, gamePnl.getHeight() + upperSectionPnl.getHeight() + lowerSectionPnl.getHeight()
				+ totalScorePnl.getHeight());
		resetBtn.setSize(100, 20);
		resetBtn.setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension size = new Dimension(400, gamePnl.getHeight() + upperSectionPnl.getHeight()
				+ lowerSectionPnl.getHeight() + totalScorePnl.getHeight() + 70);
		setPreferredSize(size);
		setSize(size);
		setResizable(false);

		
		gamePnl.getRollDiceBtn().addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO: turns
				scores(gamePnl.getDices());
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

	public void initData() {
		for (int i = 0; i < 5; i++) {
			Random rnd = new Random();
			dices[i] = rnd.nextInt(6) + 1;
			kept[i] = false;
		}
		ones = 0;
		twos = 0;
		threes = 0;
		fours = 0;
		fives = 0;
		sixes = 0;
		bonus = 0;
		totalOfUpperSection = 0;
		threeOfAKind = 0;
		fourOfAKind = 0;
		fullHouse = 0;
		smallStraight = 0;
		largeStraight = 0;
		chance = 0;
		yahtzee = 0;
		yahtzeeBonus = 0;
		totalOfLowerSection = 0;
		totalScore = 0;
	}
	
	public int[] scores(int[] dices) {
		int[] res = new int[10];
		System.out.println("Dices: " + Arrays.toString(dices));
		Arrays.sort(dices);
		System.out.println("Sorted Dices: " + Arrays.toString(dices));
		int aces_ = xs(dices, 1);
		int twos_ = xs(dices, 2);
		int threes_ = xs(dices, 3);
		int fours_ = xs(dices, 4);
		int fives_ = xs(dices, 5);
		int sixes_ = xs(dices, 6);

		int[] upperSectionScores = new int[6];
		upperSectionScores[0] = aces_;
		upperSectionScores[1] = twos_;
		upperSectionScores[2] = threes_;
		upperSectionScores[3] = fours_;
		upperSectionScores[4] = fives_;
		upperSectionScores[5] = sixes_;

		int threeOfAKind_ = nOfAKind(dices, 3);
		int fourOfAKind_ = nOfAKind(dices, 4);
		int fullHouse_ = fullHouse(dices);
		int smallStraight_ = smallStraight(dices);
		int largeStraight_ = largeStraight(dices);
		int chance_ = chance(dices);
		int yahtzee_ = yahtzeeCheck(dices);

		int[] lowerSectionScores = new int[7];
		lowerSectionScores[0] = threeOfAKind_;
		lowerSectionScores[1] = fourOfAKind_;
		lowerSectionScores[2] = fullHouse_;
		lowerSectionScores[3] = smallStraight_;
		lowerSectionScores[4] = largeStraight_;
		lowerSectionScores[5] = chance_;
		lowerSectionScores[6] = yahtzee_;

		upperSectionPnl.update(upperSectionScores);
		lowerSectionPnl.update(lowerSectionScores);

		/*
		System.out.println("aces: " + aces_);
		System.out.println("twos: " + twos_);
		System.out.println("threes: " + threes_);
		System.out.println("fours: " + fours_);
		System.out.println("fives: " + fives_);
		System.out.println("sixes: " + sixes_);
		System.out.println("three of a kind: " + threeOfAKind_);
		System.out.println("four of a kind: " + fourOfAKind_);
		System.out.println("fullHouse: " + fullHouse_);
		System.out.println("small straight: " + smallStraight_);
		System.out.println("large straight: " + largeStraight_);
		System.out.println("chance: " + chance_);
		System.out.println("yahtzee: " + yahtzee_);
		*/

		return res;
	}

	public int xs(int[] dices, int x) {
		int cnt = 0;
		for (int i = 0; i < 5; i++) {
			if (dices[i] == x) {
				cnt++;
			}
		}
		return cnt * x;
	}

	public int nOfAKind(int[] dices, int n) {
		int[] cnt = new int[6];
		int score = 0;
		for (int i = 0; i < 6; i++) cnt[i] = 0;
		for (int i = 0; i < 5; i++) {
			cnt[dices[i] - 1]++;
			score += dices[i];
		}
		for (int i = 0; i < 6; i++) {
			if (cnt[i] >= n) {
				return score;
			}
		}
		return 0;
	}

	public int fullHouse(int[] dices) {
		if (nOfAKind(dices, 3) != 0 && nOfAKind(dices, 2) != 0) return 25;
		return 0;
	}

	public int smallStraight(int[] dices) {
		int[] cnt = new int[6];
		for (int i = 0; i < 6; i++) cnt[i] = 0;
		for (int i = 0; i < 5; i++) {
			cnt[dices[i] - 1]++;
		}
		if ((cnt[0] >= 1 && cnt[1] >= 1 && cnt[2] >= 1 && cnt[3] >= 1) ||
			(cnt[1] >= 1 && cnt[2] >= 1 && cnt[3] >= 1 && cnt[4] >= 1) ||
			(cnt[2] >= 1 && cnt[3] >= 1 && cnt[4] >= 1 && cnt[5] >= 1)) {
			return 30;
		}
		return 0;
	}
	
	public int largeStraight(int[] dices) {
		int[] cnt = new int[6];
		for (int i = 0; i < 6; i++) cnt[i] = 0;
		for (int i = 0; i < 5; i++) {
			cnt[dices[i] - 1]++;
		}
		if ((cnt[0] >= 1 && cnt[1] >= 1 && cnt[2] >= 1 && cnt[3] >= 1 && cnt[4] >= 1) ||
			(cnt[1] >= 1 && cnt[2] >= 1 && cnt[3] >= 1 && cnt[4] >= 1 && cnt[5] >= 1)) {
			return 40;
		}
		return 0;
	}

	public int chance(int[] dices) {
		int sum = 0;
		for (int i = 0; i < 5; i++) {
			sum += dices[i];
		}
		return sum;
	}

	public int yahtzeeCheck(int[] dices) {
		if (nOfAKind(dices, 5) > 0) return 50;
		else return 0; 
	}
	
	public static void main(String args[]) {
		YahtzeeFrame yahtzee = new YahtzeeFrame();
		yahtzee.setVisible(true);
	}
}
