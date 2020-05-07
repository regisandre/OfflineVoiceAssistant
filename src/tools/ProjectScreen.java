package be.sioxox.tools;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import be.sioxox.utils.Screen;

public class ProjectScreen {

	Robot robot;
	Screen utils;
	JLabel label;
	GeneralPath gp;
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

	public ProjectScreen() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		label = new JLabel();

		drawScreenCapture();

		ActionListener al = new ActionListener() {
			Point lastPoint;

			public void actionPerformed(ActionEvent e) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				if (!p.equals(lastPoint)) {
					drawScreenCapture();
				}
			}
		};
		Timer timer = new Timer(1, al);
		timer.start();
	}

	public void drawScreenCapture() {
		BufferedImage biOrig = robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
		BufferedImage small = new BufferedImage(biOrig.getWidth() / 2, biOrig.getHeight() / 2,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = small.createGraphics();

		g.scale(.50, .50);
		g.drawImage(biOrig, 0, 0, label);
		g.dispose();

		label.setIcon(new ImageIcon(small));
	}

	public void projectScreen() throws Exception {
		Runnable r = new Runnable() {
			public void run() {
				JPanel panel = new JPanel(new BorderLayout(0, 0));
				panel.add(label);

				JFrame f = new JFrame(
						"Project Screen " + MouseInfo.getPointerInfo().getDevice().getIDstring().substring(8));
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setContentPane(panel);
				f.pack();
				f.setVisible(true);
			}
		};
		SwingUtilities.invokeLater(r);
	}
}
