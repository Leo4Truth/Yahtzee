package game;

import java.sql.Timestamp;
import java.util.Random;

public class YahtzeeData implements java.io.Serializable {

	public String name;
	public Timestamp timestamp;
	public int roundLeft;
    public int turnLeft;

	public int[] dices;
	public boolean[] kept;
	public int aces;
	public int twos;
	public int threes;
	public int fours;
	public int fives;
	public int sixes;
	public int bonus;
	public int totalOfUpperSection;
	public int threeOfAKind;
	public int fourOfAKind;
	public int fullHouse;
	public int smallStraight;
	public int largeStraight;
	public int chance;
	public int yahtzee;
	public int yahtzeeBonus;
	public int totalOfLowerSection;
	public int totalScore;
    
    public YahtzeeData() {
		reset();
    }
	
	public void reset() {
		name = "Player 1";
		timestamp = new java.sql.Timestamp(System.currentTimeMillis());
		roundLeft = 13;
		turnLeft = 3;

        dices = new int[5];
        kept = new boolean[5];
		for (int i = 0; i < 5; i++) {
			dices[i] = -1;
			kept[i] = false;
		}

        aces = -1;
		twos = -1;
		threes = -1;
		fours = -1;
		fives = -1;
		sixes = -1;
		bonus = 0;
		totalOfUpperSection = 0;

		threeOfAKind = -1;
		fourOfAKind = -1;
		fullHouse = -1;
		smallStraight = -1;
		largeStraight = -1;
		chance = -1;
		yahtzee = -1;
		yahtzeeBonus = 0;
		totalOfLowerSection = 0;

		totalScore = 0;
	}

	public void setTimestamp() {
		timestamp = new java.sql.Timestamp(System.currentTimeMillis());
	}
	
	@Override
	public String toString() {
		return name + ", " + timestamp.toString();
	}
}
