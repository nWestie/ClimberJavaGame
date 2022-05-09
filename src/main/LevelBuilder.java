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

import objs.Block;
import objs.Player;

public class LevelBuilder extends Level {
//	Block[][] board = new Block[50][100];
//	Block[] blocks;
	protected Point cursor = new Point(0, 0);
	protected int x = 0, y = 0;
	protected int spd = 30;
	protected int w = 151, h = 111;

	public LevelBuilder(Container cont) {
		super(cont, true);
		MouseAdapter m = new LvlMouseEvents();
		addMouseListener(m);
		addMouseMotionListener(m);
		addKeyListener(new LvlKeyEvents());
		setFocusable(true);
		Block[] b = blocks;
	}

	@Override
	public void paintComponent(Graphics g) {
//		x = Math.max(0,Math.min(x, (board[0].length-14)*w));
//		y = Math.max(0,Math.min(y, (board[0].length-11)*h));
		
		Graphics2D g2d = scaler.scale(g);
		g2d.translate(-x,-y);
		g2d.setColor(Pallete.greyBrown);
		Rectangle bounds = scaler.drawSize();
		Rectangle bBounds = new Rectangle(0, 0, 0, 0); // bounds in units of BG tiles
		bBounds.x = (x+bounds.x) / w;
		bBounds.y = (y+bounds.y) / h;
		bBounds.width = (x+bounds.width + bounds.x) / w + 1;
		bBounds.height = (y+bounds.height + bounds.y) / h + 1;
		try {
			for (int i = bBounds.y; i < bBounds.height; i++) {
				for (int j = bBounds.x; j < bBounds.width; j++) {
					g2d.drawImage(blocks[board[i][j]].getImg(), null, j * w, i * h);
				}

			} 
		} catch (Exception e) {
			System.out.println("BG Bounds err");
		}
		g2d.translate(x,y);
		plr.draw(g2d);
		g2d.drawString(String.format("%d, %d", bBounds.width,bBounds.height), 20, 20);
	}

	public void play() {
		super.play();
		long nextLoopTime;
		while (true) {
			nextLoopTime = System.currentTimeMillis() + 1000 / ClimberMain.fRate;
			plr.updatePhysics();
			repaint();
			while (System.currentTimeMillis() < nextLoopTime);
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
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
			}
			x += spd*(r-l);
			y += spd*(d-u);
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
			cursor.x = e.getX();
			cursor.y = e.getY();
		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int i,j;
			i = (x+cursor.x)/w;
			j = (y+cursor.y)/h;
			board[j][i] = (board[j][i]+e.getWheelRotation());
		}

	}
}
