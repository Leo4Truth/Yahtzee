package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SaveServer extends JFrame implements Runnable {

	private Thread thread = null;

	private JTextArea wordsBox;
	
	public SaveServer() {
		createMainPanel();
		wordsBox.append("Ready to Accept Connections\n");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,400);
		setVisible(true);
		start();
	}
	
	public void createMainPanel() {
		wordsBox = new JTextArea(35,10);

		JScrollPane listScroller = new JScrollPane(wordsBox);
		this.add(listScroller, BorderLayout.CENTER);
		listScroller.setPreferredSize(new Dimension(250, 80));
	}

	public void run() {
		try {
			ServerSocket ss_comm = new ServerSocket(8888);
			ServerSocket ss_data = new ServerSocket(8889);

			System.out.println("server is running...");
			
			int cnt = 0;
			for (;;) {
				Socket sock_comm = ss_comm.accept();
				System.out.println("comm connected from " + sock_comm.getRemoteSocketAddress());
				Socket sock_data = ss_data.accept();
				System.out.println("data connected from " + sock_data.getRemoteSocketAddress());
				Thread t = new ServerThread(sock_comm, sock_data);
				t.start();

				cnt++;
                InetAddress comm_address = sock_comm.getInetAddress();
                System.out.println("new comm connection from: " + comm_address.getHostAddress());
                InetAddress data_address = sock_data.getInetAddress();
                System.out.println("new data connection from: " + data_address.getHostAddress());
				System.out.println("connected clients number: " + cnt);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void start () {
		if (thread == null) {
			thread = new Thread (this, "Save Server main");
			thread.start ();
		}
	}
	
	public static void main(String[] main) {
		SaveServer saveServer = new SaveServer();
	}
}
