package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Frame;

import javax.swing.JFrame;

public class ClimberMain extends JFrame {
	private static final long serialVersionUID = 1L;
	private static int BUILDER = 0;
	public static final String dir = System.getProperty("user.dir");
	protected static final int fRate = 30;
//	protected static boolean exitFlag = false;

	public ClimberMain(String s) {
		super(s);

	}

	public static void main(String[] args) {
		ClimberMain frame = new ClimberMain("Climber");
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
//		frame.setBounds(10,10,1200,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		Container cont = frame.getContentPane();
		frame.setUndecorated(true);
		cont.setBackground(Color.black);
		cont.setLayout(null);
		frame.setVisible(true);
		frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

		Menu menu = new Menu(frame);
		int levelNum = 0;
		while (true) {
			Level lvl;
			if (BUILDER != 0) {
				lvl = new LevelBuilder(frame, BUILDER);
			} else {
				cont.add(menu);
				levelNum = menu.waitForSelection();
				cont.remove(menu);
				lvl = new Level(frame, levelNum, false);
			}
			cont.add(lvl);
			frame.repaint();
			int win = lvl.play();
			if(win == 2) {
				menu.setWon(levelNum);
			}
			cont.remove(lvl);
		}
	}

}
