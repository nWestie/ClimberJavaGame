package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
		int w = 150, h = 110;
		for(int i = 0; i < scaler.drawSize().getWidth(); i += w*2) {
			for(int j = 0; j < scaler.drawSize().getHeight(); j += h*2) {
				g2d.setColor(Color.blue);
				g2d.fillRect(i,j,w,h);
				g2d.fillRect(i+w,j+h,w,h);
				g2d.setColor(Color.red);
				g2d.fillRect(i+w,j+0,w,h);
				g2d.fillRect(i,j+h,w,h);
			}
			
		}
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
