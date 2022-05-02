package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;

import objs.Block;
import objs.Player;

public class Level1 extends Level {
	Block[][] board = new Block[50][100];
	public Level1(Container cont) {
		super(cont);
		plr = new Player(300,400);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = scaler.scale(g);
		g2d.fill(scaler.drawSize());
		int w = 150, h = 110;
		
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
