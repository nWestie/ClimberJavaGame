package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Line2D;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import objs.Block;

public class LevelBuilder extends Level {
	private static final long serialVersionUID = 1L;
	protected WriteEnv envWriter;
	protected Point cursor;
	protected int x, y;
	protected int spd;
	protected int w, h;

	public LevelBuilder(Container cont, int lvl) {
		super(cont, lvl, false);
		x = 0;
		y = 0;
		spd = 30;
		w = Block.width;
		h = Block.height;
		cursor = new Point(0, 0);
		MouseAdapter m = new LvlMouseEvents();
		addMouseListener(m);
		addMouseMotionListener(m);
		addMouseWheelListener(m);
		addKeyListener(new LvlKeyEvents());
		setFocusable(true);
		envWriter = new WriteEnv();
		envWriter.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g, true);
		Rectangle bounds = scaler.drawSize();

		// clamp scrolling to board size
		x = Math.max(0, Math.min(x, (board[0].length) * w - bounds.width - 1));
		y = Math.max(0, Math.min(y, (board.length) * h - bounds.height - 1));

		Graphics2D g2d = scaler.scale(g);
		g2d.translate(-x, -y);
		g2d.setColor(Color.red);

		// draw background tiles
		Rectangle bBounds = new Rectangle(0, 0, 0, 0); // bounds in units of BG tiles
		bBounds.x = (x + bounds.x) / w;
		bBounds.y = (y + bounds.y) / h;
		bBounds.width = (x + bounds.width + bounds.x) / w + 1;
		bBounds.height = (y + bounds.height + bounds.y) / h + 1;
		try {
			for (int i = bBounds.y; i < bBounds.height; i++) {
				for (int j = bBounds.x; j < bBounds.width; j++) {
					g2d.drawImage(blocks[board[i][j]].getImg(), null, j * w, i * h);
				}

			}
		} catch (Exception e) {
			System.out.println("BG Bounds err");
		}
		g2d.translate(cursor.x * w, cursor.y * h);
		final int len = 10;
		Line2D.Float[] lines = blocks[board[cursor.y][cursor.x]].getBounds();
		float[][] vecs = blocks[board[cursor.y][cursor.x]].getVecs();
		g2d.drawString(String.valueOf(board[cursor.y][cursor.x]), 10, 30);
		for (int i = 0; i < lines.length; i++) {
			g2d.setColor(Color.red);
			g2d.draw(lines[i]);
			g2d.setColor(Color.green);
			float midX = (lines[i].x1 + lines[i].x2) / 2;
			float midY = (lines[i].y1 + lines[i].y2) / 2;
			g2d.drawLine((int) midX, (int) midY, (int) (midX + len * vecs[i][0]), (int) (midY - len * vecs[i][1]));
		}
		g2d.translate(-cursor.x * w, -cursor.y * h);
		g2d.translate(x, y);
		g2d.drawString(String.format("%d, %d", cursor.x, cursor.y), 20, 20);
	}

	public int play() {
		requestFocusInWindow();
		long nextLoopTime;
		while (true) {
			nextLoopTime = System.currentTimeMillis() + 1000 / ClimberMain.fRate;
			plr.updateRequestVelocity();
			repaint();
			while (System.currentTimeMillis() < nextLoopTime)
				;
		}
	}

	protected class LvlKeyEvents extends KeyAdapter {
		private int l = 0, r = 0, u = 0, d = 0;

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
			case KeyEvent.VK_F:
				envWriter.update();
				break;
			case KeyEvent.VK_ESCAPE:
				envWriter.update();
				envWriter.reqExit();
				try {
					envWriter.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
			x += spd * (r - l);
			y += spd * (d - u);
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
		}
	}

	private class LvlMouseEvents extends MouseAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			double s = scaler.getScale();
			cursor.x = (int) (x + (e.getX() / s)) / w;
			cursor.y = (int) (y + (e.getY() / s)) / h;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			envWriter.update();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int i = cursor.x, j = cursor.y;
			board[j][i] = (board[j][i] + e.getWheelRotation() + blocks.length) % blocks.length;
		}

	}

	private class WriteEnv extends Thread {
		private volatile boolean update;
		private volatile boolean exitFlag;

		@Override
		public void run() {
//			System.out.println("Started envWirte");
			while (!exitFlag) {
				while (!update)
					;
				update = false;
				try {
					ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(lvlEnvFile));
					outStream.writeObject(board);
					outStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Saved");
			}
		}

		public void update() {
			update = true;
		}

		public void reqExit() {
			exitFlag = true;
		}
	}
}
