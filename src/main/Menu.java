package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class Menu extends JPanel {
	FScale scaler;
	volatile int levelSel;
	volatile boolean playLevel;
	private Dimension size;
	protected DrawableRect[] menuObjs = new DrawableRect[9];
	protected volatile int mouseX,mouseY;
	public Menu(Container cont) {
		size = new Dimension(1600,900);
		scaler = new FScale(cont, this,size.width,size.height);
		setBackground(Color.green);
		{
			int x = 850, y = 550, vs = 170, hs = 200;
			menuObjs[0] = new MenuLevel(x,y, 1, false);
			menuObjs[1] = new MenuLevel(x+hs,y, 2, false);
			menuObjs[2] = new MenuLevel(x+2*hs,y, 3, false);
			menuObjs[3] = new MenuLevel(x,y+vs, 4, true);
			menuObjs[4] = new MenuLevel(x+hs,y+vs, 5, true);
			menuObjs[5] = new MenuLevel(x+hs+hs,y+vs, 6, true);
		}{
			int x = 72,y = 410,vs = 160, h = 63;
			menuObjs[6] = new MenuText("PLAY",x,y,210,h);
			menuObjs[7] = new MenuText("EXIT",x,y+vs,180,h);
			menuObjs[8] = new MenuText("INSTRUCTIONS", x,y+2*vs,600,h);
		}
		MouseEvents mouse = new MouseEvents();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = scaler.scale(g);
		g2d.setColor(Pallete.greyBrown);
		g2d.fillRect(0,0,size.width,size.height);
		for(DrawableRect l:menuObjs)l.draw(g2d);
		g2d.setColor(Color.white);
//		g2d.drawString(String.format("(%d, %d) lsel: %d",mouseX, mouseY, levelSel),20,80);
		
	}
	private class MouseEvents extends MouseAdapter{
		@Override
		public void mouseMoved(MouseEvent e) {
			mouseX = (int)(e.getX()/scaler.scale);
			mouseY = (int)(e.getY()/scaler.scale);
			for(DrawableRect o:menuObjs) {
				if(o.contains(mouseX, mouseY))o.hover = true;
				else o.hover = false;
			}
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			mouseX = (int)(e.getX()/scaler.scale);
			mouseY = (int)(e.getY()/scaler.scale);
			for(DrawableRect l:menuObjs) {
				if(l.contains(mouseX,mouseY)) {
					l.selected = true;
					if(l instanceof MenuLevel)
						levelSel = ((MenuLevel)l).num;
					else {
						switch(((MenuText)l).string){
						case "PLAY":
							playLevel = true;
							break;
						case "EXIT":
							System.exit(0);
						case "INSTRUCTIONS":
							break;
						}
					}
				}
				else l.selected = false;
			}
		}
	}
	public int waitForSelection() {
		while(!playLevel) {
			repaint();
		}
		return levelSel;
	}
}
