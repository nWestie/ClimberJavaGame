package main;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class Level extends JPanel {
	private static final long serialVersionUID = 1L;
	protected FScale scaler;
	protected Player plr;

	public Level(Container cont) {
		scaler = new FScale(cont, this, 1920, 1080);
		addKeyListener(new KeyEvents());
		setFocusable(true);
		MouseEvents mouse = new MouseEvents();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = scaler.scale(g);
	}

	public void play() {
		requestFocusInWindow();
	}

	private class KeyEvents extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				plr.inpDir = 1;
				break;
			case KeyEvent.VK_LEFT:
				plr.inpDir = -1;
				break;
			case KeyEvent.VK_UP:
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
			}
		}

		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_LEFT:
				plr.inpDir = 0;
			default:
				break;
			}
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
