package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
	protected int[][] board;
	protected static Block[] blocks;
	protected int blockW = Block.width, blockH = Block.height;
	protected int xScroll, yScroll = 10 * blockH;
	private Point cursor = new Point();

	public Level(Container cont, String lvlFile, int plrX, int plrY, boolean noListen) {
		scaler = new FScale(cont, this, 1920, 1080);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
//        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
//        		new ImageIcon("my-cursor.png").getImage(),new Point(0,0),"My cursor"));
		if (!noListen) {
			addKeyListener(new Level.KeyEvents());
			setFocusable(true);
			MouseAdapter mouse = new MouseEvents();
			addMouseListener(mouse);
			addMouseMotionListener(mouse);
		}
		plr = new Player(plrX * blockW, plrY * blockH - 19);
		blocks = Block.getBlockList();
		lvlEnvFile = new File(ClimberMain.dir, lvlFile);
		try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(lvlEnvFile));) {
			Object tmpRead = inStream.readObject();
			if (!(tmpRead instanceof int[][])) {
				throw new IOException("Invalid level file");
			}
			board = (int[][]) tmpRead;
//			System.out.println("Board loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Level(Container cont, int lvlFile, int plrX, int plrY) {
		this(cont, String.format("/Lvls/Lvl%d.clvl", lvlFile), plrX, plrY, false);
	}

	public void play() {
		requestFocusInWindow();
		long nextLoopTime;
		while (true) {
			nextLoopTime = System.currentTimeMillis() + 1000 / ClimberMain.fRate;

			plr.updateRequestVelocity();
			solvePhysics();
			
			repaint();
			while (System.currentTimeMillis() < nextLoopTime)
				;
		}
	}

	private void solvePhysics() {
		int plrBX = (int) plr.getX() / blockW;
		int plrBY = (int) plr.getY() / blockH;
		while (true) {
			float[] sVecs = new float[2];
			plr.updateBounds();
			for (int j = plrBX - 1; j <= plrBX + 1; j++) {
				if (j < 0 || j >= board[0].length)
					continue;
				for (int i = plrBY - 1; i <= plrBY + 1; i++) {
					if (i < 0 || i >= board.length)
						continue;
					blocks[board[i][j]].addCollisionVecs(j, i, plr.getMvBoundPts(), sVecs);
				}
			}
			if (sVecs[0] == 0 && sVecs[1] == 0)
				return;
			double mag = Math.sqrt(Math.pow(sVecs[0], 2) + Math.pow(sVecs[1], 2));
			sVecs[0] /= mag;
			sVecs[1] /= mag;
//			System.out.println(Arrays.toString(sVecs));
			plr.forceMove(sVecs[0], -sVecs[1]);
			if (Math.abs(sVecs[0]) > .8)
				plr.setxVel(0);
			if (Math.abs(sVecs[1]) > .8)
				plr.setyVel(0);
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = scaler.scale(g);
		g2d.translate(-xScroll, -yScroll);

		Rectangle bounds = scaler.drawSize();

		// clamp scrolling to board size
		xScroll = Math.max(0, Math.min(xScroll, (board[0].length) * blockW - bounds.width - 1));
		yScroll = Math.max(0, Math.min(yScroll, (board.length) * blockH - bounds.height - 1));

		// draw background tiles
		Rectangle bBounds = new Rectangle(0, 0, 0, 0); // bounds in units of BG tiles
		bBounds.x = (xScroll + bounds.x) / blockW;
		bBounds.y = (yScroll + bounds.y) / blockH;
		bBounds.width = (xScroll + bounds.width + bounds.x) / blockW + 1;
		bBounds.height = (yScroll + bounds.height + bounds.y) / blockH + 1;
		for (int i = bBounds.y; i < bBounds.height; i++) {
			for (int j = bBounds.x; j < bBounds.width; j++) {
				g2d.drawImage(blocks[board[i][j]].getImg(), null, j * blockW, i * blockH);
			}
		}
		// draw player
		plr.draw(g2d, cursor);
		
	}

	/**
	 * allows calling only grandparent(jPanel) paintComponent
	 * 
	 * @param g
	 * @param pass
	 */
	public void paintComponent(Graphics g, boolean pass) {
		super.paintComponent(g);
	}

	protected class KeyEvents extends KeyAdapter {
		private int l = 0, r = 0;
		private int u = 0, d = 0;

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
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				d = 1;
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				u = 1;
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
			}
			plr.inpDir = r - l;
//			plr.setyVel(5 * (d - u));
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
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				d = 0;
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				u = 0;
				break;
			default:
				break;
			}
			plr.inpDir = r - l;
//			plr.setyVel(5 * (d - u));
		}
	}

	private class MouseEvents extends MouseAdapter {
		private double s = scaler.getScale();

		@Override
		public void mouseMoved(MouseEvent e) {
			cursor.x = (int) (xScroll + (e.getX() / s));
			cursor.y = (int) (yScroll + (e.getY() / s));
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			plr.ropeTo(cursor);
		}
		public void mouseReleased(MouseEvent e) {
			plr.release();
		}

	}
}
