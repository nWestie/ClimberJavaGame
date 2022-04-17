package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JPanel;
/**
 * A utility to scale Java AWT Components based on display size<br>
 * Works with swing JFrames, JPanels and all other subclasses of Frame
 */
public class FScale {
	protected Rectangle bounds;
	protected double scale;
	
	public FScale(Container cont, JPanel obj, int w, int h) {
		Dimension size = cont.getSize();
		double scaleX = size.getWidth()/w;
		double scaleY = size.getHeight()/h;
		scale = Math.min(scaleX, scaleY);
		int actW = (int)(w*scale);
		int actH = (int)(h*scale);
		bounds = new Rectangle((int)(size.getWidth()-actW)/2,(int)(size.getHeight()-actH)/2, actW, actH);
		obj.setBounds(bounds);	
	}
	
	/**
	 * Scales a Graphics object according to the scale determined in initScale()<br>
	 * Designed to be used in the object's paintComponent, right after the super.paintComponent() is called.
	 * @param g Graphics object to scale
	 * @return Converted Graphics2D object
	 */
	public Graphics2D scale(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.scale(scale, scale);
		return g2d;
	}
	/**
	 * @return the size
	 */
	public Rectangle getSize() {
		return (Rectangle)bounds.clone();
	}
	/**
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}
}
