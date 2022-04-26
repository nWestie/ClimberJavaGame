package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MenuText extends DrawableRect {
	protected String string;

	public MenuText(String string, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.string = string;
	}

	@Override
	void draw(Graphics2D g2d) {
		g2d.setFont(Pallete.menuFont);
		g2d.drawString(string, x, y + height + (hover ? 0 : 5));
	}

}
