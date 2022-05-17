package main;

import java.awt.Font;
import java.awt.Graphics2D;

public class MenuText extends DrawableRect {
	protected String string;
	private Font font;

	public MenuText(String string, int x, int y, int w, int h, Font font) {
		super(x, y, w, h);
		this.string = string;
		this.font = font;
	}
	public MenuText(String string, int x, int y, int w, int h) {
		this(string, x, y, w, h, Pallete.menuFont);
	}
	@Override
	void draw(Graphics2D g2d) {
		g2d.setFont(font);
		g2d.setColor(Pallete.blackish);
		g2d.drawString(string, x, y + height + (hover ? 0 : 5));
	}

}
