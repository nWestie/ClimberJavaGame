package main;

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
import java.awt.geom.Line2D;
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
	private boolean dieFlag;

	public Level(Container cont, int lvlNum, boolean noListen) {
		scaler = new FScale(cont, this, 1920, 1080);
//        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
//        		new ImageIcon("my-cursor.png").getImage(),new Point(0,0),"My cursor"));
		if (!noListen) {
			addKeyListener(new Level.KeyEvents());
			setFocusable(true);
			MouseAdapter mouse = new MouseEvents();
			addMouseListener(mouse);
			addMouseMotionListener(mouse);
		}
		int[] startX = { 0, 3 };
		int[] startY = { 0, 18 };
		plr = new Player(startX[lvlNum] * blockW, startY[lvlNum] * blockH - 19);
		blocks = Block.getBlockList();
		lvlEnvFile = new File(ClimberMain.dir, String.format("/Lvls/Lvl%d.clvl", lvlNum));
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


	public void play() {
		requestFocusInWindow();
		long nextLoopTime;
		while (true) {
			nextLoopTime = System.currentTimeMillis() + 1000 / ClimberMain.fRate;

			if (plr.getX() - xScroll > 1420)
				xScroll = (int) (plr.getX() - 1420);
			else if (plr.getX() - xScroll < 300)
				xScroll = (int) (plr.getX() - 300);
			if (plr.getY() - yScroll > 880)
				yScroll = (int) (plr.getY() - 880);
			else if (plr.getY() - yScroll < 600)
				yScroll = (int) (plr.getY() - 600);
			plr.updateRequestVelocity();
			solvePhysics();
			if (plr.getY() > board.length * blockH + 100) {
				dieFlag = true;
				while (dieFlag)
					repaint();
				return;
			}
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
				break;
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
		// Update player rope
		Point ptr = plr.getPointer();
		if (ptr == null)
			return;
		Line2D.Float rope = plr.getRope();
		rope.x1 = (float) plr.getX();
		rope.y1 = (float) (plr.getY() - 56);
		if (plr.isrLatched())
			return;
		double xWeight, yWeight;
		xWeight = ptr.x - plr.getX();
		yWeight = ptr.y - plr.getY() + 56;
		double mag = Math.sqrt(Math.pow(xWeight, 2) + Math.pow(yWeight, 2));
		xWeight /= mag;
		yWeight /= mag;
		double ropeLen = plr.getRopeLen()*1.1 + 30;
		rope.x2 = (int) (rope.x1 + ropeLen * xWeight);
		rope.y2 = (int) (rope.y1 + ropeLen * yWeight);
		while (true) {
			int xBlock = (int) rope.x2 / Block.width;
			int yBlock = (int) rope.y2 / Block.height;
			Line2D.Float tmp = new Line2D.Float(rope.x1 - xBlock * Block.width, rope.y1 - yBlock * Block.height,
					rope.x2 - xBlock * Block.width, rope.y2 - yBlock * Block.height);
			boolean intersect = false;
			try {
				for (Line2D.Float blockL : blocks[board[yBlock][xBlock]].getBounds()) {
					if (tmp.intersectsLine(blockL)) {
						intersect = true;
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("Rope Bounds err");
			}
			if (!intersect)
				break;
			plr.setrLatched(true);
			rope.x2 -= xWeight * 4;
			rope.y2 -= yWeight * 4;
		}
		plr.setRope(rope);
		plr.setRopeLen(ropeLen);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = scaler.scale(g);
		// clamp scrolling to board size
		Rectangle bounds = scaler.drawSize();
		xScroll = Math.max(12, Math.min(xScroll, (board[0].length) * blockW - bounds.width - 1));
		yScroll = Math.max(0, Math.min(yScroll, (board.length) * blockH - bounds.height - 1));
		g2d.translate(-xScroll, -yScroll);

		// draw background tiles
		Rectangle bBounds = new Rectangle(0, 0, 0, 0); // bounds in units of BG tiles
		bBounds.x = (xScroll) / blockW;
		bBounds.y = (yScroll) / blockH;
		bBounds.width = (xScroll + bounds.width) / blockW + 1;
		bBounds.height = (yScroll + bounds.height) / blockH + 1;
		for (int i = bBounds.y; i < bBounds.height; i++) {
			for (int j = bBounds.x; j < bBounds.width; j++) {
				g2d.drawImage(blocks[board[i][j]].getImg(), null, j * blockW, i * blockH);
			}
		}
		// draw player
		plr.draw(g2d, cursor);
		Line2D.Float rope = plr.getRope();
		int xBlock = (int) rope.x2 / Block.width;
		int yBlock = (int) rope.y2 / Block.height;
		for (Line2D.Float blockL : blocks[board[yBlock][yBlock]].getBounds()) {
			g2d.draw(blockL);
		}
		g2d.translate(xScroll, yScroll);
		if (dieFlag) {
			g2d.setFont(Pallete.menuFont);
			g2d.drawString("YOU DIED", 700, 600);
			g2d.setFont(Pallete.menuFontSmall);
			g2d.drawString("Click to Continue", 680, 650);
		}

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
		public void mousePressed(MouseEvent e) {
			plr.ropeTo(cursor);
			dieFlag = false;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			plr.releaseRope();
		}

	}
}
