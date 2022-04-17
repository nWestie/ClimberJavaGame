package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class DrawableRect extends Rectangle {
	protected boolean hover = false;
	protected boolean selected = false;
	public DrawableRect(int x, int y, int w, int h) {
		super(x,y,w,h);
	}
	abstract void draw(Graphics2D g2d);
}
