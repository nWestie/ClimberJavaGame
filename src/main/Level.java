package main;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import objs.Block;
import objs.Player;

public class Level extends JPanel {
	private static final long serialVersionUID = 1L;
	protected FScale scaler;
	protected Player plr;
	Block[][] board = new Block[50][100];
	Block[] blocks;

	public Level(Container cont, boolean noMouse) {
		scaler = new FScale(cont, this, 1920, 1080);
		addKeyListener(new Level.KeyEvents());
		setFocusable(true);
		if(!noMouse) {
			MouseAdapter mouse = new MouseEvents();
			addMouseListener(mouse);
			addMouseMotionListener(mouse);		
		}
		plr = new Player(300,400);
		blocks = Block.getBlockList();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = blocks[(i*board.length+j)%blocks.length];
			}
			
		}
	}
	public Level(Container cont) {
		this(cont, false);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = scaler.scale(g);
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
