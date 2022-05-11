package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
	protected int blockW = 151, blockH = 111;
	protected int xScroll, yScroll = 10 * blockH;

	public Level(Container cont, String lvlFile, int plrX, int plrY, boolean noListen) {
		scaler = new FScale(cont, this, 1920, 1080);
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
		

//		Area tmp = blocks[board[cursor.y][cursor.x]].getBounds();
//		AffineTransform tempTrans = new AffineTransform();
//		tempTrans.setToTranslation(cursor.x * w, cursor.y * h);
//		tmp = tmp.createTransformedArea(tempTrans);
//		g2d.draw(tmp);

		plr.draw(g2d);
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

	public void play() {
		requestFocusInWindow();
		long nextLoopTime;
		while (true) {
			nextLoopTime = System.currentTimeMillis() + 1000 / ClimberMain.fRate;
			plr.updatePhysics();
			repaint();
			while (System.currentTimeMillis() < nextLoopTime)
				;
		}
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
			plr.setyVel(d-u);
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
			plr.setyVel(d-u);
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
