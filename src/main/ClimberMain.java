package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;

import javax.swing.JFrame;

public class ClimberMain extends JFrame {
	private static final long serialVersionUID = 1L;
	protected static final String dir = System.getProperty("user.dir");
	protected static final int fRate = 30;
	public ClimberMain(String s) {
		super(s);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
		while (true) {
//			Menu menu = new Menu(frame);
//			cont.add(menu);
//			int levelNum = menu.waitForSelection();
//			cont.remove(menu);
			Level l1 = new Level1(frame);
			cont.add(l1);
			frame.repaint();
			l1.play();
		}
	}

}