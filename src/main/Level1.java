package main;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

public class Level1 extends Level {

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
		super.play();
		long nextLoopTime;
		while(true) {
			nextLoopTime = System.currentTimeMillis()+1000/ClimberMain.fRate;
			plr.updatePhysics();
			repaint();
			while(System.currentTimeMillis()<nextLoopTime);
		}
	}
}
