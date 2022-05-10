package main;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JPanel;

import objs.Block;
import objs.Player;

public class Level extends JPanel {
	private static final long serialVersionUID = 1L;
	protected FScale scaler;
	protected File lvlEnvFile;
	protected Player plr;
	protected int[][] board = new int[20][40];
	protected static Block[] blocks;

	public Level(Container cont, boolean noListen) {
		scaler = new FScale(cont, this, 1920, 1080);
		if (!noListen) {
			addKeyListener(new Level.KeyEvents());
			setFocusable(true);
			MouseAdapter mouse = new MouseEvents();
			addMouseListener(mouse);
			addMouseMotionListener(mouse);
		}
		plr = new Player(300, 400);
		blocks = Block.getBlockList();
		lvlEnvFile = new File(ClimberMain.dir, "/Lvls/LvlTest.clvl");
//		try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(lvlEnvFile));) {
//			Object tmpRead = inStream.readObject();
//			if (!(tmpRead instanceof int[][])) {
//				throw new IOException("Invalid level file");
//			}
//			board = (int[][]) tmpRead;
//			System.out.println("Board loaded");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public Level(Container cont) {
		this(cont, false);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	public void play() {
		requestFocusInWindow();
	}

	protected class KeyEvents extends KeyAdapter {
		private int l = 0, r = 0;

		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				r = 1;
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:

				l = 1;
				break;
			case KeyEvent.VK_UP:
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
			}
			plr.inpDir = r - l;
		}

		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				r = 0;
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				l = 0;
				break;
			default:
				break;
			}
			plr.inpDir = r - l;
		}
	}

	private class MouseEvents extends MouseAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {

		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}
	}
}
