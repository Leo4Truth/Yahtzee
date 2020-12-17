package game;

import java.sql.Timestamp;

public class YahtzeeArchiveInfo implements java.io.Serializable {
    private int id;
    private String name;
    private Timestamp timestamp;
    private int roundLeft;

    public YahtzeeArchiveInfo() {
        this.id = 0;
        this.name = "player";
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.roundLeft = 13;
    }

    public YahtzeeArchiveInfo(int id, Timestamp timestamp, String name, int roundLeft) {
        this.id = id;
        this.timestamp = timestamp;
        this.name = name;
        this.roundLeft = roundLeft;
    }

    public YahtzeeArchiveInfo(int id, String timestampStr, String name, int roundLeft) {
        this.id = id;
        this.timestamp = Timestamp.valueOf(timestampStr);
        this.name = name;
        this.roundLeft = roundLeft;
    }

    public Object[] getInfo() {
        Object[] info = new Object[4];
        info[0] = id;
        info[1] = timestamp;
        info[2] = name;
        info[3] = roundLeft;
        return info;
    }
}