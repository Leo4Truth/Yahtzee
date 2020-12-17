package game;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class LoadingFrame extends JFrame {

    private DefaultTableModel model;

    private JTable table;
    private JButton loadBtn;
    private JButton cancelBtn;

    public LoadingFrame(ArrayList<YahtzeeArchiveInfo> infos) {

        setTitle("Loading");

        String[] columnNames = { "ID", "Saving Time", "Name", "Round Left" };
        ArrayList<Object[]> data = new ArrayList<Object[]>();
        for (YahtzeeArchiveInfo info : infos) {
            data.add(info.getInfo());
        }

        model = new DefaultTableModel(new String[][] {}, columnNames);
        table = new JTable(model);
        table.setBounds(30, 40, 200, 400);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (Object[] row : data) {
            model.addRow(row);
        }

        loadBtn = new JButton("Load");
        cancelBtn = new JButton("Cancel");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension size = new Dimension(300, 400);
        setPreferredSize(size);
        setSize(size);
        setVisible(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        JScrollPane sp = new JScrollPane(table);
        add(sp);

        JPanel btnPnl = new JPanel();
        btnPnl.setLayout(new BoxLayout(btnPnl, BoxLayout.LINE_AXIS));
        btnPnl.add(loadBtn);
        btnPnl.add(cancelBtn);
        add(btnPnl);

        cancelBtn.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                dispose();
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

    public JButton getLoadBtn() { return loadBtn; }
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }

    public static void main(String[] args) {
        LoadingFrame loadingFrame = new LoadingFrame(new ArrayList<YahtzeeArchiveInfo>());
    }
}
