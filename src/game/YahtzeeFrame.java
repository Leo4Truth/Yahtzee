package game;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import client.Client;
import server.SaveServer;

public class YahtzeeFrame extends JFrame {

	// network
	private Client client = null;

	// UI
	private JMenuBar menuBar;
	private JMenu gameMenu;
	private JMenuItem loadGameMenuItem;
	private JMenuItem saveGameMenuItem;
	private JMenuItem exitGameMenuItem;

	private LabelTextFieldPanel namePnl;
	private LabelTextFieldPanel roundLeftPnl;
	private LabelTextFieldPanel turnLeftPnl;
	private GamePanel gamePnl;
	private UpperSectionPanel upperSectionPnl;
	private LowerSectionPanel lowerSectionPnl;

	private LabelTextFieldPanel totalScorePnl;

	private JButton resetBtn;

	// data
	private YahtzeeData data;

	public YahtzeeFrame() {
		setVisible(true);
		setTitle("Yahtzee");
		setIconImage(new ImageIcon("resource/dice-96.png").getImage());

		//server = new SaveServer();

		data = new YahtzeeData();

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

		loadGameMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				load();
			}

		});

		saveGameMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}

		});

		exitGameMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});

		// Content
		namePnl = new LabelTextFieldPanel("Player Name");
		namePnl.setEditable(true);
		namePnl.getTxtField().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Player Name: " + namePnl.getTxtField().getText());
				data.name = namePnl.getTxtField().getText();
			}
		});
		namePnl.getTxtField().setText("player 1");

		roundLeftPnl = new LabelTextFieldPanel("Round Left");
		roundLeftPnl.setEditable(false);
		roundLeftPnl.setValue(data.roundLeft);
		
		turnLeftPnl = new LabelTextFieldPanel("Turn Left");
		turnLeftPnl.setEditable(false);
		turnLeftPnl.setValue(data.turnLeft);

		gamePnl = new GamePanel();
		upperSectionPnl = new UpperSectionPanel();
		lowerSectionPnl = new LowerSectionPanel();
		totalScorePnl = new LabelTextFieldPanel("Total Score");
		resetBtn = new JButton("Reset");

		getContentPane().setLayout(null);
		add(namePnl);
		add(roundLeftPnl);
		add(turnLeftPnl);
		add(gamePnl);
		add(upperSectionPnl);
		add(lowerSectionPnl);
		add(totalScorePnl);
		add(resetBtn);

		int verticalOffset = 0;
		namePnl.setLocation(0, 0);
		verticalOffset += namePnl.getHeight();
		roundLeftPnl.setLocation(0, verticalOffset);
		verticalOffset += roundLeftPnl.getHeight();
		turnLeftPnl.setLocation(0, verticalOffset);
		verticalOffset += turnLeftPnl.getHeight();
		gamePnl.setLocation(0, verticalOffset);
		verticalOffset += gamePnl.getHeight();
		upperSectionPnl.setLocation(0, verticalOffset);
		verticalOffset +=  upperSectionPnl.getHeight();
		lowerSectionPnl.setLocation(0, verticalOffset);
		verticalOffset +=  lowerSectionPnl.getHeight();
		totalScorePnl.setLocation(0, verticalOffset);
		verticalOffset +=  totalScorePnl.getHeight();
		resetBtn.setLocation(150, verticalOffset);
		resetBtn.setSize(100, 20);
		resetBtn.setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension size = new Dimension(
			400,
			gamePnl.getHeight() + roundLeftPnl.getHeight() + turnLeftPnl.getHeight() + upperSectionPnl.getHeight() + lowerSectionPnl.getHeight() + totalScorePnl.getHeight() + 95
		);
		setPreferredSize(size);
		setSize(size);
		setResizable(false);

		gamePnl.getRollDiceBtn().addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (gamePnl.getRollDiceBtn().isEnabled()) {
					gamePnl.rollDice();
					if (--data.turnLeft == 0) gamePnl.getRollDiceBtn().setEnabled(false);
					turnLeftPnl.setValue(data.turnLeft);
					scores(gamePnl.getDices());
					updateData();
				}
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

		ArrayList<ButtonTextFieldPanel> upperPnlList = upperSectionPnl.getPnlList();
		ArrayList<ButtonTextFieldPanel> lowerPnlList = lowerSectionPnl.getPnlList();
		for (ButtonTextFieldPanel pnl : upperPnlList) {
			JButton btn = pnl.getBtn();
			JTextField txtField = pnl.getTxtField();

			btn.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if (btn.isEnabled()) {
						if (txtField.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Please roll dices");
							return;
						}
						btn.setEnabled(false);

						data.roundLeft--;
						roundLeftPnl.setValue(data.roundLeft);
						data.turnLeft = 3;
						turnLeftPnl.setValue(data.turnLeft);
						
						gamePnl.reset();
						upperSectionPnl.clear();
						upperSectionPnl.sumUp();
						lowerSectionPnl.clear();
						lowerSectionPnl.sumUp();
						
						updateData();

						if (data.roundLeft == 0) {
							gamePnl.getRollDiceBtn().setEnabled(false);
							gameOver();
						}
					}
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

		for (ButtonTextFieldPanel pnl : lowerPnlList) {
			JButton btn = pnl.getBtn();
			JTextField txtField = pnl.getTxtField();
			
			btn.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {

					if (btn.isEnabled()) {
						if (txtField.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "!");
							return;
						}
						btn.setEnabled(false);
						
						data.roundLeft--;
						roundLeftPnl.setValue(data.roundLeft);
						data.turnLeft = 3;
						turnLeftPnl.setValue(data.turnLeft);

						gamePnl.reset();
						upperSectionPnl.clear();
						upperSectionPnl.sumUp();
						lowerSectionPnl.clear();
						lowerSectionPnl.sumUp();

						if (btn.equals(lowerPnlList.get(6).getBtn())) {
							lowerSectionPnl.yahtzeeBonusUpdate();
						}
						
						updateData();

						if (data.roundLeft == 0) {
							gamePnl.getRollDiceBtn().setEnabled(false);
							gameOver();
						}
					}
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
		

		resetBtn.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (resetBtn.isEnabled()) {
					reset();
				}
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

	public void loadData(YahtzeeData d) {
		data = d;
		int[] upperSectionPnlData = {
			d.aces,
			d.twos,
			d.threes,
			d.fours,
			d.fives,
			d.sixes,
			d.bonus,
			d.totalOfUpperSection
		};
		int[] lowerSectionPnlData = {
			d.threeOfAKind,
			d.fourOfAKind,
			d.fullHouse,
			d.smallStraight,
			d.largeStraight,
			d.chance,
			d.yahtzee,
			d.yahtzeeBonus,
			d.totalOfLowerSection
		};
		namePnl.getTxtField().setText(d.name);
		roundLeftPnl.setValue(d.roundLeft);
		turnLeftPnl.setValue(d.turnLeft);
		gamePnl.loadData(d.dices, d.kept);
		upperSectionPnl.loadData(upperSectionPnlData);
		lowerSectionPnl.loadData(lowerSectionPnlData);
		totalScorePnl.setValue(d.totalScore);
		if (d.roundLeft == 0) gamePnl.getRollDiceBtn().setEnabled(false);
		if (d.turnLeft == 0) gamePnl.getRollDiceBtn().setEnabled(false);
	}

	public void updateData() {
		data.name = namePnl.getTxtField().getText();

		data.dices = gamePnl.getDicesData();
		data.kept = gamePnl.getKeptData();

		int[] upperSectionScores = upperSectionPnl.getData();
		int[] lowerSectionScores = lowerSectionPnl.getData();

		ArrayList<ButtonTextFieldPanel> upperPnlList = upperSectionPnl.getPnlList();
		ArrayList<ButtonTextFieldPanel> lowerPnlList = lowerSectionPnl.getPnlList();
		
		if (!upperPnlList.get(0).isEnabled()) data.aces = upperSectionScores[0];
		if (!upperPnlList.get(1).isEnabled()) data.twos = upperSectionScores[1];
		if (!upperPnlList.get(2).isEnabled()) data.threes = upperSectionScores[2];
		if (!upperPnlList.get(3).isEnabled()) data.fours = upperSectionScores[3];
		if (!upperPnlList.get(4).isEnabled()) data.fives = upperSectionScores[4];
		if (!upperPnlList.get(5).isEnabled()) data.sixes = upperSectionScores[5];
		
		data.bonus = upperSectionScores[6];
		data.totalOfUpperSection = upperSectionScores[7];
		
		if (!lowerPnlList.get(0).isEnabled()) data.threeOfAKind = lowerSectionScores[0];
		if (!lowerPnlList.get(1).isEnabled()) data.fourOfAKind = lowerSectionScores[1];
		if (!lowerPnlList.get(2).isEnabled()) data.fullHouse = lowerSectionScores[2];
		if (!lowerPnlList.get(3).isEnabled()) data.smallStraight = lowerSectionScores[3];
		if (!lowerPnlList.get(4).isEnabled()) data.largeStraight = lowerSectionScores[4];
		if (!lowerPnlList.get(5).isEnabled()) data.chance = lowerSectionScores[5];
		if (!lowerPnlList.get(6).isEnabled()) data.yahtzee = lowerSectionScores[6];
		
        data.yahtzeeBonus = lowerSectionScores[7];
		data.totalOfLowerSection = lowerSectionScores[8];

		data.totalScore = data.totalOfUpperSection + data.totalOfLowerSection;
		totalScorePnl.setValue(data.totalScore);

		System.out.println("update data");
		print(data);
		System.out.println("");
	}
	
	public void scores(int[] dices) {
		if (dices[0] == -1) return;

		Arrays.sort(dices);

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
	}

	public void loadScore(ButtonTextFieldPanel pnl, int x) {
		pnl.setValue(x);
		pnl.getBtn().setEnabled(false);
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
		int[] cnt = new int[6];
		int zeroCnt = 0;
		int twoCnt = 0;
		int threeCnt = 0;
		for (int i = 0; i < 6; i++) cnt[i] = 0;
		for (int i = 0; i < 5; i++) cnt[dices[i] - 1]++;
		for (int i = 0; i < 6; i++) {
			if (cnt[i] == 0) zeroCnt++;
			else if (cnt[i] == 2) twoCnt++;
			else if (cnt[i] == 3) threeCnt++;
		}
		System.out.println("full house check");
		System.out.println(cnt);
		System.out.println(zeroCnt);
		System.out.println(twoCnt);
		System.out.println(threeCnt);
		System.out.println("");
		if (zeroCnt == 4 && twoCnt == 1 && threeCnt == 1) return 25;
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

	public void gameOver() {
		JOptionPane.showMessageDialog(null, "Game Over\nYour final score is " + data.totalScore);
	}

	public void reset() {
		data.roundLeft = 13;
		data.turnLeft = 3;
		data.totalScore = -1;
		namePnl.getTxtField().setText("");
		gamePnl.reset();
		namePnl.getTxtField().setText("player 1");
		for (int i = 0; i < 5; i++) {
			data.dices[i] = -1;
			data.kept[i] = false;
		}
		roundLeftPnl.setValue(data.roundLeft);
		turnLeftPnl.setValue(data.turnLeft);
		upperSectionPnl.reset();
		lowerSectionPnl.reset();
		updateData();
	}

	public void load() {
		
		if (client == null) client = new Client();
		else if (!client.isConnected()) client = new Client();

		ArrayList<YahtzeeArchiveInfo> infoList = client.query();
		
		LoadingFrame loadingFrame = new LoadingFrame(infoList);
		
		loadingFrame.getLoadBtn().addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
				JTable table = loadingFrame.getTable();
				int row = table.getSelectedRow();
				if (row != -1) {
					int id = (int) table.getValueAt(row, 0);
					YahtzeeData d = client.request(id);
					loadData(d);
					System.out.println("load data");
					print(data);
					scores(data.dices);
					System.out.println("");
					loadingFrame.dispose();
				}
				else JOptionPane.showMessageDialog(null, "No data selected");
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

	public void save() {
		if (client == null) client = new Client();
		else if (!client.isConnected()) client = new Client();
		data.setTimestamp();
		client.save(data);
	}

	public void print(YahtzeeData data) {
		System.out.println("aces: " + data.aces);
		System.out.println("twos: " + data.twos);
		System.out.println("threes: " + data.threes);
		System.out.println("fours: " + data.fours);
		System.out.println("fives: " + data.fives);
		System.out.println("sixes: " + data.sixes);
		System.out.println("bonus: " + data.bonus);
		System.out.println("total of upper section: " + data.totalOfUpperSection);
		System.out.println("three of a kind: " + data.threeOfAKind);
		System.out.println("four of a kind: " + data.fourOfAKind);
		System.out.println("full house: " + data.fullHouse);
		System.out.println("small straight: " + data.smallStraight);
		System.out.println("large straight: " + data.largeStraight);
		System.out.println("chance: " + data.chance);
		System.out.println("yahtzee: " + data.yahtzee);
		System.out.println("yahtzee bonus: " + data.yahtzeeBonus);
		System.out.println("total of lower section: " + data.totalOfLowerSection);
	}
	
	public static void main(String args[]) {
		YahtzeeFrame yahtzee = new YahtzeeFrame();
		yahtzee.setVisible(true);
	}
}
