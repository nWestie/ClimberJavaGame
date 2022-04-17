package main;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Level extends JPanel {
	private static final long serialVersionUID = 1L;
	protected FScale scaler;
	public Level(Container cont) {
		scaler = new FScale(cont, this, 1600, 900);
	}
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = scaler.scale(g);
	}
}
