package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.rmi.server.ObjID;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import game.YahtzeeData;
import game.YahtzeeArchiveInfo;

public class ServerThread extends Thread {

    private static final String URL = "jdbc:sqlite:yahtzee.db";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS yahtzee ("
            + "    id              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
            + "    timestamp       TEXT    NOT NULL," + "    name            TEXT    NOT NULL,"
            + "    roundleft       INT     NOT NULL," + "    turnleft        INT     NOT NULL,"
            + "    dice1           INT     NOT NULL," + "    dice2           INT     NOT NULL,"
            + "    dice3           INT     NOT NULL," + "    dice4           INT     NOT NULL,"
            + "    dice5           INT     NOT NULL," + "    kept1           INT     NOT NULL,"
            + "    kept2           INT     NOT NULL," + "    kept3           INT     NOT NULL,"
            + "    kept4           INT     NOT NULL," + "    kept5           INT     NOT NULL,"
            + "    aces            INT     NOT NULL," + "    twos            INT     NOT NULL,"
            + "    threes          INT     NOT NULL," + "    fours           INT     NOT NULL,"
            + "    fives           INT     NOT NULL," + "    sixes           INT     NOT NULL,"
            + "    bonus           INT     NOT NULL,"
            + "    total_of_upper_section  INT     NOT NULL,"
            + "    three_of_a_kind INT     NOT NULL," + "    four_of_a_kind  INT     NOT NULL,"
            + "    full_house      INT     NOT NULL," + "   small_straight  INT     NOT NULL,"
            + "    large_straight  INT     NOT NULL," + "    chance          INT     NOT NULL,"
            + "    yahtzee         INT     NOT NULL," + "    yahtzee_bonus   INT     NOT NULL,"
            + "    total_of_lower_section   INT     NOT NULL,"
            + "    total_score              INT     NOT NULL"
            + ");";
    private static final String SQL_INSERT = "INSERT INTO yahtzee VALUES ("
            + "    NULL, ?, ?, ?, ?, " // 4
            + "    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 10
            + "    ?, ?, ?, ?, ?, ?, ?, ?, " // 8
            + "    ?, ?, ?, ?, ?, ?, ?, ?, ?, " // 9
            + "    ? " // 1
            + ");";
    private static final String SQL_QUERY_INFO = "SELECT id, name, timestamp, roundLeft, turnLeft FROM yahtzee;";
    private static final String SQL_QUERY_DATA = "SELECT * FROM yahtzee WHERE id = ?;";

    private Socket sock_data = null;
    private Socket sock_comm = null;
    private Connection conn = null;

    public ServerThread(Socket sock_comm, Socket sock_data) {
        this.sock_comm = sock_comm;
        this.sock_data = sock_data;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
            PreparedStatement pstmt = conn.prepareStatement(SQL_CREATE_TABLE);
            pstmt.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        InputStream comm_in = null;
        OutputStream comm_out = null;

        InputStream data_in = null;
        OutputStream data_out = null;
        try {
            comm_in = sock_comm.getInputStream();
            comm_out = sock_comm.getOutputStream();
            data_in = sock_data.getInputStream();
            data_out = sock_data.getOutputStream();
            handle(comm_in, comm_out, data_in, data_out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void handle(InputStream comm_in, OutputStream comm_out, InputStream data_in, OutputStream data_out)
            throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(comm_in, StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(comm_out, StandardCharsets.UTF_8));

            // order!!!!!
            ObjectInputStream objIn = new ObjectInputStream(data_in);
            ObjectOutputStream objOut = new ObjectOutputStream(data_out);

            String msg;
            for (;;) {
                msg = reader.readLine();
                System.out.println("recv: " + msg);
                if (msg.equals("list")) {
                    System.out.println("list");
                    ArrayList<YahtzeeArchiveInfo> list = queryInfoFormDB();
                    writer.write(String.valueOf(list.size()));
                    writer.newLine();
                    writer.flush();
                    objOut.writeObject(list);
                    objOut.flush();
                } 
                else if (msg.equals("save")) {
                    System.out.println("save");
                    try {
                        writer.write("send");
                        writer.newLine();
                        writer.flush();

                        Object obj = objIn.readObject();
                        if (obj != null) {
                            YahtzeeData data = (YahtzeeData) obj;
                            saveToDB(data);
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else if (msg.equals("id")) {
                    msg = reader.readLine();
                    int id = Integer.valueOf(msg);
                    System.out.println("load data with id: " + String.valueOf(id));
                    YahtzeeData data = queryDataFromDB(id);
                    objOut.writeObject(data);
                    objOut.flush();
                }
                else if (msg.equals("disconnect")) {
                    System.out.println("disconnect");
                    break;
                }
                else {
                    System.out.println("other");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<YahtzeeArchiveInfo> queryInfoFormDB() {
        ArrayList<YahtzeeArchiveInfo> list = new ArrayList<YahtzeeArchiveInfo>();

        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
            PreparedStatement pstmt = conn.prepareStatement(SQL_QUERY_INFO);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String timestampStr = rs.getString("timestamp");
                String name = rs.getString("name");
                int roundLeft = rs.getInt("roundleft");
                YahtzeeArchiveInfo info = new YahtzeeArchiveInfo(id, timestampStr, name, roundLeft);
                list.add(info);
            }
            
            conn.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public YahtzeeData queryDataFromDB(int id) {
        YahtzeeData data = new YahtzeeData();

        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
            PreparedStatement pstmt = conn.prepareStatement(SQL_QUERY_DATA);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            rs.next();

            data.name = rs.getString("name");
            data.timestamp = Timestamp.valueOf(rs.getString("timestamp"));
            data.roundLeft = rs.getInt("roundleft");
            data.turnLeft = rs.getInt("turnleft");

            data.dices = new int[5];
            data.kept = new boolean[5];
            data.dices[0] = rs.getInt("dice1");
            data.dices[1] = rs.getInt("dice2");
            data.dices[2] = rs.getInt("dice3");
            data.dices[3] = rs.getInt("dice4");
            data.dices[4] = rs.getInt("dice5");
            data.kept[0] = rs.getInt("kept1") == 1;
            data.kept[1] = rs.getInt("kept2") == 1;
            data.kept[2] = rs.getInt("kept3") == 1;
            data.kept[3] = rs.getInt("kept4") == 1;
            data.kept[4] = rs.getInt("kept5") == 1;

            data.aces = rs.getInt("aces");
            data.twos = rs.getInt("twos");
            data.threes = rs.getInt("threes");
            data.fours = rs.getInt("fours");
            data.fives = rs.getInt("fives");
            data.sixes = rs.getInt("sixes");
            data.bonus = rs.getInt("bonus");
            data.totalOfUpperSection = rs.getInt("total_of_upper_section");

            data.threeOfAKind = rs.getInt("three_of_a_kind");
            data.fourOfAKind = rs.getInt("four_of_a_kind");
            data.fullHouse = rs.getInt("full_house");
            data.smallStraight = rs.getInt("small_straight");
            data.largeStraight = rs.getInt("large_straight");
            data.chance = rs.getInt("chance");
            data.yahtzee = rs.getInt("yahtzee");
            data.yahtzeeBonus = rs.getInt("yahtzee_bonus");
            data.totalOfLowerSection = rs.getInt("total_of_lower_section");

            data.totalScore = rs.getInt("total_score");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void saveToDB(YahtzeeData data) {
        System.out.println("save " + data.toString() + " to db");
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
            PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT);
            pstmt.setString(1, data.timestamp.toString());
            pstmt.setString(2, data.name);
            pstmt.setInt(3, data.roundLeft);
            pstmt.setInt(4, data.turnLeft);
            
            pstmt.setInt(5, data.dices[0]);
            pstmt.setInt(6, data.dices[1]);
            pstmt.setInt(7, data.dices[2]);
            pstmt.setInt(8, data.dices[3]);
            pstmt.setInt(9, data.dices[4]);
            pstmt.setInt(10, data.kept[0] ? 1 : 0);
            pstmt.setInt(11, data.kept[1] ? 1 : 0);
            pstmt.setInt(12, data.kept[2] ? 1 : 0);
            pstmt.setInt(13, data.kept[3] ? 1 : 0);
            pstmt.setInt(14, data.kept[4] ? 1 : 0);

            pstmt.setInt(15, data.aces);
            pstmt.setInt(16, data.twos);
            pstmt.setInt(17, data.threes);
            pstmt.setInt(18, data.fours);
            pstmt.setInt(19, data.fives);
            pstmt.setInt(20, data.sixes);
            pstmt.setInt(21, data.bonus);
            pstmt.setInt(22, data.totalOfUpperSection);
            
            pstmt.setInt(23, data.threeOfAKind);
            pstmt.setInt(24, data.fourOfAKind);
            pstmt.setInt(25, data.fullHouse);
            pstmt.setInt(26, data.smallStraight);
            pstmt.setInt(27, data.largeStraight);
            pstmt.setInt(28, data.chance);
            pstmt.setInt(29, data.yahtzee);
            pstmt.setInt(30, data.yahtzeeBonus);
            pstmt.setInt(31, data.totalOfLowerSection);

            pstmt.setInt(32, data.totalScore);

            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
