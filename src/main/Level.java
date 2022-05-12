package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.ImageIcon;
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
	protected int blockW = 151, blockH = 111;
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
//		System.out.println("solve physics");
		// get player data
		Area plrBounds = plr.getBounds();
		int plrBX = plr.getX() / blockW;
		int plrBY = plr.getY() / blockH;
		double storePlrXVel = plr.getxVel();
		double storePlrYVel = plr.getyVel();
		// Allocate once
		AffineTransform tmpTrans = new AffineTransform();
		Area tmpArea;
		Rectangle tmpCol;
		boolean writeFlag = false;
		plr.setY(plr.getY()+(int)storePlrYVel);
		// iterate through adjacent blocks
		for (int j = plrBX - 1; j <= plrBX + 1; j++) {
			if (j < 0 || j >= board[0].length)
				continue;
			for (int i = plrBY - 1; i <= plrBY + 1; i++) {
				if (i < 0 || i >= board.length)
					continue;
				// get overlapping region
				tmpTrans.setToTranslation(j * blockW, i * blockH);
				tmpArea = blocks[board[i][j]].getBounds();
				tmpArea = tmpArea.createTransformedArea(tmpTrans);
				tmpArea.intersect(plrBounds);
				tmpCol = tmpArea.getBounds();
				if (tmpArea.isEmpty()||tmpCol.width<2)
					continue;
				// Handle collisions
				plr.forceMove(0,(tmpCol.height-1) * (storePlrYVel > 0 ? -1 : 1));
				plr.setyVel(0);//(storePlrYVel > 0 ? -3 : 3));
				// recalculate player state
				plrBounds = plr.getBounds();
			}
		}
		repaint();
		plr.setX(plr.getX()+(int)storePlrXVel);
		// iterate through adjacent blocks
		for (int j = plrBX - 1; j <= plrBX + 1; j++) {
			if (j < 0 || j >= board[0].length)
				continue;
			for (int i = plrBY - 1; i <= plrBY + 1; i++) {
				if (i < 0 || i >= board.length)
					continue;
				// get overlapping region
				tmpTrans.setToTranslation(j * blockW, i * blockH);
				tmpArea = blocks[board[i][j]].getBounds();
				tmpArea = tmpArea.createTransformedArea(tmpTrans);
				tmpArea.intersect(plrBounds);
				tmpCol = tmpArea.getBounds();
				if (tmpArea.isEmpty()||tmpCol.height<2)
					continue;
				// Handle collisions
//				plr.forceMove((tmpCol.width-1) * (storePlrXVel > 0 ? -1 : 1), 0);
//				plr.setxVel(0);
//				// recalculate player state
//				plrBounds = plr.getBounds();
			}
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
		g2d.setColor(Color.red);
		Rectangle bBounds = new Rectangle(0, 0, 0, 0); // bounds in units of BG tiles
		bBounds.x = (xScroll + bounds.x) / blockW;
		bBounds.y = (yScroll + bounds.y) / blockH;
		bBounds.width = (xScroll + bounds.width + bounds.x) / blockW + 1;
		bBounds.height = (yScroll + bounds.height + bounds.y) / blockH + 1;
		for (int i = bBounds.y; i < bBounds.height; i++) {
			for (int j = bBounds.x; j < bBounds.width; j++) {
				g2d.drawImage(blocks[board[i][j]].getImg(), null, j * blockW, i * blockH);
				g2d.drawString(String.format("%d,%d", i, j), j*blockW+10, i*blockH+20);
			}
		}
		plr.draw(g2d);

//		g2d.setColor(Color.red);
//		int plrBX = plr.getX() / blockW;
//		int plrBY = plr.getY() / blockH;
//		AffineTransform tmpTrans = new AffineTransform();
//		Area tmpArea;
//		Area plrBounds = plr.getBounds();
//		for (int j = plrBX - 1; j <= plrBX + 1; j++) {
//			if (j < 0 || j >= board[0].length)
//				continue;
//			for (int i = plrBY - 1; i <= plrBY + 1; i++) {
//				if (i < 0 || i >= board.length)
//					continue;
//				tmpArea = blocks[board[i][j]].getBounds();
//				tmpTrans = new AffineTransform();
//				tmpTrans.setToTranslation(j * blockW, i * blockH);
//				tmpArea = tmpArea.createTransformedArea(tmpTrans);
//				g2d.draw(tmpArea);
//				tmpArea.intersect(plrBounds);
//				g2d.fill(tmpArea.getBounds());
//			}
//		}

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
			plr.setyVel(d - u);
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
			plr.setyVel(d - u);
		}
	}

	private class MouseEvents extends MouseAdapter {

		@Override
		public void mouseMoved(MouseEvent e) {
			double s = scaler.getScale();
			cursor.x = (int) (xScroll + (e.getX() / s)) / blockW;
			cursor.y = (int) (yScroll + (e.getY() / s)) / blockH;
		}

	}
}
