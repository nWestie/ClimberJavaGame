package main;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

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
			playedImg = ImageIO.read(new File(ClimberMain.dir, "/Imgs/meunObjs/PlayedLevel.png"));
			selectedImg = ImageIO.read(new File(ClimberMain.dir, "/Imgs/meunObjs/SelLevel.png"));
			defaultImg = ImageIO.read(new File(ClimberMain.dir, "/Imgs/meunObjs/StdLevel.png"));
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
