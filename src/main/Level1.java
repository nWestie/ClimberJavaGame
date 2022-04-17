package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Level1 extends Level{

	public Level1(Container cont) {
		super(cont);
	}
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = scaler.scale(g);
		g2d.setColor(Color.red);
		g2d.fillRect(0, 0, 1600, 900);
	}
}
