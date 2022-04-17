package main;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MenuLevel extends DrawableRect{
	static BufferedImage playedImg, selectedImg, defaultImg;
	protected boolean played = false;
	protected int num;
	public MenuLevel(int x, int y, int number, boolean played) {
		super(x,y,130,100);
		num = number;
		this.played = played;
		if(playedImg!=null)return;//only add images once
		try {
			playedImg = ImageIO.read(ClassLoader.getSystemResource("playedLevel.png"));
			selectedImg = ImageIO.read(ClassLoader.getSystemResource("selectedLevel.png"));
			defaultImg = ImageIO.read(ClassLoader.getSystemResource("defaultLevel.png"));
		} catch (Exception e) {
			System.out.println("Error loading menu image");
			e.printStackTrace();
		}
	}
	public void draw(Graphics2D g2d) {
		int ty = y;
		if(hover)ty -=5;
		if(selected)g2d.drawImage(selectedImg, null, x, ty);
		else if(played)g2d.drawImage(playedImg, null, x, ty);
		else g2d.drawImage(defaultImg, null, x, ty);
		g2d.setColor(Pallete.blackish);
		g2d.setFont(new Font("Sans", Font.BOLD, 50));
		g2d.drawString(String.valueOf(num), x+30, ty+70);
	}
	 
}
