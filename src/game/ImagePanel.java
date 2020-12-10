package game;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private Image img;
	private JLabel l;
	private Image[] diceImages = new Image[6];
	private List<Integer> imageSequence;
	
	public ImagePanel() {

		for (int i=0;i<6;i++) {
			diceImages[i] = new ImageIcon("die"+(i+1) +".png").getImage();
		}
		imageSequence = new ArrayList<Integer>();
		//setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
	public ImagePanel(String img) {		
		this(new ImageIcon(img).getImage());
	}
	
	public ImagePanel(Image img) {
		this();
		this.img = img;
		l = new JLabel(new ImageIcon(this.img));
		add(l);
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        System.out.println("initialization: setting size to " + size.toString());
        setPreferredSize(size);
	}
	
	public void setImage(Image img) {
		this.img = img;
    	System.out.println("repainting");
		repaint();
	}
	
	public void setImage(String img) {
		this.img = new ImageIcon(img).getImage();
		repaint();
	}
	
	public void setSequence(List<Integer> al) {
		imageSequence = al;
		repaint();
	}
	
	public void scaleImage(double factor) {
		ImageIcon imageIcon = new ImageIcon(img);
		int height = imageIcon.getIconHeight();
		int width = imageIcon.getIconWidth();
		int newHeight = (int) (height*factor);
		int newWidth = (int) (width*factor);
		System.out.println("scaleImage: new size is  " + newWidth + ", "+ newHeight);
		Image resultingImage = img.getScaledInstance(newWidth, newHeight, 
												Image.SCALE_DEFAULT);
		imageIcon = new ImageIcon(resultingImage);
		l.setIcon(imageIcon);
        Dimension size = new Dimension(imageIcon.getIconWidth(), 
        								imageIcon.getIconHeight());

        System.out.println("scaleImage: setting size to " + size.toString());
		this.img = resultingImage;
        setPreferredSize(size);
		repaint();
	}
	
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	l.repaint();
    }
}
