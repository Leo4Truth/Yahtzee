package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import game.YahtzeeArchiveInfo;
import game.YahtzeeData;

public class Client {

    private boolean connected = false;
    private Socket sock_comm;
    private Socket sock_data;

    private InputStream comm_in = null;
    private OutputStream comm_out = null;
    private InputStream data_in = null;
    private OutputStream data_out = null;

    BufferedReader reader = null;
    BufferedWriter writer = null;
    ObjectInputStream objIn = null;
    ObjectOutputStream objOut = null;

    public Client() {
        try {
            sock_comm = new Socket("127.0.0.1", 8888);
            sock_data = new Socket("127.0.0.1", 8889);

            connected = true;

            comm_in = sock_comm.getInputStream();
            comm_out = sock_comm.getOutputStream();
            data_in = sock_data.getInputStream();
            data_out = sock_data.getOutputStream();

            reader = new BufferedReader(new InputStreamReader(comm_in, StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(comm_out, StandardCharsets.UTF_8));

            // order!!!
            objOut = new ObjectOutputStream(data_out);
            objIn = new ObjectInputStream(data_in);

            System.out.println(comm_in.available());
            System.out.println(data_in.available());
        } catch (ConnectException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Connect to server failed");
            sock_comm = null;
            sock_data = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finalize() throws IOException {
        close();
    }

    public void save(YahtzeeData data) {

        try {
            send("save");

            String msg = reader.readLine();
            while (!msg.equals("send"))
                msg = reader.readLine();

            objOut.writeObject(data);
            objOut.flush();

            System.out.println("sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<YahtzeeArchiveInfo> query() {
        try {
            send("list");

            String nStr = reader.readLine();
            int n = Integer.parseInt(nStr);

            try {
                Object obj = objIn.readObject();
                if (obj != null) {
                    ArrayList<YahtzeeArchiveInfo> list = (ArrayList<YahtzeeArchiveInfo>) obj;
                    for (int i = 0; i < n; i++) {
                        System.out.println(list.get(i));
                    }
                    return list;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public YahtzeeData request(int id) {
        YahtzeeData data = null;
        try {
            writer.write("id");
            writer.newLine();
            writer.flush();

            writer.write(String.valueOf(id));
            writer.newLine();
            writer.flush();

            try {
                Object obj = objIn.readObject();
                if (obj != null) {
                    data = (YahtzeeData) obj;
                    return data;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new YahtzeeData();
    }

    public void send(String msg) {
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        send("disconnect");
        try {
            sock_comm.close();
            sock_data.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() { return connected; }
}
