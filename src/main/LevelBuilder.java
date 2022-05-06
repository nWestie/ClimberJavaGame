package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import objs.Block;
import objs.Player;

public class LevelBuilder extends Level {
	Block[][] board = new Block[50][100];
	Block[] blocks;
	protected Point cursor = new Point(0,0);
	public LevelBuilder(Container cont) {
		super(cont, true);
		MouseAdapter m = new LvlMouseEvents();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = scaler.scale(g);
		g2d.setColor(Pallete.greyBrown);
		Rectangle bounds = scaler.drawSize();
		int w = 151, h = 111;	
		Rectangle wideBounds  = new Rectangle(0,0,0,0);
		wideBounds.x = bounds.x/w;
		wideBounds.y = bounds.y/h;
		wideBounds.width = bounds.width+bounds.x;
		
		for(int i = bounds.x; i < scaler.drawSize().getWidth(); i += w*2) {
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
	private class LvlMouseEvents extends MouseAdapter{
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
			
		}
		
	}
}
