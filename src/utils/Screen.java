package be.sioxox.utils;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;

public class Screen {
	public void getScreen() throws Exception {

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();

		System.out.println("Number of screen(s): " + gs.length);

		int test = Integer.parseInt(MouseInfo.getPointerInfo().getDevice().getIDstring().substring(8));

		System.out.println("Actual screen: " + test);

		for (int i = 0; i < gs.length; i++) {
			System.out.println("Screen dimension " + Integer.toString(i) + ": " + gs[i].getDisplayMode().getWidth()
					+ "*" + gs[i].getDisplayMode().getHeight());
		}
	}
	
	public String getActualScreen() {
		System.out.println(MouseInfo.getPointerInfo().getDevice().getIDstring().substring(8));
		return MouseInfo.getPointerInfo().getDevice().getIDstring().substring(8);
	}
}
