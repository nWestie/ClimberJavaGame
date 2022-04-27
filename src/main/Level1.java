package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Level1 extends Level {
	protected Player plr;

	public Level1(Container cont) {
		super(cont);
		plr = new Player(300,400);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = scaler.scale(g);
		g2d.setColor(Pallete.greyBrown);
		g2d.fill(scaler.drawSize());
		plr.draw(g2d);
	}
	public void play() {
		long nextLoopTime;
		while(true) {
			nextLoopTime = System.currentTimeMillis()+1000/24;
			plr.updatePhysics();
			repaint();
			while(System.currentTimeMillis()<nextLoopTime);
		}
	}
}
